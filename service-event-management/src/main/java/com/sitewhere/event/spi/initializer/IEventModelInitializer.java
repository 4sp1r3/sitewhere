/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.event.spi.initializer;

import com.sitewhere.microservice.api.device.IDeviceManagement;
import com.sitewhere.microservice.api.event.IDeviceEventManagement;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.model.IModelInitializer;

/**
 * Initializes the event model with data needed to bootstrap the system.
 */
public interface IEventModelInitializer extends IModelInitializer {

    /**
     * Initialize the event model.
     * 
     * @param deviceManagement
     * @param eventManagement
     * @throws SiteWhereException
     */
    public void initialize(IDeviceManagement deviceManagement, IDeviceEventManagement eventManagement)
	    throws SiteWhereException;
}
