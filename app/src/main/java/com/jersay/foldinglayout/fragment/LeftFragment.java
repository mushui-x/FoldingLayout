package com.jersay.foldinglayout.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jersay.foldinglayout.R;
import com.jersay.foldinglayout.adapter.LeftAdapter;

import java.util.ArrayList;
import java.util.List;

public class LeftFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left, container);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        List<String> list = new ArrayList<>();
        list.add("首页");
        list.add("历史记录");
        list.add("我的收藏");
        list.add("稍后再看");
        list.add("会员");
        list.add("设置");
        LeftAdapter leftAdapter = new LeftAdapter(list);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(leftAdapter);

        return view;
    }

}
