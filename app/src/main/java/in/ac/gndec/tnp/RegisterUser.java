package in.ac.gndec.tnp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegisterUser extends AppCompatActivity implements change{

    ViewPager viewPager;
    SectionAdapter sectionAdapter;
    registerI registerI;
    registerII registerII;
    registerIII registerIII;
    registerIV registerIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        viewPager=(ViewPager)findViewById(R.id.viewpager);
        sectionAdapter=new SectionAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionAdapter);
        viewPager.setPageTransformer(true,new ZoomOutPageTransformer());

        registerI=new registerI();
        registerII=new registerII();
        registerIII=new registerIII();
        registerIV=new registerIV();

    }

    public class SectionAdapter extends FragmentPagerAdapter{

        public SectionAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return registerIV;
                case  1:
                    return registerII;
                case 2:
                    return registerIII;
                case 3:
                    return registerIV;
            }
            return new registerI();
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    @Override
    public void changePage(int position) {
            viewPager.setCurrentItem(position,true);
    }


    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
