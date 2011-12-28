package net.contextfw.demo.web.components;

import java.util.List;

import net.contextfw.demo.web.service.TweetResult;
import net.contextfw.demo.web.service.TwitterService;
import net.contextfw.web.application.component.Attribute;
import net.contextfw.web.application.component.ComponentRegister;
import net.contextfw.web.application.component.Element;
import net.contextfw.web.application.remote.Remoted;
import net.contextfw.web.application.scope.Execution;
import net.contextfw.web.application.scope.Provided;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class TwitterFeed extends JsComponent {

    @Provided
    private final TwitterService twitterService;
    
    private final Provider<ComponentRegister> componentRegister;
    
    @Attribute
    private boolean tweetsVisible = false;
    
    private Long sinceId = null;
    
    private String search = null;
    
    private boolean updating = false;

    private List<Tweet> newTweets = null;
    
    @Element(name="tweets", onCreate=false, onUpdate=false)
    public List<Tweet> newTweets() {
        List<Tweet> rv = newTweets;
        newTweets = null;
        return rv;
    }
    
    @Element(name="tweets")
    public List<Tweet> allTweets() {
        if (tweetsVisible && search != null) {
            TweetResult result = twitterService.searchTweets(search, null);
            this.setSinceId(result.getSinceId());
            return result.getTweets();
        } else {
            return null;
        }
    }
    
    @Inject
    public TwitterFeed(TwitterService twitterService,
                       Updater updater,
                       Provider<ComponentRegister> componentRegister) {
        
        this.twitterService = twitterService;
        this.componentRegister = componentRegister;
        
        updater.addTask(new UpdaterTask() {
            public Execution run() {
                return updateTweets();
            }
        });
    }
    
    @Remoted
    public void showTweets() {
        tweetsVisible = true;
        refresh();
    }
    
    public TwitterFeedUpdateJob updateTweets() {
        if (tweetsVisible && newTweets != null) {
            addScript(new JsUpdate(this, "tweetsUpdated"));
            partialRefresh(
                        "tweetsUpdate", 
                        "newTweets", 
                        "tweetsVisible", 
                        "scripts");
        }
        return getUpdateJob();
    }
    
    private TwitterFeedUpdateJob getUpdateJob() {
        if (!updating && search != null) {
            updating = true;
            return new TwitterFeedUpdateJob(getId(),
                            search, 
                            sinceId,
                            componentRegister, 
                            twitterService);
        } else {
            return null;
        }
    }
    
    public void addTweets(TweetResult result, String search) {
        updating = false;
        if (result != null && search.equals(this.search) && this.sinceId != result.getSinceId()) {
            if (newTweets == null) {
                newTweets = result.getTweets();
            } else {
                newTweets.addAll(newTweets);
                if (newTweets.size() > 100) {
                    newTweets = newTweets.subList(0, 100);
                }
            }
            this.setSinceId(result.getSinceId());
        }
    }
    
    @Remoted
    public TwitterFeedUpdateJob setSearch(String search) {
        updating = false;
        this.search = StringUtils.trimToNull(search);
        tweetsVisible = true;
        refresh();
        return getUpdateJob();
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
}
