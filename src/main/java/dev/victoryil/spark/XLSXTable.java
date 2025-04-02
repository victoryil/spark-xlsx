package dev.victoryil.spark;

import org.apache.spark.sql.connector.catalog.SupportsRead;
import org.apache.spark.sql.connector.catalog.Table;
import org.apache.spark.sql.connector.catalog.TableCapability;
import org.apache.spark.sql.connector.read.ScanBuilder;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.util.CaseInsensitiveStringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;

/**
 * XLSX table implementation for Apache Spark.
 * This class represents a table backed by an XLSX file.
 */
public class XLSXTable implements Table, SupportsRead {
    private static final Logger logger = LoggerFactory.getLogger(XLSXTable.class);
    private final StructType schema;

    /**
     * Creates a new XLSX table with the specified schema.
     *
     * @param schema The schema of the table
     */
    public XLSXTable(StructType schema) {
        logger.debug("Creating XLSX table with schema: {}", schema);
        this.schema = schema;
    }

    @Override
    public ScanBuilder newScanBuilder(CaseInsensitiveStringMap options) {
        logger.debug("Creating scan builder with options: {}", options);
        return new XLSXScanBuilder(schema, options);
    }

    @Override
    public String name() {
        return "xlsx_table";
    }

    @Override
    public StructType schema() {
        return schema;
    }

    @Override
    public Set<TableCapability> capabilities() {
        logger.debug("Reporting table capabilities: BATCH_READ");
        return Collections.singleton(TableCapability.BATCH_READ);
    }
}
