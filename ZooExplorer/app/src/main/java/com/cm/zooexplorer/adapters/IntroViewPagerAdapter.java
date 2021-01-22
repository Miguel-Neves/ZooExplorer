package com.cm.zooexplorer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import com.cm.zooexplorer.R;
import com.cm.zooexplorer.models.ScreenItem;

public class IntroViewPagerAdapter extends PagerAdapter {
    Context context;
    List<ScreenItem> introSlides;

    public IntroViewPagerAdapter(Context context, List<ScreenItem> introSlides) {
        this.context = context;
        this.introSlides = introSlides;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View screenItem = inflater.inflate(R.layout.screen_item, null);

        ImageView img = screenItem.findViewById(R.id.image);
        TextView title = screenItem.findViewById(R.id.title);
        TextView description = screenItem.findViewById(R.id.description);

        img.setImageResource(introSlides.get(position).getImg());
        title.setText(introSlides.get(position).getTitle());
        description.setText(introSlides.get(position).getDescription());

        container.addView(screenItem);

        return screenItem;
    }

    @Override
    public int getCount() {
        return introSlides.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
