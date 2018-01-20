package info.kamleshgupta.myapplicationassets;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;


public class ImportExcelFile extends AppCompatActivity {
    

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


	}
}