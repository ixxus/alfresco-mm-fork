<@markup id="fullscreenExitButton" target="documentListContainer" action="before">
    <div class="alf-fullscreen-exit-button" class="hidden">
        <span class="yui-button">
            <span class="first-child">
                <button type="button" title="${msg("button.fullscreen.exit")}" id="${args.htmlid}-fullscreen-exit-button"></button>
            </span>
         </span>
    </div>
</@>
<@markup id="fullscreenEnterButton" target="documentListViewRendererSelect" action="before">
    <div class="alf-fullscreen-enter-button">
        <span class="yui-button">
            <span class="first-child">
                <button type="button" title="${msg("button.fullscreen.enter")}" id="${args.htmlid}-fullscreen-enter-button"></button>
            </span>
         </span>
    </div>
    <script type="text/javascript">//<![CDATA[
        var documentListFullScreen = new Alfresco.DocumentListFullScreen();
        YAHOO.Bubbling.subscribe("postSetupViewRenderers", function(layer, args) {
           var scope = args[1].scope;
           YAHOO.util.Event.addListener(Dom.get(scope.id + '-fullscreen-enter-button'), 'click', function(e)
           {
                documentListFullScreen.toggleFullScreen(scope);
           });
           YAHOO.util.Event.addListener(Dom.get(scope.id + '-fullscreen-exit-button'), 'click', function(e)
           {
                documentListFullScreen.toggleFullScreen(scope);
           });
           document.addEventListener("fullscreenchange", function()
           {
                documentListFullScreen.onFullScreenChange(scope);
           }, false);
           document.addEventListener("mozfullscreenchange", function()
           {
                documentListFullScreen.onFullScreenChange(scope);
           }, false);
           document.addEventListener("webkitfullscreenchange", function()
           {
                documentListFullScreen.onFullScreenChange(scope);
           }, false);
        });
   //]]></script>
</@>
