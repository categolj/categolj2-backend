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
package am.ik.marked4j;

public class MarkedBuilder {

    /**
     * Enable GitHub flavored markdown.
     */
    private boolean gfm = true;
    /**
     * Enable GFM tables. This option requires the gfm option to be true.
     */
    private boolean tables = true;
    /**
     * Enable GFM line breaks. This option requires the gfm option to be true.
     */
    private boolean breaks = false;
    /**
     * Conform to obscure parts of markdown.pl as much as possible. Don't fix
     * any of the original markdown bugs or poor behavior.
     */
    private boolean pedantic = false;
    /**
     * Sanitize the output. Ignore any HTML that has been input.
     */
    private boolean sanitize = false;
    /**
     * Use smarter list behavior than the original markdown. May eventually be
     * default with the old behavior moved into pedantic.
     */
    private boolean smartLists = true;
    /**
     * Use "smart" typograhic punctuation for things like quotes and dashes.
     */
    private boolean smartypants = false;

    /**
     * Constructor.
     */
    public MarkedBuilder() {
    }

    public MarkedBuilder gfm(boolean gfm) {
        this.gfm = gfm;
        return this;
    }

    public MarkedBuilder tables(boolean tables) {
        this.tables = tables;
        return this;
    }

    public MarkedBuilder breaks(boolean breaks) {
        this.breaks = breaks;
        return this;
    }

    public MarkedBuilder pedantic(boolean pedantic) {
        this.pedantic = pedantic;
        return this;
    }

    public MarkedBuilder sanitize(boolean sanitize) {
        this.sanitize = sanitize;
        return this;
    }

    public MarkedBuilder smartLists(boolean smartLists) {
        this.smartLists = smartLists;
        return this;
    }

    public MarkedBuilder smartypants(boolean smartypants) {
        this.smartypants = smartypants;
        return this;
    }

    public Marked build() {
        if (tables && !gfm) {
            throw new IllegalStateException("'tables' option requires the 'gfm' option to be true.");
        }
        if (breaks && !gfm) {
            throw new IllegalStateException("'breaks' option requires the 'gfm' option to be true.");
        }
        return new Marked(gfm, tables, breaks, pedantic, sanitize, smartLists,
                smartypants);
    }

}
