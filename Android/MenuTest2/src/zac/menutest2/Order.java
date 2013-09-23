package zac.menutest2;

import android.util.Log;
/**
 * Holds all information for procesing an individual order. It is a child
 * class of menu item so holds name, desc, price, as well as chef notes
 * boolean ordered describes if the user has hit the "Send Order" button from the ConfirmOrder Page
 * when the user selects this button, ordered becomes true and with this parameter true, the user can
 * no longer remove the item from the list of orders. If you run the code you will see it makes sense.
 * 
 * @author zac
 *
 */
public class Order extends MenuItem{
	public String notes,side;
	public boolean ordered;
	private int position;
	public Order(MenuItem i,String ChefNotes){
		super(i.Name,i.Description,i.price);
		notes = ChefNotes;
		ordered = false;
	} 
	
	public Order(String name,String notes, double price){
		super(name,null,price);
		this.notes=notes;
	}
	public Order(String name,String notes,double price,boolean ordered,String side){
		this(name,notes,price);
		this.ordered=ordered;
		this.side=side;
		
	}
	public Order(String name, String notes, double price,String side){
		this(name,notes,price);
		this.side=side;
		
	}
	//GETTERS AND SETTERS //
	public String toString(){
		return this.Name+"\n     "+notes+"\n	with "+side+"\n";
		
	}
	public int getPosition(){
		return position;
	}
	public void setposition(int i){
		position=i;
	}
}
