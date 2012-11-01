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
package org.alfresco.repo.content.transform.ffmpeg;

/**
 * Stub class for providing a JMX managed resource
 * 
 * @author rgauss
 */
public class MockManagedResource
{

    private Object resource;

    private Object objectName;

    private Object managedInterfaces;

    public Object getResource()
    {
        return resource;
    }

    public void setResource(Object resource)
    {
        this.resource = resource;
    }

    public Object getObjectName()
    {
        return objectName;
    }

    public void setObjectName(Object objectName)
    {
        this.objectName = objectName;
    }

    public Object getManagedInterfaces()
    {
        return managedInterfaces;
    }

    public void setManagedInterfaces(Object managedInterfaces)
    {
        this.managedInterfaces = managedInterfaces;
    }

}
