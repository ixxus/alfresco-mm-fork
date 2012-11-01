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
package org.alfresco.util.bean;

import org.springframework.core.io.Resource;

/**
 * Interface responsible for determining whether or not to load a particular Spring
 * context file.
 * 
 * @author rgauss
 *
 */
public interface ContextLoaderEvaluator
{
	
	/**
	 * The context resource to be evaluated.
	 * 
	 * @return the context resource
	 */
	public Resource getContextResource();

	/**
	 * Whether or not to load the context
	 * 
	 * @return whether to load the context
	 */
	public boolean loadContext();
	
}
