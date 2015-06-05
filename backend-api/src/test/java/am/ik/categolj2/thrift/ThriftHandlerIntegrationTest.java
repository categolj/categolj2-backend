package am.ik.categolj2.thrift;

import am.ik.categolj2.App;
import am.ik.categolj2.core.message.MessageKeys;
import am.ik.categolj2.domain.model.Categories;
import am.ik.categolj2.domain.model.Entry;
import am.ik.categolj2.domain.model.Link;
import am.ik.categolj2.domain.model.Tag;
import am.ik.categolj2.domain.repository.entry.EntryRepository;
import am.ik.categolj2.domain.repository.link.LinkRepository;
import com.google.common.collect.Sets;
import org.apache.http.client.HttpClient;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
@IntegrationTest({"server.port:0",
        "server-http.port:0",
        "spring.datasource.url:jdbc:h2:mem:bookmark;DB_CLOSE_ON_EXIT=FALSE",
        "flyway.enabled:false",
        "spring.jpa.hibernate.generate-ddl:true",
        "spring.jpa.hibernate.ddl-auto:create-drop",
        "endpoints.autoconfig.enabled:false",
        "endpoints.beans.enabled:false",
        "endpoints.configprops.enabled:false",
        "endpoints.dump.enabled:false",
        "endpoints.env.enabled:false",
        "endpoints.health.enabled:false",
        "endpoints.metrics.enabled:false",
        "endpoints.shutdown.enabled:false",
        "endpoints.trace.enabled:false",
        "endpoints.jmx.enabled:false"})
public class ThriftHandlerIntegrationTest {
    @Value("${local.server.port}")
    int port;
    @Autowired
    TProtocolFactory protocolFactory;
    @Autowired
    HttpClient httpClient;
    TCategolj2.Client client;

    @Autowired
    EntryRepository entryRepository;
    @Autowired
    LinkRepository linkRepository;
    // Test Data
    Entry entry1;
    Entry entry2;
    Link link1;
    Link link2;

    DateTime now = new DateTime();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        TTransport transport = new THttpClient("https://localhost:" + port + "/thrift", httpClient);
        TProtocol protocol = protocolFactory.getProtocol(transport);
        client = new TCategolj2.Client(protocol);

        // clean data
        entryRepository.deleteAll();
        linkRepository.deleteAll();

        // setup data
        entry1 = new Entry(null, "This is entry1!", "**Hello World1!**", "md", Arrays.asList(), true, Arrays.asList(), Collections.<Tag>emptySet());
        entry1.setCreatedBy("admin");
        entry1.setCreatedDate(now);
        entry1.setLastModifiedBy("admin");
        entry1.setLastModifiedDate(now);
        entry1 = entryRepository.saveAndFlush(entry1);
        entry1.setCategory(Categories.fromCategory("aa::bb::cc").getCategories());
        entry1.getCategory().stream().forEach(c -> c.getCategoryPK().setEntryId(entry1.getEntryId()));
        entry1.setTags(Sets.newHashSet(new Tag("Java"), new Tag("Spring")));

        entry2 = new Entry(null, "This is entry2!", "**Hello World2!**", "md", Arrays.asList(), true, Arrays.asList(), Collections.<Tag>emptySet());
        entry2.setCreatedBy("admin");
        entry2.setCreatedDate(now);
        entry2.setLastModifiedBy("admin");
        entry2.setLastModifiedDate(now);
        entry2 = entryRepository.saveAndFlush(entry2);
        entry2.setCategory(Categories.fromCategory("aa::bb::cc").getCategories());
        entry2.getCategory().stream().forEach(c -> c.getCategoryPK().setEntryId(entry2.getEntryId()));
        entry2.setTags(Sets.newHashSet(new Tag("Java"), new Tag("Spring")));

        link1 = new Link("https://google.com", "Google");
        link2 = new Link("https://twitter.com", "Twitter");

        entryRepository.save(Arrays.asList(entry1, entry2));
        linkRepository.save(Arrays.asList(link1, link2));
        entryRepository.flush();
    }

    @Test
    public void testFindAllPublishedUpdatedRecently() throws Exception {
        List<TEntry> result = client.findAllPublishedUpdatedRecently();
        assertThat(result, hasSize(2));
        TEntry e1 = result.get(0);
        TEntry e2 = result.get(1);
        assertThat(e2.getEntryId(), is(entry1.getEntryId()));
        assertThat(e2.getTitle(), is(entry1.getTitle()));
        assertThat(e1.getEntryId(), is(entry2.getEntryId()));
        assertThat(e1.getTitle(), is(entry2.getTitle()));
    }

    @Test
    public void testFindOnePublishedEntry_NotFound() throws Exception {
        expectedException.expect(TCategolj2ClientException.class);
        expectedException.expect(hasProperty("errorCode", is(MessageKeys.E_CT_EN_8201)));
        expectedException.expect(hasProperty("errorMessage", is("The requested entry is not found. [entryId=" + Integer.MAX_VALUE + "]")));
        client.findOnePublishedEntry(Integer.MAX_VALUE);
    }

    @Test
    public void testFindOnePublishedEntry() throws Exception {
        TEntry result = client.findOnePublishedEntry(entry1.getEntryId());
        assertThat(result.getEntryId(), is(entry1.getEntryId()));
        assertThat(result.getTitle(), is(entry1.getTitle()));
        assertThat(result.getContents(), is(entry1.getContents()));
    }

    @Test
    public void testFindAllLinks() throws Exception {
        List<TLink> result = client.findAllLinks();
        TLink tLink1 = new TLink();
        tLink1.setUrl(link1.getUrl());
        tLink1.setLinkName(link1.getLinkName());
        TLink tLink2 = new TLink();
        tLink2.setUrl(link2.getUrl());
        tLink2.setLinkName(link2.getLinkName());

        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(2));
        assertThat(result, hasItems(tLink1, tLink2));
    }
}