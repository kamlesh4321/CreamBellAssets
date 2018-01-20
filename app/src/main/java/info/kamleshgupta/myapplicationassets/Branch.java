package info.kamleshgupta.myapplicationassets;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


public class Branch extends AppCompatActivity {
    

	clsUtility clsUtility=new clsUtility();

    String Url="";
	String BType="";
	//MaterialSearchView searchView;
	Toolbar toolbar=null;
	Button btnAsansol,btnBaddi,btnGoa,btnKosi;
	private Animation animation;
	String UserId="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.branch);


		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
		// Get name and email from global/application context
		//final String Company  = globalVariable.getCompany();
		final String ShortComp = globalVariable.getShortComp();

		Intent intent=getIntent();


		//Add Search View
		toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("BRANCH");

		toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_24dp);
		//searchView=(MaterialSearchView)findViewById(R.id.search_view);

		if(getSupportActionBar()!=null)
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}



		btnAsansol=(Button)findViewById(R.id.btnAsansol);
		btnBaddi=(Button)findViewById(R.id.btnBaddi);
		btnGoa=(Button)findViewById(R.id.btnGoa);
		btnKosi=(Button)findViewById(R.id.btnKosi);

		Url = clsUtility.SetUrl(getApplicationContext());
		UserId=globalVariable.getEmail().toString();
		String strQuery = "Plant.ashx?UserId="+UserId;
		new HttpAsyncTaskPlant().execute(Url+strQuery);

		/*animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
		((Button)findViewById(R.id.btnAsansol)).setAnimation(animation);
		animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
		((Button)findViewById(R.id.btnBaddi)).setAnimation(animation);
		animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
		((Button)findViewById(R.id.btnGoa)).setAnimation(animation);
		animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
		((Button)findViewById(R.id.btnKosi)).setAnimation(animation);

		animation.start();*/

		btnAsansol.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent i = new Intent(getApplicationContext(), DashBoard.class);
				globalVariable.setBType(btnAsansol.getText().toString());
				globalVariable.setBranch("02");
				startActivity(i);

			}
		});
		btnBaddi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent i = new Intent(getApplicationContext(), DashBoard.class);
				globalVariable.setBType(btnBaddi.getText().toString());
				globalVariable.setBranch("05");
				startActivity(i);

			}
		});
		btnGoa.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent i = new Intent(getApplicationContext(), DashBoard.class);
				globalVariable.setBType(btnGoa.getText().toString());
				globalVariable.setBranch("04");
				startActivity(i);

			}
		});
		btnKosi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent i = new Intent(getApplicationContext(), DashBoard.class);
				globalVariable.setBType(btnKosi.getText().toString());
				globalVariable.setBranch("03");
				startActivity(i);

			}
		});

	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_item,menu);

		//SearchView searchView=(SearchView)menu.findItem(R.id.action_search).getActionView();
		//SearchManager searchManager=(SearchManager)getSystemService(SEARCH_SERVICE);
		//searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home)
		{
			showDialog("'MENU'");
			//finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private class HttpAsyncTaskPlant extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(Branch.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... urls)
		{
			return clsUtility.httpGetDate(urls[0]);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {

			if (null != pDialog && pDialog.isShowing()) { pDialog.dismiss(); }

			//Toast.makeText(getBaseContext(), "Data Received \n" + result, Toast.LENGTH_LONG).show();
			if (result.equals(""))
				clsUtility.msgBox(Branch.this, "Message", "No data received.");

			AddDataPlant(result);
		}
	}


	public void AddDataPlant(String strData)
	{
		try
		{
			String response=strData;
			JSONArray json=new JSONArray(response);
			if(response.contains("Abc123ErrorXyz123"))
			{
				JSONObject json1 = (JSONObject) json.get(0);
				clsUtility.msgBox(Branch.this, "Info ", json1.getString("Abc123ErrorXyz123"));
				return;
			}

			if(json.length()>0)
			{
				for(int i=0;i<json.length();i++)
				{
					JSONObject json1 = (JSONObject) json.get(i);
					String Plant= json1.getString("Code");
					if(Plant.equalsIgnoreCase("02"))
					{
						btnAsansol.setVisibility(View.VISIBLE);
					}
					else if(Plant.equalsIgnoreCase("03"))
					{
						btnKosi.setVisibility(View.VISIBLE);
					}
					else if(Plant.equalsIgnoreCase("04"))
					{
						btnGoa.setVisibility(View.VISIBLE);
					}
					else if(Plant.equalsIgnoreCase("05"))
					{
						btnBaddi.setVisibility(View.VISIBLE);
					}

				}

			}
			else
			{
				Toast.makeText(getApplicationContext(), "No Record Found ! ",Toast.LENGTH_LONG).show();
				return;
			}
		}
		catch(Exception ex)
		{
			clsUtility.msgBox(Branch.this, "Error ", "Error : "+ex.toString());
		}
	}

	//this is for back button Start
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			//Log.d("Test", "Back button pressed!");
			//Toast.makeText(getApplicationContext(), "Back",Toast.LENGTH_LONG).show();
			showDialog("'MENU'");
		}
		else if(keyCode == KeyEvent.KEYCODE_HOME)
		{
			//Log.d("Test", "Home button pressed!");
			//Toast.makeText(getApplicationContext(), "Home",Toast.LENGTH_LONG).show();
			showDialog("'MENU'");
		}
		//return super.onKeyDown(keyCode, event);
		return false;
	}
	//this is for back button End

	void showDialog(String the_key){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to exit ?")
				.setCancelable(true)
				.setPositiveButton("YES", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						finish();
					}
				})
				.setNegativeButton("NO", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		//alert.setTitle("CoderzHeaven.");
		alert.show();
	}

}