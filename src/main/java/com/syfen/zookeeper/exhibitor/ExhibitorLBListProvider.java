package com.syfen.zookeeper.exhibitor;

import com.google.gson.Gson;
import com.netflix.config.ConfigurationManager;
import com.syfen.zookeeper.Constants;
import com.syfen.zookeeper.model.ZookeeperClusterList;
import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ToneD
 * Created: 24/05/2014 9:20 AM
 */
public class ExhibitorLBListProvider implements ExhibitorListProvider {

    private static final Logger log = LoggerFactory.getLogger(ExhibitorLBListProvider.class);
    private static final AbstractConfiguration config = ConfigurationManager.getConfigInstance();

    private List<String> list = new ArrayList<>();

    public List<String> getExhibitorList() {

        DefaultHttpClient client = new DefaultHttpClient();

        try {
            //build exhibitor uri
            StringBuilder uri = new StringBuilder()
                    .append(config.getString(Constants.ZK_CONFIG_EXHIBITOR_HTTP_SCHEME))
                    .append("://")
                    .append(config.getString(Constants.ZK_CONFIG_EXHIBITOR_HOST))
                    .append(":")
                    .append(config.getString(Constants.ZK_CONFIG_EXHIBITOR_PORT))
                    .append(config.getString(Constants.ZK_CONFIG_EXHIBITOR_PATH_CLUSTER_LIST));

            // make request
            HttpGet getReq = new HttpGet(uri.toString());
            HttpResponse response = client.execute(getReq);
            String responseContent = EntityUtils.toString(response.getEntity());
            EntityUtils.consumeQuietly(response.getEntity());

            // parse response
            ZookeeperClusterList clusterList = new Gson().fromJson(responseContent, ZookeeperClusterList.class);
            list = clusterList.getServers();
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }

        return list;
    }
}
