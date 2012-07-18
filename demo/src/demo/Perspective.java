package demo;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		//layout.addView(View.ID, IPageLayout.LEFT, 0.5f, IPageLayout.ID_EDITOR_AREA);
		IFolderLayout folder = layout.createFolder("messages", IPageLayout.TOP, 0.5f, editorArea);
		//folder.addPlaceholder(View.ID + ":*");
		folder.addView(View.ID);
	}
}
