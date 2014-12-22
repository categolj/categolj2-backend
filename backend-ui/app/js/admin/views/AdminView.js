define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var NavItemList = require('js/admin/collections/NavItemList');
    var NavItemListView = require('js/admin/views/NavItemListView');
    var TabPanelView = require('js/admin/views/TabPanelView');
    var TabContentView = require('js/admin/views/TabContentView');

    return Backbone.View.extend({
        initialize: function () {
            this.navItemList = new NavItemList([
                {id: 'dashboard', itemName: 'Dashboard', itemIcon: 'home'},
                {id: 'entries', itemName: 'Entry', itemIcon: 'pencil'},
                {id: 'uploads', itemName: 'Upload', itemIcon: 'file'},
                {id: 'users', itemName: 'User', itemIcon: 'user'},
                {id: 'links', itemName: 'Link', itemIcon: 'bookmark'},
                {id: 'loggers', itemName: 'Logger', itemIcon: 'tasks'},
                {id: 'apis', itemName: 'API', itemIcon: 'asterisk'},
                {id: 'reports', itemName: 'Report', itemIcon: 'stats'},
                {id: 'system', itemName: 'System', itemIcon: 'info-sign'}
            ]);
            var navItemListView = new NavItemListView({
                el: '#nav-item-list',
                collection: this.navItemList
            });

            navItemListView.render();
        },
        render: function () {
            return this;
        },
        createTabPanelView: function (tab) {
            var navItem = this.navItemList.get(tab);
            var tabPanelView = new TabPanelView({
                model: navItem
            });
            return tabPanelView;
        },
        renderTab: function (tabPanelView, bodyView) {
            this.switchTabPanelView(tabPanelView
                    .changeBodyView(bodyView))
                .render();
            return this;
        },
        switchTabPanelView: function (tabPanelView) {
            if (this.tabContentView) {
                this.tabContentView.panelView = tabPanelView;
            } else {
                this.tabContentView = new TabContentView({
                    el: this.$('.tab-content'),
                    panelView: tabPanelView
                });
            }
            return this.tabContentView;
        }
    });
});