package com.example.krishokbondhuuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Connection.URLs;
import Model.ProductModel;
import Model.User;
import session.SharedPrefManager;

public class productPage extends AppCompatActivity {
    RecyclerView recyclerView;
    List<ProductModel> productList;

    User user = SharedPrefManager.getInstance(this).getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewProduct);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();


        loadProductList();
    }
    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductDetailsViewHolder>{
        private Context mCtx;
        private List<ProductModel> productList;
        public ProductAdapter(Context mCtx, List<ProductModel> productList) {
            this.mCtx = mCtx;
            this.productList = productList;
        }
        @NonNull
        @Override
        public ProductDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.layout_product_relativelayout, null);
            return new ProductDetailsViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull ProductDetailsViewHolder holder, int position) {
            ProductModel p1 = productList.get(position);
            if(p1.getProduct_name().contains("ইউরিয়া সার")){
                holder.img.setImageResource(R.drawable.urea);
            }
            if(p1.getProduct_name().contains("টিএসপি সার")){
                holder.img.setImageResource(R.drawable.tsp);
            }

            if(p1.getProduct_name().contains("এম,ও,পি সার")){
                holder.img.setImageResource(R.drawable.mop);
            }
            if(p1.getProduct_name().contains("জিপসাম")){
                holder.img.setImageResource(R.drawable.zipsam);
            }

            if(p1.getProduct_name().contains("ম্যাগ অক্সাইড")){
                holder.img.setImageResource(R.drawable.mag);
            }
            if(p1.getProduct_name().contains("জিংক সালফেট")){
                holder.img.setImageResource(R.drawable.zinc);
            }
            holder.product_name.setText(p1.getProduct_name());
            holder.product_unit.setText(" ( "+String.valueOf(p1.getProduct_weight())+" ) ");
            holder.product_description.setText(p1.getProduct_description());
            holder.btn1.setText("এখন-ই কিনুন \n" +String.valueOf(p1.getProduct_price())+" টাকা ");
            if(p1.getProduct_reduce_price().contains("null")){

            }else{
                String a = p1.getProduct_timelimit(); // Example input string
                // Extract the number from the string
                String numberString = a.split(" ")[0];
                int number = Integer.parseInt(numberString);
                // Convert the number to its Bengali representation
                String banglaNumber = convertToBangla(number);
                // Concatenate the Bengali number with the remaining string
                String banglaString = banglaNumber + " মাস";

                holder.btn2.setVisibility(View.VISIBLE);
                holder.btn2.setText(banglaString+" সময়কাল এর জন্য GNPL আবেদন করুন");
            }


            holder.btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("p_p_name", p1.getProduct_name());
                    bundle.putString("p_p_time", p1.getProduct_timelimit());
                    bundle.putString("p_p_total_price", p1.getProduct_price());

                    Intent intent = new Intent(productPage.this, gnpl_purchase.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            holder.btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle b2 = new Bundle();
                    b2.putString("check", "false");
                    b2.putString("p_p_name_2", p1.getProduct_name());
                    b2.putString("p_p_total_price_2", p1.getProduct_price());

                    Intent intent = new Intent(productPage.this, oneTimePurchase.class);
                    intent.putExtras(b2);
                    startActivity(intent);
                }
            });
        }
        @Override
        public int getItemCount() {return productList.size();}
        public class ProductDetailsViewHolder extends RecyclerView.ViewHolder {
            ImageView img;
            TextView product_name, product_unit, product_description;
            Button btn1, btn2;
            public ProductDetailsViewHolder(View itemView) {
                super(itemView);
                img                 = itemView.findViewById(R.id.image);
                product_name        = itemView.findViewById(R.id.product_name);
                product_unit        = itemView.findViewById(R.id.product_unit);
                product_description = itemView.findViewById(R.id.product_description);
                //details_info        = itemView.findViewById(R.id.product_details);
                btn1                = itemView.findViewById(R.id.btn1);
                btn2                = itemView.findViewById(R.id.btn2);

            }
        }
    }

    // Method to convert a number to its Bengali representation
    private static String convertToBangla(int number) {
        String[] banglaNumbers = {
                "০", "১", "২", "৩", "৪", "৫", "৬", "৭", "৮", "৯", "১০", "১১", "১২"
        };

        return banglaNumbers[number];
    }
    private void loadProductList() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_PRODUCT_LIST+String.valueOf(user.getId()),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject product = array.getJSONObject(i);
                                productList.add(new ProductModel(
                                        product.getString("product_id"),
                                        product.getString("product_name"),
                                        product.getString("product_price"),
                                        product.getString("product_description"),
                                        product.getString("product_weight"),
                                        product.getString("product_reduce_price"),
                                        product.getString("procuct_timelimit")
                                        ));
                            }
                            ProductAdapter adapter = new ProductAdapter(productPage.this, productList);
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
}