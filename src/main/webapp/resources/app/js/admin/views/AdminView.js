define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var NavItemList = require('../collections/NavItemList');
    var NavItemListView = require('../views/NavItemListView');
    var TabPanelView = require('../views/TabPanelView');
    var TabContentView = require('../views/TabContentView');

    return Backbone.View.extend({
        initialize: function () {
            this.navItemList = new NavItemList([
                {id: 'dashboard', itemName: 'ダッシュボード', itemIcon: 'home'},
                {id: 'entries', itemName: '記事管理', itemIcon: 'pencil'},
                {id: 'categories', itemName: 'カテゴリー管理', itemIcon: 'bookmark'},
                {id: 'uploads', itemName: 'アップロードファイル管理', itemIcon: 'file'},
                {id: 'users', itemName: 'ユーザー管理', itemIcon: 'user'},
                {id: 'reports', itemName: 'レポート', itemIcon: 'stats'},
                {id: 'system', itemName: 'システム情報', itemIcon: 'info-sign'},
                {id: 'h2Console', itemName: 'H2管理コンソール(ログ確認)', itemIcon: 'asterisk'}
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
                    .changeBodyView(bodyView)
                    .render())
                .render();
            return this;
        },
        switchTabPanelView: function (tabPanelView) {
            if (this.tabContentView) {
                this.tabContentView.panelView = tabPanelView;
            } else {
                this.tabContentView = new TabContentView({
                    el: $('.tab-content'),
                    panelView: tabPanelView
                });
            }
            return this.tabContentView;
        }
    });
});