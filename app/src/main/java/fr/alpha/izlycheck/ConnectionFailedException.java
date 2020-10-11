package fr.alpha.izlycheck;

public class ConnectionFailedException extends Exception
{
	public ConnectionFailedException()
	{
		super();
	}

	public ConnectionFailedException(Exception e)
	{
		super(e);
	}
}
