define(function (require) {
    var Backbone = require('backbone');
    var $ = require('jquery');
    var _ = require('underscore');


    var Entry = require('app/js/admin/models/Entry');
    var Entries = require('app/js/admin/collections/Entries');
    var EntryListView = require('app/js/admin/views/entries/EntryListView');
    var EntryFormView = require('app/js/admin/views/entries/EntryFormView');

    return Backbone.Router.extend({
        routes: {
            'entries': 'list',
            'entries/form': 'createForm',
            'entries/:id/form': 'updateForm',
            'entries/:id': 'show'
        },
        initialize: function (opts) {
            this.adminView = opts.adminView;
            this.tabPanelView = this.adminView.createTabPanelView('entries');
        },
        list: function () {
            var entries = new Entries();
            this.entryListView = new EntryListView({
                collection: entries
            });
            this.adminView.renderTab(this.tabPanelView, this.entryListView.render());
            entries.fetch();
        },
        createForm: function () {
            var entry = new Entry();
            this.entryFormView = new EntryFormView({
                model: entry
            });
            this.adminView.renderTab(this.tabPanelView, this.entryFormView.render());
            this.entryFormView.setupPagedownEditor();
        },
        show: function (id) {
            var entry = new Entry({id: id});
            entry.fetch().success(_.bind(function () {
                this.entryFormView = new EntryFormView({
                    model: entry
                });
                this.adminView.renderTab(this.tabPanelView, this.entryFormView.show());
            }, this));
        },
        updateForm: function (id) {
            var entry = new Entry({id: id});
            entry.fetch().success(_.bind(function () {
                this.entryFormView = new EntryFormView({
                    model: entry
                });
                this.adminView.renderTab(this.tabPanelView, this.entryFormView.render());
                this.entryFormView.setupPagedownEditor();
            }, this));
        }
    });
});