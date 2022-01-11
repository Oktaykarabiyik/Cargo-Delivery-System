
package yazlab1proje1;

import com.mysql.jdbc.Connection;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import static yazlab1proje1.HomePage.cargoTable1;

public class Distance {
    static int indis;
    public Distance(){}
     static ArrayList<Double> xcoordinate = new ArrayList<Double>();
    static ArrayList<Double> ycoordinate = new ArrayList<Double>();
     
    public static void cargoTable() {
        try {
    xcoordinate.clear();
   ycoordinate.clear();
      Connection myConn=(Connection) DriverManager.getConnection("---------------------------------");
             Statement myStat = (Statement) myConn.createStatement();
             ResultSet rs = myStat.executeQuery("SELECT latitude,longitude  FROM locations " );
        int k=0;
          while (rs.next()) {
                 String lat,longi;
              lat=rs.getString("latitude");
              longi=rs.getString("longitude");
              
                xcoordinate.add(Double.parseDouble(lat));
            ycoordinate.add(Double.parseDouble(longi));
            k++;
              }  
        } catch (SQLException ex) {
              Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
      
        }
    }
    public static void arraylist() {
        for (int i = 0; i <xcoordinate.size(); i++) {
            System.out.println( xcoordinate.get(i)+"  ---  "+ycoordinate.get(i));
  }
        indis=xcoordinate.size();
       
    }



  static ArrayList<Integer> dizi = new ArrayList<Integer>();
static double travllingSalesmanProblem(Double graph[][],
                                    int s)
{  
    arraylist();
 int boyut = xcoordinate.size();
 
  ArrayList<Integer> koseler = new ArrayList<Integer>();
   
  for (int u = 0; u < boyut; u++){
    if (u != s){
      koseler.add(u);}
  }
 
 
  
double min_yol = Double.MAX_VALUE;
  do
  {
   double yolagirligi = 0.0;
 
    int k = s;
    int m ;
   
    for( m= 0; m< koseler.size(); m++)
    { 
      yolagirligi +=
              graph[k][koseler.get(m)];
     
      k = koseler.get(m);
    } 
    yolagirligi += graph[k][s];
 
    
    if(yolagirligi<min_yol)
    {
       dizi.clear();
       for(int num=0;num<koseler.size();num++)
       {int ekle=koseler.get(num);
           dizi.add(ekle);
       }
       
    }
      
      
    min_yol =Math.min(min_yol,
                        yolagirligi);

  } while (findNextPermutation(koseler));
  
    
  return min_yol;
  
}

public static ArrayList<Integer> swap(
              ArrayList<Integer> data,
              int sol, int sag)
{
 
 int temp = data.get(sol);
  data.set(sol, data.get(sag));
  data.set(sag, temp);

  return data;
}

public static ArrayList<Integer> reverse(
              ArrayList<Integer> data,
              int sol, int sag)
{
  
  while (sol< sag)
  {
    int temp = data.get(sol);
    data.set(sol++,
             data.get(sag));
    data.set(sag--, temp);
  }
 

  return data;
}

public static boolean findNextPermutation(
                      ArrayList<Integer> data)
{ 
  
  if (data.size() <= 1)
    return false;
 
  int last = data.size() - 2;
 
  while (last >= 0)
  {
    if (data.get(last) <
        data.get(last + 1))
    {
      break;
    }
    last--;
  }
 
  
  if (last < 0)
    return false;
 
  int nextGreater = data.size() - 1;
 
  
  for (int i = data.size() - 1;
           i > last; i--) {
    if (data.get(i) >
        data.get(last))
    {
      nextGreater = i;
      break;
    }
  }
 
  data = swap(data,
              nextGreater, last);
 
  data = reverse(data, last + 1,
                 data.size() - 1);
 
  return true;
} 
  static ArrayList<Double> dbShortestX = new ArrayList<Double>();
    static ArrayList<Double> dbShortestY = new ArrayList<Double>();
    
    public static void shortest() throws SQLException{
          int j;
           cargoTable();
     arraylist();
      
    int i; 
    int  index=xcoordinate.size();
    Double[][] Distances=new Double[index][index];
       
    for(i=0;i<index;i++){
           for(j=0;j<index;j++){
               Distances[i][j]=sqrt((pow((xcoordinate.get(i)-xcoordinate.get(j)),2.0))+(pow((ycoordinate.get(i)-ycoordinate.get(j)),2.0)));           
               
           }
          
      System.out.println("");   }
        System.out.println("\n");
    int s = 0;
  System.out.println(
  travllingSalesmanProblem(Distances, s));
  dbShortestX.clear();
 dbShortestY.clear();
 dbShortestX.add(xcoordinate.get(0));
 dbShortestY.add(ycoordinate.get(0));
    for(int num=0;num<dizi.size();num++)
       {
           int a=dizi.get(num);
          double b,c,d,e;
           b=xcoordinate.get(a);
           c=ycoordinate.get(a);
           dbShortestX.add(b);
           dbShortestY.add(c);
      
       }HomePage home =new HomePage();
    int sayi=dizi.get(dizi.size()-1);
          dbShortestX.add(xcoordinate.get(sayi));
          dbShortestY.add(ycoordinate.get(sayi));
          DefaultTableModel dm = (DefaultTableModel)cargoTable1.getModel();
dm.getDataVector().removeAllElements();
cargoTable1.repaint(); 
   for(int num=0;num<indis;num++)
       {
           
          
String tbData[] = {String.valueOf(dbShortestX.get(num)),String.valueOf(dbShortestY.get(num))};//tablo içinn dizi oluşturdu
  DefaultTableModel tblModel = (DefaultTableModel)cargoTable1.getModel();//tablomuz
   tblModel.addRow(tbData);//tabloya oluşturduğu diziyi verdik
          
       }
 
  home.setVisible(true);
  
    }

    public static void main(String args[]) throws SQLException {
        cargoTable();
        arraylist();
         shortest();

       
            
    }
}
    

