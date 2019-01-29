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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.load(R.id.home_context, R.id.home_load, R.id.home_text, false);
    }

    //去服务器下载数据
    //仅加载一次
    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        getActivity().findViewById(R.id.home_minus).setOnClickListener(this);
        getActivity().findViewById(R.id.home_add).setOnClickListener(this);
        getActivity().findViewById(R.id.home_btn).setOnClickListener(this);

        List<GridInfo> list = new ArrayList();
        list.add(new GridInfo(R.drawable.ic_home, getString(R.string.nav_home)));
        list.add(new GridInfo(R.drawable.ic_report, getString(R.string.nav_report)));
        list.add(new GridInfo(R.drawable.ic_order, getString(R.string.nav_order)));
        list.add(new GridInfo(R.drawable.ic_my, getString(R.string.nav_my)));

        new AsyncListView().setListener(this, this).init(getActivity(), list, R.layout.item_grid);
        load(isComplete);
        if (error != null) {
            error(error);
            error = null;
        }
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
                Method.show(getActivity());
                break;
            case R.id.home_add:
                Method.ask(getActivity(), "Please Confirm Delete Item");
                break;
            case R.id.home_btn:
                Intent intent = new Intent(getActivity(), WebActivity.class);
                //将text框中的值传入
                intent.putExtra("title", "日志");
                File file = new File(Environment.getExternalStorageDirectory(), "/Meal/log.txt");
                intent.putExtra("file", "file://" + file.toString());
                startActivity(intent);
                break;
        }
    }

    public void load(boolean complete) {
        if (!isFirstVisible) {
            isComplete = complete;
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
