categolj2.SystemView = function() {
	this.$el = $('#system-body');
	var that = this;
	this.$el.load('system/info', function(e) {
		that.applyLoadedEntries(e);
	});
};

categolj2.SystemView.prototype = {
	applyLoadedEntries : function(e) {
		var that = this;
		$('a', this.$el).on('click', function(e) {
			that.changeClickBehaivor(e);
		});
	},
	changeClickBehaivor : function(e) {
		e.preventDefault();
		var $a = $(e.currentTarget);
		var href = $a.attr('href');
		this.$el.load(href);
	}
};
