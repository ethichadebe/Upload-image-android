# Image chooser and cropper

[![Developer](https://img.shields.io/badge/Developer%20Website-Ethic%20Hadebe-green.svg)](http://ethichadebe.cf/?i=1)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/42eb7b00b93645c0812c045ab26cb3b7)](https://www.codacy.com/app/dvg4000/circle-menu-android?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Ramotion/circle-menu-android&amp;utm_campaign=Badge_Grade)
[![Donate](https://img.shields.io/badge/Version-1.1.2-blue.svg)](https://paypal.me/Ramotion)

#### With the help of [Yalantis](https://yalantis.com/?utm_source=github) cropping library, This library aims to provide the easiest way to choose, crop and display a single image or an array of images in android studio. It also handles permmisions requests.

<img src="preview.gif" width="200" height="400">

# Usage

1. Include the library as local library project.

```groovy
allprojects {
	repositories {
		jcenter()
		maven { url "https://jitpack.io" }
	}
}
```

```groovy 
dependencies {
	implementation 'com.github.ethichadebe:Upload-image-android:1.1.2'
}
```

2. Create ```res/xml/file_paths.xml```

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths>
<external-path
    name="my_project"	
    path="Pictures/"/>		<!-- path where images from camera will be stored -->
</paths>
```

3. AndroidManifest.xml

* Add permissions
```xml
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

* Inside Application, add provider. In ```android:authorities``` write package name
```xml
    <provider
        android:authorities="my_package"
        android:name="androidx.core.content.FileProvider"
        android:exported="false"
        android:grantUriPermissions="true">
        <meta-data
            android:name="android.support.FILE_PROVIDER_PATHS"
            android:resource="@xml/file_paths"/>
    </provider>
```

4. Example Activity.

```java

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
    private UploadImage uploadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivImage = findViewById(R.id.ivImage);
        myDialog = new Dialog(this);

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
}

```

## MIT License

Copyright (c) 2020 Ethic Mthobisi Hadebe

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
