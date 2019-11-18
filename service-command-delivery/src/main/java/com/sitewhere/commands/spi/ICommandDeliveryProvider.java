/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.commands.spi;

import java.util.List;

import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.device.IDeviceAssignment;
import com.sitewhere.spi.device.IDeviceNestingContext;
import com.sitewhere.spi.device.command.IDeviceCommandExecution;
import com.sitewhere.spi.microservice.lifecycle.ITenantEngineLifecycleComponent;

/**
 * Handles delivery of encoded command information on an underlying transport.
 * 
 * @param <T>
 *            type of data that was encoded by the
 *            {@link ICommandExecutionEncoder}/
 * @param <P>
 *            parameters specific to the delivery provider
 */
public interface ICommandDeliveryProvider<T, P> extends ITenantEngineLifecycleComponent {

    /**
     * Deliver the given encoded invocation. The device, active assignments and
     * invocation details are included since they may contain metadata important to
     * the delivery mechanism.
     * 
     * @param nested
     * @param assignments
     * @param execution
     * @param encoded
     * @param parameters
     * @throws SiteWhereException
     */
    public void deliver(IDeviceNestingContext nested, List<IDeviceAssignment> assignments,
	    IDeviceCommandExecution execution, T encoded, P parameters) throws SiteWhereException;

    /**
     * Delivers a system command.
     * 
     * @param nested
     * @param assignments
     * @param encoded
     * @param parameters
     * @throws SiteWhereException
     */
    public void deliverSystemCommand(IDeviceNestingContext nested, List<IDeviceAssignment> assignments, T encoded,
	    P parameters) throws SiteWhereException;
}