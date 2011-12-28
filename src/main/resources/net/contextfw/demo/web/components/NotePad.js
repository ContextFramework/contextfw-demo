var NotePad = Component.extend({
  init : function(id) {
    this._super(id);
    var self = this;
  },
  bindEvents: function() {}
});

var NoteEditor = Component.extend({
  init : function(id) {
    this._super(id);
    this.bindEvents();
  }, 
  bindEvents : function() {
    var self = this;
    this.el("store").click(function() {
      self.store();
    });
    this.el("close").click(function() {
      self.call("close")();
    });
    this.el("remove").click(function() {
      self.call("remove")();
    });
  },
  store : function() {
    this.call("store")(
        this.el("title").val(), this.el("content").val());
  }
});

var NoteList = Component.extend({
  init : function(id) {
    this._super(id);
    this.bindEvents();
  },
  bindEvents : function() {
    var self = this;
    this.el().find("li").click(function() {
      self.call("selectNote")($(this).attr("dataid"));
    });
  }
});