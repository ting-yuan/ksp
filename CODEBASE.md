# KSP Codebase Overview

This document provides an overview of the Kotlin Symbol Processing (KSP) project structure and modules.

## Project Root

The root directory contains the global project configuration, including `settings.gradle.kts` and `build.gradle.kts`. It defines the multi-module structure and common build settings.

## Modules

### Core API and Plugin
*   **`:api`**
    *   **Description:** Defines the public KSP API that processor authors use.
    *   **Artifact:** `symbol-processing-api`
    *   **Key Components:** `Resolver`, `SymbolProcessor`, `KSNode` hierarchy.

*   **`:gradle-plugin`**
    *   **Description:** The KSP Gradle plugin implementation (`com.google.devtools.ksp`). It handles the integration of KSP into the Gradle build lifecycle, task configuration, and dependencies.
    *   **Artifact:** `symbol-processing-gradle-plugin`

*   **`:symbol-processing`**
    *   **Description:** Currently acts as a dummy artifact or legacy placeholder. It depends on other implementation modules in practice.
    *   **Artifact:** `symbol-processing`

### Implementation (KSP2 / AA)
*   **`:kotlin-analysis-api`**
    *   **Description:** The implementation of KSP based on the Kotlin Analysis API (K2). This is the modern implementation of KSP.
    *   **Artifact:** `symbol-processing-aa` (shadowed)
    *   **Dependencies:** Heavy dependence on Kotlin compiler internals and IntelliJ platform components.

*   **`:symbol-processing-aa-embeddable`**
    *   **Description:** Creates an embeddable artifact of the AA implementation. It shades (relocates) dependencies to avoid conflicts when used in other projects.
    *   **Artifact:** `symbol-processing-aa-embeddable`

### Shared Utilities
*   **`:common-deps`**
    *   **Description:** Bundles common dependencies and internal utilities shared across different KSP modules. It utilizes `:cmdline-parser-gen` for argument parsing.
    *   **Artifact:** `symbol-processing-common-deps`

*   **`:common-util`**
    *   **Description:** General utility functions and helpers used throughout the project.

*   **`:cmdline-parser-gen`**
    *   **Description:** A tool to generate command-line argument parsers, used by `:common-deps`.

### Testing and Infrastructure
*   **`:integration-tests`**
    *   **Description:** Contains end-to-end integration tests. These tests use Gradle TestKit to run KSP against various project setups, verifying compatibility with different Kotlin and AGP versions.

*   **`:test-utils`**
    *   **Description:** Provides utility classes and functions for testing KSP processors and the KSP infrastructure itself.

*   **`:buildSrc`**
    *   **Description:** Contains build logic, custom Gradle tasks (e.g., for Ktlint, API checks), and path providers used in the build process.

## Build System
The project uses Gradle with Kotlin DSL. Dependencies and versions are managed in the root `build.gradle.kts` and `gradle.properties` (implied), with module-specific configurations in their respective build files.

## Key Workflows
*   **API Changes:** Modified in `:api`.
*   **Plugin Logic:** Modified in `:gradle-plugin`.
*   **KSP Implementation:** Modified in `:kotlin-analysis-api`.
*   **Testing:** Unit tests in individual modules, integration tests in `:integration-tests`.
