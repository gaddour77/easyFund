package tn.esprit.easyfund.controllers;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

public class CreditExcelExporter {

    private final XSSFWorkbook workbook;
    private final List<Object> simulation;
    private XSSFSheet sheet;


    public CreditExcelExporter(List<Object> simulation) {
        this.simulation = simulation;
        workbook = new XSSFWorkbook();
    }

    private void createCell(@NotNull Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue(Objects.toString(value, ""));
        }
        cell.setCellStyle(style);
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("MicroCredit");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();

        createCell(row, 0, "Amount Remaining", style);
        createCell(row, 1, "Credit", style);
        createCell(row, 2, "Interest Amount", style);
        createCell(row, 3, "Bill payment (Tax excluded)", style);
        createCell(row, 4, "Bill Payment (Tax Included) (Principal)", style);
    }

    private void writeDataLines() {
        if (simulation == null || simulation.isEmpty())
            return;

        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        int lastIndex = simulation.size() - 1;
        for (int i = 0; i < lastIndex; i++) {
            Object periodData = simulation.get(i);
            if (periodData instanceof Map) {
                Row row = sheet.createRow(rowCount++);
                int columnCount = 0;
                Map<String, Double> periodMap = (Map<String, Double>) periodData;
                for (Map.Entry<String, Double> entry : periodMap.entrySet()) {
                    createCell(row, columnCount++, entry.getValue(), style);
                }
            }
        }

        // Add three blank rows
        for (int i = 0; i < 3; i++) {
            sheet.createRow(rowCount++);
        }

        String[] columnNames = {"Profit from Credit", "Credit Amount", "Interest Rate", "Average Bill Payment", "Credit Returned"};

        // Add columns: Profit from Credit, Credit Amount, Interest Rate, Average Bill Payment, Credit Returned
        Row columnNamesRow = sheet.createRow(rowCount++);
        int columnCount = 0;
        for (String columnName : columnNames) {
            createCell(columnNamesRow, columnCount++, columnName, style);
        }

        Object lastPeriodData = simulation.get(lastIndex);
        if (lastPeriodData instanceof Map) {
            Row lastRow = sheet.createRow(rowCount++);
            Map<String, Double> lastPeriodMap = (Map<String, Double>) lastPeriodData;
            columnCount = 0;
            for (String columnName : columnNames) {
                Double value = lastPeriodMap.get(columnName);
                createCell(lastRow, columnCount++, value, style);
            }
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
        ServletOutputStream outputStream = response.getOutputStream();

        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
