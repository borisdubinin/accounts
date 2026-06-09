package org.example.repository;

import org.example.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity, Long> {

    @Query("SELECT t FROM TransferEntity t " +
            "WHERE (t.ibanFrom = :iban OR t.ibanTo = :iban) " +
            "AND t.createdAt BETWEEN :from AND :to " +
            "ORDER BY t.createdAt DESC")
    List<TransferEntity> findByIbanAndDateRange(String iban, LocalDateTime from, LocalDateTime to);
}
