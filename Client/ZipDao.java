package ä��Ŭ���̾�Ʈ;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ZipDao {
	Connection conn;
    PreparedStatement pstmt;
    ResultSet rs;
    private String seq;
    private String zipcode;
    private String sido;
    private String gugun;
    private String dong;
    private String ri;
    private String bldg;
    private String bunji;
    
    public String getSeq() {
    	return seq;
    }
    public void setSeq(String seq) {
    	this.seq = seq;
    }
    public String getZipcode() {
    	return zipcode;
    }
    public void setZipcode(String zipcode) {
    	this.zipcode = zipcode;
    }//������ �ּ� ������ String���·� �������־� ���ڿ��� �޾ƿü� �ֵ��� �Ͽ���.
    //���� get()�� set()�� �̿��Ͽ� ������ ���� ��ȯ�ϰų� ���ý����ִ� �޼ҵ带 �߰������־���.
    
   
   
    // �����ͺ��̽� ����
    public void connection() {
    	try {
                  Class.forName("com.mysql.jdbc.Driver");
                  conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
         	} catch (ClassNotFoundException e) {
         		
            } catch (SQLException e) {
            	
            }
    }
   
    // �����ͺ��̽� ��������
    public void disconnection() {
    	try {
    		if(pstmt != null) pstmt.close();
                      
    		if(rs != null) rs.close();
                      
    		if(conn != null) conn.close();
             
    	} catch (SQLException e) {
    		
    	}
    }

    // �õ�������=============================================
    public ArrayList<ZipDto> searchSido() {
             ArrayList<ZipDto> sidoList = new ArrayList<ZipDto>();
             try {
                      String query = "select distinct(sido) from zipcode order by sido";
                      pstmt = conn.prepareStatement(query);
                      rs = pstmt.executeQuery();
                      while(rs.next()){
                              ZipDto zipcode = new ZipDto();
                              zipcode.setSido(rs.getString("SIDO"));
                              sidoList.add(zipcode);
                      }
             } catch (SQLException e) {
             }

             return sidoList;
    }
   
    // ����������=============================================
    public ArrayList<ZipDto> searchGugun(String sido) {
             ArrayList<ZipDto> gugunList = new ArrayList<>();
            
             try {
                      String query = "select distinct(gugun) from zipcode where sido = \'" + sido + "\' order by gugun";
                      pstmt = conn.prepareStatement(query);
                      rs = pstmt.executeQuery();
                      while(rs.next()){
                              ZipDto zipcode = new ZipDto();
                              zipcode.setGugun(rs.getString("GUGUN"));
                              gugunList.add(zipcode);
                      }
             } catch (SQLException e) {
             }
                             
             return gugunList;         
    }

    // ��������=============================================
    public ArrayList<ZipDto> searchDong(String sido, String gugun) {
             ArrayList<ZipDto> dongList = new ArrayList<>();

             try {
                      String query = "select distinct(dong) from zipcode where sido = \'" + sido + "\'  and gugun = \'" + gugun + "\' order by dong";
                      pstmt = conn.prepareStatement(query);
                      rs = pstmt.executeQuery();
                      while(rs.next()){
                              ZipDto zipcode = new ZipDto();
                              zipcode.setDong(rs.getString("DONG"));
                              dongList.add(zipcode);
                      }
             } catch (SQLException e) {
             }

             return dongList;          
    }

    // �����ּ� ������ =============================================
    public ArrayList<ZipDto> searchAddress(String sido, String gugun, String dong) {

    	ArrayList<ZipDto> addressList = new ArrayList<>();

    	try {
    		String query = "select * from zipcode where sido = \'" + sido + "\'  and gugun = \'" + gugun + "\' and dong = \'" + dong +"\'";

    		pstmt = conn.prepareStatement(query);

    		rs = pstmt.executeQuery();

    		while(rs.next()){

    			ZipDto zipcode = new ZipDto();
    			
    			zipcode.setSeq(rs.getString("seq"));
                zipcode.setZipcode(rs.getString("zipcode"));
                zipcode.setSido(rs.getString("sido"));
	            zipcode.setGugun(rs.getString("gugun"));
	            zipcode.setDong(rs.getString("dong"));
	            zipcode.setRi(rs.getString("ri"));
	            zipcode.setBldg(rs.getString("bldg"));
	            zipcode.setBunji(rs.getString("bungi"));
	            
	            addressList.add(zipcode);
    		}
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
             
    	return addressList;               
    }

	public ArrayList<ZipDto> searchKeyDong(String dongName) {
		
		ArrayList<ZipDto> addressList = new ArrayList<>();

    	try {
    		String query = "select * from zipcode where dong like \'%\' \'" + dongName + "\' || \'%\'";

    		pstmt = conn.prepareStatement(query);

    		rs = pstmt.executeQuery();

    		while(rs.next()){

    			ZipDto zipcode = new ZipDto();
    			
    			zipcode.setSeq(rs.getString("seq"));
                zipcode.setZipcode(rs.getString("zipcode"));
                zipcode.setSido(rs.getString("sido"));
	            zipcode.setGugun(rs.getString("gugun"));
	            zipcode.setDong(rs.getString("dong"));
	            zipcode.setRi(rs.getString("ri"));
	            zipcode.setBldg(rs.getString("bldg"));
	            zipcode.setBunji(rs.getString("bungi"));
	            
	            addressList.add(zipcode);
    		}
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
             
    	return addressList;  
	}
}
