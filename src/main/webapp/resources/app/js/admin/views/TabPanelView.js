define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');
    require('bootstrap');

    var tabPanel = require('text!../templates/tabPanel.hbs');

    return Backbone.View.extend({
        template: Handlebars.compile(tabPanel),

        initialize: function (opts) {
            this.$tab = $('#nav-item-list a[href=#' + this.model.get('id') + ']');
        },
        render: function () {
            this.$el.empty();
            this.$el.html(this.template({
                tab: this.model.get('id'),
                title: this.model.get('itemName')
            }));
            this.$('.panel-body').empty().html(this.bodyView.$el);
            this.$tab.tab('show');

            return this;
        },
        changeBodyView: function(bodyView) {
            if (this.bodyView) {
                this.bodyView.remove();
            }
            this.bodyView = bodyView;
            return this;
        }
    });
});