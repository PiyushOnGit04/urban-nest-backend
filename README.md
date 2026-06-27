# 🏠 UrbanNest Backend

Backend service for **UrbanNest**, a full-stack rental platform that helps tenants discover rental properties and enables landlords to manage their listings efficiently.

Built using **Spring Boot**, **MySQL**, and **JWT Authentication**, this backend provides secure REST APIs for authentication, property management, image handling, wishlists, and inquiry management.

---

## 🚀 Features

* 🔐 JWT Authentication & Authorization
* 👥 Role-based Access (Tenant & Landlord)
* 🏡 Property Listing Management (CRUD)
* 📷 Image Upload Support (Cloudinary Integration)
* ❤️ Wishlist Management
* 📩 Inquiry Management
* 🔍 Search Properties
* 🎯 Filter by Room Type & Rent
* ↕️ Sort by Price & Date
* 🗄️ MySQL Database Integration
* 🌐 RESTful API Design

---

## 🛠️ Tech Stack

* Java 21
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA (Hibernate)
* MySQL
* Cloudinary
* Maven

---

## 📁 Project Structure

```text
src
├── controller
├── service
├── repository
├── model
├── dto
├── security
├── config
└── exception
```

---

## 📌 Main REST APIs

### Authentication

* POST `/api/auth/register`
* POST `/api/auth/login`

### Rooms

* POST `/api/rooms`
* GET `/api/rooms`
* GET `/api/rooms/{id}`
* PUT `/api/rooms/{id}`
* DELETE `/api/rooms/{id}`

Supports:

* Search
* Filter
* Sorting

Example:

```http
GET /api/rooms?search=indore&roomType=PG&minRent=5000&maxRent=10000&sortBy=rent&order=asc
```

### Images

* Upload room images
* Delete room images

### Wishlist

* Add to Wishlist
* Remove from Wishlist
* Get Wishlist

### Inquiries

* Send Inquiry
* View Inquiries
* Update Inquiry Status

---

## 🗃️ Database

MySQL is used as the primary database.

Main Entities:

* User
* Room
* RoomImage
* InquiryRequest
* Wishlist
* Review
* Amenity

---

## ⚙️ Getting Started

### Clone the repository

```bash
git clone https://github.com/PiyushOnGit04/urban-nest-backend.git
```

### Configure Database

Update `application.properties` with your MySQL credentials.

```properties
spring.datasource.url=YOUR_DATABASE_URL
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

Also configure:

* JWT Secret
* Cloudinary Credentials

---

### Run the application

```bash
mvn spring-boot:run
```

The backend will start on:

```text
http://localhost:8080
```

---

## 📱 Frontend

Flutter application:

**(Add your frontend repository link here once it's public.)**

---

## 👨‍💻 Author

**Piyush Sahni**

GitHub:
https://github.com/PiyushOnGit04

---

## ⭐ Future Improvements

* Pagination
* Advanced Search
* Property Reviews & Ratings
* Email Notifications
* Admin Dashboard
* Docker Support
* Unit & Integration Testing

---

If you found this project interesting, consider giving the repository a ⭐.
