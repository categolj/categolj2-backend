define(function (require) {
    var AdminRouter = require('./routers/AdminRouter');
    var $ = require('jquery');

    new AdminRouter();
    $(document).ready(function () {
        Backbone.history.start();
    });
});