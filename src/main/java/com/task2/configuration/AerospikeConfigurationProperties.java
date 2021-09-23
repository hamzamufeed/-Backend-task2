package com.task2.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aerospike")
public class AerospikeConfigurationProperties {
    private String host;
    private int port;
    private String namespace;
    private boolean scansEnabled;

    public AerospikeConfigurationProperties() {
    }

    public AerospikeConfigurationProperties(String host, int port, String namespace, boolean scansEnabled) {
        this.host = host;
        this.port = port;
        this.namespace = namespace;
        this.scansEnabled = scansEnabled;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public boolean isScansEnabled() {
        return scansEnabled;
    }

    public void setScansEnabled(boolean scansEnabled) {
        this.scansEnabled = scansEnabled;
    }
}
