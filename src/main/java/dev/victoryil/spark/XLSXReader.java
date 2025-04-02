package dev.victoryil.spark;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class XLSXReader implements PartitionReader<InternalRow> {
    private static final Logger logger = LoggerFactory.getLogger(XLSXReader.class);

    private final StructType schema;
    private final Iterator<Row> rowIterator;
    private final FormulaEvaluator evaluator;
    private final DataType[] fieldTypes;
    private Row currentRow;

    public XLSXReader(StructType schema, Map<String, String> options) {
        this.schema = schema;
        this.fieldTypes = Arrays.stream(schema.fields())
                .map(StructField::dataType)
                .toArray(DataType[]::new);
        try {
            String path = options.get("path");
            logger.info("Reading Excel file from path: {}", path);

            FileInputStream fis = new FileInputStream(new File(path));
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            this.rowIterator = sheet.iterator();
            this.evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            if (rowIterator.hasNext()) rowIterator.next(); // Skip header

        } catch (Exception e) {
            logger.error("Failed to initialize ExcelReader", e);
            throw new RuntimeException("Error reading Excel file: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean next() {
        if (rowIterator.hasNext()) {
            currentRow = rowIterator.next();
            return true;
        }
        return false;
    }

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
                    logger.warn("Unsupported type '{}', returning string fallback", type.simpleString());
                    return UTF8String.fromString(cell.toString());
            }
        } catch (Exception e) {
            logger.error("Error converting cell '{}' to type '{}'", cell.toString(), type.simpleString(), e);
            throw new RuntimeException("Conversion error", e);
        }
    }

    private double getNumericValue(Cell cell) {
        if (cell.getCellType() == CellType.STRING) {
            return Double.parseDouble(cell.getStringCellValue());
        }
        return cell.getNumericCellValue();
    }

    private BigDecimal getDecimalValue(Cell cell, DecimalType type) {
        double value = getNumericValue(cell);
        return BigDecimal.valueOf(value).setScale(type.scale(), BigDecimal.ROUND_HALF_UP);
    }

    private boolean getBooleanValue(Cell cell) {
        if (cell.getCellType() == CellType.STRING) {
            return Boolean.parseBoolean(cell.getStringCellValue());
        }
        return cell.getBooleanCellValue();
    }

    private Date getDateValue(Cell cell) {
        if (DateUtil.isCellDateFormatted(cell)) {
            java.util.Date date = cell.getDateCellValue();
            return new Date(date.getTime());
        }
        throw new IllegalArgumentException("Cell is not a valid date: " + cell.toString());
    }

    private Timestamp getTimestampValue(Cell cell) {
        if (DateUtil.isCellDateFormatted(cell)) {
            java.util.Date date = cell.getDateCellValue();
            return new Timestamp(date.getTime());
        }
        throw new IllegalArgumentException("Cell is not a valid timestamp: " + cell.toString());
    }

    @Override
    public void close() {
        // You can release resources here if needed
    }
}
