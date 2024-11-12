package com.taller.renderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.Serial;

public class CellRenderer extends DefaultTableCellRenderer  {

	@Serial
    private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected,boolean hasFocus,int row,int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        Color foreg = Color.BLACK;
        if(column > 0 && column < 6){
            this.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        if(row == 0) {
            Font font = new Font(this.getFont().getFontName(), Font.BOLD, this.getFont().getSize()+4);
        	this.setFont(font);
        }else{
            int numeroFilas = table.getModel().getRowCount()-1;
            if(numeroFilas-row <= 2){
                Font font = new Font(this.getFont().getFontName(), Font.BOLD, this.getFont().getSize()+4);
                this.setFont(font);
                if(column == 4){
                    this.setHorizontalAlignment(SwingConstants.LEFT);
                }
            }
        }
        this.setForeground(foreg);
        return this;
	}
}