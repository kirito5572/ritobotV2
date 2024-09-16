package me.kirito5572.objects.main;

import me.kirito5572.App;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.sql.*;


public class SqliteConnector {
    private final Logger logger = LoggerFactory.getLogger(SqliteConnector.class);
    private final MySqlConnector mySqlConnector;
    private static Connection sqliteConnection;
    private static @NotNull String dbUrl = "";

    public final int TEXT = 0;
    public final int INTEGER = 3;   //INT + LONG;

    public SqliteConnector(MySqlConnector mySqlConnector) throws ClassNotFoundException, SQLException, URISyntaxException {
        this.mySqlConnector = mySqlConnector;
        Class.forName("org.sqlite.JDBC");
        String FilePath = new File(getClass().getProtectionDomain().getCodeSource().getLocation()
                .toURI()).getAbsolutePath();
        try {
            FilePath = FilePath.substring(0, FilePath.lastIndexOf("blitz_bot"));
            if(App.OS == App.WINDOWS) {
                dbUrl = FilePath;
            } else if(App.OS == App.UNIX) {
                dbUrl = FilePath;
            } else if(App.OS == App.MAC) {
                dbUrl = FilePath;
            }
        } catch (StringIndexOutOfBoundsException e) {
            dbUrl = "C:\\DiscordServerBotSecrets\\ritobotV2";
        }
        sqliteConnection = DriverManager.getConnection("jdbc:sqlite:" + dbUrl + "sqlite.db");
    }

    /**
     * check SQL Connection is closed
     * @return {@code true} if this {@code Connection} object
     *      is closed; {@code false} if it is still open
     * @throws SQLException if a database access error occurs
     */
    @SuppressWarnings("unused")
    public boolean isConnectionClosedSqlite() throws SQLException {
        return sqliteConnection.isClosed();
    }

    /**
     * reconnecting with sql server
     * @throws SQLException - if a database access error occurs
     */
    public void reConnectionSqlite() throws SQLException {
        if (!sqliteConnection.isClosed()) {
            sqliteConnection.close();
        }
        sqliteConnection = DriverManager.getConnection("jdbc:sqlite:" + dbUrl + "sqlite.db");
    }

    /**
     * select query to sql server
     * @param Query sql query
     * @param dataType the data types that input
     * @param data the data that input
     * @throws SQLException if query execution fail or database access error occurs
     * @return {@link ResultSet}
     */
    public ResultSet Select_Query_Sqlite(@Language("SQLite") String Query, int[] dataType, String[] data) throws SQLException {
        PreparedStatement statement = sqliteConnection.prepareStatement(Query);
        mySqlConnector.Query(statement, dataType, data);
        return statement.executeQuery();
    }

    /**
     * insert query to sql server
     * @param Query sql query
     * @param dataType the data types that input
     * @param data the data that input
     * @return true = success, false = failed
     * @throws SQLException if query execution fail or database access error occurs
     */
    public boolean Insert_Query_Sqlite(@Language("SQLite") String Query, int[] dataType, String[] data) throws SQLException {
        PreparedStatement statement = sqliteConnection.prepareStatement(Query);
        mySqlConnector.Query(statement, dataType, data);
        return statement.execute();
    }

}
