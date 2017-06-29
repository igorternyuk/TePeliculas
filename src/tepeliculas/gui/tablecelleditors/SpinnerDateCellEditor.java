package tepeliculas.gui.tablecelleditors;

import java.awt.Component;
import java.util.Calendar;
import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.table.TableCellEditor;
import javax.swing.text.DateFormatter;

/**
 *
 * @author igor
 */
public class SpinnerDateCellEditor extends AbstractCellEditor
    implements TableCellEditor {
    private final JSpinner spinner;

    public SpinnerDateCellEditor() {
        Calendar calendar = Calendar.getInstance();
        java.util.Date initialDate = calendar.getTime();
        calendar.add(Calendar.YEAR, -300);
        java.util.Date earliestDate = calendar.getTime();
        calendar.add(Calendar.YEAR, +400);
        java.util.Date latestDate = calendar.getTime();
        SpinnerDateModel model = new SpinnerDateModel(initialDate, earliestDate,
            latestDate, Calendar.DAY_OF_MONTH);
        spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd-MM-yyyy");
        DateFormatter formatter = (DateFormatter)editor.getTextField().getFormatter();
        formatter.setAllowsInvalid(false);
        formatter.setOverwriteMode(true);
        spinner.setEditor(editor);
    }
    
    @Override
    public Object getCellEditorValue() {
        return spinner.getValue();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        spinner.setValue(value);
        return spinner;
    }
}
