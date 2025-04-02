package dev.victoryil.spark;

import org.apache.spark.sql.connector.read.Scan;
import org.apache.spark.sql.connector.read.ScanBuilder;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.util.CaseInsensitiveStringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builder for XLSX scans.
 * This class builds scan objects for reading XLSX files.
 */
public class XLSXScanBuilder implements ScanBuilder {
    private static final Logger logger = LoggerFactory.getLogger(XLSXScanBuilder.class);
    private final StructType schema;
    private final CaseInsensitiveStringMap options;

    /**
     * Creates a new scan builder with the specified schema and options.
     *
     * @param schema The schema to use for the scan
     * @param options The options to use for the scan
     */
    public XLSXScanBuilder(StructType schema, CaseInsensitiveStringMap options) {
        logger.debug("Creating scan builder with schema: {} and options: {}", schema, options);
        this.schema = schema;
        this.options = options;
    }

    @Override
    public Scan build() {
        logger.debug("Building XLSX scan");
        return new XLSXScan(schema, options);
    }
}
