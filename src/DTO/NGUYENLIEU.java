package DTO;


public class NGUYENLIEU {
    private String maNL;
    private String tenNL;
    private String dvt;
    private int soLuongTon;
    
    public NGUYENLIEU(){
        
    }

    public NGUYENLIEU(String maNL, String tenNL, String dvt, int soLuongTon) {
        this.maNL = maNL;
        this.tenNL = tenNL;
        this.dvt = dvt;
        this.soLuongTon = soLuongTon;
    }

    public String getMaNL() {
        return maNL;
    }

    public void setMaNL(String maNL) {
        this.maNL = maNL;
    }

    public String getTenNL() {
        return tenNL;
    }

    public void setTenNL(String tenNL) {
        this.tenNL = tenNL;
    }

    public String getDvt() {
        return dvt;
    }

    public void setDvt(String dvt) {
        this.dvt = dvt;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }
    
}

