# Spark XLSX Usage Guide

This document provides detailed information on how to use the Spark XLSX library to read Excel files in your Apache Spark applications.

## Table of Contents

- [Installation](#installation)
- [Basic Usage](#basic-usage)
- [Configuration Options](#configuration-options)
- [Data Type Mapping](#data-type-mapping)
- [Best Practices](#best-practices)
- [Troubleshooting](#troubleshooting)

## Installation

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>dev.victoryil</groupId>
    <artifactId>spark-xlsx</artifactId>
    <version>1.0.0</version>
</dependency>
```

### SBT

Add the following to your `build.sbt`:

```scala
libraryDependencies += "dev.victoryil" % "spark-xlsx" % "1.0.0"
```

## Basic Usage

### Java Example

```java
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

SparkSession spark = SparkSession.builder()
    .appName("XLSX Reader Example")
    .getOrCreate();

// Define your schema
StructType schema = StructType.fromDDL("id string, name string, age integer, salary double");

// Read an XLSX file
Dataset<Row> df = spark.read()
    .schema(schema)
    .format("dev.victoryil.spark.Xlsx")
    .load("path/to/your/file.xlsx");

// Show the data
df.show();

// Process the data
df.createOrReplaceTempView("employees");
spark.sql("SELECT name, salary FROM employees WHERE age > 30").show();
```

### Scala Example

```scala
import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.spark.sql.types._

val spark = SparkSession.builder()
  .appName("XLSX Reader Example")
  .getOrCreate()

// Define your schema
val schema = StructType(Array(
  StructField("id", StringType, true),
  StructField("name", StringType, true),
  StructField("age", IntegerType, true),
  StructField("salary", DoubleType, true)
))

// Read an XLSX file
val df = spark.read
  .schema(schema)
  .format("dev.victoryil.spark.Xlsx")
  .load("path/to/your/file.xlsx")

// Show the data
df.show()

// Process the data
df.createOrReplaceTempView("employees")
spark.sql("SELECT name, salary FROM employees WHERE age > 30").show()
```

### Python (PySpark) Example

```python
from pyspark.sql import SparkSession
from pyspark.sql.types import StructType, StructField, StringType, IntegerType, DoubleType

spark = SparkSession.builder \
    .appName("XLSX Reader Example") \
    .getOrCreate()

# Define your schema
schema = StructType([
    StructField("id", StringType(), True),
    StructField("name", StringType(), True),
    StructField("age", IntegerType(), True),
    StructField("salary", DoubleType(), True)
])

# Read an XLSX file
df = spark.read \
    .schema(schema) \
    .format("dev.victoryil.spark.Xlsx") \
    .load("path/to/your/file.xlsx")

# Show the data
df.show()

# Process the data
df.createOrReplaceTempView("employees")
spark.sql("SELECT name, salary FROM employees WHERE age > 30").show()
```

## Configuration Options

The Spark XLSX reader supports the following options:

| Option | Description | Default |
|--------|-------------|---------|
| `path` | Path to the XLSX file | Required |

## Data Type Mapping

The Spark XLSX reader maps Excel cell types to Spark data types as follows:

| Excel Cell Type | Spark Data Type |
|-----------------|-----------------|
| String | StringType |
| Numeric | IntegerType, LongType, DoubleType, or DecimalType (based on schema) |
| Boolean | BooleanType |
| Date | DateType |
| DateTime | TimestampType |
| Formula | Evaluated based on the result type |

## Best Practices

1. **Always Define a Schema**: While the library could theoretically infer a schema, it's best practice to explicitly define one to ensure proper data type mapping.

2. **Handle Large Files Carefully**: Excel files can be memory-intensive. For very large files, consider splitting them into smaller files or using a different format like CSV or Parquet.

3. **Check for Empty Cells**: Excel files often contain empty cells. Make sure your schema allows for nullable fields or provide default values.

4. **Validate Data**: After loading, validate that the data meets your expectations, especially for numeric and date fields.

## Troubleshooting

### Common Issues

1. **OutOfMemoryError**:
   - Increase Spark driver/executor memory
   - Split large Excel files into smaller ones

2. **Schema Mismatch**:
   - Ensure your schema matches the structure of your Excel file
   - Check for data type compatibility issues

3. **Missing Dependencies**:
   - Ensure Apache POI dependencies are available on all nodes

### Logging

The library uses SLF4J for logging. To see more detailed logs, configure your logging framework (e.g., log4j) to set the log level for the `dev.victoryil.spark` package:

```properties
# log4j.properties
log4j.logger.dev.victoryil.spark=DEBUG
```

### Getting Help

If you encounter issues not covered in this documentation, please:

1. Check the [GitHub issues](https://github.com/victoryil/spark-xlsx/issues) for similar problems
2. Open a new issue with a detailed description of your problem, including:
   - Your Spark version
   - Your code
   - The error message
   - A sample of your Excel file (if possible)