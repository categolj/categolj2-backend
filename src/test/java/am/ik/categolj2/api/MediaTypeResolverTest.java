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
package am.ik.categolj2.api;


import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class MediaTypeResolverTest {
    @Test
    public void testResolve() {
        Map<String, MediaType> map = new HashMap<>();
        map.put("png", MediaType.IMAGE_PNG);
        MediaTypeResolver resolver = new MediaTypeResolver(map);

        assertThat(resolver.resolveFromExtension("png"), is(MediaType.IMAGE_PNG));
        assertThat(resolver.resolveFromExtension("PNG"), is(MediaType.IMAGE_PNG));
        assertThat(resolver.resolveFromExtension("hoge"), is(MediaType.APPLICATION_OCTET_STREAM));
    }

    @Test
    public void testResolveFromFileName() {
        Map<String, MediaType> map = new HashMap<>();
        map.put("png", MediaType.IMAGE_PNG);
        MediaTypeResolver resolver = new MediaTypeResolver(map);

        assertThat(resolver.resolveFromFilename("hoge.png"), is(MediaType.IMAGE_PNG));
        assertThat(resolver.resolveFromFilename("foo.PNG"), is(MediaType.IMAGE_PNG));
        assertThat(resolver.resolveFromFilename("bar.hoge"), is(MediaType.APPLICATION_OCTET_STREAM));
    }
}
