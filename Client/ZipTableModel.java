package 채팅클라이언트;



import javax.swing.table.AbstractTableModel;

public class ZipTableModel extends AbstractTableModel {

	String[] columNames =
		{"일련번호","우편번호","시.도","구.군","동","리","빌딩","번지"};
	//데이터
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
    	return data.length;           //2차 배열의 길이
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
