package www.ethichadebe.co.za.picturechooserandcropper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import www.ethichadebe.co.za.uploadpicture.UploadImage;

public class SingleImageExampleActivity extends AppCompatActivity {
    private static final String TAG = "SingleImageExampleActivity";
    private ImageView ivImage;
    private Dialog myDialog;
    private UploadImage uploadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image_example);

        ivImage = findViewById(R.id.ivImage);
        myDialog = new Dialog(this);

        //Instantiate Upload image class
        uploadImage = new UploadImage(this, this, getPackageManager(), myDialog, ivImage,
                "www.ethichadebe.co.za.picturechooserandcropper", TAG);

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage.start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uploadImage.onActivityResult(getCacheDir(), requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        uploadImage.onRequestPermissionsResult(requestCode, grantResults);
    }

    public void changeActivity(View view) {
        startActivity(new Intent(this, ImageArrayExampleActivity.class));
    }
}