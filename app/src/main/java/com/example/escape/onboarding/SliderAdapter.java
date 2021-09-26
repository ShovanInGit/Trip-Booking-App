package com.example.escape.onboarding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.example.escape.R;


public class SliderAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private int custom_position=0;
    LottieAnimationView anim;

    public SliderAdapter(Context context) {
        this.context = context;
    }

//    private int[] images ={R.drawable.kilaibu,
//            R.drawable.kilaibu2,
//            R.drawable.kilaibu3
//    };

    private int[] animation ={R.raw.organizer3,
            R.raw.organizer2,
            R.raw.organizer1
    };
    private int[] headings ={R.string.first_slide_title,
            R.string.second_slide_title,
            R.string.third_slide_title
    };
    private int[] description ={R.string.first_slide_desc,
            R.string.second_slide_desc,
            R.string.third_slide_desc
    };

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(ConstraintLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slides_layout,container,false);

//        ImageView image=(ImageView)view.findViewById(R.id.slider_image);
        TextView text=(TextView)view.findViewById(R.id.slider_heading);
        TextView desc=(TextView)view.findViewById(R.id.slider_desc);
        anim = (LottieAnimationView)view.findViewById(R.id.animationView);


//        image.setImageResource(images[position]);
        text.setText(headings[position]);
        desc.setText(description[position]);
        anim.setAnimation(animation[position]);



        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}

