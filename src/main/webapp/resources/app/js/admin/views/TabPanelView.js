define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');
    require('bootstrap');

    var AlertPanelView = require('app/js/admin/views/AlertPanelView');

    var tabPanel = require('text!app/js/admin/templates/tabPanel.hbs');

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
            this.alertPanel = new AlertPanelView({el: this.$('#alert-panel')});
            this.$tab.tab('show');

            this.listenTo(Backbone, 'exception', _.bind(this.showExceptionMessage, this)); // handle global event!
            return this;
        },
        changeBodyView: function (bodyView) {
            if (this.bodyView) {
                this.bodyView.remove();
            }
            this.bodyView = bodyView;
            return this;
        },
        showExceptionMessage: function (error) {
            var message = error.message;
            this.alertPanel.showMessage('[' + error.code + '] ' + message);
        }
    });
});