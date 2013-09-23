package zac.menutest2;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class SendIntent {

	public static void startNewActivity(Activity a,OrderList order, Class<?> c){
		Intent intent = new Intent(a,c);
		order.calculateTotal();
		for(int x=0;x<order.getSize();x++){
			Order o;
			o=order.getOrder(x);
			intent.putExtra("order"+x+"name", o.Name);
			intent.putExtra("order"+x+"notes",o.notes);
			intent.putExtra("order"+x+"price", o.price);
			intent.putExtra("order"+x+"ordered", o.ordered);
			intent.putExtra("order"+x+"side",o.side);
			
		}
		intent.putExtra("ordersize", order.getSize());
		intent.putExtra("billtotal", order.total);
		a.startActivity(intent);
	}
	

	public static OrderList getOrder(Activity a){
		Intent intent = a.getIntent();
		OrderList order = new OrderList();
		int ordersize = intent.getIntExtra("ordersize",0);
		Log.d(null,"Order is size "+ordersize);
		
		for(int i=0;i<ordersize;i++){
			String name = intent.getStringExtra("order"+i+"name");
			String notes = intent.getStringExtra("order"+i+"notes");
			double price = intent.getDoubleExtra("order"+i+"price",0);
			boolean ordered = intent.getBooleanExtra("order"+i+"ordered", false);
			String side = intent.getStringExtra("order"+i+"side");
			Order o = new Order(name,notes,price,ordered,side);
			order.addToOrder(o);
		}
		
		return order;
	}
}
