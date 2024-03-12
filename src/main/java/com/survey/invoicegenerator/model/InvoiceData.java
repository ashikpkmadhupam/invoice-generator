package com.survey.invoicegenerator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceData {

    private long invoiceNumber;
    private String date;
    private Address toAddress;

    private List<TableContent> tableContents;
}
