package vn.khmt.restful;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author LUONG The Nhan
 * @version 0.1
 */
public class ConnectToSQL1 {

    public static final String ERROR = "Error";
    public static final String NOTMATCH = "NotMatch";
    public static final String SQLSERVER = "sqlserver";
    public static final String SQLSERVERDRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String MYSQL = "mysql";
    public static final String MYSQLDRIVER = "com.mysql.jdbc.Driver";
    public static final String POSTGRESQL = "postgresql";
    public static final String POSTGRESQLDRIVER = "org.postgresql.Driver";

    Connection dbConnection = null;

    public ConnectToSQL1(String type, String host, String dbname, String user, String pwd) {
        this.dbConnection = getDBConnection(type, host, dbname, user, pwd);
    }

    private Connection getDBConnection(String type, String host, String dbname, String user, String pwd) {
        if (type != null && !type.isEmpty()) {
            try {
                if (type.equalsIgnoreCase(SQLSERVER)) {
                    Class.forName(SQLSERVERDRIVER);
                    dbConnection = DriverManager.getConnection(host + ";database=" + dbname + ";sendStringParametersAsUnicode=true;useUnicode=true;characterEncoding=UTF-8;", user, pwd);
                } else if (type.equalsIgnoreCase(MYSQL)) {
                    Class.forName(MYSQLDRIVER);
                    dbConnection = DriverManager.getConnection(host + "/" + dbname, user, pwd);
                } else if (type.equalsIgnoreCase(POSTGRESQL)) {
                    Class.forName(POSTGRESQLDRIVER);
                    Properties props = new Properties();
                    props.put("user", user);
                    props.put("password", pwd);
                    props.put("sslmode", "require");
                    //dbConnection = DriverManager.getConnection(host + "/" + dbname, props);
                    dbConnection = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + "5432" + "/" + dbname + "?sslmode=require&user=" + user + "&password=" + pwd);
                }
                return dbConnection;
            } catch (ClassNotFoundException | SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return dbConnection;
    }
    public boolean addUser(JSONObject userInfor) throws JSONException{
        try {
            String SQL = "INSERT INTO public.user(id, username, email, name, status, password) VALUES ('"+nextUserID()+"', '"+userInfor.getString("UserName")+"', '"+userInfor.getString("Email")+"', '"+userInfor.getString("Name")+"', '"+userInfor.getString("Status")+"', '"+userInfor.getString("Password")+"');";
            System.out.println(SQL);
            Statement stmt = this.dbConnection.createStatement();
            stmt.execute(SQL);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ConnectToSQL1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public int nextUserID(){
        try {
            String SQL = "SELECT MAX(id) + 1 FROM public.user";
            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs.next()){
                return rs.getInt(1);
            }
         } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } 
        return 0;
    }
    public List<User> getUserList() throws SQLException{
        List<User> users = new ArrayList<>();
        String SQL = "SELECT username, name, email, status, id, password FROM public.user";
        Statement stmt = this.dbConnection.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()){
            users.add(new User(rs.getString(1)+", "+rs.getString(6), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5)));
        }
        return users;
    }
    public Object[] getUser(int id) {
        try {
            String SQL = "SELECT id, username, email, name, status FROM public.user WHERE id = " + id + ";";
            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            // Iterate through the data in the result set and display it.  
            if (rs.next()) {
                System.out.println("User exists");
                Object[] res = new Object[5];
                res[0] = rs.getInt(1);
                res[1] = rs.getString(2);
                res[2] = rs.getString(3);
                res[3] = rs.getString(4);
                res[4] = rs.getInt(5);
                return res;
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } finally {
            if (this.dbConnection != null) {
                try {
                    this.dbConnection.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
        }
        return null;
    }

    public String[] checkUser(String username, String password) {
        try {
            if (username != null && password != null) {
                String SQL = "SELECT id, status FROM public.user WHERE username = '" + username + "' AND password = '" + password + "';";
                Statement stmt = this.dbConnection.createStatement();
                ResultSet rs = stmt.executeQuery(SQL);

                // Iterate through the data in the result set and display it.  
                if (rs.next()) {
                        return new String[]{rs.getString(1), rs.getString(2)};
                    
                } else {
                    return new String[]{NOTMATCH, ""};
                }
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } 
        return null;
    }
    
    private static Timestamp getTimeStampOfDate(Date date) {
        if (date != null) {
            return new Timestamp(date.getTime());
        }
        return null;
    }
    public boolean changeUserName(String userID, String name){
        try {
            String SQL = "UPDATE public.user SET name = '"+name+"' WHERE id = '" + userID + "';";
            Statement stmt = this.dbConnection.createStatement();
            stmt.execute(SQL);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ConnectToSQL1.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    public boolean executeSQL(String sql) {
        Connection con = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = this.dbConnection.prepareStatement(sql);
            // execute insert SQL stetement
            if (preparedStatement != null) {
                int res = preparedStatement.executeUpdate();
                return res == 1;
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
        }
        return false;
    }

}
