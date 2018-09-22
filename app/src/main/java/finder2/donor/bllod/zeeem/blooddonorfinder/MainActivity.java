package finder2.donor.bllod.zeeem.blooddonorfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {


    //declaring firebase refs
    public static FirebaseDatabase database;
    public static DatabaseReference mRef;

    public static String selectedBloodGroup, selectedArea;

    private static final int TIME_INTERVAL = 2000; // 2000 milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //controlling the login button
        TextView signinText = findViewById(R.id.signinText);
        if(StaticValues.loggedIn){
            signinText.setText("Edit Profile");
        } else signinText.setText("LOGIN HERE");

        Button btn = findViewById(R.id.searchBtn);

        //getting the spinner from the xml.
        final Spinner dropdownBloodGroup = findViewById(R.id.blood_grp_spinner);
        final Spinner dropdownArea = findViewById(R.id.area_spinner);

        //creating a list of items for the spinner.
        String[] items_bloodGroup = new String[]{"A+", "B+", "O+", "AB+", "A-", "B-", "O-", "AB-"};
        String[] items_area = new String[]{"","Banani", "Dhanmondi", "Gulshan", "Mirpur", "Motijheel", "Mohammadpur", "Shahbag", "Shamoli", "Savar", "Uttara"};

        //creating an adapter to describe how the items are displayed
        ArrayAdapter<String> adapter_bloodGroup = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items_bloodGroup);
        ArrayAdapter<String> adapter_area = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items_area);

        //setting the spinners adapter to the created one.
        dropdownBloodGroup.setAdapter(adapter_bloodGroup);
        dropdownArea.setAdapter(adapter_area);

        //initiating firebase database reference
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();



        //search button activity
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //storing the blood group to search in db
                selectedBloodGroup = dropdownBloodGroup.getSelectedItem().toString();
                selectedArea = dropdownArea.getSelectedItem().toString();

                Intent intent = new Intent(MainActivity.this, DonorList.class);
                startActivity(intent);
                //finish();
            }
        });



    }


    public void loginClick(View view){

        if(!StaticValues.loggedIn){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        }

    }

    //exit on double back press
    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }

}
