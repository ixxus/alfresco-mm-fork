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

import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.content.transform.AbstractContentTransformerTest;
import org.alfresco.repo.content.transform.ContentTransformer;
import org.alfresco.repo.content.transform.ProxyContentTransformer;
import org.alfresco.repo.thumbnail.ThumbnailDefinition;
import org.alfresco.repo.thumbnail.ThumbnailRegistry;
import org.alfresco.service.cmr.repository.TransformationOptions;

/**
 * @see org.alfresco.repo.content.transform.ffmpeg.FfmpegContentTransformerWorker
 * 
 * @author Derek Hulley, Ray Gauss II
 */
public class FfmpegContentTransformerTest extends AbstractContentTransformerTest
{
    private static final String THUMBNAIL_DEFINITION_NAME = "doclib";
    private ProxyContentTransformer transformer;
    private TransformationOptions transformationOptions;
    
    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        
        transformer = (ProxyContentTransformer) ctx.getBean("transformer.Ffmpeg");
        ThumbnailRegistry thumbnailRegistry = (ThumbnailRegistry) ctx.getBean("thumbnailRegistry");
        ThumbnailDefinition thumbnailDefinition = thumbnailRegistry.getThumbnailDefinition(THUMBNAIL_DEFINITION_NAME);
        transformationOptions = thumbnailDefinition.getTransformationOptions();
    }
    
    /**
     * @return Returns the same transformer regardless - it is allowed
     */
    protected ContentTransformer getTransformer(String sourceMimetype, String targetMimetype)
    {
        return transformer;
    }
    
    public void testReliability() throws Exception
    {
        boolean reliability = transformer.isTransformable(
                MimetypeMap.MIMETYPE_VIDEO_MP4, -1, MimetypeMap.MIMETYPE_IMAGE_PNG, new TransformationOptions());
        assertEquals("Should not be supported with standard TransformationOptions", false, reliability);
        reliability = transformer.isTransformable(
                MimetypeMap.MIMETYPE_VIDEO_MP4, -1, MimetypeMap.MIMETYPE_IMAGE_PNG, transformationOptions);
        assertEquals("Should be supported with standard SourceTargetTransformationOptions", true, reliability);
    }
}
