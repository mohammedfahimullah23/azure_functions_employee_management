# Employee Management System with Azure Functions

This project demonstrates a distributed architecture where a Spring Boot application handles the web interface and API routing, while an Azure Function manages the serverless execution of employee data processing and database persistence.

---

## 🛠 Prerequisites

Java 11 or 17

Maven 3.x

Azure Functions Core Tools

Visual Studio Code (with Azure Functions extension)

MySQL or PostgreSQL (configured via environment variables)

## 📌 Architecture Flow

1. Client sends a POST request to the Spring Boot App at `/api/functions/create`
2. Spring Boot Service receives the request and forwards the payload to the Azure Function via HTTP
3. Azure Function validates the data, interacts with the database, and returns the created record
4. Spring Boot Service receives the result and returns it to the client

---

## ⚡ Task 1: Azure Function Development

### 1. Local Setup

Before running the function, create a `local.settings.json` file in the function project root to manage environment variables:

```json
{
  "IsEncrypted": false,
  "Values": {
    "AzureWebJobsStorage": "UseDevelopmentStorage=true",
    "FUNCTIONS_WORKER_RUNTIME": "java",
    "DB_URL": "jdbc:mysql://your-db-endpoint:3306/db_name",
    "DB_USERNAME": "your_user",
    "DB_PASSWORD": "your_password"
  }
}
```

### 2. Local Execution

Build and run the Azure Function locally using Maven:

```
mvn clean package
mvn azure-functions:run
```
### Local Endpoint:

```
http://localhost:7071/api/employees/process
```

### 3. Deployment to Azure

```
mvn clean package
mvn azure-functions:deploy
```

### Post-Deployment Configuration
After deployment, the function will fail unless environment variables are configured in Azure.

Steps:
- Go to Azure Portal
- Open Function App
- Navigate to Configuration
- Add the following Application Settings:
  -DB_URL
  -DB_USERNAME
  -DB_PASSWORD
- Save and restart the Function App

Main app which is consuming this function app is available in https://github.com/mohammedfahimullah23/employeemanagement
