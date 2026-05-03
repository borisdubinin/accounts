package org.example.repository;

import org.example.entity.CurrencyRateEntity;
import org.example.model.AccountCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRateEntity, Long> {

    Optional<CurrencyRateEntity> getByCurrencyAndDate(AccountCurrency currency, LocalDate date);
}