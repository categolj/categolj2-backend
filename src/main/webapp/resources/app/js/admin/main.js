define(function (require) {
    var $ = require('jquery');

    var AdminRouter = require('./routers/AdminRouter');
    var SpinView = require('./views/SpinView');

    new AdminRouter();

    $(document).ready(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function (e, xhr) {
            xhr.setRequestHeader(header, token);
            xhr.setRequestHeader('X-Admin', true);
        });

        var spinView = new SpinView();
        $('body').append(spinView.render().$el);
        $(document).on('ajaxStart',function () {
            spinView.spin();
        }).on('ajaxComplete', function () {
                spinView.stop();
            });

        Backbone.history.start();
    });
});