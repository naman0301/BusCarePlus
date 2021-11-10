package ca.codingcomrades.it.buscareplus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MyAccount extends AppCompatActivity {
EditText firstName,lastName,phone,age,address,city,province,country;
Button save;
FirebaseAuth fAuth;
String cityName,uid;
    Map<String, Object> arr;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_account);
        LoginActivity log =new LoginActivity();
       bindFields();
        retriveUserData();
        save.setOnClickListener(v -> saveUserData());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public void bindFields(){
        save = findViewById(R.id.saveInfoBtn);
        firstName = findViewById(R.id.firstNameEditText);
        lastName = findViewById(R.id.lastNameEditText);
        phone = findViewById(R.id.phoneEditText);
        age = findViewById(R.id.ageEditText);
        address = findViewById(R.id.addressEditText);
        city = findViewById(R.id.cityEditText);
        province = findViewById(R.id.provinceEditText);
        country = findViewById(R.id.countryEditText);

    }
    public void retriveUserData(){
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        uid = fAuth.getUid();
        DocumentReference df = fStore.collection("Users").document(uid);
        df.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                arr = value.getData();
                Log.d("TAG", "onEvent: " + arr.get("LastName")+arr.values().toString() + "reg" +arr.values());
                setFields();

            }
        });

    }

    public void setFields(){
        firstName.setText(arr.get(getString(R.string.firstnameTitle)).toString());
        lastName.setText(arr.get(getString(R.string.last_nameTitle)).toString());
        phone.setText(arr.get(getString(R.string.phoneTitle)).toString());
        age.setText(arr.get(getString(R.string.ageTitle)).toString());
        address.setText(arr.get(getString(R.string.addressTitle)).toString());
        city.setText(arr.get(getString(R.string.cityTitle)).toString());
        province.setText(arr.get(getString(R.string.provinceTitle)).toString());
        country.setText(arr.get(getString(R.string.countryTitle)).toString());
    }
    public void saveUserData(){
        DocumentReference df = fStore.collection("Users").document(uid);
        Map<String,Object> userInfo = new HashMap<>();

        Log.d("TAG", "saveUserData: " +firstName.getText().toString());
        userInfo.put((getString(R.string.firstnameTitle)),firstName.getText().toString());
        userInfo.put(getString(R.string.last_nameTitle),lastName.getText().toString());
        userInfo.put(getString(R.string.phoneTitle), phone.getText().toString());
        userInfo.put(getString(R.string.ageTitle), age.getText().toString());
        userInfo.put(getString(R.string.addressTitle), address.getText().toString());
        userInfo.put(getString(R.string.cityTitle), city.getText().toString());
        userInfo.put(getString(R.string.provinceTitle), province.getText().toString());
        userInfo.put(getString(R.string.countryTitle), country.getText().toString());
        userInfo.put("isUser","1");
        df.set(userInfo);
        toastPrint("Information Saved!!");

    }
    public void toastPrint(String msg) {
        Log.d("TAG", "toastPrint: ");
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }
}