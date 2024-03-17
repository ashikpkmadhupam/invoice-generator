package com.survey.invoicegenerator.controller;

import com.survey.invoicegenerator.model.InvoiceData;
import com.survey.invoicegenerator.service.PDFService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/invoice/pdf")
@RequiredArgsConstructor
public class Controller {

    private final PDFService pdfService;

    @PostMapping("/generate")
    public ResponseEntity<Resource> generatePDF(@RequestBody InvoiceData invoiceData) {
        return pdfService.generateInvoice(invoiceData);
    }
}
