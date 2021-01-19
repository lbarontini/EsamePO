package com.example.esamepo.exception;


/**
 * schema class for generating exception json output
 */
public class MyExceptionSchema {

    /**
     * main exception message
     */
        private String message;

    /**
     * details of the exception
     */
        private String details;

    /**
     * action to make for resolving the error
     */
        private String todo;


        protected MyExceptionSchema() {}

        public MyExceptionSchema(
                String message, String details, String todo) {
            this.message = message;
            this.details = details;
            this.todo = todo;
        }

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
