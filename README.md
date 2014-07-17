# Backgroundgps

This is a PhoneGap/Cordova plugin that provides GPS locations via a background service.  The plugin is meant to serve as a template for authoring more PhoneGap plugins, but is functional.  The plugin currently only works on Android.

## To install from a local directory

`phonegap local plugin add <PATH TO>/com.salidasoftware.backgroundgps/`

or

`cordova plugin add <PATH TO>/com.salidasoftware.backgroundgps/`

## To install from github

`phonegap local plugin add https://github.com/aaronblondeau/com.salidasoftware.backgroundgps.git`

or

`cordova plugin add https://github.com/aaronblondeau/com.salidasoftware.backgroundgps.git`

## To uninstall

`phonegap local plugin remove com.salidasoftware.backgroundgps`

or

`cordova plugin remove com.salidasoftware.backgroundgps`

## To use

The plugin provides 3 functions : start, stop, status

Once the plugin has been started, the "success" callback will be called with every new location.  The location can also be polled using the status function.

	window.plugins.backgroundgps.start({}, function(success) {
	    $("#log").append("<div>Start callback : "+JSON.stringify(success)+"</div>");
	}, function(error){
	    $("#log").append("<div>Start error : "+error+"</div>");
	});

	window.plugins.backgroundgps.stop({}, function(success) {
	    $("#log").append("<div>Stop callback : "+JSON.stringify(success)+"</div>");
	}, function(error){
	    $("#log").append("<div>Stop error : "+error+"</div>");
	});

	window.plugins.backgroundgps.status({}, function(success) {
	    $("#log").prepend("<div>Status callback : "+JSON.stringify(success)+"</div>");
	}, function(error){
	    $("#log").prepend("<div>Status error : "+error+"</div>");
	});

# License

Copyright 2014 Aaron Blondeau, [Salida Software Inc](http://www.salidasoftware.com/)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)
    
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.