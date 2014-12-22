define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var FilesForm = require('js/admin/models/FilesForm');
    var fileupload = require('text!js/admin/templates/entries/fileupload.hbs');
    var FileRowView = require('js/admin/views/entries/FileRowView');

    var FileUploadView = Backbone.View.extend({
        template: Handlebars.compile(fileupload),
        events: {
            'click #btn-file-upload': '_upload'
        },
        bindings: {
            '#files': 'files'
        },
        initialize: function () {
            this.$el.html(this.template());
            this.listenTo(this.collection, 'sync', this.renderTable);
        },
        render: function () {
            this._resetModel();
            return this;
        },
        renderTable: function () {
            this.collection.sort();
            var $tbody = this.$('tbody').empty();
            this.collection.each(_.bind(function (file) {
                var fileRowView = new FileRowView({
                    model: file
                });
                $tbody.append(fileRowView.render().el);
                this.listenTo(fileRowView, 'fileSelected', this._onFileSelected);
            }, this));
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
        },
        _onFileSelected: function (file) {
            this.trigger('fileSelected', file);
        }
    });
    return FileUploadView;
});