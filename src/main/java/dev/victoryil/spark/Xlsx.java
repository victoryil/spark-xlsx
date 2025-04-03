package dev.victoryil.spark;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.connector.catalog.Table;
import org.apache.spark.sql.connector.catalog.TableProvider;
import org.apache.spark.sql.connector.expressions.Transform;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.util.CaseInsensitiveStringMap;

import java.util.Map;

/**
 * XLSX format provider for Apache Spark.
 * This class implements the TableProvider interface to allow reading XLSX files in Spark.
 */
@Slf4j
public class Xlsx implements TableProvider {

    /**
     * Infers the schema from the XLSX file.
     * Currently not implemented - users must provide a schema.
     *
     * @param options Options for reading the XLSX file
     * @return The inferred schema (currently returns null)
     */
    @Override
    public StructType inferSchema(CaseInsensitiveStringMap options) {
        log.warn("Schema inference not implemented yet. Please provide a schema.");
        return null;
    }

    /**
     * Infers the partitioning for the XLSX file.
     * Currently uses the default partitioning.
     *
     * @param options Options for reading the XLSX file
     * @return The inferred partitioning
     */
    @Override
    public Transform[] inferPartitioning(CaseInsensitiveStringMap options) {
        log.debug("Using default partitioning");
        return TableProvider.super.inferPartitioning(options);
    }

    /**
     * Creates a table for the XLSX file with the provided schema.
     *
     * @param schema The schema to use for the table
     * @param transforms The partitioning transforms to use
     * @param options Options for reading the XLSX file
     * @return The created table
     */
    @Override
    public Table getTable(StructType schema, Transform[] transforms, Map<String, String> options) {
        log.info("Creating XLSX table with provided schema");
        return new XLSXTable(schema);
    }

    /**
     * Indicates whether this provider supports external metadata.
     *
     * @return true if external metadata is supported, false otherwise
     */
    @Override
    public boolean supportsExternalMetadata() {
        return true;
    }
}
