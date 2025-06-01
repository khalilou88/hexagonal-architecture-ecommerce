package com.example.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

@DisplayName("Naming Convention Tests")
class NamingConventionTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setUp() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.example");
    }

//    @Test
//    @DisplayName("Repository implementations should be named *RepositoryImpl")
//    void repositoryImplementationsShouldBeNamedCorrectly() {
//        ArchRule rule = classes()
//                .that().resideInAPackage("..infrastructure..repositories..")
//                .and().implement(classes().that().resideInAPackage("..application..ports.."))
//                .should().haveNameMatching(".*RepositoryImpl");
//
//        rule.check(importedClasses);
//    }

    @Test
    @DisplayName("Repository ports should be named *Repository")
    void repositoryPortsShouldBeNamedCorrectly() {
        ArchRule rule = classes()
                .that().resideInAPackage("..application..ports..")
                .and().areInterfaces()
                .should().haveNameMatching(".*Repository");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Use cases should be named *UseCase")
    void useCasesShouldBeNamedCorrectly() {
        ArchRule rule = classes()
                .that().resideInAPackage("..application..usecases..")
                .should().haveNameMatching(".*UseCase");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Controllers should be named *Controller")
    void controllersShouldBeNamedCorrectly() {
        ArchRule rule = classes()
                .that().resideInAPackage("..infrastructure..web..")
                .and().areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
                .should().haveNameMatching(".*Controller");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("JPA entities should be named *Entity")
    void jpaEntitiesShouldBeNamedCorrectly() {
        ArchRule rule = classes()
                .that().resideInAPackage("..infrastructure..database..entities..")
                .and().areAnnotatedWith("jakarta.persistence.Entity")
                .should().haveNameMatching(".*Entity");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain entities should not be named *Entity")
    void domainEntitiesShouldNotBeNamedEntity() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().haveNameMatching(".*Entity");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Value objects should be immutable")
    void valueObjectsShouldBeImmutable() {
        ArchRule rule = classes()
                .that().resideInAPackage("..domain..")
                .and().haveNameMatching(".*Id|.*Email|.*Name|.*Address|.*Money")
                .should().haveOnlyFinalFields();

        rule.check(importedClasses);
    }
}





