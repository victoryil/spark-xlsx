# Contributing to Spark XLSX

Thank you for your interest in contributing to Spark XLSX! This document provides guidelines and instructions for contributing to this project.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Environment](#development-environment)
- [Making Changes](#making-changes)
- [Testing](#testing)
- [Pull Request Process](#pull-request-process)
- [Coding Standards](#coding-standards)
- [Documentation](#documentation)

## Code of Conduct

By participating in this project, you agree to maintain a respectful and inclusive environment for everyone. Please be kind and constructive in your communications and contributions.

## Getting Started

1. Fork the repository on GitHub
2. Clone your fork locally:
   ```
   git clone https://github.com/yourusername/spark-xlsx.git
   cd spark-xlsx
   ```
3. Add the original repository as an upstream remote:
   ```
   git remote add upstream https://github.com/victoryil/spark-xlsx.git
   ```
4. Create a new branch for your changes:
   ```
   git checkout -b feature/your-feature-name
   ```

## Development Environment

### Prerequisites

- Java JDK 11 or higher
- Maven 3.6 or higher
- Apache Spark 3.5.0 or higher (for testing)

### Building the Project

To build the project, run:

```
mvn clean package
```

This will compile the code, run the tests, and create a JAR file in the `target` directory.

## Making Changes

1. Make your changes in your feature branch
2. Keep your changes focused on a single issue or feature
3. Write clear, concise commit messages that explain the purpose of your changes
4. Add or update tests to cover your changes
5. Update documentation as needed

## Testing

All contributions should include appropriate tests. To run the tests:

```
mvn test
```

When adding new features, please add both unit tests and integration tests where appropriate.

### Test Coverage

We aim to maintain high test coverage. Please ensure your changes don't decrease the overall test coverage.

## Pull Request Process

1. Update your fork with the latest changes from the upstream repository:
   ```
   git fetch upstream
   git rebase upstream/main
   ```

2. Push your changes to your fork:
   ```
   git push origin feature/your-feature-name
   ```

3. Create a pull request from your branch to the main repository

4. In your pull request description:
   - Clearly describe the problem you're solving or the feature you're adding
   - Link to any relevant issues
   - Mention any breaking changes
   - Include screenshots or examples if applicable

5. Wait for the code review and address any feedback

6. Once approved, your pull request will be merged

## Coding Standards

### Java Code Style

- Follow standard Java naming conventions
- Use 4 spaces for indentation (not tabs)
- Maximum line length of 100 characters
- Include JavaDoc comments for all public classes and methods
- Use meaningful variable and method names

### Logging

- Use SLF4J for logging
- Use appropriate log levels:
  - ERROR: For errors that prevent normal operation
  - WARN: For potentially harmful situations
  - INFO: For informational messages about normal operation
  - DEBUG: For detailed information useful for debugging
  - TRACE: For very detailed debugging information

### Error Handling

- Use specific exception types
- Include meaningful error messages
- Log exceptions with stack traces at appropriate levels

## Documentation

- Update the README.md if you change functionality
- Update the USAGE.md with details of any new features or options
- Add JavaDoc comments to all public classes and methods
- Keep code examples in the documentation up to date

## Questions?

If you have any questions about contributing, please open an issue or reach out to the maintainers.

Thank you for contributing to Spark XLSX!