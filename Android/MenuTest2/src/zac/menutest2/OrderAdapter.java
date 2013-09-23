package zac.menutest2;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * This is a customized adapter to handle the listview on the Order Confirmation Page
 * It contains a textview to describe the name of the item, an edittext to allow the
 * user to change the notes to chef and a button to remove the item from the order
 * @author zac
 *
 */
public class OrderAdapter extends ArrayAdapter<Order>{
	private final List<Order> list;
	private final Activity context;
	private TextView footer;
	private OrderList order;
	public OrderAdapter(Activity context, List<Order> list,OrderList order,TextView footer) {
		super(context, R.layout.orderview, list);
		this.context = context;
		this.list = list;
		this.order=order;
		this.footer=footer;
	}
	/**
	 * Holds everything needed in a single row of listview
	 * @author zac
	 *
	 */
	static class ViewHolder {
		protected TextView text;
		protected Button button;
		protected EditText edit;
		protected int pos;
	}
	/**
	 * This method is how the adapter displays the information
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		final int p=position;
	
		LayoutInflater inflator=context.getLayoutInflater();
		view = inflator.inflate(R.layout.orderview, null);
		final ViewHolder viewHolder = new ViewHolder();
		viewHolder.pos=position;
		viewHolder.edit= (EditText) view.findViewById(R.id.ConfirmNotes);
		viewHolder.edit.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				try{
					String newNotes = viewHolder.edit.getText().toString();
					Order xk =(Order) viewHolder.button.getTag();
					int pos = order.getPos(xk);
					Order o = order.getOrder(pos);
					o.notes=newNotes;
				}
				catch(Exception e){
					Log.d(null,"cant change, already deleted");
				}
					
			}
				
		});
		viewHolder.text = (TextView) view.findViewById(R.id.ordername);
		viewHolder.button = (Button) view.findViewById(R.id.removeorder);
		viewHolder.button.setOnClickListener(new OnClickListener(){
			@Override
			/**
			 * There is an offset of 1 between the ListView and the LinkedList
			 * thats why you need to get the position as well
			 */
			public void onClick(View v) {
				//Returns the Order Object in the of the selected button
				Order xk =(Order) viewHolder.button.getTag();
					
				//gets the position of the Order Object in the Linked List order
				int pos = order.getPos(xk);

				//removes Order Object from ListView
				remove(xk);
				//removes the order from the linked list
				order.removeOrder(pos);
					
				//refreshes the entire list so that the Order Object is no longer in the list
				list.clear();
				for(int i=0;i<order.getSize();i++){
					list.add(order.getOrder(i));
				}
					
				//Refreshes ListView
				order.calculateTotal();
				if(order.total==0){
					footer.setText("Please Select an Item From the Menu");
				}
				else{
					NumberFormat formatter = new DecimalFormat("0.00");
					footer.setText("Total: $"+formatter.format(order.total));
				}
				notifyDataSetChanged();
			}
				
		});
			
			
		view.setTag(viewHolder);
		viewHolder.button.setTag(list.get(position));
		viewHolder.edit.setTag(list.get(position));
			
		//Need to check if an item was already sent or not
		try{
		Order xk =(Order) viewHolder.button.getTag();
		Log.d(null,xk.Name+" was ordered yet? "+xk.ordered);
			if(xk.ordered==true){
			viewHolder.button.setEnabled(false);
			viewHolder.edit.setEnabled(false);
			}
		}
		catch(Exception e){}

		ViewHolder holder = (ViewHolder) view.getTag();
		holder.edit.setText(order.getOrder(position).notes);
		if(list.get(position).side!=null)
			holder.text.setText(list.get(position).Name+ " \n    with "+list.get(position).side);
		else
			holder.text.setText(list.get(position).Name);
		return view;
		
	}
	

	
}
