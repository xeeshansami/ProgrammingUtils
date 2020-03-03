package com.cloudchef.SystemActivities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.request.MultiPartRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.multipart.MultipartEntity;
import com.cloudchef.ChefActivities.PickUpAddress;
import com.cloudchef.R;
import com.cloudchef.ChefActivities.*;
import com.cloudchef.VolleyApiHelper.Config;
import com.cloudchef.VolleyApiHelper.CustomStringRequest;
import com.cloudchef.VolleyApiHelper.SessionManager;
import com.cloudchef.VolleyApiHelper.VolleySingleton;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

//TODO:Account
public class Account extends AppCompatActivity {
    static final int PICK_IMAGE_REQUEST = 1;
    private String filePath;
    String imagePath;
    Uri selectedImageUri;
    String selectedPath1, selectedPath2;
    private String sessionId;
    TextView textView;
    EditText editText1, editText2;
    private CircleImageView mImageView;
    private Button cp;
    private String CHEFID = null, chefEmail = null;
    FloatingActionButton fab;
    private static final int SELECT_FILE = 4;
    private UploadImage AsynTask = null;
    ImageButton imgName;
    EditText tvName, editTextTags;
    Button submit;
    Bitmap bitmap = null;
    int PERMISSION_ALL = 1;
    public static final int GET_FROM_GALLERY = 3;
    String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.BLACK);
        }
        tvName = findViewById(R.id.tvName);
        SharedPreferences sharedPreferences = getSharedPreferences(SessionManager.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        CHEFID = sharedPreferences.getString(Config.CHEFID, "Chef Name");
        chefEmail = sharedPreferences.getString(Config.KEY_EMAIL, "Chef Email");
        tvName.setText(CHEFID);
        tvName.setEnabled(false);
        textView = findViewById(R.id.tvemail);
        textView.setText(chefEmail);
        imgName = findViewById(R.id.imgEditName);
        mImageView = findViewById(R.id.imgProfile);
        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });
        cp = findViewById(R.id.Changepassword);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();

                filePath = getPath(selectedImage);
                String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);
                mImageView.setImageURI(Uri.parse(filePath));
                if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
                    //FINE
                } else {
                    //NOT IN REQUIRED FORMAT
                }
            }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
       int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        imagePath = cursor.getString(column_index);

        return cursor.getString(column_index);
    }

    public void gotoChangePassword(View view) {
        Intent intent = new Intent(Account.this, ChangePassword.class);
        startActivity(intent);
    }

    public void Verify_Account(View view) {
        uploadImage(filePath);
//        Intent intent = new Intent(Account.this, VerifyAccount.class);
//        startActivity(intent);
    }

    public void PickUpAddress(View view) {
        Intent intent = new Intent(Account.this, PickUpAddress.class);
        startActivity(intent);
    }

    public void DisplayAddress(View view) {
        Intent intent = new Intent(Account.this, PickUpAddress.class);
        startActivity(intent);
    }


    public void uploadImage(final String imagePath) {
        AsynTask = new UploadImage(imagePath);
        AsynTask.execute();
    }

    public class UploadImage extends AsyncTask<Void, Void, Boolean> {

        private ProgressDialog progressDialog;
        String filePath;

        public UploadImage(final String imagePath) {
            this.filePath = imagePath;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Account.this);
            progressDialog.setMessage("Uploading Image");
            progressDialog.setTitle("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost(Config.Chef_IMAGE_URL);
//
//            org.apache.http.entity.mime.MultipartEntity mpEntity = new org.apache.http.entity.mime.MultipartEntity (HttpMultipartMode.BROWSER_COMPATIBLE);
//            if (filePath != null) {
//                File file = new File(filePath);
//                Log.d("EDIT USER PROFILE", "UPLOAD: file length = " + file.length());
//                Log.d("EDIT USER PROFILE", "UPLOAD: file exist = " + file.exists());
//                mpEntity.addPart("avatar", new FileBody(file, "application/octet"));
//            }
//            httppost.setEntity(mpEntity);
//            try {
//                HttpResponse response = httpclient.execute(httppost);
//                Log.i("responseHttp",response+"");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
         SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, Config.Chef_IMAGE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response", response);
                            try {
                                JSONObject jObj = new JSONObject(response);
                                String message = jObj.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                // JSON error
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<>();
//                        SessionManager sessionManager = new SessionManager(getApplicationContext());
//                        String chefID = sessionManager.pref.getString(Config.CHEFID, null);
//                        //Adding parameters to request
//                        params.put("ChefId", chefID);
//                        params.put("Image", imgPath);
//                        //returning parameter
//                        return params;
//
//                    };
            smr.addFile("Image", filePath);
            VolleySingleton.getInstance(getApplicationContext()).addToRequestque(smr);

//
            return null;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }
//        public String imageToString(Bitmap bitmap) {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 1000, outputStream);
//        byte[] imageBytes = outputStream.toByteArray();
//        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//        return encodedImage;
//    }
//    private String getPath(Uri contentUri) {
//        String[] proj = {MediaStore.Images.Media.DATA};
//        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
//        Cursor cursor = loader.loadInBackground();
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        String result = cursor.getString(column_index);
//        cursor.close();
//        return result;
//    }
//    private String uploadFile(Uri resourceUri) {
//        HttpURLConnection conn = null;
//        DataOutputStream dos = null;
//        String lineEnd = "\r\n";
//        String twoHyphens = "--";
//        String boundary = "***";
//        int bytesRead, bytesAvailable, bufferSize;
//        byte[] buffer;
//        int maxBufferSize = 1 * 1024 * 1024;
//        final File sourceFile = new File(getRealPathFromURI(resourceUri));
//        String serverResponseMessage = null;
//        String responce = null;
//        if (!sourceFile.isFile()) {
//
////            dialog.dismiss();
//
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    Toast.makeText(getApplicationContext(), "File not found !", Toast.LENGTH_LONG).show();
//                }
//            });
//
//            return "no file";
//        } else {
//            try {
//                FileInputStream fileInputStream = new FileInputStream(sourceFile.getPath());
//                URL url = new URL("your upload server/API url");
//                conn = (HttpURLConnection) url.openConnection();
//                conn.setDoInput(true); // Allow Inputs
//                conn.setDoOutput(true); // Allow Outputs
//                conn.setUseCaches(false); // Don't use a Cached Copy
//                conn.setRequestMethod("POST");
//                conn.setRequestProperty("Connection", "Keep-Alive");
//                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
//                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//                conn.setRequestProperty("Image", sourceFile.getName());
//                dos = new DataOutputStream(conn.getOutputStream());
//                dos.writeBytes(twoHyphens + boundary + lineEnd);
//                dos.writeBytes("Content-Disposition: form-data; name=\"" + "Image" + "\";filename="
//                        + sourceFile.getName() + lineEnd);
//                dos.writeBytes(lineEnd);
//                bytesAvailable = fileInputStream.available();
//                bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                buffer = new byte[bufferSize];
//                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//                while (bytesRead > 0) {
//
//                    dos.write(buffer, 0, bufferSize);
//                    bytesAvailable = fileInputStream.available();
//                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                }
//                dos.writeBytes(lineEnd);
//                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//                int serverResponseCode = conn.getResponseCode();
//                serverResponseMessage = conn.getResponseMessage();
//                Log.i("uploadFile", "HTTP Response is : "
//                        + serverResponseMessage + ": " + serverResponseCode);
//                if (serverResponseCode <= 200) {
//
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//
//                            Toast.makeText(Account.this, "File Upload Complete.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//                fileInputStream.close();
//                dos.flush();
//                dos.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            } catch (MalformedURLException ex) {
////                dialog.dismiss();
//                ex.printStackTrace();
//
//                runOnUiThread(new Runnable() {
//                    public void run() {
//
//                        Toast.makeText(Account.this, "MalformedURLException",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } catch (IOException e) {
////                dialog.dismiss();
//                e.printStackTrace();
//
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        Toast.makeText(Account.this, "Got Exception : see logcat ",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//                Log.e("Exception", "Exception :" + e.getMessage(), e);
//            }
//        }
////        dialog.dismiss();
//        return responce;
//
//    }
}