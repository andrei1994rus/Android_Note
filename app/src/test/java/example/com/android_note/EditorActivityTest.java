package example.com.android_note;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import example.com.android_note.activity.EditorActivity;

import static org.junit.Assert.*;

public class EditorActivityTest
{
    private EditorActivity editorActivity;

    @Before
    public void setUp() throws Exception
    {
        editorActivity=new EditorActivity();
    }

    @Test
    public void getAlbumName()
    {
        String actual=editorActivity.getAlbumName();
        String expected="Android_Note";
        assertEquals(expected,actual);
    }

    @After
    public void tearDown() throws Exception
    {
        editorActivity=null;
    }
}