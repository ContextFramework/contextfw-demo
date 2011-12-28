package net.contextfw.demo.web.model;

import net.contextfw.web.application.component.Attribute;
import net.contextfw.web.application.component.Buildable;

@Buildable
public class NoteHeader {
    
    @Attribute
    private String title;

    @Attribute
    private String id;
    
    public NoteHeader() {
    }
    
    public NoteHeader(String id, String title, Long lockedUntil) {
        this.title = title;
        this.id = id;
        this.lockedUntil = lockedUntil;
    }
    private Long lockedUntil;
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Long getLockedUntil() {
        return lockedUntil;
    }
    public void setLockedUntil(Long lockedUntil) {
        this.lockedUntil = lockedUntil;
    }
    
    @Attribute
    public boolean isLocked() {
        return lockedUntil == null ? false : lockedUntil > System.currentTimeMillis();
    }
}
