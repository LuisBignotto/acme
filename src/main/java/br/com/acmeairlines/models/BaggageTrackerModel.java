package br.com.acmeairlines.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Table(name = "baggage_trackers")
@Entity(name = "baggage_trackers")
@NoArgsConstructor
@AllArgsConstructor
public class BaggageTrackerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "baggage_id")
    private BaggageModel baggage;

    @Column(name = "tracker_user_id")
    private Long trackerUserId;
}