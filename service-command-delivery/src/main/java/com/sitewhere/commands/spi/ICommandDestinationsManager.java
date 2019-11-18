/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.commands.spi;

import java.util.Map;

import com.sitewhere.spi.microservice.lifecycle.ITenantEngineLifecycleComponent;

/**
 * Manages command invocation logic.
 */
public interface ICommandDestinationsManager extends ITenantEngineLifecycleComponent {

    /**
     * Get map of command destinations indexed by destination id.
     * 
     * @return
     */
    public Map<String, ICommandDestination<?, ?>> getCommandDestinations();
}