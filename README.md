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
    <version>1.0.0</version>
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

## License

This project is licensed under the [MIT License](LICENSE) - see the LICENSE file for details.

## Author

- Victor Yil (https://victoryil.dev)
