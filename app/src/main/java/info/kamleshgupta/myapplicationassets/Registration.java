package info.kamleshgupta.myapplicationassets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


public class Registration extends AppCompatActivity {
    

	clsUtility clsUtility=new clsUtility();

    String Url="";
	String BType="";
	//MaterialSearchView searchView;
	Toolbar toolbar=null;
	SQLiteDatabase db;
	private Button btn_signUser;
	private Animation animation;
	private EditText etUserName_signIn,etPassword_signIn,etFirstName_signUp,
			etEmail_signUp,etUserId_signUp,etPassword_signUp,etReEnterPass_signUp,etContact_signUp,etCity_signUp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
		CreateDatabase();
		//BType=intent.getStringExtra("BType");
		final String ShortComp = globalVariable.getShortComp();


		toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Registration  ");

		toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_24dp);
		//searchView=(MaterialSearchView)findViewById(R.id.search_view);
		if(getSupportActionBar()!=null)
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}
		String query="";

		btn_signUser = (Button) findViewById(R.id.btn_signUser);
		animation = AnimationUtils.loadAnimation(Registration.this,R.anim.blink);
		((Button) findViewById(R.id.btn_signUser)).setAnimation(animation);

		animation.start();

		etFirstName_signUp= (EditText) findViewById(R.id.et_firstName);

		etEmail_signUp = (EditText) findViewById(R.id.et_Email);
		etPassword_signUp = (EditText) findViewById(R.id.et_password);
		etReEnterPass_signUp = (EditText) findViewById(R.id.et_rePassword);
		etContact_signUp = (EditText) findViewById(R.id.et_Contact);
		etUserId_signUp = (EditText) findViewById(R.id.et_UserId);
		etCity_signUp= (EditText) findViewById(R.id.et_City);

		btn_signUser.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (clsUtility.isNetworkAvailable(getApplicationContext())==false)
				{
					//	clsUtility.msgBox(getBaseContext(), "No Network Connection", "Internet is not available right now.");
					Toast.makeText(getApplicationContext(), "No Network Connection "+"Internet is not available right now.", Toast.LENGTH_LONG).show();
					return;
				}
				if(!etFirstName_signUp.getText().toString().trim().equalsIgnoreCase("")&&
						!etUserId_signUp.getText().toString().trim().equalsIgnoreCase("") &&
						!etEmail_signUp.getText().toString().trim().equalsIgnoreCase("")&&
						!etPassword_signUp.getText().toString().trim().equalsIgnoreCase("")&&
						!etReEnterPass_signUp.getText().toString().trim().equalsIgnoreCase("")&&
						!etContact_signUp.getText().toString().trim().equalsIgnoreCase("")&&
						etPassword_signUp.getText().toString().trim().equals(etReEnterPass_signUp.getText().toString().trim())) {

					Url = clsUtility.SetUrl(getApplicationContext());
					String strQuery = "CHReg.ashx?UserId=" + etUserId_signUp.getText() + "&FName=" + etFirstName_signUp.getText() +
							"&LName=&Email=" + etEmail_signUp.getText() +"&City="+etCity_signUp.getText()+
							"&Pass=" + etPassword_signUp.getText() + "&Mobile=" + etContact_signUp.getText();
					//Log.d("Query=",Que);
					//Toast.makeText(getApplicationContext(), "" + Url + strQuery, Toast.LENGTH_LONG).show();
					inserData();
					new HttpAsyncTask().execute(Url + strQuery);
				}
				else {

					if (etFirstName_signUp.getText().toString().trim().equalsIgnoreCase("")) {
						etFirstName_signUp.setHintTextColor(Color.RED);
					}
					if (etEmail_signUp.getText().toString().trim().equalsIgnoreCase("")) {
						etEmail_signUp.setHintTextColor(Color.RED);
					}
					if (etPassword_signUp.getText().toString().trim().equalsIgnoreCase("")) {
						etPassword_signUp.setHintTextColor(Color.RED);
					}
					if (etReEnterPass_signUp.getText().toString().trim().equalsIgnoreCase("")) {
						etReEnterPass_signUp.setHintTextColor(Color.RED);
					}
					if (etContact_signUp.getText().toString().trim().equalsIgnoreCase("")) {
						etContact_signUp.setHintTextColor(Color.RED);
					}
					if (etUserId_signUp.getText().toString().trim().equalsIgnoreCase("")) {
						etUserId_signUp.setHintTextColor(Color.RED);
					}
					if (!etFirstName_signUp.getText().toString().trim().equalsIgnoreCase("") &&

							!etUserId_signUp.getText().toString().trim().equalsIgnoreCase("") &&

							!etEmail_signUp.getText().toString().trim().equalsIgnoreCase("") &&
							!etPassword_signUp.getText().toString().trim().equalsIgnoreCase("") &&
							!etReEnterPass_signUp.getText().toString().trim().equalsIgnoreCase("") &&
							!etContact_signUp.getText().toString().trim().equalsIgnoreCase("") &&
							!etPassword_signUp.getText().toString().trim().equals(etReEnterPass_signUp.getText().toString().trim())) {
						etReEnterPass_signUp.setError(getResources().getString(R.string.password_not_match), getResources().getDrawable(R.drawable.error_24dp));
						etReEnterPass_signUp.requestFocus();
					}
				}
			}
		});

	}


	private void inserData()
	{
		db.execSQL("delete from Register");
		String qur="insert into Register(UserId,FName,Email,Mobile,Pwd,Pin) values('"+etUserId_signUp.getText()+"',"+
				" '"+etFirstName_signUp.getText()+"','"+etEmail_signUp.getText()+"',"+
				" '"+etContact_signUp.getText()+"','"+etPassword_signUp.getText()+"','0');";

		db.execSQL(qur);
		db.close();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {

		return true;
    }

	public void function_lstvVideo_AsyncTask()
	{


	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(Registration.this);
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
				//clsUtility.msgBox(getApplicationContext(), "Message", "No data received.");
			Toast.makeText(getApplicationContext(), "No data received.", Toast.LENGTH_LONG).show();


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
				Intent i = new Intent(getBaseContext(), Login.class);
				finish();
				startActivity(i);
			}
		}
		catch(Exception ex)
		{
			//clsUtility.msgBox(getApplicationContext(), "Error", "Error : "+ex.toString());
			Toast.makeText(getApplicationContext(), "Error - "+ex.toString(), Toast.LENGTH_LONG).show();
		}
		//BindGrid();
	}
	public void CreateDatabase()
	{
		db=openOrCreateDatabase("BM.db", MODE_PRIVATE, null);
		//db.execSQL("DROP TABLE IF EXISTS AddToCart");
		db.execSQL("CREATE TABLE IF NOT EXISTS Register(UserId nvarchar(50),FName nvarchar(50),LName nvarchar(50),"+
				" Email nvarchar(70),Company nvarchar(50),Pwd nvarchar(20),Mobile nvarchar,Pin nvarchar(20));");
		//Toast.makeText(getApplicationContext(), "DB", Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}