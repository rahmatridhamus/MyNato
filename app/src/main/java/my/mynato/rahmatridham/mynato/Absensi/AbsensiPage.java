package my.mynato.rahmatridham.mynato.Absensi;

import android.content.Intent;
import android.graphics.RectF;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import my.github.mikephil.charting.data.Entry;
import my.github.mikephil.charting.highlight.Highlight;
import my.github.mikephil.charting.listener.OnChartValueSelectedListener;
import my.mynato.rahmatridham.mynato.LandingPage;
import my.mynato.rahmatridham.mynato.LandingPageMenus.Home;
import my.mynato.rahmatridham.mynato.LandingPageMenus.MyCOC;
import my.mynato.rahmatridham.mynato.LandingPageMenus.Profile;
import my.mynato.rahmatridham.mynato.Pemberitahuan.Pemberitahuan;
import my.mynato.rahmatridham.mynato.R;

public class AbsensiPage extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.mipmap.home_menu,
            R.mipmap.mycoc_menu,
            R.mipmap.profile_menu
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Koreksi Absensi");


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setEnabled(false);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

//        setupTabIcons();
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(AbsensiPage.this, LandingPage.class));
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AbsensiPage.this, LandingPage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void setupViewPager(ViewPager viewPager) {
        AbsensiPage.ViewPagerAdapter adapter = new AbsensiPage.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new absensi(), "Absensi");
        adapter.addFrag(new request(), "Request");
        adapter.addFrag(new approval(), "Approval");
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
