define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');


    var entryList = require('text!../../templates/entries/entryList.hbs');

    return Backbone.View.extend({
        events: {
            'click #btn-entry-search': '_search',
            'click #btn-show-entry-search-form': '_toggleEntrySearchForm'
        },

        entryListTemplate: Handlebars.compile(entryList),

        initialize: function () {
            this.$el.html(this.entryListTemplate({
                content: this.collection.toJSON()
            }));
            this.$entrySearch = this.$('#entrySearch');
        },
        render: function () {
            return this;
        },
        _toggleEntrySearchForm: function () {
            if (!this.isShownSearchForm) {
                this.isShownSearchForm = true;
                this.$entrySearch.css('display', 'block');
            } else {
                this.isShownSearchForm = false;
                this.$entrySearch.css('display', 'none');
            }
        }
    });
});