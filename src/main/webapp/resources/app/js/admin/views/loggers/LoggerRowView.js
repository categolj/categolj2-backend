define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var ErrorHandler = require('app/js/admin/views/ErrorHandler');
    var loggerRow = require('text!app/js/admin/templates/loggers/loggerRow.hbs');

    return Backbone.View.extend(_.extend({
        tagName: 'tr',
        template: Handlebars.compile(loggerRow),

        events: {
            'click button.logger-trace': '_toggleTrace',
            'click button.logger-debug': '_toggleDebug',
            'click button.logger-info': '_toggleInfo',
            'click button.logger-warn': '_toggleWarn',
            'click button.logger-error': '_toggleError'
        },

        initialize: function () {
        },
        render: function () {
            this.$el.html(this.template(this.model.toJSON()));
            this._changeButtonClass();
            return this;
        },
        _changeButtonClass: function () {
            if (this.model.isTraceEnabled()) {
                this.$('.logger-trace').removeClass('btn-default').addClass('btn-danger');
            } else if (this.model.isDebugEnabled()) {
                this.$('.logger-debug').removeClass('btn-default').addClass('btn-warning');
            } else if (this.model.isInfoEnabled()) {
                this.$('.logger-info').removeClass('btn-default').addClass('btn-info');
            } else if (this.model.isWarnEnabled()) {
                this.$('.logger-warn').removeClass('btn-default').addClass('btn-success');
            } else if (this.model.isErrorEnabled()) {
                this.$('.logger-error').removeClass('btn-default').addClass('btn-primary');
            }
        },
        _toggleTrace: function (e) {
            e.preventDefault();
            this._changeLevel('TRACE')
        },
        _toggleDebug: function (e) {
            e.preventDefault();
            this._changeLevel('DEBUG')
        },
        _toggleInfo: function (e) {
            e.preventDefault();
            this._changeLevel('INFO')
        },
        _toggleWarn: function (e) {
            e.preventDefault();
            this._changeLevel('WARN')
        },
        _toggleError: function (e) {
            e.preventDefault();
            this._changeLevel('ERROR')
        },
        _changeLevel: function (level) {
            this.model.set('level', level);
            this.model.save()
                .success(_.bind(this._refresh, this))
                .fail(_.bind(this._refresh, this));
            return false;
        },
        _refresh: function () {
            this.trigger('refresh');
        }
    }, ErrorHandler));
});
