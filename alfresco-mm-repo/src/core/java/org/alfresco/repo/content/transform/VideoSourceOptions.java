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

/**
 * Video conversion options
 * 
 * @author Ray Gauss II
 */
public class VideoSourceOptions extends AbstractTransformationSourceOptions
{

    private static Log logger = LogFactory.getLog(VideoSourceOptions.class);

    private static final String VIDEO_MIMETYPE_PREFIX = "video/";

    /** The offset time code from which to start the transformation */
    private String offset;

    /** The duration of the target video after the transformation */
    private String duration;

    /**
     * Default constructor
     */
    public VideoSourceOptions()
    {
    }

    public String getOffset()
    {
        return offset;
    }

    public void setOffset(String offset)
    {
        this.offset = offset;
    }

    public String getDuration()
    {
        return duration;
    }

    public void setDuration(String duration)
    {
        this.duration = duration;
    }

    @Override
    public boolean isApplicableForMimetype(String mimetype)
    {
        if (mimetype != null && mimetype.startsWith(VIDEO_MIMETYPE_PREFIX)) { return true; }
        return super.isApplicableForMimetype(mimetype);
    }

    @Override
    public String toString()
    {
        return "[offset: " + getOffset() + ", duration: " + getDuration() + ", explicitTransformer: "
                + getExplicitContentTransformer() + ", commandOptions: " + getCommandOptions() + "]";
    }

    @Override
    public TransformationSourceOptions mergedOptions(TransformationSourceOptions overridingOptions)
    {
        if (overridingOptions instanceof VideoSourceOptions)
        {
            VideoSourceOptions mergedOptions = (VideoSourceOptions) super.mergedOptions(overridingOptions);

            if (((VideoSourceOptions) overridingOptions).getDuration() != null)
            {
                mergedOptions.setDuration(((VideoSourceOptions) overridingOptions).getDuration());
            }
            if (((VideoSourceOptions) overridingOptions).getOffset() != null)
            {
                mergedOptions.setOffset(((VideoSourceOptions) overridingOptions).getOffset());
            }
            if (logger.isDebugEnabled())
            {
                logger.debug("\n\toriginal options: " + this.toString() + "\n\tmerged options:   "
                        + mergedOptions.toString());
            }

            return mergedOptions;
        }
        return null;
    }

}
