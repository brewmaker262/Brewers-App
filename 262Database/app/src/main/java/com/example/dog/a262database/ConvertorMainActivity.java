package com.example.dog.a262database;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/**
 * This class contains navigation to three convertors.
 *
 * @author Sicheng Zhu
 */
public class ConvertorMainActivity extends Activity implements View.OnClickListener {
    private Button alcoholByVolumeButton;
    private Button grainDMELMEButton;
    private Button dMELMEButton;
    private Intent intent;

    // Add components to UI once this activity is loaded.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.convertor_main_layout);

        alcoholByVolumeButton = (Button)findViewById(R.id.alcohol_by_volume_button);
        grainDMELMEButton = (Button)findViewById(R.id.grain_dme_lme_button);
        dMELMEButton = (Button)findViewById(R.id.dme_lme_button);

        alcoholByVolumeButton.setOnClickListener(this);
        grainDMELMEButton.setOnClickListener(this);
        dMELMEButton.setOnClickListener(this);

        // Enable full screen rotation.
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        // Hide keyboard until keyboard is used for input.
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    // Exit this app
    @Override
    public void onPause() {
        super.onPause();
    }

    // Back to this app
    @Override
    public void onResume() {
        super.onResume();
    }

    // Click one button on page, and redirect to respect pages.
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alcohol_by_volume_button:
                Intent aBVIntent = new Intent(getApplicationContext(),ABVConvertorActivity.class);
                startActivity(aBVIntent);
                break;

            case R.id.grain_dme_lme_button:
                Intent gDLIntent = new Intent(getApplicationContext(),GDLConvertorActivity.class);
                startActivity(gDLIntent);
                break;

            case R.id.dme_lme_button:
                Intent dLIntent = new Intent(getApplicationContext(),DLConvertorActivity.class);
                startActivity(dLIntent);
                break;
        }
    }

    /**
     * This method create menu.
     * Menu UI code is in res -> menu -> menu.xml.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * This method handle user's click on a specific item.
     * Redirect to corresponding activity once one item clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.receipt_item:
                intent = new Intent(getApplicationContext(), ViewActivity.class);
                startActivity(intent);
                break;

            case R.id.timer_item:
                intent = new Intent(getApplicationContext(), ConvertorMainActivity.class);
                startActivity(intent);
                break;

            case R.id.convertor_item:
                intent = new Intent(getApplicationContext(), ConvertorMainActivity.class);
                startActivity(intent);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}