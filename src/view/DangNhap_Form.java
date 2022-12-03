/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import javax.swing.JOptionPane;
import service.DangNhap_Serv;
import service.service_Impl.DangNhap_ServImpl;

public class DangNhap_Form extends javax.swing.JFrame {

    String pass = "";
    DangNhap_Serv dangNhap_Serv = new DangNhap_ServImpl();

    public DangNhap_Form() {
        initComponents();
        this.setLocationRelativeTo(null);
 txtEmail.setText("ducNam@gmail.com");
 txtMatKHau.setText("nam2003");
    }

    public void resetLabel() {
        txtLoiTK.setText("");
        txtLoiMK.setText("");
    }

    public boolean checkForm() {
        int demLoi = 0;
        if (txtEmail.getText().trim().length() == 0) {
            txtLoiTK.setText("Vui lòng nhập email...");
            demLoi += 1;
        } else {
            txtLoiTK.setText("");

        }
        if (txtMatKHau.getText().trim().length() == 0.) {
            txtLoiMK.setText("Vui lòng nhập mật khẩu...");
            demLoi += 1;
        } else {
            txtLoiMK.setText("");

        }
        if (demLoi == 0) {
            resetLabel();
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblDangNhap = new javax.swing.JLabel();
        txtLoiTK = new javax.swing.JTextField();
        txtLoiMK = new javax.swing.JTextField();
        txtMatKHau = new javax.swing.JPasswordField();
        lblThoat = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1018, 590));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Login-Form.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1030, -1));

        txtEmail.setBackground(new java.awt.Color(195, 191, 191));
        txtEmail.setBorder(null);
        getContentPane().add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 180, 320, 38));

        lblDangNhap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDangNhapMouseClicked(evt);
            }
        });
        getContentPane().add(lblDangNhap, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 424, 290, 40));

        txtLoiTK.setEditable(false);
        txtLoiTK.setBackground(new java.awt.Color(248, 248, 248));
        txtLoiTK.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtLoiTK.setForeground(new java.awt.Color(255, 51, 51));
        txtLoiTK.setBorder(null);
        getContentPane().add(txtLoiTK, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 230, 320, 20));

        txtLoiMK.setEditable(false);
        txtLoiMK.setBackground(new java.awt.Color(248, 248, 248));
        txtLoiMK.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtLoiMK.setForeground(new java.awt.Color(255, 51, 51));
        txtLoiMK.setBorder(null);
        getContentPane().add(txtLoiMK, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 360, 320, 20));

        txtMatKHau.setBackground(new java.awt.Color(195, 191, 191));
        txtMatKHau.setBorder(null);
        getContentPane().add(txtMatKHau, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 315, 320, 30));

        lblThoat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblThoatMouseClicked(evt);
            }
        });
        getContentPane().add(lblThoat, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 0, 40, 40));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblDangNhapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDangNhapMouseClicked
        if (checkForm()) {
            if (dangNhap_Serv.checkDN(txtEmail.getText(), String.valueOf(txtMatKHau.getPassword()))) {
                JOptionPane.showMessageDialog(this, "Dang nhap thanh cong");
                String tenNv = dangNhap_Serv.getThongTin(txtEmail.getText(), String.valueOf(txtMatKHau.getPassword())).getTenNV();
                Menu m = new Menu();
                m.txtTenNhanVien.setText(tenNv);
                m.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this, "Tài khoản mật khẩu không chính xác");
                resetLabel();
            }
        }


    }//GEN-LAST:event_lblDangNhapMouseClicked

    private void lblThoatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThoatMouseClicked
        System.exit(0);
    }//GEN-LAST:event_lblThoatMouseClicked

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DangNhap_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DangNhap_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DangNhap_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DangNhap_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DangNhap_Form().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblDangNhap;
    private javax.swing.JLabel lblThoat;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtLoiMK;
    private javax.swing.JTextField txtLoiTK;
    private javax.swing.JPasswordField txtMatKHau;
    // End of variables declaration//GEN-END:variables
}
