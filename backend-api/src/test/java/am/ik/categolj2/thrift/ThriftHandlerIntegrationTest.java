package am.ik.categolj2.thrift;

import am.ik.categolj2.App;
import am.ik.categolj2.domain.model.Categories;
import am.ik.categolj2.domain.model.Entry;
import am.ik.categolj2.domain.model.Tag;
import am.ik.categolj2.domain.repository.entry.EntryRepository;
import com.google.common.collect.Sets;
import org.apache.http.client.HttpClient;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;

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
    @Inject
    TProtocolFactory protocolFactory;
    @Inject
    HttpClient httpClient;
    TCategolj2.Client client;

    @Autowired
    EntryRepository entryRepository;
    Entry entry1;
    DateTime now = new DateTime();

    @Before
    public void setUp() throws Exception {
        TTransport transport = new THttpClient("https://localhost:" + port + "/thrift", httpClient);
        TProtocol protocol = protocolFactory.getProtocol(transport);
        client = new TCategolj2.Client(protocol);


        // clean data
        entryRepository.deleteAll();
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

        entryRepository.save(Arrays.asList(entry1));
    }

    @Test
    public void testFindOne() throws Exception {
        System.out.println(client.findOne(1));
    }
}