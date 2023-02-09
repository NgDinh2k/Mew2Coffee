/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

/**
 *
 * @author My-PC
 */
public class DSTAIKHOAN {
    private String userName;
    private String fullName;
    private String passWord;
    private String chucVu;
    private int ttXoa;
    
    public DSTAIKHOAN(){
        
    }

    public DSTAIKHOAN(String userName, String fullName, String passWord, String chucVu, int ttXoa) {
        this.userName = userName;
        this.fullName = fullName;
        this.passWord = passWord;
        this.chucVu = chucVu;
        this.ttXoa = ttXoa;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public int getTtXoa() {
        return ttXoa;
    }

    public void setTtXoa(int ttXoa) {
        this.ttXoa = ttXoa;
    }
    
    
}
