package com.example.esamepo.exception;

public class MyExceptionSchema {
        private String message;
        private String details;
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
