package com.syfen.zookeeper;

import com.netflix.config.ConfigurationManager;
import com.syfen.zookeeper.exhibitor.ExhibitorLBListProvider;
import com.syfen.zookeeper.exhibitor.ExhibitorS3ListProvider;
import junit.framework.Assert;
import org.apache.commons.configuration.AbstractConfiguration;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;


/**
 * User: ToneD
 * Created: 21/09/13 1:09 PM
 */
public class ExhibitorListProviderIT {

    private static final Logger log = LoggerFactory.getLogger(ExhibitorListProviderIT.class);
    private static final AbstractConfiguration config = ConfigurationManager.getConfigInstance();

    private static ArrayList<String> exhibitorListS3;
    private static ArrayList<String> exhibitorListLB;
    private static ArrayList<String> expectedList;

    @BeforeClass
    public static void setup() {

        expectedList = new ArrayList<>();
        exhibitorListS3 = (ArrayList<String>)new ExhibitorS3ListProvider().getExhibitorList();
        exhibitorListLB = (ArrayList<String>)new ExhibitorLBListProvider().getExhibitorList();

        for(Object item : config.getList("test.exhibitor.list")) {
            expectedList.add(item.toString());
        }

        Collections.sort(expectedList);
        Collections.sort(exhibitorListS3);
        Collections.sort(exhibitorListLB);
    }

    @Test
    public void testGetExhibitorListS3() {

        // Ensure that an array list is returned
        Assert.assertEquals(ArrayList.class, exhibitorListS3.getClass());

        // Ensure that the list is the right length
        Assert.assertEquals(expectedList.size(), exhibitorListS3.size());

        // Ensure array list contains correct values
        Assert.assertEquals(expectedList, exhibitorListS3);
    }

    @Test
    public void testGetExhibitorListLB() {

        // Ensure that an array list is returned
        Assert.assertEquals(ArrayList.class, exhibitorListLB.getClass());

        // Ensure that the list is the right length
        Assert.assertEquals(expectedList.size(), exhibitorListLB.size());

        // Ensure array list contains correct values
        Assert.assertEquals(expectedList, exhibitorListLB);
    }
}
