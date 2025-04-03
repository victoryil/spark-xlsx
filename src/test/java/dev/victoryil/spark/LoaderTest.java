package dev.victoryil.spark;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the XLSX data source.
 */
@Slf4j
public class LoaderTest {
    private static SparkSession spark;

    /**
     * Initializes the Spark session before all tests.
     */
    @BeforeAll
    public static void init() {
        log.info("Initializing Spark session for tests");
        spark = SparkSession.builder()
                .master("local[*]")
                .appName("XLSX Reader Test")
                .getOrCreate();
    }

    /**
     * Cleans up resources after all tests.
     */
    @AfterAll
    public static void cleanup() {
        if (spark != null) {
            log.info("Stopping Spark session");
            spark.stop();
        }
    }

    /**
     * Tests basic loading of an XLSX file.
     * This test verifies that the XLSX file can be loaded and that the data is correctly read.
     */
    @Test
    public void testBasicLoad() {
        log.info("Testing basic XLSX file loading");

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
        log.info("Loaded data:");
        df.show();

        // Verify row count
        long rowCount = df.count();
        log.info("Row count: {}", rowCount);
        assertTrue(rowCount > 0, "Should have at least one row");

        // Verify column count
        assertEquals(2, df.columns().length, "Should have 2 columns");

        // Verify column names
        assertEquals("id", df.columns()[0], "First column should be 'id'");
        assertEquals("name", df.columns()[1], "Second column should be 'name'");
    }

    /**
     * Tests loading an XLSX file with options.
     * This test verifies that the XLSX file can be loaded using the path option.
     */
    @Test
    public void testLoadWithOptions() {
        log.info("Testing XLSX file loading with options");

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
        log.info("Loaded data with options:");
        df.show();

        // Verify row count
        long rowCount = df.count();
        log.info("Row count: {}", rowCount);
        assertTrue(rowCount > 0, "Should have at least one row");
    }
}
