var React = require('react');

var SearchForm = React.createClass({
    mixins: [],
    propTypes: {},
    contextTypes: {
        router: React.PropTypes.func
    },
    handleSubmit: function (e) {
        e.preventDefault();
        var keyword = React.findDOMNode(this.refs.keyword).value.trim();
        this.context.router.transitionTo('entriesByKeyword', {keyword: keyword});
    },
    render: function () {
        return (
            <form cssClass="search" onSubmit={this.handleSubmit}>
                <input ref="keyword" cssClass="search-field" type="search" placeholder="search..." />
            </form>
        );
    }

});
module.exports = SearchForm;
