package net.contextfw.demo.web.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class NoteProvider {
    
    private static final long LOCK_PERIOD = 2 * 1000 * 60;
    
    private Map<String, NoteHeader> notes = new LinkedHashMap<String, NoteHeader>();
    
    public String addNote(String title, String content) {
        String id = UUID.randomUUID().toString();
        notes.put(id, new NoteHeader(id, 
                    title, 
                    System.currentTimeMillis() + LOCK_PERIOD));
        return id;
    }
    
    public Collection<NoteHeader> getNotes() {
        return notes.values();
    }
    
    public NoteHeader getNote(String id) {
        return id == null ? null : notes.get(id);
    }
    
    public boolean removeNote(String id) {
        return notes.remove(id) != null;
    }
    
    public void lock(String id) {
        NoteHeader note = getNote(id);
        if (note != null) {
            note.setLockedUntil(System.currentTimeMillis() + LOCK_PERIOD);
        }
    }
    
    public void unlock(String id) {
        NoteHeader note = getNote(id);
        if (note != null) {
            note.setLockedUntil(null);
        }
    }
}
