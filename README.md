# Stadium Tickets Application

This is a Spring Boot application for managing stadium tickets.

## Setup and Running

### Prerequisites
- Java 21 or higher
- Docker and Docker Compose

### Running the Application

1. **Start the Database and Run Migrations**

   Use Docker Compose to start the PostgreSQL database and run Flyway migrations:

   ```bash
   docker-compose up -d
   ```

   This will:
   - Start a PostgreSQL database on port 5432
   - Run Flyway migrations to create the necessary database schema

2. **Run the Application**

   ```bash
   ./mvnw spring-boot:run
   ```

   The application will be available at http://localhost:8080

3. **Access Swagger UI**

   The API documentation is available at:
   http://localhost:8080/swagger-ui.html

## Troubleshooting

### "Schema-validation: missing table [match]" Error

If you encounter this error, it means the database tables haven't been properly created. There are several ways to resolve this:

1. **Ensure Docker Containers are Running**

   Check if the database and Flyway containers are running:

   ```bash
   docker-compose ps
   ```

   If they're not running, start them with:

   ```bash
   docker-compose up -d
   ```

2. **Manually Run Migrations**

   You can manually run the Flyway migrations:

   ```bash
   docker-compose run --rm flyway
   ```

3. **Temporary Solution: Let Hibernate Create Tables**

   As a temporary solution, you can set `spring.jpa.hibernate.ddl-auto=create` in `application.properties`. 
   This will let Hibernate create the tables automatically.

   **Note:** This approach is not recommended for production as it will recreate tables and lose data on each restart.

## Database Schema

The application uses the following tables:

- `stadium`: Stores information about stadiums
- `match`: Stores information about football matches
- `ticket`: Stores information about tickets for matches
- `users` and `roles`: Stores user authentication and authorization information