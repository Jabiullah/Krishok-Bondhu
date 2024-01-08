package com.example.krishokbondhuuser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    int sliderAllImages[] = {R.drawable.farmer1,R.drawable.farmer2,R.drawable.farmer3};
    int sliderAllTitle[]  = {R.string.OnBoardScreen1,R.string.OnBoardScreen2,R.string.OnBoardScreen3};
    int sliderAllDesc[]  = {R.string.OnBoardScreen1Desc,R.string.OnBoardScreen2Desc,R.string.OnBoardScreen3Desc};

    public ViewPagerAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return sliderAllTitle.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_screen, container, false);

        ImageView sliderImage = (ImageView) view.findViewById(R.id.sliderImage);
        TextView sliderTitle  = (TextView) view.findViewById(R.id.sliderTitle);
        TextView sliderDes  = (TextView) view.findViewById(R.id.sliderDes);

        sliderImage.setImageResource(sliderAllImages[position]);
        sliderTitle.setText(this.sliderAllTitle[position]);
        sliderDes.setText(this.sliderAllDesc[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
