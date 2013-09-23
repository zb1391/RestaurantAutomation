package zac.menutest2;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

/**
 * This class is to perform any query of the database as a new thread so the entire
 * app does not get held up by the query
 * @author zac
 *
 */
public class QueryTask extends AsyncTask<String,Void,Void>{
	String foodtype;
	Activity MenuScreen;
	String macaddr;
	public MenuItem[] menu;
	public QueryTask(Activity a,String food,String m){
		foodtype = food;
		MenuScreen = a;
		macaddr = m;
	}

	@Override
	protected void onPreExecute(){
		
		Log.d(null,"Starting Thread");
		TextView t = (TextView) ((MenuTest2Activity) MenuScreen).findViewById(R.id.name);
		t.setText(R.string.hello1);
		Button b = (Button) ((MenuTest2Activity) MenuScreen).findViewById(R.id.AddOrder);
		b.setEnabled(false);
	}
	
	@Override
	protected void onPostExecute(Void v){
		Log.d(null,"Finished thread");
		((MenuTest2Activity) MenuScreen).menu=menu;
		((MenuTest2Activity) MenuScreen).resetOrderView();
		((MenuTest2Activity) MenuScreen).refreshList();
	}
	
	/*protected void onPostExecute(String param){
		Log.d(null,"Finished thread");
		
	}*/

	@Override
	protected Void doInBackground(String... params) {
		Log.d(null,"Running in background");
		// TODO Auto-generated method stub
		MenuItemQuery q = new MenuItemQuery(macaddr);
		menu= q.queryMenu(foodtype);
		
		return null;
	}
	

}
