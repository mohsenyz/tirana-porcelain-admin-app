package com.tiranaporcelain.admin.utils;


import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.languages.ArabicLigaturizer;
import com.itextpdf.text.pdf.languages.LanguageProcessor;

/**
 * Created by mphj on 11/20/17.
 */

public class PdfUtils {

    private static BaseFont iranBaseFont;
    private static LanguageProcessor persianProcessor = new ArabicLigaturizer();
    static {
        try{
            iranBaseFont = BaseFont.createFont("assets/fonts/iran.ttf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    static class DefaultFooter extends PdfPageEventHelper {
        String title, pageTitle;

        public DefaultFooter(String title, String pageTitle) {
            this.title = title;
            this.pageTitle = pageTitle;
        }

        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            Phrase footer_poweredBy = new Phrase(asRtl(title), getDefaultFont()); //public static Font FONT_HEADER_FOOTER = new Font(Font.FontFamily.UNDEFINED, 7, Font.ITALIC);
            Phrase footer_pageNumber = new Phrase(asRtl(pageTitle + " " + LocaleUtils.englishNumberToArabic("" + document.getPageNumber())));
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                    footer_pageNumber,
                    (document.getPageSize().getWidth() - 10),
                    document.bottom() - 10, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    footer_poweredBy, (document.right() - document.left()) / 2
                            + document.leftMargin(), document.bottom() - 10, 0);
        }
    }




    public static String asRtl(String text) {
        return persianProcessor.process(text);
    }

    public static PdfPCell ubCell(String text) {
        PdfPCell pdfPCell = new PdfPCell(new Phrase(asRtl(text), getDefaultFont()));
        pdfPCell.setBorder(Rectangle.NO_BORDER);
        return pdfPCell;
    }

    public static Font getDefaultFont(int size) {
        return new Font(iranBaseFont, size);
    }

    public static Font getDefaultFont() {
        return getDefaultFont(12);
    }


    public static Font getDefaultFont(int size, int style) {
        return new Font(iranBaseFont, size, style);
    }


    public static void addEmptyLine(Paragraph document, int count) throws Exception{
        for (int i = 0; i < count; i++) {
            document.add(Chunk.NEWLINE);
        }
    }


    public static PdfPTable getDefaultTable(float[] columnsWidth, String[] header, String[][] body) {
        PdfPTable table = new PdfPTable(columnsWidth);

        table.setWidthPercentage(100);
        table.getDefaultCell().setUseAscender(true);

        for (String head : header) {
            PdfPCell cell = new PdfPCell(new Phrase(asRtl(head),
                    getDefaultFont()));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new GrayColor(0.75f));
            cell.setFixedHeight(30);
            table.addCell(cell);
        }

        for (String[] row : body) {
            for (String cellData : row) {
                PdfPCell cell = new PdfPCell(new Phrase(asRtl(cellData),
                        getDefaultFont()));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setFixedHeight(30);
                table.addCell(cell);
            }
        }
        return table;
    }
}
