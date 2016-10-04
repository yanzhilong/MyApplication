package com.englishlearn.myapplication.intermediate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.grammar.GrammarFragment;
import com.englishlearn.myapplication.wordgroup.WordGroupsFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yanzl on 16-7-20.
 */
public class IntermediateFragment extends Fragment implements IntermediateContract.View {

    private static final String TAG = IntermediateFragment.class.getSimpleName();

    String[] titles;
    private List<Fragment> list;
    private IntermediateContract.Presenter mPresenter;
    public static IntermediateFragment newInstance() {
        return new IntermediateFragment();
    }

    @Override
    public void setPresenter(IntermediateContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        titles = getResources().getStringArray(R.array.intermediate_tablayout);
        //初始Fragment
        list = new ArrayList<>();
        list.add(WordGroupsFragment.newInstance());
        list.add(GrammarFragment.newInstance());

        View root = inflater.inflate(R.layout.elementary_frag, container, false);
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);

        //ViewPager
        ViewPager viewPager = (ViewPager) root.findViewById(R.id.viewPager);
        android.support.design.widget.TabLayout tableLayout = (android.support.design.widget.TabLayout) root.findViewById(R.id.tabLayout);
        viewPager.setAdapter(new IntermediateFragment.IntermediateFragmentPagerAdapter(getChildFragmentManager()));
        tableLayout.setupWithViewPager(viewPager);
        tableLayout.getTabTextColors();

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //如果有设置菜单，需要加这个
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //mPresenter.unsubscribe();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    private class IntermediateFragmentPagerAdapter extends FragmentPagerAdapter {

        public IntermediateFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
