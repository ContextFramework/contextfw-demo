contextfw = {
	handle:     null,
	updateUrl:  null,
	refreshUrl: null,
	removeUrl:  null,
	ws:         null,
	wsIsOpen:   false,
	webSocketEnabled: false,
	context:    null,

	init: function(context, handle, webSocketEnabled) {
		this.handle = handle;
		this.context = context;
		this.updateUrl = context + "/contextfw-update/"+handle;
		this.refreshUrl = context + "/contextfw-refresh/"+handle;
		this.removeUrl = context + "/contextfw-remove/"+handle;
		this.refresh();
		setInterval(function() {contextfw.refresh()}, 1000*30);
		jQuery.ajaxSetup({ scriptCharset: "utf-8" ,contentType: "application/x-www-form-urlencoded; charset=UTF-8" });
		jQuery(window).unload( function () {
		    contextfw.unload(); 
		});
		
		if (webSocketEnabled && window.WebSocket != undefined) {
		  this.webSocketEnabled = true;
		  this.openWebSocket();
		} else {
	        setTimeout(function() {
	          contextfw.doAsync()  
	        }, 1500);
		}
	},
	
	doAsync: function() {
	  var self = this;
	  var ts = new Date().getTime();
	  jQuery.ajax({
	    type: "GET",
	    url: this.context+ "/async/"+ts+"?handle="+this.handle+"&ts="+ts,
	    dataType: "text",
	    complete: function(jqXHR, textStatus) {
	      if (textStatus == "success") {
	        self.doAsync();
	      } else {
	        setTimeout(function() {
              contextfw.doAsync()  
            }, 10000);
	      }
	    },
	    success: function(data, textStatus) {
	      contextfw._handleResponse(data)
	    }
	  });
	},

	refresh: function() {
		jQuery.get(this.refreshUrl, null);
	},

	unload: function() {
	    new Image().src = this.removeUrl;
	},
	
	_call: function(elId, method, args, beforeCall, afterCall) {
		
	    var self = this;
	    
		if (jQuery.isFunction(beforeCall)) beforeCall();
		
		var params = {};

		for(var i=0; i< args.length; i++) {
	      if (args[i] != null && typeof(args[i]) == "object") {
	    	  params["p"+i] = JSON.serialize(args[i]);
	      }
	      else {
	    	  params["p"+i] = args[i];
	      }
		}

		if (this.webSocketEnabled) {
		  var msg = this.handle 
		          + "&" + elId 
		          + "&"+method;
		  for (prop in params) {
		    msg = msg + "&" + escape(params[prop]);
		  }
		  
		  this.openWebSocket(function(ws) {
		    ws.send(msg);
		  });
		} else {
	        jQuery.post(this.updateUrl+"/"+elId+"/"+method, params, function(data, textStatus) {
	            contextfw._handleResponse(data);
	            if (jQuery.isFunction(afterCall)) afterCall();
	        }, "text");		  
		}
	},
	
	openWebSocket : function(callback) {
	  var self = this;
	  if (!this.wsIsOpen) {
        this.ws = new WebSocket('ws://'+window.location.hostname+':8083/websocket?handle=' + this.handle);
        this.ws.onopen = function() {
          if (jQuery.isFunction(callback)) {
            callback(self.ws);
          }
          self.wsIsOpen = true;
        };
        this.ws.onclose = function() { 
          self.wsIsOpen = false;
        };
        this.ws.onmessage = function(msg) { 
          contextfw._handleResponse(msg.data); 
          if (jQuery.isFunction(afterCall)) afterCall();
        };
	  } else {
	    if (jQuery.isFunction(callback)) {
          callback(self.ws);
        }
	  }
	},
	
	call: function(elId, method) {
		var args = []
		for(var i=2; i< arguments.length; i++) {
	      args.push(arguments[i]);
		}
		this._call(elId, method, args);
	},
	
	/*
	 * getResource(elementId, method, [args...], callback)
	 */
	getJson: function(elId, method) {

		var params = {}
		for(var i=2; i< arguments.length - 1; i++) {
	      if (arguments[i] != null && typeof(arguments[i]) == "object") {
	    	  params["p"+(i-2)] = JSON.serialize(arguments[i]);
	      }
	      else {
	    	  params["p"+(i-2)] = arguments[i];
	      }
		}

		var callback = arguments[arguments.length - 1];
		
		jQuery.post(this.updateUrl+"/"+elId+"/"+method, params, function(data, textStatus) {
			callback(JSON.parse(data));
	    }, "text");
	},
	

	_parseUpdate: function(data, tagName, callback) {
		  var current = data;
		  var pattern = new RegExp("<"+tagName+"(\\s+?.*?)?>");
		  var sIndex = current.search(pattern);
		  var startTagName = "<"+tagName;
		  var endTagName = "</"+tagName+">";
		  while (sIndex > 0) {
			  current = current.substring(sIndex);
			  fTagStart = current.indexOf(startTagName);
			  fTagEnd = current.indexOf(">");
			  endTag = current.indexOf(endTagName);
			  attrData = current.substring(fTagStart+startTagName.length, fTagEnd)

			  attributes = attrData.match(/\S+\s*?=\s*?".*?"/g);

			  attr = {};

			  if (attributes != undefined) {
			    for (i = 0; i < attributes.length; i++) {
					splits = attributes[i].split("=", 2);
					key = this._trim(splits[0]);
					value = this._trim(splits[1]);
					value = value.substr(1, value.length - 2);
					attr[key] = value;
				}
		      }

			  data = current.substring(fTagEnd+1, endTag);
			  current = current.substring(fTagEnd+endTagName.length);
			  sIndex = current.search(pattern);

		      callback(attr, data);
		  }
	  },

	  _trim: function(value) {
		  return value.replace(/^\s*/, "").replace(/\s*$/, "");
	  },

	_handleResponse: function(domDocument) {

		this._parseUpdate(domDocument, "replace", function(attr, data) {
			try {
			  jQuery("#"+ attr.id).replaceWith(data);
			}
			catch(err) {
				// Just ignore
			}
		});

		this._parseUpdate(domDocument, "replaceInner", function(attr, data) {
			try {
			  jQuery("#"+ attr.id).html(data);
			}
			catch(err) {
				// Just ignore
			}
		});
		
		this._parseUpdate(domDocument, "prepend", function(attr, data) {
          try {
            jQuery("#"+ attr.id).prepend(data);
          }
          catch(err) {
              // Just ignore
          }
      });

		this._parseUpdate(domDocument, "script", function(attr, script) {
			script = script.replace(/&gt;/g, '>');
			script = script.replace(/&lt;/g, '<');
			script = script.replace(/&amp;/g, '&');
			eval(script);
		});
	},

	_replaceInner: function replaceInner(id, html, mode) {
		try {
			if (mode == "fade") {
				jQuery(id).fadeOut("fast", function() {
					jQuery(id).html(html);
					jQuery(id).fadeIn("fast");
				});
			}
			else {
				jQuery(id).html(html);
			}
		}
		catch(err) {
			//alert(err);
		}
	}
};

/**
 * Serializes a form to json. If the name of form value ends with
 * "[]" then that is considered an array, thus allowing multiple values.
 * The string "[]" is stripped
 */
jQuery.fn.formToObject = function() {

  var h = function(o, rawName, value) {
    var dot = rawName.indexOf(".", rawName);
    if (dot != -1) {
      var subName = rawName.substring(0, dot);
      if (!o[subName]) {
        o[subName] = {};
      }
      h(o[subName], rawName.substring(dot+1), value);
    } else {
      if (rawName.match("\\[\\]$")=="[]") {
        var name = rawName.substr(0, rawName.length-2);
        if (!o[name]) {
          o[name] = [];
        }
        o[name].push(value || '');
      }
      else {
        o[rawName] = value || '';
      }
    }
  }
  var o = {};
  
  var a = this.serializeArray();
  jQuery.each(a, function() {
    h(o, this.name, this.value);
  });
  return o;
};

var c = {
  set : function(id, obj) {
    if (!this[id]) {
      this[id] = obj;
    }
  }
};
/* Defines js-side component */
(function() {
  Component = function(id) { this.init.apply(this, arguments); };
  Component.prototype.id = null;
  Component.prototype.init = function(id) {
      this.id = id;
  }
  // A convience function to access some element within the component
  Component.prototype.el = function(id) { 
      return jQuery("#" + this.id + (id != undefined ? "_" + id : ""));
  }
  // A convinience function to make remote calls.
  Component.prototype.call = function(method, beforeCall, afterCall) {
      var self = this;
      return function() {
          contextfw._call(self.id, method, arguments, beforeCall, afterCall);
      }
  }
  Component.extend = function(ext) {
      var s = function() { this.init.apply(this, arguments); };
      s.prototype._super = Component.prototype.init;
      for (prop in this.prototype) {
          s.prototype[prop] = this.prototype[prop];
      }
      for (prop in this) {
          s[prop] = this[prop];
      }
      if (ext != undefined) {
         for (prop in ext) {
            s.prototype[prop] = ext[prop];
         }
      }               
      return s;
  }
})();

var Templates = {};