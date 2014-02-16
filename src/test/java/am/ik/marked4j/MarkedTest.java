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

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class MarkedTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        String md = "- foo\n" +
                "- bar\n" +
                "- baz";
        String expected = "<ul>\n" +
                "<li>foo</li>\n" +
                "<li>bar</li>\n" +
                "<li>baz</li>\n" +
                "</ul>\n";
        Marked marked = new Marked();
        assertThat(marked.marked(md), is(expected));
    }

    @Test
    public void testHighlight() {
        String md = " ```javascript\n" +
                " var s = \"JavaScript syntax highlighting\";\n" +
                " alert(s);\n" +
                " ```\n" +
                " \n" +
                " ```python\n" +
                " s = \"Python syntax highlighting\"\n" +
                " print s\n" +
                " ```\n" +
                " \n" +
                " ```\n" +
                " No language indicated, so no syntax highlighting. \n" +
                " But let's throw in a <b>tag</b>.\n" +
                " ```";
        String expected = "<pre><code class=\"lang-javascript\"> var s = &quot;JavaScript syntax highlighting&quot;;\n" +
                " alert(s);</code></pre>\n" +
                "<pre><code class=\"lang-python\"> s = &quot;Python syntax highlighting&quot;\n" +
                " print s</code></pre>\n" +
                "<pre><code> No language indicated, so no syntax highlighting. \n" +
                " But let&#39;s throw in a &lt;b&gt;tag&lt;/b&gt;.</code></pre>\n";
        Marked marked = new MarkedBuilder().gfm(true).build();
        assertThat(marked.marked(md), is(expected));
    }

    @Test
    public void testEnableTable() {
        String md = "Colons can be used to align columns.\n" +
                "\n" +
                "| Tables        | Are           | Cool  |\n" +
                "| ------------- |:-------------:| -----:|\n" +
                "| col 3 is      | right-aligned | $1600 |\n" +
                "| col 2 is      | centered      |   $12 |\n" +
                "| zebra stripes | are neat      |    $1 |\n" +
                "\n" +
                "The outer pipes (|) are optional, and you don't need to make the raw Markdown line up prettily. You can also use inline Markdown.\n" +
                "\n" +
                "Markdown | Less | Pretty\n" +
                "--- | --- | ---\n" +
                "*Still* | `renders` | **nicely**\n" +
                "1 | 2 | 3";
        String expected = "<p>Colons can be used to align columns.</p>\n" +
                "<table>\n" +
                "<thead>\n" +
                "<tr>\n" +
                "<th>Tables</th>\n" +
                "<th style=\"text-align:center\">Are</th>\n" +
                "<th style=\"text-align:right\">Cool</th>\n" +
                "</tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td>col 3 is</td>\n" +
                "<td style=\"text-align:center\">right-aligned</td>\n" +
                "<td style=\"text-align:right\">$1600</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>col 2 is</td>\n" +
                "<td style=\"text-align:center\">centered</td>\n" +
                "<td style=\"text-align:right\">$12</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>zebra stripes</td>\n" +
                "<td style=\"text-align:center\">are neat</td>\n" +
                "<td style=\"text-align:right\">$1</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<p>The outer pipes (|) are optional, and you don&#39;t need to make the raw Markdown line up prettily. You can also use inline Markdown.</p>\n" +
                "<table>\n" +
                "<thead>\n" +
                "<tr>\n" +
                "<th>Markdown</th>\n" +
                "<th>Less</th>\n" +
                "<th>Pretty</th>\n" +
                "</tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td><em>Still</em></td>\n" +
                "<td><code>renders</code></td>\n" +
                "<td><strong>nicely</strong></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>1</td>\n" +
                "<td>2</td>\n" +
                "<td>3</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n";

        Marked marked = new MarkedBuilder().tables(true).build();
        assertThat(marked.marked(md), is(expected));
    }

    @Test
    public void testDisableTable() {
        String md = "Colons can be used to align columns.\n" +
                "\n" +
                "| Tables        | Are           | Cool  |\n" +
                "| ------------- |:-------------:| -----:|\n" +
                "| col 3 is      | right-aligned | $1600 |\n" +
                "| col 2 is      | centered      |   $12 |\n" +
                "| zebra stripes | are neat      |    $1 |\n" +
                "\n" +
                "The outer pipes (|) are optional, and you don't need to make the raw Markdown line up prettily. You can also use inline Markdown.\n" +
                "\n" +
                "Markdown | Less | Pretty\n" +
                "--- | --- | ---\n" +
                "*Still* | `renders` | **nicely**\n" +
                "1 | 2 | 3";
        String expected = "<p>Colons can be used to align columns.</p>\n" +
                "<p>| Tables        | Are           | Cool  |\n" +
                "| ------------- |:-------------:| -----:|\n" +
                "| col 3 is      | right-aligned | $1600 |\n" +
                "| col 2 is      | centered      |   $12 |\n" +
                "| zebra stripes | are neat      |    $1 |</p>\n" +
                "<p>The outer pipes (|) are optional, and you don&#39;t need to make the raw Markdown line up prettily. You can also use inline Markdown.</p>\n" +
                "<p>Markdown | Less | Pretty\n" +
                "--- | --- | ---\n" +
                "<em>Still</em> | <code>renders</code> | <strong>nicely</strong>\n" +
                "1 | 2 | 3</p>\n";

        Marked marked = new MarkedBuilder().tables(false).build();
        assertThat(marked.marked(md), is(expected));
    }

    @Test
    public void testEnableSanitize() {
        String md = "<ul>" +
                "<li>foo</li>" +
                "<li>bar</li>" +
                "<li>baz</li>" +
                "</ul>";
        String expected = "<p>&lt;ul&gt;" +
                "&lt;li&gt;foo&lt;/li&gt;" +
                "&lt;li&gt;bar&lt;/li&gt;" +
                "&lt;li&gt;baz&lt;/li&gt;" +
                "&lt;/ul&gt;" +
                "</p>\n";
        Marked marked = new MarkedBuilder().sanitize(true).build();
        //System.out.println(marked.marked(md));
        assertThat(marked.marked(md), is(expected));
    }

    @Test
    @Ignore("Java8 got <p><ul><li>foo</li><li>bar</li><li>baz</li></ul></p>\n")
    public void testDisableSanitize() {
        String md = "<ul>" +
                "<li>foo</li>" +
                "<li>bar</li>" +
                "<li>baz</li>" +
                "</ul>";
        String expected = "<ul>" +
                "<li>foo</li>" +
                "<li>bar</li>" +
                "<li>baz</li>" +
                "</ul>";
        Marked marked = new MarkedBuilder().sanitize(false).build();
        assertThat(marked.marked(md), is(expected));
    }

    @Test
    public void testEnableBreaks() {
        String md = "The point of marked was to create a markdown compiler where it was possible to frequently parse huge chunks of markdown without having to worry about caching the compiled output somehow...or blocking for an unnecesarily long time.\n" +
                "marked is very concise and still implements all markdown features. It is also now fully compatible with the client-side.\n" +
                "marked more or less passes the official markdown test suite in its entirety. This is important because a surprising number of markdown compilers cannot pass more than a few tests. It was very difficult to get marked as compliant as it is. It could have cut corners in several areas for the sake of performance, but did not in order to be exactly what you expect in terms of a markdown rendering. In fact, this is why marked could be considered at a disadvantage in the benchmarks above.\n" +
                "Along with implementing every markdown feature, marked also implements GFM features.";
        String expected = "<p>The point of marked was to create a markdown compiler where it was possible to frequently parse huge chunks of markdown without having to worry about caching the compiled output somehow...or blocking for an unnecesarily long time.<br>marked is very concise and still implements all markdown features. It is also now fully compatible with the client-side.<br>marked more or less passes the official markdown test suite in its entirety. This is important because a surprising number of markdown compilers cannot pass more than a few tests. It was very difficult to get marked as compliant as it is. It could have cut corners in several areas for the sake of performance, but did not in order to be exactly what you expect in terms of a markdown rendering. In fact, this is why marked could be considered at a disadvantage in the benchmarks above.<br>Along with implementing every markdown feature, marked also implements GFM features.</p>\n";
        Marked marked = new MarkedBuilder().breaks(true).build();
        assertThat(marked.marked(md), is(expected));
    }

    @Test
    public void testDisableBreaks() {
        String md = "The point of marked was to create a markdown compiler where it was possible to frequently parse huge chunks of markdown without having to worry about caching the compiled output somehow...or blocking for an unnecesarily long time.\n" +
                "marked is very concise and still implements all markdown features. It is also now fully compatible with the client-side.\n" +
                "marked more or less passes the official markdown test suite in its entirety. This is important because a surprising number of markdown compilers cannot pass more than a few tests. It was very difficult to get marked as compliant as it is. It could have cut corners in several areas for the sake of performance, but did not in order to be exactly what you expect in terms of a markdown rendering. In fact, this is why marked could be considered at a disadvantage in the benchmarks above.\n" +
                "Along with implementing every markdown feature, marked also implements GFM features.";
        String expected = "<p>The point of marked was to create a markdown compiler where it was possible to frequently parse huge chunks of markdown without having to worry about caching the compiled output somehow...or blocking for an unnecesarily long time.\n" +
                "marked is very concise and still implements all markdown features. It is also now fully compatible with the client-side.\n" +
                "marked more or less passes the official markdown test suite in its entirety. This is important because a surprising number of markdown compilers cannot pass more than a few tests. It was very difficult to get marked as compliant as it is. It could have cut corners in several areas for the sake of performance, but did not in order to be exactly what you expect in terms of a markdown rendering. In fact, this is why marked could be considered at a disadvantage in the benchmarks above.\n" +
                "Along with implementing every markdown feature, marked also implements GFM features.</p>\n";
        Marked marked = new MarkedBuilder().breaks(false).build();
        //System.out.println(marked.marked(md));
        assertThat(marked.marked(md), is(expected));
    }
}
