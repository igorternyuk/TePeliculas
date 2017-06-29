package tepeliculas.gui.tablecelleditors;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellEditor;
import javax.swing.text.NumberFormatter;
import tepeliculas.gui.validators.KeyListenerType;
import tepeliculas.gui.validators.TextFieldInputValidator;

/**
 *
 * @author igor
 */
public class SpinnerNumericCellEditor extends AbstractCellEditor
    implements TableCellEditor{
    private final JSpinner spinner;

    public SpinnerNumericCellEditor(int currValue, int minValue,
            int maxValue, int stepSize) {
        SpinnerNumberModel model = new SpinnerNumberModel(currValue, minValue,
            maxValue, stepSize);
        spinner = new JSpinner(model);
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner,
            "##########");
        NumberFormatter formatter = (NumberFormatter)editor.getTextField().getFormatter();
        formatter.setAllowsInvalid(false);
        formatter.setOverwriteMode(true);
        spinner.setEditor(editor);
    }
    
    public SpinnerNumericCellEditor(double currValue, double minValue,
            double maxValue, double stepSize) {
        SpinnerNumberModel model = new SpinnerNumberModel(currValue, minValue,
            maxValue, stepSize);
        spinner = new JSpinner(model);
        DefaultEditor editor = (DefaultEditor)spinner.getEditor();
        editor.getTextField().addKeyListener(new TextFieldInputValidator(KeyListenerType.FLOAT_VALUES_ONLY));
//        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner,
//            "#,##0.###");
//        NumberFormatter formatter = (NumberFormatter)editor.getTextField().getFormatter();
//        formatter.setAllowsInvalid(false);
//        formatter.setOverwriteMode(true);
//        spinner.setEditor(editor);
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
