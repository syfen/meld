package com.syfen.zookeeper;

import com.netflix.curator.framework.CuratorFramework;
import com.syfen.zookeeper.exhibitor.ExhibitorListProvider;
import java.util.List;

/**
 * User: ToneD
 * Created: 25/08/13 12:26 PM
 */
public class ZookeeperClientCacheItem {

    private final String ensembleLabel;
    private final CuratorFramework client;
    private final ExhibitorListProvider exhibitorListProvider;

    public ZookeeperClientCacheItem(String ensemble, CuratorFramework zkClient, ExhibitorListProvider listProvider) {

        ensembleLabel = ensemble;
        client = zkClient;
        exhibitorListProvider = listProvider;
    }

    public String getEnsembleLabel() {
        return ensembleLabel;
    }

    public CuratorFramework getClient() {
        return client;
    }

    public List<String> getExhibitorList() {
        return exhibitorListProvider.getExhibitorList();
    }
}