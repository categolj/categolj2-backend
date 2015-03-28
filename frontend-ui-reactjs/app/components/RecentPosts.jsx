var React = require('react');
var RecentPostItem = require('./RecentPostItem.jsx');
var RecentPostsModel = require('../models.jsx').RecentPostsModel;


var RecentPosts = React.createClass({
    mixins: [],
    propTypes: {},
    getInitialState: function () {
        return {data: []};
    },
    componentDidMount: function () {
        RecentPostsModel.findAll()
            .then(function (x) {
                this.setState({data: x});
            }.bind(this));
    },
    render: function () {
        var recentPosts = this.state.data.map(function (entry) {
            return (
                <RecentPostItem key={entry.entryId} entry={entry}/>
            );
        });
        return (
            <ul>{recentPosts}</ul>
        );
    }

});
module.exports = RecentPosts;
