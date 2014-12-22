define(function (require) {
    var Backbone = require('backbone');
    var _ = require('underscore');
    var Constants = require('app/js/admin/Constants');

    return Backbone.Model.extend({
        idAttribute: 'fileId',
        urlRoot: Constants.API_ROOT + '/files',

        url: function () {
            return this.urlRoot + '/' + this.get('fileId') + '/' + this.get('fileName');
        },
        isImage: function () {
            return _.contains(["png", "jpg", "jpeg", "gif"], this.get('fileExtension'));
        },
        toJSONForView: function () {
            return _.extend({
                isImage: this.isImage(),
                url: this.url()
            }, this.toJSON())
        }
    });
});