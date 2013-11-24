var categolj2 = {};

categolj2.AdminView = function() {
	new categolj2.TabView();
	var $tab = null;
	if (location.hash && !_.contains(location.hash, '/')) {
		$tab = $('#nav-tab a[href=' + location.hash + ']');
	}

	if ($tab && $tab.size() > 0) {
		$tab.tab('show');
	} else {
		$('#nav-tab a[href=#dashboard]').tab('show');
	}
};

categolj2.TabView = function() {
	this.$el = $('#nav-tab a');
	var that = this;
	this.$el.on('show.bs.tab', function(e) {
		that.tabShow(e);
	});
	this.$el.on('click', this.tabChanged);
};

categolj2.TabView.prototype = {
	tabShow : function(e) {
		var $a = $(e.currentTarget);
		var hash = $a.attr('href').substring(1);
		var viewName = hash.substring(0, 1).toUpperCase() + hash.substring(1)
				+ 'View';
		this.loadView(viewName);
	},
	tabChanged : function(e) {
		e.preventDefault();
		var $this = $(this);
		$this.tab('show');
		location.hash = $this.attr('href');
	},
	loadView : function(viewName) {
		switch (viewName) {
		case 'EntriesView':
			return new categolj2.EntriesView();
			break;
		case 'UsersView':
			return new categolj2.UsersView();
			break;
		default:
			break;
		}
	}
};