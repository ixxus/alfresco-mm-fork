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

/**
 * Paged document conversion options
 * 
 * @author Ray Gauss II
 */
public class PagedDocumentSourceOptions extends AbstractTransformationSourceOptions
{

    /** The page number in the source document */
    private Integer page;

    /**
     * Default constructor
     */
    public PagedDocumentSourceOptions()
    {
    }

    /**
     * Gets page number in the source document
     * 
     * @return the page number
     */
    public Integer getPage()
    {
        return page;
    }

    /**
     * Sets page number in the source document
     * 
     * @param page the page number
     */
    public void setPage(Integer page)
    {
        this.page = page;
    }

    @Override
    public TransformationSourceOptions mergedOptions(TransformationSourceOptions overridingOptions)
    {
        if (overridingOptions instanceof PagedDocumentSourceOptions)
        {
            PagedDocumentSourceOptions mergedOptions = (PagedDocumentSourceOptions) super
                    .mergedOptions(overridingOptions);

            if (((PagedDocumentSourceOptions) overridingOptions).getPage() != null)
            {
                mergedOptions.setPage(((PagedDocumentSourceOptions) overridingOptions).getPage());
            }
            return mergedOptions;
        }
        return null;
    }

}
