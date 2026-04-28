package org.example.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Account currency (ISO 4217 code)")
public enum AccountCurrency {
    EUR, USD, BYN, RUB
}