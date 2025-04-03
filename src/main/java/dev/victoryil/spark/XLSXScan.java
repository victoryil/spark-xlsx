package dev.victoryil.spark;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.connector.read.Batch;
import org.apache.spark.sql.connector.read.Scan;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.util.CaseInsensitiveStringMap;

/**
 * XLSX scan implementation for Apache Spark.
 * This class represents a scan operation on an XLSX file.
 */
@Slf4j
public class XLSXScan implements Scan {
    private final StructType schema;
    private final CaseInsensitiveStringMap options;

    /**
     * Creates a new scan with the specified schema and options.
     *
     * @param schema The schema to use for the scan
     * @param options The options to use for the scan
     */
    public XLSXScan(StructType schema, CaseInsensitiveStringMap options) {
        log.debug("Creating XLSX scan with schema: {} and options: {}", schema, options);
        this.schema = schema;
        this.options = options;
    }

    /**
     * Gets the schema used for reading the XLSX file.
     *
     * @return The read schema
     */
    @Override
    public StructType readSchema() {
        log.debug("Returning read schema: {}", schema);
        return schema;
    }

    /**
     * Converts this scan to a batch operation.
     *
     * @return A new batch
     */
    @Override
    public Batch toBatch() {
        log.debug("Converting scan to batch");
        return new XLSXBatch(schema, options);
    }
}
