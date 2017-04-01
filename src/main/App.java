package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import services.TextReplace;

@SuppressWarnings("serial")
public class App extends JFrame implements ActionListener{
	
	private Color colorPanel;
	private Color colorBtn;
	
	public App(){
		initComponents();
		setSize(1024,720);
		setTitle("Find And Replace Text");
		setMinimumSize(new Dimension(900, 750));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void initComponents(){
		colorPanel = new Color(96,124,138);
		colorBtn = new Color(3, 169, 244);
		setLayout(new BorderLayout());
		panel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
		panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
		panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
		
		panel.setBackground(colorPanel);
		panel2.setBackground(colorPanel);
		panel3.setBackground(colorPanel);
		
		lblAddValue = new JLabel("Add new word to replace");
		lblAddValue.setForeground(Color.WHITE);
		jtSsearch = new JTextField(20);
		jtSReplace = new JTextField(20);
		jtSsearch.setPreferredSize(new Dimension(100, 30));
		jtSReplace.setPreferredSize(new Dimension(100, 30));
		
		btnDelete = new JButton("- Delete");
		btnAddValue = new JButton("+ Add value");
		btnFindAndReplace = new JButton("Find and replace in .docx");
		btnAddValue.addActionListener(this);
		btnDelete.addActionListener(this);
		btnFindAndReplace.addActionListener(this);
		
		btnAddValue.setBackground(colorBtn);
		btnAddValue.setForeground(Color.WHITE);
		
		btnDelete.setPreferredSize(new Dimension(300, 100));
		btnDelete.setBackground(colorBtn);
		btnDelete.setForeground(Color.WHITE);
		
		btnFindAndReplace.setPreferredSize(new Dimension(300, 100));
		btnFindAndReplace.setBackground(colorBtn);
		btnFindAndReplace.setForeground(Color.WHITE);
		
		panel.add(lblAddValue);
		panel.add(jtSsearch);
		panel.add(jtSReplace);
		panel.add(btnAddValue);
		
        tblModel = new DefaultTableModel(){
        	@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        Object[] title = {"#", "Text search", "Text replace"};
        tblModel.setColumnIdentifiers(title);
        tbl = new JTable(tblModel);
        tbl.setModel(tblModel);
        JScrollPane scroll = new JScrollPane(tbl);
        tbl.setFillsViewportHeight(true);
        tbl.getTableHeader().setBackground(Color.DARK_GRAY);
        tbl.getTableHeader().setForeground(Color.WHITE);
        tbl.setRowHeight(30);
	        
		panel2.add(scroll);
		panel3.add(btnDelete);
		panel3.add(btnFindAndReplace);
		add(panel,BorderLayout.PAGE_START);
		add(panel2,BorderLayout.CENTER);
		add(panel3,BorderLayout.PAGE_END);
	
	}
	
	private void addRowTbl(String str1, String str2){
		Object[] row = new Object[3];
	    	row[0] = tbl.getModel().getRowCount()+1;
	    	row[1] = str1;
	    	row[2] = str2;
	    	tblModel.addRow(row);
	    	jtSsearch.setText("");
	    	jtSReplace.setText("");
	}
	
	private void deleteRow(int row){
		tblModel.removeRow(row);
	}
	
	private void replaceTextInDocx(){
		int numRows = tbl.getModel().getRowCount();
		
		if(numRows>0){
			
			String[] strSearch 	= new String[numRows];
			String[] strReplace = new String[numRows];
					
			for(int i=0; i<numRows; i++){
				for(int j=1; j< tbl.getColumnCount(); j++){
					System.out.println("[ txtSearch: " + tbl.getValueAt(i, j) + " ]");
					if(j>1){
						strReplace[i] = tbl.getValueAt(i, j).toString();
					}else{
						strSearch[i] = tbl.getValueAt(i, j).toString();
					}
				}
			}
			
			if(strSearch.length>0 && strReplace.length>0){
				TextReplace tr = new TextReplace(strSearch, strReplace);
				tr.replaceWord();
			}else{
				JOptionPane.showMessageDialog(this, "The array replace and array search are empty","Validation",JOptionPane.ERROR_MESSAGE);
				jtSsearch.requestFocus();
			}
			
		}else{
			JOptionPane.showMessageDialog(this, "Enter a some values in the table","Validation",JOptionPane.INFORMATION_MESSAGE);
			jtSsearch.requestFocus();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton boton = (JButton)e.getSource();
		
		if(boton==btnAddValue){
			
			String val1 = jtSsearch.getText();
			val1 = (val1==null) ? "": val1.trim();
			String val2 = jtSReplace.getText();
			val1 = (val2==null) ? "": val1.trim();
			
			if(val1.length()>0 && val2.length()>0){
				addRowTbl(val1, val2);
			}else{
				JOptionPane.showMessageDialog(this, "Enter a word","Validation",JOptionPane.ERROR_MESSAGE);
				jtSsearch.requestFocus();
			}
			
		}else if(boton==btnDelete){
			int row = tbl.getSelectedRow();
			if(row != -1){
				deleteRow(row);
			}else{
				JOptionPane.showMessageDialog(this, "You should select a row","Validation",JOptionPane.INFORMATION_MESSAGE);
			}

		}else if(boton==btnFindAndReplace){
			
			replaceTextInDocx();
			
		}
		
	}
	
	public static void main(String[] args) {
		try {
		    UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
		} catch (Exception e) {
		            e.printStackTrace();
		}
		new App();
	}
	
	private JPanel panel;
	private JPanel panel2;
	private JPanel panel3;
	private JTextField jtSsearch;
	private JTextField jtSReplace;
	private JButton btnAddValue;
	private JButton btnDelete;
	private JButton btnFindAndReplace;
	private JTable tbl;
	private JLabel lblAddValue;
	private DefaultTableModel tblModel;

}
