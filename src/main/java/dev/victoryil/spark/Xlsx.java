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

    @Override
    public StructType inferSchema(CaseInsensitiveStringMap options) {
        log.warn("Schema inference not implemented yet. Please provide a schema.");
        return null;
    }

    @Override
    public Transform[] inferPartitioning(CaseInsensitiveStringMap options) {
        log.debug("Using default partitioning");
        return TableProvider.super.inferPartitioning(options);
    }

    @Override
    public Table getTable(StructType schema, Transform[] transforms, Map<String, String> options) {
        log.info("Creating XLSX table with provided schema");
        return new XLSXTable(schema);
    }

    @Override
    public boolean supportsExternalMetadata() {
        return true;
    }
}
