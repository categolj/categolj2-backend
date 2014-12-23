define(function (require) {
    var Backbone = require('backbone');
    var $ = require('jquery');
    var _ = require('underscore');

    var UploadsView = require('js/views/uploads/UploadsView');
    var Files = require('js/collections/Files');

    return Backbone.Router.extend({
        routes: {
            'uploads': 'list',
            'uploads/page=:page/size=:pageSize': 'list',
        },
        initialize: function (opts) {
            this.adminView = opts.adminView;
            this.tabPanelView = this.adminView.createTabPanelView('uploads');
        },
        list: function (page, pageSize) {
            var files = new Files({
                page: page,
                pageSize: pageSize
            });
            this.adminView.renderTab(this.tabPanelView, new UploadsView({
                collection: files
            }).render());
            files.fetch({
                data: files.pagingData()
            });
        }
    });
});