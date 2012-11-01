/*
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
package org.alfresco.repo.content.transform;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

import org.alfresco.repo.content.transform.ContentTransformer;

/**
 * Base implementation of <code>TransformationSourceOptions</code> which allows
 * for definition of command line options, an explicit
 * {@link ContentTransformer}, and a list of mimetypes these source options
 * would apply to.
 * 
 * @author Ray Gauss II
 */
public abstract class AbstractTransformationSourceOptions implements TransformationSourceOptions
{
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(AbstractTransformationSourceOptions.class);

    /** Command line options */
    private String commandOptions;

    /** An explicitly defined content transformer to use for the transformation */
    private ContentTransformer explicitContentTransformer;

    /**
     * The bean id of an explicitly defined content transformer to use for the
     * transformation
     */
    private String explicitContentTransformerId;

    /** The list of applicable mimetypes */
    private List<String> applicabledMimetypes;

    /**
     * Set the command string options
     * 
     * @param commandOptions the command string options
     */
    public void setCommandOptions(String commandOptions)
    {
        this.commandOptions = commandOptions;
    }

    /**
     * Get the command string options
     * 
     * @return String the command string options
     */
    public String getCommandOptions()
    {
        return commandOptions;
    }

    /*
     * (non-Javadoc)
     * @see org.alfresco.repo.content.transform.TransformationSourceOptions#
     * getExplicitContentTransformer()
     */
    public ContentTransformer getExplicitContentTransformer()
    {
        return explicitContentTransformer;
    }

    /**
     * Sets an explicitly defined content transformer to use for the
     * transformation
     * 
     * @param explicitContentTransformer the content transformer to use
     */
    public void setExplicitContentTransformer(ContentTransformer explicitContentTransformer)
    {
        this.explicitContentTransformer = explicitContentTransformer;
    }

    public String getExplicitContentTransformerId()
    {
        return explicitContentTransformerId;
    }

    public void setExplicitContentTransformerId(String explicitContentTransformerId)
    {
        this.explicitContentTransformerId = explicitContentTransformerId;
    }

    /**
     * Gets the list of applicable mimetypes
     * 
     * @return the applicable mimetypes
     */
    public List<String> getApplicabledMimetypes()
    {
        return applicabledMimetypes;
    }

    /**
     * Sets the list of applicable mimetypes
     * 
     * @param applicableMimetypes the applicable mimetypes
     */
    public void setApplicableMimetypes(List<String> applicabledMimetypes)
    {
        this.applicabledMimetypes = applicabledMimetypes;
    }

    public boolean isApplicableForMimetype(String mimetype)
    {
        if (mimetype != null && applicabledMimetypes != null) { return applicabledMimetypes.contains(mimetype); }
        return false;
    }

    public TransformationSourceOptions mergedOptions(TransformationSourceOptions overridingOptions)
    {
        try
        {
            AbstractTransformationSourceOptions mergedOptions = this.getClass().newInstance();
            mergedOptions.setApplicableMimetypes(this.getApplicabledMimetypes());
            mergedOptions.setExplicitContentTransformer(this.getExplicitContentTransformer());

            String mergedCommandOptions = this.getCommandOptions();
            if (overridingOptions instanceof AbstractTransformationSourceOptions)
            {
                if (((AbstractTransformationSourceOptions) overridingOptions).getCommandOptions() != null)
                {
                    mergedCommandOptions = mergedCommandOptions + " "
                            + ((AbstractTransformationSourceOptions) overridingOptions).getCommandOptions();
                }
            }
            mergedOptions.setCommandOptions(mergedCommandOptions);

            return mergedOptions;
        }
        catch (InstantiationException e)
        {
            logger.error(e);
        }
        catch (IllegalAccessException e)
        {
            logger.error(e);
        }
        return null;
    }

}
