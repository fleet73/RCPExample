package commands;

import model.ModelProvider;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import demo.View;
import dialogs.AddPersonDialog;

public class AddPerson extends AbstractHandler {
private IWorkbenchWindow window;
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		window=PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		ModelProvider persons = ModelProvider.INSTANCE;
		AddPersonDialog dialog = new AddPersonDialog(window.getShell());
		dialog.open();
		if (dialog.getPerson() != null) {
			//将dialog返回的新person对象add至原有的Persons对象并刷新对应视图
			persons.getPersons().add(dialog.getPerson());
			View part=(View)window.getActivePage().findView(View.ID);
			// Updating the display in the view
			part.refresh();
		}
		return null;
	}

}
