package com.syfen.zookeeper;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicWatchedConfiguration;
import com.netflix.config.source.ZooKeeperConfigurationSource;
import com.netflix.curator.ensemble.exhibitor.DefaultExhibitorRestClient;
import com.netflix.curator.ensemble.exhibitor.ExhibitorEnsembleProvider;
import com.netflix.curator.ensemble.exhibitor.Exhibitors;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.imps.CuratorFrameworkState;
import com.netflix.curator.retry.ExponentialBackoffRetry;
import com.syfen.zookeeper.exhibitor.ExhibitorListProvider;
import org.apache.commons.configuration.AbstractConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * User: ToneD
 * Created: 25/08/13 12:24 PM
 */
public class ZookeeperClientFactory {

    private static final Logger log = LoggerFactory.getLogger(ZookeeperClientFactory.class);
    private static final AbstractConfiguration config = ConfigurationManager.getConfigInstance();
    public static final Cache<String, ZookeeperClientCacheItem> cache = CacheBuilder.newBuilder().concurrencyLevel(64)
            .build();

    /**
     * Get a started ZK client
     */
    public static CuratorFramework getStartedZKClient(ExhibitorListProvider exhibitorListProvider, String namespace) {

        String cacheKey = namespace + "-" + exhibitorListProvider.getExhibitorList().toString();
        ZookeeperClientCacheItem cachedItem = cache.getIfPresent(cacheKey);
        if (cachedItem != null) {
            return cachedItem.getClient();
        }
        return createAndStartZKClient(exhibitorListProvider, namespace);
    }

    public static Cache<String, ZookeeperClientCacheItem> getClients() {

        return cache;
    }

    /**
     * Initialize an Archaius Zookeeper Configuration from a started ZK client
     */
    public static void initializeAndStartZkConfigSource(ExhibitorListProvider exhibitorListProvider, String namespace,
                                                        String zkConfigRootPath) throws Exception {

        // ZooKeeper Dynamic Override Properties
        CuratorFramework client = ZookeeperClientFactory.getStartedZKClient(exhibitorListProvider, namespace);

        if (client.getState() != CuratorFrameworkState.STARTED) {
            throw new RuntimeException("ZooKeeper located at " + exhibitorListProvider.getExhibitorList().toString()
                    + " is not started.");
        }

        // Create Zookeeper configuration source
        ZooKeeperConfigurationSource zookeeperConfigSource = new ZooKeeperConfigurationSource(
                client, zkConfigRootPath);
        zookeeperConfigSource.start();

        // Create new watched configuration
        DynamicWatchedConfiguration zookeeperDynamicConfig = new DynamicWatchedConfiguration(
                zookeeperConfigSource);

        // insert ZK DynamicConfig into the 2nd spot
        ((ConcurrentCompositeConfiguration) config).addConfigurationAtIndex(
                zookeeperDynamicConfig, "zk dynamic override", 1);
    }

    /**
     * Create and start a zkclient if needed
     */
    private synchronized static CuratorFramework createAndStartZKClient(ExhibitorListProvider exhibitorListProvider,
                                                                        String namespace) {

        ZookeeperClientCacheItem cachedItem = cache.getIfPresent(exhibitorListProvider.getExhibitorList().toString());
        if (cachedItem != null) {
            return cachedItem.getClient();
        }

        String zkConfigExhibitorPathClusterList = config.getString(Constants.ZK_CONFIG_EXHIBITOR_PATH_CLUSTER_LIST);
        Integer zkConfigExhibitorPort = config.getInt(Constants.ZK_CONFIG_EXHIBITOR_PORT_NODE);
        Integer zkConfigExhibitorPollInterval = config.getInt(Constants.ZK_CONFIG_EXHIBITOR_POLL_INTERVAL);

        // create ensemble provider
        Exhibitors exhibitors = new Exhibitors(exhibitorListProvider.getExhibitorList(), zkConfigExhibitorPort,
                new ZookeeperBackupConnectionStringProvider());

        ExponentialBackoffRetry rp = new ExponentialBackoffRetry(1000, 3);
        ExhibitorEnsembleProvider ep = new ExhibitorEnsembleProvider(exhibitors,
                new DefaultExhibitorRestClient(config.getBoolean(Constants.ZK_CONFIG_EXHIBITOR_SSL_NODE, false)),
                zkConfigExhibitorPathClusterList, zkConfigExhibitorPollInterval, rp);

        try {
            ep.pollForInitialEnsemble();
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }

        // create curator client
        CuratorFramework client = CuratorFrameworkFactory.builder().namespace(namespace).ensembleProvider(ep)
                .retryPolicy(rp).build();
        client.start();

        String cacheKey = namespace + "-" + exhibitorListProvider.getExhibitorList().toString();
        cache.put(cacheKey, new ZookeeperClientCacheItem(cacheKey, client,
                exhibitorListProvider));

        log.info("Created, started, and cached zk client [{}] for ensemble [{}]", client,
                exhibitorListProvider.getExhibitorList().toString());

        return client;
    }
}