package com.example.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

@DisplayName("Package Structure Tests")
class PackageStructureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setUp() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.example");
    }

//    @Test
//    @DisplayName("Domain entities should reside in domain package")
//    void domainEntitiesShouldResideInDomainPackage() {
//        ArchRule rule = classes()
//                .that().haveNameMatching("User|Order|Product")
//                .and().doNotHaveNameMatching(".*Entity|.*DTO|.*Request|.*Response")
//                .should().resideInAPackage("..domain..");
//
//        rule.check(importedClasses);
//    }

    @Test
    @DisplayName("Value objects should reside in domain package")
    void valueObjectsShouldResideInDomainPackage() {
        ArchRule rule = classes()
                .that().haveNameMatching(".*Id|.*Email|.*Name|.*Address|.*Money")
                .should().resideInAPackage("..domain..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Use cases should reside in application.usecases package")
    void useCasesShouldResideInCorrectPackage() {
        ArchRule rule = classes()
                .that().haveNameMatching(".*UseCase")
                .should().resideInAPackage("..application..usecases..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Ports should reside in application.ports package")
    void portsShouldResideInCorrectPackage() {
        ArchRule rule = classes()
                .that().haveNameMatching(".*Repository|.*Port|.*Gateway")
                .and().areInterfaces()
                .and().resideInAPackage("..application..")
                .should().resideInAPackage("..application..ports..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Controllers should reside in infrastructure.web package")
    void controllersShouldResideInCorrectPackage() {
        ArchRule rule = classes()
                .that().haveNameMatching(".*Controller")
                .should().resideInAPackage("..infrastructure..web..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("JPA entities should reside in infrastructure.database.entities package")
    void jpaEntitiesShouldResideInCorrectPackage() {
        ArchRule rule = classes()
                .that().areAnnotatedWith("jakarta.persistence.Entity")
                .should().resideInAPackage("..infrastructure..database..entities..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Repository implementations should reside in infrastructure.database.repositories package")
    void repositoryImplementationsShouldResideInCorrectPackage() {
        ArchRule rule = classes()
                .that().haveNameMatching(".*RepositoryImpl")
                .should().resideInAPackage("..infrastructure..database..repositories..");

        rule.check(importedClasses);
    }
}