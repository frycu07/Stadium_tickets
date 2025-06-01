# Aplikacja Zarządzania Biletami Stadionowymi

## Spis treści
1. [Temat](#temat)
2. [Opis](#opis)
3. [Realizowane systemy](#realizowane-systemy)
4. [Struktura Projektu](#struktura-projektu)
5. [Diagram ERD](#diagram-erd)
6. [Tabele bazodanowe](#tabele-bazodanowe)
7. [Pakiety główne](#pakiety-główne)
8. [Kontrolery REST API](#kontrolery-rest-api)
9. [Autoryzacja i bezpieczeństwo](#autoryzacja-i-bezpieczeństwo)
10. [Uruchamianie projektu](#uruchamianie-projektu)
11. [Testowanie](#testowanie)
12. [Przykładowe działanie systemu](#przykładowe-działanie-systemu)

## Temat

Zarządzanie biletami stadionowymi - aplikacja do rezerwacji biletów na mecze oraz zarządzania stadionami i meczami

## Opis

Aplikacja do zarządzania biletami stadionowymi, meczami oraz stadionami. System umożliwia użytkownikom przeglądanie dostępnych meczów, rezerwację biletów na wybrane mecze, a administratorom zarządzanie całą infrastrukturą stadionową.

## Realizowane systemy

### 1. System zarządzania stadionami

- Dodawanie, edycja i usuwanie stadionów
- Zarządzanie informacjami o stadionach (nazwa, miasto, pojemność)
- Wyszukiwanie stadionów

### 2. System zarządzania użytkownikami (RBAC)

- **USER**: Przeglądanie dostępnych meczów, zakup biletów, zarządzanie własnymi danymi
- **ADMIN**: Wszystkie funkcjonalności użytkownika rozszerzone o zarządzanie stadionami, meczami, użytkownikami oraz biletami w systemie

### 3. System zarządzania meczami

- Tworzenie nowych meczów z określeniem drużyn i terminu
- Przypisywanie meczów do konkretnych stadionów
- Przeglądanie informacji o meczach
- Edycja i usuwanie meczów

### 4. System zarządzania biletami

- Przeglądanie dostępnych biletów na mecze
- Zakup biletów na wybrane mecze
- Anulowanie zakupionych biletów
- Zarządzanie statusami biletów (FREE, RESERVED, SOLD)

### 5. System autoryzacji i bezpieczeństwa

- Kontrola dostępu oparta na rolach (Role-Based Access Control)
- Bezpieczne uwierzytelnianie użytkowników z wykorzystaniem JWT
- Szyfrowanie haseł za pomocą BCrypt
- Ochrona endpoints według uprawnień użytkownika

## Struktura Projektu

```
src/
├── main/
│   ├── java/org/example/stadium_tickets/
│   │   ├── config/                    # Konfiguracja Spring Security
│   │   │   └── SecurityConfig.java
│   │   ├── controller/                # Kontrolery REST API
│   │   │   ├── AdminController.java
│   │   │   ├── AuthController.java
│   │   │   ├── MatchController.java
│   │   │   ├── StadiumController.java
│   │   │   ├── TicketController.java
│   │   │   └── UserController.java
│   │   ├── entity/                    # Encje JPA
│   │   │   ├── User.java
│   │   │   ├── Role.java
│   │   │   ├── Stadium.java
│   │   │   ├── Match.java
│   │   │   └── Ticket.java
│   │   ├── payload/                   # Obiekty żądań i odpowiedzi
│   │   │   ├── request/
│   │   │   │   ├── LoginRequest.java
│   │   │   │   └── PasswordlessLoginRequest.java
│   │   │   └── response/
│   │   │       └── JwtResponse.java
│   │   ├── repository/                # Repozytoria Spring Data
│   │   │   ├── UserRepository.java
│   │   │   ├── RoleRepository.java
│   │   │   ├── StadiumRepository.java
│   │   │   ├── MatchRepository.java
│   │   │   └── TicketRepository.java
│   │   ├── security/                  # Komponenty bezpieczeństwa
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   └── JwtUtils.java
│   │   ├── service/                   # Logika biznesowa
│   │   │   ├── UserService.java
│   │   │   ├── StadiumService.java
│   │   │   ├── MatchService.java
│   │   │   └── TicketService.java
│   │   └── StadiumTicketsApplication.java
│   └── resources/
│       ├── db/migration/              # Migracje Flyway
│       │   ├── V1__init_schema.sql
│       │   ├── V2__create_users_and_roles.sql
│       │   ├── V3__add_sample_data.sql
│       │   └── V6__add_passwordless_users.sql
│       └── application.properties
├── test/                              
│   └── java/org/example/stadium_tickets/
│       ├── controller/                # Testy kontrolerów
│       ├── security/                  # Testy komponentów bezpieczeństwa
│       ├── service/                   # Testy serwisów
│       └── StadiumTicketsApplicationTests.java
├── docker-compose.yml                 # Konfiguracja Docker
└── pom.xml                           # Konfiguracja Maven
```

## Diagram ERD

Diagram ERD jest dostępny w pliku `src/main/resources/erd_diagram.puml` i przedstawia relacje między encjami w systemie.

## Tabele bazodanowe

Aplikacja wykorzystuje PostgreSQL z następującymi tabelami:

### 1. users - Informacje o użytkownikach systemu

- id: SERIAL PRIMARY KEY
- username: VARCHAR(50) NOT NULL UNIQUE
- password: VARCHAR(100) NOT NULL
- email: VARCHAR(100) NOT NULL UNIQUE
- created_at: TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP

### 2. roles - Role użytkowników

- id: SERIAL PRIMARY KEY
- name: VARCHAR(20) NOT NULL UNIQUE

### 3. user_roles - Tabela łącząca użytkowników z rolami

- user_id: INT NOT NULL REFERENCES users(id) ON DELETE CASCADE
- role_id: INT NOT NULL REFERENCES roles(id) ON DELETE CASCADE
- PRIMARY KEY (user_id, role_id)

### 4. stadium - Stadiony

- id: SERIAL PRIMARY KEY
- name: VARCHAR(100) NOT NULL
- city: VARCHAR(100) NOT NULL
- capacity: INT NOT NULL

### 5. match - Mecze

- id: SERIAL PRIMARY KEY
- home_team: VARCHAR(100) NOT NULL
- away_team: VARCHAR(100) NOT NULL
- match_date: TIMESTAMP NOT NULL
- stadium_id: INT NOT NULL REFERENCES stadium(id) ON DELETE RESTRICT

### 6. ticket - Bilety

- id: SERIAL PRIMARY KEY
- match_id: INT NOT NULL REFERENCES match(id) ON DELETE CASCADE
- seat_row: VARCHAR(5)
- seat_number: VARCHAR(5)
- price: NUMERIC(8,2) NOT NULL
- status: VARCHAR(10) NOT NULL DEFAULT 'FREE'

## Pakiety główne

### config

Zawiera konfigurację bezpieczeństwa aplikacji:

- **SecurityConfig**: Konfiguracja Spring Security z definicją uprawnień, uwierzytelniania oraz autoryzacji endpoints

### controller

Kontrolery REST API obsługujące żądania HTTP:

- **AuthController**: Zarządzanie uwierzytelnianiem użytkowników
- **UserController**: Operacje CRUD na użytkownikach
- **StadiumController**: Zarządzanie stadionami
- **MatchController**: Zarządzanie meczami
- **TicketController**: System zarządzania biletami

### entity

Encje JPA reprezentujące model danych:

- **User**: Użytkownicy systemu
- **Role**: Role użytkowników
- **Stadium**: Stadiony z ich charakterystykami
- **Match**: Mecze z informacjami o drużynach i terminie
- **Ticket**: Bilety na mecze

### security

Komponenty odpowiedzialne za bezpieczeństwo:

- **JwtAuthenticationFilter**: Filtr uwierzytelniania JWT
- **JwtUtils**: Narzędzia do generowania i walidacji tokenów JWT

### service

Warstwa logiki biznesowej:

- **UserService**: Zarządzanie użytkownikami i bezpieczeństwem
- **StadiumService**: Operacje na stadionach
- **MatchService**: Zarządzanie meczami
- **TicketService**: Zarządzanie biletami

## Kontrolery REST API

### 1. AuthController (/api/auth)

- POST /login - Logowanie użytkownika
- POST /register - Rejestracja nowego użytkownika
- POST /register/admin - Rejestracja nowego administratora
- POST /login/passwordless - Logowanie bez hasła

### 2. UserController (/api/users)

- GET / - Pobranie wszystkich użytkowników (tylko ADMIN)
- GET /{id} - Pobranie użytkownika po ID
- POST / - Utworzenie nowego użytkownika
- PUT /{id} - Aktualizacja użytkownika
- DELETE /{id} - Usunięcie użytkownika (tylko ADMIN)
- GET /me - Pobranie informacji o zalogowanym użytkowniku
- PUT /me - Aktualizacja informacji o zalogowanym użytkowniku

### 3. StadiumController (/api/stadiums)

- GET / - Lista wszystkich stadionów
- GET /{id} - Konkretny stadion
- POST / - Dodanie nowego stadionu
- PUT /{id} - Aktualizacja stadionu
- DELETE /{id} - Usunięcie stadionu

### 4. MatchController (/api/matches)

- GET / - Lista wszystkich meczów
- GET /{id} - Konkretny mecz
- POST / - Dodanie nowego meczu
- PUT /{id} - Aktualizacja meczu
- DELETE /{id} - Usunięcie meczu

### 5. TicketController (/api/tickets)

- GET / - Lista wszystkich biletów
- GET /{id} - Konkretny bilet
- GET /match/{matchId} - Bilety na konkretny mecz
- POST / - Zakup biletu
- DELETE /{id} - Anulowanie biletu

## Autoryzacja i bezpieczeństwo

System implementuje kompletną kontrolę dostępu opartą na rolach (RBAC):

### Mechanizm uwierzytelniania

- JWT (JSON Web Token) do uwierzytelniania użytkowników
- BCrypt do szyfrowania haseł użytkowników

### Role użytkowników

- **USER** - Standardowy użytkownik
- **ADMIN** - Administrator systemu

## Uruchamianie projektu

### Wymagania systemowe

- Java 21+
- Maven 3.9+
- Docker i Docker Compose
- PostgreSQL (automatycznie przez Docker)

### Kroki uruchomienia

#### Uruchomienie bazy danych i migracji

```bash
docker-compose up -d
```

To uruchomi:
- Bazę danych PostgreSQL na porcie 5432
- Migracje Flyway do utworzenia schematu bazy danych

#### Uruchomienie aplikacji

```bash
./mvnw spring-boot:run
```

Aplikacja będzie dostępna pod adresem http://localhost:8080

#### Dostęp do dokumentacji API

Dokumentacja API jest dostępna pod adresem:
http://localhost:8080/swagger-ui.html

### Predefiniowani użytkownicy

Po uruchomieniu aplikacji dostępni są następujący użytkownicy:

- admin / admin - Administrator
- user / user - Standardowy użytkownik
- admin_passwordless - Administrator (bez hasła, logowanie przez endpoint /api/auth/login/passwordless)
- user_passwordless - Standardowy użytkownik (bez hasła, logowanie przez endpoint /api/auth/login/passwordless)

### Dane testowe

Aplikacja automatycznie tworzy:

- 3 przykładowe stadiony
- 3 przykładowe mecze
- Przykładowe bilety na każdy mecz

## Testowanie

Projekt zawiera zestaw testów zapewniających poprawne działanie aplikacji.

### Uruchomienie testów

```bash
# Wszystkie testy
mvn clean test
```

## Przykładowe działanie systemu

### 1. Uwierzytelnianie użytkownika

```
POST /api/auth/login
```

Request body:
```json
{
  "username": "admin",
  "password": "admin"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "admin",
  "email": "admin@example.com",
  "roles": ["ROLE_USER", "ROLE_ADMIN"]
}
```

### 2. Przeglądanie dostępnych stadionów

```
GET /api/stadiums
```

Response:
```json
[
  {
    "id": 1,
    "name": "National Stadium",
    "city": "Warsaw",
    "capacity": 58000
  },
  {
    "id": 2,
    "name": "Wembley Stadium",
    "city": "London",
    "capacity": 90000
  },
  {
    "id": 3,
    "name": "Camp Nou",
    "city": "Barcelona",
    "capacity": 99000
  }
]
```

### 3. Przeglądanie meczów

```
GET /api/matches
```

Response:
```json
[
  {
    "id": 1,
    "homeTeam": "Poland",
    "awayTeam": "Germany",
    "matchDate": "2023-06-15T18:00:00",
    "stadium": {
      "id": 1,
      "name": "National Stadium",
      "city": "Warsaw",
      "capacity": 58000
    }
  },
  {
    "id": 2,
    "homeTeam": "England",
    "awayTeam": "France",
    "matchDate": "2023-06-20T20:00:00",
    "stadium": {
      "id": 2,
      "name": "Wembley Stadium",
      "city": "London",
      "capacity": 90000
    }
  }
]
```

### 4. Zakup biletu

```
POST /api/tickets
```

Request body:
```json
{
  "matchId": 1,
  "seatRow": "A",
  "seatNumber": "1",
  "price": 100.00,
  "status": "RESERVED"
}
```

Response:
```json
{
  "id": 1,
  "match": {
    "id": 1,
    "homeTeam": "Poland",
    "awayTeam": "Germany",
    "matchDate": "2023-06-15T18:00:00"
  },
  "seatRow": "A",
  "seatNumber": "1",
  "price": 100.00,
  "status": "RESERVED"
}
```
