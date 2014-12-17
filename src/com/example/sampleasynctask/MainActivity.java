package com.example.sampleasynctask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

public class MainActivity extends Activity implements OnItemClickListener {

	EditText editText;
	ListView list;
	String[] listofImages;
	ProgressBar progressbar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		editText = (EditText) findViewById(R.id.editText1);
		list = (ListView) findViewById(R.id.listView1);
		listofImages = getResources().getStringArray(R.array.imageurls);
		progressbar = (ProgressBar) findViewById(R.id.progressBar1);
		list.setOnItemClickListener(this);
	}

	public void downloadImage(View view){

		if(editText.getText().toString()!=null && editText.getText().toString().length()>0)
		{
			Log.d("Main Activity","inside download images 1");
			MyTask myTask = new MyTask();
			Log.d("Main Activity","inside download images 2");

			//	myTask.execute(editText.getText().toString());
			myTask.execute("http://www.9ori.com/store/media/images/8ab579a656.jpg");
			Log.d("Main Activity","inside download images 3");


		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.d("Main Activity","inside onitemclick");

		editText.setText(listofImages[position]);
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





	class MyTask extends AsyncTask<String, Integer, Boolean>{


		private int contentLength=-1;
		private int counter=0;
		private int calculatedProgress=0;

		@Override
		protected void onPreExecute() {
			progressbar.setVisibility(View.VISIBLE);
		}

		@SuppressWarnings({ "resource", "null" })
		@Override
		protected Boolean doInBackground(String params[]) {
			boolean successful = false;
			URL downloadURL = null;

			HttpURLConnection connection = null;
			InputStream inputStream = null;
			FileOutputStream outputStream = null;
			File file = null;
			try{
				downloadURL = new URL(params[0]);
				connection = (HttpURLConnection) downloadURL.openConnection();
				contentLength= connection.getContentLength();
				inputStream = connection.getInputStream();
				file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+Uri.parse(params[0]).getLastPathSegment());
				outputStream = new FileOutputStream(file);
				Logfile.main(""+file.getAbsolutePath());
				int read = -1;
				byte[] buffer = new byte[1024];
				while((read=inputStream.read(buffer))!=-1)
				{

					outputStream.write(buffer,0,read);
					counter=counter+read;
					Logfile.main("counter"+counter+ "length"+contentLength);
					publishProgress(counter);
				}
				successful = true;
			}
			catch (MalformedURLException e) {
				// TODO: handle exception
				Logfile.main(e+"");
			}
			catch (IOException e1){
				Logfile.main(e1+"");
			}

			finally {
				this.runOnUiThread(new Runnable()
				{

					@Override
					public void run() {
						// TODO Auto-generated method stub
						//		loadingSection.setVisibility(View.GONE);
					}


				});

				if(connection!=null){
					connection.disconnect();

				}

				if(inputStream!=null){
					try{
						inputStream.close();

					}
					catch(IOException e){
						Logfile.main(e+"");

					}


				}
			}
			return successful;
		}

		private void runOnUiThread(Runnable runnable) {
			// TODO Auto-generated method stub

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			calculatedProgress = (int)(((double)(values[0]/contentLength))*100);
			progressbar.setProgress(calculatedProgress);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progressbar.setVisibility(View.INVISIBLE);
		}
	}

}


