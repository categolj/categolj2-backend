var React = require('react');
var Spinner = require('spin.js');

var Loading = React.createClass({
    mixins: [],
    propTypes: {},
    componentDidMount: function () {
        var opts = {} || this.props.opts;
        if (this.props.defaults) {
            opts = {
                lines: 13, // The number of lines to draw
                length: 20, // The length of each line
                width: 10, // The line thickness
                radius: 30, // The radius of the inner circle
                corners: 1, // Corner roundness (0..1)
                rotate: 0, // The rotation offset
                direction: 1, // 1: clockwise, -1: counterclockwise
                color: '#000', // #rgb or #rrggbb or array of colors
                speed: 1, // Rounds per second
                trail: 60, // Afterglow percentage
                shadow: false, // Whether to render a shadow
                hwaccel: false, // Whether to use hardware acceleration
                className: 'spinner', // The CSS class to assign to the spinner
                zIndex: 2e9, // The z-index (defaults to 2000000000)
                top: '50%', // Top position relative to parent
                left: '50%' // Left position relative to parent
            };
        }
        var spinner = new Spinner(opts);
        var loading = React.findDOMNode(this.refs.loading);
        this.props.emitter.on('request', function (count) {
            if (count > 0) {
                spinner.spin(loading);
            }
        });
        this.props.emitter.on('response', function (count) {
            if (count == 0) {
                spinner.stop();
            }
        });
    },
    componentWillUnmount: function () {
        this.props.emitter.removeAllListeners('request');
        this.props.emitter.removeAllListeners('response');
    },
    render: function () {
        return (
            <div ref="loading"></div>
        );
    }

});
module.exports = Loading;
