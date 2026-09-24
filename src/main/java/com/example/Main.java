package com.example;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.example.DAO.UserDAO;
import com.example.DAO.UserDAOImpl;
import com.example.DTO.UserDTORequest;
import com.example.DTO.UserDTOResponse;
import com.example.entity.User;
import com.example.exception.DAOException;
import com.example.service.UserService;

public class Main {
	
	private static final UserDAO userDao = new UserDAOImpl();
    private static final UserService userService = new UserService(userDao);
    private static final Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
		boolean running = true;

        System.out.println("=== Консольное приложение User Service ===");

        while (running) {
            printMenu();
            System.out.print("Выберите действие: ");
            
            if (!scanner.hasNextInt()) {
                System.out.println("Ошибка: Введите число!");
                scanner.nextLine().trim();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine().trim();

            switch (choice) {
                case 1 -> createUser();
                case 2 -> findUserById();
                case 3 -> showAllUsers();
                case 4 -> updateUser();
                case 5 -> deleteUser();
                case 0 -> {
                    running = false;
                    System.out.println("Завершение работы приложения...");
                }
                default -> System.out.println("Неверный ввод. Попробуйте снова.");
            }
            System.out.println();
        }

        scanner.close();
	}
	
	private static void printMenu() {
        System.out.println("\n-----------------------------");
        System.out.println("1. Создать пользователя");
        System.out.println("2. Найти пользователя по ID");
        System.out.println("3. Вывести всех пользователей");
        System.out.println("4. Обновить пользователя");
        System.out.println("5. Удалить пользователя");
        System.out.println("0. Выход");
        System.out.println("-----------------------------");
    }
	
	private static void createUser() {
		try {
			System.out.print("Введите имя: ");
	        String name = scanner.nextLine().trim();
	
	        System.out.print("Введите email: ");
	        String email = scanner.nextLine().trim();
	
	        System.out.print("Введите возраст: ");
	        int age = Integer.parseInt(scanner.nextLine().trim());
        	userService.createUser(new UserDTORequest( name, email, age ));
		}catch (NumberFormatException e) {
            System.out.println("Ошибка ввода: Возраст должен быть целым числом!");
        }catch(IllegalArgumentException e) {
        	System.out.println("Ошибка валидации: " + e.getMessage());
        }catch (DAOException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
        }
    }
	
	private static void findUserById() {
		try {
        System.out.print("Введите ID пользователя: ");
        Long id = Long.parseLong(scanner.nextLine().trim());

        Optional<UserDTOResponse> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            UserDTOResponse user = userOptional.get();
            System.out.printf("Найден пользователь: ID=%d, Name=%s, Email=%s, Age=%d, CreatedAt=%s%n",
                    user.id(), user.name(), user.email(), user.age(), user.createdAt());
        } else {
            System.out.println("Пользователь с ID " + id + " не найден.");
        }
		}catch (NumberFormatException e) {
            System.out.println("ID должен быть целым числом");
        }catch (DAOException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
        }
    }
	
	private static void showAllUsers() {
		try {
        List<UserDTOResponse> users = userService.findAll();
        if (users.isEmpty()) {
            System.out.println("Список пользователей пуст.");
        } else {
            System.out.println("Список всех пользователей:");
            users.forEach(u -> System.out.printf("ID=%d | Name=%s | Email=%s | Age=%d | CreatedAt=%s%n",
                    u.id(), u.name(), u.email(), u.age(), u.createdAt()));
        }
		}catch (DAOException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
        }
    }
	
	private static void updateUser() {
		try {
		System.out.print("Введите ID пользователя для обновления: ");
        Long id = Long.parseLong(scanner.nextLine().trim());
        
        	Optional<UserDTOResponse> userOptional = userService.findById(id);
        if (userOptional.isEmpty()) {
            System.out.println("Пользователь с ID " + id + " не найден.");
            return;
        }

        UserDTOResponse user = userOptional.get();
        System.out.print("Введите новое имя (или нажмите Enter, чтобы оставить '" + user.name() + "'): ");
        String newName = scanner.nextLine().trim();
        if (newName.isBlank()) {
            newName = user.name();
        }

        System.out.print("Введите новый email (или нажмите Enter, чтобы оставить '" + user.email() + "'): ");
        String newEmail = scanner.nextLine().trim();
        if (newEmail.isBlank()) {
            newEmail = user.email();
        }

        System.out.print("Введите новый возраст (или 0, чтобы оставить " + user.age() + "): ");
        String newAge = scanner.nextLine().trim();
        int age = user.age();
        if(!newAge.isBlank()) {
	        try {
	        	age = Integer.parseInt( newAge );
	        }catch(Exception e) {
	        	System.out.println("Некорректный формат возраста. Оставлено прежнее значение: " + age);
	        }
        }
        UserDTORequest userDTO = new UserDTORequest(newName, newEmail, age);
        
        userService.updateUser(id,userDTO);
        }catch (NumberFormatException e) {
            System.out.println("ID должен быть целым числом");
        }catch(IllegalArgumentException e) {
        	System.out.println("Ошибка валидации: " + e.getMessage());
        }catch (DAOException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
        }
    }
	
	private static void deleteUser() {
		try {
        System.out.print("Введите ID пользователя для удаления: ");
        Long id = Long.parseLong(scanner.nextLine().trim());
        	userService.deleteUser(id);
        }catch (NumberFormatException e) {
            System.out.println("ID должен быть целым числом");
        }catch(DAOException e) {
        	System.out.println("Ошибка базы данных: " + e.getMessage());
        	e.printStackTrace();
        }
    }
}
