define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var amazonSearchForm = require('text!app/js/admin/templates/entries/amazonSearchForm.hbs');
    var AmazonSearchView  = Backbone.View.extend({
        template: Handlebars.compile(amazonSearchForm),
        initialize: function() {
            this.$el.html(this.template());
        },
        render: function() {
            return this;
        }
    });
    return AmazonSearchView;
});