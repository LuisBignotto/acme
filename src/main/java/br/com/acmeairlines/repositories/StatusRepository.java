package br.com.acmeairlines.repositories;

import br.com.acmeairlines.models.StatusModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<StatusModel, Long> {}