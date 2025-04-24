package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.result.Result;
import com.example.result.ResultType;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    
    /**
     * create a message
     * @param message
     * @return Result for the creation of a message
     */
    public Result<Message> createMessage(Message message) {
        Result<Message> result = validate(message);

        if(!result.isSuccess()) { // validation failure
            return result;
        }

        message = messageRepository.save(message); // set message with id
        result.setPayload(message); // set result payload
        return result;
    }

    
    /**
     * get all messages
     * @return List of messages
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    
    /**
     * get a message by id
     * @param id
     * @return message if found, null if not found
     */
    public Message getMessageById(int id) {
        return messageRepository.findById(id).orElse(null); // if message exists return it otherwise return null
    }

    
    /**
     * delete a message by its id
     * @param id
     * @return true if the message was deleted, false if the message was not found
     */
    public boolean deleteById(int id) {
        if(messageRepository.findById(id).isPresent()) {
            messageRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    /**
     * update a message's text field
     * @param id
     * @param message
     * @return the result of the update
     */
    public Result<Message> updateMessage(int id, Message message) {
        Result<Message> result = validate(id, message);

        if(!result.isSuccess()) { // validation failure
            return result;
        }

        Optional<Message> messageOpt = messageRepository.findById(id);
        if(messageOpt.isPresent()) { // if message is present update the message text and return result
            Message newMessage = messageOpt.get();
            newMessage.setMessageText(message.getMessageText());
            messageRepository.save(newMessage);
            return result;
        }else { // if the message is not present 
            result.addMessages("Message NOT FOUND", ResultType.NOT_FOUND);
            return result;
        }
    }

    /**
     * get all messages from an specific account
     * @param accountId
     * @return a List of messages
     */
    public List<Message> getAllMessagesFromAccount(int accountId) {
        return messageRepository.findByPostedBy(accountId);
    }

    /**
     * return results of validation on the message
     * @param message
     * @return result
     */
    private Result<Message> validate(Message message) {
        Result<Message> result = new Result<>();

        if(message == null) {
            result.addMessages("Message CANNOT BE NULL", ResultType.INVALID);
            return result;
        }

        if(message.getMessageText() == null || message.getMessageText().isBlank()) {
            result.addMessages("Message Text CANNOT BE BLANK", ResultType.INVALID);
        }

        if(message.getMessageText().length() >= 255) {
            result.addMessages("Message Text CANNOT BE OVER 255 CHARACTERS", ResultType.INVALID);
        }

        if(!accountRepository.findById(message.getPostedBy()).isPresent()) {
            result.addMessages("Posted By MUST REFER TO A REAL EXISTING USER", ResultType.INVALID);
        }

        return result;
    }

    /**
     * return results of validation on the message for update operation
     * @param message
     * @return result
     */
    private Result<Message> validate(int id, Message message) {
        Result<Message> result = new Result<>();

        if(message == null) {
            result.addMessages("Message CANNOT BE NULL", ResultType.INVALID);
            return result;
        }

        if(message.getMessageText() == null || message.getMessageText().isBlank()) {
            result.addMessages("Message Text CANNOT BE BLANK", ResultType.INVALID);
        }

        if(message.getMessageText().length() >= 255) {
            result.addMessages("Message Text CANNOT BE OVER 255 CHARACTERS", ResultType.INVALID);
        }

        return result;
    }
}
