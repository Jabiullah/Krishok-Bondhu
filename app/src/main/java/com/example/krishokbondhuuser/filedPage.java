package com.example.krishokbondhuuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Connection.RequestHandler;
import Connection.URLs;
import Model.Field;
import Model.User;
import session.Appdata;
import session.SharedPrefManager;

public class filedPage extends AppCompatActivity {
    private static final int CODE_POST_REQUEST = 1025;
    private static final int CODE_GET_REQUEST = 1024;
    List<Field> fieldListDetails;
    RelativeLayout r;
    RecyclerView recyclerView;

    ImageView addField, newField, re_fresh;
    EditText deedSerial;
    Spinner spinner;
    NotificationManagerCompat notificationManagerCompat;
    Notification n;

    private String fieldData = "" ;
    User user = SharedPrefManager.getInstance(this).getUser();

    private static final Map<Character, Character> digitMapping = createDigitMapping();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filed_page);

        Appdata.getInstance(getApplicationContext()).onDeedCheck();


        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewField);
        r = findViewById(R.id.middle_edit);
        deedSerial = findViewById(R.id.deed_no);

        spinner    = (Spinner) findViewById(R.id.spinner);
        // Spinner click listener
        // spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("বর্গা কৃষক");
        categories.add("প্রান্তিক কৃষক");
        categories.add("নিজের জমি");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        addField  = findViewById(R.id.addField);
        newField  = findViewById(R.id.new_field);
        re_fresh  = findViewById(R.id.refresh);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fieldListDetails = new ArrayList<>();

        //loading data from database
        loadFieldList();

        //push-Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MyID", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyID").
                setSmallIcon(R.drawable.logo).setContentText("খুব শিগ্রহী কৃষক বন্ধু দল দলিল যাচাই করতে আসবে").setContentTitle("একটি নতুন জমি যোগ করেছেন");
        n = builder.build();
        notificationManagerCompat = NotificationManagerCompat.from(this);
        //add field button press
        addField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r.setVisibility(View.VISIBLE);
                addField.setVisibility(View.GONE);
            }
        });
        //refresh list
        re_fresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a Random object
                Random random = new Random();
                // Generate a random number between 0 and 3
                int randomValue = random.nextInt(4);
                // Select one of the numbers based on the random value
                int randomNumber;
                switch (randomValue) {
                    case 0:
                        randomNumber = 90;
                        break;
                    case 1:
                        randomNumber = 180;
                        break;
                    case 2:
                        randomNumber = 270;
                        break;
                    case 3:
                        randomNumber = 360;
                        break;
                    default:
                        randomNumber = 45; // Default value if none of the cases match
                        break;
                }
                //animation rotate 90 "sync"
                final RotateAnimation rotateAnim = new RotateAnimation(0.0f, randomNumber,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                rotateAnim.setDuration(0);
                rotateAnim.setFillAfter(true);
                re_fresh.startAnimation(rotateAnim);

                refreshList();
            }
        });
        //new field button pressed
        newField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mark = deedSerial.getText().toString().trim();

                if (TextUtils.isEmpty(mark)) {
                    deedSerial.setError("দলিল ক্রমিক নম্বর লিখুন.");
                    deedSerial.requestFocus();
                    return;
                }
                String banglaNumber = convertToBanglaNumber(mark);
                System.out.println(banglaNumber);
                String farmer_category = String.valueOf(spinner.getSelectedItem());

                HashMap<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(user.getId()));
                params.put("mark", banglaNumber);
                params.put("category", String.valueOf(farmer_category));

                PerformNetworkRequest request = new PerformNetworkRequest(URLs.URL_FIELD_ADD, params, CODE_POST_REQUEST);
                request.execute();
                deedSerial.setText("");

                //notification call
                notificationCall();
                r.setVisibility(View.GONE);
                addField.setVisibility(View.VISIBLE);
                refreshList();
            }
        });

    }

    private void notificationCall(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManagerCompat.notify(1, n);
    }

    private void deleteField(int id) {
        PerformNetworkRequest request = new PerformNetworkRequest(URLs.URL_FIELD_DELETE+id, null, CODE_GET_REQUEST);
        request.execute();
        fieldListDetails.clear();
        refreshList();
    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressBar.setVisibility(GONE);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);
            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);
            return null;
        }
    }

    private static Map<Character, Character> createDigitMapping() {
        Map<Character, Character> mapping = new HashMap<>();
        mapping.put('0', '০');
        mapping.put('1', '১');
        mapping.put('2', '২');
        mapping.put('3', '৩');
        mapping.put('4', '৪');
        mapping.put('5', '৫');
        mapping.put('6', '৬');
        mapping.put('7', '৭');
        mapping.put('8', '৮');
        mapping.put('9', '৯');
        return mapping;
    }
    private static String convertToBanglaNumber(String englishNumber) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < englishNumber.length(); i++) {
            char digit = englishNumber.charAt(i);
            if (digitMapping.containsKey(digit)) {
                builder.append(digitMapping.get(digit));
            } else {
                builder.append(digit);
            }
        }
        return builder.toString();
    }
    private void loadFieldList() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_FIELD_LIST+user.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonFieldObj = array.getJSONObject(i);
                                fieldListDetails.add(new Field(
                                        jsonFieldObj.getInt("field_id"),
                                        jsonFieldObj.getString("land_serial_no"),
                                        jsonFieldObj.getString("deed_nature"),
                                        jsonFieldObj.getString("soil_property"),

                                        jsonFieldObj.getString("moujar_name"),
                                        jsonFieldObj.getString("ward_name"),
                                        jsonFieldObj.getString("thana_name"),


                                        jsonFieldObj.getString("deed_status"),
                                        jsonFieldObj.getString("deed_owner")
                                ));
                            }

                            FieldAdapter adapter = new FieldAdapter(filedPage.this, fieldListDetails);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void refreshList(){
        fieldListDetails.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_FIELD_LIST+user.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonFieldObj = array.getJSONObject(i);
                                fieldListDetails.add(new Field(
                                        jsonFieldObj.getInt("field_id"),
                                        jsonFieldObj.getString("land_serial_no"),
                                        jsonFieldObj.getString("deed_nature"),
                                        jsonFieldObj.getString("soil_property"),

                                        jsonFieldObj.getString("moujar_name"),
                                        jsonFieldObj.getString("ward_name"),
                                        jsonFieldObj.getString("thana_name"),


                                        jsonFieldObj.getString("deed_status"),
                                        jsonFieldObj.getString("deed_owner")
                                ));
                            }

                            FieldAdapter adapter = new FieldAdapter(filedPage.this, fieldListDetails);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.FieldViewHolder> {
        private Context mCtx;
        private List<Field> productList;
        public FieldAdapter(Context mCtx, List<Field> productList) {
            this.mCtx = mCtx;
            this.productList = productList;
        }
        @NonNull
        @Override
        public FieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.layout_field_list_relativelayout, null);
            return new FieldViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FieldViewHolder holder, int position) {
            Field product = productList.get(position);
            String concatFieldNo = product.getLand_serial_no()+" ("+product.getDeed_nature()+")";
            holder.field_no.setText(concatFieldNo);
            holder.soil.setText("মাটি প্রকৃতি : "+product.getSoil_property());
            holder.details_location.setText(String.valueOf("বিস্তারিত তথ্য : "+product.getMoujar_name()+" - "+product.getWard_name()+" - "+product.getThana_name()));


            holder.whose_deed.setText(String.valueOf(product.getDeed_owner()));

            //
            if(product.getDeed_status().contains("Processing")){
                holder.deed_validity.setText("দলিল তথ্য বৈধতা চলমান");
                holder.deed_validity.setTextColor(getColor(R.color.red));
            }else{
                fieldData = fieldData+","+product.getLand_serial_no()+"~"+product.getThana_name()+"-"+product.getWard_name();
                System.out.println(fieldData);
                holder.deed_validity.setText("অনুমোদিত");
                holder.deed_validity.setTextColor(getColor(R.color.green));
            }

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder( filedPage.this);
                    builder.setTitle("Delete " + product.getLand_serial_no())
                            .setMessage("আপনি কি নিশ্চিত, এটি মুছে ফেলতে চান ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteField(product.getField_id());
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).setIcon(android.R.drawable.ic_dialog_alert).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }
        public class FieldViewHolder extends RecyclerView.ViewHolder {
            TextView field_no, soil, details_location , whose_deed , deed_validity;
            ImageView delete;
            public FieldViewHolder(View itemView) {
                super(itemView);
                field_no            = itemView.findViewById(R.id.field_serial);
                soil                = itemView.findViewById(R.id.soil);
                details_location    = itemView.findViewById(R.id.details_info);

                whose_deed          = itemView.findViewById(R.id.deedOwner);
                deed_validity       = itemView.findViewById(R.id.deedStatus);

                delete = itemView.findViewById(R.id.removeDeed);

            }
        }
    }
    public String getOnDeed(){
        return fieldData;
    }



}