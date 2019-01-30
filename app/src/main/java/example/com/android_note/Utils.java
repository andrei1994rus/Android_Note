package example.com.android_note;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * This class is used for change size of image.
 */
public class Utils 
{
	/**
	 * Decodes image from path of image.
	 *
	 * @param PathImage
	 * 			   	   path of image.
	 *
	 * @param reqWidth
	 * 				  required width of image.
	 *
	 * @param reqHeight
	 * 				   required height of image.
	 *
	 * @return decoded image from path of image.
	 *
	 */
	public static Bitmap decodeSampledBitmapFromResource(String PathImage, int reqWidth, int reqHeight)
	{
		BitmapFactory.Options options=new BitmapFactory.Options();

		options.inJustDecodeBounds=true;

		BitmapFactory.decodeFile(PathImage, options);
	
		options.inSampleSize=calculateInSampleSize(options, reqWidth, reqHeight);
		
		options.inJustDecodeBounds=false;
		
		options.inPreferredConfig=Bitmap.Config.RGB_565;

		return BitmapFactory.decodeFile(PathImage, options);
	}

	/**
	 * Calculates size of image.
	 *
	 * @param options
	 *				 object of class Options which used for change image.
	 *
	 * @param reqWidth
	 *				  required width of image.
	 *
	 * @param reqHeight
	 * 			       required height of image.
	 *
	 * @return changed size of image.
	 *
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
	{
		int height=options.outHeight;

		int width=options.outWidth;

		int inSampleSize=1;
		
		if (height>reqHeight ||
                width>reqWidth)
		{
			int halfHeight=height/2;
			int halfWidth=width/2;

			while ((halfHeight/inSampleSize)>reqHeight &&
					(halfWidth/inSampleSize)>reqWidth)
			{
				inSampleSize*=2;
			}
		}

		return inSampleSize;
	}

}