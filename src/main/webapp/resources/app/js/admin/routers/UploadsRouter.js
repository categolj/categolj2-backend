define(function (require) {
    var Backbone = require('backbone');
    var $ = require('jquery');
    var _ = require('underscore');

    var UploadsView = require('app/js/admin/views/UploadsView');

    return Backbone.Router.extend({
        routes: {
            'uploads': 'list'
        },
        initialize: function (opts) {
            this.adminView = opts.adminView;
            this.tabPanelView = this.adminView.createTabPanelView('uploads');
        },
        list: function () {
            this.adminView.renderTab(this.tabPanelView, new UploadsView().render());
        }
    });
});