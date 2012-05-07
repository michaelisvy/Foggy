
dojo.declare("richweb.AjaxHandler", null, {

	getResource : function(/* String */ url, /* String */ fragment, /* String */ targetId) {
	
		console.log('Ajax request to ', url);
		
		dojo.xhrGet({
		
			url: url,
			
			content: {'fragment': fragment},
			
			preventCache: true,
			
			load: function(response, ioArgs) {
				console.log("successful xhrGet", ioArgs);
				dojo.byId(targetId).innerHTML = response;
			},
			
			error: function(response){
				console.error("xhrGet failed");
				return response;
			}
		});
	}
});

