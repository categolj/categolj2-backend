var Constant = require('./constants.js');
var Util = require('./util.js');
var Backbone = require('backbone');
var _ = require('underscore');

// Models
var Entry = Backbone.Model.extend({
    idAttribute: 'entryId',
    url: function () {
        return Constant.API_ROOT + '/entries/' + encodeURIComponent(this.id) + '';
    }
});

var RecentPost = Backbone.Model.extend({});

var Category = Backbone.Model.extend({});

var Link = Backbone.Model.extend({});

// Pageable object
var Pageable = {
    parse: function (response) {
        this.firstPage = response.first;
        this.lastPage = response.last;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        return response.content;
    }
};

// Collections
var Entries = Backbone.Collection.extend(
    _.extend(Pageable, { // mixin
        targetUrl: Constant.API_ROOT + '/entries',
        model: Entry,
        initialize: function (options) {
            options = _.extend({}, options);
            this.page = Number(options.page) || 0;
            this.pageSize = Number(options.pageSize) || 3;
        },

        url: function () {
            return Util.appendPageAndSize(this.targetUrl, this.page, this.pageSize);
        }
    }));

var RecentPosts = Backbone.Collection.extend({
    url: Constant.API_ROOT + '/recentposts',
    model: RecentPost
});

var Categories = Backbone.Collection.extend({
    url: Constant.API_ROOT + '/categories',
    model: Category
});

var Links = Backbone.Collection.extend({
    url: Constant.API_ROOT + '/links',
    model: Link
});

module.exports = {
    Entry: Entry,
    RecentPost: RecentPost,
    Category: Category,
    Link: Link,
    Entries: Entries,
    RecentPosts: RecentPosts,
    Categories: Categories,
    Links: Links
};