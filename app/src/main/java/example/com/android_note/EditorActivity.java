package example.com.android_note;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Class of activity where may create/update note.
 */
public class EditorActivity extends AppCompatActivity
{
	/**
	 * Code of request to gallery.
	 */
	static final int GALLERY_REQUEST=1;

	/**
	 * Code of request to action take photo.
	 */
	static final int ACTION_TAKE_PHOTO=2;

	/**
	 * Code of request to camera.
	 */
    public static final int CAMERA_REQUEST=10;

	/**
	 * Suffix of jpg file.
	 */
	String JPEG_FILE_SUFFIX=".jpg";

	/**
	 * Object of abstract class AlbumStorageDirFactory.
	 */
	AlbumStorageDirFactory mAlbumStorageDirFactory;

	/**
	 * Object of class toolbar. Is used for EditorActivity.
	 */
	Toolbar toolbarEditorActivity;

	/**
	 * Object of class Bitmap. Is used for work with image.
	 */
	Bitmap bitmap;

	/**
	 * Object of class Dialog. Is used when it's necessary to select way do image for note.
	 */
	Dialog dialog;

	/**
	 * Object of class Button. Is used to add image.
	 */
	Button buttonAddImage;

	/**
	 * Object of class Button. Is used to save note.
	 */
	Button buttonSave;

	/**
	 * Object of class Button. Is used to exit from Editor Activity without saving.
	 */
	Button buttonCancel;

	/**
	 * Object of class Button. Is used to delete image.
	 */
	Button buttonDeleteImage;

	/**
	 * Object of class Button. Is used to add image via gallery.
	 */
	Button buttonGallery;

	/**
	 * Object of class Button. Is used to adding image via camera.
	 */
	Button buttonCamera;

	/**
	 * Object of class EditText. Holds text of note.
	 */
	EditText etNoteText;

	/**
	 * Object of class ImageView. Holds image of note.
	 */
	ImageView ivImage;

	/**
	 * Object of note. Holds data of every note.
	 */
	Note note;

	/**
	 * Id of note.
	 */
	long NoteID;

	/**
	 * Text of note.
	 */
	String text;

	/**
	 * Date of note's adding/updating.
     */
	String dateString;

	/**
	 * Path of image.
	 */
	String ImPath;

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

		setContentView(R.layout.activity_editor);

		toolbarEditorActivity=(Toolbar) findViewById(R.id.toolbarEditorActivity);
		setSupportActionBar(toolbarEditorActivity);

		etNoteText=(EditText) findViewById(R.id.NoteText);

		ivImage=(ImageView) findViewById(R.id.imageView1);

        buttonAddImage=(Button) findViewById(R.id.AddImage);

        buttonSave=(Button) findViewById(R.id.Save);

        buttonCancel=(Button) findViewById(R.id.Cancel);

        buttonDeleteImage=(Button) findViewById(R.id.DeleteImage);
        
        if (getIntent().hasExtra("Note")) 
        {
        	note=(Note) getIntent().getSerializableExtra("Note");
        	
        	dateString=note.getDate();

        	text=note.getText();
			etNoteText.setText(text);

        	ImPath=note.getImagePath();
        	ivImage.setImageDrawable(Drawable.createFromPath(ImPath));

        	NoteID=note.getID();
        }
        
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
    	
    	buttonGallery=(Button) dialog.findViewById(R.id.buttonGallery);

    	buttonGallery.setOnClickListener(new OnClickListener()
    	{
    		public void onClick(View v)
    		{
    			Intent photoPickerIntent=new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    			photoPickerIntent.setType("image/*");

    			startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    			dialog.dismiss();
    		}
    	});
    	
    	mAlbumStorageDirFactory=new AlbumDirFactory();

    	buttonCamera=(Button) dialog.findViewById(R.id.buttonCamera);

    	buttonCamera.setOnClickListener(new OnClickListener()
    	{
    		public void onClick(View v)
    		{
				int api=Build.VERSION.SDK_INT;

				if (api>=23)
				{
					if (ContextCompat.checkSelfPermission(getApplicationContext(),
							Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
					{
						dispatchTakePictureIntent(ACTION_TAKE_PHOTO);
						dialog.dismiss();
					}

					else
					{
						if (ActivityCompat.shouldShowRequestPermissionRationale(EditorActivity.this,
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
				else if (api<23)
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
        switch (requestCode)
        {
            case CAMERA_REQUEST:
            {
                if (grantResults.length>0 &&
                        grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED)
                    {
                        Toast.makeText(this,"Permission Granted",Toast.LENGTH_LONG).show();

						dispatchTakePictureIntent(ACTION_TAKE_PHOTO);
						dialog.dismiss();
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
	 * Exit from activity without saving.
	 *
	 * @param v
     *         object of class View which responds for event handling and is used for widgets.
	 *
	 */
	public void CancelClick(View v)
	{
		setResult(RESULT_CANCELED, new Intent());
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
		if (etNoteText.getText().toString().equals(""))
		{
			Toast.makeText(getApplicationContext(), "Text Field is empty.", Toast.LENGTH_LONG ).show();
		}

		else
		{
			long date=System.currentTimeMillis();
			SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy kk:mm");

			dateString=sdf.format(date);

			text=etNoteText.getText().toString();

		    note=new Note (NoteID, dateString, text,ImPath);

		    Intent intent=getIntent();
		    intent.putExtra("Note", note);
		    setResult(RESULT_OK, intent);

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
        outState.putString("ImPath", ImPath);

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

		    		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		    		startActivityForResult(takePictureIntent, actionCode);
		    	}

		    	catch (IOException e)
		    	{
		    		e.printStackTrace();

		    		f=null;

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
	private File setUpPhotoFile() throws IOException 
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
	private File createImageFile() throws IOException
    {
    	String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

		String imageFileName="IMG_"+timeStamp+"_";

		File albumF=getAlbumDir();
		File imageF=File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);

		return imageF;
	}

	/**
	 * Gets name of album.
	 *
	 * @return name of album.
	 *
	 */
    private File getAlbumDir() 
    {
		File storageDir=null;
			
		storageDir=mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

		if (storageDir!=null)
		{
			if (!storageDir.mkdirs()) 
			{
				if (!storageDir.exists())
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
	private String getAlbumName()
    {
		String AlbumName="Android_Note";

		return AlbumName;
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
	private String getRealPathFromURI(Uri uri) 
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
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) 
    {
    	super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    	
    	switch(requestCode) 
    	{
    	    case GALLERY_REQUEST:
    	    	if(resultCode==RESULT_OK)
    	    	{
    	    		Uri selectedImage=imageReturnedIntent.getData();
    	    		ImPath=getRealPathFromURI(selectedImage);

    	    		try
    	    		{
    	    			bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
    	    		}

    	    		catch (IOException e)
    	    		{
    	    	    	e.printStackTrace();
    	    	    }

    	    	    ivImage.setImageBitmap(bitmap);
                }

                break;

    	    case ACTION_TAKE_PHOTO:
    	    	if (resultCode==RESULT_OK)
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
	private void galleryAddPic()
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
	private void setPic()
    {
		int ImageViewW=ivImage.getWidth();

		int ImageViewH=ivImage.getHeight();
		
		bitmap=Utils.decodeSampledBitmapFromResource(ImPath, ImageViewW, ImageViewH);
		ivImage.setImageBitmap(bitmap);
	}

}
