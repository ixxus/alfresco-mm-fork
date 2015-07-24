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

import org.alfresco.repo.content.transform.magick.ImageTransformationOptions;
import org.alfresco.service.cmr.repository.TransformationOptions;
import org.alfresco.service.cmr.repository.TransformationSourceOptions;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Extension of <code>TransformationOptions</code> which allows for the
 * specification of multiple source options and the ability to merge with a list
 * of other <code>TransformationOptions</code>.
 * <p>
 * NOTE: This should extend <code>TransformationOptions</code>!! This only
 * extends <code>ImageTransformationOptions</code> for compatibility with
 * current native Alfresco code.
 * 
 * @author Ray Gauss II
 */
public class SourceTargetTransformationOptions extends ImageTransformationOptions
{
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(SourceTargetTransformationOptions.class);

    /**
     * A list of source options further describing how the source should be
     * transformed based on its mimetype
     */
    private List<TransformationSourceOptions> sourceOptionsList;

    /**
     * Gets the list of source options further describing how the source should
     * be transformed based on its mimetype
     * 
     * @return the source mimetype to source options map
     */
    public Collection<TransformationSourceOptions> getSourceOptionsList()
    {
        return sourceOptionsList;
    }

    /**
     * Sets the list of source options further describing how the source should
     * be transformed based on its mimetype
     * 
     * @param sourceOptionsList the source mimetype to source options map
     */
    public void setSourceOptionsList(List<TransformationSourceOptions> sourceOptionsList)
    {
        this.sourceOptionsList = sourceOptionsList;
    }

    /**
     * Builds a copy of these transformation options, merging any given
     * <code>TransformationSourceOptions</code> with existing ones or adding
     * them if not already present.
     * <p>
     * This method is useful for obtaining a <code>TransformationOptions</code>
     * object nearly identical to an existing, configured one with only a few
     * changes. The resulting merged <code>TransformationOptions</code> object
     * can then be provided to the transformation service.
     * 
     * @param overridingSourceOptionsList
     * @return a <code>TransformationOptions</code> object with merged source
     *         options
     */
    public SourceTargetTransformationOptions mergedOptions(List<TransformationSourceOptions> overridingSourceOptionsList)
    {
        if (overridingSourceOptionsList != null)
        {
            SourceTargetTransformationOptions transformationOptions = (SourceTargetTransformationOptions) this.deepCopy();

            if (logger.isDebugEnabled())
            {
                for (TransformationSourceOptions transformationSourceOptions : getSourceOptionsList())
                {
                    logger.debug("original source option: "
                            + transformationSourceOptions.getClass().getSimpleName() + " - "
                            + transformationSourceOptions.toString());
                }
            }

            List<TransformationSourceOptions> mergedSourceOptionsList = new ArrayList<TransformationSourceOptions>();
            for (TransformationSourceOptions origSourceOptions : this.getSourceOptionsList())
            {
                for (TransformationSourceOptions overridingSourceOptions : overridingSourceOptionsList)
                {
                    if (overridingSourceOptions.getClass().equals(origSourceOptions.getClass()))
                    {
                        mergedSourceOptionsList.add(origSourceOptions.mergedOptions(overridingSourceOptions));
                    }
                }
            }

            if (logger.isDebugEnabled())
            {
                for (TransformationSourceOptions transformationSourceOptions : mergedSourceOptionsList)
                {
                    logger.debug("merged source option: " + transformationSourceOptions.getClass().getSimpleName()
                            + " - " + transformationSourceOptions.toString());
                }
            }
            transformationOptions.setSourceOptionsList(mergedSourceOptionsList);
            return transformationOptions;
        }
        return this;
    }
     
    @Override
    protected SourceTargetTransformationOptions clone() throws CloneNotSupportedException
    {
        SourceTargetTransformationOptions clone = (SourceTargetTransformationOptions) super.clone();
        return clone;
    }
    
    public void copyFrom(TransformationOptions origOptions)
    {
        super.copyFrom(origOptions);
        // Also copy the source options
        if (origOptions instanceof SourceTargetTransformationOptions)
        {
            this.setSourceOptionsList(((SourceTargetTransformationOptions) origOptions).getSourceOptionsList());
        }
    }

}
