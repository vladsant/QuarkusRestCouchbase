package com.pn.service;

import com.couchbase.client.core.error.DocumentExistsException;
import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.pn.config.CouchbaseConfig;
import com.pn.entities.AirlineEntity;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public class AirlineService {

  private final CouchbaseConfig couchbaseConfig;

  Cluster cluster;
  Collection collection;

  public AirlineService(CouchbaseConfig couchbaseConfig) {
    this.couchbaseConfig = couchbaseConfig;
  }

  @PostConstruct
  void initialize() {
    cluster =
        Cluster.connect(
            couchbaseConfig.host(), couchbaseConfig.username(), couchbaseConfig.password());
    Bucket bucket = cluster.bucket(couchbaseConfig.bucketName());
    collection = bucket.defaultCollection();
  }

  public Optional<AirlineEntity> createAirline(long id, AirlineEntity airlineEntity) {
    try {
      collection.insert(getDocumentId(id), airlineEntity);
      return Optional.of(airlineEntity);
    } catch (DocumentExistsException e) {
      return Optional.empty();
    }
  }

  public List<AirlineEntity> getAllAirlines() {
    return cluster
        .query("select airline.* from `travel-sample`.inventory.airline")
        .rowsAs(AirlineEntity.class);
  }

  public Optional<AirlineEntity> getAirline(long id) {
    try {
      AirlineEntity airlineEntity =
          collection.get(getDocumentId(id)).contentAs(AirlineEntity.class);
      return Optional.of(airlineEntity);
    } catch (DocumentNotFoundException e) {
      return Optional.empty();
    }
  }

  public Optional<AirlineEntity> updateAirline(long id, AirlineEntity airlineEntity) {
    try {
      airlineEntity.setId(id);
      collection.replace(getDocumentId(id), airlineEntity);
      return Optional.of(airlineEntity);
    } catch (DocumentNotFoundException e) {
      return Optional.empty();
    }
  }

  public boolean deleteAirline(long id) {
    try {
      collection.remove(getDocumentId(id));
      return true;
    } catch (DocumentNotFoundException e) {
      return false;
    }
  }

  private static String getDocumentId(long id) {
    return "airline_" + id;
  }
}
