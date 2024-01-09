package br.com.acmeairlines.domain.users;

import br.com.acmeairlines.domain.address.Address;

public record UserDataRecord(Long id, String name, String email, String phone, Role role, Address address) {
    public UserDataRecord(UserModel user){
        this(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getRole(), user.getAddress());
    }
}
