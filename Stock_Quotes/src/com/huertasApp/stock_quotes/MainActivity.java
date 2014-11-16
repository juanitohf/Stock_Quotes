package com.huertasApp.stock_quotes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;





public class MainActivity extends Activity implements OnClickListener {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";
	int seconds =0;  
	EditText stockSymbol,CompayName,price; 
	LinearLayout ll;
	Button search;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        stockSymbol = (EditText)findViewById(R.id.Et_Input_Quote);
        CompayName = (EditText)findViewById(R.id.CompanyNameId);
        price = (EditText)findViewById(R.id.priceId);
       
        search = (Button)findViewById(R.id.search);
        
        
        search.setOnClickListener(this);
        
        
    } // end onCreate fnction

    
    
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){

	      case R.id.search: 
	    	 	
	    	  	String strSymbol = stockSymbol.getText().toString();
	    	  	String URL = "http://finance.yahoo.com/webservice/v1/symbols/" + strSymbol +"/quote?format=json";
	    	 
	    	    
	    	  	if(strSymbol == ""){
	    	  		alertDialog("Company Quote are required");
	    	  	}else{
	    	  		/* If str Symbol are not empty make request with URl */
	    	  		UpdateStock(URL); 
	    	  	}
	    	  	

	        break;
	        
	     default:
	      } // End switch blok
	} // End onClick method


	
	
	
	public void alertDialog(String Message){
		
		
		 new AlertDialog.Builder(this)
		    .setTitle("TaxiCab Message")
		    .setMessage(Message)
		    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	
		         // Include function if you need it on this event
		        
		        }
		     })
		     /*
			    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            // do nothing
			        }
			     })
		     */
		    .setIcon(android.R.drawable.ic_dialog_alert)
		     .show();
		
	}

	
	
	
	
	
	
	
	
	 
	 ////////  HERE START MY FUNCTION TO INTERECT WITH THE SERVER ////////
	 ////////  THE INFORMATION WILL BE SENDING BY JSON FORMAT ///////////

	
	 protected void UpdateStock(final String URL){
	       	
		
		Log.e("Result URL",URL);
		 	Thread t = new Thread() {
		 		
	            public void run() {
	            
	            	while(true){
	            		
	            		try{
			            	Thread.sleep(10000);
			            	
			            	
			                HttpClient client = new DefaultHttpClient();
				            String json = "";
		                
	           
			                try {
			                	
			                	String line = "";
			  	                HttpGet request = new HttpGet(URL);
			  	                HttpResponse response = client.execute(request);
			  	                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			  	                while ((line = rd.readLine()) != null) {
			  	                    json += line + System.getProperty("line.separator");
			  	                }
			  	                
			  	                
			  	           

			  	              updateView(json.toString());
			  	           
		
			                } catch(Exception e) {
			                    e.printStackTrace();
			                 // createDialog("Error", "Cannot Estabilish Connection");
			                }
			              
			                
			                
			                
	            		} catch(Exception e) {
		                    e.printStackTrace();
		                 // createDialog("Error", "Cannot Estabilish Connection");
		                }
	            		
	            	} // End while loop
	               
	            }
	        };

	        t.start();
			

	    }
	 
	 
	 
	 
	 
	 
	 public static String slurp(final InputStream is) throws IOException
	    {
	    	 BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    	    StringBuilder out = new StringBuilder();
	    	    String newLine = System.getProperty("line.separator");
	    	    String line;
	    	    while ((line = reader.readLine()) != null) {
	    	        out.append(line);
	    	        out.append(newLine);
	    	    }
	    	    return out.toString();
	    }
	 
	 

	 
	 
	 
	 private void updateView(final String result) {
		    runOnUiThread(new Runnable(){
		        public void run() {
		           

					 JSONObject reader = null;
						try {
						   
							reader = new JSONObject(result);
							
			               JSONObject list  = reader.getJSONObject("list");
			               JSONArray resources = list.getJSONArray("resources");
			               
			               JSONObject resource  = resources.getJSONObject(0);
			               JSONObject fields  = resource.getJSONObject("resource").getJSONObject("fields");
			               
			          
			              String ComName = fields.getString("name");
			              String priceCompany = fields.getString("price");
			               
			              
			              /// Create error for debugin purpose
			              seconds += 10;
			              
			              Log.e("Time",Integer.toString(seconds));
			              Log.e("Company Name",ComName);
			              Log.e("Price Company",priceCompany);
			              
			              
			               CompayName.setText("Company name: " + ComName);
			               price.setText("Company price: " + priceCompany);
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 
		        	
		        	
		        	
		        }
		    });
		}


	
		 
	
}// End main class
