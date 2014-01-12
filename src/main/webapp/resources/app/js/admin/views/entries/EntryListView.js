define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');


    var entryList = require('text!app/js/admin/templates/entries/entryList.hbs');
    var entryTable = require('text!app/js/admin/templates/entries/entryTable.hbs');

    return Backbone.View.extend({
        events: {
            'click #btn-entry-search': '_search',
            'keypress #keyword': '_searchOnEnter',
            'click #btn-show-entry-search-form': '_toggleEntrySearchForm'
        },

        entryListTemplate: Handlebars.compile(entryList),
        entryTableTemplate: Handlebars.compile(entryTable),

        initialize: function () {
            this.listenTo(this.collection, 'sync', this.render);
            this.$el.html(this.entryListTemplate());
            this.$keyword = this.$('#keyword');
        },
        render: function () {
            this.$('hr').last().next().remove();
            this.$el.append(this.entryTableTemplate({
                content: this.collection.toJSON()
            }));
            this.$entrySearch = this.$('#entry-search');
            return this;
        },
        _search: function () {
            var opts = {};
            if (this.$keyword.val()) {
                opts.data = {
                    keyword: this.$keyword.val()
                }
            }
            this.collection.fetch(opts);
            this.$keyword.val('');
        },
        _searchOnEnter: function (e) {
            if (e.keyCode != 13 || !this.$keyword.val()) {
                return;
            }
            this._search();
        },
        _toggleEntrySearchForm: function () {
            this.$entrySearch.toggle(300);
        }
    });
});