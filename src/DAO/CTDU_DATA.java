package DAO;

import DTO.CT_DOUONG;
import ConDB.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class CTDU_DATA {

    private ArrayList<CT_DOUONG> listCTDU = null;

    public CTDU_DATA() {
        docListCTDU();
    }

    public void docListCTDU() {
        listCTDU = sp_getListCTDU();
    }

    public ArrayList<CT_DOUONG> sp_getListCTDU() {
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT * FROM CT_DOUONG");
            ArrayList<CT_DOUONG> ds_ctdu = new ArrayList<>();
            while (rs.next()) {
                CT_DOUONG ctdu = new CT_DOUONG();

                ctdu.setMaDU(rs.getString(1).trim());
                ctdu.setMaNL(rs.getString(2).trim());
                ctdu.setSoLuong(rs.getInt(3));

                ds_ctdu.add(ctdu);
            }
            return ds_ctdu;
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách CTDU");
        }
        return null;
    }

    public ArrayList<CT_DOUONG> getCTDU(String maDU) {
        ArrayList<CT_DOUONG> ds_ct = new ArrayList<>();
        for (CT_DOUONG ct : listCTDU) {
            if (ct.getMaDU().equals(maDU)) {
                ds_ct.add(ct);
            }
        }
        return ds_ct;
    }

    public static void xoa_1dongCTDU(String maDU, String maNL) {
        Connection conn = CONNECTION.getConnection();
        String sql = "DELETE FROM CT_DOUONG WHERE MADU = '" + maDU + "' AND MANL = '" + maNL + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            conn.close();
            JOptionPane.showMessageDialog(null, "Xóa thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi xóa 1 dòng CTDU!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void xoa_CTDU(String maDU) {
        Connection conn = CONNECTION.getConnection();
        String sql = "DELETE FROM CT_DOUONG WHERE MADU = '" + maDU + "'";
        String sql1 = "UPDATE DOUONG SET TTXOA ='2' WHERE MADU = '" + maDU + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();

            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.executeUpdate();

            ps.close();
            ps1.close();
            conn.close();
            JOptionPane.showMessageDialog(null, "Xóa CTDU thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi xóa CTDU!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }
}
