package com.example.keylesskeyboard;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.Math;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class KeylessKeyboard extends Activity {
	private static final String TAG = "KeylessKeyboard";
	private Intent intent;
	private String result = "";
//	ResultView mResultView;
	private Context mContext = this;
	private boolean motion_flag = false;
	private boolean train_flag = false;
	private boolean first_letter = true;
	public  boolean done = false;
	private static String displaylist = "";
	private double[] x_mean = new double[26];
	private double[] y_mean = new double[26];
	private static String output = "";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.main);
        
        intent = new Intent(this, BroadcastService.class);
        
        Button next = (Button) findViewById(R.id.button1);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Training.class);
                startActivityForResult(myIntent, 0);
            }

        });
        
        Button start = (Button) findViewById(R.id.button2);
        start.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					// TODO Auto-generated method stub
					// Upload data to server
					FileOutputStream fOut = openFileOutput("data.txt", MODE_WORLD_READABLE);
					OutputStreamWriter osw = new OutputStreamWriter(fOut);

					osw.write(output);    	

					/* ensure that everything is 
					 * really written out and close */
					osw.flush();
					osw.close();
	
					//** Send image and offload image processing task  to server by starting async task ** 
					ServerTask task = new ServerTask();
					task.execute( "/data/data/com.example.keylesskeyboard/files/data.txt");
					first_letter = true;
				
				}    		
				catch (IOException ex) {
						Log.e(TAG, ex.toString());
				}
			}
		});
        
        try 
        {
            File file = new File("train_data.txt");
            Scanner scanner = new Scanner(file);
            int i = 0;
            while (scanner.hasNextDouble()) 
            {
              x_mean[i] = scanner.nextDouble();
              y_mean[i] = scanner.nextDouble();
              i++;
            }
            train_flag = true;
            scanner.close();
        } 
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
			train_flag = false;
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();

        switch (eventaction) {
            case MotionEvent.ACTION_DOWN: 
            	if (train_flag == true) 
            	{
            		// finger touches the screen
            		// record the xy location
            		if (motion_flag == false)
            		{
            			if (first_letter == true)
            			{
            				displaylist = "";
            				first_letter = false;
            			}
            			
            			double distance = java.lang.Math.pow(2,24);
            			double temp_distance;
            			char letter = 'a';
            			for (int i = 0; i < 26; i++)
            			{
            				temp_distance = java.lang.Math.sqrt(java.lang.Math.pow((x_mean[i] - event.getX()), 2) + java.lang.Math.pow((y_mean[i] - event.getY()), 2));
            				if (temp_distance < distance)
            				{
            					distance = temp_distance;
            					letter = (char) (i + 97);
            				}
            			}
            			
            			displaylist = displaylist + String.valueOf(letter);
            			output += String.valueOf(letter) + " " + String.valueOf(event.getX()) + " " + String.valueOf(event.getY()) + " \r\n";
            			            	            			
            			TextView textView = (TextView) findViewById(R.id.textView1);
            			textView.setText(displaylist);
            			
            			motion_flag = true;
            		}
            	}
            	else
            	{
            		TextView textView = (TextView) findViewById(R.id.textView1);
            		textView.setText("Please do the training first");
            	}
            	break;

            case MotionEvent.ACTION_MOVE:
                // finger moves on the screen            	
                break;

            case MotionEvent.ACTION_UP:   
                // finger leaves the screen
            	motion_flag = false;
                break;
        }

        // tell the system that we handled the event and no further processing is required
        return super.onTouchEvent(event);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	updateUI(intent);       
        }
    };    
    
	@Override
	public void onResume() {
		super.onResume();		
		startService(intent);
		registerReceiver(broadcastReceiver, new IntentFilter(BroadcastService.BROADCAST_ACTION));
	}
	
	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(broadcastReceiver);
		stopService(intent); 		
	}	
	    
    private void updateUI(Intent intent) {
    	String counter = intent.getStringExtra("counter"); 
    	String time = intent.getStringExtra("time");
    	Log.d(TAG, counter);
    	Log.d(TAG, time);
    	Log.d(TAG, result);
        if (train_flag == false)
    	{
        	try 
        	{
        		File path = getFilesDir();
        		File file = new File(path.toString() + "/train_data.txt");
        		Scanner scanner = new Scanner(file);
        		int i = 0;
        		while (scanner.hasNextDouble()) 
        		{
        			x_mean[i] = scanner.nextDouble();
        			y_mean[i] = scanner.nextDouble();
        			i++;
        		}
        		train_flag = true;
        		scanner.close();
        	} 
        	catch (FileNotFoundException e)
        	{
        		e.printStackTrace();
        		train_flag = false;
        	}
    	}
        
    	if (done)
    	{
    		TextView textView = (TextView) findViewById(R.id.textView1);
    		result = "***" + result + "***";
    		textView.setText(result);
    		done = false;
    	}
    }
    
	//*******************************************************************************
	//Push processing task to server
	//*******************************************************************************
	
	public class ServerTask  extends AsyncTask<String, Integer , Void>
	{
		public byte[] dataToServer;
				
		//Task state
//		private final String SERVERURL = "http://10.0.2.2/viterbi.php";
//		private final String SERVERURL = "http://192.168.0.6/viterbi.php";
		private final String SERVERURL = "http://198.213.239.178/viterbi.php";

		private final int UPLOADING_STATE  = 0;
		private final int SERVER_PROC_STATE  = 1;
		
		private ProgressDialog dialog;
		
		//upload photo to server
		HttpURLConnection uploadPhoto(FileInputStream fileInputStream)
		{
			
			final String serverFileName = "viterbi.txt";		
			final String lineEnd = "\r\n";
			final String twoHyphens = "--";
			final String boundary = "*****";
			
			try
			{
				URL url = new URL(SERVERURL);
				// Open a HTTP connection to the URL				
				final HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				// Allow Inputs
				conn.setDoInput(true);				
				// Allow Outputs
				conn.setDoOutput(true);				
				// Don't use a cached copy.
				conn.setUseCaches(false);
				
				// Use a post method.
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
				
				DataOutputStream dos = new DataOutputStream( conn.getOutputStream() );
				
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + serverFileName +"\"" + lineEnd);
				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				int bytesAvailable = fileInputStream.available();
				int maxBufferSize = 1024;
				int bufferSize = Math.min(bytesAvailable, maxBufferSize);
				byte[] buffer = new byte[bufferSize];
				
				// read file and write it into form...
				int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				
				while (bytesRead > 0)
				{
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}
				
				// send multipart form data after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
				publishProgress(SERVER_PROC_STATE);
				// close streams
				fileInputStream.close();
				dos.flush();
				
				return conn;
			}
			catch (MalformedURLException ex){
				Log.e(TAG, "error: " + ex.getMessage(), ex);
				return null;
			}
			catch (IOException ioe){
				Log.e(TAG, "error: " + ioe.getMessage(), ioe);
				return null;
			}
		}
		
	    private void readStream(InputStream inputStream) {
			// TODO Auto-generated method stub
			
		}

		//get image result from server and display it in result view
		void getResult(HttpURLConnection conn){
			// retrieve the response from server
			InputStream is;
			StringBuilder s = new StringBuilder();
			try {
				is = conn.getInputStream();
				
				char[] buf = new char[2048];
				Reader r = new InputStreamReader(is, "UTF-8");
				while (true) {
				   int n = r.read(buf);
				   if (n < 0)
				      break;
				   s.append(buf, 0, n);
				}
			} catch (IOException e) {
				Log.e(TAG,e.toString());
				e.printStackTrace();
			}
			
			result = new String(s);
			done = true;
		}
		
		//Main code for processing image algorithm on the server
		
		void processImage(String inputImageFilePath){			
			publishProgress(UPLOADING_STATE);
			File inputFile = new File(inputImageFilePath);
			try {
				
				//create file stream for captured image file
				FileInputStream fileInputStream  = new FileInputStream(inputFile);
		    	
				//upload photo
		    	final HttpURLConnection  conn = uploadPhoto(fileInputStream);
		    	
		    	//get processed photo from server
		    	if (conn != null){
		    		getResult(conn);
		    	}
				fileInputStream.close();
			}
	        catch (FileNotFoundException ex){
	        	Log.e(TAG, ex.toString());
	        }
	        catch (IOException ex){
	        	Log.e(TAG, ex.toString());
	        }
		}
		
	    public ServerTask() {
	        dialog = new ProgressDialog(mContext);
	    }		
		
	    protected void onPreExecute() {
	        this.dialog.setMessage("Photo captured");
	        this.dialog.show();
	    }
		@Override
		protected Void doInBackground(String... params) {			//background operation 
			String uploadFilePath = params[0];
			processImage(uploadFilePath);
			return null;
		}		
		//progress update, display dialogs
		@Override
	     protected void onProgressUpdate(Integer... progress) {
	    	 if(progress[0] == UPLOADING_STATE){
	    		 dialog.setMessage("Uploading");
	    		 dialog.show();
	    	 }
	    	 else if (progress[0] == SERVER_PROC_STATE){
		           if (dialog.isShowing()) {
		               dialog.dismiss();
		           }	    	 
	    		 dialog.setMessage("Processing");
	    		 dialog.show();
	    	 }	         
	     }		
	       @Override
	       protected void onPostExecute(Void param) {
	           if (dialog.isShowing()) {
	               dialog.dismiss();
	           }
	       }
	}
}