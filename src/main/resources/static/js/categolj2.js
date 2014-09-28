var categolj2 = {};

categolj2.API_ROOT = 'api/v1';
categolj2.FRONTEND_ROOT = 'http://blog.ik.am/';
categolj2.SEPARATOR = '::';
categolj2.BLOG_TITLE = 'BLOG.IK.AM';

// Models
categolj2.Entry = Backbone.Model.extend({
    idAttribute: 'entryId',
    url: function () {
        return categolj2.API_ROOT + '/entries/' + encodeURIComponent(this.id) + '';
    }
});
categolj2.RecentPost = Backbone.Model.extend({
});
categolj2.Category = Backbone.Model.extend({
});
categolj2.Link = Backbone.Model.extend({
});

function appendPageAndSize(target, page, pageSize) {
    var append = 'page=' + page + '&size=' + pageSize;
    return _.contains(target, '?') ? target + '&' + append : target + '?' + append;
}

// Pageable object
categolj2.Pageable = {
    parse: function (response) {
        this.firstPage = response.firstPage;
        this.lastPage = response.lastPage;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        return response.content;
    }
};

// Collections
categolj2.Entries = Backbone.Collection.extend(
    _.extend(categolj2.Pageable, { // mixin
        targetUrl: categolj2.API_ROOT + '/entries',
        model: categolj2.Entry,
        initialize: function (options) {
            options = _.extend({}, options);
            this.page = Number(options.page) || 0;
            this.pageSize = Number(options.pageSize) || 3;
        },

        url: function () {
            return appendPageAndSize(this.targetUrl, this.page, this.pageSize);
        }
    }));

categolj2.RecentPosts = Backbone.Collection.extend({
    url: categolj2.API_ROOT + '/recentposts',
    model: categolj2.RecentPost
});
categolj2.Categories = Backbone.Collection.extend({
    url: categolj2.API_ROOT + '/categories',
    model: categolj2.Category
});
categolj2.Links = Backbone.Collection.extend({
    url: categolj2.API_ROOT + '/links',
    model: categolj2.Link
});

function scroll() {
    $('body').animate({scrollTop: 0}, 'fast');
}

// Views

categolj2.AppView = Backbone.View.extend({
    initialize: function () {
        this.recentPosts = new categolj2.RecentPosts();
        this.recentPostsView = new categolj2.RecentPostsView({
            el: $('#recent-posts'),
            collection: this.recentPosts
        });

        this.recentPosts.fetch().success(_.bind(function () {
            this.recentPostsView.render();
        }, this));

        this.searchFormView = new categolj2.SearchFormView({
            el: $('#search-form')
        });

        var links = new categolj2.Links({
        });
        var linksView = new categolj2.LinksView({
            el: $('#links'),
            collection: links
        });
        links.fetch().success(_.bind(function () {
            linksView.render();
        }, this));
    },
    showEntries: function (page, pageSize) {
        this.entries = new categolj2.Entries({
            page: page,
            pageSize: pageSize
        });
        var entriesView = new categolj2.EntriesView({
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
            entry = new categolj2.Entry({
                entryId: id
            });
            entry.fetch({
                async: false
            });
            if (this.entries) {
                this.entries.add(entry);
            }
        }

        document.title = entry.get('title') + ' - ' + categolj2.BLOG_TITLE;
        var entryView = new categolj2.EntryView({
            model: entry,
            render: true,
            social: true
        });
        this.$el.html(entryView.render().el);
        scroll();
    },
    showSearchResult: function (keyword, page, pageSize) {
        var searchResultView = new categolj2.SearchResultView({
            keyword: keyword,
            page: page,
            pageSize: pageSize
        });
        this.$el.html(searchResultView.render().el);
        scroll();
    },
    showCategories: function () {
        var categories = new categolj2.Categories();
        var categoriesView = new categolj2.CategoriesView({
            collection: categories
        });

        categories.fetch().success(_.bind(function () {
            this.$el.html(categoriesView.render().el);
        }, this));
        scroll();
    },
    showEntriesByCategory: function (category, page, pageSize) {
        var entriesView = new categolj2.EntriesByCategoryView({
            category: category,
            page: page,
            pageSize: pageSize
        });
        this.$el.html(entriesView.render().el);
        scroll();
    },
    showEntriesByUser: function (userId, page, pageSize) {
        var entriesView = new categolj2.EntriesByUserView({
            userId: userId,
            page: page,
            pageSize: pageSize
        });
        this.$el.html(entriesView.render().el);
        scroll();
    }
});

categolj2.EntriesView = Backbone.View.extend({
    template: Handlebars.compile($('#entry-tmpl').html()),
    render: function () {
        if (this.collection.size() == 1) {
            // if there is only one model in collection, render it soon.
            var entry = this.collection.at(0);
            // set 'render' true to show contents
            var entryView = new categolj2.EntryView({
                model: entry,
                render: true
            });
            this.$el.html(entryView.render().el);
        } else {
            // if there are some models in collection, render later and show button to render.
            _.each(this.collection.models, _.bind(function (entry) {
                // set 'render' false to show 'Read' button.
                var entryView = new categolj2.EntryView({
                    model: entry,
                    render: false
                });
                this.$el.append(entryView.render().el);
            }, this));
        }
        // add pagination after entries
        var paginationView = new categolj2.PaginationView({
            collection: this.collection
        });
        this.$el.append(paginationView.render().el);
        return this;
    }
});


categolj2.EntryView = Backbone.View.extend({
    tagName: 'div',
    template: Handlebars.compile($('#entry-tmpl').html()),
    events: {
        'click button': 'renderContents'
    },
    render: function () {
        var attributes = {
            frontendRoot: categolj2.FRONTEND_ROOT,
            blogTitle: categolj2.BLOG_TITLE,
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

categolj2.RecentPostsView = Backbone.View.extend({
    template: Handlebars.compile($('#recent-posts-tmpl').html()),
    render: function () {
        this.$el.html(this.template({
            recentPosts: this.collection.toJSON()
        }));
        return this;
    }
});

categolj2.CategoriesView = Backbone.View.extend({
    tagName: 'div',
    template: Handlebars.compile($('#categories-tmpl').html()),
    render: function () {
        this.$el.html(this.template({
            categories: this.collection.toJSON()
        }));
        return this;
    }
});

categolj2.EntriesByCategoryView = Backbone.View.extend({
    tagName: 'div',
    template: Handlebars.compile($('#category-tmpl').html()),
    render: function () {
        var category = this.options.category.split(categolj2.SEPARATOR);
        this.$el.append(this.template({
            category: category
        }));
        var entries = new categolj2.Entries(this.options);
        entries.targetUrl = categolj2.API_ROOT + '/categories/' + this.options.category + '/entries';

        var entriesView = new categolj2.EntriesView({
            collection: entries
        });
        entries.fetch().success(_.bind(function () {
            this.$el.append(entriesView.render().el);
        }, this));
        return this;
    }
});

categolj2.EntriesByUserView = Backbone.View.extend({
    tagName: 'div',
    template: Handlebars.compile($('#entries-by-user-tmpl').html()),
    render: function () {
        this.$el.append(this.template({
            username: 'User(' + this.options.userId + ')'
        }));
        var entries = new categolj2.Entries(this.options);
        entries.targetUrl = categolj2.API_ROOT + '/users/' + this.options.userId + '/entries';
        var entriesView = new categolj2.EntriesView({
            collection: entries
        });
        entries.fetch().success(_.bind(function () {
            this.$el.append(entriesView.render().el);
        }, this));
        return this;
    }
});

categolj2.SearchFormView = Backbone.View.extend({
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

categolj2.SearchResultView = Backbone.View.extend({
    template: Handlebars.compile($('#search-result-tmpl').html()),
    tagName: 'div',
    render: function () {
        this.$el.append(this.template({
            keyword: this.options.keyword
        }));
        var entries = new categolj2.Entries(this.options);
        entries.targetUrl = categolj2.API_ROOT + '/entries?keyword=' + encodeURIComponent(this.options.keyword);
        var entriesView = new categolj2.EntriesView({
            collection: entries
        });
        entries.fetch().success(_.bind(function () {
            this.$el.append(entriesView.render().el);
        }, this));
        return this;
    }
});

categolj2.PaginationView = Backbone.View.extend({
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
            return appendPageAndSize(target, page, pageSize);
        }
        return target.replace(/page=([0-9]+)/, 'page=' + page)
            .replace(/size=([0-9])+/, 'size=' + pageSize);
    }
});

categolj2.LinksView = Backbone.View.extend({
    template: Handlebars.compile($('#links-tmpl').html()),
    render: function () {
        this.$el.html(this.template({
            links: this.collection.toJSON()
        }));
        return this;
    }
});

categolj2.LoadingView = Backbone.View.extend({
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