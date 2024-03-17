package br.com.acmeairlines.domain.baggages.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Table(name = "baggages")
@Entity(name = "baggages")
@NoArgsConstructor
@AllArgsConstructor
public class BaggageModel {

    @Id
    private String id;
    private String userId;
    private String tag;
    private String color;
    private Double weight;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String lastSeenLocation;
    private String flightId;

}
