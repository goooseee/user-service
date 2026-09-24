package com.example.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotBlank(message = "Имя не может быть пустым")
	private String name;
	@Email(message = "Некорректный формат email")
    @NotBlank(message = "Email не может быть пустым")
	private String email;
	@Min(value = 0, message = "Возраст должен быть в диапазоне от 0 до 150")
    @Max(value = 100, message = "Возраст должен быть в диапазоне от 0 до 150")
	private int age;
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	public User(String name, String email, int age) {
		this.name = name;
		this.email = email;
		this.age = age;
	}
	@PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
