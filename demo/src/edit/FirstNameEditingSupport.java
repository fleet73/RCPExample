package edit;

import model.Person;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

public class FirstNameEditingSupport extends EditingSupport {
	private final TableViewer viewer;

	public FirstNameEditingSupport(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		// TODO Auto-generated method stub
		return new TextCellEditor(viewer.getTable());
	}

	@Override
	protected boolean canEdit(Object element) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		// TODO Auto-generated method stub
		return ((Person) element).getFirstName();
	}

	@Override
	protected void setValue(Object element, Object value) {
		// TODO Auto-generated method stub
		((Person) element).setFirstName(String.valueOf(value));
		viewer.refresh();

	}

}
