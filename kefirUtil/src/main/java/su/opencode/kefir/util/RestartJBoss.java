/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 11.08.2011 12:41:24$
*/
package su.opencode.kefir.util;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.net.*;
import java.sql.SQLException;

import static su.opencode.kefir.util.FileUtils.copyFile;
import static su.opencode.kefir.util.FileUtils.execute;

public class RestartJBoss
{
	private RestartJBoss() {
		final String log4j_path = System.getProperty("log4j_path");
		log.info(StringUtils.concat("log4j_path=", log4j_path));
		if (log4j_path != null)
			DOMConfigurator.configure(log4j_path);
		else
			BasicConfigurator.configure();

		newDbPath = System.getProperty("new_db_path");
		log.info(StringUtils.concat("new_db_path=", newDbPath));

		jbossPath = System.getProperty("jboss_path");
		log.info(StringUtils.concat("jboss_path=", jbossPath));

		jbossDBPath = System.getProperty("jboss_db_path");
		if (jbossDBPath == null || jbossDBPath.isEmpty())
			jbossDBPath = newDbPath;
		log.info(StringUtils.concat("jboss_db_path=", jbossDBPath));
	}
	public static void restartWithCopyingDb() throws SQLException {
		final RestartJBoss jBoss = new RestartJBoss();
		if (jbossPath == null || jbossPath.isEmpty())
			return;

		jBoss.shutdownJBoss();
		copyFile(newDbPath, jbossDBPath);
		jBoss.runJBoss();
	}
	public static void restart() throws SQLException {
		final RestartJBoss jBoss = new RestartJBoss();
		if (jbossPath == null || jbossPath.isEmpty())
			return;

		jBoss.shutdownJBoss();
		jBoss.runJBoss();
	}
	private void runJBoss() {
		execute(StringUtils.concat(jbossPath, "\\bin\\run_my.bat ", jbossPath, "\\bin\\"));
		waitForAvailable(true, WAIT_TIMEOUT_MILISECONDS);
	}
	private void shutdownJBoss() {
		if (!isHTTPAvailable(JBOSS_HOST, JBOSS_PORT, WAIT_TIMEOUT_MILISECONDS))
			return;

		execute(StringUtils.concat(jbossPath, "\\bin\\shutdown_my.bat ", jbossPath, "\\bin\\"));
		waitForAvailable(false, WAIT_TIMEOUT_MILISECONDS);
	}
	private void waitForAvailable(boolean availabale, int waitSeconds) {
		for (int second = 0; ; second++)
		{
			if (second >= waitSeconds)
				throw new RuntimeException("Waiting time is timeout");

			if (isHTTPAvailable(JBOSS_HOST, JBOSS_PORT, waitSeconds) == availabale)
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
	private boolean isHTTPAvailable(String host, int port, int timeoutMs) {
		try
		{
			final URL url = new URL(StringUtils.concat("http://", host, ":", port));
			final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("GET");
			urlConn.setConnectTimeout(timeoutMs);
			urlConn.setReadTimeout(timeoutMs);
			urlConn.connect();

			if (urlConn.getResponseCode() == 200)
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
	private static final String JBOSS_HOST = "localhost";
	private static final int JBOSS_PORT = 5150;
	private static final int WAIT_TIMEOUT_MILISECONDS = 10*60*1000;

	private static String jbossDBPath;
	private static String jbossPath;
	private static String newDbPath;

}
