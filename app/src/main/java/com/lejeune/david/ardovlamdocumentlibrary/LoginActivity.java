package com.lejeune.david.ardovlamdocumentlibrary;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LoginActivity extends Activity {

    //region Declarations
    public EditText txtPassword, txtFirstName, txtLastName;
    public CheckBox checkBoxPassword;
    public static Boolean loginOK = false;
    private Boolean savedSharedPref = false;
    private Boolean userValidity = false;
    private Boolean foundUser = false;
    private Boolean privilegedUser = false;

    private String firstName, lastName, birthDate;

    public static String connectionType ;
    private MyTools myTools= null;
    private Context cntx;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //region Initialisations
        cntx = getApplicationContext();
        myTools = new MyTools();
        //endregion

        //region UI elements
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);

        ImageView imgProfile = (ImageView) findViewById(R.id.imgProfileLogin);
        imgProfile.setVisibility(View.VISIBLE);


        //set password field to password type
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        //endregion

        //region Misc actions
        myTools.retrieveSharedPref(cntx);
        displaySharedPref();
        new AsyncRemoveOldFilesDL().execute();
        //endregion

        //region Determining profile image
        if (MyVars.registereduser.equalsIgnoreCase("1"))
        {
            setProfileImg();
        }
        else
        {
            setDefaultProfileImg();
        }
        //endregion

        //region Login button
        final ImageView buttonLogin = (ImageView) findViewById(R.id.imgLogin);
        buttonLogin.setVisibility(View.VISIBLE);
        buttonLogin.setImageDrawable(getResources().getDrawable(R.drawable.login_button));
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(LoginActivity.this,"Verifying user\nPlease wait ...",Toast.LENGTH_SHORT).show();
                loginProcedure();

            }
        });
        //endregion

    }

    //region Misc methods
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        txtPassword = (EditText) findViewById(R.id.txtPassword);


        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBoxPassword:
                if (checked) {
                    txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else {
                    txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                }

                //place cursor at end of the edit text
                int textLength = txtPassword.getText().length();
                txtPassword.setSelection(textLength, textLength);

                break;

        }
    }

    private void gotoMenu(){
        for (int i = 0; i < 100; i++) {
            MyStats.logEntry("ENTRY", "");
        }
        final Intent menuIntent = new Intent(LoginActivity.this, MenuActivity.class);
        LoginActivity.this.startActivity(menuIntent);
        LoginActivity.this.finish();
    }

    private void restartApp(){
        Intent mStartActivity = new Intent(cntx, MainActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(cntx, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)cntx.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 300, mPendingIntent);
        System.exit(0);
    }

    private void displaySharedPref(){

        txtFirstName.setText(MyVars.firstname);
        txtLastName.setText(MyVars.lastname);
        txtPassword.setText(MyVars.birthdate);

    }
    //endregion

    //region Profile image
    private void setDefaultProfileImg(){
        ImageView imgProfile = (ImageView) findViewById(R.id.imgProfileLogin);
        imgProfile.setImageDrawable(getResources().getDrawable(R.drawable.avatar_1));
    }
    private void setProfileImg() {
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, "/DocLib/IMG/profile.jpg");
        long length = file.length();

        if (length > 1) {
            if (file.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                ImageView imgProfile = (ImageView) findViewById(R.id.imgProfileLogin);
                imgProfile.setVisibility(View.VISIBLE);
                imgProfile.setImageBitmap(myBitmap);
            }
        }
        else
        {
            setDefaultProfileImg();
        }
    }
    //endregion

    //region Login procedure
    public void loginProcedure(){
        loginOK = false;

        if (txtFirstName.getText().toString().equalsIgnoreCase(MyVars.firstname) &&
                txtLastName.getText().toString().equalsIgnoreCase(MyVars.lastname) &&
                txtPassword.getText().toString().equalsIgnoreCase(MyVars.birthdate) &&
                MyVars.registereduser.equalsIgnoreCase("1"))
        {
            loginOK = true;
            gotoMenu();
        }
        else
        {
            SharedPrefHelper.getInstance().save(LoginActivity.this,"0","registered");
            SharedPrefHelper.getInstance().save(LoginActivity.this,"0","usertype");
            saveUserInfoToSharedpref();
            if (savedSharedPref)
            {
                connectionType = MyTools.checkNetworkStatus(cntx);
                if (connectionType.equalsIgnoreCase("wifi")){
                    checkUserValidity();
                    if (userValidity)
                    {
                        checkUserType();
                            loginOK = true;

                                    AsyncDownloadUserImg asyncImg = new AsyncDownloadUserImg();
                                    asyncImg.execute();


                    }
                    if(loginOK)
                    {

                    }
                    else
                    {
                        setDefaultProfileImg();
                        Toast.makeText(LoginActivity.this, "Failed to login , check your credentials",Toast.LENGTH_LONG).show();
                        resetSharedPref();
                    }

                }
                else
                {
                    Toast.makeText(LoginActivity.this, "You need to be online to verify a new user",Toast.LENGTH_LONG).show();
                }

            }

        }



    }

    private void resetSharedPref(){
        SharedPrefHelper.getInstance().save(LoginActivity.this,"0","updatendoclocal");
        SharedPrefHelper.getInstance().save(LoginActivity.this,"0","updatencomlocal");
        SharedPrefHelper.getInstance().save(LoginActivity.this,"0","updatenteclocal");
    }

    private void saveUserInfoToSharedpref(){

        savedSharedPref = false;

        firstName = txtFirstName.getText().toString();
        lastName = txtLastName.getText().toString();
        birthDate = txtPassword.getText().toString();

        if (firstName.length() < 1)
        {
            Toast.makeText(this, "Please Enter firstName!",
                    Toast.LENGTH_LONG).show();
        }
        else if (lastName.length() < 1)
        {
            Toast.makeText(this, "Please Enter lastName!",
                    Toast.LENGTH_LONG).show();
        }
        else if (birthDate.length() < 1)
        {
            Toast.makeText(this, "Please Enter birthDate!",
                    Toast.LENGTH_LONG).show();
        }
        else
        {
            SharedPreferences sharedPref = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("firstname", firstName);
            editor.putString("lastname", lastName);
            editor.putString("date", birthDate);
            editor.putString("registered", "0");
            editor.putString("usertype", "0");
            editor.apply();
            savedSharedPref = true;
        }
    }

    private void checkUserValidity(){


        myTools.retrieveSharedPref(cntx);



        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir,MyVars.FOLDER_DATA + "Users.csv");
        if (file.exists())
        {
            StringBuilder text = new StringBuilder();
            try
            {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line ;
                while ((line = br.readLine()) != null )
                {
                    String[] str ;

                    String id ="";
                    String firstName="" ;
                    String lastName ="";
                    String date="" ;
                    String type="" ;
                    if (line.length()>2){

                        str = line.split(",");

                        id = str[0].toUpperCase();
                        firstName = str[1].toUpperCase();
                        lastName = str[2].toUpperCase();
                        date = str[3].toUpperCase();
                        type = str[4].toUpperCase();
                    }

                    int countCorrect = 0;

                    if(MyVars.firstname.equalsIgnoreCase(firstName))
                    {
                        countCorrect += 1;
                    }
                    else
                    {
                    }

                    if(MyVars.lastname.equalsIgnoreCase(lastName))
                    {
                        countCorrect += 1;
                    }
                    else
                    {
                    }

                    if(MyVars.birthdate.equalsIgnoreCase(date))
                    {
                        countCorrect += 1;
                    }
                    else
                    {
                    }

                    if(countCorrect == 3)
                    {
                        SharedPrefHelper.getInstance().save(LoginActivity.this,"1","registered");
                        SharedPrefHelper.getInstance().save(LoginActivity.this,type,"usertype");
                        SharedPrefHelper.getInstance().save(LoginActivity.this,"0","updatedoclocal");
                        SharedPrefHelper.getInstance().save(LoginActivity.this,"0","updatecomlocal");
                        SharedPrefHelper.getInstance().save(LoginActivity.this,"0","updateteclocal");
                        foundUser = true;
                        userValidity = true;
                    }

                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
        }

        if(foundUser) {
            Toast.makeText(this,"User found !!! and registered", Toast.LENGTH_LONG).show();
        }
        else
        {
        }

    }

    private void checkUserType(){


        myTools.retrieveSharedPref(cntx);
        if(MyVars.usertype.equalsIgnoreCase("1")){
            privilegedUser = true;
        }

        if (privilegedUser) {
            myTools.retrieveSharedPref(cntx);
        }
    }
    //endregion

    //region Internal Async Tasks
    class AsyncDownloadUserImg extends AsyncTask<Void, Void, Void> {

        private FTPfunctions ftpclient = null;
        ProgressDialog pd;

        @Override
        protected Void doInBackground(Void... params) {

            ftpclient = new FTPfunctions();
            final String strFile = "/DocLib/IMG/" + MyVars.fullname + ".jpg";
                    boolean status = false;

                    ftpclient.ftpConnect(MyVars.HOST, MyVars.USERNAME, MyVars.PASSWORD, 21);

                    ftpclient.ftpDownload(strFile, Environment.getExternalStorageDirectory()
                            + "/DocLib/IMG/profile.jpg");

            ftpclient.ftpDisconnect();
            return null;
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(LoginActivity.this);
            pd.setTitle("Downloading user image ...");
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setMax(100);
            pd.setProgress(0);
            pd.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.setProgress(100);
            pd.dismiss();

            File dir = Environment.getExternalStorageDirectory();
            File file = new File(dir,"/DocLib/IMG/profile.jpg");
            long length = file.length();
            setProfileImg();
            if (LoginActivity.loginOK)
            {
                restartApp();
            }
            else
            {
                Toast.makeText(LoginActivity.this, "loginProcedure NOK", Toast.LENGTH_SHORT).show();
            }



        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    class AsyncLoginProcedureDL extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            loginProcedure();
            return null;
        }






    }
    //endregion

}