package com.task2.DB;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.StreamSupport;

public enum ServerCache {
    SERVER_CACHE;

    private LinkedHashMap<Integer, ServerModel> servers = new LinkedHashMap<>();

    public static synchronized ServerCache getInstance() {
        return SERVER_CACHE;
    }

    public synchronized LinkedHashMap<Integer, ServerModel> getServers() {
        return this.servers;
    }

    public synchronized void setServers(List<ServerModel> all) {
        StreamSupport
                .stream(all.spliterator(), false)
                .forEach(r -> this.servers.put(r.getServerId(), r));
    }

    public synchronized ServerModel read(int key) {
        return this.servers.get(key);
    }

    public synchronized void write(ServerModel serverModel) {
        this.servers.put(serverModel.getServerId(), serverModel);
    }

    public synchronized void update(int key, ServerModel serverModel) {
        this.servers.replace(key, serverModel);
    }

    public synchronized void delete(int key) {
        this.servers.remove(key);
    }
}
