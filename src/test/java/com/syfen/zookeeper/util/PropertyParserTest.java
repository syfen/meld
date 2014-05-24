package com.syfen.zookeeper.util;

import com.netflix.config.ConfigurationManager;
import junit.framework.Assert;
import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.lang.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * User: ToneD
 * Created: 22/09/13 10:27 AM
 */
public class PropertyParserTest {

    private static final Logger log = LoggerFactory.getLogger(PropertyParserTest.class);
    private static final AbstractConfiguration config = ConfigurationManager.getConfigInstance();

    private static String serversSpec;
    private static ArrayList<String> expectedList;
    private static int clientPort;
    private static String expectedConnectionString;

    @BeforeClass
    public static void setup() {

        // load expected list from properties file
        expectedList = new ArrayList<String>();
        for(Object item : config.getList("test.exhibitor.list")) {

            expectedList.add(item.toString());
        }

        // load servers spec from properties file
        serversSpec = StringUtils.join(config.getStringArray("test.servers.spec"), ",");

        // load client port from properties file
        clientPort = config.getInt("zookeeper.config.client.port");

        // load expected connection string from properties file
        expectedConnectionString = StringUtils.join(config.getStringArray("test.backup.connection"), ",");
    }

    @Test
    public void testServerSpecList() {

        // Ensure that array list is returned
        Assert.assertEquals(ArrayList.class, PropertyParser.serverSpecList(serversSpec).getClass());

        // Ensure the array list is the correct size
        Assert.assertEquals(expectedList.size(), PropertyParser.serverSpecList(serversSpec).size());

        // Ensure array list contains correct values
        Assert.assertEquals(expectedList, PropertyParser.serverSpecList(serversSpec));
    }

    @Test
    public void testConnectionString() {

        // Ensure a string is returned
        Assert.assertEquals(String.class, PropertyParser.connectionString(serversSpec, clientPort).getClass());

        // Ensure that correct string is returned
        Assert.assertEquals(expectedConnectionString, PropertyParser.connectionString(serversSpec, clientPort));
    }
}