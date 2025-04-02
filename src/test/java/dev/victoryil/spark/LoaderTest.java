package dev.victoryil.spark;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the XLSX data source.
 */
public class LoaderTest {
    private static final Logger logger = LoggerFactory.getLogger(LoaderTest.class);
    private static SparkSession spark;

    @BeforeAll
    public static void init() {
        logger.info("Initializing Spark session for tests");
        spark = SparkSession.builder()
                .master("local[*]")
                .appName("XLSX Reader Test")
                .getOrCreate();
    }

    @AfterAll
    public static void cleanup() {
        if (spark != null) {
            logger.info("Stopping Spark session");
            spark.stop();
        }
    }

    @Test
    public void testBasicLoad() {
        logger.info("Testing basic XLSX file loading");

        // Define the schema for the test
        StructType schema = StructType.fromDDL("id string, name string");

        // Load the XLSX file
        Dataset<Row> df = spark.read()
                .schema(schema)
                .format("dev.victoryil.spark.Xlsx")  // Correct format name
                .load("src/test/resources/example-book.xlsx");

        // Verify the data was loaded
        assertNotNull(df, "DataFrame should not be null");

        // Show the data for debugging
        logger.info("Loaded data:");
        df.show();

        // Verify row count
        long rowCount = df.count();
        logger.info("Row count: {}", rowCount);
        assertTrue(rowCount > 0, "Should have at least one row");

        // Verify column count
        assertEquals(2, df.columns().length, "Should have 2 columns");

        // Verify column names
        assertEquals("id", df.columns()[0], "First column should be 'id'");
        assertEquals("name", df.columns()[1], "Second column should be 'name'");
    }

    @Test
    public void testLoadWithOptions() {
        logger.info("Testing XLSX file loading with options");

        // Define the schema for the test
        StructType schema = StructType.fromDDL("id string, name string");

        // Load the XLSX file with options
        Dataset<Row> df = spark.read()
                .schema(schema)
                .format("dev.victoryil.spark.Xlsx")
                .option("path", "src/test/resources/example-book.xlsx")
                .load();

        // Verify the data was loaded
        assertNotNull(df, "DataFrame should not be null");

        // Show the data for debugging
        logger.info("Loaded data with options:");
        df.show();

        // Verify row count
        long rowCount = df.count();
        logger.info("Row count: {}", rowCount);
        assertTrue(rowCount > 0, "Should have at least one row");
    }
}
