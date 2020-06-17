package www.ethichadebe.co.za.uploadpicture;

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
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

public class UploadImage extends Activity {
    //constants
    public static final int CAMERA_PERMISSION = 123;
    public static final int STORAGE_PERMISSION = 1234;

    //UI variables
    private TextView tvHeading;
    private TextView tvMessage;
    private TextView tvCamera;
    private TextView tvGallery;
    private TextView tvRemove;

    private String pathToFile;
    private int width = 1;//width image width and x value for aspect ration
    private int height = 1;//height image height and y value for aspect ration
    private Activity activity;
    private Context context;
    private PackageManager packageManager;
    private String packageName;
    private String TAG;
    private Dialog myDialog;
    private ImageView ivImage;

    public UploadImage(final Activity activity, final Context context, final PackageManager packageManager,
                       final Dialog myDialog, final ImageView ivImage, final String packageName, final String TAG) {
        this.activity = activity;
        this.context = context;
        this.packageManager = packageManager;
        this.packageName = packageName;
        this.TAG = TAG;
        this.myDialog = myDialog;
        this.ivImage = ivImage;
    }

    /**
     * Open popup dialog
     */
    public void start() {
        myDialog.setContentView(R.layout.popup_dialog);

        //Initialise variables
        TextView tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvHeading = myDialog.findViewById(R.id.tvHeading);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        tvCamera = myDialog.findViewById(R.id.tvCamera);
        tvGallery = myDialog.findViewById(R.id.tvGallery);
        tvRemove = myDialog.findViewById(R.id.tvRemove);

        //Display "remove picture" option when there's an image
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

        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "tvCamera onClick: Take picture");
                    takePicture();
                } else {
                    Log.d(TAG, "tvCamera onClick: Request permissions");
                    requestPermission(CAMERA_PERMISSION);
                }
                myDialog.dismiss();
            }
        });

        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    activity.startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT)
                            .setType("image/*"), STORAGE_PERMISSION);
                } else {
                    requestPermission(STORAGE_PERMISSION);
                }
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * @return get path to image
     */
    public String getPathToFile() {
        return pathToFile;
    }

    /**
     * @return get image width and aspect ratio
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width image width and aspect ratio
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return get image height and aspect ratio
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height set image height and aspect ratio
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Change the displayed text in the popup heading
     *
     * @param text Takes string for heading
     */
    public void setHeadingText(String text) {
        tvHeading.setText(text);
    }

    /**
     * @return text in Heading TextView
     */
    public String getHeadingText() {
        return tvHeading.getText().toString();
    }

    /**
     * Change the displayed text in the popup message
     *
     * @param text Takes string for message
     */
    public void setMessageText(String text) {
        tvMessage.setText(text);
    }

    /**
     * @return text in message TextView
     */
    public String getMessageText() {
        return tvMessage.getText().toString();
    }

    /**
     * Change the displayed text in the popup "open gallery" option
     *
     * @param text Takes string for "open gallery" option
     */
    public void setGalleryText(String text) {
        tvGallery.setText(text);
    }

    /**
     * @return text in "open gallery" TextView
     */
    public String getGalleryText() {
        return tvGallery.getText().toString();
    }

    /**
     * Change the displayed text in the popup "open camera" option
     *
     * @param text Takes string for "open camera" option
     */
    public void setCameraText(String text) {
        tvCamera.setText(text);
    }

    /**
     * @return text in "open camera" TextView
     */
    public String getCameraText() {
        return tvCamera.getText().toString();
    }

    /**
     * Change the displayed text in the popup "remove image" option
     *
     * @param text Takes string for "remove image" option
     */
    public void setRemoveText(String text) {
        tvRemove.setText(text);
    }

    /**
     * @return text in "remove image" TextView
     */
    public String getRemoveText() {
        return tvRemove.getText().toString();
    }

    /**
     * Paste Paste method in onRequestPermissionsResult
     *
     * @param requestCode
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull int[] grantResults) {
        if ((requestCode == STORAGE_PERMISSION) && ((grantResults.length) > 0) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*"),
                    STORAGE_PERMISSION);
        } else if ((requestCode == CAMERA_PERMISSION) && ((grantResults.length) > 0) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takePicture();
        }
    }

    /**
     * Paste Paste method in onActivityResult
     *
     * @param file        getCacheDir()
     * @param ivImage     image
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        data
     */
    public void onActivityResult(File file, ImageView ivImage, int requestCode, int resultCode, @Nullable Intent data) {
        Uri uri;
        //civProfilePicture.
        if ((requestCode == STORAGE_PERMISSION) && (resultCode == RESULT_OK)) {
            Log.d(TAG, "onActivityResult: (requestCode == STORAGE_PERMISSION) && (resultCode == RESULT_OK)");
            uri = data.getData();
            if (uri != null) {
                Log.d(TAG, "onActivityResult: data.getData() != null");
                startCrop(file, uri);
            }
        } else if ((requestCode == CAMERA_PERMISSION) && (resultCode == RESULT_OK)) {
            Log.d(TAG, "onActivityResult: (requestCode == CAMERA_PERMISSION) && (resultCode == RESULT_OK)");
            if (BitmapFactory.decodeFile(pathToFile) != null) {
                Log.d(TAG, "onActivityResult: BitmapFactory.decodeFile(pathToFile) != null");
                startCrop(file, Uri.fromFile(new File(pathToFile)));
            }
        } else if ((requestCode == UCrop.REQUEST_CROP) && (resultCode == RESULT_OK)) {
            Log.d(TAG, "onActivityResult: (requestCode == UCrop.REQUEST_CROP) && (resultCode == RESULT_OK)");
            uri = UCrop.getOutput(data);
            if (uri != null) {
                Log.d(TAG, "onActivityResult: uri != null");
                ivImage.setImageDrawable(null);
                ivImage.setImageURI(uri);
            }
        }
    }

    /**
     * Open camera activity
     */
    private void takePicture() {
        Log.d(TAG, "takePicture: Taking picture");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Check if there's an app available to take picture
        if (intent.resolveActivity(packageManager) != null) {
            Log.d(TAG, "takePicture: getPackageManager()) != null");
            File photo;
            photo = createFile();
            //Save picture into the photo var
            if (photo != null) {
                pathToFile = photo.getAbsolutePath();
                Uri photoUri = FileProvider.getUriForFile(context, packageName,
                        photo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                Log.d(TAG, "takePicture: Start picture taking activity");
                startActivityForResult(intent, CAMERA_PERMISSION);
            }
        }
    }

    /**
     * Creates temp file to store image
     *
     * @return file
     */
    private File createFile() {
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

    /**
     * Requests camera and storage permissions
     *
     * @param permission permission
     */
    private void requestPermission(int permission) {
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

    /**
     * Start cropping activity
     *
     * @param file file
     * @param uri  image uri
     */
    private void startCrop(File file, @NonNull Uri uri) {
        Log.d(TAG, "startCrop: Start crop");
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(file, "SampleCropImg.jpg")));
        uCrop.withAspectRatio(width, height)
                .withMaxResultSize(width, height)
                .withOptions(getOptions())
                .start(activity);
    }

    /**
     * returns Ucrop options
     *
     * @return UCrop.Options
     */
    private UCrop.Options getOptions() {
        Log.d(TAG, "getOptions: get options");
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(70);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);

        return options;
    }

}
