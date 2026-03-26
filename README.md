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

- https://recipe-backend-production-2e13.up.railway.app/swagger-ui/index.html

## Postman Collection

- https://justinmnmj-9799560.postman.co/workspace/Justin-Martinez's-Workspace~91629d83-a482-4a4c-8e39-aa74c64b0269/collection/51750544-d1028f60-5505-4838-809b-faaff1db8121?action=share&creator=51750544&active-environment=51750544-d10f3341-c03e-4e9d-a435-3c3419da08db

<img width="1974" height="1221" alt="image" src="https://github.com/user-attachments/assets/c20ce9fa-d3de-44a9-8f94-d2c0dd6cae92" />
