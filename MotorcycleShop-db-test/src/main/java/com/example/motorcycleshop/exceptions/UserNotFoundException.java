package com.example.motorcycleshop.exceptions;

public class UserNotFoundException extends RuntimeException{

        public UserNotFoundException(String message) {
            super(message);
        }
    }

