/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.DSTAIKHOAN;
import ConDB.CONNECTION;
import ConDB.DBAccess;
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
public class DSTAIKHOAN_DATA {

    private ArrayList<DSTAIKHOAN> listTK = null;

    public DSTAIKHOAN_DATA() {
        docListTK();
    }

    public void docListTK() {
        listTK = sp_getListTK();
    }

    public ArrayList<DSTAIKHOAN> sp_getListTK() {
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT * FROM DSTAIKHOAN");
            ArrayList<DSTAIKHOAN> dstk = new ArrayList<>();
            while (rs.next()) {
                DSTAIKHOAN tk = new DSTAIKHOAN();

                tk.setUserName(rs.getString(1).trim());
                tk.setFullName(rs.getString(2).trim());
                tk.setPassWord(rs.getString(3).trim());
                tk.setChucVu(rs.getString(4).trim());
                tk.setTtXoa(rs.getInt(5));
                dstk.add(tk);
            }
            return dstk;
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách tài khoản!");
        }
        return null;
    }

    public DSTAIKHOAN getTK(String ma) {
        String userName = ma.toLowerCase();
        for (DSTAIKHOAN tk : listTK) {
            if (userName.equals(tk.getUserName().toLowerCase())) {
                return tk;
            }
        }
        return null;
    }

    public static void updateAccount(String ma, String ten, String pass) {
        Connection conn = CONNECTION.getConnection();
        String sql = "update DSTAIKHOAN set FULLNAME='" + ten + "', PASSWORD='" + pass + "' where USERNAME='" + ma + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            conn.close();
            JOptionPane.showMessageDialog(null, "Cập nhật thông tin cá nhân thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi cập nhật!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void themTaiKhoan(String userName, String fullName, String passWord, String chucVu, int ttXoa) {
        Connection conn = CONNECTION.getConnection();
        String sql = "insert into DSTAIKHOAN values(?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, userName);
            ps.setString(2, fullName);
            ps.setString(3, passWord);
            ps.setString(4, chucVu);
            ps.setInt(5, ttXoa);

            ps.executeUpdate();
            ps.close();
            conn.close();
            JOptionPane.showMessageDialog(null, "Thêm tài khoản thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm tài khoản!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void suaTaiKhoan(String userName, String fullName, String passWord, String chucVu, int ttXoa) {
        Connection conn = CONNECTION.getConnection();
        String sql = "update DSTAIKHOAN set FULLNAME='" + fullName + "', PASSWORD='" + passWord + "', CHUCVU='" + chucVu + "', TTXOA='" + ttXoa + "' where USERNAME='" + userName + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            conn.close();
            JOptionPane.showMessageDialog(null, "Sửa tài khoản thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi sửa tài khoản!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void xoaTaiKhoan(String userName) {
        Connection conn = CONNECTION.getConnection();
        if (kiemTraHDTK(userName) == false) {
            String sql = "delete from DSTAIKHOAN WHERE USERNAME='" + userName + "'";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.executeUpdate();
                ps.close();
                conn.close();
                JOptionPane.showMessageDialog(null, "Xóa tài khoản thành công!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi xóa tài khoản!", "ERROR!", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            String sql1 = "update DSTAIKHOAN set TTXOA='1' where USERNAME='" + userName + "'";
            try {
                PreparedStatement ps1 = conn.prepareStatement(sql1);
                ps1.executeUpdate();
                ps1.close();
                conn.close();
                JOptionPane.showMessageDialog(null, "Tài khoản đã hoạt động, đã đổi tình trạng xóa!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi cập nhật TTXOA tài khoản!", "ERROR!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static boolean kiemTraHDTK(String userName) {
        boolean check = false;
        Connection conn = CONNECTION.getConnection();
        String sql1 = "select MANV from PHIEUNHAP WHERE MANV='" + userName + "'";
        String sql2 = "select MANV from PHIEUXUAT WHERE MANV='" + userName + "'";
        String sql3 = "select MANV from PHIEUHUYNL WHERE MANV='" + userName + "'";
        try {
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            PreparedStatement ps3 = conn.prepareStatement(sql3);
            ResultSet rs1 = ps1.executeQuery();
            ResultSet rs2 = ps2.executeQuery();
            ResultSet rs3 = ps3.executeQuery();
            if (rs1.next() || rs2.next() || rs3.next()) {
                check = true;
            }
            rs1.close();
            rs2.close();
            rs3.close();
            ps1.close();
            ps2.close();
            ps3.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Lỗi hàm kiểm tra HĐ TK!");
        }
        return check;
    }

    public boolean kiemTraUserName(String ma) {
        boolean check = false;
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT USERNAME FROM DSTAIKHOAN WHERE USERNAME ='" + ma + "'");
            if (rs.next()) {
                check = true;
            }
        } catch (Exception e) {
        }
        return check;
    }
}
