package com.englishlearn.myapplication.elementary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.grammar.GrammarFragment;
import com.englishlearn.myapplication.grammars.GrammarsActivity;
import com.englishlearn.myapplication.phoneticssymbols.PhoneticsSymbolsActivity;
import com.englishlearn.myapplication.phoneticssymbols.PhoneticsSymbolsFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yanzl on 16-7-20.
 */
public class ElementaryFragment extends Fragment implements ElementaryContract.View, View.OnClickListener {

    private static final String TAG = ElementaryFragment.class.getSimpleName();

    String[] titles;
    private List<Fragment> list;

    private ElementaryContract.Presenter mPresenter;
    public static ElementaryFragment newInstance() {
        return new ElementaryFragment();
    }

    @Override
    public void setPresenter(ElementaryContract.Presenter presenter) {
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

        titles = getResources().getStringArray(R.array.elementary_tablayout);
        //初始Fragment
        list = new ArrayList<>();
        list.add(PhoneticsSymbolsFragment.newInstance());
        list.add(GrammarFragment.newInstance());

        View root = inflater.inflate(R.layout.elementary_frag, container, false);
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);

        root.findViewById(R.id.phonetics).setOnClickListener(this);
        root.findViewById(R.id.grammars).setOnClickListener(this);

       /* //ViewPager
        ViewPager viewPager = (ViewPager) root.findViewById(R.id.viewPager);
        android.support.design.widget.TabLayout tableLayout = (android.support.design.widget.TabLayout) root.findViewById(R.id.tabLayout);
        viewPager.setAdapter(new ElementaryFragmentPagerAdapter(getChildFragmentManager()));
        tableLayout.setupWithViewPager(viewPager);
        tableLayout.getTabTextColors();*/

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.phonetics:
                startActivity(new Intent(this.getContext(), PhoneticsSymbolsActivity.class));
                break;
            case R.id.grammars:
                startActivity(new Intent(this.getContext(), GrammarsActivity.class));
                break;
            default:
                break;
        }
    }

    private class ElementaryFragmentPagerAdapter extends FragmentPagerAdapter{

        public ElementaryFragmentPagerAdapter(FragmentManager fm) {
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
