package com.example.administrator.Fanpul.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.Fanpul.R;
import com.example.administrator.Fanpul.constants.Constants;
import com.example.administrator.Fanpul.model.DB.DBProxy;
import com.example.administrator.Fanpul.model.DB.IDBCallBack.DBQueryCallback;
import com.example.administrator.Fanpul.model.entity.bmobEntity.MenuCategory;
import com.example.administrator.Fanpul.model.entity.bmobEntity.Queue;
import com.example.administrator.Fanpul.presenter.Presenter;
import com.example.administrator.Fanpul.ui.RestaurantService;
import com.example.administrator.Fanpul.ui.activity.MainActivity;
import com.example.administrator.Fanpul.ui.adapter.MainPageViewPageAdapter;
import com.example.administrator.Fanpul.ui.adapter.OrdersListAdapter;
import com.example.administrator.Fanpul.ui.component.magicindicator.MagicIndicator;
import com.example.administrator.Fanpul.ui.component.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.example.administrator.Fanpul.ui.component.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.example.administrator.Fanpul.ui.component.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.example.administrator.Fanpul.ui.component.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.example.administrator.Fanpul.ui.component.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;
import com.example.administrator.Fanpul.ui.component.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import com.example.administrator.Fanpul.utils.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/2/17.
 */

public class MenuOrderFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    private List<MenuCategory> menuCategoryList;
    @Bind(R.id.toolbar)
    public Toolbar toolbar;

    @Bind(R.id.magic_indicator)
    public MagicIndicator magicIndicator;
    @Bind(R.id.view_pager)
    public ViewPager viewPager;

    @Bind(R.id.shop_name)
    public TextView shopName;

    @Bind(R.id.table_number)
    public TextView tableNumber;

    private CommonNavigator commonNavigator;
    private MainPageViewPageAdapter mainPageViewPageAdapter;

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    private String restaurantName;
    private String tableSize;
    private int tableNum;
    private Queue queue;

    /********************************************************************************************/
    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_page;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StatusBarUtil.setImmersiveStatusBar(getActivity());
        StatusBarUtil.setImmersiveStatusBarToolbar(toolbar, getActivity());

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        menuCategoryList = new ArrayList<>();
        if (getActivity().getIntent().getStringExtra(MainActivity.RES_NAME) != null) {
            Intent intent = getActivity().getIntent();
            restaurantName = intent.getStringExtra(MainActivity.RES_NAME);
            shopName.setText(restaurantName);
            tableSize = intent.getStringExtra(MainActivity.TABLE_SIZE);
            tableNum = intent.getIntExtra(MainActivity.TABLE_NUMBER, 0);
            tableNumber.setText("桌号:" + tableSize + tableNum);
            tableNumber.setVisibility(View.VISIBLE);
        }
        if (getActivity().getIntent().getStringExtra(QueueFragment.RES_NAME) != null) {
            Intent intent = getActivity().getIntent();
            restaurantName = intent.getStringExtra(QueueFragment.RES_NAME);
            shopName.setText(restaurantName);
            tableSize = intent.getStringExtra(QueueFragment.TABLE_SIZE);
            tableNum = intent.getIntExtra(QueueFragment.TABLE_NUMBER, 0);
            tableNumber.setText("桌号:" + tableSize + tableNum);
            tableNumber.setVisibility(View.VISIBLE);
        }
        if (getActivity().getIntent().getSerializableExtra(OrdersListAdapter.QUEUE) != null) {
            Intent intent = getActivity().getIntent();
            queue = (Queue)intent.getSerializableExtra(OrdersListAdapter.QUEUE);
            shopName.setText(queue.getRestaurantName());
            if(queue.isArrived())
            tableNumber.setText(queue.getTableSize()+queue.getTableNumber());
            else{
                tableNumber.setText(queue.getTableSize() + "桌");
            }
            tableNumber.setVisibility(View.VISIBLE);
            restaurantName = queue.getRestaurantName();
            tableSize = queue.getTableSize();
        }
        if(getActivity().getIntent().getSerializableExtra(RestaurantService.QUEUE)!=null){
            queue = (Queue)getActivity().getIntent().getSerializableExtra(RestaurantService.QUEUE);
            restaurantName = queue.getRestaurantName();
            tableNum = queue.getTableNumber();
            tableSize = queue.getTableSize();
            shopName.setText(restaurantName);
            tableNumber.setText("桌号:" + tableSize + tableNum);
            tableNumber.setVisibility(View.VISIBLE);
        }


        DBProxy.proxy .queryRestaurantCategory(restaurantName, new DBQueryCallback<MenuCategory>() {
            @Override
            public void Success(List<MenuCategory> bmobObjectList) {
                menuCategoryList = bmobObjectList;
                initIndicatorView();
            }

            @Override
            public void Failed() {

            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        magicIndicator.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        magicIndicator.onPageScrollStateChanged(state);
    }

    @OnClick(R.id.imgv_search)
    public void onClickImgvSearch() {
        MobclickAgent.onEvent(getActivity(), Constants.Umeng_Event_Id_Search);

        getFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, new SearchFragment(), "fragment_search")
                .addToBackStack("fragment:reveal")
                .commit();
    }

    public String getTableSize() {
        return tableSize;
    }

    public void setTableSize(String tableSize) {
        this.tableSize = tableSize;
    }

    public int getTableNum() {
        return tableNum;
    }


    /********************************************************************************************/

    private void initIndicatorView() {
        commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setScrollPivotX(0.35f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return menuCategoryList == null ? 0 : menuCategoryList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(menuCategoryList.get(index).getCategoryName());
                simplePagerTitleView.setNormalColor(Color.parseColor("#333333"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#ffffff"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                WrapPagerIndicator indicator = new WrapPagerIndicator(context);
                indicator.setFillColor(Color.parseColor("#de9816"));//ebe4e3
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        mainPageViewPageAdapter = new MainPageViewPageAdapter(getFragmentManager(), menuCategoryList);
        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(mainPageViewPageAdapter);
    }

    public MainPageViewPageAdapter getMainPageViewPageAdapter() {
        return mainPageViewPageAdapter;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }


}
