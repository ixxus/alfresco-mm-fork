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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.alfresco.repo.content.filestore.FileContentReader;
import org.alfresco.repo.content.transform.AbstractContentTransformerTest;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.datatype.DefaultTypeConverter;
import org.alfresco.service.namespace.QName;

/**
 * @see org.alfresco.repo.content.metadata.TikaImageMetadataExtracter
 * 
 * @author rgauss
 */
public class TikaImageMetadataExtracterTest extends AbstractMetadataExtracterTest
{
    private static final QName IPTC_PROP_CAPTION = 
            QName.createQName("http://purl.org/dc/elements/1.1/", "description");
    private static final QName IPTC_PROP_SUBLOCATION_SHOWN = 
            QName.createQName("http://iptc.org/std/Iptc4xmpExt/2008-02-29/", "Sublocation");

    private static final String QUICK_IPTC_TEST_JPEG_FILENAME = "quickIPTC.jpg";
    private static final String QUICK_LOCATION_SHOWN = "The Gym";
    private static final String QUICK_OBJECT_SHOWN = "The Dog";
    private static final String QUICK_IMAGE_CREATOR = QUICK_PREVIOUS_AUTHOR;
    private static final String QUICK_COPYRIGHT_OWNER = "Alfresco";
    private static final String QUICK_LICENSOR = "Ray Gauss II";
    
    private TikaImageMetadataExtracter extracter;

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        extracter = new TikaImageMetadataExtracter();
        extracter.setInheritDefaultMapping(true);
        extracter.setMappingPropertiesUrl("org/alfresco/repo/content/metadata/TikaAutoMetadataExtracter.properties");
        extracter.setDictionaryService(dictionaryService);
        extracter.register();
    }

    protected MetadataExtracter getExtracter()
    {
        return extracter;
    }

    public void testSupports() throws Exception
    {
        for (String mimetype : TikaImageMetadataExtracter.SUPPORTED_MIMETYPES)
        {
            boolean supports = extracter.isSupported(mimetype);
            assertTrue("Mimetype should be supported: " + mimetype, supports);
        }
    }
    
    public void testJPEGExtraction() throws Exception
    {
        testExtractIPTC(QUICK_IPTC_TEST_JPEG_FILENAME);
    }

    protected void testFileSpecificMetadata(String mimetype, Map<QName, Serializable> properties)
    {
       assertEquals(
             "Property " + IPTC_PROP_CAPTION + " not found for mimetype " + mimetype,
             QUICK_DESCRIPTION,
             DefaultTypeConverter.INSTANCE.convert(String.class, properties.get(IPTC_PROP_CAPTION)));
       // TODO - check structure mappings
    }
    
    protected void testExtractIPTC(String quickname) throws Exception
    {
        String ext = quickname.split("\\.")[quickname.split("\\.").length - 1];
        String mimetype = mimetypeMap.getMimetype(ext);
        try
        {
            File sourceFile = AbstractContentTransformerTest.loadNamedQuickTestFile(quickname);
            if (sourceFile == null)
            {
                throw new FileNotFoundException("No " + quickname + " file found for test");
            }
            
            Map<QName, Serializable> properties = new HashMap<QName, Serializable>();
            // construct a reader onto the source file
            ContentReader sourceReader = new FileContentReader(sourceFile);
            sourceReader.setMimetype(mimetype);
            getExtracter().extract(sourceReader, properties);
            
            // check we got something
            assertFalse("extract should return at least some properties, none found for " + mimetype,
                    properties.isEmpty());
            
            // check common metadata
            testCommonMetadata(mimetype, properties);
            // check file-type specific metadata
            testFileSpecificMetadata(mimetype, properties);
        }
        catch (FileNotFoundException e)
        {
            // The test file is not there.  We won't fail it.
           System.err.println("No test file found for mime type " + mimetype + 
                 ", skipping extraction test - " + e.getMessage());
        }
    }
    

}
