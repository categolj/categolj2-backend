define(function (require) {
    var AdminRouter = require('./routers/AdminRouter');
    var $ = require('jquery');

    new AdminRouter();

    $(document).ready(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function (e, xhr) {
            xhr.setRequestHeader(header, token);
            xhr.setRequestHeader('X-Admin', true);
        });
        Backbone.history.start();
    });
});