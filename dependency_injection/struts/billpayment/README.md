# Struts 2 Bill Payment Example

This is a Struts 2 version of the Jakarta EE CDI Bill Payment example, demonstrating dependency injection using Spring Framework instead of CDI.

## Overview

This application demonstrates:
- **Struts 2 Actions** instead of JSF managed beans
- **Spring DI** instead of CDI for dependency injection
- **Struts 2 Interceptors** instead of CDI interceptors
- **Service layer pattern** instead of event-driven architecture
- **JSP pages** instead of JSF/Facelets pages

## Architecture Conversion

### From Jakarta EE CDI to Struts 2 + Spring

| Jakarta EE CDI                      | Struts 2 + Spring                            |
| ----------------------------------- | -------------------------------------------- |
| `@Named @SessionScoped` PaymentBean | `@Component @Scope("session")` PaymentAction |
| `@Inject` Event&lt;PaymentEvent&gt; | `@Autowired` PaymentService                  |
| `@Observes` Event handlers          | Direct service method calls                  |
| `@Logged` CDI interceptor           | Struts 2 LoggingInterceptor                  |
| JSF pages (.xhtml)                  | JSP pages (.jsp)                             |
| Jakarta EE server                   | Tomcat + Spring                              |

## Key Components

### Actions
- **PaymentAction**: Main action handling payment processing (replaces PaymentBean)

### Services  
- **PaymentService**: Interface for payment operations
- **PaymentServiceImpl**: Implementation with logging (replaces event handlers)

### Models
- **PaymentEvent**: Payment data model (unchanged from original)

### Interceptors
- **LoggingInterceptor**: Struts 2 interceptor for method logging

## Building and Running

### Prerequisites
- Java 11+
- Maven 3.6+
- Docker (optional)

### Local Development
```bash
# Build the project
./mvnw clean package

# Run with Jetty
./mvnw jetty:run

# Access the application
http://localhost:8080/billpayment
```

### Using Just (if installed)
```bash
# Build and run
just build-and-run

# Or just run
just run
```

### Docker Deployment
```bash
# Build and run with Docker Compose
docker-compose up --build

# Access the application
http://localhost:8080/billpayment
```

## Configuration

### Spring Configuration
- **applicationContext.xml**: Spring bean configuration
- Component scanning enabled for DI

### Struts Configuration  
- **struts.xml**: Action mappings and interceptor configuration
- **web.xml**: Filter and listener configuration

## Features

1. **Payment Form**: Enter amount and select payment method (Debit/Credit)
2. **Validation**: Client and server-side validation
3. **Payment Processing**: Simulated payment processing with logging
4. **Result Display**: Shows payment confirmation
5. **Logging**: Method invocation logging via interceptors

## Differences from Jakarta Version

1. **No CDI Events**: Direct service method calls instead of event firing
2. **Spring DI**: Uses Spring's `@Autowired` instead of CDI `@Inject`
3. **JSP Views**: Standard JSP with Struts tags instead of JSF/Facelets
4. **Action-based**: Struts 2 action model instead of JSF component model
5. **Interceptor Stack**: Struts 2 interceptors instead of CDI interceptors

## Logging

The application logs payment operations to the console. In a production environment, configure proper logging frameworks like Log4j2 or Logback.

## Next Steps

To extend this example:
1. Add database persistence with Spring Data JPA
2. Implement real payment gateway integration
3. Add Spring Security for authentication
4. Use Spring Boot for auto-configuration
5. Add REST API endpoints
