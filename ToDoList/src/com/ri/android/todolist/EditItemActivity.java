package com.ri.android.todolist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends Activity {
	
	private EditText editItem;
	private int edited_item_position;
	private ArrayList<String> todoItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		
		//get the value of the item and its position send from the main activity 
		
		
		String item_editing = getIntent().getStringExtra("item");
		int item_position = getIntent().getIntExtra("item_pos", -1);
		
		//startActivityForResult();
	    //DEBUG
		System.out.println("item edit position"+item_position);
		
		//setting value of the item and position
		edited_item_position = item_position;
		editItem = (EditText)findViewById(R.id.editItem);
		editItem.setText(item_editing);
		
		//focusing and setting cursor in the EditText field
		editItem.requestFocus();
		int position = editItem.length();
		editItem.setSelection(position);
		
		readItems();
		//finish();
	}
	
	
	
	public void onSaveEditedItem(View v){
		//startActivityForResult();
		String edited_item = editItem.getText().toString();
		int edit_item_pos = edited_item_position;
		
		//DEBUG
		System.out.println("edited item value in save method = "+edited_item);
		System.out.println("edited item's position in the save method = "+edit_item_pos);
		
		//todoItems.add(edit_item_pos, edited_item);
		
		 Intent data = new Intent();
   	  data.putExtra("item", edited_item);
   	  data.putExtra("item_pos", edit_item_pos);
   	  setResult(RESULT_OK, data);
		
        //writeItems();
        finish();
		
	
	}
	
	
	
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}

}
