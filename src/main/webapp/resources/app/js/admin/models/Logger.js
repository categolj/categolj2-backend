define(function (require) {
    var Backbone = require('backbone');

    return Backbone.Model.extend({
        idAttribute: 'name',
        isTraceEnabled: function() {
            return this.get('level') === 'TRACE';
        },
        isDebugEnabled: function() {
            return this.get('level') === 'DEBUG';
        },
        isInfoEnabled: function() {
            return this.get('level') === 'INFO';
        },
        isWarnEnabled: function() {
            return this.get('level') === 'WARN';
        },
        isErrorEnabled: function() {
            return this.get('level') === 'ERROR';
        }
    });
});