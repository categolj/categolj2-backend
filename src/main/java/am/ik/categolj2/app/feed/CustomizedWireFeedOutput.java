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
