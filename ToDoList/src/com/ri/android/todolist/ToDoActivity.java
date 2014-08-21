package com.ri.android.todolist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


public class ToDoActivity extends Activity {
	
	private ArrayList<String> todoItems;
	private ArrayAdapter<String> todoAdapter;
    private ListView lvitems;
    private EditText etNewItem;
 // REQUEST_CODE can be any value we like, used to determine the result type later
    private final int REQUEST_CODE = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        etNewItem = (EditText) findViewById(R.id.etnewitem);
        lvitems = (ListView) findViewById(R.id.lvitems);
        //populateArrayItems();
        readItems();
        todoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
        lvitems.setAdapter(todoAdapter);
        //todoAdapter.add("homework");
        
        
        setupListViewListener();
        
        
        
        //Notes from the lecture video 
        //Create an arrayList to hold the item on the todo list
        //Create an array adapter with will get the item from the array and display in the view
        //adapter helps to populate any kind of list, so there are disfferent kind of adapter for 
        //different kind of data.It is a translator
        //create an ListView Object ,responsible for the creating the view for the array list item
        //attach the adapter to the ListView object

    }
    
    
    private void setupListViewListener() {
		lvitems.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item,int pos, long id) {
				
				//from the vidoe on the error
				//08-19 22:43:50.355: E/AndroidRuntime(2583): java.lang.IllegalStateException: 
				//The content of the adapter has changed but ListView did not receive a notification. 
				//Make sure the content of your adapter is not modified from a background thread, 
				//but only from the UI thread. [in ListView(2131230722, class android.widget.ListView) 
				//with Adapter(class android.widget.ArrayAdapter)]

				
				todoItems.remove(pos);
				
				//notifying the adapter that the data set have changed , so it does not throw the exception
				//worked like magic
				todoAdapter.notifyDataSetChanged();
				writeItems();
				return true;
			}
			
			
		});
		
		
		
		
		
		lvitems.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapter, View item, int pos,long id) {
				
				//send to the edit item activity with the pos and the value of the item
					
				launchEditItemView(pos);
			}
			
		});
		
	}
    
    
    
    public void launchEditItemView(int pos) {
    	  // first parameter is the context, second is the class of the activity to launch
    	  Intent i = new Intent(ToDoActivity.this, EditItemActivity.class);
    	  i.putExtra("item", todoAdapter.getItem(pos));
    	  i.putExtra("item_pos", pos);
    	  startActivityForResult(i,REQUEST_CODE); // brings up the second activity
    	}


	public void onAddedItem(View v){
    	String itemText = etNewItem.getText().toString();
    	todoAdapter.add(itemText);
    	etNewItem.setText("");
    	writeItems();
    }
    
	
	/* will be replaced by the readItems method to read from the file
    private void populateArrayItems(){
    	todoItems = new ArrayList<String>();
        todoItems.add("milk");
        todoItems.add("dentist");
        todoItems.add("music");
    }
    */
    
    
    //read list item from local file for persistence
    private void readItems(){
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	try{
    		todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
    		
    	}catch(IOException e){
    		todoItems = new ArrayList<String>();
    		
    	}
    	
    }
    
    //write list item to local file
    
    private void writeItems(){
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	try{
    		FileUtils.writeLines(todoFile, todoItems);
    	}catch(IOException e){
    		//todoItems = new ArrayList<String>();
    		//exception usually no write permission
    		e.printStackTrace();
    	} 	
    }
    
    
   protected void onActivityResult(int requestCode, int resultCode, Intent data){
	   if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
		   String edited_item = data.getExtras().getString("item");
		   int item_pos = data.getExtras().getInt("item_pos");
		   
		   
		   //DEBUG
		   System.out.println("data return from the edit acivity :"+edited_item+" and "+item_pos);
		   
		   todoItems.set(item_pos, edited_item);
		   todoAdapter.notifyDataSetChanged();
		   writeItems();
		   
		   
		   
	   }
	   
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.to_do, menu);
        return true;
    }

}
