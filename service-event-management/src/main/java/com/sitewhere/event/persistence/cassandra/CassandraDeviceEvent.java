/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.event.persistence.cassandra;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Row;
import com.sitewhere.rest.model.device.event.DeviceEvent;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.device.event.DeviceEventType;
import com.sitewhere.spi.device.event.IDeviceEvent;

public class CassandraDeviceEvent {

    // Device id field.
    public static final String FIELD_DEVICE_ID = "deviceId";

    // Event id field.
    public static final String FIELD_EVENT_ID = "eventId";

    // Alternate id field.
    public static final String FIELD_ALTERNATE_ID = "alternateId";

    // Event type field.
    public static final String FIELD_EVENT_TYPE = "eventType";

    // Assignment id field.
    public static final String FIELD_ASSIGNMENT_ID = "assignmentId";

    // Area id field.
    public static final String FIELD_AREA_ID = "areaId";

    // Asset id field.
    public static final String FIELD_ASSET_ID = "assetId";

    // Event date field.
    public static final String FIELD_EVENT_DATE = "eventDate";

    // Received date field.
    public static final String FIELD_RECEIVED_DATE = "receivedDate";

    /**
     * Bind fields from a device event to an existing {@link BoundStatement}.
     * 
     * @param bound
     * @param event
     * @throws SiteWhereException
     */
    public static void bindEventFields(BoundStatement bound, IDeviceEvent event) throws SiteWhereException {
	bound.setUUID(FIELD_DEVICE_ID, event.getDeviceId());
	bound.setUUID(FIELD_EVENT_ID, event.getId());
	if (event.getAlternateId() != null) {
	    bound.setString(FIELD_ALTERNATE_ID, event.getAlternateId());
	}
	bound.setByte(FIELD_EVENT_TYPE, getIndicatorForEventType(event.getEventType()));
	bound.setUUID(FIELD_ASSIGNMENT_ID, event.getDeviceAssignmentId());
	if (event.getAreaId() != null) {
	    bound.setUUID(FIELD_AREA_ID, event.getAreaId());
	}
	if (event.getAssetId() != null) {
	    bound.setUUID(FIELD_ASSET_ID, event.getAssetId());
	}
	bound.setTimestamp(FIELD_EVENT_DATE, event.getEventDate());
	bound.setTimestamp(FIELD_RECEIVED_DATE, event.getReceivedDate());
    }

    /**
     * Load fields from a row into a device event.
     * 
     * @param event
     * @param row
     * @throws SiteWhereException
     */
    public static void loadEventFields(DeviceEvent event, Row row) throws SiteWhereException {
	event.setDeviceId(row.getUUID(FIELD_DEVICE_ID));
	event.setId(row.getUUID(FIELD_EVENT_ID));
	event.setAlternateId(row.getString(FIELD_ALTERNATE_ID));
	event.setEventType(getEventTypeForIndicator(row.getByte(FIELD_EVENT_TYPE)));
	event.setDeviceAssignmentId(row.getUUID(FIELD_ASSIGNMENT_ID));
	event.setAreaId(row.getUUID(FIELD_AREA_ID));
	event.setAssetId(row.getUUID(FIELD_ASSET_ID));
	event.setEventDate(row.getTimestamp(FIELD_EVENT_DATE));
	event.setReceivedDate(row.getTimestamp(FIELD_RECEIVED_DATE));
    }

    /**
     * Get indicator value for event type.
     * 
     * @param type
     * @return
     * @throws SiteWhereException
     */
    public static Byte getIndicatorForEventType(DeviceEventType type) throws SiteWhereException {
	switch (type) {
	case Measurements: {
	    return 0;
	}
	case Location: {
	    return 1;
	}
	case Alert: {
	    return 2;
	}
	default: {
	    throw new SiteWhereException("Unsupported event type: " + type.name());
	}
	}
    }

    /**
     * Get type for indicator value.
     * 
     * @param value
     * @return
     * @throws SiteWhereException
     */
    public static DeviceEventType getEventTypeForIndicator(Byte value) throws SiteWhereException {
	if (value == 0) {
	    return DeviceEventType.Measurements;
	} else if (value == 1) {
	    return DeviceEventType.Location;
	} else if (value == 2) {
	    return DeviceEventType.Alert;
	}
	throw new SiteWhereException("Unsupported event type: " + value);
    }
}