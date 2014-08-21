package com.bangqu.yinwan.shop.core;

import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 这个类是用来激活弱光对一些拍照手机（不闪） 为了照亮表面进行扫描。有没有官方的方式来做到这一点， 但是，允许访问此功能仍然存在于一些设备类。
 * 这因此继续通过大量的反思。
 * 
 */
final class FlashlightManager {

	private static final String TAG = FlashlightManager.class.getSimpleName();

	private static final Object iHardwareService;
	private static final Method setFlashEnabledMethod;
	static {
		iHardwareService = getHardwareService();
		setFlashEnabledMethod = getSetFlashEnabledMethod(iHardwareService);
		if (iHardwareService == null) {
			Log.v(TAG, "This device does supports control of a flashlight");
		} else {
			Log.v(TAG, "This device does not support control of a flashlight");
		}
	}

	private FlashlightManager() {
	}

	private static Object getHardwareService() {
		Class<?> serviceManagerClass = maybeForName("android.os.ServiceManager");
		if (serviceManagerClass == null) {
			return null;
		}

		Method getServiceMethod = maybeGetMethod(serviceManagerClass,
				"getService", String.class);
		if (getServiceMethod == null) {
			return null;
		}

		Object hardwareService = invoke(getServiceMethod, null, "hardware");
		if (hardwareService == null) {
			return null;
		}

		Class<?> iHardwareServiceStubClass = maybeForName("android.os.IHardwareService$Stub");
		if (iHardwareServiceStubClass == null) {
			return null;
		}

		Method asInterfaceMethod = maybeGetMethod(iHardwareServiceStubClass,
				"asInterface", IBinder.class);
		if (asInterfaceMethod == null) {
			return null;
		}

		return invoke(asInterfaceMethod, null, hardwareService);
	}

	private static Method getSetFlashEnabledMethod(Object iHardwareService) {
		if (iHardwareService == null) {
			return null;
		}
		Class<?> proxyClass = iHardwareService.getClass();
		return maybeGetMethod(proxyClass, "setFlashlightEnabled", boolean.class);
	}

	private static Class<?> maybeForName(String name) {
		try {
			return Class.forName(name);
		} catch (ClassNotFoundException cnfe) {
			// OK
			return null;
		} catch (RuntimeException re) {
			Log.w(TAG, "Unexpected error while finding class " + name, re);
			return null;
		}
	}

	private static Method maybeGetMethod(Class<?> clazz, String name,
			Class<?>... argClasses) {
		try {
			return clazz.getMethod(name, argClasses);
		} catch (NoSuchMethodException nsme) {
			// OK
			return null;
		} catch (RuntimeException re) {
			Log.w(TAG, "Unexpected error while finding method " + name, re);
			return null;
		}
	}

	private static Object invoke(Method method, Object instance, Object... args) {
		try {
			return method.invoke(instance, args);
		} catch (IllegalAccessException e) {
			Log.w(TAG, "Unexpected error while invoking " + method, e);
			return null;
		} catch (InvocationTargetException e) {
			Log.w(TAG, "Unexpected error while invoking " + method,
					e.getCause());
			return null;
		} catch (RuntimeException re) {
			Log.w(TAG, "Unexpected error while invoking " + method, re);
			return null;
		}
	}

	static void enableFlashlight() {
		setFlashlight(true);
	}

	static void disableFlashlight() {
		setFlashlight(false);
	}

	private static void setFlashlight(boolean active) {
		if (iHardwareService != null) {
			invoke(setFlashEnabledMethod, iHardwareService, active);
		}
	}

}
