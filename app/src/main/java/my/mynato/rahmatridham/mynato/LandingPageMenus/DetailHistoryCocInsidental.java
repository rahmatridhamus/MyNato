package my.mynato.rahmatridham.mynato.LandingPageMenus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import my.mynato.rahmatridham.mynato.Adapter.CoCHistoryAdapter.DetailDodontAdapter;
import my.mynato.rahmatridham.mynato.Adapter.CoCHistoryAdapter.DetailTanilAdapter;
import my.mynato.rahmatridham.mynato.Adapter.CoCHistoryAdapter.DetailThematikInsidentalAdapter;
import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailHistoryCocInsidental extends AppCompatActivity {
    TextView detCerMot, detGames;
    ExpandableListView listTataNilai, listDoAndDont, listThematik;
    TextView nmMotivasi, nmGames, subDivisi, tanggal;
    String urldetCerMot, urldetGames;

    ImageView absensi, suasana;
    VideoView vidSuasana;

    ArrayList<String> tataNilaiArrayList;
    ArrayList<String> doAndDontArrayList;
    ArrayList<String> thematikArrayList;

    HashMap<String, List<String>> mapTanil;
    HashMap<String, List<String>> mapDoDont;
    HashMap<String, List<String>> mapThematik;

    ArrayList<String> itemTanil;
    ArrayList<String> itemDodont;
    ArrayList<String> itemThematik;

    String strTanil;
    String strDoDont;
    String strThematik;

    String id_coc_activity = "";

    DetailTanilAdapter tanilAdapter;
    DetailDodontAdapter dodontAdapter;
    DetailThematikInsidentalAdapter thematikAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history_coc_insidental);

        tataNilaiArrayList = new ArrayList<>();
        doAndDontArrayList = new ArrayList<>();
        thematikArrayList = new ArrayList<>();

        itemTanil = new ArrayList<>();
        itemDodont = new ArrayList<>();
        itemThematik = new ArrayList<>();

        mapTanil = new HashMap<>();
        mapDoDont = new HashMap<>();
        mapThematik = new HashMap<>();

        detCerMot = (TextView) findViewById(R.id.buttonOpendetCerMot);
        detCerMot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(urldetCerMot); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                v.getContext().startActivity(intent);
            }
        });

        detGames = (TextView) findViewById(R.id.buttonOpened);
        detGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(urldetGames); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                v.getContext().startActivity(intent);
            }
        });
        nmMotivasi = (TextView) findViewById(R.id.textViewNamot);
        nmGames = (TextView) findViewById(R.id.textVNagem);
        subDivisi = (TextView) findViewById(R.id.toolbar_subBidang);
        tanggal = (TextView) findViewById(R.id.toolbar_tanggal);

        absensi = (ImageView) findViewById(R.id.imageViewFotoAbsensi);
        suasana = (ImageView) findViewById(R.id.imageViewFotoSuasana);
        vidSuasana = (VideoView) findViewById(R.id.videoViewVideoSuasana);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            id_coc_activity = (String) b.get("id_coc_activity");
        }

        tanilAdapter = new DetailTanilAdapter(this, tataNilaiArrayList, mapTanil);
        dodontAdapter = new DetailDodontAdapter(this, doAndDontArrayList, mapDoDont);
        thematikAdapter = new DetailThematikInsidentalAdapter(this, thematikArrayList, mapThematik);

        getDetailInsidentalHistory(id_coc_activity);

        listTataNilai = (ExpandableListView) findViewById(R.id.listTanil);
        listTataNilai.setAdapter(tanilAdapter);
        listTataNilai.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });

        listDoAndDont = (ExpandableListView) findViewById(R.id.listDoAndDonts);
        listDoAndDont.setAdapter(dodontAdapter);
        listDoAndDont.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);

                return false;
            }
        });

        listThematik = (ExpandableListView) findViewById(R.id.listThematik);
        listThematik.setAdapter(thematikAdapter);
        listThematik.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);

                return false;
            }
        });

        tanilAdapter.notifyDataSetChanged();
        dodontAdapter.notifyDataSetChanged();
        thematikAdapter.notifyDataSetChanged();
    }

    void prepare() {
        tataNilaiArrayList.add(strTanil);
        doAndDontArrayList.add(strDoDont);
        thematikArrayList.add(strThematik);

        mapTanil.put(tataNilaiArrayList.get(0), itemTanil);
        mapDoDont.put(doAndDontArrayList.get(0), itemDodont);
        mapThematik.put(thematikArrayList.get(0), itemThematik);

        dodontAdapter.notifyDataSetChanged();
        tanilAdapter.notifyDataSetChanged();
        thematikAdapter.notifyDataSetChanged();

    }

    private void getDetailInsidentalHistory(final String id_coc_activity) {
        final ProgressDialog dialog = ProgressDialog.show(DetailHistoryCocInsidental.this, "", "Loading. Please wait...", true);

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "History/detail_history/" + id_coc_activity,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {

                                //array of Do and Dont
                                JSONObject data = jsonObject.getJSONObject("data");

                                tanggal.setText(data.optString("date", "null"));
                                subDivisi.setText(data.optString("nama_group", "null"));


                                nmMotivasi.setText(data.optString("cerita_motivasi", ""));
                                nmGames.setText(data.optString("games", ""));

                                strTanil = data.optString("tata_nilai", "");
                                strDoDont = data.optString("title_do_and_dont", "");
//                                strThematik = data.optString("title_thematik", "");
                                strThematik = "Insidental";
                                urldetCerMot = data.optString("file_cerita_motivasi", "null");
                                urldetGames = data.optString("file_games", "null");

                                itemTanil.add(data.optString("content_tata_nilai", "bla"));
                                itemDodont.add("<b>Do</b>:\n" + data.optString("content_do", "") + '\n' + "<br></br>" + "<br><b>Don't</b>:</br>\n" + data.optString("content_dont", "bla"));
                                itemThematik.add(data.optString("insidental", "bla"));
                                Picasso.with(DetailHistoryCocInsidental.this).load(data.optString("foto_absensi", "null")).into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        absensi.setImageBitmap(bitmap);
                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {
                                        absensi.setImageResource(R.drawable.empty_picture);
                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                });

                                Picasso.with(DetailHistoryCocInsidental.this).load(data.optString("foto_suasana", "null")).into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        suasana.setImageBitmap(bitmap);

                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {
                                        suasana.setImageResource(R.drawable.empty_picture);
                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                });

                                vidSuasana.setVideoPath(data.optString("video", "null"));
                                vidSuasana.start();
                                prepare();
                                dialog.dismiss();
                            } else {
                                String error = jsonObject.optString("message");
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),error, Snackbar.LENGTH_LONG);
                                snackbar.show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal menerima data", Snackbar.LENGTH_LONG);
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
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal menerima data", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = DetailHistoryCocInsidental.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
//                    params.put("id_coc_activity", id_coc_activity);
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(DetailHistoryCocInsidental.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(DetailHistoryCocInsidental.this);
        requestQueue.add(stringRequest);

    }

    private void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }
}
