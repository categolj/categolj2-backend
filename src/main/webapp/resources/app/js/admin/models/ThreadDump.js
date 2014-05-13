define(function (require) {
    var Backbone = require('backbone');

    return Backbone.Model.extend({
        idAttribute: 'threadId',
        getClass: function () {
            var state = this.get('threadState').toUpperCase(),
                clazz = 'default';
            switch (state) {
                case 'NEW':
                    clazz = 'primary';
                    break;
                case 'RUNNABLE':
                    clazz = 'success';
                    break;
                case 'WAITING':
                    clazz = 'info';
                    break;
                case 'TIMED_WAITING':
                    clazz = 'info';
                    break;
                case 'BLOCKED':
                    clazz = 'warning';
                    break;
                case 'TERMINATED':
                    clazz = 'danger';
                    break;
            }
            return clazz;
        },
        getDetailedState: function () {
            var state = this.get('threadState');
            if (this.get('suspended')) {
                state += ' (suspended)';
            }
            if (this.get('inNative')) {
                state += ' (in native)';
            }
            return state;
        },
        toJSONForView: function () {
            return _.extend({
                class: this.getClass(),
                detailedState: this.getDetailedState()
            }, this.toJSON())
        }
    });
});