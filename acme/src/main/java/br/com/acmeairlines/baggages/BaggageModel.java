package br.com.acmeairlines.baggages;

import br.com.acmeairlines.flights.FlightModel;
import br.com.acmeairlines.users.UserUpdateData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "baggages")
@Entity(name = "baggages")
@NoArgsConstructor
@AllArgsConstructor
public class BaggageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String tag;
    private String color;
    private Double weight;
    private String dimension;
    private String status; // Recebida, Despachada, Decolou... essas coisas
    private String lastSeenLocation;
    @ManyToOne
    private FlightModel flight;
    public BaggageModel(BaggageData data) {
        this.userId = data.userId();
        this.tag = data.tag();
        this.color = data.color();
        this.weight = data.weight();
        this.dimension = data.dimension();
        this.status = data.status();
        this.lastSeenLocation = data.lastSeenLocation();
        this.flight = new FlightModel(data.flight());
    }
    public void updateBaggage(BaggageUpdateData data) {
        if (data.status() != null) {
            this.status = data.status();
        }
        if (data.lastSeenLocation() != null) {
            this.lastSeenLocation = data.lastSeenLocation();
        }
        if (data.flight() != null) {
            this.flight.updateFlight(data.flight());
        }
    }
}
