package oguzhankada.quizwithfragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowCategoriesFragment extends ListFragment {

    //interface for method of list item click
    static interface CategoryListListener {
        void CategoryItemClicked(int position);
    }

    //create a listener for clicked items
    private CategoryListListener listener;

    //String Array of the name of the categories
    private String[] categoryNames;

    //take inflater to work on it later on onStart
    private LayoutInflater inflate;

    //Array list of categories
    private ArrayList<Category> categories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //get inflater
        inflate = inflater;

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.listener = (CategoryListListener) activity;
    }

    @Override
    public void onStart(){
        super.onStart();
        categoryNames = new String[categories.size()+1];
        for (int i = 0; i < categories.size(); i++) {
            categoryNames[i] = categories.get(i).getCategoryText();
        }
        categoryNames[categories.size()] = getText(R.string.end_the_game).toString();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflate.getContext(),
                android.R.layout.simple_list_item_1,
                categoryNames);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (listener != null) {
            listener.CategoryItemClicked(position);
        }
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }



}
