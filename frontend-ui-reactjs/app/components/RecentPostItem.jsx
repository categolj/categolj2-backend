var React = require('react');
var Link = require('react-router').Link;

var RecentPostItem = React.createClass({
    mixins: [],
    propTypes: {},
    render: function () {
        var entry = this.props.entry;
        return (
            <li>
                <Link to="entry" params={{entryId: entry.entryId}}>
                    {entry.title}
                </Link>
            </li>
        );
    }

});
module.exports = RecentPostItem;
