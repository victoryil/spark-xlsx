package dev.victoryil.spark;

import org.apache.spark.sql.connector.catalog.Table;
import org.apache.spark.sql.connector.catalog.TableProvider;
import org.apache.spark.sql.connector.expressions.Transform;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.util.CaseInsensitiveStringMap;

import java.util.Map;

public class Xlsx implements TableProvider {
    @Override
    public StructType inferSchema(CaseInsensitiveStringMap caseInsensitiveStringMap) {
        return null;
    }

    @Override
    public Transform[] inferPartitioning(CaseInsensitiveStringMap options) {
        return TableProvider.super.inferPartitioning(options);
    }

    @Override
    public Table getTable(StructType structType, Transform[] transforms, Map<String, String> map) {
        return new XLSXTable(structType);
    }

    @Override
    public boolean supportsExternalMetadata() {
        return true;
    }

}
