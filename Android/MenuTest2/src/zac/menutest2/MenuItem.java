package zac.menutest2;

/**
 * This class holds all information for processing a menu item from database
 * We only care about the name, description and price. This class holds the information
 * for properly displaying a menu item on the menu app
 * @author zac
 *
 */
public class MenuItem {
	public String Name,Description;
	public double price;
	public boolean sides;
	public MenuItem(String s,String d,double p){
		Name = s;
		Description=d;
		price=p;
	}
	
	public MenuItem(String s,String d,double p,boolean sides){
		this(s,d,p);
		this.sides=sides;
	}
	/**
	 * Override the toString method so a ListView knows what to print
	 * when handling the MenuItem class
	 */
	public String toString(){
		return Name;
	}
}
