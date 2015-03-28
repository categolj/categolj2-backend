var React = require('react');
var EntryItem = require('./EntryItem.jsx');
var EntriesModel = require('../models.jsx').EntriesModel;
var Pageable = require('./Pageable.js');


var EntryById = React.createClass({
    mixins: [],
    propTypes: {},
    contextTypes: {
        router: React.PropTypes.func
    },
    getInitialState: function () {
        return {
            entryId: '0',
            title: 'Loading ...',
            contents: 'Loading ...',
            lastModifiedDate: '',
            createdDate: '',
            lastModifiedBy: '',
            createdBy: '',
            categoryName: [],
            tags: []
        };
    },
    componentDidMount: function () {
        var entryId = this.context.router.getCurrentParams().entryId;
        EntriesModel.findOne(entryId)
            .then(function (x) {
                this.setState(x);
            }.bind(this));
    },
    render: function () {
        var entry = this.state;
        return (
            <EntryItem entry={entry} visible={true} />
        );
    }

});
module.exports = EntryById;
