var React = require('react');
var Categorizer = require('./Categorizer.jsx');

var CategoryItem = React.createClass({
    mixins: [Categorizer],
    propTypes: {},
    render: function () {
        var ret = this.createCategoryLink(this.props.categoryName);
        return (
            <li>{ret}</li>
        );
    }
});
module.exports = CategoryItem;
