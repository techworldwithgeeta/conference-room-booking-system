# conference-room-booking-system

## Overview
This project is a REST API for managing conference room bookings within a company. It supports booking for multiple conference rooms with different capacities, ensures no overlap with maintenance schedules, and adheres to specific booking rules.

## Features
- **Room Booking**: Book a conference room for a specific date and time.
- **Available Rooms**: Check available rooms for a specific time range.
- **Maintenance Intervals**: Automatically blocks rooms during maintenance intervals.

Conference Room Details:

Amaze: Capacity 3 Persons
Beauty: Capacity 7 Persons
Inspire: Capacity 12 Persons
Strive: Capacity 20 Persons
Maintenance Timings:

09:00 - 09:15
13:00 - 13:15
17:00 - 17:15

## Technology Stack
- **Java 17**
- **Spring Boot**
- **Maven**
- **In-Memory Database (H2)**

Rules:

1.Booking can only be done for the current date.
2.Booking must be done in intervals of 15 minutes.
3.Booking is on a first-come, first-served basis.
4.The room selected will be the most optimal one based on the number of attendees.
5.Booking cannot be done during maintenance times.
6.The number of attendees must be greater than 1 and less than or equal to the room's maximum capacity.

Technology Stack
Java Version: 17
Framework: Spring Boot 3
Build Tool: Maven
Database: In-Memory H2 Database

Project Structure
src/
│
├── main/
│   ├── java/com/techworld/roombooking/
│   │   ├── controller/    # REST Controllers
│   │   ├── model/         # Domain Models
│   │   ├── entity/        # Persistance
│   │   ├── dataloader/    # Intitally load the data
│   │   ├── repository/    # Repository Interfaces
│   │   ├── service/       # Business Logic Services
│   │   ├── exception/     # Excpeion Clasees
│   │   └── RoomBookingApplication.java # Main Application Class
│   └── resources/
│       ├── application.properties # Application Configuration
│
│
└── test/
    └── java/com/techworld/roombooking/ # Unit and Integration Tests

How to Run
Prerequisites
Ensure you have JDK 11 or above installed.
Ensure you have Maven installed.

Steps
1. Clone the Repository:
git clone https://github.com/techworldwithgeeta/conference-room-booking-system.git
cd conference-room-booking

2.Build the Project:
mvn clean install

3.Run the Application:
mvn spring-boot:run

4.Access the API: The API will be running on http://localhost:8080/api/conference-rooms.

Endpoints
1.Book a Room:

MethodType : POST /api/conference-rooms/book
Request Body : {
  "bookingDate": "2024-08-28",
  "startTime": "10:00",
  "endTime": "11:00",
  "attendees": 5
}

2. Check Available Rooms:

Method Type :GET /api/conference-rooms/available
Request Parameters:
bookingDate (e.g., 2024-08-28)
startTime (e.g., 10:00)
endTime (e.g., 11:00)




