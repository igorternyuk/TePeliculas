package tepeliculas.gui.tablecelleditors;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerListModel;
import javax.swing.table.TableCellEditor;
import tepeliculas.gui.validators.KeyListenerType;
import tepeliculas.gui.validators.TextFieldInputValidator;

/**
 *
 * @author igor
 */
public class SpinnerStringArrayEditor extends AbstractCellEditor
        implements TableCellEditor {
    private final JSpinner spinner = new JSpinner();
    public SpinnerStringArrayEditor(String[] items){
        spinner.setModel(new SpinnerListModel(items));
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor)spinner.getEditor();
        editor.getTextField().addKeyListener(new TextFieldInputValidator(KeyListenerType.NO_SYMBOLS));
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
