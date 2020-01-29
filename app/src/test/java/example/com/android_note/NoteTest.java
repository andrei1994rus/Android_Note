package example.com.android_note;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import example.com.android_note.model.Note;

import static org.junit.Assert.*;

public class NoteTest
{
    private Note note;

    @Before
    public void setUp() throws Exception
    {
        note=new Note(1,"16/12/2018 13:08","Test",
                "mnt\\sdcard\\Android_Note\\IMG_20190716_184325_-1096709655");
    }

    @Test
    public void getDate()
    {
        String actual=note.getDate();
        String expected="16/12/2018 13:08";
        assertEquals(expected,actual);
    }

    @Test
    public void getID()
    {
        long actual=note.getID();
        long expected=1;
        assertEquals(expected,actual);
    }

    @Test
    public void getImagePath()
    {
        String actual=note.getImagePath();
        String expected="mnt\\sdcard\\Android_Note\\IMG_20190716_184325_-1096709655";
        assertEquals(expected,actual);
    }

    @Test
    public void getText()
    {
        String actual=note.getText();
        String expected="Test";
        assertEquals(expected,actual);
    }

    @After
    public void tearDown() throws Exception
    {
        note=null;
    }
}