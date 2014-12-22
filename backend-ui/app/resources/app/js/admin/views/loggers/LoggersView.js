define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var Loggers = require('app/js/admin/collections/Loggers');
    var Logger = require('app/js/admin/models/Logger');
    var LoggerRowView = require('app/js/admin/views/loggers/LoggerRowView');
    var ErrorHandler = require('app/js/admin/views/ErrorHandler');

    var loggerTable = require('text!app/js/admin/templates/loggers/loggerTable.hbs');

    return Backbone.View.extend(_.extend({
        template: Handlebars.compile(loggerTable),
        bindings: {
            '#filter': 'filter'
        },

        initialize: function () {
            this.model = new Backbone.Model();
            this.lazyRenderTable = _.debounce(_.bind(this.renderTable, this), 300);
            this.listenTo(this.collection, 'sync', this.renderTable);
            this.listenTo(this.model, 'change:filter', this.lazyRenderTable);
        },
        render: function () {
            this.$el.html(this.template());
            this.stickit();
            return this;
        },
        renderTable: function () {
            var $tbody = this.$('tbody').empty();
            var filter = this.model.get('filter');
            var toRender;
            if (filter) {
                toRender = new Loggers(this.collection.filter(function (model) {
                    return _.contains(model.get('name'), filter);
                }));
            } else {
                toRender = this.collection;
            }
            toRender.each(_.bind(function (logger) {
                var loggerView = new LoggerRowView({
                    model: logger
                });
                $tbody.append(loggerView.render().el);
                this.listenTo(loggerView, 'refresh', this.refresh);
            }, this));
            return this;
        },
        refresh: function () {
            this.collection.fetch();
        }
    }, ErrorHandler));
});