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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sitewhere.instance.spi.microservice.IInstanceManagementMicroservice;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.device.event.IDeviceEvent;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

/**
 * Controller for search operations.
 */
@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "search")
public class ExternalSearch {

    /** Static logger instance */
    @SuppressWarnings("unused")
    private static Log LOGGER = LogFactory.getLog(ExternalSearch.class);

    @Inject
    private IInstanceManagementMicroservice<?> microservice;

    /**
     * Get list of all search providers.
     * 
     * @return
     * @throws SiteWhereException
     */
    @GET
    @ApiOperation(value = "List available search providers")
    public Response listSearchProviders() throws SiteWhereException {
	// List<ISearchProvider> providers =
	// getSearchProviderManager().getSearchProviders();
	// List<SearchProvider> retval = new ArrayList<SearchProvider>();
	// for (ISearchProvider provider : providers) {
	// retval.add(SearchProvider.copy(provider));
	// }
	// return retval;
	return Response.ok().build();
    }

    /**
     * Perform search and marshal resulting events into {@link IDeviceEvent}
     * response.
     * 
     * @param providerId
     * @return
     * @throws SiteWhereException
     */
    @GET
    @Path("/{providerId}/events")
    @ApiOperation(value = "Search for events in provider")
    public Response searchDeviceEvents(
	    @ApiParam(value = "Search provider id", required = true) @PathParam("providerId") String providerId)
	    throws SiteWhereException {
	// ISearchProvider provider =
	// getSearchProviderManager().getSearchProvider(providerId);
	// if (provider == null) {
	// throw new SiteWhereSystemException(ErrorCode.InvalidSearchProviderId,
	// ErrorLevel.ERROR,
	// HttpServletResponse.SC_NOT_FOUND);
	// }
	// if (!(provider instanceof IDeviceEventSearchProvider)) {
	// throw new SiteWhereException("Search provider does not provide event search
	// capability.");
	// }
	// String query = request.getQueryString();
	// return ((IDeviceEventSearchProvider) provider).executeQuery(query);
	return Response.ok().build();
    }

    /**
     * Perform serach and return raw JSON response.
     * 
     * @param providerId
     * @param request
     * @param servletRequest
     * @return
     * @throws SiteWhereException
     */
    @POST
    @Path("/{providerId}/raw")
    @ApiOperation(value = "Execute search and return raw results")
    public Response rawSearch(
	    @ApiParam(value = "Search provider id", required = true) @PathParam("providerId") String providerId,
	    @RequestBody String query) throws SiteWhereException {
	// ISearchProvider provider =
	// getSearchProviderManager().getSearchProvider(providerId);
	// if (provider == null) {
	// throw new SiteWhereSystemException(ErrorCode.InvalidSearchProviderId,
	// ErrorLevel.ERROR,
	// HttpServletResponse.SC_NOT_FOUND);
	// }
	// if (!(provider instanceof IDeviceEventSearchProvider)) {
	// throw new SiteWhereException("Search provider does not provide event search
	// capability.");
	// }
	// return ((IDeviceEventSearchProvider)
	// provider).executeQueryWithRawResponse(query);
	return Response.ok().build();
    }

    protected IInstanceManagementMicroservice<?> getMicroservice() {
	return microservice;
    }
}