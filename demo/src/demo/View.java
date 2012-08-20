package demo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.ModelProvider;
import model.Person;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import sort.MyViewerComparator;
import util.SearchUtil;

import edit.FirstNameEditingSupport;
import edit.GenderEditingSupport;
import edit.LastNameEditingSupport;
import edit.MarriedEditingSupport;
import filter.PersonFilter;

public class View extends ViewPart {
	public View() {
	}
	
	public static final String ID = "demo.view";
	private TableViewer viewer;
	private Text searchText;
	// We use icons
	private static final Image CHECKED = Activator.getImageDescriptor(
			"icons/checked.gif").createImage();
	private static final Image UNCHECKED = Activator.getImageDescriptor(
			"icons/unchecked.gif").createImage();
	// ******以下为扩展
	// styledLabelProvider
	private static Color colorYellow = Display.getCurrent().getSystemColor(
			SWT.COLOR_YELLOW);
	// 过滤器 查询用
	private PersonFilter filter;
	// 排序用
	private MyViewerComparator comparator;

	// ************

	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);
		Label searchLabel = new Label(parent, SWT.NONE);
		searchLabel.setText("Search: ");
		searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
		searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));
		// 自己封装创建视图的方法
		createViewer(parent);
		// 扩展******
		// 排序
		comparator = new MyViewerComparator();
		viewer.setComparator(comparator);

		// 增加查询过滤
		searchText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filter.setSearchText(searchText.getText());
				viewer.refresh();
			}
		});
		// 还没结束,要将filter加入到viewer中才会起作用
		filter = new PersonFilter();
		viewer.addFilter(filter);
		// **********************
	}

	// This will create the columns for the table
	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "First name", "Last name", "Gender", "Married" };
		int[] bounds = { 100, 100, 100, 100 };

		// First column is for the first name
		// createTableViewerColumn为自行封装的方法
		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				Person p = (Person) element;
				return p.getFirstName();
			}
		});
		// 带样式的标签提供者
		// col.setLabelProvider(new StyledCellLabelProvider() {
		// @Override
		// public void update(ViewerCell cell) {
		// String search = searchText.getText();
		// Person person = (Person) cell.getElement();
		// String cellText = person.getFirstName();
		// cell.setText(cellText);
		// if (search != null && search.length() > 0) {
		// int intRangesCorrectSize[] = SearchUtil
		// .getSearchTermOccurrences(search, cellText);
		// List<StyleRange> styleRange = new ArrayList<StyleRange>();
		// for (int i = 0; i < intRangesCorrectSize.length / 2; i++) {
		// int start = intRangesCorrectSize[i];
		// int length = intRangesCorrectSize[++i];
		// StyleRange myStyledRange = new StyleRange(start,
		// length, null, colorYellow);
		//
		// styleRange.add(myStyledRange);
		// }
		// cell.setStyleRanges(styleRange
		// .toArray(new StyleRange[styleRange.size()]));
		// } else {
		// cell.setStyleRanges(null);
		// }
		//
		// super.update(cell);
		//
		// }
		// });
		// 将支持编辑属性添加至该列
		col.setEditingSupport(new FirstNameEditingSupport(viewer));

		// Second column is for the last name
		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Person p = (Person) element;
				return p.getLastName();
			}
		});
		col.setEditingSupport(new LastNameEditingSupport(viewer));
		// Now the gender
		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Person p = (Person) element;
				return p.getGender();
			}
		});
		col.setEditingSupport(new GenderEditingSupport(viewer));

		// // Now the status married
		col = createTableViewerColumn(titles[3], bounds[3], 3);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (((Person) element).isMarried()) {
					return "Yes";
				} else
					return "No";
			}

			@Override
			public Image getImage(Object element) {
				if (((Person) element).isMarried()) {
					return CHECKED;
				} else {
					return UNCHECKED;
				}
			}
		});
		col.setEditingSupport(new MarriedEditingSupport(viewer));
	}

	private void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		// 创建列
		createColumns(parent, viewer);

		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(new ArrayContentProvider());
		// Get the content for the viewer, setInput will call getElements in the
		// contentProvider
		viewer.setInput(ModelProvider.INSTANCE.getPersons());

		// Make the selection available to other views
		getSite().setSelectionProvider(viewer);
		// Set the sorter for the table

		// Layout the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);

		// table右键菜单
		Menu menu = new Menu(parent.getShell(), SWT.POP_UP);
		viewer.getTable().setMenu(menu);
		// 非系统API 自定义的
		createMenuItem(menu, viewer.getTable().getColumn(0));
		createMenuItem(menu);
		delete(menu);
	}
	// 右键菜单实现方法,本例中通过事件来控制列的宽度实现列的可见和隐藏
	private void createMenuItem(Menu parent, final TableColumn column) {
		final MenuItem itemName = new MenuItem(parent, SWT.CHECK);
		itemName.setText(column.getText());
		// radio checkbox是否被选中 参数为什么要用getResizable?
		// 也可以自己用true false
		// getSelection为true的时候 并不是代表它已被选中,而是表示可以被选中,就是没选,说白了就是true false正好反了
		itemName.setSelection(column.getResizable());
		itemName.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (itemName.getSelection()) {
					column.setWidth(150);
					column.setResizable(true);
				} else {
					column.setWidth(0);
					column.setResizable(false);
				}
			}
		});
	}
   //question 右键菜单中加入一个弹出对话框的选项
	private void createMenuItem(Menu parent) {
		final MenuItem itemName = new MenuItem(parent, SWT.CHECK);
		itemName.setText("pop up a message");
		itemName.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (itemName.getSelection()) {
					MessageDialog.openInformation(viewer.getTable().getShell(),
							"message", "已选择");
				} else {
					MessageDialog.openInformation(viewer.getTable().getShell(),
							"message", "已取消选择");
				}
			}
		});
	}

	private TableViewerColumn createTableViewerColumn(String title, int bound,
			final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
				SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		// 增加选中事件(排序)
		column.addSelectionListener(getSelectionAdapter(column, colNumber));
		return viewerColumn;
	}

	// 事件具体定义
	private SelectionAdapter getSelectionAdapter(final TableColumn column,
			final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comparator.setColumn(index);
				int dir = comparator.getDirection();
				viewer.getTable().setSortDirection(dir);
				viewer.getTable().setSortColumn(column);
				viewer.refresh();
			}
		};
		return selectionAdapter;
	}

	//question 扩展 右键 菜单中加入删除
	private void delete(Menu parent) {
		final MenuItem itemName = new MenuItem(parent, SWT.DEFAULT);
		itemName.setText("delete");
		itemName.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
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

	// 删除的时候用到(从其他类获取viewer)
	public TableViewer getViewer() {
		return viewer;
	}

	public void refresh() {
		viewer.refresh();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

}
