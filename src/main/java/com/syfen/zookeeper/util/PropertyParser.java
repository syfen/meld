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

    private static Logger log = LoggerFactory.getLogger(PropertyParser.class);

    public static ArrayList<String> serverSpecList(String serverSpecs) {

        log.debug("Server spec parsing: " + serverSpecs);

        ArrayList<String> list = new ArrayList<String>();

        if(serverSpecs != null) {

            ArrayList<String> serverSpecsList = new ArrayList<String>(Arrays.asList(serverSpecs.split(",")));

            // parse each item to extract hostnames to build collection
            for (String spec : serverSpecsList) {

// This works for out of the box exhibitor config but not for auto-discovery mode
//                if(spec.split(":")[0].equals("S")) {
//                    try {
//                        Integer.parseInt(spec.split(":")[1]);
//                        list.add(spec.split(":")[2]);
//                    }
//                    catch (NumberFormatException e) {
//                        log.error("Invalid exhibitor server spec value");
//                    }
//                }

                try {
                    Integer.parseInt(spec.split(":")[0]);
                    list.add(spec.split(":")[1]);
                    log.debug("Server spec adding: " + spec.split(":")[1]);
                }
                catch (NumberFormatException e) {
                    log.error("Invalid exhibitor server spec value");
                }

            }
        }
        return list;
    }

    public static String connectionString(String serversSpec, int clientPort) {

        StringBuilder sb = new StringBuilder();

        if(serversSpec != null) {

            ArrayList<String> serverSpecsList = new ArrayList<String>(Arrays.asList(serversSpec.split(",")));

// This works for out of the box exhibitor config but not for auto-discovery mode
// parse each item to extract the hostnames then add the port and insert commas
//            for (int i=0; i<serverSpecsList.size(); i++) {
//
//                String spec = serverSpecsList.get(i);
//                if(spec.split(":")[0].equals("S")) {
//                    try {
//                        Integer.parseInt(spec.split(":")[1]);
//                        sb.append(spec.split(":")[2]);
//                        sb.append(":");
//                        sb.append(clientPort);
//                        if (i < serverSpecsList.size()-1) {
//                            sb.append(",");
//                        }
//                    }
//                    catch (NumberFormatException e) {
//                        log.error("Invalid exhibitor server spec value or invalid client port value");
//                    }
//                }
//            }
            for(int i=0; i<serverSpecsList.size(); i++) {

                String spec = serverSpecsList.get(i);
                try {
                    Integer.parseInt(spec.split(":")[0]);
                    sb.append(spec.split(":")[1]);
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
        return sb.toString();
    }
}