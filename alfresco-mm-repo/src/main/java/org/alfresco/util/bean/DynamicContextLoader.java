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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.GenericApplicationContext;

/**
 * Class which dynamically loads additional Spring contexts defined
 * in its list of {@link ContextLoaderEvaluator}s.
 * 
 * @author rgauss
 */
public class DynamicContextLoader implements ApplicationContextAware
{

	private static final Log logger = LogFactory.getLog(DynamicContextLoader.class);
	
	protected ApplicationContext applicationContext;
	protected List<ContextLoaderEvaluator> contextLoaderEvaluators;
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
	{
		this.applicationContext = applicationContext;
	}
	
	/**
	 * Sets the list of ContextLoaderEvaluators to evaluate
	 * 
	 * @param contextLoaderEvaluators The evaluators
	 */
	public void setContextLoaderEvaluators(List<ContextLoaderEvaluator> contextLoaderEvaluators)
	{
		this.contextLoaderEvaluators = contextLoaderEvaluators;
	}

	/**
	 * Iterates through the list of ContextLoaderEvaluators and loads the additional
	 * contexts if appropriate.
	 */
	public void init()
	{
		GenericApplicationContext childContext = new GenericApplicationContext(applicationContext);
		XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(childContext);
		boolean isContextChanged = false;
		for (ContextLoaderEvaluator contextLoaderEvaluator : contextLoaderEvaluators)
		{
			if (contextLoaderEvaluator.loadContext())
			{
				if (logger.isInfoEnabled())
				{
					logger.info("Loading dynamic context: " + contextLoaderEvaluator.getContextResource().toString());
				}
				xmlReader.loadBeanDefinitions(contextLoaderEvaluator.getContextResource());
				isContextChanged = true;
			}
			else
			{
				if (logger.isInfoEnabled())
				{
					logger.info("Skipping dynamic context: " + contextLoaderEvaluator.getContextResource().toString());
				}
			}
		}
		if (isContextChanged)
		{
			childContext.refresh();
		}
	}
	
}
