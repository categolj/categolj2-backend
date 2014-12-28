var _ = require('underscore');

function appendPageAndSize(target, page, pageSize) {
    var append = 'page=' + page + '&size=' + pageSize;
    return _.contains(target, '?') ? target + '&' + append : target + '?' + append;
}

module.exports = {
    appendPageAndSize: appendPageAndSize
}