package com.github.tvbox.gongjin.impl;

import com.github.tvbox.gongjin.bean.Site;

public interface SiteCallback {

    void setSite(Site item);

    void onChanged();
}
