package com.example.caddy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private EditText edit;
    private ListView list;
    private Button button;
    private Button btnAdd;
    private NotesDbAdapter db;
    private TextView text;
    private long currentID;
    private boolean editOuvert=false;//sert à savoir si l'ajout d'élément dans la liste est possible ou non

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.btn);
        list = findViewById(R.id.list);
        btnAdd=findViewById(R.id.input);
        db = new NotesDbAdapter(this);
        db.open();
        edit = findViewById(R.id.editText);
        fillData();
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){//affichage ou fermeture du champ d'input d'objet et de son bouton à l'appui du bouton
                if (!editOuvert) {
                    btnAdd.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.VISIBLE);
                    button.setText("Annuler");
                    editOuvert=true;
                }
                else{
                    btnAdd.setVisibility(View.GONE);
                    edit.setVisibility(View.GONE);
                    button.setText("Ajouter");
                    editOuvert=false;
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(edit.getText().toString() != ""){
                    HandleTextContent();
                    clearEditText();
                }
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) { // si click longtemps sur un élément on affiche un menu qui permet d'enlever l'élement
                showPopup(view, id); // passage de l'id en param pour supprimer dans la bdd
                return false;
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {  // si click sur un élément on le raye
                text = (TextView) view;
                text.setPaintFlags(text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);//ajoute la rayure
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Voulez-vous tout effacer ?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clearList();
                dialogInterface.cancel();
            }
        });
        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return false;
    }

    void showPopup(View v, long id){
        currentID = id;
        PopupMenu popupMenu = new PopupMenu(this,v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }
    void clearEditText(){
        edit.setText("");
    }

    //vidage complet de la liste
    void clearList(){
        db.clearAll();
        Toast.makeText(getApplicationContext(), "liste effacée", Toast.LENGTH_SHORT).show();
        fillData();
    }
    // ajout d'un élément à la base de donnée
    public void HandleTextContent(){
        String editTextContent = edit.getText().toString();
        db.createNote(editTextContent, "");
        fillData();
    }

    // remplissage de la liste avec les infos de la bdd
    private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor c = db.fetchAllNotes();
        startManagingCursor(c);

        String[] from = new String[] { NotesDbAdapter.KEY_TITLE};
        int[] to = new int[] { android.R.id.text1 };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, c, from, to,0);
        list.setAdapter(notes);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) { //effacement d'un item de la liste
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Voulez-vous vraiment effacer ?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.deleteNote(currentID);
                fillData();//appel pour refresh l'affichage après suppression
            }
        });
        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        return true;
    }
}