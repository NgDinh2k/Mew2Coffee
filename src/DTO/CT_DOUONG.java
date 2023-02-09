package DTO;


public class CT_DOUONG {
    private String maDU;
    private String maNL;
    private int soLuong;
    
    public CT_DOUONG(){
        
    }

    public CT_DOUONG(String maDU, String maNL, int soLuong) {
        this.maDU = maDU;
        this.maNL = maNL;
        this.soLuong = soLuong;
    }

    public String getMaDU() {
        return maDU;
    }

    public void setMaDU(String maDU) {
        this.maDU = maDU;
    }

    public String getMaNL() {
        return maNL;
    }

    public void setMaNL(String maNL) {
        this.maNL = maNL;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
    
    
}
