define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var Files = require('app/js/admin/collections/Files');
    var FilesForm = require('app/js/admin/models/FilesForm');
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
            '#files': 'files'
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
            if (!this.model.isValid(true)) {
                return false;
            }

            var token = $('meta[name=_csrf]').attr('content');
            var param = $('meta[name=_csrf_parameter]').attr('content');
            var data = {};
            data[param] = token;

            this.collection.upload(this.$('#files'), {
                data: data,
                success: _.bind(this._resetModel, this),
                error: _.bind(function (model, response) {
                    if (response.responseJSON.details) {
                        this.showErrors(response.responseJSON.details);
                    }
                }, this)
            });
        },
        _resetModel: function () {
            if (this.model) {
                this.unstickit(this.model);
                Backbone.Validation.unbind(this);
            }
            this.model = new FilesForm();
            Backbone.Validation.bind(this);
            this.stickit();
        }
    }, ErrorHandler));
});