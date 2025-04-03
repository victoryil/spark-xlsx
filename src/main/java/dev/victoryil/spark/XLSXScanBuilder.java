package dev.victoryil.spark;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.connector.read.Scan;
import org.apache.spark.sql.connector.read.ScanBuilder;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.util.CaseInsensitiveStringMap;

/**
 * Builder for XLSX scans.
 * This class builds scan objects for reading XLSX files.
 */
@Slf4j
public class XLSXScanBuilder implements ScanBuilder {
    private final StructType schema;
    private final CaseInsensitiveStringMap options;

    /**
     * Creates a new scan builder with the specified schema and options.
     *
     * @param schema The schema to use for the scan
     * @param options The options to use for the scan
     */
    public XLSXScanBuilder(StructType schema, CaseInsensitiveStringMap options) {
        log.debug("Creating scan builder with schema: {} and options: {}", schema, options);
        this.schema = schema;
        this.options = options;
    }

    /**
     * Builds a scan for reading the XLSX file.
     *
     * @return A new scan
     */
    @Override
    public Scan build() {
        log.debug("Building XLSX scan");
        return new XLSXScan(schema, options);
    }
}
