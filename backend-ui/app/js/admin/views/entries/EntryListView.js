define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var PaginationView = require('js/admin/views/PaginationView');
    var SearchPaginationView = require('js/admin/views/entries/SearchPaginationView');

    var entryList = require('text!js/admin/templates/entries/entryList.hbs');
    var entryTable = require('text!js/admin/templates/entries/entryTable.hbs');

    return Backbone.View.extend({
        events: {
            'click #btn-entry-search': '_search',
            'keypress #keyword': '_searchOnEnter',
            'click #btn-show-entry-search-form': '_toggleEntrySearchForm'
        },

        entryListTemplate: Handlebars.compile(entryList),
        entryTableTemplate: Handlebars.compile(entryTable),

        initialize: function () {
            this.listenTo(this.collection, 'sync', this.renderTableAndPagination);
            this.$el.html(this.entryListTemplate());
            this.$keyword = this.$('#keyword');
        },
        render: function () {
            this.$('hr').last().next().remove();
            if (this.paginationView) {
                this.paginationView.remove();
            }
            this.$el.append(this.entryTableTemplate({
                content: this.collection.toJSON()
            }));
            this.$entrySearch = this.$('#entry-search');
            return this;
        },
        renderTableAndPagination: function () {
            this.render();
            if (this.$keyword.val()) {
                // search
                this.paginationView = new SearchPaginationView({
                    keyword: this.$keyword.val(),
                    collection: this.collection
                });
            } else {
                this.paginationView = new PaginationView({
                    collection: this.collection
                });
            }
            this.$el.append(this.paginationView.render().el);
            return this;
        },
        _search: function () {
            Backbone.history.navigate('entries');
            if (this.$keyword.val()) {
                this.collection.search(this.$keyword.val());
            } else {
                this.collection.fetch();
            }
            return false;
        },
        _searchOnEnter: function (e) {
            if (e.keyCode != 13) return;
            e.preventDefault();
            return this._search();
        },
        _toggleEntrySearchForm: function () {
            this.$entrySearch.toggle(300);
            this.$keyword.focus();
        }
    });
});