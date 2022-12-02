package viewModel;

public class ThongKe_ViewModel {

    private String maSp, tenSp, mau, size, chatLieu;
    private int slTon, slBan;
    private double donGia, doanhThu, tienmat, tienkhac, tiengiamgia,gianhap;
    private String ngaytao,namBH;

    public ThongKe_ViewModel() {
    }

    public ThongKe_ViewModel(String maSp, String tenSp, String mau, String size, String chatLieu, int slTon, int slBan, double donGia, double doanhThu, double tienmat, double tienkhac, double tiengiamgia, double gianhap, String ngaytao, String namBH) {
        this.maSp = maSp;
        this.tenSp = tenSp;
        this.mau = mau;
        this.size = size;
        this.chatLieu = chatLieu;
        this.slTon = slTon;
        this.slBan = slBan;
        this.donGia = donGia;
        this.doanhThu = doanhThu;
        this.tienmat = tienmat;
        this.tienkhac = tienkhac;
        this.tiengiamgia = tiengiamgia;
        this.gianhap = gianhap;
        this.ngaytao = ngaytao;
        this.namBH = namBH;
    }

    public String getNamBH() {
        return namBH;
    }

    public void setNamBH(String namBH) {
        this.namBH = namBH;
    }

   

    public double getGianhap() {
        return gianhap;
    }

    public void setGianhap(double gianhap) {
        this.gianhap = gianhap;
    }

    

    

    public double getTienmat() {
        return tienmat;
    }

    public void setTienmat(double tienmat) {
        this.tienmat = tienmat;
    }

    public double getTienkhac() {
        return tienkhac;
    }

    public void setTienkhac(double tienkhac) {
        this.tienkhac = tienkhac;
    }

    public double getTiengiamgia() {
        return tiengiamgia;
    }

    public void setTiengiamgia(double tiengiamgia) {
        this.tiengiamgia = tiengiamgia;
    }

    public String getMaSp() {
        return maSp;
    }

    public void setMaSp(String maSp) {
        this.maSp = maSp;
    }

    public String getTenSp() {
        return tenSp;
    }

    public void setTenSp(String tenSp) {
        this.tenSp = tenSp;
    }

    public String getMau() {
        return mau;
    }

    public void setMau(String mau) {
        this.mau = mau;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getChatLieu() {
        return chatLieu;
    }

    public void setChatLieu(String chatLieu) {
        this.chatLieu = chatLieu;
    }

    public int getSlTon() {
        return slTon;
    }

    public void setSlTon(int slTon) {
        this.slTon = slTon;
    }

    public int getSlBan() {
        return slBan;
    }

    public void setSlBan(int slBan) {
        this.slBan = slBan;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public double getDoanhThu() {
        return doanhThu;
    }

    public void setDoanhThu(double doanhThu) {
        this.doanhThu = doanhThu;
    }

    public String getNgaytao() {
        return ngaytao;
    }

    public void setNgaytao(String ngaytao) {
        this.ngaytao = ngaytao;
    }

}
