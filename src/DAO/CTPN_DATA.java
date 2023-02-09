package DAO;

import DTO.CTPN;
import ConDB.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class CTPN_DATA {
    private ArrayList<CTPN> listCTPN = null;
    
    public CTPN_DATA(){
        docListCTPN();
    }
    
    public void docListCTPN(){
        listCTPN = sp_getListCTPN();
    }
    
    public ArrayList<CTPN> sp_getListCTPN(){
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT * FROM CTPN");
            ArrayList<CTPN> ds_ctpn = new ArrayList<>();
            while (rs.next()) {
                CTPN ctpn = new CTPN();
                
                ctpn.setMaPN(rs.getString(1).trim());
                ctpn.setMaNL(rs.getString(2).trim());
                ctpn.setSoLuong(rs.getInt(3));
                ctpn.setDonGia(rs.getInt(4));

                ds_ctpn.add(ctpn);
            }
            return ds_ctpn;
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách CTPN");
        }
        return null;
    }
    
    
    public static ArrayList<CTPN> getCTPN(String ma){
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT * FROM CTPN WHERE MAPN = '"+ma+"'");
            ArrayList<CTPN> dsct = new ArrayList<>();
            while (rs.next()) {
                CTPN ct = new CTPN();

                ct.setMaPN(rs.getString(1).trim());
                ct.setMaNL(rs.getString(2).trim());
                ct.setSoLuong(rs.getInt(3));
                ct.setDonGia(rs.getInt(4));
                dsct.add(ct);
            }
            return dsct;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn CTPX!","ERROR!", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
    
    public static void add_CTPN(CTPN ctpn){
        Connection conn= CONNECTION.getConnection();
        try {           
            //Insert CTPN
            String sql2 = "INSERT INTO CTPN VALUES(?,?,?,?)";
            PreparedStatement ps2=conn.prepareStatement(sql2);
            ps2.setString(1, ctpn.getMaPN());
            ps2.setString(2, ctpn.getMaNL());
            ps2.setInt(3, ctpn.getSoLuong());
            ps2.setInt(4, ctpn.getDonGia()); 
            ps2.executeUpdate();
            
            //Update SLT nguyên liệu trong kho
            String sql1 ="UPDATE NGUYENLIEU set SLT= SLT +? WHERE MANL = ?";
            PreparedStatement ps1=conn.prepareStatement(sql1);
            ps1.setInt(1, ctpn.getSoLuong());
            ps1.setString(2, ctpn.getMaNL());            
            ps1.executeUpdate();
            
            ps1.close();
            ps2.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Lỗi Thêm CTPN!!!!");
        }
    }    
    
}
