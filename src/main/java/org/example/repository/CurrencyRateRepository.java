package org.example.repository;

import org.example.entity.CurrencyRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRateEntity, Long> {
}