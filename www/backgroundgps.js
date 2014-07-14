var backgroundgps = {};

backgroundgps.install = function () {
  //backgroundgps will be available at window.plugins.backgroundgps
  if (!window.plugins) {
    window.plugins = {};
  }

  window.plugins.backgroundgps = {
    //Nothing fancy here, just map the calls to the Backgroundgps Java class
  	start: function(options, successCallback, errorCallback) {
    	cordova.exec(successCallback, errorCallback, "Backgroundgps", "start", [options]);
  	},
  	stop: function(options, successCallback, errorCallback) {
    	cordova.exec(successCallback, errorCallback, "Backgroundgps", "stop", [options]);
  	},
    status: function(options, successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Backgroundgps", "status", [options]);
    }
  };
  return window.plugins.backgroundgps;
};

cordova.addConstructor(backgroundgps.install);