# Struts 2 Mail Reader Application

A sample web application demonstrating the Struts 2 framework features including user authentication, form validation, and internationalization.

## Description

The Mail Reader application is a classic example application for Apache Struts 2 that demonstrates:

- User registration and authentication
- Form validation and error handling
- Internationalization support
- Database integration using XML data files
- Action-based MVC architecture
- JSP views with Struts tags

## Prerequisites

- Java 17 or higher
- Apache Maven 3.6 or higher
- (Optional) [Just](https://github.com/casey/just) - A command runner for convenient task execution

## Running the Application

### Using Maven directly:
To run the application locally, use the following command:

```bash
mvn clean jetty:run
```

### Using Just (recommended):
If you have Just installed, you can use the convenient recipes:

```bash
# Run the application
just run

# See all available commands
just

# Development workflow (clean, compile, run)
just dev
```

Once started, you can access the application at:
```
http://localhost:8080/mailreader2
```

The application will automatically reload when you make changes to the source code (scan interval: 10 seconds).

## Stopping the Application

To stop the application, press `Ctrl+C` in the terminal where the server is running.

## Running with Docker

You can also run the application in a Docker container:

### Using Maven/Docker directly:

#### Build the Docker image:
```bash
docker build -t mailreader2 .
```

#### Run the container:
```bash
docker run -p 9347:9347 mailreader2
```

### Using Just (recommended):

```bash
# Build and run Docker container
just docker

# Or step by step:
just docker-build
just docker-run

# Run in detached mode
just docker-run-detached

# Stop and clean up
just docker-stop
just docker-clean
```

The application will be available at:
```
http://localhost:9347/mailreader2
```

### Stop the container:
```bash
docker stop <container-id>
```

## Features

- **User Registration**: Create new user accounts
- **User Login/Logout**: Secure authentication system  
- **Subscription Management**: Manage mail subscriptions
- **Password Management**: Change user passwords
- **Internationalization**: Support for multiple languages (English, Japanese, Russian)
- **Form Validation**: Client and server-side validation

## Technology Stack

- **Framework**: Apache Struts 2.7.0.3
- **Build Tool**: Apache Maven
- **Server**: Eclipse Jetty (development)
- **View Technology**: JSP with Struts tags
- **Data Storage**: XML files
- **Logging**: Apache Log4j2
