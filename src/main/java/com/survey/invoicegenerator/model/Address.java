package com.survey.invoicegenerator.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

    private String name;
    private String building;
    private String street;

    private String mobile;

    private String fullAddress;

    public void generateFullAddress() {
        this.fullAddress = name+'\n'+building+'\n'+street+'\n'+mobile;
    }

}
