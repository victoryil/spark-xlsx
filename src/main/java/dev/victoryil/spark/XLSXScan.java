package dev.victoryil.spark;

import org.apache.spark.sql.connector.read.Batch;
import org.apache.spark.sql.connector.read.Scan;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.util.CaseInsensitiveStringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * XLSX scan implementation for Apache Spark.
 * This class represents a scan operation on an XLSX file.
 */
public class XLSXScan implements Scan {
    private static final Logger logger = LoggerFactory.getLogger(XLSXScan.class);
    private final StructType schema;
    private final CaseInsensitiveStringMap options;

    /**
     * Creates a new scan with the specified schema and options.
     *
     * @param schema The schema to use for the scan
     * @param options The options to use for the scan
     */
    public XLSXScan(StructType schema, CaseInsensitiveStringMap options) {
        logger.debug("Creating XLSX scan with schema: {} and options: {}", schema, options);
        this.schema = schema;
        this.options = options;
    }

    @Override
    public StructType readSchema() {
        logger.debug("Returning read schema: {}", schema);
        return schema;
    }

    @Override
    public Batch toBatch() {
        logger.debug("Converting scan to batch");
        return new XLSXBatch(schema, options);
    }
}
