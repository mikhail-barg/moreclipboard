package moreclipboard;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.IParameterValues;

public class PasteIndexParameterValues implements IParameterValues 
{
	@Override
	public Map<String, String> getParameterValues() 
	{
		Map<String, String> values = new HashMap<String, String>(9);
		for (int i = 1; i <= 9; ++i)
		{
			values.put("element #" + String.valueOf(i), String.valueOf(i - 1));
		}
        return values;
	}

}
