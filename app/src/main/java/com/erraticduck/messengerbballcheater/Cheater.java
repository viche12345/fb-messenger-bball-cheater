package com.erraticduck.messengerbballcheater;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findMethodExact;

/**
 * Created by Vincent on 4/13/2016.
 */
public class Cheater implements IXposedHookLoadPackage {

	public static final String SHARED_PREFS_KEY_SCORE_OVERRIDE = "score_override";

	private BballViewHook hook = new BballViewHook();

	@Override
	public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
		if (!lpparam.packageName.equals("com.facebook.orca")) {
			return;
		}

		XposedBridge.log("Facebook messenger package loaded successfully!");

		findAndHookConstructor("com.facebook.messaging.bball.BballView", lpparam.classLoader, Context.class, hook);
		findAndHookConstructor("com.facebook.messaging.bball.BballView", lpparam.classLoader, Context.class, AttributeSet.class, hook);
		findAndHookConstructor("com.facebook.messaging.bball.BballView", lpparam.classLoader, Context.class, AttributeSet.class, "int", hook);
	}

	public static class BballViewHook extends XC_MethodHook {
		@Override
		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
			SharedPreferences prefs = new XSharedPreferences(MainActivity.class.getPackage().getName(), MainActivity.class.getPackage().getName());

			int scoreToOverride = prefs.getInt(SHARED_PREFS_KEY_SCORE_OVERRIDE, -1);
			if (scoreToOverride >= 0) {
				XposedBridge.log("Overriding score with " + scoreToOverride);
				View bballView = (View) param.thisObject;
				Context context = bballView.getContext();
				Toast.makeText(context, "Overriding score with " + scoreToOverride, Toast.LENGTH_SHORT).show();

				Method m = findMethodExact(bballView.getClass(), "setDisplayScore", "int");
				m.invoke(bballView, scoreToOverride);
			} else {
				XposedBridge.log("No score to override with");
			}
		}
	}
}
