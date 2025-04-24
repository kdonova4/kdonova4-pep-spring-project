package com.example.result;
import java.util.ArrayList;
import java.util.List;
/**
 * This class captures both the outcome type (success, invalid, duplicate, etc.)
 * and any associated messages or payload. standardizes response handling and simplifies error reporting.
 */
public class Result<T> {

    private final ArrayList<String> messages = new ArrayList<>();
    private ResultType type = ResultType.SUCCESS;
    private T payload;

    /**
     * gets ResultType
     * @return ResultType
     */
    public ResultType getType() {
        return type;
    }

    /**
     * returns whether the result type is success or not
     * @return boolean
     */
    public boolean isSuccess() {
        return type == ResultType.SUCCESS;
    }

    /**
     * gets payload
     * @return Generic type
     */
    public T getPayload() {
        return payload;
    }

    /**
     * sets payload
     * @param payload
     */
    public void setPayload(T payload) {
        this.payload = payload;
    }

    /**
     * gets list of messages
     * @return
     */
    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }

    /**
     * adds messages to the arraylist
     * @param message
     * @param type
     */
    public void addMessages(String message, ResultType type) {
        messages.add(message);
        this.type = type;
    }
    
}
