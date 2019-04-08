# gRPC Wallet #

A gRPC-based implementation of a wallet client and server. The wallet server keeps track
of a user's monetary balance in the system. The client emulates users performing
deposit and withdrawal transactions.

### How do I get set up? ###

#### Database Configuration ####

Get the Postgres Docker image:
```
#!terminal
docker pull postgres
```

Start the Postgres container:
```
#!terminal
docker run --rm --name postgres-docker -e POSTGRES_PASSWORD=b3tp4w4 -d -p 5432:5432 postgres
```

Run the Postgres script to create and populate the DB.
In the terminal, go to betpawa-wallet/src/main/resources and execute:
```
#!terminal
psql -h localhost -U postgres -f wallet-schema.sql
```

Then, enter the password from the previous step: b3tp4w4

#### Running the wallet server and client ####

1. From the project's root, generate builds for the server and the client:
```
#!terminal
./gradlew installDist
```

2. Run the server:
```
#!terminal
./build/install/betpawa-wallet/bin/wallet-server
```

3. In another terminal, run the client:
```
#!terminal
./build/install/betpawa-wallet/bin/wallet-client
```