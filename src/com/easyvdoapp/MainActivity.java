package com.easyvdoapp;


import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.locks.ReadWriteLock;

import com.appnext.appnextsdk.Appnext;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppAd.AdMode;
import com.startapp.android.publish.StartAppSDK;

import org.apache.commons.io.FileExistsException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.content.Intent;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {
	
	final Context context = this;

	Appnext appnext;
		
	public String videoFormat_Quality;
	public String DownVideoLink;
	private WebView web;
	private WebView web2;
	private static final String TAG_DATA = "data";
	private static final String TAG_ITEMS = "items";
	private static final String TAG_VID = "id";
	private static final String TAG_TITLE = "title";
	private static final String TAG_THUMBNAIL = "thumbnail";
	private static final String TAG_THUMBNAIL_URL = "sqDefault";

	// declare the dialog as a member field of your activity
	private ProgressDialog cDialog;
	public String DownUrl = "";	
	final Context myApp = this;
    public String ybt = "y"+"o"+"u"+"t"+"u"+"b"+"e";
	//JSONObject data = null;
	JSONArray contacts = null;
	JSONObject data = null;
	ArrayList<HashMap<String, String>> MyArrList;
	ArrayList<HashMap<String, String>> MyDivArrayList;
    private EditText txtSearchBox;
    private ImageButton btnSearch;
    private ListView lstView1;
    private String downMP3id="";
    private String jsonStr;	
    private File destinationFile;
    private String baslik;
    public int Dakika = 0;
    public int Saat = 0;
    public int Saniye = 0;
    public String TESTSTRING = "1";
    public String READSTRING = "1";
    public AlertDialog.Builder imageDialog1;
    public LayoutInflater inflater1;
    public Dialog myDialog;
    public TextView ads;
    public Resources res;
    public String[] btnTexts;
    public Button btnYes;
    public Button btnLater;
    public Button btnNever;
    
    private StartAppAd startAppAd = new StartAppAd(this);

    public static boolean isSet(String param) { 
        // doesn't ignore spaces, but does save an object creation.
        return param !=null && param.length() != 0; 
    }


    @Override
    public void onResume() {
        super.onResume();
        startAppAd.onResume();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        startAppAd.onPause();
    }
    
    @Override
    public void onBackPressed() {
        startAppAd.onBackPressed();
        super.onBackPressed();
    }
    
    
    public String getFileName(String url) {  
        String filenameWithoutExtension = "";  
        filenameWithoutExtension = String.valueOf(baslik  
                  + ".mp4");  
        return filenameWithoutExtension;  
   }  
    

    public void openFile()
    {
    	Intent intent = new Intent();  
    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	intent.setAction(android.content.Intent.ACTION_VIEW);    
    	intent.setDataAndType(Uri.fromFile(destinationFile), "video/mp4");  
    	startActivity(intent);
    }
    
    
    public void readAdTextFile()
    {
//    	
//        final File destinationDir = new File (Environment.getExternalStorageDirectory(), "Aria");
//        if (!destinationDir.exists()) {
//            destinationDir.mkdir(); // Don't forget to make the directory if it's not there
//        }
    	
    	
    	String Path  = this.getFilesDir().getAbsolutePath() + "/reklam.txt";
    	//String Path = Environment.getExternalStorageDirectory().getPath() + "/Aria/reklam.txt";
    	 File file = new File(Path);
    	
    	if(file.exists())
    	{

    	FileInputStream fIn = null;
		try {			
			fIn = openFileInput("reklam.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        InputStreamReader isr = new InputStreamReader(fIn);
        BufferedReader inRd = new BufferedReader(isr);
        
        String getText = "";
        try {
        	
        	 char[] inputBuffer = new char[TESTSTRING.length()];
        	 isr.read(inputBuffer);
        	 READSTRING = new String(inputBuffer);
//			while ((getText = inRd.readLine()) != null)
//			{
//			    Toast.makeText(getApplicationContext(), getText, Toast.LENGTH_SHORT).show();
//			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    	}
    	
    }
    
    

    public void writeAdTextFile()
    {
    	 //  try {
    	       // TESTSTRING = new String("Hello Android");     
    		   
    		   
    		   try {
    		        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("reklam.txt", Context.MODE_PRIVATE));
    		        outputStreamWriter.write(TESTSTRING);
    		        outputStreamWriter.close();
    		    }
    		    catch (IOException e) {
    		        Log.e("Exception", "File write failed: " + e.toString());
    		    } 
//    	        FileOutputStream fOut;
//    	      
//    				fOut = openFileOutput(this.getFilesDir().getAbsolutePath() + "/reklam.txt",MODE_WORLD_READABLE);
//    				 OutputStreamWriter osw = new OutputStreamWriter(fOut); 
//    				 osw.write(TESTSTRING);
//    				 osw.flush();
//    			        osw.close();
//    			} catch (FileNotFoundException e1) {
//    				// TODO Auto-generated catch block
//    				e1.printStackTrace();  
//    			} catch (IOException e) {
//    				// TODO Auto-generated catch block
//    				e.printStackTrace();
//    			}
    	       
    }
    
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartAppSDK.init(this, "109320286", "209081260", true);
        setContentView(R.layout.activity_main);
        
        myDialog = new Dialog(context);
        inflater1 = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout1 = inflater1.inflate(R.layout.button_layout,
                (ViewGroup) findViewById(R.id.layout_root1));
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(layout1);
       
        
        
       
        //title.setGravity(Gravity.CENTER);
        //title.setTextSize(8);
       // title.setBackgroundColor(Color.GRAY);
       // title.setTextColor(Color.WHITE);
 
        //myDialog.setTitle(ads.getText);
       //myDialog.setTitle(Html.fromHtml("<font size='6'>"+title.getText()+"</font>"));

        
        
//       imageDialog1 = new AlertDialog.Builder(this);
//       inflater1 = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
//       View layout1 = inflater1.inflate(R.layout.button_layout,
//                   (ViewGroup) findViewById(R.id.layout_root1));
//        
//        imageDialog1.setView(layout1);
//        imageDialog1.setTitle("Youtube videolarını mp3 olarak indirmek ister misiniz?");
//        
        readAdTextFile();
      
      if(!READSTRING.equals("0"))
      {   
    	  
    	  myDialog.show();
    	  //imageDialog1.create();
          //imageDialog1.show();
 
      }
      
        btnYes = (Button) layout1.findViewById(R.id.btnYes);
        btnLater = (Button) layout1.findViewById(R.id.btnLater);
        btnNever = (Button) layout1.findViewById(R.id.btnNever);
        
        
        
        res = getResources();
        ads =  (TextView)layout1.findViewById(R.id.txtAds);
        
        setAdsLang();
        
        
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	
            	TESTSTRING = "0";
            	
            	writeAdTextFile();
            	myDialog.dismiss();
//            	alertDialog.cancel();
//            	alertDialog.dismiss();
//            	
            	final String appPackageName = "com.ariamsc"; // getPackageName() from Context or Activity object
            	try {
            	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            	} catch (android.content.ActivityNotFoundException anfe) {
            	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
            	}
          
                
                //Toast.makeText(getApplicationContext(), READSTRING, Toast.LENGTH_SHORT).show();
            }
        });

        
        
        btnLater.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
 
				TESTSTRING = "2";
            	
            	writeAdTextFile();
            	myDialog.dismiss();
            	
//            	alertDialog.cancel();
//            	alertDialog.dismiss();
            	
			}
		});
        
        
      btnNever.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				

				TESTSTRING = "0";
            	
            	writeAdTextFile();
				
            	myDialog.dismiss();
//            	alertDialog.cancel();
//            	alertDialog.dismiss();
            	
			}

		});
        
        

        
        //boolean isLang = Locale.getDefault().getLanguage().equals("xx");
        
     
        
        
        
//        Dialog settingsDialog = new Dialog(this); 
//        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE); 
//        settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.button_layout 
//                , null)); 
//        settingsDialog.show(); 
        
            appnext = new Appnext(this);
     		appnext.setAppID("f881c908-3f0f-4c38-8878-cbc9a53c9f80"); // Set your AppID
     		appnext.showBubble();
     		    	
         BroadcastReceiver onComplete=new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
            	 String action = intent.getAction();
                 if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                	 openFile();
                 }                 
            }
            
        };
        
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            
        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
  
              // listView1
        lstView1 = (ListView)findViewById(R.id.listView1); 
        lstView1.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        lstView1.setStackFromBottom(true);
        lstView1.setFastScrollEnabled(true);
        lstView1.setScrollingCacheEnabled(false); 
        txtSearchBox = (EditText)findViewById(R.id.txtSearchBox);        
        txtSearchBox.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(40)
       });	
        
        btnSearch = (ImageButton)findViewById(R.id.button1);
        //btnClear = (ImageButton)findViewById(R.id.button2);
        web = (WebView) findViewById(R.id.webView1);
	    web2 = (WebView) findViewById(R.id.webView2);
	    web.getSettings().setJavaScriptEnabled(true);
	  	web2.getSettings().setJavaScriptEnabled(true);
	  	web2.destroyDrawingCache();
    	web.destroyDrawingCache();
           
	    MyArrList = new ArrayList<HashMap<String, String>>();
	    MyDivArrayList = new ArrayList<HashMap<String, String>>();
             
        btnSearch.setOnClickListener(new OnClickListener()
        {
        public void onClick(View v)
        {
        
        	if(MyArrList.size()>0)
        	{
              MyArrList.clear();
          
        	 ((BaseAdapter) lstView1.getAdapter()).notifyDataSetChanged();
        	}
        	
        	InputMethodManager imm = (InputMethodManager)getSystemService(
        		      Context.INPUT_METHOD_SERVICE);
        		imm.hideSoftInputFromWindow(txtSearchBox.getWindowToken(), 0);
        	
         	 if( txtSearchBox.getText().toString().trim().equals(""))
        	 {    
        		 txtSearchBox.setError( "This field can not be blank!" );

        		 txtSearchBox.setHint("Enter keywords to search");
        		 
        		 return;
        	 }
        	
         	 if(lstView1.getCount() > 0)
         	 {         		 
         		 Toast.makeText(getApplicationContext(), "Please clear list!", Toast.LENGTH_LONG).show();
         	     return;
         	 }
            		 
    		 new GetContacts().execute();
 
        }
        });

            final AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
            final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
	        
            
            
               // OnClick
    	        lstView1.setOnItemClickListener(new OnItemClickListener() {

    				public void onItemClick(AdapterView<?> parent, View v,
    					int position, long id) {	
  			            
    					CookieSyncManager.createInstance(MainActivity.this);         
                        CookieManager cookieManager = CookieManager.getInstance();        
                        cookieManager.removeAllCookie();
   
    					web.clearCache(true);
    					web2.clearCache(true);
						baslik = MyArrList.get(position).get(TAG_TITLE).toString();

    					final ProgressDialog mProgress;	
    					downMP3id = MyArrList.get(position).get(TAG_VID).toString();
        	        	
    				
	               		 /* WebViewClient must be set BEFORE calling loadUrl! */
	                    	/* Register a new JavaScript interface called HTMLOUT */

    					 web.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
    					 web.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
	               		 web2.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
	       
	               		  
	               		View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
   	                        (ViewGroup) findViewById(R.id.layout_root));
   	                ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
   	                
   	       
   		           	 try
   		           	 {
   		           		image.setImageBitmap(loadBitmap(MyArrList.get(position).get(TAG_THUMBNAIL_URL)));
   		           	 } catch (Exception e) {
   		           		 // When Error
   		           		image.setImageResource(android.R.drawable.ic_menu_report_image);
   		           	 }
   		           
   	                imageDialog.setIcon(android.R.drawable.btn_star_big_on);	
   	        		imageDialog.setTitle(MyArrList.get(position).get(TAG_TITLE));
   	        		
   	        		
   	        		if (layout.getParent() == null) {
   	        			imageDialog.setView(layout);
   	        		} else {
   	     
   	        			layout = null; //set it to null
   	        		    // now initialized yourView and its component again
   	        		 imageDialog.setView(layout);
   	        		}
   	        		
   	        		
   	                    imageDialog.setPositiveButton("Download", new DialogInterface.OnClickListener(){

	                   
	                    public void onClick(DialogInterface dialog, int which) {
	                                 
	                    	
	                    	for (HashMap<String, String> map : MyDivArrayList) {
	                    	    for (String key : map.keySet()) {
	                    	      if (key.contains("Mp4")&&key.contains("360")) {

	                    	          DownUrl= map.get(key);

	                    	      }
	                    	   } 
	                    	}
	                    	startAppAd.loadAd(AdMode.AUTOMATIC);
	       
	                    	//start download mp3
	                    	if (!DownUrl.startsWith("http://") && !DownUrl.startsWith("https://"))
	                    		DownUrl = "http://" + DownUrl;
	                    	
	                    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(DownUrl));
	                    	startActivity(browserIntent);
	                    	
                     	      // Intent i = new Intent(Intent.ACTION_VIEW);
		                      // i.setData(Uri.parse(DownUrl));		                       
    		                  // startActivity(i); 
    		                   startAppAd.showAd();
    		                   startAppAd.loadAd();
    		                   
    		                //	           
	                          	
	    //	                    	if(DownUrl.equals(""))
//	                    	{
//	                    		
//	                    		 web.loadUrl("javascript:window.HTMLOUT.showHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
//	                    		 Toast msg = Toast.makeText(getBaseContext(),
//		                                  "Link Boş geldi", Toast.LENGTH_LONG);
//			                
//		                           msg.show();     
//		                 
//	                    	}
//	                    	else
//	                    	{
//	                    		 Toast msg = Toast.makeText(getBaseContext(),
//		                                  DownUrl, Toast.LENGTH_LONG);
//			                
//		                           msg.show();     
//		                 
//	                    	}

                             	
//		                      web2.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		                 
////		                      web2.loadUrl(DownUrl); 
//		                      final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);	
//	                          final File destinationDir = new File (Environment.getExternalStorageDirectory(), "Video Download Tube Pro");
//		                      if (!destinationDir.exists()) {
//		                          destinationDir.mkdir(); // Don't forget to make the directory if it's not there
//		                      }
       
//		                      web2.setWebViewClient(new WebViewClient() {
//                  	            @Override
//                  	            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                  	            	boolean shouldOverride = true;
//                  	
//			                 	 Toast msg = Toast.makeText(getBaseContext(),
//		                                  "Please wait download will start in a moment", Toast.LENGTH_LONG);
//			                
//		                           msg.show();     
//		                 
//	                    	            	if(url.startsWith("https:"))
//	                    	            	{
//		                           if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH){
//		                        	   Intent i = new Intent(Intent.ACTION_VIEW);
//        		                       i.setData(Uri.parse(DownUrl));
//	           		                   startActivity(i); 
//		                        	  }
//		                           else
//		                           {
//		                               url=url.replace("https", "http");
//		                        	   Uri source = Uri.parse(url);
//	                                      // Make a new request pointing to the .apk url
//		                        	      
//	                                      DownloadManager.Request request = new DownloadManager.Request(source);
//	                                      // appears the same in Notification bar while downloading		                                     
//	                                      request.setTitle(baslik);
//	                                      request.setDescription("Downloading via Video Download Tube..");
//	                                      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//	                                          request.allowScanningByMediaScanner();
//	                                          request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//	                                      }
//	                                      
//	                                      destinationFile = new File (destinationDir, getFileName(baslik));
//	                                      // save the file in the "Downloads" folder of SDCARD
//	                                      request.setDestinationUri(Uri.fromFile(destinationFile));
//	                                      
//	                                      manager.enqueue(request);
//                                   
//	  
//                                   // destinationFile = new File (destinationDir, getFileName(url));
//	                                   request.setDestinationUri(Uri.fromFile(destinationFile));
//	                                    // Add it to the manager
//	      
//		                           }
	                                    		
	                                   
	                    	            	
//	                    	            	 return shouldOverride;
//	                    	            }
//
//                  	           
//            	                  });
//            	            web2.loadUrl(DownUrl);
	                    }
	                    
	                    }); //Download button end
	                
	                	                	                	                
	                imageDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                    	dialog.cancel();
	                    	dialog.dismiss();
	                  } });

   	        		
   	        	  		mProgress = new ProgressDialog(MainActivity.this);
	           
	            mProgress.setMessage("Loading , Please wait...");
	            mProgress.setCancelable(false);
	               		mProgress.show();
	               		
	               		
	                 		
	               		 web2.setWebChromeClient(new WebChromeClient());
	               		 web.setWebViewClient(new WebViewClient() {
	               		     @Override
	               		     public void onPageFinished(WebView view, String url)
	               		     {	               		    		               		    	 
	               		         /* This call inject JavaScript into the page which just finished loading. */

	               		    	
	               		    	               		    	
	               		    	 //web.loadUrl("javascript:document.getElementById('home_search_q').value='"+test+"';");
	               		    	 
	               		    	 //web.loadUrl("javascript:document.getElementById('home_search_submit').click();");
	               		    	              		    	 
	               		    	
	               		    	//try {
	               		    	  //Thread.sleep(5000);  
	               		    		web.loadUrl("javascript:window.HTMLOUT.showHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
		               		    	   
	               		    		               //1000 milliseconds is one second.
	               		    	//} catch(InterruptedException ex) {
	               		    	  //  Thread.currentThread().interrupt();
	               		    	//}
	               		    	 
	               		    	 	  	                    	
              		  		  if(mProgress.isShowing()) {
	                                 mProgress.dismiss();	
	                                 imageDialog.create();
	                                 imageDialog.show();
	                            		  		  }
                          
	                             }           		  		
              		  			               		    	
	               		    
	               		 });     		 
	               		 /* load a web page */
	               		
	               	
	               		//web.loadUrl("http://www.downvids.net/");
	               		 
	               		web.loadUrl("http://9xbuddy.com/"+ybt+"?url=http://www."+ybt+".com/watch?v="+downMP3id+"+#download_box");
	               		 
	              }
    			});	
           
	    
			
		} 
  
    //reklam dilini ayarla 
    
    public void setAdsLang()
    {
    	String dil= "0";
     	  	
    	if(Locale.getDefault().getLanguage().equals("en"))
        {
        	ads.setText(getString(R.string.adsTextEn));
        	btnTexts = res.getStringArray(R.array.adsTextEn_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);
        	dil = "1";
  	
        }
        	
        if(Locale.getDefault().getLanguage().equals("tr"))
        {
        	ads.setText(getString(R.string.adsTextTr));
        	btnTexts = res.getStringArray(R.array.adsTextTr_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);
        	dil = "1";
        
        }
        
        if(Locale.getDefault().getLanguage().equals("de"))
        {
        	ads.setText(getString(R.string.adsTextDe));
        	btnTexts = res.getStringArray(R.array.adsTextDe_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);
        	dil = "1";
        
        }
        
        if(Locale.getDefault().getLanguage().equals("zh"))
        {
        	ads.setText(getString(R.string.adsTextZh));
        	btnTexts = res.getStringArray(R.array.adsTextZh_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);
        	dil = "1";
        
        }
        
        if(Locale.getDefault().getLanguage().equals("cs"))
        {
        	ads.setText(getString(R.string.adsTextCs));
        	btnTexts = res.getStringArray(R.array.adsTextCs_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);
        	dil = "1";
        }
        
        
        if(Locale.getDefault().getLanguage().equals("nl"))
        {
        	ads.setText(getString(R.string.adsTextNl));
        	btnTexts = res.getStringArray(R.array.adsTextNl_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("fr"))
        {
        	ads.setText(getString(R.string.adsTextFr));
        	btnTexts = res.getStringArray(R.array.adsTextFr_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);   
        	dil = "1";
        }
        
        
        if(Locale.getDefault().getLanguage().equals("it"))
        {
        	ads.setText(getString(R.string.adsTextIt));
        	btnTexts = res.getStringArray(R.array.adsTextIt_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]); 
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("ja"))
        {
        	ads.setText(getString(R.string.adsTextJa));
        	btnTexts = res.getStringArray(R.array.adsTextJa_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]); 
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("ko"))
        {
        	ads.setText(getString(R.string.adsTextKo));
        	btnTexts = res.getStringArray(R.array.adsTextKo_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);    
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("pl"))
        {
        	ads.setText(getString(R.string.adsTextPl));
        	btnTexts = res.getStringArray(R.array.adsTextPl_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]); 
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("ru"))
        {
        	ads.setText(getString(R.string.adsTextRu));
        	btnTexts = res.getStringArray(R.array.adsTextRu_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]); 
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("es"))
        {
        	ads.setText(getString(R.string.adsTextEs));
        	btnTexts = res.getStringArray(R.array.adsTextEs_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]); 
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("ar"))
        {
        	ads.setText(getString(R.string.adsTextAr));
        	btnTexts = res.getStringArray(R.array.adsTextAr_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);    
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("bg"))
        {
        	ads.setText(getString(R.string.adsTextBg));
        	btnTexts = res.getStringArray(R.array.adsTextBg_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);    
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("ca"))
        {
        	ads.setText(getString(R.string.adsTextCa));
        	btnTexts = res.getStringArray(R.array.adsTextCa_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);   
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("hr"))
        {
        	ads.setText(getString(R.string.adsTextHr));
        	btnTexts = res.getStringArray(R.array.adsTextHr_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);
        	dil = "1";
        }
        
        
        if(Locale.getDefault().getLanguage().equals("da"))
        {
        	ads.setText(getString(R.string.adsTextDa));
        	btnTexts = res.getStringArray(R.array.adsTextDa_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]); 
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("fn"))
        {
        	ads.setText(getString(R.string.adsTextFn));
        	btnTexts = res.getStringArray(R.array.adsTextFn_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);   
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("el"))
        {
        	ads.setText(getString(R.string.adsTextEl));
        	btnTexts = res.getStringArray(R.array.adsTextEl_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("iw"))
        {
        	ads.setText(getString(R.string.adsTextIw));
        	btnTexts = res.getStringArray(R.array.adsTextIw_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);  
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("hi"))
        {
        	ads.setText(getString(R.string.adsTextHi));
        	btnTexts = res.getStringArray(R.array.adsTextHi_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);   
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("hu"))
        {
        	ads.setText(getString(R.string.adsTextHu));
        	btnTexts = res.getStringArray(R.array.adsTextHu_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);     
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("in"))
        {
        	ads.setText(getString(R.string.adsTextIn));
        	btnTexts = res.getStringArray(R.array.adsTextIn_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);      
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("lt"))
        {
        	ads.setText(getString(R.string.adsTextLt));
        	btnTexts = res.getStringArray(R.array.adsTextLt_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);       
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("lv"))
        {
        	ads.setText(getString(R.string.adsTextLv));
        	btnTexts = res.getStringArray(R.array.adsTextLv_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);       
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("nb"))
        {
        	ads.setText(getString(R.string.adsTextNb));
        	btnTexts = res.getStringArray(R.array.adsTextNb_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);   
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("pt"))
        {
        	ads.setText(getString(R.string.adsTextPt));
        	btnTexts = res.getStringArray(R.array.adsTextPt_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);     
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("ro"))
        {
        	ads.setText(getString(R.string.adsTextRo));
        	btnTexts = res.getStringArray(R.array.adsTextRo_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);     
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("sr"))
        {
        	ads.setText(getString(R.string.adsTextSr));
        	btnTexts = res.getStringArray(R.array.adsTextSr_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("sk"))
        {
        	ads.setText(getString(R.string.adsTextSk));
        	btnTexts = res.getStringArray(R.array.adsTextSk_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);  
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("sl"))
        {
        	ads.setText(getString(R.string.adsTextSl));
        	btnTexts = res.getStringArray(R.array.adsTextSl_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("sv"))
        {
        	ads.setText(getString(R.string.adsTextSv));
        	btnTexts = res.getStringArray(R.array.adsTextSv_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);     
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("tl"))
        {
        	ads.setText(getString(R.string.adsTextTl));
        	btnTexts = res.getStringArray(R.array.adsTextTl_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);  
        	dil = "1";
        }

        if(Locale.getDefault().getLanguage().equals("th"))
        {
        	ads.setText(getString(R.string.adsTextTh));
        	btnTexts = res.getStringArray(R.array.adsTextTh_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);  
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("uk"))
        {
        	ads.setText(getString(R.string.adsTextUk));
        	btnTexts = res.getStringArray(R.array.adsTextUk_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);   
        	dil = "1";
        }
        
        if(Locale.getDefault().getLanguage().equals("vi"))
        {
        	ads.setText(getString(R.string.adsTextVi));
        	btnTexts = res.getStringArray(R.array.adsTextVi_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);   
        	dil = "1";
        }
        
        
        if(dil == "0")
        {
        	ads.setText(getString(R.string.adsTextEn));
        	btnTexts = res.getStringArray(R.array.adsTextEn_array);
        	btnYes.setText(btnTexts[0]);
        	btnLater.setText(btnTexts[1]);
        	btnNever.setText(btnTexts[2]);
        	
        }
    	
    }
    
    /***** Get Image Resource from URL (Start) *****/
  		private static final String TAG = "ERROR";
  		private static final int IO_BUFFER_SIZE = 4 * 1024;
  	
  		
 	
  		public static Bitmap loadBitmap(String url) {
  		    Bitmap bitmap = null;
  		    InputStream in = null;
  		    BufferedOutputStream out = null;

  		    try {
  		        in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);

  		        final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
  		        out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
  		        copy(in, out);
  		        out.flush();

  		        final byte[] data = dataStream.toByteArray();
  		        BitmapFactory.Options options = new BitmapFactory.Options();
  		        //roptions.inSampleSize = 1;
  		      
  		        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
  
  		    } catch (IOException e) {
  		        Log.e(TAG, "Could not load Bitmap from: " + url);
  		    } finally {
  		        closeStream(in);
  		        closeStream(out);
  		    }

  		   

  		
  	    return bitmap;
  		}

  		
  	//decodes image and scales it to reduce memory consumption
  
  		
  		
  		
  		 //for rounded image
  		public static Bitmap loadRoundedBitmap(Bitmap bitmap) {
  			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
  			        bitmap.getHeight(), Config.ARGB_8888);
  			    Canvas canvas = new Canvas(output);

  			    final int color = 0xff424242;
  			    final Paint paint = new Paint();
  			    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
  			    final RectF rectF = new RectF(rect);
  			    final float roundPx = 35;

  			    paint.setAntiAlias(true);
  			    canvas.drawARGB(0, 0, 0, 0);
  			    paint.setColor(color);
  			    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

  			    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
  			    canvas.drawBitmap(bitmap, rect, rect, paint);
  			
  			    return output;

  		}
  		 private static void closeStream(Closeable stream) {
  		        if (stream != null) {
  		            try {
  		                stream.close();
  		            } catch (IOException e) {
  		                android.util.Log.e(TAG, "Could not close stream", e);
  		            }
  		        }
  		    }
  		 
  		 private static void copy(InputStream in, OutputStream out) throws IOException {
  	        byte[] b = new byte[IO_BUFFER_SIZE];
  	        int read;
  	        while ((read = in.read(b)) != -1) {
  	            out.write(b, 0, read);
  	        }
  	    }
  		 /***** Get Image Resource from URL (End) *****/
  	
  	/*** Get JSON Code from URL ***/
  	public String getJSONUrl(String url) {
  		StringBuilder str = new StringBuilder();
  		HttpClient client = new DefaultHttpClient();
  		HttpGet httpGet = new HttpGet(url);
  		try {
  			HttpResponse response = client.execute(httpGet);
  			StatusLine statusLine = response.getStatusLine();
  			int statusCode = statusLine.getStatusCode();
  			if (statusCode == 200) { // Download OK
  				HttpEntity entity = response.getEntity();
  				InputStream content = entity.getContent();
  				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
  				String line;
  				while ((line = reader.readLine()) != null) {
  					str.append(line);
  				}
  			} else {
  				Log.e("Log", "Failed to download file..");
  			}
  		} catch (ClientProtocolException e) {
  			e.printStackTrace();
  		} catch (IOException e) {
  			e.printStackTrace();
  		}
  		return str.toString();
  	}
    
  	//*****************             Download part  *************************************//
  	
  	
  	/* An instance of this class will be registered as a JavaScript interface */
  	//@SuppressLint("SetJavaScriptEnabled")
	class MyJavaScriptInterface
	{
		
		@JavascriptInterface
	    @SuppressWarnings("unused")
	    public void showHTML(String html) throws Throwable
	    {   
			
            Document document = Jsoup.parse(html);

            // selector query
	        //Elements nodeBlogStats = document.select("div.link-group > a");
	        
//

//		    	 document.getElementById("home_search_q").appendText(test);
            
//            for(Element e:document.body().getElementsContainingOwnText("MP4"))
//            {
//            	if(e.attr("title").contains("360"))
//            	{         			
//            	  MP4_360p = e.attr("href");
//            	  DownUrl = MP4_360p;
//            	}
//            	
//            }
            
            
            
            
            Elements inputElements = document.getElementsByTag("li");
            String tempOutputFormat = null;
            String tempVideoQuality = null;
            
            int say = 0;
        
    		for (Element inputElement : inputElements) {
     
    			
    			Elements DivElements = inputElement.getAllElements();
    			
    			   tempOutputFormat = "";
     	           tempVideoQuality = "";
    		       
    			for (Element divelement : DivElements) {
    				{
    					
    					
   					if(divelement.className().equals("output_format"))
    					{
    					  say++;
    					  tempOutputFormat = divelement.text();						
    					}
    					
    					if(divelement.className().equals("output_quality"))
     					{
    						tempVideoQuality = divelement.text();						
    					}
    					
    					
    					 
    					if(divelement.className().equals("output_size")&&say>2)
    					{
    				
    						
    						Element link = divelement.getElementsByTag("a").first();
    						
    						 DownVideoLink = link.attr("href");
    						
    						 videoFormat_Quality = tempOutputFormat+"|"+tempVideoQuality;
    						 
    						 
    						 HashMap<String,String> divmap = new HashMap<String,String>();

     						  divmap.put(videoFormat_Quality, DownVideoLink);
     						
     						 MyDivArrayList.add(divmap);
    						
    					}
    			
    				}
    
    				
    			}
    		}
            
            
            
//        	Elements inputElements = document.getElementsByTag("a");
//       
//    		for (Element inputElement : inputElements) {
//     
//    			
//    			if(inputElement.text().contains("Download this Video"))
//    			{
//    				DownUrl = inputElement.attr("href");
//
//    					break;
//    				}
//    				
//    			}
    		}
	}
//            
//            for(Element e:document.body().getElementsContainingOwnText("Download this Video"))
//            {
//
//            	  DownUrl = e.attr("href");
//
//            }
	

           

	
	public String pageHTML = ""; // the HTML for the page
 	
    public class ImageAdapter extends BaseAdapter 
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();

        public ImageAdapter(Context c, ArrayList<HashMap<String, String>> list) 
        {
        	// TODO Auto-generated method stub
            context = c;
            MyArr = list;
        }
 
        public int getCount() {
        	// TODO Auto-generated method stub
            return MyArr.size();
        }
 
        public Object getItem(int position) {
        	// TODO Auto-generated method stub
            return position;
        }
 
        public long getItemId(int position) {
        	// TODO Auto-generated method stub
            return position;
        }
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 
		 
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.activity_column, null); 
				//convertView.setBackgroundResource(R.drawable.rounded_corners);
			}

			// ColImage
			ImageView imageView = (ImageView) convertView.findViewById(R.id.ColImgPath);
			imageView.getLayoutParams().height = 100;
			imageView.getLayoutParams().width = 100;
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        	 try
        	 {
        		 imageView.setImageBitmap(loadRoundedBitmap(loadBitmap(MyArr.get(position).get(TAG_THUMBNAIL_URL))));
        	 } catch (Exception e) {
        		 // When Error
        		 imageView.setImageResource(android.R.drawable.ic_menu_report_image);
        	 }
					
			// ColPicname
			TextView txtPicName = (TextView) convertView.findViewById(R.id.ColImgDesc);
			txtPicName.setPadding(5, 20, 0, 0);
			txtPicName.setTypeface(null, Typeface.BOLD);
			txtPicName.setText(MyArr.get(position).get(TAG_TITLE));
			
			
			TextView txtVideoTime = (TextView) convertView.findViewById(R.id.VideoDuration);
			txtVideoTime.setPadding(5, 20, 0, 0);
			txtVideoTime.setTypeface(null, Typeface.BOLD);
			txtVideoTime.setText(MyArr.get(position).get("sure"));
		 
			return convertView;
				
		}
    }
    
    
    /**
	 * Async task class to get json by making HTTP call
	 * */
	
	@SuppressLint("NewApi")
	private class GetContacts extends AsyncTask<Void, Void, Void> {

		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			cDialog = new ProgressDialog(MainActivity.this);
			cDialog.setMessage(getString(R.string.videoListLoad));
			cDialog.setCancelable(false);
			cDialog.show();
			
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			


			 String searchWord = txtSearchBox.getText().toString();
        	 searchWord = searchWord.replaceAll("\\s","+").trim();
             final String url = "http://gdata."+ybt+".com/feeds/api/videos?q="+searchWord+"&max-results=50&v=2&alt=jsonc";
              
             
        	 jsonStr = getJSONUrl(url);
        	 
        	
        	 
          	 try {
                 JSONObject jsonObj = new JSONObject(jsonStr);
                  
                 // Getting JSON Array node
                 data = jsonObj.getJSONObject(TAG_DATA);
                 contacts = data.getJSONArray(TAG_ITEMS);

                   
                 // looping through All Contacts
                 for (int i = 0; i < contacts.length(); i++) {
                     JSONObject c = contacts.getJSONObject(i);
                      
                     String id = c.getString(TAG_VID);
                     String title = c.getString(TAG_TITLE);
                           // Phone node is JSON Object
                     JSONObject thumbnail = c.getJSONObject(TAG_THUMBNAIL);
                     String thumbnailUrl = thumbnail.getString(TAG_THUMBNAIL_URL);
                     int duration = c.getInt("duration");
                     float videoDk = 0;
                    // tmp hashmap for single contact
                     
                     if(!(duration <60))
                	 {
                     if(duration < 3600)
                     {
                    	
                       videoDk = (float)duration / 60;
                       Dakika = (int) Math.abs(videoDk);
                       Saniye = (int) ((videoDk - Dakika)*60);
                       Saat = 0;
                     }
                     else
                     {
                    	 float videoSaat =(float) duration / 3600;
                    	 Saat = (int) Math.abs(videoSaat);
                    	 videoDk = (videoSaat-Saat)*60;
                    	 Dakika  = (int) Math.abs(videoDk);
                    	 Saniye = (int) ((videoDk - Dakika)*60);
                    	
                   	 
                     }
                	 }
                     else
                     {
                    	 Saat = 0;
                    	 Dakika = 0;
                    	 Saniye = duration;
                     }
                     
                     String time = Saat+":"+Dakika+":"+Saniye;                    
                     HashMap<String, String> map = new HashMap<String, String>();

                     // adding each child node to HashMap key => value
                      map.put("sure", time);
                      map.put(TAG_VID, id);
                      map.put(TAG_TITLE, title);
                      map.put(TAG_THUMBNAIL_URL, thumbnailUrl);
                    // myImage.setImageUrl(thumbnailUrl);
                     // adding contact to contact list
                      MyArrList.add(map);
                     

                 }
             } catch (JSONException e) {
                 e.printStackTrace();
             }
 
            return null;
		}
		
		private Drawable LoadImageFromURL(String url) {
	        try {
	            InputStream is = (InputStream) new URL(url).getContent();
	            Drawable d = Drawable.createFromStream(is, "src name");
	            return d;
	        } catch (Exception e) {
	            System.out.println("Excecption is=" + e);
	            return null;
	        }
	    }
	
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
						

			ImageAdapter adapter = new ImageAdapter(getApplicationContext(), MyArrList);       
            lstView1.setAdapter(adapter);
			// Dismiss the progress dialog
        
            if(cDialog!=null) 
            {
			if (cDialog.isShowing())
				try
		      {
					cDialog.dismiss();
		      }
		      catch(Exception e) { }
				
				
            }
			if(lstView1.getCount() == 0)
            {           	 
           	 Toast.makeText(getApplicationContext(), "No results were found!",Toast.LENGTH_LONG).show();
            }
            
            lstView1.setSelection(0);
          
			/**
			 * Updating parsed JSON data into ListView
			 * */
									
			
		}
  
	}

}