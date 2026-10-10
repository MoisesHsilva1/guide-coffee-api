# Guide Coffee API

Spring Boot REST API for creating and browsing coffee shop reviews. Reviews are stored in MongoDB, and optional images are uploaded to Cloudinary.

## Requirements

- Java 21+
- Docker and Docker Compose
- Cloudinary account for image uploads

## Configuration

Set the following environment variables before starting the API:

```bash
export SPRING_DATA_MONGODB_URI='mongodb://localhost:27017/guidecoffee'
export CLOUDINARY_URL='cloudinary://<api-key>:<api-secret>@<cloud-name>'
export APP_CORS_ALLOWED_ORIGINS='http://localhost:3000'
```

## Run

Start MongoDB and the application:

```bash
docker compose up -d mongodb
./mvnw spring-boot:run
```

The API runs at `http://localhost:8080`.

## API

Base path: `/api/v1/reviews`

- `POST /api/v1/reviews` — create a review using `multipart/form-data`.
- `GET /api/v1/reviews?offset=0&limit=10` — list reviews with pagination.
- `GET /api/v1/reviews/{id}` — get a review by ID.

Create review fields:

- `title` — required, up to 120 characters.
- `description` — required, up to 2,000 characters.
- `address` — required, up to 120 characters.
- `rating` — required, from 1 to 5.
- `drinkType` — optional.
- `imageUrl` — optional JPEG, PNG, or WebP file.

Example:

```bash
curl -X POST http://localhost:8080/api/v1/reviews \
  -F 'title=Morning Brew' \
  -F 'description=Great espresso and a quiet atmosphere.' \
  -F 'address=123 Main Street' \
  -F 'rating=5' \
  -F 'drinkType=Espresso'
```

## API documentation

With the application running:

- Swagger UI: <http://localhost:8080/swagger-ui.html>
- OpenAPI JSON: <http://localhost:8080/v3/api-docs>

## Tests and build

```bash
./mvnw test
./mvnw clean package
```

To build the Docker image:

```bash
docker build -t guide-coffee-api .
```
