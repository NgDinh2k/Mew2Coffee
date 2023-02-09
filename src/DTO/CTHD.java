package DTO;


public class CTHD {
    private String maPX;
    private String maDU;
    private int soLuong;
    private int donGia;
    
    public CTHD(){
        
    }

    public CTHD(String maPX, String maDU, int soLuong, int donGia) {
        this.maPX = maPX;
        this.maDU = maDU;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public String getMaPX() {
        return maPX;
    }

    public void setMaPX(String maPX) {
        this.maPX = maPX;
    }

    public String getMaDU() {
        return maDU;
    }

    public void setMaDU(String maDU) {
        this.maDU = maDU;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getDonGia() {
        return donGia;
    }

    public void setDonGia(int donGia) {
        this.donGia = donGia;
    }
    
    
}
