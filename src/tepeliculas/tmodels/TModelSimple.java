package tepeliculas.tmodels;

import java.util.List;
import javax.swing.JButton;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import tepeliculas.dto.SimpleDTO;

/**
 *
 * @author igor
 */
public class TModelSimple implements TableModel{
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_BTN_EDIT = 2;
    public static final int COLUMN_BTN_DELETE = 3;
    private static final String[] titles = {"ID", "Name", "Edit", "Delete"};
    private List<? extends SimpleDTO> list;
    private final JButton btnEdit;
    private final JButton btnDelete;
    public TModelSimple(List<? extends SimpleDTO> list) {
        this.list = list;
        btnEdit = new JButton("Edit");
        btnEdit.setName("edit");
        btnDelete = new JButton("Delete");
        btnDelete.setName("delete");
    }

    public void setList(List<? extends SimpleDTO> list) {
        this.list = list;
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
        return columnIndex == COLUMN_NAME;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SimpleDTO dto = list.get(rowIndex);
        Object result = null;
        switch(columnIndex){
            case COLUMN_ID :
                result = dto.getId();
                break;
            case COLUMN_NAME :
                result = dto.getName();
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
        SimpleDTO dto = list.get(rowIndex);
        if(columnIndex == COLUMN_NAME){
            dto.setName((String)aValue);
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
    }
    
}
