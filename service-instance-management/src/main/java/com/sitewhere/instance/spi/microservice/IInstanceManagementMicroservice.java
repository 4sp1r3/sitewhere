/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.instance.spi.microservice;

import com.sitewhere.grpc.client.spi.client.IAssetManagementApiChannel;
import com.sitewhere.grpc.client.spi.client.IBatchManagementApiChannel;
import com.sitewhere.grpc.client.spi.client.IDeviceEventManagementApiChannel;
import com.sitewhere.grpc.client.spi.client.IDeviceManagementApiChannel;
import com.sitewhere.grpc.client.spi.client.IDeviceStateApiChannel;
import com.sitewhere.grpc.client.spi.client.ILabelGenerationApiChannel;
import com.sitewhere.grpc.client.spi.client.IScheduleManagementApiChannel;
import com.sitewhere.instance.spi.tenant.grpc.ITenantManagementGrpcServer;
import com.sitewhere.instance.spi.user.grpc.IUserManagementGrpcServer;
import com.sitewhere.microservice.api.user.IUserManagement;
import com.sitewhere.spi.microservice.IFunctionIdentifier;
import com.sitewhere.spi.microservice.IGlobalMicroservice;
import com.sitewhere.spi.microservice.groovy.IGroovyConfiguration;
import com.sitewhere.spi.microservice.scripting.IScriptContext;
import com.sitewhere.spi.microservice.scripting.IScriptSynchronizer;

/**
 * Microservice that provides web/REST functionality.
 */
public interface IInstanceManagementMicroservice<T extends IFunctionIdentifier> extends IGlobalMicroservice<T> {

    /**
     * Get instance script synchronizer.
     * 
     * @return
     */
    public IScriptSynchronizer getScriptSynchronizer();

    /**
     * Get script context for instance.
     * 
     * @return
     */
    public IScriptContext getScriptContext();

    /**
     * Get component which bootstraps instance with data.
     * 
     * @return
     */
    public IInstanceBootstrapper getInstanceBootstrapper();

    /**
     * Get Groovy configuration for instance-scoped operations.
     * 
     * @return
     */
    public IGroovyConfiguration getGroovyConfiguration();

    /**
     * Get user management implementation.
     * 
     * @return
     */
    public IUserManagement getUserManagement();

    /**
     * Get user management gRPC server.
     * 
     * @return
     */
    public IUserManagementGrpcServer getUserManagementGrpcServer();

    /**
     * Get tenant management gRPC server.
     * 
     * @return
     */
    public ITenantManagementGrpcServer getTenantManagementGrpcServer();

    /**
     * Device management API access via GRPC channel.
     * 
     * @return
     */
    public IDeviceManagementApiChannel<?> getDeviceManagementApiChannel();

    /**
     * Device event management API access via GRPC channel.
     * 
     * @return
     */
    public IDeviceEventManagementApiChannel<?> getDeviceEventManagementApiChannel();

    /**
     * Asset management API access via GRPC channel.
     * 
     * @return
     */
    public IAssetManagementApiChannel<?> getAssetManagementApiChannel();

    /**
     * Batch management API access via GRPC channel.
     * 
     * @return
     */
    public IBatchManagementApiChannel<?> getBatchManagementApiChannel();

    /**
     * Schedule management API access via GRPC channel.
     * 
     * @return
     */
    public IScheduleManagementApiChannel<?> getScheduleManagementApiChannel();

    /**
     * Label generation API access via GRPC channel.
     * 
     * @return
     */
    public ILabelGenerationApiChannel<?> getLabelGenerationApiChannel();

    /**
     * Device state API access via GRPC channel.
     * 
     * @return
     */
    public IDeviceStateApiChannel<?> getDeviceStateApiChannel();
}