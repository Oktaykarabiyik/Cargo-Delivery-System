/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yazlab1proje1;

import com.mysql.jdbc.Connection;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
 
 
public class Kargo_dagitim1 extends Application {
    
    private Scene scene;
    @Override public void start(Stage stage) {
        // create the scene
        stage.setTitle("Kargo Dagitim");
        scene = new Scene(new Browser(),750,500, Color.web("#666970"));
        stage.setScene(scene);
        //scene.getStylesheets().add("webviewsample/BrowserToolbar.css");        
        stage.show();
    }
 
    public static void main(String[] args){
        launch(args);
    }
}
class Browser extends Region {
 
    private final String locationPoints = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    private final Map<String, Coordinate> locations = new ConcurrentHashMap<>();
    int varmikontrol=0;
    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
     List<String>db=new ArrayList<>();
    public Browser() {
        //apply the styles
        //getStyleClass().add("browser");
        
        //Acilista dbdeki yerleri cekme fonksiyonu
         getInsertedLocations();
        
        // process page loading
        webEngine.getLoadWorker().stateProperty().addListener((ObservableValue<? extends State> ov, State oldState, State newState) -> {
            //toolBar.getChildren().remove(showPrevDoc);
            if (newState == State.SUCCEEDED) {//Sayfa load olunca buraya  girer
              
                //İlk acildiginda dbdeki verileri ekrana yazdiran thread
              
             
                new Thread(() -> {
                    try {
                        Thread.sleep(1000l);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Browser.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    Platform.runLater(() -> {
                        locations.forEach((k, v) -> {
                            webEngine.executeScript("addMarkerFromDB('"+v.getLat()+"','"+v.getLng()+"','"+v.getCustomerName()+"')");
                            v.setShowed(true);
                        });
                    });
                    
                    //Yeni eklemeleri kontrol eden fonksiyonu cagrimi
                    try {
                        Connection con = DbHelper.getConnection();
                        newLocationControlFromDB(con);
                    } catch (SQLException ex) {
                        Logger.getLogger(Browser.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    
                }).start();
                
                //Ekran üzerinde nokta isaretlenince ve tamama basınca ekrandaki bilgileri db ye insert eden thread
                new Thread(()-> {
                do {
                Platform.runLater(
                    () -> {
                            try {
                                //Ekranda tamam butonu tıklanmışmı
                                String isOkButtonClicked = (String)webEngine.executeScript("document.getElementById('transportOkHidden').value");
                                if ("1".equals(isOkButtonClicked)) {//Tamam tıklandıysa

                                    char[] locPoints = locationPoints.toCharArray();
                                    for (int i = 0; i< locPoints.length;i++) {//Ekrandaki degerler loop ile alınır
                                        Object obj = webEngine.executeScript("document.getElementById('lat"+locPoints[i]+"')");
                                        if (obj != null) {
                                            obj = webEngine.executeScript("document.getElementById('lat"+locPoints[i]+"').value");
                                            if (obj == null) continue;
                                            String x = (String)obj;
                                            obj = webEngine.executeScript("document.getElementById('lng"+locPoints[i]+"').value");
                                            String y = (String)obj;

                                            obj = webEngine.executeScript("document.getElementById('customer"+locPoints[i]+"').value");
                                            String customerName = (String)obj;

                                            if (customerName == null || customerName.equals("")) {
                                                continue;
                                            }

                                            if(!locations.containsKey(String.valueOf(locPoints[i]))) {//Mevcut degil ise mape at
                                                System.out.println("customerName : "+customerName);
                                                System.out.println("Eklendi : "+String.valueOf(locPoints[i])+" lat - lng : " + x + " - " + y);
                                                locations.put(String.valueOf(locPoints[i]), new Coordinate(customerName,x, y));
                                            }
                                        } else {
                                            if(locations.containsKey(String.valueOf(locPoints[i]))) {
                                                locations.remove(String.valueOf(locPoints[i]));
                                            }
                                        }
                                    }
                                    //Mapdekileri db ye insert eden loop
                                    locations.forEach((k, v) -> insertDB(v));
                                    
                              }
                          } catch (Exception e) {
                              e.printStackTrace();
                          }
                    }
                  );
                    try {
                        Thread.sleep(1000l);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Browser.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }while(true);
                }).start();
            }
        });
               
        //Ekranda musteri adi girilmemis icin hata mesaji gosterilmesi
        webEngine.setOnAlert(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Hata");
            alert.setContentText(event.getData());
            alert.showAndWait();
        });
        
        File f = new File("C:\\Users\\Oktay\\Desktop\\maphesaplama.html");
        
        // load the web page
        webEngine.load(f.toURI().toString());
        //add the web view to the scene
        getChildren().add(browser);
       ListTheUsers();  
        ListTheCustomer(); 	                                  
       ClearingTheList();
      CompareAndDelete();
        
        
    }
    
    //Diger ekrandan ekleme yapilip yapilmadigini 1 sn araliklarla kontrol eden Thread
    public void newLocationControlFromDB(Connection con) {
        new Thread(() -> {
            do {
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Browser.class.getName()).log(Level.SEVERE, null, ex);
                }

                checkNewLocation(con);

                Platform.runLater(() -> {
                    locations.forEach((k, coordinate) -> {
                        if(!coordinate.isShowed()) {//Ekranda gosteriliyor mu kontrolü
                            webEngine.executeScript("addMarkerFromDB('"+coordinate.getLat()+"','"+coordinate.getLng()+"','"+coordinate.getCustomerName()+"')");
                            coordinate.setShowed(true);
                        }             
                    });
                });
            }while(true);
        }).start();
    }
    private void ListTheCustomer(){
        Connection con = null;
			try {
				con = DbHelper.getConnection();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

        		try {
        			Statement statement = (Statement) con.createStatement();
        			ResultSet rs = statement.executeQuery("SELECT name FROM customers" );	
                                 while(rs.next()) {
                                   
                        	db.add(rs.getString("name"));
        			}
                                rs.close();
        			statement.close();
        		} catch (SQLException e) {
        			e.printStackTrace();
        		}
     	
    }
     private void ListTheUsers(){
        Connection con = null;
			try {
				con = DbHelper.getConnection();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

        		try {
        			Statement statement = (Statement) con.createStatement();		
                                ResultSet rs = statement.executeQuery("SELECT username FROM users" );
        			while(rs.next())
                                {
                                    db.add(rs.getString("username"));
                                }
        			rs.close();             
        			statement.close();
        		} catch (SQLException e) {
        			e.printStackTrace();
        		}
     	
    }
     //Listeyi temizleyip yeniden dolduruyoruz
    private void ClearingTheList() {      
               db.clear();
             ListTheUsers();
             ListTheCustomer();
             System.out.println("refresh"+db);
        	
	}
  //Listemizle haritamızda olanları karşılaştırıyoruz haritada fazla olanları siliyoruz
    private void CompareAndDelete() {      
     new Thread(() -> {
        	Connection con = null;

        	do {
        		try {
        			Thread.sleep(3000l);
        		} catch (InterruptedException ex) {
        			Logger.getLogger(Browser.class.getName()).log(Level.SEVERE, null, ex);
        		}

        		try {
                             ClearingTheList();
        	                                  
        		  Platform.runLater(() -> {
                    locations.forEach((k, coordinate) -> {
                        if(!db.contains(coordinate.getCustomerName()))
                        { 
                            System.out.println("sddddalşkfşakgqğprkdqwwilkdqaiadaapikfliasisa");
                     locations.remove(k, coordinate);
                        webEngine.reload();
                     
                       
                        }
                      
                    });
                });
        			
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
        	}while(true);
        	
        }).start();
	}
    
    
    
    ///Diger ekrandan elle manuel musteri ekleme yapilmis mi kontrolu
    //yapildiysa ekrana ekleme
    public void checkNewLocation(Connection con) {
        try {
            
            Statement statement = (Statement) con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT b.name,a.latitude,a.longitude,a.id FROM transport.locations a,transport.customers b where b.location_id = a.id and b.status = '1' order by a.id" );
            int i = 1;
            char[] locPoints = locationPoints.toCharArray();
            while(rs.next()) {
                if (!isExist(rs.getInt("id"))) {
                    Coordinate coordinate = new Coordinate(rs.getString("name"),rs.getString("latitude"), rs.getString("longitude"));
                    coordinate.setInserted(true);
                    coordinate.setLocationId(rs.getInt("id"));
                    locations.put(String.valueOf(locPoints[i]), coordinate);
                    db.add(rs.getString("name"));
                }
                i++;
            }
            statement.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(Browser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Class taki map de var mi diye bakar
    public boolean isExist(Integer locationId) {
        boolean isExist = false;
        
        Iterator<Map.Entry<String, Coordinate>> iterator = locations.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Coordinate> entry = iterator.next();
            if (entry.getValue().getLocationId() == null) continue;
           if (locationId.compareTo(entry.getValue().getLocationId()) == 0) {
                isExist = true;
            }
        } 
        
        return isExist;
    }
    
    //Ekran ilk acildiginda databasedeki kullanicinin yerini ve musterilerin yerini alır
    public void getInsertedLocations() {
        try {
            try (Connection con = DbHelper.getConnection(); Statement statement = (Statement) con.createStatement()) {
                int i = 0;
                char[] locPoints = locationPoints.toCharArray();
                ResultSet rsUser = statement.executeQuery("SELECT b.username,a.latitude,a.longitude,a.id FROM transport.locations a,transport.users b,transport.user_locations c where c.location_id = a.id and c.user_id = b.id order by a.id" );
                
                while(rsUser.next()) {
                    Coordinate coordinate = new Coordinate(rsUser.getString("username"),rsUser.getString("latitude"), rsUser.getString("longitude"));
                    coordinate.setInserted(true);
                    coordinate.setLocationId(rsUser.getInt("id"));
                    locations.put(String.valueOf(locPoints[i]), coordinate);
                   
                    i++;
                }
                
                ResultSet rs = statement.executeQuery("SELECT b.name,a.latitude,a.longitude,a.id FROM transport.locations a,transport.customers b where b.location_id = a.id and b.status = '1' order by a.id" );
                
                while(rs.next()) {
                    Coordinate coordinate = new Coordinate(rs.getString("name"),rs.getString("latitude"), rs.getString("longitude"));
                    coordinate.setInserted(true);
                    coordinate.setLocationId(rs.getInt("id"));
                    locations.put(String.valueOf(locPoints[i]), coordinate);
                    i++;
                    
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Browser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Ekranda yeni kordinat girildiginde db ye ekler
    public void insertDB(Coordinate coordinate) {
        if(coordinate.isInserted()) return;
        try {
            
            try (Connection con = DbHelper.getConnection()) {
                con.setAutoCommit(false);
                PreparedStatement statement = con.prepareStatement("insert into transport.locations(latitude,longitude) values(?,?)",Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, coordinate.getLat());//latitude
                statement.setString(2, coordinate.getLng());//longitude
                statement.executeUpdate();
                Integer locationId = null;
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    keys.next();
                    locationId = keys.getInt(1);
                    statement = con.prepareStatement("insert into transport.customers(name,location_id,status) values(?,?,?)");
                    statement.setString(1, coordinate.getCustomerName());//name
                    statement.setInt(2, locationId);//location_id
                    statement.setString(3, "1");//Status
                    statement.executeUpdate();
                    db.add(coordinate.getCustomerName());
                }
                statement.close();
                con.commit();
                coordinate.setInserted(true);
                coordinate.setLocationId(locationId);
                coordinate.setShowed(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Browser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Kordinat classi
    private class Coordinate {
        private String customerName;
        private String lat;
        private String lng;
        private boolean inserted = false;
        private Integer locationId;
        private boolean showed = false;
        
        public Coordinate(String customerName, String lat, String lng) {
            this.customerName = customerName;
            this.lat = lat;
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public String getLng() {
            return lng;
        }

        public String getCustomerName() {
            return customerName;
        }

        public boolean isInserted() {
            return inserted;
        }

        public void setInserted(boolean inserted) {
            this.inserted = inserted;
        }

        public Integer getLocationId() {
            return locationId;
        }

        public void setLocationId(Integer locationId) {
            this.locationId = locationId;
        }

        public boolean isShowed() {
            return showed;
        }

        public void setShowed(boolean showed) {
            this.showed = showed;
        }
    }
    
    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }
 
    @Override protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }
 
    @Override protected double computePrefWidth(double height) {
        return 750;
    }
 
    @Override protected double computePrefHeight(double width) {
        return 500;
    }
}
