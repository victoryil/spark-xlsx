package dev.victoryil.spark;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LoaderTest {
    static SparkSession spark;
    @BeforeAll
    public static void init() {
        spark = SparkSession.builder().master("local[*]").appName("Test").getOrCreate();
    }
    @Test
    public void load() {
        spark.read()
                .schema(StructType.fromDDL("id string, name string"))
                .format("dev.victoryil.spark.xlsx.ExcelTableProvider")
                .load("src/test/resources/Book.xlsx")
                .show();
    }
}
