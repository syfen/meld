package com.syfen.zookeeper;

import com.netflix.config.ConfigurationManager;
import com.netflix.curator.ensemble.exhibitor.Exhibitors;
import com.syfen.zookeeper.util.PropertyParser;
import com.syfen.zookeeper.util.S3PropertiesFile;
import org.apache.commons.configuration.AbstractConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * User: ToneD
 * Created: 25/08/13 10:01 AM
 */
public class ZookeeperBackupConnectionStringProvider implements Exhibitors.BackupConnectionStringProvider {

    private static final Logger log = LoggerFactory.getLogger(ZookeeperBackupConnectionStringProvider.class);
    private static final AbstractConfiguration config = ConfigurationManager.getConfigInstance();

    @Override
    public String getBackupConnectionString() throws Exception {

        // get exhibitor shared properties file
        S3PropertiesFile props = new S3PropertiesFile(
                config.getString(Constants.ZK_CONFIG_EXHIBITOR_S3BUCKET),
                config.getString(Constants.ZK_CONFIG_EXHIBITOR_S3KEY),
                config.getString(Constants.AWS_ACCESS_KEY),
                config.getString(Constants.AWS_SECRET_KEY));

        return PropertyParser.connectionString(
                props.get(Constants.ZK_CONFIG_EXHIBITOR_SERVER_SPEC),
                config.getInt(Constants.ZK_CONFIG_CLIENT_PORT));
    }
}
