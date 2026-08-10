# User Service

Консольное Java-приложение для управления пользователями (CRUD) с использованием Hibernate ORM и PostgreSQL.

## Технологии
* Java 17+
* Hibernate 6
* PostgreSQL
* Maven
* Lombok
.
## Инструкция по запуску
1. Создайте базу данных PostgreSQL (например, `user_service`).
2. Укажите свои данные подключения в `src/main/resources/hibernate.properties`:
   ```properties
   hibernate.connection.url=jdbc:postgresql://localhost:5433/user_service
   hibernate.connection.username=postgres
   hibernate.connection.password=your_password