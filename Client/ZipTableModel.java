package ä��Ŭ���̾�Ʈ;



import javax.swing.table.AbstractTableModel;

public class ZipTableModel extends AbstractTableModel {

	String[] columNames =
		{"�Ϸù�ȣ","�����ȣ","��.��","��.��","��","��","����","����"};
	//������
	Object[][] data = {{" ", " "," "," "," "," "," "," "}};
       
    public ZipTableModel(){
          
    }
 
    public ZipTableModel(Object[][] data) {
           this.data = data;
    }
 
    @Override
    public int getColumnCount() {
    	return columNames.length;
    }
 
    @Override
    public int getRowCount() {
    	return data.length;           //2�� �迭�� ����
    }
 
    @Override
    public Object getValueAt(int arg0, int arg1) {
        return data[arg0][arg1];
    }
 
    @Override
    public String getColumnName(int arg0) {
    	return columNames[arg0];
    }

}
