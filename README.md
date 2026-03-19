# 🩸 BloodConnect

A full-stack blood donation management platform that connects hospitals with nearby eligible donors in real time. Hospitals raise blood requests, and the system automatically finds and notifies compatible donors within a specified radius via email.

**Live Demo:** [bloodconnect-app-eight.vercel.app](https://bloodconnect-app-eight.vercel.app)

---

## 🚀 Features

**For Hospitals**
- Register and log in as a hospital
- Raise blood requests with blood type, urgency level, units needed, and search radius
- Automatically notify eligible donors within the specified radius via email
- Track request status — Pending, Fulfilled, Cancelled
- View all donor responses per request

**For Donors**
- Register and log in as a donor
- Get notified when a nearby hospital needs compatible blood
- Accept or decline donation requests
- View notification history

**Core System**
- JWT-based authentication for both roles
- Blood type compatibility matching (all 8 blood types)
- Haversine formula for accurate real-world distance calculation
- 56-day donation cooldown rule enforced automatically
- Email notifications via Gmail SMTP

---

## 🛠️ Tech Stack

### Backend
| Technology | Version |
|---|---|
| Java | 21 |
| Spring Boot | 4.0.3 |
| Spring Security | 7.0.3 |
| Spring Data JPA | 4.0.3 |
| Hibernate ORM | 7.2.4 |
| PostgreSQL Driver | 42.7.10 |
| JJWT | 0.11.5 |
| Spring Mail | 4.0.3 |
| Lombok | 1.18.42 |
| Maven | 3.x |

### Frontend
| Technology | Version |
|---|---|
| React | 19 |
| Vite | 6 |
| Tailwind CSS | 4 |
| React Router | 7 |
| Axios | 1.x |
| Lucide React | 0.577 |

### Infrastructure
| Service | Purpose |
|---|---|
| Railway | Backend hosting |
| Vercel | Frontend hosting |
| Neon | PostgreSQL cloud database |

---

## 🏗️ Project Structure

```
bloodconnect/
├── blodconnect-backend/
│   └── src/main/java/com/bloodconnect/blodconnect/
│       ├── config/          # Spring Security & CORS configuration
│       ├── controller/      # REST API endpoints
│       ├── dto/             # Request & response objects
│       ├── entity/          # JPA database models
│       ├── enums/           # BloodType, UrgencyLevel, Role
│       ├── repository/      # JPA interfaces
│       ├── security/        # JWT filter & utilities
│       └── service/         # Business logic & matching algorithm
│
└── bloodconnect-frontend/
    └── src/
        ├── api/             # Axios instance & interceptors
        ├── components/      # Sidebar, Navbar
        ├── context/         # Auth context (JWT state)
        └── pages/           # Login, Register, Dashboards
```

---

## ⚙️ How It Works

```
Hospital raises blood request (blood type + urgency + radius)
        ↓
BloodCompatibilityUtil → finds all compatible donor blood types
        ↓
DonorRepository.findEligibleDonors() → filters by blood type,
        availability, and 56-day cooldown rule
        ↓
HaversineUtil.calculateDistance() → calculates real-world distance
        for each eligible donor from the hospital
        ↓
Donors within radiusKm → receive email notification
        ↓
Donor accepts/declines → hospital sees responses in dashboard
```

---

## 🔐 API Endpoints

### Auth
```
POST /api/v1/auth/register/donor       → Register a donor
POST /api/v1/auth/register/hospital    → Register a hospital
POST /api/v1/auth/login                → Login (returns JWT token)
```

### Hospital (requires HOSPITAL role)
```
GET  /api/v1/hospital/{id}/requests              → Get all requests
POST /api/v1/hospital/{id}/request               → Raise new request
PUT  /api/v1/hospital/{id}/request/{rid}/cancel  → Cancel a request
GET  /api/v1/hospital/{id}/donors                → View donor responses
```

### Donor (requires DONOR role)
```
GET /api/v1/donor/{id}/notifications             → Get all notifications
PUT /api/v1/donor/{id}/response/{rid}            → Accept or decline
```

---

## 🧑‍💻 Local Development

### Prerequisites
- Java 21
- Node.js 18+
- PostgreSQL (local) or Neon account
- Maven

### Backend Setup

1. Clone the repo
```bash
git clone https://github.com/maurya-prashant/bloodconnect.git
cd blodconnect-backend
```

2. Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bloodconnect
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update

app.jwt.secret=your_jwt_secret_key
app.jwt.expiration=86400000

spring.mail.username=your_gmail@gmail.com
spring.mail.password=your_gmail_app_password
```

3. Run:
```bash
./mvnw spring-boot:run
```
Backend starts on `http://localhost:8080`

### Frontend Setup

1. Navigate to frontend:
```bash
cd bloodconnect-frontend
npm install
```

2. Update `src/api/axios.js`:
```js
baseURL: 'http://localhost:8080/api/v1'
```

3. Run:
```bash
npm run dev
```
Frontend starts on `http://localhost:5173`

---

## 🌍 Deployment

| Layer | Platform | URL |
|---|---|---|
| Frontend | Vercel | [bloodconnect-app-eight.vercel.app](https://bloodconnect-app-eight.vercel.app) |
| Backend | Railway | [bloodconnect-production-7cb6.up.railway.app](https://bloodconnect-production-7cb6.up.railway.app) |
| Database | Neon (PostgreSQL 17) | Singapore region |

---

## 👨‍💻 Author

**Prashant Maurya**
- GitHub: [@maurya-prashant](https://github.com/maurya-prashant)

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
