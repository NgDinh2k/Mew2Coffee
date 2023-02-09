package Main;

import DTO.CT_DOUONG;
import DTO.PHIEUNHAP;
import DTO.NGUYENLIEU;
import DTO.DOUONG;
import DTO.CTHD;
import DTO.PHIEUXUAT;
import DTO.CTPX;
import DTO.DSTAIKHOAN;
import DTO.CTPN;
import DTO.DU_ORDER;
import DAO.CTPN_DATA;
import DAO.DSTAIKHOAN_DATA;
import DAO.DANHMUC_DATA;
import DAO.NGUYENLIEU_DATA;
import DAO.PHIEUNHAP_DATA;
import DAO.PHIEUXUAT_DATA;
import DAO.DOUONG_DATA;
import DAO.CTDU_DATA;
import ConDB.CONNECTION;
import ConDB.DBAccess;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import com.microsoft.sqlserver.jdbc.SQLServerPreparedStatement;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author My-PC
 */
public class Mew2Coffee extends javax.swing.JFrame {

    /**
     * Creates new form Mew2Coffee
     * @param userName
     */
    public Mew2Coffee(String userName) {
        initComponents();
        setIcon();

        loadMainForm();
        loadTTCaNhan(userName);
        loadQLTK();
        loadQLNL();
        loadQLDU();
        loadNhapNL();
        loadOrder();

        customControls();

    }
    private DSTAIKHOAN_DATA tk_data = new DSTAIKHOAN_DATA();
    private NGUYENLIEU_DATA nl_Data = new NGUYENLIEU_DATA();
    private DANHMUC_DATA dm_Data = new DANHMUC_DATA();
    private DOUONG_DATA du_Data = new DOUONG_DATA();
    private CTDU_DATA ctdu_data = new CTDU_DATA();
    private PHIEUNHAP_DATA pn_data = new PHIEUNHAP_DATA();
    private PHIEUXUAT_DATA px_data = new PHIEUXUAT_DATA();
    private CTPN_DATA ctpn_data = new CTPN_DATA();
    private String action_QLTK = "";
    private String action_QLNL = "";
    private String action_QLDU = "";
    private String tt_DU = "";
    private String sNL = "";
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd_HH:mm:ss");
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    private File fileAnhDU;

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("Logo_Mew2Coffee.png")));
    }

    private void customControls() {
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                btnTimNL.doClick();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                btnTimNL.doClick();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                btnTimNL.doClick();
            }
        });
        btnTimNL.setVisible(false);

        txtTimKiem_QLNL.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                btnTimNL_QLNL.doClick();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                btnTimNL_QLNL.doClick();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                btnTimNL_QLNL.doClick();
            }
        });
        btnTimNL_QLNL.setVisible(false);
    }

    private void loadMainForm() {
        btn_OpenOrDer.setVisible(true);
        btn_OpenNhapNL.setVisible(true);
        btn_OpenQLNL.setVisible(false);
        btn_OpenQLDU.setVisible(false);
        btn_OpenQLTK.setVisible(false);
        btn_OpenThongKe.setVisible(false);
        btn_OpenTT.setVisible(true);

        pnOrder.setVisible(true);
        pnNhapNL.setVisible(false);
        pnQLNL.setVisible(false);
        pnQLDU.setVisible(false);
        pnQLTK.setVisible(false);
        pnThongKe.setVisible(false);
        pnTTCaNhan.setVisible(false);
    }

    private void loadQLTK() {
        loadDataTableDSTK();
        loadCBQLTK();
        btnGhiTK.setEnabled(false);
        btnHuy_TK.setEnabled(false);
    }

    private void loadQLNL() {
        btnGhiNL.setEnabled(false);
        btnHuyNL.setEnabled(false);
        txtSLB_NL.setEnabled(false);
        loadTablePH_trongNgay();
    }

    private void loadTTCaNhan(String userName) {
        DSTAIKHOAN tk = tk_data.getTK(userName);
        String chucVu = tk.getChucVu();
        if (chucVu.equals("NV")) {
            lb_ChucVu.setText("Nhân viên:");
        } else if (chucVu.equals("QL")) {
            lb_ChucVu.setText("  Quản lý:");
            btn_OpenQLNL.setVisible(true);
            btn_OpenQLDU.setVisible(true);
            btn_OpenQLTK.setVisible(true);
            btn_OpenThongKe.setVisible(true);
        }
        lb_MaNV.setText(tk.getUserName().trim());
        txtUserName.setText(tk.getUserName().trim());
        txtFullName.setText(tk.getFullName().trim());
        txtPass1.setText(tk.getPassWord().trim());
        txtPass2.setText(tk.getPassWord().trim());
    }

    private void loadNhapNL() {
        loadDataTableKho();
        loadDataTablePN();
    }

    private void loadQLDU() {
        loadCBDM(cbDanhMuc);
        loadCBDM(cbDanhMuc_CT);
        loadDataTableDU_KoCT();
        loadDataTableNL_CTDU();
    }

    private void loadOrder() {
        loadCBDM(cbDanhMuc_PX);
        loadDataTablePX_trongNgay();
    }

    //===== load Combobox =====
    private void loadCBDM(JComboBox cbox) {
        cbox.removeAllItems();
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT TENDM FROM DANHMUC");
            while (rs.next()) {
                cbox.addItem(rs.getString("TENDM").trim());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi loadCBDM!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCBQLTK() {
        cbChucVu_TK.removeAllItems();
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT DISTINCT CHUCVU FROM DSTAIKHOAN");
            while (rs.next()) {
                String chucVu = rs.getString("CHUCVU").trim();
                if (chucVu.equals("QL")) {
                    cbChucVu_TK.addItem("Quản lý");
                } else {
                    cbChucVu_TK.addItem("Nhân viên");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi loadCB_QLTK!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }
    //=========================

    //===== load data Table =====
    // Table đồ uống PHIẾU XUẤT
    private void loadDataTable_DUPX(String maDM) {
        du_Data.docListDU();
        DefaultTableModel dtm = (DefaultTableModel) tb_DSDU.getModel();
        dtm.setNumRows(0);
        ArrayList<DOUONG> dsdu = du_Data.sp_getListDU_PX(maDM);
        for (DOUONG du : dsdu) {
            Vector vec = new Vector();
            vec.add(du.getMaDU());
            vec.add(du.getTenDU());
            vec.add(du.getGiaTien());
            vec.add(du.getGiaSale());
            dtm.addRow(vec);
        }
        tb_DSDU.setModel(dtm);
    }

    // Table xem lại phiếu xuất
    private void loadDataTablePX_trongNgay() {
        Date date = new Date();
        String ngay = format1.format(date);
        DefaultTableModel dtm = (DefaultTableModel) tb_PX.getModel();
        dtm.setNumRows(0);
        ArrayList<PHIEUXUAT> dspx = px_data.sp_getListPX_trongNgay(ngay);
        if (dspx != null) {
            for (PHIEUXUAT px : dspx) {
                Vector vec = new Vector();
                vec.add(px.getMaPX());
                vec.add(px.getMaNV());
                dtm.addRow(vec);
            }
            tb_PX.setModel(dtm);
        }
    }

    private void loadDataTablePX_TG(String tuNgay, String denNgay) {
        DefaultTableModel dtm = (DefaultTableModel) tb_PX.getModel();
        dtm.setNumRows(0);
        ArrayList<PHIEUXUAT> dspx = px_data.sp_getListPX_TG(tuNgay, denNgay);
        if (dspx != null) {
            for (PHIEUXUAT px : dspx) {
                Vector vec = new Vector();
                vec.add(px.getMaPX());
                vec.add(px.getMaNV());
                dtm.addRow(vec);
            }
            tb_PX.setModel(dtm);
        }
    }

    // Table xem lại PHIẾU NHẬP
    private void loadDataTablePN() {
        Date date = new Date();
        String ngay = format1.format(date);
        DefaultTableModel dtm = (DefaultTableModel) tb_PN.getModel();
        dtm.setNumRows(0);
        ArrayList<PHIEUNHAP> dspn = pn_data.sp_getListPN_TrongNgay(ngay);
        if (dspn != null) {
            for (PHIEUNHAP pn : dspn) {
                Vector vec = new Vector();
                vec.add(pn.getMaPN());
                vec.add(pn.getMaNV());
                dtm.addRow(vec);
            }
            tb_PN.setModel(dtm);
        }
    }

    private void loadDataTablePN_TG(String tuNgay, String denNgay) {
        DefaultTableModel dtm = (DefaultTableModel) tb_PN.getModel();
        dtm.setNumRows(0);
        ArrayList<PHIEUNHAP> dspn = pn_data.sp_getListPN_TG(tuNgay, denNgay);
        if (dspn != null) {
            for (PHIEUNHAP pn : dspn) {
                Vector vec = new Vector();
                vec.add(pn.getMaPN());
                vec.add(pn.getMaNV());
                dtm.addRow(vec);
            }
            tb_PN.setModel(dtm);
        }
    }

    // Table xem lại phiếu huỷ nguyên liệu
    private void loadTablePH_trongNgay() {
        Date date = new Date();
        String ngay = format1.format(date);
        DefaultTableModel dtm = (DefaultTableModel) tb_PH.getModel();
        dtm.setNumRows(0);
        ArrayList<PHIEUXUAT> dsph = px_data.sp_getListPH_TrongNgay(ngay);
        if (dsph != null) {
            for (PHIEUXUAT ph : dsph) {
                Vector vec = new Vector();
                vec.add(ph.getMaPX());
                vec.add(ph.getMaNV());
                dtm.addRow(vec);
            }
            tb_PH.setModel(dtm);
        }
    }

    private void loadTablePH_TG(String tuNgay, String denNgay) {

        DefaultTableModel dtm = (DefaultTableModel) tb_PH.getModel();
        dtm.setNumRows(0);
        ArrayList<PHIEUXUAT> dsph = px_data.sp_getListPH_TG(tuNgay, denNgay);
        if (dsph != null) {
            for (PHIEUXUAT ph : dsph) {
                Vector vec = new Vector();
                vec.add(ph.getMaPX());
                vec.add(ph.getMaNV());
                dtm.addRow(vec);
            }
            tb_PH.setModel(dtm);
        }
    }

    // Table nguyên liệu
    private void loadDataTableKho() {
        DefaultTableModel dtm = (DefaultTableModel) tb_NL.getModel();
        DefaultTableModel dtm1 = (DefaultTableModel) tb_QLNL.getModel();
        dtm.setNumRows(0);
        dtm1.setNumRows(0);
        ArrayList<NGUYENLIEU> dsnl = nl_Data.sp_getListNL();
        for (NGUYENLIEU nl : dsnl) {
            Vector vec = new Vector();
            vec.add(nl.getMaNL());
            vec.add(nl.getTenNL());
            vec.add(nl.getDvt());
            vec.add(nl.getSoLuongTon());
            dtm.addRow(vec);

            Vector vec1 = new Vector();
            vec1.add(nl.getMaNL());
            vec1.add(nl.getTenNL());
            vec1.add(nl.getDvt());
            vec1.add(nl.getSoLuongTon());
            dtm1.addRow(vec1);
        }
        tb_NL.setModel(dtm);
        tb_QLNL.setModel(dtm1);
    }

    private void loadDataTableKho_TheoTen(JTable tb, String tenNL) {
        DefaultTableModel dtm = (DefaultTableModel) tb.getModel();
        dtm.setNumRows(0);
        ArrayList<NGUYENLIEU> dsnl = nl_Data.getNLTheoTen(tenNL);
        for (NGUYENLIEU nl : dsnl) {
            Vector vec = new Vector();
            vec.add(nl.getMaNL());
            vec.add(nl.getTenNL());
            vec.add(nl.getDvt());
            vec.add(nl.getSoLuongTon());
            dtm.addRow(vec);
        }
        tb.setModel(dtm);
    }

    // Table trong quản lý đồ uống
    // Table đồ uống theo danh mục
    private void loadDataTableDU_DM(String maDM) {
        du_Data.docListDU();
        DefaultTableModel dtm = (DefaultTableModel) tb_QLDU.getModel();
        dtm.setNumRows(0);
        ArrayList<DOUONG> dsdu = du_Data.sp_getListDU_DM(maDM);
        for (DOUONG du : dsdu) {
            Vector vec = new Vector();
            vec.add(du.getMaDU());
            vec.add(du.getTenDU());
            int ttxoa = du.getTtXoa();
            if (ttxoa == 0) {
                vec.add("Không bị xoá");
            } else if (ttxoa == 1) {
                vec.add("Bị xoá");
            } else if (ttxoa == 2) {
                vec.add("Chưa có công thức");
            } else {
                vec.add("Không xác định");
            }
            vec.add(du.getGiaTien());
            vec.add(du.getGiaSale());
            dtm.addRow(vec);
        }
        tb_QLDU.setModel(dtm);
    }
    // Table đồ uống có công thức và không công thức

    private void loadDataTableDU_CoCT(String maDM) {
        DefaultTableModel dtm = (DefaultTableModel) tb_DU_CoCT.getModel();
        dtm.setNumRows(0);
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT DOUONG.MADU, DOUONG.TENDU FROM DOUONG,CT_DOUONG"
                    + " WHERE DOUONG.MADM = '" + maDM + "' AND DOUONG.TTXOA = '0' AND CT_DOUONG.MADU = DOUONG.MADU"
                    + " GROUP BY DOUONG.MADU, DOUONG.TENDU");
            while (rs.next()) {
                Vector vec = new Vector();
                vec.add(rs.getString(1).trim());
                vec.add(rs.getString(2).trim());

                dtm.addRow(vec);
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách đồ uống có công thức!");
        }
        tb_DU_CoCT.setModel(dtm);
    }

    private void loadDataTableDU_KoCT() {
        DefaultTableModel dtm = (DefaultTableModel) tb_DU_KoCT.getModel();
        dtm.setNumRows(0);
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT DOUONG.MADU, DOUONG.TENDU FROM DOUONG WHERE TTXOA = '2'");
            while (rs.next()) {
                Vector vec = new Vector();
                vec.add(rs.getString(1).trim());
                vec.add(rs.getString(2).trim());

                dtm.addRow(vec);
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách đồ uống chưa có công thức!");
        }
        tb_DU_KoCT.setModel(dtm);
    }
    // Table công thức đồ uống

    private void loadDataTable_CTDU(String maDU) {
        ctdu_data.docListCTDU();
        DefaultTableModel dtm = (DefaultTableModel) tb_CTDU.getModel();
        dtm.setNumRows(0);
        ArrayList<CT_DOUONG> ctdu = ctdu_data.getCTDU(maDU);
        for (CT_DOUONG ct : ctdu) {
            Vector vec = new Vector();
            vec.add(ct.getMaNL());
            String tenNL = nl_Data.maNL_to_tenNL(ct.getMaNL());
            vec.add(tenNL);
            vec.add(ct.getSoLuong());
            dtm.addRow(vec);
        }
    }
    // Table nguyên liệu trong công thức pha chế

    private void loadDataTableNL_CTDU() {
        DefaultTableModel dtm = (DefaultTableModel) tb_NL_CTDU.getModel();
        dtm.setNumRows(0);
        ArrayList<NGUYENLIEU> dsnl = nl_Data.sp_getListNL();
        for (NGUYENLIEU nl : dsnl) {
            Vector vec = new Vector();
            vec.add(nl.getMaNL());
            vec.add(nl.getTenNL());
            vec.add(nl.getDvt());
            dtm.addRow(vec);
        }
        tb_NL_CTDU.setModel(dtm);
    }

    // Table trong Quản lý tài khoản
    private void loadDataTableDSTK() {
        tk_data.docListTK();
        DefaultTableModel dtm = (DefaultTableModel) tb_DSTK.getModel();
        dtm.setNumRows(0);
        ArrayList<DSTAIKHOAN> dstk = tk_data.sp_getListTK();
        for (DSTAIKHOAN tk : dstk) {
            Vector vec = new Vector();
            vec.add(tk.getUserName());
            vec.add(tk.getFullName());
            String chucVu = tk.getChucVu();
            if (chucVu.equals("QL")) {
                vec.add("Quản lý");
            } else {
                vec.add("Nhân viên");
            }
            int ttxoa = tk.getTtXoa();
            if (ttxoa == 0) {
                vec.add("Không bị khóa");
            } else {
                vec.add("Bị khóa");
            }
            dtm.addRow(vec);
        }
        tb_DSTK.setModel(dtm);
    }

    // Table thống kê
    // Table top 5 đồ uống bán chạy
    private void loadTableTop5(String tuNgay, String denNgay) {
        DefaultTableModel dtm = (DefaultTableModel) tb_TOP5DU.getModel();
        dtm.setNumRows(0);
        Connection conn = CONNECTION.getConnection();
        String sql = "EXEC dbo.SP_TOP5DU @TUNGAY = '" + tuNgay + "', @DENNGAY ='" + denNgay + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int i = 0;
            while (rs.next()) {
                i++;
                Vector vt = new Vector();
                vt.add(i);
                vt.add(rs.getString("MADU").trim());
                vt.add(rs.getString("TENDU").trim());
                vt.add(rs.getString("DABAN"));
                dtm.addRow(vt);
            }

            tb_TOP5DU.setModel(dtm);
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi lấy danh sách top 5 đồ uống bán chạy!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }
    // Table tổng tiền nhập xuất

    private void loadTableTienNhap(String tuNgay, String denNgay) {
        DefaultTableModel dtm = (DefaultTableModel) tb_TKTienNhap.getModel();
        dtm.setNumRows(0);
        Connection conn = CONNECTION.getConnection();
        String sql = "EXEC dbo.SP_TONGTIENNHAP @TUNGAY = '" + tuNgay + "', @DENNGAY ='" + denNgay + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int i = 0;
            while (rs.next()) {
                i++;
                Vector vt = new Vector();
                vt.add(i);
                vt.add(rs.getString("MAPN").trim());
                vt.add(rs.getString("THANHTIEN"));
                dtm.addRow(vt);
            }

            tb_TKTienNhap.setModel(dtm);
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi lấy danh sách tổng tiền nhập nguyên liệu!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTableTienXuat(String tuNgay, String denNgay) {
        DefaultTableModel dtm = (DefaultTableModel) tb_TKTienBan.getModel();
        dtm.setNumRows(0);
        Connection conn = CONNECTION.getConnection();
        String sql = "EXEC dbo.SP_TONGTIENBAN @TUNGAY = '" + tuNgay + "', @DENNGAY ='" + denNgay + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int i = 0;
            while (rs.next()) {
                i++;
                Vector vt = new Vector();
                vt.add(i);
                vt.add(rs.getString("MAPX").trim());
                vt.add(rs.getString("THANHTIEN"));
                dtm.addRow(vt);
            }

            tb_TKTienBan.setModel(dtm);
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi lấy danh sách tổng tiền bán đồ uống!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }
    // Table tổng nhập, xuất, huỷ nguyên liệu

    private void loadTableTongNhapNL(String tuNgay, String denNgay) {
        DefaultTableModel dtm = (DefaultTableModel) tb_TongNhapNL.getModel();
        dtm.setNumRows(0);
        Connection conn = CONNECTION.getConnection();
        String sql = "EXEC dbo.SP_TONGNHAPNL @TUNGAY = '" + tuNgay + "', @DENNGAY ='" + denNgay + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int i = 0;
            while (rs.next()) {
                i++;
                Vector vt = new Vector();
                vt.add(i);
                vt.add(rs.getString("MANL").trim());
                vt.add(rs.getString("TENNL").trim());
                vt.add(rs.getString("SLN"));
                dtm.addRow(vt);
            }

            tb_TongNhapNL.setModel(dtm);
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi lấy danh sách tổng nhập nguyên liệu!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTableTongXuatNL(String tuNgay, String denNgay) {
        DefaultTableModel dtm = (DefaultTableModel) tb_TongXuatNL.getModel();
        dtm.setNumRows(0);
        Connection conn = CONNECTION.getConnection();
        String sql = "EXEC dbo.SP_TONGXUATNL @TUNGAY = '" + tuNgay + "', @DENNGAY ='" + denNgay + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int i = 0;
            while (rs.next()) {
                i++;
                Vector vt = new Vector();
                vt.add(i);
                vt.add(rs.getString("MANL").trim());
                vt.add(rs.getString("TENNL").trim());
                vt.add(rs.getString("SLX"));
                dtm.addRow(vt);
            }

            tb_TongXuatNL.setModel(dtm);
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi lấy danh sách tổng xuất nguyên liệu!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTableTongHuyNL(String tuNgay, String denNgay) {
        DefaultTableModel dtm = (DefaultTableModel) tb_TongHuyNL.getModel();
        dtm.setNumRows(0);
        Connection conn = CONNECTION.getConnection();
        String sql = "EXEC dbo.SP_TONGHUYNL @TUNGAY = '" + tuNgay + "', @DENNGAY ='" + denNgay + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int i = 0;
            while (rs.next()) {
                i++;
                Vector vt = new Vector();
                vt.add(i);
                vt.add(rs.getString("MANL").trim());
                vt.add(rs.getString("TENNL").trim());
                vt.add(rs.getString("SLH"));
                dtm.addRow(vt);
            }

            tb_TongHuyNL.setModel(dtm);
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi lấy danh sách tổng huỷ nguyên liệu!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    //===========================
    private ImageIcon getAnhDU(String src) {
        src = src.trim().equals("") ? "default.png" : src;
        //Xử lý ảnh
        BufferedImage img = null;
        File fileImg = new File(src);

        if (!fileImg.exists()) {
            src = "default.png";
            fileImg = new File("src/Images/" + src);
        }

        try {
            img = ImageIO.read(fileImg);
            fileAnhDU = new File(src);
        } catch (IOException e) {
            fileAnhDU = new File("src/Images/default.png");
        }

        if (img != null) {
            Image dimg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            return new ImageIcon(dimg);
        }
        return null;
    }

    private void luuFileAnh() {
        BufferedImage bImage = null;
        try {
            File initialImage = new File(fileAnhDU.getPath());
            bImage = ImageIO.read(initialImage);

            ImageIO.write(bImage, "png", new File("src/Images/" + fileAnhDU.getName()));

        } catch (IOException e) {
            System.out.println("Exception occured :" + e.getMessage());
        }
    }

    private boolean checkDate(String dateStr) {
        format1.setLenient(false);
        try {
            format1.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public String inCTHD(String maPX) {
        PHIEUXUAT px = px_data.timPhieuXuat(maPX);
        String maNV = px.getMaNV().trim();
        String ngayXuat = px.getNgayXuat().trim();
        ArrayList<CTHD> cthd = PHIEUXUAT_DATA.getCTHD(maPX);
        int tongTien = 0;
        int i = 0;
        String hd = "<div style=\"text-align: center\"><h1> CHI TIẾT HOÁ ĐƠN </h1></div>\n";
        hd += "Mã nhân viên: " + maNV + "<br>" + "\n";
        hd += "Ngày lập: " + ngayXuat + "<br>" + "\n";
        hd += "<table border=\"1\">\n"
                + "    <thead>\n"
                + "        <tr>\n"
                + "            <th>STT</th>\n"
                + "            <th>Tên đồ uống</th>\n"
                + "            <th>Số lượng</th>\n"
                + "            <th>Đơn giá</th>\n"
                + "            <th>Thành tiền</th>\n"
                + "        </tr>\n"
                + "    </thead>\n";
        for (CTHD ct : cthd) {
            i++;
            int thanhTien = 0;
            String maDU = ct.getMaDU();
            String tenDU = du_Data.maDU_to_tenDU(maDU);
            thanhTien = ct.getDonGia() * ct.getSoLuong();
            hd += "<tr>";
            hd += "<td style='text-align:center;'>" + i + "</td>";
            hd += "<td style='text-align:left;'>" + tenDU + "</td>";
            hd += "<td style='text-align:center;'>" + ct.getSoLuong() + "</td>";
            hd += "<td style='text-align:center;'>" + ct.getDonGia() + "</td>";
            hd += "<td style='text-align:center;'>" + (ct.getDonGia() * ct.getSoLuong()) + "</td>";
            hd += "</tr>";
            tongTien += thanhTien;
        }
        hd += "    <tfoot>\n"
                + "        <tr>\n"
                + "            <th colspan=\"4\">Thành tiền đơn hàng</th>\n";
        hd += "            <th colspan=\"1\">" + tongTien + "</th>\n"
                + "\n"
                + "        </tr>\n"
                + "    </tfoot>\n"
                + "</table>\n";
        return hd;
    }

    public String inCTPX(String maPX) {
        PHIEUXUAT px = px_data.timPhieuXuat(maPX);
        String maNV = px.getMaNV().trim();
        String ngayXuat = px.getNgayXuat().trim();
        ArrayList<CTPX> ctpx = PHIEUXUAT_DATA.getCTPX(maPX);
        int tongTien = 0;
        int i = 0;
        String hd = "<div style=\"text-align: center\"><h1> CHI TIẾT PHIẾU XUẤT </h1></div>\n";
        hd += "Mã nhân viên: " + maNV + "<br>" + "\n";
        hd += "Ngày lập: " + ngayXuat + "<br>" + "\n";
        hd += "<table border=\"1\">\n"
                + "    <thead>\n"
                + "        <tr>\n"
                + "            <th>STT</th>\n"
                + "            <th>Tên nguyên liệu</th>\n"
                + "            <th>Số lượng</th>\n"
                + "        </tr>\n"
                + "    </thead>\n";
        for (CTPX ct : ctpx) {
            i++;
            String maNL = ct.getMaNL().trim();
            NGUYENLIEU nl = nl_Data.timNguyenlieu(maNL);
            String dvt = nl.getDvt();
            hd += "<tr>";
            hd += "<td style='text-align:center;'>" + i + "</td>";
            hd += "<td style='text-align:left;'>" + nl.getTenNL() + "</td>";
            hd += "<td style='text-align:center;'>" + ct.getSoLuong() + " " + dvt + "</td>";
            hd += "</tr>";
        }
        hd += "</table>\n";
        return hd;
    }

    public String inCTPH(String maPH, String maNV) {
        ArrayList<CTPX> ctph = PHIEUXUAT_DATA.getCTPH(maPH);
        int i = 0;
        String hd = "<div style=\"text-align: center\"><h1> CHI TIẾT PHIẾU XUẤT </h1></div>\n";
        hd += "Mã nhân viên: " + maNV + "<br>" + "\n";
        hd += "Phiếu huỷ: " + maPH + "<br>" + "\n";
        hd += "<table border=\"1\">\n"
                + "    <thead>\n"
                + "        <tr>\n"
                + "            <th>STT</th>\n"
                + "            <th>Tên nguyên liệu</th>\n"
                + "            <th>Số lượng</th>\n"
                + "        </tr>\n"
                + "    </thead>\n";
        for (CTPX ct : ctph) {
            i++;
            String maNL = ct.getMaNL().trim();
            NGUYENLIEU nl = nl_Data.timNguyenlieu(maNL);
            String dvt = nl.getDvt();
            hd += "<tr>";
            hd += "<td style='text-align:center;'>" + i + "</td>";
            hd += "<td style='text-align:left;'>" + nl.getTenNL() + "</td>";
            hd += "<td style='text-align:center;'>" + ct.getSoLuong() + " " + dvt + "</td>";
            hd += "</tr>";
        }
        hd += "</table>\n";
        return hd;
    }

    public String inCTPN(String maPN) {
        PHIEUNHAP pn = pn_data.timPhieuNhap(maPN);
        String maNV = pn.getMaNV().trim();
        String ngayNhap = pn.getNgayNhap().trim();
        ArrayList<CTPN> ctpn = CTPN_DATA.getCTPN(maPN);
        int tongTien = 0;
        int i = 0;
        String hd = "<div style=\"text-align: center\"><h1> CHI TIẾT PHIẾU NHẬP </h1></div>\n";
        hd += "Mã nhân viên: " + maNV + "<br>" + "\n";
        hd += "Ngày lập: " + ngayNhap + "<br>" + "\n";
        hd += "<table border=\"1\">\n"
                + "    <thead>\n"
                + "        <tr>\n"
                + "            <th>STT</th>\n"
                + "            <th>Tên nguyên liệu</th>\n"
                + "            <th>Đơn giá</th>\n"
                + "            <th>Số lượng</th>\n"
                + "            <th>Thành tiền</th>\n"
                + "        </tr>\n"
                + "    </thead>\n";
        for (CTPN ct : ctpn) {
            i++;
            int thanhTien = 0;
            String maNL = ct.getMaNL().trim();
            NGUYENLIEU nl = nl_Data.timNguyenlieu(maNL);
            String dvt = nl.getDvt();
            thanhTien = ct.getDonGia() * ct.getSoLuong();
            hd += "<tr>";
            hd += "<td style='text-align:center;'>" + i + "</td>";
            hd += "<td style='text-align:left;'>" + nl.getTenNL() + "</td>";
            hd += "<td style='text-align:center;'>" + ct.getDonGia() + "</td>";
            hd += "<td style='text-align:center;'>" + ct.getSoLuong() + " " + dvt + "</td>";
            hd += "<td style='text-align:center;'>" + (ct.getDonGia() * ct.getSoLuong()) + "</td>";
            hd += "</tr>";
            tongTien += thanhTien;
        }
        hd += "    <tfoot>\n"
                + "        <tr>\n"
                + "            <th colspan=\"4\">Thành tiền đơn hàng</th>\n";
        hd += "            <th colspan=\"1\">" + tongTien + "</th>\n"
                + "\n"
                + "        </tr>\n"
                + "    </tfoot>\n"
                + "</table>\n";
        return hd;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnMenu = new javax.swing.JPanel();
        icon = new javax.swing.JLabel();
        btn_OpenOrDer = new javax.swing.JPanel();
        lb_Order = new javax.swing.JLabel();
        btn_OpenNhapNL = new javax.swing.JPanel();
        lb_NhapNL = new javax.swing.JLabel();
        btn_OpenQLNL = new javax.swing.JPanel();
        lb_QLNL = new javax.swing.JLabel();
        btn_OpenQLDU = new javax.swing.JPanel();
        lb_QLDU = new javax.swing.JLabel();
        btn_OpenQLTK = new javax.swing.JPanel();
        lb_QLTK = new javax.swing.JLabel();
        btn_OpenThongKe = new javax.swing.JPanel();
        lb_ThongKe = new javax.swing.JLabel();
        btn_OpenTT = new javax.swing.JPanel();
        lb_ChucVu = new javax.swing.JLabel();
        lb_MaNV = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        PnTong = new javax.swing.JPanel();
        pnOrder = new javax.swing.JPanel();
        tab_Xuat = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tb_DSDU = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        tb_CTHD = new javax.swing.JTable();
        jScrollPane14 = new javax.swing.JScrollPane();
        tb_CTPX = new javax.swing.JTable();
        cbDanhMuc_PX = new javax.swing.JComboBox<>();
        txtMaDU_PX = new javax.swing.JTextField();
        txtSoLuong_PX = new javax.swing.JTextField();
        txtTenDU_PX = new javax.swing.JTextField();
        txtGia_PX = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        btnThemHD = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        lb_TongTienPX = new javax.swing.JLabel();
        btnGhiPX = new javax.swing.JButton();
        btnHuyPX = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        tb_PX = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        txtTuNgayPX = new javax.swing.JTextField();
        txtDenNgayPX = new javax.swing.JTextField();
        btnReload_PX = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        editorCTHD = new javax.swing.JEditorPane();
        jScrollPane17 = new javax.swing.JScrollPane();
        editorCTPX = new javax.swing.JEditorPane();
        btnInHD = new javax.swing.JButton();
        btnInPX = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        pnNhapNL = new javax.swing.JPanel();
        tab_NhapNL = new javax.swing.JTabbedPane();
        pn_NhapNL = new javax.swing.JPanel();
        pn_NL = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tb_NL = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_GioNL = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        btnTimNL = new javax.swing.JButton();
        pn_ThongTin = new javax.swing.JPanel();
        txtGiaNhap = new javax.swing.JTextField();
        btn_GhiGioNhap = new javax.swing.JButton();
        txtTenNL = new javax.swing.JTextField();
        txtMaNL = new javax.swing.JTextField();
        txtSLNhap = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnGhiPhieuNhap = new javax.swing.JButton();
        btnXoaGioNhap = new javax.swing.JButton();
        btnHuyPN = new javax.swing.JButton();
        pn_XemPN = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tb_PN = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtTuNgay = new javax.swing.JTextField();
        txtDenNgay = new javax.swing.JTextField();
        btnReloadPN = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnInPN = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        editorCTPN = new javax.swing.JEditorPane();
        pnQLNL = new javax.swing.JPanel();
        tab_QLNL = new javax.swing.JTabbedPane();
        pnNguyenLieu = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtTimKiem_QLNL = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        tb_QLNL = new javax.swing.JTable();
        btnTimNL_QLNL = new javax.swing.JButton();
        txtMaNL_QL = new javax.swing.JTextField();
        txtTenNL_QL = new javax.swing.JTextField();
        txtDVT_QL = new javax.swing.JTextField();
        btnThemNL = new javax.swing.JButton();
        btnSuaNL = new javax.swing.JButton();
        btnXoaNL = new javax.swing.JButton();
        btnGhiNL = new javax.swing.JButton();
        btnHuyNL = new javax.swing.JButton();
        btnCapNhatSL_NL = new javax.swing.JButton();
        txtSLB_NL = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane19 = new javax.swing.JScrollPane();
        tb_CTPH = new javax.swing.JTable();
        jLabel23 = new javax.swing.JLabel();
        pn_XemPH = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane20 = new javax.swing.JScrollPane();
        tb_PH = new javax.swing.JTable();
        jLabel24 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        txtTuNgayPH = new javax.swing.JTextField();
        txtDenNgayPH = new javax.swing.JTextField();
        btnReloadPN1 = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        btnInPH = new javax.swing.JButton();
        jScrollPane21 = new javax.swing.JScrollPane();
        editorCTPH = new javax.swing.JEditorPane();
        pnQLDU = new javax.swing.JPanel();
        tab_QLDU = new javax.swing.JTabbedPane();
        pn_QLDU = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tb_QLDU = new javax.swing.JTable();
        imgDU = new javax.swing.JLabel();
        btnChonAnh = new javax.swing.JButton();
        cbDanhMuc = new javax.swing.JComboBox<>();
        txtMaDU = new javax.swing.JTextField();
        txtTenDU = new javax.swing.JTextField();
        cbTTXoa_DU = new javax.swing.JComboBox<>();
        txtGiaBan = new javax.swing.JTextField();
        txtGiaSale = new javax.swing.JTextField();
        btnThemDU = new javax.swing.JButton();
        btnSuaDU = new javax.swing.JButton();
        btnXoaDU = new javax.swing.JButton();
        btnHuyDU = new javax.swing.JButton();
        btnGhiDU = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        pn_CTDU = new javax.swing.JPanel();
        pnDU_KoCT = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tb_DU_KoCT = new javax.swing.JTable();
        pnDU_CoCT = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tb_DU_CoCT = new javax.swing.JTable();
        cbDanhMuc_CT = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        pnDU_DSNL = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tb_NL_CTDU = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        txtTenNL_CTDU = new javax.swing.JTextField();
        txtMaNL_CTDU = new javax.swing.JTextField();
        txtSL_CTDU = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        pnCTPC = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        tb_CTDU = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        lbMADU_CT = new javax.swing.JLabel();
        btnXoa_CTDU = new javax.swing.JButton();
        btnHuyCTDU = new javax.swing.JButton();
        btnGhiCTDU = new javax.swing.JButton();
        btnThem_CTDU = new javax.swing.JButton();
        pnQLTK = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tb_DSTK = new javax.swing.JTable();
        txtUsername_TK = new javax.swing.JTextField();
        txtFullname_TK = new javax.swing.JTextField();
        txtPass_TK = new javax.swing.JPasswordField();
        cbChucVu_TK = new javax.swing.JComboBox<>();
        cbTTXOA_Tk = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        btnThemTK = new javax.swing.JButton();
        btnSuaTK = new javax.swing.JButton();
        btnXoaTK = new javax.swing.JButton();
        btnGhiTK = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        btnHuy_TK = new javax.swing.JButton();
        pnThongKe = new javax.swing.JPanel();
        tab_ThongKe = new javax.swing.JTabbedPane();
        pnTOP5DU = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane18 = new javax.swing.JScrollPane();
        tb_TOP5DU = new javax.swing.JTable();
        jLabel22 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        txt_FromTop5 = new javax.swing.JTextField();
        txt_ToTop5 = new javax.swing.JTextField();
        pnTongNhapNL = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane22 = new javax.swing.JScrollPane();
        tb_TongNhapNL = new javax.swing.JTable();
        jLabel25 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane23 = new javax.swing.JScrollPane();
        tb_TongXuatNL = new javax.swing.JTable();
        jLabel26 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane24 = new javax.swing.JScrollPane();
        tb_TongHuyNL = new javax.swing.JTable();
        jLabel27 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        txt_TuNgayNXNL = new javax.swing.JTextField();
        txt_DenNgayNXNL = new javax.swing.JTextField();
        pnTongTien = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane25 = new javax.swing.JScrollPane();
        tb_TKTienNhap = new javax.swing.JTable();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        lbTongTienNhap = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane26 = new javax.swing.JScrollPane();
        tb_TKTienBan = new javax.swing.JTable();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        lbTongTienBan = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        txt_TuNgayTKTien = new javax.swing.JTextField();
        txt_DenNgayTKTien = new javax.swing.JTextField();
        pnTTCaNhan = new javax.swing.JPanel();
        txtUserName = new javax.swing.JTextField();
        txtFullName = new javax.swing.JTextField();
        txtPass1 = new javax.swing.JPasswordField();
        txtPass2 = new javax.swing.JPasswordField();
        btnCapNhat_TTCaNhan = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mew2Coffee");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        pnMenu.setBackground(new java.awt.Color(254, 235, 208));
        pnMenu.setPreferredSize(new java.awt.Dimension(250, 1000));

        icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Main/Logo_Mew2Coffee_250.png"))); // NOI18N

        btn_OpenOrDer.setBackground(new java.awt.Color(254, 235, 208));

        lb_Order.setBackground(new java.awt.Color(255, 255, 255));
        lb_Order.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lb_Order.setForeground(new java.awt.Color(0, 0, 102));
        lb_Order.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_Order.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/order1.png"))); // NOI18N
        lb_Order.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lb_Order.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lb_OrderMouseMoved(evt);
            }
        });
        lb_Order.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_OrderMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lb_OrderMouseExited(evt);
            }
        });

        javax.swing.GroupLayout btn_OpenOrDerLayout = new javax.swing.GroupLayout(btn_OpenOrDer);
        btn_OpenOrDer.setLayout(btn_OpenOrDerLayout);
        btn_OpenOrDerLayout.setHorizontalGroup(
            btn_OpenOrDerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_OpenOrDerLayout.createSequentialGroup()
                .addComponent(lb_Order, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        btn_OpenOrDerLayout.setVerticalGroup(
            btn_OpenOrDerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lb_Order, javax.swing.GroupLayout.PREFERRED_SIZE, 57, Short.MAX_VALUE)
        );

        btn_OpenNhapNL.setBackground(new java.awt.Color(254, 235, 208));

        lb_NhapNL.setBackground(new java.awt.Color(254, 235, 208));
        lb_NhapNL.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lb_NhapNL.setForeground(new java.awt.Color(0, 0, 102));
        lb_NhapNL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/nhaphang.png"))); // NOI18N
        lb_NhapNL.setText("Nhập nguyên liệu");
        lb_NhapNL.setFocusable(false);
        lb_NhapNL.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lb_NhapNLMouseMoved(evt);
            }
        });
        lb_NhapNL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_NhapNLMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lb_NhapNLMouseExited(evt);
            }
        });

        javax.swing.GroupLayout btn_OpenNhapNLLayout = new javax.swing.GroupLayout(btn_OpenNhapNL);
        btn_OpenNhapNL.setLayout(btn_OpenNhapNLLayout);
        btn_OpenNhapNLLayout.setHorizontalGroup(
            btn_OpenNhapNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lb_NhapNL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        btn_OpenNhapNLLayout.setVerticalGroup(
            btn_OpenNhapNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lb_NhapNL)
        );

        btn_OpenQLNL.setBackground(new java.awt.Color(254, 235, 208));

        lb_QLNL.setBackground(new java.awt.Color(255, 255, 255));
        lb_QLNL.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lb_QLNL.setForeground(new java.awt.Color(0, 0, 102));
        lb_QLNL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/supplies.png"))); // NOI18N
        lb_QLNL.setText("Nguyên liệu");
        lb_QLNL.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lb_QLNLMouseMoved(evt);
            }
        });
        lb_QLNL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_QLNLMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lb_QLNLMouseExited(evt);
            }
        });

        javax.swing.GroupLayout btn_OpenQLNLLayout = new javax.swing.GroupLayout(btn_OpenQLNL);
        btn_OpenQLNL.setLayout(btn_OpenQLNLLayout);
        btn_OpenQLNLLayout.setHorizontalGroup(
            btn_OpenQLNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lb_QLNL, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
        );
        btn_OpenQLNLLayout.setVerticalGroup(
            btn_OpenQLNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_OpenQLNLLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lb_QLNL))
        );

        btn_OpenQLDU.setBackground(new java.awt.Color(254, 235, 208));

        lb_QLDU.setBackground(new java.awt.Color(254, 235, 208));
        lb_QLDU.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lb_QLDU.setForeground(new java.awt.Color(0, 0, 102));
        lb_QLDU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/order.png"))); // NOI18N
        lb_QLDU.setText("Đồ Uống");
        lb_QLDU.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lb_QLDUMouseMoved(evt);
            }
        });
        lb_QLDU.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_QLDUMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lb_QLDUMouseExited(evt);
            }
        });

        javax.swing.GroupLayout btn_OpenQLDULayout = new javax.swing.GroupLayout(btn_OpenQLDU);
        btn_OpenQLDU.setLayout(btn_OpenQLDULayout);
        btn_OpenQLDULayout.setHorizontalGroup(
            btn_OpenQLDULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lb_QLDU, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        btn_OpenQLDULayout.setVerticalGroup(
            btn_OpenQLDULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_OpenQLDULayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lb_QLDU))
        );

        btn_OpenQLTK.setBackground(new java.awt.Color(254, 235, 208));

        lb_QLTK.setBackground(new java.awt.Color(254, 235, 208));
        lb_QLTK.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lb_QLTK.setForeground(new java.awt.Color(0, 0, 102));
        lb_QLTK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Staffs.png"))); // NOI18N
        lb_QLTK.setText("Tài khoản");
        lb_QLTK.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lb_QLTKMouseMoved(evt);
            }
        });
        lb_QLTK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_QLTKMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lb_QLTKMouseExited(evt);
            }
        });

        javax.swing.GroupLayout btn_OpenQLTKLayout = new javax.swing.GroupLayout(btn_OpenQLTK);
        btn_OpenQLTK.setLayout(btn_OpenQLTKLayout);
        btn_OpenQLTKLayout.setHorizontalGroup(
            btn_OpenQLTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lb_QLTK, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
        );
        btn_OpenQLTKLayout.setVerticalGroup(
            btn_OpenQLTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_OpenQLTKLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lb_QLTK))
        );

        btn_OpenThongKe.setBackground(new java.awt.Color(254, 235, 208));

        lb_ThongKe.setBackground(new java.awt.Color(254, 235, 208));
        lb_ThongKe.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lb_ThongKe.setForeground(new java.awt.Color(0, 0, 102));
        lb_ThongKe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/statistics.png"))); // NOI18N
        lb_ThongKe.setText("Thống kê");
        lb_ThongKe.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lb_ThongKeMouseMoved(evt);
            }
        });
        lb_ThongKe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_ThongKeMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lb_ThongKeMouseExited(evt);
            }
        });

        javax.swing.GroupLayout btn_OpenThongKeLayout = new javax.swing.GroupLayout(btn_OpenThongKe);
        btn_OpenThongKe.setLayout(btn_OpenThongKeLayout);
        btn_OpenThongKeLayout.setHorizontalGroup(
            btn_OpenThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lb_ThongKe, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
        );
        btn_OpenThongKeLayout.setVerticalGroup(
            btn_OpenThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_OpenThongKeLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lb_ThongKe))
        );

        btn_OpenTT.setBackground(new java.awt.Color(254, 235, 208));
        btn_OpenTT.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                btn_OpenTTMouseMoved(evt);
            }
        });
        btn_OpenTT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_OpenTTMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_OpenTTMouseExited(evt);
            }
        });

        lb_ChucVu.setBackground(new java.awt.Color(254, 235, 208));
        lb_ChucVu.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lb_ChucVu.setForeground(new java.awt.Color(0, 0, 102));
        lb_ChucVu.setText("Chức vụ");

        lb_MaNV.setBackground(new java.awt.Color(254, 235, 208));
        lb_MaNV.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lb_MaNV.setForeground(new java.awt.Color(0, 0, 102));
        lb_MaNV.setText("MÃ NV");
        lb_MaNV.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/staff.png"))); // NOI18N

        javax.swing.GroupLayout btn_OpenTTLayout = new javax.swing.GroupLayout(btn_OpenTT);
        btn_OpenTT.setLayout(btn_OpenTTLayout);
        btn_OpenTTLayout.setHorizontalGroup(
            btn_OpenTTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_OpenTTLayout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_ChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_MaNV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        btn_OpenTTLayout.setVerticalGroup(
            btn_OpenTTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6)
            .addGroup(btn_OpenTTLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(btn_OpenTTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_ChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_MaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout pnMenuLayout = new javax.swing.GroupLayout(pnMenu);
        pnMenu.setLayout(pnMenuLayout);
        pnMenuLayout.setHorizontalGroup(
            pnMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnMenuLayout.createSequentialGroup()
                .addGroup(pnMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnMenuLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(pnMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_OpenQLTK, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_OpenThongKe, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnMenuLayout.createSequentialGroup()
                        .addGroup(pnMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(icon)
                            .addGroup(pnMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btn_OpenQLDU, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_OpenOrDer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_OpenNhapNL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(btn_OpenQLNL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btn_OpenTT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnMenuLayout.setVerticalGroup(
            pnMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnMenuLayout.createSequentialGroup()
                .addComponent(icon)
                .addGap(25, 25, 25)
                .addComponent(btn_OpenOrDer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(btn_OpenNhapNL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(btn_OpenQLNL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(btn_OpenQLDU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(btn_OpenQLTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(btn_OpenThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_OpenTT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );

        PnTong.setBackground(new java.awt.Color(255, 255, 255));
        PnTong.setPreferredSize(new java.awt.Dimension(1203, 1000));
        PnTong.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnOrder.setBackground(new java.awt.Color(255, 255, 255));
        pnOrder.setPreferredSize(new java.awt.Dimension(1200, 1000));

        tab_Xuat.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane12.setBackground(new java.awt.Color(255, 255, 255));

        tb_DSDU.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tb_DSDU.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MADU", "Tên đồ uống", "Giá bán", "Giá sale"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_DSDU.setRowHeight(25);
        tb_DSDU.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_DSDUMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(tb_DSDU);
        if (tb_DSDU.getColumnModel().getColumnCount() > 0) {
            tb_DSDU.getColumnModel().getColumn(0).setResizable(false);
            tb_DSDU.getColumnModel().getColumn(0).setPreferredWidth(30);
            tb_DSDU.getColumnModel().getColumn(1).setResizable(false);
            tb_DSDU.getColumnModel().getColumn(1).setPreferredWidth(350);
            tb_DSDU.getColumnModel().getColumn(2).setResizable(false);
            tb_DSDU.getColumnModel().getColumn(2).setPreferredWidth(40);
            tb_DSDU.getColumnModel().getColumn(3).setResizable(false);
            tb_DSDU.getColumnModel().getColumn(3).setPreferredWidth(40);
        }

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Chọn đồ uống");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        jScrollPane13.setBackground(new java.awt.Color(255, 255, 255));

        tb_CTHD.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tb_CTHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MADU", "Tên đồ uống", "Số lượng", "Đơn giá", "Thành tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_CTHD.setRowHeight(25);
        jScrollPane13.setViewportView(tb_CTHD);
        if (tb_CTHD.getColumnModel().getColumnCount() > 0) {
            tb_CTHD.getColumnModel().getColumn(0).setResizable(false);
            tb_CTHD.getColumnModel().getColumn(0).setPreferredWidth(30);
            tb_CTHD.getColumnModel().getColumn(1).setResizable(false);
            tb_CTHD.getColumnModel().getColumn(1).setPreferredWidth(200);
            tb_CTHD.getColumnModel().getColumn(2).setResizable(false);
            tb_CTHD.getColumnModel().getColumn(2).setPreferredWidth(30);
            tb_CTHD.getColumnModel().getColumn(3).setResizable(false);
            tb_CTHD.getColumnModel().getColumn(3).setPreferredWidth(30);
            tb_CTHD.getColumnModel().getColumn(4).setResizable(false);
        }

        jScrollPane14.setBackground(new java.awt.Color(255, 255, 255));

        tb_CTPX.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tb_CTPX.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MANL", "Tên nguyên liệu", "Số lượng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_CTPX.setRowHeight(25);
        jScrollPane14.setViewportView(tb_CTPX);
        if (tb_CTPX.getColumnModel().getColumnCount() > 0) {
            tb_CTPX.getColumnModel().getColumn(0).setResizable(false);
            tb_CTPX.getColumnModel().getColumn(0).setPreferredWidth(20);
            tb_CTPX.getColumnModel().getColumn(1).setResizable(false);
            tb_CTPX.getColumnModel().getColumn(1).setPreferredWidth(200);
            tb_CTPX.getColumnModel().getColumn(2).setResizable(false);
            tb_CTPX.getColumnModel().getColumn(2).setPreferredWidth(20);
        }

        cbDanhMuc_PX.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        cbDanhMuc_PX.setBorder(javax.swing.BorderFactory.createTitledBorder("Danh mục"));
        cbDanhMuc_PX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbDanhMuc_PXActionPerformed(evt);
            }
        });

        txtMaDU_PX.setEditable(false);
        txtMaDU_PX.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtMaDU_PX.setBorder(javax.swing.BorderFactory.createTitledBorder("Mã đồ uống"));

        txtSoLuong_PX.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtSoLuong_PX.setBorder(javax.swing.BorderFactory.createTitledBorder("Số lượng"));

        txtTenDU_PX.setEditable(false);
        txtTenDU_PX.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtTenDU_PX.setBorder(javax.swing.BorderFactory.createTitledBorder("Tên đồ uống"));

        txtGia_PX.setEditable(false);
        txtGia_PX.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtGia_PX.setBorder(javax.swing.BorderFactory.createTitledBorder("Giá bán"));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Hoá đơn");
        jLabel16.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Phiếu xuất");
        jLabel19.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        btnThemHD.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnThemHD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/confirm.png"))); // NOI18N
        btnThemHD.setText("THÊM");
        btnThemHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemHDActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Tổng tiền:");
        jLabel20.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        lb_TongTienPX.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N

        btnGhiPX.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnGhiPX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/confirm.png"))); // NOI18N
        btnGhiPX.setText("XÁC NHẬN");
        btnGhiPX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGhiPXActionPerformed(evt);
            }
        });

        btnHuyPX.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnHuyPX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/cancel.png"))); // NOI18N
        btnHuyPX.setText("HUỶ");
        btnHuyPX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyPXActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane13)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(cbDanhMuc_PX, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(btnHuyPX)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(157, 157, 157)
                                .addComponent(jLabel20)
                                .addGap(18, 18, 18)
                                .addComponent(lb_TongTienPX, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(btnGhiPX)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                            .addComponent(txtMaDU_PX, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSoLuong_PX, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTenDU_PX, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGia_PX, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(144, 144, 144)
                        .addComponent(btnThemHD)))
                .addGap(20, 20, 20))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cbDanhMuc_PX, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtMaDU_PX, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTenDU_PX, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSoLuong_PX, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGia_PX, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnThemHD, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(lb_TongTienPX, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGhiPX, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHuyPX, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(263, Short.MAX_VALUE))
        );

        tab_Xuat.addTab("Xuất nguyên liệu", jPanel5);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane15.setBackground(new java.awt.Color(255, 255, 255));

        tb_PX.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tb_PX.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã phiếu xuất", "Mã nhân viên"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_PX.setRowHeight(25);
        tb_PX.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_PXMouseClicked(evt);
            }
        });
        jScrollPane15.setViewportView(tb_PX);

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel21.setText("Phiếu xuất");

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Tìm kiếm"));

        txtTuNgayPX.setColumns(10);
        txtTuNgayPX.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtTuNgayPX.setBorder(javax.swing.BorderFactory.createTitledBorder("Từ ngày"));
        txtTuNgayPX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTuNgayPXActionPerformed(evt);
            }
        });

        txtDenNgayPX.setColumns(10);
        txtDenNgayPX.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtDenNgayPX.setBorder(javax.swing.BorderFactory.createTitledBorder("Đến ngày"));
        txtDenNgayPX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDenNgayPXActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(txtTuNgayPX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtDenNgayPX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTuNgayPX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDenNgayPX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        btnReload_PX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/refresh.png"))); // NOI18N
        btnReload_PX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReload_PXActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReload_PX, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnReload_PX, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 574, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        editorCTHD.setEditable(false);
        editorCTHD.setContentType("text/html"); // NOI18N
        editorCTHD.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jScrollPane16.setViewportView(editorCTHD);

        editorCTPX.setEditable(false);
        editorCTPX.setContentType("text/html"); // NOI18N
        editorCTPX.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jScrollPane17.setViewportView(editorCTPX);

        btnInHD.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnInHD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/print.png"))); // NOI18N
        btnInHD.setText("IN HOÁ ĐƠN");
        btnInHD.setEnabled(false);
        btnInHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInHDActionPerformed(evt);
            }
        });

        btnInPX.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnInPX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/print.png"))); // NOI18N
        btnInPX.setText("IN PHIẾU XUẤT");
        btnInPX.setEnabled(false);
        btnInPX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInPXActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(81, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 689, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 689, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 689, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(75, 75, 75))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addComponent(btnInPX)
                        .addGap(317, 317, 317))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addComponent(btnInHD, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(332, 332, 332))))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnInHD, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnInPX, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(281, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tab_Xuat.addTab("Xem lại phiếu xuất", jPanel6);

        javax.swing.GroupLayout pnOrderLayout = new javax.swing.GroupLayout(pnOrder);
        pnOrder.setLayout(pnOrderLayout);
        pnOrderLayout.setHorizontalGroup(
            pnOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab_Xuat)
        );
        pnOrderLayout.setVerticalGroup(
            pnOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab_Xuat)
        );

        PnTong.add(pnOrder, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, -1));

        pnNhapNL.setBackground(new java.awt.Color(255, 255, 255));
        pnNhapNL.setPreferredSize(new java.awt.Dimension(1203, 1000));

        tab_NhapNL.setBackground(new java.awt.Color(255, 255, 255));
        tab_NhapNL.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        tab_NhapNL.setPreferredSize(new java.awt.Dimension(1230, 1000));

        pn_NhapNL.setBackground(new java.awt.Color(204, 204, 204));
        pn_NhapNL.setPreferredSize(new java.awt.Dimension(1230, 1007));

        pn_NL.setBackground(new java.awt.Color(255, 255, 255));
        pn_NL.setPreferredSize(new java.awt.Dimension(840, 920));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setText("Kho nguyên liệu");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setText("Tìm kiếm");

        txtTimKiem.setColumns(20);
        txtTimKiem.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));

        tb_NL.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tb_NL.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã nguyên liệu", "Tên nguyên liệu", "ĐVT", "Tồn kho"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_NL.setRowHeight(25);
        tb_NL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_NLMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tb_NL);
        if (tb_NL.getColumnModel().getColumnCount() > 0) {
            tb_NL.getColumnModel().getColumn(0).setResizable(false);
            tb_NL.getColumnModel().getColumn(0).setPreferredWidth(40);
            tb_NL.getColumnModel().getColumn(1).setResizable(false);
            tb_NL.getColumnModel().getColumn(1).setPreferredWidth(250);
            tb_NL.getColumnModel().getColumn(2).setResizable(false);
            tb_NL.getColumnModel().getColumn(2).setPreferredWidth(20);
            tb_NL.getColumnModel().getColumn(3).setResizable(false);
            tb_NL.getColumnModel().getColumn(3).setPreferredWidth(20);
        }

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        tb_GioNL.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tb_GioNL.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã nguyên liệu", "Tên nguyên liệu", "Số lượng", "Đơn giá", "Thành tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_GioNL.setRowHeight(25);
        jScrollPane1.setViewportView(tb_GioNL);
        if (tb_GioNL.getColumnModel().getColumnCount() > 0) {
            tb_GioNL.getColumnModel().getColumn(0).setResizable(false);
            tb_GioNL.getColumnModel().getColumn(0).setPreferredWidth(40);
            tb_GioNL.getColumnModel().getColumn(1).setResizable(false);
            tb_GioNL.getColumnModel().getColumn(1).setPreferredWidth(250);
            tb_GioNL.getColumnModel().getColumn(2).setResizable(false);
            tb_GioNL.getColumnModel().getColumn(2).setPreferredWidth(20);
            tb_GioNL.getColumnModel().getColumn(3).setResizable(false);
            tb_GioNL.getColumnModel().getColumn(3).setPreferredWidth(30);
            tb_GioNL.getColumnModel().getColumn(4).setResizable(false);
            tb_GioNL.getColumnModel().getColumn(4).setPreferredWidth(30);
        }

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel2.setText("Nguyên liệu chờ nhập");

        btnTimNL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimNLActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pn_NLLayout = new javax.swing.GroupLayout(pn_NL);
        pn_NL.setLayout(pn_NLLayout);
        pn_NLLayout.setHorizontalGroup(
            pn_NLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_NLLayout.createSequentialGroup()
                .addGroup(pn_NLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pn_NLLayout.createSequentialGroup()
                        .addGap(190, 190, 190)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pn_NLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pn_NLLayout.createSequentialGroup()
                                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnTimNL)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pn_NLLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pn_NLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2))))
                .addContainerGap())
            .addGroup(pn_NLLayout.createSequentialGroup()
                .addGap(185, 185, 185)
                .addComponent(jLabel2)
                .addContainerGap(261, Short.MAX_VALUE))
        );
        pn_NLLayout.setVerticalGroup(
            pn_NLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_NLLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pn_NLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel12)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTimNL, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        pn_ThongTin.setBackground(new java.awt.Color(255, 255, 255));

        txtGiaNhap.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtGiaNhap.setBorder(javax.swing.BorderFactory.createTitledBorder("Đơn giá"));

        btn_GhiGioNhap.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_GhiGioNhap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/confirm.png"))); // NOI18N
        btn_GhiGioNhap.setText("CHỌN NHẬP");
        btn_GhiGioNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GhiGioNhapActionPerformed(evt);
            }
        });

        txtTenNL.setEditable(false);
        txtTenNL.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTenNL.setBorder(javax.swing.BorderFactory.createTitledBorder("Tên nguyên liệu"));

        txtMaNL.setEditable(false);
        txtMaNL.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtMaNL.setBorder(javax.swing.BorderFactory.createTitledBorder("Mã nguyên liệu"));

        txtSLNhap.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtSLNhap.setBorder(javax.swing.BorderFactory.createTitledBorder("Số lượng"));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel5.setText("Thông tin nguyên liệu");

        btnGhiPhieuNhap.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnGhiPhieuNhap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/confirm.png"))); // NOI18N
        btnGhiPhieuNhap.setText("XÁC NHẬN");
        btnGhiPhieuNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGhiPhieuNhapActionPerformed(evt);
            }
        });

        btnXoaGioNhap.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnXoaGioNhap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/delete.png"))); // NOI18N
        btnXoaGioNhap.setText("XÓA");
        btnXoaGioNhap.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnXoaGioNhap.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnXoaGioNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaGioNhapActionPerformed(evt);
            }
        });

        btnHuyPN.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnHuyPN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/cancel.png"))); // NOI18N
        btnHuyPN.setText("HỦY NHẬP");
        btnHuyPN.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnHuyPN.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnHuyPN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyPNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pn_ThongTinLayout = new javax.swing.GroupLayout(pn_ThongTin);
        pn_ThongTin.setLayout(pn_ThongTinLayout);
        pn_ThongTinLayout.setHorizontalGroup(
            pn_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(pn_ThongTinLayout.createSequentialGroup()
                .addGroup(pn_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pn_ThongTinLayout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addGroup(pn_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTenNL, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaNL, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSLNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGiaNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pn_ThongTinLayout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addComponent(btn_GhiGioNhap))
                    .addGroup(pn_ThongTinLayout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addGroup(pn_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnHuyPN)
                            .addComponent(btnXoaGioNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnGhiPhieuNhap)))
                    .addGroup(pn_ThongTinLayout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jLabel5)))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        pn_ThongTinLayout.setVerticalGroup(
            pn_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_ThongTinLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel5)
                .addGap(44, 44, 44)
                .addComponent(txtMaNL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(txtTenNL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(txtSLNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(txtGiaNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(btn_GhiGioNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addComponent(btnXoaGioNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(btnHuyPN, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(btnGhiPhieuNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(139, 139, 139))
        );

        javax.swing.GroupLayout pn_NhapNLLayout = new javax.swing.GroupLayout(pn_NhapNL);
        pn_NhapNL.setLayout(pn_NhapNLLayout);
        pn_NhapNLLayout.setHorizontalGroup(
            pn_NhapNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_NhapNLLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(pn_NL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pn_ThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pn_NhapNLLayout.setVerticalGroup(
            pn_NhapNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_NhapNLLayout.createSequentialGroup()
                .addGroup(pn_NhapNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pn_ThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pn_NL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        tab_NhapNL.addTab("Nhập nguyên liệu", pn_NhapNL);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));

        tb_PN.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tb_PN.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã phiếu nhập", "Mã nhân viên"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_PN.setRowHeight(25);
        tb_PN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_PNMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tb_PN);
        if (tb_PN.getColumnModel().getColumnCount() > 0) {
            tb_PN.getColumnModel().getColumn(0).setResizable(false);
            tb_PN.getColumnModel().getColumn(0).setPreferredWidth(200);
            tb_PN.getColumnModel().getColumn(1).setResizable(false);
            tb_PN.getColumnModel().getColumn(1).setPreferredWidth(40);
        }

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel4.setText("Phiếu nhập");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Tìm kiếm"));

        txtTuNgay.setColumns(10);
        txtTuNgay.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtTuNgay.setBorder(javax.swing.BorderFactory.createTitledBorder("Từ ngày"));
        txtTuNgay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTuNgayActionPerformed(evt);
            }
        });

        txtDenNgay.setColumns(10);
        txtDenNgay.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtDenNgay.setBorder(javax.swing.BorderFactory.createTitledBorder("Đến ngày"));
        txtDenNgay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDenNgayActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(txtTuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtDenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        btnReloadPN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/refresh.png"))); // NOI18N
        btnReloadPN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReloadPNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(btnReloadPN, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReloadPN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 524, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(225, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        btnInPN.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnInPN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/print.png"))); // NOI18N
        btnInPN.setText("IN PHIẾU NHẬP");
        btnInPN.setEnabled(false);
        btnInPN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInPNActionPerformed(evt);
            }
        });

        editorCTPN.setEditable(false);
        editorCTPN.setContentType("text/html"); // NOI18N
        editorCTPN.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jScrollPane5.setViewportView(editorCTPN);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 715, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(313, 313, 313)
                        .addComponent(btnInPN)))
                .addContainerGap(99, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 667, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnInPN, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pn_XemPNLayout = new javax.swing.GroupLayout(pn_XemPN);
        pn_XemPN.setLayout(pn_XemPNLayout);
        pn_XemPNLayout.setHorizontalGroup(
            pn_XemPNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_XemPNLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pn_XemPNLayout.setVerticalGroup(
            pn_XemPNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tab_NhapNL.addTab("Xem lại phiếu nhập", pn_XemPN);

        javax.swing.GroupLayout pnNhapNLLayout = new javax.swing.GroupLayout(pnNhapNL);
        pnNhapNL.setLayout(pnNhapNLLayout);
        pnNhapNLLayout.setHorizontalGroup(
            pnNhapNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab_NhapNL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        pnNhapNLLayout.setVerticalGroup(
            pnNhapNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnNhapNLLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(tab_NhapNL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        PnTong.add(pnNhapNL, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 3, 1200, 1010));

        pnQLNL.setBackground(new java.awt.Color(255, 255, 255));
        pnQLNL.setPreferredSize(new java.awt.Dimension(1200, 966));

        tab_QLNL.setBackground(new java.awt.Color(255, 255, 255));
        tab_QLNL.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N

        pnNguyenLieu.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel11.setText("Kho nguyên liệu");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setText("Tìm kiếm");

        txtTimKiem_QLNL.setColumns(20);
        txtTimKiem_QLNL.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jScrollPane4.setBackground(new java.awt.Color(255, 255, 255));

        tb_QLNL.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tb_QLNL.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã nguyên liệu", "Tên nguyên liệu", "ĐVT", "Tồn kho"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_QLNL.setRowHeight(25);
        tb_QLNL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_QLNLMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tb_QLNL);
        if (tb_QLNL.getColumnModel().getColumnCount() > 0) {
            tb_QLNL.getColumnModel().getColumn(0).setResizable(false);
            tb_QLNL.getColumnModel().getColumn(0).setPreferredWidth(40);
            tb_QLNL.getColumnModel().getColumn(1).setResizable(false);
            tb_QLNL.getColumnModel().getColumn(1).setPreferredWidth(250);
            tb_QLNL.getColumnModel().getColumn(2).setResizable(false);
            tb_QLNL.getColumnModel().getColumn(2).setPreferredWidth(20);
            tb_QLNL.getColumnModel().getColumn(3).setResizable(false);
            tb_QLNL.getColumnModel().getColumn(3).setPreferredWidth(20);
        }

        btnTimNL_QLNL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimNL_QLNLActionPerformed(evt);
            }
        });

        txtMaNL_QL.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtMaNL_QL.setBorder(javax.swing.BorderFactory.createTitledBorder("Mã nguyên liệu"));

        txtTenNL_QL.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtTenNL_QL.setBorder(javax.swing.BorderFactory.createTitledBorder("Tên nguyên liệu"));

        txtDVT_QL.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtDVT_QL.setBorder(javax.swing.BorderFactory.createTitledBorder("ĐVT"));

        btnThemNL.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnThemNL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/add.png"))); // NOI18N
        btnThemNL.setText("THÊM");
        btnThemNL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemNLActionPerformed(evt);
            }
        });

        btnSuaNL.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnSuaNL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/edit_user.png"))); // NOI18N
        btnSuaNL.setText("SỬA");
        btnSuaNL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaNLActionPerformed(evt);
            }
        });

        btnXoaNL.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnXoaNL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/delete_30.png"))); // NOI18N
        btnXoaNL.setText("XÓA");
        btnXoaNL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaNLActionPerformed(evt);
            }
        });

        btnGhiNL.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnGhiNL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/edit_30.png"))); // NOI18N
        btnGhiNL.setText("GHI");
        btnGhiNL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGhiNLActionPerformed(evt);
            }
        });

        btnHuyNL.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnHuyNL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/cancel.png"))); // NOI18N
        btnHuyNL.setText("HỦY");
        btnHuyNL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyNLActionPerformed(evt);
            }
        });

        btnCapNhatSL_NL.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnCapNhatSL_NL.setText("Cập nhật số lượng");
        btnCapNhatSL_NL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatSL_NLActionPerformed(evt);
            }
        });

        txtSLB_NL.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtSLB_NL.setBorder(javax.swing.BorderFactory.createTitledBorder("Số lượng bỏ"));

        jPanel12.setBackground(new java.awt.Color(204, 204, 204));

        jScrollPane19.setBackground(new java.awt.Color(255, 255, 255));

        tb_CTPH.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tb_CTPH.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MANL", "Tên nguyên liệu", "Số lượng huỷ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_CTPH.setRowHeight(25);
        jScrollPane19.setViewportView(tb_CTPH);
        if (tb_CTPH.getColumnModel().getColumnCount() > 0) {
            tb_CTPH.getColumnModel().getColumn(0).setResizable(false);
            tb_CTPH.getColumnModel().getColumn(0).setPreferredWidth(20);
            tb_CTPH.getColumnModel().getColumn(1).setResizable(false);
            tb_CTPH.getColumnModel().getColumn(1).setPreferredWidth(200);
            tb_CTPH.getColumnModel().getColumn(2).setResizable(false);
            tb_CTPH.getColumnModel().getColumn(2).setPreferredWidth(20);
        }

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Phiếu huỷ nguyên liệu");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(jLabel23)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnNguyenLieuLayout = new javax.swing.GroupLayout(pnNguyenLieu);
        pnNguyenLieu.setLayout(pnNguyenLieuLayout);
        pnNguyenLieuLayout.setHorizontalGroup(
            pnNguyenLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnNguyenLieuLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(pnNguyenLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtMaNL_QL, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txtTenNL_QL, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDVT_QL, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSLB_NL, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnNguyenLieuLayout.createSequentialGroup()
                        .addGroup(pnNguyenLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnNguyenLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnGhiNL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnXoaNL, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
                            .addComponent(btnThemNL))
                        .addGap(74, 74, 74)
                        .addGroup(pnNguyenLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnSuaNL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnHuyNL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(btnCapNhatSL_NL, javax.swing.GroupLayout.Alignment.LEADING))
                .addGroup(pnNguyenLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnNguyenLieuLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 137, Short.MAX_VALUE)
                        .addGroup(pnNguyenLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnNguyenLieuLayout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtTimKiem_QLNL, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(200, 200, 200)
                                .addComponent(btnTimNL_QLNL))
                            .addGroup(pnNguyenLieuLayout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 711, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(39, 39, 39))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnNguyenLieuLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(138, 138, 138))))
        );
        pnNguyenLieuLayout.setVerticalGroup(
            pnNguyenLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnNguyenLieuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnNguyenLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnTimNL_QLNL, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnNguyenLieuLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnNguyenLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(txtTimKiem_QLNL))))
                .addGap(41, 41, 41)
                .addGroup(pnNguyenLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnNguyenLieuLayout.createSequentialGroup()
                        .addComponent(txtMaNL_QL, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(txtTenNL_QL, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(txtDVT_QL, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(txtSLB_NL, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(pnNguyenLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnNguyenLieuLayout.createSequentialGroup()
                        .addGroup(pnNguyenLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnThemNL)
                            .addComponent(btnSuaNL))
                        .addGap(18, 18, 18)
                        .addComponent(btnXoaNL)
                        .addGap(26, 26, 26)
                        .addGroup(pnNguyenLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnHuyNL, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnGhiNL))
                        .addGap(30, 30, 30)
                        .addComponent(btnCapNhatSL_NL, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tab_QLNL.addTab("Quản lý nguyên liệu", pnNguyenLieu);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane20.setBackground(new java.awt.Color(255, 255, 255));

        tb_PH.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tb_PH.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã phiếu huỷ", "Mã nhân viên"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_PH.setRowHeight(25);
        tb_PH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_PHMouseClicked(evt);
            }
        });
        jScrollPane20.setViewportView(tb_PH);
        if (tb_PH.getColumnModel().getColumnCount() > 0) {
            tb_PH.getColumnModel().getColumn(0).setResizable(false);
            tb_PH.getColumnModel().getColumn(0).setPreferredWidth(200);
            tb_PH.getColumnModel().getColumn(1).setResizable(false);
            tb_PH.getColumnModel().getColumn(1).setPreferredWidth(40);
        }

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel24.setText("Phiếu huỷ");

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Tìm kiếm"));

        txtTuNgayPH.setColumns(10);
        txtTuNgayPH.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtTuNgayPH.setBorder(javax.swing.BorderFactory.createTitledBorder("Từ ngày"));
        txtTuNgayPH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTuNgayPHActionPerformed(evt);
            }
        });

        txtDenNgayPH.setColumns(10);
        txtDenNgayPH.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtDenNgayPH.setBorder(javax.swing.BorderFactory.createTitledBorder("Đến ngày"));
        txtDenNgayPH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDenNgayPHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(txtTuNgayPH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtDenNgayPH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTuNgayPH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDenNgayPH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        btnReloadPN1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/refresh.png"))); // NOI18N
        btnReloadPN1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReloadPN1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel24)
                .addGap(18, 18, 18)
                .addComponent(btnReloadPN1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReloadPN1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 524, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(198, Short.MAX_VALUE))
        );

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        btnInPH.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnInPH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/print.png"))); // NOI18N
        btnInPH.setText("IN PHIẾU HUỶ");
        btnInPH.setEnabled(false);
        btnInPH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInPHActionPerformed(evt);
            }
        });

        editorCTPH.setEditable(false);
        editorCTPH.setContentType("text/html"); // NOI18N
        editorCTPH.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jScrollPane21.setViewportView(editorCTPH);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 715, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(313, 313, 313)
                        .addComponent(btnInPH)))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 667, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnInPH, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pn_XemPHLayout = new javax.swing.GroupLayout(pn_XemPH);
        pn_XemPH.setLayout(pn_XemPHLayout);
        pn_XemPHLayout.setHorizontalGroup(
            pn_XemPHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_XemPHLayout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pn_XemPHLayout.setVerticalGroup(
            pn_XemPHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tab_QLNL.addTab("Xem lại phiếu huỷ", pn_XemPH);

        javax.swing.GroupLayout pnQLNLLayout = new javax.swing.GroupLayout(pnQLNL);
        pnQLNL.setLayout(pnQLNLLayout);
        pnQLNLLayout.setHorizontalGroup(
            pnQLNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1200, Short.MAX_VALUE)
            .addGroup(pnQLNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(tab_QLNL, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE))
        );
        pnQLNLLayout.setVerticalGroup(
            pnQLNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 966, Short.MAX_VALUE)
            .addGroup(pnQLNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(tab_QLNL, javax.swing.GroupLayout.PREFERRED_SIZE, 966, Short.MAX_VALUE))
        );

        PnTong.add(pnQLNL, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pnQLDU.setBackground(new java.awt.Color(255, 255, 255));

        tab_QLDU.setBackground(new java.awt.Color(255, 255, 255));
        tab_QLDU.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N

        pn_QLDU.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane7.setBackground(new java.awt.Color(255, 255, 255));

        tb_QLDU.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tb_QLDU.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MADU", "Tên đồ uống", "TT Xoá", "Giá bán", "Giá sale"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_QLDU.setRowHeight(25);
        tb_QLDU.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_QLDUMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tb_QLDU);
        if (tb_QLDU.getColumnModel().getColumnCount() > 0) {
            tb_QLDU.getColumnModel().getColumn(0).setResizable(false);
            tb_QLDU.getColumnModel().getColumn(0).setPreferredWidth(30);
            tb_QLDU.getColumnModel().getColumn(1).setResizable(false);
            tb_QLDU.getColumnModel().getColumn(1).setPreferredWidth(200);
            tb_QLDU.getColumnModel().getColumn(2).setResizable(false);
            tb_QLDU.getColumnModel().getColumn(2).setPreferredWidth(100);
            tb_QLDU.getColumnModel().getColumn(3).setResizable(false);
            tb_QLDU.getColumnModel().getColumn(3).setPreferredWidth(100);
            tb_QLDU.getColumnModel().getColumn(4).setResizable(false);
            tb_QLDU.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

        imgDU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/default.png"))); // NOI18N

        btnChonAnh.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnChonAnh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/choose.png"))); // NOI18N
        btnChonAnh.setText("CHỌN ẢNH");
        btnChonAnh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonAnhActionPerformed(evt);
            }
        });

        cbDanhMuc.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        cbDanhMuc.setBorder(javax.swing.BorderFactory.createTitledBorder("Danh mục"));
        cbDanhMuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbDanhMucActionPerformed(evt);
            }
        });

        txtMaDU.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtMaDU.setBorder(javax.swing.BorderFactory.createTitledBorder("Mã đồ uống"));

        txtTenDU.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtTenDU.setBorder(javax.swing.BorderFactory.createTitledBorder("Tên đồ uống"));

        cbTTXoa_DU.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        cbTTXoa_DU.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Không bị xoá", "Bị xoá" }));
        cbTTXoa_DU.setBorder(javax.swing.BorderFactory.createTitledBorder("Tình trạng xóa"));

        txtGiaBan.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtGiaBan.setBorder(javax.swing.BorderFactory.createTitledBorder("Giá bán"));

        txtGiaSale.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtGiaSale.setBorder(javax.swing.BorderFactory.createTitledBorder("Giá Sale"));

        btnThemDU.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnThemDU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/add.png"))); // NOI18N
        btnThemDU.setText("THÊM");
        btnThemDU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemDUActionPerformed(evt);
            }
        });

        btnSuaDU.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnSuaDU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/edit_user.png"))); // NOI18N
        btnSuaDU.setText("SỬA");
        btnSuaDU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaDUActionPerformed(evt);
            }
        });

        btnXoaDU.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnXoaDU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/delete_30.png"))); // NOI18N
        btnXoaDU.setText("XOÁ");
        btnXoaDU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaDUActionPerformed(evt);
            }
        });

        btnHuyDU.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnHuyDU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/cancel.png"))); // NOI18N
        btnHuyDU.setText("HUỶ");
        btnHuyDU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyDUActionPerformed(evt);
            }
        });

        btnGhiDU.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnGhiDU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/edit.png"))); // NOI18N
        btnGhiDU.setText("GHI");
        btnGhiDU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGhiDUActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pn_QLDULayout = new javax.swing.GroupLayout(pn_QLDU);
        pn_QLDU.setLayout(pn_QLDULayout);
        pn_QLDULayout.setHorizontalGroup(
            pn_QLDULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_QLDULayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(pn_QLDULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pn_QLDULayout.createSequentialGroup()
                        .addGroup(pn_QLDULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbDanhMuc, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMaDU)
                            .addComponent(txtTenDU)
                            .addComponent(cbTTXoa_DU, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(53, 53, 53)
                        .addGroup(pn_QLDULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pn_QLDULayout.createSequentialGroup()
                                .addGroup(pn_QLDULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_QLDULayout.createSequentialGroup()
                                        .addGap(259, 259, 259)
                                        .addComponent(btnGhiDU, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pn_QLDULayout.createSequentialGroup()
                                        .addComponent(txtGiaSale, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnThemDU)))
                                .addGap(26, 26, 26)
                                .addGroup(pn_QLDULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(pn_QLDULayout.createSequentialGroup()
                                        .addComponent(btnSuaDU)
                                        .addGap(26, 26, 26)
                                        .addComponent(btnXoaDU, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnHuyDU))
                                .addGap(87, 87, 87))
                            .addGroup(pn_QLDULayout.createSequentialGroup()
                                .addComponent(txtGiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pn_QLDULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnChonAnh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(imgDU, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(196, 196, 196))))
                    .addGroup(pn_QLDULayout.createSequentialGroup()
                        .addGroup(pn_QLDULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 1119, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(39, Short.MAX_VALUE))))
        );
        pn_QLDULayout.setVerticalGroup(
            pn_QLDULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_QLDULayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pn_QLDULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pn_QLDULayout.createSequentialGroup()
                        .addComponent(cbDanhMuc, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(txtMaDU, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addGroup(pn_QLDULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTenDU, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pn_QLDULayout.createSequentialGroup()
                        .addGap(0, 49, Short.MAX_VALUE)
                        .addComponent(imgDU, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnChonAnh)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pn_QLDULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbTTXoa_DU, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGiaSale, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemDU)
                    .addComponent(btnSuaDU)
                    .addComponent(btnXoaDU))
                .addGap(26, 26, 26)
                .addGroup(pn_QLDULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGhiDU, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHuyDU, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );

        tab_QLDU.addTab("Quản lý đồ uống", pn_QLDU);

        pn_CTDU.setBackground(new java.awt.Color(255, 255, 255));
        pn_CTDU.setPreferredSize(new java.awt.Dimension(1168, 933));

        pnDU_KoCT.setBackground(new java.awt.Color(204, 204, 204));
        pnDU_KoCT.setPreferredSize(new java.awt.Dimension(376, 125));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("DSDU chưa có công thức");

        jScrollPane8.setBackground(new java.awt.Color(255, 255, 255));

        tb_DU_KoCT.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tb_DU_KoCT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MADU", "Tên đồ uống"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_DU_KoCT.setRowHeight(25);
        tb_DU_KoCT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_DU_KoCTMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tb_DU_KoCT);
        if (tb_DU_KoCT.getColumnModel().getColumnCount() > 0) {
            tb_DU_KoCT.getColumnModel().getColumn(0).setResizable(false);
            tb_DU_KoCT.getColumnModel().getColumn(0).setPreferredWidth(50);
            tb_DU_KoCT.getColumnModel().getColumn(1).setResizable(false);
            tb_DU_KoCT.getColumnModel().getColumn(1).setPreferredWidth(200);
        }

        javax.swing.GroupLayout pnDU_KoCTLayout = new javax.swing.GroupLayout(pnDU_KoCT);
        pnDU_KoCT.setLayout(pnDU_KoCTLayout);
        pnDU_KoCTLayout.setHorizontalGroup(
            pnDU_KoCTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDU_KoCTLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnDU_KoCTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnDU_KoCTLayout.setVerticalGroup(
            pnDU_KoCTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDU_KoCTLayout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnDU_CoCT.setBackground(new java.awt.Color(202, 232, 249));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("DSDU đã có công thức");

        jScrollPane9.setBackground(new java.awt.Color(255, 255, 255));

        tb_DU_CoCT.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tb_DU_CoCT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MADU", "Tên đồ uống"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_DU_CoCT.setRowHeight(25);
        tb_DU_CoCT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_DU_CoCTMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(tb_DU_CoCT);
        if (tb_DU_CoCT.getColumnModel().getColumnCount() > 0) {
            tb_DU_CoCT.getColumnModel().getColumn(0).setResizable(false);
            tb_DU_CoCT.getColumnModel().getColumn(0).setPreferredWidth(50);
            tb_DU_CoCT.getColumnModel().getColumn(1).setResizable(false);
            tb_DU_CoCT.getColumnModel().getColumn(1).setPreferredWidth(200);
        }

        cbDanhMuc_CT.setBorder(javax.swing.BorderFactory.createTitledBorder("Danh mục"));
        cbDanhMuc_CT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbDanhMuc_CTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnDU_CoCTLayout = new javax.swing.GroupLayout(pnDU_CoCT);
        pnDU_CoCT.setLayout(pnDU_CoCTLayout);
        pnDU_CoCTLayout.setHorizontalGroup(
            pnDU_CoCTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDU_CoCTLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnDU_CoCTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                    .addComponent(cbDanhMuc_CT, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnDU_CoCTLayout.setVerticalGroup(
            pnDU_CoCTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDU_CoCTLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbDanhMuc_CT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane10.setBackground(new java.awt.Color(255, 255, 255));

        tb_NL_CTDU.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tb_NL_CTDU.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MANL", "Tên nguyên liệu", "ĐVT"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_NL_CTDU.setRowHeight(25);
        tb_NL_CTDU.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_NL_CTDUMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(tb_NL_CTDU);
        if (tb_NL_CTDU.getColumnModel().getColumnCount() > 0) {
            tb_NL_CTDU.getColumnModel().getColumn(0).setResizable(false);
            tb_NL_CTDU.getColumnModel().getColumn(0).setPreferredWidth(40);
            tb_NL_CTDU.getColumnModel().getColumn(1).setResizable(false);
            tb_NL_CTDU.getColumnModel().getColumn(1).setPreferredWidth(200);
            tb_NL_CTDU.getColumnModel().getColumn(2).setResizable(false);
            tb_NL_CTDU.getColumnModel().getColumn(2).setPreferredWidth(60);
        }

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Danh sách nguyên liệu");

        javax.swing.GroupLayout pnDU_DSNLLayout = new javax.swing.GroupLayout(pnDU_DSNL);
        pnDU_DSNL.setLayout(pnDU_DSNLLayout);
        pnDU_DSNLLayout.setHorizontalGroup(
            pnDU_DSNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDU_DSNLLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnDU_DSNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        pnDU_DSNLLayout.setVerticalGroup(
            pnDU_DSNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnDU_DSNLLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addGap(5, 5, 5)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                .addContainerGap())
        );

        txtTenNL_CTDU.setEditable(false);
        txtTenNL_CTDU.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTenNL_CTDU.setBorder(javax.swing.BorderFactory.createTitledBorder("Tên nguyên liệu"));

        txtMaNL_CTDU.setEditable(false);
        txtMaNL_CTDU.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtMaNL_CTDU.setBorder(javax.swing.BorderFactory.createTitledBorder("Mã nguyên liệu"));

        txtSL_CTDU.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtSL_CTDU.setBorder(javax.swing.BorderFactory.createTitledBorder("Số lượng"));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel17.setText("Thông tin nguyên liệu");

        jScrollPane11.setBackground(new java.awt.Color(255, 255, 255));

        tb_CTDU.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tb_CTDU.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MANL", "Tên nguyên liệu", "Số lượng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_CTDU.setRowHeight(25);
        tb_CTDU.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_CTDUMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(tb_CTDU);
        if (tb_CTDU.getColumnModel().getColumnCount() > 0) {
            tb_CTDU.getColumnModel().getColumn(0).setResizable(false);
            tb_CTDU.getColumnModel().getColumn(0).setPreferredWidth(40);
            tb_CTDU.getColumnModel().getColumn(1).setResizable(false);
            tb_CTDU.getColumnModel().getColumn(1).setPreferredWidth(200);
            tb_CTDU.getColumnModel().getColumn(2).setResizable(false);
            tb_CTDU.getColumnModel().getColumn(2).setPreferredWidth(60);
        }

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel18.setText("Công thức pha chế");

        lbMADU_CT.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N

        javax.swing.GroupLayout pnCTPCLayout = new javax.swing.GroupLayout(pnCTPC);
        pnCTPC.setLayout(pnCTPCLayout);
        pnCTPCLayout.setHorizontalGroup(
            pnCTPCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnCTPCLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnCTPCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnCTPCLayout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbMADU_CT, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        pnCTPCLayout.setVerticalGroup(
            pnCTPCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnCTPCLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnCTPCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(lbMADU_CT))
                .addGap(5, 5, 5)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnXoa_CTDU.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnXoa_CTDU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/delete_30.png"))); // NOI18N
        btnXoa_CTDU.setText("XÓA");
        btnXoa_CTDU.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnXoa_CTDU.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnXoa_CTDU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoa_CTDUActionPerformed(evt);
            }
        });

        btnHuyCTDU.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnHuyCTDU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/cancel.png"))); // NOI18N
        btnHuyCTDU.setText("HỦY");
        btnHuyCTDU.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnHuyCTDU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyCTDUActionPerformed(evt);
            }
        });

        btnGhiCTDU.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnGhiCTDU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/confirm.png"))); // NOI18N
        btnGhiCTDU.setText("XÁC NHẬN");
        btnGhiCTDU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGhiCTDUActionPerformed(evt);
            }
        });

        btnThem_CTDU.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnThem_CTDU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/add.png"))); // NOI18N
        btnThem_CTDU.setText("THÊM");
        btnThem_CTDU.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnThem_CTDU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThem_CTDUActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnDU_DSNL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jSeparator4)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 65, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtMaNL_CTDU)
                                    .addComponent(txtTenNL_CTDU)
                                    .addComponent(txtSL_CTDU, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(74, 74, 74))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnGhiCTDU)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(btnXoa_CTDU, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnThem_CTDU, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(39, 39, 39)
                                        .addComponent(btnHuyCTDU)))
                                .addGap(76, 76, 76)))))
                .addComponent(pnCTPC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(jLabel17)
                .addGap(18, 18, 18)
                .addComponent(txtMaNL_CTDU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtTenNL_CTDU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtSL_CTDU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnThem_CTDU, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHuyCTDU, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(btnXoa_CTDU, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnGhiCTDU, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnCTPC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnDU_DSNL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout pn_CTDULayout = new javax.swing.GroupLayout(pn_CTDU);
        pn_CTDU.setLayout(pn_CTDULayout);
        pn_CTDULayout.setHorizontalGroup(
            pn_CTDULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_CTDULayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnDU_KoCT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(pnDU_CoCT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pn_CTDULayout.setVerticalGroup(
            pn_CTDULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_CTDULayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pn_CTDULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnDU_CoCT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnDU_KoCT, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tab_QLDU.addTab("Công thức pha chế", pn_CTDU);

        javax.swing.GroupLayout pnQLDULayout = new javax.swing.GroupLayout(pnQLDU);
        pnQLDU.setLayout(pnQLDULayout);
        pnQLDULayout.setHorizontalGroup(
            pnQLDULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab_QLDU)
        );
        pnQLDULayout.setVerticalGroup(
            pnQLDULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab_QLDU)
        );

        PnTong.add(pnQLDU, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pnQLTK.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane6.setBackground(new java.awt.Color(255, 255, 255));

        tb_DSTK.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tb_DSTK.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Username", "Fullname", "Chức vụ", "Tình trạng xóa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_DSTK.setAlignmentX(1.0F);
        tb_DSTK.setAlignmentY(1.0F);
        tb_DSTK.setRowHeight(25);
        tb_DSTK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_DSTKMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tb_DSTK);
        if (tb_DSTK.getColumnModel().getColumnCount() > 0) {
            tb_DSTK.getColumnModel().getColumn(0).setResizable(false);
            tb_DSTK.getColumnModel().getColumn(0).setPreferredWidth(30);
            tb_DSTK.getColumnModel().getColumn(1).setResizable(false);
            tb_DSTK.getColumnModel().getColumn(1).setPreferredWidth(100);
            tb_DSTK.getColumnModel().getColumn(2).setResizable(false);
            tb_DSTK.getColumnModel().getColumn(2).setPreferredWidth(30);
            tb_DSTK.getColumnModel().getColumn(3).setResizable(false);
            tb_DSTK.getColumnModel().getColumn(3).setPreferredWidth(50);
        }

        txtUsername_TK.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtUsername_TK.setBorder(javax.swing.BorderFactory.createTitledBorder("Username"));

        txtFullname_TK.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtFullname_TK.setBorder(javax.swing.BorderFactory.createTitledBorder("Fullname"));

        txtPass_TK.setBorder(javax.swing.BorderFactory.createTitledBorder("Password"));

        cbChucVu_TK.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        cbChucVu_TK.setBorder(javax.swing.BorderFactory.createTitledBorder("Chức vụ"));

        cbTTXOA_Tk.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        cbTTXOA_Tk.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Không bị khóa", "Bị khóa" }));
        cbTTXOA_Tk.setBorder(javax.swing.BorderFactory.createTitledBorder("Tình trạng xóa"));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel14.setText("Quản lý tài khoản");

        btnThemTK.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnThemTK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/add_user.png"))); // NOI18N
        btnThemTK.setText("THÊM");
        btnThemTK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemTKActionPerformed(evt);
            }
        });

        btnSuaTK.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnSuaTK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/edit_user.png"))); // NOI18N
        btnSuaTK.setText("SỬA");
        btnSuaTK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaTKActionPerformed(evt);
            }
        });

        btnXoaTK.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnXoaTK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/delete_user.png"))); // NOI18N
        btnXoaTK.setText("XÓA");
        btnXoaTK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaTKActionPerformed(evt);
            }
        });

        btnGhiTK.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnGhiTK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/edit_30.png"))); // NOI18N
        btnGhiTK.setText("GHI");
        btnGhiTK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGhiTKActionPerformed(evt);
            }
        });

        btnHuy_TK.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnHuy_TK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/cancel.png"))); // NOI18N
        btnHuy_TK.setText("HỦY");
        btnHuy_TK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuy_TKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnQLTKLayout = new javax.swing.GroupLayout(pnQLTK);
        pnQLTK.setLayout(pnQLTKLayout);
        pnQLTKLayout.setHorizontalGroup(
            pnQLTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnQLTKLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(pnQLTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnQLTKLayout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(btnGhiTK, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(97, 97, 97)
                        .addComponent(btnHuy_TK)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnQLTKLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel14)
                        .addGap(854, 854, 854))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnQLTKLayout.createSequentialGroup()
                        .addGroup(pnQLTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnQLTKLayout.createSequentialGroup()
                                .addComponent(btnThemTK)
                                .addGap(67, 67, 67)
                                .addComponent(btnSuaTK)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnXoaTK))
                            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnQLTKLayout.createSequentialGroup()
                                .addGroup(pnQLTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnQLTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtPass_TK, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                                        .addComponent(txtFullname_TK, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtUsername_TK, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnQLTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(cbChucVu_TK, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cbTTXOA_Tk, 0, 321, Short.MAX_VALUE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 686, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35))))
        );
        pnQLTKLayout.setVerticalGroup(
            pnQLTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnQLTKLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addGap(32, 32, 32)
                .addGroup(pnQLTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnQLTKLayout.createSequentialGroup()
                        .addComponent(txtUsername_TK, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(txtFullname_TK, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(txtPass_TK, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(cbChucVu_TK, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(cbTTXOA_Tk, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addGroup(pnQLTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemTK)
                    .addComponent(btnSuaTK)
                    .addComponent(btnXoaTK))
                .addGap(18, 18, 18)
                .addGroup(pnQLTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHuy_TK, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGhiTK))
                .addContainerGap(257, Short.MAX_VALUE))
        );

        PnTong.add(pnQLTK, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pnThongKe.setBackground(new java.awt.Color(255, 255, 255));

        tab_ThongKe.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N

        pnTOP5DU.setBackground(new java.awt.Color(255, 255, 255));

        jPanel10.setBackground(new java.awt.Color(202, 232, 249));

        jScrollPane18.setBackground(new java.awt.Color(255, 255, 255));

        tb_TOP5DU.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tb_TOP5DU.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "STT", "MADU", "Tên đồ uống", "Đã bán"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_TOP5DU.setRowHeight(25);
        jScrollPane18.setViewportView(tb_TOP5DU);
        if (tb_TOP5DU.getColumnModel().getColumnCount() > 0) {
            tb_TOP5DU.getColumnModel().getColumn(0).setResizable(false);
            tb_TOP5DU.getColumnModel().getColumn(0).setPreferredWidth(5);
            tb_TOP5DU.getColumnModel().getColumn(1).setResizable(false);
            tb_TOP5DU.getColumnModel().getColumn(1).setPreferredWidth(60);
            tb_TOP5DU.getColumnModel().getColumn(2).setResizable(false);
            tb_TOP5DU.getColumnModel().getColumn(2).setPreferredWidth(250);
            tb_TOP5DU.getColumnModel().getColumn(3).setResizable(false);
            tb_TOP5DU.getColumnModel().getColumn(3).setPreferredWidth(30);
        }

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("TOP 5 ĐỒ UỐNG BÁN CHẠY");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 524, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addComponent(jLabel22)
                .addGap(33, 33, 33)
                .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86))
        );

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Chọn khoảng thời gian"));

        txt_FromTop5.setColumns(10);
        txt_FromTop5.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_FromTop5.setBorder(javax.swing.BorderFactory.createTitledBorder("Từ ngày"));
        txt_FromTop5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_FromTop5ActionPerformed(evt);
            }
        });

        txt_ToTop5.setColumns(10);
        txt_ToTop5.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_ToTop5.setBorder(javax.swing.BorderFactory.createTitledBorder("Đến ngày"));
        txt_ToTop5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_ToTop5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(txt_FromTop5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txt_ToTop5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_FromTop5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_ToTop5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnTOP5DULayout = new javax.swing.GroupLayout(pnTOP5DU);
        pnTOP5DU.setLayout(pnTOP5DULayout);
        pnTOP5DULayout.setHorizontalGroup(
            pnTOP5DULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTOP5DULayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(71, 71, 71)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(82, Short.MAX_VALUE))
        );
        pnTOP5DULayout.setVerticalGroup(
            pnTOP5DULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTOP5DULayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(pnTOP5DULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(550, Short.MAX_VALUE))
        );

        tab_ThongKe.addTab("TOP 5 đồ uống bán chạy", pnTOP5DU);

        pnTongNhapNL.setBackground(new java.awt.Color(255, 255, 255));

        jPanel16.setBackground(new java.awt.Color(202, 232, 249));

        jScrollPane22.setBackground(new java.awt.Color(255, 255, 255));

        tb_TongNhapNL.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tb_TongNhapNL.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "STT", "MANL", "Tên nguyên liệu", "Số lượng nhập"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_TongNhapNL.setRowHeight(25);
        jScrollPane22.setViewportView(tb_TongNhapNL);
        if (tb_TongNhapNL.getColumnModel().getColumnCount() > 0) {
            tb_TongNhapNL.getColumnModel().getColumn(0).setResizable(false);
            tb_TongNhapNL.getColumnModel().getColumn(0).setPreferredWidth(5);
            tb_TongNhapNL.getColumnModel().getColumn(1).setResizable(false);
            tb_TongNhapNL.getColumnModel().getColumn(1).setPreferredWidth(60);
            tb_TongNhapNL.getColumnModel().getColumn(2).setResizable(false);
            tb_TongNhapNL.getColumnModel().getColumn(2).setPreferredWidth(250);
            tb_TongNhapNL.getColumnModel().getColumn(3).setResizable(false);
            tb_TongNhapNL.getColumnModel().getColumn(3).setPreferredWidth(30);
        }

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Tổng nhập nguyên liệu");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane22, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 524, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane22, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );

        jPanel17.setBackground(new java.awt.Color(179, 193, 135));

        jScrollPane23.setBackground(new java.awt.Color(255, 255, 255));

        tb_TongXuatNL.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tb_TongXuatNL.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "STT", "MANL", "Tên nguyên liệu", "Số lượng xuất"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_TongXuatNL.setRowHeight(25);
        jScrollPane23.setViewportView(tb_TongXuatNL);
        if (tb_TongXuatNL.getColumnModel().getColumnCount() > 0) {
            tb_TongXuatNL.getColumnModel().getColumn(0).setResizable(false);
            tb_TongXuatNL.getColumnModel().getColumn(0).setPreferredWidth(5);
            tb_TongXuatNL.getColumnModel().getColumn(1).setResizable(false);
            tb_TongXuatNL.getColumnModel().getColumn(1).setPreferredWidth(60);
            tb_TongXuatNL.getColumnModel().getColumn(2).setResizable(false);
            tb_TongXuatNL.getColumnModel().getColumn(2).setPreferredWidth(250);
            tb_TongXuatNL.getColumnModel().getColumn(3).setResizable(false);
            tb_TongXuatNL.getColumnModel().getColumn(3).setPreferredWidth(30);
        }

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("Tổng xuất nguyên liệu");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 524, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );

        jPanel18.setBackground(new java.awt.Color(204, 204, 255));

        jScrollPane24.setBackground(new java.awt.Color(255, 255, 255));

        tb_TongHuyNL.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tb_TongHuyNL.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "STT", "MANL", "Tên nguyên liệu", "Số lượng huỷ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_TongHuyNL.setRowHeight(25);
        jScrollPane24.setViewportView(tb_TongHuyNL);
        if (tb_TongHuyNL.getColumnModel().getColumnCount() > 0) {
            tb_TongHuyNL.getColumnModel().getColumn(0).setResizable(false);
            tb_TongHuyNL.getColumnModel().getColumn(0).setPreferredWidth(5);
            tb_TongHuyNL.getColumnModel().getColumn(1).setResizable(false);
            tb_TongHuyNL.getColumnModel().getColumn(1).setPreferredWidth(60);
            tb_TongHuyNL.getColumnModel().getColumn(2).setResizable(false);
            tb_TongHuyNL.getColumnModel().getColumn(2).setPreferredWidth(250);
            tb_TongHuyNL.getColumnModel().getColumn(3).setResizable(false);
            tb_TongHuyNL.getColumnModel().getColumn(3).setPreferredWidth(30);
        }

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("Tổng huỷ nguyên liệu");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 524, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder("Chọn khoảng thời gian"));

        txt_TuNgayNXNL.setColumns(10);
        txt_TuNgayNXNL.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_TuNgayNXNL.setBorder(javax.swing.BorderFactory.createTitledBorder("Từ ngày"));
        txt_TuNgayNXNL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_TuNgayNXNLActionPerformed(evt);
            }
        });

        txt_DenNgayNXNL.setColumns(10);
        txt_DenNgayNXNL.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_DenNgayNXNL.setBorder(javax.swing.BorderFactory.createTitledBorder("Đến ngày"));
        txt_DenNgayNXNL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_DenNgayNXNLActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(txt_TuNgayNXNL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txt_DenNgayNXNL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_TuNgayNXNL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_DenNgayNXNL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnTongNhapNLLayout = new javax.swing.GroupLayout(pnTongNhapNL);
        pnTongNhapNL.setLayout(pnTongNhapNLLayout);
        pnTongNhapNLLayout.setHorizontalGroup(
            pnTongNhapNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTongNhapNLLayout.createSequentialGroup()
                .addGroup(pnTongNhapNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnTongNhapNLLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnTongNhapNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnTongNhapNLLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(43, 43, 43))
        );
        pnTongNhapNLLayout.setVerticalGroup(
            pnTongNhapNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTongNhapNLLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(pnTongNhapNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(51, Short.MAX_VALUE))
        );

        tab_ThongKe.addTab("Nhập xuất nguyên liệu", pnTongNhapNL);

        pnTongTien.setBackground(new java.awt.Color(255, 255, 255));

        jPanel20.setBackground(new java.awt.Color(202, 232, 249));

        jScrollPane25.setBackground(new java.awt.Color(255, 255, 255));

        tb_TKTienNhap.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tb_TKTienNhap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "MAPN", "Tiền nhập"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_TKTienNhap.setRowHeight(25);
        jScrollPane25.setViewportView(tb_TKTienNhap);
        if (tb_TKTienNhap.getColumnModel().getColumnCount() > 0) {
            tb_TKTienNhap.getColumnModel().getColumn(0).setResizable(false);
            tb_TKTienNhap.getColumnModel().getColumn(0).setPreferredWidth(5);
            tb_TKTienNhap.getColumnModel().getColumn(1).setResizable(false);
            tb_TKTienNhap.getColumnModel().getColumn(1).setPreferredWidth(250);
            tb_TKTienNhap.getColumnModel().getColumn(2).setResizable(false);
            tb_TKTienNhap.getColumnModel().getColumn(2).setPreferredWidth(60);
        }

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Tổng tiền nhập nguyên liệu");

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        jLabel29.setText("Tổng tiền nhập:");

        lbTongTienNhap.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        lbTongTienNhap.setText("0 VNĐ");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbTongTienNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane25)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(70, 70, 70))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane25, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbTongTienNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13))
        );

        jPanel21.setBackground(new java.awt.Color(179, 193, 135));

        jScrollPane26.setBackground(new java.awt.Color(255, 255, 255));

        tb_TKTienBan.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tb_TKTienBan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "MAPX", "Tiền bán"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_TKTienBan.setRowHeight(25);
        jScrollPane26.setViewportView(tb_TKTienBan);
        if (tb_TKTienBan.getColumnModel().getColumnCount() > 0) {
            tb_TKTienBan.getColumnModel().getColumn(0).setResizable(false);
            tb_TKTienBan.getColumnModel().getColumn(0).setPreferredWidth(5);
            tb_TKTienBan.getColumnModel().getColumn(1).setResizable(false);
            tb_TKTienBan.getColumnModel().getColumn(1).setPreferredWidth(250);
            tb_TKTienBan.getColumnModel().getColumn(2).setResizable(false);
            tb_TKTienBan.getColumnModel().getColumn(2).setPreferredWidth(60);
        }

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Tổng tiền bán đồ uống");

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        jLabel31.setText("Tổng tiền bán:");

        lbTongTienBan.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        lbTongTienBan.setText("0 VNĐ");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbTongTienBan, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane26)
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(70, 70, 70))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane26, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbTongTienBan, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13))
        );

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder("Chọn khoảng thời gian"));

        txt_TuNgayTKTien.setColumns(10);
        txt_TuNgayTKTien.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_TuNgayTKTien.setBorder(javax.swing.BorderFactory.createTitledBorder("Từ ngày"));
        txt_TuNgayTKTien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_TuNgayTKTienActionPerformed(evt);
            }
        });

        txt_DenNgayTKTien.setColumns(10);
        txt_DenNgayTKTien.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_DenNgayTKTien.setBorder(javax.swing.BorderFactory.createTitledBorder("Đến ngày"));
        txt_DenNgayTKTien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_DenNgayTKTienActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addComponent(txt_TuNgayTKTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txt_DenNgayTKTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_TuNgayTKTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_DenNgayTKTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnTongTienLayout = new javax.swing.GroupLayout(pnTongTien);
        pnTongTien.setLayout(pnTongTienLayout);
        pnTongTienLayout.setHorizontalGroup(
            pnTongTienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnTongTienLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                .addGroup(pnTongTienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51))
        );
        pnTongTienLayout.setVerticalGroup(
            pnTongTienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTongTienLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(pnTongTienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tab_ThongKe.addTab("Tổng tiền nhập xuất", pnTongTien);

        javax.swing.GroupLayout pnThongKeLayout = new javax.swing.GroupLayout(pnThongKe);
        pnThongKe.setLayout(pnThongKeLayout);
        pnThongKeLayout.setHorizontalGroup(
            pnThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab_ThongKe)
        );
        pnThongKeLayout.setVerticalGroup(
            pnThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab_ThongKe)
        );

        PnTong.add(pnThongKe, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pnTTCaNhan.setBackground(new java.awt.Color(255, 255, 255));

        txtUserName.setEditable(false);
        txtUserName.setBackground(new java.awt.Color(255, 255, 255));
        txtUserName.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtUserName.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tên đăng nhập", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 15))); // NOI18N

        txtFullName.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtFullName.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tên đầy đủ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 15))); // NOI18N

        txtPass1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtPass1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Mật khẩu", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 15))); // NOI18N

        txtPass2.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtPass2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Xác nhận mật khẩu", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 15))); // NOI18N

        btnCapNhat_TTCaNhan.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        btnCapNhat_TTCaNhan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/confirm.png"))); // NOI18N
        btnCapNhat_TTCaNhan.setText("Cập nhật thông tin");
        btnCapNhat_TTCaNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhat_TTCaNhanActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/information.png"))); // NOI18N
        jLabel10.setText("Thông tin cá nhân");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Slogan.png"))); // NOI18N

        javax.swing.GroupLayout pnTTCaNhanLayout = new javax.swing.GroupLayout(pnTTCaNhan);
        pnTTCaNhan.setLayout(pnTTCaNhanLayout);
        pnTTCaNhanLayout.setHorizontalGroup(
            pnTTCaNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTTCaNhanLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(pnTTCaNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(pnTTCaNhanLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(197, 197, 197)
                        .addGroup(pnTTCaNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPass1, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPass2, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnTTCaNhanLayout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addComponent(btnCapNhat_TTCaNhan)))))
                .addContainerGap(199, Short.MAX_VALUE))
        );
        pnTTCaNhanLayout.setVerticalGroup(
            pnTTCaNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTTCaNhanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnTTCaNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnTTCaNhanLayout.createSequentialGroup()
                        .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtPass1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtPass2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(btnCapNhat_TTCaNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(284, Short.MAX_VALUE))
        );

        PnTong.add(pnTTCaNhan, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 960));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PnTong, javax.swing.GroupLayout.PREFERRED_SIZE, 1195, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnMenu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1013, Short.MAX_VALUE)
                    .addComponent(PnTong, javax.swing.GroupLayout.PREFERRED_SIZE, 1013, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTimNLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimNLActionPerformed
        // TODO add your handling code here:
        loadDataTableKho_TheoTen(tb_NL, txtTimKiem.getText());
    }//GEN-LAST:event_btnTimNLActionPerformed

    private void tb_NLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_NLMouseClicked
        // TODO add your handling code here:
        int i = tb_NL.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) tb_NL.getModel();
        txtMaNL.setText(dtm.getValueAt(i, 0).toString());
        txtTenNL.setText(dtm.getValueAt(i, 1).toString());
        txtSLNhap.setText("1");
        txtGiaNhap.setText("1");

    }//GEN-LAST:event_tb_NLMouseClicked

    private void btn_GhiGioNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GhiGioNhapActionPerformed
        // TODO add your handling code here:
        DefaultTableModel dtm = (DefaultTableModel) tb_GioNL.getModel();
        int row = tb_NL.getSelectedRow();
        if (row > -1) {
            try {
                int soLuong = Integer.parseInt(txtSLNhap.getText());
                int donGia = Integer.parseInt(txtGiaNhap.getText());
                String maNL = txtMaNL.getText();
                for (int i = 0; i < tb_GioNL.getRowCount(); i++) {
                    if (maNL.equals(tb_GioNL.getValueAt(i, 0))) {
                        int soLuongCu = Integer.parseInt(tb_GioNL.getValueAt(i, 2).toString());
                        soLuong += soLuongCu;
                        int thanhTien = soLuong * donGia;
                        tb_GioNL.setValueAt(soLuong, i, 2);
                        tb_GioNL.setValueAt(donGia, i, 3);
                        tb_GioNL.setValueAt(thanhTien, i, 4);
                        return;
                    }
                }
                String tenNL = txtTenNL.getText();
                int thanhTien = soLuong * donGia;
                Vector vt = new Vector();
                vt.add(maNL);
                vt.add(tenNL);
                vt.add(soLuong);
                vt.add(donGia);
                vt.add(thanhTien);
                dtm.addRow(vt);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng và đơn giá!", "Input warning", JOptionPane.WARNING_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(this, "Chưa chọn nguyên liệu để nhập!", "Input warning", JOptionPane.WARNING_MESSAGE);
        }
        tb_GioNL.setModel(dtm);
    }//GEN-LAST:event_btn_GhiGioNhapActionPerformed

    private void btnXoaGioNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaGioNhapActionPerformed
        // TODO add your handling code here:
        DefaultTableModel dtm = (DefaultTableModel) tb_GioNL.getModel();
        int row = tb_GioNL.getSelectedRow();
        if (row > -1) {
            dtm.removeRow(row);
        } else {
            JOptionPane.showMessageDialog(this, "Chưa chọn chi tiết để xóa!", "Input warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnXoaGioNhapActionPerformed

    private void btnHuyPNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyPNActionPerformed
        // TODO add your handling code here:
        DefaultTableModel dtm = (DefaultTableModel) tb_GioNL.getModel();
        int rows = dtm.getRowCount();
        for (int i = rows - 1; i >= 0; i--) {
            dtm.removeRow(i);
        }
    }//GEN-LAST:event_btnHuyPNActionPerformed

    private void btnGhiPhieuNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGhiPhieuNhapActionPerformed
        // TODO add your handling code here:
        int row = tb_GioNL.getRowCount();
        if (row < 1) {
            JOptionPane.showMessageDialog(this, "Chưa chọn nguyên liệu để nhập!", "Input warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String maNV = lb_MaNV.getText();
        Date date = new Date();
        String ngayNhap = format.format(date);
        String ngay = format2.format(date);
        String maPN = ("PN_" + ngay).trim();
        Connection conn = CONNECTION.getConnection();
        try {
            conn.setAutoCommit(false);

            String sql_insertPN = "INSERT INTO PHIEUNHAP values(?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql_insertPN);
            ps.setString(1, maPN);
            ps.setString(2, ngayNhap);
            ps.setString(3, maNV);
            ps.execute();
            ps.close();

            String sql = "EXEC dbo.SP_INSERT_CTPN @CT=?";
            SQLServerDataTable dt = new SQLServerDataTable();
            dt.addColumnMetadata("MAPN", java.sql.Types.NCHAR);
            dt.addColumnMetadata("MADL", java.sql.Types.NCHAR);
            dt.addColumnMetadata("SOLUONG", java.sql.Types.INTEGER);
            dt.addColumnMetadata("DONGIA", java.sql.Types.INTEGER);
            for (int i = 0; i < row; i++) {
                String maNL = tb_GioNL.getValueAt(i, 0).toString();
                String soLuong = tb_GioNL.getValueAt(i, 2).toString();
                String donGia = tb_GioNL.getValueAt(i, 3).toString();
                dt.addRow(maPN, maNL, soLuong, donGia);
            }
            PreparedStatement stmt = conn.prepareStatement(sql);
            ((SQLServerPreparedStatement) stmt).setStructured(1, "dbo.TYPE_CTPN", dt);
            stmt.execute();
            stmt.close();

            conn.commit();
            conn.setAutoCommit(true);
            conn.close();
            JOptionPane.showMessageDialog(null, "Ghi phiếu nhập thành công!");
            btnHuyPNActionPerformed(evt);
            loadDataTableKho();
            loadDataTablePN();
            pn_data.docListPN();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi GHI phiếu nhập!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGhiPhieuNhapActionPerformed

    private void txtTuNgayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTuNgayActionPerformed
        // TODO add your handling code here:
        txtDenNgay.requestFocus();
    }//GEN-LAST:event_txtTuNgayActionPerformed

    private void txtDenNgayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDenNgayActionPerformed
        // TODO add your handling code here:
        String tuNgay = txtTuNgay.getText().trim();
        String denNgay = txtDenNgay.getText().trim();
        if (checkDate(tuNgay) == false) {
            JOptionPane.showMessageDialog(this, "Hãy nhập từ ngày phù hợp (yyyy-MM-dd)!", "Input warning", JOptionPane.WARNING_MESSAGE);
            txtTuNgay.requestFocus();
            return;
        }
        if (checkDate(denNgay) == false) {
            JOptionPane.showMessageDialog(this, "Hãy nhập đến ngày phù hợp (yyyy-MM-dd)!", "Input warning", JOptionPane.WARNING_MESSAGE);
            txtDenNgay.requestFocus();
            return;
        }
        try {
            Date check_to = format1.parse(denNgay);
            Date check_from = format1.parse(tuNgay);
            if (check_to.before(check_from)) {
                JOptionPane.showMessageDialog(this, "Hãy chọn khoảng thời gian phù hợp!", "Input warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String denngay = denNgay + " 23:59:59";
            loadDataTablePN_TG(tuNgay, denngay);
            editorCTPN.setText("");
            btnInPN.setEnabled(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hãy nhập ngày hợp lệ (yyyy-MM-dd)!", "Input warning", JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_txtDenNgayActionPerformed

    private void tb_PNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_PNMouseClicked
        // TODO add your handling code here:
        int i = tb_PN.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) tb_PN.getModel();
        String maPN = tb_PN.getValueAt(i, 0).toString();
        editorCTPN.setText(inCTPN(maPN));
        btnInPN.setEnabled(true);

    }//GEN-LAST:event_tb_PNMouseClicked

    private void btnCapNhat_TTCaNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhat_TTCaNhanActionPerformed
        // TODO add your handling code here:
        String userName = txtUserName.getText();
        String ten = txtFullName.getText();
        String matKhau = txtPass1.getText().toString();
        String xacNhanMK = txtPass2.getText().toString();
        boolean check = true;
        if (matKhau.matches("\\w{1,10}") == false) {
            JOptionPane.showMessageDialog(this, "Mật khẩu tối đa 10 ký tự!", "Input warning", JOptionPane.WARNING_MESSAGE);
            check = false;
        }
        if (xacNhanMK.equals(matKhau) == false) {
            JOptionPane.showMessageDialog(this, "Xác nhận mật khẩu sai!!!", "Input warning", JOptionPane.WARNING_MESSAGE);
            check = false;
        }
        if (check == true) {
            tk_data.updateAccount(userName, ten, matKhau);
            tk_data.docListTK();
            loadDataTableDSTK();
            loadTTCaNhan(userName);
        }
    }//GEN-LAST:event_btnCapNhat_TTCaNhanActionPerformed

    private void btnInPNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInPNActionPerformed
        // TODO add your handling code here:
        try {
            editorCTPN.print();
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, "Lỗi in CTPN!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnInPNActionPerformed

    private void tb_QLNLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_QLNLMouseClicked
        // TODO add your handling code here:
        int i = tb_QLNL.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) tb_QLNL.getModel();
        txtMaNL_QL.setText(dtm.getValueAt(i, 0).toString());
        txtTenNL_QL.setText(dtm.getValueAt(i, 1).toString());
        txtDVT_QL.setText(dtm.getValueAt(i, 2).toString());
        txtSLB_NL.setText("0");
        sNL = dtm.getValueAt(i, 3).toString();
    }//GEN-LAST:event_tb_QLNLMouseClicked

    private void btnTimNL_QLNLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimNL_QLNLActionPerformed
        // TODO add your handling code here:
        loadDataTableKho_TheoTen(tb_QLNL, txtTimKiem_QLNL.getText());
    }//GEN-LAST:event_btnTimNL_QLNLActionPerformed

    private void tb_DSTKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_DSTKMouseClicked
        // TODO add your handling code here:
        int i = tb_DSTK.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) tb_DSTK.getModel();
        txtUsername_TK.setText(dtm.getValueAt(i, 0).toString());
        txtFullname_TK.setText(dtm.getValueAt(i, 1).toString());
        DSTAIKHOAN tk = tk_data.getTK(dtm.getValueAt(i, 0).toString());
        txtPass_TK.setText(tk.getPassWord());
        cbChucVu_TK.setSelectedItem(dtm.getValueAt(i, 2).toString());
        cbTTXOA_Tk.setSelectedItem(dtm.getValueAt(i, 3).toString());
    }//GEN-LAST:event_tb_DSTKMouseClicked

    private void btnThemTKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemTKActionPerformed
        // TODO add your handling code here:
        action_QLTK = "Thêm";
        btnSuaTK.setEnabled(false);
        btnXoaTK.setEnabled(false);
        btnGhiTK.setEnabled(true);
        btnHuy_TK.setEnabled(true);
        txtUsername_TK.setText("");
        txtFullname_TK.setText("");
        txtPass_TK.setText("");
    }//GEN-LAST:event_btnThemTKActionPerformed

    private void btnSuaTKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaTKActionPerformed
        // TODO add your handling code here:
        action_QLTK = "Sửa";
        btnThemTK.setEnabled(false);
        btnXoaTK.setEnabled(false);
        btnGhiTK.setEnabled(true);
        btnHuy_TK.setEnabled(true);
        txtUsername_TK.setEditable(false);
        txtUsername_TK.setText("");
        txtFullname_TK.setText("");
        txtPass_TK.setText("");
    }//GEN-LAST:event_btnSuaTKActionPerformed

    private void btnXoaTKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaTKActionPerformed
        // TODO add your handling code here:
        action_QLTK = "Xóa";
        btnThemTK.setEnabled(false);
        btnSuaTK.setEnabled(false);
        btnGhiTK.setEnabled(true);
        btnHuy_TK.setEnabled(true);
        txtUsername_TK.setEditable(false);
        txtFullname_TK.setEditable(false);
        txtPass_TK.setEditable(false);
        cbChucVu_TK.setEnabled(false);
        cbTTXOA_Tk.setEnabled(false);
        txtUsername_TK.setText("");
        txtFullname_TK.setText("");
        txtPass_TK.setText("");
    }//GEN-LAST:event_btnXoaTKActionPerformed

    private void btnGhiTKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGhiTKActionPerformed
        // TODO add your handling code here:
        try {
            String userName = txtUsername_TK.getText().trim();
            String fullName = txtFullname_TK.getText().trim();
            String passWord = txtPass_TK.getText();
            String chucvu = cbChucVu_TK.getSelectedItem().toString();
            String ttXoa = cbTTXOA_Tk.getSelectedItem().toString();
            String chucVu = "";
            int ttxoa;
            if (chucvu.equals("Quản lý")) {
                chucVu = "QL";
            } else {
                chucVu = "NV";
            }
            if (ttXoa.equals("Bị khóa")) {
                ttxoa = 1;
            } else {
                ttxoa = 0;
            }
            if (action_QLTK.equals("Thêm")) {
                boolean check = true;
                if (userName.matches("\\w{1,}") == false) {
                    JOptionPane.showMessageDialog(this, "Nhập UserName lỗi ký tự!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    check = false;
                }
                if (fullName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nhập FullName!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    check = false;
                }
                if (passWord.matches("\\w{1,10}") == false) {
                    JOptionPane.showMessageDialog(this, "Mật khẩu tối đa 10 ký tự!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    check = false;
                }
                if (check == true) {
                    if (tk_data.kiemTraUserName(userName) == false) {
                        DSTAIKHOAN_DATA.themTaiKhoan(userName, fullName, passWord, chucVu, ttxoa);
                        loadDataTableDSTK();
                    } else {
                        JOptionPane.showMessageDialog(this, "Username đã được sử dụng!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } else if (action_QLTK.equals("Sửa")) {
                boolean check_s = true;
                if (userName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Hãy chọn tài khoản để sửa!", "Input warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (fullName.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Nhập FullName!", "Input warning", JOptionPane.WARNING_MESSAGE);
                        check_s = false;
                    }
                    if (passWord.matches("\\w{1,10}") == false) {
                        JOptionPane.showMessageDialog(this, "Mật khẩu tối đa 10 ký tự!", "Input warning", JOptionPane.WARNING_MESSAGE);
                        check_s = false;
                    }
                    if (check_s == true) {
                        DSTAIKHOAN_DATA.suaTaiKhoan(userName, fullName, passWord, chucVu, ttxoa);
                        loadDataTableDSTK();
                        String user = lb_MaNV.getText();
                        if (user.equals(userName)) {
                            loadTTCaNhan(userName);
                        }
                        txtUsername_TK.setText("");
                        txtFullname_TK.setText("");
                        txtPass_TK.setText("");
                    }
                }
            } else if (action_QLTK.equals("Xóa")) {
                boolean check_x = true;
                if (userName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Hãy chọn tài khoản để xóa!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    check_x = false;
                }
                if (check_x == true) {
                    DSTAIKHOAN_DATA.xoaTaiKhoan(userName);
                    loadDataTableDSTK();
                    txtUsername_TK.setText("");
                    txtFullname_TK.setText("");
                    txtPass_TK.setText("");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Chưa chọn hành động!", "Action warning", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            System.out.println("Lỗi");
        }
    }//GEN-LAST:event_btnGhiTKActionPerformed

    private void btnHuy_TKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuy_TKActionPerformed
        // TODO add your handling code here:
        action_QLTK = "";
        txtUsername_TK.setText("");
        txtFullname_TK.setText("");
        txtPass_TK.setText("");
        btnThemTK.setEnabled(true);
        btnSuaTK.setEnabled(true);
        btnXoaTK.setEnabled(true);
        btnGhiTK.setEnabled(false);
        btnHuy_TK.setEnabled(false);
        txtUsername_TK.setEditable(true);
        txtFullname_TK.setEditable(true);
        txtPass_TK.setEditable(true);
        cbChucVu_TK.setEnabled(true);
        cbTTXOA_Tk.setEnabled(true);
    }//GEN-LAST:event_btnHuy_TKActionPerformed

    private void btnThemNLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemNLActionPerformed
        // TODO add your handling code here:
        action_QLNL = "Thêm";
        btnSuaNL.setEnabled(false);
        btnXoaNL.setEnabled(false);
        btnCapNhatSL_NL.setEnabled(false);
        btnGhiNL.setEnabled(true);
        btnHuyNL.setEnabled(true);
        txtMaNL_QL.setText("");
        txtTenNL_QL.setText("");
        txtDVT_QL.setText("");
    }//GEN-LAST:event_btnThemNLActionPerformed

    private void btnSuaNLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaNLActionPerformed
        // TODO add your handling code here:
        action_QLNL = "Sửa";
        btnThemNL.setEnabled(false);
        btnXoaNL.setEnabled(false);
        btnCapNhatSL_NL.setEnabled(false);
        btnGhiNL.setEnabled(true);
        btnHuyNL.setEnabled(true);
        txtMaNL_QL.setEditable(false);
        txtMaNL_QL.setText("");
        txtTenNL_QL.setText("");
        txtDVT_QL.setText("");
    }//GEN-LAST:event_btnSuaNLActionPerformed

    private void btnXoaNLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaNLActionPerformed
        // TODO add your handling code here:
        action_QLNL = "Xóa";
        btnSuaNL.setEnabled(false);
        btnThemNL.setEnabled(false);
        btnCapNhatSL_NL.setEnabled(false);
        btnGhiNL.setEnabled(true);
        btnHuyNL.setEnabled(true);
        txtMaNL_QL.setEditable(false);
        txtTenNL_QL.setEditable(false);
        txtDVT_QL.setEditable(false);
        txtMaNL_QL.setText("");
        txtTenNL_QL.setText("");
        txtDVT_QL.setText("");
    }//GEN-LAST:event_btnXoaNLActionPerformed

    private void btnHuyNLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyNLActionPerformed
        // TODO add your handling code here:
        action_QLNL = "";
        btnThemNL.setEnabled(true);
        btnSuaNL.setEnabled(true);
        btnXoaNL.setEnabled(true);
        btnGhiNL.setEnabled(false);
        btnHuyNL.setEnabled(false);
        btnCapNhatSL_NL.setEnabled(true);
        txtSLB_NL.setText("0");
        txtSLB_NL.setEnabled(false);
        txtMaNL_QL.setText("");
        txtMaNL_QL.setEditable(true);
        txtTenNL_QL.setText("");
        txtTenNL_QL.setEditable(true);
        txtDVT_QL.setText("");
        txtDVT_QL.setEditable(true);
        DefaultTableModel dtm = (DefaultTableModel) tb_CTPH.getModel();
        int rows = dtm.getRowCount();
        for (int i = rows - 1; i >= 0; i--) {
            dtm.removeRow(i);
        }
    }//GEN-LAST:event_btnHuyNLActionPerformed

    private void btnGhiNLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGhiNLActionPerformed
        // TODO add your handling code here:
        try {
            String maNL = txtMaNL_QL.getText().trim();
            String tenNL = txtTenNL_QL.getText().trim();
            String dvt = txtDVT_QL.getText().trim();
            if (action_QLNL.equals("Thêm")) {
                boolean check = true;
                if (maNL.matches("\\w{1,}") == false) {
                    JOptionPane.showMessageDialog(this, "Nhập MANL lỗi ký tự!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    check = false;
                }
                if (tenNL.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nhập tên nguyên liệu!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    check = false;
                }
                if (dvt.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nhập đơn vị tính!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    check = false;
                }
                if (check == true) {
                    if (nl_Data.kiemTraMaNL(maNL) == false) {
                        NGUYENLIEU_DATA.themNguyenLieu(maNL, tenNL, dvt);
                        nl_Data.docListNL();
                        loadDataTableKho();
                        loadDataTableNL_CTDU();
                    } else {
                        JOptionPane.showMessageDialog(this, "Mã nguyên liệu đã được sử dụng!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } else if (action_QLNL.equals("Sửa")) {
                boolean check = true;
                if (maNL.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Hãy chọn nguyên liệu để sửa!", "Input warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (tenNL.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Nhập tên nguyên liệu!", "Input warning", JOptionPane.WARNING_MESSAGE);
                        check = false;
                    }
                    if (dvt.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Nhập đơn vị tính!", "Input warning", JOptionPane.WARNING_MESSAGE);
                        check = false;
                    }
                    if (check == true) {
                        NGUYENLIEU_DATA.suaNguyenLieu(maNL, tenNL, dvt);
                        nl_Data.docListNL();
                        loadDataTableKho();
                        loadDataTableNL_CTDU();
                    }
                }
            } else if (action_QLNL.equals("Xóa")) {
                boolean check = true;
                if (maNL.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Hãy chọn nguyên liệu để xóa!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    check = false;
                }

                if (!sNL.equals("0")) {
                    JOptionPane.showMessageDialog(this, "Chỉ xoá nguyên liệu có số lượng tồn là 0!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    check = false;
                }
                if (check == true) {
                    NGUYENLIEU_DATA.xoaNguyenLieu(maNL);
                    loadDataTableKho();
                    txtMaNL_QL.setText("");
                    txtTenNL_QL.setText("");
                    txtDVT_QL.setText("");
                    loadDataTableNL_CTDU();
                }
            } else if (action_QLNL.equals("UpdateSLT")) {
                int row = tb_CTPH.getRowCount();
                if (row < 1) {
                    JOptionPane.showMessageDialog(this, "Chưa chọn nguyên liệu để nhập!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String maNV = lb_MaNV.getText();
                Date date = new Date();
                String ngayHuy = format.format(date);
                String ngay = format2.format(date);
                String maPH = ("PH_" + ngay).trim();
                Connection conn = CONNECTION.getConnection();
                try {
                    conn.setAutoCommit(false);

                    String sql_insertPH = "INSERT INTO PHIEUHUYNL values(?,?,?)";
                    PreparedStatement ps = conn.prepareStatement(sql_insertPH);
                    ps.setString(1, maPH);
                    ps.setString(2, ngayHuy);
                    ps.setString(3, maNV);
                    ps.execute();
                    ps.close();

                    String sql = "EXEC dbo.SP_INSERT_CTPH @CT=?";
                    SQLServerDataTable dt = new SQLServerDataTable();
                    dt.addColumnMetadata("MA", java.sql.Types.NCHAR);
                    dt.addColumnMetadata("MANL", java.sql.Types.NCHAR);
                    dt.addColumnMetadata("SOLUONG", java.sql.Types.INTEGER);
                    for (int i = 0; i < row; i++) {
                        String maNLHuy = tb_CTPH.getValueAt(i, 0).toString();
                        String soLuongHuy = tb_CTPH.getValueAt(i, 2).toString();
                        dt.addRow(maPH, maNLHuy, soLuongHuy);
                    }
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    ((SQLServerPreparedStatement) stmt).setStructured(1, "dbo.TYPE_CTXNL", dt);
                    stmt.execute();
                    stmt.close();

                    conn.commit();
                    conn.setAutoCommit(true);
                    conn.close();
                    JOptionPane.showMessageDialog(null, "Ghi phiếu huỷ nguyên liệu thành công!");
                    btnHuyNLActionPerformed(evt);
                    loadDataTableKho();
                    loadTablePH_trongNgay();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Lỗi GHI phiếu huỷ!", "ERROR!", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi ghi nguyên liệu!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGhiNLActionPerformed

    private void btnCapNhatSL_NLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatSL_NLActionPerformed
        // TODO add your handling code here:
        action_QLNL = "UpdateSLT";
        txtSLB_NL.setEnabled(true);
        btnThemNL.setEnabled(false);
        btnXoaNL.setEnabled(false);
        btnSuaNL.setEnabled(false);
        btnGhiNL.setEnabled(true);
        btnHuyNL.setEnabled(true);
        txtMaNL_QL.setEditable(false);
        txtTenNL_QL.setEditable(false);
        txtDVT_QL.setEditable(false);

        DefaultTableModel dtm = (DefaultTableModel) tb_CTPH.getModel();
        int row = tb_QLNL.getSelectedRow();
        if (row > -1) {
            if (!sNL.equals("0")) {
                try {
                    int soLuong = Integer.parseInt(txtSLB_NL.getText());
                    String maNL = txtMaNL_QL.getText();
                    String tenNL = txtTenNL_QL.getText();
                    boolean check = true;
                    int slt = Integer.parseInt(sNL);
                    if (soLuong <= 0) {
                        check = false;
                        JOptionPane.showMessageDialog(this, "Nhập số lượng bỏ > 0!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    }
                    if (soLuong > slt) {
                        check = false;
                        JOptionPane.showMessageDialog(this, "Nhập số lượng bỏ < SLT!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    }
                    if (check == true) {
                        for (int i = 0; i < tb_CTPH.getRowCount(); i++) {
                            if (maNL.equals(tb_CTPH.getValueAt(i, 0))) {
                                tb_CTPH.setValueAt(soLuong, i, 2);
                                return;
                            }
                        }
                        Vector vt = new Vector();
                        vt.add(maNL);
                        vt.add(tenNL);
                        vt.add(soLuong);
                        dtm.addRow(vt);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng huỷ!", "Input warning", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Nguyên liệu có SLT =0!", "Input warning", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Chưa chọn nguyên liệu để nhập!", "Input warning", JOptionPane.WARNING_MESSAGE);
        }
        tb_CTPH.setModel(dtm);
    }//GEN-LAST:event_btnCapNhatSL_NLActionPerformed

    private void cbDanhMucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDanhMucActionPerformed
        // TODO add your handling code here:
        try {
            String danhMuc = cbDanhMuc.getSelectedItem().toString();
            String maDM = dm_Data.tenDM_to_maDM(danhMuc);
            loadDataTableDU_DM(maDM);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi lấy danh sách đồ uống theo danh mục!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_cbDanhMucActionPerformed

    private void tb_QLDUMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_QLDUMouseClicked
        // TODO add your handling code here:
        int i = tb_QLDU.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) tb_QLDU.getModel();
        String maDU = dtm.getValueAt(i, 0).toString();
        txtMaDU.setText(maDU);
        txtTenDU.setText(dtm.getValueAt(i, 1).toString());
        String tinhTrang = dtm.getValueAt(i, 2).toString();
        if (tinhTrang.equals("Chưa có công thức")) {
            cbTTXoa_DU.setEnabled(false);
            tt_DU = "KoCT";
        } else {
            if (action_QLDU.equals("Thêm")) {
                cbTTXoa_DU.setEnabled(false);
            } else if (action_QLDU.equals("Sửa")) {
                cbTTXoa_DU.setEnabled(true);
            } else if (action_QLDU.equals("Xoá")) {
                cbTTXoa_DU.setEnabled(false);
            }
            cbTTXoa_DU.setSelectedItem(dtm.getValueAt(i, 2).toString());
            tt_DU = "CoCT";
        }
        txtGiaBan.setText(dtm.getValueAt(i, 3).toString());
        txtGiaSale.setText(dtm.getValueAt(i, 4).toString());
        DOUONG du = du_Data.getDU(maDU);
        String anh = du.getAnh();
        imgDU.setIcon(getAnhDU("src/Images/" + anh));

    }//GEN-LAST:event_tb_QLDUMouseClicked

    private void btnChonAnhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonAnhActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser("src/Images/");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Tệp hình ảnh", "jpg", "png", "jpeg");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            fileAnhDU = fileChooser.getSelectedFile();
            imgDU.setIcon(getAnhDU(fileAnhDU.getPath()));
        }
    }//GEN-LAST:event_btnChonAnhActionPerformed

    private void btnThemDUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemDUActionPerformed
        // TODO add your handling code here:
        action_QLDU = "Thêm";
        btnSuaDU.setEnabled(false);
        btnXoaDU.setEnabled(false);
        btnGhiDU.setEnabled(true);
        btnHuyDU.setEnabled(true);
        cbTTXoa_DU.setEnabled(false);
        txtMaDU.setText("");
        txtTenDU.setText("");
        txtGiaBan.setText("");
        txtGiaSale.setText("");
        imgDU.setIcon(getAnhDU("src/Images/default.png"));
    }//GEN-LAST:event_btnThemDUActionPerformed

    private void btnSuaDUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaDUActionPerformed
        // TODO add your handling code here:
        action_QLDU = "Sửa";
        btnThemDU.setEnabled(false);
        btnXoaDU.setEnabled(false);
        btnGhiDU.setEnabled(true);
        btnHuyDU.setEnabled(true);
        txtMaDU.setEditable(false);
        txtMaDU.setText("");
        txtTenDU.setText("");
        txtGiaBan.setText("");
        txtGiaSale.setText("");
        imgDU.setIcon(getAnhDU("src/Images/default.png"));
    }//GEN-LAST:event_btnSuaDUActionPerformed

    private void btnXoaDUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaDUActionPerformed
        // TODO add your handling code here:
        action_QLDU = "Xoá";
        btnThemDU.setEnabled(false);
        btnSuaDU.setEnabled(false);
        btnGhiDU.setEnabled(true);
        btnHuyDU.setEnabled(true);
        txtMaDU.setEditable(false);
        txtTenDU.setEditable(false);
        txtGiaBan.setEditable(false);
        txtGiaSale.setEditable(false);
        cbTTXoa_DU.setEnabled(false);
        txtMaDU.setText("");
        txtTenDU.setText("");
        txtGiaBan.setText("");
        txtGiaSale.setText("");
        imgDU.setIcon(getAnhDU("src/Images/default.png"));
    }//GEN-LAST:event_btnXoaDUActionPerformed

    private void btnHuyDUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyDUActionPerformed
        // TODO add your handling code here:
        action_QLDU = "";
        btnThemDU.setEnabled(true);
        btnSuaDU.setEnabled(true);
        btnXoaDU.setEnabled(true);
        btnGhiDU.setEnabled(false);
        btnHuyDU.setEnabled(false);
        txtMaDU.setEditable(true);
        txtTenDU.setEditable(true);
        txtGiaBan.setEditable(true);
        txtGiaSale.setEditable(true);
        cbTTXoa_DU.setEnabled(true);
        txtMaDU.setText("");
        txtTenDU.setText("");
        txtGiaBan.setText("");
        txtGiaSale.setText("");
        imgDU.setIcon(getAnhDU("src/Images/default.png"));
    }//GEN-LAST:event_btnHuyDUActionPerformed

    private void btnGhiDUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGhiDUActionPerformed
        // TODO add your handling code here:
        try {
            String maDU = txtMaDU.getText().trim();
            String maDM = dm_Data.tenDM_to_maDM(cbDanhMuc.getSelectedItem().toString());
            String tenDU = txtTenDU.getText().trim();
            String anh = fileAnhDU.getName();
            String ttXoa = cbTTXoa_DU.getSelectedItem().toString();
            int ttxoa;
            if (ttXoa.equals("Bị xoá")) {
                ttxoa = 1;
            } else {
                ttxoa = 0;
            }
            int giaBan = 0;
            String giaSale = txtGiaSale.getText().trim();

            if (action_QLDU.equals("Thêm")) {
                boolean check = true;
                int giaban;
                if (maDU.matches("\\w{1,}") == false) {
                    JOptionPane.showMessageDialog(this, "Nhập MADU lỗi ký tự!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    check = false;
                }
                if (tenDU.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nhập tên đồ uống!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    check = false;
                }
                try {
                    giaBan = Integer.parseInt(txtGiaBan.getText().trim());
                    if (giaBan <= 0) {
                        check = false;
                        JOptionPane.showMessageDialog(this, "Nhập giá bán > 0!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Nhập giá bán!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    txtGiaBan.requestFocus();
                    return;
                }
                if (check == true) {
                    if (du_Data.kiemTraMaDU(maDU) == false) {
                        DOUONG_DATA.add_DU(maDU, maDM, tenDU, anh, 2, giaBan, giaSale);
                        luuFileAnh();
                        loadDataTableDU_DM(maDM);
                        loadDataTableDU_KoCT();
                    } else {
                        JOptionPane.showMessageDialog(this, "Mã đồ uống đã được sử dụng!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } else if (action_QLDU.equals("Sửa")) {
                boolean check = true;
                if (maDU.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Hãy chọn đồ uống để sửa!", "Input warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (tenDU.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Nhập tên đồ uống!", "Input warning", JOptionPane.WARNING_MESSAGE);
                        check = false;
                    }
                    try {
                        giaBan = Integer.parseInt(txtGiaBan.getText().trim());
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Nhập giá bán!", "Input warning", JOptionPane.WARNING_MESSAGE);
                        txtGiaBan.requestFocus();
                        return;
                    }
                    if (check == true) {
                        if (tt_DU.equals("KoCT")) {
                            DOUONG_DATA.update_DU(maDU, maDM, tenDU, anh, 2, giaBan, giaSale);
                        } else {
                            DOUONG_DATA.update_DU(maDU, maDM, tenDU, anh, ttxoa, giaBan, giaSale);
                        }
                        luuFileAnh();
                        loadDataTableDU_DM(maDM);
                        loadDataTableDU_KoCT();
                        loadDataTable_DUPX(maDM);
                    }
                }
            } else if (action_QLDU.equals("Xoá")) {
                if (maDU.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Hãy chọn đồ uống để xoá!", "Input warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    DOUONG_DATA.del_DU(maDU);
                    loadDataTableDU_DM(maDM);
                    loadDataTable_DUPX(maDM);
                    loadDataTableDU_KoCT();
                    txtMaDU.setText("");
                    txtTenDU.setText("");
                    txtGiaBan.setText("");
                    txtGiaSale.setText("");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi ghi đồ uống!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGhiDUActionPerformed

    private void cbDanhMuc_CTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDanhMuc_CTActionPerformed
        // TODO add your handling code here:
        try {
            String danhMuc = cbDanhMuc_CT.getSelectedItem().toString();
            String maDM = dm_Data.tenDM_to_maDM(danhMuc);
            loadDataTableDU_CoCT(maDM);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi lấy danh sách đồ uống có công thức theo danh mục!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_cbDanhMuc_CTActionPerformed

    private void tb_DU_CoCTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_DU_CoCTMouseClicked
        // TODO add your handling code here:
        int i = tb_DU_CoCT.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) tb_DU_CoCT.getModel();
        String maDU = dtm.getValueAt(i, 0).toString();
        lbMADU_CT.setText(maDU);
        loadDataTable_CTDU(maDU);
    }//GEN-LAST:event_tb_DU_CoCTMouseClicked

    private void tb_NL_CTDUMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_NL_CTDUMouseClicked
        // TODO add your handling code here:
        int i = tb_NL_CTDU.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) tb_NL_CTDU.getModel();
        txtMaNL_CTDU.setText(dtm.getValueAt(i, 0).toString());
        txtTenNL_CTDU.setText(dtm.getValueAt(i, 1).toString());
        txtSL_CTDU.setText("0");
    }//GEN-LAST:event_tb_NL_CTDUMouseClicked

    private void tb_CTDUMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_CTDUMouseClicked
        // TODO add your handling code here:
        int i = tb_CTDU.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) tb_CTDU.getModel();
        txtMaNL_CTDU.setText(dtm.getValueAt(i, 0).toString());
        txtTenNL_CTDU.setText(dtm.getValueAt(i, 1).toString());
        txtSL_CTDU.setText(dtm.getValueAt(i, 2).toString());
    }//GEN-LAST:event_tb_CTDUMouseClicked

    private void btnGhiCTDUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGhiCTDUActionPerformed
        // TODO add your handling code here:
        int row = tb_CTDU.getRowCount();
        String danhMuc = cbDanhMuc_CT.getSelectedItem().toString();
        String maDM = dm_Data.tenDM_to_maDM(danhMuc);
        String maDU = lbMADU_CT.getText();
        if (row < 1) {
            ctdu_data.xoa_CTDU(maDU);
            loadDataTableDU_KoCT();
            loadDataTableDU_CoCT(maDM);
            cbDanhMucActionPerformed(evt);
            cbDanhMuc_CTActionPerformed(evt);
        } else {
            try {
                Connection conn = CONNECTION.getConnection();
                String sql = "EXEC dbo.SP_UPDATE_CTDU @CT=?";
                SQLServerDataTable dt = new SQLServerDataTable();
                dt.addColumnMetadata("MADU", java.sql.Types.NCHAR);
                dt.addColumnMetadata("MANL", java.sql.Types.NCHAR);
                dt.addColumnMetadata("SOLUONG", java.sql.Types.INTEGER);
                for (int i = 0; i < row; i++) {
                    String maNl = tb_CTDU.getValueAt(i, 0).toString();
                    String soLuong = tb_CTDU.getValueAt(i, 2).toString();
                    dt.addRow(maDU, maNl, soLuong);
                }
                PreparedStatement stmt = conn.prepareStatement(sql);
                ((SQLServerPreparedStatement) stmt).setStructured(1, "dbo.TYPE_CTDU", dt);
                stmt.execute();
                du_Data.update_TT_DU(maDU, 0);
                JOptionPane.showMessageDialog(null, "Ghi CTDU thành công!");
                loadDataTableDU_KoCT();
                loadDataTableDU_CoCT(maDM);
                cbDanhMuc_CTActionPerformed(evt);
                cbDanhMucActionPerformed(evt);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi GHI CTDU!", "ERROR!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnGhiCTDUActionPerformed

    private void btnXoa_CTDUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoa_CTDUActionPerformed
        // TODO add your handling code here:
        if (!lbMADU_CT.getText().equals("")) {
            DefaultTableModel dtm = (DefaultTableModel) tb_CTDU.getModel();
            int rows = dtm.getRowCount();
            if (rows > 0) {
                for (int i = rows - 1; i >= 0; i--) {
                    dtm.removeRow(i);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Không có CT để xoá!", "Input warning", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Chưa chọn công thức đồ uống!", "Input warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnXoa_CTDUActionPerformed

    private void btnHuyCTDUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyCTDUActionPerformed
        // TODO add your handling code here:
        if (!lbMADU_CT.getText().equals("")) {
            DefaultTableModel dtm = (DefaultTableModel) tb_CTDU.getModel();
            int row = tb_CTDU.getSelectedRow();
            if (row > -1) {
                String maDU = lbMADU_CT.getText();
                String maNL = dtm.getValueAt(row, 0).toString();
                CTDU_DATA.xoa_1dongCTDU(maDU, maNL);
                dtm.removeRow(row);
            } else {
                JOptionPane.showMessageDialog(this, "Chưa chọn chi tiết để huỷ!", "Input warning", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Chưa chọn công thức đồ uống!", "Input warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnHuyCTDUActionPerformed

    private void btnThem_CTDUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThem_CTDUActionPerformed
        // TODO add your handling code here:
        if (!lbMADU_CT.getText().equals("")) {
            DefaultTableModel dtm = (DefaultTableModel) tb_CTDU.getModel();
            int row = tb_NL_CTDU.getSelectedRow();
            if (row > -1) {
                try {
                    int soLuong = Integer.parseInt(txtSL_CTDU.getText());
                    if (soLuong > 0) {
                        String maNL = txtMaNL_CTDU.getText();
                        for (int i = 0; i < tb_CTDU.getRowCount(); i++) {
                            if (maNL.equals(tb_CTDU.getValueAt(i, 0))) {
                                tb_GioNL.setValueAt(soLuong, i, 2);
                                return;
                            }
                        }
                        String tenNL = txtTenNL_CTDU.getText();
                        Vector vt = new Vector();
                        vt.add(maNL);
                        vt.add(tenNL);
                        vt.add(soLuong);
                        dtm.addRow(vt);
                    } else {
                        JOptionPane.showMessageDialog(this, "Số lượng > 0!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng!", "Input warning", JOptionPane.WARNING_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Chưa chọn nguyên liệu để thêm!", "Input warning", JOptionPane.WARNING_MESSAGE);
            }
            tb_GioNL.setModel(dtm);
        } else {
            JOptionPane.showMessageDialog(this, "Chưa chọn đồ uống!", "Input warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnThem_CTDUActionPerformed

    private void tb_DU_KoCTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_DU_KoCTMouseClicked
        // TODO add your handling code here:
        int i = tb_DU_KoCT.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) tb_DU_KoCT.getModel();
        String maDU = dtm.getValueAt(i, 0).toString();
        lbMADU_CT.setText(maDU);
        DefaultTableModel dtm1 = (DefaultTableModel) tb_CTDU.getModel();
        dtm1.setRowCount(0);
    }//GEN-LAST:event_tb_DU_KoCTMouseClicked

    private void cbDanhMuc_PXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDanhMuc_PXActionPerformed
        // TODO add your handling code here:
        try {
            String danhMuc = cbDanhMuc_PX.getSelectedItem().toString();
            String maDM = dm_Data.tenDM_to_maDM(danhMuc);
            loadDataTable_DUPX(maDM);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi lấy danh sách đồ uống theo danh mục!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_cbDanhMuc_PXActionPerformed

    private void btnThemHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemHDActionPerformed
        // TODO add your handling code here:
        DefaultTableModel dtm = (DefaultTableModel) tb_CTHD.getModel();
        DefaultTableModel dtm1 = (DefaultTableModel) tb_CTPX.getModel();
        int row = tb_DSDU.getSelectedRow();
        int tongTien = 0;
        boolean check = true;
        if (row > -1) {
            try {
                int soLuong = Integer.parseInt(txtSoLuong_PX.getText());
                int donGia = Integer.parseInt(txtGia_PX.getText());
                String maDU = txtMaDU_PX.getText();

                if (soLuong < 0) {
                    JOptionPane.showMessageDialog(this, "Số lượng > 0!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    check = false;
                }
                if (soLuong == soLuong_DU(maDU)) {
                    JOptionPane.showMessageDialog(this, "Số lượng không đổi!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    check = false;
                }
                if (check == true) {
                    if (check_trungMADU(maDU) == true) {
                        for (int i = 0; i < tb_CTHD.getRowCount(); i++) {
                            if (maDU.equals(tb_CTHD.getValueAt(i, 0))) {
                                int soLuongCu = Integer.parseInt(tb_CTHD.getValueAt(i, 2).toString());
                                int thanhTien = soLuong * donGia;
                                int dem = 0;
                                int position = 0;
                                for (DU_ORDER du : dsdu) {

                                    if (maDU.equals(du.getMaDU())) {
                                        du.setSoLuong(soLuong);
                                        position = dem;
                                    }
                                    dem++;
                                }
                                if (kiemTraNLPhaChe(dsdu) == false) {
                                    DU_ORDER du = new DU_ORDER();
                                    du.setMaDU(maDU);
                                    du.setSoLuong(soLuongCu);
                                    dsdu.set(position, du);
                                    return;
                                } else {
                                    if (soLuong == 0) {
                                        dtm.removeRow(i);
                                    } else {
                                        tb_CTHD.setValueAt(soLuong, i, 2);
                                        tb_CTHD.setValueAt(thanhTien, i, 4);
                                        tb_CTHD.setModel(dtm);
                                    }
                                    soLuong -= soLuongCu;
                                }
                            }
                        }
                    } else {
                        DU_ORDER du = new DU_ORDER();
                        du.setMaDU(maDU);
                        du.setSoLuong(soLuong);
                        dsdu.add(du);
                        if (kiemTraNLPhaChe(dsdu) == false) {
                            dsdu.remove(du);
                            return;
                        } else {
                            String tenDU = txtTenDU_PX.getText();
                            int thanhTien = soLuong * donGia;
                            Vector vt = new Vector();
                            vt.add(maDU);
                            vt.add(tenDU);
                            vt.add(soLuong);
                            vt.add(donGia);
                            vt.add(thanhTien);
                            dtm.addRow(vt);
                        }
                    }
                    ArrayList<CT_DOUONG> ctdu = ctdu_data.getCTDU(maDU);
                    for (CT_DOUONG du : ctdu) {
                        String maNL = du.getMaNL();
                        int soLuongNL = soLuong * du.getSoLuong();
                        if (check_trungMANL(maNL) == true) {
                            for (int j = 0; j < tb_CTPX.getRowCount(); j++) {
                                if (maNL.equals(tb_CTPX.getValueAt(j, 0))) {
                                    int soLuongNLCu = Integer.parseInt(tb_CTPX.getValueAt(j, 2).toString());
                                    soLuongNL += soLuongNLCu;
                                    if (soLuongNL == 0) {
                                        dtm1.removeRow(j);

                                    } else {
                                        tb_CTPX.setValueAt(soLuongNL, j, 2);
                                    }
                                }
                            }
                        } else {
                            Vector vt1 = new Vector();
                            vt1.add(maNL);
                            vt1.add(nl_Data.maNL_to_tenNL(maNL));
                            vt1.add(soLuongNL);
                            dtm1.addRow(vt1);
                        }
                    }
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng!", "Input warning", JOptionPane.WARNING_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(this, "Chưa chọn đồ uống!", "Input warning", JOptionPane.WARNING_MESSAGE);
        }
        tb_CTHD.setModel(dtm);
        tb_CTPX.setModel(dtm1);
        for (int i = 0; i < tb_CTHD.getRowCount(); i++) {
            tongTien += Integer.parseInt(dtm.getValueAt(i, 4).toString());
        }
        lb_TongTienPX.setText(String.valueOf(tongTien));
    }//GEN-LAST:event_btnThemHDActionPerformed

    private void tb_DSDUMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_DSDUMouseClicked
        // TODO add your handling code here:
        int i = tb_DSDU.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) tb_DSDU.getModel();
        txtMaDU_PX.setText(dtm.getValueAt(i, 0).toString());
        txtTenDU_PX.setText(dtm.getValueAt(i, 1).toString());
        txtGia_PX.setText(dtm.getValueAt(i, 2).toString());
        txtSoLuong_PX.setText("1");

    }//GEN-LAST:event_tb_DSDUMouseClicked

    private void btnHuyPXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyPXActionPerformed
        // TODO add your handling code here:
        dsdu.removeAll(dsdu);
        DefaultTableModel dtm = (DefaultTableModel) tb_CTHD.getModel();
        int rows = dtm.getRowCount();
        if (rows > 0) {
            for (int i = rows - 1; i >= 0; i--) {
                dtm.removeRow(i);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Không có gì để xoá!", "Input warning", JOptionPane.WARNING_MESSAGE);
        }

        DefaultTableModel dtm1 = (DefaultTableModel) tb_CTPX.getModel();
        int rows1 = dtm1.getRowCount();
        if (rows1 > 0) {
            for (int i = rows1 - 1; i >= 0; i--) {
                dtm1.removeRow(i);
            }
        }
    }//GEN-LAST:event_btnHuyPXActionPerformed

    private void btnGhiPXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGhiPXActionPerformed
        // TODO add your handling code here:
        int row = tb_CTHD.getRowCount();
        int row1 = tb_CTPX.getRowCount();
        if (row < 1) {
            JOptionPane.showMessageDialog(this, "Chưa chọn đồ uống!", "Input warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String maNV = lb_MaNV.getText();
        Date date = new Date();
        String ngayXuat = format.format(date);
        String ngay = format2.format(date);
        String maPX = ("PX_" + ngay).trim();
        Connection conn = CONNECTION.getConnection();

        try {
            SQLServerDataTable dt1 = new SQLServerDataTable();
            dt1.addColumnMetadata("MA", java.sql.Types.NCHAR);
            dt1.addColumnMetadata("MANL", java.sql.Types.NCHAR);
            dt1.addColumnMetadata("SOLUONG", java.sql.Types.INTEGER);
            for (int i = 0; i < row1; i++) {
                String maNL = tb_CTPX.getValueAt(i, 0).toString();
                String soLuongNL = tb_CTPX.getValueAt(i, 2).toString();
                dt1.addRow(maPX, maNL, soLuongNL);
            }
            String checkSLT = "EXEC dbo.SP_CHECK_SLT @CT=?";
            PreparedStatement stmt2 = conn.prepareStatement(checkSLT);
            ((SQLServerPreparedStatement) stmt2).setStructured(1, "dbo.TYPE_CTXNL", dt1);
            ResultSet rs = stmt2.executeQuery();
            boolean check = true;
            while (rs.next()) {
                int soLuongCon = rs.getInt("SOLUONGCON");
                if (soLuongCon < 0) {
                    JOptionPane.showMessageDialog(this, "Lỗi! Số lượng nguyên liệu k đủ pha chế", "ERROR!", JOptionPane.ERROR_MESSAGE);
                    check = false;
                }
            }
            if (check == true) {
                conn.setAutoCommit(false);
                String sql_insertPX = "INSERT INTO PHIEUXUAT values(?,?,?)";
                PreparedStatement ps = conn.prepareStatement(sql_insertPX);
                ps.setString(1, maPX);
                ps.setString(2, ngayXuat);
                ps.setString(3, maNV);
                ps.execute();
                ps.close();
                
                String sql = "EXEC dbo.SP_INSERT_CTHD @CT=?";
                SQLServerDataTable dt = new SQLServerDataTable();
                dt.addColumnMetadata("MAPX", java.sql.Types.NCHAR);
                dt.addColumnMetadata("MADU", java.sql.Types.NCHAR);
                dt.addColumnMetadata("SOLUONG", java.sql.Types.INTEGER);
                dt.addColumnMetadata("DONGIA", java.sql.Types.INTEGER);
                for (int i = 0; i < row; i++) {
                    String maDU = tb_CTHD.getValueAt(i, 0).toString();
                    String soLuong = tb_CTHD.getValueAt(i, 2).toString();
                    String donGia = tb_CTHD.getValueAt(i, 3).toString();
                    dt.addRow(maPX, maDU, soLuong, donGia);
                }
                PreparedStatement stmt = conn.prepareStatement(sql);
                ((SQLServerPreparedStatement) stmt).setStructured(1, "dbo.TYPE_CTHD", dt);
                stmt.execute();
                stmt.close();

                String sql1 = "EXEC dbo.SP_INSERT_CTPX @CT=?";
                PreparedStatement stmt1 = conn.prepareStatement(sql1);
                ((SQLServerPreparedStatement) stmt1).setStructured(1, "dbo.TYPE_CTXNL", dt1);
                stmt1.execute();
                stmt1.close();
                
                conn.commit();
                conn.setAutoCommit(true);
                conn.close();
                JOptionPane.showMessageDialog(null, "Ghi phiếu xuất thành công!");
                btnHuyPXActionPerformed(evt);
                px_data.docListPX();
                loadDataTableKho();
                loadDataTablePX_trongNgay();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi GHI phiếu xuất!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGhiPXActionPerformed

    private void tb_PXMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_PXMouseClicked
        // TODO add your handling code here:
        int i = tb_PX.getSelectedRow();
        String maPX = tb_PX.getValueAt(i, 0).toString();
        editorCTHD.setText(inCTHD(maPX));
        editorCTPX.setText(inCTPX(maPX));
        btnInHD.setEnabled(true);
        btnInPX.setEnabled(true);
    }//GEN-LAST:event_tb_PXMouseClicked

    private void txtTuNgayPXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTuNgayPXActionPerformed
        // TODO add your handling code here:
        txtDenNgayPX.requestFocus();
    }//GEN-LAST:event_txtTuNgayPXActionPerformed

    private void txtDenNgayPXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDenNgayPXActionPerformed
        // TODO add your handling code here:
        String tuNgay = txtTuNgayPX.getText().trim();
        String denNgay = txtDenNgayPX.getText().trim();
        if (checkDate(tuNgay) == false) {
            JOptionPane.showMessageDialog(this, "Hãy nhập từ ngày phù hợp (yyyy-MM-dd)!", "Input warning", JOptionPane.WARNING_MESSAGE);
            txtTuNgayPX.requestFocus();
            return;
        }
        if (checkDate(denNgay) == false) {
            JOptionPane.showMessageDialog(this, "Hãy nhập đến ngày phù hợp (yyyy-MM-dd)!", "Input warning", JOptionPane.WARNING_MESSAGE);
            txtDenNgayPX.requestFocus();
            return;
        }
        try {
            Date check_to = format1.parse(denNgay);
            Date check_from = format1.parse(tuNgay);
            if (check_to.before(check_from)) {
                JOptionPane.showMessageDialog(this, "Hãy chọn khoảng thời gian phù hợp!", "Input warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String denngay = denNgay + " 23:59:59";
            loadDataTablePX_TG(tuNgay, denngay);
            editorCTHD.setText("");
            editorCTPX.setText("");
            btnInHD.setEnabled(false);
            btnInPX.setEnabled(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hãy nhập ngày hợp lệ (yyyy-MM-dd)!", "Input warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_txtDenNgayPXActionPerformed

    private void btnReload_PXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReload_PXActionPerformed
        // TODO add your handling code here:
        loadDataTablePX_trongNgay();
        txtTuNgayPX.setText("");
        txtDenNgayPX.setText("");
        editorCTHD.setText("");
        editorCTPX.setText("");
        btnInHD.setEnabled(false);
        btnInPX.setEnabled(false);
    }//GEN-LAST:event_btnReload_PXActionPerformed

    private void btnInHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInHDActionPerformed
        // TODO add your handling code here:
        try {
            editorCTHD.print();
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, "Lỗi in CTHD!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnInHDActionPerformed

    private void btnInPXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInPXActionPerformed
        // TODO add your handling code here:
        try {
            editorCTPX.print();
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, "Lỗi in CTPX!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnInPXActionPerformed

    private void btnReloadPNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReloadPNActionPerformed
        // TODO add your handling code here:
        loadDataTablePN();
        txtTuNgay.setText("");
        txtDenNgay.setText("");
        editorCTPN.setText("");
        btnInPN.setEnabled(false);
    }//GEN-LAST:event_btnReloadPNActionPerformed

    private void btn_OpenTTMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_OpenTTMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_OpenTTMouseExited

    private void btn_OpenTTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_OpenTTMouseClicked
        // TODO add your handling code here:
        pnOrder.setVisible(false);
        pnNhapNL.setVisible(false);
        pnQLNL.setVisible(false);
        pnQLDU.setVisible(false);
        pnQLTK.setVisible(false);
        pnThongKe.setVisible(false);
        pnTTCaNhan.setVisible(true);
    }//GEN-LAST:event_btn_OpenTTMouseClicked

    private void btn_OpenTTMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_OpenTTMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_OpenTTMouseMoved

    private void lb_ThongKeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_ThongKeMouseExited
        // TODO add your handling code here:
        btn_OpenThongKe.setBackground(new Color(254, 235, 208));
    }//GEN-LAST:event_lb_ThongKeMouseExited

    private void lb_ThongKeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_ThongKeMouseClicked
        // TODO add your handling code here:
        pnOrder.setVisible(false);
        pnNhapNL.setVisible(false);
        pnQLNL.setVisible(false);
        pnQLDU.setVisible(false);
        pnQLTK.setVisible(false);
        pnThongKe.setVisible(true);
        pnTTCaNhan.setVisible(false);
    }//GEN-LAST:event_lb_ThongKeMouseClicked

    private void lb_ThongKeMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_ThongKeMouseMoved
        // TODO add your handling code here:
        btn_OpenThongKe.setBackground(new Color(204, 204, 255));
    }//GEN-LAST:event_lb_ThongKeMouseMoved

    private void lb_QLTKMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_QLTKMouseExited
        // TODO add your handling code here:
        btn_OpenQLTK.setBackground(new Color(254, 235, 208));
    }//GEN-LAST:event_lb_QLTKMouseExited

    private void lb_QLTKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_QLTKMouseClicked
        // TODO add your handling code here:
        pnOrder.setVisible(false);
        pnNhapNL.setVisible(false);
        pnQLNL.setVisible(false);
        pnQLDU.setVisible(false);
        pnQLTK.setVisible(true);
        pnThongKe.setVisible(false);
        pnTTCaNhan.setVisible(false);
    }//GEN-LAST:event_lb_QLTKMouseClicked

    private void lb_QLTKMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_QLTKMouseMoved
        // TODO add your handling code here:
        btn_OpenQLTK.setBackground(new Color(204, 204, 255));
    }//GEN-LAST:event_lb_QLTKMouseMoved

    private void lb_QLDUMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_QLDUMouseExited
        // TODO add your handling code here:
        btn_OpenQLDU.setBackground(new Color(254, 235, 208));
    }//GEN-LAST:event_lb_QLDUMouseExited

    private void lb_QLDUMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_QLDUMouseClicked
        // TODO add your handling code here:
        pnOrder.setVisible(false);
        pnNhapNL.setVisible(false);
        pnQLNL.setVisible(false);
        pnQLDU.setVisible(true);
        pnQLTK.setVisible(false);
        pnThongKe.setVisible(false);
        pnTTCaNhan.setVisible(false);
    }//GEN-LAST:event_lb_QLDUMouseClicked

    private void lb_QLDUMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_QLDUMouseMoved
        // TODO add your handling code here:
        btn_OpenQLDU.setBackground(new Color(204, 204, 255));
    }//GEN-LAST:event_lb_QLDUMouseMoved

    private void lb_QLNLMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_QLNLMouseExited
        // TODO add your handling code here:
        btn_OpenQLNL.setBackground(new Color(254, 235, 208));
    }//GEN-LAST:event_lb_QLNLMouseExited

    private void lb_QLNLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_QLNLMouseClicked
        // TODO add your handling code here:
        pnOrder.setVisible(false);
        pnNhapNL.setVisible(false);
        pnQLNL.setVisible(true);
        pnQLDU.setVisible(false);
        pnQLTK.setVisible(false);
        pnThongKe.setVisible(false);
        pnTTCaNhan.setVisible(false);
    }//GEN-LAST:event_lb_QLNLMouseClicked

    private void lb_QLNLMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_QLNLMouseMoved
        // TODO add your handling code here:
        btn_OpenQLNL.setBackground(new Color(204, 204, 255));
    }//GEN-LAST:event_lb_QLNLMouseMoved

    private void lb_NhapNLMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_NhapNLMouseExited
        // TODO add your handling code here:
        btn_OpenNhapNL.setBackground(new Color(254, 235, 208));
    }//GEN-LAST:event_lb_NhapNLMouseExited

    private void lb_NhapNLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_NhapNLMouseClicked
        // TODO add your handling code here:
        pnOrder.setVisible(false);
        pnNhapNL.setVisible(true);
        pnQLNL.setVisible(false);
        pnQLDU.setVisible(false);
        pnQLTK.setVisible(false);
        pnThongKe.setVisible(false);
        pnTTCaNhan.setVisible(false);
    }//GEN-LAST:event_lb_NhapNLMouseClicked

    private void lb_NhapNLMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_NhapNLMouseMoved
        // TODO add your handling code here:
        btn_OpenNhapNL.setBackground(new Color(204, 204, 255));
    }//GEN-LAST:event_lb_NhapNLMouseMoved

    private void lb_OrderMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_OrderMouseExited
        // TODO add your handling code here:
        btn_OpenOrDer.setBackground(new Color(254, 235, 208));
    }//GEN-LAST:event_lb_OrderMouseExited

    private void lb_OrderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_OrderMouseClicked
        // TODO add your handling code here:
        pnOrder.setVisible(true);
        pnNhapNL.setVisible(false);
        pnQLNL.setVisible(false);
        pnQLDU.setVisible(false);
        pnQLTK.setVisible(false);
        pnThongKe.setVisible(false);
        pnTTCaNhan.setVisible(false);
    }//GEN-LAST:event_lb_OrderMouseClicked

    private void lb_OrderMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_OrderMouseMoved
        // TODO add your handling code here:
        btn_OpenOrDer.setBackground(new Color(204, 204, 255));
    }//GEN-LAST:event_lb_OrderMouseMoved

    private void txt_FromTop5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_FromTop5ActionPerformed
        // TODO add your handling code here:
        txt_ToTop5.requestFocus();
    }//GEN-LAST:event_txt_FromTop5ActionPerformed

    private void txt_ToTop5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_ToTop5ActionPerformed
        // TODO add your handling code here:
        String tuNgay = txt_FromTop5.getText().trim();
        String denNgay = txt_ToTop5.getText().trim();
        if (checkDate(tuNgay) == false) {
            JOptionPane.showMessageDialog(this, "Hãy nhập từ ngày phù hợp (yyyy-MM-dd)!", "Input warning", JOptionPane.WARNING_MESSAGE);
            txt_FromTop5.requestFocus();
            return;
        }
        if (checkDate(denNgay) == false) {
            JOptionPane.showMessageDialog(this, "Hãy nhập đến ngày phù hợp (yyyy-MM-dd)!", "Input warning", JOptionPane.WARNING_MESSAGE);
            txt_ToTop5.requestFocus();
            return;
        }
        try {
            Date check_to = format1.parse(denNgay);
            Date check_from = format1.parse(tuNgay);
            if (check_to.before(check_from)) {
                JOptionPane.showMessageDialog(this, "Hãy chọn khoảng thời gian phù hợp!", "Input warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String temp_denNgay = denNgay.substring(0, 8);
            String ngay_denNgay = denNgay.substring(8, 10);
            int dngay = Integer.parseInt(ngay_denNgay) + 1;
            String denngay = temp_denNgay + String.valueOf(dngay);

            loadTableTop5(tuNgay, denngay);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hãy nhập ngày hợp lệ (yyyy-MM-dd)!", "Input warning", JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_txt_ToTop5ActionPerformed

    private void tb_PHMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_PHMouseClicked
        // TODO add your handling code here:
        int i = tb_PH.getSelectedRow();
        String maPH = tb_PH.getValueAt(i, 0).toString();
        String maNV = tb_PH.getValueAt(i, 1).toString();
        editorCTPH.setText(inCTPH(maPH, maNV));
        btnInPH.setEnabled(true);
    }//GEN-LAST:event_tb_PHMouseClicked

    private void txtTuNgayPHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTuNgayPHActionPerformed
        // TODO add your handling code here:
        txtDenNgayPH.requestFocus();
    }//GEN-LAST:event_txtTuNgayPHActionPerformed

    private void txtDenNgayPHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDenNgayPHActionPerformed
        // TODO add your handling code here:
        String tuNgay = txtTuNgayPH.getText().trim();
        String denNgay = txtDenNgayPH.getText().trim();
        if (checkDate(tuNgay) == false) {
            JOptionPane.showMessageDialog(this, "Hãy nhập từ ngày phù hợp (yyyy-MM-dd)!", "Input warning", JOptionPane.WARNING_MESSAGE);
            txtTuNgayPH.requestFocus();
            return;
        }
        if (checkDate(denNgay) == false) {
            JOptionPane.showMessageDialog(this, "Hãy nhập đến ngày phù hợp (yyyy-MM-dd)!", "Input warning", JOptionPane.WARNING_MESSAGE);
            txtDenNgayPH.requestFocus();
            return;
        }
        try {
            Date check_to = format1.parse(denNgay);
            Date check_from = format1.parse(tuNgay);
            if (check_to.before(check_from)) {
                JOptionPane.showMessageDialog(this, "Hãy chọn khoảng thời gian phù hợp!", "Input warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String denngay = denNgay + " 23:59:59";
            loadTablePH_TG(tuNgay, denngay);
            editorCTPH.setText("");
            btnInPH.setEnabled(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hãy nhập ngày hợp lệ (yyyy-MM-dd)!", "Input warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_txtDenNgayPHActionPerformed

    private void btnReloadPN1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReloadPN1ActionPerformed
        // TODO add your handling code here:
        loadTablePH_trongNgay();
        txtTuNgayPH.setText("");
        txtDenNgayPH.setText("");
        editorCTPH.setText("");
        btnInPH.setEnabled(false);
    }//GEN-LAST:event_btnReloadPN1ActionPerformed

    private void btnInPHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInPHActionPerformed
        // TODO add your handling code here:
        try {
            editorCTPH.print();
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, "Lỗi in CTPH!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnInPHActionPerformed

    private void txt_TuNgayNXNLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_TuNgayNXNLActionPerformed
        // TODO add your handling code here:
        txt_DenNgayNXNL.requestFocus();
    }//GEN-LAST:event_txt_TuNgayNXNLActionPerformed

    private void txt_DenNgayNXNLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_DenNgayNXNLActionPerformed
        // TODO add your handling code here:
        String tuNgay = txt_TuNgayNXNL.getText().trim();
        String denNgay = txt_DenNgayNXNL.getText().trim();
        if (checkDate(tuNgay) == false) {
            JOptionPane.showMessageDialog(this, "Hãy nhập từ ngày phù hợp (yyyy-MM-dd)!", "Input warning", JOptionPane.WARNING_MESSAGE);
            txt_TuNgayNXNL.requestFocus();
            return;
        }
        if (checkDate(denNgay) == false) {
            JOptionPane.showMessageDialog(this, "Hãy nhập đến ngày phù hợp (yyyy-MM-dd)!", "Input warning", JOptionPane.WARNING_MESSAGE);
            txt_DenNgayNXNL.requestFocus();
            return;
        }
        try {
            Date check_to = format1.parse(denNgay);
            Date check_from = format1.parse(tuNgay);
            if (check_to.before(check_from)) {
                JOptionPane.showMessageDialog(this, "Hãy chọn khoảng thời gian phù hợp!", "Input warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String denngay = denNgay + " 23:59:59";
            loadTableTongNhapNL(tuNgay, denngay);
            loadTableTongXuatNL(tuNgay, denngay);
            loadTableTongHuyNL(tuNgay, denngay);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hãy nhập ngày hợp lệ (yyyy-MM-dd)!", "Input warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_txt_DenNgayNXNLActionPerformed

    private void txt_TuNgayTKTienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_TuNgayTKTienActionPerformed
        // TODO add your handling code here:
        txt_DenNgayTKTien.requestFocus();
    }//GEN-LAST:event_txt_TuNgayTKTienActionPerformed

    private void txt_DenNgayTKTienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_DenNgayTKTienActionPerformed
        // TODO add your handling code here:
        String tuNgay = txt_TuNgayTKTien.getText().trim();
        String denNgay = txt_DenNgayTKTien.getText().trim();
        if (checkDate(tuNgay) == false) {
            JOptionPane.showMessageDialog(this, "Hãy nhập từ ngày phù hợp (yyyy-MM-dd)!", "Input warning", JOptionPane.WARNING_MESSAGE);
            txt_TuNgayTKTien.requestFocus();
            return;
        }
        if (checkDate(denNgay) == false) {
            JOptionPane.showMessageDialog(this, "Hãy nhập đến ngày phù hợp (yyyy-MM-dd)!", "Input warning", JOptionPane.WARNING_MESSAGE);
            txt_DenNgayTKTien.requestFocus();
            return;
        }
        try {
            Date check_to = format1.parse(denNgay);
            Date check_from = format1.parse(tuNgay);
            if (check_to.before(check_from)) {
                JOptionPane.showMessageDialog(this, "Hãy chọn khoảng thời gian phù hợp!", "Input warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int tongTienNhap = 0;
            int tongTienBan = 0;
            String denngay = denNgay + " 23:59:59";
            loadTableTienNhap(tuNgay, denngay);
            for (int i = 0; i < tb_TKTienNhap.getRowCount(); i++) {
                tongTienNhap += Integer.parseInt(tb_TKTienNhap.getValueAt(i, 2).toString());
            }
            lbTongTienNhap.setText(tongTienNhap + " VNĐ");
            loadTableTienXuat(tuNgay, denngay);
            for (int i = 0; i < tb_TKTienBan.getRowCount(); i++) {
                tongTienBan += Integer.parseInt(tb_TKTienBan.getValueAt(i, 2).toString());
            }
            lbTongTienBan.setText(tongTienBan + " VNĐ");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hãy nhập ngày hợp lệ (yyyy-MM-dd)!", "Input warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_txt_DenNgayTKTienActionPerformed

    private int soLuong_DU(String maDU) {
        for (int j = 0; j < tb_CTHD.getRowCount(); j++) {
            if (maDU.equals(tb_CTHD.getValueAt(j, 0))) {
                int soLuong = Integer.parseInt(tb_CTHD.getValueAt(j, 2).toString());
                return soLuong;
            }
        }
        return 0;
    }

    private boolean check_trungMADU(String maDU) {
        boolean check = false;
        for (int j = 0; j < tb_CTHD.getRowCount(); j++) {
            if (maDU.equals(tb_CTHD.getValueAt(j, 0))) {
                return check = true;
            }
        }
        return check;
    }

    private boolean check_trungMANL(String maNL) {
        boolean check = false;
        for (int j = 0; j < tb_CTPX.getRowCount(); j++) {
            if (maNL.equals(tb_CTPX.getValueAt(j, 0))) {
                return check = true;
            }
        }
        return check;
    }

    private ArrayList<DU_ORDER> dsdu = new ArrayList<>();

    private boolean kiemTraNLPhaChe(ArrayList<DU_ORDER> listDU) {
        boolean check = true;
        ArrayList<NGUYENLIEU> dsnl = nl_Data.sp_getListNL();
        String thongBao = "";
        for (DU_ORDER du : listDU) {
            String maDU = du.getMaDU();
            int soLuong = du.getSoLuong();
            ArrayList<CT_DOUONG> ctdu = ctdu_data.getCTDU(maDU);
            for (CT_DOUONG ct : ctdu) {
                String maNL = ct.getMaNL();
                int soLuongCan = ct.getSoLuong() * soLuong;
                for (NGUYENLIEU nl : dsnl) {
                    if (nl.getMaNL().equals(maNL)) {
                        int soLuongCon = nl.getSoLuongTon() - soLuongCan;
                        if (soLuongCon < 0) {
                            thongBao += maNL + ": " + soLuongCon + "\n";
                            check = false;
                        }
                        nl.setSoLuongTon(soLuongCon);
                    }
                }
            }
        }
        if (check == false) {
            JOptionPane.showMessageDialog(this, "Số lượng nguyên liệu không đủ pha chế:\n" + thongBao, "Input warning", JOptionPane.WARNING_MESSAGE);
        }
        return check;
    }

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PnTong;
    private javax.swing.JButton btnCapNhatSL_NL;
    private javax.swing.JButton btnCapNhat_TTCaNhan;
    private javax.swing.JButton btnChonAnh;
    private javax.swing.JButton btnGhiCTDU;
    private javax.swing.JButton btnGhiDU;
    private javax.swing.JButton btnGhiNL;
    private javax.swing.JButton btnGhiPX;
    private javax.swing.JButton btnGhiPhieuNhap;
    private javax.swing.JButton btnGhiTK;
    private javax.swing.JButton btnHuyCTDU;
    private javax.swing.JButton btnHuyDU;
    private javax.swing.JButton btnHuyNL;
    private javax.swing.JButton btnHuyPN;
    private javax.swing.JButton btnHuyPX;
    private javax.swing.JButton btnHuy_TK;
    private javax.swing.JButton btnInHD;
    private javax.swing.JButton btnInPH;
    private javax.swing.JButton btnInPN;
    private javax.swing.JButton btnInPX;
    private javax.swing.JButton btnReloadPN;
    private javax.swing.JButton btnReloadPN1;
    private javax.swing.JButton btnReload_PX;
    private javax.swing.JButton btnSuaDU;
    private javax.swing.JButton btnSuaNL;
    private javax.swing.JButton btnSuaTK;
    private javax.swing.JButton btnThemDU;
    private javax.swing.JButton btnThemHD;
    private javax.swing.JButton btnThemNL;
    private javax.swing.JButton btnThemTK;
    private javax.swing.JButton btnThem_CTDU;
    private javax.swing.JButton btnTimNL;
    private javax.swing.JButton btnTimNL_QLNL;
    private javax.swing.JButton btnXoaDU;
    private javax.swing.JButton btnXoaGioNhap;
    private javax.swing.JButton btnXoaNL;
    private javax.swing.JButton btnXoaTK;
    private javax.swing.JButton btnXoa_CTDU;
    private javax.swing.JButton btn_GhiGioNhap;
    private javax.swing.JPanel btn_OpenNhapNL;
    private javax.swing.JPanel btn_OpenOrDer;
    private javax.swing.JPanel btn_OpenQLDU;
    private javax.swing.JPanel btn_OpenQLNL;
    private javax.swing.JPanel btn_OpenQLTK;
    private javax.swing.JPanel btn_OpenTT;
    private javax.swing.JPanel btn_OpenThongKe;
    private javax.swing.JComboBox<String> cbChucVu_TK;
    private javax.swing.JComboBox<String> cbDanhMuc;
    private javax.swing.JComboBox<String> cbDanhMuc_CT;
    private javax.swing.JComboBox<String> cbDanhMuc_PX;
    private javax.swing.JComboBox<String> cbTTXOA_Tk;
    private javax.swing.JComboBox<String> cbTTXoa_DU;
    private javax.swing.JEditorPane editorCTHD;
    private javax.swing.JEditorPane editorCTPH;
    private javax.swing.JEditorPane editorCTPN;
    private javax.swing.JEditorPane editorCTPX;
    private javax.swing.JLabel icon;
    private javax.swing.JLabel imgDU;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JLabel lbMADU_CT;
    private javax.swing.JLabel lbTongTienBan;
    private javax.swing.JLabel lbTongTienNhap;
    private javax.swing.JLabel lb_ChucVu;
    private javax.swing.JLabel lb_MaNV;
    private javax.swing.JLabel lb_NhapNL;
    private javax.swing.JLabel lb_Order;
    private javax.swing.JLabel lb_QLDU;
    private javax.swing.JLabel lb_QLNL;
    private javax.swing.JLabel lb_QLTK;
    private javax.swing.JLabel lb_ThongKe;
    private javax.swing.JLabel lb_TongTienPX;
    private javax.swing.JPanel pnCTPC;
    private javax.swing.JPanel pnDU_CoCT;
    private javax.swing.JPanel pnDU_DSNL;
    private javax.swing.JPanel pnDU_KoCT;
    private javax.swing.JPanel pnMenu;
    private javax.swing.JPanel pnNguyenLieu;
    private javax.swing.JPanel pnNhapNL;
    private javax.swing.JPanel pnOrder;
    private javax.swing.JPanel pnQLDU;
    private javax.swing.JPanel pnQLNL;
    private javax.swing.JPanel pnQLTK;
    private javax.swing.JPanel pnTOP5DU;
    private javax.swing.JPanel pnTTCaNhan;
    private javax.swing.JPanel pnThongKe;
    private javax.swing.JPanel pnTongNhapNL;
    private javax.swing.JPanel pnTongTien;
    private javax.swing.JPanel pn_CTDU;
    private javax.swing.JPanel pn_NL;
    private javax.swing.JPanel pn_NhapNL;
    private javax.swing.JPanel pn_QLDU;
    private javax.swing.JPanel pn_ThongTin;
    private javax.swing.JPanel pn_XemPH;
    private javax.swing.JPanel pn_XemPN;
    private javax.swing.JTabbedPane tab_NhapNL;
    private javax.swing.JTabbedPane tab_QLDU;
    private javax.swing.JTabbedPane tab_QLNL;
    private javax.swing.JTabbedPane tab_ThongKe;
    private javax.swing.JTabbedPane tab_Xuat;
    private javax.swing.JTable tb_CTDU;
    private javax.swing.JTable tb_CTHD;
    private javax.swing.JTable tb_CTPH;
    private javax.swing.JTable tb_CTPX;
    private javax.swing.JTable tb_DSDU;
    private javax.swing.JTable tb_DSTK;
    private javax.swing.JTable tb_DU_CoCT;
    private javax.swing.JTable tb_DU_KoCT;
    private javax.swing.JTable tb_GioNL;
    private javax.swing.JTable tb_NL;
    private javax.swing.JTable tb_NL_CTDU;
    private javax.swing.JTable tb_PH;
    private javax.swing.JTable tb_PN;
    private javax.swing.JTable tb_PX;
    private javax.swing.JTable tb_QLDU;
    private javax.swing.JTable tb_QLNL;
    private javax.swing.JTable tb_TKTienBan;
    private javax.swing.JTable tb_TKTienNhap;
    private javax.swing.JTable tb_TOP5DU;
    private javax.swing.JTable tb_TongHuyNL;
    private javax.swing.JTable tb_TongNhapNL;
    private javax.swing.JTable tb_TongXuatNL;
    private javax.swing.JTextField txtDVT_QL;
    private javax.swing.JTextField txtDenNgay;
    private javax.swing.JTextField txtDenNgayPH;
    private javax.swing.JTextField txtDenNgayPX;
    private javax.swing.JTextField txtFullName;
    private javax.swing.JTextField txtFullname_TK;
    private javax.swing.JTextField txtGiaBan;
    private javax.swing.JTextField txtGiaNhap;
    private javax.swing.JTextField txtGiaSale;
    private javax.swing.JTextField txtGia_PX;
    private javax.swing.JTextField txtMaDU;
    private javax.swing.JTextField txtMaDU_PX;
    private javax.swing.JTextField txtMaNL;
    private javax.swing.JTextField txtMaNL_CTDU;
    private javax.swing.JTextField txtMaNL_QL;
    private javax.swing.JPasswordField txtPass1;
    private javax.swing.JPasswordField txtPass2;
    private javax.swing.JPasswordField txtPass_TK;
    private javax.swing.JTextField txtSLB_NL;
    private javax.swing.JTextField txtSLNhap;
    private javax.swing.JTextField txtSL_CTDU;
    private javax.swing.JTextField txtSoLuong_PX;
    private javax.swing.JTextField txtTenDU;
    private javax.swing.JTextField txtTenDU_PX;
    private javax.swing.JTextField txtTenNL;
    private javax.swing.JTextField txtTenNL_CTDU;
    private javax.swing.JTextField txtTenNL_QL;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txtTimKiem_QLNL;
    private javax.swing.JTextField txtTuNgay;
    private javax.swing.JTextField txtTuNgayPH;
    private javax.swing.JTextField txtTuNgayPX;
    private javax.swing.JTextField txtUserName;
    private javax.swing.JTextField txtUsername_TK;
    private javax.swing.JTextField txt_DenNgayNXNL;
    private javax.swing.JTextField txt_DenNgayTKTien;
    private javax.swing.JTextField txt_FromTop5;
    private javax.swing.JTextField txt_ToTop5;
    private javax.swing.JTextField txt_TuNgayNXNL;
    private javax.swing.JTextField txt_TuNgayTKTien;
    // End of variables declaration//GEN-END:variables
}
