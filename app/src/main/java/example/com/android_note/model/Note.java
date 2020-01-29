package example.com.android_note.model;

import java.io.Serializable;

/**
 * This class is used for work with information of every note.
 */
public class Note implements Serializable
{
    /**
     * Date of creating/editing of note.
     */
	private String date;

    /**
     * Identifier of note.
     */
	private long id;

    /**
     * Path of image in note.
     */
	private String imagePath;

    /**
     * Text of note.
     */
	private String text;

    /**
     * Constructor of class Note.
     *
     * @param id
	 * 			id of note.
     *
	 * @param date
	 * 			date of creating/updating note.
     *
	 * @param text
	 * 			text of note.
     *
	 * @param imagePath
	 * 			path of image in note.
	 *
     */
	public Note(long id, String date, String text, String imagePath)
	{
		this.id=id;

		this.date=date;

		this.text=text;

		this.imagePath=imagePath;
	}

    /**
     * Gets date.
     *
     * @return date of note's creating/updating.
     */
	public String getDate()
	{
		return date;
	}

    /**
     * Gets identifier.
     *
     * @return id of note.
	 *
     */
	public long getID()
	{
		return id;
	}

    /**
     * Gets path of image.
     *
     * @return path of image in note.
	 *
     */
	public String getImagePath()
	{
		return imagePath;
	}

    /**
     * Gets text.
     *
     * @return text of note.
	 *
     */
	public String getText()
	{
		return text;
	}
}
