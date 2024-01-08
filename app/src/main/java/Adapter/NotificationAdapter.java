package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.krishokbondhuuser.R;
import java.util.List;

import Model.NotificationModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationModelViewHolder>{
    private Context mCtx;

    private List<NotificationModel> notificationList;

    private static final Map<Character, Character> digitMapping = createDigitMapping();
    private static final String[] banglaMonths = {
            "", "জানুয়ারী", "ফেব্রুয়ারী", "মার্চ", "এপ্রিল", "মে", "জুন",
            "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
    };

    public NotificationAdapter(Context mCtx, List<NotificationModel> notificationList) {
        this.mCtx = mCtx;
        this.notificationList = notificationList;
    }
    @NonNull
    @Override
    public NotificationAdapter.NotificationModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_notification_relativelayout, null);
        return new NotificationAdapter.NotificationModelViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationModelViewHolder holder, int position) {
        NotificationModel n1 = notificationList.get(position);
        String ben_date = convertToBanglaText(n1.getDate());
        holder.n_date.setText(ben_date);
        holder.n_info.setText(String.valueOf(n1.getInfo()));
    }
    @Override
    public int getItemCount() {return notificationList.size();}
    public class NotificationModelViewHolder extends RecyclerView.ViewHolder {
        TextView n_date, n_info;
        public NotificationModelViewHolder(View itemView) {
            super(itemView);
            n_date = itemView.findViewById(R.id.date);
            n_info = itemView.findViewById(R.id.n_message);
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
