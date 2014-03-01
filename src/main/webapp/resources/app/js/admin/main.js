/*global require*/
'use strict';

// Require.js allows us to configure shortcut alias
require.config({
    baseUrl: 'resources',
    // The shim config allows us to configure dependencies for
    // scripts that do not call define() to register a module
    shim: {
        underscore: {
            exports: '_'
        },
        backbone: {
            deps: [
                'underscore',
                'jquery',
                'jquery.iframe-transport'
            ],
            exports: 'Backbone'
        },
        'backbone.validation': {
            deps: [
                'backbone'
            ],
            exports: 'Backbone.Validation'
        },
        handlebars: {
            exports: 'Handlebars'
        },
        pagedown: {
            exports: 'Markdown'
        },
        'pagedown.editor': {
            deps: [
                'pagedown'
            ]
        },
        'jquery.iframe-transport': {
            deps: [
                'jquery'
            ]
        },
        'backbone.stickit': ['backbone'],
        bootstrap: ['jquery']
    },
    paths: {
        jquery: 'vendor/jquery/jquery.min',
        underscore: 'vendor/lodash/dist/lodash.min',
        backbone: 'vendor/backbone/backbone-min',
        'backbone.stickit': 'vendor/backbone.stickit/backbone.stickit',
        'backbone.validation': 'vendor/backbone.validation/dist/backbone-validation-min',
        handlebars: 'vendor/handlebars/handlebars.min',
        marked: 'vendor/marked/lib/marked',
        spin: 'vendor/spin.js/dist/spin.min',
        pagedown: 'vendor/pagedown/Markdown.Converter',
        'pagedown.editor': 'vendor/pagedown/Markdown.Editor',
        'jquery.iframe-transport': 'vendor/jquery.iframe-transport/jquery.iframe-transport',
        bootstrap: 'vendor/bootstrap/dist/js/bootstrap.min',
        text: 'vendor/requirejs-text/text'
    }
});

define(function (require) {
    var $ = require('jquery');
    var _ = require('underscore');

    var cookie = _.chain(document.cookie.split(';'))
        .map(function (x) {
            return $.trim(x).split('=')
        })
        .object()
        .value();

    if (_.isEmpty(cookie.CATEGOLJ2_ACCESS_TOKEN_VALUE)) {
        location.href = "login.jsp";
        return;
    }

    var accessToken = decodeURIComponent(cookie.CATEGOLJ2_ACCESS_TOKEN_VALUE);
    var expiration = new Date(Number(cookie.CATEGOLJ2_ACCESS_TOKEN_EXPIRATION));
    if (cookie.CATEGOLJ2_REFRESH_TOKEN_VALUE) {
        var refreshToken = decodeURIComponent(cookie.CATEGOLJ2_REFRESH_TOKEN_VALUE);
    }

    var Backbone = require('backbone');
    Backbone.Validation = require('backbone.validation');

    var AdminRouter = require('app/js/admin/routers/AdminRouter');
    var SpinView = require('app/js/admin/views/SpinView');
    var spinView = new SpinView();

    new AdminRouter();

    $(document).ajaxSend(function (e, xhr) {
        xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
        xhr.setRequestHeader('X-Admin', true);
        // prevent cache
        xhr.setRequestHeader('Pragma', 'no-cache');
        xhr.setRequestHeader('Cache-Control', 'no-cache');
        xhr.setRequestHeader('If-Modified-Since', 'Thu, 01 Jun 1970 00:00:00 GMT');
    });

    $(document)
        .on('ajaxStart',function () {
            spinView.spin();
        }).on('ajaxComplete',function () {
            spinView.stop();
        }).on('ajaxError', function (event, xhr) {
            console.log(arguments);
            var resp = xhr.responseJSON;
            if (xhr.status == 401 && resp.error == "invalid_token") {
                // TODO refresh token
                alert(resp.error_description);
                location.href = "logout";
            }
            if (xhr.status == 403) {
                if (_.isArray(resp.details)) {
                    Backbone.trigger('exception', resp.details[0]);
                }
            }
        });

    $(document).ready(function () {
        $('body').append(spinView.render().$el);
        var user = JSON.parse(decodeURIComponent(cookie.CATEGOLJ2_USER));
        $('#user-display-name').text(user.firstName + ' ' + user.lastName);

        // Global validation configuration
        _.extend(Backbone.Validation.callbacks, {
            valid: function (view, attr) {
                var $el = view.$('[name=' + attr + ']'),
                    $group = $el.closest('.form-group');
                $group.removeClass('has-error');
                $group.find('.help-block').text('').addClass('hidden');
            },
            invalid: function (view, attr, error) {
                var $el = view.$('[name=' + attr + ']'),
                    $group = $el.closest('.form-group');
                $group.addClass('has-error');
                $group.find('.help-block').text(error).removeClass('hidden');
            }
        });
        Backbone.history.start();
    });
});