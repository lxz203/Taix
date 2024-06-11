package com.example.takeataxiproject.map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.example.takeataxiproject.BaseActivity;
import com.example.takeataxiproject.R;
import com.example.takeataxiproject.adapter.MapSearchAdapter;
import com.example.takeataxiproject.util.ToastHelper;

import java.util.ArrayList;
import java.util.List;

public class MapSearchActivity extends BaseActivity implements MapSearchAdapter.OnItemClickListener
        , Inputtips.InputtipsListener, TextWatcher {
    private MapSearchAdapter rvAdapter;
    private Inputtips inputtips;
    private EditText editText;
    private RecyclerView recyclerView;
    private ArrayList<Tip> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        editText = findViewById(R.id.search_edit);
        editText.addTextChangedListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.search_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        rvAdapter = new MapSearchAdapter(list, this, recyclerView);
        rvAdapter.setmOnItemClickListener(this);
        recyclerView.setAdapter(rvAdapter);
        inputtips = new Inputtips(this, (InputtipsQuery) null);
        inputtips.setInputtipsListener(this);
        rvAdapter.setmOnItemClickListener((recyclerView, view, position, data) -> {
            editText.setText(list.get(position).getAddress());

            if(list.get(position).getPoint()==null){
                ToastHelper.showShort("请选择具体地址");
            }else{
                Intent intent = new Intent();
                intent.putExtra("latitude", list.get(position).getPoint().getLatitude());
                intent.putExtra("longitude", list.get(position).getPoint().getLongitude());
                intent.putExtra("endLocation", list.get(position).getAddress());
                setResult(1, intent);
                finish();
            }

        });
    }


    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        rvAdapter.setData(list);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        InputtipsQuery inputtipsQuery = new InputtipsQuery(String.valueOf(charSequence), null);
        inputtipsQuery.setCityLimit(true);
        inputtips.setQuery(inputtipsQuery);
        inputtips.requestInputtipsAsyn();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int postion, Tip data) {

    }

}
