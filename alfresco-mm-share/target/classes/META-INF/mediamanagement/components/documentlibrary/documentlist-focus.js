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
 * Focus view extension of DocumentListViewRenderer component.
 *
 * @namespace Alfresco
 * @class Alfresco.DocumentListFocusViewRenderer
 * @extends Alfresco.DocumentListGalleryViewRenderer
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
   var $html = Alfresco.util.encodeHTML;
   
   var DND_CONTAINER_ID = 'ygddfdiv',
      DETAIL_PANEL_WIDTH = '377px',
      DETAIL_PANEL_OFFSET = [-355, 20],
      NAV_ITEM_WIDTH = 120,
      NAV_ITEM_PADDING_X = 4,
      NAV_PADDING_X = 52,
      NAV_HEIGHT = 112,
      HEADER_HEIGHT = 50,
      SHARE_DOCLIB_HEADER_FOOTER_HEIGHT = 345,
      FOCUS_WINDOW_RESIZE_CHECK_TIME = 50,
      FOCUS_WINDOW_RESIZE_MIN_TIME = 200;
   
   /**
    * FocusViewRenderer constructor.
    *
    * @param name {String} The name of the FocusViewRenderer
    * @return {Alfresco.DocumentListFocusViewRenderer} The new FocusViewRenderer instance
    * @constructor
    */
   Alfresco.DocumentListFocusViewRenderer = function(name, galleryColumns)
   {
      Alfresco.DocumentListFocusViewRenderer.superclass.constructor.call(this, name);
      this.parentElementIdSuffix = "-focus";
      this.windowResizeCheckTime = FOCUS_WINDOW_RESIZE_CHECK_TIME;
      this.windowResizeMinTime = FOCUS_WINDOW_RESIZE_MIN_TIME;
      return this;
   };

   /**
    * Extend from Alfresco.DocumentListViewRenderer
    */
   YAHOO.extend(Alfresco.DocumentListFocusViewRenderer, Alfresco.DocumentListGalleryViewRenderer);
   
   /**
    * Generates a focus item nav id from the given dataTable record
    *
    * @method getFocusNavItemId
    * @param scope {object} The DocumentList object
    * @param oRecord {object} data table record
    * @return {string} the focus nav item id
    */
   Alfresco.DocumentListFocusViewRenderer.prototype.getFocusNavItemId = function DL_FVR_getFocusNavItemId(oRecord)
   {
      if (this.documentList != null && oRecord != null)
      {
         return this.documentList.id + '-focus-nav-item-' + oRecord.getId();
      }
   };
   
   /**
    * Gets the focus nav item's thumbnail element from the given focus nav item
    *
    * @method getFocusNavItemThumbnailElement
    * @param focusNavItem {HTMLElement} The focus nav item object
    * @return {HTMLElement} the focus nav item thumbnail element
    */
   Alfresco.DocumentListFocusViewRenderer.prototype.getFocusNavItemThumbnailElement = function DL_FVR_getFocusNavItemThumbnailElement(focusNavItem)
   {
      if (focusNavItem != null)
      {
         return Dom.getFirstChild(focusNavItem);
      }
   };
   
   /**
    * Gets the focus nav item's label element from the given focus nav item
    *
    * @method getFocusNavItemLabelElement
    * @param focusNavItem {HTMLElement} The focus nav item object
    * @return {HTMLElement} the focus nav item label element
    */
   Alfresco.DocumentListFocusViewRenderer.prototype.getFocusNavItemLabelElement = function DL_FVR_getFocusNavItemLabelElement(focusNavItem)
   {
      if (focusNavItem != null)
      {
         var focusNavItemThumbnailDiv = this.getFocusNavItemThumbnailElement(focusNavItem);
         return Dom.getChildren(focusNavItemThumbnailDiv)[0];
      }
   };
   
   /**
    * Gets the focus carousel element ID
    *
    * @method getFocusCarouselContainerId
    * @param scope {Object} The document list object
    * @return {String} the focus carousel element ID
    */
   Alfresco.DocumentListFocusViewRenderer.prototype.getFocusCarouselContainerId = function DL_FVR_getFocusCarouselContainerId(scope)
   {
      if (scope != null)
      {
         return scope.id + '-focus-carousel';
      }
   };
   
   /**
    * Gets the focus nav element
    *
    * @method getFocusNavElement
    * @param scope {Object} The document list object
    * @return {HTMLElement} the focus nav element
    */
   Alfresco.DocumentListFocusViewRenderer.prototype.getFocusNavElement = function DL_FVR_getFocusNavElement(scope)
   {
      if (scope != null)
      {
         return Dom.get(scope.id + '-focus-nav');
      }
   };
   
   /**
    * Gets the focus nav carousel element ID
    *
    * @method getFocusNavCarouselContainerId
    * @param scope {Object} The document list object
    * @return {String} the focus nav carousel element ID
    */
   Alfresco.DocumentListFocusViewRenderer.prototype.getFocusNavCarouselContainerId = function DL_FVR_getFocusNavCarouselContainerId(scope)
   {
      if (scope != null)
      {
         return scope.id + '-focus-nav-carousel';
      }
   };
   
   /**
    * @see Alfresco.DocumentListGalleryViewRenderer.getRowItemLabelElement
    */
   Alfresco.DocumentListFocusViewRenderer.prototype.getRowItemLabelElement = function DL_FVR_getRowItemLabelElement(rowItem)
   {
      if (rowItem != null)
      {
         var focusItemHeaderDiv = this.getRowItemHeaderElement(rowItem);
         return Dom.getChildren(focusItemHeaderDiv)[2];
      }
   };
   
   /**
    * @see Alfresco.DocumentListGalleryViewRenderer.getRowItemActionsElement
    */
   Alfresco.DocumentListFocusViewRenderer.prototype.getRowItemActionsElement = function DL_FVR_getRowItemActionsElement(rowItem)
   {
      if (rowItem)
      {
         var galleryItemDetailDiv = this.getRowItemDetailElement(rowItem);
         return Dom.getChildren(Dom.getFirstChild(galleryItemDetailDiv))[1];
      }
   };
   
   /**
    * @see Alfresco.DocumentListGalleryViewRenderer.getRowItemStatusElement
    */
   Alfresco.DocumentListFocusViewRenderer.prototype.getRowItemStatusElement = function DL_FVR_getRowItemStatusElement(rowItem)
   {
      if (rowItem != null)
      {
         var galleryItemDetailDiv = this.getRowItemDetailElement(rowItem);
         return Dom.getChildren(Dom.getFirstChild(galleryItemDetailDiv))[0];
      }
   };
   
   /**
    * Gets the focus item's info button element from the given focus item
    *
    * @method getRowItemInfoButtonElement
    * @param focusItem {HTMLElement} The focus item object
    * @return {HTMLElement} the focus item info button element
    */
   Alfresco.DocumentListFocusViewRenderer.prototype.getRowItemInfoButtonElement = function DL_FVR_getRowItemInfoButtonElement(rowItem)
   {
      if (rowItem != null)
      {
         var focusItemHeaderDiv = this.getRowItemHeaderElement(rowItem);
         return Dom.getChildren(focusItemHeaderDiv)[1];
      }
   };
   
   /**
    * Handler for click of a focus content nav item
    *
    * @method onClickFocusContentNavItem
    * @param scope {Object} The document list object
    * @param index {Number} The index of the nav item
    */
   Alfresco.DocumentListFocusViewRenderer.prototype.onClickFocusContentNavItem = function DL_FVR_onClickFocusContentNavItem(scope, index)
   {
      if (scope != null && index != null)
      {
         scope.widgets.focusCarousel.scrollTo(index, false);
      }
   };
   
   /**
    * Handler for new focus main content item
    *
    * @method onFocusMainContentChanged
    * @param scope {Object} The document list object
    * @param index {Number} The index of the nav item
    */
   Alfresco.DocumentListFocusViewRenderer.prototype.onFocusMainContentChanged = function DL_FVR_onFocusMainContentChanged(scope, index)
   {
      if (scope != null)
      {
         var pageNum = scope.widgets.focusNavCarousel.getPageForItem(index) - 1;
         var currentPage = scope.widgets.focusNavCarousel.get('currentPage');
         if (pageNum != currentPage)
         {
            var firstOnPage = scope.widgets.focusNavCarousel.getFirstVisibleOnPage(pageNum + 1);
            scope.widgets.focusNavCarousel.scrollTo(firstOnPage, true);
         }
      }
   };
   
   /**
    * Handler for hiding header and content nav elements
    *
    * @method onToggleHeaderAndNav
    * @param scope {Object} The document list object
    */
   Alfresco.DocumentListFocusViewRenderer.prototype.onToggleHeaderAndNav = function DL_FVR_onToggleHeaderAndNav(scope)
   {
      if (scope != null)
      {
         var viewRendererInstance = this;
         var container = Dom.get(scope.id + this.parentElementIdSuffix);
         var currentPage = scope.widgets.focusCarousel.get('currentPage');
         var focusItem = Dom.getFirstChild(scope.widgets.focusCarousel.getElementForItem(currentPage));
         var header = this.getRowItemHeaderElement(focusItem);
         var focusNav = this.getFocusNavElement(scope);
         var headerAnim, navAnim;
         if (!Dom.hasClass(container, 'alf-focus-content-only'))
         {
            navAnim = new Anim(focusNav, { bottom: {to: -NAV_HEIGHT} }, 0.2);
            headerAnim = new Anim(header, { top: {to: -HEADER_HEIGHT} }, 0.2);
            headerAnim.onComplete.subscribe(function() { 
               Dom.addClass(container, 'alf-focus-content-only');
            });
         }
         else
         {
            Dom.setStyle(header, 'top', '-' + HEADER_HEIGHT + 'px');
            Dom.removeClass(container, 'alf-focus-content-only');
            navAnim = new Anim(focusNav, { bottom: {to: 0} }, 0.2);
            headerAnim = new Anim(header, { top: {to: 0} }, 0.2);
            headerAnim.onComplete.subscribe(function() { 
               // Manually set all other headers
               var i, j, otherFocusItemHeader, otherFocusItems = scope.widgets.focusCarousel.getElementForItems();
               for (i = 0, j = otherFocusItems.length; i < j; i++)
               {
                  otherFocusItemHeader = viewRendererInstance.getRowItemHeaderElement(Dom.getFirstChild(otherFocusItems[i]));
                  Dom.setStyle(otherFocusItemHeader, 'top', '0px');
               }
            });
         }
         headerAnim.animate();
         navAnim.animate();
      }
   };
   
   /**
    * @see Alfresco.DocumentListGalleryViewRenderer.setupRenderer
    */
   Alfresco.DocumentListFocusViewRenderer.prototype.setupRenderer = function DL_FVR_setupRenderer(scope)
   {
      Dom.addClass(scope.id + this.buttonElementIdSuffix, this.buttonCssClass);
      
      this.documentList = scope;
      
      var container = Dom.get(scope.id + this.parentElementIdSuffix);
      
      var viewRendererInstance = this;
      
      // TODO slide in and out of header and content nav here
      
      // On mouseover show the select checkbox and detail pull down
      Event.delegate(container, 'mouseover', function DL_GVR_onGalleryItemMouseOver(event, matchedEl, container)
      {
//         Dom.addClass(matchedEl, 'alf-hover');
         viewRendererInstance.onEventHighlightRow(scope, event, matchedEl);
      }, 'div.' + this.rowClassName, this);
      
      // On mouseout hide the select checkbox and detail pull down
//      Event.delegate(container, 'mouseout', function DL_GVR_onGalleryItemMouseOut(event, matchedEl, container)
//      {
//         Dom.removeClass(matchedEl, 'alf-hover');
//      }, 'div.' + this.rowClassName, this);
      
      // On click of detail pull down show detail panel
      Event.delegate(container, 'click', function DL_GVR_infoPopup(event, matchedEl, container)
      {
         viewRendererInstance.onShowGalleryItemDetail(scope, viewRendererInstance, event, matchedEl, container);
      }, '.alf-show-detail', this);
      
      // On click of handle toggle header and nav
      Event.addListener(Dom.get(scope.id + '-focus-nav-handle'), 'click', function(e)
      {
         viewRendererInstance.onToggleHeaderAndNav(scope);
      });
      
      YAHOO.Bubbling.on("selectedFilesChanged", function(layer, args) {
         this.onSelectedFilesChanged(scope);
      }, this);
   };
   
   /**
    * @see Alfresco.DocumentListGalleryViewRenderer.destroyView
    */
   Alfresco.DocumentListFocusViewRenderer.prototype.destroyView = function DL_GVR_destroyView(scope, sRequest, oResponse, oPayload)
   {
      this.restoreDataTable(scope);
      var focusCarousel = Dom.get(this.getFocusCarouselContainerId(scope));
      if (focusCarousel != null)
      {
         Dom.get(this.getFocusCarouselContainerId(scope)).innerHTML = '';
         Dom.get(this.getFocusNavCarouselContainerId(scope)).innerHTML = '';
      }
      Dom.addClass(Dom.get(scope.id + this.parentElementIdSuffix), 'hidden');
      var dndContainer = Dom.get(DND_CONTAINER_ID);
      Dom.removeClass(dndContainer, 'alf-focus-dragging');
      if (this.windowResizeCallback)
      {
         Event.removeListener(window, 'resize', this.windowResizeCallback);
      }
   };
   
   /**
    * @see Alfresco.DocumentListGalleryViewRenderer.renderView
    */
   Alfresco.DocumentListFocusViewRenderer.prototype.renderView = function DL_FVR_renderView(scope, sRequest, oResponse, oPayload)
   {
      this.overrideDataTable(scope);
      
      var viewRendererInstance = this;
      
      // Call the dataTable render to setup the dataTable.recordSet
      scope.widgets.dataTable.onDataReturnInitializeTable.call(
            scope.widgets.dataTable, sRequest, oResponse, oPayload);
      
      var container = Dom.get(scope.id + this.parentElementIdSuffix);
      var oRecordSet = scope.widgets.dataTable.getRecordSet();
      
      if (oRecordSet.getLength() == 0)
      {
         // No records, display the empty container and exit
         var emptyContainer = Dom.get(scope.id + this.parentElementEmptytIdSuffix);
         container.innerHTML = emptyContainer.innerHTML;
         Dom.removeClass(container, 'alf-gallery');
         Dom.removeClass(container, 'hidden');
         Dom.setStyle(container, 'height', 'auto');
         return;
      }
      
      // Hide the container while we build it
      Dom.setStyle(container, 'opacity', 0);
      
      var focusMainContent = Dom.get(scope.id + '-focus-main-content');
      var carouselContainerId = this.getFocusCarouselContainerId(scope);
      var carouselContainer = Dom.get(carouselContainerId);
      carouselContainer.innerHTML = '';
      var carouselList = document.createElement('ol');
      carouselContainer.appendChild(carouselList);
      
      var focusNav = Dom.get(scope.id + '-focus-nav');
      
      var navCarouselContainerId = this.getFocusNavCarouselContainerId(scope);
      var navCarouselContainer = Dom.get(navCarouselContainerId);
      navCarouselContainer.innerHTML = '';
      var navCarouselList = document.createElement('ol');
      navCarouselContainer.appendChild(navCarouselList);
      
      Dom.addClass(container, 'alf-gallery');
      Dom.addClass(container, 'alf-focus');
      Dom.removeClass(container, 'hidden');
      
      var containerWidth = parseInt(Dom.getComputedStyle(container, 'width'));
      // Set a 3:2 aspect ratio if we have room
      var itemHeight = Math.floor((2 / 3) * containerWidth);
      var maxItemHeight = Dom.getViewportHeight() - SHARE_DOCLIB_HEADER_FOOTER_HEIGHT;
      if (Dom.hasClass(container, 'alf-fullscreen'))
      {
         maxItemHeight = Dom.getViewportHeight();
      }
      if (itemHeight > maxItemHeight)
      {
         itemHeight = maxItemHeight;
      }
      
      var focusItemTemplate = Dom.get(scope.id + '-focus-item-template'),
         focusNavItemTemplate = Dom.get(scope.id + '-focus-nav-item-template'),
         focusItem = null,
         focusNavItem = null,
         focusListItem = null,
         focusNavListItem = null;
      
      var oRecord, record, i, j;
      for (i = 0, j = oRecordSet.getLength(); i < j; i++)
      {
         oRecord = oRecordSet.getRecord(i);
         record = oRecord.getData();
         
         focusListItem = document.createElement('li');
         carouselList.appendChild(focusListItem);
         
         focusNavListItem = document.createElement('li');
         navCarouselList.appendChild(focusNavListItem);
         
         // Append a focus item div
         var focusItemId = this.getRowItemId(oRecord);
         focusItem = focusItemTemplate.cloneNode(true);
         Dom.removeClass(focusItem, 'hidden');
         focusItem.setAttribute('id', focusItemId);
         focusItem.style.width = containerWidth + 'px';
         focusItem.style.height = itemHeight + 'px';
         focusListItem.appendChild(focusItem);
         
         // Append a focus item nav div
         var focusNavItemId = this.getFocusNavItemId(oRecord);
         focusNavItem = focusNavItemTemplate.cloneNode(true);
         Dom.removeClass(focusNavItem, 'hidden');
         focusNavItem.setAttribute('id', focusNavItemId);
         focusNavItem.style.width = NAV_ITEM_WIDTH + 'px';
         Event.addListener(focusNavItem, 'click', function(i)
               { return function(e)
                  { YAHOO.Bubbling.fire('focusNavItemClicked', { index: i }) };
               }(i), this, true);
         focusNavListItem.appendChild(focusNavItem);
         
         var galleryItemThumbnailDiv = this.getRowItemThumbnailElement(focusItem);
         var galleryItemHeaderDiv = this.getRowItemHeaderElement(focusItem);
         var galleryItemDetailDiv = this.getRowItemDetailElement(focusItem);
         var galleryItemActionsDiv = this.getRowItemActionsElement(focusItem);
         
         // Set the item header id
         galleryItemHeaderDiv.setAttribute('id', scope.id + '-item-header-' + oRecord.getId());
         
         // Suffix of the content actions div id must match the onEventHighlightRow target id
         galleryItemActionsDiv.setAttribute('id', scope.id + '-actions-' + focusItemId);
         
         // TODO - Document browse, media playback, and folder preview here
         // Render the thumbnail for the focus item
         var thumbnail = this.getThumbnail(
               scope, galleryItemThumbnailDiv, oRecord, null, null, '-focus-main-content');
         galleryItemThumbnailDiv.innerHTML += thumbnail.html;
         
         var focusNavItemThumbnailDiv = this.getFocusNavItemThumbnailElement(focusNavItem);
         
         // Render the thumbnail for the focus nav item
         this.renderCellThumbnail(
               scope,
               focusNavItemThumbnailDiv, 
               oRecord, 
               focusItem, 
               null,
               '',
               'doclib');
         // Add a simple display label
         this.getFocusNavItemLabelElement(focusNavItem).innerHTML = $html(oRecord.getData().displayName);
         
         // TODO - YUI carousel does not like variable widths
//         var focusNavItemWidth = parseInt(Dom.getComputedStyle(focusNavItemThumbnailDiv, 'width'));
//         focusItem.style.width = focusNavItemWidth;
         
         // Add the drag and drop
         var imgId = record.jsNode.nodeRef.nodeRef;
         var dnd = new Alfresco.DnD(imgId, scope);
         
         var galleryItemInfoButtonDiv = this.getRowItemInfoButtonElement(focusItem);
         // Create a YUI Panel with a relative context of its associated galleryItem
         galleryItemDetailDiv.panel = new YAHOO.widget.Panel(galleryItemDetailDiv,
         { 
            visible:false, draggable:false, close:false, constraintoviewport: true, 
            underlay: 'none', width: DETAIL_PANEL_WIDTH,
            context: [galleryItemInfoButtonDiv, 'tl', 'tl', null, DETAIL_PANEL_OFFSET]
         });
      };
      
      scope.widgets.focusCarousel = new YAHOO.widget.Carousel(this.getFocusCarouselContainerId(scope), {
         animation: { speed: 0.2 },
         numVisible: 1,
         navigation:
         {
            prev: scope.id + '-focus-nav-main-previous',
            next: scope.id + '-focus-nav-main-next'
         }
      });
      scope.widgets.focusCarousel.render();
      scope.widgets.focusCarousel.show();
      
      var numNavItemsVisible = Math.floor((containerWidth - (NAV_PADDING_X * 2)) / (NAV_ITEM_WIDTH + NAV_ITEM_PADDING_X));
      scope.widgets.focusNavCarousel = new YAHOO.widget.Carousel(this.getFocusNavCarouselContainerId(scope), {
         animation: { speed: 0.5 },
         numVisible: numNavItemsVisible,
         navigation:
         {
            prev: scope.id + '-focus-nav-previous',
            next: scope.id + '-focus-nav-next'
         }
      });
      scope.widgets.focusNavCarousel.render();
      scope.widgets.focusNavCarousel.show();
      
      // Add the content navigation handling
      YAHOO.Bubbling.on('focusNavItemClicked', function(layer, args)
      {
         this.onClickFocusContentNavItem(scope, args[1].index);
      }, this);
      scope.widgets.focusCarousel.on('pageChange', function (ev)
      {
         viewRendererInstance.onFocusMainContentChanged(scope, ev);
      });
      
      var dndContainer = Dom.get(DND_CONTAINER_ID);
      Dom.addClass(dndContainer, 'alf-focus-dragging');
      
      this.currentResizeCallback = function(e)
      {
         viewRendererInstance.resizeView(scope, sRequest, oResponse, oPayload);
      };
      this.setupWindowResizeListener();
      
      var fadeIn = new YAHOO.util.Anim(
            container, { opacity: {from: 0, to: 1 } }, 0.4, YAHOO.util.Easing.easeOut);
      fadeIn.animate();
   };
   
   Alfresco.DocumentListFocusViewRenderer.prototype.resizeView = function DL_FVR_resizeView(scope, sRequest, oResponse, oPayload)
   {
      var container = Dom.get(scope.id + this.parentElementIdSuffix);
      // Ignore if true full screen resizing due to conflicting firing times
      if (!Dom.hasClass(container, 'alf-true-fullscreen'))
      {
         var currentPage = scope.widgets.focusCarousel.get('currentPage');
         this.renderView(scope, sRequest, oResponse, oPayload);
         // Wait for carousel to render
         setTimeout(function()
         {
            scope.widgets.focusCarousel.scrollTo(currentPage, false);
         }, 50);
      }
   }
   
   /**
    * @see Alfresco.DocumentListGalleryViewRenderer.onSelectedFilesChanged
    */
   Alfresco.DocumentListFocusViewRenderer.prototype.onSelectedFilesChanged = function DL_FVR_onSelectedFilesChanged(scope)
   {
      // Set all selected decorators
      var oRecordSet = scope.widgets.dataTable.getRecordSet(), oRecord, record, jsNode, nodeRef, focusNavItem, i, j;
      var anySelected = false;
      for (i = 0, j = oRecordSet.getLength(); i < j; i++)
      {
         oRecord = oRecordSet.getRecord(i);
         jsNode = oRecord.getData("jsNode");
         nodeRef = jsNode.nodeRef;
         focusNavItem = Dom.get(this.getFocusNavItemId(oRecord));
         var isChecked = scope.selectedFiles[nodeRef];
         if (isChecked)
         {
            Dom.addClass(focusNavItem, 'alf-selected');
            anySelected = true;
         }
         else
         {
            Dom.removeClass(focusNavItem, 'alf-selected');
         }
      }
      
      // If any have been selected add indicator to the parent container
      var container = Dom.get(scope.id + this.parentElementIdSuffix);
      if (anySelected)
      {
       if (!Dom.hasClass(container, 'alf-selected'))
       {
            Dom.addClass(container, 'alf-selected');
       }
      }
      else
      {
         Dom.removeClass(container, 'alf-selected');
      }
   }
   
   /**
    * @see Alfresco.DocumentListGalleryViewRenderer.renderCellDescription
    */
   Alfresco.DocumentListFocusViewRenderer.prototype.renderCellDescription = function DL_FVR_renderCellDescription(scope, elCell, oRecord, oColumn, oData)
   {
      Alfresco.DocumentListGalleryViewRenderer.superclass.renderCellDescription.call(this, scope, elCell, oRecord, oColumn, oData);
      var focusItem = this.getRowItem(oRecord, elCell);
      // Check for null galleryItem due to ALF-15529
      if (focusItem != null)
      {
         // Copy description
         var focusItemDetailDescriptionElement = this.getRowItemDetailDescriptionElement(focusItem).innerHTML = elCell.innerHTML;
         // Clear out the table cell so there's no conflicting HTML IDs
         elCell.innerHTML = '';
         // Add a simple display label
         this.getRowItemLabelElement(focusItem).innerHTML = $html(oRecord.getData().displayName);
      }
   };

   
})();

