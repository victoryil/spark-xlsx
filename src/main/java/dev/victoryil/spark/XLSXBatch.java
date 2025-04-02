package dev.victoryil.spark;

import org.apache.spark.sql.connector.read.Batch;
import org.apache.spark.sql.connector.read.InputPartition;
import org.apache.spark.sql.connector.read.PartitionReaderFactory;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.util.CaseInsensitiveStringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * XLSX batch implementation for Apache Spark.
 * This class represents a batch operation on an XLSX file.
 */
public class XLSXBatch implements Batch {
    private static final Logger logger = LoggerFactory.getLogger(XLSXBatch.class);
    private final StructType schema;
    private final CaseInsensitiveStringMap options;

    /**
     * Creates a new batch with the specified schema and options.
     *
     * @param schema The schema to use for the batch
     * @param options The options to use for the batch
     */
    public XLSXBatch(StructType schema, CaseInsensitiveStringMap options) {
        logger.debug("Creating XLSX batch with schema: {} and options: {}", schema, options);
        this.schema = schema;
        this.options = options;
    }

    @Override
    public InputPartition[] planInputPartitions() {
        // For now: a single partition that reads the entire file
        logger.debug("Planning input partitions (single partition)");
        return new InputPartition[] {
                new XLSXInputPartition(options)
        };
    }

    @Override
    public PartitionReaderFactory createReaderFactory() {
        logger.debug("Creating partition reader factory");
        return new XLSXPartitionReaderFactory(schema, options);
    }
}
