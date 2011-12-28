package net.contextfw.demo.web.components;

import net.contextfw.web.application.component.Attribute;
import net.contextfw.web.application.component.Buildable;

@Buildable
public class Tweet {

    @Attribute
    public final String user;
    @Attribute
    public final String text;
    @Attribute
    public final String profileImageUrl;
    
    public Tweet(twitter4j.Tweet tweet) {
        user = tweet.getFromUser();
        text = tweet.getText();
        profileImageUrl = tweet.getProfileImageUrl();
    }
}
