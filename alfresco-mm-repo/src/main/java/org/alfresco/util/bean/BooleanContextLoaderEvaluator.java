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

public class BooleanContextLoaderEvaluator extends AbstractClasspathContextLoaderEvaluator
{
	
	private static final String PLACE_HOLDER_REGEX = "\\$\\{.*\\}";
	private boolean loadContext = true;
	
	/**
	 * Sets whether or not to load the context
	 * 
	 * @param loadContext Load context, yes or no
	 */
	public void setLoadContext(String loadContext)
	{
		if (loadContext != null && !loadContext.matches(PLACE_HOLDER_REGEX))
		{
			this.loadContext = new Boolean(loadContext);
		}
	}

	/* (non-Javadoc)
	 * @see org.alfresco.util.bean.AbstractClasspathContextLoaderEvaluator#loadContext()
	 */
	public boolean loadContext()
	{
		return loadContext;
	}

}
