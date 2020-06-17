package www.ethichadebe.co.za.picturechooserandcropper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final int CAMERA_PERMISSION = 123;
    public static final int STORAGE_PERMISSION = 1234;
    private ImageView ivImage;
    private Dialog myDialog;
    private String pathToFile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivImage = findViewById(R.id.ivImage);
        myDialog = new Dialog(this);
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