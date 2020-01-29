package example.com.android_note.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import example.com.android_note.directory.AlbumDirFactory;
import example.com.android_note.directory.AlbumStorageDirFactory;
import example.com.android_note.model.Note;
import example.com.android_note.R;

/**
 * Class of activity where may create/update note.
 */
public class EditorActivity extends AppCompatActivity
{
	/**
	 * Code of request to gallery.
	 */
	private static final int GALLERY_REQUEST=1;

	/**
	 * Code of request to action take photo.
	 */
	private static final int ACTION_TAKE_PHOTO=2;

	/**
	 * Code of request to camera.
	 */
    private static final int CAMERA_REQUEST=10;

	/**
	 * Object of abstract class AlbumStorageDirFactory.
	 */
	private AlbumStorageDirFactory mAlbumStorageDirFactory;

    /**
	 * Object of class Bitmap. Is used for work with image.
	 */
	private Bitmap bitmap;

	/**
	 * Object of class Dialog. Is used when it's necessary to select way do image for note.
	 */
	private Dialog dialog;

    /**
	 * Object of class EditText. Holds text of note.
	 */
	EditText etNoteText;

	/**
	 * Object of class ImageView. Holds image of note.
	 */
	private ImageView ivImage;

	/**
	 * Object of note. Holds data of every note.
	 */
	private Note note;

	/**
	 * Id of note.
	 */
	private long NoteID;

	/**
	 * Text of note.
	 */
	private String text;

	/**
	 * Date of note's adding/updating.
     */
	private String dateString;

	/**
	 * Path of image.
	 */
	private String ImPath;

    /**
     * Version of API. Is used for check version.
     */
	private int api;

	/**
	 * Creates activity.
	 *
	 * @param savedInstanceState
     *                          object of class Bundle. Is saved instance state of activity.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT)
		{
			setContentView(R.layout.activity_editor);
		}

		else if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)
		{
			setContentView(R.layout.activity_editor_land);
		}

        /*
          Object of class toolbar. Is used for EditorActivity.
         */
        Toolbar toolbarEditorActivity = findViewById(R.id.toolbarEditorActivity);
		setSupportActionBar(toolbarEditorActivity);

		etNoteText=findViewById(R.id.NoteText);

		ivImage=findViewById(R.id.imageViewNote);
        
        if(getIntent().hasExtra("Note"))
        {
        	note=(Note)getIntent().getSerializableExtra("Note");
        	
        	dateString=note.getDate();

        	text=note.getText();
			etNoteText.setText(text);

        	ImPath=note.getImagePath();
        	ivImage.setImageDrawable(Drawable.createFromPath(ImPath));

        	NoteID=note.getID();
        }

        ivImage.setOnClickListener(new View.OnClickListener()
		{
			@Override
		    public void onClick(View v)
			{
				Drawable imageDrawable=ivImage.getDrawable();

				if(imageDrawable!=null)
				{
					Intent intent=new Intent(getApplicationContext(),WatchImageActivity.class).
										putExtra("path",ImPath);
					startActivity(intent);
				}

				else
				{
					Toast.makeText(getApplicationContext(),"This note doesn't have image.",
							Toast.LENGTH_LONG).show();
				}
			}
		});
    }

	/**
	 * Deletes image.
	 *
	 * @param v
	 *         object of class View which responds for event handling and is used for widgets.
	 *
	 */
	public void DeleteImageClick(View v)
	{
		ImPath=null;
		ivImage.setImageBitmap(null);
	}

	/**
	 * Adds image.
	 *
	 * @param v
	 *         object of class View which responds for event handling and is used for widgets.
	 *
	 */
    public void AddImageClick(View v)
    {
    	dialog=new Dialog(EditorActivity.this,R.style.CustomDialog);
    	dialog.setTitle("Select way to add photo:");
    	dialog.setContentView(R.layout.dialog_view);
    	dialog.getActionBar();
    	dialog.show();

        /*
          Object of class Button. Is used to add image via gallery.
         */
        Button buttonGallery = dialog.findViewById(R.id.buttonGallery);

    	buttonGallery.setOnClickListener(new OnClickListener()
    	{
    		public void onClick(View v)
    		{
    			Intent photoPickerIntent=new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    			photoPickerIntent.setType("image/*");

    			startActivityForResult(photoPickerIntent,GALLERY_REQUEST);
    			dialog.dismiss();
    		}
    	});
    	
    	mAlbumStorageDirFactory=new AlbumDirFactory();

        /*
          Object of class Button. Is used to adding image via camera.
         */
        Button buttonCamera = dialog.findViewById(R.id.buttonCamera);

    	buttonCamera.setOnClickListener(new OnClickListener()
    	{
    		public void onClick(View v)
    		{
				api=Build.VERSION.SDK_INT;

				if(api>=23)
				{
					if(ContextCompat.checkSelfPermission(getApplicationContext(),
							Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED)
					{
						dispatchTakePictureIntent(ACTION_TAKE_PHOTO);
						dialog.dismiss();
					}

					else
					{
						if(ActivityCompat.shouldShowRequestPermissionRationale(EditorActivity.this,
								Manifest.permission.CAMERA))
						{
							ActivityCompat.requestPermissions(EditorActivity.this,
									new  String[]
									{
											Manifest.permission.CAMERA
									},
									CAMERA_REQUEST);
						}
						else
						{
							ActivityCompat.requestPermissions(EditorActivity.this,
									new String[]
									{
											Manifest.permission.CAMERA
									},
									CAMERA_REQUEST);
						}
					}
				}
				else
				{
					dispatchTakePictureIntent(ACTION_TAKE_PHOTO);
					dialog.dismiss();
				}
    		}
    	});
   }


	/**
	 * Is used for result of requesting permissions.
	 *
	 * @param requestCode
     *                   code of request.
	 *
     * @param permissions
     *                   permissions.
	 *
     * @param grantResults
     *                    results of grant.
	 *
	 */
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults)
    {
        switch(requestCode)
        {
            case CAMERA_REQUEST:
            {
                if(grantResults.length>0 &&
                        grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED)
                    {
                        Toast.makeText(this,"Permission is granted",Toast.LENGTH_LONG).show();

						dispatchTakePictureIntent(ACTION_TAKE_PHOTO);
						dialog.dismiss();
                    }
                }
                else
                {
                    Toast.makeText(this,"Permission isn't granted",Toast.LENGTH_LONG).show();
                }

            }
        }
    }


	/**
	 * Exit from activity without saving.
	 *
	 * @param v
     *         object of class View which responds for event handling and is used for widgets.
	 *
	 */
	public void CancelClick(View v)
	{
		setResult(RESULT_CANCELED,new Intent());
		finish();
	}

	/**
	 * Saves note.
	 *
	 * @param v
     *         object of class View which responds for event handling and is used for widgets.
	 *
	 */
	public void SaveClick(View v)
	{
		if(etNoteText.getText().toString().equals(""))
		{
			Toast.makeText(getApplicationContext(),"Text Field is empty.",Toast.LENGTH_LONG ).show();
		}

		else
		{
			long date=System.currentTimeMillis();
			SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy kk:mm");

			dateString=sdf.format(date);

			text=etNoteText.getText().toString();

		    note=new Note(NoteID,dateString,text,ImPath);

		    Intent intent=getIntent();
		    intent.putExtra("Note",note);
		    setResult(RESULT_OK,intent);

		    finish();
		}
	}

	/**
	 * Restores saved instance state.
	 *
	 * @param savedInstanceState
	 *                          object of class Bundle. Is saved instance state of activity.
	 *
	 */
	@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) 
	{
        super.onRestoreInstanceState(savedInstanceState);

        ImPath=savedInstanceState.getString("ImPath");
        ivImage.setImageDrawable(Drawable.createFromPath(ImPath));
    }

	/**
	 * Saves instance state.
	 *
	 * @param outState
     *                object of class Bundle. Is instance state of activity which it's necessary to save.
	 *
	 */
	@Override
    protected void onSaveInstanceState(Bundle outState) 
    {
        outState.putString("ImPath",ImPath);

        super.onSaveInstanceState(outState);
    }

	/**
	 * Calls camera. Then does image via camera.
	 *
	 * @param actionCode
     *                  code of action.
	 *
	 */
	private void dispatchTakePictureIntent(int actionCode) 
    {
		Intent takePictureIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		switch(actionCode) 
		{
		    case ACTION_TAKE_PHOTO:
		    	File f;

		    	try
		    	{
					f=setUpPhotoFile();
					ImPath=f.getAbsolutePath();

					if(api>=24)
					{
						takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
													FileProvider.getUriForFile(this,
													"com.example.android.provider",
													f));
					}

					else
					{
						takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(f));

					}
		    		startActivityForResult(takePictureIntent,actionCode);
		    	}

		    	catch(IOException e)
		    	{
		    		e.printStackTrace();

		    		ImPath=null;
		    	}

		    	break;

		    default:
				break;
		} 	
	}

	/**
	 * Sets file during action take photo.
	 *
	 * @return file (image).
	 *
	 * @throws IOException
	 * 					  is called during wrong input/output.
	 *
	 */
	File setUpPhotoFile() throws IOException
    {	
		File f=createImageFile();
		ImPath=f.getAbsolutePath();

		return f;
	}

	/**
	 * Creates image.
	 *
	 * @return created file (image).
	 *
	 * @throws IOException
	 * 					  is called during wrong input/output.
	 *
	 */
	File createImageFile() throws IOException
    {
    	String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

		String imageFileName="IMG_"+timeStamp+"_";

		File albumF=getAlbumDir();
		File imageF=File.createTempFile(imageFileName,".jpg",albumF);
		return imageF;
	}

	/**
	 * Gets directory of album.
	 *
	 * @return directory of album.
	 *
	 */
    File getAlbumDir()
    {
		File storageDir=null;
			
		storageDir=mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

		if(storageDir!=null)
		{
			if(!storageDir.mkdirs())
			{
				if(!storageDir.exists())
				{
					return null;
				}
			}
		}
			
		return storageDir;
	}

	/**
	 * Gets name of album.
	 *
	 * @return name of album.
	 *
	 */
	public String getAlbumName()
    {
		return "Android_Note";
	}

	/**
	 * Gets path from URI of image.
	 *
	 * @param uri
	 * 			 object of class Uri. Is URI of image.
     *
	 * @return path of image.
	 *
	 */
	String getRealPathFromURI(Uri uri)
    {
    	String[] projection={MediaStore.Images.Media.DATA};
    	Cursor cursor=managedQuery(uri,
									projection,
									null,
									null,
									null);

    	int column_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    	cursor.moveToFirst();

    	return cursor.getString(column_index);
    }

	/**
	 * Is used for result of intent.
	 *
	 * @param requestCode
	 *					 code of request: gallery and action take photo.
     *
	 * @param resultCode
	 *					code of result.
     *
	 * @param imageReturnedIntent
	 *							 object of class Intent. Is used during selection of image.
	 *
	 */
	@Override
    protected void onActivityResult(int requestCode,int resultCode,Intent imageReturnedIntent)
    {
    	super.onActivityResult(requestCode,resultCode,imageReturnedIntent);
    	
    	switch(requestCode) 
    	{
    	    case GALLERY_REQUEST:
    	    	if(resultCode==RESULT_OK)
    	    	{
    	    		Uri selectedImage=imageReturnedIntent.getData();
    	    		ImPath=getRealPathFromURI(selectedImage);

    	    		try
    	    		{
    	    			bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
    	    		}

    	    		catch(IOException e)
    	    		{
    	    	    	e.printStackTrace();
    	    	    }

    	    	    ivImage.setImageBitmap(bitmap);
                }

                break;

    	    case ACTION_TAKE_PHOTO:
    	    	if(resultCode==RESULT_OK)
    	    	{
    	    		galleryAddPic();
    	    		setPic();
    			}

    			break;
        }
    }

	/**
	 * Adds image to gallery.
	 */
	void galleryAddPic()
	{
		Intent mediaScanIntent=new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");

		File f=new File(ImPath);
		Uri contentUri=Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}

	/**
	 * Sets image.
	 */
	void setPic()
    {
		Glide.with(this).load(ImPath).apply(new RequestOptions().
				override(ivImage.getWidth(),ivImage.getHeight())).into(ivImage);
	}

}
