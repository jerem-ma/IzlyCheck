package fr.alpha.izlycheck;

public class EventNotRegisteredException extends RuntimeException
{
	public EventNotRegisteredException()
	{
		super();
	}

	public EventNotRegisteredException(String eventName)
	{
		super(eventName + " is not registered !");
	}
}
