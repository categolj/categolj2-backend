define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var fileupload = require('text!app/js/admin/templates/entries/fileupload.hbs');
    var FileUploadView  = Backbone.View.extend({
        template: Handlebars.compile(fileupload),
//        attributes: {
//            enctype: 'multipart/form-data'
//        },
        initialize: function() {
            this.$el.html(this.template());
        },
        render: function() {
            return this;
        }
    });
    return FileUploadView;
});