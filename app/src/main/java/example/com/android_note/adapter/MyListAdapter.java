package example.com.android_note.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import example.com.android_note.model.Note;
import example.com.android_note.R;

/**
 * Class of Adapter which is used for fill of listNotes.
 */
public class MyListAdapter extends BaseAdapter
{
    /**
     * Object of class Note. Holds data of every note.
     */
    private Note note;

    /**
     * Object of class ArrayList. Is array of note.
     */
    private ArrayList<Note> arrayNote;

    /**
     * Object of class LayoutInflater. Is used for converting View.
     */
    private LayoutInflater layoutInflater;

    /**
     * Size of image.
     */
    private int sizeImage;

    /**
     * Constructor.
     *
     * @param ctx
     *           object of class Context. Is used for layout inflating.
     *
     * @param arr
     *           object of class ArrayList. Is used for filling listNotes.
     */
    public MyListAdapter(Context ctx, ArrayList<Note> arr)
    {
        layoutInflater=LayoutInflater.from(ctx);

        sizeImage=ctx.getResources().getDimensionPixelSize(R.dimen.dimen);

        setArrayNote(arr);
    }

    /**
     * Set array of note.
     *
     * @param arrayNote
     *                 object of class ArrayList. Is array of note.
     *
     */
    public void setArrayNote(ArrayList<Note> arrayNote)
    {
        this.arrayNote=arrayNote;
    }

    /**
     * Gets array of note.
     *
     * @return array of note.
     *
     */
    public ArrayList<Note> getArrayNote()
    {
        return arrayNote;
    }

    /**
     * Gets count of notes in array.
     *
     * @return count of notes in array.
     *
     */
    public int getCount()
    {
        return arrayNote.size();
    }

    /**
     * Get item in array.
     *
     * @param position
     *                position of ArrayList.
     *
     * @return position of ArrayList.
     *
     */
    public Object getItem(int position)
    {
        return position;
    }

    /**
     * Gets id.
     *
     * @param position
     *                position of ArrayList.
     *
     * @return id of note.
     *
     */
    public long getItemId(int position)
    {
        note=arrayNote.get(position);

        if(note!=null)
        {
            return note.getID();
        }

        return 0;
    }

    /**
     * Gets view.
     *
     * @param position
     *                position of ArrayList.
     *
     * @param convertView
     *                   object of class View. Is converted view.
     *
     * @param parent
     *              object of class ViewGroup.
     *
     * @return converted view.
     *
     */
    public View getView(int position, View convertView, ViewGroup parent)
    {

        convertView=layoutInflater.inflate(R.layout.item,null);

        /*
          Object of class ImageView. Holds image of note.
         */
        ImageView ivImage=convertView.findViewById(R.id.Image);

        /*
          Object of TextView. Holds text of note.
         */
        TextView tvText=convertView.findViewById(R.id.Text);

        /*
           Object of class TextView. Holds date of note's creating/updating.
         */
        TextView tvDate=convertView.findViewById(R.id.Date);

        note=arrayNote.get(position);

        tvDate.setText(note.getDate());

        tvText.setText(note.getText());

        Glide.with(convertView).
                load(note.getImagePath()).
                apply(new RequestOptions().
                        override(sizeImage)).
                into(ivImage);

        return convertView;
    }
}