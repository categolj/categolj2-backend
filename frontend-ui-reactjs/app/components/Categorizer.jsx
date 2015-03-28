var React = require('react');
var Link = require('react-router').Link;
var Config = require('../Config.js');

var Categorizer = {
    createCategoryLink: function (category) {
        var separator = Config.SEPARATOR;
        var ret = [], buf = [];
        category.forEach(function (c) {
            buf.push(c);
            var category = buf.join(separator);
            ret.push(<Link to="entriesByCategory"
                key={category}
                params={{category: category}}>{c}</Link>);
            ret.push(separator);
        });
        ret.pop();
        return ret;
    }
};
module.exports = Categorizer;