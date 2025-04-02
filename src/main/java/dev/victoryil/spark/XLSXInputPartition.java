package dev.victoryil.spark;

import org.apache.spark.sql.connector.read.InputPartition;
import org.apache.spark.sql.util.CaseInsensitiveStringMap;

import java.util.HashMap;
import java.util.Map;

public class XLSXInputPartition implements InputPartition {
    private final Map<String, String> options;

    public XLSXInputPartition(CaseInsensitiveStringMap options) {
        this.options = new HashMap<>();
        for (String key : options.keySet()) {
            this.options.put(key, options.get(key));
        }    }

    public Map<String, String> getOptions() {
        return options;
    }
}
