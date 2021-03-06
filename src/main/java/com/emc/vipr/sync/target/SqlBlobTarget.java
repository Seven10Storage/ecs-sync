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
package com.emc.vipr.sync.target;

import com.emc.vipr.sync.filter.SyncFilter;
import com.emc.vipr.sync.model.object.SyncObject;
import com.emc.vipr.sync.source.SyncSource;
import com.emc.vipr.sync.util.SyncUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Inserts objects into a SQL database as BLOBs.
 *
 * @author cwikj
 */
public class SqlBlobTarget extends SyncTarget {
    private static final Logger l4j = Logger.getLogger(SqlBlobTarget.class);

    private DataSource dataSource;
    private String insertSql;

    @Override
    public boolean canHandleTarget(String targetUri) {
        return false; // this plug-in is not CLI capable
    }

    @Override
    public Options getCustomOptions() {
        return new Options();
    }

    @Override
    protected void parseCustomOptions(CommandLine line) {
    }

    @Override
    public void configure(SyncSource source, Iterator<SyncFilter> filters, SyncTarget target) {
    }

    @Override
    public void filter(SyncObject obj) {
        if (obj.isDirectory()) {
            return;
        }
        Connection con = null;
        PreparedStatement ps = null;
        InputStream in = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement(insertSql);
            in = obj.getInputStream();
            ps.setBinaryStream(1, in, obj.getMetadata().getSize());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert into database: " + e.getMessage(), e);
        } finally {
            SyncUtil.safeClose(in);
            try {
                if (ps != null) ps.close();
            } catch (Throwable t) {
                l4j.warn("could not close resource", t);
            }
            try {
                if (con != null) con.close();
            } catch (Throwable t) {
                l4j.warn("could not close resource", t);
            }
        }
    }

    @Override
    public SyncObject reverseFilter(SyncObject obj) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " does not yet support reverse filters (verification)");
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDocumentation() {
        return null;
    }

    /**
     * @return the dataSource
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * @param dataSource the dataSource to set
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @return the insertSql
     */
    public String getInsertSql() {
        return insertSql;
    }

    /**
     * @param insertSql the insertSql to set
     */
    public void setInsertSql(String insertSql) {
        this.insertSql = insertSql;
    }
}
