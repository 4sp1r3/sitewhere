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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sitewhere.instance.spi.microservice.IInstanceManagementMicroservice;
import com.sitewhere.microservice.api.user.IUserManagement;
import com.sitewhere.rest.model.user.GrantedAuthority;
import com.sitewhere.rest.model.user.GrantedAuthoritySearchCriteria;
import com.sitewhere.rest.model.user.request.GrantedAuthorityCreateRequest;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.search.ISearchResults;
import com.sitewhere.spi.user.IGrantedAuthority;
import com.sitewhere.web.rest.model.GrantedAuthorityHierarchyBuilder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

/**
 * Controller for user operations.
 */
@Path("/authorities")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "authorities")
public class Authorities {

    @Inject
    private IInstanceManagementMicroservice<?> microservice;

    /**
     * Create a new authority.
     * 
     * @param input
     * @return
     * @throws SiteWhereException
     */
    @POST
    @ApiOperation(value = "Create a new authority")
    public Response createAuthority(@RequestBody GrantedAuthorityCreateRequest input) throws SiteWhereException {
	// checkAuthForAll(SiteWhereAuthority.REST, SiteWhereAuthority.AdminUsers);
	IGrantedAuthority auth = getUserManagement().createGrantedAuthority(input);
	return Response.ok(GrantedAuthority.copy(auth)).build();
    }

    /**
     * Get an authority by unique name.
     * 
     * @param name
     * @return
     * @throws SiteWhereException
     */
    @GET
    @Path("/{name}")
    @ApiOperation(value = "Get authority by id")
    public Response getAuthorityByName(
	    @ApiParam(value = "Authority name", required = true) @PathParam("name") String name)
	    throws SiteWhereException {
	// checkAuthForAll(SiteWhereAuthority.REST, SiteWhereAuthority.AdminUsers);
	IGrantedAuthority auth = getUserManagement().getGrantedAuthorityByName(name);
	if (auth == null) {
	    return Response.status(Status.NOT_FOUND).build();
	}
	return Response.ok(GrantedAuthority.copy(auth)).build();
    }

    /**
     * List authorities that match given criteria.
     * 
     * @return
     * @throws SiteWhereException
     */
    @GET
    @ApiOperation(value = "List authorities that match criteria")
    public Response listAuthorities() throws SiteWhereException {
	// checkAuthForAll(SiteWhereAuthority.REST, SiteWhereAuthority.AdminUsers);
	GrantedAuthoritySearchCriteria criteria = new GrantedAuthoritySearchCriteria();
	return Response.ok(getUserManagement().listGrantedAuthorities(criteria)).build();
    }

    /**
     * Get the hierarchy of granted authorities.
     * 
     * @return
     * @throws SiteWhereException
     */
    @GET
    @Path("/hierarchy")
    @ApiOperation(value = "Get authorities hierarchy")
    public Response getAuthoritiesHierarchy() throws SiteWhereException {
	// checkAuthForAll(SiteWhereAuthority.REST, SiteWhereAuthority.AdminUsers);
	GrantedAuthoritySearchCriteria criteria = new GrantedAuthoritySearchCriteria(1, 0);
	ISearchResults<IGrantedAuthority> auths = getUserManagement().listGrantedAuthorities(criteria);
	return Response.ok(GrantedAuthorityHierarchyBuilder.build(auths.getResults())).build();
    }

    /**
     * Get user management implementation.
     * 
     * @return
     * @throws SiteWhereException
     */
    protected IUserManagement getUserManagement() throws SiteWhereException {
	return getMicroservice().getUserManagement();
    }

    protected IInstanceManagementMicroservice<?> getMicroservice() {
	return microservice;
    }
}