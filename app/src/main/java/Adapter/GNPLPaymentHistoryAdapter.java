package Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.krishokbondhuuser.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.GNPLPaymentHistoryModel;
import Model.PaymentHistoryModel;

public class GNPLPaymentHistoryAdapter extends RecyclerView.Adapter<GNPLPaymentHistoryAdapter.GNPLPaymentModelViewHolder>{
    private Context mCtx;
    private List<GNPLPaymentHistoryModel> payHistoryList;

    private static final Map<Character, Character> digitMapping = createDigitMapping();
    private static final Map<Character, Character> banglaNumberMap = new HashMap<>();

    static {
        banglaNumberMap.put('0', '০');
        banglaNumberMap.put('1', '১');
        banglaNumberMap.put('2', '২');
        banglaNumberMap.put('3', '৩');
        banglaNumberMap.put('4', '৪');
        banglaNumberMap.put('5', '৫');
        banglaNumberMap.put('6', '৬');
        banglaNumberMap.put('7', '৭');
        banglaNumberMap.put('8', '৮');
        banglaNumberMap.put('9', '৯');
    }

    private static final String[] banglaMonths = {
            "", "জানুয়ারী", "ফেব্রুয়ারী", "মার্চ", "এপ্রিল", "মে", "জুন",
            "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
    };



    public GNPLPaymentHistoryAdapter(Context mCtx, List<GNPLPaymentHistoryModel> payHistoryList) {
        this.mCtx = mCtx;
        this.payHistoryList = payHistoryList;
    }
    @NonNull
    @Override
    public GNPLPaymentHistoryAdapter.GNPLPaymentModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_gnpl_history, null);
        return new GNPLPaymentHistoryAdapter.GNPLPaymentModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GNPLPaymentHistoryAdapter.GNPLPaymentModelViewHolder holder, int position) {
        GNPLPaymentHistoryModel h1 = payHistoryList.get(position);

        holder.g_date.setText(convertToBanglaText(h1.getDate_history()));

        holder.g_name.setText(h1.getProduct_name()+"("+convertToBanglaNumber(h1.getProduct_quantity())+" কেজি)");
        holder.g_description.setText(h1.getField_value()+"\n("+convertToBanglaNumber(h1.getTotal_cost())+" টাকার পণ্যের আবেদন )");
        if(h1.getGnpl_status().contains("Approved")){
            holder.g_status.setText("GNPL Status : অনুমোদিত");
            holder.g_status.setTextColor(Color.GREEN);
        }else{
            holder.g_status.setText("GNPL Status : অপেক্ষমান ");
        }

    }

    @Override
    public int getItemCount() {
        return payHistoryList.size();
    }
    public class GNPLPaymentModelViewHolder extends RecyclerView.ViewHolder {
        TextView g_date, g_name, g_status, g_description;
        public GNPLPaymentModelViewHolder(View itemView) {
            super(itemView);

            g_date      = itemView.findViewById(R.id.p_name);
            g_name      = itemView.findViewById(R.id.p_unit);
            g_status    = itemView.findViewById(R.id.p_status);
            g_description= itemView.findViewById(R.id.p_description);

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

    private static String convertToBanglaText(String dateString) {
        String banglaText = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-M-d"));
            int day = date.getDayOfMonth();
            int month = date.getMonthValue();
            int year = date.getYear() % 100;
            for (char c : String.valueOf(day).toCharArray()) {
                if (digitMapping.containsKey(c)) {
                    banglaText += digitMapping.get(c);
                } else {
                    banglaText += c;
                }
            }

            banglaText += " " + banglaMonths[month] + " ";

            for (char c : String.valueOf(year).toCharArray()) {
                if (digitMapping.containsKey(c)) {
                    banglaText += digitMapping.get(c);
                } else {
                    banglaText += c;
                }
            }
        }
        return banglaText;
    }
    public static String convertToBanglaNumber(String number) {
        StringBuilder banglaNumber = new StringBuilder();

        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            Character banglaDigit = banglaNumberMap.get(ch);

            if (banglaDigit != null) {
                banglaNumber.append(banglaDigit);
            } else {
                banglaNumber.append(ch);
            }
        }

        return banglaNumber.toString();
    }


}