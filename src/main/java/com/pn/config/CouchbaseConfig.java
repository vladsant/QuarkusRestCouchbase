package com.pn.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "couchbase", namingStrategy = ConfigMapping.NamingStrategy.VERBATIM)
public interface CouchbaseConfig {
  String host();

  String username();

  String password();

  String bucketName();
}
