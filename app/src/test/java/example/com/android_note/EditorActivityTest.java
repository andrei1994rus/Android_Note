package example.com.android_note;

import org.junit.Test;

import static org.junit.Assert.*;

public class EditorActivityTest
{
    EditorActivity editorActivity=new EditorActivity();

    @Test
    public void getAlbumName()
    {
        String actual=editorActivity.getAlbumName();
        String expected="Android_Note";
        assertEquals(expected,actual);
    }
}