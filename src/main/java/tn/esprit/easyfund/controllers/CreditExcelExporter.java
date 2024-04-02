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

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.util.List;


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

    public void exportExcel(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
        ServletOutputStream outputStream = response.getOutputStream();

        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    public void exportPDF(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            PdfPTable dataTable = createDataTable();
            PdfPTable summaryTable = createSummaryTable();

            document.add(new Paragraph("Credit Simulation Data"));
            Paragraph spacer = new Paragraph("");
            spacer.setSpacingBefore(20f); // Adjust spacing as needed
            spacer.setSpacingBefore(20f); // Adjust spacing as needed
            document.add(spacer);
            document.add(dataTable);

            document.add(new Paragraph("Summary"));
            spacer.setSpacingBefore(20f); // Adjust spacing as needed
            spacer.setSpacingBefore(20f); // Adjust spacing as needed

            document.add(spacer);

            document.add(summaryTable);

        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    private PdfPTable createDataTable() {
        PdfPTable table = new PdfPTable(5);
        addTableHeader(table);
        int lastIndex = simulation.size() - 1;
        for (int i = 0; i < lastIndex; i++) {
            Object rowData = simulation.get(i);
            if (rowData instanceof Map) {
                Map<String, Double> dataMap = (Map<String, Double>) rowData;
                for (Map.Entry<String, Double> entry : dataMap.entrySet()) {
                    table.addCell(entry.getValue().toString());
                }
            }
        }
        return table;
    }

    private void addTableHeader(PdfPTable table) {
        table.addCell("Amount Remaining");
        table.addCell("Credit");
        table.addCell("Interest Amount");
        table.addCell("Bill payment (Tax excluded)");
        table.addCell("Bill Payment (Tax Included) (Principal)");
    }

    private PdfPTable createSummaryTable() {
        PdfPTable table = new PdfPTable(2);
        table.addCell("Summary Data");
        table.addCell("Value");

        if (!simulation.isEmpty()) {
            Object lastRow = simulation.get(simulation.size() - 1);
            if (lastRow instanceof Map) {
                Map<String, Double> dataMap = (Map<String, Double>) lastRow;
                for (Map.Entry<String, Double> entry : dataMap.entrySet()) {
                    table.addCell(entry.getKey());
                    table.addCell(entry.getValue().toString());
                }
            }
        }

        return table;
    }

    // invoice generator


}
