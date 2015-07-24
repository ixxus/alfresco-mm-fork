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
import java.io.InputStream;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.repo.content.filestore.FileContentWriter;
import org.alfresco.repo.content.transform.ContentTransformerHelper;
import org.alfresco.repo.content.transform.ContentTransformerWorker;
import org.alfresco.repo.content.transform.SourceTargetTransformationOptions;
import org.alfresco.service.cmr.repository.ContentIOException;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.TransformationOptions;
import org.alfresco.service.cmr.repository.TransformationSourceOptions;
import org.alfresco.util.TempFileProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Abstract helper for transformations based on <b>FFMpeg</b>
 * 
 * @author Derek Hulley, Ray Gauss II
 */
public abstract class AbstractFfmpegContentTransformerWorker extends ContentTransformerHelper implements
        ContentTransformerWorker, InitializingBean
{
    private static final Log logger = LogFactory.getLog(AbstractFfmpegContentTransformerWorker.class);

    /** the prefix for mimetypes supported by the transformer */
    public static final String MIMETYPE_VIDEO_PREFIX = "video/";

    public static final String MIMETYPE_IMAGE_PREFIX = "image/";

    public static final String VAR_OPTIONS = "options";

    /** offset variable name */
    public static final String VAR_OFFSET = "offset";

    /** source variable name */
    public static final String VAR_SOURCE = "source";

    /** target variable name */
    public static final String VAR_TARGET = "target";

    protected static final String DEFAULT_OFFSET = "00:00:00";

    private boolean available;

    public AbstractFfmpegContentTransformerWorker()
    {
        this.available = false;
    }

    /**
     * @return Returns true if the transformer is functioning otherwise false
     */
    public boolean isAvailable()
    {
        return available;
    }

    /**
     * Make the transformer available
     * 
     * @param available If the transformer is available or not
     */
    protected void setAvailable(boolean available)
    {
        this.available = available;
    }

    /**
     * Checks that ffmpeg is available, using the common transformation method to check
     * that the sample video can be converted.
     * <p>
     * If initialization is successful, then autoregistration takes place.
     */
    public void afterPropertiesSet()
    {
        if (getMimetypeService() == null) { throw new AlfrescoRuntimeException("MimetypeMap not present"); }
        try
        {
            // load, into memory the sample video
            String resourcePath = "org/alfresco/repo/content/transform/ffmpeg/test.mp4";
            InputStream imageStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
            if (imageStream == null) { throw new AlfrescoRuntimeException("Sample video not found: " + resourcePath); }
            // dump to a temp file
            File inputFile = TempFileProvider.createTempFile(getClass().getSimpleName() + "_init_source_", ".mp4");
            FileContentWriter writer = new FileContentWriter(inputFile);
            writer.putContent(imageStream);

            // create the output file
            File outputFile = TempFileProvider.createTempFile(getClass().getSimpleName() + "_init_target_", ".avi");
            if (logger.isDebugEnabled())
            {
                logger.debug("trying conversion from " + inputFile + " to " + outputFile);
            }
            // execute it
            transformInternal(inputFile, outputFile, new TransformationOptions(), "video/avi");

            // check that the file exists
            if (!outputFile.exists()) { throw new Exception("Video conversion failed: \n" + "   from: " + inputFile
                    + "\n" + "   to: " + outputFile); }
            // we can be sure that it works
            setAvailable(true);
        }
        catch (Throwable e)
        {
            logger.error(getClass().getSimpleName() + " not available: "
                    + (e.getMessage() != null ? e.getMessage() : ""));
            // debug so that we can trace the issue if required
            logger.debug(e);
        }
    }

    /**
     * Determines if the mimetype is supported by ffmpeg
     * 
     * @param mimetype the mimetype to check
     * @return Returns true if ffmpeg can handle the given image format
     */
    public static boolean isSupported(String mimetype)
    {
        if (mimetype.startsWith(MIMETYPE_VIDEO_PREFIX) || mimetype.startsWith(MIMETYPE_IMAGE_PREFIX)) { return true; }
        return false;
    }

    public boolean isTransformable(String sourceMimetype, String targetMimetype, TransformationOptions options)
    {
        if (logger.isDebugEnabled() && options != null)
        {
            logger.debug("checking support of sourceMimetype=" + sourceMimetype + " targetMimetype=" + targetMimetype
                    + " " + options.getClass().getCanonicalName() + "=" + options.toString());
        }
        if (!available)
        {
            logger.trace("transformer is marked as unavailable");
            return false;
        }
        if (options != null && options instanceof SourceTargetTransformationOptions)
        {
            boolean sourceFound = false;
            SourceTargetTransformationOptions sourceTargetOptions = (SourceTargetTransformationOptions) options;
            if (sourceTargetOptions.getSourceOptionsList() != null)
            {
                for (TransformationSourceOptions sourceOptions : sourceTargetOptions.getSourceOptionsList())
                {
                    if (sourceOptions.isApplicableForMimetype(sourceMimetype))
                    {
                        sourceFound = true;
                        logger.debug("found sourceOptions applicable for " + sourceMimetype + ":"
                                + sourceOptions.toString());
                        break;
                    }
                }
            }
            return (targetMimetype.startsWith(MIMETYPE_IMAGE_PREFIX) && sourceFound);
        }
        logger.trace("no applicable sourceOptions found");
        return false;
    }

    /**
     * Transform a file given the options
     * @throws Exception If the mimetypes are unknown, or the target file does not exist
     */
    public final void transform(ContentReader reader, ContentWriter writer, TransformationOptions options)
            throws Exception
    {
        // get mimetypes
        String sourceMimetype = getMimetype(reader);
        String targetMimetype = getMimetype(writer);

        // get the extensions to use
        String sourceExtension = getMimetypeService().getExtension(sourceMimetype);
        String targetExtension = getMimetypeService().getExtension(targetMimetype);
        if (sourceExtension == null || targetExtension == null) { throw new AlfrescoRuntimeException(
                "Unknown extensions for mimetypes: \n" + "   source mimetype: " + sourceMimetype + "\n"
                        + "   source extension: " + sourceExtension + "\n" + "   target mimetype: " + targetMimetype
                        + "\n" + "   target extension: " + targetExtension); }

        // create required temp files
        File sourceFile = TempFileProvider.createTempFile(getClass().getSimpleName() + "_source_", "."
                + sourceExtension);
        File targetFile = TempFileProvider.createTempFile(getClass().getSimpleName() + "_target_", "."
                + targetExtension);

        // pull reader file into source temp file
        reader.getContent(sourceFile);

        // transform the source temp file to the target temp file
        transformInternal(sourceFile, targetFile, options, reader.getMimetype());

        // check that the file was created
        if (!targetFile.exists()) { throw new ContentIOException("ffmpeg transformation failed to write output file"); }
        // upload the output image
        writer.putContent(targetFile);
        // done
        if (logger.isDebugEnabled())
        {
            logger.debug("Transformation completed: \n" + "   source: " + reader + "\n" + "   target: " + writer + "\n"
                    + "   options: " + options);
        }
    }

    /**
     * Transform the image content from the source file to the target file
     * 
     * @param sourceFile the source of the transformation
     * @param targetFile the target of the transformation
     * @param options the transformation options supported by ImageMagick
     * @param sourceMimetype The source mimetype
     * @throws Exception If the mimetypes are unknown, or the target file does not exist
     */
    protected abstract void transformInternal(File sourceFile, File targetFile, TransformationOptions options,
            String sourceMimetype) throws Exception;
}
