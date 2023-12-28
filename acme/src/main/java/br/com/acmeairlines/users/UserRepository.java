package br.com.acmeairlines.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Page<UserModel> findByActive(Boolean active, Pageable pageable);
    List<UserModel> findByRole(Role role);
    UserModel findUserByEmail(String email);
}
