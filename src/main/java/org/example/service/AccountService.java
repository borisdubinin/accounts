package org.example.service;

import org.example.model.Account;

import java.util.List;

public interface AccountService {

    /**
     *
     * @param account with required fields balance, currency
     * @return created account with id and status
     */
    Account create(Account account);

    /**
     *
     * @return all existing accounts
     */
    List<Account> getAll();
}
