package tepeliculas.tmodels;

import java.util.List;
import javax.swing.JButton;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import tepeliculas.dao.CountryDAO;
import tepeliculas.dto.Country;
import tepeliculas.dto.Director;
import tepeliculas.dto.Gender;

/**
 *
 * @author igor
 */
public class TModelDirectors implements TableModel {
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_GENDER = 2;
    public static final int COLUMN_DATE = 3;
    public static final int COLUMN_COUNTRY = 4;
    public static final int COLUMN_BTN_EDIT = 5;
    public static final int COLUMN_BTN_DELETE = 6;
    private static final String[] titles = {
        "ID",
        "Name",
        "Gender",
        "Date",
        "Country",
        "Edit",
        "Delete"
    };
    private final JButton btnEdit;
    private final JButton btnDelete;
    private List<Director> list;
    private final CountryDAO daoCountry;
    public TModelDirectors(List<Director> list) {
        btnEdit = new JButton("Edit");
        btnEdit.setName("edit");
        btnDelete = new JButton("Delete");
        btnDelete.setName("delete");
        this.list = list;
        daoCountry = new CountryDAO();
    }

    public void setList(List<Director> list) {
        this.list = list;
    }

    public Director getDirector(int row){
        if(row >= 0 && row < getRowCount()){
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
        return !(columnIndex == COLUMN_ID || columnIndex == COLUMN_BTN_EDIT ||
               columnIndex == COLUMN_BTN_DELETE);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Director d = list.get(rowIndex);
        Object result = null;
        switch(columnIndex){
            case COLUMN_ID :
                result = d.getId();
                break;
            case COLUMN_NAME :
                result = d.getName();
                break;
            case COLUMN_GENDER :
                result = d.getGender();
                break;
            case COLUMN_DATE :
                java.util.Date fecha = new java.util.Date(d.getDob().getTime());                
                result = fecha;
                break;
            case COLUMN_COUNTRY :
                int id_country = d.getCountry();
                result = daoCountry.read(id_country);
                break;
            case COLUMN_BTN_EDIT :
                result = btnEdit;
                break;
            case COLUMN_BTN_DELETE :
                result = btnDelete;
                break;
            default :
                throw new IllegalArgumentException("Invalid column index");
        }
        return result;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Director d = list.get(rowIndex);
        switch(columnIndex){
            case COLUMN_NAME :
                d.setName((String)aValue);
                break;
            case COLUMN_GENDER :
                d.setGender((Gender)aValue);
                break;
            case COLUMN_DATE :
                java.util.Date date = (java.util.Date)aValue;
                d.setDob(new java.sql.Date(date.getTime()));
                break;
            case COLUMN_COUNTRY :
                Country c = (Country)aValue;
                d.setCountry(c.getId());
                break;
            default :
                throw new IllegalArgumentException("Invalid column index");
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
    }

}
