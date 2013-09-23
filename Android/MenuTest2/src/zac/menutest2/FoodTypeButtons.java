package zac.menutest2;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;

/**
 * This class sets up the foodtype buttons located in the top left of the screen
 * during the OrderMenu Activity
 * @author zac
 *
 */
public class FoodTypeButtons {
	ImageButton Dessert,Appetizer;
	Activity MenuScreen;
	private ButtonListener b;
	public FoodTypeButtons(Activity a){
		MenuScreen = a;
		b = new ButtonListener();
		Dessert = (ImageButton) a.findViewById(R.id.Dessert);
		Dessert.setImageResource(R.drawable.dessert_button);
		Dessert.setOnClickListener(b);
		Appetizer = (ImageButton) a.findViewById(R.id.Appetizer);
		Appetizer.setImageResource(R.drawable.appetizer_button);
		Appetizer.setOnClickListener(b);
	}
	
	private class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = v.getId();
			if(id == R.id.Dessert){
				
				((MenuTest2Activity) MenuScreen).setMenu("Flatbreads");
			}
			else if(id == R.id.Appetizer){
				
				((MenuTest2Activity) MenuScreen).setMenu("Appetizers");
			}
			
			
		}
		
	}
	
}
