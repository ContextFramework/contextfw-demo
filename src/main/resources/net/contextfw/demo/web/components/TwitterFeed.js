var TwitterFeed = Component.extend({
  init : function(id) {
    this._super(id);
    var self = this;
    self.call("showTweets")();
    this.el("form").submit(function() {
      self.call("setSearch", function() {
        self.el("search").slideUp();
        self.el("searching").slideDown();
      },
      function() {
        self.el("search").slideDown();
        self.el("searching").slideUp();
      })(self.el("search").val());
      return false;
    })
  },
  tweetsUpdated : function() {
    this.el().find(".feed").find("div").slideDown("slow");
  },
  bindEvents: function() {}
});