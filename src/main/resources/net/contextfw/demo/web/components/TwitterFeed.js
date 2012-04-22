var TwitterFeed = Component.extend({
  init : function(id) {
    this._super(id);
    var self = this;
    self.call("showTweets")();
    this.el("form").submit(function() {
      self.call("setSearch", function() {
        self.el("search").slideUp();
        self.el("searching").slideDown();
      })(self.el("search").val());
      return false;
    });
  },
  searchUpdated : function() {
    this.el("search").slideDown();
    this.el("searching").slideUp();
  },
  tweetsUpdated : function() {
    this.el().find(".feed").find("div").slideDown("slow");
  },
  bindEvents: function() {}
});