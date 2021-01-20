package com.example.esamepo.exception;


/**
 * class for handling server exception,
 * launched when the api on https://api.domainsdb.info/v1/ does notwork properly
 */
public class ServerException extends RuntimeException {

    /**
     * needed for the serialization of RuntimeException
     */
    private static final long serialVersionUID = 1L;

    /**
     * main exception message, always set to "ServerException"
     */
    private static final String message= "ServerException";

    /**
     * details of the exception
     */
    private String details;

    /**
     * action to make for resolving the error
     */
    private String todo;

    /**
     * constructor
     * @param details details of the exception
     * @param todo action to make for resolving the error
     */
    public ServerException( String details, String todo) {
        super.printStackTrace();
        this.details = details;
        this.todo = todo;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }
}
