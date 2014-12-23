define(function (require) {
    var Backbone = require('backbone');
    var $ = require('jquery');
    var _ = require('underscore');

    var H2ConsoleView = require('js/views/H2ConsoleView');

    return Backbone.Router.extend({
        routes: {
            'h2Console': 'list'
        },
        initialize: function (opts) {
            this.adminView = opts.adminView;
            this.tabPanelView = this.adminView.createTabPanelView('h2Console');
        },
        list: function () {
            this.adminView.renderTab(this.tabPanelView, new H2ConsoleView().render());
        }
    });
});