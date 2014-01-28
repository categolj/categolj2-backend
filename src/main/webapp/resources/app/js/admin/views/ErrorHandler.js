define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    return {
        showErrors: function (details) {
            _.each(details, function (detail) {
                if (detail.target) {
                    var vals = detail.target.split('.');
                    var $target = this.$('#' + vals[0] + ' [name=' + vals[1] + ']')
                    if ($target.length) {
                        var $group = $target.closest('.form-group');
                        $group.addClass('has-error');
                        $group.find('.help-block').text(detail.message).removeClass('hidden');
                    }
                } else if (detail.message) {
                    alert(detail.message);
                }
            });
        }
    };
});