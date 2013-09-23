package zac.menutest2;

import java.util.LinkedList;

import android.util.Log;
/**
 * This is a linked list of orders. It is a dynamic list of all orders sent by the user
 * @author zac
 *
 */
public class OrderList {
	//order is a static list so any time you instantiate a new orderlist it automatically has the order set up
	private static LinkedList<Order> order;
	public double total;
	public OrderList(){
		order = new LinkedList<Order>();
		total=0;
	}
	
	/**
	 * Adds an item to linked list
	 * @param newOrder
	 */
	public void addToOrder(Order newOrder){
		newOrder.setposition(order.size()+1);
		order.add(newOrder);
	//	Log.d(null, "Added "+newOrder.Name+" to order");
	}
	
	/**
	 * Checks to see if the desired object is the last in list
	 * i dont think i use this anywhere anymore
	 * @param position
	 * @return
	 */
	public boolean isLast(int position){
		if (position==(order.size()-1))
			return true;
		else
			return false;
	}
	/**
	 * Prints order to terminal screen
	 */
	public void printOrderList(){
		for(int i=0;i<order.size();i++){
			Order o = order.get(i);
			Log.d(null,""+ o.toString());
		}
	}
	
	public boolean orderedYet(){
		for(int i=0;i<order.size();i++){
				Order o = order.get(i);
				if(o.ordered==false)
					return false;
			}
		return true;
	}
	
	/**
	 * Calculates bill
	 */
	public void calculateTotal(){
		total=0;
		for(int i=0;i<order.size();i++){
			total+=order.get(i).price;
		}
		Log.d(null, "Total is "+total);
	}
	
	/**
	 * removes an order item from list
	 * @param i position in list of order to be removed
	 */
	public void removeOrder(int i){
		order.remove(i);
	}
	
	public void setToOrdered(){
		for(int i=0;i<order.size();i++)
			order.get(i).ordered=true;
	}
	
	public void setToOrdered(int i){
		order.get(i).ordered=true;
	}
	//GETTERS and SETTERS
	public int getPos(Order o){
		return order.indexOf(o);
	}

	
	public Order getOrder(int i){
		return order.get(i);
	}
	
	public int getSize(){
		return order.size();
	}
	

}
