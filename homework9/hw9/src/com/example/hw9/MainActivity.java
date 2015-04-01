package com.example.hw9;

import java.io.ByteArrayOutputStream;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	private Spinner state_spinner;
	private ArrayAdapter<String> state_array_adapter;
	private TextView state_alarm_text;
	private String state;

	private EditText addr_editText;
	private TextView addr_alarm_text;
	private String address;

	private EditText city_editText;
	private TextView city_alarm_text;
	private String city;

	private Button srh_btn;

	private TextView noRst_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// add values to spinner
		state_spinner = (Spinner) findViewById(R.id.spinnerState);
		String[] states = getResources().getStringArray(R.array.state_list);
		state_array_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, states);
		state_array_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		state_spinner.setAdapter(state_array_adapter);
		// State Listen
		state_alarm_text = (TextView) findViewById(R.id.textStateAlarm);
		state_spinner.setSelection(0, true);
		state_spinner.setOnItemSelectedListener(state_OnItemSelectedListener);

		// Listen EditText
		// Address
		addr_editText = (EditText) findViewById(R.id.editAddress);
		addr_alarm_text = (TextView) findViewById(R.id.textAddrAlarm);
		addr_editText.addTextChangedListener(addr_textWatcher);

		// city
		city_editText = (EditText) findViewById(R.id.editCity);
		city_alarm_text = (TextView) findViewById(R.id.textCityAlarm);
		city_editText.addTextChangedListener(city_textWatcher);

//		 button
		 srh_btn = (Button)findViewById(R.id.btnSearch);

		// no Result
		noRst_text = (TextView) findViewById(R.id.textNoRst);
	}

	// Address Listener
	private TextWatcher addr_textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if (s.length() == 0) {
				addr_alarm_text.setVisibility(View.VISIBLE);
			} else {
				addr_alarm_text.setVisibility(View.INVISIBLE);
			}
		}
	};

	// City Listener
	private TextWatcher city_textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if (s.length() == 0) {
				city_alarm_text.setVisibility(View.VISIBLE);
			} else {
				city_alarm_text.setVisibility(View.INVISIBLE);
			}
		}
	};

	// State Listener
	private OnItemSelectedListener state_OnItemSelectedListener = new OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			if (state_spinner.getSelectedItem().toString().equals("--") == true) {
				state_alarm_text.setVisibility(View.VISIBLE);
			} else {
				state_alarm_text.setVisibility(View.INVISIBLE);
			}
		}

		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// AsyncTask
	private class myTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			String resultString = null;
			String url = params[0];
			url += "streetInput=" + params[1].replaceAll(" ", "%20") +
					"&cityInput=" + params[2].replaceAll(" ", "%20") + 
					"&stateInput=" + params[3].replaceAll(" ", "%20");
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				// excute get method
				HttpResponse response = client.execute(get);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					//
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					resultString = out.toString();
					out.close();
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println(resultString);
			// return resultString;
			return resultString;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.length() == 0) {
				noRst_text.setVisibility(View.VISIBLE);
			} else {
				// transfer data to next activity-ResultActivity
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ResultActivity.class);
				intent.putExtra("JSON", result);
				startActivityForResult(intent, 0);
			}
			srh_btn.setEnabled(true);
		}
	}

	// self Define Function
	// Srh btn click
	public void Validate(View v) {
		// TODO Auto-generated method stub
		address = addr_editText.getText().toString();
		city = city_editText.getText().toString();
		state = state_spinner.getSelectedItem().toString();

		// /////////////////////////
		noRst_text.setVisibility(View.INVISIBLE);

		if (address.length() == 0) {
			addr_alarm_text.setVisibility(View.VISIBLE);
		}
		if (city.length() == 0) {
			city_alarm_text.setVisibility(View.VISIBLE);
		}
		if (state.equals("--") == true) {
			state_alarm_text.setVisibility(View.VISIBLE);
		}

		if (address.equals("") == false && state.equals("--") == false
				&& city.equals("") == false) {
			String url = "http://default-environment-akusfbpazd.elasticbeanstalk.com/?";
			myTask getJson = new myTask();
			getJson.execute(url, address, city, state);

			// disable search button, avoid double search
			 srh_btn.setEnabled(false);
		}
	}
}
