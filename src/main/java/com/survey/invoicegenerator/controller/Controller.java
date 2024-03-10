package com.survey.invoicegenerator.controller;


import com.itextpdf.text.DocumentException;
import com.survey.invoicegenerator.service.PDFService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/invoice/pdf")
@RequiredArgsConstructor
public class Controller {

    private final PDFService pdfService;

    @GetMapping("/generate")
    public ResponseEntity<Resource> generateItext() throws DocumentException, IOException {
        return pdfService.generateInvoiceIText();
    }
}
