/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.DOUONG;
import ConDB.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class DOUONG_DATA {

    private ArrayList<DOUONG> listDU = null;

    public DOUONG_DATA() {
        docListDU();
    }

    public void docListDU() {
        listDU = sp_getListDU();
    }

    public ArrayList<DOUONG> sp_getListDU() {
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT * FROM DOUONG");
            ArrayList<DOUONG> dsdu = new ArrayList<>();
            while (rs.next()) {
                DOUONG du = new DOUONG();

                du.setMaDU(rs.getString(1).trim());
                du.setMaDM(rs.getString(2).trim());
                du.setTenDU(rs.getString(3).trim());
                du.setAnh(rs.getString(4).trim());
                du.setTtXoa(rs.getInt(5));
                du.setGiaTien(rs.getInt(6));
                du.setGiaSale(rs.getString(7).trim());

                dsdu.add(du);
            }
            return dsdu;
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách đồ uống!");
        }
        return null;
    }

    public ArrayList<DOUONG> sp_getListDU_DM(String maDM) {
        ArrayList<DOUONG> dsdu = new ArrayList<>();
        for (DOUONG du : listDU) {
            if (maDM.equals(du.getMaDM())) {
                dsdu.add(du);
            }
        }
        return dsdu;
    }

    public ArrayList<DOUONG> sp_getListDU_PX(String maDM) {
        ArrayList<DOUONG> dsdu = new ArrayList<>();
        for (DOUONG du : listDU) {
            if (maDM.equals(du.getMaDM()) && (du.getTtXoa() == 0)) {
                dsdu.add(du);
            }
        }
        return dsdu;
    }

    public DOUONG getDU(String ma) {
        for (DOUONG du : listDU) {
            if (ma.equals(du.getMaDU())) {
                return du;
            }
        }
        return null;
    }

    public static void add_DU(String maDU, String maDM, String tenDU, String anh, int ttXoa, int giaTien, String giaSale) {
        Connection conn = CONNECTION.getConnection();
        String sql = "insert into DOUONG values(?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maDU);
            ps.setString(2, maDM);
            ps.setString(3, tenDU);
            ps.setString(4, anh);
            ps.setInt(5, ttXoa);
            ps.setInt(6, giaTien);
            ps.setString(7, giaSale);
            ps.executeUpdate();
            ps.close();
            conn.close();
            JOptionPane.showMessageDialog(null, "Thêm đồ uống thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm đồ uống!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void update_DU(String maDU, String maDM, String tenDU, String anh, int ttXoa, int giaTien, String giaSale) {
        Connection conn = CONNECTION.getConnection();
        String sql = "update DOUONG set MADM='" + maDM + "', TENDU='" + tenDU + "', ANH='" + anh + "', TTXOA='" + ttXoa + "', GIATIEN='" + giaTien + "', GIASALE='" + giaSale + "' where MADU='" + maDU + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            conn.close();
            JOptionPane.showMessageDialog(null, "Sửa đồ uống thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi sửa đồ uống!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void update_TT_DU(String maDU, int ttXoa) {
        Connection conn = CONNECTION.getConnection();
        String sql = "update DOUONG set TTXOA='" + ttXoa + "' where MADU='" + maDU + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi sửa Trạng thái đồ uống!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void del_DU(String maDU) {
        Connection conn = CONNECTION.getConnection();
        if (check_HDDU(maDU) == false) {
            String sql = "delete from CT_DOUONG WHERE MADU ='" + maDU + "'";
            String sql1 = "delete from DOUONG WHERE MADU='" + maDU + "'";
            try {
                conn.setAutoCommit(false);
                
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.executeUpdate();
                ps.close(); 
                
                PreparedStatement ps1 = conn.prepareStatement(sql1);
                ps1.executeUpdate();
                ps1.close();               
                conn.commit();
                conn.setAutoCommit(true);
                conn.close();
                JOptionPane.showMessageDialog(null, "Xóa đồ uống thành công!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi xóa đồ uống!", "ERROR!", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            String sql1 = "update DOUONG set TTXOA='1' where MADU='" + maDU + "'";
            try {
                PreparedStatement ps1 = conn.prepareStatement(sql1);
                ps1.executeUpdate();
                ps1.close();
                conn.close();
                JOptionPane.showMessageDialog(null, "Đồ uống này đã bán, đã đổi tình trạng xóa!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi cập nhật TTXOA đồ uống!", "ERROR!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static boolean check_HDDU(String ma) {
        boolean check = false;
        Connection conn = CONNECTION.getConnection();
        String sql = "select MADU from CTHD WHERE MADU='" + ma + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                check = true;
            }
            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Lỗi hàm kiểm tra sử dụng đồ uống!");
        }
        return check;
    }

    public String maDU_to_tenDU(String maDU) {
        for (DOUONG du : listDU) {
            if (maDU.equals(du.getMaDU())) {
                return du.getTenDU();
            }
        }
        return null;
    }
    
    public boolean kiemTraMaDU(String ma) {
        boolean check = false;
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT MADU FROM DOUONG WHERE MADU ='" + ma + "'");
            if (rs.next()) {
                check = true;
            }
        } catch (Exception e) {
        }
        return check;
    }
       
}
