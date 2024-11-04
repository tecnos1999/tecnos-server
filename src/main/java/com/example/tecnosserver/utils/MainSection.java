package com.example.tecnosserver.utils;

public enum MainSection {
    ACASA("Acasă"),
    PRODUSE("Produse"),
    APLICATII_TEHNOLOGIE("Aplicații și Tehnologie"),
    PARTENERI("Parteneri");

    private final String label;

    MainSection(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

