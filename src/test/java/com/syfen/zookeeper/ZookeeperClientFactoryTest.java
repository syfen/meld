package com.syfen.zookeeper;

import com.netflix.config.ConfigurationManager;
import com.syfen.zookeeper.exhibitor.ExhibitorLBListProvider;
import com.syfen.zookeeper.exhibitor.ExhibitorS3ListProvider;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: ToneD
 * Created: 26/05/2014 9:05 PM
 */
public class ZookeeperClientFactoryTest {

    private static final Logger log = LoggerFactory.getLogger(ZookeeperClientFactoryTest.class);

    @Test
    public void testLBListProvider() {

        String zkConfigRootPath = "/" + ConfigurationManager.getDeploymentContext().getDeploymentEnvironment();

        try {
            ZookeeperClientFactory.initializeAndStartZkConfigSource(new ExhibitorLBListProvider(),
                    ConfigurationManager.getDeploymentContext().getApplicationId(), zkConfigRootPath);
        }
        catch(Exception e) {
            log.error(e.getMessage());
        }
    }

    @Test
    public void testS3ListProvider() {

        String zkConfigRootPath = "/" + ConfigurationManager.getDeploymentContext().getDeploymentEnvironment();

        try {
            ZookeeperClientFactory.initializeAndStartZkConfigSource(new ExhibitorS3ListProvider(),
                    ConfigurationManager.getDeploymentContext().getApplicationId(), zkConfigRootPath);
        }
        catch(Exception e) {
            log.error(e.getMessage());
        }
    }
}
