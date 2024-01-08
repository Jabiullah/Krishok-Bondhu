package Adapter;

import android.content.Context;
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

import Model.NotificationModel;
import Model.PaymentHistoryModel;
import Model.PaymentModel;

public class PaymentHistoryAdapter  extends RecyclerView.Adapter<PaymentHistoryAdapter.PaymentModelViewHolder>{
    private Context mCtx;

    private static final Map<Character, Character> digitMapping = createDigitMapping();
    private static final String[] banglaMonths = {
            "", "জানুয়ারী", "ফেব্রুয়ারী", "মার্চ", "এপ্রিল", "মে", "জুন",
            "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
    };
    private List<PaymentHistoryModel> payHistoryList;

    public PaymentHistoryAdapter(Context mCtx, List<PaymentHistoryModel> payHistoryList) {
        this.mCtx = mCtx;
        this.payHistoryList = payHistoryList;
    }
    @NonNull
    @Override
    public PaymentHistoryAdapter.PaymentModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_payment_history_relativelayout, null);
        return new PaymentHistoryAdapter.PaymentModelViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PaymentHistoryAdapter.PaymentModelViewHolder holder, int position) {
        PaymentHistoryModel h1 = payHistoryList.get(position);
        String ben_date = convertToBanglaText(h1.getDate_history());


        holder.h_process.setText(h1.getBuy_structure());
        holder.h_date.setText("~"+ben_date);
        holder.h_info.setText(String.valueOf(h1.getInfo_history()));
        holder.h_payValue.setText(h1.getPayment_value()+"("+h1.getPayment_process()+")");
        holder.h_transID.setText("Trans. ID -"+String.valueOf(h1.getTransaction_id()));
    }
    @Override
    public int getItemCount() {return payHistoryList.size();}
    public class PaymentModelViewHolder extends RecyclerView.ViewHolder {
        TextView h_date, h_info, h_payValue, h_transID, h_process;
        public PaymentModelViewHolder(View itemView) {
            super(itemView);

            h_date      = itemView.findViewById(R.id.date);
            h_info      = itemView.findViewById(R.id.h_message);
            h_payValue  = itemView.findViewById(R.id.PayValue);
            h_transID   = itemView.findViewById(R.id.h_transaction_id);
            h_process   = itemView.findViewById(R.id.PayProcess);
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

}
