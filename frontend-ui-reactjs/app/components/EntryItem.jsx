var React = require('react');
var Link = require('react-router').Link;
var Categorizer = require('./Categorizer.jsx');

var EntryItem = React.createClass({
    mixins: [Categorizer],
    propTypes: {},
    getInitialState: function () {
        if (this.props.visible) {
            return {
                contentsVisible: 'visible',
                buttonVisible: 'invisible'
            };
        } else {
            return {
                contentsVisible: 'invisible',
                buttonVisible: 'visible'
            };
        }
    },
    handleClick: function () {
        this.setState({
            contentsVisible: 'visible',
            buttonVisible: 'invisible'
        });
    },
    render: function () {
        var entry = this.props.entry;
        var category = this.createCategoryLink(entry.categoryName);
        var tags = [];
        entry.tags
            .forEach(function (x) {
                var tagName = x.tagName;
                tags.push(
                    <Link to="entriesByTag"
                        key={tagName}
                        params={{tagName: tagName}}>
                    {tagName}
                    </Link>
                );
                tags.push(' ');
            });
        tags.pop();

        return (
            <article>
                <h3 cssClass="article-title">
                    <Link to="entry" params={{entryId: entry.entryId}} cssClass="entry-title">
                    {entry.title}
                    </Link>
                </h3>
                <p cssClass="article-meta">updated at {entry.lastModifiedDate} by
                &nbsp;
                    <Link to="entriesByUsername" params={{username: entry.lastModifiedBy}}>
                    {entry.lastModifiedBy}
                    </Link>
                &nbsp;created at {entry.createdDate} by
                &nbsp;
                    <Link to="entriesByUsername" params={{username: entry.createdBy}}>
                    {entry.createdBy}
                    </Link>
                    <br />
                    category: {category}
                    <br />
                    tags: {tags}
                </p>
                <button className={this.state.buttonVisible}
                    onClick={this.handleClick}>Read this article</button>
                <div className={this.state.contentsVisible}
                    dangerouslySetInnerHTML={{__html: entry.contents}} />
            </article>
        );
    }
});
module.exports = EntryItem;
