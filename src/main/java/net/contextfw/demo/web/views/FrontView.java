package net.contextfw.demo.web.views;

import net.contextfw.demo.web.components.ProgressHolder;
import net.contextfw.demo.web.components.TwitterFeed;
import net.contextfw.demo.web.components.notepad.NotePad;
import net.contextfw.web.application.component.Component;
import net.contextfw.web.application.component.Element;
import net.contextfw.web.application.lifecycle.PageScoped;
import net.contextfw.web.application.lifecycle.View;

import com.google.inject.Inject;

@View(url="/", parent=RootView.class)
@PageScoped
public class FrontView extends Component {
    
    @SuppressWarnings("unused")
    @Inject
    @Element
    private TwitterFeed twitterFeed;

    @SuppressWarnings("unused")
    @Inject
    @Element
    private ProgressHolder progressHolder;
    
    @SuppressWarnings("unused")
    @Inject
    @Element
    private NotePad notePad;
}