package com.comsol.fleamarket_demo;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter_MarketsList extends ArrayAdapter<MarketDataModel> {


    private Activity context;
    //    Context context;
    private List<MarketDataModel> marketDataModelList;
    TextView marketName, marketArea;
//    ImageView img;

//    private ArrayList<String> FilteredArrayNames;
//    private ArrayList<String> FilteredArrayMarketAreas;
//    private ArrayList<String> FilteredArrayMarketRequest;
//    private ArrayList<String> FilteredArrayMarket_day;
//    private ArrayList<String> FilteredArrayMarket_id;

//    private ArrayList<MarketDataModel> FilteredDataModelList;


//    private String[] FilteredArrayMarketAreasArray;
//    private String[] FilteredArrayMarketRequestArray;
//    private String[] FilteredArrayMarket_dayArray;
//    private String[] FilteredArrayMarket_idArray;
//    private MarketDataModel[] FilteredDataModelListArray;

//    private int selectedTab = 0;


    public CustomAdapter_MarketsList(Activity context, List<MarketDataModel> marketDataModelsList) {
        super(context, R.layout.layout_market_list, marketDataModelsList);

        this.context = context;
        this.marketDataModelList = marketDataModelsList;
//        this.market_names = names;
////        this.FilteredArrayNames = names;
//        this.market_area = area;
////        this.request = request;
//        this.selectedTab = selectedTab;
    }

//    @Override
//    public int getCount() {
//        return FilteredArrayNames.size();
//    }

//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View v = inflater.inflate(R.layout.layout_market_list, null, true);

        marketName = (TextView) v.findViewById(R.id.tv_name);
        marketArea = (TextView) v.findViewById(R.id.tv_area);
//        marketRequest = (TextView) v.findViewById(R.id.no_req);

//        MarketDataModel marketDataModel = marketDataModelList.get(position);


        Log.i("Test","Market Name"+ marketDataModelList.get(position).getName());
        marketName.setText(marketDataModelList.get(position).getName());
        marketArea.setText(marketDataModelList.get(position).getAddress());

//        img = (ImageView)v.findViewById(R.id.img_next);
//
//        if(request==null) {
//            marketRequest.setVisibility(View.INVISIBLE);
//            img.setVisibility(View.INVISIBLE);
//        } else
//            marketRequest.setText(request.get(position).toString());

        return v;
    }
}
