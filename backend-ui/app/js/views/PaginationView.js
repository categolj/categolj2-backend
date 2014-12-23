define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');

    var pagination = require('text!js/templates/pagination.hbs');

    var PaginationView = Backbone.View.extend({
        maxDisplayCount: 5,
        template: Handlebars.compile(pagination),
        events: {
            'click .go-first': 'goFirst',
            'click .go-last': 'goLast',
            'click .go-to': 'goTo'
        },
        initialize: function (opts) {
            _.extend(this, opts);
            this.page = this.collection.page;
            this.pageSize = this.collection.pageSize;
            this.last = this.collection.totalPages - 1;
        },
        render: function () {
            var attributes = this._buildAttributes();
            this.$el.html(this.template(attributes));
            return this;
        },
        goFirst: function (e) {
            e.preventDefault();
            var page = 0,
                to = this._changeParameter(Backbone.history.fragment, page, this.pageSize);
            Backbone.history.navigate(to, true);
        },
        goLast: function (e) {
            e.preventDefault();
            var page = this.last,
                to = this._changeParameter(Backbone.history.fragment, page, this.pageSize);
            Backbone.history.navigate(to, true);
        },
        goTo: function (e) {
            e.preventDefault();
            var $a = $(e.target),
                page = $a.data('page'),
                to = this._changeParameter(Backbone.history.fragment, page, this.pageSize);
            Backbone.history.navigate(to, true);
        },
        _buildAttributes: function () {
            var collection = this.collection,
                attributes = {
                    firstPageEnabled: !collection.first,
                    firstPageDisabled: collection.first,
                    lastPageEnabled: !collection.last,
                    lastPageDisabled: collection.last
                },
                beginAndEnd = this._calcBeginAndEnd();
            attributes.links = [];
            for (var page = beginAndEnd.begin; page <= beginAndEnd.end; page++) {
                attributes.links.push({
                    page: page,
                    displayPage: page + 1,
                    active: page == this.page,
                    inactive: page != this.page
                });
            }
            return attributes;
        },
        _calcBeginAndEnd: function () {
            var begin = Math.max(0, this.page - Math.floor(this.maxDisplayCount / 2))
                , end = begin + this.maxDisplayCount - 1;
            if (end > this.last) {
                end = this.last;
                begin = Math.max(0, end - (this.maxDisplayCount - 1));
            }
            return {
                begin: begin,
                end: end
            };
        },
        _changeParameter: function (target, page, pageSize) {
            if (!target.match('page') || !target.match('size')) {
                return target + '/page=' + page + '/size=' + pageSize;
            }
            return target.replace(/page=([0-9]+)/, 'page=' + page)
                .replace(/size=([0-9])+/, 'size=' + pageSize);
        }
    });
    return PaginationView;
});