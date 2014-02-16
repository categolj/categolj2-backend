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
package am.ik.categolj2.api.file;

import am.ik.categolj2.api.MediaTypeResolver;
import org.joda.time.DateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.concurrent.TimeUnit;

public class HttpHeadersBuilder {
    private final HttpHeaders headers = new HttpHeaders();
    private final MediaTypeResolver mediaTypeResolver;

    public HttpHeadersBuilder(MediaTypeResolver mediaTypeResolver) {
        this.mediaTypeResolver = mediaTypeResolver;
    }

    public HttpHeadersBuilder contentTypeForceAttachment(String fileName) {
        MediaType mediaType = mediaTypeResolver.resolveFromFilename(fileName);
        headers.setContentType(mediaType);
        headers.setContentDispositionFormData("attachment", fileName);
        return this;
    }

    public HttpHeadersBuilder contentTypeAttachmentIfNeccessary(String fileName) {
        MediaType mediaType = mediaTypeResolver.resolveFromFilename(fileName);
        headers.setContentType(mediaType);
        if (MediaType.APPLICATION_OCTET_STREAM.equals(mediaType)) {
            // set attachment only in case of application/octet-stream
            headers.setContentDispositionFormData("attachment", fileName);
        }
        return this;
    }

    public HttpHeadersBuilder cacheForSeconds(int seconds, boolean mustRevalidate) {
        // HTTP 1.0 header
        headers.setExpires(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds));
        // HTTP 1.1 header
        String headerValue = "private, max-age=" + seconds;
        if (mustRevalidate) {
            headerValue += ", must-revalidate";
        }
        headers.setCacheControl(headerValue);
        return this;
    }

    public HttpHeadersBuilder preventCaching() {
        headers.setPragma("no-cache");
        // HTTP 1.0 header
        headers.setExpires(1L);
        // HTTP 1.1 header: "no-cache" is the standard value,
        // "no-store" is necessary to prevent caching on FireFox.
        headers.setCacheControl("no-cache, no-store");
        return this;
    }

    public HttpHeadersBuilder lastModified(DateTime lastModifiedDate) {
        headers.setLastModified(lastModifiedDate.getMillis());
        return this;
    }

    public HttpHeaders build() {
        return headers;
    }
}
