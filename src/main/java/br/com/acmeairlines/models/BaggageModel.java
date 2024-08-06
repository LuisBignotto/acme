package br.com.acmeairlines.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Table(name = "baggages")
@Entity(name = "baggages")
@NoArgsConstructor
@AllArgsConstructor
public class BaggageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    @Size(min = 1, max = 10)
    private String tag;

    @NotNull
    @Size(min = 1, max = 50)
    private String color;

    @NotNull
    private BigDecimal weight;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private StatusModel status;

    @NotNull
    @Size(min = 1, max = 100)
    private String lastLocation;

    @NotNull
    private Long flightId;

    @OneToMany(mappedBy = "baggage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BaggageTrackerModel> trackers;
}