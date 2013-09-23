package zac.menutest2;


import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/**
 * This is the main activity of the project.
 * It displays the order menu page for users to order items off menu
 * @author zac
 *
 */
public class MenuTest2Activity extends Activity implements OnClickListener{
	final static int INVISIBLE = 8;
	TextView tv,ChefNotesTitle,PriceLabel,SideLabel,SelectedSide;
	EditText ChefNotes;
	ListView v,SidesList;
	public MenuItem[] menu;
	Button b,ConfirmOrder;
	MenuItemQuery q;
	FoodTypeButtons FoodButtons;
	MenuItem orderItem;
	OrderList order;
	String notes,side,address;
	Intent intent;
	int ordersize;
	double billtotal;
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		//Sets activity to read from main.xml
		setContentView(R.layout.main);
		
		//Sets Background to desired Image
		getWindow().setBackgroundDrawableResource(R.drawable.menubackground2);
		
		//Sets Order Details Window to be Empty
		initializeOrderView();
		
		//Set up current menu queries for chicken items right now
		setMenu("Appetizers");

		//Set up Different Menu Category Buttons
		FoodButtons = new FoodTypeButtons(this);
		
		//get macaddr
        WifiManager wimanager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        address= wimanager.getConnectionInfo().getMacAddress();

		//instantiate the order list
		intent = getIntent();
		order =SendIntent.getOrder(this);
		ordersize = intent.getIntExtra("ordersize",0);
		billtotal = intent.getDoubleExtra("billtotal", 0);
	}
	
	public void initializeOrderView(){
		//Set up add to order button
		b = (Button) findViewById(R.id.AddOrder);
		b.setEnabled(false);
		b.setVisibility(INVISIBLE);
		b.setOnClickListener(this);
		ConfirmOrder = (Button) findViewById(R.id.ConfirmOrder);
		ConfirmOrder.setOnClickListener(this);
		
		//Set up Price Label
		PriceLabel = (TextView) findViewById(R.id.price);
		PriceLabel.setVisibility(INVISIBLE);
		
		//Set up Name and Description TextView (located in white box on screen)
		tv = (TextView) findViewById(R.id.name);
		tv.setText(R.string.hello);
		tv.setMovementMethod(new ScrollingMovementMethod());
		v = (ListView) findViewById(R.id.mylist);
		ChefNotesTitle = (TextView)findViewById(R.id.ChefNotesTitle);
		ChefNotesTitle.setVisibility(INVISIBLE);
		
		//set up text field for chef notes
		ChefNotes = (EditText)findViewById(R.id.ChefNotes);
		ChefNotes.setEnabled(false);
		ChefNotes.setVisibility(INVISIBLE);
		notes = null;
		
		//Setup Sides List
		SidesList = (ListView) findViewById(R.id.sideslist);
		SidesList.setVisibility(INVISIBLE);
		SideLabel = (TextView)findViewById(R.id.curside);
		SideLabel.setVisibility(INVISIBLE);
		SelectedSide = (TextView) findViewById(R.id.selectedside);
		SelectedSide.setVisibility(INVISIBLE);
	}
	
	/**
	 * After an item has been ordered the screen should return to the
	 * original status with a message that says to select an item from the menu
	 */
	public void resetOrderView(){
		b.setEnabled(false);
		b.setVisibility(INVISIBLE);
		PriceLabel.setVisibility(INVISIBLE);
		tv.setText(R.string.hello);
		ChefNotesTitle.setVisibility(INVISIBLE);
		ChefNotes.setEnabled(false);
		ChefNotes.setVisibility(INVISIBLE);
		SidesList.setVisibility(INVISIBLE);
		SideLabel.setVisibility(INVISIBLE);
		SelectedSide.setVisibility(INVISIBLE);
		notes = null;
		side=null;
		getWindow().setBackgroundDrawableResource(R.drawable.menubackground2);
	}
	/**
	 * Queries the database
	 * @param foodtype - the foodtype selected by user i.e. appetizer, flatbread, etc.
	 */
	public void setMenu(String foodtype){
		tv.setText("LOADING...");
		new QueryTask(this,foodtype,address).execute("");
	//	refreshList();
	}
	
	/**
	 * This method refreshes the listview after a query to the database
	 */
	public void refreshList(){
		ArrayAdapter<MenuItem> adapter = new ArrayAdapter<MenuItem>(this,
				android.R.layout.simple_list_item_1, menu);
		v.setAdapter(adapter);
		OnItemClickListener listener = new OnItemClickListener(){
			
			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int position,long id) {
				orderItem = new MenuItem(null, null, 0);
				orderItem = (MenuItem) v.getAdapter().getItem(position);
				tv.setText(orderItem.Name+"\n"+orderItem.Description);
				b.setEnabled(true);
				b.setVisibility(0);
				ChefNotesTitle.setVisibility(0);
				ChefNotes.setEnabled(true);
				ChefNotes.setVisibility(0);
				ChefNotes.setText(null);
				NumberFormat formatter = new DecimalFormat("0.00");
				PriceLabel.setText("Price: $"+formatter.format(orderItem.price));
				PriceLabel.setVisibility(0);
				side=null;
				if(menu[0].sides==true){
					getWindow().setBackgroundDrawableResource(R.drawable.menubackground);
					showSides();
					
				}
			}
			
		};
		
		v.setOnItemClickListener(listener);
	}
	

	public void showSides(){
		SidesList.setVisibility(0);
		SideLabel.setVisibility(0);
		SelectedSide.setVisibility(0);
		SelectedSide.setText("Side: ");
		String[] sides = {"French Fries","Onion Rings","Buffalo Chips","Coleslaw","Potato Wedges","Side Salad","Veggie Boat"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, sides);
		SidesList.setAdapter(adapter);
		OnItemClickListener listener = new OnItemClickListener(){
			
			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int position,long id) {
				String s = (String) SidesList.getAdapter().getItem(position);
				SelectedSide.setText("Side: "+s);
				side=s;
			}
			
		};
		SidesList.setOnItemClickListener(listener);
		}
	/**
	 * Button Listener for activity
	 */
	@Override
	public void onClick(View v) {
		int id = v.getId();
		//If the user selects add to order, the linked list of orders (OrderList order)
		//gets updated
		if(id == R.id.AddOrder){
			Context context = v.getContext();
			Toast toast;
			int duration = Toast.LENGTH_SHORT;
			if(menu[0].sides==true && side==null){
				toast = Toast.makeText(context, "Please Select a Side First",duration);
				toast.show();
			}
			else{
				notes = ChefNotes.getText().toString();
				Order o = new Order(orderItem, notes);
				o.side=side;
				order.addToOrder(o);
				order.printOrderList();
				resetOrderView();
				toast = Toast.makeText(context, "Added "+o.Name+" to order", duration);
				toast.show();
			}
		}
		/**
		 * If the user selects the confirm order, the app will start a new activity called ConfirmOrder
		 * In order to receive all of the information in the order, we must package all of the info into
		 * an intent. This intent can be retrieved in a different activity
		 */
		else if(id == R.id.ConfirmOrder){
			//Log.d(null,"Confirm Order Pressed");
			SendIntent.startNewActivity(this, order, ConfirmOrder.class);
			
		}
		
	}
	
}