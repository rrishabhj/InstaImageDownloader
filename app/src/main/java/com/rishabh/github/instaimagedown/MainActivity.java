package com.rishabh.github.instaimagedown;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    EditText editTexturl;
    EditText nameTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTexturl = (EditText) findViewById(R.id.edittxturl);
        nameTxt = (EditText) findViewById(R.id.nameTxt);
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
}
