var React = require('react');


var LinkItem = React.createClass({
    mixins: [],
    propTypes: {},
    render: function () {
        return (
            <li>
                <a href={this.props.link.url}>{this.props.link.linkName}</a>
            </li>
        );
    }

});
module.exports = LinkItem;
