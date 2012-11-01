<@markup id="spreadsheetViewTemplates" target="documentListContainer" action="after">
   <script type="text/javascript">//<![CDATA[
        // Create a new spreadsheet view instance when we hear the postSetupViewRenderers event has fired
        YAHOO.Bubbling.subscribe("postSetupViewRenderers", function(layer, args) {
            var scope = args[1].scope;
            var spreadsheetViewRenderer = new Alfresco.DocumentListSpreadsheetViewRenderer("spreadsheet");
            scope.registerViewRenderer(spreadsheetViewRenderer);
        });
   //]]></script>
</@>
<@markup id="spreadsheetViewConfig" target="documentListSortSelect" action="after">
   <div id="${args.htmlid}-spreadsheet-config-button-container" class="alf-spreadsheet-config-button hidden">
        <span class="yui-button">
            <span class="first-child">
                <button type="button" title="${msg("button.spreadsheet.config")}" id="${args.htmlid}-spreadsheet-config-button"></button>
            </span>
         </span>
    </div>
   <div id="${args.htmlid}-spreadsheet-config-dialog" class="alf-spreadsheet-config-dialog">
       <div class="bd">
            <div class="hd">${msg("panel.header.spreadsheetConfig")}</div>
            <div id="${args.htmlid}-dt-dlg-picker"></div>
       </div>
   </div>
</@>
