# gRPC Wallet #

[![GitHub issues](https://img.shields.io/github/issues/jeetkunecoder/grpc-wallet.svg)](https://github.com/jeetkunecoder/grpc-wallet/issues)
[![GitHub forks](https://img.shields.io/github/forks/jeetkunecoder/grpc-wallet.svg)](https://github.com/jeetkunecoder/grpc-wallet/network)
[![GitHub stars](https://img.shields.io/github/stars/jeetkunecoder/grpc-wallet.svg)](https://github.com/jeetkunecoder/grpc-wallet/stargazers)

A gRPC-based implementation of a wallet client and server. The wallet server keeps track
of a user's monetary balance in the system. The client emulates users performing
deposit and withdrawal transactions.

### How do I get set up? ###

Clone the project
```
git clone
```

#### Database Configuration ####

Get the Postgres Docker image:
```
docker pull postgres
```

Start the Postgres container:
```
docker run --rm --name postgres-docker -e POSTGRES_PASSWORD=b3tp4w4 -d -p 5432:5432 postgres
```

Run the Postgres script to create and populate the DB.
In the terminal, run the following command from the project's root:
```
psql -h localhost -U postgres -f src/main/resources/wallet-schema.sql
```

Then, enter the password from the previous step: b3tp4w4

#### Running the wallet server and client ####

1. From the project's root, generate builds for the server and the client:
```
./gradlew installDist
```

2. Run the server:
```
./build/install/betpawa-wallet/bin/wallet-server
```

3. In another terminal, run the client:
```
./build/install/betpawa-wallet/bin/wallet-client -u <num_users> -t <num_threads> -r <rounds>
```
Where:

 -r,--rounds <arg>    number of rounds each thread is executing
 -t,--threads <arg>   number of concurrent requests a user will make
 -u,--users <arg>     number of concurrent users emulated
