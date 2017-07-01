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
import tepeliculas.dto.Genre;
import tepeliculas.exceptions.GenreNotFoundException;

/**
 *
 * @author igor
 */
public class GenreDAO implements DAO<Genre> {
    private static final String SQL_INSERT = "insert into genres(name)"
            + " values(?);";
    private static final String SQL_UPDATE = "update genres set "
            + "name = ? where id = ?;";
    private static final String SQL_DELETE = "delete from genres where"
            + " id = ?;";
    private static final String SQL_READ = "select * from genres where"
            + " id = ?;";
    private static final String SQL_READ_ALL = "select * from genres;";
    private static final DBManager manager = DBManager.getInstance();
    private PreparedStatement cmd;
    private ResultSet rs;
    
    @Override
    public boolean insert(Genre register) {
        try {
            cmd = manager.getConnection().prepareStatement(SQL_INSERT);
            cmd.setString(1, register.getName());
            if(cmd.executeUpdate() > 0){
                JOptionPane.showMessageDialog(
                    null,
                    "New genre was succsessfully inserted to the database!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenreDAO.class.getName()).log(Level.SEVERE, null, ex);
            showDataBaseErrorMessage(ex);
        } finally {
            manager.closeConnection();
        }
        return false;
    }

    @Override
    public boolean update(Genre register) {
        try {
            cmd = manager.getConnection().prepareStatement(SQL_UPDATE);
            cmd.setString(1, register.getName());
            cmd.setInt(2, register.getId());
            if(cmd.executeUpdate() > 0){
                JOptionPane.showMessageDialog(
                    null,
                    "Selected genre was succsessfully updated!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(GenreDAO.class.getName()).log(Level.SEVERE, null, ex);
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
                    "The genre with id = " + id + " was succsessfully deleted!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(GenreDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return false;
    }

    @Override
    public Genre read(int id) {
        try {
            cmd = manager.getConnection().prepareStatement(SQL_READ);
            cmd.setInt(1, id);
            rs = cmd.executeQuery();
            if(rs.next()){
                Genre g = new Genre(rs.getInt(1), rs.getString(2));
//                JOptionPane.showMessageDialog(
//                    null,
//                    "The genre with id = " + id + " was found!",
//                    "Success",
//                    JOptionPane.INFORMATION_MESSAGE
//                );
                return g;
            } else {
                throw new GenreNotFoundException("No genres with id = " + id +
                        " found!");
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(GenreDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GenreNotFoundException ex) {
            Logger.getLogger(GenreDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return null;
    }

    @Override
    public List<Genre> readAll() {
        List<Genre> list = new ArrayList<>();
        try {
            cmd = manager.getConnection().prepareStatement(SQL_READ_ALL);
            rs = cmd.executeQuery();
            while(rs.next()){
                Genre g = new Genre(rs.getInt(1), rs.getString(2));
                list.add(g);
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(GenreDAO.class.getName()).log(Level.SEVERE, null, ex);
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
//        GenreDAO dao = new GenreDAO();
//        Genre g = new Genre("Mega genre 2");
//        dao.insert(g);
//        //dao.delete(14);
//        //dao.update(g);
//        //Genre g6 = dao.read(6);
//        //System.out.println("g6 = " + g6);
//        List<Genre> list = dao.readAll();
//        for(Genre gnr : list){
//            System.out.println(gnr);
//        }
//    }
}
