package com.pn.entities;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AirlineEntity {
  private long id;
  private String type;
  private String name;
  private String iata;
  private String icao;
  private String callsign;
  private String country;
}
