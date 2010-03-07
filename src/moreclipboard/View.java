package moreclipboard;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.part.ViewPart;

/**
 * @author Mikhail Barg
 * 
 */
public class View extends ViewPart implements SelectionListener
{
	class RemoveCurrentItemAction extends Action
	{

		@SuppressWarnings("restriction")
		RemoveCurrentItemAction()
		{
			super(Plugin.RemoveCurrentItemAction_text);

			ImageDescriptor id = WorkbenchImages.getWorkbenchImageDescriptor("/elcl16/progress_rem.gif"); //$NON-NLS-1$
			if (id != null)
			{
				setImageDescriptor(id);
			}
			id = WorkbenchImages.getWorkbenchImageDescriptor("/dlcl16/progress_rem.gif"); //$NON-NLS-1$
			if (id != null)
			{
				setDisabledImageDescriptor(id);
			}
		}

		@Override
		public void run()
		{
			removeCurrentItem();
		}
	}

	static class ClearContentsAction extends Action
	{

		@SuppressWarnings("restriction")
		public ClearContentsAction()
		{
			super(Plugin.ClearContentsAction_text);

			ImageDescriptor id = WorkbenchImages.getWorkbenchImageDescriptor("/elcl16/progress_remall.gif"); //$NON-NLS-1$
			if (id != null)
			{
				setImageDescriptor(id);
			}
			id = WorkbenchImages.getWorkbenchImageDescriptor("/dlcl16/progress_remall.gif"); //$NON-NLS-1$
			if (id != null)
			{
				setDisabledImageDescriptor(id);
			}
		}

		@Override
		public void run()
		{
			Plugin.getInstance().getContents().clear();
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////
	
	
	private org.eclipse.swt.widgets.List m_listView;

	private Action m_removeCurAction;
	private Action m_removeAllAction;

	@Override
	public void createPartControl(Composite parent)
	{
		m_listView = new org.eclipse.swt.widgets.List(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		m_listView.addSelectionListener(this);
		createActions();
		createContextMenu();
		initToolBar();

		Contents contents = Plugin.getInstance().getContents();
		assert (contents != null);
		contents.registerView(this);
	}

	private void createActions()
	{
		m_removeCurAction = new RemoveCurrentItemAction();
		m_removeAllAction = new ClearContentsAction();
		updateRemoveCurAction();
		updateRemoveAllAction();
	}
	
	private void createContextMenu()
	{
		MenuManager manager = new MenuManager();
		manager.setRemoveAllWhenShown(true);
		manager.addMenuListener(new IMenuListener()
			{
				public void menuAboutToShow(IMenuManager manager)
				{
					manager.add(m_removeCurAction);
					manager.add(m_removeAllAction);
				}
			});
		m_listView.setMenu(manager.createContextMenu(m_listView));
	}

	private void initToolBar()
	{
		IToolBarManager tm = getViewSite().getActionBars().getToolBarManager();
		tm.add(m_removeCurAction);
		tm.add(m_removeAllAction);
	}

	private void updateRemoveCurAction()
	{
		m_removeCurAction.setEnabled(m_listView.getSelectionIndex() >= 0);
	}

	private void updateRemoveAllAction()
	{
		m_removeCurAction.setEnabled(m_listView.getItemCount() > 0);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus()
	{
		m_listView.setFocus();
	}

	@Override
	public void dispose()
	{
		Plugin.getInstance().getContents().removeView(this);
		super.dispose();
	}

	public void setElements(String elements[])
	{
		m_listView.setItems(elements);
		updateRemoveCurAction();
		updateRemoveAllAction();
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e)
	{
		if (e.widget != m_listView) { return; }
		int itemIndex = m_listView.getSelectionIndex();
		if (itemIndex < 0) { return; }
		Plugin.getInstance().getContents().setCurrentElement(itemIndex);
	}

	@Override
	public void widgetSelected(SelectionEvent e)
	{
		updateRemoveCurAction();
	}

	public void removeCurrentItem()
	{
		int itemIndex = m_listView.getSelectionIndex();
		if (itemIndex < 0) { return; }
		Plugin.getInstance().getContents().removeElement(itemIndex);
	}
}