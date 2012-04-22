package net.contextfw.demo.web.components;

import java.util.ArrayList;
import java.util.List;

import net.contextfw.demo.web.service.TweetResult;
import net.contextfw.demo.web.service.TwitterService;
import net.contextfw.web.application.component.Attribute;
import net.contextfw.web.application.component.Element;
import net.contextfw.web.application.lifecycle.AfterBuild;
import net.contextfw.web.application.lifecycle.BeforeBuild;
import net.contextfw.web.application.remote.Remoted;
import net.contextfw.web.application.scope.Provided;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;

public class TwitterFeed extends JsComponent {

    @Provided
    private final TwitterService twitterService;
    
    @Attribute
    private boolean tweetsVisible = false;
    
    private Long sinceId = 0L;
    
    private String search = null;
    
    private boolean immediately = false;
    
    private String currentUpdater;
    
    @Element
    private List<Tweet> tweets = new ArrayList<Tweet>();
    
    @Inject
    public TwitterFeed(TwitterService twitterService) {
        this.twitterService = twitterService;
    }
    
    @BeforeBuild
    public void beforeBuild() {
        if (!tweets.isEmpty()) {
            addScript(new JsUpdate(this, "tweetsUpdated"));
        }
    }
    
    @AfterBuild
    public void afterBuild() {
        tweets.clear();
        if (tweetsVisible) {
            currentUpdater = twitterService.createUpdateJob(this, search, sinceId, immediately);
            immediately = false;
        }
    }
        
    public AsyncAction addTweetsAsync(TweetResult result, String search, String updater) {
        if (!currentUpdater.equals(updater)) {
           return AsyncAction.DIE;
        }
        if ((search == null && this.search == null) || search.equals(this.search)) {
            tweets.addAll(result.getTweets());
            sinceId = result.getSinceId();
        }
        return AsyncAction.CONTINUE;
    }
    
    @Remoted
    public void setSearch(String search) {
        this.search = StringUtils.trimToNull(search);
        tweetsVisible = true;
        immediately = true;
        sinceId = 0L;
        addScript(new JsUpdate(this, "searchUpdated"));
        refresh();
    }

    public Long getSinceId() {
        return sinceId;
    }

    public void setSinceId(Long sinceId) {
        this.sinceId = sinceId;
    }
    
    public String getSearch() {
        return search;
    }

    public boolean isTweetsVisible() {
        return tweetsVisible;
    }
}
