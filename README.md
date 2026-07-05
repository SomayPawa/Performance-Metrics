# 🚀 High-Performance URL Shortener

A URL shortener built with a focus on **performance optimization, scalability, and benchmarking**.

Instead of stopping at a basic implementation, this project explores how different architectural decisions impact system performance under **1,000 concurrent users**.

---

## ✨ Features

* Generate short URLs from long URLs
* Redirect users using short codes
* Track URL hit counts
* MySQL persistence
* Redis caching
* Redis-based counters
* Batch synchronization to MySQL
* Performance benchmarking using K6

---

## 🏗️ Architecture Evolution

### 1. MySQL (No Index)

Initial implementation:

* Store short codes and original URLs in MySQL
* Update hit count on every request
* Full table scans during lookups

**Problem:**

* High latency
* Poor scalability under load

---

### 2. MySQL (With Index)

Added an index on the `short_code` column.

**Benefits:**

* Faster lookups
* Significant reduction in response time
* Improved throughput

---

### 3. Redis Cache + MySQL Updates

Introduced Redis for caching.

Workflow:

1. Lookup short code in Redis.
2. If not found, fetch from MySQL.
3. Cache the result.
4. Update hit count directly in MySQL.

**Benefits:**

* Reduced database reads
* Faster redirects

**Limitation:**

* Database writes still occurred for every request.

---

### 4. Redis Cache + Redis Counters

Final optimized architecture.

Workflow:

1. Retrieve URL mapping from Redis.
2. Increment hit count using Redis atomic counters.
3. Batch synchronize counters to MySQL every 5 minutes.

**Benefits:**

* Minimal database load
* High throughput
* Low latency
* Better scalability

---

## ⚡ Performance Results (K6 Load Testing)

Test Configuration:

* Concurrent Users: **1000**
* Tool: **K6**
* Multiple iterations executed for consistent measurements

| Architecture                | Avg Latency | P95 Latency  | Throughput (req/s) |
| --------------------------- | ----------- | ------------ | ------------------ |
| MySQL (No Index)            | 221 ms      | 497 ms       | 450                |
| MySQL (With Index)          | 20.88 ms    | 49.25 ms     | 4,778              |
| Redis Cache + MySQL UPDATE  | 21.97 ms    | 26.69 ms     | 4,543              |
| Redis Cache + Redis Counter | **6.74 ms** | **15.56 ms** | **14,749**         |

---

## 📈 Key Learnings

This project helped me understand:

* Database indexing strategies
* Redis caching patterns
* Atomic counters in Redis
* Batch processing concepts
* Reducing write amplification
* Performance testing with K6
* Scalability trade-offs in backend systems
* System design decisions based on metrics

---

## 🛠️ Tech Stack

* Java
* Spring Boot
* MySQL
* Redis
* K6
* Maven

---

## 🚀 Running the Project

### Clone the repository

```bash
git clone https://github.com/SomayPawa/Performance-Metrics.git
cd Performance-Metrics
```

### Start MySQL

Ensure MySQL is running and create the required database.

### Start Redis

```bash
redis-server
```

### Run the application

```bash
mvn spring-boot:run
```

---

## 📊 Running K6 Benchmarks

Example command:

```bash
k6 run load-test.js
```

Example configuration:

```javascript
export const options = {
  vus: 1000,
  duration: "30s",
};
```

---

## 💡 Major Takeaway

> A simple database index can deliver a 10x improvement, but true scalability comes from rethinking where reads and writes should happen.

Measure first. Optimize second.

---

## 👨‍💻 Author

**Somay Lal Pawa**

GitHub: https://github.com/SomayPawa
