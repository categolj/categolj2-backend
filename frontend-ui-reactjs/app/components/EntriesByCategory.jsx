var React = require('react');
var EntryItem = require('./EntryItem.jsx');
var Pager = require('react-pager');
var EntriesModel = require('../models.jsx').EntriesModel;
var Pageable = require('./Pageable.js');
var Categorizer = require('./Categorizer.jsx');
var Config = require('../Config.js');


var EntriesByCategory = React.createClass({
    mixins: [Pageable, Categorizer],
    propTypes: {},
    contextTypes: {
        router: React.PropTypes.func
    },
    getInitialState: function () {
        return {
            content: [],
            totalPages: 10,
            number: 0
        };
    },
    loadFromServer: function () {
        var params = this.context.router.getCurrentParams(),
            query = this.context.router.getCurrentQuery();
        EntriesModel.findByCategory(params.category, query.page, query.size)
            .then(function (x) {
                this.setState(x);
            }.bind(this));
    },
    componentDidMount: function () {
        this.loadFromServer();
    },
    handlePageChanged: function (newPage) {
        var params = this.context.router.getCurrentParams();
        this.changeLocation(newPage, this.state.size);

        EntriesModel.findByCategory(params.category, newPage, this.state.size)
            .then(function (x) {
                this.setState(x);
            }.bind(this));
        window.scroll(0, 0);
    },
    render: function () {
        var entries = this.state.content
            .map(function (entry) {
                return (
                    <EntryItem key={entry.entryId} entry={entry}/>
                );
            });

        var separator = Config.SEPARATOR;
        var category = this.context.router.getCurrentParams().category.split(separator);
        var ret = this.createCategoryLink(category);

        return (
            <div>
                <h2>{ret}</h2>
                {entries}
                <Pager total={this.state.totalPages}
                    current={this.state.number}
                    visiblePages={5}
                    onPageChanged={this.handlePageChanged} />
            </div>
        );
    }

});
module.exports = EntriesByCategory;
