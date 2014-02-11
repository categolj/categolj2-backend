define(function (require) {
    var Backbone = require('backbone');
    var File = require('app/js/admin/models/File');
    var Page = require('app/js/admin/collections/Page');

    return Backbone.Collection.extend(_.extend({
        model: File,
        url: 'api/v1/files',
        comparator: function (a, b) {
            return a.get('lastModifiedDate') > b.get('lastModifiedDate') ? -1 : 1;
        },
        upload: function (files, options) {
            var opts = _.extend({
                url: this.url,
                validate: false,
                files: files,
                iframe: true
            }, options);

            var success = options.success;
            var collection = this;
            opts.success = function (files, resp, options) {
                collection.add(files);
                collection.trigger('sync');
                if (success) {
                    success(files, resp, options);
                }
            }
            Backbone.sync('create', new Backbone.Model(), opts);
        }
    }, Page));
});