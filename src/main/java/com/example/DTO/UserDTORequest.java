package com.example.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UserDTORequest(
		@NotBlank(message = "Имя не может быть пустым")
		String name,
		@Email(message = "Некорректный формат email")
	    @NotBlank(message = "Email не может быть пустым")
		String email,
		@Min(value = 0, message = "Возраст должен быть в диапазоне от 0 до 150")
	    @Max(value = 100, message = "Возраст должен быть в диапазоне от 0 до 150")
		int age) {

}
