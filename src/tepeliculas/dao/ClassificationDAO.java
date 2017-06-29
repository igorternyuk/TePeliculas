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
import tepeliculas.dto.Classification;
import tepeliculas.exceptions.ClassificationNotFoundException;

/**
 *
 * @author igor
 */
public class ClassificationDAO implements DAO<Classification>{
    private static final String SQL_INSERT = "insert into classifications(name)"
            + " values(?);";
    private static final String SQL_UPDATE = "update classifications set "
            + "name = ? where id = ?;";
    private static final String SQL_DELETE = "delete from classifications where"
            + " id = ?;";
    private static final String SQL_READ = "select * from classifications where"
            + " id = ?;";
    private static final String SQL_READ_ALL = "select * from classifications;";
    private static final DBManager manager = DBManager.getInstance();
    private PreparedStatement cmd;
    private ResultSet rs;
    
    @Override
    public boolean insert(Classification register) {
        try {
            cmd = manager.getConnection().prepareStatement(SQL_INSERT);
            cmd.setString(1, register.getName());
            if(cmd.executeUpdate() > 0){
                JOptionPane.showMessageDialog(
                    null,
                    "New classification was successfully inserted to the database!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(ClassificationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return false;
    }

    @Override
    public boolean update(Classification register) {
        try {
            cmd = manager.getConnection().prepareStatement(SQL_UPDATE);
            cmd.setString(1, register.getName());
            cmd.setInt(2, register.getId());
            if(cmd.executeUpdate() > 0){
                JOptionPane.showMessageDialog(
                    null,
                    "Selected classification was successfully updated!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(ClassificationDAO.class.getName()).log(Level.SEVERE, null, ex);
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
                    "Selected classification was successfully deleted!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(ClassificationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return false;
    }

    @Override
    public Classification read(int id) {
        try {
            cmd = manager.getConnection().prepareStatement(SQL_READ);
            cmd.setInt(1, id);
            rs = cmd.executeQuery();
            if(rs.next()){
                Classification c = new Classification(rs.getInt(1), rs.getString(2));
//                JOptionPane.showMessageDialog(
//                    null,
//                    "Register with id = " + id + " was found!",
//                    "Success",
//                    JOptionPane.INFORMATION_MESSAGE
//                );
                return c;
            } else {
                throw new ClassificationNotFoundException("No classifications "
                        + " with id = " + id + " found!");
            }

        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(ClassificationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassificationNotFoundException ex) {
            Logger.getLogger(ClassificationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return null;
    }

    @Override
    public List<Classification> readAll() {
        List<Classification> list = new ArrayList<>();
        try {
            cmd = manager.getConnection().prepareStatement(SQL_READ_ALL);
            rs = cmd.executeQuery();
            while(rs.next()){
                Classification c = new Classification(rs.getInt(1),
                        rs.getString(2));
                list.add(c);
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(ClassificationDAO.class.getName()).log(Level.SEVERE, null, ex);
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
//        ClassificationDAO dao = new ClassificationDAO();
//        //Classification c = new Classification(7,"45+");
//        Classification cls6 = dao.read(12);
//        System.out.println("cl6 = " + cls6);
//        List<Classification> list = dao.readAll();
//        for(Classification cl : list){
//            System.out.println(cl);
//        }
//    }
}
