package my.mynato.rahmatridham.mynato.StepCoCActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.LandingPage;
import my.mynato.rahmatridham.mynato.Model.AbsensiModel;
import my.mynato.rahmatridham.mynato.VolleyAssets.AppHelper;
import my.mynato.rahmatridham.mynato.VolleyAssets.VolleyMultipartRequest;
import my.mynato.rahmatridham.mynato.VolleyAssets.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Step6Absensi extends AppCompatActivity implements View.OnClickListener {
    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE_ABSENSI = 100;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE_PEGAWAI = 300;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final String IMAGE_DIRECTORY_NAME = "MyNato File Upload";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Uri fileUri;

    Button selesai;
    ArrayList<AbsensiModel> absensiModelArrayList;
    //    AbsensiAdapter adapter;
    MyCustomAdapter adapter;
    ListView listView;
    ImageView absen, pegawai, video;
    ArrayList<String> pushDaftarAbsen = new ArrayList<>();
    boolean isAbsen = false, isPegawai = false, isVideo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(my.mynato.rahmatridham.mynato.R.layout.activity_step6_absensi);

        absensiModelArrayList = new ArrayList<>();
        Toolbar mToolBar = (Toolbar) findViewById(my.mynato.rahmatridham.mynato.R.id.my_toolbar);
        absen = (ImageView) findViewById(my.mynato.rahmatridham.mynato.R.id.fotoAbsen);
        pegawai = (ImageView) findViewById(my.mynato.rahmatridham.mynato.R.id.fotoPegawai);
        video = (ImageView) findViewById(my.mynato.rahmatridham.mynato.R.id.videoSuasana);
        absen.setOnClickListener(this);
        pegawai.setOnClickListener(this);
        video.setOnClickListener(this);

        adapter = new MyCustomAdapter(this, my.mynato.rahmatridham.mynato.R.layout.listrow_absensi, absensiModelArrayList);
        listView = (ListView) findViewById(my.mynato.rahmatridham.mynato.R.id.listCheckAbsensi);
        listView.setAdapter(adapter);
        selesai = (Button) findViewById(my.mynato.rahmatridham.mynato.R.id.buttonSelesai);
        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAbsen && isPegawai && isVideo) {
//                if (true) {
                    StringBuffer responseText = new StringBuffer();
                    responseText.append("The following were selected...\n");
                    pushDaftarAbsen = new ArrayList<String>();

                    ArrayList<AbsensiModel> absensiModels = adapter.absensiModels;
                    for (int i = 0; i < absensiModels.size(); i++) {
                        AbsensiModel model = absensiModels.get(i);
                        if (model.isSelected()) {
                            responseText.append("\n" + model.getNama());
                            pushDaftarAbsen.add(model.getNipeg());
                        }
                    }


//                    Toast.makeText(getApplicationContext(), "size = " + pushDaftarAbsen.size(), Toast.LENGTH_LONG).show();
                    SharedPreferences sharedPreferences = Step6Absensi.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    pushAbsensi(sharedPreferences.getString(Config.IDGROUPCOC_SHARED_PREF, ""));
                } else {
//                    Toast.makeText(Step6Absensi.this, "Tolong Lengkapi submit Gambar dan Video", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Tolong Lengkapi submit Gambar dan Video", Snackbar.LENGTH_LONG);
                    snackbar.show();

                }
            }
        });

//        mToolBar.setNavigationIcon(my.mynato.rahmatridham.mynato.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolBar.setTitle("Step 6: Absensi");
//        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(Step6Absensi.this, "Mantap", Toast.LENGTH_SHORT).show();
//            }
//        });
        SharedPreferences sharedPreferences = Step6Absensi.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getAbsensi(sharedPreferences.getString(Config.IDGROUPCOC_SHARED_PREF, ""));
        adapter.notifyDataSetChanged();
    }

    private void getAbsensi(String idGroup) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(Step6Absensi.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Absensi/get_data/" + idGroup, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(Step6Absensi.this, response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status").trim();

                    if (status.equals(String.valueOf(1))) {
                        JSONObject data = jsonObject.getJSONObject("data");

                        JSONArray dataAnggota = data.getJSONArray("data_anggota");
                        for (int i = 0; i < dataAnggota.length(); i++) {
                            JSONObject object = dataAnggota.getJSONObject(i);
                            AbsensiModel model = new AbsensiModel(object.optString("nama", ""), object.optString("nipeg", ""), true);
                            absensiModelArrayList.add(model);
                        }
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    } else {
                        String error = jsonObject.optString("message");
//                        Toast.makeText(Step6Absensi.this, "errorMessage: \n" + error, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(Step6Absensi.this, "errorMessage: \n" + "Gagal mentransfer data, mohon diulang.", Toast.LENGTH_SHORT).show();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
                        snackbar.show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
//                    Toast.makeText(Step6Absensi.this, "error Response: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(Step6Absensi.this, "error Response: \n" + "Gagal mentransfer data, mohon diulang.", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal menerima data \n", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    dialog.dismiss();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
//                        Toast.makeText(Step6Absensi.this, "error getting response: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(Step6Absensi.this, "error getting response: \n" + "Gagal mentransfer data, mohon diulang.", Toast.LENGTH_SHORT).show();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal menerima data ", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = Step6Absensi.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));

                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(Step6Absensi.this, "error param: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal mengirim data", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    dialog.dismiss();
                    return params;
                }
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void postGambar(final String idGroup, final Uri fileUri, final int typeInput) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(Step6Absensi.this, "", "Loading. Please wait...", true);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Config.MAIN_URL + "Do_CoC/" + idGroup, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
//                Toast.makeText(Step6Absensi.this, resultResponse, Toast.LENGTH_SHORT).show();
                // parse success output
                try {
                    JSONObject jsonObject = new JSONObject(resultResponse);
                    String status = jsonObject.optString("status").trim();
                    if (status.equals(String.valueOf(1))) {
                        if (typeInput == 0) {
                            absen.setImageResource(my.mynato.rahmatridham.mynato.R.drawable.check);
                            isAbsen = true;
                        } else {
                            pegawai.setImageResource(my.mynato.rahmatridham.mynato.R.drawable.check);
                            isPegawai = true;
                        }
                        dialog.dismiss();
                    } else {
                        String error = jsonObject.optString("message");
//                        Toast.makeText(Step6Absensi.this, "responseError\n" + error, Toast.LENGTH_SHORT).show();
                        Toast.makeText(Step6Absensi.this, "responseError: \n" + "Gagal mentransfer data", Toast.LENGTH_SHORT).show();

                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
                        snackbar.show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
//                    Toast.makeText(Step6Absensi.this, "errorJSON: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(Step6Absensi.this, "errorJSON: \n" + "Gagal mentransfer data, mohon diulang.", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal mengirim data", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                Toast.makeText(Step6Absensi.this, "errorResponse" + "Gagal mentransfer data, mohon diulang.", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal mengirim data", Snackbar.LENGTH_LONG);
                snackbar.show();
                dialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                SharedPreferences sharedPreferences = Step6Absensi.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                //Adding parameters to request
                params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                if (typeInput == 0) {
                    params.put("foto_absensi", new DataPart(fileUri.getPath(), AppHelper.getFileDataFromDrawable(getBaseContext(), absen.getDrawable()), "image/jpeg"));
                } else {
                    params.put("foto_suasana", new DataPart(fileUri.getPath(), AppHelper.getFileDataFromDrawable(getBaseContext(), pegawai.getDrawable()), "image/jpeg"));
                }
                return params;
            }
        };

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
    }

    private void postVideo(String idGroup, final Uri fileUri) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(Step6Absensi.this, "", "Loading. Please wait...", true);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Config.MAIN_URL + "Do_CoC/" + idGroup, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                // parse success output
                try {
                    JSONObject jsonObject = new JSONObject(resultResponse);
                    String status = jsonObject.optString("status").trim();
                    if (status.equals(String.valueOf(1))) {
                        video.setImageResource(my.mynato.rahmatridham.mynato.R.drawable.check);
                        isVideo = true;
                        dialog.dismiss();
                    } else {
                        String error = jsonObject.optString("message");
//                        Toast.makeText(Step6Absensi.this, "responseError\n" + error, Toast.LENGTH_SHORT).show();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
                        snackbar.show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
//                    Toast.makeText(Step6Absensi.this, "errorJSON: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal mengirim data", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                Toast.makeText(Step6Absensi.this, "errorResponse" + error.getMessage(), Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal mengirim data", Snackbar.LENGTH_LONG);
                snackbar.show();
                dialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                SharedPreferences sharedPreferences = Step6Absensi.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                //Adding parameters to request
                params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("video", new DataPart(fileUri.getPath(), AppHelper.getFileDataFromDrawable(getBaseContext(), absen.getDrawable()), "image/jpeg"));
                return params;
            }
        };

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
    }

    public void pushAbsensi(String idGroup) {
        final ProgressDialog dialog = ProgressDialog.show(Step6Absensi.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Do_CoC/set_absensi/" + idGroup,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                Intent intent = new Intent(Step6Absensi.this, LandingPage.class);
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "CoC hari ini berhasil dilakukan", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                Config.isAnggota = false;
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                Toast.makeText(Step6Absensi.this, "CoC hari ini berhasil dilakukan", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                String error = jsonObject.optString("message");
//                                Toast.makeText(Step6Absensi.this, "responseError\n" + error, Toast.LENGTH_SHORT).show();
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
                                snackbar.show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            Toast.makeText(Step6Absensi.this, "errorJSON: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal mengirim data", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(Step6Absensi.this, "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal mengirim data", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = Step6Absensi.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    String s = "";
                    for (int i = 0; i < pushDaftarAbsen.size(); i++) {
                        if (s.equals("")) {
                            s = pushDaftarAbsen.get(i);
                        } else {
                            s += "," + pushDaftarAbsen.get(i);
                        }
                    }
                    params.put("anggota", s);
                    Log.d("daftarHadir", s);
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(Step6Absensi.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal mengirim data", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case my.mynato.rahmatridham.mynato.R.id.fotoAbsen:
                Intent intenta = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                intenta.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                // start the image capture Intent
                startActivityForResult(intenta, CAMERA_CAPTURE_IMAGE_REQUEST_CODE_ABSENSI);
                break;
            case my.mynato.rahmatridham.mynato.R.id.fotoPegawai:
                Intent intentb = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                intentb.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                // start the image capture Intent
                startActivityForResult(intentb, CAMERA_CAPTURE_IMAGE_REQUEST_CODE_PEGAWAI);
                break;
            case my.mynato.rahmatridham.mynato.R.id.videoSuasana:
                Intent intentv = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                // set video quality
                intentv.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                intentv.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
                // start the video capture Intent
                startActivityForResult(intentv, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
                break;

        }

    }

    /**
     * Receiving activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SharedPreferences sharedPreferences = Step6Absensi.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String idGroup = sharedPreferences.getString(Config.IDGROUPCOC_SHARED_PREF, "");
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE_ABSENSI) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // launching upload activity
                postGambar("set_foto_absensi/" + idGroup, fileUri, 0);
//                postGambar("set_foto_absensi/" + idGroup, data.getData(), 0);
            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
//                Toast.makeText(this, "User cancelled image capture", Toast.LENGTH_SHORT).show();

            } else {
                // failed to capture image
                Toast.makeText(this, "Gagal mengambil gambar", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE_PEGAWAI) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // launching upload activity


                postGambar("set_foto_suasana/" + idGroup, fileUri, 1);
//                Bundle extras = data.getExtras();
//                Bitmap bmp = (Bitmap) extras.get("data");
//                postGambar("set_foto_suasana/" + idGroup,data.getData() , 1);

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(this, "User cancelled image capture", Toast.LENGTH_SHORT).show();

            } else {
                // failed to capture image
                Toast.makeText(this, "Gagal mengambil gambar", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // video successfully recorded
                // launching upload activity
                postVideo("set_video/" + idGroup, fileUri);
//                postVideo("set_video/" + idGroup, data.getData());

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(this, "User cancelled video recording", Toast.LENGTH_SHORT).show();

            } else {
                // failed to record video
                Toast.makeText(this, "Gagal mengambil video", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("TAG", "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    private class MyCustomAdapter extends ArrayAdapter<AbsensiModel> {

        private ArrayList<AbsensiModel> absensiModels;

        public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<AbsensiModel> countryList) {
            super(context, textViewResourceId, countryList);
            this.absensiModels = countryList;
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(my.mynato.rahmatridham.mynato.R.layout.listrow_absensi, null);

                holder = new ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(my.mynato.rahmatridham.mynato.R.id.checkBoxAbsensi);
                holder.name.setChecked(absensiModels.get(position).isSelected());
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        AbsensiModel model = (AbsensiModel) cb.getTag();
                        model.setSelected(cb.isChecked());
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            AbsensiModel absensiModel = absensiModels.get(position);
            holder.name.setText(absensiModel.getNama());
            holder.name.setChecked(absensiModel.isSelected());
            holder.name.setTag(absensiModel);

            return convertView;

        }

    }

}
