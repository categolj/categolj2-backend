define(function (require) {
    var Backbone = require('backbone');
    var RecentPost = require('js/admin/models/RecentPost');
    var Constants = require('js/admin/Constants');

    return Backbone.Collection.extend({
        model: RecentPost,
        url: function () {
            return Constants.API_ROOT + '/recentposts';
        }
    });
});