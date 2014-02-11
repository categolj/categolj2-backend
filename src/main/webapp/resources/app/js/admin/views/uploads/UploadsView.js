define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var Files = require('app/js/admin/collections/Files');
    var File = require('app/js/admin/models/File');
    var FileRowView = require('app/js/admin/views/uploads/FileRowView');
    var PaginationView = require('app/js/admin/views/PaginationView');
    var ErrorHandler = require('app/js/admin/views/ErrorHandler');

    var fileTable = require('text!app/js/admin/templates/uploads/fileTable.hbs');

    return Backbone.View.extend(_.extend({
        events: {
            'click #btn-file-upload': '_upload',
            'click #btn-file-clear': '_resetModel'
        },
        bindings: {
            '#file': 'file'
        },

        template: Handlebars.compile(fileTable),

        initialize: function () {
            this.listenTo(this.collection, 'sync', this.renderTable);
        },
        render: function () {
            this.$el.html(this.template());
            this._resetModel();
            return this;
        },
        renderTable: function () {
            this.collection.sort();
            var $tbody = this.$('tbody').empty();
            this.collection.each(function (file) {
                $tbody.append(new FileRowView({
                    model: file
                }).render().el)
            });
            if (this.paginationView) {
                this.paginationView.remove();
            }
            this.paginationView = new PaginationView({
                collection: this.collection
            });
            this.$el.append(this.paginationView.render().el);
            return this;
        },

        _upload: function (e) {
            e.preventDefault();
            console.log(this.model);
            if (!this.model.isValid(true)) {
                return false;
            }

            var token = $('meta[name=_csrf]').attr('content');
            var param = $('meta[name=_csrf_parameter]').attr('content');
            var values = {};
            values[param] = token;

            this.collection.create(this.model, {
                validate: false,
                success: _.bind(this._resetModel, this),
                files: this.$('#file'),
                iframe: true,
                data: values
            });
        },

        _resetModel: function () {
            if (this.model) {
                this.unstickit(this.model);
                Backbone.Validation.unbind(this);
            }
            this.model = new File();
            Backbone.Validation.bind(this);
            this.stickit();
        }
    }, ErrorHandler));
});