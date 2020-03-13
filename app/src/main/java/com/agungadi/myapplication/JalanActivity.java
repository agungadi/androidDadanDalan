package com.agungadi.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agungadi.myapplication.Helper.JalanInterface;
import com.agungadi.myapplication.Helper.PreferenceHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class JalanActivity extends Activity {


    private Button mSubmitBtn, photoButton;

    private static final int IMAGE_REQUEST = 1;
    String currentImagePath = null;


    private ImageView imageView, imageViewGone;

    private EditText tAlamat, tDeskripsi, tKecamatan;
    private Bitmap bitmap;

    TextView mtextLat, mtextLong, tvId;
    private PreferenceHelper preferenceHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jalan);
        changeStatusBarColor();


        preferenceHelper = new PreferenceHelper(this);
        tvId = (TextView) findViewById(R.id.tvid);


        mtextLat = findViewById(R.id.textLat);
        mtextLong = findViewById(R.id.textLong);

        tAlamat = findViewById(R.id.textAlamat);
        tDeskripsi = findViewById(R.id.textDeskripsi);
        tKecamatan = findViewById(R.id.textKecamatan);

        imageView = (ImageView) findViewById(R.id.imageView1);
        imageViewGone = (ImageView) findViewById(R.id.imageViewgone);

        mSubmitBtn = (Button) findViewById(R.id.submitBtn);
        photoButton = (Button) findViewById(R.id.button1);

        tvId.setText(preferenceHelper.getId());


        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener ll = new mylocationlistener();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 5, ll);

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitReport();
            }
        });
    }

    public void captureImage(View view) {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File imageFile = null;
            try {
                imageFile = getImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (imageFile != null) {
                Uri imageUri = FileProvider.getUriForFile(this, "com.agungadi.myapplication.fileprovider", imageFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                //cameraIntent.putExtra("image_path",currentImagePath);
                startActivityForResult(cameraIntent, IMAGE_REQUEST);

            }
        }

    }

    class mylocationlistener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            if (location != null) {
                double pLong = location.getLongitude();
                double pLat = location.getLatitude();

                mtextLat.setText(Double.toString(pLat));
                mtextLong.setText(Double.toString(pLong));

            }
        }

        @Override
        public void onStatusChanged(String s, int status, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data!=null) {
        //  Uri imageUri = data.getData();
        if (requestCode == 1) {
            File imgFile = new File(currentImagePath);
            if(imgFile.exists()){
                bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageView.setImageBitmap(bitmap);
                imageViewGone.setVisibility(View.GONE);


            }


        }
    }

    private File getImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyymmdd_HHmmss").format(new Date());
        String imageName = "jpg_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(imageName, ".jpg", storageDir);
        currentImagePath = imageFile.getAbsolutePath();
        return imageFile;
    }


    private String imageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);

    }


    public void submitReport() {
        String id = tvId.getText().toString();
        String image = imageToString();
        String alamat = tAlamat.getText().toString();
        String deskripsi = tDeskripsi.getText().toString();
        String kecamatan = tKecamatan.getText().toString();

        String latitude = mtextLat.getText().toString();
        String longtitude = mtextLong.getText().toString();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JalanInterface.JALANIURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        JalanInterface api = retrofit.create(JalanInterface.class);


        Call<String> call = api.getJALAN(id, image, alamat, deskripsi, kecamatan, latitude, longtitude);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());
                        Toast.makeText(JalanActivity.this, "Laporan Terkirim", Toast.LENGTH_LONG).show();

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
                        Toast.makeText(JalanActivity.this, "Nothing returned", Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.bar));
        }
    }





    public void onHomeClick(View view){
        Intent intent1 = new Intent(this,HomeActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
        // startActivity(new Intent(this,LoginActivity.class));
        //overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
    }


}
