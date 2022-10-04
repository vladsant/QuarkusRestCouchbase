package com.pn.resource;

import com.pn.entities.AirlineEntity;
import com.pn.service.AirlineService;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.util.Optional;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

@ExtendWith(MockitoExtension.class)
class AirlineResourceTest {
  @Mock AirlineService airlineService;
  @InjectMocks AirlineResource underTest;

  @Test
  void should_succeed_addAirline() {

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(airlineService.createAirline(expected.getId(), expected))
        .thenReturn(Optional.of(expected));

    Response actual = underTest.addAirline(expected);

    Assertions.assertEquals(CREATED.getStatusCode(), actual.getStatus());
    Assertions.assertEquals(expected, actual.getEntity());
  }

  @Test
  void should_fail_addAirline() {

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(airlineService.createAirline(expected.getId(), expected))
        .thenReturn(Optional.empty());

    Response actual = underTest.addAirline(expected);

    Assertions.assertEquals(BAD_REQUEST.getStatusCode(), actual.getStatus());
    Assertions.assertEquals(
        "The airline with id: " + expected.getId() + " already exist", actual.getEntity());
  }

  @Test
  void should_succeed_getAirlines() {

    List<AirlineEntity> expected =
        List.of(
            new AirlineEntity(
                1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States"));
    Mockito.when(airlineService.getAllAirlines()).thenReturn(expected);
    List<AirlineEntity> actual = underTest.getAllAirlines();
    Assertions.assertArrayEquals(expected.toArray(), actual.toArray());
  }

  @Test
  void should_fail_getAirline() {

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(airlineService.getAirline(expected.getId())).thenReturn(Optional.empty());

    Response actual = underTest.getAirline(expected.getId());

    Assertions.assertEquals(NOT_FOUND.getStatusCode(), actual.getStatus());
    Assertions.assertEquals(
        "The airline with id: " + expected.getId() + " doesn't exist", actual.getEntity());
  }

  @Test
  void should_succeed_updateAirline() {

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(airlineService.updateAirline(expected.getId(), expected))
        .thenReturn(Optional.of(expected));

    Response actual = underTest.updateAirline(expected.getId(), expected);

    Assertions.assertEquals(CREATED.getStatusCode(), actual.getStatus());
    Assertions.assertEquals(expected, actual.getEntity());
  }

  @Test
  void should_fail_updateAirline() {

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(airlineService.updateAirline(expected.getId(), expected))
        .thenReturn(Optional.empty());

    Response actual = underTest.updateAirline(expected.getId(), expected);

    Assertions.assertEquals(NOT_FOUND.getStatusCode(), actual.getStatus());
    Assertions.assertEquals(
        "The airline with id: " + expected.getId() + " doesn't exist", actual.getEntity());
  }

  @Test
  void should_succeed_deleteAirline() {

    long id = 1;

    Mockito.when(airlineService.deleteAirline(id)).thenReturn(true);

    Response actual = underTest.deleteAirline(id);

    Assertions.assertEquals(NO_CONTENT.getStatusCode(), actual.getStatus());
  }

  @Test
  void should_fail_deleteAirline() {

    long id = 1;

    Mockito.when(airlineService.deleteAirline(id)).thenReturn(false);

    Response actual = underTest.deleteAirline(id);

    Assertions.assertEquals(NOT_FOUND.getStatusCode(), actual.getStatus());
    Assertions.assertEquals("The airline with id: " + id + " doesn't exist", actual.getEntity());
  }
}
