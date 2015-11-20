/*
 * Copyright 2013 EMC Corporation. All Rights Reserved.
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

import com.emc.vipr.sync.model.SyncMetadata;
import com.emc.vipr.sync.model.SyncMetadata.UserMetadata;
import com.emc.vipr.sync.model.object.SyncObject;
import com.emc.vipr.sync.source.SyncSource;
import com.emc.vipr.sync.target.SyncTarget;
import com.emc.vipr.sync.util.OptionBuilder;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author cwikj
 */
public class MetadataFilter extends SyncFilter {
    private static final Logger l4j = Logger.getLogger(MetadataFilter.class);

    public static final String ACTIVATION_NAME = "metadata";
    public static final String ADD_META_ARG = "name=value,name=value,...";
    
    public static final String ADD_META_OPTION = "add-metadata";
    public static final String ADD_META_DESC = "Adds a regular metadata element to items";

    public static final String INCLUDE_META_OPTION = "include-meta";
    public static final String INCLUDE_META_DESC = "Includes items which have the metadata element values";

    public static final String EXCLUDE_META_OPTION = "exclude-meta";
    public static final String EXCLUDE_META_DESC = "Excludes items which have the metadata element values";

    public static final String ADD_LISTABLE_META_OPTION = "add-listable-meta";
    public static final String ADD_LISTABLE_META_DESC = "Adds a listable metadata element to items";

    public static final String INCLUDE_LISTABLE_META_OPTION = "include-listable-meta";
    public static final String INCLUDE_LISTABLE_META_DESC = "Includes items which have the listable metadata element values";

    public static final String EXCLUDE_LISTABLE_META_OPTION = "exclude-listable-meta";
    public static final String EXCLUDE_LISTABLE_META_DESC = "Excludes items which have the listable metadata element values";

    Map<String, String> metadataAdd = new HashMap<String, String>();
    Map<String, String> metadataInclude = new HashMap<String, String>();
    Map<String, String> metadataExclude = new HashMap<String, String>();
    Map<String, String> listableMetadataAdd = new HashMap<String, String>();
    Map<String, String> listableMetadataInclude = new HashMap<String, String>();
    Map<String, String> listableMetadataExclude = new HashMap<String, String>();

    @Override
    public String getActivationName() {
        return ACTIVATION_NAME;
    }

    @Override
    public Options getCustomOptions() {
        Options opts = new Options();
        opts.addOption(new OptionBuilder().withLongOpt(ADD_META_OPTION).withDescription(ADD_META_DESC)
                .hasArgs().withArgName(ADD_META_ARG).withValueSeparator(',').create());
        opts.addOption(new OptionBuilder().withLongOpt(ADD_LISTABLE_META_OPTION).withDescription(ADD_LISTABLE_META_DESC)
                .hasArgs().withArgName(ADD_META_ARG).withValueSeparator(',').create());
        opts.addOption(new OptionBuilder().withLongOpt(INCLUDE_META_OPTION).withDescription(INCLUDE_META_DESC)
                .hasArgs().withArgName(ADD_META_ARG).withValueSeparator(',').create());
        opts.addOption(new OptionBuilder().withLongOpt(EXCLUDE_META_OPTION).withDescription(EXCLUDE_META_DESC)
                .hasArgs().withArgName(ADD_META_ARG).withValueSeparator(',').create());
        opts.addOption(new OptionBuilder().withLongOpt(INCLUDE_LISTABLE_META_OPTION).withDescription(INCLUDE_LISTABLE_META_DESC)
                .hasArgs().withArgName(ADD_META_ARG).withValueSeparator(',').create());
        opts.addOption(new OptionBuilder().withLongOpt(EXCLUDE_LISTABLE_META_OPTION).withDescription(EXCLUDE_LISTABLE_META_DESC)
                .hasArgs().withArgName(ADD_META_ARG).withValueSeparator(',').create());
        return opts;
    }

    @Override
    protected void parseCustomOptions(CommandLine line) {

        if (line.hasOption(ADD_META_OPTION)) {
            String[] values = line.getOptionValues(ADD_META_OPTION);

            for (String value : values) {
                String[] parts = value.split("=", 2);
                if (parts.length != 2) {
                    // Empty value?
                    metadataAdd.put(parts[0], "");
                } else {
                    metadataAdd.put(parts[0], parts[1]);
                }
            }
        }

        if (line.hasOption(INCLUDE_META_OPTION)) {
            String[] values = line.getOptionValues(INCLUDE_META_OPTION);

            for (String value : values) {
                String[] parts = value.split("=", 2);
                if (parts.length != 2) {
                    // Empty value?
                    metadataInclude.put(parts[0], "");
                } else {
                    metadataInclude.put(parts[0], parts[1]);
                }
            }
        }

        if (line.hasOption(EXCLUDE_META_OPTION)) {
            String[] values = line.getOptionValues(EXCLUDE_META_OPTION);

            for (String value : values) {
                String[] parts = value.split("=", 2);
                if (parts.length != 2) {
                    // Empty value?
                    metadataExclude.put(parts[0], "");
                } else {
                    metadataExclude.put(parts[0], parts[1]);
                }
            }
        }

        if (line.hasOption(ADD_LISTABLE_META_OPTION)) {
            String[] values = line.getOptionValues(ADD_LISTABLE_META_OPTION);

            for (String value : values) {
                String[] parts = value.split("=", 2);
                if (parts.length != 2) {
                    // Empty value?
                    listableMetadataAdd.put(parts[0], "");
                } else {
                    listableMetadataAdd.put(parts[0], parts[1]);
                }
            }
        }

        if (line.hasOption(INCLUDE_LISTABLE_META_OPTION)) {
            String[] values = line.getOptionValues(INCLUDE_LISTABLE_META_OPTION);

            for (String value : values) {
                String[] parts = value.split("=", 2);
                if (parts.length != 2) {
                    // Empty value?
                    listableMetadataInclude.put(parts[0], "");
                } else {
                    listableMetadataInclude.put(parts[0], parts[1]);
                }
            }
        }

        if (line.hasOption(EXCLUDE_LISTABLE_META_OPTION)) {
            String[] values = line.getOptionValues(EXCLUDE_LISTABLE_META_OPTION);

            for (String value : values) {
                String[] parts = value.split("=", 2);
                if (parts.length != 2) {
                    // Empty value?
                    listableMetadataExclude.put(parts[0], "");
                } else {
                    listableMetadataExclude.put(parts[0], parts[1]);
                }
            }
        }        
    }

    @Override
    public void configure(SyncSource source, Iterator<SyncFilter> filters, SyncTarget target) {
    }

    @Override
    public void filter(SyncObject obj) {

        SyncMetadata meta = obj.getMetadata();

        // Check include/exclude filters first. Return from here ensures object is stopped from processing in the chain.

        for (String key : metadataInclude.keySet()) {            
            l4j.debug(String.format("checking include of metadata %s=%s to %s", key, metadataInclude.get(key), obj));

            Map<String, UserMetadata> map = meta.getUserMetadata();

            // If this metadata item doesn't exist, or its indexed value doesn't match, ignore it.
            if ((map.get(key) == null)
                    ||  (map.get(key).isIndexed() != false)) {            
                return;
            }
            else {
                // If the including value is empty, accept all values, otherwise compare
                if ((!metadataInclude.get(key).contentEquals("")) 
                    && (!metadataInclude.get(key).contentEquals(map.get(key).getValue()))) {
                        return;
                }
            }
        }

        for (String key : listableMetadataInclude.keySet()) {            
            l4j.debug(String.format("checking include of listable metadata %s=%s to %s", key, listableMetadataInclude.get(key), obj));

            Map<String, UserMetadata> map = meta.getUserMetadata();

            // If this metadata item doesn't exist, or its indexed value doesn't match, ignore it.
            if ((map.get(key) == null)
                    ||  (map.get(key).isIndexed() != true)) {            
                return;
            }
            else {
                // If the including value is empty, accept all values, otherwise compare
                if ((!listableMetadataInclude.get(key).contentEquals("")) 
                    && (!listableMetadataInclude.get(key).contentEquals(map.get(key).getValue()))) {
                        return;
                }
            }
        }

        for (String key : metadataExclude.keySet()) {            
            l4j.debug(String.format("checking exclude of metadata %s=%s to %s", key, metadataExclude.get(key), obj));

            Map<String, UserMetadata> map = meta.getUserMetadata();

            // If this metadata item doesn't exist, or its indexed value doesn't match, do not ignore it
            if ((map.get(key) != null) 
                && (map.get(key).isIndexed() == false)) {
                // If the excluded value is empty, ignore all values. If the excluded value matches, ignore it also.
                if ((metadataExclude.get(key).contentEquals(""))
                    || (metadataExclude.get(key).contentEquals(map.get(key).getValue()))) {
                        return;
                }
            }
        }

        for (String key : listableMetadataExclude.keySet()) {            
            l4j.debug(String.format("checking exclude of listable metadata %s=%s to %s", key, listableMetadataExclude.get(key), obj));

            Map<String, UserMetadata> map = meta.getUserMetadata();

            // If this metadata item doesn't exist, or its indexed value doesn't match, do not ignore it
            if ((map.get(key) != null) 
                && (map.get(key).isIndexed() == true)) {
                // If the excluded value is empty, ignore all values. If the excluded value matches, ignore it also.
                if ((listableMetadataExclude.get(key).contentEquals(""))
                    || (listableMetadataExclude.get(key).contentEquals(map.get(key).getValue()))) {
                        return;
                }
            }
        }

        for (String key : metadataAdd.keySet()) {
            l4j.debug(String.format("adding metadata %s=%s to %s", key, metadataAdd.get(key), obj.getSourceIdentifier()));
            meta.setUserMetadataValue(key, metadataAdd.get(key));
        }

        for (String key : listableMetadataAdd.keySet()) {
            l4j.debug(String.format("adding listable metadata %s=%s to %s", key, metadataAdd.get(key), obj));
            meta.setUserMetadataValue(key, metadataAdd.get(key), true);
        }

        getNext().filter(obj);
    }

    // TODO: if verification ever includes metadata, remove added metadata, here
    @Override
    public SyncObject reverseFilter(SyncObject obj) {
        return getNext().reverseFilter(obj);
    }

    @Override
    public String getName() {
        return "Metadata Filter";
    }

    @Override
    public String getDocumentation() {
        return "Allows adding regular and listable (Atmos only) metadata to each object";
    }

    /**
     * @return the metadataAdd
     */
    public Map<String, String> getMetadataAdd() {
        return metadataAdd;
    }

    /**
     * @param metadataAdd the metadataAdd to set
     */
    public void setMetadataAdd(Map<String, String> metadataAdd) {
        this.metadataAdd = metadataAdd;
    }

    /**
     * @return the metadataInclude
     */
    public Map<String, String> getMetadataInclude() {
        return metadataInclude;
    }

    /**
     * @param metadataInclude the metadataInclude to set
     */
    public void setMetadataInclude(Map<String, String> metadataInclude) {
        this.metadataInclude = metadataInclude;
    }

    /**
     * @return the metadataExclude
     */
    public Map<String, String> getMetadataExclude() {
        return metadataExclude;
    }

    /**
     * @param metadataExclude the metadataExclude to set
     */
    public void setMetadataExclude(Map<String, String> metadataExclude) {
        this.metadataExclude = metadataExclude;
    }

    /**
     * @return the listableMetadataAdd
     */
    public Map<String, String> getListableMetadataAdd() {
        return listableMetadataAdd;
    }

    /**
     * @param listableMetadataAdd the listableMetadataAdd to set
     */
    public void setListableMetadataAdd(Map<String, String> listableMetadataAdd) {
        this.listableMetadataAdd = listableMetadataAdd;
    }

    /**
     * @return the listableMetadataInclude
     */
    public Map<String, String> getListableMetadataInclude() {
        return listableMetadataInclude;
    }

    /**
     * @param listableMetadataInclude the listableMetadataInclude to set
     */
    public void setListableMetadataInclude(Map<String, String> listableMetadataInclude) {
        this.listableMetadataInclude = listableMetadataInclude;
    }

    /**
     * @return the listableMetadataExclude
     */
    public Map<String, String> getListableMetadataExclude() {
        return listableMetadataExclude;
    }

    /**
     * @param listableMetadataExclude the listableMetadataExclude to set
     */
    public void setListableMetadataExclude(Map<String, String> listableMetadataExclude) {
        this.listableMetadataExclude = listableMetadataExclude;
    }
}
