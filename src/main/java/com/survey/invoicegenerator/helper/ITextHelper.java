package com.survey.invoicegenerator.helper;


import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ITextHelper extends PdfPageEventHelper{

    public void onEndPage(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Thank you for your valued custom"), 300, 30, 0);
    }

}
