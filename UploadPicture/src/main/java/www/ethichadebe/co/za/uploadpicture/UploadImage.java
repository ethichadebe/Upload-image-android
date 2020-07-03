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
import android.widget.LinearLayout;
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

public class UploadImage extends AppCompatActivity {
    //constants
    public static final int CAMERA_PERMISSION = 2323, STORAGE_PERMISSION = 2434;
    private Activity activity;
    private Context context;
    private PackageManager packageManager;
    private String packageName, TAG;
    private Dialog myDialog;

    //Single image
    private String pathToFile;
    private int width = 1, height = 1;//image width and x value for aspect ration, image height and y value for aspect ration
    private ImageView ivImage;
    //UI variables
    private TextView tvHeading, tvMessage, tvCamera, tvGallery, tvRemove;
    private LinearLayout llHeading,llMessage, llCancel,llRemove,rlButtons,llCamera,llGallery;

    //Image array
    private ImageView[] ivImageArr;
    private String[] pathToFileArr;
    private int[] widthArr, heightArr;//image width and x value for aspect ration, image height and y value for aspect ration
    //UI variables
    private TextView[] tvHeadingArr, tvMessageArr,tvCameraArr, tvGalleryArr, tvRemoveArr;
    private LinearLayout[] llHeadingArr,llMessageArr, llCancelArr,llRemoveArr,rlButtonsArr,llCameraArr,llGalleryArr;

    /**
     * Single picture constructor
     *
     * @param activity       Activity
     * @param context        Context
     * @param packageManager getPackageManager()
     * @param myDialog       Dialog
     * @param ivImage        ImageView
     * @param packageName    e.g "www.ethichadebe.co.za.uploadpicture"
     * @param TAG            String
     */
    public UploadImage(final Activity activity, final Context context, PackageManager packageManager,
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
     * Single picture constructor
     *
     * @param activity       Activity
     * @param context        Context
     * @param packageManager getPackageManager()
     * @param myDialog       Dialog
     * @param ivImageArr     ImageView[]
     * @param packageName    e.g "www.ethichadebe.co.za.uploadpicture"
     * @param TAG            String
     */
    public UploadImage(final Activity activity, final Context context, PackageManager packageManager,
                       final Dialog myDialog, final ImageView[] ivImageArr, final String packageName, final String TAG) {
        this.activity = activity;
        this.context = context;
        this.packageManager = packageManager;
        this.packageName = packageName;
        this.TAG = TAG;
        this.myDialog = myDialog;
        this.ivImageArr = ivImageArr;
        this.pathToFileArr = new String[ivImageArr.length];
        this.widthArr = new int[ivImageArr.length];
        this.heightArr = new int[ivImageArr.length];
        this.tvCameraArr = new TextView[ivImageArr.length];
        this.tvGalleryArr = new TextView[ivImageArr.length];
        this.tvHeadingArr = new TextView[ivImageArr.length];
        this.tvMessageArr = new TextView[ivImageArr.length];
        this.tvRemoveArr = new TextView[ivImageArr.length];
        this.llCameraArr = new LinearLayout[ivImageArr.length];
        this.llCancelArr = new LinearLayout[ivImageArr.length];
        this.llGalleryArr = new LinearLayout[ivImageArr.length];
        this.llHeadingArr = new LinearLayout[ivImageArr.length];
        this.llMessageArr = new LinearLayout[ivImageArr.length];
        this.llRemoveArr = new LinearLayout[ivImageArr.length];
        this.rlButtonsArr = new LinearLayout[ivImageArr.length];
        for (int i = 0; i < ivImageArr.length; i++) {
            widthArr[i] = 1;
            heightArr[i] = 1;
        }
    }

    /**
     * Open popup dialog for single image
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

        llCamera = myDialog.findViewById(R.id.llCamera);
        llCancel = myDialog.findViewById(R.id.llCancel);
        llGallery = myDialog.findViewById(R.id.llGallery);
        llHeading = myDialog.findViewById(R.id.llHeading);
        llMessage = myDialog.findViewById(R.id.llMessage);
        llRemove = myDialog.findViewById(R.id.llRemove);
        rlButtons = myDialog.findViewById(R.id.rlButtons);

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
     * Open popup dialog for image array
     *
     * @param index image index
     */
    public void start(final int index) {
        myDialog.setContentView(R.layout.popup_dialog);

        //Initialise variables
        TextView tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvHeadingArr[index] = myDialog.findViewById(R.id.tvHeading);
        tvMessageArr[index] = myDialog.findViewById(R.id.tvMessage);
        tvCameraArr[index] = myDialog.findViewById(R.id.tvCamera);
        tvGalleryArr[index] = myDialog.findViewById(R.id.tvGallery);
        tvRemoveArr[index] = myDialog.findViewById(R.id.tvRemove);

        llCameraArr[index] = myDialog.findViewById(R.id.llCamera);
        llCancelArr[index] = myDialog.findViewById(R.id.llCancel);
        llGalleryArr[index] = myDialog.findViewById(R.id.llGallery);
        llHeadingArr[index] = myDialog.findViewById(R.id.llHeading);
        llMessageArr[index] = myDialog.findViewById(R.id.llMessage);
        llRemoveArr[index] = myDialog.findViewById(R.id.llRemove);
        rlButtonsArr[index] = myDialog.findViewById(R.id.rlButtons);

        //Display "remove picture" option when there's an image
        if (ivImageArr[index].getDrawable() != null) {
            tvRemoveArr[index].setVisibility(View.VISIBLE);
        } else {
            tvRemoveArr[index].setVisibility(View.GONE);
        }

        tvRemoveArr[index].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivImageArr[index].setImageDrawable(null);
                myDialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        tvCameraArr[index].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "tvCamera onClick: Take picture");
                    takePicture(index);
                } else {
                    Log.d(TAG, "tvCamera onClick: Request permissions");
                    requestPermission(CAMERA_PERMISSION);
                }
                myDialog.dismiss();
            }
        });

        tvGalleryArr[index].setOnClickListener(new View.OnClickListener() {
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
     * @param index index
     * @return get path to image Array
     */
    public String getPathToFile(int index) {
        return pathToFileArr[index];
    }

    /**
     * @return image width and height. width = getWidthHeight()[0] and height = getWidthHeight()[1]
     */
    public int[] getWidthHeight() {
        return new int[]{width, height};
    }

    /**
     * @param index index
     * @return image width and height. width = getWidthHeight(index)[0] and height = getWidthHeight(index)[1]
     */
    public int[] getWidthHeight(int index) {
        return new int[]{widthArr[index], heightArr[index]};
    }

    /**
     * set image width and height
     *
     * @param width  width
     * @param height height
     */
    public void setWidthHeight(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * set image width and height
     *
     * @param width  width
     * @param height height
     * @param index  index
     */
    public void setWidthHeight(int width, int height, int index) {
        this.widthArr[index] = width;
        this.heightArr[index] = height;
    }

    /**
     * @return get image width and aspect ratio
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param index index
     * @return get image width and aspect ratio
     */
    public int getWidth(int index) {
        return widthArr[index];
    }

    /**
     * @param width image width and aspect ratio
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @param width image width and aspect ratio
     * @param index index
     */
    public void setWidth(int width, int index) {
        this.widthArr[index] = width;
    }

    /**
     * @return get image height and aspect ratio
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param index index
     * @return get image height and aspect ratio
     */
    public int getHeight(int index) {
        return heightArr[index];
    }

    /**
     * @param height set image height and aspect ratio
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @param height set image height and aspect ratio
     * @param index  index
     */
    public void setHeight(int height, int index) {
        this.heightArr[index] = height;
    }

    /**
     * Change the displayed text in the popup heading
     *
     * @param textView Takes textView for heading
     */
    public void setHeadingTextView(TextView textView) {
        tvHeading=textView;
    }

    /**
     * Change the displayed text in the popup heading
     *
     * @param textView  Takes textView for heading
     * @param index index
     */
    public void setHeadingTextView(TextView textView, int index) {
        tvHeadingArr[index]=textView;
    }

    /**
     * @return text in Heading TextView
     */
    public TextView getHeadingTextView() {
        return tvHeading;
    }

    /**
     * @param index index
     * @return text in Heading TextView
     */
    public TextView getHeadingTextView(int index) {
        return tvHeadingArr[index];
    }

    /**
     * Change the displayed text in the popup message
     *
     * @param textView Takes textView for message
     */
    public void setMessageTextView(TextView textView) {
        tvMessage=textView;
    }

    /**
     * Change the displayed text in the popup message
     *
     * @param textView  Takes textView for message
     * @param index index
     */
    public void setMessageTextView(TextView textView, int index) {
        tvMessageArr[index]=textView;
    }

    /**
     * @return text in message TextView
     */
    public TextView getMessageTextView() {
        return tvMessage;
    }

    /**
     * @param index index
     * @return text in message TextView
     */
    public TextView getMessageTextView(int index) {
        return tvMessageArr[index];
    }

    /**
     * Change the displayed text in the popup "open gallery" option
     *
     * @param textView Takes textView for "open gallery" option
     */
    public void setGalleryTextView(TextView textView) {
        tvGallery=textView;
    }

    /**
     * Change the displayed text in the popup "open gallery" option
     *
     * @param textView  Takes textView for "open gallery" option
     * @param index index
     */
    public void setGalleryTextView(TextView textView, int index) {
        tvGalleryArr[index] = textView;
    }

    /**
     * @return text in "open gallery" TextView
     */
    public TextView getGalleryTextView() {
        return tvGallery;
    }

    /**
     * @param index index
     * @return text in "open gallery" TextView
     */
    public TextView getGalleryTextView(int index) {
        return tvGalleryArr[index];
    }

    /**
     * Change the displayed text in the popup "open camera" option
     *
     * @param textView Takes textView for "open camera" option
     */
    public void setCameraTextView(TextView textView) {
        tvCamera=textView;
    }

    /**
     * Change the displayed text in the popup "open camera" option
     *
     * @param textView Takes textView for "open camera" option
     * @param index index
     */
    public void setCameraTextView(TextView textView, int index) {
        tvCameraArr[index]=textView;
    }

    /**
     * @return text in "open camera" TextView
     */
    public TextView getCameraText() {
        return tvCamera;
    }

    /**
     *
     * @param index index
     * @return text in "open camera" TextView
     */
    public TextView getCameraTextView(int index) {
        return tvCameraArr[index];
    }

    /**
     * Change the displayed text in the popup "remove image" option
     *
     * @param textView Takes textView for "remove image" option
     */
    public void setRemoveTextView(TextView textView) {
        tvRemove=textView;
    }

    /**
     * Change the displayed text in the popup "remove image" option
     *
     * @param textView Takes textView for "remove image" option
     * @param index index
     */
    public void setRemoveTextView(TextView textView, int index) {
        tvRemoveArr[index]=textView;
    }

    /**
     * @return text in "remove image" TextView
     */
    public TextView getRemoveTextView() {
        return tvRemove;
    }

    /**
     *
     * @param index index
     * @return text in "remove image" TextView
     */
    public TextView getRemoveTextView(int index) {
        return tvRemoveArr[index];
    }

    /**
     * Paste Paste method in onRequestPermissionsResult
     *
     * @param requestCode  requestCode
     * @param grantResults grantResults
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
     * Paste Paste method in onRequestPermissionsResult
     *
     * @param requestCode  requestCode
     * @param grantResults grantResults
     * @param index index
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull int[] grantResults, int index) {
        if ((requestCode == STORAGE_PERMISSION) && ((grantResults.length) > 0) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*"),
                    STORAGE_PERMISSION);
        } else if ((requestCode == CAMERA_PERMISSION) && ((grantResults.length) > 0) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takePicture(index);
        }
    }

    /**
     * Paste Paste method in onActivityResult for single image
     *
     * @param file        getCacheDir()
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        data
     */
    public void onActivityResult(File file, int requestCode, int resultCode, @Nullable Intent data) {
        Uri uri;
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
     * Paste Paste method in onActivityResult for image Array
     *
     * @param file        getCacheDir()
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        data
     * @param index       image index
     */
    public void onActivityResult(File file, int requestCode, int resultCode, @Nullable Intent data, int index) {
        Uri uri;
        if ((requestCode == STORAGE_PERMISSION) && (resultCode == RESULT_OK)) {
            Log.d(TAG, "onActivityResult: (requestCode == STORAGE_PERMISSION) && (resultCode == RESULT_OK)");
            uri = data.getData();
            if (uri != null) {
                Log.d(TAG, "onActivityResult: data.getData() != null");
                startCrop(file, uri, index);
            }
        } else if ((requestCode == CAMERA_PERMISSION) && (resultCode == RESULT_OK)) {
            Log.d(TAG, "onActivityResult: (requestCode == CAMERA_PERMISSION) && (resultCode == RESULT_OK)");
            if (BitmapFactory.decodeFile(pathToFileArr[index]) != null) {
                Log.d(TAG, "onActivityResult: BitmapFactory.decodeFile(pathToFile) != null");
                startCrop(file, Uri.fromFile(new File(pathToFileArr[index])), index);
            }
        } else if ((requestCode == UCrop.REQUEST_CROP) && (resultCode == RESULT_OK)) {
            Log.d(TAG, "onActivityResult: (requestCode == UCrop.REQUEST_CROP) && (resultCode == RESULT_OK)");
            uri = UCrop.getOutput(data);
            if (uri != null) {
                Log.d(TAG, "onActivityResult: uri != null");
                ivImageArr[index].setImageDrawable(null);
                ivImageArr[index].setImageURI(uri);
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
                activity.startActivityForResult(intent, CAMERA_PERMISSION);
            }
        }
    }

    /**
     * Open camera activity
     * @param index index
     */
    private void takePicture(int index) {
        Log.d(TAG, "takePicture: Taking picture");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Check if there's an app available to take picture
        if (intent.resolveActivity(packageManager) != null) {
            Log.d(TAG, "takePicture: getPackageManager()) != null");
            File photo;
            photo = createFile();
            //Save picture into the photo var
            if (photo != null) {
                pathToFileArr[index] = photo.getAbsolutePath();
                Uri photoUri = FileProvider.getUriForFile(context, packageName,
                        photo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                Log.d(TAG, "takePicture: Start picture taking activity");
                activity.startActivityForResult(intent, CAMERA_PERMISSION);
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
                .withMaxResultSize(width * 1000, height * 1000)
                .withOptions(getOptions())
                .start(activity);
    }

    /**
     * Start cropping activity
     *
     * @param file file
     * @param uri  image uri
     * @param index index
     */
    private void startCrop(File file, @NonNull Uri uri, int index) {
        Log.d(TAG, "startCrop: Start crop");
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(file, "SampleCropImg.jpg")));
        uCrop.withAspectRatio(widthArr[index], heightArr[index])
                .withMaxResultSize(widthArr[index] * 1000, heightArr[index] * 1000)
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

    public LinearLayout getMessageBackground() {
        return llMessage;
    }

    public void setMessageBackground(LinearLayout llMessage) {
        this.llMessage = llMessage;
    }

    public LinearLayout getHeadingBackground() {
        return llHeading;
    }

    public void setHeadingBackground(LinearLayout llHeading) {
        this.llHeading = llHeading;
    }

    public LinearLayout getCancelBackground() {
        return llCancel;
    }

    public void setCancelBackground(LinearLayout llCancel) {
        this.llCancel = llCancel;
    }

    public LinearLayout getRemoveBackground() {
        return llRemove;
    }

    public void setRemoveBackground(LinearLayout llRemove) {
        this.llRemove = llRemove;
    }

    public LinearLayout getButtonsBackground() {
        return rlButtons;
    }

    public void setButtonsBackground(LinearLayout rlButtons) {
        this.rlButtons = rlButtons;
    }

    public LinearLayout getCameraBackground() {
        return llCamera;
    }

    public void setCameraBackground(LinearLayout llCamera) {
        this.llCamera = llCamera;
    }

    public LinearLayout getGalleryBackground() {
        return llGallery;
    }

    public void setGalleryBackground(LinearLayout llGallery) {
        this.llGallery = llGallery;
    }

    public LinearLayout[] getHeadingBackgroundArr() {
        return llHeadingArr;
    }

    public void setHeadingBackgroundArr(LinearLayout[] llHeadingArr) {
        this.llHeadingArr = llHeadingArr;
    }

    public LinearLayout[] getMessageBackgroundArr() {
        return llMessageArr;
    }

    public void setMessageBackgroundArr(LinearLayout[] llMessageArr) {
        this.llMessageArr = llMessageArr;
    }

    public LinearLayout[] getCancelBackgroundArr() {
        return llCancelArr;
    }

    public void setCancelBackgroundArr(LinearLayout[] llCancelArr) {
        this.llCancelArr = llCancelArr;
    }

    public LinearLayout[] getRemoveBackgroundArr() {
        return llRemoveArr;
    }

    public void setRemoveBackgroundArr(LinearLayout[] llRemoveArr) {
        this.llRemoveArr = llRemoveArr;
    }

    public LinearLayout[] getButtonsBackgroundArr() {
        return rlButtonsArr;
    }

    public void setButtonsBackgroundArr(LinearLayout[] rlButtonsArr) {
        this.rlButtonsArr = rlButtonsArr;
    }

    public LinearLayout[] getCameraBackgroundArr() {
        return llCameraArr;
    }

    public void setCameraBackgroundArr(LinearLayout[] llCameraArr) {
        this.llCameraArr = llCameraArr;
    }

    public LinearLayout[] getGalleryBackgroundArr() {
        return llGalleryArr;
    }

    public void setGalleryBackgroundArr(LinearLayout[] llGalleryArr) {
        this.llGalleryArr = llGalleryArr;
    }
}
