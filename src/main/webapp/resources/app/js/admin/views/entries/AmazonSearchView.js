define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');

    var Books = require('app/js/admin/collections/Books');
    var AmazonBookRowView = require('app/js/admin/views/entries/AmazonBookRowView');

    var amazonSearchForm = require('text!app/js/admin/templates/entries/amazonSearchForm.hbs');

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
            this.$('#keyword').focus();
            this.model = new Backbone.Model();
            this.collection = new Books();
            this.stickit();
            this.$searchResult = this.$('#amazon-search-result');
            this.listenTo(this.collection, 'sync', this.renderSearchResult);
        },
        render: function () {
            return this;
        },
        renderSearchResult: function () {
            this.$searchResult.removeClass('hidden');
            var $tableBody = this.$searchResult.find('tbody');
            $tableBody.empty();
            this.collection.forEach(function (book) {
                $tableBody.append(new AmazonBookRowView({
                    model: book
                }).render().el);
            });
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
        }
    });
    return AmazonSearchView;
});