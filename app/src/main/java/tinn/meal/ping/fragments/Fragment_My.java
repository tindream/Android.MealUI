package tinn.meal.ping.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;
import tinn.meal.ping.R;
import tinn.meal.ping.enums.IListListener;
import tinn.meal.ping.enums.ILoadListener;
import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.info.HolderInfo;
import tinn.meal.ping.info.loadInfo.AdapterInfo;
import tinn.meal.ping.info.loadInfo.GridInfo;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.info.loadInfo.LoaderInfo;
import tinn.meal.ping.info.loadInfo.SetInfo;
import tinn.meal.ping.support.AsyncListView;
import tinn.meal.ping.support.Config;
import tinn.meal.ping.support.Method;
import tinn.meal.ping.support.ViewHolder;

public class Fragment_My extends Fragment_Base implements View.OnClickListener, View.OnLongClickListener, IListListener, ILoadListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.fragment_my, container, false);
        return messageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView textView = getActivity().findViewById(R.id.my_name);
        textView.setText(Config.Admin.UserId + "");

        List<SetInfo> list = new ArrayList();
        list.add(new SetInfo(null));
        list.add(new SetInfo(R.drawable.ic_home, getString(R.string.nav_home)));
        list.add(new SetInfo(R.drawable.ic_report, getString(R.string.nav_report)));
        list.add(new SetInfo(R.drawable.ic_order, getString(R.string.nav_order)));
        list.add(new SetInfo(R.drawable.ic_my, getString(R.string.nav_my)));
        list.add(new SetInfo(null));
        list.add(new SetInfo(0, "退出"));
        list.add(new SetInfo(0, "关闭"));
        new AsyncListView().setListener(this, this).init(getActivity(), list, R.layout.item_set);
    }

    @Override
    public <T> void onReady(ObservableEmitter<LoadInfo> emitter, ViewHolder holder, T object) {
        if (!(object instanceof SetInfo)) return;
        SetInfo obj = (SetInfo) object;
        {
            HolderInfo info = new HolderInfo(holder, R.id.set_name, obj.Message);
            emitter.onNext(new LoaderInfo(LoadType.setText, info));

            info = new HolderInfo(holder, R.id.set_img, obj.imageId);
            emitter.onNext(new LoaderInfo(LoadType.setImageId, info));
        }
        if (obj.iHeard) {
            HolderInfo info = new HolderInfo(holder, R.id.set_name, "");
            emitter.onNext(new LoaderInfo(LoadType.setLine, info));
        }
    }

    @Override
    public void onReady(LoadInfo info) {
        switch (info.Types) {
            case setLine:
                HolderInfo holder = ((LoaderInfo) info).holderInfo;
                TextView textView = holder.viewHolder.getView(holder.id);
                textView.setPadding(0, 0, 0, 0);
                textView.setBackgroundColor(getActivity().getColor(R.color.colorGray));
                Method.setSize(textView, 0, 10 * Config.display.density);
                holder.viewHolder.getView(R.id.set_right).setVisibility(View.GONE);
                break;
            case setImageId:
                holder = ((LoaderInfo) info).holderInfo;
                if (holder.imageId == 0) {
                    holder.viewHolder.getView(holder.id).setVisibility(View.GONE);
                }
                break;
            case setAdapter:
                ListView listView = getActivity().findViewById(R.id.my_listView);
                //设置listView的Adapter
                listView.setAdapter(((AdapterInfo) info).adapter);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
