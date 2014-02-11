define(function (require) {
    var Backbone = require('backbone');
    var $ = require('jquery');
    var _ = require('underscore');

    var UploadsView = require('app/js/admin/views/uploads/UploadsView');
    var Files = require('app/js/admin/collections/Files');

    return Backbone.Router.extend({
        routes: {
            'uploads': 'list'
        },
        initialize: function (opts) {
            this.adminView = opts.adminView;
            this.tabPanelView = this.adminView.createTabPanelView('uploads');
        },
        list: function () {
            var files = new Files();
            this.adminView.renderTab(this.tabPanelView, new UploadsView({
                collection: files
            }).render());
            files.fetch();
        }
    });
});