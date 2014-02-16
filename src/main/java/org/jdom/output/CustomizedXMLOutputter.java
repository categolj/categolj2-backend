package org.jdom.output;

public class CustomizedXMLOutputter extends XMLOutputter {
    public CustomizedXMLOutputter() {
        super();
    }

    public CustomizedXMLOutputter(Format format) {
        super(format);
    }

    public CustomizedXMLOutputter(XMLOutputter that) {
        super(that);
    }

    @Override
    public String escapeElementEntities(String str) {
        if (str.startsWith("<![CDATA[") && str.endsWith("]]>")) {
            return str;
        } else {
            return super.escapeElementEntities(str);
        }

    }

}
