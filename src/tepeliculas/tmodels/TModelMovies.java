package tepeliculas.tmodels;

import java.util.List;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import tepeliculas.dao.ClassificationDAO;
import tepeliculas.dao.DirectorDAO;
import tepeliculas.dao.GenreDAO;
import tepeliculas.dto.Classification;
import tepeliculas.dto.Director;
import tepeliculas.dto.Genre;
import tepeliculas.dto.Movie;

/**
 *
 * @author igor
 */
public class TModelMovies implements TableModel{
    public static final  int COLUMN_ID = 0;
    public static final  int COLUMN_NAME = 1;
    public static final  int COLUMN_CLASSIFICATION = 2;
    public static final  int COLUMN_GENRE = 3;
    public static final  int COLUMN_DURATION = 4;
    public static final  int COLUMN_RELEASE_DATE = 5;
    public static final  int COLUMN_DIRECTOR = 6;
    public static final  int COLUMN_RATING = 7;
    
    private static final String[] titles = {
        "ID",
        "Clasification",
        "Genre",
        "Duration",
        "Release",
        "Director",
        "Rating"        
    };
    
    private static final ClassificationDAO daoClassification = new ClassificationDAO();
    private static final GenreDAO daoGenre = new GenreDAO();
    private static final DirectorDAO daoDirector= new DirectorDAO();
    
    private List<Movie> list;

    public TModelMovies(List<Movie> list) {
        this.list = list;
    }

    public void setList(List<Movie> list) {
        this.list = list;
    }
    
    public Movie getMovie(int row){
        if(row >= 0 && row < list.size()){
            return list.get(row);
        } else {
            throw new IllegalArgumentException("Invalid row index");
        }
    }
    
    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return titles.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return titles[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(list.isEmpty()){
            return Object.class;
        } else {
            return getValueAt(0, columnIndex).getClass();
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Movie m = list.get(rowIndex);
        Object result = null;
        switch(columnIndex){
            case COLUMN_ID :
                result = m.getId();
                break;
            case COLUMN_NAME :
                result = m.getName();
                break;
            case COLUMN_CLASSIFICATION :
                Classification classification = 
                        daoClassification.read(m.getClassification());
                result = classification.getName();
                break;
            case COLUMN_GENRE :
                Genre genre = daoGenre.read(m.getGenre());
                result = genre.getName();
                break;
            case COLUMN_DURATION :
                result = m.getDuration();
                break;
            case COLUMN_RELEASE_DATE :
                result = m.getDate().toString();
                break;
            case COLUMN_DIRECTOR :
                Director director = daoDirector.read(m.getDirector());
                result = director.getName();
                break;
            case COLUMN_RATING :
                result = m.getRating();
                break;
            default :
                throw new IllegalArgumentException("Invalid column index");
        }
        return result;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        
    }
    
    @Override
    public void addTableModelListener(TableModelListener l) {
        
    }
    
    @Override
    public void removeTableModelListener(TableModelListener l) {
        
    }
    
}
