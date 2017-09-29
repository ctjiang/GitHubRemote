package idv.ctjiang.intenttest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    static final int PICK_CONTACT_REQUEST = 8888;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Intent_Name, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String[] Intent_IDs = getResources().getStringArray(R.array.Intent_Name);
        String selID = (String) adapterView.getSelectedItem();
        int selPos = adapterView.getSelectedItemPosition();
        Intent startedIntent = null;

        if(selID.compareToIgnoreCase(Intent_IDs[1]) == 0) {
            Uri loc = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
            startedIntent = new Intent(Intent.ACTION_VIEW, loc);
        }
        else if(selID.equalsIgnoreCase(Intent_IDs[2]))
        {
            Uri webPage = Uri.parse("http://www.android.com");
            startedIntent = new Intent(Intent.ACTION_VIEW, webPage);
        }
        else if(selID.equalsIgnoreCase(Intent_IDs[3]))
        {
            Uri contracts = Uri.parse("content://contacts");
            startedIntent = new Intent(Intent.ACTION_PICK, contracts);
        }

        if(startedIntent != null) {
            PackageManager pm = getPackageManager();
            List activities = pm.queryIntentActivities(startedIntent, PackageManager.MATCH_DEFAULT_ONLY);
            if (activities.size() > 0)
            {
                if(!selID.equalsIgnoreCase(Intent_IDs[3]))
                    startActivity(startedIntent);
                else
                    startActivityForResult(startedIntent, PICK_CONTACT_REQUEST);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_CONTACT_REQUEST)
        {
            if(resultCode == RESULT_OK)
            {
                Uri contactUri = data.getData();

                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);

                EditText ed = (EditText) findViewById(R.id.editText);
                ed.setText(number);
            }
        }
    }
}
