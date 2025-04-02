package dev.victoryil.spark;

import org.apache.spark.sql.connector.read.Scan;
import org.apache.spark.sql.connector.read.ScanBuilder;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.util.CaseInsensitiveStringMap;

public class XLSXScanBuilder implements ScanBuilder {
    private final StructType schema;
    private final CaseInsensitiveStringMap options;


    public XLSXScanBuilder(StructType schema, CaseInsensitiveStringMap options) {
        this.schema = schema;
        this.options = options;
    }

    @Override
    public Scan build() {
        return new XLSXScan(schema, options);
    }
}
