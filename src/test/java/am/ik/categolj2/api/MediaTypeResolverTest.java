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
