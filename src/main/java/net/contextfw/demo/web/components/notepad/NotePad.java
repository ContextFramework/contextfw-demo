package net.contextfw.demo.web.components.notepad;

import net.contextfw.demo.web.components.JsComponent;
import net.contextfw.demo.web.components.Updater;
import net.contextfw.demo.web.components.UpdaterTask;
import net.contextfw.demo.web.model.NoteModel;
import net.contextfw.web.application.component.Element;
import net.contextfw.web.application.scope.Execution;
import net.contextfw.web.commons.cloud.session.CloudSession;

import com.google.inject.Inject;

public class NotePad extends JsComponent {

    private NoteModel model;
    
    @Element
    private final NoteList noteList;
    
    @Element
    private final NoteEditor noteEditor;
    
    @Inject
    public NotePad(CloudSession session, Updater updater) {
        model = new NoteModel(session);
        noteList = new NoteList(model);
        noteEditor = new NoteEditor(model);
        updater.addTask(new UpdaterTask() {
            public Execution run() {
                updateNoteList();
                return null;
            }
        });
    }
    
    private void updateNoteList() {
        getNoteList().refresh();
    }

    public NoteList getNoteList() {
        return noteList;
    }

    public NoteEditor getNoteEditor() {
        return noteEditor;
    }
}
