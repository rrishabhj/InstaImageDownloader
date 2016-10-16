package com.rishabh.github.instaimagedown;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class ImageActivity extends AppCompatActivity {

    public static final String KEY_NAME = "name";
    public static final String KEY_URL = "url";
    private String name;
    private ImageView imgDisplay;
    private final int RES_ERROR = R.drawable.error_orange;
    private final int RES_PLACEHOLDER = R.drawable.placeholder_grey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        name = getIntent().getStringExtra(KEY_NAME);
        final String url = getIntent().getStringExtra(KEY_URL);

        setContentView(R.layout.activity_image_basic_dm);
        getSupportActionBar().setTitle("Image Downloaded");
        imgDisplay = (ImageView) findViewById(R.id.imgResult);
        imgDisplay.setImageResource(RES_PLACEHOLDER);
        final TextView tvPercent = (TextView) findViewById(R.id.tvPercent);
        final ProgressBar pbLoading = (ProgressBar) findViewById(R.id.pbImageLoading);
        final BasicImageDownloader downloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
            @Override
            public void onError(BasicImageDownloader.ImageError error) {
                Toast.makeText(ImageActivity.this, "Error code " + error.getErrorCode() + ": " +
                        error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
                imgDisplay.setImageResource(RES_ERROR);
                tvPercent.setVisibility(View.GONE);
                pbLoading.setVisibility(View.GONE);
            }

            @Override
            public void onProgressChange(int percent) {
                pbLoading.setProgress(percent);
                tvPercent.setText(percent + "%");
            }

            @Override
            public void onComplete(Bitmap result) {

                final Bitmap.CompressFormat mFormat = Bitmap.CompressFormat.JPEG;

                final File myImageFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "image_test" + File.separator + name + "." + mFormat.name().toLowerCase());
                BasicImageDownloader.writeToDisk(myImageFile, result, new BasicImageDownloader.OnBitmapSaveListener() {
                    @Override
                    public void onBitmapSaved() {
                        Toast.makeText(ImageActivity.this, "Image saved as: " + myImageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        new SingleMediaScanner(getApplicationContext(), myImageFile);
                    }

                    @Override
                    public void onBitmapSaveError(BasicImageDownloader.ImageError error) {
                        Toast.makeText(ImageActivity.this, "Error code " + error.getErrorCode() + ": " +
                                error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }


                }, mFormat, false);

                tvPercent.setVisibility(View.GONE);
                pbLoading.setVisibility(View.GONE);
                imgDisplay.setImageBitmap(result);
                imgDisplay.startAnimation(AnimationUtils.loadAnimation(ImageActivity.this, android.R.anim.fade_in));
            }
        });
        downloader.download(url, true);
    }
}
