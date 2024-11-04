package com.example.tecnosserver.utils;

import lombok.Getter;

@Getter
public enum MainSection {
    ACASA("Acasă"),
    PRODUSE("Produse"),
    APLICATII_TEHNOLOGIE("Aplicații și Tehnologie"),
    PARTENERI("Parteneri");

    private final String label;

    MainSection(String label) {
        this.label = label;
    }

}

