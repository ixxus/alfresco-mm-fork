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

import org.alfresco.repo.content.transform.ContentTransformer;

/**
 * Defines any options and demarcations needed to describe the details of how
 * the source should be transformed.
 * <p>
 * An explicit <code>ContentTransformer</code> can also be defined that may be
 * used instead of letting the <code>ContentService</code> make the
 * determination.
 * <p>
 * See PagedDocumentSourceOptions for an example implementation that
 * describes the page number that should be used from the source content.
 * 
 * @author Ray Gauss II
 */
public interface TransformationSourceOptions
{

    /**
     * Gets whether or not these transformation source options apply for the
     * given mimetype
     * 
     * @param mimetype the mimetype of the source
     * @return if these transformation source options apply
     */
    public boolean isApplicableForMimetype(String mimetype);

    /**
     * A <code>ContentTransformer</code> that should be used for the
     * transformation
     * 
     * @return an explicitly defined content transformer
     */
    public ContentTransformer getExplicitContentTransformer();

    /**
     * Creates a new <code>TransformationSourceOptions</code> object from this
     * one, merging any non-null overriding fields in the given
     * <code>overridingOptions</code>
     * 
     * @param overridingOptions The options to override
     * @return a merged <code>TransformationSourceOptions</code> object
     */
    public TransformationSourceOptions mergedOptions(TransformationSourceOptions overridingOptions);

}
