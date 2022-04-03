package com.example.caddy;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.caddy.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private ListView list;
    private NotesDbAdapter db;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_first, container, false);

        //Var
        list = getActivity().findViewById(R.id.list);
        db = new NotesDbAdapter(getActivity());
        db.open();
        //Valeur test
        db.createNote("Defaut 1", "1");
        db.createNote("Defaut 2", "2");
        db.createNote("Defaut 3", "3");
        //fillData(); ça ne marche pas

        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor c = db.fetchAllNotes();
        getActivity().startManagingCursor(c);
        System.out.println(c.getCount());

        String[] from = new String[] { NotesDbAdapter.KEY_TITLE};
        int[] to = new int[] { android.R.id.text1 };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_list_item_1,
                c,
                from,
                to,
                0);
        if (adapter != null) {
            System.out.println("adapter non null");
            list.setAdapter(adapter);
        }
        else {
            System.out.println("adapter null");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}