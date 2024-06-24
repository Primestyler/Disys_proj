# Distributed Systems Semester Project

## Introduction
The Fuel Station Data Collector is a distributed system that generates an invoice PDF for customers who use electric car charging stations. The system consists of a JavaFX UI, a REST-based API built with Java Spring Boot, and a RabbitMQ message queue for managing tasks.

It is a docker-compose project that sets up five databases and a queue. One database stores user information, one database stores the access information of the other three databases, which itself store the charging information (every charging station has its own database).

### System Architecture
Below is a graphical representation of the system
![image](https://cdn.discordapp.com/attachments/536906295064789006/1253571068086587494/Screenshot_37.png?ex=66765688&is=66750508&hm=de7321777aa58756bdddf40863c567e9adb602d588a5f8ea5c6919a516607b5d&)

### UML

![image](https://cdn.discordapp.com/attachments/536906295064789006/1254648451015376936/image.png?ex=667a41ec&is=6678f06c&hm=f0334bde32c579eca4555db5327a551d97e444763436e363dd8c75c733f8b57d&)

### Entity Relationship Diagram
The following Entity Relationship Diagram (ERD) illustrates the tables of the database used in the Fuel Station Data Collector system and the relationships between these tables.

![image](https://cdn.discordapp.com/attachments/536906295064789006/1253577906937004052/Screenshot_36.png?ex=66765ce7&is=66750b67&hm=ef799bd0ba0430b6a1d05f041e0ada5f6119feb14ff0edd0b0aa639d116dbd25&)

The customer table contains all relevant customer data and is connected to the charge table via the customer_id field. The charge table records information about each charging session, including the amount of energy used (kWh) and a unique identifier for tracking the record. The station table holds information about each charging station and is connected to the charge table via the station_id field. The structure of the station table is consistent across all station databases.
## Components Description

### JavaFX UI

The Station JavaFX Application is the user interface where customers can enter their customer ID. After entering the ID, a list of all corresponding invoices is displayed. The customer can select an invoice to be generated as a PDF.

![image](https://cdn.discordapp.com/attachments/536906295064789006/1253593556313182320/Screenshot_38.png?ex=66766b7a&is=667519fa&hm=3d013f4397f90c8d37e426bbae32f07dcfc5700dd5734bb357a4e36f936fe140&)


### Spring Boot Application

The Spring Boot Application initiates the process by sending a start message to the Data Collection Dispatcher. It includes the Data Collection Controller, which handles the REST API, starts the data gathering job, and returns the invoice PDF. The Data Collection Service within this component contains the business logic for data collection.

### RabbitMQ
RabbitMQ acts as a message broker between components in the system. Its primary role is to ensure reliable messaging between components that send messages and components that receive messages.

### Data Collection Dispatcher

The Data Collection Dispatcher coordinates the data gathering process. It interacts with various stations, assigns tasks to the Station Data Collector for each charging station, and informs the Data Collection Receiver when a new job starts. It includes the Database Connector for PostgreSQL and defines the Station Class for station details in the central database.

### Station Data Collector

The Station Data Collector retrieves specific customer data from charging stations and sends it to the Data Collection Receiver. It uses the Database Connector for PostgreSQL and includes the Charge class for customer charging information.

### Data Collection Receiver
The Data Collection Receiver gathers data from all sources, organizes it by job, and forwards complete datasets to the PDF Generator. It hosts the Data Collection Receiver class and includes model classes like Invoice and Station.

### PDF Generator

The PDF Generator generates the invoice from data and saves PDF to the file system.he component also contains the class Database Connector which is responsible for the connection to the database POSTGRES as well as the model classes Customer, Invoice and Station.

## Services
- Customer Database
	- Contains customer data (id, first name, last name)
	- URL: localhost:30001
- Stations Database
	- Contains station data (id, db_url, latitude, longitude)
	- URL: localhost:30002
- Individual Station Databases
	- Contains customer station data (id, kwh, customer_id)
	- URL Station 1: localhost:30011
	- URL Station 2: localhost:30012
	- URL Station 3: localhost:30013
- Queue
	- URL: localhost:30003
	- Web: localhost:30083
### RabbitMQ-Dashboard
- [RabbitMQ-Dashboard](http://localhost:30083)
- Username: guest
- Password: guest


## User Guide


### Requirements
Make sure you have the following installed on your system:

- [Docker](https://docs.docker.com/get-docker/)
- IntelliJ IDEA (Community or Ultimate edition)

### Installation Steps
1. Clone or download the project from the GitHub repository.
2. Open the project in IntelliJ IDEA.

### Starting Docker
```shell
docker-compose up
```
- or locate docker-compose.yml and run the services

### Running the Application
1. Open the project in IntelliJ IDEA.
2. In the IntelliJ toolbar, choose the desired run configuration from the dropdown menu.
3. Click the Run button (or press Shift+F10) to start the application

### Generating PDFs
1. Once the applications are running, open the graphical interface (GUI) for the desired application.
2. Enter the id-number of the customer for whom you wish to generate an invoice.
3. Monitor the progress displayed within the application as the PDF is generated.
4. After the PDF is ready, click the "View" button to open the PDF file.
	- The PDF file will open using your system's default PDF viewer.


## Lessons Learned


- Managing a distributed system requires careful planning and handling of inter-service communication.



- RabbitMQ is effective for decoupling services and managing asynchronous tasks, but it requires robust error handling and retry mechanisms.



- Creating UML diagrams helped in understanding and designing the system architecture effectively

## Tracked Time
| Task         | Time Spent (hours) |
|--------------|--------------------|
| Initial Setup and Research | 5                  |
| System Design and UML | 5                  |
| API Development             | 15                 |
| Message Queue Integration  | 10                 |
| UI Development (JavaFX)  | 10                 |
| Testing and Debugging  | 10                 |
| Documentation  | 5                  |
| Total  | 60                 |

## Unit Testing Decisions


| Component                              | Reason                                                                                 |
|----------------------------------------|-------------------------------------------------------------------------------------------------|
| **DataCollectionDispatcher**           |                                                                                                 |
| DataCollectionDispatcherListenerTest   | Verify message handling and interaction with the dispatcher service.                            |
| DataCollectionDispatcherServiceTest    | Verify data gathering job starts correctly, interacts with RabbitMQ, and processes stations.    |
| **DataCollectionReceiver**             |                                                                                                 |
| DataCollectionListenerTest             | Verify data message processing, interaction with messaging service, and correct JSON formatting. |
| **PDFGenerator**                       |                                                                                                 |
| PDFGeneratorListenerTest               | Verify message handling and interaction with the customer repository.                           |
| **SpringBootApp**                      |                                                                                                 |
| InvoiceControllerTest                  | Verify invoice generation start, check invoice status, and ensure correct HTTP responses.       |
| **StationDataCollector**               |                                                                                                 |
| StationDataListenerTest                | Verify message processing, interaction with dynamic entity manager, and sending data correctly. |


## GIT Repository
[Github](https://github.com/Primestyler/Disys_proj)