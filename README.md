# gradle-sql-runner-task
containing custom task that execute sql statement

#usage 
-PconnectionPropertyXml=path/to/connectionPropertyXml.xml
-Psql=path/to/sql/statement.sql

#connectionPropertyXml sample 
```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
	<entry key="jdbc.url">jdbc:mysql://localhost:3306/mydb?useUnicode=true&amp;characterEncoding=UTF8&amp;connectTimeout=5000&amp;socketTimeout=6000</entry>
	<entry key="jdbc.username">username</entry>
	<entry key="jdbc.password">password</entry>
	<entry key="jdbc.driver">com.mysql.jdbc.Driver</entry>
</properties>
```
