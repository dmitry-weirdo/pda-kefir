/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 11.08.2011 12:41:24$
*/
package su.opencode.kefir.util;

import org.apache.log4j.Logger;

import java.net.*;

public class HttpUtils
{
	public static void waitForHttpAvailable(String host, int port, int timeoutSeconds, boolean availabale) {
		waitForHttpAvailable(host, port, timeoutSeconds, availabale, 200);
	}
	public static void waitForHttpAvailable(String host, int port, int timeoutSeconds, boolean availabale,
																					int successResponseCode)
	{
		for (int second = 0; ; second++)
		{
			if (second >= timeoutSeconds)
				throw new RuntimeException("Waiting time is timeout");

			if (isHttpAvailable(host, port, timeoutSeconds, successResponseCode) == availabale)
				break;

			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				throw new RuntimeException(e);
			}
		}
	}
	public static boolean isHttpAvailable(String host, int port, int timeoutSeconds) {
		return isHttpAvailable(host, port, timeoutSeconds, 200);
	}
	public static boolean isHttpAvailable(String host, int port, int timeoutSeconds, int successResponseCode) {
		try
		{
			final int timeout = timeoutSeconds * 1000;
			final URL url = new URL(StringUtils.concat("http://", host, ":", port));
			final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("GET");
			urlConn.setConnectTimeout(timeout);
			urlConn.setReadTimeout(timeout);
			urlConn.connect();

			if (urlConn.getResponseCode() == successResponseCode)
			{
				log.debug(StringUtils.concat("http request to ", host + ":", port, " is true (", urlConn.getResponseCode(), ")"));
				return true;
			}
		}
		catch (SocketException ex)
		{
			return false;
		}
		catch (SocketTimeoutException ex)
		{
			throw new RuntimeException(ex);
		}
		catch (UnknownHostException ex)
		{
			throw new RuntimeException(ex);
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}

		return false;
	}

	private static final Logger log = Logger.getLogger(RestartJBoss.class);
}