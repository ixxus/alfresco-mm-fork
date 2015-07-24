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
package org.alfresco.repo.thumbnail;

import java.io.Serializable;
import java.util.Map;

import org.alfresco.repo.content.transform.SourceTargetImageTransformationOptions;
import org.alfresco.repo.content.transform.VideoSourceOptions;
import org.alfresco.repo.rendition.executer.SourceTargetImageRenderingEngine;
import org.alfresco.service.cmr.repository.TransformationOptions;
import org.alfresco.service.cmr.repository.TransformationSourceOptions;
import org.alfresco.service.cmr.thumbnail.ThumbnailParentAssociationDetails;

public class SourceTargetThumbnailRenditionConvertor extends ThumbnailRenditionConvertor
{

    public Map<String, Serializable> convert(TransformationOptions transformationOptions,
            ThumbnailParentAssociationDetails assocDetails)
    {
        Map<String, Serializable> parameters = super.convert(transformationOptions, assocDetails);

        if (transformationOptions instanceof SourceTargetImageTransformationOptions)
        {
            SourceTargetImageTransformationOptions sourceTargetOptions = (SourceTargetImageTransformationOptions) transformationOptions;

            if (sourceTargetOptions.getSourceOptionsList() != null
                    && sourceTargetOptions.getSourceOptionsList().size() > 0)
            {
                for (TransformationSourceOptions sourceOptions : sourceTargetOptions.getSourceOptionsList())
                {
                    if (sourceOptions instanceof VideoSourceOptions)
                    {
                        VideoSourceOptions videoSourceOptions = (VideoSourceOptions) sourceOptions;
                        putParameterIfNotNull(SourceTargetImageRenderingEngine.PARAM_SOURCE_VIDEO_OFFSET,
                                videoSourceOptions.getOffset(), parameters);
                        putParameterIfNotNull(SourceTargetImageRenderingEngine.PARAM_SOURCE_VIDEO_DURATION,
                                videoSourceOptions.getDuration(), parameters);
                    }
                }
            }
        }

        return parameters;
    }

    private void putParameterIfNotNull(String paramName, Serializable paramValue, Map<String, Serializable> params)
    {
        if (paramValue != null)
        {
            params.put(paramName, paramValue);
        }
    }

}
