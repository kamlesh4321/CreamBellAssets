package info.kamleshgupta.myapplicationassets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


public class HomePage extends AppCompatActivity {


	clsUtility clsUtility=new clsUtility();
	GridView grdProduct;
	Button btnSearch;
	EditText txtSearch;

	//MaterialSearchView searchView;

	String Url="";
	RelativeLayout rtlEnter;
	ProgressDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		setContentView(R.layout.homepage);

		/*dialog = ProgressDialog.show(HomePage.this, "", "Please Wait...", true);
		     Thread background = new Thread() {
				 public void run() {
					 try {
						 sleep(5 * 1000);
						 Intent i = new Intent(getBaseContext(),Branch.class);
						 dialog.dismiss();
						 //finish();
						startActivity(i);
						// finish();

					 } catch (Exception e) {
						 dialog.dismiss();
					 }
				 }
			 };
	       background.start();*/

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