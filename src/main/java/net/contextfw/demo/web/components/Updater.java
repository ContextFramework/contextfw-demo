package net.contextfw.demo.web.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import net.contextfw.web.application.lifecycle.PageScoped;
import net.contextfw.web.application.remote.Remoted;
import net.contextfw.web.application.scope.Execution;
import net.contextfw.web.commons.cloud.session.CloudSessionOpenMode;
import net.contextfw.web.commons.cloud.session.OpenMode;

@PageScoped
public class Updater extends JsComponent {

    private Set<UpdaterTask> tasks = new CopyOnWriteArraySet<UpdaterTask>();
    
    public void addTask(UpdaterTask runnable) {
        tasks.add(runnable);
    }
    
    public void removeTask(UpdaterTask runnable) {
        tasks.remove(runnable);
    }
    
    @Remoted
    @CloudSessionOpenMode(OpenMode.EXISTING)
    public List<Execution> checkUpdates() {
        List<Execution> executions = new ArrayList<Execution>();
        for (UpdaterTask task : tasks) {
            Execution e = task.run();
            if (e != null) {
                executions.add(e);
            }
        }
        return executions;
    }
}
