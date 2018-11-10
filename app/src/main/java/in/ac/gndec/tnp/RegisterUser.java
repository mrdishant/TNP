package in.ac.gndec.tnp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.eftimoff.viewpagertransformers.FlipHorizontalTransformer;

import Interface.change;

public class RegisterUser extends AppCompatActivity implements change {

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
        viewPager.setPageTransformer(true,new FlipHorizontalTransformer());

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
                    return registerI;
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

}
