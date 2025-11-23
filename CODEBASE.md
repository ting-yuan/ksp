# Kotlin Symbol Processing (KSP) Codebase Analysis

## Project Overview
Kotlin Symbol Processing (KSP) is an API that allows for the development of lightweight compiler plugins. It provides a simplified API compared to the full Kotlin compiler plugin API, leveraging the power of Kotlin while minimizing the learning curve. KSP is designed to be faster than KAPT (Kotlin Annotation Processing Tool).

## Modules

### Core Modules

- **`api`**
  - **Description**: The public API of KSP. This is the library that annotation processor authors interact with.
  - **Key Components**: Interfaces for symbols, types, and processing environment.

- **`symbol-processing`**
  - **Description**: Historically the core implementation of KSP (KSP 1.x). In the current codebase, KSP 1.x support has been removed, and this module serves as a dummy artifact/placeholder, likely to satisfy build requirements or legacy dependency structures.

- **`kotlin-analysis-api`**
  - **Description**: The implementation of KSP based on the Kotlin Analysis API. This represents KSP 2.0 (KSP2), which is now the default and only supported implementation. It offers better performance and a more robust architecture.
  - **Dependencies**: Heavily depends on the Kotlin compiler and IntelliJ platform libraries.

- **`symbol-processing-aa-embeddable`**
  - **Description**: An embeddable version of the `kotlin-analysis-api` module. It uses the Shadow plugin to relocate dependencies, creating a standalone jar that avoids classpath conflicts when used in other projects (like the Gradle plugin).

- **`gradle-plugin`**
  - **Description**: The Gradle plugin that integrates KSP into the build process. It allows users to apply KSP processors to their Kotlin projects.
  - **Key Components**: `KspGradleSubplugin`, `KspAATask`. It enforces the use of KSP 2.0 (`symbol-processing-aa-embeddable`) and throws an error if KSP 1.x is requested.

### Support Modules

- **`common-deps`**
  - **Description**: Contains common dependencies and utilities. It uses `cmdline-parser-gen` to generate code.

- **`common-util`**
  - **Description**: General utility functions and classes used across the KSP codebase. Depends on the `api` module.

- **`cmdline-parser-gen`**
  - **Description**: A tool for generating command-line argument parsers. This is likely used to handle arguments passed to the KSP compiler plugin.

### Testing and Examples

- **`test-utils`**
  - **Description**: Utilities for testing KSP processors and the KSP infrastructure itself.

- **`integration-tests`**
  - **Description**: A comprehensive suite of integration tests to ensure KSP works correctly in various scenarios and with different Kotlin versions.

- **`examples`**
  - **Description**: Example projects demonstrating how to use KSP.

- **`benchmark`**
  - **Description**: Benchmarking tools to measure the performance of KSP.

## Build System
The project uses Gradle with Kotlin DSL (`build.gradle.kts`). It employs a composite build structure or multi-module build.
- **`buildSrc`**: Contains custom Gradle build logic and dependency management.
- **`settings.gradle.kts`**: Defines the project structure and included builds.

## Key Technologies
- **Kotlin**: The primary language for the project.
- **Gradle**: The build system.
- **Kotlin Analysis API**: The foundation for KSP 2.0.
- **IntelliJ Platform**: Underlying infrastructure used by the Kotlin compiler and Analysis API.
