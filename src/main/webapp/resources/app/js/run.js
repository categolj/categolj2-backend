var curl;
(function () {
    curl({
        main: 'main',
        baseUrl: 'resources',
        packages: {
            main: {location: 'app/js/admin'},
            curl: {location: 'vendor/curl/src/curl'},
            jquery: {location: 'vendor/jquery/jquery', main: '.'},
            backbone: {location: 'vendor/backbone-amd/backbone', main: '.'},
            'backbone.stickit': {location: 'vendor/backbone.stickit/backbone.stickit', main: '.'},
            'backbone.validation': {location: 'vendor/backbone.validation/src/backbone-validation-amd', main: '.'},
            underscore: {location: 'vendor/lodash/dist/lodash', main: '.'},
            handlebars: {location: 'vendor/handlebars/handlebars', main: '.',
                config: {
                    dontWrapLegacy: true,
                    exports: 'Handlebars',
                    loader: 'curl/loader/legacy'
                }},
            spin: {location: 'vendor/spin.js/dist/spin.js', main: '.'},
            bootstrap: {location: 'vendor/bootstrap/dist/js/bootstrap', main: '.',
                config: {
                    exports: 'jQuery',
                    requires: ['jquery'],
                    loader: 'curl/loader/legacy'

                }
            }
        }
    });
}());