package info.kamleshgupta.myapplicationassets;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
//import com.guerrilla.ptf.LoginApp;
import android.util.Base64;
import android.util.Log;

//Uploader class
   public class HttpUploader extends AsyncTask<String, Void, String> 
   {
	   protected String doInBackground(String... path) {
             
            String outPut = null;

            for (String sdPath : path) {

            	//System.out.println("���-path" + sdPath);
            //	Log.d("Path", sdPath);
            	int start=sdPath.length()-23;
            	String imgnm=sdPath.substring(start,sdPath.length());
            	//Log.d("imgname", imgname);
                Bitmap bitmapOrg = BitmapFactory.decodeFile(sdPath);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                 
                //Resize the image
                double width = bitmapOrg.getWidth();
                double height = bitmapOrg.getHeight();
                double ratio = 600/width;
                int height2 = (int)(ratio*height);

             //   System.out.println("���-width" + width);
               // System.out.println("���-height" + height);
                 
                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 600, height2, true);
                 
                //Here you can define .PNG as well
                bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 75, bao);
                byte[] ba = bao.toByteArray();
                //String ba1 = Base64.encodeBytes(ba);
               // System.out.println("1234" + height);
                String ba1 = Base64.encodeToString(ba, 0);
                //System.out.println("4321" + height);
                //System.out.println("uploading image now ���" + ba1);
                 
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("image", ba1));
                nameValuePairs.add(new BasicNameValuePair("imgname", imgnm));
                 
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    //HttpPost httppost = new HttpPost("http://path to the image upload .php file/api.upload.php");
                    
                    //HttpPost httppost = new HttpPost("http://192.168.1.111/Upload_image_ANDROID/uploads/upload_image.php");
                    HttpPost httppost = new HttpPost("http://perfectsoftware.co.in/s2/uploads/upload_image.php");
                    
                    
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                     
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();                
 
                    // print responce
                    outPut = EntityUtils.toString(entity);
                    Log.i("GET RESPONSE�-", outPut);

                    Log.e("log_tag ******", "good connection");
                     
                    bitmapOrg.recycle();
                     
                } catch (Exception e) {
                    Log.e("log_tag ******", "Error in http connection " + e.toString());
                }
            }
            return outPut;
        }
   }