package com.example.esamepo.exception;


/**
 * class for handling server exception,
 * launched when the api on https://api.domainsdb.info/v1/ does notwork properly
 */
public class ServerException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private static final String message= "ServerException";
    private String details;
    private String todo;

    public ServerException() {
        super();
    }

    public ServerException( String details, String todo) {
        super.printStackTrace();
        this.details = details;
        this.todo = todo;
    }

    @Override
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
