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

public class ImageArrayExampleActivity extends AppCompatActivity {
    private static final String TAG = "ImageArrayExampleActivi";
    private ImageView[] ivImage = new ImageView[4];
    private Dialog myDialog;
    private UploadImage uploadImage;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_array_example);

        ivImage[0] = findViewById(R.id.ivImage1);
        ivImage[1] = findViewById(R.id.ivImage2);
        ivImage[2] = findViewById(R.id.ivImage3);
        ivImage[3] = findViewById(R.id.ivImage4);
        myDialog = new Dialog(this);

        //Instantiate Upload image class
        uploadImage = new UploadImage(this, this, getPackageManager(), myDialog, ivImage,
                "www.ethichadebe.co.za.picturechooserandcropper", TAG);

        ivImage[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 0;
                uploadImage.start(index);
            }
        });

        ivImage[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 1;
                uploadImage.start(index);
            }
        });

        ivImage[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 2;
                uploadImage.start(index);
            }
        });

        ivImage[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 3;
                uploadImage.start(index );
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uploadImage.onActivityResult(getCacheDir(), requestCode, resultCode, data, index);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        uploadImage.onRequestPermissionsResult(requestCode, grantResults);
    }

    public void changeActivity(View view) {
        startActivity(new Intent(this, SingleImageExampleActivity.class));
    }
}