package DTO;


public class CTPX {
    private String maPX;
    private String maNL;
    private int soLuong;
    
    public CTPX(){
        
    }    

    public CTPX(String maPX, String maNL, int soLuong) {
        this.maPX = maPX;
        this.maNL = maNL;
        this.soLuong = soLuong;
    }

    public String getMaPX() {
        return maPX;
    }

    public void setMaPX(String maPX) {
        this.maPX = maPX;
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
