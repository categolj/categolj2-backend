define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var Files = require('js/collections/Files');
    var FilesForm = require('js/models/FilesForm');
    var FileRowView = require('js/views/uploads/FileRowView');
    var PaginationView = require('js/views/PaginationView');
    var ErrorHandler = require('js/views/ErrorHandler');

    var fileTable = require('text!js/templates/uploads/fileTable.hbs');

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

            // TODO refactor
            var cookie = _.chain(document.cookie.split(';'))
                .map(function (x) {
                    return $.trim(x).split('=')
                })
                .object()
                .value();
            if (_.isEmpty(cookie.CATEGOLJ2_ACCESS_TOKEN_VALUE)) {
                location.href = "login.jsp";
                return;
            }
            var accessToken = decodeURIComponent(cookie.CATEGOLJ2_ACCESS_TOKEN_VALUE);

            var data = {'access_token': accessToken};

            this.collection.upload(this.$('#files'), {
                data: data,
                success: _.bind(this._resetModel, this),
                error: _.bind(function (model, response) {
                    this.showErrors(response.responseJSON);
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