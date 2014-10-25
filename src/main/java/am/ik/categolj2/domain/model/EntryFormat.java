/*
 * Copyright (C) 2014 Toshiaki Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package am.ik.categolj2.domain.model;

import am.ik.categolj2.infra.codelist.CodeListItem;
import am.ik.marked4j.Marked;
import am.ik.marked4j.MarkedBuilder;
import org.asciidoctor.Asciidoctor;

import java.util.Collections;

public enum EntryFormat implements CodeListItem {
    MD("markdown") {
        final Marked marked = new MarkedBuilder().build();

        @Override
        public String format(String contents) {
            return marked.marked(contents);
        }
    },
    ASCIIDOC("asciidoc") {
        final Asciidoctor asciidoctor = Asciidoctor.Factory.create();

        @Override
        public String format(String contents) {
            return asciidoctor.render(contents, Collections.emptyMap());
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
