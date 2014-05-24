package com.syfen.zookeeper.model;

import java.util.ArrayList;

/**
 * User: ToneD
 * Created: 24/05/2014 6:55 PM
 */
public class ZookeeperClusterList {

    private ArrayList<String> servers;
    private int port;

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public ArrayList<String> getServers() { return servers; }
    public void setServers(ArrayList<String> servers) { this.servers = servers; }
}
