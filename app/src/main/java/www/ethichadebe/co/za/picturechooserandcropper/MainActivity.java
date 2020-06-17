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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ImageView ivImage;
    private Dialog myDialog;
    private String pathToFile = "";
    private UploadImage uploadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivImage = findViewById(R.id.ivImage);
        myDialog = new Dialog(this);

        uploadImage = new UploadImage(this,this,getPackageManager(),myDialog,ivImage,
                "www.ethichadebe.co.za.uploadpicture.UploadImage",TAG);
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // onActivityResult(this, TAG, ivImage, pathToFile, requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //onRequestPermissionsResult(TAG, requestCode, grantResults);
    }
}