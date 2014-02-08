define(function (require) {
    var Backbone = require('backbone');
    var $ = require('jquery');
    var _ = require('underscore');

    var LinksView = require('app/js/admin/views/LinksView');

    return Backbone.Router.extend({
        routes: {
            'links': 'list'
        },
        initialize: function (opts) {
            this.adminView = opts.adminView;
            this.tabPanelView = this.adminView.createTabPanelView('links');
        },
        list: function () {
            this.adminView.renderTab(this.tabPanelView, new LinksView().render());
        }
    });
});