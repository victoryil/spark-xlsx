package dev.victoryil.spark;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.connector.read.InputPartition;
import org.apache.spark.sql.util.CaseInsensitiveStringMap;

import java.util.HashMap;
import java.util.Map;

/**
 * XLSX input partition implementation for Apache Spark.
 * This class represents a partition of an XLSX file to be read.
 */
@Slf4j
public class XLSXInputPartition implements InputPartition {
    private final Map<String, String> options;

    /**
     * Creates a new input partition with the specified options.
     *
     * @param options The options to use for the partition
     */
    public XLSXInputPartition(CaseInsensitiveStringMap options) {
        log.debug("Creating XLSX input partition with options: {}", options);
        this.options = new HashMap<>();
        for (String key : options.keySet()) {
            this.options.put(key, options.get(key));
        }
    }

    /**
     * Gets the options for this partition.
     *
     * @return The options map
     */
    public Map<String, String> getOptions() {
        return options;
    }
}
