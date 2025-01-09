Functionality: 

Manages user registration, login, and profile updates. 

Implements role-based access control for different user roles (e.g., admin, user). 

Key Components: 

BookingController: Exposes booking management APIs. 

BookingEventPublisher: Sends booking-related events to RabbitMQ. 

 

Database: 

Maintains booking details, linked with user and payment services
