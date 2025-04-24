package com.example.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.result.Result;
import com.example.result.ResultType;
import com.example.service.AccountService;
import com.example.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /**
     * handles registering accounts at 'POST localhost:8080/register'
     * 
     * Expected Status Codes:
     * - 200 OK: Account successfully created and returned in reponse body.
     * - 409 CONFLICT: Username is a duplicate.
     * - 400 BAD REQUEST: Register failed.
     *
     * @param account account from the request body
     * @return return ReponseEntity containing status code
     */
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        Result<Account> result = accountService.createAccount(account); // get result of account creation

        if(result.isSuccess()) { // if creation is a success return the payload and 200 status
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        } else if(result.getType() == ResultType.DUPLICATE) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // if the result is a duplicate account, then return 409
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // else return 400
        }
    }

    /**
     * handles loggin in users at 'POST localhost:8080/login'
     * 
     * Expected Status Codes:
     * - 200 OK: login is a success
     * - 401 UNAUTHORIZED: login failed
     * 
     * @param account account from the request body
     * @return ResponseEntity containing account and status code or just the status code
     */
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Result<Account> result = accountService.loginAccount(account); // get result of login

        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK); // if login is a success, return 200 and result payload
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // else return 401
        }
    }

    /**
     * create a message
     * 
     * Expected Status Codes:
     * - 200 OK: successful message creation
     * - 400 BAD REQUEST: unsuccessful message creation
     * 
     * @param message
     * @return ResponseEntity contatining the message created and status code
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Result<Message> result = messageService.createMessage(message); // get result of message creation

        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK); // return 200 and message
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // return 400
        }
    }

    /**
     * get all messages
     * 
     * Expected Status Codes:
     * 200 OK: returns message list empty or not
     * 
     * @return ResponseEntity containing list of messages and status code
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return new ResponseEntity<>(messageService.getAllMessages(), HttpStatus.OK); // return 200 and list of messages empty or not
    }

    /**
     * get message by id
     * 
     * Expected Status Code:
     * 200 OK: returns message found or nothing
     * @param messageId
     * @return Response entity containing message or nothing and status code
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
        return new ResponseEntity<>(messageService.getMessageById(messageId), HttpStatus.OK); // return 200 and message or nothing
    }

    /**
     * delete message by id
     * 
     * Expected Status Code:
     * 200 OK: returned whether deletion occured or not
     * 
     * @param messageId
     * @return ResponseEntity containing the number of rows affected and a status code
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId)  {
        if(messageService.deleteById(messageId)) {
            return new ResponseEntity<>(1, HttpStatus.OK); // return number of rows affected and 200
        } else {
            return new ResponseEntity<>(HttpStatus.OK); // return 200 if not found
        }
    }

    /**
     * Updates a message
     * 
     * Expected Status Codes:
     * - 200 OK: successful message update
     * - 400 BAD REQUEST: unsuccessful message update
     * 
     * @param messageId
     * @param message
     * @return ResponseEntity containing the number of rows affected and a status code
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable int messageId, @RequestBody Message message) {
        Result<Message> result = messageService.updateMessage(messageId, message);

        if(result.isSuccess()) {
            return new ResponseEntity<>(1, HttpStatus.OK); // return rows affected and 200 if message is updated
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // return 400 if message is not updated
        }
    }

    /**
     * gets all messages from account
     * 
     * Expected Status Codes:
     * 200 OK: returns list of messages empty or not
     * 
     * @param accountId
     * @return ResponseEntity containing list of messages and status code
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesFromAccount(@PathVariable int accountId) {
        return new ResponseEntity<>(messageService.getAllMessagesFromAccount(accountId), HttpStatus.OK); // return list of messages or empty list and 200
    }

    
}
