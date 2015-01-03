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
Handlebars.registerHelper('tagsLink', function (tags) {
    var ret = [];
    _.each(tags, function (tag) {
        ret.push('<a href="#/tags/' + tag.tagName + '/entries">'
        + _.escape(tag.tagName) + '</a>');
    });
    return new Handlebars.SafeString(ret.join(' '));
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
        'tags': 'showTags',
        'tags/:tag/entries?page=:page&size=:pageSize': 'showEntriesByCategory',
        'tags/:tag/entries': 'showEntriesByTag',
        'users/:id/entries?page=:page&size=:pageSize': 'showEntriesByTag',
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
        var keyword;
        var qps = /q=(.+)&page=([0-9]+)&size=([0-9]+)/.exec(page);
        var ps = /page=([0-9]+)&size=([0-9]+)/.exec(page);
        var q = /q=(.+)/.exec(page);
        if (qps) {
            keyword = qps[1];
            page = qps[2];
            pageSize = qps[3];
        }
        else if (ps) {
            page = ps[1];
            pageSize = ps[2];
        } else if (q) {
            keyword = q[1];
        }
        // since Backbone.js 1.2(?)
        if (keyword) {
            this.showSearchResult(keyword, page, pageSize)
        } else {
            this.appView.showEntries(page, pageSize);
        }
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
    showTags: function () {
        this.appView.showTags();
    },
    showEntriesByTag: function (tag, page, pageSize) {
        this.appView.showEntriesByTag(tag, page, pageSize);
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