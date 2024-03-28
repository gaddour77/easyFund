package tn.esprit.easyfund.controllers;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

public class CreditExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private Hashtable<String, Double> simulation;
    int period;

    public CreditExcelExporter(List<Object> simulation, int period) {
        this.simulation =(Hashtable<String, Double>) simulation;
        this.period = period;
        workbook = new XSSFWorkbook();
    }

    private void createCell(@NotNull Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("MicroCredit");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        style.setFont(font);

        createCell(row, 0, "Period", style);
        createCell(row, 1, "CRD", style);
        createCell(row, 2, "Mensuality", style);
        createCell(row, 3, "Interest", style);
        createCell(row, 4, "Principal", style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(12);
        style.setFont(font);
        //Iterator itr = simulation.keySet().iterator();
        //System.out.println(simulation.size());
        for (int i=1;i<=period;i++) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            //System.out.println("crd "+i+" : " +simulation.get("crd"+i));
            createCell(row, columnCount++, i, style);
            createCell(row, columnCount++, simulation.get("CRD"+i).toString(), style);
            createCell(row, columnCount++, simulation.get("Mensuality").toString(), style);
            createCell(row, columnCount++, simulation.get("I"+i).toString(), style);
            createCell(row, columnCount++, simulation.get("P"+i).toString(), style);
        }
    }

    public void export(@NotNull HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
        ServletOutputStream outputStream = response.getOutputStream();

        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }


}
