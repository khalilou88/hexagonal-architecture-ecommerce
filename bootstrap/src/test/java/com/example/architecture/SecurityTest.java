package com.example.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

@DisplayName("Security Architecture Tests")
class SecurityTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setUp() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.example");
    }

    @Test
    @DisplayName("Domain layer should not access logging directly")
    void domainShouldNotAccessLoggingDirectly() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "org.slf4j..",
                        "java.util.logging..",
                        "org.apache.logging.."
                );

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain layer should not access external APIs")
    void domainShouldNotAccessExternalAPIs() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "java.net..",
                        "org.apache.http..",
                        "okhttp3..",
                        "retrofit2.."
                );

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Only infrastructure layer should access external libraries")
    void onlyInfrastructureShouldAccessExternalLibraries() {
        ArchRule rule = noClasses()
                .that().resideOutsideOfPackage("..infrastructure..")
                .and().resideOutsideOfPackage("..bootstrap..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "com.fasterxml.jackson..",
                        "org.hibernate..",
                        "org.springframework.web.."
                );

        rule.check(importedClasses);
    }

//    @Test
//    @DisplayName("Database passwords should not be hardcoded")
//    void databasePasswordsShouldNotBeHardcoded() {
//        ArchRule rule = noClasses()
//                .should().containAnyFieldsWhere(field ->
//                        field.getName().toLowerCase().contains("password") &&
//                                field.getInitialValue() != null &&
//                                field.getInitialValue().isPresent());
//
//        rule.check(importedClasses);
//    }
}