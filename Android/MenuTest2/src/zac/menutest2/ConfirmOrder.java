package zac.menutest2;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class displays the Order Confirmation Page
 * It uses a ListView with a customized adapter to display each item
 * Because this Activity depends on the OrderMenu Activity
 * it retrieves information from the OrderMenu activity through an intent
 * in order
 * @author zac
 * 
 */
public class ConfirmOrder extends Activity implements OnClickListener{
	OrderList order;
	ListView v;
	Intent intent;
	int ordersize;
	TextView total;
	double billtotal;
	ImageButton SendOrder;
	Button Back;
	String address;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirmorder);
		getWindow().setBackgroundDrawableResource(R.drawable.confirmorderscreen);
		View footer = getLayoutInflater().inflate(R.layout.pricefooter, null);
		intent = getIntent();
		order =SendIntent.getOrder(this);
		ordersize = intent.getIntExtra("ordersize",0);
		billtotal = intent.getDoubleExtra("billtotal", 0);
		SendOrder = (ImageButton) findViewById(R.id.SendOrder);
		Back = (Button) findViewById(R.id.BackToMenu);
		SendOrder.setOnClickListener(this);
		Back.setOnClickListener(this);
		v = (ListView) findViewById(R.id.orderlist);
		v.addFooterView(footer);
		total=(TextView) footer.findViewById(R.id.TotalPrice);
		NumberFormat formatter = new DecimalFormat("0.00");
		
		//get mac address
        WifiManager wimanager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        address= wimanager.getConnectionInfo().getMacAddress();
        
		if(billtotal==0){
			total.setText("Please Select an Item From the Menu");
		}
		else{
			total.setText("Total: $"+formatter.format(billtotal));
		}
		
		displayOrder();
	}
	
	
	/**
	 * Sets up listview to be viewed
	 */
	private void displayOrder(){
		
		List<Order> orderlist = new ArrayList<Order>();
		
		for(int i=0;i<order.getSize();i++){
			orderlist.add(order.getOrder(i));
		}
		OrderAdapter adapter = new OrderAdapter(this, orderlist,order,total);
		v.setAdapter(adapter);

	}


	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id==R.id.SendOrder){
			if(order.getSize()!=0 && !order.orderedYet()){
				new UpdateTask(order,address).execute("");
				order.printOrderList();
				Context context = v.getContext();
				Toast toast = Toast.makeText(context, "Order Sent!",Toast.LENGTH_SHORT);
				toast.show();
				SendIntent.startNewActivity(this, order, MenuTest2Activity.class);
				
			}
			else{
				Context context = v.getContext();
				Toast toast;
				if(order.getSize()==0)
					toast = Toast.makeText(context, "Please Select an Item From Menu First",Toast.LENGTH_SHORT);
				
				else
					toast = Toast.makeText(context, "Order Already Sent", Toast.LENGTH_SHORT);
				
				toast.show();
			}
		}
		else if(id == R.id.BackToMenu){
			SendIntent.startNewActivity(this, order, MenuTest2Activity.class);
		}
		
	}
	
	
}
