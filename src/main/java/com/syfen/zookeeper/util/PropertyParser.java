package com.syfen.zookeeper.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * User: ToneD
 * Created: 25/08/13 1:58 PM
 */
public class PropertyParser {

    private static Logger log = LoggerFactory.getLogger(S3PropertiesFile.class);

    public static Collection<String> serverSpecList(String serverSpecs) {

        Collection<String> list = new ArrayList<String>();

        if(serverSpecs != null) {

            ArrayList<String> serverSpecsList = new ArrayList<String>(Arrays.asList(serverSpecs.split(",")));

            // parse each item to extract hostnames to build collection
            for (String spec : serverSpecsList) {
                if(spec.split(":")[0].equals("S")) {
                    try {
                        Integer.parseInt(spec.split(":")[1]);
                        list.add(spec.split(":")[2]);
                    }
                    catch (NumberFormatException e) {
                        log.error("Invalid exhibitor server spec value");
                    }
                }
            }
        }
        return list;
    }

    public static String connectionString(String serverSpecs, int clientPort) {

        StringBuilder sb = new StringBuilder();

        if(serverSpecs != null) {

            ArrayList<String> serverSpecsList = new ArrayList<String>(Arrays.asList(serverSpecs.split(",")));

            // parse each item to extract the hostnames then add the port and insert commas
            for (int i=0; i<serverSpecsList.size(); i++) {

                String spec = serverSpecsList.get(i);
                if(spec.split(":")[0].equals("S")) {
                    try {
                        Integer.parseInt(spec.split(":")[1]);
                        sb.append(spec.split(":")[2]);
                        sb.append(":");
                        sb.append(clientPort);
                        if (i < serverSpecsList.size()-1) {
                            sb.append(",");
                        }
                    }
                    catch (NumberFormatException e) {
                        log.error("Invalid exhibitor server spec value or invalid client port value");
                    }
                }
            }
        }
        return sb.toString();
    }
}