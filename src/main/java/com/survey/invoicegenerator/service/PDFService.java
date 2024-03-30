package com.survey.invoicegenerator.service;


import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.*;
import com.survey.invoicegenerator.exception.PDFException;
import com.survey.invoicegenerator.helper.ITextHelper;
import com.survey.invoicegenerator.model.InvoiceData;
import com.survey.invoicegenerator.model.TableContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PDFService {

    public PdfPCell getCell(String text, int alignment,BaseColor color) {
        PdfPCell cell = new PdfPCell(new Phrase(text,new Font(Font.FontFamily.HELVETICA,12,0,color)));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }


    public ResponseEntity<Resource> generateInvoice(InvoiceData invoiceData) {

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

            com.itextpdf.text.Image img = Image.getInstance("src/main/resources/images/Header1.png");
            img.scaleAbsolute(540f, 110f);
            img.setSpacingAfter(10f);
            img.setAlignment(Element.ALIGN_CENTER);
            document.add(img);

            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            Paragraph invoice = new Paragraph(new Phrase("INVOICE", new Font(Font.FontFamily.HELVETICA, 16, 1, BaseColor.BLACK)));
            invoice.setAlignment(Element.ALIGN_CENTER);
            document.add(invoice);

            PdfPTable tableDate = new PdfPTable(2);
            tableDate.setWidthPercentage(100);
            tableDate.addCell(getCell("No : "+invoiceData.getInvoiceNumber(), PdfPCell.ALIGN_LEFT, BaseColor.RED));
            tableDate.addCell(getCell("Date : "+invoiceData.getDate(), PdfPCell.ALIGN_RIGHT, BaseColor.BLACK));
            document.add(tableDate);

            document.add(Chunk.NEWLINE);

            invoiceData.getToAddress().generateFullAddress();

            Paragraph to = new Paragraph("To :");
            to.setAlignment(Element.ALIGN_LEFT);
            document.add(to);

            Paragraph toAddress = new Paragraph(invoiceData.getToAddress().getFullAddress());
            toAddress.setAlignment(Element.ALIGN_LEFT);
            toAddress.setIndentationLeft(20f);
            document.add(toAddress);

            document.add(Chunk.NEWLINE);

            float[] columnWidths = {10f, 50f, 15f, 10f, 15f};
            String[] tableHeaders = {"Sl No.", "Particulars", "Rate/Acre", "Area", "Amount"};

            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100f);

            for (int i = 0; i < tableHeaders.length; ++i) {
                PdfPCell cell = new PdfPCell(new Phrase(tableHeaders[i], new Font(Font.FontFamily.HELVETICA, 12, 1, BaseColor.BLACK)));
                cell.setBackgroundColor(WebColors.getRGBColor("#979ea8"));
                cell.setPaddingBottom(5f);
                cell.setPaddingLeft(5f);
                table.addCell(cell);
            }



            List<TableContent> tableList = invoiceData.getTableContents();

            calculateAmount(tableList);

            int i = 0;
            double sum = 0;
            for (TableContent content : tableList) {
                sum = sum + content.getAmount();
            }
            tableList.add(TableContent.builder().particulars("Total").amount(sum).build());
            for (TableContent content : tableList) {
                BaseColor color;
                if (i % 2 == 0) {
                    color = WebColors.getRGBColor("#e6eaf0");
                } else {
                    color = WebColors.getRGBColor("#f5f8fc");
                }
                PdfPCell cell;
                if (i != tableList.size() - 1) {
                    cell = new PdfPCell(new Phrase((i + 1) + ""));
                    cell.setBackgroundColor(color);
                    cell.setPaddingBottom(5f);
                    cell.setPaddingLeft(5f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(content.getParticulars()));
                    cell.setBackgroundColor(color);
                    cell.setPaddingBottom(5f);
                    cell.setPaddingLeft(5f);
                    table.addCell(cell);
                } else {
                    cell = new PdfPCell(new Phrase(""));
                    cell.setBackgroundColor(color);
                    cell.setPaddingBottom(5f);
                    cell.setPaddingLeft(5f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(content.getParticulars(), new Font(Font.FontFamily.HELVETICA, 14, 1, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(color);
                    cell.setPaddingBottom(5f);
                    cell.setPaddingLeft(5f);
                    table.addCell(cell);
                }

                cell = new PdfPCell(content.getRate() > 0 ? new Phrase(String.format("%.2f", content.getRate())) : new Phrase(""));
                cell.setBackgroundColor(color);
                cell.setPaddingBottom(5f);
                cell.setPaddingLeft(5f);
                table.addCell(cell);

                cell = new PdfPCell(content.getArea() > 0 ? new Phrase(String.format("%.2f", content.getArea())) : new Phrase(""));
                cell.setBackgroundColor(color);
                cell.setPaddingBottom(5f);
                cell.setPaddingLeft(5f);
                table.addCell(cell);

                if (i != tableList.size() - 1) {
                    cell = new PdfPCell(new Phrase(String.format("%.2f", content.getAmount())));
                    cell.setBackgroundColor(color);
                    cell.setPaddingBottom(5f);
                    cell.setPaddingLeft(5f);
                    table.addCell(cell);
                } else {
                    cell = new PdfPCell(new Phrase(String.format("%.2f", content.getAmount()), new Font(Font.FontFamily.HELVETICA, 14, 1, BaseColor.BLACK)));
                    cell.setBackgroundColor(color);
                    cell.setPaddingBottom(5f);
                    cell.setPaddingRight(9f);
                    table.addCell(cell);
                }

                ++i;
            }

            document.add(table);

            Paragraph sign = new Paragraph("Signature : ");
            sign.setAlignment(Element.ALIGN_RIGHT);
            sign.setIndentationRight(135f);
            sign.setSpacingBefore(80f);
            document.add(sign);

            com.itextpdf.text.Image signature = Image.getInstance("src/main/resources/images/Sign.png");
            signature.scaleAbsolute(100f, 25f);
            signature.setSpacingAfter(5f);
            signature.setAlignment(Element.ALIGN_RIGHT);
            signature.setIndentationRight(40f);
            document.add(signature);

            Paragraph name = new Paragraph("Akhil A");
            name.setAlignment(Element.ALIGN_RIGHT);
            name.setIndentationRight(80f);
            //name.setSpacingBefore(20f);
            document.add(name);

            Paragraph company_name = new Paragraph("Digital Land Survey");
            company_name.setAlignment(Element.ALIGN_RIGHT);
            company_name.setIndentationRight(14f);
            document.add(company_name);

            Paragraph place_name = new Paragraph("Kallikkandy");
            place_name.setAlignment(Element.ALIGN_RIGHT);
            place_name.setIndentationRight(57f);
            document.add(place_name);

            Paragraph cert = new Paragraph("C.No: 72/2010");
            cert.setAlignment(Element.ALIGN_RIGHT);
            cert.setIndentationRight(41f);
            document.add(cert);

            PdfContentByte canvas = writer.getDirectContent();
            CMYKColor magentaColor = new CMYKColor(0.f, 0.f, 0.f, 1.f);
            canvas.setColorStroke(magentaColor);
            canvas.moveTo(30, 63);
            canvas.lineTo(560, 63);
            canvas.closePathStroke();


            ITextHelper itextFooter = new ITextHelper();
            writer.setPageEvent(itextFooter);

            document.close();
            ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=invoice_itext.pdf");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);

        } catch (Exception e){
            log.error(e.getMessage());
            throw new PDFException("Error occurred while generating PDF");
        }
    }

    private void calculateAmount(List<TableContent> tableList) {

        for(TableContent content: tableList) {
            if(content.getAmount()<=0 && content.getRate()>0 && content.getArea()>0) {
                content.setAmount(content.getArea() * content.getRate());
            }
        }
    }


}
