var React = require('react');
var Router = require('react-router');
var Config = require('./Config.js');
var App = require('./components/App.jsx');

//React.render(
//    <App />,
//    document.getElementById('example')
//);

Router.run(App.routes, function (Handler) {
    document.title = Config.BLOG_TITLE;
    React.render(<Handler/>, document.getElementById('example'));
});
