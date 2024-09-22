# Mercarry

2nd Hand Shopping Service

# Setup

1. setup postgresql

```
docker compose up -d
```

or if you use local postgres, get into sql console by `psql` and run this command

```
CREATE USER mercarry_user WITH ENCRYPTED PASSWORD 'password';
CREATE DATABASE mercarry_db OWNER mercarry_user;
GRANT ALL PRIVILEGES ON DATABASE mercarry_db TO mercarry_user;
```

2. Run the app

```
./gradlew bootRun
```
