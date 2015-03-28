var React = require('react');
var CategoryItem = require('./CategoryItem.jsx');
var CategoriesModel = require('../models.jsx').CategoriesModel;
var Config = require('../Config.js');

var Categories = React.createClass({
    mixins: [],
    propTypes: {},
    getInitialState: function () {
        return {data: []};
    },
    componentDidMount: function () {
        CategoriesModel.findAll()
            .then(function (x) {
                this.setState({data: x});
            }.bind(this));
    },
    render: function () {
        var categories = this.state.data.map(function (category) {
            var key = category.categoryName.join(Config.SEPARATOR);
            return (
                <CategoryItem key={key} categoryName={category.categoryName}/>
            );
        });
        return (
            <div>
                <h2>All Categories</h2>
                <ul>{categories}</ul>
            </div>
        );
    }

});
module.exports = Categories;
