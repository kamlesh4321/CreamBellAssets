package info.kamleshgupta.myapplicationassets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;


public class FirstPage extends AppCompatActivity {


	clsUtility clsUtility=new clsUtility();
	GridView grdProduct;
	Button btnSearch;
	EditText txtSearch;



	String Url="";
	RelativeLayout rtlEnter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firstpage);


		     Thread background = new Thread() {
				 public void run() {
					 try {
						 sleep(6 * 1000);
						 Intent i = new Intent(getBaseContext(), MainActivity.class);

						// startActivity(i);
						// finish();

					 } catch (Exception e) {
					 }
				 }
			 };
	       background.start();

		rtlEnter=(RelativeLayout)findViewById(R.id.rtlEnter);
		rtlEnter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			/*	if (clsUtility.isNetworkAvailable(HomePage.this) == false) {
					clsUtility.msgBox(HomePage.this, "No Network Connection", "Internet is not available right now.");
					return;
				}*/
				//db.execSQL("delete from Mobiletbl");
				Intent i = new Intent(getBaseContext(), Login.class);
				startActivity(i);
			}
		});
	}




}