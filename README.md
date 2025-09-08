# Benchmark Applications

This repository provides a collection of self-contained applications designed for benchmarking agent driven application refactoring focusing on enterprise java modernization and migration. Each application is structured to be easily deployable and modifiable, allowing for comprehensive testing of refactoring strategies.

## Prerequisites
Install / enable the following before running any examples.

1. just
   - Project command runner used throughout the repository.
   - Install (choose one):
     - With package manager (Debian/Ubuntu): `sudo apt install just` (may require adding a backport / latest repo)
     - With Cargo: `cargo install just`
     - See latest instructions: https://github.com/casey/just
   - Verify: `just --version`

2. Docker
   - Required to build and run containerized examples.
   - Install Engine: https://docs.docker.com/engine/install/
   - Add your user to the docker group (logout/login): `sudo usermod -aG docker "$USER"`
   - Verify: `docker run --rm hello-world`

3. Docker Compose
   - Used for multi-service examples.
   - Modern Docker includes the `docker-compose` plugin. If you still need the v1 binary follow: https://docs.docker.com/compose/
   - Verify: `docker-compose version`

4. Java (multiple toolchains via SDKMAN!)
   - Some projects target different LTS versions (11, 17, 21).
   - Install SDKMAN:
     ```bash
     curl -s "https://get.sdkman.io" | bash
     source "$HOME/.sdkman/bin/sdkman-init.sh"
     ```
   - Install required JDKs (Semeru examples shown — use any compatible distribution):
     ```bash
     sdk install java 11.0.23-sem
     sdk install java 17.0.11-sem
     sdk install java 21.0.3-sem
     ```
   - (Optional) Set a default (e.g. latest LTS): `sdk default java 21.0.3-tem`
   - Switch per need: `sdk use java 17.0.11-tem`
   - Verify: `java -version`

# Benchmark Structure


# Transformation Matrix (arranged by benchmark folder structure)

## Legend
- 🛑: Not started
- ✅: Complete
- 🏗️: In progress

## 1. Whole Applications (`/whole_applications/`)

| Application  | Spring | Jakarta | Quarkus | Struts |
| ------------ | ------ | ------- | ------- | ------ |
| cargotracker | ✅      | ✅       | ✅       | 🛑      |
| coffeeshop   | ✅      | 🛑       | ✅       | 🛑      |
| daytrader    | ✅      | ✅       | ✅       | 🛑      |
| realworld    | ✅      | ✅       | ✅       | 🛑      |
| petclinic    | ✅      | 🛑       | ✅       | 🛑      |
| mailreader2*  | 🛑      | 🛑       | 🛑       | ✅      |

*not in paper 
---

## 2. Dependency Injection (`/dependency_injection/`)

| Application     | Spring | Jakarta | Quarkus | Struts |
| --------------- | ------ | ------- | ------- | ------ |
| billpayment     | ✅      | ✅       | 🛑       | 🛑      |
| decorators      | 🛑      | ✅       | 🛑       | 🛑      |
| encoder         | 🛑      | ✅       | 🛑       | 🛑      |
| guessnumber     | 🛑      | ✅       | 🛑       | 🛑      |
| producerfields  | 🛑      | ✅       | 🛑       | 🛑      |
| producermethods | 🛑      | ✅       | 🛑       | 🛑      |
| simplegreeting  | 🛑      | ✅       | 🛑       | 🛑      |

---
## 3. Persistence (`/persistence/`)

| Application  | Spring | Jakarta | Quarkus | Struts |
| ------------ | ------ | ------- | ------- | ------ |
| address-book | ✅      | ✅       | ✅       | 🛑      |
| order        | ✅      | ✅       | 🛑       | 🛑      |
| roster       | ✅      | ✅       | 🛑       | 🛑      |

---

## 4. Presentation (`/presentation/`)

| Application   | Spring | Jakarta | Quarkus | Struts |
| ------------- | ------ | ------- | ------- | ------ |
| dukeetf       | 🛑      | ✅       | 🛑       | 🛑      |
| dukeetf2      | 🛑      | ✅       | 🛑       | 🛑      |
| fileupload    | 🛑      | ✅       | 🛑       | 🛑      |
| hello-servlet | 🛑      | ✅       | 🛑       | 🛑      |
| mood          | 🛑      | ✅       | 🛑       | 🛑      |
| websocketbot  | 🛑      | ✅       | 🛑       | 🛑      |
| jaxrs-hello   | 🛑      | ✅       | 🛑       | 🛑      |
| jaxrs-rsvp    | 🛑      | ✅       | 🛑       | 🛑      |
| jaxrs-customer| 🛑      | ✅       | 🛑       | 🛑      |

---

## 5. Business Domain (`/business_domain/`)

| Application   | Spring | Jakarta | Quarkus | Struts |
| ------------- | ------ | ------- | ------- | ------ |
| cart          | 🛑      | ✅       | 🛑       | 🛑      |
| converter     | 🛑      | ✅       | 🛑       | 🛑      |
| counter       | 🛑      | ✅       | 🛑       | 🛑      |
| helloservice  | 🛑      | ✅       | 🛑       | 🛑      |
| standalone    | 🛑      | ✅       | 🛑       | 🛑      |

---

## 6. Integration (`/integration/`)

| Application             | Spring | Jakarta | Quarkus | Struts |
| ----------------------- | ------ | ------- | ------- | ------ |
| jms-simple              | 🛑      | ✅       | 🛑       | 🛑      |
| jms-simplemessage       | 🛑      | ✅       | 🛑       | 🛑      |
| jms-clientmdbentity     | 🛑      | ✅       | 🛑       | 🛑      |
| jms-clientsessionmdb    | 🛑      | ✅       | 🛑       | 🛑      |
| jms-durablesubscription | 🛑      | ✅       | 🛑       | 🛑      |
| jms-transactedexample   | 🛑      | ✅       | 🛑       | 🛑      |
| jms-websimplemessage    | 🛑      | ✅       | 🛑       | 🛑      |
| jaxws-hello-appclient   | 🛑      | ✅       | 🛑       | 🛑      |
| jaxws-hello-webclient   | 🛑      | ✅       | 🛑       | 🛑      |
| connectors-trading      | 🛑      | ✅       | 🛑       | 🛑      |
| connectors-traffic      | 🛑      | ✅       | 🛑       | 🛑      |
| batch-phonebilling      | 🛑      | ✅       | 🛑       | 🛑      |
| batch-webserverlog      | 🛑      | ✅       | 🛑       | 🛑      |

---

## 7. Infrastructure (`/infrastructure/`)

| Application    | Spring | Jakarta | Quarkus | Struts |
| -------------- | ------ | ------- | ------- | ------ |
| ejb-async      | 🛑      | ✅       | 🛑       | 🛑      |
| ejb-timersession| 🛑     | ✅       | 🛑       | 🛑      |
| ejb-interceptor| 🛑      | ✅       | 🛑       | 🛑      |
| concurrency-jobs       | 🛑      | ✅       | 🛑       | 🛑      |
| concurrency-taskcreator| 🛑      | ✅       | 🛑       | 🛑      |
| security-cart-secure    | 🛑      | ✅       | 🛑       | 🛑      |
| security-converter-secure| 🛑     | ✅       | 🛑       | 🛑      |
| security-hello1-formauth| 🛑      | ✅       | 🛑       | 🛑      |
| security-hello2-basicauth| 🛑     | ✅       | 🛑       | 🛑      |
| security-api            | 🛑      | ✅       | 🛑       | 🛑      |
