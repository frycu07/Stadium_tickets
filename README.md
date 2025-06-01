# Aplikacja Zarządzania Biletami Stadionowymi

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

## Tabele bazodanowe

Aplikacja wykorzystuje PostgreSQL z następującymi tabelami (Diagram ERD):

<img width="369" alt="image" src="https://github.com/user-attachments/assets/d18b5cb5-3955-4b17-b63c-46dafd0aaa5d" />

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

## Polimorfizm

W projekcie zaimplementowano poplimorfizm:
<img width="717" alt="image" src="https://github.com/user-attachments/assets/6a3c0f3d-a40c-4256-9c29-e71c871a2855" />

## Wzorce projektowe

### Service Pattern 
Przykład warstwy serwisowej, która kapsułkuje logikę biznesową dotyczącą biletów
![Uploading image.png…]()

### MVC Pattern - architektura aplikacji
Aplikacja oparta na Spring Boot domyślnie stosuje wzorzec Model-View-Controller. Przykładowe warstwy:
	- Model: klasy Ticket, Match, Stadium,
	- View: Swagger UI
	-	Controller: klasy kontrolerów REST (np. TicketController),
	-	Service: TicketServiceImpl.

### Repository Pattern 
Wszystkie repozytoria implementują interfejsy (np. TicketRepository), które rozszerzają JpaRepository zapewniając operacje CRUD:

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

Projekt zawiera zestaw testów zapewniających poprawne działanie aplikacji. Pokrycie spełnia wymagania projektowe
<img width="1155" alt="image" src="https://github.com/user-attachments/assets/d92bf494-6461-4e66-b346-1c1d19e88462" />


### Uruchomienie testów

# Wszystkie testy
mvn clean test

# Raport pokrycia JaCoCo
mvn clean test jacoco:report

### Przykładowy test
<img width="648" alt="image" src="https://github.com/user-attachments/assets/62585a1c-2f41-46a0-a69d-dfb23a982f21" />



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

