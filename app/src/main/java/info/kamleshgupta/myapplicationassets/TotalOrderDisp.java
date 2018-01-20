package info.kamleshgupta.myapplicationassets;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import info.kamleshgupta.myapplicationassets.adapter.RecyclerviewAdapter;
import info.kamleshgupta.myapplicationassets.model.OrderModel;


public class TotalOrderDisp extends AppCompatActivity {


	clsUtility clsUtility=new clsUtility();


	String BType="";
	//MaterialSearchView searchView;
	Toolbar toolbar=null;
	SQLiteDatabase db;
	TableLayout table_layout;

	String frmDate="";
	String toDate="";
	String Pin="";
	String Url="";
	String UrlPath="";
	String UserId="";

	HashMap<String, String> hmap_Plant=new HashMap<String, String>();
	Spinner spPlant;
	Button btnSearch,btnSave;
	private DatePicker dtpDate;
	private int year;
	private int month;
	private int day;
	String FromDate="";
	String ToDate="";
	static final int DATE_DIALOG_ID = 100;
	static final int DATE_DIALOG_ID2 = 200;

	EditText et_FromDt,et_ToDt;
	TextView txtTotalCrate,txtTotalLiter;
	private Button btnAddOrd;
	String OrdNo="";
	String Status="";
	Handler handler =new Handler();
	private final int FIVE_SECONDS = 2500;
	CheckBox chkDRange;
	private RecyclerviewAdapter recyclerViewAdapter;
	private RecyclerView recyclerView;
	private RecyclerView.LayoutManager mLayoutManager;
	private ArrayList<OrderModel> orderModelList;
	String BCode="";
	public static TotalOrderDisp approveOrder=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_torder_disp);


		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
		final String ShortComp = globalVariable.getShortComp();

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		Intent intent=getIntent();
		BType=globalVariable.getDashHeading();
		Status=globalVariable.getStatus();

		recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

		//Add Search View
		toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle(BType+"    ");
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_24dp);
		toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

		if(getSupportActionBar()!=null)
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}
		String query="";
		if(intent.ACTION_SEARCH.equals(intent.getAction()))
		{
		}
		CreateDatabase();

		btnSearch = (Button) findViewById(R.id.btnSearch);


		et_FromDt=(EditText) findViewById(R.id.et_FromDt);
		et_ToDt=(EditText) findViewById(R.id.et_ToDt);
		txtTotalCrate=(TextView) findViewById(R.id.txtTotalCrate);
		txtTotalLiter=(TextView) findViewById(R.id.txtTotalLiter);
		chkDRange=(CheckBox)findViewById(R.id.chkDate);
		setCurrentDateOnView();

		et_FromDt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);

			}
		});
		et_ToDt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				showDialog(DATE_DIALOG_ID2);

			}
		});

		if (clsUtility.isNetworkAvailable(this)==false)
		{
			clsUtility.msgBox(TotalOrderDisp.this, "No Network Connection", "Internet is not available right now.");
			return;
		}

		Url=clsUtility.SetUrl(getApplicationContext());

		//=openOrCreateDatabase("BM.db", MODE_PRIVATE, null);

		String Name="";
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		table_layout = (TableLayout) findViewById(R.id.tableLayout1);

		BCode=globalVariable.getBranch().toString();

		Url = clsUtility.SetUrl(getApplicationContext());
		UserId=globalVariable.getEmail().toString();

		spPlant= (Spinner) findViewById(R.id.spPlant);
		String strQuery = "ItemGroup.ashx";

		new HttpAsyncTaskPlant().execute(Url+strQuery);

		String LType=globalVariable.getUType();
		String DRange="0";
		String PGroup="";
		strQuery = "TOrderDisp.ashx?UserId="+UserId+"&FromDt="+FromDate+"&ToDate="+ToDate+"&Branch="+BCode+"&LType="+
				LType+"&DRange="+DRange+"&PGroup="+PGroup;



		new HttpAsyncTask().execute(Url+strQuery);

		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Url = clsUtility.SetUrl(getApplicationContext());
				String LType=globalVariable.getUType();
				String DRange="0";
				if(chkDRange.isChecked()==true)
				{
					DRange="1";
					//Toast.makeText(getApplicationContext(),""+DRange,Toast.LENGTH_SHORT).show();
				}
				String PGroup=hmap_Plant.get(spPlant.getSelectedItem());
				String strQuery ="TOrderDisp.ashx?UserId="+UserId+"&FromDt="+FromDate+"&ToDate="+ToDate+"&Branch="+BCode+
						"&LType="+LType+"&DRange="+DRange+"&PGroup="+PGroup;
				new HttpAsyncTask().execute(Url+strQuery);

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


	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(TotalOrderDisp.this);
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
				clsUtility.msgBox(TotalOrderDisp.this, "Message", "No data received.");

			AddData(result);
		}
	}


	public void AddData(String strData)
	{

		try
		{
			String response=strData;
			JSONArray json=new JSONArray(response);
			//Toast.makeText(getApplicationContext(), response+"",Toast.LENGTH_LONG).show();
			String[] ColArray=new String[]{"Plant"};
			//String[] ColArray=new String[]{"Plant","OrdDate","Liter","Value"};
			//String[] ColArrayP=new String[]{"Distributor","OrdDate","Crate","Liter","Value"};
			if(response.contains("Abc123ErrorXyz123"))
			{
				JSONObject json1 = (JSONObject) json.get(0);
				//Toast.makeText(getApplicationContext(), ""+json1.getString("Abc123ErrorXyz123"), Toast.LENGTH_LONG).show();
				clsUtility.msgBox(TotalOrderDisp.this, "Info ", json1.getString("Abc123ErrorXyz123"));
				recyclerView.setVisibility(View.GONE);
				return;
			}

			mLayoutManager = new LinearLayoutManager(this);
			recyclerView.setLayoutManager(mLayoutManager);
			orderModelList = new ArrayList<>();
			Double Crate=0.0;
			Double Liters=0.0;
			if(json.length()>0)
			{
				recyclerView.setVisibility(View.VISIBLE);
				//Toast.makeText(getApplicationContext(), "1",Toast.LENGTH_LONG).show();
				//Toast.makeText(getApplicationContext(), response+"2",Toast.LENGTH_LONG).show();
				for(int i=0;i<json.length();i++)
				{
					JSONObject json1 = (JSONObject) json.get(i);
					OrderModel o1 = new OrderModel("Party - "+json1.getString("Distributor"),"Order Date:  "+json1.getString("OrdDate"),"Crate:  "+json1.getString("Crate"),"Liters:  "+json1.getString("Liter"),"Approx V.:  "+json1.getString("Value"),"Plant:  "+json1.getString("Plant"),json1.getString("IsApp")+"",json1.getString("Order_No")+"",Status);
					orderModelList.add(o1);
					Crate=Crate+Double.parseDouble(json1.getString("Crate"));
					Liters=Liters+Double.parseDouble(json1.getString("Liter"));
				}
				recyclerViewAdapter = new RecyclerviewAdapter(this,orderModelList);
				recyclerView.setAdapter(recyclerViewAdapter);
				DecimalFormat df = new DecimalFormat("#.#");
				txtTotalCrate.setText("Total Crates : "+Crate);
				txtTotalLiter.setText("Total Liters : "+ Double.valueOf(df.format(Liters)));
			}
			else
			{
				Toast.makeText(getApplicationContext(), "No Record Found ! ",Toast.LENGTH_LONG).show();
				return;
			}


		}
		catch(Exception ex)
		{
			clsUtility.msgBox(TotalOrderDisp.this, "Error ", "Error : "+ex.toString());
		}
	}




	public void CreateDatabase()
	{

		db=openOrCreateDatabase("BM2.db", MODE_PRIVATE, null);
		//db.execSQL("DROP TABLE IF EXISTS Order");
		db.execSQL("CREATE TABLE IF NOT EXISTS OrderTrn(UserId nvarchar(50),SNo nvarchar(10),PName nvarchar(100),MRP nvarchar(70),Packing nvarchar(50),Company nvarchar(50),Expiry nvarchar(20),Rate nvarchar(20),Qty nvarchar(20),TotalRate nvarchar(20));");
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home)
		{
			//Toast.makeText(getApplicationContext(), "CLick Home ",Toast.LENGTH_LONG).show();
			Intent i = new Intent(getApplicationContext(), DashBoard.class);
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
			Intent i = new Intent(getApplicationContext(), DashBoard.class);
			startActivity(i);
			finish();
		}
		else if(keyCode == KeyEvent.KEYCODE_HOME)
		{
			//Log.d("Test", "Home button pressed!");
			Intent i = new Intent(getApplicationContext(), DashBoard.class);
			startActivity(i);
			finish();
		}
		//return super.onKeyDown(keyCode, event);
		return false;
	}
	//this is for back button End

	private DatePickerDialog.OnDateSetListener datePickerListener=new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
			// TODO Auto-generated method stub
			year = selectedYear;

			month = selectedMonth;

			day = selectedDay;

			// set selected date into Text View
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			et_FromDt.setText(new StringBuilder()
					.append(day).append("/").append(month + 1).append("/").append(year).append(""));
			FromDate=""+new StringBuilder().append(month + 1).append("/").append(day).append("/").append(year).append("");
		}
	};

	private DatePickerDialog.OnDateSetListener datePickerListener2 = new DatePickerDialog.OnDateSetListener() {
		// when dialog box is closed, below method will be called.

		public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {

			year = selectedYear;

			month = selectedMonth;

			day = selectedDay;

			// set selected date into Text View
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			et_ToDt.setText(new StringBuilder()
					.append(day).append("/").append(month + 1).append("/").append(year).append(""));
			ToDate=""+new StringBuilder().append(month + 1).append("/").append(day).append("/").append(year).append("");
		}
	};
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {

			case DATE_DIALOG_ID:
				// set date picker as current date
				return new DatePickerDialog(this, datePickerListener, year, month,day);
			case DATE_DIALOG_ID2:
				// set date picker as current date
				return new DatePickerDialog(this, datePickerListener2, year, month,day);

		}
		return null;
	}

	// display current date
	public void setCurrentDateOnView() {



		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		// set current date into textview
		et_FromDt.setText(new StringBuilder()
				// Month is 0 based, just add 1
				.append(day).append("/").append(month + 1).append("/").append(year).append(""));
		et_ToDt.setText(new StringBuilder()
				// Month is 0 based, just add 1
				.append(day).append("/").append(month + 1).append("/").append(year).append(""));

		FromDate=""+new StringBuilder().append(month + 1).append("/").append(day).append("/").append(year).append("");
		ToDate=""+new StringBuilder().append(month + 1).append("/").append(day).append("/").append(year).append("");
	}

	private class HttpAsyncTaskPlant extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(TotalOrderDisp.this);
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
				clsUtility.msgBox(TotalOrderDisp.this, "Message", "No data received.");

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
				clsUtility.msgBox(TotalOrderDisp.this, "Info ", json1.getString("Abc123ErrorXyz123"));
				return;
			}

			List<String> listPlant=new ArrayList<String>();
			ArrayAdapter dataAdapter=null;
			if(json.length()>0)
			{
				for(int i=0;i<json.length();i++)
				{
					JSONObject json1 = (JSONObject) json.get(i);
					listPlant.add(json1.getString("Name"));
					hmap_Plant.put(json1.getString("Name"), json1.getString("Code"));
					dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, listPlant){
						public View getView(int position, View convertView, ViewGroup parent) {
							View v = super.getView(position, convertView, parent);
							((TextView) v).setTextSize(13);
							((TextView) v).setTextColor(Color.BLACK);
							return v;
						}
					};
				}
				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spPlant.setAdapter(dataAdapter);

			/*	if(BCode.equalsIgnoreCase("02"))
				{
					spPlant.setSelection(0);
				}
				else if(BCode.equalsIgnoreCase("03"))
				{
					spPlant.setSelection(3);
				}
				else if(BCode.equalsIgnoreCase("04"))
				{
					spPlant.setSelection(2);
				}
				else if(BCode.equalsIgnoreCase("05"))
				{
					spPlant.setSelection(1);
				}*/

			}
			else
			{
				Toast.makeText(getApplicationContext(), "No Record Found ! ",Toast.LENGTH_LONG).show();
				return;
			}
		}
		catch(Exception ex)
		{
			clsUtility.msgBox(TotalOrderDisp.this, "Error ", "Error : "+ex.toString());
		}
	}


}