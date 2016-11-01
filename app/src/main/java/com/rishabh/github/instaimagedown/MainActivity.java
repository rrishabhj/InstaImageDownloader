package com.rishabh.github.instaimagedown;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rishabh.github.instaimagedown.service.CustomFloatingViewService;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    EditText editTexturl;
    EditText nameTxt;
    private static final int CUSTOM_OVERLAY_PERMISSION_REQUEST_CODE = 101;
    Button fSwitch;
    private boolean switchStatus=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("");

        setContentView(R.layout.activity_main);

        fSwitch= (Button) findViewById(R.id.floatingButton);

        editTexturl = (EditText) findViewById(R.id.edittxturl);
        nameTxt = (EditText) findViewById(R.id.nameTxt);

//        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        switchStatus=pref.getBoolean("TOGGLE_FBUTTON",true);

        //if(switchStatus){
          fSwitch.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  showCustomFloatingView(getApplicationContext(), true);
              }
          });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.aboutbtn:
                Toast.makeText(getApplicationContext(), "Developed by Rishabh Jindal", Toast.LENGTH_LONG).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnreset:
                editTexturl.setText("");
                break;

            case R.id.btnPaste:
                ClipData data = ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).getPrimaryClip();
                if (data == null) {
                    Toast.makeText(this, "nothing found to paste", Toast.LENGTH_SHORT).show();
                }
                ClipData.Item toPaste = data.getItemAt(0);
                editTexturl.setText(toPaste.getText().toString());
                editTexturl.setSelection(editTexturl.getText().length());
                break;

            case R.id.btnDownload:
                String name = nameTxt.getText().toString();
                if (name.isEmpty()) {
                    name = "InstaImage" + Math.abs(new Random().nextInt());
                }
                String url = editTexturl.getText().toString().replaceAll("\\s+", "") + "media/?size=l";
                if (url.length() < 6) {
                    Toast.makeText(this, "url too short", Toast.LENGTH_SHORT).show();
                    editTexturl.setText("");
                    return;
                }
                Intent imageAct = new Intent(MainActivity.this, ImageActivity.class);
                imageAct.putExtra(ImageActivity.KEY_NAME, name);
                imageAct.putExtra(ImageActivity.KEY_URL, url);
                startActivity(imageAct);
                break;

            default:
                break;
        }

    }


    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CUSTOM_OVERLAY_PERMISSION_REQUEST_CODE) {
            showCustomFloatingView(getApplicationContext(), false);
        }
    }

    @SuppressLint("NewApi")
    private void showCustomFloatingView(Context context, boolean isShowOverlayPermission) {
        // API22以下かチェック
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            context.startService(new Intent(context, CustomFloatingViewService.class));
            return;
        }

        // 他のアプリの上に表示できるかチェック
        if (Settings.canDrawOverlays(context)) {
            context.startService(new Intent(context, CustomFloatingViewService.class));
            return;
        }

        // オーバレイパーミッションの表示
        if (isShowOverlayPermission) {
            final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
            startActivityForResult(intent, CUSTOM_OVERLAY_PERMISSION_REQUEST_CODE);
        }
    }
}
