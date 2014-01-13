define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');
    require('handsontable');


    var Users = require('app/js/admin/collections/Users');
    var User = require('app/js/admin/models/User');

    // normally, you'd get these from the server with .fetch()
    function attr(attr) {
        // this lets us remember `attr` for when when it is get/set
        return {data: function (car, value) {
            if (_.isUndefined(value)) {
                return car.get(attr);
            }
            car.set(attr, value);
        }};
    }

    return Backbone.View.extend({
        events: {
            'click #btn-user-save': '_save'
        },

        initialize: function () {
            this.listenTo(this.collection, 'sync', this.renderTable);
            this.listenTo(this.collection, 'all', function () {
                console.log(arguments);
            });
        },
        render: function () {
            return this;
        },
        renderTable: function () {
            this.$el.handsontable({
                data: this.collection,
                dataSchema: function () {
                    return new User();
                },
                contextMenu: true,
                columns: [
                    attr('username'),
                    attr('email'),
                    attr('firstName'),
                    attr('lastName'),
                    attr('password')
                ],
                colHeaders: [
                    'Username',
                    'E-mail',
                    'First Name',
                    'Last Name',
                    'Password'
                ]
            });
            this.$el.append('<button id="btn-user-save" class="btn btn-primary">Save</button>');
            this.listenTo(this.collection, 'add', this.$el.handsontable('render'));
            this.listenTo(this.collection, 'remove', this.$el.handsontable('render'));
            return this;
        },

        _save: function () {
            this.collection.each(function (user) {
                user.save();
            });
        }
    });
});