package tinn.meal.ping.support;

import io.reactivex.ObservableEmitter;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.info.loadInfo.TimeInfo;

public class AsyncWait extends AsyncBase {
    public AsyncWait() {
        initData(new TimeInfo(0));
    }

    @Override
    protected void onLoadData(ObservableEmitter<LoadInfo> emitter, LoadInfo info) throws Exception {
        Thread.sleep(((TimeInfo) info).onlyTime);
        emitter.onNext(info);
    }
}
