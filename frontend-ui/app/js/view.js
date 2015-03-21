var Config = require('./config.js');
var Util = require('./util.js');
var model = require('./model.js');
var Backbone = require('backbone');
var _ = require('underscore');
var $ = require('jquery');
var Handlebars = require('handlebars');
var Spinner = require('spin.js');

function scroll() {
    $('body').animate({scrollTop: 0}, 'fast');
}

// Views
var AppView = Backbone.View.extend({
    initialize: function () {
        this.recentPosts = new model.RecentPosts();
        this.recentPostsView = new RecentPostsView({
            el: $('#recent-posts'),
            collection: this.recentPosts
        });

        this.recentPosts.fetch().success(_.bind(function () {
            this.recentPostsView.render();
        }, this));

        this.searchFormView = new SearchFormView({
            el: $('#search-form')
        });

        var links = new model.Links({});
        var linksView = new LinksView({
            el: $('#links'),
            collection: links
        });
        links.fetch().success(_.bind(function () {
            linksView.render();
        }, this));
    },
    showEntries: function (page, pageSize) {
        this.entries = new model.Entries({
            page: page,
            pageSize: pageSize
        });
        var entriesView = new EntriesView({
            collection: this.entries
        });

        this.entries.fetch().success(_.bind(function () {
            this.$el.html(entriesView.render().el);
        }, this));
        scroll();
    },
    showEntry: function (id) {
        var entry;
        if (this.entries) {
            entry = this.entries.get(Number(id));
        }

        if (!entry) {
            entry = new model.Entry({
                entryId: id
            });
            entry.fetch({
                async: false
            });
            if (this.entries) {
                this.entries.add(entry);
            }
        }

        document.title = entry.get('title') + ' - ' + Config.BLOG_TITLE;
        var entryView = new EntryView({
            model: entry,
            render: true,
            social: true
        });
        this.$el.html(entryView.render().el);
        scroll();
    },
    showSearchResult: function (keyword, page, pageSize) {
        var searchResultView = new SearchResultView({
            keyword: keyword,
            page: page,
            pageSize: pageSize
        });
        this.$el.html(searchResultView.render().el);
        scroll();
    },
    showCategories: function () {
        var categories = new model.Categories();
        var categoriesView = new CategoriesView({
            collection: categories
        });

        categories.fetch().success(_.bind(function () {
            this.$el.html(categoriesView.render().el);
        }, this));
        scroll();
    },
    showEntriesByCategory: function (category, page, pageSize) {
        var entriesView = new EntriesByCategoryView({
            category: category,
            page: page,
            pageSize: pageSize
        });
        this.$el.html(entriesView.render().el);
        scroll();
    },
    showTags: function () {
        var tags = new model.Tags();
        var tagsView = new TagsView({
            collection: tags
        });

        tags.fetch().success(_.bind(function () {
            this.$el.html(tagsView.render().el);
        }, this));
        scroll();
    },
    showEntriesByTag: function (tag, page, pageSize) {
        var entriesView = new EntriesByTagView({
            tag: tag,
            page: page,
            pageSize: pageSize
        });
        this.$el.html(entriesView.render().el);
        scroll();
    },
    showEntriesByUser: function (userId, page, pageSize) {
        var entriesView = new EntriesByUserView({
            userId: userId,
            page: page,
            pageSize: pageSize
        });
        this.$el.html(entriesView.render().el);
        scroll();
    }
});

var EntriesView = Backbone.View.extend({
    template: Handlebars.compile($('#entry-tmpl').html()),
    initialize: function (options) {
        this.options = options;
    },
    render: function () {
        if (this.collection.size() == 1) {
            // if there is only one model in collection, render it soon.
            var entry = this.collection.at(0);
            // set 'render' true to show contents
            var entryView = new EntryView({
                model: entry,
                render: true
            });
            this.$el.html(entryView.render().el);
        } else {
            // if there are some models in collection, render later and show button to render.
            _.each(this.collection.models, _.bind(function (entry) {
                // set 'render' false to show 'Read' button.
                var entryView = new EntryView({
                    model: entry,
                    render: false
                });
                this.$el.append(entryView.render().el);
            }, this));
        }
        // add pagination after entries
        var paginationView = new PaginationView({
            collection: this.collection
        });
        this.$el.append(paginationView.render().el);
        return this;
    }
});


var EntryView = Backbone.View.extend({
    tagName: 'div',
    template: Handlebars.compile($('#entry-tmpl').html()),
    events: {
        'click button': 'renderContents'
    },
    initialize: function (options) {
        this.options = options;
    },
    render: function () {
        var attributes = {
            frontendRoot: Config.BLOG_URL,
            blogTitle: Config.BLOG_TITLE,
            social: this.options.social
        };
        if (this.options.render) {
            // set 'render' true to show contents
            attributes.render = true;
        } else {
            attributes.button = true;
        }
        var attributes = _.extend(attributes, this.model.toJSON());
        this.$el.html(this.template(attributes));
        return this;
    },
    renderContents: function (e) {
        var $button = $(e.target);
        $button.parent().html(this.model.get('contents'));
    }
});

var RecentPostsView = Backbone.View.extend({
    template: Handlebars.compile($('#recent-posts-tmpl').html()),
    render: function () {
        this.$el.html(this.template({
            recentPosts: this.collection.toJSON()
        }));
        return this;
    }
});

var CategoriesView = Backbone.View.extend({
    tagName: 'div',
    template: Handlebars.compile($('#categories-tmpl').html()),
    render: function () {
        this.$el.html(this.template({
            categories: this.collection.toJSON()
        }));
        return this;
    }
});

var EntriesByCategoryView = Backbone.View.extend({
    tagName: 'div',
    template: Handlebars.compile($('#category-tmpl').html()),
    initialize: function (options) {
        this.options = options;
    },
    render: function () {
        var category = this.options.category.split(Config.SEPARATOR);
        this.$el.append(this.template({
            category: category
        }));
        var entries = new model.Entries(this.options);
        entries.targetUrl = Config.API_ROOT + '/categories/' + this.options.category + '/entries';

        var entriesView = new EntriesView({
            collection: entries
        });
        entries.fetch().success(_.bind(function () {
            this.$el.append(entriesView.render().el);
        }, this));
        return this;
    }
});

var TagsView = Backbone.View.extend({
    tagName: 'div',
    template: Handlebars.compile($('#tags-tmpl').html()),
    render: function () {
        this.$el.html(this.template({
            tags: this.collection.toJSON()
        }));
        return this;
    }
});

var EntriesByTagView = Backbone.View.extend({
    tagName: 'div',
    template: Handlebars.compile($('#entries-by-tag-tmpl').html()),
    initialize: function (options) {
        this.options = options;
    },
    render: function () {
        this.$el.append(this.template({
            tag: this.options.tag
        }));
        var entries = new model.Entries(this.options);
        entries.targetUrl = Config.API_ROOT + '/tags/' + this.options.tag + '/entries';
        var entriesView = new EntriesView({
            collection: entries
        });
        entries.fetch().success(_.bind(function () {
            this.$el.append(entriesView.render().el);
        }, this));
        return this;
    }
});

var EntriesByUserView = Backbone.View.extend({
    tagName: 'div',
    template: Handlebars.compile($('#entries-by-user-tmpl').html()),
    initialize: function (options) {
        this.options = options;
    },
    render: function () {
        this.$el.append(this.template({
            username: 'User(' + this.options.userId + ')'
        }));
        var entries = new model.Entries(this.options);
        entries.targetUrl = Config.API_ROOT + '/users/' + this.options.userId + '/entries';
        var entriesView = new EntriesView({
            collection: entries
        });
        entries.fetch().success(_.bind(function () {
            this.$el.append(entriesView.render().el);
        }, this));
        return this;
    }
});

var SearchFormView = Backbone.View.extend({
    template: Handlebars.compile($('#search-result-tmpl').html()),
    events: {
        'submit': 'search'
    },
    search: function (e) {
        e.preventDefault();
        var q = this.$('input[name=q]').val();
        Backbone.history.navigate('entries?q=' + q, {trigger: true});
    }
});

var SearchResultView = Backbone.View.extend({
    template: Handlebars.compile($('#search-result-tmpl').html()),
    tagName: 'div',
    initialize: function (options) {
        this.options = options;
    },
    render: function () {
        this.$el.append(this.template({
            keyword: this.options.keyword
        }));
        var entries = new model.Entries(this.options);
        entries.targetUrl = Config.API_ROOT + '/entries?keyword=' + encodeURIComponent(this.options.keyword);
        var entriesView = new EntriesView({
            collection: entries
        });
        entries.fetch().success(_.bind(function () {
            this.$el.append(entriesView.render().el);
        }, this));
        return this;
    }
});

var PaginationView = Backbone.View.extend({
    maxDisplayCount: 5,
    template: Handlebars.compile($('#pagination-tmpl').html()),
    events: {
        'click a.go-first': 'goFirst',
        'click a.go-last': 'goLast',
        'click a.go-to': 'goTo'
    },
    initialize: function () {
        this.page = this.collection.page;
        this.pageSize = this.collection.pageSize;
        this.last = this.collection.totalPages - 1;
    },
    render: function () {
        var attributes = this.buildAttributes();
        this.$el.html(this.template(attributes));
        return this;
    },
    goFirst: function (e) {
        e.preventDefault();
        var page = 0,
            to = this.changeParameter(_.contains(location.hash, '#') ? location.hash.substring(1) : '', page, this.pageSize);
        Backbone.history.navigate(to, true);
    },
    goLast: function (e) {
        e.preventDefault();
        var page = this.last,
            to = this.changeParameter(_.contains(location.hash, '#') ? location.hash.substring(1) : '', page, this.pageSize);
        Backbone.history.navigate(to, true);
    },
    goTo: function (e) {
        e.preventDefault();
        var $a = $(e.target),
            page = $a.data('page'),
            to = this.changeParameter(_.contains(location.hash, '#') ? location.hash.substring(1) : '', page, this.pageSize);
        Backbone.history.navigate(to, true);
    },
    buildAttributes: function () {
        var collection = this.collection,
            attributes = {
                firstPageEnabled: !collection.firstPage,
                firstPageDisabled: collection.firstPage,
                lastPageEnabled: !collection.lastPage,
                lastPageDisabled: collection.lastPage
            },
            beginAndEnd = this.calcBeginAndEnd();
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
    calcBeginAndEnd: function () {
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
    changeParameter: function (target, page, pageSize) {
        if (!target.match('page') || !target.match('size')) {
            return Util.appendPageAndSize(target, page, pageSize);
        }
        return target.replace(/page=([0-9]+)/, 'page=' + page)
            .replace(/size=([0-9])+/, 'size=' + pageSize);
    }
});

var LinksView = Backbone.View.extend({
    template: Handlebars.compile($('#links-tmpl').html()),
    render: function () {
        this.$el.html(this.template({
            links: this.collection.toJSON()
        }));
        return this;
    }
});

var LoadingView = Backbone.View.extend({
    el: '#loading',
    initialize: function () {
        this.spinner = new Spinner();
        $(document).on('ajaxStart', _.bind(this.spin, this));
        $(document).on('ajaxComplete', _.bind(this.stop, this));
    },
    spin: function () {
        this.spinner.spin(this.el);
    },
    stop: function () {
        this.spinner.stop();
    }
});

module.exports = {
    AppView: AppView,
    EntriesView: EntriesView,
    EntryView: EntryView,
    RecentPostsView: RecentPostsView,
    CategoriesView: CategoriesView,
    EntriesByCategoryView: EntriesByCategoryView,
    TagsView: TagsView,
    EntriesByTagView: EntriesByTagView,
    EntriesByUserView: EntriesByUserView,
    SearchFormView: SearchFormView,
    SearchResultView: SearchResultView,
    PaginationView: PaginationView,
    LinksView: LinksView,
    LoadingView: LoadingView
}