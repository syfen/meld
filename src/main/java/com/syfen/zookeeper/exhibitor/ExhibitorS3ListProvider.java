package com.syfen.zookeeper.exhibitor;

import com.netflix.config.ConfigurationManager;
import com.syfen.zookeeper.Constants;
import com.syfen.zookeeper.exceptions.PropertyNotFoundException;
import com.syfen.zookeeper.util.PropertyParser;
import com.syfen.zookeeper.util.S3PropertiesFile;
import org.apache.commons.configuration.AbstractConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: ToneD
 * Created: 23/10/13 11:17 PM
 */
public class ExhibitorS3ListProvider implements ExhibitorListProvider {

    private static final Logger log = LoggerFactory.getLogger(ExhibitorListProvider.class);
    private static final AbstractConfiguration config = ConfigurationManager.getConfigInstance();

    private List<String> list = null;

    public List<String> getExhibitorList() {

        if(list == null) {

            list = new ArrayList<>();

            // get exhibitor shared properties file
            S3PropertiesFile props = new S3PropertiesFile(
                    config.getString(Constants.ZK_CONFIG_EXHIBITOR_S3BUCKET),
                    config.getString(Constants.ZK_CONFIG_EXHIBITOR_S3KEY),
                    config.getString(Constants.ZK_CONFIG_EXHIBITOR_AWS_ACCESS_KEY),
                    config.getString(Constants.ZK_CONFIG_EXHIBITOR_AWS_SECRET_KEY));

            try {
                log.debug("Exhibitor server spec: " + props.get(Constants.ZK_CONFIG_EXHIBITOR_SERVER_SPEC));
                list.addAll(PropertyParser.serverSpecList(props.get(Constants.ZK_CONFIG_EXHIBITOR_SERVER_SPEC)));
            }
            catch(PropertyNotFoundException e) {
                log.error(e.getMessage());
            }

            log.debug("Exhibitor list: " + list.toString());

            Collections.sort(list);
        }

        return list;
    }
}
