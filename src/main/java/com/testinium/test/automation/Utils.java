package com.testinium.test.automation;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Utils {
    public static String encodeUri(String value) {
        try {
            URI uri= new URI(value);

            return uri.toASCIIString();
        }catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void exelWrite(String productName,String productPass) throws IOException, WriteException {
        new File("product.xls").delete();
        WritableWorkbook workbook = Workbook.createWorkbook(new File("product.xls"));
        WritableSheet sheet = workbook.createSheet("Product", 0);

        WritableCellFormat headerFormat = new WritableCellFormat();
        WritableFont font
                = new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD);
        headerFormat.setFont(font);
        headerFormat.setBackground(Colour.LIGHT_BLUE);
        headerFormat.setWrap(true);

        WritableCellFormat bodyFormat = new WritableCellFormat();
        bodyFormat.setWrap(true);

        Label headerLabel = new Label(0, 0, "Ürün Adı", headerFormat);
        sheet.setColumnView(0, 60);
        sheet.addCell(headerLabel);

        headerLabel = new Label(1, 0, "Ürün Fiyatı", headerFormat);
        sheet.setColumnView(0, 40);
        sheet.addCell(headerLabel);

        Label bodyCell = new Label(0, 1, productName, bodyFormat);
        sheet.setColumnView(1, 60);
        sheet.addCell(bodyCell);

        bodyCell = new Label(1, 1, productPass, bodyFormat);
        sheet.setColumnView(1, 40);
        sheet.addCell(bodyCell);

        workbook.write();
        workbook.close();
    }
}
