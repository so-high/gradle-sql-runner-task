package org.gradle

import groovy.sql.Sql
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class SQLExecutionTask extends DefaultTask {
	public static final String DB_URL_PROPERTY_NAME = "jdbc.url"
	public static final String DB_USERNAME_PROPERTY_NAME = "jdbc.username"
	public static final String DB_PASSWORD_PROPERTY_NAME = "jdbc.password"
	public static final String DB_DRIVER_CLASS_NAME = 'jdbc.driver'


	String connPropsFilePath
	String sqlStmtFilePath

	@TaskAction
	def run() {

		if (connPropsFilePath == null || !new File(connPropsFilePath).exists()) {
			throw new IllegalArgumentException(String.format("A connection property file(%s) does not exists ", connPropsFilePath))
		}

		if (sqlStmtFilePath == null || !new File(sqlStmtFilePath).exists()) {
			throw new IllegalArgumentException(String.format("A sql-statement file(%s) does not exists ", sqlStmtFilePath))
		}

		def dbProperties = readDbConnectionProperties(connPropsFilePath)

		Sql.withInstance(dbProperties.url, dbProperties.user, dbProperties.password, dbProperties.driver) { sqlConn ->
			sqlConn.getConnection().setAutoCommit(false)
			new File(sqlStmtFilePath).withReader { reader ->
				String line
				Integer lineNo = 1

				while ((line = reader.readLine()) != null) {
					if ("".equals(line))
						continue;
					sqlConn.execute(line)
					logger.info("run-${lineNo} ${line}")
					lineNo++
				}
			}
			sqlConn.commit()
		}

	}

	def readDbConnectionProperties(String connectionFilePath) {
		def propertiesFile = connectionFilePath

		def properties = new Properties()
		new File(propertiesFile).withInputStream { inputStream ->
			properties.loadFromXML(inputStream)
		}

		def dbProperties = [url     : properties.getProperty(DB_URL_PROPERTY_NAME),
							user    : properties.getProperty(DB_USERNAME_PROPERTY_NAME),
							password: properties.getProperty(DB_PASSWORD_PROPERTY_NAME),
							driver  : properties.getProperty(DB_DRIVER_CLASS_NAME)]

		if (dbProperties.url == null || dbProperties.url.empty) {
			throw new InvalidPropertiesFormatException("Can't find jdbc.url in the properties file $propertiesFile")
		}

		if (dbProperties.user == null || dbProperties.user.empty) {
			throw new InvalidPropertiesFormatException("Can't find jdbc.username in the properties file $propertiesFile")
		}

		if (dbProperties.password == null || dbProperties.password.empty) {
			throw new InvalidPropertiesFormatException("Can't find jdbc.password in the properties file $propertiesFile")
		}

		if (dbProperties.password == null || dbProperties.password.empty) {
			throw new InvalidPropertiesFormatException("Can't find jdbc.driver in the properties file $propertiesFile")
		}

		return dbProperties
	}
}
