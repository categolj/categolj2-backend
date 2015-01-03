define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');
    var Bloodhound = require('bloodhound');
    require('bootstrap-tagsinput');
    var Constants = require('js/Constants');

    return Backbone.View.extend({
        events: {},

        initialize: function () {
            this.tags = new Bloodhound({
                datumTokenizer: Bloodhound.tokenizers.obj.whitespace('name'),
                queryTokenizer: Bloodhound.tokenizers.whitespace,
                prefetch: {
                    url: Constants.API_ROOT + '/tags',
                    filter: function (list) {
                        return $.map(list, function (tag) {
                            return {name: tag.tagName};
                        });
                    }
                }
            });
            this.tags.clearPrefetchCache(); // always refresh
            this.tags.initialize(true);
            // HACK: overrule hardcoded display inline-block of typeahead.js
            $(".twitter-typeahead").css('display', 'inline');
        },
        render: function () {
            this.$el.tagsinput({
                typeaheadjs: {
                    name: 'tags',
                    displayKey: 'name',
                    valueKey: 'name',
                    trimValue: true,
                    maxChars: 255,
                    source: this.tags.ttAdapter()
                }
            });
            return this;
        }
    });
});