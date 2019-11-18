/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.device.initializer;

import java.io.File;
import java.nio.file.Path;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sitewhere.device.spi.initializer.IDeviceModelInitializer;
import com.sitewhere.microservice.api.asset.AssetManagementRequestBuilder;
import com.sitewhere.microservice.api.asset.IAssetManagement;
import com.sitewhere.microservice.api.device.DeviceManagementRequestBuilder;
import com.sitewhere.microservice.api.device.IDeviceManagement;
import com.sitewhere.microservice.model.ModelInitializer;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.groovy.IGroovyConfiguration;
import com.sitewhere.spi.microservice.groovy.IGroovyVariables;
import com.sitewhere.spi.microservice.scripting.ScriptType;

import groovy.lang.Binding;

/**
 * Implementation of {@link IDeviceModelInitializer} that delegates creation
 * logic to a Groovy script.
 */
public class GroovyDeviceModelInitializer extends ModelInitializer implements IDeviceModelInitializer {

    /** Static logger instance */
    private static Log LOGGER = LogFactory.getLog(GroovyDeviceModelInitializer.class);

    /** Tenant Groovy configuration */
    private IGroovyConfiguration groovyConfiguration;

    /** Relative path to Groovy script */
    private String scriptPath;

    public GroovyDeviceModelInitializer(IGroovyConfiguration groovyConfiguration, Path scriptPath) {
	this.groovyConfiguration = groovyConfiguration;
	this.scriptPath = File.separator + scriptPath.toString();
    }

    /*
     * @see
     * com.sitewhere.device.spi.initializer.IDeviceModelInitializer#initialize(com.
     * sitewhere.spi.device.IDeviceManagement,
     * com.sitewhere.spi.asset.IAssetManagement)
     */
    @Override
    public void initialize(IDeviceManagement deviceManagement, IAssetManagement assetManagement)
	    throws SiteWhereException {
	Binding binding = new Binding();
	binding.setVariable(IGroovyVariables.VAR_LOGGER, LOGGER);
	binding.setVariable(IGroovyVariables.VAR_DEVICE_MANAGEMENT_BUILDER,
		new DeviceManagementRequestBuilder(deviceManagement));
	binding.setVariable(IGroovyVariables.VAR_ASSET_MANAGEMENT_BUILDER,
		new AssetManagementRequestBuilder(assetManagement));

	try {
	    getGroovyConfiguration().run(ScriptType.Initializer, getScriptPath(), binding);
	} catch (SiteWhereException e) {
	    throw new SiteWhereException("Unable to run device model initializer. " + e.getMessage(), e);
	}
    }

    public IGroovyConfiguration getGroovyConfiguration() {
	return groovyConfiguration;
    }

    public void setGroovyConfiguration(IGroovyConfiguration groovyConfiguration) {
	this.groovyConfiguration = groovyConfiguration;
    }

    public String getScriptPath() {
	return scriptPath;
    }

    public void setScriptPath(String scriptPath) {
	this.scriptPath = scriptPath;
    }
}