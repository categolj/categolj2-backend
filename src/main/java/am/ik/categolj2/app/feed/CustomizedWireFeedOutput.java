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
package am.ik.categolj2.app.feed;

import java.io.IOException;
import java.io.Writer;

import org.jdom.Document;
import org.jdom.output.CustomizedXMLOutputter;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.sun.syndication.feed.WireFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.WireFeedOutput;

public class CustomizedWireFeedOutput extends WireFeedOutput {
    @Override
    public void output(WireFeed feed, Writer writer, boolean prettyPrint)
            throws IllegalArgumentException, IOException, FeedException {
        Document doc = outputJDom(feed);
        String encoding = feed.getEncoding();
        Format format = prettyPrint ? Format.getPrettyFormat() : Format
                .getCompactFormat();
        if (encoding != null) {
            format.setEncoding(encoding);
        }
        XMLOutputter outputter = new CustomizedXMLOutputter(format);
        outputter.output(doc, writer);
    }

    @Override
    public String outputString(WireFeed feed, boolean prettyPrint)
            throws IllegalArgumentException, FeedException {
        Document doc = outputJDom(feed);
        String encoding = feed.getEncoding();
        Format format = prettyPrint ? Format.getPrettyFormat() : Format
                .getCompactFormat();
        if (encoding != null) {
            format.setEncoding(encoding);
        }
        XMLOutputter outputter = new CustomizedXMLOutputter(format);
        return outputter.outputString(doc);
    }
}
