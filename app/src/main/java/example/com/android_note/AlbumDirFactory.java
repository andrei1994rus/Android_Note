package example.com.android_note;

import java.io.File;

/**
 * This class is used for creating of album. Is derivative class.
 */
public class AlbumDirFactory extends AlbumStorageDirFactory 
{
	/**
	 * Path of album.
	 */
	String path="mnt/sdcard";

	/**
	 * Creates album.
	 *
	 * @param albumName
	 * 				   name of album.
	 *
	 * @return created album in the path with defined name of album.
	 *
	 */
	@Override
	public File getAlbumStorageDir(String albumName) 
	{
		return new File(path,albumName);
	}

}
