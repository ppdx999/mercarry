# Mercarry

2nd Hand Shopping Service

# Setup

1. setup postgresql

```
CREATE DATABASE mercarry_db;
CREATE USER mercarry_user WITH ENCRYPTED PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE mercarry_db TO mercarry_user;
```

2. Run the app

```
./gradlew bootRun
```