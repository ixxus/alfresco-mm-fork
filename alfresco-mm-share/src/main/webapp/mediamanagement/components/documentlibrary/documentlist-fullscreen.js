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
 * Enables full-screen document libraries
 *
 * @namespace Alfresco
 * @class Alfresco.DocumentListFullScreen
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
   
   /**
    * FullScreen constructor.
    *
    * @return {Alfresco.DocumentListFullScreen} The new full screen instance
    * @constructor
    */
   Alfresco.DocumentListFullScreen = function()
   {
      return this;
   };
   
   /**
    * Toggles full-screen mode for the current view renderer
    *
    * @method toggleFullScreen
    * @param scope {object} The DocumentList object
    */
   Alfresco.DocumentListFullScreen.prototype.toggleFullScreen = function DL_FS_toggleFullScreen(scope)
   {
      if (scope != null)
      {
         if (!document.fullscreen && !document.mozFullScreen && !document.webkitFullScreen)
         {
            this.requestFullScreen(scope);
         }
         else
         {
            this.cancelFullScreen();
         }
      }
   };
   
   /**
    * Enters full-screen mode for the current view renderer
    *
    * @method requestFullScreen
    * @param scope {object} The DocumentList object
    */
   Alfresco.DocumentListFullScreen.prototype.requestFullScreen = function DL_FS_requestFullScreen(scope)
   {
      var viewRendererInstance = scope.viewRenderers[scope.options.viewRendererName];
      var container = Dom.get(scope.id + viewRendererInstance.parentElementIdSuffix);
      if (container.requestFullscreen || container.mozRequestFullScreen || container.webkitRequestFullScreen)
      {
         Dom.addClass(container, 'alf-fullscreen');
         Dom.addClass(container, 'alf-entering-true-fullscreen');
      }
      if (container.requestFullscreen)
      {
         container.requestFullscreen();
      }
      else if (container.mozRequestFullScreen)
      {
         container.mozRequestFullScreen();
      }
      else if (container.webkitRequestFullScreen)
      {
         container.webkitRequestFullScreen(Element.ALLOW_KEYBOARD_INPUT);
      }
      else
      {
         this.toggleFullWindow(scope);
      }
   };
   
   /**
    * Exits full-screen mode for the current view renderer
    *
    * @method cancelFullScreen
    */
   Alfresco.DocumentListFullScreen.prototype.cancelFullScreen = function DL_FS_cancelFullScreen()
   {
      if (document.exitFullscreen)
      {
         document.exitFullscreen();
      }
      else if (document.mozCancelFullScreen)
      {
         document.mozCancelFullScreen();
      }
      else if (document.webkitCancelFullScreen)
      {
         document.webkitCancelFullScreen();
      }
      else
      {
         this.toggleFullWindow(scope);
      }
   };
   
   /**
    * Handles changes to the full screen mode
    *
    * @method onFullScreenChange
    * @param scope {object} The DocumentList object
    */
   Alfresco.DocumentListFullScreen.prototype.onFullScreenChange = function DL_FS_onFullScreenChange(scope)
   {
      if (scope != null)
      {
         var viewRendererInstance = scope.viewRenderers[scope.options.viewRendererName];
         var container = Dom.get(scope.id + viewRendererInstance.parentElementIdSuffix);
         if (Dom.hasClass(container, 'alf-entering-true-fullscreen'))
         {
            Dom.removeClass(container, 'alf-entering-true-fullscreen');
            // Let resizing take place then add the true-fullscreen class
            setTimeout(function()
            {
               Dom.addClass(container, 'alf-true-fullscreen');
            }, 1000);
         }
         else
         {
            if (Dom.hasClass(container, 'alf-true-fullscreen'))
            {
               if (Dom.hasClass(container, 'alf-fullscreen'))
               {
                  // Exiting true fullscreen complete
                  Dom.removeClass(container, 'alf-fullscreen');
                  Dom.removeClass(container, 'alf-true-fullscreen');
                  if (viewRendererInstance.currentResizeCallback)
                  {
                     viewRendererInstance.currentResizeCallback();
                  }
               }
            }
            else
            {
               // We've probably been programatically called in fullwindow mode
               if (!Dom.hasClass(container, 'alf-fullscreen'))
               {
                  Dom.addClass(container, 'alf-fullscreen');
               }
               else
               {
                  Dom.removeClass(container, 'alf-fullscreen');
               }
               if (viewRendererInstance.currentResizeCallback)
               {
                  viewRendererInstance.currentResizeCallback();
               }
            }
         }
      }
   };
   
   /**
    * Toggles full-window mode for the current view renderer for browsers that don't support full-screen
    *
    * @method toggleFullWindow
    * @param scope {object} The DocumentList object
    */
   Alfresco.DocumentListFullScreen.prototype.toggleFullWindow = function DL_FS_toggleFullWindow(scope)
   {
      if (scope != null)
      {
         var viewRendererInstance = scope.viewRenderers[scope.options.viewRendererName];
         var pageContainer = Dom.get('Share');
         var container = Dom.get(scope.id + viewRendererInstance.parentElementIdSuffix);
         if (!Dom.hasClass(pageContainer, 'alf-fullwindow'))
         {
            Dom.addClass(pageContainer, 'alf-fullwindow');
            var doclistFullScreen = this;
            scope.escKeyListener = function(e)
            {
               if (e.keyCode == 27) {
                  doclistFullScreen.toggleFullWindow(scope);
               }
            }
            document.addEventListener("keydown", scope.escKeyListener, false);
         }
         else
         {
            Dom.removeClass(pageContainer, 'alf-fullwindow');
            if (scope.escKeyListener)
            {
               document.removeEventListener("keydown", scope.escKeyListener, false);
            }
         }
         this.onFullScreenChange(scope);
      }
   }
   
})();

