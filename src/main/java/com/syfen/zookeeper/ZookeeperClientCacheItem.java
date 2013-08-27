package com.syfen.zookeeper;

import com.netflix.curator.framework.CuratorFramework;

/**
 * User: ToneD
 * Created: 25/08/13 12:26 PM
 */
public class ZookeeperClientCacheItem {

    public final String ensembleLabel;
    public final CuratorFramework client;

    public ZookeeperClientCacheItem(String ensembleLabel, CuratorFramework client) {

        this.ensembleLabel = ensembleLabel;
        this.client = client;
    }
}