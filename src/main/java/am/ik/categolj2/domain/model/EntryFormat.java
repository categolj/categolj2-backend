package am.ik.categolj2.domain.model;

import am.ik.categolj2.infra.codelist.CodeListItem;

public enum EntryFormat implements CodeListItem {
    MD("markdown"), HTML("html");

    private final String label;

    private EntryFormat(String label) {
        this.label = label;
    }

    @Override
    public String getCodeLabel() {
        return label;
    }

    @Override
    public String getCodeValue() {
        return name().toLowerCase();
    }

}
