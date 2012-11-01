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

/**
 * Example {@link ContextLoaderEvaluator} which extends the boolean
 * implementation and does a simple check against a hard-coded string
 * license key.
 * 
 * @author rgauss
 */
public class SimpleLicensedBooleanContextLoaderEvaluator extends
		BooleanContextLoaderEvaluator {

	private static final Log logger = LogFactory.getLog(SimpleLicensedBooleanContextLoaderEvaluator.class);
	
	private static final String VALID_LICENSE_KEY = "12345";
	
	private String licenseKey;
	
	public void setLicenseKey(String licenseKey) {
		this.licenseKey = licenseKey;
	}

	@Override
	public boolean loadContext()
	{
		return super.loadContext() && isValidLicenseKey();
	}
	
	public boolean isValidLicenseKey() {
		if (VALID_LICENSE_KEY.equals(licenseKey))
		{
			return true;
		}
		else
		{
			logger.warn("License key '" + licenseKey + "' is invalid");
			return false;
		}
	}

}
