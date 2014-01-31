define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var _ = require('underscore');


    var AutoCompleteItemView = Backbone.View.extend({
        tagName: 'li',
        template: Handlebars.compile('<a href="#">{{label}}</a>'),

        events: {
            'click': 'select'
        },

        initialize: function (opts) {
            this.parent = opts.parent;
        },
        render: function () {
            this.$el.html(this.template({
                label: this.model.label()
            }));
            return this;
        },

        select: function () {
            this.parent.hide().select(this.model);
            return false;
        }
    });

    var AutoCompleteView = Backbone.View.extend({
        tagName: 'ul',
        itemView: AutoCompleteItemView,
        className: 'dropdown-menu',

        wait: 300,
        queryParameter: 'query',
        minKeywordLength: 2,
        currentText: '',

        initialize: function (opts) {
            _.extend(this, opts);
            this.filter = _.debounce(this.filter, this.wait);
        },

        render: function () {
            this.input.attr('autocomplete', 'off');

            this.$el.width(this.input.outerWidth());
            this.$el.css('left', '15px'); // move to adjusted position

            this.input
                .keyup(_.bind(this.keyup, this))
                .keydown(_.bind(this.keydown, this))
                .after(this.$el);

            return this;
        },

        keydown: function (e) {
            if (e.keyCode == 38) return this.move(-1);
            if (e.keyCode == 40) return this.move(+1);
            if (e.keyCode == 13) return this.onEnter();
            if (e.keyCode == 27) return this.hide();
        },

        keyup: function () {
            var keyword = this.input.val();
            if (this.isChanged(keyword)) {
                if (this.isValid(keyword)) {
                    this.filter(keyword);
                } else {
                    this.hide()
                }
            }
        },

        filter: function (keyword) {
            if (this.model.url) {
                var parameters = {};
                parameters[this.queryParameter] = keyword;

                this.model.fetch({
                    data: parameters
                }).done(_.bind(function () {
                        this.loadResult(this.model.models, keyword);
                    }, this));
            } else {
                this.loadResult(this.model.filter(function (model) {
                    return model.label().indexOf(keyword) > -1
                }), keyword);
            }
        },

        isValid: function (keyword) {
            return keyword.length >= this.minKeywordLength
        },

        isChanged: function (keyword) {
            return this.currentText != keyword;
        },

        move: function (position) {
            var current = this.$el.children('.active'),
                siblings = this.$el.children(),
                index = current.index() + position;
            if (siblings.eq(index).length) {
                current.removeClass('active');
                siblings.eq(index).addClass('active');
            }
            return false;
        },

        onEnter: function () {
            this.$el.children('.active').click();
            return false;
        },

        loadResult: function (model, keyword) {
            this.currentText = keyword;
            this.show().reset();
            if (model.length) {
                _.forEach(model, this.addItem, this);
                this.show();
            } else {
                this.hide();
            }
        },

        addItem: function (model) {
            this.$el.append(new this.itemView({
                model: model,
                parent: this
            }).render().$el);
        },

        select: function (model) {
            var label = model.label();
            this.input.val(label);
            this.currentText = label;
            this.onSelect(model);
        },

        reset: function () {
            this.$el.empty();
            return this;
        },

        hide: function () {
            this.$el.hide();
            return this;
        },

        show: function () {
            this.$el.show();
            return this;
        },

        // callback definitions
        onSelect: function (model) {
        }

    });
    return AutoCompleteView;
});