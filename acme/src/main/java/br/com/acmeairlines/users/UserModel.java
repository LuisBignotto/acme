package br.com.acmeairlines.users;

import br.com.acmeairlines.address.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "users")
@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private Boolean active;
    @Embedded
    private Address address;
    @Enumerated(EnumType.STRING)
    private Role role;
    public UserModel(UserRegisterData data) {
        this.name = data.name();
        this.email = data.email();
        this.password = data.password();
        this.role = data.role();
        this.active = data.active();
    }
    public void updateUser(UserUpdateData data) {
        if (data.name() != null) {
            this.name = data.name();
        }
        if (data.email() != null) {
            this.email = data.email();
        }
        if (data.password() != null) {
            this.password = data.password();
        }
        if (data.phone() != null) {
            this.phone = data.phone();
        }
        if (data.address() != null) {
            this.address.updateAddress(data.address());
        }
    }

}
