var rest = require('rest');
var mime = require('rest/interceptor/mime');
var pathPrefix = require('rest/interceptor/pathPrefix');
var template = require('rest/interceptor/template');
var defaultRequest = require('rest/interceptor/defaultRequest');
var Config = require('./Config.js');

var client = rest.wrap(mime)
    .wrap(defaultRequest, {headers: {'X-Formatted': 'true'}})
    .wrap(pathPrefix, {prefix: Config.BLOG_URL + '/' + Config.API_ROOT});


var createBaseModel = function (path, others) {
    var baseModel = {
        findAll: function () {
            return client({path: path})
                .then(returnEntity);
        }
    };
    if (others) {
        for (prop in others) {
            baseModel[prop] = others[prop];
        }
    }
    return baseModel;
};

var returnEntity = function (response) {
    return response.entity;
}

var RecentPostsModel = createBaseModel('recentposts');

var LinksModel = createBaseModel('links');

var EntriesModel = {
    findAll: function (page, size) {
        page = page || 0;
        size = size || 3;
        return client({
            path: 'entries', params: {
                page: page,
                size: size
            }
        })
            .then(returnEntity);
    },
    findByCategory: function (category, page, size) {
        page = page || 0;
        size = size || 3;
        return client({
            path: 'categories/{category}/entries',
            params: {
                page: page,
                size: size,
                category: category
            }
        })
            .then(returnEntity);
    },
    findByTagName: function (tagName, page, size) {
        page = page || 0;
        size = size || 3;
        return client({
            path: 'tags/{tagName}/entries',
            params: {
                page: page,
                size: size,
                tagName: tagName
            }
        })
            .then(returnEntity);
    },
    findByUsername: function (username, page, size) {
        page = page || 0;
        size = size || 3;
        return client({
            path: 'users/{username}/entries',
            params: {
                page: page,
                size: size,
                username: username
            }
        })
            .then(returnEntity);
    },
    findByKeyword: function (keyword, page, size) {
        page = page || 0;
        size = size || 3;
        return client({
            path: 'entries',
            params: {
                page: page,
                size: size,
                keyword: keyword
            }
        })
            .then(returnEntity);
    },
    findOne: function (entryId) {
        return client({
            path: 'entries/{entryId}',
            params: {entryId: entryId}
        })
            .then(returnEntity);
    }
};

var TagsModel = createBaseModel('tags');

var CategoriesModel = createBaseModel('categories');

module.exports = {
    RecentPostsModel: RecentPostsModel,
    LinksModel: LinksModel,
    EntriesModel: EntriesModel,
    TagsModel: TagsModel,
    CategoriesModel: CategoriesModel
};