var Pageable = {
    changeLocation: function (page, size) {
        var query = this.context.router.getCurrentQuery();
        var routes = this.context.router.getCurrentRoutes();
        var route = routes[routes.length - 1];
        query.page = page;
        query.size = size;
        this.context.router.transitionTo(route.path,
            this.context.router.getCurrentParams(),
            query);
    }
};

module.exports = Pageable;