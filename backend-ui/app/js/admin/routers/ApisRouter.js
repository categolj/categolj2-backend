define(function (require) {
    var Backbone = require('backbone');
    var $ = require('jquery');
    var _ = require('underscore');

    var ApisView = require('js/admin/views/apis/ApisView');

    return Backbone.Router.extend({
        routes: {
            'apis': 'list'
        },
        initialize: function (opts) {
            this.adminView = opts.adminView;
            this.tabPanelView = this.adminView.createTabPanelView('apis');
        },
        list: function () {
            this.adminView.renderTab(this.tabPanelView, new ApisView().render());
        }
    });
});