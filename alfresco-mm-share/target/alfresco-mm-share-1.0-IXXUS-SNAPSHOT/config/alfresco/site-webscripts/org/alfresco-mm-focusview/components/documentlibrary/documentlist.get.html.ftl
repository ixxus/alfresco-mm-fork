<@markup id="focusViewTemplates" target="documentListContainer" action="after">
   <div id="${args.htmlid}-focus" class="alf-focus alf-gallery documents">
        <div id="${args.htmlid}-focus-main-content" class="alf-focus-main-content">
            <div id="${args.htmlid}-focus-carousel"></div>
            <div id="${args.htmlid}-focus-nav-main-previous" class="alf-focus-nav-button alf-focus-main-nav-button alf-focus-nav-prev">
                <img src="${page.url.context}/res/mediamanagement/components/documentlibrary/images/focus-main-nav-prev.png" />
            </div>
            <div id="${args.htmlid}-focus-nav-main-next" class="alf-focus-nav-button alf-focus-main-nav-button alf-focus-nav-next">
                <img src="${page.url.context}/res/mediamanagement/components/documentlibrary/images/focus-main-nav-next.png" />
            </div>
        </div>
        <div id="${args.htmlid}-focus-nav" class="alf-focus-nav">
            <div id="${args.htmlid}-focus-nav-handle" class="alf-focus-nav-handle"></div>
            <div id="${args.htmlid}-focus-nav-carousel"></div>
            <div id="${args.htmlid}-focus-nav-buttons" class="alf-focus-nav-buttons">
                <div id="${args.htmlid}-focus-nav-previous" class="alf-focus-nav-button alf-focus-nav-prev">
                    <img src="${page.url.context}/res/mediamanagement/components/documentlibrary/images/focus-content-nav-prev.png" />
                </div>
                <div id="${args.htmlid}-focus-nav-next" class="alf-focus-nav-button alf-focus-nav-next">
                    <img src="${page.url.context}/res/mediamanagement/components/documentlibrary/images/focus-content-nav-next.png" />
                </div>
            </div>
        </div>
   </div>
   <div id="${args.htmlid}-focus-nav-item-template" class="alf-focus-nav-item hidden">
      <div class="alf-focus-nav-item-thumbnail">
         <div class="alf-label"></div>
      </div>
   </div>
   <div id="${args.htmlid}-focus-item-template" class="alf-gallery-item hidden">
      <div class="alf-gallery-item-thumbnail">
         <div class="alf-header">
            <div class="alf-select"></div>
            <a href="javascript:void(0)" class="alf-show-detail">&nbsp;</a>
            <div class="alf-label"></div>
         </div>
      </div>
      <div class="alf-detail">
          <div class="bd">
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
            var focusViewRenderer = new Alfresco.DocumentListFocusViewRenderer("focus");
            scope.registerViewRenderer(focusViewRenderer);
        });
   //]]></script>
</@>
