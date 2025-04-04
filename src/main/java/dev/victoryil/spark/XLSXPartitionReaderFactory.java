package dev.victoryil.spark;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.catalyst.InternalRow;
import org.apache.spark.sql.connector.read.InputPartition;
import org.apache.spark.sql.connector.read.PartitionReader;
import org.apache.spark.sql.connector.read.PartitionReaderFactory;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.util.CaseInsensitiveStringMap;

import java.util.HashMap;
import java.util.Map;

/**
 * XLSX partition reader factory implementation for Apache Spark.
 * This class creates readers for XLSX partitions.
 */
@Slf4j
public class XLSXPartitionReaderFactory implements PartitionReaderFactory {
    private final StructType schema;
    private final Map<String, String> options; // Serializable

    /**
     * Creates a new partition reader factory with the specified schema and options.
     *
     * @param schema The schema to use for reading
     * @param options The options to use for reading
     */
    public XLSXPartitionReaderFactory(StructType schema, CaseInsensitiveStringMap options) {
        log.debug("Creating XLSX partition reader factory with schema: {} and options: {}", schema, options);
        this.schema = schema;
        this.options = new HashMap<>();
        for (String key : options.keySet()) {
            this.options.put(key, options.get(key));
        }
    }

    /**
     * Creates a reader for the specified partition.
     *
     * @param partition The partition to create a reader for
     * @return A new partition reader
     */
    @Override
    public PartitionReader<InternalRow> createReader(InputPartition partition) {
        // Pass the options to our reader
        log.debug("Creating reader for partition: {}", partition);
        XLSXInputPartition excelPartition = (XLSXInputPartition) partition;
        return new XLSXReader(schema, excelPartition.getOptions());
    }
}
