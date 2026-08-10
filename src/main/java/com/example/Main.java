package com.example;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.example.DAO.UserDAO;
import com.example.DAO.UserDAOImpl;
import com.example.Entity.User;
import com.example.Service.UserService;

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
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

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
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();

        System.out.print("Введите email: ");
        String email = scanner.nextLine();

        System.out.print("Введите возраст: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        userService.createUser(name, email, age);
    }
	
	private static void findUserById() {
        System.out.print("Введите ID пользователя: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.printf("Найден пользователь: ID=%d, Name=%s, Email=%s, Age=%d, CreatedAt=%s%n",
                    user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getCreatedAt());
        } else {
            System.out.println("Пользователь с ID " + id + " не найден.");
        }
    }
	
	private static void showAllUsers() {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            System.out.println("Список пользователей пуст.");
        } else {
            System.out.println("Список всех пользователей:");
            users.forEach(u -> System.out.printf("ID=%d | Name=%s | Email=%s | Age=%d | CreatedAt=%s%n",
                    u.getId(), u.getName(), u.getEmail(), u.getAge(), u.getCreatedAt()));
        }
    }
	
	private static void updateUser() {
        System.out.print("Введите ID пользователя для обновления: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isEmpty()) {
            System.out.println("Пользователь с ID " + id + " не найден.");
            return;
        }

        User user = userOptional.get();
        System.out.print("Введите новое имя (или нажмите Enter, чтобы оставить '" + user.getName() + "'): ");
        String newName = scanner.nextLine();
        if (!newName.isBlank()) {
            user.setName(newName);
        }

        System.out.print("Введите новый email (или нажмите Enter, чтобы оставить '" + user.getEmail() + "'): ");
        String newEmail = scanner.nextLine();
        if (!newEmail.isBlank()) {
            user.setEmail(newEmail);
        }

        System.out.print("Введите новый возраст (или 0, чтобы оставить " + user.getAge() + "): ");
        int newAge = scanner.nextInt();
        scanner.nextLine();
        if (newAge > 0) {
            user.setAge(newAge);
        }

        userService.updateUser(user);
    }
	
	private static void deleteUser() {
        System.out.print("Введите ID пользователя для удаления: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        userService.deleteUser(id);
    }
}
