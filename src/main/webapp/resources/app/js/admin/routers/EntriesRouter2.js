define(function (require) {
    var Backbone = require('backbone');
    var $ = require('jquery');
    var _ = require('underscore');


    var Entry = require('../models/Entry');
    var Entries = require('../collections/Entries');
    var EntryListView = require('../views/entries/EntryListView');
    var EntryFormView = require('../views/entries/EntryFormView');
    var EntryShowView = require('../views/entries/EntryShowView');

    return Backbone.Router.extend({
        routes: {
            'entries': 'list',
            'entries?form': 'createForm',
            'entries/:id?form': 'updateForm',
            'entries/:id': 'show'
        },
        initialize: function (opts) {
            this.adminView = opts.adminView;
            this.tabPanelView = this.adminView.createTabPanelView('entries');
        },
        list: function () {
            var entries = new Entries();
            entries.fetch().success(_.bind(function () {
                this.entryListView = new EntryListView({
                    collection: entries
                });
                this.adminView.renderTab(this.tabPanelView, this.entryListView.render());
            }, this));
        },
        createForm: function () {
            var entry = new Entry();
            this.entryFormView = new EntryFormView({
                model: entry
            });
            this.adminView.renderTab(this.tabPanelView, this.entryFormView.render());
        },
        show: function (id) {
            var entry = new Entry({entryId: id});
            entry.fetch().success(_.bind(function () {
                this.entryShowView = new EntryShowView({
                    model: entry
                });
                this.adminView.renderTab(this.tabPanelView, this.entryShowView.render());
            }, this));
        },
        updateForm: function (id) {
            var entry = new Entry({entryId: id});
            entry.fetch().success(_.bind(function () {
                this.entryFormView = new EntryFormView({
                    model: entry
                });
                this.adminView.renderTab(this.tabPanelView, this.entryFormView.render());
            }, this));
        }
    });
});