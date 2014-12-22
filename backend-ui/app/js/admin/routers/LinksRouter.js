define(function (require) {
    var Backbone = require('backbone');
    var $ = require('jquery');
    var _ = require('underscore');

    var LinksView = require('js/admin/views/links/LinksView');
    var Links = require('js/admin/collections/Links');

    return Backbone.Router.extend({
        routes: {
            'links': 'list'
        },
        initialize: function (opts) {
            this.adminView = opts.adminView;
            this.tabPanelView = this.adminView.createTabPanelView('links');
        },
        list: function () {
            var links = new Links();
            this.adminView.renderTab(this.tabPanelView, new LinksView({
                collection: links
            }).render());
            links.fetch();
        }
    });
});