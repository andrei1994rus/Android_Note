package example.com.android_note;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.os.Bundle;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Class of activity where are shown notes.
 */
public class ListNotesActivity extends AppCompatActivity
{
    /**
     * Code of note's adding.
     */
    public static final int CREATE_ACTIVITY=0;

    /**
     * Object of class DBConnector. Is used for work with database.
     */
	DBConnector DBConnector;

    /**
     * Code of request to read.
     */
    static final int READ_REQUEST=10;

    /**
     * Object of class myListAdapter. Is used for fill of listNotes.
     */
	myListAdapter listAdapter;

    /**
     * Object of ListView. Holds notes.
     */
	ListView listNotes;

    /**
     * Object of class Bitmap. Is used for work with image.
     */
	Bitmap bitmap;

    /**
     * Object of class Toolbar. Is used for ListNodesActivity.
     */
    Toolbar toolbarListNotesActivity;

    /**
     * Code of note's updating.
     */
    public static final int UPDATE_ACTIVITY=1;

    /**
     * Object of class Note. Holds data of every note.
     */
    Note note;

    /**
     * Creates Activity.
     *
     * @param savedInstanceState
     *                          object of class Bundle. Is saved instance state of activity.
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_notes);

        int api=Build.VERSION.SDK_INT;


        if (api>=23)
        {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
            {
                init();
            }

            else
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ListNotesActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    ActivityCompat.requestPermissions(ListNotesActivity.this,
                            new String[]
                            {
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                            },
                            READ_REQUEST);
                }
                else
                {
                    ActivityCompat.requestPermissions(ListNotesActivity.this,
                            new String[]
                            {
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                            },
                            READ_REQUEST);
                }
            }

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
            {
                init();
            }
        }

        else if (api<23)
        {
            init();
        }
    }

    /**
     * This method is used for result of requesting permissions.
     *
     * @param requestCode
     *                   code of request.
     *
     * @param permissions
     *                   permissions.
     *
     * @param grantResults
     *                    results of grant.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults)
    {
        switch (requestCode)
        {
            case READ_REQUEST:
            {
                if (grantResults.length>0 &&
                        grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
                    {
                        Toast.makeText(this,"Permission Granted",Toast.LENGTH_LONG).show();
                        init();
                    }
                }

                else
                {
                    Toast.makeText(this,"Permission isn't granted",Toast.LENGTH_LONG).show();
                }

                return;
            }
        }
    }

    /**
     * Initialisation after check of version and permissions.
     */
    public void init()
    {
        DBConnector=new DBConnector(this);

        toolbarListNotesActivity=(Toolbar) findViewById(R.id.toolbarListNotesActivity);
        setSupportActionBar(toolbarListNotesActivity);

        listNotes=(ListView) findViewById(R.id.list);

        listAdapter=new myListAdapter(getApplicationContext(), DBConnector.selectAll());

        listNotes.setAdapter(listAdapter);

        registerForContextMenu(listNotes);
    }

    /**
     * This method is used for result of intent.
     *
     * @param requestCode
     *                   code of request.
     *
     * @param resultCode
     *                  code of result.
     *
     * @param data
     *            object of class Intent. Transfers data.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
        if (resultCode==Activity.RESULT_OK)
        {
        	note=(Note) data.getExtras().getSerializable("Note");

        	if (requestCode==UPDATE_ACTIVITY)
        		DBConnector.update(note);

        	else
        		DBConnector.insert(note);

        	updateList();
        }        
    }

    /**
     * Happens reaction of select of item in ContextMenu.
     *
     * @param item
     *            item of ContextMenu.
     *
     * @return true.
     */
    @Override
	public boolean onContextItemSelected(MenuItem item) 
    {
    	AdapterContextMenuInfo info=(AdapterContextMenuInfo) item.getMenuInfo();

    	switch(item.getItemId())
    	{
			case R.id.edit:
				Intent update=new Intent(getApplicationContext(), EditorActivity.class);

				note=DBConnector.select(info.id);

				update.putExtra("Note", note);

				startActivityForResult(update, UPDATE_ACTIVITY);

				updateList();

				return true;

			case R.id.delete:
				DBConnector.delete(info.id);

				updateList();

				return true;

			default:
				return super.onContextItemSelected(item);
    	}
    }

    /**
     * This method is used for memory clearing.
     */
    @Override
    public void onPause() 
    {
	    super.onPause(); 

	    if (bitmap!=null)
	    {
	        bitmap.recycle();
	        bitmap=null;
	    }
    }

    /**
     * Creates ContextMenu.
     *
     * @param menu
     *            object of class ContextMenu.
     *
     * @param v
     *         object of class View which responds for event handling and is used for widgets.
     *
     * @param menuInfo
     *                object of class ContextMenuInfo.
     *
     */
    @Override  
    public void onCreateContextMenu(ContextMenu menu,View v,ContextMenuInfo menuInfo)
    {  
    	super.onCreateContextMenu(menu,v,menuInfo);

    	MenuInflater inflater=getMenuInflater();
    	inflater.inflate(R.menu.context_menu, menu);
    }

    /**
     * Creates Menu.
     *
     * @param menu
     *            object of class Menu.
     *
     * @return true.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main,menu);

        return true; 
    }

    /**
     * Happens reaction of select of item in Menu.
     *
     * @param item
     *            object of class MenuItem.
     *
     * @return true.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        switch (item.getItemId()) 
        {
	        case R.id.add:
	        	Intent add=new Intent(getApplicationContext(),EditorActivity.class);

	        	startActivityForResult(add,CREATE_ACTIVITY);

	        	updateList();

	            return true;

            case R.id.deleteAll:
            	DBConnector.deleteAll();

            	updateList();

                return true;

            case R.id.exit:
                finish();

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Updates list of notes.
     */
    private void updateList()
    {
    	listAdapter.setArrayNote(DBConnector.selectAll());
    	listAdapter.notifyDataSetChanged();
    }
}