package com.syfen.zookeeper;

import com.netflix.curator.framework.CuratorFramework;

/**
 * User: ToneD
 * Created: 25/08/13 12:26 PM
 */
public class ZookeeperClientCacheItem {

    private final String ensembleLabel;
    private final CuratorFramework client;

    public ZookeeperClientCacheItem(String ensemble, CuratorFramework zkClient) {

        ensembleLabel = ensemble;
        client = zkClient;
    }

    public String getEnsembleLabel() {
        return ensembleLabel;
    }

    public CuratorFramework getClient() {
        return client;
    }
}