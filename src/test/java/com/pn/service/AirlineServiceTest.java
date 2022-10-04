package com.pn.service;

import com.couchbase.client.core.error.DocumentExistsException;
import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.kv.MutationResult;
import com.couchbase.client.java.query.QueryResult;
import com.pn.config.CouchbaseConfig;
import com.pn.entities.AirlineEntity;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AirlineServiceTest {

  @Mock CouchbaseConfig couchbaseConfig;
  @InjectMocks AirlineService underTest;

  @Test
  void should_succeed_createAirline() {
    underTest.collection = Mockito.mock(Collection.class);
    MutationResult result = Mockito.mock(MutationResult.class);

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(underTest.collection.insert("airline_" + expected.getId(), expected))
        .thenReturn(result);

    Optional<AirlineEntity> actual = underTest.createAirline(expected.getId(), expected);

    Assertions.assertTrue(actual.isPresent());
    Assertions.assertEquals(expected, actual.get());
  }

  @Test
  void should_fail_createAirline() {
    underTest.collection = Mockito.mock(Collection.class);

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(underTest.collection.insert("airline_" + expected.getId(), expected))
        .thenThrow(new DocumentExistsException(null));

    Optional<AirlineEntity> actual = underTest.createAirline(expected.getId(), expected);

    Assertions.assertFalse(actual.isPresent());
  }

  @Test
  void should_succeed_getAllAirlines() {
    underTest.cluster = Mockito.mock(Cluster.class);
    QueryResult result = Mockito.mock(QueryResult.class);

    List<AirlineEntity> expected =
        List.of(
            new AirlineEntity(
                1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States"));

    Mockito.when(underTest.cluster.query("select airline.* from `travel-sample`.inventory.airline"))
        .thenReturn(result);
    Mockito.when(result.rowsAs(AirlineEntity.class)).thenReturn(expected);

    List<AirlineEntity> actual = underTest.getAllAirlines();

    Assertions.assertArrayEquals(expected.toArray(), actual.toArray());
  }

  @Test
  void should_succeed_getAirline() {
    underTest.collection = Mockito.mock(Collection.class);
    GetResult result = Mockito.mock(GetResult.class);

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(underTest.collection.get("airline_" + expected.getId())).thenReturn(result);
    Mockito.when(result.contentAs(AirlineEntity.class)).thenReturn(expected);

    Optional<AirlineEntity> actual = underTest.getAirline(expected.getId());

    Assertions.assertTrue(actual.isPresent());
    Assertions.assertEquals(expected, actual.get());
  }

  @Test
  void should_fail_getAirline() {
    underTest.collection = Mockito.mock(Collection.class);

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(underTest.collection.get("airline_" + expected.getId()))
        .thenThrow(new DocumentNotFoundException(null));

    Optional<AirlineEntity> actual = underTest.getAirline(expected.getId());

    Assertions.assertFalse(actual.isPresent());
  }

  @Test
  void should_succeed_updateAirline() {
    underTest.collection = Mockito.mock(Collection.class);
    MutationResult result = Mockito.mock(MutationResult.class);

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(underTest.collection.replace("airline_" + expected.getId(), expected))
        .thenReturn(result);

    Optional<AirlineEntity> actual = underTest.updateAirline(expected.getId(), expected);

    Assertions.assertTrue(actual.isPresent());
    Assertions.assertEquals(expected, actual.get());
  }

  @Test
  void should_fail_updateAirline() {
    underTest.collection = Mockito.mock(Collection.class);

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(underTest.collection.replace("airline_" + expected.getId(), expected))
        .thenThrow(new DocumentNotFoundException(null));

    Optional<AirlineEntity> actual = underTest.updateAirline(expected.getId(), expected);

    Assertions.assertFalse(actual.isPresent());
  }

  @Test
  void should_succeed_deleteAirline() {
    underTest.collection = Mockito.mock(Collection.class);
    MutationResult result = Mockito.mock(MutationResult.class);

    long id = 1;

    Mockito.when(underTest.collection.remove("airline_" + id)).thenReturn(result);

    boolean actual = underTest.deleteAirline(id);

    Assertions.assertTrue(actual);
  }

  @Test
  void should_fail_deleteAirline() {
    underTest.collection = Mockito.mock(Collection.class);

    long id = 1;

    Mockito.when(underTest.collection.remove("airline_" + id))
        .thenThrow(new DocumentNotFoundException(null));

    boolean actual = underTest.deleteAirline(id);

    Assertions.assertFalse(actual);
  }
}
