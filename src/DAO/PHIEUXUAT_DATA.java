/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import ConDB.DBAccess;
import DTO.CTHD;
import DTO.CTPX;
import DTO.PHIEUXUAT;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author bangn
 */
public class PHIEUXUAT_DATA {

    private ArrayList<PHIEUXUAT> listPX = null;

    public PHIEUXUAT_DATA() {
        docListPX();
    }

    public void docListPX() {
        listPX = sp_getListPX();
    }

    public ArrayList<PHIEUXUAT> sp_getListPX() {
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT * FROM PHIEUXUAT");
            ArrayList<PHIEUXUAT> dspx = new ArrayList<>();
            while (rs.next()) {
                PHIEUXUAT px = new PHIEUXUAT();

                px.setMaPX(rs.getString(1).trim());
                px.setNgayXuat(rs.getString(2).trim());
                px.setMaNV(rs.getString(3).trim());
                dspx.add(px);
            }
            return dspx;
        } catch (SQLException e) {
        }
        return null;
    }

    public ArrayList<PHIEUXUAT> sp_getListPX_trongNgay(String tuNgay) {
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT * FROM PHIEUXUAT where NGAYXUAT >= '" + tuNgay + "'");
            ArrayList<PHIEUXUAT> dspx = new ArrayList<>();
            while (rs.next()) {
                PHIEUXUAT px = new PHIEUXUAT();

                px.setMaPX(rs.getString(1).trim());
                px.setNgayXuat(rs.getString(2).trim());
                px.setMaNV(rs.getString(3).trim());
                dspx.add(px);
            }
            return dspx;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn danh sách phiếu xuất trong ngày!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public ArrayList<PHIEUXUAT> sp_getListPX_TG(String tuNgay, String denNgay) {
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT * FROM PHIEUXUAT where NGAYXUAT >= '" + tuNgay + "' AND NGAYXUAT <= '" + denNgay + "'");
            ArrayList<PHIEUXUAT> dspx = new ArrayList<>();
            while (rs.next()) {
                PHIEUXUAT px = new PHIEUXUAT();

                px.setMaPX(rs.getString(1).trim());
                px.setNgayXuat(rs.getString(2).trim());
                px.setMaNV(rs.getString(3).trim());
                dspx.add(px);
            }
            return dspx;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn danh sách phiếu xuất trong khoảng thời gian!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public ArrayList<PHIEUXUAT> sp_getListPH_TrongNgay(String ngay) {
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT * FROM PHIEUHUYNL where NGAYHUY >= '" + ngay + "'");
            ArrayList<PHIEUXUAT> dsph = new ArrayList<>();
            while (rs.next()) {
                PHIEUXUAT px = new PHIEUXUAT();

                px.setMaPX(rs.getString(1).trim());
                px.setNgayXuat(rs.getString(2).trim());
                px.setMaNV(rs.getString(3).trim());
                dsph.add(px);
            }
            return dsph;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn danh sách phiếu huỷ trong khoảng thời gian!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public ArrayList<PHIEUXUAT> sp_getListPH_TG(String tuNgay, String denNgay) {
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT * FROM PHIEUHUYNL where NGAYHUY >= '" + tuNgay + "' and NGAYHUY <= '" + denNgay + "'");
            ArrayList<PHIEUXUAT> dsph = new ArrayList<>();
            while (rs.next()) {
                PHIEUXUAT px = new PHIEUXUAT();

                px.setMaPX(rs.getString(1).trim());
                px.setNgayXuat(rs.getString(2).trim());
                px.setMaNV(rs.getString(3).trim());
                dsph.add(px);
            }
            return dsph;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn danh sách phiếu huỷ trong khoảng thời gian!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public PHIEUXUAT timPhieuXuat(String ma) {
        for (PHIEUXUAT px : listPX) {
            if (ma.equals(px.getMaPX())) {
                return px;
            }
        }
        return null;
    }

    public static ArrayList<CTPX> getCTPX(String ma) {
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT * FROM CTPX WHERE MAPX = '" + ma + "'");
            ArrayList<CTPX> dsct = new ArrayList<>();
            while (rs.next()) {
                CTPX ct = new CTPX();

                ct.setMaPX(rs.getString(1).trim());
                ct.setMaNL(rs.getString(2).trim());
                ct.setSoLuong(rs.getInt(3));
                dsct.add(ct);
            }
            return dsct;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn CTPX!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public static ArrayList<CTHD> getCTHD(String ma) {
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT * FROM CTHD WHERE MAPX = '" + ma + "'");
            ArrayList<CTHD> dsct = new ArrayList<>();
            while (rs.next()) {
                CTHD ct = new CTHD();

                ct.setMaPX(rs.getString(1).trim());
                ct.setMaDU(rs.getString(2).trim());
                ct.setSoLuong(rs.getInt(3));
                ct.setDonGia(rs.getInt(4));
                dsct.add(ct);
            }
            return dsct;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn CTHD!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public static ArrayList<CTPX> getCTPH(String ma) {
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT * FROM CTPH WHERE MAPH = '" + ma + "'");
            ArrayList<CTPX> dsct = new ArrayList<>();
            while (rs.next()) {
                CTPX ct = new CTPX();

                ct.setMaPX(rs.getString(1).trim());
                ct.setMaNL(rs.getString(2).trim());
                ct.setSoLuong(rs.getInt(3));
                dsct.add(ct);
            }
            return dsct;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn CTPH!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
    
}
