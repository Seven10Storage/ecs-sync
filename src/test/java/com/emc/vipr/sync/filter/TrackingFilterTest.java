/*
 * Copyright 2015 EMC Corporation. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.emc.vipr.sync.filter;

import com.emc.vipr.sync.filter.TrackingFilter.StatusProperty;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TrackingFilterTest {
    public static final String META1 = "size";
    public static final String META2 = "mtime";

    BasicDataSource dataSource;
    TrackingFilter trackingFilter;

    @Before
    public void setup() throws Exception {
        // Initialize a DB connection pool
        dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:hsqldb:mem:TRACKING_FILTER_TEST");
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");

        trackingFilter = new TrackingFilter();
        trackingFilter.setDataSource(dataSource);
        trackingFilter.setMetaTags(Arrays.asList(META1, META2));
        trackingFilter.setCreateTable(true);

        // create table
        trackingFilter.configure(null, null, null);
    }

    @After
    public void teardown() throws Exception {
        if (dataSource != null) {
            Connection con = dataSource.getConnection();
            con.createStatement().execute("SHUTDOWN");
            con.close();
        }
        trackingFilter = null;
        dataSource = null;
    }

    @Test
    public void testRowInsert() throws Exception {
        // test with various parameters and verify result
        String id = "1";
        Map<StatusProperty, Object> properties = new HashMap<StatusProperty, Object>();
        properties.put(StatusProperty.status, "Foo");
        trackingFilter.statusInsert(id, properties);
        verify(trackingFilter, id, properties, null);

        try {
            id = "2";
            properties = new HashMap<StatusProperty, Object>();
            properties.put(StatusProperty.started_at, 1500000000000L);
            trackingFilter.statusInsert(id, properties);
            verify(trackingFilter, id, properties, null);
            Assert.fail("status should be required");
        } catch (DataIntegrityViolationException e) {
            // expected
        }

        id = "3";
        properties = new HashMap<StatusProperty, Object>();
        properties.put(StatusProperty.status, "Foo");
        properties.put(StatusProperty.started_at, 1500000000000L);
        trackingFilter.statusInsert(id, properties);
        verify(trackingFilter, id, properties, null);

        id = "4";
        properties = new HashMap<StatusProperty, Object>();
        properties.put(StatusProperty.status, "Foo");
        properties.put(StatusProperty.completed_at, 1500000000000L);
        trackingFilter.statusInsert(id, properties);
        verify(trackingFilter, id, properties, null);

        id = "5";
        properties = new HashMap<StatusProperty, Object>();
        properties.put(StatusProperty.status, "Foo");
        properties.put(StatusProperty.verified_at, 1500000000000L);
        trackingFilter.statusInsert(id, properties);
        verify(trackingFilter, id, properties, null);

        id = "6";
        properties = new HashMap<StatusProperty, Object>();
        properties.put(StatusProperty.status, "Foo");
        properties.put(StatusProperty.message, "Hello Tracking!");
        trackingFilter.statusInsert(id, properties);
        verify(trackingFilter, id, properties, null);

        id = "7";
        properties = new HashMap<StatusProperty, Object>();
        properties.put(StatusProperty.status, "Foo");
        properties.put(StatusProperty.target_id, "Bar");
        trackingFilter.statusInsert(id, properties);
        verify(trackingFilter, id, properties, null);

        id = "8";
        properties = new HashMap<StatusProperty, Object>();
        Map<String, String> meta = new HashMap<String, String>();
        meta.put(META1, "12345");
        meta.put(META2, "2020-02-02T02:20:00Z");
        properties.put(StatusProperty.status, "Foo");
        properties.put(StatusProperty.meta, meta);
        trackingFilter.statusInsert(id, properties);
        verify(trackingFilter, id, properties, null);
    }

    @Test
    public void testRowUpdate() throws Exception {
        Map<String, String> meta = new HashMap<String, String>();
        meta.put(META1, "12345");
        meta.put(META2, "2020-02-02T02:20:00Z");

        // test with various parameters and verify result
        String id = "1";
        Map<StatusProperty, Object> properties = new HashMap<StatusProperty, Object>();
        properties.put(StatusProperty.status, "Foo");
        properties.put(StatusProperty.started_at, 1500000000000L);
        properties.put(StatusProperty.completed_at, 1500000000000L);
        properties.put(StatusProperty.verified_at, 1500000000000L);
        properties.put(StatusProperty.target_id, "Target");
        properties.put(StatusProperty.message, "Hello Tracking!");
        properties.put(StatusProperty.meta, meta);
        trackingFilter.statusInsert(id, properties);

        trackingFilter.statusUpdate(id, properties); // reset properties
        Map<StatusProperty, Object> newProps = new HashMap<StatusProperty, Object>();
        newProps.put(StatusProperty.status, "Bar");
        trackingFilter.statusUpdate(id, newProps);
        verify(trackingFilter, id, newProps, properties);

        trackingFilter.statusUpdate(id, properties); // reset properties
        newProps = new HashMap<StatusProperty, Object>();
        newProps.put(StatusProperty.started_at, 1500000001111L);
        trackingFilter.statusUpdate(id, newProps);
        verify(trackingFilter, id, newProps, properties);

        trackingFilter.statusUpdate(id, properties); // reset properties
        newProps = new HashMap<StatusProperty, Object>();
        newProps.put(StatusProperty.completed_at, 1500000001111L);
        trackingFilter.statusUpdate(id, newProps);
        verify(trackingFilter, id, newProps, properties);

        trackingFilter.statusUpdate(id, properties); // reset properties
        newProps = new HashMap<StatusProperty, Object>();
        newProps.put(StatusProperty.verified_at, 1500000001111L);
        trackingFilter.statusUpdate(id, newProps);
        verify(trackingFilter, id, newProps, properties);

        trackingFilter.statusUpdate(id, properties); // reset properties
        newProps = new HashMap<StatusProperty, Object>();
        newProps.put(StatusProperty.target_id, "My Target 2");
        trackingFilter.statusUpdate(id, newProps);
        verify(trackingFilter, id, newProps, properties);

        trackingFilter.statusUpdate(id, properties); // reset properties
        newProps = new HashMap<StatusProperty, Object>();
        newProps.put(StatusProperty.message, "Hello Updated Message!");
        trackingFilter.statusUpdate(id, newProps);
        verify(trackingFilter, id, newProps, properties);

        trackingFilter.statusUpdate(id, properties); // reset properties
        newProps = new HashMap<StatusProperty, Object>();
        Map<String, String> newMeta = new HashMap<String, String>();
        newMeta.put(META1, "98765");
        newMeta.put(META2, "1970-01-01T00:00:00Z");
        newProps.put(StatusProperty.meta, newMeta);
        trackingFilter.statusUpdate(id, newProps);
        verify(trackingFilter, id, newProps, properties);
    }

    @SuppressWarnings("unchecked")
    private void verify(TrackingFilter filter, String id, Map<StatusProperty, Object> properties,
                        Map<StatusProperty, Object> oldProperties) {
        if (oldProperties == null) oldProperties = new HashMap<StatusProperty, Object>();
        Object expectedValue;
        SqlRowSet rowSet = filter.getExistingStatus(id);
        Assert.assertTrue(rowSet.next());

        for (StatusProperty property : new StatusProperty[]
                {StatusProperty.target_id, StatusProperty.status, StatusProperty.message}) {
            expectedValue = properties.containsKey(property) ? properties.get(property) : oldProperties.get(property);
            Assert.assertEquals(expectedValue, rowSet.getString(property.toString()));
        }

        for (StatusProperty property : new StatusProperty[]
                {StatusProperty.started_at, StatusProperty.completed_at, StatusProperty.verified_at}) {
            expectedValue = properties.containsKey(property) ? properties.get(property) : oldProperties.get(property);
            if (expectedValue == null) Assert.assertNull(rowSet.getTimestamp(property.toString()));
            else Assert.assertEquals(new Timestamp((Long) expectedValue), rowSet.getTimestamp(property.toString()));
        }

        Map<String, String> meta = properties.containsKey(StatusProperty.meta) ?
                (Map<String, String>) properties.get(StatusProperty.meta) :
                (Map<String, String>) oldProperties.get(StatusProperty.meta);
        if (meta != null) {
            for (String key : meta.keySet()) {
                Assert.assertEquals(meta.get(key), rowSet.getString(key));
            }
        }
    }
}
