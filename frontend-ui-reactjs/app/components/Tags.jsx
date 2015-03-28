var React = require('react');
var TagItem = require('./TagItem.jsx');
var models = require('../models.jsx');


var Tags = React.createClass({
    mixins: [],
    propTypes: {},
    getInitialState: function () {
        return {data: []};
    },
    componentDidMount: function () {
        models.TagsModel.findAll()
            .then(function (x) {
                this.setState({data: x});
            }.bind(this));
    },
    render: function () {
        var tags = this.state.data.map(function (tag) {
            return (
                <TagItem key={tag.tagName} tagName={tag.tagName}/>
            );
        });
        return (
            <div>
                <h2>All Tags</h2>
                <ul>{tags}</ul>
            </div>
        );
    }

});
module.exports = Tags;
