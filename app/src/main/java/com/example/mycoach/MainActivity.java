package com.example.mycoach;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mycoach.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.concurrent.TimeUnit;

import static com.example.mycoach.R.id;
import static com.example.mycoach.R.layout;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn, btnRegister;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference users;
    RelativeLayout root;
    String sex;
    CheckBox rememberMe;
    MaterialEditText log, password;
    SharedPreferences sPref1, sPref2;
    final String savedLog = "SAVED_LOG";
    final String savedPass = "SAVED_PASS";
    public static final String BOOL_CHECKBOX = "checkboxset";
    public SharedPreferences mSet;
    public static final String NAME_PREFERENCES = "mysetting";
    String currentName, currentPhone, currentEmail, currentAge, currentSex, currentPassword, currentExp;
    String codeSent;
    String exp;
    String code;
    boolean check;
    boolean defCheck=false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        btnSignIn = findViewById(id.btnSignIn);
        btnRegister = findViewById(id.btnRegister);
        rememberMe = findViewById(id.rememberMe);
        log = findViewById(id.login);
        password = findViewById(id.passIn);

        Intent intent = getIntent();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        root = findViewById(id.rootEl);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowRegWindow();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });

        mSet = getSharedPreferences(NAME_PREFERENCES, Context.MODE_PRIVATE);
        if(mSet.contains(BOOL_CHECKBOX)){
            rememberMe.setChecked(mSet.getBoolean(BOOL_CHECKBOX, false));
        }

        if(rememberMe.isChecked()){
            LoadLog();
            LoadPass();
        }



        check = intent.getBooleanExtra("a",false);
        if(rememberMe.isChecked()){
            if(check == false){
                SignIn();
            }
        }

    }

    private void SignIn() {
        final MaterialEditText login = findViewById(id.login);
        final MaterialEditText pass = findViewById(id.passIn);



        if (TextUtils.isEmpty(login.getText().toString())){
            Snackbar.make(root,"Введите логин",Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(pass.getText().toString())){
            Snackbar.make(root,"Введите пароль",Snackbar.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(login.getText().toString(),pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                startActivity(new Intent(MainActivity.this, MapActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               Snackbar.make(root, "Ошибка авторизации. "+e.getMessage(),Snackbar.LENGTH_SHORT).show();
            }
        });



    }

    private void SaveLog(){
        sPref1 = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed1 = sPref1.edit();
        ed1.putString(savedLog, log.getText().toString());
        ed1.commit();
    }

    private void LoadLog(){
        sPref1 = getPreferences(MODE_PRIVATE);
        String savedLg = sPref1.getString(savedLog, "");
        log.setText(savedLg);

    }

    private void SavePass(){
        sPref2 = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed2 = sPref2.edit();
        ed2.putString(savedPass, password.getText().toString());
        ed2.commit();
    }

    private void LoadPass(){
        sPref2 = getPreferences(MODE_PRIVATE);
        String savedPS = sPref2.getString(savedPass, "");
        password.setText(savedPS);
    }

    private void ShowRegWindow() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Зарегестрироваться");
        dialog.setMessage("Выберите способ регистрации и введите данные");

        final LayoutInflater inflater = LayoutInflater.from(this);
        View regWindow = inflater.inflate(layout.register_window, null);
        dialog.setView(regWindow);
        final MaterialEditText email = regWindow.findViewById(id.emailField);
        final MaterialEditText pass = regWindow.findViewById(id.passField);
        final MaterialEditText name = regWindow.findViewById(id.nameField);
        final MaterialEditText phone = regWindow.findViewById(id.phoneField);
        final MaterialEditText age = regWindow.findViewById(id.age);
        final RadioButton checkEmail = regWindow.findViewById(id.checkEmail);
        final RadioButton checkPhone = regWindow.findViewById(id.checkPhoneNum);
        final RadioButton checkBeginner = regWindow.findViewById(id.beginner);
        final RadioButton checkIntermediate = regWindow.findViewById(id.intermediate);
        final RadioButton checkExpert = regWindow.findViewById(id.expert);



        final RadioGroup rgSex = regWindow.findViewById(id.rgSex);
        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });


        dialog.setPositiveButton("Зарегистрироваться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (checkEmail.isChecked()){
                    email.setHint("Необязательно");

                }

                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(name.getText().toString())) {
                    Snackbar.make(root, "Введите ваше имя", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if(checkPhone.isChecked()){
                    phone.setHint("Необязательно");
                }

                if (TextUtils.isEmpty(phone.getText().toString())) {
                    Snackbar.make(root, "Введите ваш телефон", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (pass.getText().toString().length() < 5) {
                    Snackbar.make(root, "Введите пароль, который больше 5 символов", Snackbar.LENGTH_SHORT).show();
                    return;
                }

               /* experience.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case id.beginner:
                                exp = "beginner";
                                break;
                            case id.intermediate:
                                exp = "intermediate";
                                break;
                            case id.expert:
                                exp = "expert";
                                break;
                        }
                    }
                });*/

               if(checkBeginner.isChecked()){
                   exp = "beginner";
               }else if(checkIntermediate.isChecked()){
                   exp = "intermediate";
               }else if(checkExpert.isChecked()){
                   exp = "expert";
               }else {
                   Snackbar.make(root, "Укажите ваш опыт", Snackbar.LENGTH_LONG).show();
               }

                rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(checkedId == id.male){
                            sex = "male";
                        }if(checkedId == id.female){
                            sex="female";
                        }else{
                            Snackbar.make(root, "Выберите ваш пол", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });


                if(checkBeginner.isChecked()==false && checkIntermediate.isChecked()==false && checkExpert.isChecked()==false){
                    Snackbar.make(root, "Выберите ваш опыт занятий", Snackbar.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(age.getText().toString())) {
                    Snackbar.make(root, "Введите ваш возраст", Snackbar.LENGTH_SHORT).show();
                    return;
                } else if (Integer.parseInt(age.getText().toString()) < 0 || Integer.parseInt(age.getText().toString()) > 80) {
                    Snackbar.make(root, "Введите ваш настоящий возраст", Snackbar.LENGTH_SHORT).show();
                    return;
                }



                if (checkEmail.isChecked()) {
                    //Регистрация по email
                    auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            User user = new User();
                            user.setEmail(email.getText().toString());
                            user.setPhone(phone.getText().toString());
                            user.setAge(age.getText().toString());
                            user.setName(name.getText().toString());
                            user.setSex(sex);
                            user.setPass(pass.getText().toString());
                            user.setExperience(exp);
                           // Snackbar.make(root, exp, Snackbar.LENGTH_LONG).show();
                            users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Snackbar.make(root, "Пользователь добавлен", Snackbar.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(root, "Ошибка! ", Snackbar.LENGTH_LONG).show();
                        }
                    });
                }else if(checkEmail.isChecked()==false && checkPhone.isChecked()==false){
                    Snackbar.make(root, "Выберите способ регистрации", Snackbar.LENGTH_SHORT).show();
                }else{
                    //Snackbar.make(root, "Еще не реализовано", Snackbar.LENGTH_SHORT).show();
                    currentEmail = email.getText().toString();
                    currentPhone = phone.getText().toString();
                    currentAge = age.getText().toString();
                    currentName = name.getText().toString();
                    currentPassword = pass.getText().toString();
                    currentSex = sex;
                    currentExp = exp;
                    SmsVerify();
                    SendVerificationCode();

                }
            }

        });

        dialog.show();


    }

    private void SendVerificationCode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                currentPhone,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Snackbar.make(root, "Uspeh", Snackbar.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Snackbar.make(root, "Proval", Snackbar.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
        }
    };

    private void SmsVerify() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Введите код из СМС.");

        final LayoutInflater inflater = LayoutInflater.from(this);
        View smsVerifyWindow = inflater.inflate(layout.sms_verify, null);
        dialog.setView(smsVerifyWindow);

        //final MaterialEditText sms = findViewById(R.id.sms);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        //здесь жопа
        dialog.setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               VerifyCode();
               // Snackbar.make(root, "otpr", Snackbar.LENGTH_SHORT).show();
               // code = "a+b";
               // Toast.makeText(getApplicationContext(), code.getText().toString(), Toast.LENGTH_LONG).show();
               /*EditText txtCode = findViewById(id.txtCode);*/

              // code = txtCode.getText().toString();
               /*if(code == null){
                   Snackbar.make(root, "cool", Snackbar.LENGTH_SHORT).show();
               }else{
                   Snackbar.make(root, "bad", Snackbar.LENGTH_SHORT).show();
               }*/

            }
        });

        dialog.show();
    }

   public void VerifyCode() {
        final EditText code = findViewById(id.txtCode);
        Toast.makeText(getApplicationContext(),code.getText(),Toast.LENGTH_LONG).show();
   }
        /*MaterialEditText txtCode = findViewById(R.id.code);
        String code = txtCode.getText().toString();
       Snackbar.make(root, "somethig", Snackbar.LENGTH_SHORT).show();*/
       // PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);

        //signInWithPhoneAuthCredential(credential);
      //* String s = credential.getSmsCode();*//*

       /* if(code == codeSent){
            Snackbar.make(root, "kruto", Snackbar.LENGTH_SHORT).show();
        }else {
            Snackbar.make(root, "neud", Snackbar.LENGTH_SHORT).show();
        }*/

   // }

 /* private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                        //FirebaseUser user = task.getResult().getUser();


                           auth.createUserWithEmailAndPassword(currentEmail, currentPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    User user = new User();
                                    user.setEmail(currentEmail);
                                    user.setPhone(currentPhone);
                                    user.setAge(currentAge);
                                    user.setName(currentName);
                                    user.setSex(currentSex);
                                    user.setPass(currentPassword);

                                    users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Snackbar.make(root, "Пользователь добавлен", Snackbar.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            });

                        } else {
                            Snackbar.make(root, "Неправильный код", Snackbar.LENGTH_SHORT);
                        }
                    }
                    });
                }*/





    @Override
    protected void onStop() {
        super.onStop();
        mSet = getSharedPreferences(NAME_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSet.edit();
        editor.putBoolean(BOOL_CHECKBOX, rememberMe.isChecked());
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SavePass();
        SaveLog();
    }


}
