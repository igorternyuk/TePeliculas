package tepeliculas.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import tepeliculas.connector.DBManager;
import tepeliculas.dto.Country;
import tepeliculas.exceptions.CountryNotFoundException;

/**
 *
 * @author igor
 */
public class CountryDAO implements DAO<Country>{
    private static final String SQL_INSERT = "insert into countries(name) values(?);";
    private static final String SQL_UPDATE = "update countries set name = ?"
            + " where id = ?;";
    private static final String SQL_DELETE = "delete from countries where id = ?;";
    private static final String SQL_READ = "select * from countries where id = ?;";
    private static final String SQL_READ_ALL = "select * from countries;";
    private static final DBManager manager = DBManager.getInstance();
    private PreparedStatement cmd;
    private ResultSet rs;
    
    @Override
    public boolean insert(Country register) {
        try {
            cmd = manager.getConnection().prepareStatement(SQL_INSERT);
            cmd.setString(1, register.getName());
            if(cmd.executeUpdate() > 0){
                JOptionPane.showMessageDialog(
                    null,
                    "New country was succsessfully inserted to the database!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return false;
    }

    @Override
    public boolean update(Country register) {
        try {
            cmd = manager.getConnection().prepareStatement(SQL_UPDATE);
            cmd.setString(1, register.getName());
            cmd.setInt(2, register.getId());
            if(cmd.executeUpdate() > 0){
                JOptionPane.showMessageDialog(
                    null,
                    "Selected country was succsessfully updated!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try {
            cmd = manager.getConnection().prepareStatement(SQL_DELETE);
            cmd.setInt(1, id);
            if(cmd.executeUpdate() > 0){
                JOptionPane.showMessageDialog(
                    null,
                    "The country with id = " + id + " was succsessfully deleted!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return false;
    }

    @Override
    public Country read(int id) {
        try {
            cmd = manager.getConnection().prepareStatement(SQL_READ);
            cmd.setInt(1, id);
            rs = cmd.executeQuery();
            if(rs.next()){
                Country c = new Country(rs.getInt(1), rs.getString(2));
//                JOptionPane.showMessageDialog(
//                    null,
//                    "The country with id = " + id + " was found!",
//                    "Success",
//                    JOptionPane.INFORMATION_MESSAGE
//                );
                return c;
            } else {
                throw new CountryNotFoundException("No countries with id = " + id +
                    " found");
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CountryNotFoundException ex) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return null;
    }

    @Override
    public List<Country> readAll() {
        List<Country> list = new ArrayList<>();
        try {
            cmd = manager.getConnection().prepareStatement(SQL_READ_ALL);
            rs = cmd.executeQuery();
            while(rs.next()){
                Country c = new Country(rs.getInt(1), rs.getString(2));
                list.add(c);
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return list;
    }
    
    private void showDataBaseErrorMessage(SQLException ex){
        JOptionPane.showMessageDialog(
            null,
            ex.getMessage(),
            "Datebase error",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
//    public static void main(String[] args) {
//        CountryDAO dao = new CountryDAO();
//        //Country c = new Country(11, "Zalupa");
//        dao.delete(11);
//        Country c6 = dao.read(6);
//        System.out.println("c6 = " + c6);
//        List<Country> list = dao.readAll();
//        for(Country cc : list){
//            System.out.println(cc);
//        }
//    }
}
