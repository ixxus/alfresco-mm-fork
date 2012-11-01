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

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Base class for classpath context loader evaluators.
 * 
 * @author rgauss
 */
public abstract class AbstractClasspathContextLoaderEvaluator implements ContextLoaderEvaluator, ApplicationContextAware 
{
	
	protected ApplicationContext applicationContext;
	protected String contextClasspath;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
	{
		this.applicationContext = applicationContext;
	}

	/**
	 * Sets the string classpath of the context file.
	 * 
	 * @param contextClasspath
	 */
	public void setContextClasspath(String contextClasspath)
	{
		this.contextClasspath = contextClasspath;
	}
	
	/* (non-Javadoc)
	 * @see org.alfresco.util.bean.ContextLoaderEvaluator#getContextResource()
	 */
	public Resource getContextResource()
	{
		return new ClassPathResource(contextClasspath);
	}
	
	/* (non-Javadoc)
	 * @see org.alfresco.util.bean.ContextLoaderEvaluator#loadContext()
	 */
	public abstract boolean loadContext();
	
}
