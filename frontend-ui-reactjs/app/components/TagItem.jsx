var React = require('react');
var Link = require('react-router').Link;

var TagItem = React.createClass({
    mixins: [],
    propTypes: {},
    render: function () {
        return (
            <li>
                <Link to="entriesByTag" params={{tagName: this.props.tagName}}>
                {this.props.tagName}
                </Link>
            </li>
        );
    }

});
module.exports = TagItem;
