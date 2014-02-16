define(function (require) {
    var $ = require('jquery');
    var Backbone = require('backbone');
    var File = require('app/js/admin/models/File');
    var Page = require('app/js/admin/collections/Page');

    return Backbone.Collection.extend(_.extend({
        model: File,
        url: 'api/v1/files',
        comparator: function (a, b) {
            var isSameLastModifiedDate = a.get('lastModifiedDate') == b.get('lastModifiedDate');
            if (isSameLastModifiedDate) {
                return a.get('fileName') > b.get('fileName') ? 1 : -1;
            } else {
                return a.get('lastModifiedDate') > b.get('lastModifiedDate') ? -1 : 1;
            }
        },
        upload: function (files, options) {
            var opts = _.extend({
                url: this.url + '?' + $.param(options.data),
                validate: false,
                files: files,
                iframe: true
            }, options);

            var success = options.success;
            var collection = this;
            opts.success = function (files, resp, xhr) {
                // jquery-iframe-transport cannot detect http status
                // http://cmlenz.github.io/jquery-iframe-transport/#section-13
                if (xhr.responseJSON.details) {
                    // error
                    options.error(files, xhr);
                    return;
                }
                collection.add(files);
                collection.trigger('sync');
                if (success) {
                    success(files, resp, xhr);
                }
            }
            Backbone.sync('create', new Backbone.Model(), opts);
        }
    }, Page));
});