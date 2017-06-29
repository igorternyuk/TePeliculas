package tepeliculas.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import tepeliculas.connector.DBManager;
import tepeliculas.dto.Director;
import tepeliculas.dto.Gender;
import tepeliculas.exceptions.DirectorNotFoundException;

/**
 *
 * @author igor
 */
public class DirectorDAO implements DAO<Director>{
    private static final String SQL_INSERT = "insert into directors values(default,?,?,?,?);";
    private static final String SQL_UPDATE = "update directors set name = ?, "
            + "gender = ?, dob = ?, fk_country = ? where id = ?;";
    private static final String SQL_DELETE = "delete from directors where id=?;";
    private static final String SQL_READ = "select * from directors where id=?;";
    private static final String SQL_READ_ALL = "select * from directors;";
    private static final DBManager manager = DBManager.getInstance();
    private PreparedStatement cmd;
    private ResultSet rs;
    
    @Override
    public boolean insert(Director register) {
        try {
            cmd = manager.getConnection().prepareStatement(SQL_INSERT);
            cmd.setString(1, register.getName());
            cmd.setString(2, register.getGender().toString());
            cmd.setDate(3, register.getDob());
            cmd.setInt(4, register.getCountry());
            if(cmd.executeUpdate() > 0){
                JOptionPane.showMessageDialog(
                    null,
                    "New director was successfully inserted to the database!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(DirectorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return false;
    }

    @Override
    public boolean update(Director register) {
        try {
            cmd = manager.getConnection().prepareStatement(SQL_UPDATE);
            cmd.setString(1, register.getName());
            cmd.setString(2, register.getGender().toString());
            cmd.setDate(3, register.getDob());
            cmd.setInt(4, register.getCountry());
            cmd.setInt(5, register.getId());
            if(cmd.executeUpdate() > 0){
                JOptionPane.showMessageDialog(
                    null,
                    "Selected director register was successfully updated",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(DirectorDAO.class.getName()).log(Level.SEVERE, null, ex);
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
                    "The director with id = " + id + " was successfully deleted from database",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(DirectorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return false;
    }

    @Override
    public Director read(int id) {
        try {
            cmd = manager.getConnection().prepareStatement(SQL_READ);
            cmd.setInt(1, id);
            rs = cmd.executeQuery();
            if(rs.next()){
                Director d = readDirectorFtomResultSet(rs);
//                JOptionPane.showMessageDialog(
//                    null,
//                    "The director with id = " + id + " was found!",
//                    "Success",
//                    JOptionPane.INFORMATION_MESSAGE
//                );
                return d;
            } else {
                throw new DirectorNotFoundException("No directors with id = " +
                        id + " found!");
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(DirectorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DirectorNotFoundException ex) {
            Logger.getLogger(DirectorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return null;
    }

    @Override
    public List<Director> readAll() {
        List<Director> list = new ArrayList<>();
        try {
            cmd = manager.getConnection().prepareStatement(SQL_READ_ALL);
            rs = cmd.executeQuery();
            while(rs.next()){
                list.add(readDirectorFtomResultSet(rs));
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(DirectorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return list;
    }
    
    private Director readDirectorFtomResultSet(ResultSet rs) throws SQLException{
        Director d = new Director();
        d.setId(rs.getInt(1));
        d.setName(rs.getString(2));
        d.setGender(Gender.valueOf(rs.getString(3)));
        d.setDob(rs.getDate(4));
        d.setCountry(rs.getInt(5));
        return d;
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
//        DirectorDAO dao = new DirectorDAO();
//        Calendar cal = Calendar.getInstance();
//        cal.set(1983, 7, 15);
//        java.sql.Date dob = new java.sql.Date(cal.getTimeInMillis());
//        Director c = new Director(7, "Artem Livinenko", Gender.Male, dob, 1);
//        dao.update(c);
//        Director d6 = dao.read(6);
//        System.out.println("d6 = " + d6);
//        List<Director> list = dao.readAll();
//        for(Director dr : list){
//            System.out.println(dr);
//        }
//    }
}
