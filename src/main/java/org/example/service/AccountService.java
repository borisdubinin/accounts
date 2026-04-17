package org.example.service;

import org.example.model.Account;

import java.util.List;

public interface AccountService {

    Account create(Account account);

    List<Account> getAll();
}
