package br.com.acmeairlines.domain.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Page<UserModel> findByActive(Boolean active, Pageable pageable);
    UserModel findUserByEmail(String email);
    UserDataRecord findUserDataRecordByEmail(String email);
    UserDetails findByEmail(String login);
}
