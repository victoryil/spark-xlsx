package dev.victoryil.spark;

import org.apache.spark.sql.catalyst.InternalRow;
import org.apache.spark.sql.connector.read.InputPartition;
import org.apache.spark.sql.connector.read.PartitionReader;
import org.apache.spark.sql.connector.read.PartitionReaderFactory;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.util.CaseInsensitiveStringMap;

import java.util.HashMap;
import java.util.Map;

public class XLSXPartitionReaderFactory implements PartitionReaderFactory {
    private final StructType schema;
    private final Map<String, String> options; // Serializable

    public XLSXPartitionReaderFactory(StructType schema, CaseInsensitiveStringMap options) {
        this.schema = schema;
        this.options = new HashMap<>();
        for (String key : options.keySet()) {
            this.options.put(key, options.get(key));
        }
    }

    @Override
    public PartitionReader<InternalRow> createReader(InputPartition partition) {
        // Pasamos las opciones a nuestro lector
        XLSXInputPartition excelPartition = (XLSXInputPartition) partition;
        return new XLSXReader(schema, excelPartition.getOptions());
    }
}
