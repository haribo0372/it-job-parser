# IT Job Parser

## Описание

IT Job Parser - это веб-приложение на базе Spring Boot, которое парсит IT-вакансии с сайтов [hh.ru](https://hh.ru) и [career.habr.com](https://career.habr.com) и показывает все вакансии на собственном сайте.

## Функциональность

- Парсинг вакансий с сайтов hh.ru и career.habr.com
- Отображение всех собранных вакансий в удобном формате
- Возможности отфильтровать вакансии по требованиям пользователи
- Автоматическая проверка актуальности ссылок на вакансии
- Поддержка авторизации
- Возможность авторизированному пользователю добавлять понравившиеся вакансии в избранное 
- CRUD приложение для администраторов

## Технологии

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- Spring Security
- PostgreSQL
- Jsoup (для парсинга HTML)
- Thymeleaf (для представлений)
- Docker (для контейнеризации)
- Maven (для управления зависимостями и сборки проекта)

## Установка и запуск

### Предварительные требования

- Java 21
- Maven
- Docker
- PostgreSQL

### Настройка окружения

1. Склонируйте репозиторий:

    ```bash
    git clone https://github.com/haribo0372/it-job-parser.git
    cd it-job-parser
    ```

2. Создайте файл `.env` в корне проекта и добавьте следующие переменные окружения:

    ```env
    USER_AGENT=your_user_agent
    DB_HOST=localhost
    DB_PORT=5432
    DB_NAME=itjobparser
    DB_USERNAME=your_db_username
    DB_PASSWORD=your_db_password
    ADMIN_LOGIN=your_admin_login
    ADMIN_PASSWORD=your_admin_password
    ```

3. Настройте `application.properties` для подключения к базе данных PostgreSQL:

    ```properties
    spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    spring.datasource.username=${DB_USERNAME}
    spring.datasource.password=${DB_PASSWORD}
    spring.jpa.hibernate.ddl-auto=update
    ```

### Сборка и запуск приложения

#### Запуск с использованием docker

1. Соберите проект с помощью Maven:

    ```bash
    mvn clean package
    ```

2. Запустите Docker-контейнеры:

    ```bash
    docker-compose up --build
    ```
   * **Примечание**: если вы хотите запускать spring приложение внутри контейнера без парсинга вакансий, то измените 7 строку Dockerfile на
     ```
     CMD ["java", "-jar", "/jobParser.jar"] ```
#### Запуск без использования docker
1. Соберите проект с помощью Maven:

    ```bash
    mvn clean package
    ```

2. Запустите Spring Boot приложение:

    ```bash
    java -jar target/jobparser-0.0.1-SNAPSHOT.jar
    ```
   или если вы хотите запустить приложение с парсингом и заполнением базы данных вакансиями, то используйте:
    ```bash
    java -jar target/jobparser-0.0.1-SNAPSHOT.jar --fill_db=true
    ```

### Использование

- Откройте браузер и перейдите по адресу [http://localhost:8080](http://localhost:8080).

## Разработка

### Структура проекта

- `src/main/java/com/osipov/jobparser` - основной код приложения
- `src/main/resources` - ресурсы приложения (шаблоны, стили, файлы конфигурации)

### Основные классы и компоненты

- `JobParserApplication` - основной класс Spring Boot приложения
- `DatabaseInitializer` - класс для предварительного заполнения базы данных
- `ApplicationRunner` - класс для обработки параметров запуска
- `HHParser` - класс для парсинга [hh.ru](https://hh.ru)
- `HabrCareerParser` - класс для парсинга [career.habr.com](https://career.habr.com)
- `ParseManager` - менеджер, который связывает результаты парсинга с БД
- `Vacancy` - сущность, представляющая вакансии
- `City` - сущность, представляющая города
- `Profession` - сущность, представляющая спициальности
- `Skill` - сущность, представляющая навыки
- `User` - сущность, представляющая зарегистрированного пользователя
- `Role` - сущность, представляющая роли для пользователей
- `VacancyService` - сервис для работы с вакансиями
- `UserService` - сервис для работы с пользователями
- `MyMvcConfig` - конфигурационный класс для MVC
- `SecurityConfig` - конфигурационный класс для Spring Security
