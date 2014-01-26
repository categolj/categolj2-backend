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
                'jquery'
            ],
            exports: 'Backbone'
        },
        'backbone.validation': {
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
        }
    },
    paths: {
        jquery: 'vendor/jquery/jquery',
        underscore: 'vendor/lodash/dist/lodash',
        backbone: 'vendor/backbone/backbone',
        'backbone.stickit': 'vendor/backbone.stickit/backbone.stickit',
        'backbone.validation': 'vendor/backbone.validation/src/backbone-validation',
        handlebars: 'vendor/handlebars/handlebars',
        handsontable: 'vendor/handsontable/dist/jquery.handsontable.full',
        marked: 'vendor/marked/lib/marked',
        spin: 'vendor/spin.js/dist/spin',
        pagedown: 'vendor/pagedown/Markdown.Converter',
        'pagedown.editor': 'vendor/pagedown/Markdown.Editor',
        bootstrap: 'vendor/bootstrap/dist/js/bootstrap',
        text: 'vendor/requirejs-text/text'
    }
});

define(function (require) {
    var $ = require('jquery');

    var AdminRouter = require('app/js/admin/routers/AdminRouter');
    var SpinView = require('app/js/admin/views/SpinView');

    new AdminRouter();

    $(document).ready(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function (e, xhr) {
            xhr.setRequestHeader(header, token);
            xhr.setRequestHeader('X-Admin', true);
        });

        var spinView = new SpinView();
        $('body').append(spinView.render().$el);
        $(document).on('ajaxStart',function () {
            spinView.spin();
        }).on('ajaxComplete', function () {
                spinView.stop();
            });
        Backbone.history.start();
    });
});