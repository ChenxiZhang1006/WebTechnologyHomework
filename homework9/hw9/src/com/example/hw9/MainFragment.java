package com.example.hw9;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

import android.R.layout;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainFragment extends Activity {
	private Bitmap pic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay(); 
		WindowManager.LayoutParams p = getWindow().getAttributes(); 
		p.height = (int) (d.getHeight() * 0.4); 
		p.width = (int) (d.getWidth() * 0.7); 
		p.alpha = 1.0f; 
		p.dimAmount = 0.0f; 
		getWindow().setAttributes(p); 
		getWindow().setGravity(Gravity.CENTER);
		
		//create();
		setContentView(R.layout.activity_mainfragment);
	}
	
	private void delete(){
		finish();
	}
	
	private void shareDialog() {
	    Bundle params = new Bundle();
	    params.putString("message", "Learn how to make your Android apps social");

	    WebDialog shareDialog = (
	        new WebDialog.RequestsDialogBuilder(this,
	            Session.getActiveSession(),
	            params))
	            .setOnCompleteListener(new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						// TODO Auto-generated method stub
						if (error != null) {
	                        if (error instanceof FacebookOperationCanceledException) {
	                            Toast.makeText(null, 
	                                "Request cancelled", 
	                                Toast.LENGTH_SHORT).show();
	                        } else {
	                            Toast.makeText(null, 
	                                "Network Error", 
	                                Toast.LENGTH_SHORT).show();
	                        }
	                    } else {
	                        final String requestId = values.getString("request");
	                        if (requestId != null) {
	                            Toast.makeText(null, 
	                                "Request sent",  
	                                Toast.LENGTH_SHORT).show();
	                        } else {
	                            Toast.makeText(null, 
	                                "Request cancelled", 
	                                Toast.LENGTH_SHORT).show();
	                        }
	                    } 
					}

	            })
	            .build();
	    shareDialog.show();
	}
	private class DownloadImgTask extends AsyncTask<String, Void, Bitmap> {
		
		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			Bitmap bmp = null;
			try {
				URL ulrn = new URL(params[0]);
				HttpURLConnection con = (HttpURLConnection) ulrn.openConnection();
				InputStream is = con.getInputStream();
				bmp = BitmapFactory.decodeStream(is);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bmp;
		}
		
		@Override
		// protected void onPostExecute(Bitmap result){
		protected void onPostExecute(Bitmap bmp) {
			pic = bmp;
			
			
		}
	
	}
}