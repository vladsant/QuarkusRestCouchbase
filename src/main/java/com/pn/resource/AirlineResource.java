package com.pn.resource;

import com.pn.entities.AirlineEntity;
import com.pn.service.AirlineService;
import lombok.AllArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

import static javax.ws.rs.core.Response.Status.*;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@AllArgsConstructor
@Path("/airlines")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AirlineResource {

  private AirlineService airlineService;

  private static final String THE_AIRLINE_WITH_ID = "The airline with id: ";
  private static final String DOES_NOT_EXIST = " doesn't exist";

  @POST
  public Response addAirline(AirlineEntity airlineEntity) {
    long id = airlineEntity.getId();
    Optional<AirlineEntity> response = airlineService.createAirline(id, airlineEntity);
    if (response.isPresent()) {
      return Response.ok(response.get()).status(CREATED).build();
    }
    return Response.status(BAD_REQUEST)
        .entity(THE_AIRLINE_WITH_ID + id + " already exist")
        .build();
  }

  @GET
  public List<AirlineEntity> getAllAirlines() {
    return airlineService.getAllAirlines();
  }

  @GET
  @Path("/{id}")
  public Response getAirline(@PathParam("id") long id) {
    Optional<AirlineEntity> airlineEntity = airlineService.getAirline(id);
    if (airlineEntity.isPresent()) {
      return Response.ok(airlineEntity.get()).build();
    }
    return Response.status(NOT_FOUND)
        .entity(THE_AIRLINE_WITH_ID + id + DOES_NOT_EXIST)
        .build();
  }

  @PUT
  @Path("/{id}")
  public Response updateAirline(@PathParam("id") long id, AirlineEntity airlineEntity) {
    Optional<AirlineEntity> response = airlineService.updateAirline(id, airlineEntity);
    if (response.isPresent()) {
      return Response.ok(response.get()).status(CREATED).build();
    }
    return Response.status(NOT_FOUND)
        .entity(THE_AIRLINE_WITH_ID + id + DOES_NOT_EXIST)
        .build();
  }

  @DELETE
  @Path("/{id}")
  public Response deleteAirline(@PathParam("id") long id) {
    boolean deleted = airlineService.deleteAirline(id);
    if (deleted) {
      return Response.status(NO_CONTENT).build();
    }
    return Response.status(NOT_FOUND)
        .entity(THE_AIRLINE_WITH_ID + id + DOES_NOT_EXIST)
        .build();
  }
}
