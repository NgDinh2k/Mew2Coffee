package DAO;

import DTO.PHIEUNHAP;
import ConDB.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class PHIEUNHAP_DATA {

    private ArrayList<PHIEUNHAP> listPN = null;

    public PHIEUNHAP_DATA() {
        docListPN();
    }

    public void docListPN() {
        listPN = sp_getListPN();
    }

    public ArrayList<PHIEUNHAP> sp_getListPN() {
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT * FROM PHIEUNHAP");
            ArrayList<PHIEUNHAP> dspn = new ArrayList<>();
            while (rs.next()) {
                PHIEUNHAP pn = new PHIEUNHAP();

                pn.setMaPN(rs.getString(1).trim());
                pn.setNgayNhap(rs.getString(2).trim());
                pn.setMaNV(rs.getString(3).trim());
                dspn.add(pn);
            }
            return dspn;
        } catch (SQLException e) {
        }
        return null;
    }
    
    public ArrayList<PHIEUNHAP> sp_getListPN_TrongNgay(String tuNgay) {
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT * FROM PHIEUNHAP where NGAYNHAP >= '"+tuNgay+"'");
            ArrayList<PHIEUNHAP> dspn = new ArrayList<>();
            while (rs.next()) {
                PHIEUNHAP pn = new PHIEUNHAP();

                pn.setMaPN(rs.getString(1).trim());
                pn.setNgayNhap(rs.getString(2).trim());
                pn.setMaNV(rs.getString(3).trim());
                dspn.add(pn);
            }
            return dspn;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn danh sách phiếu nhập trong ngày!","ERROR!", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
    
    public ArrayList<PHIEUNHAP> sp_getListPN_TG(String tuNgay, String denNgay) {
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT * FROM PHIEUNHAP where NGAYNHAP >= '"+tuNgay+"' and NGAYNHAP <= '"+denNgay+"'");
            ArrayList<PHIEUNHAP> dspn = new ArrayList<>();
            while (rs.next()) {
                PHIEUNHAP pn = new PHIEUNHAP();

                pn.setMaPN(rs.getString(1).trim());
                pn.setNgayNhap(rs.getString(2).trim());
                pn.setMaNV(rs.getString(3).trim());
                dspn.add(pn);
            }
            return dspn;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn danh sách phiếu nhập trong khoảng thời gian!","ERROR!", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public static boolean add_PN(String maPN, String ngayNhap, String maNV) {
        Connection conn = CONNECTION.getConnection();
        try {
            String sql = "INSERT INTO PHIEUNHAP values(?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maPN);
            ps.setString(2, ngayNhap);
            ps.setString(3, maNV);
            ps.executeUpdate();
            ps.close();
            conn.close();
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi thêm PN!!!");
        }
        return false;
    }

    public PHIEUNHAP timPhieuNhap(String ma) {
        for (PHIEUNHAP pn : listPN) {
            if (ma.equals(pn.getMaPN())) {
                return pn;
            }
        }
        return null;
    }
    
    
}
