# Video Streaming API

## Introduction

The **Video Streaming API** is designed to provide video content management and streaming capabilities. It allows content managers to publish videos, manage metadata, and track user engagement metrics. This project is built to meet the requirements of a next-generation video streaming platform, ensuring a scalable and efficient solution for handling video assets.

## Why These Choices?

### API-Driven Design

The **Video Streaming API** follows a structured API-driven approach to ensure flexibility, scalability, and maintainability. Below are the key design decisions that shape the API:

#### **1. RESTful API Principles**

- **Resource-Based Endpoints:** The API is designed around core resources such as `/videos`, `/videos/{id}`, and `/videos/search` to provide a clear and predictable interface.
- **Stateless Communication:** Every request is self-contained, allowing horizontal scaling without dependency on session storage.
- **Proper HTTP Methods:**
  - `GET` → Retrieve video metadata or engagement stats.
  - `POST` → Create a new video record.
  - `PUT` → Update video metadata.
  - `DELETE` → Soft-delete a video.
- **Standardized Response Codes:**
  - `200 OK` → Successful retrieval.
  - `201 Created` → New resource successfully created.
  - `400 Bad Request` → Invalid input or request parameters.
  - `404 Not Found` → Resource not found.
  - `500 Internal Server Error` → Unexpected failure.

#### **2. JSON-Based API Responses**

- Uses **Jackson** for JSON serialization and deserialization.
- Ensures uniform API responses with a consistent data structure, facilitating frontend and third-party integrations.
- Example response:
  ```json
  {
    "id": 123,
    "title": "Inception",
    "director": "Christopher Nolan",
    "genre": "Sci-Fi",
    "runningTime": 148
  }
  ```

#### **3. Pagination & Sorting for Large Datasets**

- Supports **pagination** for retrieving large sets of videos efficiently:
  ```sh
  GET /videos?page=0&size=10&sort=title,asc
  ```
- Enables sorting by multiple fields, such as title, release year, and views.
- Uses **Spring Data JPA's Pageable** interface to handle these operations efficiently.

#### **4. Secure & Optimized Data Access**

- Implements **DTO (Data Transfer Object) patterns** to control exposed data.
- Uses **Lazy Loading and Fetch Strategies** in JPA to optimize database queries.
- Avoids **N+1 query issues** by implementing **JPQL joins** where necessary.
- Uses **Spring Validation** to enforce constraints on input data.

#### **5. Scalability & Performance Enhancements**

- Designed to scale horizontally by adding more application instances behind a load balancer.
- Uses pagination for listing videos to handle large datasets efficiently.
- Logging is centralized, making it easier to monitor in distributed environments.
- Can be extended with **Redis or Memcached** for caching frequently accessed metadata.
- Prepared for **asynchronous event handling** via Kafka or RabbitMQ for tracking engagement metrics without blocking requests.
- Future-proofed for **GraphQL support**, allowing more flexible querying of video metadata.

#### **6. Testing Strategy**

- Unit Tests: Mockito is used to isolate components and mock dependencies.
- Integration Tests: Testcontainers ensures a real PostgreSQL instance is used for testing.
- Controller Tests: Uses MockMvc to test API endpoints.

#### **7. Testcontainers for Database Isolation**

- The application uses Testcontainers to spin up a temporary PostgreSQL container for testing.
- Ensures tests do not rely on a local database setup and remain consistent across environments.

#### **8. Containerization & Deployment Strategy**

- The project is container-ready, meaning it can be easily deployed with Docker.
- Uses Docker Compose or Kubernetes for deployment in production-like environments.

#### **9. Code Maintainability & Best Practices**

- Uses Lombok to reduce boilerplate code.
- Method extractions improve readability and reusability.
- Consistent coding standards are maintained across the project.

---

## 🚀 Features

- Video upload and streaming
- Metadata management (title, director, genre, etc.)
- Video search functionality
- Analytics tracking for user engagement

## 🛠️ Technologies Used

- **Backend:** Spring Boot, Java 17
- **Database:** PostgreSQL (or another specified database)
- **Storage:** Local file storage
- **Logging & Monitoring:** Log4j, Prometheus, Grafana

## 📖 API Documentation

This **API-Driven Design** ensures that the **Video Streaming API** remains **scalable, reliable, and easy to integrate** while adhering to industry best practices.

### **1. Get All Videos & Search Videos**

#### Request

```http
GET /videos
```

#### Response

```json
[
  {
    "id": 2,
    "title": "Inception",
    "director": "Christopher Nolan",
    "mainActor": "Leonardo DiCaprio",
    "genre": "Sci-Fi",
    "runningTime": 148
  }
]
```

### **2. Get Video Details**

#### Request

```http
GET /videos/{id}
```

#### Response

```json
{
  "id": 2,
  "title": "Inception",
  "director": "Christopher Nolan",
  "mainActor": "Leonardo DiCaprio",
  "genre": "Sci-Fi",
  "runningTime": 148
}
```

### **3. Create a New Video**

#### Request

```http
POST /videos
Content-Type: application/json
```

#### Body

```json
{
  "title": "Inception",
  "director": "Christopher Nolan",
  "mainActor": "Leonardo DiCaprio",
  "genre": "Sci-Fi",
  "runningTime": 148
}
```

#### Response

```json
{
  "id": 3,
  "title": "Inception",
  "director": "Christopher Nolan",
  "mainActor": "Leonardo DiCaprio",
  "genre": "Sci-Fi",
  "runningTime": 148
}
```

### **4. Play Video**

#### Request

```http
GET /videos/{id}/play
```

#### Response

```
Streaming video: Inception
```

### **5. Get Video Engagement**

#### Request

```http
GET /videos/{id}/engagement
```

#### Response

```json
{
  "videoId": 2,
  "views": 5,
  "impressions": 16
}
```

### **6. Update Video Details**

#### Request

```http
PUT /videos/{id}
Content-Type: application/json
```

#### Body

```json
{
  "title": "Updated Video Title",
  "director": "Updated Director",
  "mainActor": "Updated Actor",
  "genre": "Updated Genre",
  "runningTime": 160
}
```

#### Response

```json
{
  "id": 2,
  "title": "Updated Video Title",
  "director": "Updated Director",
  "mainActor": "Updated Actor",
  "genre": "Updated Genre",
  "runningTime": 160
}
```

### **7. Delete a Video**

#### Request

```http
DELETE /videos/{id}
```

#### Response

```json
{
  "message": "Video successfully deleted"
}
```

# 🗄 Database Schema

The database consists of the following tables:

## 1. `videos`

Stores information about each video.

| Column         | Data Type      | Constraints   | Description                         |
| -------------- | -------------- | ------------- | ----------------------------------- |
| `id`           | `SERIAL`       | Primary Key   | Unique identifier for each video.   |
| `title`        | `VARCHAR(255)` | NOT NULL      | Title of the video.                 |
| `director`     | `VARCHAR(255)` |               | Director of the video.              |
| `main_actor`   | `VARCHAR(255)` |               | Main actor in the video.            |
| `genre`        | `VARCHAR(100)` |               | Genre of the video.                 |
| `running_time` | `INT`          |               | Duration of the video in minutes.   |
| `created_at`   | `TIMESTAMP`    | DEFAULT NOW() | Timestamp when the video was added. |

### SQL Definition:

```sql
CREATE TABLE videos (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    director VARCHAR(255),
    main_actor VARCHAR(255),
    genre VARCHAR(100),
    running_time INT,
    created_at TIMESTAMP DEFAULT NOW()
);
```

## 2. `video_engagement`

Tracks engagement metrics for each video.

| Column        | Data Type | Constraints                   | Description                                                                      |
| ------------- | --------- | ----------------------------- | -------------------------------------------------------------------------------- |
| `id`          | `SERIAL`  | Primary Key                   | Unique identifier for each engagement record.                                    |
| `video_id`    | `INT`     | Foreign Key, UNIQUE, NOT NULL | References the `id` in the `videos` table. Each video has one engagement record. |
| `views`       | `INT`     | DEFAULT 0                     | Number of times the video has been viewed.                                       |
| `impressions` | `INT`     | DEFAULT 0                     | Number of times the video has been displayed or accessed.                        |

### SQL Definition:

```sql
CREATE TABLE video_engagement (
    id SERIAL PRIMARY KEY,
    video_id INT UNIQUE NOT NULL REFERENCES videos(id),
    views INT DEFAULT 0,
    impressions INT DEFAULT 0
);
```

## Notes:

- The `video_engagement` table has a one-to-one relationship with the `videos` table, ensuring each video has a unique engagement record.
- The `views` and `impressions` columns are initialized to zero by default and can be incremented to reflect user interactions.

## Future Improvements

To make this Video Streaming API suitable for real-world applications, the following upgrades are essential:

### **1. Authentication & Authorization**

- Implement **JWT-based authentication** with Spring Security.
- Support **OAuth 2.0** (Google, Facebook login) for broader authentication options.
- Apply **Role-Based Access Control (RBAC)** to manage permissions for admins, users, and guests.

### **2. Scalable Storage & Content Delivery**

- Use **AWS S3, Google Cloud Storage, or Azure Blob Storage** for video hosting instead of local storage.
- Implement **CDN integration** (Cloudflare, AWS CloudFront) to optimize video delivery worldwide.
- Support **Adaptive Bitrate Streaming (HLS/DASH)** to dynamically adjust quality based on user bandwidth.

### **3. Database & Caching Optimization**

- Implement **Flyway or Liquibase** for database schema versioning.
- Optimize metadata queries with **Redis or Memcached caching**.
- Add **proper indexing strategies** to improve search/filter performance.

### **4. Improved API Performance & Scalability**

- Introduce **Kafka or RabbitMQ** for async event handling (e.g., tracking views/impressions).
- Refactor into **microservices** to scale individual services independently.
- Add **GraphQL support** for flexible metadata queries.
- Implement **Rate Limiting & API Throttling** to prevent abuse.

### **5. Monitoring & Logging**

- Use **Prometheus & Grafana** for real-time API monitoring.
- Implement **ELK Stack (Elasticsearch, Logstash, Kibana)** for centralized logging.
- Enable **Distributed Tracing** (Jaeger, OpenTelemetry) to track API request paths.

### **6. Video Processing & AI Enhancements**

- Integrate **FFmpeg-based video transcoding** to support multiple resolutions and formats.
- Implement **AI-based video recommendations** based on watch history and engagement.
- Use **Machine Learning for Automatic Video Tagging** (e.g., detecting actors, scenes).

### **7. Security Hardening**

- Enforce **HTTPS with SSL/TLS** for all API communications.
- Deploy **API Gateway with Web Application Firewall (WAF)** to block malicious traffic.
- Ensure **GDPR & CCPA compliance** for secure user data management.

## How to Run the Project

## 📦 Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/melihcanaydin/VideoStreamingAPI.git
cd VideoStreamingAPI
```

### 2. Navigate to Project Directory

```bash
cd /path/to/your/project
```

### 3. Start the Application

```bash
docker-compose up
```

- If you’ve made changes to the code or Dockerfile, use the `--build` flag to rebuild the containers:

```bash
docker-compose up --build
```

### 4. Access the API

Once the application is running, you can access the API at:

#### Video Management

- **Publish Video:** `POST` → `http://localhost:8080/videos`
- **Update Metadata:** `PUT` → `http://localhost:8080/videos/{id}`
- **Soft Delete Video:** `DELETE` → `http://localhost:8080/videos/{id}`
- **List Available Videos:** `GET` → `http://localhost:8080/videos`
- **Search Videos:** `GET` → `http://localhost:8080/videos/search`

#### Streaming & Engagement

- **Load Video:** `GET` → `http://localhost:8080/videos/{id}/load`
- **Play Video:** `GET` → `http://localhost:8080/videos/{id}/play`
- **Retrieve Engagement Stats:** `GET` → `http://localhost:8080/videos/{id}/stats`

#### Swagger API Documentation

The API includes Swagger UI for interactive API documentation, making it easier to explore, test, and integrate the available endpoints.

#### 📌 How to Access Swagger UI

Once the application is running, you can access the Swagger documentation via:

```bash
http://localhost:8080/swagger-ui/index.html
```

### Postman Collection

To simplify API testing, you can use the provided **Postman Collection**. Import it into **Postman** and run the predefined requests.

## Running Tests

To execute unit and integration tests:

- Run video_db over Docker and;

```sh
mvn test
```
