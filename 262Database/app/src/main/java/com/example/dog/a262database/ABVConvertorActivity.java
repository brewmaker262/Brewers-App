package com.example.dog.a262database;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * This class calculates ABV.
 *
 * @author Sicheng Zhu
 */
public class ABVConvertorActivity extends Activity {
    private EditText originalAmountET;
    private EditText finalAmountET;
    private TextView abvPercentTV;
    private Intent intent;
    private StringBuffer oriStrBuffer = new StringBuffer();
    private StringBuffer finStrBuffer = new StringBuffer();
    private StringBuffer resStrBuffer = new StringBuffer();
    private SharedPreferences aBVSavedValues;
    private SharedPreferences.Editor editor;
    private String originalAmountStr, finalAmountStr; // Used for SharedPreferences purpose

    /**
     * The isChanged variable below is required for user input listening to avoid setText() method,
     * in onTextChanged(), is called infinitely and throw stackoverflow exception.
     */
    private boolean isChanged = false;

    // Add components to UI once this activity is loaded.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abv_convertor_layout);
        originalAmountET = (EditText)findViewById(R.id.originalAmountET);
        finalAmountET = (EditText)findViewById(R.id.finalAmountET);
        abvPercentTV = (TextView)findViewById(R.id.abvPercentTV);

        /**
         * The two lines below ensure only valid decimal number is input by user, and ignore
         * any input other than 0 - 9 and '.'.
         *
         * Also these codes allow input without leading 0 (i.e. .98).
         */
        originalAmountET.setInputType(0x00002002);
        finalAmountET.setInputType(0x00002002);

        /**
         * The code below is for original gravity textfield listener. When user input original
         * gravity pound amount, call checkLeadingZero() to add leading zero if necessary, and
         * then call calculateABV() method to do the calculation,and display results on abvPercentTV
         * textview.
         */
        originalAmountET.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s,int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isChanged) {
                    return;
                }

                isChanged = true;

                if (s.length() != 0) {
                    oriStrBuffer.delete(0, oriStrBuffer.length());
                    checkLeadingZero('o', s);
                    calculateABV();
                }

                isChanged = false;
            }
        });

        /**
         * The code below is for final gravity textfield listener. When user input final
         * gravity pound amount, call checkLeadingZero() to add leading zero if necessary, and
         * then call calculateABV() method to do the calculation,and display results on abvPercentTV
         * textview.
         */
        finalAmountET.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s,int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isChanged) {
                    return;
                }

                isChanged = true;

                if (s.length() != 0) {
                    finStrBuffer.delete(0, finStrBuffer.length());
                    checkLeadingZero('f', s);
                    calculateABV();
                }

                isChanged = false;
            }
        });

        aBVSavedValues = getSharedPreferences("aBVSavedValues", MODE_PRIVATE);
        editor = aBVSavedValues.edit();

        // Enable full screen rotation.
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        // Hide keyboard until keyboard is used for input.
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    // Add user's input when user exit this app.
    @Override
    public void onPause() {
        originalAmountStr = originalAmountET.getText().toString();
        finalAmountStr = finalAmountET.getText().toString();

        editor.putString("originalAmountStr", originalAmountStr);
        editor.putString("finalAmountStr", finalAmountStr);
        editor.commit();

        super.onPause();
    }

    // Retrieve user's input when user is back to this app.
    @Override
    public void onResume() {
        super.onResume();

        originalAmountET.setText(aBVSavedValues.getString("originalAmountStr", ""));
        finalAmountET.setText(aBVSavedValues.getString("finalAmountStr", ""));
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

    /**
     * This checkLeadingZero method transfer user input from CharSequence to StringBuffer type.
     * This method is called in onCreate() method.
     *
     * If user input format is like ".99", add a leading 0.
     *
     * For type argument, 'o' means original, and 'f' means final.
     *
     * @param type  Convert from type
     * @param s     user's input
     */
    private void checkLeadingZero(char type, CharSequence s) {
        if (type == 'o') {
            if (s.charAt(0) == '.') {
                oriStrBuffer.append(0);
            }

            oriStrBuffer.append(s);
        } else if (type == 'f') {
            if (s.charAt(0) == '.') {
                finStrBuffer.append(0);
            }

            finStrBuffer.append(s);
        }
    }

    /**
     * Do ABV calculation. This method is called in onCreate() method.
     */
    private void calculateABV() {
        if (oriStrBuffer.length() != 0 && finStrBuffer.length() != 0) {
            resStrBuffer.delete(0, resStrBuffer.length());

            double oriAmt = Double.valueOf(oriStrBuffer.toString());
            double finAmt = Double.valueOf(finStrBuffer.toString());

            if ((finAmt - oriAmt)  * 131.25 < 0) {
                abvPercentTV.setText("0.0%");
                return;
            }

            // Keep two digits after decimal point.
            DecimalFormat df = new DecimalFormat("#.00");
            resStrBuffer.append(df.format((finAmt - oriAmt)  * 131.25));
            resStrBuffer.append("%");

            abvPercentTV.setText(resStrBuffer.toString());
        } else {
            abvPercentTV.setText("0.0%");
        }
    }
}
