package moreclipboard;

import java.util.LinkedList;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;

/**
 * The class to store the current MoreClipboard contents
 * 
 * <p> As the instance of the system clipboard is created in constructor,
 * the Contents instance should be explicitly disposed when the plugin is stopped.
 * 
 * @see #dispose()
 * @see org.eclipse.swt.dnd.Clipboard
 *  
 * @author Mikhail Barg
 */
public class Contents
{
	/** maximal number of elements in the plugin*/
	private static final int MAX_ELEMENTS = 16;
	
	/** the content itself */
	private final LinkedList<String> m_list = new LinkedList<String>();
	
	/** link to the View that is created for plugin*/
	private ContentsView m_view;

	
	/**
	 * Adds new string to contents. If the contents size exceeds the maximal possible size, last elements got removed
	 * 
	 * <p> If a View is assigned to contents, it gets updated.
	 * 
	 * @param newString - a string to add to top of the contents 
	 */
	public void addString(String newString)
	{
		// remove last element(s) if necessary
		while (m_list.size() >= MAX_ELEMENTS)
		{
			m_list.removeLast();
		}
		
		m_list.addFirst(newString);
		
		updateView();
	}
	
	/**
	 * Removes element at index
	 * 
	 * <p> If a View is assigned to contents, it gets updated.
	 * 
	 * @param itemIndex
	 */
	public void removeElement(int itemIndex)
	{
		if (itemIndex < 0 || itemIndex >= m_list.size())
		{
			throw new IndexOutOfBoundsException("Illegal element index on removal :" + itemIndex);
		}

		m_list.remove(itemIndex);
		
		updateView();
	}

	
	/**
	 * Moves element at index to the contents beginning
	 * 
	 * <p> If a View is assigned to contents, it gets updated.
	 * 
	 * @param itemIndex
	 */
	public void setCurrentElement(int itemIndex)
	{
		if (itemIndex < 0 || itemIndex >= m_list.size())
		{
			throw new IndexOutOfBoundsException("Illegal element index on setCurrent :" + itemIndex);
		}

		m_list.addFirst(m_list.remove(itemIndex));
		
		updateView();
	}

	/**
	 * Removes everything from the list 
	 * 
	 * <p> If a View is assigned to contents, it gets updated.
	 * 
	 */
	public void clear()
	{
		m_list.clear();
		updateView();
	}

	/**
	 * @return the array of contained String elements.
	 */
	public String[] getElements()
	{
		return m_list.toArray(new String[m_list.size()]);
	}

	/**
	 * Links a View to this content
	 * 
	 * <p> The View gets updated
	 * 
	 * @param view a View to be linked
	 */
	public void registerView(ContentsView view)
	{
		if (view == null)
		{
			throw new NullPointerException("Attempt to register a View which is null");
		}
		if (m_view != null)
		{
			throw new IllegalStateException("Attempt to register a new View while already have a registered one");
		}
		
		m_view = view;
		
		updateView();
	}

	/**
	 * Removes the previously assigned View
	 * 
	 * @param view should be the View that was previously assigned to this contents
	 */
	public void removeView(ContentsView view)
	{
		if (view == null)
		{
			throw new NullPointerException("Attempt to remove a View which is null");
		}
		if (m_view != view)
		{
			throw new IllegalArgumentException("Attempt to remove a View which was not previously assigned");
		}
		
		m_view = null;
	}

	/**
	 * Synchronize the assigned View to the changes in contents.
	 * If no View is assigned, does nothing. 
	 */
	private void updateView()
	{
		if (m_view == null)
		{
			return;
		}
		
		m_view.setElements(getElements());
	}

	/**
	 * Retrieves the text from the system clipboard and puts it as a first element in the list
	 * 
	 * <p> If clipboard does not contain the text data, does not change the current contents. 
	 */
	public void getFromClipboard()
	{
		final Clipboard clipboard = new Clipboard(Display.getCurrent());
		
		final String string = (String) clipboard.getContents(TextTransfer.getInstance());
		if (string != null)
		{
			addString(string);
		}
		
		clipboard.dispose();
	}

	
	/**
	 * Puts the first element of the list to the system clipboard as text
	 * 
	 * <p> If the contents is empty, does nothing
	 */
	public void setToClipboard()
	{
		if (m_list.isEmpty())
		{
			return;
		}

		final Clipboard clipboard = new Clipboard(Display.getCurrent());
		
		clipboard.setContents(new Object[]{ m_list.get(0) }, new Transfer[]{ TextTransfer.getInstance() });
		
		clipboard.dispose();
	}
}
