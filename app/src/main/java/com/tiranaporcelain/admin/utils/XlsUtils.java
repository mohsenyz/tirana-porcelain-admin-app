package com.tiranaporcelain.admin.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Created by mphj on 11/20/17.
 */

public class XlsUtils {

    public static void getDefaultTable(Sheet sheet, float[] columnsWidth, String[] header, String[][] body, CellStyle cellStyle) {
        Row headRow = sheet.createRow(0);
        Cell c = null;
        for (int i = 0; i < header.length; i++) {
            c = headRow.createCell(i);
            c.setCellStyle(cellStyle);
            c.setCellValue(header[i]);
        }

        for (int i = 0; i < body.length; i++) {
            String[] row = body[i];
            Row tRow = sheet.createRow(i + 1);
            for (int b = 0; b < row.length; b++) {
                c = tRow.createCell(b);
                c.setCellValue(row[b]);
            }
        }
        for (int i = 0; i < columnsWidth.length; i++) {
            sheet.setColumnWidth(i, (int)(columnsWidth[i] * 1000));
        }
    }
}
