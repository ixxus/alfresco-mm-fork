package org.alfresco.repo.thumbnail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.alfresco.repo.thumbnail.ThumbnailDefinition;
import org.alfresco.repo.thumbnail.ThumbnailRegistry;

/**
 * Extends <code>ThumbnailDefinition</code> to allow for self registering with
 * the <code>ThumbnailRegistry</code>
 * 
 * @author rgauss
 */
public class RegisteringThumbnailDefinition extends ThumbnailDefinition
{
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(RegisteringThumbnailDefinition.class);

    private boolean replaceDefinitions = true;

    private ThumbnailRegistry thumbnailRegistry;

    public void setThumbnailRegistry(ThumbnailRegistry thumbnailRegistry)
    {
        this.thumbnailRegistry = thumbnailRegistry;
    }

    public void init()
    {
        if (thumbnailRegistry != null)
        {
            ThumbnailDefinition existingDefinition = thumbnailRegistry.getThumbnailDefinition(this.getName());
            if (existingDefinition != null)
            {
                if (replaceDefinitions)
                {
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("Replacing " + this.getName() + " thumbnail definition");
                    }
                    thumbnailRegistry.addThumbnailDefinition(this);
                }
                else
                {
                    if (logger.isDebugEnabled())
                    {
                        logger.debug(this.getName()
                                + " thumbnail definition already registered and replacing disabled, leaving in place");
                    }
                }
            }
            else
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug("Registering " + this.getName() + " thumbnail definition");
                }
                thumbnailRegistry.addThumbnailDefinition(this);
            }
        }
    }

}
