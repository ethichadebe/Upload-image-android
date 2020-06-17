package www.ethichadebe.co.za.picturechooserandcropper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final int CAMERA_PERMISSION = 123;
    public static final int STORAGE_PERMISSION = 1234;
    private ImageView ivImage;
    private Dialog myDialog;
    private String pathToFile="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivImage = findViewById(R.id.ivImage);
        myDialog = new Dialog(this);

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDPEditPopup(TAG, myDialog, ivImage);

            }
        });
    }

    public void ShowDPEditPopup(final String TAG, final Dialog myDialog, final ImageView ivImage) {
        TextView tvCancel, tvHeading, tvMessage, tvCamera, tvGallery, tvRemove;
        myDialog.setContentView(R.layout.popup_dialog);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvHeading = myDialog.findViewById(R.id.tvHeading);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        tvCamera = myDialog.findViewById(R.id.tvCamera);
        tvGallery = myDialog.findViewById(R.id.tvGallery);
        tvRemove = myDialog.findViewById(R.id.tvRemove);

        if (ivImage.getDrawable() != null) {
            tvRemove.setVisibility(View.VISIBLE);
        } else {
            tvRemove.setVisibility(View.GONE);
        }

        tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivImage.setImageDrawable(null);
                myDialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        tvMessage.setText("Change Profile Picture");

        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "ShowDPEditPopup: Take picture");
                    MainActivity.this.takePicture(TAG);
                    myDialog.dismiss();
                } else {
                    myDialog.dismiss();
                    requestPermission(MainActivity.this, MainActivity.this, CAMERA_PERMISSION);
                }

            }
        });

        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    myDialog.dismiss();
                    MainActivity.this.startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*"),
                            STORAGE_PERMISSION);
                } else {
                    myDialog.dismiss();
                    requestPermission(MainActivity.this, MainActivity.this, STORAGE_PERMISSION);
                }
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
    }

    private void takePicture(String TAG) {
        Log.d(TAG, "takePicture: Taking picture");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Check if there's an app available to take picture
        if (intent.resolveActivity(getPackageManager()) != null) {
            Log.d(TAG, "takePicture: getPackageManager()) != null");
            File photo;
            photo = createFile(TAG);
            //Save picture into the photo var
            if (photo != null) {
                pathToFile = photo.getAbsolutePath();
                String fileName = pathToFile.substring(pathToFile.lastIndexOf("/") + 1);
                Uri photoUri = FileProvider.getUriForFile(this,
                        "www.ethichadebe.co.za.picturechooserandcropper", photo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                Log.d(TAG, "takePicture: Start picture taking activity");
                startActivityForResult(intent, CAMERA_PERMISSION);
            }
        }
    }

    public static void requestPermission(final Activity activity, Context context, int permission) {
        if (permission == STORAGE_PERMISSION) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(context)
                        .setTitle("Permission needed")
                        .setMessage("This permission is needed so you can gain access to your gallery")
                        .setPositiveButton("Enable permission", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);

                            }
                        })
                        .setNegativeButton("close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
            }
        } else if (permission == CAMERA_PERMISSION) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(context)
                        .setTitle("Permission needed")
                        .setMessage("This permission is needed so you can gain access to your camera")
                        .setPositiveButton("Enable permission", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                        CAMERA_PERMISSION);

                            }
                        })
                        .setNegativeButton("close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, CAMERA_PERMISSION);
            }
        }
    }

    public static File createFile(String TAG) {
        Log.d(TAG, "createFile: Creating File");

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            Log.d(TAG, "createFile: File created");
            return File.createTempFile(String.valueOf(System.currentTimeMillis()), ".jpg", storageDir);
        } catch (IOException e) {
            Log.d(TAG, "createFile: " + e.toString());
        }
        return null;
    }

    private void onActivityResult(Activity activity, String TAG, ImageView ivImage, String pathToFile, int requestCode, int resultCode, @Nullable Intent data) {
        Uri uri;
        //civProfilePicture.
        if ((requestCode == STORAGE_PERMISSION) && (resultCode == RESULT_OK)) {
            uri = data.getData();
            if (uri != null) {
                startCrop(activity, getCacheDir(), uri, 350, 350);
            }
        } else if ((requestCode == CAMERA_PERMISSION) && (resultCode == RESULT_OK)) {
            if (BitmapFactory.decodeFile(pathToFile) != null) {
                startCrop(activity, getCacheDir(), Uri.fromFile(new File(pathToFile)), 350,
                        350);
            }
        } else if ((requestCode == UCrop.REQUEST_CROP) && (resultCode == RESULT_OK)) {
            uri = UCrop.getOutput(data);
            if (uri != null) {
                ivImage.setImageDrawable(null);
                ivImage.setImageURI(uri);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onActivityResult(this,TAG,ivImage,pathToFile,requestCode,resultCode,data);
    }

    private void onRequestPermissionsResult(String TAG, int requestCode, @NonNull int[] grantResults) {
        if ((requestCode == STORAGE_PERMISSION) && ((grantResults.length) > 0) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*"),
                    STORAGE_PERMISSION);
        } else if ((requestCode == CAMERA_PERMISSION) && ((grantResults.length) > 0) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takePicture(TAG);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        onRequestPermissionsResult(TAG, requestCode, grantResults);
    }

    public static void startCrop(Activity activity, File file, @NonNull Uri uri, int width, int height) {
        //Crop
        String destFilename = "SampleCropImg.jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(file, destFilename)));
        uCrop.withAspectRatio(width, height)
                .withMaxResultSize(width, height)
                .withOptions(getOptions())
                .start(activity);
    }

    private static UCrop.Options getOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(70);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);

        return options;
    }
}