package com.example.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import com.example.result.Result;
import com.example.result.ResultType;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * create a new account
     * @param account
     * @return The Result of the account creation
     */
    public Result<Account> createAccount(Account account) {
        Result<Account> result = validateCreate(account);

        if(!result.isSuccess()) { // validation failure
            return result;
        }


        account = accountRepository.save(account); // set account with id
        result.setPayload(account); // set payload in result
        return result;
    }
    

    /**
     * login user if their credentials match existing account
     * @param account
     * @return Result of the login
     */
    public Result<Account> loginAccount(Account account) {
        Result<Account> result = validateLogin(account);

        if(!result.isSuccess()) { // validation failure
            return result;
        }

        // find account with same username and password
        Optional<Account> attempt = accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword());


        if(attempt.isPresent()) { // set payload and return result
            result.setPayload(attempt.get());
            return result;
        } else {
            // fail the attempt to login and return result
            result.addMessages("Login FAILED", ResultType.INVALID); 
            return result;
        }
    }

    /**
     * Validates the account for a account creation attempt
     * @param account
     * @return result of account creation validation
     */
    private Result<Account> validateCreate(Account account) {
        Result<Account> result = new Result<>();

        if(account == null) {
            result.addMessages("Account CANNOT BE NULL", ResultType.INVALID);
            return result;
        }

        if(account.getUsername() == null || account.getUsername().isBlank()) {
            result.addMessages("Username CANNOT BE BLANK", ResultType.INVALID);
        }

        if(accountRepository.findByUsername(account.getUsername()).isPresent()) {
            result.addMessages("Username IN USE", ResultType.DUPLICATE); // classify as duplicate result type for controller
        }

        if(account.getPassword().length() < 4) {
            result.addMessages("Password CANNOT BE LESS THAN 4 CHARACTERS", ResultType.INVALID);
        }

        return result;
    }

    /**
     * Validates the account for a login attempt
     * @param account
     * @return result of login validation
     */
    private Result<Account> validateLogin(Account account) {
        Result<Account> result = new Result<>();

        if(account == null) {
            result.addMessages("Account CANNOT BE NULL", ResultType.INVALID);
            return result;
        }

        if(!accountRepository.findByUsername(account.getUsername()).isPresent()) {
            result.addMessages("Username NOT FOUND", ResultType.NOT_FOUND);
        }

        if(account.getPassword().length() < 4) {
            result.addMessages("Password CANNOT BE LESS THAN 4 CHARACTERS", ResultType.INVALID);
        }

        return result;
    }
    
}
