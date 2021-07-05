# Tourplanner - Protocol

__Author:__ Konstantin Lampalzer

# Design Choices
## General
The software is split into a __server-client architecture__, where the backend is responsible for business logic and storage, contrary to the frontend which only does the presentation to the user. The communication between server and client is done using a __REST-API__.

## Frontend
The fronted is implemented in Java using JavaFX and FXML. Furthermore, the MVVM pattern is used to follow best-practices in building a JavaFX application. No data is stored on the frontend.

## Backend
The backend is responsible for all business logic and data storage. Furthermore, it connects to __min.io__ to store the images. __PostgreSQL__ is used as the database to store all logs and tours. The **Spring Framework** is used in the backend allowing Inversion of Control and Dependency Injection.


# Failures / Lessons learned
* Communication between two views can be implemented using events. This makes it possible to decouple the two views.
* Don't implement a REST API yourself. Frameworks are easier and cleaner.

# Tests
## repository
Both repository-classes responsible for persistence are tested using integration tests. During each test a embedded PostgreSQL database is started, simulating a real environment. These tests are really important, as  then SQL statements are written by hand, which can quickly result in human errors.

## search-service
As this service has a large amount of logic behind it, it's critical to make sure everything behaves as expected. The dependecies on the persistence-layer have been broken by using __Mockito__ to mock the LogRepository and TourRepository.

## tour / log-service
These two services handle all logic of inserting, updating and deleting tours and logs. Therefore, testing is a requirement.

# Unique Feature
The TourPlanner support Lucene query syntax for searching. This syntax is described in detail at https://www.elastic.co/guide/en/kibana/current/lucene-query.html

# Data Model

## Tour
* id
* name
* description
* distance
* image

## Log
* id
* tourId
* startTime
* endTime
* startLocation
* endLocation
* rating
* meansOfTransport
* distance
* notes
* moneySpent

# Time spent

* __28.04.:__ Initial Commit
* __29.04.:__ Setup and start on backend
* __28.04.:__ Database setup, client for mapQuest
* __06.05.:__ Tour detail panel
* __20.05.:__ Client code refactoring & cleanup
* __16.04.:__ Log details panel, Import and export
* __01.06.:__ Switch backend to Spring Framework
* __07.06.:__ Finish import & export, Add reports
* __10.06.:__ Tour searching, Config properties
* __21.06.:__ Unit tests, cleanup, protocol

On average one day was about 3-4 hours of programming. This results in ~50 Hours spent in total on the project.