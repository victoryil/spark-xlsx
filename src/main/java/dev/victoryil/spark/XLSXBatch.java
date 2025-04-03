package dev.victoryil.spark;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.connector.read.Batch;
import org.apache.spark.sql.connector.read.InputPartition;
import org.apache.spark.sql.connector.read.PartitionReaderFactory;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.util.CaseInsensitiveStringMap;

/**
 * XLSX batch implementation for Apache Spark.
 * This class represents a batch operation on an XLSX file.
 */
@Slf4j
public class XLSXBatch implements Batch {
    private final StructType schema;
    private final CaseInsensitiveStringMap options;

    /**
     * Creates a new batch with the specified schema and options.
     *
     * @param schema The schema to use for the batch
     * @param options The options to use for the batch
     */
    public XLSXBatch(StructType schema, CaseInsensitiveStringMap options) {
        log.debug("Creating XLSX batch with schema: {} and options: {}", schema, options);
        this.schema = schema;
        this.options = options;
    }

    /**
     * Plans the input partitions for reading the XLSX file.
     * Currently creates a single partition that reads the entire file.
     *
     * @return An array of input partitions
     */
    @Override
    public InputPartition[] planInputPartitions() {
        // For now: a single partition that reads the entire file
        log.debug("Planning input partitions (single partition)");
        return new InputPartition[] {
                new XLSXInputPartition(options)
        };
    }

    /**
     * Creates a factory for partition readers.
     *
     * @return A new partition reader factory
     */
    @Override
    public PartitionReaderFactory createReaderFactory() {
        log.debug("Creating partition reader factory");
        return new XLSXPartitionReaderFactory(schema, options);
    }
}
