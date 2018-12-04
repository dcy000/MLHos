package com.gcml.consitutionmodule.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.consitutionmodule.ConsitutionActivity;

/**
 * Created by lenovo on 2018/12/4.
 */

public class ConsitusionComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.consitusion";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent = new Intent(context, ConsitutionActivity.class);
        if (!(context instanceof Activity))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
