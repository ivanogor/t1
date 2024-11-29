package ru.t1.java.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.enums.ClientStatus;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    @Override
    Optional<Client> findById(Long aLong);

    long countClientsByStatus(ClientStatus clientStatus);

    @Query(value = "SELECT * FROM client WHERE client_status = 'BLACKLISTED' LIMIT :limit", nativeQuery = true)
    List<Client> findTopNBlockedClients(@Param("limit") int limit);
}