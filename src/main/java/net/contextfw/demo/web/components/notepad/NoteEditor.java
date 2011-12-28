package net.contextfw.demo.web.components.notepad;

import net.contextfw.demo.web.components.JsComponent;
import net.contextfw.demo.web.model.NoteHeader;
import net.contextfw.demo.web.model.NoteModel;
import net.contextfw.demo.web.model.NoteModelListener;
import net.contextfw.web.application.component.Element;
import net.contextfw.web.application.remote.Remoted;

import org.apache.commons.lang.StringUtils;

public class NoteEditor extends JsComponent implements NoteModelListener {
    
    private final NoteModel model;
    
    private String editableId;

    public NoteEditor(NoteModel model) {
        this.model = model.addListener(this);
    }
    
    @Remoted
    public void store(String title, String content) {
        if (StringUtils.isNotBlank(title)) {
            if (editableId == null) {
                model.addNote(title, content);
            } else {
                model.storeNote(editableId, StringUtils.stripToEmpty(title), content);
                close();
            }
        }
    }
    
    @Remoted
    public void close() {
        if (editableId != null) {
            model.unlock(editableId);
            editableId = null;
        }
    }
    
    @Remoted
    public void remove() {
        if (editableId != null) {
            model.remove(editableId);
        }
    }
    
    @Element
    public NoteHeader header() {
        return model.getNoteHeader(editableId);
    }

    @Element
    public String content() {
        return model.getNoteContent(editableId);
    }
    
    public void setEditableId(String editableId) {
        this.editableId = editableId;
        refresh();
    }

    @Override
    public void noteSelected(String id) {
        if (this.editableId != null) {
            model.unlock(editableId);
        }
        if (id != null) {
            model.lock(id);
        }
        this.editableId = id;
        refresh();
    }

    @Override
    public void noteAdded(String id) {
        this.editableId = id;
        refresh();
    }

    @Override
    public void noteChanced(String id) {
    }

    @Override
    public void noteRemoved(String id) {
        if (id.equals(editableId)) {
            editableId = null;
            refresh();
        }
    }
    
    @Override
    public void noteUnlocked(String id) {
        refresh();
    }
}