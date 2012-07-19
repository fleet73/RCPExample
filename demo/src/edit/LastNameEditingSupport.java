package edit;

import model.Person;

import org.eclipse.jface.viewers.TableViewer;

public class LastNameEditingSupport extends FirstNameEditingSupport {
private final TableViewer viewer;
	public LastNameEditingSupport(TableViewer viewer) {
		super(viewer);
		this.viewer=viewer;
	}
	@Override
	protected Object getValue(Object element) {
		return ((Person) element).getLastName();
	}
	@Override
	protected void setValue(Object element, Object value) {
		((Person) element).setLastName(String.valueOf(value));
		viewer.refresh();
	}

}
