package example.com.android_note;

import java.io.File;

/**
 * This class is used for creating album. Is base class.
 */
abstract class AlbumStorageDirFactory 
{
	public abstract File getAlbumStorageDir(String albumName);
}