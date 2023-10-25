package com.example.lab6.domain.validators;

import com.example.lab6.domain.validators.ValidationException;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}