package com.survey.invoicegenerator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableContent {

    private String particulars;
    private double rate;
    private double area;
    private double amount;
}
