var ProgressHolder = Component.extend({
  init : function(id) {
    this._super(id);
    var self = this;
    this.bindEvents();
  },
  bindEvents : function() {
    var self = this;
    this.el("add").click(function() {
      self.call("addProgress")(self.el("name").val(), self.el("duration").val());
    });
  }
});

var ProgressIndicator = Component.extend({
  init : function(id, progress) {
    this._super(id);
    this.bindEvents();
    this.updateProgress(progress);
  },
  bindEvents : function() {
  },
  updateProgress : function(progress) {
    this.el("progressbar").progressbar({
      value: progress*100
    });
  }
});