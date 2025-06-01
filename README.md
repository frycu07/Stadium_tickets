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

## API Usage

### Authentication

The API uses JWT (JSON Web Token) for authentication. To access protected endpoints, you need to:

1. Obtain a JWT token by logging in
2. Include the token in the `Authorization` header of your requests

#### Login

```
POST /api/auth/login
```

Request body:
```json
{
  "username": "your_username",
  "password": "your_password"
}
```

**Note:** Always use the plain text password, not the BCrypt hash. For example, to log in as the admin user:
```json
{
  "username": "admin",
  "password": "admin"
}
```

Response:
```json
{
  "token": "your_jwt_token",
  "type": "Bearer",
  "id": 1,
  "username": "your_username",
  "email": "your_email",
  "roles": ["ROLE_USER", "ROLE_ADMIN"]
}
```

#### Using the JWT Token

For all protected endpoints, include the JWT token in the Authorization header:

```
Authorization: Bearer your_jwt_token
```


### Common Issues

#### 403 Forbidden Error

If you receive a 403 Forbidden error when accessing protected endpoints, check:

1. You are using a valid JWT token in the Authorization header
2. The token is formatted correctly: `Bearer your_jwt_token`
3. Your user account has the appropriate role for the endpoint

#### 401 Unauthorized Error

If you receive a 401 Unauthorized error, your JWT token might be:

1. Missing from the request
2. Expired
3. Invalid

Try logging in again to obtain a new token.

## Troubleshooting

### Flyway Migration Checksum Mismatch

If you encounter an error like:
```
Migration checksum mismatch for migration version X
-> Applied to database : XXXXXXXXX
-> Resolved locally    : XXXXXXXXX
```

This means that a migration file has been modified after it was already applied to the database. There are two ways to resolve this:

1. **Disable Flyway Validation (Current Configuration)**

   The application is configured with `spring.flyway.validate-on-migrate=false` in `application.properties` to allow modified migrations, particularly for the admin password change in V3.

2. **Use Flyway Repair**

   If you need to update the checksums in the database to match the current migration files:

   ```bash
   docker-compose run --rm flyway repair
   ```

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

### Entity Relationship Diagram (ERD)

An Entity Relationship Diagram (ERD) is available to visualize the database schema and relationships between entities. The ERD is created using PlantUML and can be found at:

- PlantUML file: `src/main/resources/erd_diagram.puml`
- Documentation: `src/main/resources/ERD_README.md`

To view the ERD diagram:

1. Open the PlantUML file in an IDE with PlantUML plugin (IntelliJ IDEA, VS Code)
2. Use the online PlantUML server: [PlantUML Online Server](https://www.plantuml.com/plantuml/uml/)
3. See the `ERD_README.md` file for more detailed instructions
