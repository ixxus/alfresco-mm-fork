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

/**
 * Image transformation options that apply to the desired final result of the
 * transformation
 * 
 * @author Roy Wetherall, Ray Gauss II
 */
public class SourceTargetImageTransformationOptions extends SourceTargetTransformationOptions
{

    @Override
    public SourceTargetImageTransformationOptions deepCopy() {
        SourceTargetImageTransformationOptions clonedOptions = (SourceTargetImageTransformationOptions) super.deepCopy();
        clonedOptions.copyFrom(this);
        return clonedOptions;
    }

    @Override
    public void copyFrom(TransformationOptions origOptions) {
        super.copyFrom(origOptions);
        if (origOptions != null)
        {
            if (origOptions instanceof ImageTransformationOptions)
            {
                // Clone ImageTransformationOptions
                this.setCommandOptions(((ImageTransformationOptions) origOptions).getCommandOptions());
                this.setResizeOptions(((ImageTransformationOptions) origOptions).getResizeOptions());
                this.setCropOptions(((ImageTransformationOptions) origOptions).getCropOptions());
                this.setAutoOrient(((ImageTransformationOptions) origOptions).isAutoOrient());
            }
        }
    }

}
