/*
 * DefaultCommandProcessingStrategy.java 
 * --------------------------------------------------------------------------------------
 * Copyright (c) Reveal Technologies, LLC. All rights reserved. http://www.reveal-tech.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.device.provisioning;

import java.util.List;

import org.apache.log4j.Logger;

import com.sitewhere.SiteWhere;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.device.IDevice;
import com.sitewhere.spi.device.IDeviceAssignment;
import com.sitewhere.spi.device.IDeviceManagement;
import com.sitewhere.spi.device.IDeviceNestingContext;
import com.sitewhere.spi.device.command.IDeviceCommand;
import com.sitewhere.spi.device.command.IDeviceCommandExecution;
import com.sitewhere.spi.device.event.IDeviceCommandInvocation;
import com.sitewhere.spi.device.provisioning.ICommandExecutionBuilder;
import com.sitewhere.spi.device.provisioning.ICommandProcessingStrategy;
import com.sitewhere.spi.device.provisioning.ICommandTargetResolver;
import com.sitewhere.spi.device.provisioning.IDeviceProvisioning;

/**
 * Default implementation of {@link ICommandProcessingStrategy}.
 * 
 * @author Derek
 */
public class DefaultCommandProcessingStrategy implements ICommandProcessingStrategy {

	/** Static logger instance */
	private static Logger LOGGER = Logger.getLogger(DefaultCommandProcessingStrategy.class);

	/** Configured command target resolver */
	private ICommandTargetResolver commandTargetResolver = new DefaultCommandTargetResolver();

	/** Configured command execution builder */
	private ICommandExecutionBuilder commandExecutionBuilder = new DefaultCommandExecutionBuilder();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sitewhere.spi.device.provisioning.ICommandProcessingStrategy#deliverCommand
	 * (com.sitewhere.spi.device.provisioning.IDeviceProvisioning,
	 * com.sitewhere.spi.device.event.IDeviceCommandInvocation)
	 */
	@Override
	public void deliverCommand(IDeviceProvisioning provisioning, IDeviceCommandInvocation invocation)
			throws SiteWhereException {
		LOGGER.debug("Command processing strategy handling invocation.");
		IDeviceCommand command =
				SiteWhere.getServer().getDeviceManagement().getDeviceCommandByToken(
						invocation.getCommandToken());
		if (command != null) {
			IDeviceCommandExecution execution =
					getCommandExecutionBuilder().createExecution(command, invocation);
			List<IDeviceAssignment> assignments = getCommandTargetResolver().resolveTargets(invocation);
			for (IDeviceAssignment assignment : assignments) {
				IDevice device =
						SiteWhere.getServer().getDeviceManagement().getDeviceForAssignment(assignment);
				if (device == null) {
					throw new SiteWhereException("Targeted assignment references device that does not exist.");
				}

				IDeviceNestingContext nesting = NestedDeviceSupport.calculateNestedDeviceInformation(device);
				provisioning.getOutboundCommandRouter().routeCommand(execution, nesting, assignment);
			}
		} else {
			throw new SiteWhereException("Invalid command referenced from invocation.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sitewhere.spi.device.provisioning.ICommandProcessingStrategy#deliverSystemCommand
	 * (com.sitewhere.spi.device.provisioning.IDeviceProvisioning, java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void deliverSystemCommand(IDeviceProvisioning provisioning, String hardwareId, Object command)
			throws SiteWhereException {
		IDeviceManagement management = SiteWhere.getServer().getDeviceManagement();
		IDevice device = management.getDeviceByHardwareId(hardwareId);
		if (device == null) {
			throw new SiteWhereException("Targeted assignment references device that does not exist.");
		}
		IDeviceAssignment assignment = management.getCurrentDeviceAssignment(device);
		IDeviceNestingContext nesting = NestedDeviceSupport.calculateNestedDeviceInformation(device);
		provisioning.getOutboundCommandRouter().routeSystemCommand(command, nesting, assignment);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sitewhere.spi.ISiteWhereLifecycle#start()
	 */
	@Override
	public void start() throws SiteWhereException {
		LOGGER.info("Started command processing strategy.");

		// Start command execution builder.
		if (getCommandExecutionBuilder() == null) {
			throw new SiteWhereException("No command execution builder configured for provisioning.");
		}
		getCommandExecutionBuilder().start();

		// Start command target resolver.
		if (getCommandTargetResolver() == null) {
			throw new SiteWhereException("No command target resolver configured for provisioning.");
		}
		getCommandTargetResolver().start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sitewhere.spi.ISiteWhereLifecycle#stop()
	 */
	@Override
	public void stop() throws SiteWhereException {
		LOGGER.info("Stopped command processing strategy");

		// Stop command execution builder.
		if (getCommandExecutionBuilder() != null) {
			getCommandExecutionBuilder().stop();
		}

		// Stop command target resolver.
		if (getCommandTargetResolver() != null) {
			getCommandTargetResolver().stop();
		}
	}

	public ICommandTargetResolver getCommandTargetResolver() {
		return commandTargetResolver;
	}

	public void setCommandTargetResolver(ICommandTargetResolver commandTargetResolver) {
		this.commandTargetResolver = commandTargetResolver;
	}

	public ICommandExecutionBuilder getCommandExecutionBuilder() {
		return commandExecutionBuilder;
	}

	public void setCommandExecutionBuilder(ICommandExecutionBuilder commandExecutionBuilder) {
		this.commandExecutionBuilder = commandExecutionBuilder;
	}
}