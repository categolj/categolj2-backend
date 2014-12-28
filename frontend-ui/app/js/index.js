var model = require('./model.js');
var view = require('./view.js');
var Constant = require('./constants.js');
var Backbone = require('backbone');
var _ = require('underscore');
var $ = require('jquery');
var Handlebars = require('handlebars');

Backbone.$ = $; // http://backbonejs.org/#Utility-Backbone-$

Handlebars.registerHelper('categoryLink', function (category) {
    var ret = [], categoriesBuf = [];
    _.each(category, function (c) {
        categoriesBuf.push(_.escape(c));
        ret.push('<a href="#/categories/' + categoriesBuf.join(Constant.SEPARATOR) + '/entries">'
        + _.escape(c) + '</a>');
    });
    return new Handlebars.SafeString(ret.join(Constant.SEPARATOR));
});
Handlebars.registerHelper('breadcrumb', function (category) {
    var ret = [], categoriesBuf = [];
    _.each(category, function (c) {
        categoriesBuf.push(_.escape(c));
        ret.push('<li><a href="#/categories/' + categoriesBuf.join(Constant.SEPARATOR) + '/entries">'
        + _.escape(c) + '</a></li>');
    });
    return new Handlebars.SafeString(ret.join(''));
});
Handlebars.registerHelper('unescape', function (string) {
    return new Handlebars.SafeString(string);
});
Handlebars.registerHelper('toString', function (obj) {
    return JSON.stringify(obj);
});

var Router = Backbone.Router.extend({
    routes: {
        '': 'showEntries',
        '?page=:page&size=:pageSize': 'showEntries',
        'entries': 'showEntries',
        'entries?page=:page&size=:pageSize': 'showEntries',
        'entries/:id/title/:title': 'showEntry', // this is for migration from categolj-java
        'entries/:id': 'showEntry',
        'entries?q=:keyword&page=:page&size=:pageSize': 'showSearchResult',
        'entries?q=:keyword': 'showSearchResult',
        'categories': 'showCategories',
        'categories/:categories/entries?page=:page&size=:pageSize': 'showEntriesByCategory',
        'categories/:categories/entries': 'showEntriesByCategory',
        'users/:id/entries?page=:page&size=:pageSize': 'showEntriesByUser',
        'users/:id/entries': 'showEntriesByUser'
    },
    initialize: function () {
        new view.LoadingView();
        this.appView = new view.AppView({
            el: $('#main')
        });
    },
    // delegate to appView
    showEntries: function (page, pageSize) {
        var r = /page=([0-9]+)&size=([0-9]+)/.exec(page);
        if (r) {
            page = r[1];
            pageSize = r[2];
        }
        this.appView.showEntries(page, pageSize);
    },
    showEntry: function (id) {
        this.appView.showEntry(id);
    },
    showSearchResult: function (keyword, page, pageSize) {
        this.appView.showSearchResult(keyword, page, pageSize);
    },
    showCategories: function () {
        this.appView.showCategories();
    },
    showEntriesByCategory: function (category, page, pageSize) {
        this.appView.showEntriesByCategory(category, page, pageSize);
    },
    showEntriesByUser: function (userId, page, pageSize) {
        this.appView.showEntriesByUser(userId, page, pageSize);
    }
});

var router = new Router();
$(function () {
    $(document).ajaxSend(function (e, xhr) {
        xhr.setRequestHeader('X-Formatted', true);
    });
    Backbone.history.start();
});