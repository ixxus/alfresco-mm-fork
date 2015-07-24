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

import java.io.Serializable;
import java.util.Map;

import org.alfresco.service.cmr.repository.AbstractTransformationSourceOptions;
import org.alfresco.service.cmr.repository.SerializedTransformationOptionsAccessor;
import org.alfresco.service.cmr.repository.TransformationSourceOptions;
import org.alfresco.service.cmr.repository.TransformationSourceOptions.TransformationSourceOptionsSerializer;
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
    
    private String commandOptions;

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

	public String getCommandOptions() {
		return commandOptions;
	}

	public void setCommandOptions(String commandOptions) {
		this.commandOptions = commandOptions;
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
        return "[offset: " + getOffset() + ", duration: " + getDuration() + ", explicitTransformer: '', commandOptions: " + getCommandOptions() + "]";
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

	@Override
	public TransformationSourceOptionsSerializer getSerializer() {
		return new VideoSourceOptionsSerializer();
	}
}

class VideoSourceOptionsSerializer implements TransformationSourceOptionsSerializer {

	public static final String SERIAL_PARAM_OFFSET = "param_offset";
	public static final String SERIAL_PARAM_COMMAND = "param_command";
	public static final String SERIAL_PARAM_DURATION = "param_duration";
	
	@Override
	public void serialize(
			TransformationSourceOptions transformationSourceOptions,
			Map<String, Serializable> parameters) {
		if (parameters == null || transformationSourceOptions == null) {
			return;
		}
		VideoSourceOptions options = (VideoSourceOptions)transformationSourceOptions;
		parameters.put(SERIAL_PARAM_COMMAND, options.getCommandOptions());
		parameters.put(SERIAL_PARAM_OFFSET, options.getOffset());
		parameters.put(SERIAL_PARAM_DURATION, options.getDuration());
	}

	@Override
	public TransformationSourceOptions deserialize(
			SerializedTransformationOptionsAccessor serializedOptions) {
		
		String command = serializedOptions.getCheckedParam(SERIAL_PARAM_COMMAND, String.class);
		String offset = serializedOptions.getCheckedParam(SERIAL_PARAM_DURATION, String.class);
		String duration = serializedOptions.getCheckedParam(SERIAL_PARAM_DURATION, String.class);
		
		VideoSourceOptions options = new VideoSourceOptions();
		options.setCommandOptions(command);
		options.setDuration(duration);
		options.setOffset(offset);
		
		return options;
	}
	
}