package dev.victoryil.spark;

import org.apache.spark.sql.connector.read.Batch;
import org.apache.spark.sql.connector.read.Scan;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.util.CaseInsensitiveStringMap;

public class XLSXScan implements Scan {
    private final StructType schema;
    private final CaseInsensitiveStringMap options;

    public XLSXScan(StructType schema, CaseInsensitiveStringMap options) {
        this.schema = schema;
        this.options = options;
    }

    @Override
    public StructType readSchema() {
        return schema;
    }

    @Override
    public Batch toBatch() {
        return new XLSXBatch(schema, options);
    }
}
