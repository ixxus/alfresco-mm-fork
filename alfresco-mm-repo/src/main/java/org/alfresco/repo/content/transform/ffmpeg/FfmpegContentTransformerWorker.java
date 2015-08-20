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
package org.alfresco.repo.content.transform.ffmpeg;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.repo.content.transform.SourceTargetImageTransformationOptions;
import org.alfresco.repo.content.transform.TransformationSourceOptions;
import org.alfresco.repo.content.transform.VideoSourceOptions;
import org.alfresco.repo.content.transform.magick.ImageResizeOptions;
import org.alfresco.repo.content.transform.magick.ImageTransformationOptions;
import org.alfresco.service.cmr.repository.ContentIOException;
import org.alfresco.service.cmr.repository.TransformationOptions;
import org.alfresco.util.exec.RuntimeExec;
import org.alfresco.util.exec.RuntimeExec.ExecutionResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Executes a statement to implement
 * 
 * @author Derek Hulley, Ray Gauss II
 */
public class FfmpegContentTransformerWorker extends AbstractFfmpegContentTransformerWorker
{

    private static final Log logger = LogFactory.getLog(FfmpegContentTransformerWorker.class);

    /** the system command executer */
    private RuntimeExec executer;

    /** the check command executer */
    private RuntimeExec checkCommand;

    /** the output from the check command */
    private String versionString;

    public FfmpegContentTransformerWorker()
    {
    }

    /**
     * Set the runtime command executer that must be executed in order to run
     * <b>FFMpeg</b>. Whether or not this is the full path to the convertCommand
     * or just the convertCommand itself depends the environment setup.
     * <p>
     * The command must contain the variables <code>${source}</code> and
     * <code>${target}</code>, which will be replaced by the names of the file
     * to be transformed and the name of the output file respectively.
     * 
     * <pre>
     *    convert ${source} ${target}
     * </pre>
     * 
     * @param executer the system command executer
     */
    public void setExecuter(RuntimeExec executer)
    {
        this.executer = executer;
    }

    /**
     * Sets the command that must be executed in order to retrieve version
     * information from the converting executable and thus test that the
     * executable itself is present.
     * 
     * @param checkCommand command executer to retrieve version information
     */
    public void setCheckCommand(RuntimeExec checkCommand)
    {
        this.checkCommand = checkCommand;
    }

    /**
     * Gets the version string captured from the check command.
     * 
     * @return the version string
     */
    public String getVersionString()
    {
        return this.versionString;
    }

    /**
     * Checks for the ffmpeg dependency, using the common
     * transformInternal(File, File) transformation method to check
     * that the sample image can be converted.
     */
    @Override
    public void afterPropertiesSet()
    {
        if (executer == null) { throw new AlfrescoRuntimeException("System runtime executer not set"); }
        super.afterPropertiesSet();
        if (isAvailable())
        {
            try
            {
                // On some platforms / versions, the -version command seems to
                // return an error code whilst still
                // returning output, so let's not worry about the exit code!
                ExecutionResult result = this.checkCommand.execute();
                this.versionString = result.getStdOut().trim();
            }
            catch (Throwable e)
            {
                setAvailable(false);
                logger.error(getClass().getSimpleName() + " not available: "
                        + (e.getMessage() != null ? e.getMessage() : ""));
                // debug so that we can trace the issue if required
                logger.debug(e);
            }

        }
    }

    protected void transformInternal(File sourceFile, File targetFile, TransformationOptions options,
            String sourceMimetype) throws ContentIOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("options class: " + options.getClass().getCanonicalName());
        }
        Map<String, String> properties = new HashMap<String, String>(5);
        // set properties
        if (options instanceof SourceTargetImageTransformationOptions)
        {
            SourceTargetImageTransformationOptions transformOptions = (SourceTargetImageTransformationOptions) options;
            String commandOptions = transformOptions.getCommandOptions();

            if (transformOptions instanceof ImageTransformationOptions)
            {
                ImageResizeOptions targetOptions = ((ImageTransformationOptions) transformOptions).getResizeOptions();
                if (targetOptions != null)
                {
                    commandOptions = commandOptions + " " + getTargetCommandOptions(targetOptions);
                }
            }

            if (transformOptions.getSourceOptionsList() != null)
            {
                for (TransformationSourceOptions sourceOptions : transformOptions.getSourceOptionsList())
                {
                    if ((sourceOptions instanceof VideoSourceOptions)
                            && sourceOptions.isApplicableForMimetype(sourceMimetype))
                    {
                        VideoSourceOptions videoSourceOptions = (VideoSourceOptions) sourceOptions;
                        commandOptions = commandOptions + " " + getSourceCommandOptions(videoSourceOptions);
                        if (videoSourceOptions.getOffset() != null && !videoSourceOptions.getOffset().equals(""))
                        {
                            properties.put(VAR_OFFSET, videoSourceOptions.getOffset());
                        }
                        break;
                    }
                }
            }
            properties.put(VAR_OPTIONS, commandOptions);
        }
        properties.put(VAR_SOURCE, sourceFile.getAbsolutePath());
        properties.put(VAR_TARGET, targetFile.getAbsolutePath());

        // execute the statement
        RuntimeExec.ExecutionResult result = executer.execute(properties);
        if (result.getExitValue() != 0 && result.getStdErr() != null && result.getStdErr().length() > 0)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("full error: \n" + result.getStdErr());
            }
            throw new ContentIOException("Failed to perform ffmpeg transformation: \n" + result);
        }
        // success
        if (logger.isDebugEnabled())
        {
            logger.debug("ffmpeg executed successfully: \n" + result);
        }
    }

    /**
     * Gets the ffmpeg command string for the image resize transform options
     * provided
     * <p>
     * Note: The current implementation assumes a 4:3 aspect ratio in the source
     * and that the <code>imageResizeOptions</code> given signify max width and
     * heights.
     * <p>
     * TODO: Future implementations should examine the source for the aspect ratio to
     * correctly create the thumbnail.
     * 
     * @param imageResizeOptions image resize options
     * @return String the ffmpeg command options
     */
    private String getTargetCommandOptions(ImageResizeOptions imageResizeOptions)
    {
        float aspectRatio = 1.3333f;

        StringBuilder builder = new StringBuilder(32);
        int width = 0;
        int height = 0;

        if (imageResizeOptions.getWidth() > 0 && imageResizeOptions.getHeight() > 0)
        {
            if (imageResizeOptions.getWidth() <= imageResizeOptions.getHeight())
            {
                width = imageResizeOptions.getWidth();
                height = Math.round(width * (1 / aspectRatio));
            }
            else if (imageResizeOptions.getWidth() > imageResizeOptions.getHeight())
            {
                height = imageResizeOptions.getHeight();
                width = Math.round(height * aspectRatio);
            }
        }

        if (width > 0 && height > 0)
        {
            if ((height % 2) != 0)
            {
                height = height - 1;
            }
            if ((width % 2) != 0)
            {
                width = width + 1;
            }
            builder.append("-s ");
            builder.append(width);
            builder.append("x");
            builder.append(height);
        }

        return builder.toString();
    }

    /**
     * Gets the ffmpeg command string for the video conversion transform options
     * provided
     * 
     * @param videoConversionOptions image resize options
     * @return String the ffmpeg command options
     */
    private String getSourceCommandOptions(VideoSourceOptions videoConversionOptions)
    {
        StringBuilder builder = new StringBuilder(32);

        if (videoConversionOptions.getCommandOptions() != null)
        {
            builder.append(videoConversionOptions.getCommandOptions());
        }

        if (videoConversionOptions.getDuration() != null)
        {
            builder.append("-t ");
            builder.append(videoConversionOptions.getDuration());
        }

        return builder.toString();
    }

}
