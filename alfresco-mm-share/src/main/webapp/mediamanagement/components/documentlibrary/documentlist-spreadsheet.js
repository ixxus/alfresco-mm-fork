/**
 * Copyright (C) 2005-2012 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * @module DocumentLibrary
 */

/**
 * Spreadsheet view extension of DocumentListViewRenderer component.
 *
 * @namespace Alfresco
 * @class Alfresco.DocumentListSpreadsheetViewRenderer
 * @extends Alfresco.DocumentListViewRenderer
 */
(function()
{
   /**
    * YUI Library aliases
    */
   var Dom = YAHOO.util.Dom,
       Event = YAHOO.util.Event,
       Anim = YAHOO.util.Anim;
   
   /**
    * Alfresco Slingshot aliases
    */
   var $html = Alfresco.util.encodeHTML,
      $isValueSet = Alfresco.util.isValueSet;
   
   var DND_CONTAINER_ID = 'ygddfdiv';
   
   /**
    * SpreadsheetViewRenderer constructor.
    *
    * @param name {String} The name of the SpreadsheetViewRenderer
    * @return {Alfresco.DocumentListSpreadsheetViewRenderer} The new SpreadsheetViewRenderer instance
    * @constructor
    */
   Alfresco.DocumentListSpreadsheetViewRenderer = function(name)
   {
      Alfresco.DocumentListSpreadsheetViewRenderer.superclass.constructor.call(this, name);
      this.actionsColumnWidth = 160;
      this.actionsSplitAtModifier = 0;
      return this;
   };

   /**
    * Extend from Alfresco.DocumentListViewRenderer
    */
   YAHOO.extend(Alfresco.DocumentListSpreadsheetViewRenderer, Alfresco.DocumentListViewRenderer);
   
   /**
    * Constructs the table config dialog based on the current data table columns
    *
    * @method onShowDataTableConfigDialog
    * @param scope {Object} the DocumentList object
    * @param viewRendererInstance {Object} the DocumentListSpreadsheetViewRenderer
    * @param e {Event} the show event
    */
   Alfresco.DocumentListSpreadsheetViewRenderer.prototype.onShowDataTableConfigDialog = function DL_SVR_onShowDataTableConfigDialog(scope, viewRendererInstance, e)
   {
      YAHOO.util.Event.stopEvent(e);
      
      var handleColumnChange = function(clickEvent, columnKey)
      {
         viewRendererInstance.onShowToggleDataTableColumn(scope, clickEvent, columnKey);
      }
      
      if (viewRendererInstance.hasNewDataTableColumns)
      {
         var allColumns = scope.widgets.dataTable.getColumnSet().keys;
         var elPicker = Dom.get(scope.id + "-dt-dlg-picker");
         var elTemplateCol = document.createElement("div");
         Dom.addClass(elTemplateCol, "dt-dlg-pickercol");
         var elTemplateKey = elTemplateCol.appendChild(document.createElement("span"));
         Dom.addClass(elTemplateKey, "dt-dlg-pickerkey");
         var elTemplateBtns = elTemplateCol.appendChild(document.createElement("span"));
         Dom.addClass(elTemplateBtns, "dt-dlg-pickerbtns");
         
         var elColumn, elKey, oButtonGrp;
         for (var i = 0, l = allColumns.length; i < l; i++)
         {
            var oColumn = allColumns[i];
            
            elColumn = elTemplateCol.cloneNode(true);
            elKey = elColumn.firstChild;
            elKey.innerHTML = oColumn.label;
                 
            oButtonGrp = new YAHOO.widget.ButtonGroup(
            { 
               id: "data-table-column-toggle-"+i, 
               name: oColumn.getKey(), 
               container: elKey.nextSibling
            });
            oButtonGrp.addButtons([
               { label: "Show", value: "Show", checked: ((!oColumn.hidden)) },
               { label: "Hide", value: "Hide", checked: ((oColumn.hidden)) }
            ]);
            oButtonGrp.subscribe('valueChange', handleColumnChange, oColumn.getKey());
                                 
            elPicker.appendChild(elColumn);
          }
         
          viewRendererInstance.hasNewDataTableColumns = false;
      }
      this.dataTableConfigDialog.render();
      this.dataTableConfigDialog.show();
   };
   
   /**
    * Resets the table config dialog
    *
    * @method resetDataTableConfigDialog
    * @param scope {Object} the DocumentList object
    * @param viewRendererInstance {Object} the DocumentListSpreadsheetViewRenderer
    */
   Alfresco.DocumentListSpreadsheetViewRenderer.prototype.resetDataTableConfigDialog = function DL_SVR_resetDataTableConfigDialog(scope, viewRendererInstance)
   {
      viewRendererInstance.hasNewDataTableColumns = true;
      Dom.get(scope.id + "-dt-dlg-picker").innerHTML = "";
   };
   
   /**
    * Resets the table config dialog
    *
    * @method onShowToggleDataTableColumn
    * @param scope {Object} the DocumentList object
    * @param e {Event} the toggle event
    * @param columnKey {String} the key of the data column toggled
    */
   Alfresco.DocumentListSpreadsheetViewRenderer.prototype.onShowToggleDataTableColumn = function DL_SVR_onShowToggleDataTableColumn(scope, e, columnKey) {
      if (e.newValue === "Hide")
      {
          scope.widgets.dataTable.hideColumn(columnKey);
      }
      else {
          scope.widgets.dataTable.showColumn(columnKey);
      }
   };
   
   /**
    * Creates column definitions for any 'line' metadata templates defined for this view
    *
    * @method getConfiguredColumnDefinitions
    * @param scope {Object} the DocumentList object
    * @param oRecordSet {Object} the set of current DataSource records
    * @return {Object} a map of index keys to YUI DataTable Column definitions
    */
   Alfresco.DocumentListSpreadsheetViewRenderer.prototype.getConfiguredColumnDefinitions = function DL_SVR_getConfiguredColumnDefinitions(scope, oRecordSet)
   {
      var viewRendererInstance = this;
      
      var additionalColumnDefinitions = {}, oRecord, record;
      for (var i = 0; i < oRecordSet.getLength(); i++)
      {
         oRecord = oRecordSet.getRecord(i);
         record = oRecord.getData();
      
         var metadataTemplate = record.metadataTemplate;
         if (metadataTemplate)
         {
            if (YAHOO.lang.isArray(metadataTemplate.lines))
            {
               var html, line;
               for (var j = 0; j < metadataTemplate.lines.length; j++)
               {
                  line = metadataTemplate.lines[j];
                  if ((!$isValueSet(line.view) || line.view == this.metadataLineViewName) && !additionalColumnDefinitions[line.index])
                  {
                     var fnRenderCellMetadataLineRenderer = function DL_SVR_fnRenderCellMetadataLineRenderer(line)
                     {
                        return function (elCell, oRecord, oColumn, oData)
                        {
                           viewRendererInstance.renderCellMetadataLineRenderer(scope, elCell, oRecord, oColumn, oData, line);
                        }
                     }
                     // TODO: get localized label here
                     additionalColumnDefinitions[line.index] = 
                        { key: line.index, label: line.index, sortable: false, formatter: fnRenderCellMetadataLineRenderer(line) }
                  }
               }
            }
         }
      }
      return additionalColumnDefinitions;
   };
   
   /**
    * Gets configured and user-specified column definitions
    *
    * @method getAdditionalColumnDefinitions
    * @param scope {Object} the DocumentList object
    * @param oRecordSet {Object} the set of current DataSource records
    * @return {Object} a map of index keys to YUI DataTable Column definitions
    */
   Alfresco.DocumentListSpreadsheetViewRenderer.prototype.getAdditionalColumnDefinitions = function DL_SVR_getAdditionalColumnDefinitions(scope, oRecordSet)
   {
      var columnDefinitions = this.getConfiguredColumnDefinitions(scope, oRecordSet);
      
      // TODO: Allow user config and order preference
      // TODO: localized labels and from metadataTemplates here
      
      if (columnDefinitions['10'])
      {
         // Hide the long date and size
         columnDefinitions['10'] = null;
      }
      if (columnDefinitions['60'])
      {
         // Change the tags label
         columnDefinitions['60'].label = 'Camera Model';
         columnDefinitions['60'].className = ['alf-nowrap'];
      }
      if (columnDefinitions['61'])
      {
         // Change the tags label
         columnDefinitions['61'].label = 'Width';
         columnDefinitions['61'].className = ['alf-nowrap'];
      }
      if (columnDefinitions['62'])
      {
         // Change the tags label
         columnDefinitions['62'].label = 'Height';
         columnDefinitions['62'].className = ['alf-nowrap'];
      }
      if (columnDefinitions['70'])
      {
         // Change the size label
         columnDefinitions['70'].label = 'Size';
         columnDefinitions['70'].className = ['alf-nowrap'];
      }
      if (columnDefinitions['80'])
      {
         // Change the size label
         columnDefinitions['80'].label = 'Tags';
         columnDefinitions['80'].className = ['alf-editable', 'alf-nowrap'];
      }
      
      columnDefinitions['10'] =
         { key: 'cm_title', label: 'Title', sortable: false, formatter: this.renderCellGenericString, className: 'alf-nowrap', hidden: true };
      
      columnDefinitions['30'] =
         { key: 'cm_description', label: 'Description', sortable: false, formatter: this.renderCellGenericString };
      
      // Convoluted sort by index
      var columnDefinitionKeys = [],
         additionalColumnDefinitions = [];
      for (var key in columnDefinitions)
      {
         columnDefinitionKeys[columnDefinitionKeys.length] = key;
      }
      columnDefinitionKeys.sort();
      for (var i = 0; i < columnDefinitionKeys.length; i++)
      {
         additionalColumnDefinitions[i] = columnDefinitions[columnDefinitionKeys[i]];
      }
      
      return additionalColumnDefinitions;
   };
   
   /**
    * @see Alfresco.DocumentListViewRenderer.renderView
    */
   Alfresco.DocumentListSpreadsheetViewRenderer.prototype.renderView = function DL_SVR_renderView(scope, sRequest, oResponse, oPayload)
   {
      Alfresco.DocumentListSpreadsheetViewRenderer.superclass.renderView.call(this, scope, sRequest, oResponse, oPayload);
      // this.overrideDataTable(scope);
      
      var viewRendererInstance = this;
      
      scope.widgets.dataTable.set('draggableColumns', true);
      
      var container = Dom.get(scope.id + this.parentElementIdSuffix);
      var oRecordSet = scope.widgets.dataTable.getRecordSet();
      Dom.addClass(container, 'alf-spreadsheet');
      Dom.removeClass(Dom.get(scope.id + "-spreadsheet-config-button-container"), 'hidden');
      
      if (!this.originalColumnDefinitions)
      {
         // Save the originalColumnDefinitions
         this.originalColumnDefinitions = [];
         var columnDefinitions = scope.widgets.dataTable.getColumnSet().getDefinitions();
         for (var i = 0; i < columnDefinitions.length; i++)
         {
            this.originalColumnDefinitions[i] = columnDefinitions[i];
         }
      }
      
      // Insert the additionalColumns
      var insertIndex = this.originalColumnDefinitions.length - 1;
      var additionalColumnDefinitions = this.getAdditionalColumnDefinitions(scope, oRecordSet);
      for (var i = 0; i < additionalColumnDefinitions.length; i++)
      {
         if (additionalColumnDefinitions[i])
         {
            scope.widgets.dataTable.insertColumn(additionalColumnDefinitions[i], insertIndex + i);
         }
      }
      
      // Change the existing fileName column a bit
      var fileNameColumnDefinition = scope.widgets.dataTable.getColumn('fileName');
      fileNameColumnDefinition.className = 'alf-nowrap';
      // TODO get localized label here
      var newFileNameColumnLabel = 'Name';
      fileNameColumnDefinition.label = newFileNameColumnLabel;
      // Also set the HTML which has already been rendered
      var fileNameHeaderLiner = fileNameColumnDefinition.getThLinerEl();
      fileNameHeaderLiner.innerHTML = '<span class="yui-dt-label">' + newFileNameColumnLabel + '</span>';
      
      scope.widgets.dataTable.getColumn("status").width = "auto";      
      
      // Remove the thumbnail column
//      var thumbnailColumnDefinition = scope.widgets.dataTable.getColumn('thumbnail');
//      scope.widgets.dataTable.removeColumn(thumbnailColumnDefinition);
      
      var configDialogButton = Dom.get(scope.id + '-spreadsheet-config-button');
      var configDialogContainer = Dom.get(scope.id + '-spreadsheet-config-dialog');
      if (!this.dataTableConfigDialog)
      {
         // Set up the table config dialog panel and event listeners
         this.dataTableConfigDialog = new YAHOO.widget.Panel(configDialogContainer,
         { 
            visible:true, draggable:false, close:true, constraintoviewport: true, 
            width: '260px', underlay: 'none'
         });
         scope.widgets.dataTable.subscribe("columnReorderEvent", function(){
            Event.purgeElement(scope.id + "-dt-dlg-picker", true);
            viewRendererInstance.resetDataTableConfigDialog(scope, viewRendererInstance);
         }, this, true);
         
         Event.addListener(configDialogButton, 'click', function (e)
         {
            viewRendererInstance.onShowDataTableConfigDialog(scope, viewRendererInstance, e);
         });
      }
      viewRendererInstance.resetDataTableConfigDialog(scope, viewRendererInstance);
   };
   
   /**
    * @see Alfresco.DocumentListViewRenderer.renderCellThumbnail
    */
   Alfresco.DocumentListSpreadsheetViewRenderer.prototype.renderCellThumbnail = function DL_SVR_renderCellThumbnail(scope, elCell, oRecord, oColumn, oData)
   {
      var record = oRecord.getData(),
      node = record.jsNode,
      properties = node.properties,
      name = record.displayName,
      isContainer = node.isContainer,
      isLink = node.isLink,
      extn = name.substring(name.lastIndexOf(".")),
      imgId = node.nodeRef.nodeRef; // DD added
   
      var containerTarget; // This will only get set if thumbnail represents a container
      
      oColumn.width = 16;
      Dom.setStyle(elCell, "width", oColumn.width + "px");
      Dom.setStyle(elCell.parentNode, "width", oColumn.width + "px");
   
      if (isContainer)
      {
         elCell.innerHTML = '<span class="folder-small">' + (isLink ? '<span class="link"></span>' : '') + (scope.dragAndDropEnabled ? '<span class="droppable"></span>' : '') + Alfresco.DocumentList.generateFileFolderLinkMarkup(scope, record) + '<img id="' + imgId + '" src="' + Alfresco.constants.URL_RESCONTEXT + 'components/documentlibrary/images/folder-32.png" /></a>';
      }
      else
      {
         var id = scope.id + '-preview-' + oRecord.getId();
         elCell.innerHTML = '<span id="' + id + '" class="thumbnail">' + (isLink ? '<span class="link"></span>' : '') + Alfresco.DocumentList.generateFileFolderLinkMarkup(scope, record) + '<img id="' + imgId + '" src="' + Alfresco.DocumentList.generateThumbnailUrl(record) + '" alt="' + extn + '" title="' + $html(name) + '" /></a></span>';
         
         // Preview tooltip
         scope.previewTooltips.push(id);
      }
   };
   
   /**
    * @see Alfresco.DocumentListViewRenderer.destroyView
    */
   Alfresco.DocumentListSpreadsheetViewRenderer.prototype.destroyView = function DL_SVR_destroyView(scope, sRequest, oResponse, oPayload)
   {
      Alfresco.DocumentListSpreadsheetViewRenderer.superclass.destroyView.call(this, scope, sRequest, oResponse, oPayload);
      var container = Dom.get(scope.id + this.parentElementIdSuffix);
      Dom.removeClass(container, 'alf-spreadsheet');
      Dom.addClass(Dom.get(scope.id + "-spreadsheet-config-button-container"), 'hidden');
      
      if (this.originalColumnDefinitions)
      {
         // Put back the thumbnail column
//         for (var i=0; i < this.originalColumnDefinitions.length; i++)
//         {
//            if (this.originalColumnDefinitions[i].key == 'thumbnail')
//            {
//               scope.widgets.dataTable.insertColumn(this.originalColumnDefinitions[i], i);
//               break;
//            }
//         }
         
         // Remove the additional columns
         var currentColumnDefinitions = [];
         var currentColumnDefinitionsCall = scope.widgets.dataTable.getColumnSet().getDefinitions();
         // currentColumnDefinitionsCall changes during iteration
         for (var i = 0; i < currentColumnDefinitionsCall.length; i++)
         {
            currentColumnDefinitions[i] = currentColumnDefinitionsCall[i];
         }
         for (var i = 0; i < currentColumnDefinitions.length; i++)
         {
            var currentColumnDefinition = currentColumnDefinitions[i];
            var isOriginal = false;
            for (var j = 0; j < this.originalColumnDefinitions.length; j++)
            {
               if (currentColumnDefinition.key == this.originalColumnDefinitions[j].key)
               {
                  isOriginal = true;
                  break;
               }
            }
            if (!isOriginal)
            {
               currentColumnDefinitionsCall = scope.widgets.dataTable.getColumnSet().getDefinitions();
               // Need to get the new index
               for (var k = 0; k < currentColumnDefinitionsCall.length; k++)
               {
                  if (currentColumnDefinitionsCall[k].key == currentColumnDefinition.key)
                  {
                     scope.widgets.dataTable.removeColumn(k);
                     break;
                  }
               }
            }
         }
         
         // Reset fileName column label
         scope.widgets.dataTable.getColumn('fileName').label = 'Description';
         scope.widgets.dataTable.getColumn('fileName').className = '';
         this.originalColumnDefinitions = null;
      }
   };
   
   /**
    * Generic string custom datacell formatter
    *
    * @method renderCellGenericString
    * @param elCell {object}
    * @param oRecord {object}
    * @param oColumn {object}
    * @param oData {object|string}
    */
   Alfresco.DocumentListSpreadsheetViewRenderer.prototype.renderCellGenericString = function DL_SVR_renderCellGenericString(elCell, oRecord, oColumn, oData)
   {
      var record = oRecord.getData(),
         node = record.jsNode,
         properties = node.properties,
         propertyValue = properties[oColumn.field];
      if (propertyValue != null)
      {
         elCell.innerHTML = '<span class="alf-generic-property">' + propertyValue + '</span>';
      }
   };
   
   /**
    * Uses a defined metadata 'line' template and renderer as a custom datacell formatter
    *
    * @method renderCellMetadataLineRenderer
    * @param scope {object} The DocumentList object
    * @param elCell {object}
    * @param oRecord {object}
    * @param oColumn {object}
    * @param oData {object|string}
    * @param line {object} the metadata 'line' object
    */
   Alfresco.DocumentListSpreadsheetViewRenderer.prototype.renderCellMetadataLineRenderer = function DL_SVR_renderCellMetadataLineRenderer(scope, elCell, oRecord, oColumn, oData, line)
   {
      var desc = "", i, j,
         record = oRecord.getData(),
         jsNode = record.jsNode;
      
      var fnRenderTemplate = function fnRenderTemplate_substitute(p_key, p_value, p_meta)
      {
         var label = (p_meta !== null ? '<em>' + scope.msg(p_meta) + '</em>: ': ''),
            value = "";
             
         // render value from properties or custom renderer
         if (scope.renderers.hasOwnProperty(p_key) && typeof scope.renderers[p_key] === "function")
         {
            value = scope.renderers[p_key].call(scope, record, label);
         }
         else
         {
            if (jsNode.hasProperty(p_key))
            {
               value = '<span class="item">' + label + $html(jsNode.properties[p_key]) + '</span>';
            }
         }

         return value;
      };

      var html;
      if (!$isValueSet(line.view) || line.view == this.metadataLineViewName)
      {
         html = YAHOO.lang.substitute(line.template, scope.renderers, fnRenderTemplate);
         if ($isValueSet(html))
         {
            desc += '<div class="detail">' + html + '</div>';
         }
      }
      
      elCell.innerHTML = desc;
   };

})();

