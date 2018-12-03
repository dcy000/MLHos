package com.gcml.auth.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.auth.require2.login.ChoiceLoginTypeActivity;

/**
 * Created by lenovo on 2018/12/3.
 */

public class ChoseLoginComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.chocelogin";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent = new Intent(context, ChoiceLoginTypeActivity.class);
        if (!(context instanceof Activity))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
