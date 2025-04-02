package dev.victoryil.spark;

import org.apache.spark.sql.connector.read.Batch;
import org.apache.spark.sql.connector.read.InputPartition;
import org.apache.spark.sql.connector.read.PartitionReaderFactory;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.util.CaseInsensitiveStringMap;

public class XLSXBatch implements Batch {
    private final StructType schema;
    private final CaseInsensitiveStringMap options;

    public XLSXBatch(StructType schema, CaseInsensitiveStringMap options) {
        this.schema = schema;
        this.options = options;
    }

    @Override
    public InputPartition[] planInputPartitions() {
        // Por ahora: una sola partición que lee un archivo completo
        return new InputPartition[] {
                new XLSXInputPartition(options)
        };
    }

    @Override
    public PartitionReaderFactory createReaderFactory() {
        return new XLSXPartitionReaderFactory(schema, options);
    }
}
