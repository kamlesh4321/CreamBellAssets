package info.kamleshgupta.myapplicationassets;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import android.widget.AdapterView.OnItemSelectedListener;

public class Outlet extends AppCompatActivity {

	// 	Activity request codes
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	private Uri fileUri; // file url to store image/video
	int serverResponseCode = 0;
	ProgressDialog dialog = null;
	String upLoadServerUri = null;
	String UrlData="";
	// directory name to store captured images and videos
	private static final String IMAGE_DIRECTORY_NAME = "Hello_Camera";

	clsUtility clsUtility = new clsUtility();
	String query = "", Url = "", BType = "";
	//MaterialSearchView searchView;
	Toolbar toolbar = null;
	SQLiteDatabase db;

	HashMap<String, String> hmap_Area = new HashMap<String, String>();
	HashMap<String, String> hmap_Town = new HashMap<String, String>();
	HashMap<String, String> hmap_Area1 = new HashMap<String, String>();
	HashMap<String, String> hmap_Town1 = new HashMap<String, String>();
	HashMap<String, String> hmap_Party = new HashMap<String, String>();
	HashMap<String, String> hmap_Party1 = new HashMap<String, String>();
	List<String> listArea = new ArrayList<String>();
	List<String> listTown = new ArrayList<String>();
	List<String> listArea1 = new ArrayList<String>();
	List<String> listTown1 = new ArrayList<String>();
	List<String> listParty = new ArrayList<String>();
	List<String> listParty1 = new ArrayList<String>();

	private Spinner spArea, spArea1, spTown, spTown1, sp_PartyName, sp_PartyName1;
	private EditText et_Address, et_Address2, et_Pincode,et_Pincode2, et_Owner, et_Owner2, et_Contact, et_Contact2;
	private ImageView image1;
	private Button btn_signUser, btn_TakePhoto;
	private Animation animation;

	String AreaCode="",AreaCode2="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.outlet);
		CreateDatabase();
		init();

		Url = clsUtility.SetUrl(getApplicationContext());
		String strQuery = "BindDRP.ashx?Type=area";
		new HttpAsyncTaskArea().execute(Url + strQuery);


		btn_signUser.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (clsUtility.isNetworkAvailable(getApplicationContext()) == false) {
					Toast.makeText(getApplicationContext(), "No Network Connection " + "Internet is not available right now.", Toast.LENGTH_LONG).show();
					return;
				}

				if(spArea1.getSelectedItem().equals(".") || spArea1.getSelectedItem().equals("")) {
					clsUtility.msgBox(Outlet.this,"Info","Please Select Area !");
					return;
				}
				if(spTown1.getSelectedItem().equals(".") || spTown1.getSelectedItem().equals("")) {
					clsUtility.msgBox(Outlet.this,"Info","Please Select Town !");
					return;
				}
				if(sp_PartyName1.getSelectedItem().equals(".") || sp_PartyName1.getSelectedItem().equals("")) {
					clsUtility.msgBox(Outlet.this,"Info","Please Select Party !");
					return;
				}

				db = openOrCreateDatabase("CBAS.db", MODE_PRIVATE, null);
				db.execSQL("delete from Outletmst");
				db.execSQL("insert into Outletmst(ArCode,Town,Party,Address,PinCode,Owner,ContactNo,FilePath)values('"+hmap_Area.get(spArea.getSelectedItem())+"',"+
						" '"+ hmap_Town.get(spTown.getSelectedItem()) +"','"+hmap_Party.get(sp_PartyName.getSelectedItem())+"',"+
				" '"+et_Address2.getText()+"','"+et_Pincode2.getText()+"','"+et_Owner2.getText()+"','"+et_Contact2.getText()+"','"+UrlData+"');");
				//Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
				db.close();

				image1.setImageResource(R.drawable.white);
				spArea.setSelection(0);
				spArea1.setSelection(0);
				spTown.setSelection(0);
				spTown1.setSelection(0);
				sp_PartyName.setSelection(0);
				sp_PartyName1.setSelection(0);

				et_Address2.setText("");
				et_Address.setText("");
				et_Pincode.setText("");
				et_Pincode2.setText("");
				et_Owner.setText("");
				et_Owner2.setText("");
				et_Contact.setText("");
				et_Contact2.setText("");

				Intent i = new Intent(getApplicationContext(), Assets.class);
				startActivity(i);
			}
		});
		btn_TakePhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				captureImage();
			}
		});

	}


	public void init() {
		//Title Start
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Outlet Information ");
		toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_24dp);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}
		//Title End
		btn_TakePhoto = (Button) findViewById(R.id.btn_TakePhoto);
		btn_signUser = (Button) findViewById(R.id.btn_signUser);
		animation = AnimationUtils.loadAnimation(Outlet.this, R.anim.blink);
		((Button) findViewById(R.id.btn_signUser)).setAnimation(animation);
		animation.start();

		et_Address = (EditText) findViewById(R.id.et_Address);
		et_Address2 = (EditText) findViewById(R.id.et_Address2);
		et_Pincode = (EditText) findViewById(R.id.et_Pincode);
		et_Pincode2 = (EditText) findViewById(R.id.et_Pincode2);
		et_Owner = (EditText) findViewById(R.id.et_Owner);
		et_Owner2 = (EditText) findViewById(R.id.et_Owner2);
		et_Contact = (EditText) findViewById(R.id.et_Contact);
		et_Contact2 = (EditText) findViewById(R.id.et_Contact2);

		spArea = (Spinner) findViewById(R.id.sp_Area);
		spTown = (Spinner) findViewById(R.id.sp_Town);
		spArea1 = (Spinner) findViewById(R.id.sp_Area1);
		spTown1 = (Spinner) findViewById(R.id.sp_Town1);
		sp_PartyName = (Spinner) findViewById(R.id.sp_PartyName);
		sp_PartyName1 = (Spinner) findViewById(R.id.sp_PartyName1);
		image1 = (ImageView) findViewById(R.id.image1);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	}


	private class HttpAsyncTaskArea extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(Outlet.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... urls) {
			return clsUtility.httpGetDate(urls[0]);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			//Toast.makeText(getBaseContext(), "Data Received \n" + result, Toast.LENGTH_LONG).show();
			if (result.equals(""))
				clsUtility.msgBox(Outlet.this, "Message", "No data received.");
			listArea.clear();
			listArea1.clear();
			AddDataArea(result, spArea, hmap_Area, listArea);
			AddDataArea(result, spArea1, hmap_Area1, listArea1);
			spArea.setOnItemSelectedListener(
					new OnItemSelectedListener() {
						public void onItemSelected(
								AdapterView<?> parent, View view, int position, long id) {
							if(spArea.getSelectedItem().equals(".")) {}
							else{
								//Toast.makeText(getApplicationContext(),"Value "+hmap_Area.get(parent.getItemAtPosition(position)),Toast.LENGTH_SHORT).show();
								String strQuery = "BindDRP.ashx?Type=town&Area=" + hmap_Area.get(parent.getItemAtPosition(position));
								new HttpAsyncTaskTown().execute(Url + strQuery);
								AreaCode2=hmap_Area.get(parent.getItemAtPosition(position));

							}
						}

						public void onNothingSelected(AdapterView<?> parent) {
							//Toast.makeText(getApplicationContext(),"Spinner2: unselected",Toast.LENGTH_SHORT).show();
						}
					});

			spArea1.setOnItemSelectedListener(
					new OnItemSelectedListener() {
						public void onItemSelected(
								AdapterView<?> parent, View view, int position, long id) {

							if(spArea1.getSelectedItem().equals(".")) {}
							else
							{
								spArea.setSelection(position);
								String strQuery = "BindDRP.ashx?Type=town&Area=" + hmap_Area1.get(parent.getItemAtPosition(position));
								new HttpAsyncTaskTown1().execute(Url + strQuery);
								AreaCode=hmap_Area1.get(parent.getItemAtPosition(position));

							}
						}

						public void onNothingSelected(AdapterView<?> parent) {
							//Toast.makeText(getApplicationContext(),"Spinner2: unselected",Toast.LENGTH_SHORT).show();
						}
					});
		}
	}


	private class HttpAsyncTaskTown1 extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Outlet.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... urls) {
			return clsUtility.httpGetDate(urls[0]);
		}
		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}
			if (result.equals(""))
				clsUtility.msgBox(Outlet.this, "Message", "No data received.");
			listTown1.clear();
			AddDataArea(result, spTown1, hmap_Town1, listTown1);
			spTown1.setOnItemSelectedListener(
					new OnItemSelectedListener() {
						public void onItemSelected(
								AdapterView<?> parent, View view, int position, long id) {
							if(spTown1.getSelectedItem().equals(".")) {}
							else{
								spTown.setSelection(position);
								//BindParty
								String strQuery="";
								strQuery = "BindDRP.ashx?Type=party&Area=" + AreaCode +"&Town="+hmap_Town1.get(parent.getItemAtPosition(position));
								new HttpAsyncTaskParty1().execute(Url + strQuery);
								Log.e("qqq : ",Url+strQuery);
							}
						}

						public void onNothingSelected(AdapterView<?> parent) {
							//Toast.makeText(getApplicationContext(),"Spinner2: unselected",Toast.LENGTH_SHORT).show();
						}
					});
		}
	}

	private class HttpAsyncTaskTown extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Outlet.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... urls) {
			return clsUtility.httpGetDate(urls[0]);
		}
		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}
			if (result.equals(""))
				clsUtility.msgBox(Outlet.this, "Message", "No data received.");
			listTown.clear();
			AddDataArea(result, spTown, hmap_Town, listTown);
			spTown.setOnItemSelectedListener(
					new OnItemSelectedListener() {
						public void onItemSelected(
								AdapterView<?> parent, View view, int position, long id) {
							if(spTown.getSelectedItem().equals(".")) {}
							else{
							String	strQuery = "BindDRP.ashx?Type=party&Area=" + AreaCode2+"&Town="+hmap_Town1.get(parent.getItemAtPosition(position));
								new HttpAsyncTaskParty().execute(Url + strQuery);
							}
						}

						public void onNothingSelected(AdapterView<?> parent) {
							//Toast.makeText(getApplicationContext(),"Spinner2: unselected",Toast.LENGTH_SHORT).show();
						}
					});
		}
	}

	public void AddDataArea(String strData, Spinner spinner, HashMap hashMap, List list) {
		try {
			String response = strData;
			JSONArray json = new JSONArray(response);
			if (response.contains("Abc123ErrorXyz123")) {
				JSONObject json1 = (JSONObject) json.get(0);
				Toast.makeText(getBaseContext(), "Info : " + json1.getString("Abc123ErrorXyz123"), Toast.LENGTH_LONG).show();
				return;
			}

			ArrayAdapter dataAdapter = null;
			if (json.length() > 0) {
				for (int i = 0; i < json.length(); i++) {
					JSONObject json1 = (JSONObject) json.get(i);
					list.add(json1.getString("Name"));
					hashMap.put(json1.getString("Name"), json1.getString("Code"));
					dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list) {
						public View getView(int position, View convertView, ViewGroup parent) {
							View v = super.getView(position, convertView, parent);
							((TextView) v).setTextSize(13);
							((TextView) v).setTextColor(Color.BLACK);
							return v;
						}
					};
				}
				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(dataAdapter);

			} else {
				Toast.makeText(getApplicationContext(), "No Record Found ! ", Toast.LENGTH_LONG).show();
				return;
			}
		} catch (Exception ex) {
			clsUtility.msgBox(Outlet.this, "Error ", "Error : " + ex.toString());
		}
	}


	private class HttpAsyncTaskParty extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Outlet.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... urls) {
			return clsUtility.httpGetDate(urls[0]);
		}
		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}
			if (result.equals(""))
				clsUtility.msgBox(Outlet.this, "Message", "No data received.");
			listParty.clear();
			AddDataArea(result, sp_PartyName, hmap_Party, listParty);
		}
	}

	private class HttpAsyncTaskParty1 extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Outlet.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... urls) {
			return clsUtility.httpGetDate(urls[0]);
		}
		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}
			if (result.equals(""))
				clsUtility.msgBox(Outlet.this, "Message", "No data received.");
			listParty1.clear();
			AddDataArea(result, sp_PartyName1, hmap_Party1, listParty1);
			sp_PartyName1.setOnItemSelectedListener(
					new OnItemSelectedListener() {
						public void onItemSelected(
								AdapterView<?> parent, View view, int position, long id) {

							if(sp_PartyName1.getSelectedItem().equals("")) {}
							else
							{
								sp_PartyName.setSelection(position);
								String strQuery = "BindDRP.ashx?Type=detail&PCode=" + hmap_Party1.get(parent.getItemAtPosition(position));
								new HttpAsyncTaskDetail().execute(Url + strQuery);
								Log.e("Details url : ",Url+strQuery);
							}
						}

						public void onNothingSelected(AdapterView<?> parent) {
							//Toast.makeText(getApplicationContext(),"Spinner2: unselected",Toast.LENGTH_SHORT).show();
						}
					});
		}
	}

	private class HttpAsyncTaskDetail extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Outlet.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... urls) {
			return clsUtility.httpGetDate(urls[0]);
		}
		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}
			if (result.equals(""))
				clsUtility.msgBox(Outlet.this, "Message", "No data received.");

			AddDataDetail(result);
		}
	}

	public void AddDataDetail(String strData) {
		try {
			String response = strData;
			JSONArray json = new JSONArray(response);
			if (response.contains("Abc123ErrorXyz123")) {
				JSONObject json1 = (JSONObject) json.get(0);
				Toast.makeText(getBaseContext(), "Info : " + json1.getString("Abc123ErrorXyz123"), Toast.LENGTH_LONG).show();
				return;
			}

			db = openOrCreateDatabase("CBAS.db", MODE_PRIVATE, null);
			db.execSQL("delete from Detail");
			ArrayAdapter dataAdapter = null;
			if (json.length() > 0) {

				for (int i = 0; i < json.length(); i++) {
					JSONObject json1 = (JSONObject) json.get(i);

					String qur = "insert into Detail(SrNo,RowId,TagNo,AssetsType,Capacity,Make,MachineNo,MYear,DocDate) values('" + json1.getString("SrNo") + "','" + json1.getString("RowId") + "'," +
							" '" + json1.getString("TagNo") + "','" + json1.getString("AssetsType") + "'," +
							" '" + json1.getString("Capacity") + "','" + json1.getString("Make") + "', "+
							" '" + json1.getString("MachineNo") + "','" + json1.getString("MYear") + "','" + json1.getString("DocDate") + "');";

					db.execSQL(qur);
					//Toast.makeText(getApplicationContext(), "Save Data "+i+json1.getString("Capacity"), Toast.LENGTH_LONG).show();
					et_Address.setText(json1.getString("Address"));
					et_Address2.setText(json1.getString("Address"));
					et_Contact.setText(json1.getString("MobileNo"));
					et_Pincode.setText(json1.getString("PinCode"));

				}

			} else {
				Toast.makeText(getApplicationContext(), "No Record Found ! ", Toast.LENGTH_LONG).show();
				return;
			}
			//db.close();
		} catch (Exception ex) {
			clsUtility.msgBox(Outlet.this, "Error ", "Error : " + ex.toString());
		}
	}


	public void CreateDatabase() {
		db = openOrCreateDatabase("CBAS.db", MODE_PRIVATE, null);

		db.execSQL("DROP TABLE IF EXISTS Outletmst");
		db.execSQL("DROP TABLE IF EXISTS Outletmst2");
		db.execSQL("DROP TABLE IF EXISTS Detail");

		db.execSQL("CREATE TABLE IF NOT EXISTS Detail(SrNo varchar(10),RowId nvarchar(150),TagNo nvarchar(50),AssetsType nvarchar(50)," +
				" Capacity nvarchar(50),Make nvarchar(50),MachineNo nvarchar(50),MYear nvarchar(50),DocDate nvarchar(50));");

		db.execSQL("CREATE TABLE IF NOT EXISTS Outletmst(RowId nvarchar(150),TagNo nvarchar(50),AssetsType nvarchar(50)," +
				" Capacity nvarchar(50),Make nvarchar(50),MachineNo nvarchar(50),MYear nvarchar(50),PYear nvarchar(50),"+
				" Working nvarchar(50),Remark nvarchar(300),ArCode nvarchar(50),Town nvarchar(50),"+
				" Party nvarchar(50),Address nvarchar(500),PinCode nvarchar(50),Owner nvarchar(50),ContactNo nvarchar(50),FilePath nvarchar(150));");

		db.execSQL("CREATE TABLE IF NOT EXISTS Outletmst2(RowId nvarchar(150),TagNo nvarchar(50),AssetsType nvarchar(50)," +
				" Capacity nvarchar(50),Make nvarchar(50),MachineNo nvarchar(50),MYear nvarchar(50),PYear nvarchar(50),"+
				" Working nvarchar(50),Remark nvarchar(300),ArCode nvarchar(50),Town nvarchar(50),"+
				" Party nvarchar(50),Address nvarchar(500),PinCode nvarchar(50),Owner nvarchar(50),ContactNo nvarchar(50),"+
				" FilePath nvarchar(150),ImgAss nvarchar(150),ImgAss2 nvarchar(150),ImgAss3 nvarchar(150),ImgAss4 nvarchar(150));");
		//Toast.makeText(getApplicationContext(), "DB", Toast.LENGTH_LONG).show();
	}

	//Image Capture Start
	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}


	static int i=1;
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		try
		{
			if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE)
			{
				if (resultCode == RESULT_OK) {

					super.onActivityResult(requestCode, resultCode, data);
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 8;
					//i=IdNo;
					String filePath ="";
					//Get File URi Path Start
					//Uri fileUri;
					//fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
					if(fileUri==null || fileUri.equals(""))
					{
						Toast.makeText(getApplicationContext(), "Device Error Try Again !", Toast.LENGTH_SHORT).show();
						//return;
					}
					else
					{
						filePath=fileUri.getPath();
						UrlData=filePath;
						ImageView img=new ImageView(this);
						final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),options);
						image1.setImageBitmap(bitmap);
					}
				}
				else if (resultCode == RESULT_CANCELED) {
					// user cancelled Image capture
					Toast.makeText(getApplicationContext(),"User Cancelled Image Capture", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(getApplicationContext(),"Sorry! Failed To Capture Image", Toast.LENGTH_SHORT).show();
				}
			}
		}
		catch(Exception ex)
		{
			Log.d("Error", ex.getMessage());
			Toast.makeText(getApplicationContext(), "Error : "+ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	/*
      * Creating file uri to store image/video
      */
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}
	/*
     * returning image / video
     */
	private static File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
						+ IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}
	//Image Capture End
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

}