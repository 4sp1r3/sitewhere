/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.web.rest.controllers;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sitewhere.instance.spi.microservice.IInstanceManagementMicroservice;
import com.sitewhere.rest.model.search.tenant.TenantSearchCriteria;
import com.sitewhere.rest.model.tenant.request.TenantCreateRequest;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.SiteWhereSystemException;
import com.sitewhere.spi.error.ErrorCode;
import com.sitewhere.spi.error.ErrorLevel;
import com.sitewhere.spi.microservice.tenant.ITenantManagement;
import com.sitewhere.spi.tenant.ITenant;
import com.sitewhere.spi.user.SiteWhereAuthority;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

/**
 * Controller for tenant operations.
 */
@Path("/tenants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "tenants")
public class Tenants {

    /** Static logger instance */
    @SuppressWarnings("unused")
    private static Log LOGGER = LogFactory.getLog(Tenants.class);

    @Inject
    private IInstanceManagementMicroservice<?> microservice;

    /**
     * Create a new tenant.
     * 
     * @param request
     * @return
     * @throws SiteWhereException
     */
    @POST
    @ApiOperation(value = "Create new tenant")
    public Response createTenant(@RequestBody TenantCreateRequest request) throws SiteWhereException {
	// checkAuthForAll(SiteWhereAuthority.REST, SiteWhereAuthority.AdminTenants);
	return Response.ok(getTenantManagement().createTenant(request)).build();
    }

    /**
     * Update an existing tenant.
     * 
     * @param tenantToken
     * @param request
     * @return
     * @throws SiteWhereException
     */
    @PUT
    @Path("/{tenantToken}")
    @ApiOperation(value = "Update an existing tenant.")
    public Response updateTenant(
	    @ApiParam(value = "Tenant token", required = true) @PathParam("tenantToken") String tenantToken,
	    @RequestBody TenantCreateRequest request) throws SiteWhereException {
	ITenant tenant = assureTenant(tenantToken);
	checkForAdminOrEditSelf(tenant);
	return Response.ok(getTenantManagement().updateTenant(null, request)).build();
    }

    /**
     * Get a tenant by unique id.
     * 
     * @param tenantToken
     * @return
     * @throws SiteWhereException
     */
    @GET
    @Path("/{tenantToken}")
    @ApiOperation(value = "Get tenant by token")
    public Response getTenantByToken(
	    @ApiParam(value = "Tenant token", required = true) @PathParam("tenantToken") String tenantToken)
	    throws SiteWhereException {
	ITenant tenant = assureTenant(tenantToken);
	checkForAdminOrEditSelf(tenant);
	return Response.ok(tenant).build();
    }

    /**
     * List tenants that match the given criteria.
     * 
     * @param criteria
     * @return
     * @throws SiteWhereException
     */
    @GET
    @ApiOperation(value = "List tenants that match criteria")
    public Response listTenants(
	    @ApiParam(value = "Text search (partial id or name)", required = false) @QueryParam("textSearch") String textSearch,
	    @ApiParam(value = "Authorized user id", required = false) @QueryParam("authUserId") String authUserId,
	    @ApiParam(value = "Include runtime info", required = false) @QueryParam("includeRuntimeInfo") @DefaultValue("true") boolean includeRuntimeInfo,
	    @ApiParam(value = "Page number", required = false) @QueryParam("page") @DefaultValue("1") int page,
	    @ApiParam(value = "Page size", required = false) @QueryParam("pageSize") @DefaultValue("100") int pageSize)
	    throws SiteWhereException {
	checkAuthFor(SiteWhereAuthority.REST, true);

	// Return all tenants if authorized as tenant admin.
	if (checkAuthFor(SiteWhereAuthority.AdminTenants, false)) {
	    TenantSearchCriteria criteria = new TenantSearchCriteria(page, pageSize);
	    criteria.setTextSearch(textSearch);
	    criteria.setUserId(authUserId);
	    criteria.setIncludeRuntimeInfo(includeRuntimeInfo);
	    return Response.ok(getTenantManagement().listTenants(criteria)).build();
	}

	// Only return auth tenants if user has 'admin own tenant'.
	else if (checkAuthFor(SiteWhereAuthority.AdminOwnTenant, false)) {
	    // IUser loggedIn = UserContextManager.getCurrentlyLoggedInUser();
	    // if (loggedIn != null) {
	    // TenantSearchCriteria criteria = new TenantSearchCriteria(page, pageSize);
	    // criteria.setTextSearch(textSearch);
	    // criteria.setUserId(loggedIn.getUsername());
	    // criteria.setIncludeRuntimeInfo(includeRuntimeInfo);
	    // return getTenantManagement().listTenants(criteria);
	    // }
	}

	return Response.status(Status.FORBIDDEN).build();
    }

    /**
     * Delete tenant by token.
     * 
     * @param tenantToken
     * @return
     * @throws SiteWhereException
     */
    @DELETE
    @Path("/{tenantToken}")
    @ApiOperation(value = "Delete existing tenant")
    public Response deleteTenantById(
	    @ApiParam(value = "Tenant token", required = true) @PathParam("tenantToken") String tenantToken)
	    throws SiteWhereException {
	// checkAuthForAll(SiteWhereAuthority.REST, SiteWhereAuthority.AdminTenants);
	ITenant tenant = assureTenant(tenantToken);
	checkForAdminOrEditSelf(tenant);
	return Response.ok(getTenantManagement().deleteTenant(null)).build();
    }

    /**
     * Lists all available tenant templates.
     * 
     * @return
     * @throws SiteWhereException
     */
    @GET
    @Path("/templates")
    @ApiOperation(value = "List templates available for creating tenants")
    public Response listTenantConfigurationTemplates() throws SiteWhereException {
	checkAuthFor(SiteWhereAuthority.REST, true);
	if (checkAuthFor(SiteWhereAuthority.AdminTenants, false)
		|| checkAuthFor(SiteWhereAuthority.AdminOwnTenant, false)) {
	}
	// return getInstanceManagement().getTenantTemplates();
	return Response.ok().build();
    }

    /**
     * Lists all available dataset templates.
     * 
     * @return
     * @throws SiteWhereException
     */
    @GET
    @Path("/datasets")
    @ApiOperation(value = "List datasets available for creating tenants")
    public Response listTenantDatasetTemplates() throws SiteWhereException {
	checkAuthFor(SiteWhereAuthority.REST, true);
	if (checkAuthFor(SiteWhereAuthority.AdminTenants, false)
		|| checkAuthFor(SiteWhereAuthority.AdminOwnTenant, false)) {
	}
	// return getInstanceManagement().getDatasetTemplates();
	return Response.ok().build();
    }

    protected boolean checkAuthFor(SiteWhereAuthority auth, boolean flag) {
	return true;
    }

    /**
     * Check for privileges to use REST services + either admin all tenants or admin
     * own tenant on the currently logged in user.
     * 
     * @param tenant
     * @throws SiteWhereException
     */
    public static void checkForAdminOrEditSelf(ITenant tenant) throws SiteWhereException {
	// checkAuthFor(SiteWhereAuthority.REST, true);
	// if (!checkAuthFor(SiteWhereAuthority.AdminTenants, false)) {
	// checkAuthFor(SiteWhereAuthority.AdminOwnTenant, true);
	// IUser loggedIn = UserContextManager.getCurrentlyLoggedInUser();
	// if ((loggedIn == null) ||
	// (!tenant.getAuthorizedUserIds().contains(loggedIn.getUsername()))) {
	// throw new SiteWhereSystemException(ErrorCode.OperationNotPermitted,
	// ErrorLevel.ERROR,
	// HttpServletResponse.SC_FORBIDDEN);
	// }
	// }
    }

    /**
     * Assure that a tenant exists for the given token.
     * 
     * @param tenantToken
     * @return
     * @throws SiteWhereException
     */
    protected ITenant assureTenant(String tenantToken) throws SiteWhereException {
	ITenant tenant = getTenantManagement().getTenantByToken(tenantToken);
	if (tenant == null) {
	    throw new SiteWhereSystemException(ErrorCode.InvalidTenantToken, ErrorLevel.ERROR);
	}
	return tenant;
    }

    protected ITenantManagement getTenantManagement() {
	return getMicroservice().getTenantManagement();
    }

    protected IInstanceManagementMicroservice<?> getMicroservice() {
	return microservice;
    }
}