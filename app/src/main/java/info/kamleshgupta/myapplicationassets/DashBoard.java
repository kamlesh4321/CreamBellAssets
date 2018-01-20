package info.kamleshgupta.myapplicationassets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


public class DashBoard extends AppCompatActivity {
    

	clsUtility clsUtility=new clsUtility();

    String Url="";
	String BType="";
	//MaterialSearchView searchView;
	Toolbar toolbar=null;
	Button btnAssetsIssue,btnAssetsReport,btnNonApproved;
	private Animation animation;
	String UserId="";
	Handler handler =new Handler();
	private final int FIVE_SECONDS = 500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);



		//Add Search View
		toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Dashboard");

		toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_24dp);
		//searchView=(MaterialSearchView)findViewById(R.id.search_view);

		if(getSupportActionBar()!=null)
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}

		init();

		Url = clsUtility.SetUrl(getApplicationContext());

		//String strQuery = "CountOrder.ashx?Branch="+BCode;
		//new HttpAsyncTask().execute(Url + strQuery);


		btnAssetsIssue.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent i = new Intent(getApplicationContext(),Outlet.class);
				//globalVariable.setDashHeading("New Order");
				//globalVariable.setStatus("0");
				startActivity(i);
				//finish();

			}
		});
		btnAssetsReport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent i = new Intent(getApplicationContext(), ApproveOrder.class);
				//globalVariable.setDashHeading("Approved Order");
				//globalVariable.setStatus("1");
				//startActivity(i);
				//finish();

			}
		});

	}
	public void init()
	{
		btnAssetsIssue=(Button)findViewById(R.id.btnAssetsIssue);
		btnAssetsReport=(Button)findViewById(R.id.btnAssetsReport);
		btnNonApproved=(Button)findViewById(R.id.btnNonApproved);

		animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
		((Button)findViewById(R.id.btnAssetsIssue)).setAnimation(animation);
		((Button)findViewById(R.id.btnAssetsReport)).setAnimation(animation);
		((Button)findViewById(R.id.btnNonApproved)).setAnimation(animation);

		animation.start();


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

	public void function_lstvVideo_AsyncTask(String strSearch)
	 {
	 	Url=clsUtility.SetUrl(getApplicationContext());

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