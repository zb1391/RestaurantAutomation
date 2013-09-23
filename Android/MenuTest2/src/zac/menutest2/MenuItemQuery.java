package zac.menutest2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

/**
 * This class uses a php script to query a mysql database for menuitems
 * @author zac
 *
 */
public class MenuItemQuery {
	private HttpClient httpclient,client2;
	private HttpPost httppost,httppost2;
	private String macaddr;

	/**
	 * Constructer
	 * @param m - mac address of tablet
	 */
	public  MenuItemQuery(String m){
		macaddr = m;
		if (macaddr == null){
			macaddr = "11:11:11:11:11:11";
		}
	}
	
	/**
	 * This method connects to the database and updates the orderinfo table
	 * this table will be viewed by the chef in the chef interface
	 * @param order - the list of orders to be sent to database
	 */
	public void updateOrderTable(OrderList order){
		JSONObject json = new JSONObject();
		for(int i =0;i<order.getSize();i++){
			Order o = order.getOrder(i);
			if(o.ordered==false){
				try {
					json.put("name", o.Name);
					json.put("notes", o.notes);
					json.put("side", o.side);
					json.put("price", o.price);
					json.put("mac", macaddr);
	           	    HttpClient httpclient = new DefaultHttpClient();
	         	    HttpClient client2 = new DefaultHttpClient();

	         	    HttpPost httppost = new HttpPost("http://172.31.63.117/updateorders.php");
	         	    httppost.setEntity(new ByteArrayEntity(json.toString().getBytes(
	                    "UTF8")));
	         	    httppost.setHeader("json",json.toString());
	         	    httpclient.execute(httppost);
	         	    order.setToOrdered(i);
	         	    
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e(null,"SOMETHING BAD HAPPENED");
				}	
			
			}	
		}
	}
	
	
	/**
	 * This method querys database for menu items
	 * The query selects all menu items where foodtype = String FoodType
	 * @param foodtype a string represent a foodtype in database
	 * @return
	 */
	public MenuItem[] queryMenu(String foodtype){
		Log.i("log_tag","Starting query");
		InputStream is = null;
        MenuItem[] items = new MenuItem[0];
        String queryitem ="sqltest";
        String result = "";
        //the year data to send
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("foodtype",foodtype));
        //Establish Connection
        try{
            httpclient = new DefaultHttpClient();
            client2 = new DefaultHttpClient();
            httppost = new HttpPost("http://172.31.63.117/startconnection.php"); 
            httpclient.execute(httppost);
         }
         catch(Exception e){
             Log.e("log_tag", "Error in http connection "+e.toString());
         }
        
        //http post
        try{
                
                httppost2 = new HttpPost("http://172.31.63.117/"+queryitem+".php");
                httppost2.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client2.execute(httppost2);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
        }catch(Exception e){
                Log.e("log_tag", "Error in http connection "+e.toString());
        }
        //convert response to string
        try{
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                }
                is.close();
         
                result=sb.toString();
        }catch(Exception e){
                Log.e("log_tag", "Error converting result "+e.toString());
        }
         
        //parse json data into array
        try{
                JSONArray jArray = new JSONArray(result);
                items = new MenuItem[jArray.length()];
                String n,d;
                double p;
                int sides;
                boolean s;
                for(int i=0;i<jArray.length();i++){
                        JSONObject json_data = jArray.getJSONObject(i);
                        n = json_data.getString("name");
                        d=json_data.getString("description");
                        p=json_data.getDouble("price");
                        sides =json_data.getInt("sides");
                        Log.d(null,"sides is "+sides);
                        if(sides==1)
                        	s=true;
                        else
                        	s=false;
                        items[i]= new MenuItem(n,d,p,s);
                        Log.d(null,items[i].toString()+" "+items[i].sides);
                        
                }
        }
        catch(JSONException e){
                Log.e("log_tag", "Error parsing data "+e.toString());
        }
        Log.i("log_tag","Finished query");
        return items;
	}
}
