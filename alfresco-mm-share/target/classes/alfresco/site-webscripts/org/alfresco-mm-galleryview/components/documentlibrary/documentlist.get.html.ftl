<@markup id="galleryViewTemplates" target="documentListContainer" action="after">
   <div id="${args.htmlid}-gallery" class="alf-gallery documents"></div>
   <div id="${args.htmlid}-gallery-empty" class="hidden">
      <div class="yui-dt-liner"></div>
   </div>
   <div id="${args.htmlid}-gallery-item-template" class="alf-gallery-item hidden">
      <div class="alf-gallery-item-thumbnail">
         <div class="alf-header">
            <div class="alf-select"></div>
               <a href="javascript:void(0)" class="alf-show-detail">&nbsp;</a>
         </div>
         <div class="alf-label"></div>
      </div>
      <div class="alf-detail" style="display: none;">
	      <div class="bd">
	          <div class="alf-detail-thumbnail"></div>
	          <div class="alf-status"></div>
	          <div class="alf-actions"></div>
	          <div style="clear: both;"></div>
		      <div class="alf-description"></div>
          </div>
      </div>
   </div>
   <script type="text/javascript">//<![CDATA[
   		// Create a new gallery view instance when we hear the postSetupViewRenderers event has fired
		YAHOO.Bubbling.subscribe("postSetupViewRenderers", function(layer, args) {
		   var scope = args[1].scope;
		   var galleryViewRenderer = new Alfresco.DocumentListGalleryViewRenderer("gallery", ${preferences.galleryColumns!7});
		   scope.registerViewRenderer(galleryViewRenderer);
		});
   //]]></script>
</@>
<@markup id="galleryViewSlider" target="documentListSortSelect" action="after">
	<div id="${args.htmlid}-gallery-slider" class="alf-gallery-slider hidden">
     	<div class="alf-gallery-slider-small"><img src="${url.context}/res/mediamanagement/components/documentlibrary/images/gallery-size-small-16.png"></div>
         <div id="${args.htmlid}-gallery-slider-bg" class="yui-h-slider alf-gallery-slider-bg"> 
            <div id="${args.htmlid}-gallery-slider-thumb" class="yui-slider-thumb alf-gallery-slider-thumb"><img src="${url.context}/res/mediamanagement/components/documentlibrary/images/thumb-n.png"></div> 
         </div>
         <div class="alf-gallery-slider-large"><img src="${url.context}/res/mediamanagement/components/documentlibrary/images/gallery-size-large-16.png"></div>
     </div>
</@>
