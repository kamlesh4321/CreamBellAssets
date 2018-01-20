package info.kamleshgupta.myapplicationassets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public class clsUtility {

	
	 ConnectivityManager connectivityManager;
	    NetworkInfo wifiInfo, mobileInfo;
	 
	    /**
	     * Check for <code>TYPE_WIFI</code> and <code>TYPE_MOBILE</code> connection using <code>isConnected()</code>
	     * Checks for generic Exceptions and writes them to logcat as <code>CheckConnectivity Exception</code>. 
	     * Make sure AndroidManifest.xml has appropriate permissions.
	     * @param con Application context
	     * @return Boolean
	     */
	    public Boolean isNetworkAvailable(Context con){
	        try{
	            connectivityManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
	            wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	            mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);   
	            if(wifiInfo.isConnected() || mobileInfo.isConnected())
	            {
	                return true;
	            }
	        }
	        catch(Exception e){
	            System.out.println("CheckConnectivity Exception: " + e.getMessage());
	        }
	         
	        return false;
	    } 
	    
	    
	    public void msgBox(Context cxt,String title,String msg) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(cxt);

			alertDialog.setTitle(title);

			alertDialog.setMessage(msg);
			alertDialog.setPositiveButton("OK",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});

			alertDialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			alertDialog.show();
		}
	    
	    public String SetUrl(Context con){
	    	String Url="";
	    	try{
	    		//Url="http://192.168.1.105/DSR/";
				//Url="http://bm.perfectsoftware.in/BM/";
				//Url="http://rcu.maheshandco.com/BM/";
				//Url="http://rcu.maheshandco.com/CBO/";
                Url="http://cbo.perfectsoftware.in/CbAssets/";
				//Url="http://rcu.maheshandco.com/CBOTEST/";
	        }
	        catch(Exception e){
	            System.out.println("CheckConnectivity Exception: " + e.getMessage());
	        }
	        return Url;
	    }
	public String SetUrlPHP(Context con){
		String Url="";
		try{
			//Url="http://192.168.1.111/";
			//Url="http://rcu.maheshandco.com/UpImg/";
			Url="http://perfectsoftware.co.in/s2/";
		}
		catch(Exception e){
			System.out.println("CheckConnectivity Exception: " + e.getMessage());
		}
		return Url;
	}

	public String httpGetDate(String mURL)
		 {
		 	HttpResponse res=null;
		 	mURL=mURL.replace(" ", "%20");
		 	HttpClient httpclient=new DefaultHttpClient();
		 	HttpGet httppost=new HttpGet(mURL);
		 	try
		 	{
		 		res = httpclient.execute(httppost);
		 	}
		 	catch(IOException e)
		 	{
		 		Log.i("LocAndroid Response HTTP ",e.getMessage());
		 	}
		 	BufferedReader in=null;
		     try {
		         in = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
		     } catch (IllegalStateException e) {
		         Log.d("","3. "+e.toString());
		     } catch (IOException e) {
		         Log.d("","4. "+e.toString());
		     }
		     StringBuffer sb = new StringBuffer("");
		     String line = "";
		     String newline = System.getProperty("line.separator");
		     try {
		         while ((line = in.readLine()) !=null){
		             sb.append(line + newline);
		         }
		         
		     } catch (IOException e) {
		         Log.d("","5. "+e.toString());
		     }
		     try {
		         in.close();
		     } catch (IOException e) {
		         Log.d("","6. "+e.toString());
		     }
		     String data = sb.toString();
		 	return data;
		 }
	  //Get Data Using Url End

}
