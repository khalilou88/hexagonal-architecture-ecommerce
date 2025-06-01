package com.example.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@DisplayName("Hexagonal Architecture Tests")
class HexagonalArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setUp() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.example");
    }

    @Test
    @DisplayName("Domain layer should not depend on any other layers")
    void domainShouldNotDependOnOtherLayers() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "..infrastructure..",
                        "..application..",
                        "..bootstrap.."
                );

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Application layer should only depend on domain layer")
    void applicationShouldOnlyDependOnDomain() {
        ArchRule rule = classes()
                .that().resideInAPackage("..application..")
                .should().onlyDependOnClassesThat().resideInAnyPackage(
                        "..domain..",
                        "..application..",
                        "java..",
                        "javax..",
                        "jakarta.."
                );

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Infrastructure layer can depend on application and domain layers")
    void infrastructureCanDependOnApplicationAndDomain() {
        ArchRule rule = classes()
                .that().resideInAPackage("..infrastructure..")
                .should().onlyDependOnClassesThat().resideInAnyPackage(
                        "..domain..",
                        "..application..",
                        "..infrastructure..",
                        "java..",
                        "javax..",
                        "jakarta..",
                        "org.springframework..",
                        "org.hibernate..",
                        "com.fasterxml..",
                        "org.slf4j.."
                );

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Bootstrap layer can depend on all layers")
    void bootstrapCanDependOnAllLayers() {
        ArchRule rule = classes()
                .that().resideInAPackage("..bootstrap..")
                .should().onlyDependOnClassesThat().resideInAnyPackage(
                        "..domain..",
                        "..application..",
                        "..infrastructure..",
                        "..bootstrap..",
                        "java..",
                        "javax..",
                        "jakarta..",
                        "org.springframework..",
                        "org.slf4j.."
                );

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain entities should not use framework annotations")
    void domainEntitiesShouldNotUseFrameworkAnnotations() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().beAnnotatedWith("org.springframework..")
                .orShould().beAnnotatedWith("jakarta.persistence..")
                .orShould().beAnnotatedWith("javax.persistence..")
                .orShould().beAnnotatedWith("com.fasterxml.jackson..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Infrastructure adapters should implement application ports")
    void infrastructureAdaptersShouldImplementPorts() {
        ArchRule rule = classes()
                .that().resideInAPackage("..infrastructure..repositories..")
                .and().haveNameMatching(".*RepositoryImpl")
                .should().implement(classes().that().resideInAPackage("..application..ports.."));

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Ports should be interfaces")
    void portsShouldBeInterfaces() {
        ArchRule rule = classes()
                .that().resideInAPackage("..application..ports..")
                .should().beInterfaces();

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Use cases should not depend on infrastructure")
    void useCasesShouldNotDependOnInfrastructure() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..application..usecases..")
                .should().dependOnClassesThat().resideInAPackage("..infrastructure..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Controllers should only call use cases")
    void controllersShouldOnlyCallUseCases() {
        ArchRule rule = classes()
                .that().resideInAPackage("..infrastructure..web..")
                .and().haveNameMatching(".*Controller")
                .should().onlyAccessClassesThat().resideInAnyPackage(
                        "..application..usecases..",
                        "..application..dto..",
                        "java..",
                        "org.springframework..",
                        "jakarta..",
                        "org.slf4j.."
                );

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain services should only exist in domain package")
    void domainServicesShouldOnlyExistInDomainPackage() {
        ArchRule rule = classes()
                .that().haveNameMatching(".*DomainService")
                .should().resideInAPackage("..domain..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Layered architecture should be respected")
    void layeredArchitectureShouldBeRespected() {
        ArchRule rule = layeredArchitecture()
                .consideringAllDependencies()
                .layer("Domain").definedBy("..domain..")
                .layer("Application").definedBy("..application..")
                .layer("Infrastructure").definedBy("..infrastructure..")
                .layer("Bootstrap").definedBy("..bootstrap..")

                .whereLayer("Domain").mayNotAccessAnyLayer()
                .whereLayer("Application").mayOnlyAccessLayers("Domain")
                .whereLayer("Infrastructure").mayOnlyAccessLayers("Application", "Domain")
                .whereLayer("Bootstrap").mayAccessAnyLayer();

        rule.check(importedClasses);
    }
}

