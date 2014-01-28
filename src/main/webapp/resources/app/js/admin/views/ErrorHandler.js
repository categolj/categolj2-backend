define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    return {
        showErrors: function (details) {
            _.each(details, function (detail) {
                if (detail.target) {
                    var name;
                    if (_.contains(detail.target, '.')) {
                        name = detail.target.split('.')[1];
                    } else {
                        name = detail.target;
                    }

                    var $target = this.$('[name=' + name + ']');
                    if (!_.isEmpty($target)) {
                        var $group = $target.closest('.form-group');
                        $group.addClass('has-error');
                        $group.find('.help-block').text(detail.message).removeClass('hidden');
                    }
                } else if (detail.message) {
                    alert(detail.message);
                }
            }, this);
        },
        handleError: function (response) {
            console.log(response);
            if (this.buttonView) {
                this.buttonView.enable();
            }
            this.render();

            if (response.responseJSON.details) {
                this.showErrors(response.responseJSON.details);
            }
        }
    };
});