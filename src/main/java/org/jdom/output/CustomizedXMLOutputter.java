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
