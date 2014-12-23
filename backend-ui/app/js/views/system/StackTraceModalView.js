define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var ModalView = require('js/views/ModalView');

    var stackTrace = require('text!js/templates/system/stackTrace.hbs');


    return ModalView.extend({
        stackTraceTemplate: Handlebars.compile(stackTrace),
        initialize: function (dump) {
            var stackTrace = dump.get('stackTrace');
            var body = this.stackTraceTemplate({stackTrace: stackTrace});
            this.opts = {
                title: 'Stack Trace (' + dump.get('threadName') + ')',
                body: new Handlebars.SafeString(body)
            }
        }
    });
})
;