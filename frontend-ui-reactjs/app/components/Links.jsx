var React = require('react');
var LinkItem = require('./LinkItem.jsx');
var LinksModel = require('../models.jsx').LinksModel;


var Links = React.createClass({
    mixins: [],
    propTypes: {},
    getInitialState: function () {
        return {data: []};
    },
    componentDidMount: function () {
        LinksModel.findAll()
            .then(function (x) {
                this.setState({data: x});
            }.bind(this));
    },
    render: function () {
        var links = this.state.data.map(function (link) {
            return (
                <LinkItem key={link.url} link={link}/>
            );
        });
        return (
            <ul>{links}</ul>
        );
    }

});
module.exports = Links;
