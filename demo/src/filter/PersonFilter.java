package filter;

import model.Person;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class PersonFilter extends ViewerFilter {

	private String searchString;

	public void setSearchText(String s) {
		// Search must be a substring of the existing value
		//(?i):忽略后面所有字符大小写
		this.searchString = "(?i).*" + s + ".*";
	}


	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (searchString == null || searchString.length() == 0) {
			return true;
		}
		Person p = (Person) element;
		//这里只比较了姓名,可根据实际需求修改
		if (p.getFirstName().matches(searchString)) {
			return true;
		}
		if (p.getLastName().matches(searchString)) {
			return true;
		}

		return false;

	}

}
