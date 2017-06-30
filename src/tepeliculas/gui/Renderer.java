package tepeliculas.gui;

import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author igor
 */
public class Renderer extends DefaultTableCellRenderer{
    private static final JTextField txtField = new JTextField();
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column){
        if(value instanceof JButton){
            JButton btn = (JButton)value;
            if(isSelected){
                btn.setForeground(Color.BLACK);
                btn.setBackground(btn.getName().equalsIgnoreCase("edit") ?
                        Color.GREEN : Color.RED);
            } else {
                btn.setForeground(Color.BLACK);
                btn.setBackground(UIManager.getColor("Button.background"));
            }
            return btn;
        } else if(value instanceof java.util.Date){
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            java.util.Date date = (java.util.Date)value;
            String str = dateFormat.format(date);
            txtField.setText(str);
            return txtField;
        }
        return super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);
    }
}