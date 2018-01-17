package B4A.GPS.PaiNaiMa;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class setup extends Activity implements B4AActivity{
	public static setup mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "B4A.GPS.PaiNaiMa", "B4A.GPS.PaiNaiMa.setup");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (setup).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, true))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "B4A.GPS.PaiNaiMa", "B4A.GPS.PaiNaiMa.setup");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "B4A.GPS.PaiNaiMa.setup", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (setup) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (setup) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEvent(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return setup.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (setup) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (setup) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        Object[] o;
        if (permissions.length > 0)
            o = new Object[] {permissions[0], grantResults[0] == 0};
        else
            o = new Object[] {"", false};
        processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlmapmarkers = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlmapdefaultlocation = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _cbxmapdraggable = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _cbxdispmaptypecontrol = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _cbxdispmapzoomcontrol = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _cbxdispmapscalecontrol = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _cbxdispmapcentermarker = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _cbxdispmapmarkers = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _cbxdispmappolyline = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _cbxmapmarkersclickable = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _cbxmapmarkersdragable = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _cbxshowgpsonmap = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _cbxdrawgpspath = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _cbxsavegpspath = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _cbxgpsdispspeed = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _cbxgpsdispbearing = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _cbxgpsdispwindrose = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edtmapdefaultlocationlng = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edtmapdefaultlocationlat = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edtgpsmintime = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edtgpsmindistance = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnmapzoomlevel = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnmaplinewidth = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnmaplinecolor = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnmaplineopacity = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnmaptypecontrolid = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnmapzoomcontroltype = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnaltitudeunit = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnspeedunit = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spndistanceunit = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _scvsetup = null;
public B4A.GPS.PaiNaiMa.main _main = null;
public B4A.GPS.PaiNaiMa.satellites _satellites = null;
public B4A.GPS.PaiNaiMa.gpspaths _gpspaths = null;
public B4A.GPS.PaiNaiMa.gpssave _gpssave = null;
public B4A.GPS.PaiNaiMa.gpsmodule _gpsmodule = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 27;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 28;BA.debugLine="scvSetup.Initialize(0)";
mostCurrent._scvsetup.Initialize(mostCurrent.activityBA,(int) (0));
 //BA.debugLineNum = 29;BA.debugLine="Activity.AddView(scvSetup, 0, 0, 100%x, 100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._scvsetup.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 30;BA.debugLine="scvSetup.Color = Colors.RGB(255, 205, 250)";
mostCurrent._scvsetup.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (255),(int) (205),(int) (250)));
 //BA.debugLineNum = 31;BA.debugLine="scvSetup.Panel.LoadLayout(\"setup\")";
mostCurrent._scvsetup.getPanel().LoadLayout("setup",mostCurrent.activityBA);
 //BA.debugLineNum = 32;BA.debugLine="scvSetup.Panel.Height = pnlMapMarkers.Top + pnlMa";
mostCurrent._scvsetup.getPanel().setHeight((int) (mostCurrent._pnlmapmarkers.getTop()+mostCurrent._pnlmapmarkers.getHeight()));
 //BA.debugLineNum = 33;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 40;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 41;BA.debugLine="Main.GPSMinTime = edtGPSMinTime.Text * 1000		' ti";
mostCurrent._main._gpsmintime = (long) ((double)(Double.parseDouble(mostCurrent._edtgpsmintime.getText()))*1000);
 //BA.debugLineNum = 42;BA.debugLine="Main.GPSMinDistance = edtGPSMinDistance.Text";
mostCurrent._main._gpsmindistance = (float)(Double.parseDouble(mostCurrent._edtgpsmindistance.getText()));
 //BA.debugLineNum = 43;BA.debugLine="Main.MapDefaultLat = edtMapDefaultLocationLat.Tex";
mostCurrent._main._mapdefaultlat = (double)(Double.parseDouble(mostCurrent._edtmapdefaultlocationlat.getText()));
 //BA.debugLineNum = 44;BA.debugLine="Main.MapDefaultLng = edtMapDefaultLocationLng.Tex";
mostCurrent._main._mapdefaultlng = (double)(Double.parseDouble(mostCurrent._edtmapdefaultlocationlng.getText()));
 //BA.debugLineNum = 45;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 35;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 36;BA.debugLine="InitSetup";
_initsetup();
 //BA.debugLineNum = 37;BA.debugLine="UpdateSetup";
_updatesetup();
 //BA.debugLineNum = 38;BA.debugLine="End Sub";
return "";
}
public static String  _cbxdispmapcentermarker_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 163;BA.debugLine="Sub cbxDispMapCenterMarker_CheckedChange(Checked A";
 //BA.debugLineNum = 164;BA.debugLine="Main.DispMapCenter = Checked";
mostCurrent._main._dispmapcenter = _checked;
 //BA.debugLineNum = 165;BA.debugLine="End Sub";
return "";
}
public static String  _cbxdispmapmarkers_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 167;BA.debugLine="Sub cbxDispMapMarkers_CheckedChange(Checked As Boo";
 //BA.debugLineNum = 168;BA.debugLine="Main.DispMapMarkers = Checked";
mostCurrent._main._dispmapmarkers = _checked;
 //BA.debugLineNum = 169;BA.debugLine="End Sub";
return "";
}
public static String  _cbxdispmappolyline_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 179;BA.debugLine="Sub cbxDispMapPolyline_CheckedChange(Checked As Bo";
 //BA.debugLineNum = 180;BA.debugLine="Main.DispMapPolylne = Checked";
mostCurrent._main._dispmappolylne = _checked;
 //BA.debugLineNum = 181;BA.debugLine="End Sub";
return "";
}
public static String  _cbxdispmapscalecontrol_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 159;BA.debugLine="Sub cbxDispMapScaleControl_CheckedChange(Checked A";
 //BA.debugLineNum = 160;BA.debugLine="Main.DispMapScaleControl = Checked";
mostCurrent._main._dispmapscalecontrol = _checked;
 //BA.debugLineNum = 161;BA.debugLine="End Sub";
return "";
}
public static String  _cbxdispmaptypecontrol_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 149;BA.debugLine="Sub cbxDispMapTypeControl_CheckedChange(Checked As";
 //BA.debugLineNum = 150;BA.debugLine="Main.DispMapTypeControl = Checked";
mostCurrent._main._dispmaptypecontrol = _checked;
 //BA.debugLineNum = 151;BA.debugLine="spnMapTypeControlID.Visible = Main.DispMapTypeCon";
mostCurrent._spnmaptypecontrolid.setVisible(mostCurrent._main._dispmaptypecontrol);
 //BA.debugLineNum = 152;BA.debugLine="End Sub";
return "";
}
public static String  _cbxdispmapzoomcontrol_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 154;BA.debugLine="Sub cbxDispMapZoomControl_CheckedChange(Checked As";
 //BA.debugLineNum = 155;BA.debugLine="Main.DispMapZoomControl = Checked";
mostCurrent._main._dispmapzoomcontrol = _checked;
 //BA.debugLineNum = 156;BA.debugLine="spnMapZoomControlType.Visible = Main.DispMapZoomC";
mostCurrent._spnmapzoomcontroltype.setVisible(mostCurrent._main._dispmapzoomcontrol);
 //BA.debugLineNum = 157;BA.debugLine="End Sub";
return "";
}
public static String  _cbxdrawgpspath_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 187;BA.debugLine="Sub cbxDrawGPSPath_CheckedChange(Checked As Boolea";
 //BA.debugLineNum = 188;BA.debugLine="Main.DrawGPSPath = Checked";
mostCurrent._main._drawgpspath = _checked;
 //BA.debugLineNum = 189;BA.debugLine="End Sub";
return "";
}
public static String  _cbxgpsdispbearing_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 199;BA.debugLine="Sub cbxGPSDispBearing_CheckedChange(Checked As Boo";
 //BA.debugLineNum = 200;BA.debugLine="Main.GPSDispBearing = Checked";
mostCurrent._main._gpsdispbearing = _checked;
 //BA.debugLineNum = 201;BA.debugLine="End Sub";
return "";
}
public static String  _cbxgpsdispspeed_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 195;BA.debugLine="Sub cbxGPSDispSpeed_CheckedChange(Checked As Boole";
 //BA.debugLineNum = 196;BA.debugLine="Main.GPSDispSpeed = Checked";
mostCurrent._main._gpsdispspeed = _checked;
 //BA.debugLineNum = 197;BA.debugLine="End Sub";
return "";
}
public static String  _cbxgpsdispwindrose_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 203;BA.debugLine="Sub cbxGPSDispWindrose_CheckedChange(Checked As Bo";
 //BA.debugLineNum = 204;BA.debugLine="Main.GPSDispWindrose = Checked";
mostCurrent._main._gpsdispwindrose = _checked;
 //BA.debugLineNum = 205;BA.debugLine="End Sub";
return "";
}
public static String  _cbxmapdraggable_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 145;BA.debugLine="Sub cbxMapDraggable_CheckedChange(Checked As Boole";
 //BA.debugLineNum = 146;BA.debugLine="Main.MapSetDraggable = Checked";
mostCurrent._main._mapsetdraggable = _checked;
 //BA.debugLineNum = 147;BA.debugLine="End Sub";
return "";
}
public static String  _cbxmapmarkersclickable_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 171;BA.debugLine="Sub cbxMapMarkersClickable_CheckedChange(Checked A";
 //BA.debugLineNum = 172;BA.debugLine="Main.MapMarkerClickable = Checked";
mostCurrent._main._mapmarkerclickable = _checked;
 //BA.debugLineNum = 173;BA.debugLine="End Sub";
return "";
}
public static String  _cbxmapmarkersdragable_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 175;BA.debugLine="Sub cbxMapMarkersDragable_CheckedChange(Checked As";
 //BA.debugLineNum = 176;BA.debugLine="Main.MapMarkerDragable = Checked";
mostCurrent._main._mapmarkerdragable = _checked;
 //BA.debugLineNum = 177;BA.debugLine="End Sub";
return "";
}
public static String  _cbxsavegpspath_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 191;BA.debugLine="Sub cbxSaveGPSPath_CheckedChange(Checked As Boolea";
 //BA.debugLineNum = 192;BA.debugLine="Main.SaveGPSPath = Checked";
mostCurrent._main._savegpspath = _checked;
 //BA.debugLineNum = 193;BA.debugLine="End Sub";
return "";
}
public static String  _cbxshowgpsonmap_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 183;BA.debugLine="Sub cbxShowGPSOnMap_CheckedChange(Checked As Boole";
 //BA.debugLineNum = 184;BA.debugLine="Main.ShowGPSOnMap = Checked";
mostCurrent._main._showgpsonmap = _checked;
 //BA.debugLineNum = 185;BA.debugLine="End Sub";
return "";
}
public static String  _edtmapdefaultlocationlat_focuschanged(boolean _hasfocus) throws Exception{
 //BA.debugLineNum = 133;BA.debugLine="Sub edtMapDefaultLocationLat_FocusChanged (HasFocu";
 //BA.debugLineNum = 134;BA.debugLine="If HasFocus = True Then";
if (_hasfocus==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 135;BA.debugLine="scvSetup.ScrollPosition = pnlMapDefaultLocation.";
mostCurrent._scvsetup.setScrollPosition(mostCurrent._pnlmapdefaultlocation.getTop());
 };
 //BA.debugLineNum = 137;BA.debugLine="End Sub";
return "";
}
public static String  _edtmapdefaultlocationlng_focuschanged(boolean _hasfocus) throws Exception{
 //BA.debugLineNum = 139;BA.debugLine="Sub edtMapDefaultLocationLng_FocusChanged (HasFocu";
 //BA.debugLineNum = 140;BA.debugLine="If HasFocus = True Then";
if (_hasfocus==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 141;BA.debugLine="scvSetup.ScrollPosition = pnlMapDefaultLocation.";
mostCurrent._scvsetup.setScrollPosition(mostCurrent._pnlmapdefaultlocation.getTop());
 };
 //BA.debugLineNum = 143;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 11;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 12;BA.debugLine="Dim pnlMapMarkers, pnlMapDefaultLocation As Panel";
mostCurrent._pnlmapmarkers = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._pnlmapdefaultlocation = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 13;BA.debugLine="Dim cbxMapDraggable, cbxDispMapTypeControl, cbxDi";
mostCurrent._cbxmapdraggable = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
mostCurrent._cbxdispmaptypecontrol = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
mostCurrent._cbxdispmapzoomcontrol = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
mostCurrent._cbxdispmapscalecontrol = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Dim cbxDispMapCenterMarker, cbxDispMapMarkers, cb";
mostCurrent._cbxdispmapcentermarker = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
mostCurrent._cbxdispmapmarkers = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
mostCurrent._cbxdispmappolyline = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 15;BA.debugLine="Dim cbxMapMarkersClickable, cbxMapMarkersDragable";
mostCurrent._cbxmapmarkersclickable = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
mostCurrent._cbxmapmarkersdragable = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
mostCurrent._cbxshowgpsonmap = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Dim cbxDrawGPSPath, cbxSaveGPSPath, cbxGPSDispSpe";
mostCurrent._cbxdrawgpspath = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
mostCurrent._cbxsavegpspath = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
mostCurrent._cbxgpsdispspeed = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
mostCurrent._cbxgpsdispbearing = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
mostCurrent._cbxgpsdispwindrose = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Dim edtMapDefaultLocationLng, edtMapDefaultLocati";
mostCurrent._edtmapdefaultlocationlng = new anywheresoftware.b4a.objects.EditTextWrapper();
mostCurrent._edtmapdefaultlocationlat = new anywheresoftware.b4a.objects.EditTextWrapper();
mostCurrent._edtgpsmintime = new anywheresoftware.b4a.objects.EditTextWrapper();
mostCurrent._edtgpsmindistance = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Dim spnMapZoomLevel, spnMapLineWidth, spnMapLineC";
mostCurrent._spnmapzoomlevel = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spnmaplinewidth = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spnmaplinecolor = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spnmaplineopacity = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Dim spnMapTypeControlID, spnMapZoomControlType As";
mostCurrent._spnmaptypecontrolid = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spnmapzoomcontroltype = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Dim spnAltitudeUnit, spnSpeedUnit, spnDistanceUni";
mostCurrent._spnaltitudeunit = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spnspeedunit = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spndistanceunit = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Dim scvSetup As ScrollView";
mostCurrent._scvsetup = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 25;BA.debugLine="End Sub";
return "";
}
public static String  _initsetup() throws Exception{
int _i = 0;
 //BA.debugLineNum = 47;BA.debugLine="Sub InitSetup";
 //BA.debugLineNum = 49;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 51;BA.debugLine="spnMapZoomLevel.Clear";
mostCurrent._spnmapzoomlevel.Clear();
 //BA.debugLineNum = 52;BA.debugLine="For i = 0 To 8";
{
final int step3 = 1;
final int limit3 = (int) (8);
for (_i = (int) (0) ; (step3 > 0 && _i <= limit3) || (step3 < 0 && _i >= limit3); _i = ((int)(0 + _i + step3)) ) {
 //BA.debugLineNum = 53;BA.debugLine="spnMapZoomLevel.Add(i + 10)";
mostCurrent._spnmapzoomlevel.Add(BA.NumberToString(_i+10));
 }
};
 //BA.debugLineNum = 56;BA.debugLine="spnMapLineWidth.Clear";
mostCurrent._spnmaplinewidth.Clear();
 //BA.debugLineNum = 57;BA.debugLine="spnMapLineWidth.Add(1)";
mostCurrent._spnmaplinewidth.Add(BA.NumberToString(1));
 //BA.debugLineNum = 58;BA.debugLine="spnMapLineWidth.Add(2)";
mostCurrent._spnmaplinewidth.Add(BA.NumberToString(2));
 //BA.debugLineNum = 59;BA.debugLine="spnMapLineWidth.Add(3)";
mostCurrent._spnmaplinewidth.Add(BA.NumberToString(3));
 //BA.debugLineNum = 60;BA.debugLine="spnMapLineWidth.Add(5)";
mostCurrent._spnmaplinewidth.Add(BA.NumberToString(5));
 //BA.debugLineNum = 62;BA.debugLine="spnMapLineColor.Clear";
mostCurrent._spnmaplinecolor.Clear();
 //BA.debugLineNum = 63;BA.debugLine="spnMapLineColor.Add(\"Red\")";
mostCurrent._spnmaplinecolor.Add("Red");
 //BA.debugLineNum = 64;BA.debugLine="spnMapLineColor.Add(\"Blue\")";
mostCurrent._spnmaplinecolor.Add("Blue");
 //BA.debugLineNum = 65;BA.debugLine="spnMapLineColor.Add(\"Green\")";
mostCurrent._spnmaplinecolor.Add("Green");
 //BA.debugLineNum = 67;BA.debugLine="spnMapLineOpacity.Clear";
mostCurrent._spnmaplineopacity.Clear();
 //BA.debugLineNum = 68;BA.debugLine="spnMapLineOpacity.Add(0.25)";
mostCurrent._spnmaplineopacity.Add(BA.NumberToString(0.25));
 //BA.debugLineNum = 69;BA.debugLine="spnMapLineOpacity.Add(0.5)";
mostCurrent._spnmaplineopacity.Add(BA.NumberToString(0.5));
 //BA.debugLineNum = 70;BA.debugLine="spnMapLineOpacity.Add(0.75)";
mostCurrent._spnmaplineopacity.Add(BA.NumberToString(0.75));
 //BA.debugLineNum = 71;BA.debugLine="spnMapLineOpacity.Add(1)";
mostCurrent._spnmaplineopacity.Add(BA.NumberToString(1));
 //BA.debugLineNum = 73;BA.debugLine="spnAltitudeUnit.Clear";
mostCurrent._spnaltitudeunit.Clear();
 //BA.debugLineNum = 74;BA.debugLine="For i = 0 To Main.AltitudeUnitNumber - 1";
{
final int step21 = 1;
final int limit21 = (int) (mostCurrent._main._altitudeunitnumber-1);
for (_i = (int) (0) ; (step21 > 0 && _i <= limit21) || (step21 < 0 && _i >= limit21); _i = ((int)(0 + _i + step21)) ) {
 //BA.debugLineNum = 75;BA.debugLine="spnAltitudeUnit.Add(Main.AltitudeUnitText(i))";
mostCurrent._spnaltitudeunit.Add(mostCurrent._main._altitudeunittext[_i]);
 }
};
 //BA.debugLineNum = 78;BA.debugLine="spnSpeedUnit.Clear";
mostCurrent._spnspeedunit.Clear();
 //BA.debugLineNum = 79;BA.debugLine="For i = 0 To Main.SpeedUnitNumber - 1";
{
final int step25 = 1;
final int limit25 = (int) (mostCurrent._main._speedunitnumber-1);
for (_i = (int) (0) ; (step25 > 0 && _i <= limit25) || (step25 < 0 && _i >= limit25); _i = ((int)(0 + _i + step25)) ) {
 //BA.debugLineNum = 80;BA.debugLine="spnSpeedUnit.Add(Main.SpeedUnitText(i))";
mostCurrent._spnspeedunit.Add(mostCurrent._main._speedunittext[_i]);
 }
};
 //BA.debugLineNum = 83;BA.debugLine="spnDistanceUnit.Clear";
mostCurrent._spndistanceunit.Clear();
 //BA.debugLineNum = 84;BA.debugLine="For i = 0 To Main.DistanceUnitNumber - 1";
{
final int step29 = 1;
final int limit29 = (int) (mostCurrent._main._distanceunitnumber-1);
for (_i = (int) (0) ; (step29 > 0 && _i <= limit29) || (step29 < 0 && _i >= limit29); _i = ((int)(0 + _i + step29)) ) {
 //BA.debugLineNum = 85;BA.debugLine="spnDistanceUnit.Add(Main.DistanceUnitText(i))";
mostCurrent._spndistanceunit.Add(mostCurrent._main._distanceunittext[_i]);
 }
};
 //BA.debugLineNum = 88;BA.debugLine="spnMapTypeControlID.Clear";
mostCurrent._spnmaptypecontrolid.Clear();
 //BA.debugLineNum = 89;BA.debugLine="For i = 0 To Main.DispMapTypeControlIDNumber - 1";
{
final int step33 = 1;
final int limit33 = (int) (mostCurrent._main._dispmaptypecontrolidnumber-1);
for (_i = (int) (0) ; (step33 > 0 && _i <= limit33) || (step33 < 0 && _i >= limit33); _i = ((int)(0 + _i + step33)) ) {
 //BA.debugLineNum = 90;BA.debugLine="spnMapTypeControlID.Add(Main.DispMapTypeControlI";
mostCurrent._spnmaptypecontrolid.Add(mostCurrent._main._dispmaptypecontrolidtext[_i]);
 }
};
 //BA.debugLineNum = 93;BA.debugLine="spnMapZoomControlType.Clear";
mostCurrent._spnmapzoomcontroltype.Clear();
 //BA.debugLineNum = 94;BA.debugLine="For i = 0 To Main.DispMapZoomControlTypeNumber -";
{
final int step37 = 1;
final int limit37 = (int) (mostCurrent._main._dispmapzoomcontroltypenumber-1);
for (_i = (int) (0) ; (step37 > 0 && _i <= limit37) || (step37 < 0 && _i >= limit37); _i = ((int)(0 + _i + step37)) ) {
 //BA.debugLineNum = 95;BA.debugLine="spnMapZoomControlType.Add(Main.DispMapZoomContro";
mostCurrent._spnmapzoomcontroltype.Add(mostCurrent._main._dispmapzoomcontroltypetext[_i]);
 }
};
 //BA.debugLineNum = 97;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 7;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="End Sub";
return "";
}
public static String  _spnaltitudeunit_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 212;BA.debugLine="Sub spnAltitudeUnit_ItemClick (Position As Int, Va";
 //BA.debugLineNum = 213;BA.debugLine="Main.AltitudeUnitIndex = Position";
mostCurrent._main._altitudeunitindex = _position;
 //BA.debugLineNum = 214;BA.debugLine="End Sub";
return "";
}
public static String  _spndistanceunit_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 220;BA.debugLine="Sub spnDistanceUnit_ItemClick (Position As Int, Va";
 //BA.debugLineNum = 221;BA.debugLine="Main.DistanceUnitIndex = Position";
mostCurrent._main._distanceunitindex = _position;
 //BA.debugLineNum = 222;BA.debugLine="End Sub";
return "";
}
public static String  _spnmaplinecolor_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 224;BA.debugLine="Sub spnMapLineColor_ItemClick (Position As Int, Va";
 //BA.debugLineNum = 225;BA.debugLine="Main.MapLineColorIndex = Position";
mostCurrent._main._maplinecolorindex = _position;
 //BA.debugLineNum = 226;BA.debugLine="Select Main.MapLineColorIndex";
switch (BA.switchObjectToInt(mostCurrent._main._maplinecolorindex,(int) (0),(int) (1),(int) (2))) {
case 0: {
 //BA.debugLineNum = 228;BA.debugLine="Main.MapLineColor = \"ff0000\"";
mostCurrent._main._maplinecolor = "ff0000";
 break; }
case 1: {
 //BA.debugLineNum = 230;BA.debugLine="Main.MapLineColor = \"0000ff\"";
mostCurrent._main._maplinecolor = "0000ff";
 break; }
case 2: {
 //BA.debugLineNum = 232;BA.debugLine="Main.MapLineColor = \"00ff00\"";
mostCurrent._main._maplinecolor = "00ff00";
 break; }
}
;
 //BA.debugLineNum = 234;BA.debugLine="End Sub";
return "";
}
public static String  _spnmaplineopacity_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 241;BA.debugLine="Sub spnMapLineOpacity_ItemClick (Position As Int,";
 //BA.debugLineNum = 242;BA.debugLine="Main.MapLineOpacityIndex = Position";
mostCurrent._main._maplineopacityindex = _position;
 //BA.debugLineNum = 243;BA.debugLine="Main.MapLineOpacity = Value";
mostCurrent._main._maplineopacity = (float)(BA.ObjectToNumber(_value));
 //BA.debugLineNum = 244;BA.debugLine="End Sub";
return "";
}
public static String  _spnmaplinewidth_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 236;BA.debugLine="Sub spnMapLineWidth_ItemClick (Position As Int, Va";
 //BA.debugLineNum = 237;BA.debugLine="Main.MapLineWidthIndex = Position";
mostCurrent._main._maplinewidthindex = _position;
 //BA.debugLineNum = 238;BA.debugLine="Main.MapLineWidth = Value";
mostCurrent._main._maplinewidth = (int)(BA.ObjectToNumber(_value));
 //BA.debugLineNum = 239;BA.debugLine="End Sub";
return "";
}
public static String  _spnmaptypecontrolid_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 246;BA.debugLine="Sub spnMapTypeControlID_ItemClick (Position As Int";
 //BA.debugLineNum = 247;BA.debugLine="Main.DispMapTypeControlIDIndex = Position";
mostCurrent._main._dispmaptypecontrolidindex = _position;
 //BA.debugLineNum = 248;BA.debugLine="Main.DispMapTypeControlID = Value";
mostCurrent._main._dispmaptypecontrolid = BA.ObjectToString(_value);
 //BA.debugLineNum = 249;BA.debugLine="End Sub";
return "";
}
public static String  _spnmapzoomcontroltype_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 251;BA.debugLine="Sub spnMapZoomControlType_ItemClick (Position As I";
 //BA.debugLineNum = 252;BA.debugLine="Main.DispMapZoomControlTypeIndex = Position";
mostCurrent._main._dispmapzoomcontroltypeindex = _position;
 //BA.debugLineNum = 253;BA.debugLine="Main.DispMapZoomControlType = Value";
mostCurrent._main._dispmapzoomcontroltype = BA.ObjectToString(_value);
 //BA.debugLineNum = 254;BA.debugLine="End Sub";
return "";
}
public static String  _spnmapzoomlevel_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 207;BA.debugLine="Sub spnMapZoomLevel_ItemClick (Position As Int, Va";
 //BA.debugLineNum = 208;BA.debugLine="Main.MapDefaultZoomLevel = Value";
mostCurrent._main._mapdefaultzoomlevel = (int)(BA.ObjectToNumber(_value));
 //BA.debugLineNum = 209;BA.debugLine="Main.MapDefaultZoomLevelIndex = Position";
mostCurrent._main._mapdefaultzoomlevelindex = _position;
 //BA.debugLineNum = 210;BA.debugLine="End Sub";
return "";
}
public static String  _spnspeedunit_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 216;BA.debugLine="Sub spnSpeedUnit_ItemClick (Position As Int, Value";
 //BA.debugLineNum = 217;BA.debugLine="Main.SpeedUnitIndex = Position";
mostCurrent._main._speedunitindex = _position;
 //BA.debugLineNum = 218;BA.debugLine="End Sub";
return "";
}
public static String  _updatesetup() throws Exception{
 //BA.debugLineNum = 99;BA.debugLine="Sub UpdateSetup";
 //BA.debugLineNum = 100;BA.debugLine="edtMapDefaultLocationLng.Text = Main.MapDefaultLn";
mostCurrent._edtmapdefaultlocationlng.setText((Object)(mostCurrent._main._mapdefaultlng));
 //BA.debugLineNum = 101;BA.debugLine="edtMapDefaultLocationLat.Text = Main.MapDefaultLa";
mostCurrent._edtmapdefaultlocationlat.setText((Object)(mostCurrent._main._mapdefaultlat));
 //BA.debugLineNum = 102;BA.debugLine="edtGPSMinTime.Text = Main.GPSMinTime / 1000		' ti";
mostCurrent._edtgpsmintime.setText((Object)(mostCurrent._main._gpsmintime/(double)1000));
 //BA.debugLineNum = 103;BA.debugLine="edtGPSMinDistance.Text = Main.GPSMinDistance";
mostCurrent._edtgpsmindistance.setText((Object)(mostCurrent._main._gpsmindistance));
 //BA.debugLineNum = 104;BA.debugLine="cbxMapDraggable.Checked = Main.MapSetDraggable";
mostCurrent._cbxmapdraggable.setChecked(mostCurrent._main._mapsetdraggable);
 //BA.debugLineNum = 105;BA.debugLine="cbxDispMapTypeControl.Checked = Main.DispMapTypeC";
mostCurrent._cbxdispmaptypecontrol.setChecked(mostCurrent._main._dispmaptypecontrol);
 //BA.debugLineNum = 106;BA.debugLine="cbxGPSDispSpeed.Checked = Main.GPSDispSpeed";
mostCurrent._cbxgpsdispspeed.setChecked(mostCurrent._main._gpsdispspeed);
 //BA.debugLineNum = 107;BA.debugLine="cbxGPSDispBearing.Checked = Main.GPSDispBearing";
mostCurrent._cbxgpsdispbearing.setChecked(mostCurrent._main._gpsdispbearing);
 //BA.debugLineNum = 108;BA.debugLine="cbxGPSDispWindrose.Checked = Main.GPSDispWindrose";
mostCurrent._cbxgpsdispwindrose.setChecked(mostCurrent._main._gpsdispwindrose);
 //BA.debugLineNum = 109;BA.debugLine="cbxDispMapZoomControl.Checked = Main.DispMapZoomC";
mostCurrent._cbxdispmapzoomcontrol.setChecked(mostCurrent._main._dispmapzoomcontrol);
 //BA.debugLineNum = 110;BA.debugLine="cbxDispMapScaleControl.Checked = Main.DispMapScal";
mostCurrent._cbxdispmapscalecontrol.setChecked(mostCurrent._main._dispmapscalecontrol);
 //BA.debugLineNum = 111;BA.debugLine="cbxDispMapCenterMarker.Checked = Main.DispMapCent";
mostCurrent._cbxdispmapcentermarker.setChecked(mostCurrent._main._dispmapcenter);
 //BA.debugLineNum = 112;BA.debugLine="cbxDispMapMarkers.Checked = Main.DispMapMarkers";
mostCurrent._cbxdispmapmarkers.setChecked(mostCurrent._main._dispmapmarkers);
 //BA.debugLineNum = 113;BA.debugLine="cbxDispMapPolyline.Checked = Main.DispMapPolylne";
mostCurrent._cbxdispmappolyline.setChecked(mostCurrent._main._dispmappolylne);
 //BA.debugLineNum = 114;BA.debugLine="cbxMapMarkersClickable.Checked = Main.MapMarkerCl";
mostCurrent._cbxmapmarkersclickable.setChecked(mostCurrent._main._mapmarkerclickable);
 //BA.debugLineNum = 115;BA.debugLine="cbxMapMarkersDragable.Checked = Main.MapMarkerDra";
mostCurrent._cbxmapmarkersdragable.setChecked(mostCurrent._main._mapmarkerdragable);
 //BA.debugLineNum = 116;BA.debugLine="cbxShowGPSOnMap.Checked = Main.ShowGPSOnMap";
mostCurrent._cbxshowgpsonmap.setChecked(mostCurrent._main._showgpsonmap);
 //BA.debugLineNum = 117;BA.debugLine="cbxDrawGPSPath.Checked = Main.DrawGPSPath";
mostCurrent._cbxdrawgpspath.setChecked(mostCurrent._main._drawgpspath);
 //BA.debugLineNum = 118;BA.debugLine="cbxSaveGPSPath.Checked = Main.SaveGPSPath";
mostCurrent._cbxsavegpspath.setChecked(mostCurrent._main._savegpspath);
 //BA.debugLineNum = 120;BA.debugLine="spnMapZoomLevel.SelectedIndex = Main.MapDefaultZo";
mostCurrent._spnmapzoomlevel.setSelectedIndex(mostCurrent._main._mapdefaultzoomlevelindex);
 //BA.debugLineNum = 121;BA.debugLine="spnMapLineColor.SelectedIndex = Main.MapLineColor";
mostCurrent._spnmaplinecolor.setSelectedIndex(mostCurrent._main._maplinecolorindex);
 //BA.debugLineNum = 122;BA.debugLine="spnMapLineWidth.SelectedIndex = Main.MapLineWidth";
mostCurrent._spnmaplinewidth.setSelectedIndex(mostCurrent._main._maplinewidthindex);
 //BA.debugLineNum = 123;BA.debugLine="spnMapLineOpacity.SelectedIndex = Main.MapLineOpa";
mostCurrent._spnmaplineopacity.setSelectedIndex(mostCurrent._main._maplineopacityindex);
 //BA.debugLineNum = 124;BA.debugLine="spnAltitudeUnit.SelectedIndex = Main.AltitudeUnit";
mostCurrent._spnaltitudeunit.setSelectedIndex(mostCurrent._main._altitudeunitindex);
 //BA.debugLineNum = 125;BA.debugLine="spnSpeedUnit.SelectedIndex = Main.SpeedUnitIndex";
mostCurrent._spnspeedunit.setSelectedIndex(mostCurrent._main._speedunitindex);
 //BA.debugLineNum = 126;BA.debugLine="spnDistanceUnit.SelectedIndex = Main.DistanceUnit";
mostCurrent._spndistanceunit.setSelectedIndex(mostCurrent._main._distanceunitindex);
 //BA.debugLineNum = 127;BA.debugLine="spnMapTypeControlID.SelectedIndex = Main.DispMapT";
mostCurrent._spnmaptypecontrolid.setSelectedIndex(mostCurrent._main._dispmaptypecontrolidindex);
 //BA.debugLineNum = 128;BA.debugLine="spnMapZoomControlType.SelectedIndex = Main.DispMa";
mostCurrent._spnmapzoomcontroltype.setSelectedIndex(mostCurrent._main._dispmapzoomcontroltypeindex);
 //BA.debugLineNum = 129;BA.debugLine="spnMapTypeControlID.Visible = Main.DispMapTypeCon";
mostCurrent._spnmaptypecontrolid.setVisible(mostCurrent._main._dispmaptypecontrol);
 //BA.debugLineNum = 130;BA.debugLine="spnMapZoomControlType.Visible = Main.DispMapZoomC";
mostCurrent._spnmapzoomcontroltype.setVisible(mostCurrent._main._dispmapzoomcontrol);
 //BA.debugLineNum = 131;BA.debugLine="End Sub";
return "";
}
}
