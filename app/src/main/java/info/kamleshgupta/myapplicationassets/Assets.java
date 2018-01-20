package info.kamleshgupta.myapplicationassets;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.TableRow.LayoutParams;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


public class Assets extends AppCompatActivity {



	String imagepath;
	File sourceFile;
	//public DonutProgress donut_progress;
	int totalSize = 0;
	private static final int REQUEST_WRITE_STORAGE = 112;
	String FILE_UPLOAD_URL ="http://perfectsoftware.co.in/s2/UploadToServer.php";

	clsUtility clsUtility=new clsUtility();
    String Url="";
	String UrlPHP="";
	String BType="";
	// 	Activity request codes
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private static final String IMAGE_DIRECTORY_NAME = "Hello_Camera";
	private Uri fileUri; // file url to store image/video

	//MaterialSearchView searchView;
	private   int CaptureImg=0;
	String upLoadServerUri = null;
	//ProgressDialog dialog = null;
	ProgressDialog dialog=null;
	int serverResponseCode = 0;

	Spinner sp_Make,sp_AssetsType,sp_Capacity,sp_MYear,sp_PYear,sp_Working;
	Toolbar toolbar=null;
	SQLiteDatabase db;
	private Animation animation;
	private ImageView image1,image2,image3,image4;
	private Button btn_signUser, btn_TakePhoto;
	private EditText et_Make,et_AssetsType,et_Capacity,et_TagNo,et_TagNo2,et_MachineNo,et_MachineNo2,et_MYear,et_PYear,et_Working,et_Remark;
	private TableLayout table_layout;
	String[] RowId;
	static int RowIndex=0;
	static int RowIndexchk=0;
	static int IsSaved=0;
	static String StrRowId="";
	static String UrlImage1="";
	static String UrlImage2="";
	static String UrlImage3="";
	static String UrlImage4="";
	HashMap<String, String> hmap_Make = new HashMap<String, String>();
	HashMap<String, String> hmap_Assets = new HashMap<String, String>();
	HashMap<String, String> hmap_Capacity = new HashMap<String, String>();
	List<String> listMake = new ArrayList<String>();
	List<String> listAsstes = new ArrayList<String>();
	List<String> listCapacity = new ArrayList<String>();

	String ArCode="";String Town="";String Party="";String Address="";String PinCode="";String Owner="";String ContactNo="",outFilePath="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assets);
		//this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		//CreateDatabase();
		//BType=intent.getStringExtra("BType");
		//final String ShortComp = globalVariable.getShortComp();
		init();

		Url = clsUtility.SetUrl(getApplicationContext());
		String strQuery = "BindDRP.ashx?Type=make";
		new HttpAsyncTaskMake().execute(Url + strQuery);

		strQuery = "BindDRP.ashx?Type=assets";
		new HttpAsyncTaskAssets().execute(Url + strQuery);

		strQuery = "BindDRP.ashx?Type=capacity";
		new HttpAsyncTaskCapacity().execute(Url + strQuery);


		//Toast.makeText(getApplicationContext(),"handler 1 ",Toast.LENGTH_LONG).show();
		db = openOrCreateDatabase("CBAS.db", MODE_PRIVATE, null);
		Cursor c = null;

		c = db.rawQuery("select ArCode,Town,Party,Address,PinCode,Owner,ContactNo,FilePath from Outletmst  ", null);
		int countr = c.getCount();
		if(countr>0) {
			c.moveToFirst();
			for (int j = 0; j < countr; j++) {
				ArCode=c.getString(c.getColumnIndex("ArCode"));
				Town=c.getString(c.getColumnIndex("Town"));
				Party=c.getString(c.getColumnIndex("Party"));
				Address=c.getString(c.getColumnIndex("Address"));
				PinCode=c.getString(c.getColumnIndex("PinCode"));
				Owner=c.getString(c.getColumnIndex("Owner"));
				ContactNo=c.getString(c.getColumnIndex("ContactNo"));
				outFilePath=c.getString(c.getColumnIndex("FilePath"));
				//Toast.makeText(getApplicationContext(),"Path "+outFilePath,Toast.LENGTH_LONG).show();
			}
		}
		db.execSQL("delete from Outletmst  ");
        db.execSQL("delete from Outletmst2  ");

		final int Count=BindGrid();
        if(RowIndexchk+1==Count)
        {
            btn_signUser.setText("SAVE");
        }
        else
        {
            btn_signUser.setText("NEXT");
        }
		btn_signUser.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if(sp_Make.getSelectedItem().equals(".") || sp_Make.getSelectedItem().equals("")) {
					clsUtility.msgBox(Assets.this,"Info","Please Select Make !");
					return;
				}
				if(sp_AssetsType.getSelectedItem().equals(".") || sp_AssetsType.getSelectedItem().equals("")) {
					clsUtility.msgBox(Assets.this,"Info","Please Select Assets Type !");
					return;
				}
				if(sp_Capacity.getSelectedItem().equals(".") || sp_Capacity.getSelectedItem().equals("")) {
					clsUtility.msgBox(Assets.this,"Info","Please Select Capacity !");
					return;
				}
				if(sp_MYear.getSelectedItem().equals("Choose a Year")) {
					clsUtility.msgBox(Assets.this,"Info","Please Select M. Year !");
					return;
				}
				if(sp_PYear.getSelectedItem().equals("Choose a Year")) {
					clsUtility.msgBox(Assets.this,"Info","Please Select P. Year !");
					return;
				}


                if(btn_signUser.getText().equals("SAVE"))
                {
                    //Insert Code
					db = openOrCreateDatabase("CBAS.db", MODE_PRIVATE, null);
					String q="insert into Outletmst2(RowId,TagNo,AssetsType,Capacity,Make,MachineNo,MYear,PYear,Working,Remark,"+
							" ArCode,Town,Party,Address,PinCode,Owner,ContactNo,FilePath,ImgAss,ImgAss2,ImgAss3,ImgAss4)values('"+StrRowId+"','"+et_TagNo.getText()+"',"+
							" '"+hmap_Assets.get(sp_AssetsType.getSelectedItem()) +"','"+hmap_Capacity.get(sp_Capacity.getSelectedItem())+"',"+
							" '"+hmap_Make.get(sp_Make.getSelectedItem())+"','"+et_MachineNo2.getText()+"','"+sp_MYear.getSelectedItem()+"',"+
							" '"+sp_PYear.getSelectedItem()+"','"+sp_Working.getSelectedItem()+"','"+et_Remark.getText()+"','"+ArCode+"',"+
							" '"+ Town +"','"+ Party+"','"+Address+"','"+PinCode+"','"+Owner+"','"+ContactNo+"','"+outFilePath+"',"+
					" '"+UrlImage1+"','"+UrlImage2+"','"+UrlImage3+"','"+UrlImage4+"');";
					db.execSQL(q);
					Log.e("insert q ",q);

					UploadFile();
                   // Toast.makeText(getApplicationContext(),"SAVE Code",Toast.LENGTH_LONG).show();
					db.execSQL("delete from Detail");
					//BindGrid();
					//ClearText();
					image1.setImageResource(R.drawable.p1);
					image2.setImageResource(R.drawable.p2);
					image3.setImageResource(R.drawable.p3);
					image4.setImageResource(R.drawable.p4);
                }
                else {
                    if (Count > 0) {
						//
                        RowIndex = RowIndex + 1;
						RowIndexchk=RowIndex;
						BindText(RowIndex);
                        if (RowIndex+1 == Count) {
                            btn_signUser.setText("SAVE");
                        } else {
                            btn_signUser.setText("NEXT");
                        }
						db = openOrCreateDatabase("CBAS.db", MODE_PRIVATE, null);

						String q="insert into Outletmst2(RowId,TagNo,AssetsType,Capacity,Make,MachineNo,MYear,PYear,Working,Remark,"+
								" ArCode,Town,Party,Address,PinCode,Owner,ContactNo,FilePath,ImgAss,ImgAss2,ImgAss3,ImgAss4)values('"+StrRowId+"','"+et_TagNo.getText()+"',"+
								" '"+hmap_Assets.get(sp_AssetsType.getSelectedItem()) +"','"+hmap_Capacity.get(sp_Capacity.getSelectedItem())+"',"+
								" '"+hmap_Make.get(sp_Make.getSelectedItem())+"','"+et_MachineNo2.getText()+"','"+sp_MYear.getSelectedItem()+"',"+
								" '"+sp_PYear.getSelectedItem()+"','"+sp_Working.getSelectedItem()+"','"+et_Remark.getText()+"','"+ArCode+"',"+
								" '"+ Town +"','"+ Party+"','"+Address+"','"+PinCode+"','"+Owner+"','"+ContactNo+"','"+outFilePath+"',"+
								" '"+UrlImage1+"','"+UrlImage2+"','"+UrlImage3+"','"+UrlImage4+"');";
						db.execSQL(q);
						Log.e("insert q ",q);
						ClearText();
						//Toast.makeText(getApplicationContext(),"SAVE "+Town+","+Party+","+ArCode,Toast.LENGTH_LONG).show();
                    }
                }

			}
		});
		image1.setOnClickListener(new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			//captureImage1();
		}
	});
		btn_TakePhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				captureImage();
			}
		});
	}

	public void ClearText()
	{
		sp_Make.setSelection(0);
		sp_AssetsType.setSelection(0);
		sp_Capacity.setSelection(0);
		et_TagNo2.setText("");
		et_MachineNo2.setText("");
		sp_MYear.setSelection(0);
		sp_PYear.setSelection(0);
		sp_Working.setSelection(0);
		et_Remark.setText("");
		image1.setImageResource(R.drawable.p1);
		image2.setImageResource(R.drawable.p2);
		image3.setImageResource(R.drawable.p3);
		image4.setImageResource(R.drawable.p4);
		UrlImage1="";
		UrlImage2="";
		UrlImage3="";
		UrlImage4="";
		CaptureImg=0;
	}

	private int BindGrid()
	{
		table_layout.removeAllViews();
        int count=0;
		try {
			String[] ColArray=new String[]{"SrNo","TagNo","AssetsType","Capacity","Make","DocDate"};
			db = openOrCreateDatabase("CBAS.db", MODE_PRIVATE, null);
			Cursor c = null;
			c = db.rawQuery("select SrNo,RowId,TagNo,AssetsType,Capacity,Make,DocDate from Detail  ", null);
            count = c.getCount();
			TableRow row2 = new TableRow(this);
			row2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			for(int j=0;j<ColArray.length;j++) {
				TextView tv = new TextView(this);
				tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				tv.setBackgroundResource(R.drawable.cell_shapeheader);
				tv.setGravity(Gravity.CENTER);
				tv.setTextSize(12);
				tv.setPadding(0, 12, 0, 12);
				tv.setTextColor(Color.parseColor("#000000"));
				String Data = ColArray[j]+"";
				tv.setText(Data);
				row2.addView(tv);
			}
			table_layout.addView(row2);

			if (count > 0) {
				c.moveToFirst();
				RowId=new String[count];
				for (int i = 0; i < count; i++) {
					//Mobile=c.getString(c.getColumnIndex("Mobile"));
					TableRow row = new TableRow(this);
					row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
					row.setId(i);
					RowId[i]=c.getString(c.getColumnIndex("RowId"));
					for(int j=0;j<ColArray.length;j++) {
						TextView tv = new TextView(this);
						tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
						tv.setBackgroundResource(R.drawable.cell_shape2);
						tv.setGravity(Gravity.CENTER);
						tv.setTextSize(12);
						tv.setPadding(0, 12, 0, 12);
						tv.setTextColor(Color.parseColor("#000000"));
						String Data = c.getString(c.getColumnIndex(ColArray[j]+""));
						tv.setText(Data);
						row.addView(tv);
					}

					table_layout.addView(row);
					row.setOnClickListener(new View.OnClickListener(){
						@Override
						public void onClick(View v) {
							int index=Integer.parseInt(v.getId()+"");
							BindText(index);
						}
					});
					c.moveToNext();
				}
			}
			db.close();
			BindText(RowIndex);

		}
		catch(Exception ex)
		{
			Toast.makeText(getApplicationContext(),"Error : "+ex.getMessage(),Toast.LENGTH_LONG).show();
		}
        return count;
	}
	public void BindText(final int index)
	{
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				//Toast.makeText(getApplicationContext(),"handler 2",Toast.LENGTH_LONG).show();
				db = openOrCreateDatabase("CBAS.db", MODE_PRIVATE, null);
				Cursor c = null;
				c = db.rawQuery("select SrNo,RowId,TagNo,AssetsType,Capacity,Make,MachineNo,MYear from Detail where RowId='"+RowId[index]+"' ", null);
				int count = c.getCount();

				if (count > 0) {
					c.moveToFirst();
					for (int i = 0; i < count; i++) {
						et_Make.setText(c.getString(c.getColumnIndex("Make")));
						et_AssetsType.setText(c.getString(c.getColumnIndex("AssetsType")));
						et_Capacity.setText(c.getString(c.getColumnIndex("Capacity")));
						et_TagNo.setText(c.getString(c.getColumnIndex("TagNo")));
						et_MachineNo.setText(c.getString(c.getColumnIndex("MachineNo")));
						et_MYear.setText(c.getString(c.getColumnIndex("MYear")));
						StrRowId=c.getString(c.getColumnIndex("RowId"));
						et_TagNo2.setText(et_TagNo.getText());
						et_MachineNo2.setText(et_MachineNo.getText());
						int intPos=getPostiton(et_Make.getText().toString(),listMake);
						sp_Make.setSelection(intPos);
						intPos=getPostiton(et_AssetsType.getText().toString(),listAsstes);
						sp_AssetsType.setSelection(intPos);
						intPos=getPostiton(et_Capacity.getText().toString(),listCapacity);
						sp_Capacity.setSelection(intPos);
						getSupportActionBar().setTitle("Assets Information   SrNo : "+c.getString(c.getColumnIndex("SrNo")));
						//sp_Make.setPrompt(et_Make.getText());
						//Toast.makeText(getApplicationContext(),"StrRowId 1 "+StrRowId,Toast.LENGTH_LONG).show();
					}
				}
			}
		}, 1500);

		db.close();
	}

	private int getPostiton(String locationid,List list)
	{
		int i;
		int position=0;
		//Toast.makeText(getApplicationContext(),"loc "+locationid,Toast.LENGTH_LONG).show();
		for(i=0;i< list.size();i++)
		{
			//String locationVal = ColumnName;
			String locationVal = list.get(i).toString();
			//Toast.makeText(getApplicationContext(),"Str "+locationVal,Toast.LENGTH_LONG).show();
			//Toast.makeText(getApplicationContext(),"Val "+locationid,Toast.LENGTH_LONG).show();
			//Log.e("Str ",locationVal);
			//Log.e("Vsl ",locationid);
			if(locationVal.trim().equalsIgnoreCase(locationid.trim()))
			{
				position = i;
				break;
			}
			else
			{
				position = 0;
			}
		}
		//Toast.makeText(getApplicationContext(), "return : "+position,Toast.LENGTH_LONG).show();
		// Toast.makeText(getApplicationContext(), "return : "+locationVal+"  ,"+locationid,Toast.LENGTH_LONG).show();
		return position;
	}

	public void init() {
		toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Assets Information ");

		toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_24dp);
		//searchView=(MaterialSearchView)findViewById(R.id.search_view);
		if(getSupportActionBar()!=null)
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}
		String query="";

		btn_TakePhoto = (Button) findViewById(R.id.btn_TakePhoto);
		btn_signUser = (Button) findViewById(R.id.btn_signUser);
		animation = AnimationUtils.loadAnimation(Assets.this,R.anim.blink);
		((Button) findViewById(R.id.btn_signUser)).setAnimation(animation);
		animation.start();

		et_Make = (EditText) findViewById(R.id.et_Make);
		et_AssetsType = (EditText) findViewById(R.id.et_AssetsType);
		et_Capacity = (EditText) findViewById(R.id.et_Capacity);
		et_TagNo = (EditText) findViewById(R.id.et_TagNo);
		et_TagNo2 = (EditText) findViewById(R.id.et_TagNo2);
		et_MachineNo = (EditText) findViewById(R.id.et_MachineNo);
		et_MachineNo2 = (EditText) findViewById(R.id.et_MachineNo2);
		et_MYear = (EditText) findViewById(R.id.et_MYear);
		et_PYear = (EditText) findViewById(R.id.et_PYear);
		et_Working = (EditText) findViewById(R.id.et_Working);
		et_Remark = (EditText) findViewById(R.id.et_Remark);

		sp_Make = (Spinner) findViewById(R.id.sp_Make);
		sp_AssetsType = (Spinner) findViewById(R.id.sp_AssetsType);
		sp_Capacity = (Spinner) findViewById(R.id.sp_Capacity);
		sp_MYear = (Spinner) findViewById(R.id.sp_MYear);
		sp_MYear = (Spinner) findViewById(R.id.sp_MYear);
		sp_PYear = (Spinner) findViewById(R.id.sp_PYear);
		sp_Working = (Spinner) findViewById(R.id.sp_Working);

		image1 = (ImageView) findViewById(R.id.image1);
		image2 = (ImageView) findViewById(R.id.image2);
		image3 = (ImageView) findViewById(R.id.image3);
		image4 = (ImageView) findViewById(R.id.image4);

		table_layout=(TableLayout)findViewById(R.id.tableLayout1);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {

		return true;
    }

	private class HttpAsyncTaskMake extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(Assets.this);
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
				clsUtility.msgBox(Assets.this, "Message", "No data received.");
			listMake.clear();
			AddDataArea(result, sp_Make, hmap_Make, listMake);

		}
	}

	private class HttpAsyncTaskAssets extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(Assets.this);
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
				clsUtility.msgBox(Assets.this, "Message", "No data received.");
			listAsstes.clear();
			AddDataArea(result, sp_AssetsType, hmap_Assets, listAsstes);

		}
	}

	private class HttpAsyncTaskCapacity extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(Assets.this);
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
				clsUtility.msgBox(Assets.this, "Message", "No data received.");
			listCapacity.clear();
			AddDataArea(result, sp_Capacity, hmap_Capacity, listCapacity);

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
					hashMap.put(json1.getString("Name"),json1.getString("Code"));
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
			clsUtility.msgBox(Assets.this, "Error ", "Error : " + ex.toString());
		}
	}

		public void UploadFile()
		{

			try
			{
				if (clsUtility.isNetworkAvailable(Assets.this)==false)
				{
					clsUtility.msgBox(Assets.this, "No Network Connection", "Internet is not available right now.");
					return;
				}
				Log.e("Exe","1");
				upLoadServerUri =UrlPHP+"fileUpload.php";//"http://192.168.1.111/index.php";
				//dialog = ProgressDialog.show(Assets.this, "", "Uploading file...", true);
				dialog = new ProgressDialog(Assets.this);
				dialog.setMessage("Uploading file...");
				dialog.setCancelable(false);
				dialog.show();
				new Thread(new Runnable() {
					public void run() {

								Log.e("Exe", "2");
								int SavedData = 0;
								db = openOrCreateDatabase("CBAS.db", MODE_PRIVATE, null);
								String query = "";
								Log.e("Exe", "3");
								query = "select RowId,TagNo,AssetsType,Capacity,Make,MachineNo,MYear,PYear,Working,Remark," +
										" ArCode,Town,Party,Address,PinCode,Owner,ContactNo,FilePath,ImgAss,ImgAss2,ImgAss3,ImgAss4 from Outletmst2 ";
								Cursor c = null;
								c = db.rawQuery(query, null);
								Log.e("Qu", query);
								int count = c.getCount();
								Log.e("Count", "" + count);
										if (count > 0) {
											c.moveToFirst();
											String OutPut = "";
											int serverResponseCode=0;
											if (c.getString(c.getColumnIndex("FilePath")).length() > 0) {
													//HttpUploader uploader = new HttpUploader();
													 imagepath = c.getString(c.getColumnIndex("FilePath"));
													//OutPut = uploader.execute(currImageURI).get();
													Log.e("OutPut", imagepath);
                                                    if (imagepath != null) {
                                                        //new UploadFileToServer().execute();
														serverResponseCode=uploadFile(imagepath);
                                                    }else{
                                                       // Toast.makeText(getApplicationContext(), "Please select a file to upload.", Toast.LENGTH_SHORT).show();
                                                    }
											}
											for (int j = 0; j < count; j++) {
												String ImgAss = c.getString(c.getColumnIndex("ImgAss"));
												if (ImgAss.length() > 0) {
														serverResponseCode=uploadFile(ImgAss);
												}
												String ImgAss2 = c.getString(c.getColumnIndex("ImgAss2"));
												if (ImgAss2.length() > 0) {
													serverResponseCode=uploadFile(ImgAss2);
												}
												String ImgAss3 = c.getString(c.getColumnIndex("ImgAss3"));
												if (ImgAss3.length() > 0) {
													serverResponseCode=uploadFile(ImgAss3);
												}
												String ImgAss4 = c.getString(c.getColumnIndex("ImgAss4"));
												if (ImgAss4.length() > 0) {
													serverResponseCode=uploadFile(ImgAss3);
												}
												Log.e("Strchk", serverResponseCode+"");
												String Strchk = OutPut;
												Log.e("Response Image", Strchk);
												Strchk="Image upload complete";
												if (serverResponseCode==200) {
													String RowId = c.getString(c.getColumnIndex("RowId"));
													String TagNo = c.getString(c.getColumnIndex("TagNo"));
													String AssetsType = c.getString(c.getColumnIndex("AssetsType"));
													String Capacity = c.getString(c.getColumnIndex("Capacity"));
													String Make = c.getString(c.getColumnIndex("Make"));
													String MachineNo = c.getString(c.getColumnIndex("MachineNo"));
													String MYear = c.getString(c.getColumnIndex("MYear"));
													String PYear = c.getString(c.getColumnIndex("PYear"));
													String Working = c.getString(c.getColumnIndex("Working"));
													String Remark = c.getString(c.getColumnIndex("Remark"));
													String ArCode = c.getString(c.getColumnIndex("ArCode"));
													String Town = c.getString(c.getColumnIndex("Town"));
													String Party = c.getString(c.getColumnIndex("Party"));
													String Address = c.getString(c.getColumnIndex("Address"));
													String PinCode = c.getString(c.getColumnIndex("PinCode"));
													String Owner = c.getString(c.getColumnIndex("Owner"));
													String ContactNo = c.getString(c.getColumnIndex("ContactNo"));

													//String ImgAss = c.getString(c.getColumnIndex("ImgAss"));
													//String ImgAss2 = c.getString(c.getColumnIndex("ImgAss2"));
													//String ImgAss3 = c.getString(c.getColumnIndex("ImgAss3"));
													//String ImgAss4 = c.getString(c.getColumnIndex("ImgAss4"));


													//if(serverResponseCode==200)

													query = Url + "SaveData.ashx?RowId=" + RowId + "&TagNo=" + TagNo;
													query = query + "&AssetsType=" + AssetsType + "&Capacity=" + Capacity + "&Make=" + Make;
													query = query + "&MachineNo=" + MachineNo + "&MYear=" + MYear + "&PYear=" + PYear;
													query = query + "&Working=" + Working + "&Remark=" + Remark.trim() + "&ArCode=" + ArCode;
													query = query + "&Town=" + Town + "&Party=" + Party + "&Address=" + Address + "&PinCode=" + PinCode;
													query = query + "&Owner=" + Owner + "&ContactNo=" + ContactNo + "&Img1=" + outFilePath;
													query = query + "&Img2=" + ImgAss + "&Img3=" + ImgAss2 + "&Img4=" + ImgAss3 + "&Img5=" + ImgAss4;
													Log.e("Quary url", query);

													final String responce = clsUtility.httpGetDate(query);
													runOnUiThread(new Runnable() {
														public void run() {
															dialog.dismiss();
															JSONArray json;
															try {
																json = new JSONArray(responce);
																if (responce.contains("Abc123ErrorXyz123")) {
																	JSONObject json1 = (JSONObject) json.get(0);
																	Log.e("Error : ", json1.getString("Abc123ErrorXyz123"));
																	Toast.makeText(Assets.this, "Error : " + json1.getString("Abc123ErrorXyz123"), Toast.LENGTH_SHORT).show();
																	return;
																}
															} catch (JSONException e) {
																// TODO Auto-generated catch block
																e.printStackTrace();
															}
														}
													});
													if (responce.contains("123SaveRecord123")) {
														Log.e("Save: ", "12");
														//db = openOrCreateDatabase("CBAS.db", MODE_PRIVATE, null);
														//db.execSQL("delete from Outletmst2 where RowId="+RowId);
														SavedData = SavedData + 1;
														Log.e("Save: ", "" + SavedData);
													}
												}
												c.moveToNext();
											}
											if (SavedData > 0) {
												runOnUiThread(new Runnable() {
													public void run() {
														dialog.dismiss();
														RowIndex = 0;
														RowIndexchk = 0;
														//Toast.makeText(Assets.this, "Data Saved Successfully.",Toast.LENGTH_SHORT).show();
														clsUtility.msgBox(Assets.this, "Info ", "Data Saved Successfully !");
														//Intent i = new Intent(getApplicationContext(), DashBoard.class);
														//startActivity(i);
													}
												});
											}
										} else {
											dialog.dismiss();
											runOnUiThread(new Runnable() {
												public void run() {
													Toast.makeText(getApplicationContext(), "No Record Found !", Toast.LENGTH_LONG).show();
													Log.e("Error : ", "No Record Found !");
												}
											});
										}
							}

				}).start();
			}
			catch(Exception ex)
			{
				dialog.dismiss();
				//clsUtility.msgBox(OfficeRemark.this, "Error", "Error : "+ex.toString());
				Log.e("Error : ", ""+ex.toString());
			}
		}



	public void CreateDatabase()
	{
		db=openOrCreateDatabase("CBAS.db", MODE_PRIVATE, null);
		//db.execSQL("DROP TABLE IF EXISTS AddToCart");
		db.execSQL("CREATE TABLE IF NOT EXISTS AssetsSave(UserId nvarchar(50),FName nvarchar(50),LName nvarchar(50),"+
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


	public void ShowMsg()
	{
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(Assets.this);

		// Setting Dialog Title
		alertDialog.setTitle("Info");

		// Setting Dialog Message
		alertDialog.setMessage("Do you want to cancel !");
		alertDialog.setCancelable(false);
		// Setting Icon to Dialog
		//alertDialog.setIcon(R.drawable.info);

		// Setting Positive "Yes" Button
		alertDialog.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// User pressed YES button. Write Logic Here
				finish();
			}
		});

		// Setting Netural "Cancel" Button
		alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// User pressed Cancel button. Write Logic Here
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}
	//this is for back button Start
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			ShowMsg();
		}
		else if(keyCode == KeyEvent.KEYCODE_HOME)
		{
			ShowMsg();
		}
		//return super.onKeyDown(keyCode, event);
		return false;
	}
	//this is for back button End

	//Image Capture Start
	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}


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
						String imgfilePath=fileUri.getPath();
						ImageView img=new ImageView(this);
						final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),options);

						if(CaptureImg==0) {
							image1.setImageBitmap(bitmap);
							CaptureImg=CaptureImg+1;
							UrlImage1=imgfilePath;
						}
						else if(CaptureImg==1)
						{
							image2.setImageBitmap(bitmap);
							CaptureImg=CaptureImg+1;
							UrlImage2=imgfilePath;
						}
						else if(CaptureImg==2)
						{
							image3.setImageBitmap(bitmap);
							CaptureImg=CaptureImg+1;
							UrlImage3=imgfilePath;
						}
						else if(CaptureImg==3)
						{
							image4.setImageBitmap(bitmap);
							CaptureImg=CaptureImg+1;
							UrlImage4=imgfilePath;
						}

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


//Image Uploader Function
public int uploadFile(String sourceFileUri) {
	/************* Php script path ****************/
	upLoadServerUri = "http://perfectsoftware.co.in/s2/UploadToServer.php";
    String fileName = sourceFileUri;

    HttpURLConnection conn = null;
    DataOutputStream dos = null;
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";
    int bytesRead, bytesAvailable, bufferSize;
    byte[] buffer;
    int maxBufferSize = 1 * 1024 * 1024;
    File sourceFile = new File(sourceFileUri);
    if (!sourceFile.isFile()) {
        dialog.dismiss();

        Log.e("uploadFile", "Source File not exist :" +sourceFileUri);

        runOnUiThread(new Runnable() {
            public void run() {
               // messageText.setText("Source File not exist :"+uploadFilePath + "" + uploadFileName);
            }
        });
        return 0;
    }
    else
    {
        try {

            // open a URL connection to the Servlet
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(upLoadServerUri);

            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", fileName);

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                    + fileName + "\"" + lineEnd);

            dos.writeBytes(lineEnd);

            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : "
                    + serverResponseMessage + ": " + serverResponseCode);

            if(serverResponseCode == 200){

                runOnUiThread(new Runnable() {
                    public void run() {

                        String msg = "File Upload Completed.\n\n See uploaded file here : \n\n" +" http://www.androidexample.com/media/uploads/";
                        //messageText.setText(msg);
                        //Toast.makeText(UploadToServer.this, "File Upload Complete.",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {

            dialog.dismiss();
            ex.printStackTrace();

            runOnUiThread(new Runnable() {
                public void run() {
                    //messageText.setText("MalformedURLException Exception : check script url.");
                    Toast.makeText(Assets.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                }
            });

            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {

            dialog.dismiss();
            e.printStackTrace();

            runOnUiThread(new Runnable() {
                public void run() {
                    //messageText.setText("Got Exception : see logcat ");
                    Toast.makeText(Assets.this, "Got Exception : see logcat ",Toast.LENGTH_SHORT).show();
                }
            });
            //Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);
        }
        dialog.dismiss();
        return serverResponseCode;

    } // End else block
}
}


