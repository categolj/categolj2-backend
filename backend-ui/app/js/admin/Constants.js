define(function (require) {
    var BACKEND_HOST = 'http://blog.ik.am/';
    // var BACKEND_HOST = '';
    return {
        API_ROOT: BACKEND_HOST + 'api/v1',
        MANAGEMENT_ROOT: BACKEND_HOST + 'management'
    };
});