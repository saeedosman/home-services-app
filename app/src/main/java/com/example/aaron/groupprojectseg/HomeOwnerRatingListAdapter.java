package com.example.aaron.groupprojectseg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeOwnerRatingListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Rating> ratings;
    private Context context;
    String username;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    public HomeOwnerRatingListAdapter(ArrayList<Rating> ratings, Context context) {
        this.ratings = ratings;
        this.context = context;
    }

    @Override
    public int getCount() {
        return ratings.size();
    }

    @Override
    public Object getItem(int pos) {
        return ratings.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.home_owner_rating_item, null);
        }

        TextView itemNameText = (TextView) view.findViewById(R.id.item_name);
        itemNameText.setText(ratings.get(position).getServiceProvider().getName());

        String rating = Float.toString(ratings.get(position).getRating());

        TextView itemRatingText = (TextView) view.findViewById(R.id.item_rating);
        itemRatingText.setText(Html.fromHtml("<b>Rating:</b> " + rating));

        String comment = ratings.get(position).getComment();

        TextView itemCommentText = (TextView) view.findViewById(R.id.item_comment);
        itemCommentText.setText(Html.fromHtml("<b>Comment:</b> " + comment));

        return view;
    }
}