package com.syfen.zookeeper;

import com.netflix.config.ConfigurationManager;
import com.syfen.zookeeper.util.PropertyParser;
import org.apache.commons.configuration.AbstractConfiguration;
import com.syfen.zookeeper.util.S3PropertiesFile;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: ToneD
 * Created: 25/08/13 2:15 PM
 */
public class ExhibitorListProvider {

    private static final AbstractConfiguration config = ConfigurationManager.getConfigInstance();

    public static Collection<String> getExhibitorList() {

        Collection<String> list = new ArrayList<String>();

        // get exhibitor shared properties file
        S3PropertiesFile props = new S3PropertiesFile(
                config.getString(Constants.ZK_CONFIG_EXHIBITOR_S3BUCKET),
                config.getString(Constants.ZK_CONFIG_EXHIBITOR_S3KEY),
                config.getString(Constants.AWS_ACCESS_KEY),
                config.getString(Constants.AWS_SECRET_KEY));

        list.addAll(PropertyParser.serverSpecList(props.get(Constants.ZK_CONFIG_EXHIBITOR_SERVER_SPEC)));

        return list;
    }
}