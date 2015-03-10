var FRONTEND_ROOT = 'http://blog.ik.am/';
var defaults = {
    FRONTEND_ROOT: FRONTEND_ROOT,
    API_ROOT: 'api/v1',
    SEPARATOR: '::',
    BLOG_TITLE: 'BLOG.IK.AM'
};

var Config;
if (!window.Config) {
    Config = {};
} else {
    Config = window.Config;
}

for (var propName in defaults) {
    if (!Config[propName]) {
        Config[propName] = defaults[propName];
    }
}

module.exports = Config;