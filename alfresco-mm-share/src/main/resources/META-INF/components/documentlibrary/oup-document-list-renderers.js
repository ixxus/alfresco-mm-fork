(function() {

	/*
	 * https://ixxusdev.atlassian.net/browse/IXSUPP-79
	 * Expected result: Description content appears unformatted in the Doc Lib view.
	 */
	if (Alfresco.DocumentList){    
		YAHOO.Bubbling.fire("registerRenderer", {
			
			propertyName : "plainDescription",
			renderer : function registerRendererDescription(record, label) {
				var description = record.jsNode.properties.description;
				var plainDescription = '';
				// Description non-blank?
	            if (description && description !== ""){
	            	var elem = document.createElement('div');
	                elem.innerHTML = description;
	                plainDescription = (elem.textContent===undefined) ? elem.innerText : elem.textContent;	                
				}
				
				return plainDescription;
			}
		});
	}

})();