/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.NGUYENLIEU;
import ConDB.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author My-PC
 */
public class NGUYENLIEU_DATA {

    private ArrayList<NGUYENLIEU> listNL = null;

    public NGUYENLIEU_DATA() {
        docListNL();
    }

    public void docListNL() {
        listNL = sp_getListNL();
    }

    public ArrayList<NGUYENLIEU> sp_getListNL() {
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT * FROM NGUYENLIEU");
            ArrayList<NGUYENLIEU> dsnl = new ArrayList<>();
            while (rs.next()) {
                NGUYENLIEU nl = new NGUYENLIEU();

                nl.setMaNL(rs.getString(1).trim());
                nl.setTenNL(rs.getString(2).trim());
                nl.setDvt(rs.getString(3).trim());
                nl.setSoLuongTon(rs.getInt(4));

                dsnl.add(nl);
            }
            return dsnl;
        } catch (SQLException e) {
        }
        return null;
    }

    public ArrayList<NGUYENLIEU> getNLTheoTen(String ten) {
        ArrayList<NGUYENLIEU> dssp = new ArrayList<>();
        for (NGUYENLIEU nl : listNL) {
            String tenNL = nl.getTenNL().toLowerCase();
            if (tenNL.contains(ten.toLowerCase())) {
                dssp.add(nl);
            }
        }
        return dssp;
    }

    public boolean kiemTraMaNL(String ma) {
        boolean check = false;
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT MANL FROM NGUYENLIEU WHERE MANL ='" + ma + "'");
            if (rs.next()) {
                check = true;
            }
        } catch (Exception e) {
        }
        return check;
    }

    public NGUYENLIEU timNguyenlieu(String ma) {
        for (NGUYENLIEU nl : listNL) {
            if (ma.equals(nl.getMaNL())) {
                return nl;
            }
        }
        return null;
    }

    public String maNL_to_tenNL(String maNL) {
        for (NGUYENLIEU nl : listNL) {
            if (maNL.equals(nl.getMaNL())) {
                return nl.getTenNL();
            }
        }
        return null;
    }

    public static void themNguyenLieu(String maNL, String tenNL, String dvt) {
        Connection conn = CONNECTION.getConnection();
        String sql = "insert into NGUYENLIEU values(?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maNL);
            ps.setString(2, tenNL);
            ps.setString(3, dvt);
            ps.setInt(4, 0);

            ps.executeUpdate();
            ps.close();
            conn.close();
            JOptionPane.showMessageDialog(null, "Thêm nguyên liệu thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm nguyên liệu!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void suaNguyenLieu(String maNL, String tenNL, String dvt) {
        Connection conn = CONNECTION.getConnection();
        String sql = "update NGUYENLIEU set TENNL='" + tenNL + "', DVT='" + dvt + "' where MANL='" + maNL + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            conn.close();
            JOptionPane.showMessageDialog(null, "Sửa nguyên liệu thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi sửa nguyên liệu!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void update_SLNL(String maNL, int soLuong) {
        Connection conn = CONNECTION.getConnection();
        String sql = "update NGUYENLIEU set SLT = SLT -'" + soLuong + "' where MANL='" + maNL + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            conn.close();
            JOptionPane.showMessageDialog(null, "Cập nhật số lượng nguyên liệu thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi cập nhật số lượng nguyên liệu!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void xoaNguyenLieu(String maNL) {
        Connection conn = CONNECTION.getConnection();
        if (kiemTraSDNL(maNL) == false) {
            String sql = "delete from NGUYENLIEU WHERE MANL='" + maNL + "'";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.executeUpdate();
                ps.close();
                conn.close();
                JOptionPane.showMessageDialog(null, "Xóa nguyên liệu thành công!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi xóa nguyên liệu!", "ERROR!", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Nguyên liệu này đã được sử dụng, không được xóa!");
        }
    }

    private static boolean kiemTraSDNL(String maNL) {
        boolean check = false;
        Connection conn = CONNECTION.getConnection();
        String sql = "select MANL from CTPN WHERE MANL='" + maNL + "'";
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
            System.out.println("Lỗi hàm kiểm tra sử dụng nl!");
        }
        return check;
    }
}
