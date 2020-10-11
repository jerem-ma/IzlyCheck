package fr.alpha.izlycheck;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

public class EasyHttpURLConnection
{
	private static boolean isCookieManagerSet = false;

	private Map<String, String> data;
	private URL url;

	private HttpURLConnection connection;
	private boolean hasStarted;
	private String page;

	static
	{
		CookieHandler.setDefault(new CookieManager());
	}

	public EasyHttpURLConnection(String websiteLink, Map<String, String> data)
		throws ConnectionFailedException
	{
		try
		{
			tryToInstanciate(websiteLink, data);
		}
		catch (MalformedURLException e)
		{
			throw new ConnectionFailedException(e);
		}
	}

	private void tryToInstanciate(String websiteLink, Map<String, String> data)
		throws MalformedURLException
	{
		this.url = new URL(websiteLink);

		this.data = data;
		this.hasStarted = false;
	}

	public String getPage() throws ConnectionFailedException
	{
		if (!this.hasStarted)
			connect();

		return this.page;
	}

	public void disconnect()
	{
		if (this.hasStarted)
			this.connection.disconnect();
	}

	private void connect() throws ConnectionFailedException
	{
		try
		{
			tryToConnect();
		}
		catch (IOException e)
		{
			throw new ConnectionFailedException(e);
		}
	}

	private void tryToConnect() throws IOException
	{
		this.hasStarted = true;
		this.connection = (HttpURLConnection) url.openConnection();
		if (hasData())
			postData();

		readPage();
	}

	private boolean hasData()
	{
		return data != null && !data.isEmpty();
	}

	private void postData() throws IOException
	{
		this.connection.setDoOutput(true);
		OutputStream output = connection.getOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(output);
		String formatedData = formatDataForURL();
		try
		{
			dataOutputStream.writeBytes(formatedData);
		}
		finally
		{
			output.close();
		}
	}

	private String formatDataForURL()
	{
		StringBuilder formatedDataBuilder = new StringBuilder();
		for (Entry<String, String> parameter : data.entrySet())
		{
			String formatedParameter = formatParameterForURL(parameter);
			formatedDataBuilder.append(formatedParameter);
			formatedDataBuilder.append("&");
		}

		int dataLength = formatedDataBuilder.length();
		formatedDataBuilder.deleteCharAt(dataLength - 1);

		return formatedDataBuilder.toString();
	}

	private String formatParameterForURL(Entry<String, String> parameter)
	{
		String key = parameter.getKey();
		String value = parameter.getValue();
		String formatedParameter = key + "=" + value;

		return formatedParameter;
	} 

	private void readPage() throws IOException
	{
		InputStream inputStream = this.connection.getInputStream();
		try
		{
			this.page = readInputStream(inputStream);
		}
		finally
		{
			inputStream.close();
		}
	}

	private String readInputStream(InputStream inputStream) throws IOException
	{
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;

		while ((length = inputStream.read(buffer)) != -1)
		{
			result.write(buffer, 0, length);
		}

		return result.toString("UTF-8");
	}
}
