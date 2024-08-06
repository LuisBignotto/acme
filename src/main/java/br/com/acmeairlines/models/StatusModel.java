package br.com.acmeairlines.models;

import jakarta.persistence.*;
import lombok.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Setter
@Getter
@Table(name = "baggage_status")
@Entity(name = "baggage_status")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StatusModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    private String status;
}