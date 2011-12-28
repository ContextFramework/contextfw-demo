var Updater = Component.extend({
  init : function(id) {
    this._super(id);
    var self = this;
    setInterval(function() {
      self.call("checkUpdates", null)();
    }, 5000);
  }
});