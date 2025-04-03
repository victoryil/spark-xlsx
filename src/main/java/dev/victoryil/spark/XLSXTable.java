package dev.victoryil.spark;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.connector.catalog.SupportsRead;
import org.apache.spark.sql.connector.catalog.Table;
import org.apache.spark.sql.connector.catalog.TableCapability;
import org.apache.spark.sql.connector.read.ScanBuilder;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.util.CaseInsensitiveStringMap;

import java.util.Collections;
import java.util.Set;

/**
 * XLSX table implementation for Apache Spark.
 * This class represents a table backed by an XLSX file.
 */
@Slf4j
public class XLSXTable implements Table, SupportsRead {
    private final StructType schema;

    /**
     * Creates a new XLSX table with the specified schema.
     *
     * @param schema The schema of the table
     */
    public XLSXTable(StructType schema) {
        log.debug("Creating XLSX table with schema: {}", schema);
        this.schema = schema;
    }

    /**
     * Creates a new scan builder for this table.
     *
     * @param options Options for reading the XLSX file
     * @return A new scan builder
     */
    @Override
    public ScanBuilder newScanBuilder(CaseInsensitiveStringMap options) {
        log.debug("Creating scan builder with options: {}", options);
        return new XLSXScanBuilder(schema, options);
    }

    /**
     * Gets the name of this table.
     *
     * @return The table name
     */
    @Override
    public String name() {
        return "xlsx_table";
    }

    /**
     * Gets the schema of this table.
     *
     * @return The table schema
     */
    @Override
    public StructType schema() {
        return schema;
    }

    /**
     * Gets the capabilities of this table.
     * Currently only supports batch reading.
     *
     * @return The set of table capabilities
     */
    @Override
    public Set<TableCapability> capabilities() {
        log.debug("Reporting table capabilities: BATCH_READ");
        return Collections.singleton(TableCapability.BATCH_READ);
    }
}
