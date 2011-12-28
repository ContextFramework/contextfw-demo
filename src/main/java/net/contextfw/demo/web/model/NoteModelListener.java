package net.contextfw.demo.web.model;

public interface NoteModelListener {

    void noteSelected(String id);
    void noteAdded(String id);
    void noteChanced(String id);
    void noteRemoved(String id);
    void noteUnlocked(String id);
}
