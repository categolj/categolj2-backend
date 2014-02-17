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
        jquery: 'vendor/jquery/jquery',
        underscore: 'vendor/lodash/dist/lodash',
        backbone: 'vendor/backbone/backbone',
        'backbone.stickit': 'vendor/backbone.stickit/backbone.stickit',
        'backbone.validation': 'vendor/backbone.validation/src/backbone-validation',
        handlebars: 'vendor/handlebars/handlebars',
        marked: 'vendor/marked/lib/marked',
        spin: 'vendor/spin.js/dist/spin',
        pagedown: 'vendor/pagedown/Markdown.Converter',
        'pagedown.editor': 'vendor/pagedown/Markdown.Editor',
        'jquery.iframe-transport': 'vendor/jquery.iframe-transport/jquery.iframe-transport',
        bootstrap: 'vendor/bootstrap/dist/js/bootstrap',
        text: 'vendor/requirejs-text/text'
    }
});

define(function (require) {
    var $ = require('jquery');
    var _ = require('underscore');
    var Backbone = require('backbone');
    Backbone.Validation = require('backbone.validation');

    var AdminRouter = require('app/js/admin/routers/AdminRouter');
    var SpinView = require('app/js/admin/views/SpinView');

    new AdminRouter();

    $(document).ready(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function (e, xhr) {
            xhr.setRequestHeader(header, token);
            xhr.setRequestHeader('X-Admin', true);
            // prevent cache
            xhr.setRequestHeader('Pragma', 'no-cache');
            xhr.setRequestHeader('Cache-Control', 'no-cache');
            xhr.setRequestHeader('If-Modified-Since', 'Thu, 01 Jun 1970 00:00:00 GMT');
        });

        var spinView = new SpinView();
        $('body').append(spinView.render().$el);
        $(document)
            .on('ajaxStart',function () {
                spinView.spin();
            }).on('ajaxComplete',function () {
                spinView.stop();
            }).on('ajaxError', function (event, xhr) {
                console.log(arguments);
                var resp = xhr.responseJSON;
                if (xhr.status == 403) {
                    if (_.isArray(resp.details)) {
                        Backbone.trigger('exception', resp.details[0]);
                    }
                }
            });

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