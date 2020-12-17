package com.example.esamepo.exception;
//this is the schema to use for custom springboot exceptions
public class ApiSchemaException extends RuntimeException {
    private String message;
    private String details;
    private String todo;

    public ApiSchemaException() {
        super();
    }

    public ApiSchemaException(String message, String details, String todo) {
        super.printStackTrace();
        this.message = message;
        this.details = details;
        this.todo = todo;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
