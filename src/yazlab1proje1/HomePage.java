/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yazlab1proje1;



import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import javax.swing.table.DefaultTableModel;


public class HomePage extends javax.swing.JFrame {

    
   
 
    String customer_Name;
    String coordinateX,coordinateY;
    String status;
    public HomePage()  {
        initComponents();
        setResizable(false);
        cargoTable();
        checkNewLocation();
       
      
       
    }
    private void checkNewLocation() {
		new Thread(() -> {
        	Connection con = null;
			try {
				con = DbHelper.getConnection();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
        	do {
        		try {
        			Thread.sleep(1000l);
        		} catch (InterruptedException ex) {
        			Logger.getLogger(Browser.class.getName()).log(Level.SEVERE, null, ex);
        		}

        		try {
        			Statement statement = (Statement) con.createStatement();
        			ResultSet rs = statement.executeQuery("SELECT b.status,b.name,a.latitude,a.longitude,a.id FROM transport.locations a,transport.customers b where b.location_id = a.id  order by a.id" );
        			DefaultTableModel tblModel1 = (DefaultTableModel) cargoTable.getModel();
        			while(tblModel1.getRowCount() > 0) {
        				tblModel1.removeRow(0);
        			}
        			while(rs.next()) {
        				String tbData1[] = {rs.getString("name"),rs.getString("latitude"),rs.getString("longitude"),rs.getString("status")};
        				tblModel1.addRow(tbData1);
        			}
        			rs.close();
        			statement.close();
        		} catch (SQLException e) {
        			e.printStackTrace();
        		}
        	}while(true);
        	
        }).start();
	}
    
 public void cargoTable() {
        try {
 DefaultTableModel dm = (DefaultTableModel)cargoTable.getModel();
dm.getDataVector().removeAllElements();
cargoTable.repaint(); 
      Connection myConn=(Connection) DriverManager.getConnection("jdbc:mysql://130.211.228.7:3306/transport","root","1234");
             Statement myStat = (Statement) myConn.createStatement();
             ResultSet rs = myStat.executeQuery("SELECT name,location_id,status,latitude,longitude,locations.id  FROM customers,locations WHERE customers.location_id=locations.id " );
          
            while (rs.next()) {
              customer_Name= String.valueOf(rs.getString("name"));
            
              coordinateX=String.valueOf(rs.getString("latitude"));
               coordinateY=String.valueOf(rs.getString("longitude"));
               status=String.valueOf(rs.getString("status"));
               String tbData[] = {customer_Name,coordinateX,coordinateY,status};//tablo içinn dizi oluşturdu
                DefaultTableModel tblModel = (DefaultTableModel) cargoTable.getModel();//tablomuz
   tblModel.addRow(tbData);//tabloya oluşturduğu diziyi verdi
              
              }  
           

        } catch (SQLException ex) {
              Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
      
        }
    }
int  locationId;
String x,y;
    public void addCustomer() throws SQLException {
        Connection connect = null;
        DbHelper db = new DbHelper();
        PreparedStatement statement = null;
        try {
            connect = db.getConnection();
            String sql = "insert into transport.locations(latitude,longitude)" + "values(?,?)";
            statement = connect.prepareStatement(sql);
            statement.setString(1,AddressX.getText());
            x=AddressX.getText();
            y=AddressY.getText();
            statement.setString(2,AddressY.getText());
             statement.executeUpdate();
              Connection myConn=(Connection) DriverManager.getConnection("jdbc:mysql://130.211.228.7:3306/transport","root","1234");
             Statement myStat = (Statement) myConn.createStatement();
             ResultSet rs = myStat.executeQuery("SELECT id  FROM locations WHERE latitude="+"\""+x+"\""+"&& longitude="+"\""+y+"\"");
            
            while (rs.next()) {
            locationId = rs.getInt("id");
              } 
           
              String sql2 = "insert into transport.customers(name,location_id,status)" + "values(?,?,?)";
            statement = connect.prepareStatement(sql2);
                statement.setString(1,customerName.getText());
                  statement.setInt(2,locationId);
                   statement.setInt(3,1);
              statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Add successful");
        } catch (SQLException exception) {
            db.ShowError(exception);
        } finally {
            statement.close();
            connect.close();
        }
    }
 int customerId;
      public void deleteCustomer() throws SQLException
   {
       Connection connect = null;
        DbHelper db = new DbHelper();
        PreparedStatement statement = null;
        try {
             Connection myConn=(Connection) DriverManager.getConnection("jdbc:mysql://130.211.228.7:3306/transport","root","1234");
             Statement myStat = (Statement) myConn.createStatement();
             ResultSet rs = myStat.executeQuery("SELECT id,name  FROM customers WHERE name="+"\""+customerName.getText()+"\"");
         
            while (rs.next()) {
                if(rs.getString("name").equals("NULL")){
                customerId=0;
                    }
                else{
                      customerId = rs.getInt("id");}
               
              } 
            if(customerId!=0){
            connect = db.getConnection();
            String sql = "DELETE from customers WHERE id=?";
            statement = connect.prepareStatement(sql);
            statement.setInt(1,customerId);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "customer delete is successful");
        } 
        else
            {   JOptionPane.showMessageDialog(null, "User not found!");
            }
        }catch (SQLException exception) {
            db.ShowError(exception);
        } finally {
            statement.close();
            connect.close();
        }
           
   }
       public void statusUpdate() throws SQLException {
        Connection connect = null;
        DbHelper db = new DbHelper();
        PreparedStatement statement = null;
        try {
            connect = db.getConnection();
            String sql = "UPDATE  customers SET Status=? WHERE name=?";
            statement = connect.prepareStatement(sql);
             statement.setString(1,statusUpdate.getText());
             statement.setString(2, customernameUpdate.getText());
           
           
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, " Status updaate is succesful");
        } catch (SQLException exception) {
            db.ShowError(exception);
        } finally {
            statement.close();
            connect.close();
        }
    }
       int userId,locId,userlocId=0;
       String locX,locY;
       public void userLocationUpdate() throws SQLException {
        Connection connect = null;
        DbHelper db = new DbHelper();
        PreparedStatement statement = null;
        Connection myConn=(Connection) DriverManager.getConnection("jdbc:mysql://130.211.228.7:3306/transport","root","1234");
             Statement myStat = (Statement) myConn.createStatement();
             locX=locationX.getText();
             locY=locationY.getText();
             ResultSet rs = myStat.executeQuery("SELECT  users.id,locations.id ,users.username FROM users,locations WHERE  username="+"\""+userName.getText()+"\""+"&& latitude="+"\""+locX+"\""+"&& longitude="+"\""+locY+"\"" );
      
            while (rs.next()) {
                
                if(rs.getString("username").equals("NULL")){
                   userId=0;
                  }
                else{
                     userId = rs.getInt("users.id");
                     locId = rs.getInt("locations.id");}
               
              } 
                Statement myStat2 = (Statement) myConn.createStatement();
             ResultSet rs2 = myStat2.executeQuery("SELECT  user_locations.id FROM user_locations,users WHERE  user_locations.user_id=users.id &&  users.id="+userId );
         
             while (rs2.next()) {
                 if(rs2.getInt("user_locations.id")==0){
                     userlocId=0;
                 }else{
                userlocId=rs2.getInt("user_locations.id");
                 }
              } 
            if(userId!=0 && userlocId!=0){
        try {
            connect = db.getConnection();
            String sql = "UPDATE  user_locations SET location_id=? WHERE user_id=?";
            statement = connect.prepareStatement(sql);
             statement.setInt(1,locId);
             statement.setInt(2, userId);
           
           
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, " Status update is succesful");
        } catch (SQLException exception) {
            db.ShowError(exception);
        } finally {
            statement.close();
            connect.close();
        }} else if(userId!=0 && userlocId==0)
        {
             connect = db.getConnection();
            String sql = "insert into transport.user_locations(user_id,location_id)" + "values(?,?)";
            statement = connect.prepareStatement(sql);
            statement.setInt(1,userId);
            
            statement.setInt(2,locId);
             statement.executeUpdate();
                 JOptionPane.showMessageDialog(null, "Kayıt başarılı");
        }
        
        else if(userId==0){
                      JOptionPane.showMessageDialog(null, "User not found!");
            }
            userlocId=0;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cargoScrollPane = new javax.swing.JScrollPane();
        cargoTable = new javax.swing.JTable();
        shortestRoute = new javax.swing.JLabel();
        signOut = new javax.swing.JToggleButton();
        AddressX = new javax.swing.JTextField();
        customerName = new javax.swing.JTextField();
        cargoAdd = new javax.swing.JToggleButton();
        userLocationSave = new javax.swing.JToggleButton();
        userName = new javax.swing.JTextField();
        AddressY = new javax.swing.JTextField();
        locationX = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cargoDelete = new javax.swing.JToggleButton();
        locationY = new javax.swing.JTextField();
        customernameUpdate = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        cargoUpdate = new javax.swing.JToggleButton();
        statusUpdate = new javax.swing.JTextField();
        cargoScrollPane1 = new javax.swing.JScrollPane();
        cargoTable1 = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(900, 900));
        getContentPane().setLayout(null);

        cargoScrollPane.setBackground(new java.awt.Color(13, 82, 167));
        cargoScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(42, 107, 149), 5), "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 36), new java.awt.Color(204, 204, 204))); // NOI18N
        cargoScrollPane.setForeground(new java.awt.Color(13, 82, 167));
        cargoScrollPane.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N

        cargoTable.setBackground(new java.awt.Color(13, 82, 167));
        cargoTable.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 24))); // NOI18N
        cargoTable.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cargoTable.setForeground(new java.awt.Color(204, 204, 204));
        cargoTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Customer Name", "Coordinate X", "Coordinate Y", "Status"
            }
        ));
        cargoTable.setDoubleBuffered(true);
        cargoTable.setRowHeight(30);
        cargoTable.setSelectionBackground(new java.awt.Color(42, 107, 149));
        cargoTable.setSelectionForeground(new java.awt.Color(204, 204, 204));
        cargoTable.setShowGrid(true);
        cargoTable.setShowVerticalLines(false);
        cargoScrollPane.setViewportView(cargoTable);
        if (cargoTable.getColumnModel().getColumnCount() > 0) {
            cargoTable.getColumnModel().getColumn(0).setHeaderValue("Customer Name");
            cargoTable.getColumnModel().getColumn(3).setMaxWidth(300);
            cargoTable.getColumnModel().getColumn(3).setHeaderValue("Status");
        }

        getContentPane().add(cargoScrollPane);
        cargoScrollPane.setBounds(300, 250, 580, 300);

        shortestRoute.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        shortestRoute.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(shortestRoute);
        shortestRoute.setBounds(300, 770, 560, 40);

        signOut.setBackground(new java.awt.Color(13, 82, 167));
        signOut.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        signOut.setForeground(new java.awt.Color(255, 255, 255));
        signOut.setText("SIGN OUT");
        signOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signOutActionPerformed(evt);
            }
        });
        getContentPane().add(signOut);
        signOut.setBounds(450, 800, 270, 30);

        AddressX.setBackground(new java.awt.Color(255, 255, 255));
        AddressX.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        AddressX.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(AddressX);
        AddressX.setBounds(30, 430, 220, 40);

        customerName.setBackground(new java.awt.Color(255, 255, 255));
        customerName.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        customerName.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(customerName);
        customerName.setBounds(30, 350, 220, 40);

        cargoAdd.setBackground(new java.awt.Color(255, 51, 0));
        cargoAdd.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cargoAdd.setForeground(new java.awt.Color(255, 255, 255));
        cargoAdd.setText("ADD");
        cargoAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cargoAddActionPerformed(evt);
            }
        });
        getContentPane().add(cargoAdd);
        cargoAdd.setBounds(20, 570, 240, 30);

        userLocationSave.setBackground(new java.awt.Color(13, 82, 167));
        userLocationSave.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        userLocationSave.setForeground(new java.awt.Color(255, 255, 255));
        userLocationSave.setText("SAVE");
        userLocationSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userLocationSaveActionPerformed(evt);
            }
        });
        getContentPane().add(userLocationSave);
        userLocationSave.setBounds(760, 170, 120, 70);

        userName.setBackground(new java.awt.Color(255, 255, 255));
        userName.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        userName.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(userName);
        userName.setBounds(20, 190, 200, 40);

        AddressY.setBackground(new java.awt.Color(255, 255, 255));
        AddressY.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        AddressY.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(AddressY);
        AddressY.setBounds(30, 510, 220, 40);

        locationX.setBackground(new java.awt.Color(255, 255, 255));
        locationX.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        locationX.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(locationX);
        locationX.setBounds(270, 190, 210, 40);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        jLabel16.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Address Coordinate Y", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        getContentPane().add(jLabel16);
        jLabel16.setBounds(20, 480, 240, 80);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        jLabel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Address Coordinate X", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        getContentPane().add(jLabel11);
        jLabel11.setBounds(20, 400, 240, 80);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Customer Name", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 17), new java.awt.Color(255, 255, 255))); // NOI18N
        getContentPane().add(jLabel10);
        jLabel10.setBounds(20, 320, 240, 80);

        jLabel2.setBackground(new java.awt.Color(255, 51, 0));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("CARGO ADDRESS INFORMATION");
        jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 0), 5));
        getContentPane().add(jLabel2);
        jLabel2.setBounds(0, 260, 280, 50);

        cargoDelete.setBackground(new java.awt.Color(255, 51, 0));
        cargoDelete.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cargoDelete.setForeground(new java.awt.Color(255, 255, 255));
        cargoDelete.setText("DELETE");
        cargoDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cargoDeleteActionPerformed(evt);
            }
        });
        getContentPane().add(cargoDelete);
        cargoDelete.setBounds(20, 610, 240, 30);

        locationY.setBackground(new java.awt.Color(255, 255, 255));
        locationY.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        locationY.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(locationY);
        locationY.setBounds(530, 190, 210, 40);

        customernameUpdate.setBackground(new java.awt.Color(255, 255, 255));
        customernameUpdate.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        customernameUpdate.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(customernameUpdate);
        customernameUpdate.setBounds(30, 670, 220, 40);

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel18.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Customer Name", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 17), new java.awt.Color(255, 255, 255))); // NOI18N
        getContentPane().add(jLabel18);
        jLabel18.setBounds(20, 640, 240, 80);

        cargoUpdate.setBackground(new java.awt.Color(255, 51, 0));
        cargoUpdate.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cargoUpdate.setForeground(new java.awt.Color(255, 255, 255));
        cargoUpdate.setText("UPDATE");
        cargoUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cargoUpdateActionPerformed(evt);
            }
        });
        getContentPane().add(cargoUpdate);
        cargoUpdate.setBounds(20, 800, 240, 30);

        statusUpdate.setBackground(new java.awt.Color(255, 255, 255));
        statusUpdate.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        statusUpdate.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(statusUpdate);
        statusUpdate.setBounds(30, 750, 220, 40);

        cargoScrollPane1.setBackground(new java.awt.Color(13, 82, 167));
        cargoScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(42, 107, 149), 5), "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 36), new java.awt.Color(204, 204, 204))); // NOI18N
        cargoScrollPane1.setForeground(new java.awt.Color(13, 82, 167));
        cargoScrollPane1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N

        cargoTable1.setBackground(new java.awt.Color(13, 82, 167));
        cargoTable1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 24))); // NOI18N
        cargoTable1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cargoTable1.setForeground(new java.awt.Color(204, 204, 204));
        cargoTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Coordinate X", "Coordinate Y"
            }
        ));
        cargoTable1.setDoubleBuffered(true);
        cargoTable1.setRowHeight(30);
        cargoTable1.setSelectionBackground(new java.awt.Color(42, 107, 149));
        cargoTable1.setSelectionForeground(new java.awt.Color(204, 204, 204));
        cargoTable1.setShowGrid(true);
        cargoTable1.setShowVerticalLines(false);
        cargoScrollPane1.setViewportView(cargoTable1);

        getContentPane().add(cargoScrollPane1);
        cargoScrollPane1.setBounds(300, 610, 580, 160);

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        jLabel19.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Status", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        getContentPane().add(jLabel19);
        jLabel19.setBounds(20, 720, 240, 80);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        jLabel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Location Y", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        getContentPane().add(jLabel17);
        jLabel17.setBounds(520, 160, 230, 80);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logoMini.png"))); // NOI18N
        jLabel9.setText("jLabel9");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(40, 0, 150, 150);

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CARGO DELIVERY SYSTEM");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(190, 20, 680, 50);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        jLabel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Username", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        getContentPane().add(jLabel3);
        jLabel3.setBounds(10, 160, 230, 80);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        jLabel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Location X", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        getContentPane().add(jLabel13);
        jLabel13.setBounds(260, 160, 230, 80);

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/blueBig.png"))); // NOI18N
        jLabel14.setText("jLabel4");
        jLabel14.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "YOUR SHORTEST ROUTE", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 28), new java.awt.Color(255, 255, 255))); // NOI18N
        jLabel14.setMaximumSize(new java.awt.Dimension(450, 900));
        jLabel14.setMinimumSize(new java.awt.Dimension(450, 900));
        getContentPane().add(jLabel14);
        jLabel14.setBounds(300, 570, 580, 190);

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/blue.png"))); // NOI18N
        jLabel12.setText("jLabel4");
        jLabel12.setMaximumSize(new java.awt.Dimension(450, 900));
        jLabel12.setMinimumSize(new java.awt.Dimension(450, 900));
        getContentPane().add(jLabel12);
        jLabel12.setBounds(0, 250, 280, 660);

        jLabel5.setBackground(new java.awt.Color(13, 82, 167));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/blueBig.png"))); // NOI18N
        jLabel5.setText("jLabel4");
        jLabel5.setMaximumSize(new java.awt.Dimension(450, 900));
        jLabel5.setMinimumSize(new java.awt.Dimension(450, 900));
        getContentPane().add(jLabel5);
        jLabel5.setBounds(0, 0, 900, 150);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/orangeBig.png"))); // NOI18N
        jLabel4.setText("jLabel4");
        jLabel4.setMaximumSize(new java.awt.Dimension(450, 900));
        jLabel4.setMinimumSize(new java.awt.Dimension(450, 900));
        getContentPane().add(jLabel4);
        jLabel4.setBounds(0, 0, 910, 910);

        jPanel1.setForeground(new java.awt.Color(255, 0, 0));
        jPanel1.setLayout(null);
        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 900, 900);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void signOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signOutActionPerformed
       this.setVisible(false);
       Login login=new Login();
       login.setVisible(true);
    }//GEN-LAST:event_signOutActionPerformed

    private void cargoAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cargoAddActionPerformed
         
        
        if (customerName.getText().length() == 0 || AddressX.getText().length() == 0 || AddressY.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "Please fill the fields");
        } else {

            try {
                Connection myConn = (Connection) DriverManager.getConnection("jdbc:mysql://130.211.228.7:3306/transport", "root", "1234");
                Statement myStat = (Statement) myConn.createStatement();
             ResultSet rs = myStat.executeQuery("SELECT name,location_id,status,latitude,longitude,locations.id  FROM customers,locations WHERE customers.location_id=locations.id " );
       

                while (rs.next()) {
                        customer_Name = customerName.getText();
                       coordinateX = AddressX.getText();
                        coordinateY =AddressY.getText();
                      status="1";
                       
     
                }
              
                    try {
                     addCustomer();
                    } catch (SQLException ex) {
                        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    String tbData1[] = {customer_Name,coordinateX,coordinateY,status};
                    DefaultTableModel tblModel1 = (DefaultTableModel) cargoTable.getModel();
                    tblModel1.addRow(tbData1);
               
              
        
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_cargoAddActionPerformed
     int silkontrol1=0;
     int silkontrol=0;
    
    private void cargoDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cargoDeleteActionPerformed
            
        if (customerName.getText().length() == 0 || AddressX.getText().length() == 0 || AddressY.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "Please fill the fields");
        }else{
   
                int silkontrol2=0;
           try {

                Connection myConn = (Connection) DriverManager.getConnection("jdbc:mysql://130.211.228.7:3306/transport", "root", "1234");
                Statement myStat = (Statement) myConn.createStatement();
                ResultSet rs = myStat.executeQuery("SELECT * FROM customers");
                
                while (rs.next()) {
                    if (customerName.getText().equals(rs.getString("name"))) {
                        silkontrol1=1;
                        silkontrol2=1;
                        
                    }
                    else {
                        if(silkontrol1==0)
                        {
                           silkontrol++; 
                        }
                    }
                       if (silkontrol1==1)
                   {
                       String tbData[] = {customer_Name,coordinateX,coordinateY,status};
                DefaultTableModel tblModel = (DefaultTableModel) cargoTable.getModel();
                    tblModel.removeRow(silkontrol);
                    silkontrol1=0;
                   }
                }   
                    try {
                      
                        if(silkontrol2==1)
                        {
                            System.out.println(silkontrol1);
                          deleteCustomer();
                        }
                        silkontrol1=0;
                         silkontrol=0;
                         silkontrol2=0;
           
        } catch (SQLException ex) {
            Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        catch (SQLException ex) {
                java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } 
        silkontrol2=0;
        }
    }//GEN-LAST:event_cargoDeleteActionPerformed

    private void cargoUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cargoUpdateActionPerformed
                    silkontrol1=0;
                    silkontrol=0;
                    if (customernameUpdate.getText().length() == 0 || statusUpdate.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "Please fill the fields");
        }else{
        try {

                Connection myConn = (Connection) DriverManager.getConnection("jdbc:mysql://130.211.228.7:3306/transport", "root", "1234");
                Statement myStat = (Statement) myConn.createStatement();
               ResultSet rs = myStat.executeQuery("SELECT * FROM customers");
                while (rs.next()) {
                    if (customernameUpdate.getText().equals(rs.getString("name"))) {
                        silkontrol1=1;
                        
                    }
                    else {
                        if(silkontrol1==0)
                        {
                           silkontrol++; 
                        }
                       
                    }
                        status=statusUpdate.getText();
                }
                
                if(silkontrol1==1)
                {
                    String tbData1[] = {customer_Name,coordinateX,coordinateY,status};
                    DefaultTableModel tblModel = (DefaultTableModel) cargoTable.getModel();
                    tblModel.removeRow(silkontrol);  
                    statusUpdate();
                    customer_Name=customernameUpdate.getText();
                }
                
                    
                    
                if(silkontrol1==1)
                {
                    
                    String tbData1[] = {customer_Name,coordinateX,coordinateY,status};
                    DefaultTableModel tblModel = (DefaultTableModel) cargoTable.getModel();
                    tblModel.addRow(tbData1);
                }
        }
        catch (SQLException ex) {
                java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
      
                    silkontrol1=0;
                    silkontrol=0;}
    }//GEN-LAST:event_cargoUpdateActionPerformed

    private void userLocationSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userLocationSaveActionPerformed
       if (userName.getText().length() == 0 || locationX.getText().length() == 0 || locationY.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "Please fill the fields");
        }
       else{
        try {
            userLocationUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
        }}
    }//GEN-LAST:event_userLocationSaveActionPerformed

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
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               
                  
           
       Distance distance=new Distance();
                try {
                    distance.shortest();
                } catch (SQLException ex) {
                    Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AddressX;
    private javax.swing.JTextField AddressY;
    private javax.swing.JToggleButton cargoAdd;
    private javax.swing.JToggleButton cargoDelete;
    private javax.swing.JScrollPane cargoScrollPane;
    private javax.swing.JScrollPane cargoScrollPane1;
    public static javax.swing.JTable cargoTable;
    public static javax.swing.JTable cargoTable1;
    private javax.swing.JToggleButton cargoUpdate;
    private javax.swing.JTextField customerName;
    private javax.swing.JTextField customernameUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField locationX;
    private javax.swing.JTextField locationY;
    public static javax.swing.JLabel shortestRoute;
    private javax.swing.JToggleButton signOut;
    private javax.swing.JTextField statusUpdate;
    private javax.swing.JToggleButton userLocationSave;
    private javax.swing.JTextField userName;
    // End of variables declaration//GEN-END:variables
}
