package br.com.acmeairlines.domain.users.repository;

import br.com.acmeairlines.domain.users.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, String> {
    Page<UserModel> findByActive(Boolean active, Pageable pageable);

    UserModel findByEmail(String email);

    UserModel findByCpf(String cpf);
}
