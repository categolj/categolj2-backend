define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');


    var entryList = require('text!../../templates/entries/entryList.hbs');

    return Backbone.View.extend({
        events: {
            'click #btn-show-entry-search-form': 'toggleEntrySearchForm',
            'click #btn-close-entry-search-form': 'toggleEntrySearchForm'
        },

        entryListTemplate: Handlebars.compile(entryList),

        initialize: function () {
            this.$el.html(this.entryListTemplate({
                content: this.collection.toJSON()
            }));
        },
        render: function () {
            console.log(this.collection.toJSON());
            return this;
        },
        toggleEntrySearchForm: function () {
            if (!this.isShownSearchForm) {
                this.isShownSearchForm = true;
                this.$el.find('#entrySearch').css('display', 'block');
            } else {
                this.isShownSearchForm = false;
                this.$el.find('#entrySearch').css('display', 'none');
            }
        }
    });
});