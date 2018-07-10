package com.example.androiddevelopment.slobodangolocorbin;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.androiddevelopment.slobodangolocorbin.db.model.ORMLightHelper;
import com.example.androiddevelopment.slobodangolocorbin.db.model.Prijava;
import com.example.androiddevelopment.slobodangolocorbin.db.model.Stavka;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedUpdate;

import java.sql.SQLException;
import java.util.List;

import static com.example.androiddevelopment.slobodangolocorbin.FirstActivity.NOTIF_STATUS;
import static com.example.androiddevelopment.slobodangolocorbin.FirstActivity.NOTIF_TOAST;

public class SecondActivity extends AppCompatActivity {

    private ORMLightHelper databaseHelper;
    private SharedPreferences prefs;
    private Prijava a;

    private EditText naziv;
    private EditText opis;
    private EditText status;
    private EditText datum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int key = getIntent().getExtras().getInt(FirstActivity.PRIJAVA_KEY);

        try {
            a = getDatabaseHelper().getPrijavaDao().queryForId(key);

            naziv = (EditText) findViewById(R.id.prijava_naziv);
            opis = (EditText) findViewById(R.id.prijava_opis);
            status = (EditText) findViewById(R.id.prijava_datum);
            datum = (EditText) findViewById(R.id.prijava_status);

            naziv.setText(a.getmIme());
            opis.setText(a.getmOpis());
            status.setText(a.getmDatum());
            datum.setText(a.getmStatus());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final ListView listView = (ListView) findViewById(R.id.prijava_stavke);

        try {
            List<Stavka> list = getDatabaseHelper().getStavkaDao().queryBuilder()
                    .where()
                    .eq(Stavka.FIELD_NAME_USER, a.getmId())
                    .query();

            ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Stavka m = (Stavka) listView.getItemAtPosition(position);
                    Toast.makeText(SecondActivity.this, m.getmNaslov()+" "+m.getmKomentar()+" "+m.getmImage(), Toast.LENGTH_SHORT).show();

                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.prijava_stavke);

        if (listview != null){
            ArrayAdapter<Stavka> adapter = (ArrayAdapter<Stavka>) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Stavka> list = getDatabaseHelper().getStavkaDao().queryBuilder()
                            .where()
                            .eq(Stavka.FIELD_NAME_USER, a.getmId())
                            .query();

                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showStatusMesage(String message){
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle("TEST");
        mBuilder.setContentText(message);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_add);

        mBuilder.setLargeIcon(bm);
        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void showMessage(String message){
        //provera podesenja
        boolean toast = prefs.getBoolean(NOTIF_TOAST, false);
        boolean status = prefs.getBoolean(NOTIF_STATUS, false);

        if (toast){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        if (status){
            showStatusMesage(message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_stavka:

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.add_stavka);

                Button add = (Button) dialog.findViewById(R.id.add_stavka);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText naslov = (EditText) dialog.findViewById(R.id.stavka_naslov);
                        EditText komentar = (EditText) dialog.findViewById(R.id.stavka_komentar);
                        EditText image = (EditText) dialog.findViewById(R.id.stavka_image);

                        Stavka m = new Stavka();
                        m.setmNaslov(naslov.getText().toString());
                        m.setmKomentar(komentar.getText().toString());
                        m.setmImage(image.getText().toString());
                        m.setmUser(a);

                        try {
                            getDatabaseHelper().getStavkaDao().create(m);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        refresh();

                        showMessage("Nova stavka dodata");

                        dialog.dismiss();
                    }
                });

                dialog.show();

                break;
            case R.id.edit:
                a.setmIme(naziv.getText().toString());
                a.setmDatum(datum.getText().toString());
                a.setmOpis(opis.getText().toString());
                a.setmStatus(status.getText().toString());

                try {
                    getDatabaseHelper().getStavkaDao().update((PreparedUpdate<Stavka>) a);

                    showMessage("Actor detail updated");

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.remove:
                try {
                    getDatabaseHelper().getStavkaDao().delete((PreparedDelete<Stavka>) a);

                    showMessage("Stavka obrisana");

                    finish();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    public ORMLightHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, ORMLightHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

}
