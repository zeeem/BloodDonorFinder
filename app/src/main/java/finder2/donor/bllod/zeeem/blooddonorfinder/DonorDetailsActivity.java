package finder2.donor.bllod.zeeem.blooddonorfinder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class DonorDetailsActivity extends Activity {

    public String donor_mobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_details);

        int index = getIntent().getExtras().getInt("INDEX");

        TextView nameTV = findViewById(R.id.nameTextV);
        TextView ageTV = findViewById(R.id.age_textV);
        TextView areaTV = findViewById(R.id.area_textV);
        TextView bloodTV = findViewById(R.id.blood_textV);
        TextView historyTV = findViewById(R.id.historyTextV);

        nameTV.setText(DonorList.donorList_sv.get(index).getName());
        ageTV.setText(DonorList.donorList_sv.get(index).getAge());
        areaTV.setText(DonorList.donorList_sv.get(index).getArea());
        bloodTV.setText(DonorList.donorList_sv.get(index).getBlood());
        historyTV.setText(DonorList.donorList_sv.get(index).getHistory());

        donor_mobile = DonorList.donorList_sv.get(index).getMobile();

    }

    public void contactBtn(View view) {

        if(StaticValues.loggedIn) {

            if (isPermissionGranted()) {
                call_action();
            }
//        else {
//            startActivity( new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + donor_mobile)));
//        }
        } else {
            Intent intent = new Intent(DonorDetailsActivity.this,LoginActivity.class);
            LoginActivity.login_and_goBackToResultPage = true;
            startActivity(intent);

        }

    }


    @SuppressLint("MissingPermission")
    public void call_action() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + donor_mobile));
        startActivity(callIntent);
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

}
