/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tepeliculas.gui.tablecelleditors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author igor
 */
public class ComboBoxCellEditor<T> extends AbstractCellEditor
    implements TableCellEditor, ActionListener {
    private T selectedItem;
    private List<T> lista;
    private Class<T> type;
    private JComboBox<T> comboBox;
    
    public ComboBoxCellEditor(Class<T> type, List<T> lista) {
        this.lista = lista;
        this.type = type;
        comboBox = new JComboBox<>();
    }
    
    public void updateList(List<T> newList){
        lista.clear();
        lista.addAll(newList);
    }
    
    @Override
    public Object getCellEditorValue() {
        return this.selectedItem;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox<T> comboBox = (JComboBox<T>)e.getSource();
        this.selectedItem = (T)comboBox.getSelectedItem();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if(value != null && type.isAssignableFrom(value.getClass())){
            this.selectedItem = (T)value;
        }
        comboBox.removeAllItems();
        for(T item : lista){
            comboBox.addItem(item);
        }
        comboBox.setSelectedItem(selectedItem);
        comboBox.addActionListener(this);
        return comboBox;
    }
}
