package am.ik.categolj2.api;

import com.google.common.io.Files;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.accept.MappingMediaTypeFileExtensionResolver;

import java.util.Locale;
import java.util.Map;

@Data
public class MediaTypeResolver extends MappingMediaTypeFileExtensionResolver {
    private MediaType fallbackMediaType = MediaType.APPLICATION_OCTET_STREAM;

    public MediaTypeResolver(Map<String, MediaType> mediaTypes) {
        super(mediaTypes);
    }

    public MediaType resolveFromExtension(String extension) {
        Assert.notNull(extension, "extension must not be null");
        MediaType mediaType = super.lookupMediaType(extension.toLowerCase(Locale.ENGLISH));
        if (mediaType == null) {
            mediaType = fallbackMediaType;
        }
        return mediaType;
    }

    public MediaType resolveFromFilename(String fileName) {
        return resolveFromExtension(Files.getFileExtension(fileName));
    }
}
