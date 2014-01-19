/*
 Copyright 2008 SEC "Knowledge Genesis", Ltd.
 http://www.kg.ru, http://www.knowledgegenesis.com
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 20.10.2009 15:04:47$
*/
package su.opencode.kefir.util;

import org.apache.log4j.Logger;
import static su.opencode.kefir.util.StringUtils.getConcatenation;

import java.sql.*;
import java.util.List;

/**
 * // todo: make logging configurable
 *
 * @author pda (20.10.2009 15:04:47)
 * @version $	Revision$
 */
public class SqlConnector
{
	public static SqlConnector create(String url) throws SQLException {
		SqlConnector connector = new SqlConnector(url);
		connector.createConnection();
		return connector;
	}

	public SqlConnector(String url) {
		sb = new StringBuffer();
		databaseUrl = url;
	}

	public Connection createConnection() throws SQLException {
		try
		{
			if (driver == null)
				driver = Class.forName(DRIVER_CLASS).newInstance();

			logger.info(StringUtils.getConcatenation(sb, "creating connection to ", databaseUrl));
			connection = DriverManager.getConnection(databaseUrl, USER_NAME, PASSWORD);
			logger.info(StringUtils.getConcatenation(sb, "connection to ", databaseUrl, " created successfully"));
			return connection;
		}
		catch (ClassNotFoundException e)
		{
			logger.error(StringUtils.getConcatenation(sb, "JDBC driver class not found for name ", DRIVER_CLASS, ": "), e);
			throw new RuntimeException(e);
		}
		catch (InstantiationException e)
		{
			logger.error(StringUtils.getConcatenation(sb, "error while creating driver instance for class ", DRIVER_CLASS, ": "), e);
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			logger.error(StringUtils.getConcatenation(sb, "error while creating driver instance for class ", DRIVER_CLASS, ": "), e);
			throw new RuntimeException(e);
		}
		catch (SQLException e)
		{
			logger.error(StringUtils.getConcatenation(sb, "error while creating connection to ", databaseUrl, ": "), e);
			throw e;
		}
	}
	public void closeConnection() throws SQLException {
		try
		{
			logger.info(StringUtils.getConcatenation(sb, "closing connection to ", databaseUrl));
			connection.close();
			logger.info(StringUtils.getConcatenation(sb, "connection to ", databaseUrl, " closed successfully"));
		}
		catch (SQLException e)
		{
			logger.error(StringUtils.getConcatenation(sb, "failed to close connection to ", databaseUrl, ": "), e);
			throw e;
		}
	}
	public static void closeConnector(SqlConnector connector) {
		try
		{
			if (connector != null)
				connector.closeConnection();
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
	public Connection getConnection() {
		return connection;
	}

	public ResultSet getResultSet(String query) throws SQLException {
		logger.info(StringUtils.getConcatenation(sb, "Query: ", query)); // todo: use lesser log level

		Statement statement = connection.createStatement();
		try
		{
			return statement.executeQuery(query);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	public ResultSet getResultSet(String query, boolean showQuery) throws SQLException {
		if (showQuery)
			logger.info(StringUtils.getConcatenation(sb, "Query: ", query)); // todo: use lesser log level

		Statement statement = connection.createStatement();
		try
		{
			return statement.executeQuery(query);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	public ResultSet getResultSetNext(String query) throws SQLException {
		logger.info(StringUtils.getConcatenation(sb, "Query: ", query)); // todo: use lesser log level

		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);
		resultSet.next();

		return resultSet;
	}
	public boolean hasResult(String query) throws SQLException {
		ResultSet resultSet = null;
		try
		{
			resultSet = getResultSet(query);
			return resultSet.next();
		}
		finally
		{
			closeResultSet(resultSet);
		}
	}
	public boolean hasResult(PreparedStatement statement) throws SQLException {
		ResultSet resultSet = null;

		try
		{
			resultSet = getResultSet(statement);
			return resultSet.next();
		}
		finally
		{
			if (resultSet != null)
				resultSet.close();
		}
	}
	public void closeStatement(PreparedStatement statement) throws SQLException {
		if (statement == null)
			return;

		statement.close();
	}
	public void closeResultSet(ResultSet resultSet) throws SQLException {
		if (resultSet == null)
			return; // resultSet wasn't actually created

		Statement statement = resultSet.getStatement();
		statement.close();
		resultSet.close();
	}
	public void execute(String query) throws SQLException {
		execute(query, true);
	}
	public void execute(String query, boolean showQuery) throws SQLException {
		if (showQuery)
			logger.debug(StringUtils.getConcatenation(sb, "Query: ", query)); // todo: use lesser log level

		Statement statement = null;
		try
		{
			statement = connection.createStatement();
			statement.execute(query);
		}
		finally
		{
			if (statement != null)
				statement.close();
		}
	}
	public void executeAll(List<String> queries, boolean showQuery) throws SQLException {
		Statement statement = null;
		boolean autoCommit = connection.getAutoCommit();

		try
		{
			connection.setAutoCommit(false);
			statement = connection.createStatement();

			for (String query : queries)
			{
				if (showQuery)
					logger.debug(StringUtils.getConcatenation(sb, "Query: ", query));

				statement.addBatch(query);
			}

			statement.executeBatch();
			connection.commit();
			connection.setAutoCommit(autoCommit);
		}
		catch (BatchUpdateException be)
		{
			int updateCounts = be.getUpdateCounts().length;
			final String message = StringUtils.getConcatenation(sb, be.getMessage(), "\nError in query line ",
				Integer.toString(updateCounts + 1), ": ", queries.get(updateCounts));
			connection.rollback();
			throw new RuntimeException(message);
		}
		catch (SQLException e)
		{
			connection.rollback();
			throw new RuntimeException(e);
		}
		finally
		{
			if (statement != null)
				statement.close();
		}
	}
	public int executeUpdate(String query) throws SQLException {
//		logger.info(getConcatenation(sb, "Query: ", query)); // todo: use lesser log level

		Statement statement = null;
		try
		{
			statement = connection.createStatement();
			return statement.executeUpdate(query);
		}
		finally
		{
			if (statement != null)
				statement.close();
		}
	}

	public PreparedStatement prepareStatement(String query) throws SQLException {
//		logger.info(getConcatenation(sb, "Prepared query: ", query)); // todo: use lesser log level
		return connection.prepareStatement(query);
	}
	public ResultSet getResultSet(PreparedStatement statement) throws SQLException {
		statement.execute();
		return statement.getResultSet();
	}
	public ResultSet getResultSetNext(PreparedStatement statement) throws SQLException {
		statement.execute();
		ResultSet resultSet = statement.getResultSet();
		resultSet.next();

		return resultSet;
	}


	public int getCount(String query) throws SQLException {
		ResultSet resultSet = null;

		try
		{
			resultSet = getResultSetNext(query);
			return resultSet.getInt(1);
		}
		finally
		{
			closeResultSet(resultSet);
		}
	}

	public int getCount(PreparedStatement statement) throws SQLException {
		ResultSet resultSet = null;
		try
		{
			resultSet = getResultSetNext(statement);
			return resultSet.getInt(1);
		}
		finally
		{
			if (resultSet != null)
				resultSet.close();
		}
	}
	public String getDatabaseUrl() {
		return databaseUrl;
	}

	private StringBuffer sb;
	private Connection connection;
	private String databaseUrl;

	private static Object driver = null;
	private static final Logger logger = Logger.getLogger(SqlConnector.class);
	public static final String USER_NAME = "SYSDBA";
	public static final String PASSWORD = "masterkey";
	public static final String DRIVER_CLASS = "org.firebirdsql.jdbc.FBDriver";
	public static String OLD_DATABASE_URL;// = "jdbc:firebirdsql://localhost:3050/d:/db/Firebird/Base.gdb";
	public static String NEW_DATABASE_URL;// = "jdbc:firebirdsql://localhost:3050/d:/db/Firebird/NewBase.gdb";
}