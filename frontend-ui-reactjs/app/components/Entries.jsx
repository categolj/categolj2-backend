var React = require('react');
var EntryItem = require('./EntryItem.jsx');
var Pager = require('react-pager');
var EntriesModel = require('../models.jsx').EntriesModel;
var Pageable = require('./Pageable.js');


var Entries = React.createClass({
    mixins: [Pageable],
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
    componentDidMount: function () {
        var query = this.context.router.getCurrentQuery();
        EntriesModel.findAll(query.page, query.size)
            .then(function (x) {
                this.setState(x);
            }.bind(this));
    },
    handlePageChanged: function (newPage) {
        this.changeLocation(newPage, this.state.size);
        EntriesModel.findAll(newPage, this.state.size)
            .then(function (x) {
                this.setState(x);
            }.bind(this));
        window.scroll(0, 0);
    },
    render: function () {
        var entries = this.state.content.map(function (entry) {
            return (
                <EntryItem key={entry.entryId} entry={entry}/>
            );
        });
        return (
            <div>
            {entries}
                <Pager total={this.state.totalPages}
                    current={this.state.number}
                    visiblePages={5}
                    onPageChanged={this.handlePageChanged} />
            </div>
        );
    }

});
module.exports = Entries;
