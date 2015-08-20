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
package org.alfresco.repo.rendition.executer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.repo.content.transform.ContentTransformer;
import org.alfresco.repo.content.transform.SourceTargetImageTransformationOptions;
import org.alfresco.repo.content.transform.TransformationSourceOptions;
import org.alfresco.repo.content.transform.VideoSourceOptions;
import org.alfresco.repo.content.transform.magick.ImageTransformationOptions;
import org.alfresco.service.cmr.repository.TransformationOptions;
import org.springframework.context.ApplicationContext;

/**
 * Extension of the <code>ImageRenderingEngine</code> which allows for
 * de-serializing additional parameters related to options applicable to the
 * source content.
 * <p>
 * This implementation supports the de-serialization of video parameters.
 * </p>
 * 
 * @author rgauss
 */
public class SourceTargetImageRenderingEngine extends ImageRenderingEngine
{
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(SourceTargetImageRenderingEngine.class);

    /**
     * Parameter for how far into the video clip to capture
     */
    public static final String PARAM_SOURCE_VIDEO_OFFSET = "source_video_offset";

    /**
     * Parameter for how long of a clip to capture
     */
    public static final String PARAM_SOURCE_VIDEO_DURATION = "source_video_duration";

    /**
     * Parameter for raw command line options
     */
    public static final String PARAM_SOURCE_VIDEO_COMMAND_OPTIONS = "source_video_command_options";

    /**
     * Parameter for the name of an explicit transformer to use for the
     * transformation
     */
    public static final String PARAM_SOURCE_VIDEO_EXPLICIT_TRANSFORMER_ID = "source_video_transformer_name";

    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.alfresco.repo.rendition.executer.ImageRenderingEngine#getTransformOptions
     * (
     * org.alfresco.repo.rendition.executer.AbstractRenderingEngine.RenderingContext
     * )
     */
    protected TransformationOptions getTransformOptions(RenderingContext context)
    {
        // first get standard image transform options from super
        ImageTransformationOptions imageTransformationOptions = 
                (ImageTransformationOptions) super.getTransformOptions(context);

        List<TransformationSourceOptions> allTransformationSourceOptions = getAllTransformationSourceOptions(context);

        if (allTransformationSourceOptions != null && allTransformationSourceOptions.size() > 0)
        {
            logger.debug("sourceOptions present, building SourceTargetImageTransformationOptions");
            SourceTargetImageTransformationOptions sourceTargetOptions = new SourceTargetImageTransformationOptions();
            sourceTargetOptions.copyFrom(imageTransformationOptions);

            sourceTargetOptions.setSourceOptionsList(allTransformationSourceOptions);

            return sourceTargetOptions;
        }
        else
        {
            logger.debug("no sourceOptions found, returning standard imageTransformationOptions");
            return imageTransformationOptions;
        }
    }

    /**
     * Gets any source options that may be needed for the transformation.
     * <p>
     * Extending implementations may add to this list for other source options
     * types
     * </p>
     * 
     * @param context The context
     * @return the list of <code>TransformationSourceOptions</code>
     */
    protected List<TransformationSourceOptions> getAllTransformationSourceOptions(RenderingContext context)
    {
        List<TransformationSourceOptions> allTransformationSourceOptions = new ArrayList<TransformationSourceOptions>();

        TransformationSourceOptions videoSourceOptions = getVideoTransformationSourceOptions(context);
        if (videoSourceOptions != null)
        {
            allTransformationSourceOptions.add(videoSourceOptions);
        }
        return allTransformationSourceOptions;
    }

    /**
     * Gets a @{link VideoSourceOptions} object from the parameters in the
     * <code>RenderingContext</code>
     * 
     * @param context The context
     * @return the video source options
     */
    protected TransformationSourceOptions getVideoTransformationSourceOptions(RenderingContext context)
    {
        String offset = context.getCheckedParam(PARAM_SOURCE_VIDEO_OFFSET, String.class);
        String duration = context.getCheckedParam(PARAM_SOURCE_VIDEO_DURATION, String.class);
        String commandOptions = context.getCheckedParam(PARAM_SOURCE_VIDEO_COMMAND_OPTIONS, String.class);
        String explicitTransformerId = context
                .getCheckedParam(PARAM_SOURCE_VIDEO_EXPLICIT_TRANSFORMER_ID, String.class);

        if ((offset == null || offset.equals("")) && (duration == null || duration.equals(""))
                && (commandOptions == null || commandOptions.equals(""))
                && (explicitTransformerId == null || explicitTransformerId.equals("")))
        {
            logger.debug("no video source options found");
            return null;
        }
        VideoSourceOptions videoSourceOptions = new VideoSourceOptions();
        if (offset != null && !offset.equals(""))
        {
            videoSourceOptions.setOffset(offset);
        }
        if (duration != null && !duration.equals(""))
        {
            videoSourceOptions.setDuration(duration);
        }
        if (commandOptions != null && !commandOptions.equals(""))
        {
            videoSourceOptions.setCommandOptions(commandOptions);
        }
        if (explicitTransformerId != null && !explicitTransformerId.equals(""))
        {
            ContentTransformer explicitTransformer = (ContentTransformer) this.applicationContext
                    .getBean(explicitTransformerId);
            videoSourceOptions.setExplicitContentTransformer(explicitTransformer);
        }
        return videoSourceOptions;
    }

}
