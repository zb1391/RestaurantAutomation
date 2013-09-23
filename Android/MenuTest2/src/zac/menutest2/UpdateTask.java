package zac.menutest2;

import android.os.AsyncTask;
import android.util.Log;

public class UpdateTask extends AsyncTask<String,Void,Void>{

	public OrderList order;
	private String macaddr;
	public UpdateTask(OrderList order,String macaddr){
		this.order=order;
		this.macaddr=macaddr;
	}
	@Override
	protected Void doInBackground(String... params) {
		new MenuItemQuery(macaddr).updateOrderTable(order);
		Log.d(null,"Finished Update");
		return null;
	}

}
