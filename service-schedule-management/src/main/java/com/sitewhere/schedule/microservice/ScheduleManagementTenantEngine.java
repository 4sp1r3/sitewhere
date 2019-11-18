/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.schedule.microservice;

import com.sitewhere.grpc.service.ScheduleManagementGrpc;
import com.sitewhere.microservice.api.schedule.IScheduleManagement;
import com.sitewhere.microservice.lifecycle.CompositeLifecycleStep;
import com.sitewhere.microservice.multitenant.MicroserviceTenantEngine;
import com.sitewhere.schedule.spi.microservice.IScheduleManagementTenantEngine;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.lifecycle.ICompositeLifecycleStep;
import com.sitewhere.spi.microservice.lifecycle.ILifecycleProgressMonitor;
import com.sitewhere.spi.microservice.multitenant.IMicroserviceTenantEngine;
import com.sitewhere.spi.tenant.ITenant;

import io.sitewhere.k8s.crd.tenant.engine.dataset.TenantEngineDatasetTemplate;

/**
 * Implementation of {@link IMicroserviceTenantEngine} that implements schedule
 * management functionality.
 */
public class ScheduleManagementTenantEngine extends MicroserviceTenantEngine
	implements IScheduleManagementTenantEngine {

    /** Schedule management persistence API */
    private IScheduleManagement scheduleManagement;

    /** Responds to schedule management GRPC requests */
    private ScheduleManagementGrpc.ScheduleManagementImplBase scheduleManagementImpl;

    public ScheduleManagementTenantEngine(ITenant tenant) {
	super(tenant);
    }

    /*
     * @see com.sitewhere.spi.microservice.multitenant.IMicroserviceTenantEngine#
     * tenantInitialize(com.sitewhere.spi.microservice.lifecycle.
     * ILifecycleProgressMonitor)
     */
    @Override
    public void tenantInitialize(ILifecycleProgressMonitor monitor) throws SiteWhereException {
	// // Create management interfaces.
	// this.scheduleManagement = (IScheduleManagement) getModuleContext()
	// .getBean(ScheduleManagementBeans.BEAN_SCHEDULE_MANAGEMENT);
	// this.scheduleManagementImpl = new
	// ScheduleManagementImpl((IScheduleManagementMicroservice) getMicroservice(),
	// getScheduleManagement());

	// Create step that will initialize components.
	ICompositeLifecycleStep init = new CompositeLifecycleStep("Initialize " + getComponentName());

	// Initialize discoverable lifecycle components.
	init.addStep(initializeDiscoverableBeans(getModuleContext()));

	// Initialize schedule management persistence.
	init.addInitializeStep(this, getScheduleManagement(), true);

	// Execute initialization steps.
	init.execute(monitor);
    }

    /*
     * @see com.sitewhere.spi.microservice.multitenant.IMicroserviceTenantEngine#
     * tenantStart(com.sitewhere.spi.microservice.lifecycle.
     * ILifecycleProgressMonitor)
     */
    @Override
    public void tenantStart(ILifecycleProgressMonitor monitor) throws SiteWhereException {
	// Create step that will start components.
	ICompositeLifecycleStep start = new CompositeLifecycleStep("Start " + getComponentName());

	// Start discoverable lifecycle components.
	start.addStep(startDiscoverableBeans(getModuleContext()));

	// Start schedule management persistence.
	start.addStartStep(this, getScheduleManagement(), true);

	// Execute startup steps.
	start.execute(monitor);
    }

    /*
     * @see com.sitewhere.spi.microservice.multitenant.IMicroserviceTenantEngine#
     * tenantBootstrap(io.sitewhere.k8s.crd.tenant.engine.dataset.
     * TenantEngineDatasetTemplate,
     * com.sitewhere.spi.microservice.lifecycle.ILifecycleProgressMonitor)
     */
    @Override
    public void tenantBootstrap(TenantEngineDatasetTemplate template, ILifecycleProgressMonitor monitor)
	    throws SiteWhereException {
	// String scriptName = String.format("%s.groovy",
	// template.getMetadata().getName());
	// Path path = getScriptSynchronizer().add(getScriptContext(),
	// ScriptType.Initializer, scriptName,
	// template.getSpec().getConfiguration().getBytes());

	// Execute calls as superuser.
	// Authentication previous =
	// SecurityContextHolder.getContext().getAuthentication();
	// try {
	// SecurityContextHolder.getContext()
	// .setAuthentication(getMicroservice().getSystemUser().getAuthenticationForTenant(getTenant()));
	//
	// getLogger().info(String.format("Applying bootstrap script '%s'.", path));
	// GroovyScheduleModelInitializer initializer = new
	// GroovyScheduleModelInitializer(getGroovyConfiguration(),
	// path);
	// initializer.initialize(getScheduleManagement());
	// } catch (Throwable e) {
	// getLogger().error("Unhandled exception in bootstrap script.", e);
	// throw new SiteWhereException(e);
	// } finally {
	// SecurityContextHolder.getContext().setAuthentication(previous);
	// }
    }

    /*
     * @see com.sitewhere.spi.microservice.multitenant.IMicroserviceTenantEngine#
     * tenantStop(com.sitewhere.spi.microservice.lifecycle.
     * ILifecycleProgressMonitor)
     */
    @Override
    public void tenantStop(ILifecycleProgressMonitor monitor) throws SiteWhereException {
	// Create step that will stop components.
	ICompositeLifecycleStep stop = new CompositeLifecycleStep("Stop " + getComponentName());

	// Stop schedule management persistence.
	stop.addStopStep(this, getScheduleManagement());

	// Stop discoverable lifecycle components.
	stop.addStep(stopDiscoverableBeans(getModuleContext()));

	// Execute shutdown steps.
	stop.execute(monitor);
    }

    /*
     * @see com.sitewhere.schedule.spi.microservice.IScheduleManagementTenantEngine#
     * getScheduleManagement()
     */
    @Override
    public IScheduleManagement getScheduleManagement() {
	return scheduleManagement;
    }

    protected void setScheduleManagement(IScheduleManagement scheduleManagement) {
	this.scheduleManagement = scheduleManagement;
    }

    /*
     * @see com.sitewhere.schedule.spi.microservice.IScheduleManagementTenantEngine#
     * getScheduleManagementImpl()
     */
    @Override
    public ScheduleManagementGrpc.ScheduleManagementImplBase getScheduleManagementImpl() {
	return scheduleManagementImpl;
    }

    protected void setScheduleManagementImpl(ScheduleManagementGrpc.ScheduleManagementImplBase scheduleManagementImpl) {
	this.scheduleManagementImpl = scheduleManagementImpl;
    }
}