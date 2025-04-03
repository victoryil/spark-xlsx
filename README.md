# Spark XLSX

A data source implementation for Apache Spark that enables reading XLSX (Excel) files directly into Spark DataFrames.

## Overview

Spark XLSX is a library that extends Apache Spark's data source API to support reading Microsoft Excel XLSX files. It allows you to seamlessly integrate Excel data into your Spark data processing pipelines.

## Features

- Read XLSX files directly into Spark DataFrames
- Support for schema definition
- Simple integration with Spark's standard read API
- Compatible with Apache Spark 3.5.0+

## Quick Start

### Maven Dependency

Add the following dependency to your project:

```xml
<dependency>
    <groupId>dev.victoryil</groupId>
    <artifactId>spark-xlsx</artifactId>
    <version>0.1.0</version>
</dependency>
```

### Basic Usage

```java
// Define your schema
StructType schema = StructType.fromDDL("id string, name string");

// Read an XLSX file
Dataset<Row> df = spark.read()
        .schema(schema)
        .format("dev.victoryil.spark.Xlsx")
        .load("path/to/your/file.xlsx");

// Show the data
df.show();
```

### Using Options

```java
Dataset<Row> df = spark.read()
        .schema(schema)
        .format("dev.victoryil.spark.Xlsx")
        .option("path", "path/to/your/file.xlsx")
        .load();
```

## Requirements

- Apache Spark 3.5.0+
- Java 11+

## Documentation

For more detailed information on usage and configuration options, please see the [Usage Documentation](USAGE.md).

If you're interested in contributing to the project, check out our [Contribution Guidelines](CONTRIBUTING.md).

## Deploying to Maven Central

This project is configured for deployment to Maven Central via Central Sonatype. To deploy a new version:

### Prerequisites

1. Create a [Sonatype account](https://central.sonatype.org/)
2. Request access to the `dev.victoryil` group ID in Central Sonatype
3. Set up GPG signing keys on your machine
4. Configure your Maven settings.xml with Sonatype token

### Configuration

1. Copy the template settings file from `.mvn/settings.xml` to `~/.m2/settings.xml` (or merge with your existing settings)
2. Update the file with your Sonatype token
3. Configure your GPG key information if needed

### Deployment Process

#### Option 1: Using the Automated Release Script

The project includes a Bash script that automates the entire release process:

```bash
./release.sh -r RELEASE_VERSION -d DEVELOPMENT_VERSION -u MAVEN_USERNAME -p MAVEN_PASSWORD \
  -k "$(cat your_gpg_key.asc)" -i GPG_KEY_ID -s GPG_PASSPHRASE [-g GITHUB_TOKEN]
```

Arguments:
- `-r RELEASE_VERSION`: The version to release (e.g., 0.1.0)
- `-d DEVELOPMENT_VERSION`: The next development version with -SNAPSHOT suffix (e.g., 0.2.0-SNAPSHOT)
- `-u MAVEN_USERNAME`: Maven Central (Sonatype) username
- `-p MAVEN_PASSWORD`: Maven Central (Sonatype) password
- `-k GPG_PRIVATE_KEY`: GPG private key (exported with `gpg --export-secret-keys --armor KEY_ID`)
- `-i GPG_KEY_ID`: GPG key ID
- `-s GPG_PASSPHRASE`: GPG passphrase
- `-g GITHUB_TOKEN`: (Optional) GitHub token for creating GitHub releases

This script will:
1. Update the version to the release version
2. Build and deploy to Maven Central
3. Create a Git tag for the release
4. Update to the next development version
5. Push changes to main branch and tags
6. Handle the develop branch
7. Create a GitHub release (if GitHub token is provided)

#### Option 2: Manual Deployment

To deploy a snapshot version:

```bash
mvn clean deploy
```

To deploy a release version:

```bash
mvn clean deploy -P release
```

This will:
1. Compile the code
2. Run tests
3. Generate Javadocs and source JARs
4. Sign all artifacts with GPG
5. Deploy to Central Sonatype
6. Automatically release to Maven Central (if using the release profile)

## License

This project is licensed under the [MIT License](LICENSE) - see the LICENSE file for details.

## Author

- Victor Yil (https://victoryil.dev)
