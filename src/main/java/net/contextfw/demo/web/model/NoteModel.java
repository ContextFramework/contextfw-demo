package net.contextfw.demo.web.model;

import java.util.Collection;

import net.contextfw.web.application.lifecycle.PageScoped;
import net.contextfw.web.application.scope.Provided;
import net.contextfw.web.commons.cloud.session.CloudSession;

import com.google.inject.Inject;

@PageScoped
public class NoteModel {

    private ProxyHandler listenersHandle = new ProxyHandler(NoteModelListener.class);
    
    private NoteModelListener listeners = listenersHandle.getProxy(NoteModelListener.class);
    
    @Provided
    private final CloudSession session;
    
    @Inject
    public NoteModel(CloudSession session) {
        this.session = session;
    }
    
    public NoteModel addListener(NoteModelListener listener) {
        listenersHandle.addListener(listener);
        return this;
    }

    public void addNote(String title, String content) {
        String id = getNoteProvider(true).addNote(title, content);
        session.set(id, content);
        listeners.noteAdded(id);
    }
    
    public void selectNote(String id) {
        NoteHeader note = getNoteProvider(false).getNote(id);
        if (note != null && !note.isLocked()) {
            getNoteProvider(true).lock(id);
            listeners.noteSelected(id);
        }
    }
    
    public void storeNote(String id, String title, String content) {
        NoteHeader note = getNoteProvider(false).getNote(id);
        if (note != null) {
            setChanged();
            note.setTitle(title);
            session.set(id, content);
            listeners.noteChanced(id);
        }
    }
    
    public String getNoteContent(String id) {
        return id == null ? null : session.get(id, String.class);
    }
    
    private NoteProvider getNoteProvider(boolean changed) {
        NoteProvider provider = session.get(NoteProvider.class);
        if (provider == null) {
            provider = new NoteProvider();
            session.set(provider);
        } else if (changed) {
            setChanged();
        }
        return provider;
    }
    
    public void unlock(String id) {
        NoteHeader note = getNoteProvider(false).getNote(id);
        if (note != null) {
            note.setLockedUntil(null);
            setChanged();
            listeners.noteUnlocked(id);
        }
    }
    
    private void setChanged() {
        session.set(session.get((NoteProvider.class)));
    }
    
    public Collection<NoteHeader> getNoteHeaders() {
        return getNoteProvider(false).getNotes();
    }
    
    public NoteHeader getNoteHeader(String id) {
        return getNoteProvider(false).getNote(id);
    }

    public void lock(String id) {
        NoteHeader note = getNoteProvider(false).getNote(id);
        if (note != null) {
            getNoteProvider(true).lock(id);
            listeners.noteChanced(id);
        }        
    }

    public void remove(String id) {
        if (getNoteProvider(false).removeNote(id)) {
            setChanged();
            listeners.noteRemoved(id);
        }
    }
}