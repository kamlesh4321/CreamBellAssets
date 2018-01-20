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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class OrderTrn extends AppCompatActivity {
    

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
	String BCode="";

	Paginator p=new Paginator();
	private int totalPages=Paginator.TOTAL_NUM_ITEMS/Paginator.ITEMS_PER_PAGE;
	private int currentPage=1;


	Button btnSearch,btnSave,btnPrev,btnNext;
	private EditText etProduct;
	private EditText etCompany;
	private Button btnAddOrd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_trn);

		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
		final String ShortComp = globalVariable.getShortComp();

		Intent intent=getIntent();

		UserId=globalVariable.getEmail().toString();
		BType=globalVariable.getDashHeading();
		BCode=globalVariable.getBranch().toString();
		//Toast.makeText(getApplicationContext(), "UserId"+UserId, Toast.LENGTH_LONG).show();
		//Add Search View
		toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle(BType+"    ");

		toolbar.setTitleTextColor(Color.parseColor("#000000"));

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
		btnSave = (Button) findViewById(R.id.btnSave);
		btnAddOrd = (Button) findViewById(R.id.btnAddOrd);
		btnPrev=(Button) findViewById(R.id.btnPrev);
		btnNext=(Button) findViewById(R.id.btnNext);
		btnPrev.setEnabled(false);

		etProduct= (EditText) findViewById(R.id.et_Product);
		etCompany= (EditText) findViewById(R.id.et_Company);

		if (clsUtility.isNetworkAvailable(this)==false)
		{
			clsUtility.msgBox(OrderTrn.this, "No Network Connection", "Internet is not available right now.");
			return;
		}

		Url=clsUtility.SetUrl(getApplicationContext());

		String Name="";


		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		table_layout = (TableLayout) findViewById(R.id.tableLayout1);

		Url = clsUtility.SetUrl(getApplicationContext());
		String Product="";
		String strQuery = "OrderItem.ashx?Nor=700&PageNo=1&LocCode="+BCode+"&Product="+Product;


		new HttpAsyncTask().execute(Url+strQuery);
		//Toast.makeText(getApplicationContext(), ""+Url+strQuery, Toast.LENGTH_LONG).show();
		//Toast.makeText(getApplicationContext(), ""+Url+strQuery, Toast.LENGTH_LONG).show();
		//Toast.makeText(getApplicationContext(), ""+Url+strQuery, Toast.LENGTH_LONG).show();

		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Url = clsUtility.SetUrl(getApplicationContext());
				String Product="";
				String strQuery = "OrderItem.ashx?Nor=700&PageNo=1&LocCode="+BCode+"&Product="+Product;
				new HttpAsyncTask().execute(Url+strQuery);

			}
		});

		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Url=clsUtility.SetUrl(getApplicationContext());
				//GetOrder Number Start
				new HttpAsyncTaskGetOrd().execute(Url+"GetOrderNo.ashx");
			}
		});

		btnNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				currentPage+=1;
				Url = clsUtility.SetUrl(getApplicationContext());
				String strQuery = "MedicineDet.ashx?Nor=100&PageNo="+currentPage;
				new HttpAsyncTask().execute(Url+strQuery);
				//gv.setAdapter(new ArrayAdapter<String>(LotStatement_2.this,android.R.layout.simple_list_item_1,p.generatePage(currentPage)));
				toggleButtons();
			}
		});
		btnPrev.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				currentPage-=1;
				Url = clsUtility.SetUrl(getApplicationContext());
				String strQuery = "MedicineDet.ashx?Nor=100&PageNo="+currentPage;
				new HttpAsyncTask().execute(Url+strQuery);
				toggleButtons();
			}
		});

		btnAddOrd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				db=openOrCreateDatabase("CBO.db", MODE_PRIVATE, null);
				int Flag=0;

				for(int i=1;i<table_layout.getChildCount()-1;i++)
				{
					TableRow row = (TableRow)table_layout.getChildAt(i);
					TextView SNo = (TextView)row.getChildAt(0);
					TextView PName = (TextView)row.getChildAt(1);
					TextView MRP = (TextView)row.getChildAt(2);
					TextView Packing = (TextView)row.getChildAt(3);
					TextView Company = (TextView)row.getChildAt(4);

					TextView Rate = (TextView)row.getChildAt(5);
					EditText Qty = (EditText)row.getChildAt(6);
					TextView lblTotalRate = (TextView)findViewById(R.id.lblTotalRate);

					if(Qty.getText().toString().trim().length()>0) {
						db.execSQL("insert into OrderTrn(SNo,PName,MRP,Packing,Company,Rate,Qty,TotalRate) " +
								" values('" + SNo.getText() + "','" + PName.getText() + "','" + MRP.getText() + "','" + Packing.getText() + "'," +
								" '" + Company.getText() + "','" + Rate.getText() + "','" + Qty.getText() + "','"+lblTotalRate.getText()+"');");
						Flag=1;
					}
					//Toast.makeText(getApplicationContext(), " Id "+TotalRate,Toast.LENGTH_LONG).show();
				}
				if(Flag!=0) {
					Toast.makeText(getApplicationContext(), "Data Added Successfully !", Toast.LENGTH_LONG).show();
				}


			}
		});

	}
	private void toggleButtons()
	{
		if(currentPage==13)
		{
			btnNext.setEnabled(false);
			btnPrev.setEnabled(true);
		}else if(currentPage==1)
		{
			btnPrev.setEnabled(false);
			btnNext.setEnabled(true);
		}else if(currentPage>=2 && currentPage<=12)
		{
			btnNext.setEnabled(true);
			btnPrev.setEnabled(true);
		}
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

			pDialog = new ProgressDialog(OrderTrn.this);
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
				clsUtility.msgBox(OrderTrn.this, "Message", "No data received.");

			AddData(result);
		}
	}

	private class HttpAsyncTaskSave extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(OrderTrn.this);
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
				clsUtility.msgBox(OrderTrn.this, "Message", "No data received.");

			String response=result;
			JSONArray json= null;
			try {
				json = new JSONArray(response);

				if(response.contains("Abc123ErrorXyz123"))
				{
					JSONObject json1 = (JSONObject) json.get(0);
					//Toast.makeText(getApplicationContext(), ""+json1.getString("Abc123ErrorXyz123"), Toast.LENGTH_LONG).show();
					clsUtility.msgBox(OrderTrn.this, "Error ", json1.getString("Abc123ErrorXyz123"));
					return;
				}
				/*if(response.contains("Abc123SuccessXyz123"))
				{
					JSONObject json1 = (JSONObject) json.get(0);
					clsUtility.msgBox(MedicineDetails.this, "Success ", json1.getString("Abc123SuccessXyz123"));
					return;
				}
*/
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}


	private class HttpAsyncTaskGetOrd extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(OrderTrn.this);
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
				clsUtility.msgBox(OrderTrn.this, "Message", "No data received.");

			String response=result;
			int OrderNo=0;
			try {
				JSONArray json=new JSONArray(response);
				if(response.contains("Abc123ErrorXyz123"))
				{
					JSONObject json1=  (JSONObject) json.get(0);
					Toast.makeText(getApplicationContext(), ""+json1.getString("Abc123ErrorXyz123"), Toast.LENGTH_LONG).show();
					return;
				}
				if(json.length()>0)
				{
					JSONObject json1= (JSONObject) json.get(0);
					OrderNo=Integer.parseInt(json1.getString("OrderNo"));
					OrderNo++;
				}
				//Order Save Start
				db=openOrCreateDatabase("CBO.db", MODE_PRIVATE, null);
				String query="select SNo,PName,MRP,Packing,Company,Rate,Qty,TotalRate from OrderTrn ";
				Cursor c=db.rawQuery(query, null);
				int count=c.getCount();
				if(count>0)
				{
					c.moveToFirst();
					for(int j=0;j<count;j++)
					{
						//Toast.makeText(getApplicationContext(), "No Record Found !"+c.getString(c.getColumnIndex("SNo")),Toast.LENGTH_LONG).show();
						String strQuery = "SaveOrd.ashx?UserId="+UserId+"&SNo="+c.getString(c.getColumnIndex("SNo"))+"&OrdNo="+OrderNo+"";
						strQuery=strQuery+"&PName="+c.getString(c.getColumnIndex("PName"))+"&MRP="+c.getString(c.getColumnIndex("MRP"))+"";
						strQuery=strQuery+"&Packing="+c.getString(c.getColumnIndex("Packing"))+"&Company="+c.getString(c.getColumnIndex("Company"))+"";
						strQuery=strQuery+"&Expiry=&Rate="+c.getString(c.getColumnIndex("Rate"))+"";
						strQuery=strQuery+"&Qty="+c.getString(c.getColumnIndex("Qty"))+"&TRate="+c.getString(c.getColumnIndex("TotalRate"))+"";
						new HttpAsyncTaskSave().execute(Url+strQuery);
						//Toast.makeText(getApplicationContext(), "No Record Found !"+Url+strQuery,Toast.LENGTH_LONG).show();
						//Log.d("Query=",Url+strQuery);
						db.execSQL("delete from  OrderTrn where SNo='"+c.getString(c.getColumnIndex("SNo"))+"' ");

						c.moveToNext();
					}
					clsUtility.msgBox(OrderTrn.this, "Success ", "Order Saved Successfully !");
				}
				else
				{
					Toast.makeText(getApplicationContext(), "No Record Found !",Toast.LENGTH_LONG).show();
				}
				//Order Save End
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void AddData(String strData)
	{

		try
		{
			String response=strData;
			JSONArray json=new JSONArray(response);
			table_layout.removeAllViews();
			//Toast.makeText(getApplicationContext(), response+"",Toast.LENGTH_LONG).show();
			String[] ColArray=new String[]{"ItemName","MRP","Value","PackQty","CratePackQty","Unit","Liters","Crate"};
			if(response.contains("Abc123ErrorXyz123"))
			{
				JSONObject json1 = (JSONObject) json.get(0);
				//Toast.makeText(getApplicationContext(), ""+json1.getString("Abc123ErrorXyz123"), Toast.LENGTH_LONG).show();
				clsUtility.msgBox(OrderTrn.this, "Info ", json1.getString("Abc123ErrorXyz123"));
				return;
			}
			if(json.length()>0)
			{
				//Toast.makeText(getApplicationContext(), "1",Toast.LENGTH_LONG).show();
				//Toast.makeText(getApplicationContext(), response+"2",Toast.LENGTH_LONG).show();
				//Header Of Grid Start
				TableRow rowheader = new TableRow(OrderTrn.this);
				for(int j=0;j<ColArray.length;j++) {

					rowheader.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
					TextView tvHead = new TextView(OrderTrn.this);
					tvHead.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
					tvHead.setBackgroundResource(R.drawable.cell_shapeheader);
					tvHead.setTextColor(Color.parseColor("#ffffff"));

					tvHead.setGravity(Gravity.CENTER);
					tvHead.setTextSize(14);
					tvHead.setPadding(0, 10, 0, 10);
					tvHead.setText(ColArray[j]+"");
					rowheader.addView(tvHead);

				}
				table_layout.addView(rowheader);
				for(int i=0;i<json.length();i++)
				{

					TableRow row = new TableRow(OrderTrn.this);
					row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
					// inner for loop
					row.setId(i);
					JSONObject json1 = (JSONObject) json.get(i);
					//Color[i]=json1.getString("Color");



					for(int j=0;j<ColArray.length;j++)
					{
						//Toast.makeText(getApplicationContext(), Query+"3",Toast.LENGTH_LONG).show();
						TextView tv = new TextView(OrderTrn.this);
						final EditText et_text = new EditText(OrderTrn.this);
						et_text.setInputType(InputType.TYPE_CLASS_NUMBER);
						et_text.setId(Integer.valueOf(i));
						//et_text.setHint("Qty");
						tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
						et_text.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
						/*if(i==0) {
							tv.setBackgroundResource(R.drawable.cell_shapeheader);
							tv.setTextColor(Color.parseColor("#ffffff"));
						}
						else*/
						if(i%2==0) {
							tv.setBackgroundResource(R.drawable.cell_shape2);
							tv.setTextColor(Color.parseColor("#000000"));
						}
						else
						{
							tv.setBackgroundResource(R.drawable.cell_shape);
							tv.setTextColor(Color.parseColor("#000000"));
						}
						tv.setGravity(Gravity.CENTER);
						tv.setTextSize(14);
						tv.setPadding(0, 10, 0, 10);

						et_text.setGravity(Gravity.CENTER);
						et_text.setTextSize(14);
						et_text.setPadding(0, 10, 0, 10);

						String Data= json1.getString(ColArray[j]+"");
						if(j==0 && i==0)
						{
							//Toast.makeText(getApplicationContext(), Data,Toast.LENGTH_LONG).show();
							//Data="SNo";
						}
						if(j==7) {

							if(i%2==0) {
								et_text.setBackgroundResource(R.drawable.edittext);
								et_text.setTextColor(Color.parseColor("#000000"));
							}
							else
							{
								et_text.setBackgroundResource(R.drawable.edittext);
								et_text.setTextColor(Color.parseColor("#000000"));
							}
							et_text.setImeOptions(EditorInfo.IME_ACTION_DONE);
							et_text.setText(Data);
							row.addView(et_text);
						}
						else {
							tv.setText(Data);
							row.addView(tv);
						}

						et_text.addTextChangedListener(new TextWatcher() {

							public void onTextChanged(CharSequence s, int start, int before,
													  int count) {
								if(!s.equals("") )
								{
									//do your work here }
									TableRow row = (TableRow)table_layout.getChildAt(et_text.getId()+1);

									TextView firstTextView = (TextView)row.getChildAt(1);
									TextView Rate = (TextView)row.getChildAt(5);
									Double TotalRate=Double.parseDouble(Rate.getText().toString().equals("") ? "0" :Rate.getText().toString())*Integer.parseInt(s.toString().equals("") ? "0" :s.toString());
									//Double.parseDouble(Rate.getText().toString())*Integer.parseInt(s.toString());
									TextView lblTotalRate=(TextView)findViewById(R.id.lblTotalRate);
									Double lblTotRate=Double.parseDouble(lblTotalRate.getText().toString());
									lblTotalRate.setText((lblTotRate+TotalRate)+"");
									//Toast.makeText(getApplicationContext(), " Id "+TotalRate,Toast.LENGTH_LONG).show();
								}
							}

							public void beforeTextChanged(CharSequence s, int start, int count,
														  int after) {

							}

							public void afterTextChanged(Editable s) {


							}
						});

					}
					table_layout.addView(row);


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
			clsUtility.msgBox(OrderTrn.this, "Error ", "Error : "+ex.toString());
		}
	}

	public void CreateDatabase()
	{

		db=openOrCreateDatabase("CBO.db", MODE_PRIVATE, null);
		//db.execSQL("DROP TABLE IF EXISTS Order");
		db.execSQL("CREATE TABLE IF NOT EXISTS OrderTrn(UserId nvarchar(50),SNo nvarchar(10),PName nvarchar(100),MRP nvarchar(70),Packing nvarchar(50),Company nvarchar(50),Expiry nvarchar(20),Rate nvarchar(20),Qty nvarchar(20),TotalRate nvarchar(20));");
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