/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 04.02.2011 11:13:17$
*/
package su.opencode.kefir.util;

import org.apache.log4j.Logger;
import static su.opencode.kefir.util.FileUtils.close;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessHandler extends Thread
{
	ProcessHandler(InputStream is, int type) {
		this.is = is;
		this.type = type;
	}
	public boolean isError() {
		return error;
	}
	public void run() {
		InputStreamReader is = null;
		BufferedReader br = null;
		try
		{
			is = new InputStreamReader(this.is);
			br = new BufferedReader(is);
			String line;
			while ((line = br.readLine()) != null)
			{
				switch (type)
				{
					case OUT_TYPE:
						log.debug(line);
						break;
					case ERR_TYPE:
						error = true;
						errorCount++;
						if (errorCount < MAX_ERROR_COUNT_WRITE_MESSAGE)
							log.error(line);
						break;
				}

				if (errorCount == MAX_ERROR_COUNT_WRITE_MESSAGE)
				{
					log.error("and more errors...");
					break;
				}
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			close(br);
			close(is);
		}
	}

	private InputStream is;
	private int type;
	private int errorCount = 0;
	private boolean error = false;

	private final int MAX_ERROR_COUNT_WRITE_MESSAGE = 100;

	private static final Logger log = Logger.getLogger(ProcessHandler.class);

	public static final int OUT_TYPE = 0;
	public static final int ERR_TYPE = 1;
}