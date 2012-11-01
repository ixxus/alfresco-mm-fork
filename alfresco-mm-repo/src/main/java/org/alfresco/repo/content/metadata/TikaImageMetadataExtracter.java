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
package org.alfresco.repo.content.metadata;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.content.metadata.TikaPoweredMetadataExtracter;
import org.apache.tika.metadata.IPTC;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.exiftool.ExiftoolImageParser;

/**
 * Tika metadata extracter to force the use of the exiftool parser and
 * workround ALF-12478
 * 
 * @author rgauss
 */
public class TikaImageMetadataExtracter extends TikaPoweredMetadataExtracter {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(TikaImageMetadataExtracter.class);
	
	private String runtimeExiftoolExecutable;

    public static ArrayList<String> SUPPORTED_MIMETYPES = buildSupportedMimetypes(
        new String[] {
            MimetypeMap.MIMETYPE_IMAGE_GIF,
            MimetypeMap.MIMETYPE_IMAGE_JPEG,
            MimetypeMap.MIMETYPE_IMAGE_PNG,
           "image/tiff"
        }, new ExiftoolImageParser()
    );

    public TikaImageMetadataExtracter()
    {
        super(SUPPORTED_MIMETYPES);
    }
    
    public TikaImageMetadataExtracter(String runtimeExiftoolExecutable)
    {
        super(SUPPORTED_MIMETYPES);
        this.runtimeExiftoolExecutable = runtimeExiftoolExecutable;
    }

    public void setRuntimeExiftoolExecutable(String runtimeExiftoolExecutable) {
		this.runtimeExiftoolExecutable = runtimeExiftoolExecutable;
	}
    
    public void setMappingPropertiesUrl(String mappingPropertiesUrl) {
    	setMapping(readMappingProperties(mappingPropertiesUrl));
    }

	@Override
    protected Parser getParser() {
		return new ExiftoolImageParser(runtimeExiftoolExecutable);
    }

	protected void addTikaProperties(Property[] tikaProperties,
			Metadata metadata, Map<String, Serializable> properties) {
		for (Property tikaProperty : tikaProperties) {
			Serializable metadataValue = null;
			if (metadata.get(tikaProperty) != null) {
				if (tikaProperty.getValueType() == Property.ValueType.DATE) {
					metadataValue = metadata.getDate(tikaProperty);
				} else if (tikaProperty.getValueType() == Property.ValueType.INTEGER) {
					metadataValue = metadata.getInt(tikaProperty);
				} else if (metadata.isMultiValued(tikaProperty.getName())){
					metadataValue = metadata.getValues(tikaProperty.getName());
				} else {
					metadataValue = metadata.get(tikaProperty);
				}
			}
			if (logger.isTraceEnabled()) {
				String logValue = null;
				if (metadataValue != null) {
					logValue = metadataValue.toString();
				}
				logger.trace(tikaProperty.getName() + "=" + logValue);
			}
			if (metadataValue != null) {
				putRawValue(tikaProperty.getName(), metadataValue, properties);
			}
		}
	}

    @Override
	protected Map<String, Serializable> extractSpecific(Metadata metadata,
			Map<String, Serializable> properties, Map<String, String> headers) {
		if (logger.isTraceEnabled()) {
			logger.trace("existing properties:\n\n");
			for (String propertyKey : properties.keySet()) {
				logger.trace(propertyKey + "=" + properties.get(propertyKey));
			}
			logger.trace("additional properties:\n\n");
		}
		addTikaProperties(IPTC.PROPERTY_GROUP_IPTC_CORE, metadata, properties);
		addTikaProperties(IPTC.PROPERTY_GROUP_IPTC_EXT, metadata, properties);
		return properties;
	}

}
