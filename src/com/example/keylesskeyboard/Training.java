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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;





import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
	private LinkedList<FloatNode>[] x_cord = (LinkedList<FloatNode>[]) new LinkedList[26];
	private LinkedList<FloatNode>[] y_cord = (LinkedList<FloatNode>[]) new LinkedList[26];
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_training);
        
        for (int i = 0; i < 26; i++)
        {
        	x_cord[i] = new LinkedList<FloatNode>();
        	y_cord[i] = new LinkedList<FloatNode>();
        }
        
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
					start_flag = false;
					// Update display text to "***Training Complete***"
					displaylist = new SpannableString("***Training Complete***");
					textView.setText(displaylist);
					list_idx = 0;
					disp_idx = 0;
					
					// Process the Data
				    double x_avg[] = new double[26];
				    double y_avg[] = new double[26];
				    for (int i = 0; i < 26; i++)
				    {
				       x_avg[i] = 0;
				       y_avg[i] = 0;
				       for (int j = 0; j < x_cord[i].size(); j++)
				       {
				          x_avg[i] += x_cord[i].get(j).getItem();
				          y_avg[i] += y_cord[i].get(j).getItem();
				       }				       

				       if (x_cord[i].size() > 0)
				       {
				          x_avg[i] /= x_cord[i].size();
				          y_avg[i] /= y_cord[i].size();
				       }
				    }
				    
				    double distance_std[] = new double[26];
				    for (int i = 0; i < 26; i++)
				    {
				    	for (int j = 0; j < x_cord[i].size(); j++)
				    	{
				    	   distance_std[i] += Math.pow(x_cord[i].get(j).getItem() - x_avg[i], 2);
				    	   distance_std[i] += Math.pow(y_cord[i].get(j).getItem() - y_avg[i], 2);
				    	}
				    	
				    	if (x_cord[i].size() > 1)
				    	{
				    	   distance_std[i] /= x_cord[i].size() - 1;
				    	}
				    	
				    	distance_std[i] = Math.sqrt(distance_std[i]);
				    }
				    
				    for (int i = 0; i < 26; i++)
				    {
        			   output += String.valueOf(x_avg[i]) + " " + String.valueOf(y_avg[i]) + " " + String.valueOf(distance_std[i]) + " ";
				    }
				    output += "\r\n"; 
				    
					// Print out the file and upload the file
		    		try {		    			
		    			// ##### Write a file to the disk #####
		    			/* We have to use the openFileOutput()-method 
		    			 * the ActivityContext provides, to
		    			 * protect your file from others and 
		    			 * This is done for security-reasons. 
		    			 * We chose MODE_WORLD_READABLE, because
		    			 *  we have nothing to hide in our file */    			    
    
		    			FileOutputStream fOut = openFileOutput("train_data.txt", 
		    								Context.MODE_PRIVATE);
		    			OutputStreamWriter osw = new OutputStreamWriter(fOut);

		    			osw.write(output);    	

		    		    /* ensure that everything is 
		    			 * really written out and close */
		    			osw.flush();
		    			osw.close();
		    			
		 				//** Send image and offload image processing task  to server by starting async task ** 
//		  				ServerTask task = new ServerTask();
//		  				task.execute("/data/data/com.example.keylesskeyboard/files/output.txt");
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

        
        Button delete = (Button) findViewById(R.id.button3);
        delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView textView = (TextView) findViewById(R.id.textView1);
				// TODO Auto-generated method stub
				if (disp_idx > 0)
				{
					// delete previous entry
					int last_idx = wordlist.charAt(list_idx) - 'a';
					
					disp_idx--;
        			UnderlineSpan[] toRemoveSpans = displaylist.getSpans(0, displaylist.length(), UnderlineSpan.class);
        			for (int i = 0; i < toRemoveSpans.length; i++)
        			{
        			    displaylist.removeSpan(toRemoveSpans[i]);
        			}

					displaylist.setSpan(new UnderlineSpan(), disp_idx, disp_idx+1, 0);
					textView.setText(displaylist);
					list_idx--;
					
					if (x_cord[last_idx].size() != 0)
					{
						x_cord[last_idx].remove(x_cord[last_idx].size()-1);
						y_cord[last_idx].remove(y_cord[last_idx].size()-1);                    
					}
				}			   
			}
		});
		
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
            			int index = (int) (wordlist.charAt(list_idx) - 'a');
                        x_cord[index].add(new FloatNode(event.getX()));
                        y_cord[index].add(new FloatNode(event.getY()));
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
    
    
    public class FloatNode
    {
      private float item;
      private FloatNode next;
     
      public FloatNode()
      {
        item = 0;
        next = null;
      }
      public FloatNode(float newItem)
      {
        item = newItem;
        next = null;
      }
      public FloatNode(float newItem, FloatNode nextNode)
      {
        item = newItem;
        next = null;
      }
      public void setItem(float newItem)
      {
        item = newItem;
      }
      public float getItem()
      {
        return item;
      }
      public void setNext(FloatNode nextNode)
      {
        next = nextNode;
      }
      public FloatNode getNext()
      {
        return next;
      }
    }
    
	public void onResume() {
		super.onResume();		
		list_idx = 0;
		disp_idx = 0;
		output = "";
		motion_flag = false;
		start_flag = false;
		download = "";
	}
    

}

