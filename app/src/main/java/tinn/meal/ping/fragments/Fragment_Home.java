package tinn.meal.ping.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;
import tinn.meal.ping.R;
import tinn.meal.ping.activity.WebActivity;
import tinn.meal.ping.enums.IListListener;
import tinn.meal.ping.enums.ILoadListener;
import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.info.setInfo.AdapterInfo;
import tinn.meal.ping.info.loadInfo.GridInfo;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.info.setInfo.LoaderInfo;
import tinn.meal.ping.support.AsyncListView;
import tinn.meal.ping.support.Method;
import tinn.meal.ping.support.ViewHolder;

public class Fragment_Home extends Fragment_Base implements View.OnClickListener, IListListener, ILoadListener {
    private boolean isComplete;
    private LoadInfo error;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.fragment_home, container, false);
        return messageLayout;
    }

    //去服务器下载数据
    //仅加载一次
    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        super.load(R.id.home_context, R.id.home_load, R.id.home_text, false);
        getActivity().findViewById(R.id.home_minus).setOnClickListener(this);
        getActivity().findViewById(R.id.home_add).setOnClickListener(this);

        List<GridInfo> list = new ArrayList();
        list.add(new GridInfo(R.drawable.ic_home, getString(R.string.nav_home)));
        list.add(new GridInfo(R.drawable.ic_report, getString(R.string.nav_report)));
        list.add(new GridInfo(R.drawable.ic_order, getString(R.string.nav_order)));
        list.add(new GridInfo(R.drawable.ic_my, getString(R.string.nav_my)));

        new AsyncListView().setListener(this, this).init(getActivity(), list, R.layout.item_grid);
        load(isComplete);
        if (error != null) error(error);
    }

    @Override
    public <T> void onReady(ObservableEmitter<LoadInfo> emitter, ViewHolder holder, T object) {
        if (!(object instanceof GridInfo)) return;
        GridInfo obj = (GridInfo) object;

        emitter.onNext(new LoaderInfo(LoadType.setText, holder, R.id.grid_name, obj.Message));
        emitter.onNext(new LoaderInfo(LoadType.setImageId, holder, R.id.grid_img, obj.imageId));
    }

    @Override
    public void onReady(LoadInfo info) {
        switch (info.Types) {
            case setAdapter:
                GridView gridView = getActivity().findViewById(R.id.gridView1);
                //设置listView的Adapter
                gridView.setAdapter(((AdapterInfo) info).adapter);
                gridView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
                    //我们需要的内容，跳转页面或显示详细信息
                    GridInfo gridInfo = (GridInfo) ((AdapterInfo) info).list.get(position);
                    TextView grid_name = view.findViewById(R.id.grid_name);
                });
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_minus:
                TextView textView = getActivity().findViewById(R.id.home_value);
                int value = Integer.parseInt(textView.getText().toString());
                value--;
                if (value < 0) value = 0;
                textView.setText(value + "");
                break;
            case R.id.home_add:
                textView = getActivity().findViewById(R.id.home_value);
                value = Integer.parseInt(textView.getText().toString());
                value++;
                textView.setText(value + "");
                break;
        }
    }

    public void load(boolean complete) {
        if (!isFirstVisible) {
            this.isComplete = complete;
            return;
        }
        super.load(R.id.home_context, R.id.home_load, R.id.home_text, complete);
    }

    public void error(LoadInfo info) {
        if (!isFirstVisible) {
            this.error = info;
            return;
        }
        TextView textView = getActivity().findViewById(R.id.home_text);
        String desc = info.Types + "\n" + info.Message;
        int end = desc.indexOf("\n");
        int color = getActivity().getResources().getColor(R.color.colorRed);
        SpannableString span = new SpannableString(desc);
        span.setSpan(new ForegroundColorSpan(color), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(span);
    }
}
