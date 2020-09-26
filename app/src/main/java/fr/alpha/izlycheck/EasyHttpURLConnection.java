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
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

public class EasyHttpURLConnection{

	private static boolean isCookieManagerSet = false;

	private final HttpURLConnection connection;
	private final Map<String, String> data;

	private boolean hasStarted;
	private String page;

	static{
		CookieHandler.setDefault(new CookieManager());
	}

	public EasyHttpURLConnection(
		String websiteLink,
		Map<String, String> data
	) throws IOException
	{
		URL url = new URL(websiteLink);

		this.connection = (HttpURLConnection) url.openConnection();
		this.data = data;
		this.hasStarted = false;
	}

	public String getPage() throws IOException{
		if (!this.hasStarted)
			connect();

		return this.page;
	}

	public void disconnect(){
		if (this.hasStarted)
			this.connection.disconnect();
	}

	private void connect() throws IOException{
		this.hasStarted = true;
		if (hasData())
			postData();

		readPage();
	}

	private boolean hasData(){
		return data != null && !data.isEmpty();
	}

	private void postData() throws IOException{
		this.connection.setDoOutput(true);
		OutputStream output = connection.getOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(output);

		String formatedData = formatDataForURL();
		dataOutputStream.writeBytes(formatedData);
		dataOutputStream.close();
	}

	private String formatDataForURL(){
		StringBuilder formatedDataBuilder = new StringBuilder();
		for (Entry<String, String> parameter : data.entrySet()){
			String formatedParameter = formatParameterForURL(parameter);
			formatedDataBuilder.append(formatedParameter);
			formatedDataBuilder.append("&");
		}

		int dataLength = formatedDataBuilder.length();
		formatedDataBuilder.deleteCharAt(dataLength - 1);

		return formatedDataBuilder.toString();
	}

	private String formatParameterForURL(Entry<String, String> parameter){
		String key = parameter.getKey();
		String value = parameter.getValue();
		String formatedParameter = key + "=" + value;

		return formatedParameter;
	} 

	private void readPage() throws IOException{
			InputStream inputStream = this.connection.getInputStream();
			this.page = readInputStream(inputStream);

			inputStream.close();
	}

	private String readInputStream(InputStream inputStream)
		throws UnsupportedEncodingException, IOException{
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;

			while ((length = inputStream.read(buffer)) != -1){
				result.write(buffer, 0, length);
			}

			return result.toString("UTF-8");
	}
}
