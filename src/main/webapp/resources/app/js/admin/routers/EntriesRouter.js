define(function (require) {
    var Backbone = require('backbone');
    var $ = require('jquery');
    var _ = require('underscore');

    var Entry = require('../models/Entry');
    var EntriesView = require('../views/EntriesView');

    return Backbone.Router.extend({
        routes: {
            'entries': 'list',
            'entries?form': 'createForm'
        },
        initialize: function (opts) {
            this.adminView = opts.adminView;
            this.tabPanelView = this.adminView.createTabPanelView('entries');
        },
        list: function () {
            this.entriesView = new EntriesView();
            this.adminView.renderTab(this.tabPanelView, this.entriesView.render());
        },
        createForm: function () {
            var entry = new Entry();
            this.entriesView = new EntriesView({
                model: entry
            });
            this.adminView.renderTab(this.tabPanelView, this.entriesView.createForm());
        },
        confirm: function() {
            if (!this.entriesView) {
                return this.createForm();
            }
        }
    });
});