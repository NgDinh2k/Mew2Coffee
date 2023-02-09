/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.DANHMUC;
import ConDB.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DANHMUC_DATA {

    private ArrayList<DANHMUC> listDM = null;

    public DANHMUC_DATA() {
        docListDM();
    }

    public void docListDM() {
        listDM = sp_getListDM();
    }

    public ArrayList<DANHMUC> sp_getListDM() {
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT * FROM DANHMUC");
            ArrayList<DANHMUC> dsdm = new ArrayList<>();
            while (rs.next()) {
                DANHMUC dm = new DANHMUC();

                dm.setMaDM(rs.getString(1).trim());
                dm.setTenDM(rs.getString(2).trim());

                dsdm.add(dm);
            }
            return dsdm;
        } catch (SQLException e) {
        }
        return null;
    }
    
    public String maDM_to_tenDM(String maDM) {
        for (DANHMUC dm : listDM) {
            if (maDM.equals(dm.getMaDM())) {
                return dm.getTenDM();
            }
        }
        return null;
    }
    
    public String tenDM_to_maDM(String tenDM) {
        for (DANHMUC dm : listDM) {
            if (tenDM.equals(dm.getTenDM())) {
                return dm.getMaDM();
            }
        }
        return null;
    }
}
