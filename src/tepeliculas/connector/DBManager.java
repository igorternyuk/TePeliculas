package tepeliculas.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author igor
 */
public class DBManager {
    private static final String PATH_TO_CONFIG_FILE = "config/config.dat";
    private static DBManager instance = null;
    private Connection con;
    private String url, server, port, user, pass, database;
    private DBManager(){
        readSettingFromFile(PATH_TO_CONFIG_FILE);
        url = "jdbc:mysql://" + server + ":" + port + "/" + database;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(
                null,
                ex.getMessage(),
                "Database error",
                JOptionPane.ERROR_MESSAGE
            );
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public static DBManager getInstance(){
        if(instance == null){
            instance = new DBManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return con;
    }
    
    public void closeConnection(){
        instance = null;
    }
    
    private enum LoadState{
        SERVER,
        PORT,
        USER,
        PASS,
        DATABASE,
        STOP
    }
    
    private void readSettingFromFile(String path){
        try(InputStream ins = getClass().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(ins, "UTF-8"))){
            String str;
            LoadState ls;
            lbl: while((str = br.readLine()) != null){
                if(str.equalsIgnoreCase("[Server]")){
                    ls = LoadState.SERVER;
                } else if(str.equalsIgnoreCase("[Port]")){
                    ls = LoadState.PORT;
                } else if(str.equalsIgnoreCase("[User]")){
                    ls = LoadState.USER;
                } else if(str.equalsIgnoreCase("[Password]")){
                    ls = LoadState.PASS;
                } else if(str.equalsIgnoreCase("[Database]")){
                    ls = LoadState.DATABASE;
                } else {
                    ls = LoadState.STOP;
                }
                str = br.readLine();
                switch(ls){
                    case SERVER :
                        server = str;
                        break;
                    case PORT :
                        port = str;
                        break;
                    case USER :
                        user = str;
                        break;
                    case PASS :
                        pass = str;
                        break;
                    case DATABASE:
                        database = str;
                        break;
                    case STOP:
                        break lbl;
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                null,
                "Could not read database config file",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    public static void main(String[] args) {
//        DBManager manager = DBManager.getInstance();
//        try {
//            PreparedStatement cmd = manager.getConnection().prepareStatement("select"
//                    + " id, nombre,descripcion from peliculas;");
//            ResultSet rs = cmd.executeQuery();
//            while(rs.next()){
//                System.out.println(String.format("id = %d, nombre = %s, desc = %s",
//                        rs.getInt(1), rs.getString(2), rs.getString(3)));
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
