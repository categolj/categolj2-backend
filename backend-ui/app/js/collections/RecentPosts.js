define(function (require) {
    var Backbone = require('backbone');
    var RecentPost = require('js/models/RecentPost');
    var Constants = require('js/Constants');

    return Backbone.Collection.extend({
        model: RecentPost,
        url: function () {
            return Constants.API_ROOT + '/recentposts';
        }
    });
});