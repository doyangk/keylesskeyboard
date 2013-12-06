package com.example.keylesskeyboard;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.Math;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Node;

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
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.view.GestureDetectorCompat;


public class KeylessKeyboard extends Activity 
{
	private static final String TAG = "KeylessKeyboard";
	private Intent intent;
	private String result = "";
//	ResultView mResultView;
	private Context mContext = this;
	private boolean motion_flag = false;
	private boolean train_flag = false;
	private boolean dict_flag = false;
	private boolean first_letter = true;
	public  boolean done = false;
	private boolean punctuation = false;
	private static String displaylist = "";
	private static String actualText = "";
	private double[] x_mean = new double[26];
	private double[] y_mean = new double[26];
	private double[] distance_std = new double[26];
	private static String[] output = null;
	private LinkedList<FloatNode> x_cord;
	private LinkedList<FloatNode> y_cord;
	private LinkedList<Double[][]> emission_table;
	private String [] candidates = new String[100];
	private GestureDetectorCompat mDetector; 
	private int selection = 0;
	private static String[] correction = null;
	
	private enum Editdone {
		NONE, DELETION, TRANSPOSITION, INSERTION, SUBSTITUTION
	}

//    private static double[][] transition_probability = new double[729][27];
    /*
    private String[] states = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
                               "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
                               "y", "z", " "};

    private String[] observations = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
                                     "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
                                     "y", "z", " "};
    */

/*    
    private double[] forward_start_probability =  
    {  // aa through z_
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
       -1.6099391458723804, // _a 
       -2.0044944723954528, // _b 
       -1.9809797959279611, // _c 
       -2.1795466660813196, // _d 
       -2.278606827845907, // _e 
       -2.0452412592095186, // _f 
       -2.4306821100938354, // _g 
       -1.9293778352254642, // _h 
       -1.838945563420635, // _i 
       -2.9306475777611105, // _j 
       -2.960098451208195, // _k 
       -2.282992001573488, // _l 
       -2.065981352408, // _m 
       -2.331267760890007, // _n 
       -1.816530232870902, // _o 
       -2.072604883590289, // _p 
       -3.3722093328504497, // _q 
       -2.2444066865380794, // _r 
       -1.7909132339494194, // _s 
       -1.4628227663489048, // _t 
       -2.6004664454412234, // _u 
       -2.837105116153399, // _v 
       -1.8847184940407191, // _w 
       -4.837419708330794, // _x 
       -2.7345586501264028, // _y 
       -4.301520453057945, // _z 
       -100 // __
   };

   private double[] backward_start_probability = 
   {  // aa through z_
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-2.210618314324357,
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-3.5690588294201806,
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-2.935590373840594,
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-1.6481328722499384,
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-1.3630535094921743,	
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-2.0357078646634124,
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-2.1731457269912093,
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-2.234259639927097,
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-2.8190569567511568,
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-4.49157736825045,  	
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-2.699139898487367,
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-2.1605008937596035,
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-2.4357445109556854,
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-1.7128965826377596,
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-2.0397106991195946,  	
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-2.864157957864949,
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-4.8129459389475935,
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-1.8925581236671307,
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-1.561274390705496,
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-1.6923539801174945,  	
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-3.0304536807714073,
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-4.072042745983579,
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-2.7042048830600267,
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-3.5197196477362676,
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-1.89434117646968,
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-4.165723788861437,
      -100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,
    };
*/   
    private double [][] p_table = 
    	{{0.5,   0.504, 0.508, 0.512, 0.516, 0.5199,0.5239,0.5279,0.5319,0.5359},
    	 {0.5398,0.5438,0.5478,0.5517,0.5557,0.5596,0.5636,0.5675,0.5714,0.5753},
    	 {0.5793,0.5832,0.5871,0.591, 0.5948,0.5987,0.6026,0.6064,0.6103,0.6141},
    	 {0.6179,0.6217,0.6255,0.6293,0.6331,0.6368,0.6406,0.6443,0.648, 0.6517},    
    	 {0.6554,0.6591,0.6628,0.6664,0.67,  0.6736,0.6772,0.6808,0.6844,0.6879},
    	 {0.6915,0.695, 0.6985,0.7019,0.7054,0.7088,0.7123,0.7157,0.719, 0.7224},
    	 {0.7257,0.7291,0.7324,0.7357,0.7389,0.7422,0.7454,0.7486,0.7517,0.7549},
    	 {0.758, 0.7611,0.7642,0.7673,0.7704,0.7734,0.7764,0.7794,0.7823,0.7852},
    	 {0.7881,0.791, 0.7939,0.7967,0.7995,0.8023,0.8051,0.8078,0.8106,0.8133},
    	 {0.8159,0.8186,0.8212,0.8238,0.8264,0.8289,0.8315,0.834, 0.8365,0.8389},
    	 {0.8413,0.8438,0.8461,0.8485,0.8508,0.8531,0.8554,0.8577,0.8599,0.8621},
    	 {0.8643,0.8665,0.8686,0.8708,0.8729,0.8749,0.877, 0.879, 0.881, 0.8830},
    	 {0.8849,0.8869,0.8888,0.8907,0.8925,0.8944,0.8962,0.898, 0.8997,0.9015},
    	 {0.9032,0.9049,0.9066,0.9082,0.9099,0.9115,0.9131,0.9147,0.9162,0.9177},
    	 {0.9192,0.9207,0.9222,0.9236,0.9251,0.9265,0.9279,0.9292,0.9306,0.9319},
    	 {0.9332,0.9345,0.9357,0.937, 0.9382,0.9394,0.9406,0.9418,0.9429,0.9441},
    	 {0.9452,0.9463,0.9474,0.9484,0.9495,0.9505,0.9515,0.9525,0.9535,0.9545},
    	 {0.9554,0.9564,0.9573,0.9582,0.9591,0.9599,0.9608,0.9616,0.9625,0.9633},
    	 {0.9641,0.9649,0.9656,0.9664,0.9671,0.9678,0.9686,0.9693,0.9699,0.9706},
    	 {0.9713,0.9719,0.9726,0.9732,0.9738,0.9744,0.975, 0.9756,0.9761,0.9767},    	   										
    	 {0.9772,0.9778,0.9783,0.9788,0.9793,0.9798,0.9803,0.9808,0.9812,0.9817},
    	 {0.9821,0.9826,0.983, 0.9834,0.9838,0.9842,0.9846,0.985, 0.9854,0.9857},
    	 {0.9861,0.9864,0.9868,0.9871,0.9875,0.9878,0.9881,0.9884,0.9887,0.989 },
    	 {0.9893,0.9896,0.9898,0.9901,0.9904,0.9906,0.9909,0.9911,0.9913,0.9916},
    	 {0.9918,0.992, 0.9922,0.9925,0.9927,0.9929,0.9931,0.9932,0.9934,0.9936},
    	 {0.9938,0.994, 0.9941,0.9943,0.9945,0.9946,0.9948,0.9949,0.9951,0.9952},
    	 {0.9953,0.9955,0.9956,0.9957,0.9959,0.996, 0.9961,0.9962,0.9963,0.9964},
    	 {0.9965,0.9966,0.9967,0.9968,0.9969,0.997, 0.9971,0.9972,0.9973,0.9974},
    	 {0.9974,0.9975,0.9976,0.9977,0.9977,0.9978,0.9979,0.9979,0.998, 0.9981},
    	 {0.9981,0.9982,0.9982,0.9983,0.9984,0.9984,0.9985,0.9985,0.9986,0.9986},
    	 {0.9987,0.9987,0.9987,0.9988,0.9988,0.9989,0.9989,0.9989,0.999, 0.999 },
    	 {0.999, 0.9991,0.9991,0.9991,0.9992,0.9992,0.9992,0.9992,0.9993,0.9993},
    	 {0.9993,0.9993,0.9994,0.9994,0.9994,0.9994,0.9994,0.9995,0.9995,0.9995},
    	 {0.9995,0.9995,0.9995,0.9996,0.9996,0.9996,0.9996,0.9996,0.9996,0.9997},
    	 {0.9997,0.9997,0.9997,0.9997,0.9997,0.9997,0.9997,0.9997,0.9997,0.9998}   	 
    	};

    private spellchecker dictionary;
    
    private static final String DEBUG_TAG = "Gestures";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.main);
        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
        
        // Set the gesture detector as the double tap
        // listener.

        
        intent = new Intent(this, BroadcastService.class);
        
        Button next = (Button) findViewById(R.id.button1);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Training.class);
                startActivityForResult(myIntent, 0);
            }
        });
        
        Button del = (Button) findViewById(R.id.button3);
        del.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
			    TextView textView = (TextView) findViewById(R.id.textView1);
			    TextView textView2 = (TextView) findViewById(R.id.textView2);
			    if (displaylist == "" || displaylist.length() == 1)
			    {
			    	displaylist = "";
			    	if (x_cord != null)
			    		x_cord.removeAll(x_cord);
			    	if (y_cord != null)
			    		y_cord.removeAll(y_cord);
			    	first_letter = true;
			    	actualText = actualText.substring(0,actualText.length()-1);
			    }
			    else
			    {
			    	displaylist = displaylist.substring(0, displaylist.length()-1);
			    	x_cord.remove(x_cord.size() - 1);
			    	y_cord.remove(y_cord.size() - 1);
			    	actualText = actualText.substring(0,actualText.length()-1);
			    }
    			textView.setText(displaylist);
    			textView2.setText(actualText);
            }
        });
        
        Button email = (Button) findViewById(R.id.button4);
        email.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent email = new Intent(Intent.ACTION_SEND);
            	email.putExtra(Intent.EXTRA_EMAIL, new String[]{"playdo@gmail.com"});		  
            	email.putExtra(Intent.EXTRA_SUBJECT, "subject");
            	email.putExtra(Intent.EXTRA_TEXT, actualText);
            	email.setType("message/rfc822");
            	startActivity(Intent.createChooser(email, "Choose an Email client :"));      	
            }
        });
        
        Button start = (Button) findViewById(R.id.button2);
        start.setOnClickListener(new View.OnClickListener() {
        	// start button			
			@Override
			public void onClick(View v) {
				if (train_flag && !first_letter)
				{
					double distance;
					double z_score;
					double two_tailed_p;
					
				    String word = new String(displaylist);	    
				    
				    // calculate emission probability
				    double emission_prob[][] = new double[word.length()][26];
				    for (int i = 0; i < word.length(); i++)
				    {
				    	/*
				       int index = 0;
				       if ((displaylist.charAt(i) >= 'a') && (displaylist.charAt(i) <= 'z'))
			    	   {
			    	      index = (int) (displaylist.charAt(i) - 'a');
			    	   }
			    	   */
				    	
				       for (int j = 0; j < 26; j++)
				       {				   
					      distance = Math.sqrt(Math.pow((double) (x_cord.get(i).getItem() - x_mean[j]), 2) +
					    				       Math.pow((double) (y_cord.get(i).getItem() - y_mean[j]), 2));
					      z_score = distance / distance_std[j];
					      if (z_score <= 3.49)
					      {
					    	 two_tailed_p = 1 - 2*(p_table[(int) Math.floor(z_score*10)][(int) (Math.round(z_score*100) % 10)]);
					    	 emission_prob[i][j] = Math.log10(Math.abs(two_tailed_p));
					      }
					      else
					      {
					    	 emission_prob[i][j] = -100;	
					      }
				       }
				    }

//				    String[] output = dictionary.correct(word, emission_prob);
				    String[] correction = dictionary.correct(word, emission_prob);
				    int count = 0;
				    int i = 0;
				    output = new String[5];
				    while (count < 5 && i < 25)
				    {
				    	if (correction[i] != "null")
				    	{
				    		output[count] = new String(correction[i]);
				    		count++;
				    	}
				    	i++;
				    }
				    displaylist = output[0]+" "+output[1]+" "+output[2]+" "+output[3]+" "+output[4];
				    TextView textView = (TextView) findViewById(R.id.textView1);
        			textView.setText(displaylist);
        			int index = actualText.lastIndexOf(' ');
        			actualText = actualText.substring(0,index);
        			actualText = actualText + " " + output[0];
        			TextView textView2 = (TextView) findViewById(R.id.textView2);
        			textView2.setText(actualText);
        			first_letter = true;
        			selection = 0;

			      
			   }
			}
		});
        

    	if (train_flag == false)
    	{
           try 
           {
              File file = new File("train_data.txt");
              Scanner scanner = new Scanner(file);
              int i = 0;
              while (scanner.hasNextDouble()) 
              {
                 x_mean[i] = scanner.nextDouble();
                 y_mean[i] = scanner.nextDouble();
                 distance_std[i] = scanner.nextDouble();
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

    	if (dict_flag == false)
    	{
            try 
            {
    			InputStream instr = getResources().openRawResource(R.raw.brown);
    			dictionary = new spellchecker(instr);
    			dict_flag = true;
    		} 
            catch (IOException e) 
            {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			dict_flag = false;
    		}
    	}
        
    	/*
        long count = 0;
        long found = 0;
        long garbage;
                
        InputStream DictionaryInputStream = getResources().openRawResource(R.raw.count_1w);
		Scanner scanner = new Scanner(DictionaryInputStream);
		//File file = new File("count_1w.txt");
		//Scanner scanner = new Scanner(file);
		String word;
		while (scanner.hasNext() && (count < 101)) 
		{
		  count++;
		  word = scanner.next();
		  garbage = scanner.nextLong();
		  double [][][] e = null;
		  String[] result = dictionary.correct(word, e);
		  System.out.println(result);
		  System.out.println(word);

		}
		scanner.close();
		count--;
		System.out.println("Found: " + found + ", Count: " + count);
		*/
    }
    
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);

        // tell the system that we handled the event and no further processing is required
        return super.onTouchEvent(event);
    }
    
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures"; 
        
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            result = onSwipeRight();
                        } else {
                            result = onSwipeLeft();
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            result = onSwipeBottom();
                        } else {
                            result = onSwipeTop();
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
        
//        public void onLongPress(MotionEvent event) {
//        	longPress();
//        }
        
        public boolean onSingleTapUp (MotionEvent event){
        	if (train_flag == true) 
        	{
        		// finger touches the screen
        		// record the xy location

        		if (first_letter == true)
        		{
        			x_cord = new LinkedList<FloatNode>();
        			y_cord = new LinkedList<FloatNode>();
        			displaylist = "";
        			actualText += " ";
        			output = null;
        			first_letter = false;
        			punctuation = false;
        		}
        			
        		double distance = java.lang.Math.pow(2,24);
        		double temp_distance;
        		char letter = 'a';
        		for (int i = 0; i < 26; i++)
        		{
        			temp_distance = Math.sqrt(java.lang.Math.pow((x_mean[i] - event.getX()), 2) + Math.pow((y_mean[i] - event.getY()), 2));
        			if (temp_distance < distance)
        			{
        				distance = temp_distance;
        				letter = (char) (i + 97);
        			}
        		}
        			
        		displaylist += String.valueOf(letter);
        		actualText += String.valueOf(letter);
                x_cord.add(new FloatNode(event.getX()));
                y_cord.add(new FloatNode(event.getY()));
        			            	            			
        		TextView textView = (TextView) findViewById(R.id.textView1);
        		textView.setText(displaylist);
        		
        		TextView textView2 = (TextView) findViewById(R.id.textView2);
        		textView2.setText(actualText);	
        	}
        	else
        	{
        		TextView textView = (TextView) findViewById(R.id.textView1);
        		textView.setText("Please do the training first");
        	}
        	return false;
        }
    }
    
//    public void longPress() {
//        Toast.makeText(this, "long press", Toast.LENGTH_SHORT).show();
//    }
    
    public boolean onSwipeRight() {
    	//Toast.makeText(this, "right", Toast.LENGTH_SHORT).show();
    	output = new String[5];
    	output[0] = ".";
    	output[1] = ",";
    	output[2] = "?";
    	output[3] = "!";
    	output[4] = "'";
    	first_letter = true;
    	punctuation = true;
    	displaylist = "   " + output[0] + "   " + output[1] + "   " + output[2] + "   " + output[3] + "   " + output[4];
    	TextView textView = (TextView) findViewById(R.id.textView1);
    	textView.setText(displaylist);
    	
		actualText = actualText + output[0];
		selection = 0;
		TextView textView2 = (TextView) findViewById(R.id.textView2);
		textView2.setText(actualText);
        return true;
    }

    public boolean onSwipeLeft() {
        //Toast.makeText(this, "left", Toast.LENGTH_SHORT).show();
        return true;
    }

    public boolean onSwipeTop() {
    	if (output != null)
    	{
    		if (punctuation)
    		{
 		       if (selection != 0)
 			      selection--;
		       actualText = actualText.substring(0,actualText.length()-1);
		       actualText += output[selection];
    		}
    		else
    		{
		       int index = actualText.lastIndexOf(' ');
		       actualText = actualText.substring(0,index);
		       if (selection != 0)
			      selection--;
		       actualText = actualText + " " + output[selection];
    		}
		    TextView textView2 = (TextView) findViewById(R.id.textView2);
		    textView2.setText(actualText);
    	}
    	//Toast.makeText(this, "top", Toast.LENGTH_SHORT).show();
        return true;
    }

    public boolean onSwipeBottom() {
    	if (output != null)
    	{
    		if (punctuation)
    		{
 		       if (selection != 4)
 			      selection++;
		       actualText = actualText.substring(0,actualText.length()-1);
		       actualText += output[selection];
    		}
    		else
    		{
		       int index = actualText.lastIndexOf(' ');
		       actualText = actualText.substring(0,index);
		       if (selection != 4)
			      selection++;
		       actualText = actualText + " " + output[selection];
    		}
		   TextView textView2 = (TextView) findViewById(R.id.textView2);
		   textView2.setText(actualText);
    	}
        //Toast.makeText(this, "bottom", Toast.LENGTH_SHORT).show();
        return true;
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
    	if (train_flag == false)
    	{
           try 
           {
        	  File dir = getFilesDir();
              File file = new File("/data/data/com.example.keylesskeyboard/files/train_data.txt");
              Scanner scanner = new Scanner(file);
              int i = 0;
              while (scanner.hasNextDouble() && i < 26) 
              {
                 x_mean[i] = scanner.nextDouble();
                 y_mean[i] = scanner.nextDouble();
                 distance_std[i] = scanner.nextDouble();
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
    	/*
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
    	*/
        
    	if (done)
    	{
    		TextView textView = (TextView) findViewById(R.id.textView1);
    		result = "***" + result + "***";
    		textView.setText(result);
    		done = false;
    		first_letter = true;
    	}
    }
    
    // FloatNode for Linklist
    private class FloatNode
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
    
    // Function for viterbi
    private int[] copyIntArray(int[] ia) 
    {
       int[] newIa = new int[ia.length];
 	  for(int i=0; i<ia.length; i++)
 	  {
 	     newIa[i] = ia[i];
 	  }
 	  return newIa;
    }
 	  
    private int[] copyIntArray(int[] ia, int newInt)
    {
       int[] newIa = new int[ia.length + 1];
 	  for(int i=0; i<ia.length; i++)
 	  {
 	     newIa[i] = ia[i];
 	  }
 	  newIa[ia.length] = newInt;
 	  return newIa;
    }

    /*
	private class TNode 
    {
 	    public double prob;
 	    public int[] v_path;
 	    public double  v_prob;
 	    public TNode(double prob, int[] v_path, double v_prob)
 	    {
 	      this.prob = prob;
 	      this.v_path = copyIntArray(v_path);
 	      this.v_prob = v_prob;
 	    }
 	    
    }
    */
	
	private class TNode
	{
	   private String word;
	   private TNode next;
	   private Double prob;
	   private Editdone prev_edit_type;
	   private int prev_edit_index;
	   private Editdone prev_prev_edit_type;
	   private int prev_prev_edit_index;

	   
	   public TNode (String input, Double p)
	   {
	      word = input;
	      prob = p;
	      next = null;
	      prev_edit_type = Editdone.NONE;
	      prev_edit_index = 0;
	      prev_prev_edit_type = Editdone.NONE;
	      prev_prev_edit_index = 0;
	   }
	   
	   public TNode (String input, Double p, Editdone edit_type, int edit_index)
	   {
	      word = input;
	      prob = p;
	      next = null;
	      prev_prev_edit_type = prev_edit_type;
	      prev_prev_edit_index = prev_edit_index; 
	      prev_edit_type = edit_type;
	      prev_edit_index = edit_index;
	   }
		
	   public void setItem(String input)
	   {
		  word = input;
	   }
	   
	   public void setItem(Double p)
	   {
		  prob = p;
	   }
	       
	   public void getItem(String input, Double p)
	   {
	      p = prob;
	      input = word;
	   }
	   
	   public Double getItem()
	   {
		   return this.prob;
	   }
	   
	   public void setNext(TNode nextNode)
	   {
	      next = nextNode;
	   }
	       
	   public TNode getNext()
	   {
	      return next;
	   }
	   
	}
    


	    class ProbComparator implements Comparator<TNode> {

	        public int compare(TNode c1, TNode c2) {

	            return c1.getItem().compareTo(c2.getItem());
	        }
	    }



    
	private final HashMap<String, Integer> nWords = new HashMap<String, Integer>();

	public class spellchecker {
		private final HashMap<String, Integer> nWords = new HashMap<String, Integer>();
//		private static HashMap<String, Integer> sortedWords = new HashMap<String, Integer>();

		public spellchecker(InputStream file) throws IOException {
			BufferedReader in = new BufferedReader(new InputStreamReader(file));
			Pattern p = Pattern.compile("\\w+");
			for(String temp = ""; temp != null; temp = in.readLine())
			{
				Matcher m = p.matcher(temp.toLowerCase());
				while(m.find()) 
					nWords.put((temp = m.group()), nWords.containsKey(temp) ? nWords.get(temp) + 1 : 1);
			}
			in.close();								
		}

		private final LinkedList<TNode> edits1(String word, double [][] ep) {
			LinkedList<TNode> result = new LinkedList<TNode>();
			double prob = 0;
			for (int i = 0; i < word.length() ; ++i)
			{
			   int index = word.charAt(i) - 'a';
               prob += ep[i][index];
 			}
			result.add(new TNode(word, prob, Editdone.NONE, 0));
			
			for (int i=0; i < word.length(); ++i) 
			{ // delete				
			   String temp = word.substring(0, i) + word.substring(i+1);
			   
			   prob = 0;
			   for (int j = 0; j < i; ++j)
			   {
				  int index = word.charAt(j) - 'a';
	              prob += ep[j][index];
	 		   }
			   			
			   // review later
			   
			   for (int j = i; j < word.length()-1; ++j)
			   {
				  int index = word.charAt(j) - 'a';
		          prob += ep[j+1][index];
			   }
			   
			   // penalty for deletion
			   int index1;
			   int index2;
			   if (i == 0)
			   {
				  index1 = temp.charAt(i) - 'a';
			      prob += ep[i+1][index1];   
			   }
			   else if (i == (word.length() - 1))
			   {
				  index1 = temp.charAt(i-1) - 'a';
		          prob += ep[i-1][index1];
			   }
			   else
			   {
				  index1 = temp.charAt(i-1) - 'a';	              
	              index2 = temp.charAt(i) - 'a';
	           
			      if (ep[i-1][index1] < ep[i+1][index2])
			      {
				     prob += ep[i-1][index1];
			      } 
			      else
			      {
				     prob += ep[i+1][index2];
			      } 
			   }
				   				
			   result.add(new TNode(temp, prob, Editdone.DELETION, i));
			   //result.add(word.substring(0, i) + word.substring(i+1));
			}
			
			for(int i=0; i < word.length()-1; ++i)
			{ // switch
			   String temp = word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2);

			   prob = 0;
			   for (int j = 0; j < i; j++)
			   {
				  int index = temp.charAt(j) - 'a';
		          prob += ep[j][index];
		 	   }
				   			   
			   for (int j = i+2; j < temp.length(); ++j)
			   {
				  int index = temp.charAt(j) - 'a';
			      prob += ep[j][index];
			   }
				   
			   // no penalty
			   int index1 = temp.charAt(i) - 'a';
			   int index2 = temp.charAt(i+1) - 'a';
	           prob += ep[i+1][index1];
			   prob += ep[i][index2];			   

			   result.add(new TNode(temp, prob, Editdone.TRANSPOSITION, i));
			   //result.add(word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2));
			}
				
			for(int i=0; i < word.length(); ++i) 
				for(char c='a'; c <= 'z'; ++c)
				{ // insert
				   String temp = word.substring(0, i) + String.valueOf(c) + word.substring(i);
				   prob = 0;
				   for (int j = 0; j < i; j++)
				   {
				      int index = temp.charAt(j) - 'a';
				      prob += ep[j][index];
				   }
						   			   
				   for (int j = i+1; j < temp.length(); ++j)
				   {
				      int index = temp.charAt(j) - 'a';
					  prob += ep[j-1][index];
				   }
						   
				   // penalty for insertion
				   int index1;
				   int index2;
				   if (i == 0)
				   {
					  index1 = temp.charAt(1+1) - 'a';
					  prob += ep[i][index1];   
				   }
				   else if (i == (word.length() - 1))
				   {
					  index1 = temp.charAt(i-1) - 'a';
				      prob += ep[i-1][index1];
				   }
				   else
				   {
					  index1 = temp.charAt(i+1) - 'a';
			          index2 = temp.charAt(i-1) - 'a';
			           
					  if (ep[i][index1] < ep[i-1][index2])
					  {
						 prob += ep[i][index1];
					  } 
					  else
					  {
						 prob += ep[i-1][index2];
					  }
				   }
					
				   result.add(new TNode(temp, prob, Editdone.INSERTION, i));
				   //result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i+1));
				}
			
			for(char c='a'; c <= 'z'; ++c)
			{
				String temp = word + String.valueOf(c);
				prob = 0;
				int index = 0;
				for (int i = 0; i < word.length() ; ++i)
				{
				   index = word.charAt(i) - 'a';
	               prob += ep[i][index];
	 			}
				prob += ep[word.length()-1][index];
				result.add(new TNode(temp, prob, Editdone.INSERTION, word.length()));
			}
			
			for(int i=0; i < word.length(); ++i) 
				for(char c='a'; c <= 'z'; ++c) 
				{ // substitute
				   String temp = word.substring(0, i) + String.valueOf(c) + word.substring(i+1);
					
				   prob = 0;
				   for (int j = 0; j < i; j++)
				   {
					  int index = temp.charAt(j) - 'a';
				      prob += ep[j][index];
				   }
						   			   
				   for (int j = i+1; j < temp.length(); ++j)
				   {
				      int index = temp.charAt(j) - 'a';
					  prob += ep[j][index];
				   }
						   
				   // penalty for substitution
				   int index = c - 'a';
			       prob += ep[i][index];
			              
				   result.add(new TNode(temp, prob, Editdone.SUBSTITUTION, i));
				   //result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));
				}
						
			return result;
		}
		
		/*
		private final LinkedList<TNode> edits2(LinkedList<TNode> wordlist, double [][][] ep) {
			LinkedList<TNode> result = new LinkedList<TNode>();
			for (int ndx = 0; ndx < wordlist.size(); ++ndx)
			{
			   TNode node = wordlist.get(ndx);
			   String word = node.word;
			   double prob;
			   boolean doNotAdd = false;
			
			   for (int i=0; i < word.length(); ++i) 
			   { // delete				
			      String temp = word.substring(0, i) + word.substring(i+1);
			   
			      prob = node.prob;
				  int index1 = 26;
				  int index2 = 26;
			      switch(node.prev_edit_type)
			      {
			         case DELETION:
						// penalty for deletion
						index1 = 26;
						index2 = 26;
						if (i == 0)
						{
						   if ((temp.charAt(0) >= 'a') && (temp.charAt(0) <= 'z'))
						   {
							  index1 -= 'a';
						   }
						   
						   if (node.prev_edit_index == 0 || node.prev_edit_index == 1)
						   { // First two letters from original word deleted
							  prob += ep[2][index1][index1];   
						   }
						   else
						   {
							  prob += ep[1][index1][index1];
						   }
			            }
						else if (i == temp.length())
						{
						   if ((temp.charAt(temp.length()-1) >= 'a') && (temp.charAt(temp.length()-1) <= 'z'))
						   {
							  index1 -= 'a';
						   }
						   
						   if ((node.prev_edit_index == (i+1)) || (node.prev_edit_index == i))
						   {
						      prob += ep[i-2][index1][index1];   
						   }
						   else
						   {
							  prob += ep[i-1][index1][index1];
						   }
						}
						else
						{
					       if ((temp.charAt(i-1) >= 'a') && (temp.charAt(i-1) <= 'z'))
					       {
					          index1 -= 'a';
					       }
					       
					       if ((temp.charAt(i+1) >= 'a') && (temp.charAt(i+1) <= 'z'))
					       {
						      index2 -= 'a';
						   }
						   
					       int position1 = i - 1; 
					       int position2 = i + 1;
					       if (position1 == node.prev_edit_index)
					       {
					          position1 = i - 2;
					       }
					       if (position2 == node.prev_edit_index)
					       {
					          position2 = i + 2;
					       }
				           
						   if (ep[position1][index1][index1] < ep[position2][index2][index2])
						   {
							  prob += ep[position1][index1][index1];
						   } 
						   else
						   {
							  prob += ep[position2][index2][index2];
						   } 
						}
			            break;
			        	 
			         case TRANSPOSITION:
			        	index1 = 26;
						index2 = 26;
						if (i == 0)
						{
						   if ((temp.charAt(0) >= 'a') && (temp.charAt(0) <= 'z'))
						   {
						      index1 -= 'a';
						   }
						   if (node.prev_edit_index == 0)
						   { // First two letters from original word deleted
							  prob += ep[0][index1][index1];   
						   }
						   else if (node.prev_edit_index == 1)
						   {
							  prob += ep[2][index1][index1];
						   }
						   else
						   {
							  prob += ep[1][index1][index1];
						   }							
						}
						else if (i == temp.length())
						{
						   if ((temp.charAt(temp.length()-1) >= 'a') && (temp.charAt(temp.length()-1) <= 'z'))
						   {
							  index1 -= 'a';
						   }
						   
						   if (node.prev_edit_index == (i-1))
						   {
						      prob += ep[i][index1][index1];   
						   }
						   else	if (node.prev_edit_index == (i-2))
						   {
							  prob += ep[i-2][index1][index1];   
						   }
						   else
						   {
							  prob += ep[i-1][index1][index1];
						   }
						}
						else
						{
						   if ((temp.charAt(i-1) >= 'a') && (temp.charAt(i-1) <= 'z'))
						   {
						      index1 -= 'a';
						   }
						       
						   if ((temp.charAt(i) >= 'a') && (temp.charAt(i) <= 'z'))
						   {
							  index2 -= 'a';
						   }
							   
						   int position1 = i - 1; 
						   int position2 = i + 1;
						   if (i == node.prev_edit_index+1)
						   {
						      position1 = i;
						   }
						   else if (i == node.prev_edit_index+2)
						   {
							  position1 = i-2;
						   }
						   
						   if (i == node.prev_edit_index)
						   {
						      position2 = i;
						   }
						   else if (i == node.prev_edit_index-1)
						   {
							  position2 = i+2;
						   }
					           
						   if (ep[position1][index1][index1] < ep[position2][index2][index2])
						   {
							  prob += ep[position1][index1][index1];
						   } 
						   else
						   {
						      prob += ep[position2][index2][index2];
						   } 						   
						}
			        	break;
			         
			         case INSERTION:
				        index1 = 26;
						index2 = 26;
						if (i == node.prev_edit_index)
						{ // same as original string
						   doNotAdd = true;
						} 
						else
						{ //        original   insert      delete
						  // word:  ABCDEFG -> *ABCDEFG -> *BCDEFG
						  // index: 0123456 -> 01234567 -> 0123456
						  // edit:             prev=0      i=1 
						   if ((i == 0 && node.prev_edit_index == 1) || (i == 1 && node.prev_edit_index == 0))
						   {
							  if ((temp.charAt(1) >= 'a') && (temp.charAt(1) <= 'z'))
							  {
							     index1 -= 'a';
							  }
						      prob += ep[2][index1][index1];   
						   }
						   else if (i == (temp.length() - 1) && node.prev_edit_index == (i - 2) ||
								   (i == (temp.length() - 2) && node.prev_edit_index == (i - 1)))
						   {
							  if ((temp.charAt(temp.length()-1) >= 'a') && (temp.charAt(temp.length()-1) <= 'z'))
							  {
								 index1 -= 'a';
							  }
							  prob += ep[temp.length() - 2][index1][index1];   
						   }
						   else
						   {
							  if ((i - node.prev_edit_index) == -1)
							  {
								 if ((temp.charAt(i-1) >= 'a') && (temp.charAt(i-1) <= 'z'))
								 {
								    index1 -= 'a';
								 }
								       
								 if ((temp.charAt(i+1) >= 'a') && (temp.charAt(i+1) <= 'z'))
								 {
									index2 -= 'a';
								 }
									   
								 int position1 = i - 1; 
								 int position2 = i + 2;
							           
								 if (ep[position1][index1][index1] < ep[position2][index2][index2])
								 {
								    prob += ep[position1][index1][index1];
								 } 
								 else
								 {
								    prob += ep[position2][index2][index2];
								 } 
							  }
							  else if ((i - node.prev_edit_index) == 1)
							  {
								 if ((temp.charAt(i-2) >= 'a') && (temp.charAt(i-2) <= 'z'))
								 {
									index1 -= 'a';
								 }
									       
								 if ((temp.charAt(i) >= 'a') && (temp.charAt(i) <= 'z'))
								 {
									index2 -= 'a';
								 }
										   
								 int position1 = i - 2; 
								 int position2 = i + 1;
								           
								 if (ep[position1][index1][index1] < ep[position2][index2][index2])
								 {
									prob += ep[position1][index1][index1];
								 } 
								 else
								 {
									prob += ep[position2][index2][index2];
								 }
							  }
							  else
							  { // i > node.prev_edit_index and i < node.prev_edit_index
								 if ((temp.charAt(i-1) >= 'a') && (temp.charAt(i-1) <= 'z'))
								 {
								    index1 -= 'a';
								 }
										       
								 if ((temp.charAt(i) >= 'a') && (temp.charAt(i) <= 'z'))
								 {
									index2 -= 'a';
								 }
											   
								 int position1 = i - 1; 
								 int position2 = i + 1;
									           
								 if (ep[position1][index1][index1] < ep[position2][index2][index2])
								 {
									prob += ep[position1][index1][index1];
								 } 
								 else
								 {
									prob += ep[position2][index2][index2];
								 }
							  }
						   }
						}
			        	break;
			        
			         case SUBSTITUTION:
					    index1 = 26;
						index2 = 26;
						if (i == node.prev_edit_index)
						{
						   doNotAdd = true;
						} 
						else
						{
						   if ((i == 0 && node.prev_edit_index == 1) || (i == 1 && node.prev_edit_index == 0))
						   {
							  if ((temp.charAt(1) >= 'a') && (temp.charAt(1) <= 'z'))
							  {
								 index1 -= 'a';
							  }
							  prob += ep[2][index1][index1];   
						   }
						   else if (i == (temp.length() - 1) && node.prev_edit_index == (i - 2) ||
								   (i == (temp.length() - 2) && node.prev_edit_index == (i - 1)))
						   {
							  if ((temp.charAt(temp.length()-1) >= 'a') && (temp.charAt(temp.length()-1) <= 'z'))
							  {
							     index1 -= 'a';
							  }
							  prob += ep[temp.length() - 1][index1][index1];   
						   }
						   else
						   {
							  if (i < node.prev_edit_index)
							  {
								 if ((temp.charAt(i-1) >= 'a') && (temp.charAt(i-1) <= 'z'))
								 {
									index1 -= 'a';
								 }
									       
								 if ((temp.charAt(i+1) >= 'a') && (temp.charAt(i+1) <= 'z'))
								 {
								    index2 -= 'a';
								 }
										   
								 int position1 = i - 1; 
								 int position2 = i + 1;
								           
								 if (ep[position1][index1][index1] < ep[position2][index2][index2])
								 {
									prob += ep[position1][index1][index1];
								 } 
								 else
								 {
									prob += ep[position2][index2][index2];
							     } 
						      }
							  else
							  { // (i < node.prev_edit_index)
								 if ((temp.charAt(i-2) >= 'a') && (temp.charAt(i-2) <= 'z'))
								 {
									index1 -= 'a';
								 }
										       
								 if ((temp.charAt(i) >= 'a') && (temp.charAt(i) <= 'z'))
								 {
								    index2 -= 'a';
								 }
											   
								 int position1 = i - 2; 
								 int position2 = i;
									           
								 if (ep[position1][index1][index1] < ep[position2][index2][index2])
								 {
								    prob += ep[position1][index1][index1];
								 } 
								 else
								 {
									prob += ep[position2][index2][index2];
								 } 
							  }
						   }
						}
				        break;
			      
			         case NONE:
			         default:
			        	 break;
			      
			      }
			      if (!doNotAdd)
				  result.add(new TNode(temp, prob, Editdone.DELETION, i));
			   }			   

			   // **************************************************************
			  //  *************************************************************** 
			   // *************************************************************** 
			  //  *************************************************************** 
			  //  *************************************************************** 
			  //  *************************************************************** 
			  //  *************************************************************** 
			  //  *************************************************************** 
			    //
			   for (int i=0; i < word.length()-1; ++i) 
			   { // Transposition
			      String temp = word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2);
			      prob = node.prob;
				  int index1 = 26;
				  int index2 = 26;
			      switch(node.prev_edit_type)
			      {
			         case DELETION:
				        	index1 = 26;
							index2 = 26;
							if (i == 0)
							{
							   if ((temp.charAt(0) >= 'a') && (temp.charAt(0) <= 'z'))
							   {
							      index1 -= 'a';
							   }
							   if (node.prev_edit_index == 0)
							   { // First two letters from original word deleted
								  prob += ep[0][index1][index1];   
							   }
							   else if (node.prev_edit_index == 1)
							   {
								  prob += ep[2][index1][index1];
							   }
							   else
							   {
								  prob += ep[1][index1][index1];
							   }							
							}
							else if (i == temp.length())
							{
							   if ((temp.charAt(temp.length()-1) >= 'a') && (temp.charAt(temp.length()-1) <= 'z'))
							   {
								  index1 -= 'a';
							   }
							   
							   if (node.prev_edit_index == (i-1))
							   {
							      prob += ep[i][index1][index1];   
							   }
							   else	if (node.prev_edit_index == (i-2))
							   {
								  prob += ep[i-2][index1][index1];   
							   }
							   else
							   {
								  prob += ep[i-1][index1][index1];
							   }
							}
							else
							{
							   if ((temp.charAt(i-1) >= 'a') && (temp.charAt(i-1) <= 'z'))
							   {
							      index1 -= 'a';
							   }
							       
							   if ((temp.charAt(i) >= 'a') && (temp.charAt(i) <= 'z'))
							   {
								  index2 -= 'a';
							   }
								   
							   int position1 = i - 1; 
							   int position2 = i + 1;
							   if (i == node.prev_edit_index+1)
							   {
							      position1 = i;
							   }
							   else if (i == node.prev_edit_index+2)
							   {
								  position1 = i-2;
							   }
							   
							   if (i == node.prev_edit_index)
							   {
							      position2 = i;
							   }
							   else if (i == node.prev_edit_index-1)
							   {
								  position2 = i+2;
							   }
						           
							   if (ep[position1][index1][index1] < ep[position2][index2][index2])
							   {
								  prob += ep[position1][index1][index1];
							   } 
							   else
							   {
							      prob += ep[position2][index2][index2];
							   } 						   
							}
				        	break;
			         
			         case TRANSPOSITION:
			        	
			        	break;
			         
			         case INSERTION:
			        	
			        	break;
			         
			         case SUBSTITUTION:
			        	
			        	break;
			         
			         case NONE:
			         default:
			        	break;
			      }
			   }
			        	 
			         
			        	 
			        	 
			   prob = 0;
			   for (int j = 0; j < i; j++)
			   {
				  int index = 26;
		          if ((temp.charAt(j) >= 'a') && (temp.charAt(j) <= 'z'))
		          {
		             index -= 'a';
		          }
		          prob += ep[j][index][index];
		 	   }
				   			   
			   for (int j = i+2; j < temp.length(); ++i)
			   {
				  int index = 26;
			      if ((temp.charAt(j) >= 'a') && (temp.charAt(j) <= 'z'))
			      {
			         index -= 'a';
			      }
			      prob += ep[j][index][index];
			   }
				   
			   // penalty for insertion
			   int index1 = 26;
			   int index2 = 26;
	           if ((temp.charAt(i) >= 'a') && (temp.charAt(i) <= 'z'))
	           {
	              index1 -= 'a';
	           }
	           prob += ep[i+1][index1][index1];
	              
	           if ((temp.charAt(i+1) >= 'a') && (temp.charAt(i+1) <= 'z'))
	           {
	              index2 -= 'a';
	           }
			   prob += ep[i][index2][index2];			   

			   result.add(new TNode(temp, prob, "transposes", i));
			   //result.add(word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2));
			}
				
			for(int i=0; i < word.length(); ++i) 
				for(char c='a'; c <= 'z'; ++c)
				{ // insert
					
				   String temp = word.substring(0, i) + String.valueOf(c) + word.substring(i+1);
					
				   prob = 0;
				   for (int j = 0; j < i; j++)
				   {
				      int index = 26;
				      if ((temp.charAt(j) >= 'a') && (temp.charAt(j) <= 'z'))
				      {
				         index -= 'a';
				      }
				      prob += ep[j][index][index];
				   }
						   			   
				   for (int j = i+1; j < temp.length(); ++i)
				   {
				      int index = 26;
					  if ((temp.charAt(j) >= 'a') && (temp.charAt(j) <= 'z'))
					  {
					     index -= 'a';
					  }
					  prob += ep[j][index][index];
				   }
						   
				   // penalty for insertion
				   int index1 = 26;
				   int index2 = 26;
				   if (i == 0)
				   {
				      if ((temp.charAt(i+1) >= 'a') && (temp.charAt(i+1) <= 'z'))
				      {
				         index1 -= 'a';
				      }
					  prob += ep[i+1][index1][index1];   
				   }
				   else if (i == (word.length() - 1))
				   {
				      if ((temp.charAt(i-1) >= 'a') && (temp.charAt(i-1) <= 'z'))
				      {
				         index1 -= 'a';
				      }
				      prob += ep[i-1][index1][index1];
				   }
				   else
				   {
			          if ((temp.charAt(i+1) >= 'a') && (temp.charAt(i+1) <= 'z'))
			          {
			             index1 -= 'a';
			          }
			              
			          if ((temp.charAt(i-1) >= 'a') && (temp.charAt(i-1) <= 'z'))
			          {
			             index2 -= 'a';
			          }
			           
					  if (ep[i+1][index1][index1] < ep[i-1][index2][index2])
					  {
						 prob += ep[i+1][index1][index1];
					  } 
					  else
					  {
						 prob += ep[i-1][index2][index2];
					  }
				   }
					
				   result.add(new TNode(temp, prob, "insertion", i));
				   //result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i+1));
				}
			
			for(int i=0; i <= word.length(); ++i) 
				for(char c='a'; c <= 'z'; ++c) 
				{ // substitute
				   String temp = word.substring(0, i) + String.valueOf(c) + word.substring(i);
					
				   prob = 0;
				   for (int j = 0; j < i; j++)
				   {
					  int index = 26;
				      if ((temp.charAt(j) >= 'a') && (temp.charAt(j) <= 'z'))
				      {
				         index -= 'a';
				      }
				      prob += ep[j][index][index];
				   }
						   			   
				   for (int j = i+1; j < temp.length(); ++i)
				   {
				      int index = 26;
					  if ((temp.charAt(j) >= 'a') && (temp.charAt(j) <= 'z'))
					  {
					     index -= 'a';
					  }
					  prob += ep[j][index][index];
				   }
						   
				   // penalty for substitution
				   int index = 26;
			       if ((c >= 'a') && (c <= 'z'))
			       {
			          index -= 'a';
			       }
			       prob += ep[i][index][index];
			              
				   result.add(new TNode(temp, prob, "supstitution", i));
				   //result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));
				}
			}
			
			return result;
		}
		*/


		public final String[] correct(String word, double[][] emission_p) {
			ArrayList<TNode> result = new ArrayList<TNode>();
			String findWord = "";
			LinkedList<TNode> list = edits1(word, emission_p);
			HashMap<String, Integer> candidates = new HashMap<String, Integer>();
			
			String example = list.get(0).word;
			for(int i = 0; i < list.size(); i++)
				if(nWords.containsKey(list.get(i).word)){
					String test = list.get(i).word;
					candidates.put(list.get(i).word, nWords.get(list.get(i).word));
				}
			    // temporary
				else
					candidates.put(list.get(i).word, 1);
			
			int i = 0;
			while(candidates.size() > 0 && i < 25)
			{ // remove key after saving it				   
			   Map.Entry<String, Integer> maxEntry = null;
			   for (Map.Entry<String, Integer> entry : candidates.entrySet())
			   {
			       if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
			       {
			           maxEntry = entry;
			       }
			   }
			   findWord = maxEntry.getKey();
			   for (int j=0; j < list.size(); j++)
			   {
				  if (list.get(j).word == findWord)
				  {
				     result.add(list.get(j));
				     break;
				  }
			   }
			   candidates.remove(maxEntry.getKey());
			   i++;
			}

			/*
			ArrayList<String> list2 = new ArrayList<String>();
			for (i = 0; i < result.size(); i++)
			{  // list 2 is top 25 highest hit
			   list2.add(result.get(i).word);
			}*/
			
			SortedMap<Double,String> rank = new TreeMap<Double,String>();
			for (i = 0; i < result.size(); i++)
			{  // rank is top 25 probability in ascending order (lowest at top)
			   rank.put(result.get(i).prob, result.get(i).word);
			}
			
			ArrayList<String> rankList = new ArrayList<String>();
			for(Map.Entry entry: rank.entrySet())
			{  // list3 is rank in arraylist of string
			   rankList.add((String) entry.getValue());
			}
			
			String[] e_list = new String[rankList.size()];
			int k = 0;
			for (int j = rankList.size()-1; j >= 0; j--)
			{
				e_list[j]=rankList.get(k);
				k++;
			}
			
			SortedMap<Double,String> finalRank = new TreeMap<Double,String>();
			for (int j = 0; j < result.size(); j++)
			{  
			   boolean match = false;
			   for (k = 0; k < e_list.length; k++)
			   {
				  if (result.get(j).word == e_list[k])
				  {
					 match = true;
					 break;
				  }
			   }
			   
			   if (match) // match
			      finalRank.put(((1-.04*j)/3 + 2*(1-.04*k)/3), result.get(j).word);
			   else
				  finalRank.put((1-.04*j)/3, result.get(j).word);
			}
			
			for (k = 0; k < e_list.length; k++)
			{
				boolean match = false;
				for (int j= 0; j< result.size(); j++)
				{
				   if (e_list[k] == result.get(j).word)
				   {
					   match = true;
					   break;
				   }
				}
				
				if (!match)
				{
					finalRank.put(2*(1-.04*k)/3, e_list[k]);
				}
			}
			
			String[] finalList = new String[finalRank.size()];
			i = finalRank.size() - 1;
			for(Map.Entry entry: finalRank.entrySet())
			{
				finalList[i] = (String) entry.getValue();
				i--;
			}
			
			return finalList;
			
			
			/*
			for(String s : list)) 
				for(String w : edit2(s)) 
					if(nWords.containsKey(w)) 
						candidates.put(nWords.get(w),w);
			
			if (candidates.size() > 0)
			{
				while(candidates.size() > 0)
				{
				   result.add(candidates.get(Collections.max(candidates.keySet())));
	               candidates.remove(Collections.max(candidates.keySet()));
				}
				return result;
			}
			else
			{
				result.add(word);
			    return result;
			}
			*/
		}
		
		/*
		public final boolean check(String word) {
			return nWords.containsKey(word) ? true : false;  
		} */

	}
	
}