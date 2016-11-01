package com.rishabh.github.instaimagedown;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.rishabh.github.instaimagedown.service.CustomFloatingViewService;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    EditText editTexturl;
    EditText nameTxt;
    private static final int CUSTOM_OVERLAY_PERMISSION_REQUEST_CODE = 101;
    Switch fSwitch;
    private boolean switchStatus=true;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        fSwitch= (Switch) findViewById(R.id.floatingButton);

        editTexturl = (EditText) findViewById(R.id.edittxturl);
        nameTxt = (EditText) findViewById(R.id.nameTxt);

//        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        switchStatus=pref.getBoolean("TOGGLE_FBUTTON",true);

        //if(switchStatus){

        fSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    showCustomFloatingView(getApplicationContext(), true);
                }else{
                    stopService(intent);
                }
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
            case R.id.settings:
                Toast.makeText(getApplicationContext(), "Developed by Rishabh Jindal", Toast.LENGTH_LONG).show();
                break;
            case R.id.playStore:
                Toast.makeText(getApplicationContext(),"Rate Instant Insta",Toast.LENGTH_LONG).show();

                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
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
        intent=new Intent(context, CustomFloatingViewService.class);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            context.startService(intent);
            return;
        }

        // 他のアプリの上に表示できるかチェック
        if (Settings.canDrawOverlays(context)) {
            context.startService(intent);
            return;
        }

        // オーバレイパーミッションの表示
        if (isShowOverlayPermission) {
            final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
            startActivityForResult(intent, CUSTOM_OVERLAY_PERMISSION_REQUEST_CODE);
        }
    }
}
