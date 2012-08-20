package demo;

import java.util.Iterator;
import java.util.List;

import model.ModelProvider;
import model.Person;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.ViewPart;

import dialogs.AddPersonDialog;

public class ChangeView extends ViewPart {
	public static final String ID = "demo.changeview";
	private Button btnNewButton;

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		btnNewButton = new Button(parent, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				ModelProvider persons = ModelProvider.INSTANCE;
				AddPersonDialog dialog = new AddPersonDialog(getViewSite()
						.getShell());
				dialog.open();
				if (dialog.getPerson() != null) {
					persons.getPersons().add(dialog.getPerson());
					View part = (View) getViewSite().getPage()
							.findView(View.ID);
					// Updating the display in the view
					part.refresh();
				}
			}
		});
		btnNewButton.setText("Add");

		Button btnDelete = new Button(parent, SWT.NONE);
		btnDelete.setText("Delete");
		btnDelete.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				IWorkbenchPage page = getViewSite().getPage();
				View view = (View) page.findView(View.ID);
				ISelection selection = view.getSite().getSelectionProvider()
						.getSelection();
				if (selection != null
						&& selection instanceof IStructuredSelection) {
					List<Person> persons = ModelProvider.INSTANCE.getPersons();
					IStructuredSelection sel = (IStructuredSelection) selection;
					for (Iterator<Person> iterator = sel.iterator(); iterator
							.hasNext();) {
						Person person = iterator.next();
						persons.remove(person);
					}
					view.getViewer().refresh();
				}
			}
			
		});

	}

	@Override
	public void setFocus() {
		btnNewButton.setFocus();
	}

}
