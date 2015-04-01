package com.example.hw9;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

@SuppressLint("ResourceAsColor") public class ResultActivity extends ActionBarActivity {
	private String homedetails;
	private String street;
	private String city;
	private String state;
	private String zipcode;
	private String useCode;
	private String lastSoldPrice;
	private String yearBuilt;
	private String lastSoldDate;
	private String lotSizeSqFt;
	private String estimateLastUpdate;
	private String estimateAmount;
	private String finishedSqFt;
	private String estimateValueChangeSign;
	private String estimateValueChange;
	private String bathrooms;
	private String estimateValuationRangeLow;
	private String estimateValuationRangeHigh;
	private String bedrooms;
	private String restimateLastUpdate;
	private String restimateAmount;
	private String taxAssessmentYear;
	private String restimateValueChangeSign;
	private String restimateValueChange;
	private String taxAssessment;
	private String restimateValuationRangeLow;
	private String restimateValuationRangeHigh;
	// private String imgn;
	// private String imgp;
	private String year1;
	private String years5;
	private String years10;
	private ArrayList<Bitmap> bitmapList = new ArrayList<Bitmap>();
	
	private  ArrayList<Bitmap> bmlist = new ArrayList<Bitmap>();

	
	TextSwitcher textSwitcher ;
	 ImageSwitcher imgSwitcher;
	// private ImageSwitcher imgSwitcher;
	// private TextSwitcher textSwitcher;
	// private Button btnPre;
	// private Button btnNext;
//	private Handler handler;
	
	private Handler handler1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		// parse JSON
		Intent intent = this.getIntent();
		String result = intent.getStringExtra("JSON");
		parseResults(result);

		// create Tab
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		addTabs(actionBar);
		
		
		// get Picture
		parseCharts();
		
		// show result
		showResults();
		initChart();
		

		// show footer
		showInfo();
		
		try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "com.example.hw9", 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	        }
	    } catch (NameNotFoundException e) {

	    } catch (NoSuchAlgorithmException e) {

	    }
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

	    super.onActivityResult(requestCode, resultCode, data);

	    Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);

	}

	private void initChart(){
		textSwitcher= (TextSwitcher) findViewById(R.id.textSwitcher);
		imgSwitcher = (ImageSwitcher) findViewById(R.id.imgSwitcher);
		// set Image Switcher
		imgSwitcher.setFactory(new ViewFactory() {

			@Override
			public View makeView() {
				// TODO Auto-generated method stub
				ImageView imageView = new ImageView(ResultActivity.this);
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
						ImageSwitcher.LayoutParams.MATCH_PARENT,
						ImageSwitcher.LayoutParams.MATCH_PARENT));

				return imageView;
			}
		});
		
		textSwitcher.setFactory(new ViewFactory() {

			@Override
			public View makeView() {
				// TODO Auto-generated method stub
				TextView textView = new TextView(ResultActivity.this);
				textView.setGravity(Gravity.CENTER);
				return textView;
			}
		});

	}
	private void addTabs(final ActionBar actionBar) {
		ActionBar.Tab tab1 = actionBar.newTab();
		tab1.setText(R.string.tab1);
		tab1.setTabListener(new myTabListenser());

		final ActionBar.Tab tab2 = actionBar.newTab();
		tab2.setText(R.string.tab2);
		tab2.setTabListener(new myTabListenser());

		actionBar.addTab(tab1);
		
		
		
		handler1 = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what==0x123){
//					bitmapList = (ArrayList<Bitmap>) msg.obj;
					actionBar.addTab(tab2);
				}
			}
		};
		
	}

	private boolean bInit = true;
	// tab Listenser
	class myTabListenser implements TabListener {

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			if (tab.getText().equals("BASIC INFO")) {
				// show table
				setLayoutVisible(true);
				
			} else if (tab.getText().equals("HISTORICAL ZESTIMATES")) {
				// show charts
				setLayoutVisible(false);
				if(bInit == true){
					showCharts();
					bInit = false;
				}
			}

		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}

	}

	// parse JSON function
	private void parseResults(String result) {
		try {
			JSONObject jObject1 = new JSONObject(result);
			JSONObject jObject = new JSONObject();
			// JSONObject jObject = new JSONObject(result);
			jObject = jObject1.getJSONObject("result");
			homedetails = jObject.getString("homedetails");
			street = jObject.getString("street");
			city = jObject.getString("city");
			state = jObject.getString("state");
			zipcode = jObject.getString("zipcode");
			useCode = jObject.getString("useCode");
			lastSoldPrice = jObject.getString("lastSoldPrice");
			yearBuilt = jObject.getString("yearBuilt");
			lastSoldDate = jObject.getString("lastSoldDate");
			lotSizeSqFt = jObject.getString("lotSizeSqFt");
			estimateLastUpdate = jObject.getString("estimateLastUpdate");
			estimateAmount = jObject.getString("estimateAmount");
			finishedSqFt = jObject.getString("finishedSqFt");
			estimateValueChangeSign = jObject
					.getString("estimateValueChangeSign");
			estimateValueChange = jObject.getString("estimateValueChange");
			bathrooms = jObject.getString("bathrooms");
			estimateValuationRangeLow = jObject
					.getString("estimateValuationRangeLow");
			estimateValuationRangeHigh = jObject
					.getString("estimateValuationRangeHigh");
			bedrooms = jObject.getString("bedrooms");
			restimateLastUpdate = jObject.getString("restimateLastUpdate");
			restimateAmount = jObject.getString("restimateAmount");
			taxAssessmentYear = jObject.getString("taxAssessmentYear");
			restimateValueChangeSign = jObject
					.getString("restimateValueChangeSign");
			restimateValueChange = jObject.getString("restimateValueChange");
			taxAssessment = jObject.getString("taxAssessment");
			restimateValuationRangeLow = jObject
					.getString("restimateValuationRangeLow");
			restimateValuationRangeHigh = jObject
					.getString("restimateValuationRangeHigh");
			// imgn = jObject.getString("imgn");
			// imgp = jObject.getString("imgp");

			// JSONObject chart=new JSONObject();
			jObject = jObject1.getJSONObject("chart");
			year1 = jObject.getJSONObject("year1").getString("url");
			years5 = jObject.getJSONObject("years5").getString("url");
			years10 = jObject.getJSONObject("years10").getString("url");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void publishfeed(){
		Bundle params = new Bundle();
		String address = street +", " + city +", " + state +"-" +zipcode;
		params.putString("name", address);
	    params.putString("caption", "Property information from Zillow.com");
		String description = "Last Sold Price:" + lastSoldPrice +", 30 Days Overall Change:" + estimateValueChangeSign + estimateValueChange;
	    params.putString("description", description);
	    params.putString("link", homedetails);
	    params.putString("picture", year1);
	    WebDialog feedDialog = (
		        new WebDialog.FeedDialogBuilder(ResultActivity.this,
		            Session.getActiveSession(),
		            params))
		        .setOnCompleteListener(new OnCompleteListener() {

		            @Override
		            public void onComplete(Bundle values,
		                FacebookException error) {
		                if (error == null) {
		                    // When the story is posted, echo the success
		                    // and the post Id.
		                    final String postId = values.getString("post_id");
		                    if (postId != null) {
		                        Toast.makeText(ResultActivity.this,
		                            "Posted story, id: "+postId,
		                            Toast.LENGTH_SHORT).show();
		                    } else {
		                        // User clicked the Cancel button
		                        Toast.makeText(ResultActivity.this.getApplicationContext(), 
		                            "Publish cancelled", 
		                            Toast.LENGTH_SHORT).show();
		                    }
		                } else if (error instanceof FacebookOperationCanceledException) {
		                    // User clicked the "x" button
		                    Toast.makeText(ResultActivity.this.getApplicationContext(), 
		                        "Publish cancelled", 
		                        Toast.LENGTH_SHORT).show();
		                } else {
		                    // Generic, ex: network error
		                    Toast.makeText(ResultActivity.this.getApplicationContext(), 
		                        "Error posting story", 
		                        Toast.LENGTH_SHORT).show();
		                }
		            }

		        })
		        .build();
		    feedDialog.show();
	}

	// get Pictures From url
	private void parseCharts() {
		DownloadImgTask myTask = new DownloadImgTask();
		myTask.execute(year1, years5, years10);
	}

	// set visibility
	private void setLayoutVisible(boolean bRst) {
		ScrollView sView = (ScrollView) findViewById(R.id.scrollViewRst);
		TextSwitcher textSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);
		ImageSwitcher imgSwitcher = (ImageSwitcher) findViewById(R.id.imgSwitcher);
		Button btnPre = (Button) findViewById(R.id.btnPre);
		Button btnNext = (Button) findViewById(R.id.btnNext);
		if (bRst == true) {
			// Show Rst Table
			sView.setVisibility(View.VISIBLE);
			textSwitcher.setVisibility(View.INVISIBLE);
			imgSwitcher.setVisibility(View.INVISIBLE);
			btnPre.setVisibility(View.INVISIBLE);
			btnNext.setVisibility(View.INVISIBLE);
		} else {
			// Show chart
			sView.setVisibility(View.INVISIBLE);
			textSwitcher.setVisibility(View.VISIBLE);
			imgSwitcher.setVisibility(View.VISIBLE);
			btnPre.setVisibility(View.VISIBLE);
			btnNext.setVisibility(View.VISIBLE);
		}
	}

	// Show results
	private  void showResults() {
		// set visible

		TableLayout rstTable = (TableLayout) findViewById(R.id.tableRst);
		rstTable.setShrinkAllColumns(true);
		TableRow.LayoutParams lp = new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT,
				TableRow.LayoutParams.WRAP_CONTENT);
		
		// Row 1
		TableRow tb1 = new TableRow(this);
		tb1.setLayoutParams(lp);
		TextView text = new TextView(this);
		text.setGravity(Gravity.LEFT);
		text.setText("See more details on Zillows:");
		Button share = new Button(this);
		share.setBackgroundResource(R.drawable.fb);
		share.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(ResultActivity.this)  
				.setTitle("Post to Facebook")  
				.setPositiveButton("Post Property Details", new DialogInterface.OnClickListener() {      
				    @Override  
				    public void onClick(DialogInterface dialog, int which) {  
				    	Session.StatusCallback ssCallback = new Session.StatusCallback() {

							@Override
							public void call(Session session,SessionState state, Exception exception) {
								// TODO Auto-generated method stub
								if (session.isOpened()) {
									Request.newMeRequest(session, new Request.GraphUserCallback() {

										@Override
										public void onCompleted(
												GraphUser user,
												Response response) {
											// TODO Auto-generated method stub
												publishfeed();
										}
									}).executeAsync();
								}
								else if(session.getState() == SessionState.CLOSED_LOGIN_FAILED){
									int i = 1;
								}
							}
				    		
				    	};
				    	/*
				    	Session.OpenRequest request = new Session.OpenRequest(ResultActivity.this);
				    	request.setCallback(ssCallback );
				    	Session mFacebookSession = Session.getActiveSession();
				    	if (mFacebookSession == null || mFacebookSession.isClosed()) 
				    	{
				    	    mFacebookSession = new Session(ResultActivity.this);
				    	}
				    	mFacebookSession.openForRead(request);*/
				    	Session.openActiveSession(ResultActivity.this, true, ssCallback );
				    }  
				})  
				.setNegativeButton("Cancel",null )  
				.show();  
			}
		});
		share.setGravity(Gravity.LEFT);
		tb1.addView(text);
		tb1.addView(share);
		rstTable.addView(tb1);
		//underline
		TextView line1 = new TextView(this);
		line1.setBackgroundColor(R.color.black);
		line1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		rstTable.addView(line1);
		// Row 2
		TableRow tb2 = new TableRow(this);
		tb2.setLayoutParams(lp);
		TextView link = new TextView(this);
		SpannableString sp = new SpannableString(street + ", " + city + ", "
				+ state + "-" + zipcode);
		sp.setSpan(new URLSpan(homedetails), 0, sp.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		link.setText(sp);
		link.setMovementMethod(LinkMovementMethod.getInstance());
		link.setGravity(Gravity.LEFT);
		tb2.addView(link);
		rstTable.addView(tb2);
		
		//underline
		TextView line2 = new TextView(this);
		line2.setBackgroundColor(R.color.black);
		line2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		rstTable.addView(line2);
		// Row3 useCode
		TableRow tb3 = new TableRow(this);
		TextView typeTittle = new TextView(this);
		typeTittle.setText("Property Type");
		typeTittle.setGravity(Gravity.LEFT);
		typeTittle.setSingleLine(false);
		typeTittle.setWidth(50);
		TextView type = new TextView(this);
		type.setText(useCode);
		type.setGravity(Gravity.LEFT);
		 tb3.addView(typeTittle);
		tb3.addView(type);
		tb3.setBackgroundColor(Color.rgb(246, 246, 254));
		rstTable.addView(tb3);
		
		//underline
		TextView line3 = new TextView(this);
		line3.setBackgroundColor(R.color.black);
		line3.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		rstTable.addView(line3);
		// Row 4 yearBuilt
		TableRow tb4 = new TableRow(this);
		TextView yearBuildTitle = new TextView(this);
		yearBuildTitle.setText("Year Built");
		yearBuildTitle.setGravity(Gravity.LEFT);
		TextView yearBuildText = new TextView(this);
		yearBuildText.setText(yearBuilt);
		yearBuildText.setGravity(Gravity.LEFT);
		tb4.addView(yearBuildTitle);
		tb4.addView(yearBuildText);
		rstTable.addView(tb4);
		
		//underline
		TextView line4 = new TextView(this);
		line4.setBackgroundColor(R.color.black);
		line4.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		rstTable.addView(line4);
		// Row 5 lotSizeSqFt
		TableRow tb5 = new TableRow(this);
		TextView sizeTittle = new TextView(this);
		sizeTittle.setText("Lot Size");
		sizeTittle.setGravity(Gravity.LEFT);
		TextView size = new TextView(this);
		size.setText(lotSizeSqFt);
		size.setGravity(Gravity.LEFT);
		tb5.addView(sizeTittle);
		tb5.addView(size);
		tb5.setBackgroundColor(Color.rgb(246, 246, 254));
		rstTable.addView(tb5);
		
		//underline
		TextView line5 = new TextView(this);
		line5.setBackgroundColor(R.color.black);
		line5.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		rstTable.addView(line5);
		// Row 6 finished area
		TableRow tb6 = new TableRow(this);
		TextView finishedTittle = new TextView(this);
		finishedTittle.setText("Finished Area");
		finishedTittle.setGravity(Gravity.LEFT);
		TextView finished = new TextView(this);
		finished.setText(finishedSqFt);
		finished.setGravity(Gravity.LEFT);
		tb6.addView(finishedTittle);
		tb6.addView(finished);
		rstTable.addView(tb6);
		
		//underline
		TextView line6 = new TextView(this);
		line6.setBackgroundColor(R.color.black);
		line6.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		rstTable.addView(line6);
		// Row 7 Bathrooms
		TableRow tb7 = new TableRow(this);
		TextView bathroomsTittle = new TextView(this);
		bathroomsTittle.setText("Bathrooms");
		bathroomsTittle.setGravity(Gravity.LEFT);
		TextView bathroom = new TextView(this);
		bathroom.setText(bathrooms);
		bathroom.setGravity(Gravity.LEFT);
		tb7.addView(bathroomsTittle);
		tb7.addView(bathroom);
		tb7.setBackgroundColor(Color.rgb(246, 246, 254));
		rstTable.addView(tb7);
		
		//underline
		TextView line7 = new TextView(this);
		line7.setBackgroundColor(R.color.black);
		line7.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		rstTable.addView(line7);
		// Row 8 bedroom
		TableRow tb8 = new TableRow(this);
		TextView bedroomsTittle = new TextView(this);
		bedroomsTittle.setText("Bedrooms");
		bedroomsTittle.setGravity(Gravity.LEFT);
		TextView bedroom = new TextView(this);
		bedroom.setText(bedrooms);
		bedroom.setGravity(Gravity.LEFT);
		tb8.addView(bedroomsTittle);
		tb8.addView(bedroom);
		rstTable.addView(tb8);
		
		//underline
		TextView line8 = new TextView(this);
		line8.setBackgroundColor(R.color.black);
		line8.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		rstTable.addView(line8);
		// Row 9 taxAssessmentYear
		TableRow tb9 = new TableRow(this);
		TextView taxYearTittle = new TextView(this);
		taxYearTittle.setText("Tax Assessment Year");
		taxYearTittle.setGravity(Gravity.LEFT);
		TextView taxYear = new TextView(this);
		taxYear.setText(taxAssessmentYear);
		taxYear.setGravity(Gravity.LEFT);
		tb9.addView(taxYearTittle);
		tb9.addView(taxYear);
		tb9.setBackgroundColor(Color.rgb(246, 246, 254));
		rstTable.addView(tb9);
		
		//underline
		TextView line9 = new TextView(this);
		line9.setBackgroundColor(R.color.black);
		line9.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		rstTable.addView(line9);
		// Row 10 taxAssessment
		TableRow tb10 = new TableRow(this);
		TextView taxTittle = new TextView(this);
		taxTittle.setText("Tax Assessment");
		taxTittle.setGravity(Gravity.LEFT);
		TextView tax = new TextView(this);
		tax.setText(taxAssessment);
		tax.setGravity(Gravity.LEFT);
		tb10.addView(taxTittle);
		tb10.addView(tax);
		rstTable.addView(tb10);
		
		//underline
		TextView line10 = new TextView(this);
		line10.setBackgroundColor(R.color.black);
		line10.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		rstTable.addView(line10);
		// Row 11 lastSoldPrice
		TableRow tb11 = new TableRow(this);
		TextView lastSoldTittle = new TextView(this);
		lastSoldTittle.setText("Last Sold Price");
		lastSoldTittle.setGravity(Gravity.LEFT);
		TextView lastPrice = new TextView(this);
		lastPrice.setText(lastSoldPrice);
		lastPrice.setGravity(Gravity.LEFT);
		tb11.addView(lastSoldTittle);
		tb11.addView(lastPrice);
		tb11.setBackgroundColor(Color.rgb(246, 246, 254));
		rstTable.addView(tb11);
		
		//underline
		TextView line11 = new TextView(this);
		line11.setBackgroundColor(R.color.black);
		line11.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		rstTable.addView(line11);
		// Row 12 lastPriceDate
		TableRow tb12 = new TableRow(this);
		TextView lastSoldDateTittle = new TextView(this);
		lastSoldDateTittle.setText("Last Sold Date");
		lastSoldDateTittle.setGravity(Gravity.LEFT);
		TextView lastPriceDateText = new TextView(this);
		lastPriceDateText.setText(lastSoldDate);
		lastPriceDateText.setGravity(Gravity.LEFT);
		tb12.addView(lastSoldDateTittle);
		tb12.addView(lastPriceDateText);
		rstTable.addView(tb12);
		
		//underline
		TextView line12 = new TextView(this);
		line12.setBackgroundColor(R.color.black);
		line12.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		rstTable.addView(line12);
		// Row 13 estimateAmount
		TableRow tb13 = new TableRow(this);
		TextView estimateUpdateTittle = new TextView(this);
		char rightSymbol = 169;
		estimateUpdateTittle.setText("Zestimate " + String.valueOf(rightSymbol) +
		 " Property Estimate as of " + estimateLastUpdate);
		estimateUpdateTittle.setGravity(Gravity.LEFT);
		TextView estimateUpdate = new TextView(this);
		estimateUpdate.setText(estimateAmount);
		estimateUpdate.setGravity(Gravity.LEFT);
		tb13.addView(estimateUpdateTittle);
		tb13.addView(estimateUpdate);
		tb13.setBackgroundColor(Color.rgb(246, 246, 254));
		rstTable.addView(tb13);
		
		//underline
		TextView line13 = new TextView(this);
		line13.setBackgroundColor(R.color.black);
		line13.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		rstTable.addView(line13);
		// Row 14 estimateValueChange
		TableRow tb14 = new TableRow(this);
		TextView allChangeTittle = new TextView(this);
		allChangeTittle.setText("30 Days Overall Change");
		allChangeTittle.setGravity(Gravity.LEFT);
		//ImageView valueChangeImg = new ImageView(this);
		ImageSpan imgUpValue = new ImageSpan(this, R.drawable.up_g);
		ImageSpan imgDownValue = new ImageSpan(this, R.drawable.down_r);
		SpannableString ValueSpan = new SpannableString(" ");
		if (estimateValueChangeSign.equals("+")) {
			ValueSpan.setSpan(imgUpValue, ValueSpan.length() - 1, ValueSpan.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			//valueChangeImg.setImageResource(R.drawable.up_g);
		} else {
			ValueSpan.setSpan(imgDownValue, ValueSpan.length() - 1, ValueSpan.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			//valueChangeImg.setImageResource(R.drawable.down_r);
		}
		TextView allChange = new TextView(this);
		allChange.setText(ValueSpan);
		allChange.append(estimateValueChange);
		allChange.setGravity(Gravity.LEFT);
		tb14.addView(allChangeTittle);
		//tb14.addView(valueChangeImg);
		tb14.addView(allChange);
		rstTable.addView(tb14);
		
		//underline
		TextView line14 = new TextView(this);
		line14.setBackgroundColor(R.color.black);
		line14.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		rstTable.addView(line14);
		// Row 15
		TableRow tb15 = new TableRow(this);
		TextView propertyChangeTittle = new TextView(this);
		propertyChangeTittle.setText("All Time Property Change");
		propertyChangeTittle.setGravity(Gravity.LEFT);
		TextView propertyChange = new TextView(this);
		propertyChange.setText(estimateValuationRangeLow + " - "
				+ estimateValuationRangeHigh);
		propertyChange.setGravity(Gravity.LEFT);
		tb15.addView(propertyChangeTittle);
		tb15.addView(propertyChange);
		tb15.setBackgroundColor(Color.rgb(246, 246, 254));
		rstTable.addView(tb15);
		
		//underline
		TextView line15 = new TextView(this);
		line15.setBackgroundColor(R.color.black);
		line15.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		rstTable.addView(line15);
		// Row 16
		TableRow tb16 = new TableRow(this);
		TextView rentUpdateTittle = new TextView(this);
		char Symbol = 174;
		 rentUpdateTittle.setText("Rent Zestimate " + String.valueOf(Symbol) +
		 " Values as of " + restimateLastUpdate);
		rentUpdateTittle.setGravity(Gravity.LEFT);
		TextView rentUpdate = new TextView(this);
		rentUpdate.setText(restimateAmount);
		rentUpdate.setGravity(Gravity.LEFT);
		tb16.addView(rentUpdateTittle);
		tb16.addView(rentUpdate);
		rstTable.addView(tb16);
		
		//underline
		TextView line16 = new TextView(this);
		line16.setBackgroundColor(R.color.black);
		line16.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		rstTable.addView(line16);
		// Row 17 estimateValueChange
		TableRow tb17 = new TableRow(this);
		TextView rentTittle = new TextView(this);
		rentTittle.setText("30 Days Rent Change");
		rentTittle.setGravity(Gravity.LEFT);
		//ImageView rentImg = new ImageView(this);
		ImageSpan imgUpRent = new ImageSpan(this, R.drawable.up_g);
		ImageSpan imgDownRent = new ImageSpan(this, R.drawable.down_r);
		SpannableString rentSpan = new SpannableString(" ");
		
		if (restimateValueChangeSign.equals("+")) {
			rentSpan.setSpan(imgUpRent, rentSpan.length() - 1, rentSpan.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			//rentImg.setImageResource(R.drawable.up_g);
		} else {
			rentSpan.setSpan(imgDownRent, rentSpan.length() - 1, rentSpan.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			//rentImg.setImageResource(R.drawable.down_r);
		}
		TextView rent = new TextView(this);
		rent.setGravity(Gravity.LEFT);
		rent.setText(rentSpan);
		rent.append(restimateValueChange);
		tb17.addView(rentTittle);
		//tb17.addView(rentImg);
		tb17.addView(rent);
		tb17.setBackgroundColor(Color.rgb(246, 246, 254));
		rstTable.addView(tb17);
		
		//underline
		TextView line17 = new TextView(this);
		line17.setBackgroundColor(R.color.black);
		line17.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		rstTable.addView(line17);
		// Row 18
		TableRow tb18 = new TableRow(this);
		TextView rentChangeTittle = new TextView(this);
		rentChangeTittle.setText("All Time Rent Change");
		rentChangeTittle.setGravity(Gravity.LEFT);
		TextView rentChange = new TextView(this);
		rentChange.setText(restimateValuationRangeLow + " - "
				+ restimateValuationRangeHigh);
		rentChange.setGravity(Gravity.LEFT);
		tb18.addView(rentChangeTittle);
		tb18.addView(rentChange);
		rstTable.addView(tb18);
		//underline
		TextView line18 = new TextView(this);
		line18.setBackgroundColor(R.color.black);
		line18.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		rstTable.addView(line18);
	}

	
	
	
	// Show Charts
	private void showCharts() {
		// set visibility
		
		
		TextSwitcher textSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);
		final ImageSwitcher imgSwitcher = (ImageSwitcher) findViewById(R.id.imgSwitcher);
/*
		// set Image Switcher
		imgSwitcher.setFactory(new ViewFactory() {

			@Override
			public View makeView() {
				// TODO Auto-generated method stub
				ImageView imageView = new ImageView(ResultActivity.this);
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
						ImageSwitcher.LayoutParams.WRAP_CONTENT,
						ImageSwitcher.LayoutParams.WRAP_CONTENT));

				return imageView;
			}
		});
*/
		imgSwitcher.setImageDrawable(new BitmapDrawable(bitmapList.get(0)));

/*		textSwitcher.setFactory(new ViewFactory() {

			@Override
			public View makeView() {
				// TODO Auto-generated method stub
				TextView textView = new TextView(ResultActivity.this);
				textView.setGravity(Gravity.CENTER);
				return textView;
			}
		});
*/
		textSwitcher.setText("Historical Zestimate for the past 1 year\n"
				+ street + ", " + city + ", " + state + "-" + zipcode);
	}

	private int currentIndex = 0;

	// click previous
	public void onLeft(View v) {
		TextSwitcher textSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);
		ImageSwitcher imgSwitcher = (ImageSwitcher) findViewById(R.id.imgSwitcher);
		Button btnPre = (Button) findViewById(R.id.btnPre);
		String[] year = new String[] { "1 year", "5 years", "10 years" };
		if (currentIndex == 0) {
			currentIndex = 2;
		} else {
			currentIndex--;
		}
		imgSwitcher.setImageDrawable(new BitmapDrawable(bitmapList
				.get(currentIndex)));
		textSwitcher.setText("Historical Zestimate for the past "
				+ year[currentIndex] + "\n" + street + ", " + city + ", "
				+ state + "-" + zipcode);
	}

	// click next
	public void onRight(View v) {
		TextSwitcher textSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);
		ImageSwitcher imgSwitcher = (ImageSwitcher) findViewById(R.id.imgSwitcher);
		Button btnNext = (Button) findViewById(R.id.btnNext);
		String[] year = new String[] { "1 year", "5 years", "10 years" };
		if (currentIndex == 2) {
			currentIndex = 0;
		} else {
			currentIndex++;
		}
		imgSwitcher.setImageDrawable(new BitmapDrawable(bitmapList
				.get(currentIndex)));
		textSwitcher.setText("Historical Zestimate for the past "
				+ year[currentIndex] + "\n" + street + ", " + city + ", "
				+ state + "-" + zipcode);
	}

	private class DownloadImgTask extends
			AsyncTask<String, Void, ArrayList<Bitmap>> {
		// private class DownloadImgTask extends AsyncTask<String, Void,
		// Bitmap>{

		@Override
		protected ArrayList<Bitmap> doInBackground(String... params) {
			// protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			// Bitmap bmp = null;
			Bitmap bmp = null;
			
			for (int i = 0; i < params.length; i++) {
				try {
					URL ulrn = new URL(params[i]);
					HttpURLConnection con = (HttpURLConnection) ulrn.openConnection();
					InputStream is = con.getInputStream();
					bmp = BitmapFactory.decodeStream(is);
					if (null != bmp) {
						bmlist.add(bmp);
						
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			return bmlist;
		}

		@Override
		// protected void onPostExecute(Bitmap result){
		protected void onPostExecute(ArrayList<Bitmap> bmlist) {
			
			bitmapList=bmlist;
			Message msg = new Message();
			msg.what = 0x123;
			handler1.sendMessage(msg);
			
			
		}

	}

	// show foot
	public void showInfo() {
		TextView term = (TextView) findViewById(R.id.term);
		TextView what = (TextView) findViewById(R.id.what);
		SpannableString termsp = new SpannableString("Terms of Use");
		termsp.setSpan(new URLSpan("http://www.zillow.com/corp/Terms.htm"), 0,
				termsp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		term.append(termsp);
		term.setMovementMethod(LinkMovementMethod.getInstance());
		SpannableString whatsp = new SpannableString("What's a Zestimate?");
		whatsp.setSpan(new URLSpan("http://www.zillow.com/zestimate/"), 0,
				whatsp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		what.setText(whatsp);
		what.setMovementMethod(LinkMovementMethod.getInstance());
	}

}
