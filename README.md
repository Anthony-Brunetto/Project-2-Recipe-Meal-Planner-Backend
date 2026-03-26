# Recipe Meal Planner Backend

Backend API for the Recipe Meal Planner application.

## Team Members

- Anthony Brunetto
- Ciaran Moynihan
- Ivan Martinez
- Justin Martinez

## Tech Stack

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- Supabase
- Docker
- Maven

## Run Locally

### Option 1: Run with Docker

1. Clone the repository.
2. Create a `.env` file in the project root.
3. Add the required environment variables:

```env
SPRING_DATASOURCE_URL=[your database url]
SPRING_DATASOURCE_USERNAME=[your database username]
SPRING_DATASOURCE_PASSWORD=[your database password]
SUPABASE_JWT_ISSUER=[your Supabase JWT issuer]
```

4. Build and start the container:

```bash
docker compose up --build
```

5. The API will run on:

```text
http://localhost:8080
```

### Option 2: Run with Maven

1. Make sure Java 21 is installed.
2. Make sure the required environment variables are set.
3. Run the app:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

4. The API will run on:

```text
http://localhost:8080
```

## Live API URL

- https://recipe-backend-production-2e13.up.railway.app

## Swagger Docs

- [Swagger docs link]

## Postman Collection

- [Postman collection link]
