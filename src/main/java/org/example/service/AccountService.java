package org.example.service;

import org.example.model.Account;

import java.util.List;

public interface AccountService {

    /**
     * Creates an account with specified details
     * @param account with required fields balance, currency
     * @return created account with id and status
     */
    Account create(Account account);

    /**
     * @return all existing accounts
     */
    List<Account> getAll();

    /**
     * @param iban IBAN of the account
     * @return balance and currency of the account
     */
    Account getByIban(String iban);
}
