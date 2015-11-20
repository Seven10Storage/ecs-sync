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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.emc.vipr.sync.ViPRSync;
import com.emc.vipr.sync.model.SyncMetadata;
import com.emc.vipr.sync.test.TestObjectSource;
import com.emc.vipr.sync.test.TestObjectTarget;
import com.emc.vipr.sync.test.TestSyncObject;

public class MetadataFilterTest {

    public static final String META1_KEY = "meta1";
    public static final String META1_VAL = "val1";
    public static final String META2_KEY = "meta2";
    public static final String META2_VAL = "val2";    
    public static final String META_NOTPRESENT_KEY = "key3";
    public static final String META_NOTPRESENT_VAL = "val3";    
    public static final String META_EMPTY_VAL = "";
    
    private final int numObjects = 10;
    private final int sizeObjects = 1;
    
    /**
     * @throws Exception
     */
    @Test
    public void testFilterIncludeNotIndexedKeyPresentValueEmpty() throws Exception {
        ViPRSync sync = new ViPRSync();
        Map<String, String> metaFilterMap = new HashMap<String, String>();
        MetadataFilter metaFilter = new MetadataFilter();
        TestObjectSource source = new TestObjectSource(numObjects, sizeObjects, null);
        TestObjectTarget target = new TestObjectTarget();
        for (TestSyncObject object : source.getObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            syncMeta.setUserMetadataValue(META1_KEY, META1_VAL);
        }

        metaFilterMap.put(META1_KEY, META_EMPTY_VAL);
        metaFilter.setMetadataInclude(metaFilterMap);
        sync.setSource(source);
        sync.setFilters(Arrays.asList((SyncFilter) metaFilter));
        sync.setTarget(target);
        sync.run();

        assertEquals(source.getObjects().size(), target.getRootObjects().size());
        for (TestSyncObject object : target.getRootObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            assertNotNull(syncMeta.getUserMetadataValue(META1_KEY));
        }
    }
        
    /**
     * @throws Exception
     */
    @Test
    public void testFilterIncludeNotIndexedKeyPresentValueEqual() throws Exception {
        ViPRSync sync = new ViPRSync();
        Map<String, String> metaFilterMap = new HashMap<String, String>();
        MetadataFilter metaFilter = new MetadataFilter();
        TestObjectSource source = new TestObjectSource(numObjects, sizeObjects, null);
        TestObjectTarget target = new TestObjectTarget();
        for (TestSyncObject object : source.getObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            syncMeta.setUserMetadataValue(META1_KEY, META1_VAL);
        }

        metaFilterMap.put(META1_KEY, META1_VAL);
        metaFilter.setMetadataInclude(metaFilterMap);
        sync.setSource(source);
        sync.setFilters(Arrays.asList((SyncFilter) metaFilter));
        sync.setTarget(target);
        sync.run();

        assertEquals(source.getObjects().size(), target.getRootObjects().size());
        for (TestSyncObject object : target.getRootObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            assertNotNull(syncMeta.getUserMetadataValue(META1_KEY));
        }
    }
    
    /**
     * @throws Exception
     */
    @Test
    public void testFilterIncludeNotIndexedKeyPresentValueNotEqual() throws Exception {
        ViPRSync sync = new ViPRSync();
        Map<String, String> metaFilterMap = new HashMap<String, String>();
        MetadataFilter metaFilter = new MetadataFilter();
        TestObjectSource source = new TestObjectSource(numObjects, sizeObjects, null);
        TestObjectTarget target = new TestObjectTarget();
        for (TestSyncObject object : source.getObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            syncMeta.setUserMetadataValue(META1_KEY, META1_VAL);
        }

        metaFilterMap.put(META1_KEY, META_NOTPRESENT_VAL);
        metaFilter.setMetadataInclude(metaFilterMap);
        sync.setSource(source);
        sync.setFilters(Arrays.asList((SyncFilter) metaFilter));
        sync.setTarget(target);
        sync.run();

        assertEquals(0, target.getRootObjects().size());
    }    
    
    /**
     * @throws Exception
     */
    @Test
    public void testFilterIncludeNotIndexedKeyNotPresentValueEmpty() throws Exception {
        ViPRSync sync = new ViPRSync();
        Map<String, String> metaFilterMap = new HashMap<String, String>();
        MetadataFilter metaFilter = new MetadataFilter();
        TestObjectSource source = new TestObjectSource(numObjects, sizeObjects, null);
        TestObjectTarget target = new TestObjectTarget();
        for (TestSyncObject object : source.getObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            syncMeta.setUserMetadataValue(META1_KEY, META1_VAL);
        }

        metaFilterMap.put(META_NOTPRESENT_KEY, META_EMPTY_VAL);
        metaFilter.setMetadataInclude(metaFilterMap);
        sync.setSource(source);
        sync.setFilters(Arrays.asList((SyncFilter) metaFilter));
        sync.setTarget(target);
        sync.run();

        assertEquals(0, target.getRootObjects().size());
    }

    /**
     * @throws Exception
     */
    @Test
    public void testFilterIncludeNotIndexedKeyNotPresentValueEqual() throws Exception {
        ViPRSync sync = new ViPRSync();
        Map<String, String> metaFilterMap = new HashMap<String, String>();
        MetadataFilter metaFilter = new MetadataFilter();
        TestObjectSource source = new TestObjectSource(numObjects, sizeObjects, null);
        TestObjectTarget target = new TestObjectTarget();
        for (TestSyncObject object : source.getObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            syncMeta.setUserMetadataValue(META1_KEY, META1_VAL, false);
        }

        metaFilterMap.put(META_NOTPRESENT_KEY, META1_VAL);
        metaFilter.setMetadataInclude(metaFilterMap);
        sync.setSource(source);
        sync.setFilters(Arrays.asList((SyncFilter) metaFilter));
        sync.setTarget(target);
        sync.run();

        assertEquals(0, target.getRootObjects().size());        
    }
    
    /**
     * @throws Exception
     */
    @Test
    public void testFilterIncludeNotIndexedKeyNotPresentValueNotEqual() throws Exception {
        ViPRSync sync = new ViPRSync();
        Map<String, String> metaFilterMap = new HashMap<String, String>();
        MetadataFilter metaFilter = new MetadataFilter();
        TestObjectSource source = new TestObjectSource(numObjects, sizeObjects, null);
        TestObjectTarget target = new TestObjectTarget();
        for (TestSyncObject object : source.getObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            syncMeta.setUserMetadataValue(META1_KEY, META1_VAL);
        }

        metaFilterMap.put(META_NOTPRESENT_KEY, META_NOTPRESENT_VAL);
        metaFilter.setMetadataInclude(metaFilterMap);
        sync.setSource(source);
        sync.setFilters(Arrays.asList((SyncFilter) metaFilter));
        sync.setTarget(target);
        sync.run();

        assertEquals(0, target.getRootObjects().size());
    }

    /**
     * @throws Exception
     */
    @Test
    public void testFilterIncludeIndexedKeyPresentValueEmpty() throws Exception {
        ViPRSync sync = new ViPRSync();
        Map<String, String> metaFilterMap = new HashMap<String, String>();
        MetadataFilter metaFilter = new MetadataFilter();
        TestObjectSource source = new TestObjectSource(numObjects, sizeObjects, null);
        TestObjectTarget target = new TestObjectTarget();
        for (TestSyncObject object : source.getObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            syncMeta.setUserMetadataValue(META1_KEY, META1_VAL, true);
        }

        metaFilterMap.put(META1_KEY, META_EMPTY_VAL);
        metaFilter.setListableMetadataInclude(metaFilterMap);
        sync.setSource(source);
        sync.setFilters(Arrays.asList((SyncFilter) metaFilter));
        sync.setTarget(target);
        sync.run();

        assertEquals(source.getObjects().size(), target.getRootObjects().size());
        for (TestSyncObject object : target.getRootObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            assertNotNull(syncMeta.getUserMetadataValue(META1_KEY));
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void testFilterIncludeIndexedKeyPresentValueEqual() throws Exception {
        ViPRSync sync = new ViPRSync();
        Map<String, String> metaFilterMap = new HashMap<String, String>();
        MetadataFilter metaFilter = new MetadataFilter();
        TestObjectSource source = new TestObjectSource(numObjects, sizeObjects, null);
        TestObjectTarget target = new TestObjectTarget();
        for (TestSyncObject object : source.getObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            syncMeta.setUserMetadataValue(META1_KEY, META1_VAL, true);
        }

        metaFilterMap.put(META1_KEY, META1_VAL);
        metaFilter.setListableMetadataInclude(metaFilterMap);
        sync.setSource(source);
        sync.setFilters(Arrays.asList((SyncFilter) metaFilter));
        sync.setTarget(target);
        sync.run();

        assertEquals(source.getObjects().size(), target.getRootObjects().size());
        for (TestSyncObject object : target.getRootObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            assertNotNull(syncMeta.getUserMetadataValue(META1_KEY));
        }
    }
    
    /**
     * @throws Exception
     */
    @Test
    public void testFilterIncludeIndexedKeyPresentValueNotEqual() throws Exception {
        ViPRSync sync = new ViPRSync();
        Map<String, String> metaFilterMap = new HashMap<String, String>();
        MetadataFilter metaFilter = new MetadataFilter();
        TestObjectSource source = new TestObjectSource(numObjects, sizeObjects, null);
        TestObjectTarget target = new TestObjectTarget();
        for (TestSyncObject object : source.getObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            syncMeta.setUserMetadataValue(META1_KEY, META1_VAL, true);
        }

        metaFilterMap.put(META1_KEY, META_NOTPRESENT_VAL);
        metaFilter.setListableMetadataInclude(metaFilterMap);
        sync.setSource(source);
        sync.setFilters(Arrays.asList((SyncFilter) metaFilter));
        sync.setTarget(target);
        sync.run();

        assertEquals(0, target.getRootObjects().size());
    }
    
    /**
     * @throws Exception
     */
    @Test
    public void testFilterIncludeIndexedKeyNotPresentValueEmpty() throws Exception {
        ViPRSync sync = new ViPRSync();
        Map<String, String> metaFilterMap = new HashMap<String, String>();
        MetadataFilter metaFilter = new MetadataFilter();
        TestObjectSource source = new TestObjectSource(numObjects, sizeObjects, null);
        TestObjectTarget target = new TestObjectTarget();
        for (TestSyncObject object : source.getObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            syncMeta.setUserMetadataValue(META1_KEY, META1_VAL, true);
        }

        metaFilterMap.put(META_NOTPRESENT_KEY, META_EMPTY_VAL);
        metaFilter.setListableMetadataInclude(metaFilterMap);
        sync.setSource(source);
        sync.setFilters(Arrays.asList((SyncFilter) metaFilter));
        sync.setTarget(target);
        sync.run();

        assertEquals(0, target.getRootObjects().size());
    }

    /**
     * @throws Exception
     */
    @Test
    public void testFilterIncludeIndexedKeyNotPresentValueEqual() throws Exception {
        ViPRSync sync = new ViPRSync();
        Map<String, String> metaFilterMap = new HashMap<String, String>();
        MetadataFilter metaFilter = new MetadataFilter();
        TestObjectSource source = new TestObjectSource(numObjects, sizeObjects, null);
        TestObjectTarget target = new TestObjectTarget();
        for (TestSyncObject object : source.getObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            syncMeta.setUserMetadataValue(META1_KEY, META1_VAL, true);
        }

        metaFilterMap.put(META_NOTPRESENT_KEY, META1_VAL);
        metaFilter.setListableMetadataInclude(metaFilterMap);
        sync.setSource(source);
        sync.setFilters(Arrays.asList((SyncFilter) metaFilter));
        sync.setTarget(target);
        sync.run();

        assertEquals(0, target.getRootObjects().size());        
    }

    /**
     * @throws Exception
     */
    @Test
    public void testFilterIncludeIndexedKeyNotPresentValueNotEqual() throws Exception {
        ViPRSync sync = new ViPRSync();
        Map<String, String> metaFilterMap = new HashMap<String, String>();
        MetadataFilter metaFilter = new MetadataFilter();
        TestObjectSource source = new TestObjectSource(numObjects, sizeObjects, null);
        TestObjectTarget target = new TestObjectTarget();
        for (TestSyncObject object : source.getObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            syncMeta.setUserMetadataValue(META1_KEY, META1_VAL, true);
        }

        metaFilterMap.put(META_NOTPRESENT_KEY, META_NOTPRESENT_VAL);
        metaFilter.setListableMetadataInclude(metaFilterMap);
        sync.setSource(source);
        sync.setFilters(Arrays.asList((SyncFilter) metaFilter));
        sync.setTarget(target);
        sync.run();

        assertEquals(0, target.getRootObjects().size());
    }

    /**
     * @throws Exception
     */
    @Test
    public void testFilterIncludeIndexedNotIndexedMixed() throws Exception {
        ViPRSync sync = new ViPRSync();
        Map<String, String> metaFilterMap = new HashMap<String, String>();
        MetadataFilter metaFilter = new MetadataFilter();
        TestObjectSource source = new TestObjectSource(numObjects, sizeObjects, null);
        TestObjectTarget target = new TestObjectTarget();
        int i = 0;
        for (TestSyncObject object : source.getObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            // Rotate indexed or not indexed values
            if (i++ % 2 == 0)
                syncMeta.setUserMetadataValue(META1_KEY, META1_VAL, true);
            else
                syncMeta.setUserMetadataValue(META1_KEY, META1_VAL);
        }

        metaFilterMap.put(META1_KEY, META_EMPTY_VAL);
        metaFilter.setListableMetadataInclude(metaFilterMap);
        sync.setSource(source);
        sync.setFilters(Arrays.asList((SyncFilter) metaFilter));
        sync.setTarget(target);
        sync.run();

        // Half of the objects should have been synced and their values indexed
        assertEquals(numObjects / 2, target.getRootObjects().size());
        for (TestSyncObject object : target.getRootObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            assertNotNull(syncMeta.getUserMetadata().get(META1_KEY));
            assertTrue(syncMeta.getUserMetadata().get(META1_KEY).isIndexed());
        }
        
        // Now test non-indexed objects
        metaFilterMap.clear();
        metaFilter = new MetadataFilter();
        source = new TestObjectSource(numObjects, sizeObjects, null);
        target = new TestObjectTarget();
        i = 0;
        for (TestSyncObject object : source.getObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            // Rotate indexed or not indexed values
            if (i++ % 2 == 0)
                syncMeta.setUserMetadataValue(META1_KEY, META1_VAL, true);
            else
                syncMeta.setUserMetadataValue(META1_KEY, META1_VAL);
        }
        metaFilterMap.put(META1_KEY, META_EMPTY_VAL);
        metaFilter.setMetadataInclude(metaFilterMap);
        sync.setSource(source);
        sync.setFilters(Arrays.asList((SyncFilter) metaFilter));
        sync.setTarget(target);
        sync.run();

        // Half of the objects should have been synced and their values indexed
        assertEquals(numObjects / 2, target.getRootObjects().size());
        for (TestSyncObject object : target.getRootObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            assertNotNull(syncMeta.getUserMetadata().get(META1_KEY));
            assertFalse(syncMeta.getUserMetadata().get(META1_KEY).isIndexed());
        }        
    }
    
    /**
     * @throws Exception
     */
    @Test
    public void testFilterIncludeMultiKeyIndexed() throws Exception {
        ViPRSync sync = new ViPRSync();
        Map<String, String> metaFilterMap = new HashMap<String, String>();
        MetadataFilter metaFilter = new MetadataFilter();
        TestObjectSource source = new TestObjectSource(numObjects, sizeObjects, null);
        TestObjectTarget target = new TestObjectTarget();
        int i = 0;
        for (TestSyncObject object : source.getObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            // Rotate keys and values
            if (i++ % 2 == 0)
                syncMeta.setUserMetadataValue(META1_KEY, META1_VAL, true);
            else
                syncMeta.setUserMetadataValue(META2_KEY, META2_VAL, true);
        }

        metaFilterMap.put(META1_KEY, META_EMPTY_VAL);
        metaFilter.setListableMetadataInclude(metaFilterMap);
        sync.setSource(source);
        sync.setFilters(Arrays.asList((SyncFilter) metaFilter));
        sync.setTarget(target);
        sync.run();

        // Half of the objects should have been synced
        assertEquals(numObjects / 2, target.getRootObjects().size());
        for (TestSyncObject object : target.getRootObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            assertNotNull(syncMeta.getUserMetadata().get(META1_KEY));
            assertTrue(syncMeta.getUserMetadata().get(META1_KEY).getValue().contentEquals(META1_VAL));
        }
        
        metaFilter = new MetadataFilter();
        metaFilterMap.clear();
        source = new TestObjectSource(numObjects, sizeObjects, null);
        target = new TestObjectTarget();
        i = 0;
        for (TestSyncObject object : source.getObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            // Rotate keys and values
            if (i++ % 2 == 0)
                syncMeta.setUserMetadataValue(META1_KEY, META1_VAL, true);
            else
                syncMeta.setUserMetadataValue(META2_KEY, META2_VAL, true);
        }
        metaFilterMap.put(META2_KEY, META_EMPTY_VAL);
        metaFilter.setListableMetadataInclude(metaFilterMap);
        sync.setSource(source);
        sync.setFilters(Arrays.asList((SyncFilter) metaFilter));
        sync.setTarget(target);
        sync.run();

        // Half of the objects should have been synced
        assertEquals(numObjects / 2, target.getRootObjects().size());
        for (TestSyncObject object : target.getRootObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            assertNotNull(syncMeta.getUserMetadata().get(META2_KEY));
            assertTrue(syncMeta.getUserMetadata().get(META2_KEY).getValue().contentEquals(META2_VAL));
        }        
    }
    
    /**
     * @throws Exception
     */
    @Test
    public void testFilterIncludeMultiKeyNotIndexed() throws Exception {
        ViPRSync sync = new ViPRSync();
        Map<String, String> metaFilterMap = new HashMap<String, String>();
        MetadataFilter metaFilter = new MetadataFilter();
        TestObjectSource source = new TestObjectSource(numObjects, sizeObjects, null);
        TestObjectTarget target = new TestObjectTarget();
        int i = 0;
        for (TestSyncObject object : source.getObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            // Rotate keys and values
            if (i++ % 2 == 0)
                syncMeta.setUserMetadataValue(META1_KEY, META1_VAL);
            else
                syncMeta.setUserMetadataValue(META2_KEY, META2_VAL);
        }

        metaFilterMap.put(META1_KEY, META_EMPTY_VAL);
        metaFilter.setMetadataInclude(metaFilterMap);
        sync.setSource(source);
        sync.setFilters(Arrays.asList((SyncFilter) metaFilter));
        sync.setTarget(target);
        sync.run();

        // Half of the objects should have been synced
        assertEquals(numObjects / 2, target.getRootObjects().size());
        for (TestSyncObject object : target.getRootObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            assertNotNull(syncMeta.getUserMetadata().get(META1_KEY));
            assertTrue(syncMeta.getUserMetadata().get(META1_KEY).getValue().contentEquals(META1_VAL));
        }
        
        metaFilter = new MetadataFilter();
        metaFilterMap.clear();
        source = new TestObjectSource(numObjects, sizeObjects, null);
        target = new TestObjectTarget();
        i = 0;
        for (TestSyncObject object : source.getObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            // Rotate keys and values
            if (i++ % 2 == 0)
                syncMeta.setUserMetadataValue(META1_KEY, META1_VAL);
            else
                syncMeta.setUserMetadataValue(META2_KEY, META2_VAL);
        }
        metaFilterMap.put(META2_KEY, META_EMPTY_VAL);
        metaFilter.setMetadataInclude(metaFilterMap);
        sync.setSource(source);
        sync.setFilters(Arrays.asList((SyncFilter) metaFilter));
        sync.setTarget(target);
        sync.run();

        // Half of the objects should have been synced and their values indexed
        assertEquals(numObjects / 2, target.getRootObjects().size());
        for (TestSyncObject object : target.getRootObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            assertNotNull(syncMeta.getUserMetadata().get(META2_KEY));
            assertTrue(syncMeta.getUserMetadata().get(META2_KEY).getValue().contentEquals(META2_VAL));
        }        
    }
    
    /**
     * @throws Exception
     */
    @Test
    public void testFilterIncludeMultiValueNotIndexed() throws Exception {
        ViPRSync sync = new ViPRSync();
        Map<String, String> metaFilterMap = new HashMap<String, String>();
        MetadataFilter metaFilter = new MetadataFilter();
        TestObjectSource source = new TestObjectSource(numObjects, sizeObjects, null);
        TestObjectTarget target = new TestObjectTarget();
        int i = 0;
        for (TestSyncObject object : source.getObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            // Rotate values
            if (i++ % 2 == 0)
                syncMeta.setUserMetadataValue(META1_KEY, META1_VAL);
            else
                syncMeta.setUserMetadataValue(META1_KEY, META2_VAL);
        }

        metaFilterMap.put(META1_KEY, META_EMPTY_VAL);
        metaFilter.setMetadataInclude(metaFilterMap);
        sync.setSource(source);
        sync.setFilters(Arrays.asList((SyncFilter) metaFilter));
        sync.setTarget(target);
        sync.run();

        // All of the objects should have been synced
        assertEquals(numObjects, target.getRootObjects().size());
        i = 0;
        for (TestSyncObject object : target.getRootObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            assertNotNull(syncMeta.getUserMetadata().get(META1_KEY));
        }
    }
    
    /**
     * @throws Exception
     */
    @Test
    public void testFilterIncludeMultiValueIndexed() throws Exception {
        ViPRSync sync = new ViPRSync();
        Map<String, String> metaFilterMap = new HashMap<String, String>();
        MetadataFilter metaFilter = new MetadataFilter();
        TestObjectSource source = new TestObjectSource(numObjects, sizeObjects, null);
        TestObjectTarget target = new TestObjectTarget();
        int i = 0;
        for (TestSyncObject object : source.getObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            // Rotate values
            if (i++ % 2 == 0)
                syncMeta.setUserMetadataValue(META1_KEY, META1_VAL, true);
            else
                syncMeta.setUserMetadataValue(META1_KEY, META2_VAL, true);
        }

        metaFilterMap.put(META1_KEY, META_EMPTY_VAL);
        metaFilter.setListableMetadataInclude(metaFilterMap);
        sync.setSource(source);
        sync.setFilters(Arrays.asList((SyncFilter) metaFilter));
        sync.setTarget(target);
        sync.run();

        // All of the objects should have been synced
        assertEquals(numObjects, target.getRootObjects().size());
        i = 0;
        for (TestSyncObject object : target.getRootObjects()) {
            SyncMetadata syncMeta = object.getMetadata();
            assertNotNull(syncMeta.getUserMetadata().get(META1_KEY));
        }
    }    
}
