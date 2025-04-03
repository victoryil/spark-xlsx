package dev.victoryil.spark;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.spark.sql.catalyst.InternalRow;
import org.apache.spark.sql.catalyst.expressions.GenericInternalRow;
import org.apache.spark.sql.connector.read.PartitionReader;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DecimalType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.unsafe.types.UTF8String;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Reader for XLSX files in Apache Spark.
 * This class reads data from XLSX files and converts it to Spark's internal row format.
 */
@Slf4j
public class XLSXReader implements PartitionReader<InternalRow> {

    private final StructType schema;
    private final Iterator<Row> rowIterator;
    private final FormulaEvaluator evaluator;
    private final DataType[] fieldTypes;
    private Row currentRow;

    /**
     * Creates a new reader for an XLSX file.
     *
     * @param schema The schema to use for reading
     * @param options The options to use for reading, including the path to the XLSX file
     * @throws RuntimeException if there is an error reading the XLSX file
     */
    public XLSXReader(StructType schema, Map<String, String> options) {
        this.schema = schema;
        this.fieldTypes = Arrays.stream(schema.fields())
                .map(StructField::dataType)
                .toArray(DataType[]::new);
        try {
            String path = options.get("path");
            log.info("Reading Excel file from path: {}", path);

            FileInputStream fis = new FileInputStream(new File(path));
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            this.rowIterator = sheet.iterator();
            this.evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            if (rowIterator.hasNext()) rowIterator.next(); // Skip header

        } catch (Exception e) {
            log.error("Failed to initialize ExcelReader", e);
            throw new RuntimeException("Error reading Excel file: " + e.getMessage(), e);
        }
    }

    /**
     * Advances to the next row in the XLSX file.
     *
     * @return true if there is another row, false otherwise
     */
    @Override
    public boolean next() {
        if (rowIterator.hasNext()) {
            currentRow = rowIterator.next();
            return true;
        }
        return false;
    }

    /**
     * Gets the current row as a Spark internal row.
     *
     * @return The current row
     */
    @Override
    public InternalRow get() {
        Object[] values = new Object[schema.length()];
        for (int i = 0; i < schema.length(); i++) {
            Cell cell = currentRow.getCell(i);
            DataType type = fieldTypes[i];
            values[i] = convertCellValue(cell, type);
        }
        return new GenericInternalRow(values);
    }

    /**
     * Converts a cell value to the appropriate Spark data type.
     *
     * @param cell The cell to convert
     * @param type The target Spark data type
     * @return The converted value
     */
    private Object convertCellValue(Cell cell, DataType type) {
        if (cell == null) return null;

        if (cell.getCellType() == CellType.FORMULA) {
            cell = evaluator.evaluateInCell(cell);
        }

        try {
            switch (type.typeName()) {
                case "string":
                    return UTF8String.fromString(cell.toString());
                case "integer":
                    return (int) getNumericValue(cell);
                case "long":
                    return (long) getNumericValue(cell);
                case "double":
                    return getNumericValue(cell);
                case "decimal":
                    return getDecimalValue(cell, (DecimalType) type);
                case "boolean":
                    return getBooleanValue(cell);
                case "date":
                    return getDateValue(cell);
                case "timestamp":
                    return getTimestampValue(cell);
                default:
                    log.warn("Unsupported type '{}', returning string fallback", type.simpleString());
                    return UTF8String.fromString(cell.toString());
            }
        } catch (Exception e) {
            log.error("Error converting cell '{}' to type '{}'", cell.toString(), type.simpleString(), e);
            throw new RuntimeException("Conversion error", e);
        }
    }

    /**
     * Gets a numeric value from a cell.
     *
     * @param cell The cell to get the value from
     * @return The numeric value
     */
    private double getNumericValue(Cell cell) {
        if (cell.getCellType() == CellType.STRING) {
            return Double.parseDouble(cell.getStringCellValue());
        }
        return cell.getNumericCellValue();
    }

    /**
     * Gets a decimal value from a cell with the specified precision and scale.
     *
     * @param cell The cell to get the value from
     * @param type The decimal type with precision and scale information
     * @return The decimal value
     */
    private BigDecimal getDecimalValue(Cell cell, DecimalType type) {
        double value = getNumericValue(cell);
        return BigDecimal.valueOf(value).setScale(type.scale(), BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Gets a boolean value from a cell.
     *
     * @param cell The cell to get the value from
     * @return The boolean value
     */
    private boolean getBooleanValue(Cell cell) {
        if (cell.getCellType() == CellType.STRING) {
            return Boolean.parseBoolean(cell.getStringCellValue());
        }
        return cell.getBooleanCellValue();
    }

    /**
     * Gets a date value from a cell.
     *
     * @param cell The cell to get the value from
     * @return The date value
     * @throws IllegalArgumentException if the cell does not contain a valid date
     */
    private Date getDateValue(Cell cell) {
        if (DateUtil.isCellDateFormatted(cell)) {
            java.util.Date date = cell.getDateCellValue();
            return new Date(date.getTime());
        }
        throw new IllegalArgumentException("Cell is not a valid date: " + cell.toString());
    }

    /**
     * Gets a timestamp value from a cell.
     *
     * @param cell The cell to get the value from
     * @return The timestamp value
     * @throws IllegalArgumentException if the cell does not contain a valid timestamp
     */
    private Timestamp getTimestampValue(Cell cell) {
        if (DateUtil.isCellDateFormatted(cell)) {
            java.util.Date date = cell.getDateCellValue();
            return new Timestamp(date.getTime());
        }
        throw new IllegalArgumentException("Cell is not a valid timestamp: " + cell.toString());
    }

    /**
     * Closes the reader and releases any resources.
     * This method should be called when the reader is no longer needed.
     */
    @Override
    public void close() {
        log.debug("Closing XLSX reader and releasing resources");
        try {
            if (evaluator != null) {
                // Clear any cached formula results
                evaluator.clearAllCachedResultValues();
            }

            // Close the workbook which will also close the sheet
            if (rowIterator != null && rowIterator.hasNext()) {
                // Get the sheet from the current row to close it properly
                Row row = rowIterator.next();
                if (row != null && row.getSheet() != null) {
                    row.getSheet().getWorkbook().close();
                }
            }
        } catch (IOException e) {
            log.warn("Error closing workbook resources", e);
        }
    }
}
