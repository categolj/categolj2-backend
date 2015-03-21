var defaults = {
    BLOG_URL: 'http://localhost:8080',
    BLOG_TITLE: 'Your Blog Title',
    BLOG_DESCRIPTION: "Your Blog Description",
    API_ROOT: 'api/v1',
    SEPARATOR: '::'
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