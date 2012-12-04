package com.example.keylesskeyboard;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Training extends Activity {
	private static final String TAG = "Training";
	private Context mContext = this;

	private static final String wordlist = "question quick equate square " + //"abcdef ghijklm nopqrs tuvwxyz abcdef ghijklm nopqrs tuvwxyz ";
			"quite equal quart require liquid example exercise excite " +
			"experiment exact except expect oxygen suffix experience size " +
			"cozy lazy quiz amaze blaze glaze jazzy freeze prize just " +
			"object subject jump join major joke jerk java japan think " +
			"check speak broke stick market chick white begin often always " +
			"river carry friend began watch young ready above family " +
			"leave measure black happen order whole better during hundred " +
			"remember early ground table travel morning several vowel " +
			"toward money serve govern voice power figure field beauty " +
			"front final green develop minute behind wheel force common " +
			"wonder shape brought bring perhaps weight "; 
	
	private SpannableString displaylist = new SpannableString("Press 'Start' to begin training");
	private int list_idx = 0;
	private int disp_idx = 0;
	private String output = "";
	private boolean motion_flag = false;
	private boolean start_flag = false;
	private String download = "";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_training);
        
        Button next = (Button) findViewById(R.id.button1);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {            	
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        
        Button space = (Button) findViewById(R.id.button2);
        space.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				TextView textView = (TextView) findViewById(R.id.textView1);

				if (list_idx == (wordlist.length()-1))
				{
					// Update display text to "***Training Complete***"
					displaylist = new SpannableString("***Training Complete***");
					textView.setText(displaylist);

					// Print out the file and upload the file
		    		try {		    			
		    			// ##### Write a file to the disk #####
		    			/* We have to use the openFileOutput()-method 
		    			 * the ActivityContext provides, to
		    			 * protect your file from others and 
		    			 * This is done for security-reasons. 
		    			 * We chose MODE_WORLD_READABLE, because
		    			 *  we have nothing to hide in our file */    			    
		    			    
		    			FileOutputStream fOut = openFileOutput("output.txt", 
		    								MODE_WORLD_READABLE);
		    			OutputStreamWriter osw = new OutputStreamWriter(fOut);

		    			osw.write(output);    	

		    		    /* ensure that everything is 
		    			 * really written out and close */
		    			osw.flush();
		    			osw.close();
		    			
		 				//** Send image and offload image processing task  to server by starting async task ** 
		  				ServerTask task = new ServerTask();
		  				task.execute("/data/data/com.example.keylesskeyboard/files/output.txt");
		    		}
		    	    catch (IOException ex) {
		    	        Log.e(TAG, ex.toString());
		    	    }
				}
				else
				{
					if (start_flag == false)
					{
						disp_idx = 0;
						
						int tmp_idx = list_idx;
						for (int idx = 0; idx < 4; idx++)
						{
							tmp_idx = wordlist.indexOf(' ', tmp_idx);
							tmp_idx++;
						}
						displaylist = new SpannableString(wordlist.substring(list_idx, tmp_idx));
						displaylist.setSpan(new UnderlineSpan(), disp_idx, disp_idx+1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
						textView.setText(displaylist);						
						
						start_flag = true;
					}
					else if (disp_idx == (displaylist.length()-1))
					{ // get the next 4 words
						disp_idx = 0;
						list_idx++;
						int tmp_idx = list_idx;
						for (int idx = 0; idx < 4; idx++)
						{
							tmp_idx = wordlist.indexOf(' ', tmp_idx);
							tmp_idx++;
						}
            			UnderlineSpan[] toRemoveSpans = displaylist.getSpans(0, displaylist.length(), UnderlineSpan.class);
            			for (int i = 0; i < toRemoveSpans.length; i++)
            			{
            			    displaylist.removeSpan(toRemoveSpans[i]);
            			}

						displaylist = new SpannableString(wordlist.substring(list_idx, tmp_idx));
						displaylist.setSpan(new UnderlineSpan(), disp_idx, disp_idx+1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
						textView.setText(displaylist);
					}
					else
					{ // move the cursor
						if (displaylist.charAt(disp_idx) == ' ')
						{
							disp_idx++;
	            			UnderlineSpan[] toRemoveSpans = displaylist.getSpans(0, displaylist.length(), UnderlineSpan.class);
	            			for (int i = 0; i < toRemoveSpans.length; i++)
	            			{
	            			    displaylist.removeSpan(toRemoveSpans[i]);
	            			}

							displaylist.setSpan(new UnderlineSpan(), disp_idx, disp_idx+1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
							textView.setText(displaylist);
							list_idx++;
						}
					}
					
				}				
			}
		});

        /*
        Button delete = (Button) findViewById(R.id.button3);
        delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView textView = (TextView) findViewById(R.id.textView1);
				// TODO Auto-generated method stub
				if (disp_idx > 0)
				{
					// delete previous entry
					int last_idx = output.lastIndexOf(wordlist.charAt(list_idx), wordlist.length()-1);
					output = output.substring(0, last_idx-1);
					
					disp_idx--;
        			UnderlineSpan[] toRemoveSpans = displaylist.getSpans(0, displaylist.length(), UnderlineSpan.class);
        			for (int i = 0; i < toRemoveSpans.length; i++)
        			{
        			    displaylist.removeSpan(toRemoveSpans[i]);
        			}

					displaylist.setSpan(new UnderlineSpan(), disp_idx, disp_idx+1, 0);
					textView.setText(displaylist);
					list_idx--;
				}			   
			}
		});
		*/
    }
    
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();

        switch (eventaction) {
            case MotionEvent.ACTION_DOWN: 
            	if ((start_flag == true) && (displaylist.charAt(disp_idx) != ' '))
            	{
            		// finger touches the screen
            		// record the xy location
            		if (motion_flag == false)
            		{            		
            			output += String.valueOf(wordlist.charAt(list_idx)) + " " + String.valueOf(event.getX()) + " " + String.valueOf(event.getY()) + " \r\n";
            			motion_flag = true;
            	
            			// move the cursor
            			disp_idx++;
            			
            			UnderlineSpan[] toRemoveSpans = displaylist.getSpans(0, displaylist.length(), UnderlineSpan.class);
            			for (int i = 0; i < toRemoveSpans.length; i++)
            			{
            			    displaylist.removeSpan(toRemoveSpans[i]);
            			}
            			
            			displaylist.setSpan(new UnderlineSpan(), disp_idx, disp_idx+1, 0);
            			TextView textView = (TextView) findViewById(R.id.textView1);
            			textView.setText(displaylist);
            			list_idx++;
            		}
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
        return true; 
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_training, menu);
        return true;
    }
    */
    
	//*******************************************************************************
	//Push processing task to server
	//*******************************************************************************
	
	public class ServerTask  extends AsyncTask<String, Integer , Void>
	{
		public byte[] dataToServer;
				
		//Task state
//		private final String SERVERURL = "http://10.0.2.2/training.php";
//		private final String SERVERURL = "http://192.168.0.6/training.php";
		private final String SERVERURL = "http://198.213.239.178/training.php";
		private final int UPLOADING_STATE  = 0;
		private final int SERVER_PROC_STATE  = 1;
		
		private ProgressDialog dialog;
		
		//upload photo to server
		HttpURLConnection uploadPhoto(FileInputStream fileInputStream)
		{
			
			final String serverFileName = "temp.txt";		
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
			
			download = new String(s);

			try {
				FileOutputStream fOut = openFileOutput ("train_data.txt", MODE_WORLD_READABLE);
				OutputStreamWriter osw = new OutputStreamWriter(fOut);
				osw.write(download);
			
				/* ensure that everything is 
				 * really written out and close */
				osw.flush();
				osw.close();
			}
    	    catch (IOException ex) {
    	        Log.e(TAG, ex.toString());
    	    }
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

