# Hexagonal Architecture with Maven Multi-Module Project

## Project Structure

```
hexagonal-architecture-ecommerce/
├── pom.xml (parent)
├── domain/
│   └── pom.xml
├── application/
│   └── pom.xml
├── infrastructure/
│   ├── pom.xml
│   ├── database/
│   │   └── pom.xml
│   ├── web/
│   │   └── pom.xml
│   └── messaging/
│       └── pom.xml
└── bootstrap/
    └── pom.xml
```

## Module Responsibilities

### 1. Domain Module (Core/Hexagon Center)
- **Purpose**: Contains business logic, entities, and domain services
- **Dependencies**: None (completely isolated)
- **Contents**:
    - Domain entities
    - Value objects
    - Domain services
    - Business rules and validations
    - Domain events

### 2. Application Module (Use Cases/Ports)
- **Purpose**: Orchestrates domain operations and defines ports
- **Dependencies**: Domain module only
- **Contents**:
    - Use case implementations
    - Input/Output ports (interfaces)
    - Application services
    - Command/Query handlers
    - DTOs for application boundaries

### 3. Infrastructure Module (Adapters)
- **Purpose**: Implements ports and handles external concerns
- **Dependencies**: Application and Domain modules
- **Submodules**:
    - **database**: JPA repositories, database configurations
    - **web**: REST controllers, web configurations
    - **messaging**: Message handlers, queue configurations

### 4. Bootstrap Module (Main Application)
- **Purpose**: Wires everything together and starts the application
- **Dependencies**: All other modules
- **Contents**:
    - Main class
    - Spring Boot configuration
    - Dependency injection configuration

## Parent POM Configuration

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>my-hexagonal-app</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <modules>
        <module>domain</module>
        <module>application</module>
        <module>infrastructure</module>
        <module>bootstrap</module>
    </modules>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <spring.boot.version>3.2.0</spring.boot.version>
        <junit.version>5.10.0</junit.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring.boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```

## Domain Module POM

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>my-hexagonal-app</artifactId>
        <groupId>com.example</groupId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>domain</artifactId>

    <dependencies>
        <!-- Only basic Java dependencies, no frameworks -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

## Application Module POM

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>my-hexagonal-app</artifactId>
        <groupId>com.example</groupId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>application</artifactId>

    <dependencies>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>domain</artifactId>
            <version>${project.version}</version>
        </dependency>
        
        <!-- Optional: validation annotations -->
        <dependency>
            <groupId>jakarta.validation</groupId>
            <artifactId>jakarta.validation-api</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

## Infrastructure Module POM

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>my-hexagonal-app</artifactId>
        <groupId>com.example</groupId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>infrastructure</artifactId>
    <packaging>pom</packaging>

    <modules>
        <module>database</module>
        <module>web</module>
        <module>messaging</module>
    </modules>

    <dependencies>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>application</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>domain</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
</project>
```

## Infrastructure Database Module POM

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>infrastructure</artifactId>
        <groupId>com.example</groupId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>infrastructure-database</artifactId>

    <dependencies>
        <!-- Spring Data JPA -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        
        <!-- Database drivers -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
        
        <!-- Flyway for database migrations -->
        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-core</artifactId>
        </dependency>
        
        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>postgresql</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

## Infrastructure Web Module POM

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>infrastructure</artifactId>
        <groupId>com.example</groupId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>infrastructure-web</artifactId>

    <dependencies>
        <!-- Spring Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <!-- Validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        
        <!-- OpenAPI/Swagger -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.2.0</version>
        </dependency>
        
        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

## Infrastructure Messaging Module POM

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>infrastructure</artifactId>
        <groupId>com.example</groupId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>infrastructure-messaging</artifactId>

    <dependencies>
        <!-- Spring AMQP for RabbitMQ -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>
        
        <!-- Or Spring Kafka -->
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
        </dependency>
        
        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.amqp</groupId>
            <artifactId>spring-rabbit-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

## Bootstrap Module POM

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>my-hexagonal-app</artifactId>
        <groupId>com.example</groupId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>bootstrap</artifactId>

    <dependencies>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>domain</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>application</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>infrastructure-web</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>infrastructure-database</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>infrastructure-messaging</artifactId>
            <version>${project.version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

## Code Example Structure

### Domain Layer
```java
// domain/src/main/java/com/example/domain/User.java
public class User {
    private final UserId id;
    private final Email email;
    private final Name name;
    
    // Business logic methods
    public void updateProfile(Name newName, Email newEmail) {
        // Domain validation and business rules
    }
}
```

### Application Layer
```java
// application/src/main/java/com/example/application/ports/UserRepository.java
public interface UserRepository {
    User findById(UserId id);
    void save(User user);
}

// application/src/main/java/com/example/application/usecases/UpdateUserProfileUseCase.java
public class UpdateUserProfileUseCase {
    private final UserRepository userRepository;
    
    public void execute(UpdateUserProfileCommand command) {
        User user = userRepository.findById(command.getUserId());
        user.updateProfile(command.getName(), command.getEmail());
        userRepository.save(user);
    }
}
```

### Infrastructure Layer
```java
// infrastructure/database/src/main/java/com/example/infrastructure/database/JpaUserRepository.java
@Repository
public class JpaUserRepository implements UserRepository {
    // JPA implementation
}

// infrastructure/web/src/main/java/com/example/infrastructure/web/UserController.java
@RestController
public class UserController {
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    
    @PutMapping("/users/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable String id, @RequestBody UpdateUserRequest request) {
        // Controller logic
    }
}
```

## Benefits of This Structure

1. **Dependency Inversion**: Domain doesn't depend on infrastructure
2. **Testability**: Each layer can be tested in isolation
3. **Flexibility**: Easy to swap implementations (e.g., database, messaging)
4. **Maintainability**: Clear separation of concerns
5. **Build Optimization**: Only affected modules are rebuilt
6. **Team Collaboration**: Teams can work on different modules independently

## Maven Commands

```bash
# Build entire project
mvn clean install

# Build specific module
mvn clean install -pl domain

# Build with dependencies
mvn clean install -pl bootstrap -am

# Run application
mvn spring-boot:run -pl bootstrap
```

## Architecture Enforcement

Use ArchUnit for testing architectural boundaries:

```java
@Test
void domainShouldNotDependOnOtherLayers() {
    noClasses()
        .that().resideInAPackage("..domain..")
        .should().dependOnClassesThat().resideInAnyPackage("..infrastructure..", "..application..")
        .check(importedClasses);
}
```

This structure ensures your hexagonal architecture remains clean and maintainable while leveraging Maven's multi-module capabilities for better organization and build management.