define(function (require) {
    var Backbone = require('backbone');
    var AccessLog = require('js/models/AccessLog');
    var Page = require('js/collections/Page');
    var Constants = require('js/Constants');

    return Backbone.Collection.extend(_.extend({
        model: AccessLog,
        url: function () {
            return Constants.API_ROOT + '/accesslogs';
        },
        comparator: function (a, b) {
            return a.get('accessDate') > b.get('accessDate') ? -1 : 1;
        },
        deleteByRemoteAddress: function (remoteAddress) {
            var opts = {
                url: this.url() + '?remoteAddress=' + remoteAddress,
                validate: false
            };
            return Backbone.sync('delete', new Backbone.Model(), opts)
                .success(_.bind(function () {
                    this.remove(this.where({
                        remoteAddress: remoteAddress
                    }));
                    this.trigger('sync');
                }, this));
        }
    }, Page));
});