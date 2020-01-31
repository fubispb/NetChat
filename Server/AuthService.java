package NetChat.Server;

import java.sql.*;

public class AuthService {

    private static Connection connection;
    private static Statement statement;

    public static void connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:db.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getNickByLoginAndPass(String login, int pass) throws SQLException {
        String qry = String.format("SELECT nick FROM main_tbl where login = '%s' and password = %s", login, pass);
        ResultSet rs = statement.executeQuery(qry);
        if (rs.next()) {
            return rs.getString(1);
        }
        return null;

    }
}