define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var ModalView = require('js/views/ModalView');


    return ModalView.extend({
        initialize: function (entry) {
            var body = entry.getFormattedContents();
            this.opts = {
                title: 'Preview',
                body: new Handlebars.SafeString(body)
            }
        }
    });
});