package com.example.configuration;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ValidatorUtil {
	
	private static final Validator VALIDATOR;
	
	static {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		VALIDATOR = factory.getValidator();
	}
	
	public static <T> void validate(T object) {
		Set<ConstraintViolation<T>> violations = VALIDATOR.validate(object);
		if(!violations.isEmpty()) {
			String errorMessage = violations.iterator().next().getMessage();
			throw new IllegalArgumentException(errorMessage);
		}
	}

}
