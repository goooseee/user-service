package com.example.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserDTOResponse(
		Long id,
		String name,
		String email,
		int age,
		LocalDateTime createdAt) {
}
