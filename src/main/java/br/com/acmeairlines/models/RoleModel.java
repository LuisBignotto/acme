package br.com.acmeairlines.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Table(name = "roles")
@Entity(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RoleModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roleName;
}