package example.com.android_note.directory;

import java.io.File;

/**
 * This class is used for creating album. Is base class.
 */
public abstract class AlbumStorageDirFactory
{
    /**
     * Abstract method. Is used to create album
     *
     * @param albumName name of album
     * @return created album
     */
	public abstract File getAlbumStorageDir(String albumName);
}