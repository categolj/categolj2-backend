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
package am.ik.categolj2.app.feed;

import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import am.ik.categolj2.domain.model.EntryFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;

@Component
public class RssEntryFeedView extends AbstractView {
    @Value("${categolj2.title}")
    private String feedTitle;
    @Value("${categolj2.url}")
    private String feedLink;
    @Value("${categolj2.description}")
    private String feedDescription;

    public RssEntryFeedView() {
        setContentType("application/rss+xml");
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
                                           HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        response.setContentType(getContentType());

        SyndFeed feed = new SyndFeedImpl();
        feed.setTitle(feedTitle);
        feed.setEncoding("UTF-8");
        feed.setLink(feedLink);
        feed.setPublishedDate(new Date());
        feed.setDescription(feedDescription);
        feed.setFeedType("rss_2.0");

        FeedEntries entries = (FeedEntries) model.get("entries");
        List<SyndEntry> feedEntries = entries.getEntries().stream()
                .map(e -> {
                    SyndContent description = new SyndContentImpl();
                    EntryFormat format = EntryFormat.valueOf(e.getFormat().toUpperCase());
                    description.setValue("<![CDATA["
                            + format.format(e.getContents()) + "]]>");
                    description.setType("text/html");

                    SyndEntry entry = new SyndEntryImpl();
                    entry.setTitle(e.getTitle());
                    entry.setLink(feed.getLink() + "#entries/" + e.getId());
                    entry.setPublishedDate(e.getCreatedDate().toDate());
                    entry.setUpdatedDate(e.getLastModifiedDate().toDate());
                    entry.setDescription(description);
                    return entry;
                })
                .collect(Collectors.toList());
        feed.setEntries(feedEntries);

        CustomizedWireFeedOutput feedOutput = new CustomizedWireFeedOutput();
        ServletOutputStream out = response.getOutputStream();
        feedOutput.output(feed.createWireFeed(), new OutputStreamWriter(out,
                feed.getEncoding()), true);
    }
}
