
package yazlab1proje1;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class SignUp extends javax.swing.JFrame {
int k=0;
    /**
     * Creates new form SignUp
     */
    public SignUp() {
        initComponents();
        setResizable(false);
    }
 public void  createNewAccount() throws SQLException{
     Connection connect = null;
        DbHelper db = new DbHelper();
        PreparedStatement statement = null;
        try {
            connect = db.getConnection();
            String sql = "insert into transport.users(username,password)" + "values(?,?)";
            statement = connect.prepareStatement(sql);
              
            statement.setString(1, usernameSignup.getText());
            statement.setString(2, passwordSignup.getText());
  
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Sign up successful");
        } catch (SQLException exception) {
            db.ShowError(exception);
        } finally {
            statement.close();
      
        }
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        signUp = new javax.swing.JToggleButton();
        passwordSignup = new javax.swing.JPasswordField();
        usernameSignup = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(300, 300));
        setMinimumSize(new java.awt.Dimension(460, 450));
        setSize(new java.awt.Dimension(450, 400));
        getContentPane().setLayout(null);

        signUp.setBackground(new java.awt.Color(13, 82, 167));
        signUp.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        signUp.setForeground(new java.awt.Color(255, 255, 255));
        signUp.setText("Sign Up");
        signUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signUpActionPerformed(evt);
            }
        });
        getContentPane().add(signUp);
        signUp.setBounds(120, 300, 210, 30);

        passwordSignup.setBackground(new java.awt.Color(255, 255, 255));
        passwordSignup.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        passwordSignup.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(passwordSignup);
        passwordSignup.setBounds(130, 230, 190, 40);

        usernameSignup.setBackground(new java.awt.Color(255, 255, 255));
        usernameSignup.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        usernameSignup.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(usernameSignup);
        usernameSignup.setBounds(130, 150, 190, 40);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Let’s begin the adventure.");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(90, 80, 230, 30);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(13, 82, 167), 5), "Create your Account", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 24), new java.awt.Color(255, 255, 255))); // NOI18N
        getContentPane().add(jLabel2);
        jLabel2.setBounds(70, 20, 320, 350);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Password", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16), new java.awt.Color(255, 255, 255))); // NOI18N
        getContentPane().add(jLabel6);
        jLabel6.setBounds(120, 200, 210, 80);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Username", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16), new java.awt.Color(255, 255, 255))); // NOI18N
        getContentPane().add(jLabel5);
        jLabel5.setBounds(120, 120, 210, 80);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Welcome to Cargo Delivery System!");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(90, 60, 290, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/orange.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 0, 450, 460);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 460, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 450, 460);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void signUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signUpActionPerformed
        Login menu=new Login();
       
     
       if( usernameSignup.getText().length()==0 || passwordSignup.getText().length()==0 )
       {
           JOptionPane.showMessageDialog(null, "Please fill the fields ");
       }
      
       else if(usernameSignup.getText().length()!=0 || passwordSignup.getText().length()!=0){
                  try {
                     Connection myConn = (Connection) DriverManager.getConnection("-------------------------------------");
          
                      Statement myStat=(Statement)myConn.createStatement();
                      ResultSet rs=myStat.executeQuery("SELECT * FROM users");
                      while(rs.next())
                      {
                          if(usernameSignup.getText().equals(rs.getString("username")))
                      {
                          JOptionPane.showMessageDialog(null, "Username is already taken");
                          k++;
                      }
                      }
                      
                      if(k==0)
                      {
                        createNewAccount();
                        
                          menu.setVisible(true);
                         this.setVisible(false);
                      }
                            k=0;
                      
                          
            
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
       }
       
    }//GEN-LAST:event_signUpActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(SignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SignUp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField passwordSignup;
    private javax.swing.JToggleButton signUp;
    private javax.swing.JTextField usernameSignup;
    // End of variables declaration//GEN-END:variables
}
