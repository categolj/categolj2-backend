define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var Users = require('app/js/admin/collections/Users');
    var User = require('app/js/admin/models/User');
    var UserRowView = require('app/js/admin/views/users/UserRowView');

    var userTable = require('text!app/js/admin/templates/users/userTable.hbs');

    return Backbone.View.extend({
        events: {
            'click #btn-user-create': '_create',
            'click #btn-user-clear': '_resetModel'
        },
        bindings: {
            '#username': 'username',
            '#password': 'password',
            '#email': 'email',
            '#firstName': 'firstName',
            '#lastName': 'lastName'
        },

        template: Handlebars.compile(userTable),

        initialize: function () {
            this.listenTo(this.collection, 'sync add', this.renderTable);
        },
        render: function () {
            this.$el.html(this.template());
            this._resetModel();
            return this;
        },
        renderTable: function () {
            var $tbody = this.$('tbody').empty();
            this.collection.each(function (user) {
                $tbody.append(new UserRowView({
                    model: user
                }).render().el)
            });
            return this;
        },

        _create: function () {
            console.log(this.model);
            if (!this.model.isValid(true)) {
                return false;
            }
            this.collection.add(this.model);
            this._resetModel();
        },

        _resetModel: function () {
            if (this.model) {
                this.unstickit(this.model);
            }
            this.model = new User();
            this.stickit();

            Backbone.Validation.bind(this, {
                valid: function (view, attr) {
                    var $el = view.$('[name=' + attr + ']'),
                        $group = $el.closest('.form-group');
                    $group.removeClass('has-error');
                    $group.find('.help-block').text('').addClass('hidden');
                },
                invalid: function (view, attr, error) {
                    var $el = view.$('[name=' + attr + ']'),
                        $group = $el.closest('.form-group');
                    $group.addClass('has-error');
                    $group.find('.help-block').text(error).removeClass('hidden');
                }
            });
        }
    });
});