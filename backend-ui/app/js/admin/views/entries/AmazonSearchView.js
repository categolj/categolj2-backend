define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');

    var Books = require('js/admin/collections/Books');
    var AmazonBookRowView = require('js/admin/views/entries/AmazonBookRowView');

    var amazonSearchForm = require('text!js/admin/templates/entries/amazonSearchForm.hbs');

    var AmazonSearchView = Backbone.View.extend({
        template: Handlebars.compile(amazonSearchForm),
        events: {
            'click #search-by-keyword': '_searchByKeyword',
            'keypress #keyword': '_searchOnEnter'
        },
        bindings: {
            '#keyword': 'keyword'
        },
        initialize: function () {
            this.$el.html(this.template());
            this.model = new Backbone.Model();
            this.collection = new Books();
            this.stickit();
            this.$searchResult = this.$('#amazon-search-result');
            this.$searchBtn = this.$('#search-by-keyword');
            this.listenTo(this.collection, 'sync', this.renderSearchResult);
            this.listenTo(this.model, 'change:keyword', this._checkState);
        },
        render: function () {
            this.focus();
            return this;
        },
        renderSearchResult: function () {
            this.$searchResult.removeClass('hidden');
            var $tableBody = this.$searchResult.find('tbody');
            $tableBody.empty();
            this.collection.each(_.bind(function (book) {
                var amazonBookRowView = new AmazonBookRowView({
                    model: book
                });
                this.listenTo(amazonBookRowView, 'bookSelected', this._onBookSelected);
                $tableBody.append(amazonBookRowView.render().el);
            }, this));
        },
        focus: function () {
            this.$('#keyword').focus();
            return this;
        },
        _checkState: function () {
            if (this.model.get('keyword')) {
                this.$searchBtn.removeClass('disabled');
            } else {
                this.$searchBtn.addClass('disabled');
            }
        },
        _searchByKeyword: function (e) {
            e.preventDefault();
            this.collection.fetch({
                data: this.model.toJSON()
            });
        },
        _searchOnEnter: function (e) {
            if (e.keyCode !== 13) return;
            this._searchByKeyword(e);
        },
        _onBookSelected: function (book) {
            this.trigger('bookSelected', book);
        }
    });
    return AmazonSearchView;
});