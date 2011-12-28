package net.contextfw.demo.web.components.notepad;

import java.util.Collection;

import net.contextfw.demo.web.components.JsComponent;
import net.contextfw.demo.web.model.NoteHeader;
import net.contextfw.demo.web.model.NoteModel;
import net.contextfw.demo.web.model.NoteModelListener;
import net.contextfw.web.application.component.Attribute;
import net.contextfw.web.application.component.Element;
import net.contextfw.web.application.remote.Remoted;

public class NoteList extends JsComponent implements NoteModelListener {

    private final NoteModel model;
    
    @Attribute
    private String selectedId; 
    
    public NoteList(NoteModel model) {
        this.model = model.addListener(this);
    }
    
    @Element
    public Collection<NoteHeader> notes() {
        return model.getNoteHeaders();
    }
    
    @Remoted
    public void selectNote(String id) {
        model.selectNote(id);
        refresh();
    }

    @Override
    public void noteAdded(String id) {
        selectedId = id;
        refresh();
    }

    @Override
    public void noteChanced(String id) {
        refresh();
        //jsUpdate(new JsUpdate(this, "noteChanged", model.getNoteProvider().getNote(id)));
    }

    @Override
    public void noteRemoved(String id) {
        if (id.equals(selectedId)) {
            selectedId = null;
        }
        refresh();
    }

    @Override
    public void noteSelected(String id) {
        selectedId = id;
        refresh();
    }
    
    @Override
    public void noteUnlocked(String id) {
        if (id.equals(selectedId)) {
            selectedId = null;
        }
        refresh();
    }
}
