package info.kamleshgupta.myapplicationassets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


public class ApprovedRemark extends AppCompatActivity {


	clsUtility clsUtility=new clsUtility();
	SQLiteDatabase db;
    String Url="";
	String BType="";
	//MaterialSearchView searchView;
	Toolbar toolbar=null;
	private Button btn_signIn;
	private EditText et_Remark;
	private Animation animation;
	String OrderNo="";
	Spinner spStatus;
	HashMap<String, String> hmap_Plant=new HashMap<String, String>();



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.approved_remark);


		CreateDatabase();
		Intent intent=getIntent();
		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
		OrderNo=intent.getStringExtra("OrderNo");
		String Status=intent.getStringExtra("Status");
		toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Approved Order  ");

		toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_24dp);
		if(getSupportActionBar()!=null)
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}
		String query="";

		if(ApproveOrder.approveOrder != null) {
			try {
				ApproveOrder.approveOrder.finish();
			} catch (Exception e) {}
		}

		btn_signIn = (Button) findViewById(R.id.btn_signIN);
		animation = AnimationUtils.loadAnimation(ApprovedRemark.this,R.anim.blink);
		((Button) findViewById(R.id.btn_signIN)).setAnimation(animation);
		animation.start();

		et_Remark = (EditText) findViewById(R.id.et_Remark);
		spStatus= (Spinner) findViewById(R.id.spStatus);

		if(Status.equalsIgnoreCase("1"))
		{
			spStatus.setSelection(1);
			spStatus.setEnabled(false);
			et_Remark.setVisibility(View.INVISIBLE);
			btn_signIn.setText("UnApproved");
		}
		else
		{
			spStatus.setEnabled(true);
			btn_signIn.setText("Submit");
		}

		btn_signIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			String	UserId=globalVariable.getEmail().toString();
				Url = clsUtility.SetUrl(getApplicationContext());
				String Status="0";
				if(spStatus.getSelectedItem().toString().equalsIgnoreCase("Approved")) {
					Status="1";
				}
				else if(spStatus.getSelectedItem().toString().equalsIgnoreCase("UnApproved")) {
					Status="0";
				}
				else if(spStatus.getSelectedItem().toString().equalsIgnoreCase("Reject")) {
					Status="2";
				}
				else if(spStatus.getSelectedItem().toString().equalsIgnoreCase("Hole")) {
					Status="3";
				}
				String strQuery = "AppSaved.ashx?OrderNo=" + OrderNo+"&Remark="+et_Remark.getText()+"&Status="+Status+"&UserId="+UserId;
				if (clsUtility.isNetworkAvailable(getApplicationContext())==false)
				{
					Toast.makeText(getApplicationContext(), "No Network Connection "+"Internet is not available right now.", Toast.LENGTH_LONG).show();
					return;
				}
				new HttpAsyncTask().execute(Url + strQuery);

			}
		});

	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {

		return true;
    }


	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(ApprovedRemark.this);
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
				clsUtility.msgBox(getApplicationContext(), "Message", "No data received.");

			AddData(result);
		}
	}

	public void AddData(String strData)
	{
		try
		{
			String response=null;
			response=strData;
			JSONArray json=new JSONArray(response);

			if(response.contains("Abc123ErrorXyz123"))
			{
				JSONObject json1=  (JSONObject) json.get(0);
				Toast.makeText(getApplicationContext(), ""+json1.getString("Abc123ErrorXyz123"), Toast.LENGTH_LONG).show();
				return;
			}
			if(response.contains("Abc123SuccessXyz123"))
			{
				JSONObject json1=  (JSONObject) json.get(0);
				Toast.makeText(getApplicationContext(), ""+json1.getString("Abc123SuccessXyz123"), Toast.LENGTH_LONG).show();
				final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
				globalVariable.setRefAction("1");
				globalVariable.setRefActiond("1");
				Intent i = new Intent(ApprovedRemark.this, ApproveOrder.class);
				startActivity(i);
				finish();
				return;
			}
		}
		catch(Exception ex)
		{
			//clsUtility.msgBox(getApplicationContext(), "Error", "Error : "+ex.toString());
			Toast.makeText(getApplicationContext(), ""+"Error : "+ex.toString(), Toast.LENGTH_LONG).show();
		}
		//BindGrid();
	}

	public void CreateDatabase()
	{
		db=openOrCreateDatabase("BM2.db", MODE_PRIVATE, null);
		//db.execSQL("DROP TABLE IF EXISTS AddToCart");
		db.execSQL("CREATE TABLE IF NOT EXISTS Register(UserId nvarchar(50),FName nvarchar(50),LName nvarchar(50),"+
				" Email nvarchar(70),Company nvarchar(50),Pwd nvarchar(20),Mobile nvarchar,Pin nvarchar(20));");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home)
		{
			//Toast.makeText(getApplicationContext(), ""+"Click Home", Toast.LENGTH_LONG).show();
			Intent i = new Intent(ApprovedRemark.this, ApproveOrder.class);
			startActivity(i);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	//this is for back button Start
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			//Log.d("Test", "Back button pressed!");
			//Toast.makeText(getApplicationContext(), "Back",Toast.LENGTH_LONG).show();
			Intent i = new Intent(ApprovedRemark.this, ApproveOrder.class);
			startActivity(i);
			finish();
		}
		else if(keyCode == KeyEvent.KEYCODE_HOME)
		{
			//Log.d("Test", "Home button pressed!");
			//Toast.makeText(getApplicationContext(), "Home",Toast.LENGTH_LONG).show();
			Intent i = new Intent(ApprovedRemark.this, ApproveOrder.class);
			startActivity(i);
			finish();
		}
		//return super.onKeyDown(keyCode, event);
		return false;
	}
	//this is for back button End
}