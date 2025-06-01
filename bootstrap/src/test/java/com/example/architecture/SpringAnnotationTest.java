package com.example.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

@DisplayName("Spring Annotation Tests")
class SpringAnnotationTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setUp() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.example");
    }

    @Test
    @DisplayName("Repository implementations should be annotated with @Component")
    void repositoryImplementationsShouldBeComponents() {
        ArchRule rule = classes()
                .that().resideInAPackage("..infrastructure..repositories..")
                .and().haveNameMatching(".*RepositoryImpl")
                .should().beAnnotatedWith("org.springframework.stereotype.Component");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Use cases should be annotated with @Service")
    void useCasesShouldBeServices() {
        ArchRule rule = classes()
                .that().resideInAPackage("..application..usecases..")
                .should().beAnnotatedWith("org.springframework.stereotype.Service");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Controllers should be annotated with @RestController")
    void controllersShouldBeRestControllers() {
        ArchRule rule = classes()
                .that().resideInAPackage("..infrastructure..web..")
                .and().haveNameMatching(".*Controller")
                .should().beAnnotatedWith("org.springframework.web.bind.annotation.RestController");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("JPA repositories should be annotated with @Repository")
    void jpaRepositoriesShouldBeRepositories() {
        ArchRule rule = classes()
                .that().resideInAPackage("..infrastructure..database..repositories..")
                .and().areInterfaces()
                .and().haveNameMatching("Jpa.*Repository")
                .should().beAnnotatedWith("org.springframework.stereotype.Repository");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Configuration classes should be annotated with @Configuration")
    void configurationClassesShouldBeConfigurations() {
        ArchRule rule = classes()
                .that().haveNameMatching(".*Config")
                .should().beAnnotatedWith("org.springframework.context.annotation.Configuration");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain classes should not use Spring annotations")
    void domainClassesShouldNotUseSpringAnnotations() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().beAnnotatedWith("org.springframework.stereotype.Component")
                .orShould().beAnnotatedWith("org.springframework.stereotype.Service")
                .orShould().beAnnotatedWith("org.springframework.stereotype.Repository")
                .orShould().beAnnotatedWith("org.springframework.context.annotation.Configuration");

        rule.check(importedClasses);
    }
}