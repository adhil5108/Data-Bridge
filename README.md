#  Data-Bridge

**Data-Bridge** is an event-driven migration engine designed to simplify moving data from **NoSQL databases (MongoDB)** to **SQL systems (PostgreSQL)**.

The platform focuses on safe, scalable data migration through modular services, asynchronous processing, and structured schema transformation.

Its goal is to reduce the complexity of database paradigm transitions while maintaining reliability, scalability, and clean system design.

---

##  What It Does

- Migrates large datasets from NoSQL to SQL databases
- Performs schema discovery and data transformation
- Processes migration tasks asynchronously
- Supports scalable worker-based execution
- Ensures reliable and maintainable migration workflows

---

##  Architecture Overview

Data-Bridge follows a modular, event-driven architecture:

- **API Gateway** — Handles requests and authentication
- **Orchestrator Service** — Coordinates migration workflows
- **Worker Services** — Process migration jobs asynchronously
- **Message Broker (Kafka)** — Enables fault-tolerant communication
- **Target Database (PostgreSQL)** — Stores migrated structured data

Each component operates independently, allowing horizontal scalability and easier system evolution.

---

##  Tech Stack

- Java
- Spring Boot
- Apache Kafka
- MongoDB
- PostgreSQL
- Redis
- Docker

---

##  Project Status

Actively under development.  
Architecture and features continue to evolve as the system grows.

---

##  Vision

Modern applications often outgrow their original database choices.  
Data-Bridge aims to provide a reliable bridge between NoSQL flexibility and SQL consistency, enabling safer and more predictable migrations.

---

##  Author
Hadil 
