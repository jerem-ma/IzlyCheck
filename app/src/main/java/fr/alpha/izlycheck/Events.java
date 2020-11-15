package fr.alpha.izlycheck;

import java.util.Map;
import java.util.HashMap;

import org.greenrobot.eventbus.EventBus;

public class Events
{
	private static final Map<String, EventBus> events = new HashMap<String, EventBus>();

	public static void registerEvent(String key, EventBus event)
	{
		events.put(key,event);
	}

	public static EventBus getEvent(String key)
	{
		EventBus event = events.get(key);
		
		if (event == null)
			throw new EventNotRegisteredException(key);

		return event;
	}
}
