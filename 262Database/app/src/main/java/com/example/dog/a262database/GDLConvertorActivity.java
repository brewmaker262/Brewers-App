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
 * This class calculates the conversion between grain, LME and DME.
 *
 * @author Sicheng Zhu
 */
public class GDLConvertorActivity extends Activity {
    private EditText grainAmtET;
    private EditText lMEAmtET;
    private EditText dMEAmtET;
    private Intent intent;
    private StringBuffer strBuffer = new StringBuffer();
    private SharedPreferences gDLSavedValues;
    private SharedPreferences.Editor editor;
    private String grainAmtStr, dMEAmtStr, lMEAmtStr; // Used for SharedPreferences purpose

    /**
     * The isChanged variable below is required for user input listening to avoid setText() method,
     * in onTextChanged(), is called infinitely and throw stackoverflow exception.
     */
    private boolean isChanged = false;

    // Add components to UI once this activity is loaded.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gdl_convertor_layout);
        grainAmtET = (EditText)findViewById(R.id.grainAmountET);
        lMEAmtET = (EditText)findViewById(R.id.lMEAmountET);
        dMEAmtET = (EditText)findViewById(R.id.dMEAmountET);

        /**
         * The three lines below ensure only valid decimal number is input by user, and ignore
         * any input other than 0 - 9 and '.'.
         *
         * Also these codes allow input without leading 0 (i.e. .98).
         */
        grainAmtET.setInputType(0x00002002);
        lMEAmtET.setInputType(0x00002002);
        dMEAmtET.setInputType(0x00002002);

        /**
         * The code below is for grain textfield listener. When user input grain pound amount,
         * clear DME and LME textfields, call convertAmongGDL() method to do the conversion,
         * and display results on DME and LME textfields.
         */
        grainAmtET.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s,int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isChanged) {
                    return;
                }

                isChanged = true;
                lMEAmtET.getText().clear();
                dMEAmtET.getText().clear();

                if (s.length() != 0) {
                    checkLeadingZero(s);
                    lMEAmtET.setText(convertAmongGDL(strBuffer, 'g','l'));
                    dMEAmtET.setText(convertAmongGDL(strBuffer, 'g','d'));
                }

                isChanged = false;
                strBuffer.delete(0, strBuffer.length());
            }
        });

        /**
         * The code below is for LME textfield listener. When user input LME pound amount,
         * clear DME and grain textfields, call convertAmongGDL() method to do the conversion,
         * and display results on DME and grain textfields.
         */
        lMEAmtET.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s,int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isChanged) {
                    return;
                }

                isChanged = true;
                grainAmtET.getText().clear();
                dMEAmtET.getText().clear();

                if (s.length() != 0) {
                    checkLeadingZero(s);
                    grainAmtET.setText(convertAmongGDL(strBuffer, 'l','g'));
                    dMEAmtET.setText(convertAmongGDL(strBuffer, 'l','d'));
                }

                isChanged = false;
                strBuffer.delete(0, strBuffer.length());
            }
        });

        /**
         * The code below is for DME textfield listener. When user input DME pound amount,
         * clear grain and LME textfields, call convertAmongGDL() method to do the conversion,
         * and display results on grain and LME textfields.
         */
        dMEAmtET.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s,int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isChanged) {
                    return;
                }

                isChanged = true;
                grainAmtET.getText().clear();
                lMEAmtET.getText().clear();

                if (s.length() != 0) {
                    checkLeadingZero(s);
                    grainAmtET.setText(convertAmongGDL(strBuffer, 'd','g'));
                    lMEAmtET.setText(convertAmongGDL(strBuffer, 'd','l'));
                }

                isChanged = false;
                strBuffer.delete(0, strBuffer.length());
            }
        });

        gDLSavedValues = getSharedPreferences("GDLSavedValues", MODE_PRIVATE);
        editor = gDLSavedValues.edit();

        // Enable full screen rotation.
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        // Hide keyboard until keyboard is used for input.
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    // Add user's input when user exit this app.
    @Override
    public void onPause() {
        grainAmtStr = grainAmtET.getText().toString();
        dMEAmtStr = dMEAmtET.getText().toString();
        lMEAmtStr = lMEAmtET.getText().toString();

        editor.putString("grainAmtStr", grainAmtStr);
        editor.putString("dMEAmtStr", dMEAmtStr);
        editor.putString("lMEAmtStr", lMEAmtStr);
        editor.commit();

        super.onPause();
    }

    // Retrieve user's input when user is back to this app.
    @Override
    public void onResume() {
        super.onResume();

        grainAmtET.setText(gDLSavedValues.getString("grainAmtStr", ""));
        dMEAmtET.setText(gDLSavedValues.getString("dMEAmtStr", ""));
        lMEAmtET.setText(gDLSavedValues.getString("lMEAmtStr", ""));
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
     * Do conversion among grain, DME and LME. This method is called in onCreate() method.
     *
     * For convertFrom, and convertTo, 'g' means grain, 'd' means DME, and 'l' means LME.
     *
     * @param strBuffer    User's input in StringBuffer type.
     * @param convertFrom  Convert from which unit
     * @param convertTo    Convert to which unit.
     * @return              Converted value in string representation.
     */
    private String convertAmongGDL(StringBuffer strBuffer, char convertFrom, char convertTo) {
        double inputAmt = Double.valueOf(strBuffer.toString());

        if (convertFrom == 'g') {
            if (convertTo == 'd') {
                return Double.toString(inputAmt * 0.6);
            } else if (convertTo == 'l') {
                return Double.toString(inputAmt * 0.75);
            } else {
                return "";
            }
        } else if (convertFrom == 'd') {
            if (convertTo == 'g') {
                return Double.toString(inputAmt * 5 / 3);
            } else if (convertTo == 'l') {
                return Double.toString(inputAmt * 1.25);
            } else {
                return "";
            }
        } else if (convertFrom == 'l') {
            if (convertTo == 'g') {
                return Double.toString(inputAmt * 4 / 3);
            } else if (convertTo == 'd') {
                return Double.toString(inputAmt * 0.8);
            } else {
                return "";
            }
        }

        return "";
    }
}
