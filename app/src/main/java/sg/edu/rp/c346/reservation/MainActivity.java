package sg.edu.rp.c346.reservation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {

    EditText etName, etTelephone, etSize, etDay, etTime;
    CheckBox checkBox;
    Button btReserve, btReset;

    Calendar cal = Calendar.getInstance();
    int curDay = cal.get(Calendar.DAY_OF_MONTH);
    int curMonth = cal.get(Calendar.MONTH);
    int curYear = cal.get(Calendar.YEAR);

    int curHour = cal.get(Calendar.HOUR_OF_DAY);
    int curMin = cal.get(Calendar.MINUTE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.editTextName);
        etTelephone = findViewById(R.id.editTextTelephone);
        etSize = findViewById(R.id.editTextSize);
        checkBox = findViewById(R.id.checkBox);

        etDay = findViewById(R.id.editTextDay);
        etTime = findViewById(R.id.editTextTime);

        btReserve = findViewById(R.id.buttonReserve);
        btReset = findViewById(R.id.buttonReset);

        etDay.setText(String.format("%02d/%02d/%04d", curDay, (curMonth + 1), curYear));
        etTime.setText(String.format("%02d : %02d", curHour, curMin));

        etDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog myDateDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        etDay.setText(String.format("%02d/%02d/%04d", dayOfMonth, (monthOfYear + 1), year));
                    }
                }, curYear, curMonth, curDay);

                //show date picker dialog
                myDateDialog.show();
            }
        });

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int curHour = cal.get(Calendar.HOUR_OF_DAY);
                int curMin = cal.get(Calendar.MINUTE);

                TimePickerDialog myTimeDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        etTime.setText(String.format("%02d : %02d", hourOfDay, minute));
                    }
                }, curHour, curMin, true);

                //show time picker dialog
                myTimeDialog.show();
            }
        });

        btReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String isSmoke = "";
                if (checkBox.isChecked()) {
                    isSmoke = "smoking";
                }
                else {
                    isSmoke = "non-smoking";
                }

                //build dialog
                AlertDialog.Builder myBuilder = new AlertDialog.Builder(MainActivity.this);

                //set characteristics
                myBuilder.setTitle("Demo 2 buttons Dialog")
                        .setMessage("New Reservation\n" +
                                "Name: " + etName.getText().toString() + "\n" +
                                "Size: " + etSize.getText().toString() + "\n" +
                                "Smoking: " + isSmoke + "\n" +
                                "Date: " + String.format("%02d/%02d/%04d", curDay, (curMonth + 1), curYear) + "\n" +
                                "Time: " + String.format("%02d : %02d", curHour, curMin))
                        .setCancelable(false)
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MainActivity.this, "Reservation Confirmed", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MainActivity.this, "Reservation Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        });

                //show dialog
                AlertDialog myDialog  = myBuilder.create();
                myDialog.show();
            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.getText().clear();
                etTelephone.getText().clear();
                etSize.getText().clear();
                checkBox.setChecked(false);
                //set to current date and time
                Calendar cal = Calendar.getInstance();
                curDay = cal.get(Calendar.DAY_OF_MONTH);
                curMonth = cal.get(Calendar.MONTH);
                curYear = cal.get(Calendar.YEAR);
                curHour = cal.get(Calendar.HOUR_OF_DAY);
                curMin = cal.get(Calendar.MINUTE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        String name = sp.getString("name","");
        String tel = sp.getString("tel", "");
        String size = sp.getString("size","");
        boolean smoking = sp.getBoolean("smoking", false);
        String day = sp.getString("day", String.format("%02d/%02d/%04d", curDay, (curMonth + 1), curYear));
        String time = sp.getString("time", String.format("%02d : %02d", curHour, curMin));

        etName.setText(name);
        etTelephone.setText(tel);
        etSize.setText(size);
        checkBox.setChecked(smoking);
        etDay.setText(day);
        etTime.setText(time);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEdit = sp.edit();

        //store
        prefEdit.putString("name", etName.getText().toString());
        prefEdit.putString("tel", etTelephone.getText().toString());
        prefEdit.putString("size", etSize.getText().toString());
        prefEdit.putBoolean("smoking", checkBox.isChecked());
        prefEdit.putString("day", String.format("%02d/%02d/%04d", curDay, (curMonth + 1), curYear));
        prefEdit.putString("time", String.format("%02d : %02d", curHour, curMin));

        prefEdit.commit();
    }
}