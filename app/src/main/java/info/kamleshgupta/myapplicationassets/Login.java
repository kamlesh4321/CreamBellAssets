package info.kamleshgupta.myapplicationassets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


public class Login extends AppCompatActivity {


	clsUtility clsUtility=new clsUtility();
	SQLiteDatabase db;
    String Url="";
	String BType="";
	TextView textRegister;
	//MaterialSearchView searchView;
	Toolbar toolbar=null;
	private Button btn_signIn;
	private EditText etPassword_signUp,etUserId_signUp;
	private Animation animation;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		CreateDatabase();

		Intent intent=getIntent();
		BType=intent.getStringExtra("BType");
		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
		//final String Company  = globalVariable.getCompany();
		final String ShortComp = globalVariable.getShortComp();

		toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("LOGIN  ");

		toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_24dp);
		//searchView=(MaterialSearchView)findViewById(R.id.search_view);
		if(getSupportActionBar()!=null)
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}
		String query="";

		btn_signIn = (Button) findViewById(R.id.btn_signIN);
		animation = AnimationUtils.loadAnimation(Login.this,R.anim.blink);
		((Button) findViewById(R.id.btn_signIN)).setAnimation(animation);
		animation.start();
		etPassword_signUp = (EditText) findViewById(R.id.et_password);
		etUserId_signUp = (EditText) findViewById(R.id.et_UserId);
		textRegister = (TextView) findViewById(R.id.textRegister);

		textRegister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getApplicationContext(),Registration.class);
				startActivity(i);
			}
		});

//Get User Id and Password From Db Starr
		db=openOrCreateDatabase("CBO.db", MODE_PRIVATE, null);
		Cursor c2=null;
		c2=db.rawQuery("select UserId,Pwd from Register", null);
		int count=c2.getCount();
		if(count>0)
		{
			c2.moveToFirst();
			for(int i=0;i<count;i++)
			{
				etPassword_signUp.setText(c2.getString(c2.getColumnIndex("Pwd")));
				etUserId_signUp.setText(c2.getString(c2.getColumnIndex("UserId")));
			}
			c2.moveToNext();
		}
		db.close();
//Get User Id and Password From Db End
		btn_signIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent i= new Intent(getApplicationContext(), DashBoard.class);
				startActivity(i);
				return;
/*
				if(!etUserId_signUp.getText().toString().trim().equalsIgnoreCase("") &&
						!etPassword_signUp.getText().toString().trim().equalsIgnoreCase("")) {

					Url = clsUtility.SetUrl(getApplicationContext());
					String strQuery = "CHLogin.ashx?UserId=" + etUserId_signUp.getText() +
							"&Pass=" + etPassword_signUp.getText();
					//Log.d("Query=",Que);

					if (clsUtility.isNetworkAvailable(getApplicationContext())==false)
					{
					//	clsUtility.msgBox(getBaseContext(), "No Network Connection", "Internet is not available right now.");
						Toast.makeText(getApplicationContext(), "No Network Connection "+"Internet is not available right now.", Toast.LENGTH_LONG).show();
						return;
					}
					//Toast.makeText(getApplicationContext(), "" + Url + strQuery, Toast.LENGTH_LONG).show();
					new HttpAsyncTask().execute(Url + strQuery);


				}
				else {

					if (etUserId_signUp.getText().toString().trim().equalsIgnoreCase("")) {
						etUserId_signUp.setError(getResources().getString(R.string.UserId_Enter), getResources().getDrawable(R.drawable.error_24dp));
						etUserId_signUp.requestFocus();
					}
					else if (etPassword_signUp.getText().toString().trim().equalsIgnoreCase("")) {
						etPassword_signUp.setError(getResources().getString(R.string.Password_Enter), getResources().getDrawable(R.drawable.error_24dp));
						etPassword_signUp.requestFocus();
					}
				}
				*/
			}
		});

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

			pDialog = new ProgressDialog(Login.this);
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
				db=openOrCreateDatabase("CBO.db", MODE_PRIVATE, null);
				db.execSQL("delete from Register");
				db.execSQL("insert into Register(UserId,Pwd) values('"+etUserId_signUp.getText()+"','"+etPassword_signUp.getText()+"');");
				db.close();
				//Toast.makeText(getApplicationContext(), ""+json1.getString("UType"), Toast.LENGTH_LONG).show();
				final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
				globalVariable.setEmail(etUserId_signUp.getText().toString());
				globalVariable.setUType(json1.getString("UType"));
				globalVariable.setBType("");
				globalVariable.setBranch("0");
				Intent i;
				if(json1.getString("UType").toString().equalsIgnoreCase("sh")) {
					 i = new Intent(getApplicationContext(), DashBoard.class);
				}
				else {
					 i = new Intent(getApplicationContext(), Branch.class);
				}
					finish();
					startActivity(i);
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
		db=openOrCreateDatabase("CBO.db", MODE_PRIVATE, null);
		//db.execSQL("DROP TABLE IF EXISTS Register");
		db.execSQL("CREATE TABLE IF NOT EXISTS Register(UserId nvarchar(50),Pwd nvarchar(50));");
		db.close();
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