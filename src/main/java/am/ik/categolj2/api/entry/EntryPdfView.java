package am.ik.categolj2.api.entry;

import am.ik.categolj2.domain.model.Categories;
import am.ik.categolj2.domain.model.Entry;
import am.ik.categolj2.domain.model.EntryFormat;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class EntryPdfView extends AbstractPdfView {
    @Value("${frontend.entry.path.prefix:/#/entries/}")
    String frontEndEntryPathPrefix;
    @Value("${frontend.entry.path.prepend.host:true}")
    boolean frontEndEntryPathPrependHost;

    FontProvider fontProvider = new FontFactoryImp() {
        @Override
        public boolean isRegistered(String fontname) {
            return fontname != null && super.isRegistered(fontname);
        }

        @Override
        public Font getFont(String fontname, String encoding, boolean embedded, float size, int style, Color color) {
            try {
                if (fontname == null) {
                    return new Font(BaseFont.createFont("HeiseiKakuGo-W5", "UniJIS-UCS2-H", BaseFont.NOT_EMBEDDED),
                            size, style, color);
                } else {
                    return super.getFont(fontname, encoding, embedded, size, style, color);
                }
            } catch (DocumentException | IOException e) {
                throw new IllegalStateException(e);
            }
        }
    };
    Pattern RERATIVE_LINK_PATH = Pattern.compile("src=['\"]{0,1}[/]{0,1}api/([a-zA-Z0-9]+)/files/(.+)/(.+)\\.([a-zA-Z]+)['\"]{0,1}");

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter pdfWriter,
                                    HttpServletRequest servletRequest, HttpServletResponse httpServletResponse) throws Exception {
        Entry entry = (Entry) model.get("entry");
        UriComponentsBuilder uriBuilder = (UriComponentsBuilder) model.get("uriComponentsBuilder");
        UriComponents components = uriBuilder.build();
        String base = components.getScheme() + "://" + components.getHost() + ((components.getPort() != -1) ? ":" + components.getPort() : "");
        EntryFormat format = EntryFormat.valueOf(entry.getFormat().toUpperCase());
        String url = (frontEndEntryPathPrependHost ? base : "") + frontEndEntryPathPrefix + entry.getEntryId();
        String html = "<html>" +
                "<body style='font-size:8px;'> " +
                "<table border='1'>" +
                "<tr><th bgcolor='#eee'>Title</th><td colspan='5'>" + entry.getTitle() + "</td></tr>" +
                "<tr><th bgcolor='#eee'>Category</th><td colspan='5'>" + Categories.toString(entry.getCategory()) + "</td></tr>" +
                "<tr><th bgcolor='#eee'>Created at</th><td colspan='2'>" + entry.getCreatedDate() + "</td>" +
                "<th bgcolor='#eee'>Created by</th><td colspan='2'>" + entry.getCreatedBy() + "</td></tr>" +
                "<tr><th bgcolor='#eee'>Updated at</th><td colspan='2'>" + entry.getLastModifiedDate() + "</td>" +
                "<th bgcolor='#eee'>Updated by</th><td colspan='2'>" + entry.getLastModifiedBy() + "</td></tr>" +
                "<tr><th bgcolor='#eee'>Orininal URL</th><td colspan='5'><a href=\"" + url + "\">" + url + "</a></td></tr>" +
                "</table>" +
                format.format(entry.getContents())  +
                "</body></html>";
        String body = RERATIVE_LINK_PATH.matcher(html).replaceAll("src='" + base + "/api/$1/files/$2/$3.$4'");
        HTMLWorker htmlWorker = new HTMLWorker(document);
        HashMap<String, FontProvider> providers = new HashMap<>();
        providers.put("font_factory", fontProvider);
        htmlWorker.setInterfaceProps(providers);
        htmlWorker.parse(new StringReader(body));
        document.addTitle(entry.getTitle());
        document.addAuthor(entry.getLastModifiedBy());
        document.addCreator(entry.getCreatedBy());
    }
}
