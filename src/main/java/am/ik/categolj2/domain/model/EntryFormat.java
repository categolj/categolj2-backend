package am.ik.categolj2.domain.model;

import am.ik.categolj2.infra.codelist.CodeListItem;
import am.ik.marked4j.Marked;
import am.ik.marked4j.MarkedBuilder;

public enum EntryFormat implements CodeListItem {
    MD("markdown") {
        final Marked marked = new MarkedBuilder().build();

        @Override
        public String format(String contents) {
            return marked.marked(contents);
        }
    }, HTML("html") {
        @Override
        public String format(String contents) {
            return contents;
        }
    };

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

    public abstract String format(String contents);

}
