package tepeliculas.dao;

import java.io.ByteArrayInputStream;
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
import tepeliculas.dto.Movie;
import tepeliculas.exceptions.MovieNotFoundException;

/**
 *
 * @author igor
 */
public class MovieDAO implements DAO<Movie>{
    private static final String SQL_INSERT = "insert into movies values("
            + "default,?,?,?,?,?,?,?,?,?);";
    private static final String SQL_UPDATE = "update movies set name = ?,"
            + "description = ?, fk_classification = ?, fk_genre = ?, "
            + "duration = ?, release_date = ?,fk_director = ?, picture = ?,"
            + "rating = ? where id = ?;";
    private static final String SQL_DELETE = "delete from movies where id = ?;";
    private static final String SQL_READ = "select * from movies where id = ?;";
    private static final String SQL_READ_ALL = "select * from movies;";
    private static final String SQL_FIVE_BEST_MOVIES = "SELECT * FROM "
            + "db_movies.movies ORDER BY movies.rating DESC LIMIT 5";
    private static final DBManager manager = DBManager.getInstance();
    private PreparedStatement cmd;
    private ResultSet rs;
    private String lastSQL = SQL_READ_ALL;
    
    @Override
    public boolean insert(Movie movie) {
        boolean result = false;
        try {
            cmd = manager.getConnection().prepareStatement(SQL_INSERT);
            cmd.setString(1, movie.getName());
            cmd.setString(2, movie.getDescription());
            cmd.setInt(3, movie.getClassification());
            cmd.setInt(4, movie.getGenre());
            cmd.setInt(5, movie.getDuration());
            cmd.setDate(6, movie.getDate());
            cmd.setInt(7, movie.getDirector());
            ByteArrayInputStream byteArray = new ByteArrayInputStream(
                movie.getPicture()
            );
            cmd.setBlob(8, byteArray);
            if(movie.getRating() < 100){
                movie.setRating(movie.getRating() + 1);
            }
            cmd.setInt(9, movie.getRating());
            if(cmd.executeUpdate() > 0){
                result = true;
                JOptionPane.showMessageDialog(
                    null,
                    "New movie was succsessfully inserted to the database!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return result;
    }

    @Override
    public boolean update(Movie movie) {
        boolean result = false;
        try {
            cmd = manager.getConnection().prepareStatement(SQL_UPDATE);
            cmd.setString(1, movie.getName());
            cmd.setString(2, movie.getDescription());
            cmd.setInt(3, movie.getClassification());
            cmd.setInt(4, movie.getGenre());
            cmd.setInt(5, movie.getDuration());
            cmd.setDate(6, movie.getDate());
            cmd.setInt(7, movie.getDirector());
            ByteArrayInputStream byteArray = new ByteArrayInputStream(
            movie.getPicture() == null ? null : movie.getPicture());
            cmd.setBlob(8, byteArray);
            cmd.setInt(9, movie.getRating());
            cmd.setInt(10, movie.getId());
            if(cmd.executeUpdate() > 0){
                result = true;
                JOptionPane.showMessageDialog(
                    null,
                    "Selected movie was succsessfully updated!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return result;
    }

    @Override
    public boolean delete(int id) {
        boolean result = false;
        try {
            cmd = manager.getConnection().prepareStatement(SQL_DELETE);
            cmd.setInt(1, id);
            if(cmd.executeUpdate() > 0){
                result = true;
                JOptionPane.showMessageDialog(
                    null,
                    "The movie with id = " + id + " was succsessfully deleted!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return result;
    }

    @Override
    public Movie read(int id) {
        Movie result = null;
        try {
            cmd = manager.getConnection().prepareStatement(SQL_READ);
            cmd.setInt(1, id);
            rs = cmd.executeQuery();
            if(rs.next()){
                result = readMovieFromReaultSet(rs);
            } else {
                throw new MovieNotFoundException("No movies with id = " + id +
                        " found!");
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MovieNotFoundException ex) {
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return result;
    }

    @Override
    public List<Movie> readAll() {
        List<Movie> list = new ArrayList<>();
        try {
            cmd = manager.getConnection().prepareStatement(SQL_READ_ALL);
            rs = cmd.executeQuery();
            while(rs.next()){
                list.add(readMovieFromReaultSet(rs));
            }
            lastSQL = SQL_READ_ALL;
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return list;
    }
    
    public List<Movie> search(
        String regExp,
        boolean considerClassification, int classification,
        boolean considerGenre, int genre, 
        boolean considerDuration, int durationMin, int durationMax,
        boolean considerDate, java.sql.Date dateMin, java.sql.Date dateMax,
        boolean considerDirector, int director,
        boolean considerCountry, int country,
        boolean considerRating, int ratingMin, int ratingMax
    ){
        List<Movie> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder("select movies.id, movies.name,"
        + " movies.description, movies.fk_classification, movies.fk_genre,"
        + " movies.duration, movies.release_date, movies.fk_director,"
        + " movies.picture, movies.rating "
        + " from classifications, countries, directors, genres, movies where "
        + " movies.fk_classification = classifications.id and "
        + " movies.fk_genre = genres.id and movies.fk_director = directors.id"
        + " and directors.fk_country = countries.id ");
        sb.append(" and movies.name like '%").append(regExp).append("%'");
        
        if(considerClassification){
            sb.append(" and movies.fk_classification = ").append(classification);
        }
        
        if(considerGenre){
            sb.append(" and movies.fk_genre = ").append(genre).append(" ");
        }
        
        if(considerDuration){
            sb.append(String.format(" and (movies.duration >= %d and"
            + " movies.duration.duration <= %d) ", durationMin, durationMax));
        }
        
        if(considerDate){
            sb.append(String.format(" and (movies.release_date >= '%s' and"
            + " movies.release_date <= '%s') ", dateMin.toString(), dateMax.toString()));
        }
        
        if(considerDirector){
            sb.append(" and movies.fk_director = ").append(director).append(" ");
        }
        
        if(considerCountry){
            sb.append(" and countries.id = ").append(country).append(" ");
        }
        
        if(considerRating){
            sb.append(String.format(" and (movies.rating >= %d and"
            + " movies.rating <= %d) ", ratingMin, ratingMax));
        }
        sb.append(";");
        lastSQL = sb.toString();
        System.out.println("SQL = " + sb.toString());
        try {
            cmd = manager.getConnection().prepareStatement(sb.toString());
            rs = cmd.executeQuery();
            while(rs.next()){
                list.add(readMovieFromReaultSet(rs));
            }
            if(!list.isEmpty()) {
                JOptionPane.showMessageDialog(
                    null,
                    list.size() + " movies were found",
                    "Search results",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    null,
                    "No movies found",
                    "No results ...",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return list;
    }
    
    public List<Movie> repeatLastSearch(){
        List<Movie> list = new ArrayList<>();
        try {
            cmd = manager.getConnection().prepareStatement(lastSQL);
            rs = cmd.executeQuery();
            while(rs.next()){
                list.add(readMovieFromReaultSet(rs));
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return list;
    }
    
    public List<Movie> getFiveBestMovies(){
        List<Movie> list = new ArrayList<>();
        try {
            cmd = manager.getConnection().prepareStatement(SQL_FIVE_BEST_MOVIES);
            rs = cmd.executeQuery();
            while(rs.next()){
                list.add(readMovieFromReaultSet(rs));
            }
        } catch (SQLException ex) {
            showDataBaseErrorMessage(ex);
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }    
        return list;
    }
    
    private Movie readMovieFromReaultSet(ResultSet rs) throws SQLException{
        Movie movie = new Movie();
        movie.setId(rs.getInt(1));
        movie.setName(rs.getString(2));
        movie.setDescription(rs.getString(3));
        movie.setClassification(rs.getInt(4));
        movie.setGenre(rs.getInt(5));
        movie.setDuration(rs.getInt(6));
        movie.setDate(rs.getDate(7));
        movie.setDirector(rs.getInt(8));
        movie.setPicture(rs.getBytes(9));
        movie.setRating(rs.getInt(10));
        return movie;
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
//        MovieDAO dao = new MovieDAO();
//        Movie m = new Movie();
//        m.setId(4);
//        m.setName("Sniffer");
//        m.setDescription("Sniffer");
//        m.setClassification(4);
//        m.setGenre(9);
//        m.setDuration(360);
//        Calendar cal = Calendar.getInstance();
//        cal.set(2013, 11, 05);
//        java.sql.Date date = new java.sql.Date(cal.getTimeInMillis());
//        m.setDate(date);
//        m.setDirector(7);
//        m.setPicture(new byte[1]);
//        m.setRating(7);
//        dao.update(m);
////        Movie m2 = dao.read(2);
////        System.out.println(m2);
//        List<Movie> list = dao.search(
//                "S", false, 0,
//                false, 0,
//                false, 0, 0,
//                false,
//                date,
//                date,
//                false, 0,
//                false, 0,
//                true, 5, 10);
//        for(Movie mv : list){
//            System.out.println(mv);
//        }
//        
//    }
}
