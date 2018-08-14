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

/**
 * This class calculates the conversion between LME and DME.
 *
 * @author Sicheng Zhu
 */
public class DLConvertorActivity extends Activity {
    private EditText lMEAmountET;
    private EditText dMEAmountET;
    private Intent intent;
    private StringBuffer strBuffer = new StringBuffer();
    private SharedPreferences dLSavedValues;
    private SharedPreferences.Editor editor;
    private String dMEAmountStr, lMEAmountStr; // Used for SharedPreferences purpose

    /**
     * The isChanged variable below is required for user input listening to avoid setText() method,
     * in onTextChanged(), is called infinitely and throw stackoverflow exception.
     */
    private boolean isChanged = false;

    // Add components to UI once this activity is loaded.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dl_convertor_layout);
        lMEAmountET = (EditText)findViewById(R.id.lMEAmountET);
        dMEAmountET = (EditText)findViewById(R.id.dMEAmountET);

        /**
         * The two lines below ensure only valid decimal number is input by user, and ignore
         * any input other than 0 - 9 and '.'.
         *
         * Also these codes allow input without leading 0 (i.e. .98).
         */
        lMEAmountET.setInputType(0x00002002);
        dMEAmountET.setInputType(0x00002002);

        /**
         * The code below is for LME textfield listener. When user input LME pound amount,
         * clear DME textfield, call convertAmongGDL() method to do the conversion,
         * and display results on DME textfield.
         */
        lMEAmountET.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s,int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isChanged) {
                    return;
                }

                isChanged = true;
                dMEAmountET.getText().clear();

                if (s.length() != 0) {
                    checkLeadingZero(s);
                    dMEAmountET.setText(convertBetweenDL(strBuffer, 'l'));
                }

                isChanged = false;
                strBuffer.delete(0, strBuffer.length());
            }
        });

        /**
         * The code below is for DME textfield listener. When user input DME pound amount,
         * clear LME textfield, call convertAmongGDL() method to do the conversion,
         * and display results on LME textfield.
         */
        dMEAmountET.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s,int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isChanged) {
                    return;
                }

                isChanged = true;
                lMEAmountET.getText().clear();

                if (s.length() != 0) {
                    checkLeadingZero(s);
                    lMEAmountET.setText(convertBetweenDL(strBuffer, 'd'));
                }

                isChanged = false;
                strBuffer.delete(0, strBuffer.length());
            }
        });

        dLSavedValues = getSharedPreferences("dLSavedValues", MODE_PRIVATE);
        editor = dLSavedValues.edit();

        // Enable full screen rotation.
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        // Hide keyboard until keyboard is used for input.
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    // Add user's input when user exit this app.
    @Override
    public void onPause() {
        dMEAmountStr = dMEAmountET.getText().toString();
        lMEAmountStr = lMEAmountET.getText().toString();

        editor.putString("dMEAmountStr", dMEAmountStr);
        editor.putString("lMEAmountStr", lMEAmountStr);
        editor.commit();

        super.onPause();
    }

    // Retrieve user's input when user is back to this app.
    @Override
    public void onResume() {
        super.onResume();

        dMEAmountET.setText(dLSavedValues.getString("dMEAmountStr", ""));
        lMEAmountET.setText(dLSavedValues.getString("lMEAmountStr", ""));
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
     * @param s  user's input
     */
    private void checkLeadingZero(CharSequence s) {
        if (s.charAt(0) == '.') {
            strBuffer.append(0);
        }

        strBuffer.append(s);
    }

    /**
     * Do conversion between DME and LME. This method is called in onCreate() method.
     *
     * For convertFrom, 'd' means DME, and 'l' means LME.
     *
     * @param strBuffer    User's input in StringBuffer type.
     * @param convertFrom  Convert from which unit
     * @return              Converted value in string representation.
     */
    private String convertBetweenDL(StringBuffer strBuffer, char convertFrom) {
        double inputAmt = Double.valueOf(strBuffer.toString());

        if (convertFrom == 'd') {
            return Double.toString(inputAmt * 0.84);

        } else if (convertFrom == 'l') {
            return Double.toString(inputAmt * 1.19);
        }

        return "";
    }
}