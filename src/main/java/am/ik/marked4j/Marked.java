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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

public class Marked {
    private final Invocable invocableEngine;
    private final Object marked4j;

    public Marked(boolean gfm, boolean tables, boolean breaks,
                  boolean pedantic, boolean sanitize, boolean smartLists,
                  boolean smartypants) {
        this();
        ScriptEngine engine = (ScriptEngine) invocableEngine;
        try {
            String options = String.format("{\"gfm\": %s,\"tables\": %s,\"breaks\": %s,\"pedantic\": %s,\"sanitize\": %s,\"smartLists\": %s,\"smartypants\": %s}",
                    gfm, tables, breaks, pedantic, sanitize, smartLists, smartypants);
            invocableEngine.invokeMethod(marked4j, "setOptions", options);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new IllegalStateException("invalid script!", e);
        }
    }

    public Marked() {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        this.invocableEngine = (Invocable) engine;
        try (InputStream strm = Marked.class.getClassLoader()
                .getResourceAsStream(
                        "META-INF/resources/assets/marked/lib/marked.js")) {
            String js = copyToString(strm, StandardCharsets.UTF_8);
            Bindings bindings = new SimpleBindings(); // todo
            this.marked4j = engine
                    .eval(js + ";" +
                            "Marked4J = function(marked){this.marked = marked};" +
                            "Marked4J.prototype.setOptions = function(options) {" +
                            "   var opts = JSON.parse(options);" +
                            "   this.marked.setOptions(opts);" +
                            "};" +
                            "new Marked4J(marked);",
                            bindings);
        } catch (IOException e) {
            throw new IllegalStateException("marked.js is not found.", e);
        } catch (ScriptException e) {
            throw new IllegalStateException("invalid script!", e);
        }
    }

    public String marked(String markdownText) {
        try {
            Object result = this.invocableEngine.invokeMethod(marked4j,
                    "marked", markdownText);
            return result == null ? null : result.toString();
        } catch (NoSuchMethodException | ScriptException e) {
            throw new IllegalArgumentException(
                    "Cannot parse the given markdown text!", e);
        }
    }

    private static String copyToString(InputStream in, Charset charset)
            throws IOException {
        StringBuilder out = new StringBuilder();
        try (InputStreamReader reader = new InputStreamReader(in, charset);) {
            char[] buffer = new char[4096];
            int bytesRead = -1;
            while ((bytesRead = reader.read(buffer)) != -1) {
                out.append(buffer, 0, bytesRead);
            }
            return out.toString();
        }
    }

}
