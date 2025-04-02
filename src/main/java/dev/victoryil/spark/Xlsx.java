package dev.victoryil.spark;

import org.apache.spark.sql.connector.catalog.Table;
import org.apache.spark.sql.connector.catalog.TableProvider;
import org.apache.spark.sql.connector.expressions.Transform;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.util.CaseInsensitiveStringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * XLSX format provider for Apache Spark.
 * This class implements the TableProvider interface to allow reading XLSX files in Spark.
 */
public class Xlsx implements TableProvider {
    private static final Logger logger = LoggerFactory.getLogger(Xlsx.class);

    @Override
    public StructType inferSchema(CaseInsensitiveStringMap options) {
        logger.warn("Schema inference not implemented yet. Please provide a schema.");
        return null;
    }

    @Override
    public Transform[] inferPartitioning(CaseInsensitiveStringMap options) {
        logger.debug("Using default partitioning");
        return TableProvider.super.inferPartitioning(options);
    }

    @Override
    public Table getTable(StructType schema, Transform[] transforms, Map<String, String> options) {
        logger.info("Creating XLSX table with provided schema");
        return new XLSXTable(schema);
    }

    @Override
    public boolean supportsExternalMetadata() {
        return true;
    }
}
