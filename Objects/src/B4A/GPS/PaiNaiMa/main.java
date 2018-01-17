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

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = true;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "B4A.GPS.PaiNaiMa", "B4A.GPS.PaiNaiMa.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
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
		activityBA = new BA(this, layout, processBA, "B4A.GPS.PaiNaiMa", "B4A.GPS.PaiNaiMa.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "B4A.GPS.PaiNaiMa.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
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
		return main.class;
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
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (main) Resume **");
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
public static String _progname = "";
public static String _progversion = "";
public static anywheresoftware.b4a.gps.GPS _gps1 = null;
public static anywheresoftware.b4a.phone.Phone.PhoneWakeState _phoneawake = null;
public static anywheresoftware.b4a.objects.Timer _mapdisptimer = null;
public static anywheresoftware.b4a.gps.LocationWrapper _mapcenter = null;
public static anywheresoftware.b4a.gps.LocationWrapper _mapoldcenter = null;
public static boolean _mapsetdraggable = false;
public static int _mapdefaultzoomlevelindex = 0;
public static int _mapdefaultzoomlevel = 0;
public static int _mapzoomlevel = 0;
public static int _mapzoomlevelold = 0;
public static boolean _mapzoomcalculated = false;
public static double _mapdefaultlat = 0;
public static double _mapdefaultlng = 0;
public static int _maplinewidthindex = 0;
public static int _maplinewidth = 0;
public static int _maplinecolorindex = 0;
public static String _maplinecolor = "";
public static int _maplineopacityindex = 0;
public static float _maplineopacity = 0f;
public static boolean _mapmarkerclickable = false;
public static boolean _mapmarkerdragable = false;
public static float _mapxcursor = 0f;
public static float _mapycursor = 0f;
public static anywheresoftware.b4a.objects.collections.List _gpspath = null;
public static String _gpsdir = "";
public static String _gpspathfilename = "";
public static String _gpspathfilenamekml = "";
public static String _gpsfilenameold = "";
public static String _gpspathcomment = "";
public static double _gpspathlatmin = 0;
public static double _gpspathlatmean = 0;
public static double _gpspathlatmax = 0;
public static double _gpspathlngmin = 0;
public static double _gpspathlngmean = 0;
public static double _gpspathlngmax = 0;
public static long _gpsmintime = 0L;
public static float _gpsmindistance = 0f;
public static boolean _gpsdispspeed = false;
public static boolean _gpsdispbearing = false;
public static boolean _gpsdispwindrose = false;
public static anywheresoftware.b4a.gps.LocationWrapper _gpspathpreviuous = null;
public static double _gpspathdisttotprev = 0;
public static long _gpspathtime0 = 0L;
public static double _gpsfilterdelta = 0;
public static double _gpsdistance = 0;
public static anywheresoftware.b4a.objects.collections.List _gpsmarkerindexes = null;
public static boolean _flagscvgpspath0 = false;
public static boolean _flagscvgpspath1 = false;
public static boolean _gps_on = false;
public static boolean _gpspathmodified = false;
public static boolean _map_on = false;
public static boolean _sats_on = false;
public static boolean _setup_on = false;
public static double _mapscalelat = 0;
public static double _mapscalelng = 0;
public static float _mapx = 0f;
public static float _mapy = 0f;
public static int _tilesize = 0;
public static boolean _dispmaptypecontrol = false;
public static int _dispmaptypecontrolidnumber = 0;
public static int _dispmaptypecontrolidindex = 0;
public static String[] _dispmaptypecontrolidtext = null;
public static String _dispmaptypecontrolid = "";
public static boolean _dispmapzoomcontrol = false;
public static int _dispmapzoomcontroltypenumber = 0;
public static int _dispmapzoomcontroltypeindex = 0;
public static String[] _dispmapzoomcontroltypetext = null;
public static String _dispmapzoomcontroltype = "";
public static boolean _dispmapscalecontrol = false;
public static boolean _dispmapcenter = false;
public static boolean _dispmapmarkers = false;
public static boolean _dispmappolylne = false;
public static boolean _showgpsonmap = false;
public static boolean _savegpspath = false;
public static boolean _drawgpspath = false;
public static int _altitudeunitnumber = 0;
public static int _altitudeunitindex = 0;
public static double[] _altitudeunitratio = null;
public static String[] _altitudeunittext = null;
public static int _speedunitnumber = 0;
public static int _speedunitindex = 0;
public static double[] _speedunitratio = null;
public static String[] _speedunittext = null;
public static int _distanceunitnumber = 0;
public static int _distanceunitindex = 0;
public static double[] _distanceunitratio = null;
public static String[] _distanceunittext = null;
public static int _windrosecolor = 0;
public static int _windrosetickscolor = 0;
public static int _windroseneedlecolor = 0;
public anywheresoftware.b4a.objects.PanelWrapper _pnldispgpslatlng = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnltoolbox = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlsavegps = null;
public anywheresoftware.b4a.objects.WebViewWrapper _mapviewer = null;
public uk.co.martinpearman.b4a.webviewextras.WebViewExtras _mapviewerextra = null;
public anywheresoftware.b4a.objects.PanelWrapper _btnsatellites = null;
public anywheresoftware.b4a.objects.PanelWrapper _btngps = null;
public anywheresoftware.b4a.objects.PanelWrapper _btnmap = null;
public anywheresoftware.b4a.objects.PanelWrapper _btnmap1 = null;
public anywheresoftware.b4a.objects.PanelWrapper _btnsetup = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlmap = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlmainbackgound = null;
public anywheresoftware.b4a.objects.PanelWrapper _btngpspath = null;
public anywheresoftware.b4a.objects.PanelWrapper _btngpspath1 = null;
public anywheresoftware.b4a.objects.PanelWrapper _btngpspath2 = null;
public anywheresoftware.b4a.objects.PanelWrapper _btngpspath3 = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnldispgpsaltitude = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnldispgpsspeed = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnldispgpsbearing = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnldispgpswindrose = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnldispgpsneedle = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlgpspathtoolbox = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper _cvsmap = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper _cvsdispgpswindrose = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper _cvsdispgpsneedle = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper _cvsneedle = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _rectmappos = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imvneedle = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper _csvneedle = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmpneedle = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _rectneedlesrc = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _rectneedledest = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _rectdispgpsneedle = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbltooltip = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllatitude = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllongitude = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblaltitude = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblspeed = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblbearing = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllatitude0 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllongitude0 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblaltitude0 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblspeed0 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblbearing0 = null;
public anywheresoftware.b4a.phone.Phone _phone1 = null;
public B4A.GPS.PaiNaiMa.satellites _satellites = null;
public B4A.GPS.PaiNaiMa.setup _setup = null;
public B4A.GPS.PaiNaiMa.gpspaths _gpspaths = null;
public B4A.GPS.PaiNaiMa.gpssave _gpssave = null;
public B4A.GPS.PaiNaiMa.gpsmodule _gpsmodule = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (satellites.mostCurrent != null);
vis = vis | (setup.mostCurrent != null);
vis = vis | (gpspaths.mostCurrent != null);
vis = vis | (gpssave.mostCurrent != null);
return vis;}
public static class _position{
public boolean IsInitialized;
public double Latitude;
public double Longitude;
public void Initialize() {
IsInitialized = true;
Latitude = 0;
Longitude = 0;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static class _gpslocation{
public boolean IsInitialized;
public double Latitude;
public double Longitude;
public double Altitude;
public double Distance;
public double DistTot;
public long Time;
public float Speed;
public float Bearing;
public boolean Marker;
public void Initialize() {
IsInitialized = true;
Latitude = 0;
Longitude = 0;
Altitude = 0;
Distance = 0;
DistTot = 0;
Time = 0L;
Speed = 0f;
Bearing = 0f;
Marker = false;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.keywords.LayoutValues _lv = null;
 //BA.debugLineNum = 169;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 170;BA.debugLine="Activity.LoadLayout(\"main\")";
mostCurrent._activity.LoadLayout("main",mostCurrent.activityBA);
 //BA.debugLineNum = 171;BA.debugLine="If FirstTime = True Then";
if (_firsttime==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 172;BA.debugLine="If File.Exists(File.DirAssets,\"Test1.GPP\") Then";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Test1.GPP")) { 
 //BA.debugLineNum = 173;BA.debugLine="File.Copy(File.DirAssets, \"Test1.GPP\", GPSDir,\"";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Test1.GPP",_gpsdir,"Test1.GPP");
 };
 //BA.debugLineNum = 175;BA.debugLine="If File.Exists(File.DirAssets,\"Test2.GPP\") Then";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Test2.GPP")) { 
 //BA.debugLineNum = 176;BA.debugLine="File.Copy(File.DirAssets, \"Test2.GPP\", GPSDir,\"";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Test2.GPP",_gpsdir,"Test2.GPP");
 };
 //BA.debugLineNum = 178;BA.debugLine="GPS1.Initialize(\"GPS1\")";
_gps1.Initialize("GPS1");
 //BA.debugLineNum = 179;BA.debugLine="MapDispTimer.Initialize(\"MapDispTimer\", 1000)";
_mapdisptimer.Initialize(processBA,"MapDispTimer",(long) (1000));
 //BA.debugLineNum = 180;BA.debugLine="GPSPath.Initialize	' initialize the GPSPath list";
_gpspath.Initialize();
 //BA.debugLineNum = 181;BA.debugLine="MapCenter.Initialize";
_mapcenter.Initialize();
 //BA.debugLineNum = 182;BA.debugLine="MapOldCenter.Initialize";
_mapoldcenter.Initialize();
 //BA.debugLineNum = 183;BA.debugLine="GPSPathPreviuous.Initialize";
_gpspathpreviuous.Initialize();
 //BA.debugLineNum = 185;BA.debugLine="Dim lv As LayoutValues";
_lv = new anywheresoftware.b4a.keywords.LayoutValues();
 //BA.debugLineNum = 186;BA.debugLine="lv = GetDeviceLayoutValues";
_lv = anywheresoftware.b4a.keywords.Common.GetDeviceLayoutValues(mostCurrent.activityBA);
 //BA.debugLineNum = 187;BA.debugLine="TileSize = TileSize * lv.Scale";
_tilesize = (int) (_tilesize*_lv.Scale);
 //BA.debugLineNum = 189;BA.debugLine="If File.Exists(File.DirInternal, \"Setup.txt\") Th";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"Setup.txt")) { 
 //BA.debugLineNum = 190;BA.debugLine="LoadSetup";
_loadsetup();
 };
 //BA.debugLineNum = 193;BA.debugLine="MapCenter.Initialize";
_mapcenter.Initialize();
 //BA.debugLineNum = 194;BA.debugLine="MapOldCenter.Initialize";
_mapoldcenter.Initialize();
 //BA.debugLineNum = 195;BA.debugLine="MapCenter.Latitude = MapDefaultLat";
_mapcenter.setLatitude(_mapdefaultlat);
 //BA.debugLineNum = 196;BA.debugLine="MapCenter.Longitude = MapDefaultLng";
_mapcenter.setLongitude(_mapdefaultlng);
 //BA.debugLineNum = 197;BA.debugLine="MapOldCenter = MapCenter";
_mapoldcenter = _mapcenter;
 //BA.debugLineNum = 199;BA.debugLine="If File.Exists(GPSDir, GPSPathFilename) = True T";
if (anywheresoftware.b4a.keywords.Common.File.Exists(_gpsdir,_gpspathfilename)==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 200;BA.debugLine="GPSModule.LoadPath";
mostCurrent._gpsmodule._loadpath(mostCurrent.activityBA);
 };
 };
 //BA.debugLineNum = 204;BA.debugLine="Activity.Title = ProgName & \" \" & ProgVersion";
mostCurrent._activity.setTitle((Object)(_progname+" "+_progversion));
 //BA.debugLineNum = 205;BA.debugLine="DateTime.DateFormat = \"yyyy.MM.dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy.MM.dd");
 //BA.debugLineNum = 207;BA.debugLine="MapViewer.Top = 0";
mostCurrent._mapviewer.setTop((int) (0));
 //BA.debugLineNum = 208;BA.debugLine="MapViewer.Left = 0";
mostCurrent._mapviewer.setLeft((int) (0));
 //BA.debugLineNum = 209;BA.debugLine="MapViewer.Width = Activity.Width";
mostCurrent._mapviewer.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 210;BA.debugLine="MapViewer.Height = Activity.Height";
mostCurrent._mapviewer.setHeight(mostCurrent._activity.getHeight());
 //BA.debugLineNum = 211;BA.debugLine="MapViewerExtra.addJavascriptInterface(MapViewer,";
mostCurrent._mapviewerextra.addJavascriptInterface(mostCurrent.activityBA,(android.webkit.WebView)(mostCurrent._mapviewer.getObject()),"B4A");
 //BA.debugLineNum = 213;BA.debugLine="pnlMap.Initialize(\"pnlMap\")";
mostCurrent._pnlmap.Initialize(mostCurrent.activityBA,"pnlMap");
 //BA.debugLineNum = 214;BA.debugLine="Activity.AddView(pnlMap, MapViewer.Left, MapViewe";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pnlmap.getObject()),mostCurrent._mapviewer.getLeft(),mostCurrent._mapviewer.getTop(),mostCurrent._mapviewer.getWidth(),mostCurrent._mapviewer.getHeight());
 //BA.debugLineNum = 215;BA.debugLine="pnlMap.Color = Colors.Transparent";
mostCurrent._pnlmap.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 216;BA.debugLine="pnlMap.Visible = False";
mostCurrent._pnlmap.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 217;BA.debugLine="cvsMap.Initialize(pnlMap)";
mostCurrent._cvsmap.Initialize((android.view.View)(mostCurrent._pnlmap.getObject()));
 //BA.debugLineNum = 218;BA.debugLine="rectMapPos.Initialize(0, 10, 0, 10)";
mostCurrent._rectmappos.Initialize((int) (0),(int) (10),(int) (0),(int) (10));
 //BA.debugLineNum = 220;BA.debugLine="pnlToolBox.Top = Activity.Height - pnlToolBox.Hei";
mostCurrent._pnltoolbox.setTop((int) (mostCurrent._activity.getHeight()-mostCurrent._pnltoolbox.getHeight()));
 //BA.debugLineNum = 221;BA.debugLine="pnlToolBox.Left = 0";
mostCurrent._pnltoolbox.setLeft((int) (0));
 //BA.debugLineNum = 223;BA.debugLine="pnlGPSPathToolbox.Top = pnlToolBox.Top - 3 * btnG";
mostCurrent._pnlgpspathtoolbox.setTop((int) (mostCurrent._pnltoolbox.getTop()-3*mostCurrent._btngpspath.getHeight()));
 //BA.debugLineNum = 224;BA.debugLine="pnlGPSPathToolbox.Left = btnGPSPath.Left";
mostCurrent._pnlgpspathtoolbox.setLeft(mostCurrent._btngpspath.getLeft());
 //BA.debugLineNum = 226;BA.debugLine="btnMap1.Top = pnlToolBox.Top - btnMap1.Height";
mostCurrent._btnmap1.setTop((int) (mostCurrent._pnltoolbox.getTop()-mostCurrent._btnmap1.getHeight()));
 //BA.debugLineNum = 227;BA.debugLine="btnMap1.Left = btnMap.Left";
mostCurrent._btnmap1.setLeft(mostCurrent._btnmap.getLeft());
 //BA.debugLineNum = 229;BA.debugLine="pnlMainBackgound.Left = (100%x - pnlMainBackgound";
mostCurrent._pnlmainbackgound.setLeft((int) ((anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-mostCurrent._pnlmainbackgound.getWidth())/(double)2));
 //BA.debugLineNum = 230;BA.debugLine="pnlMainBackgound.Top = (pnlToolBox.Top + pnlDispG";
mostCurrent._pnlmainbackgound.setTop((int) ((mostCurrent._pnltoolbox.getTop()+mostCurrent._pnldispgpslatlng.getTop()+mostCurrent._pnldispgpslatlng.getHeight()-mostCurrent._pnlmainbackgound.getHeight())/(double)2));
 //BA.debugLineNum = 231;BA.debugLine="pnlMainBackgound.SendToBack";
mostCurrent._pnlmainbackgound.SendToBack();
 //BA.debugLineNum = 233;BA.debugLine="imvNeedle.Initialize(\"\")";
mostCurrent._imvneedle.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 234;BA.debugLine="Activity.AddView(imvNeedle, 0, 0, 20dip, 100dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._imvneedle.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)));
 //BA.debugLineNum = 236;BA.debugLine="lblToolTip.Top = 0";
mostCurrent._lbltooltip.setTop((int) (0));
 //BA.debugLineNum = 237;BA.debugLine="lblToolTip.Left = 0";
mostCurrent._lbltooltip.setLeft((int) (0));
 //BA.debugLineNum = 238;BA.debugLine="lblToolTip.BringToFront";
mostCurrent._lbltooltip.BringToFront();
 //BA.debugLineNum = 239;BA.debugLine="lblToolTip.Visible = False";
mostCurrent._lbltooltip.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 241;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
int _answ = 0;
String _txt = "";
 //BA.debugLineNum = 324;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 325;BA.debugLine="Dim Answ As Int";
_answ = 0;
 //BA.debugLineNum = 326;BA.debugLine="Dim txt As String";
_txt = "";
 //BA.debugLineNum = 328;BA.debugLine="Activity.Title = ProgName & \" : \" & ProgVersion";
mostCurrent._activity.setTitle((Object)(_progname+" : "+_progversion));
 //BA.debugLineNum = 330;BA.debugLine="Select KeyCode";
switch (BA.switchObjectToInt(_keycode,anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK)) {
case 0: {
 //BA.debugLineNum = 332;BA.debugLine="txt = \"ต้องการออกจากโปรแกรมหรือไม่\"";
_txt = "ต้องการออกจากโปรแกรมหรือไม่";
 //BA.debugLineNum = 333;BA.debugLine="Answ = Msgbox2(txt, \"โปรดเลือก\", \"ตกลง\", \"\", \"ยก";
_answ = anywheresoftware.b4a.keywords.Common.Msgbox2(_txt,"โปรดเลือก","ตกลง","","ยกเลิก",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 334;BA.debugLine="If Answ = DialogResponse.NEGATIVE Then";
if (_answ==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
 //BA.debugLineNum = 335;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 337;BA.debugLine="GPS1.Stop";
_gps1.Stop();
 //BA.debugLineNum = 338;BA.debugLine="SaveSetup";
_savesetup();
 //BA.debugLineNum = 339;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 };
 break; }
}
;
 //BA.debugLineNum = 342;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 317;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 318;BA.debugLine="If UserClosed And GPS_On = True Then";
if (_userclosed && _gps_on==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 319;BA.debugLine="GPS1.Stop";
_gps1.Stop();
 };
 //BA.debugLineNum = 321;BA.debugLine="SaveSetup";
_savesetup();
 //BA.debugLineNum = 322;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
String _txt = "";
float _pnltop = 0f;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bmd = null;
 //BA.debugLineNum = 243;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 244;BA.debugLine="Dim txt As String";
_txt = "";
 //BA.debugLineNum = 245;BA.debugLine="Dim pnlTop As Float";
_pnltop = 0f;
 //BA.debugLineNum = 247;BA.debugLine="If DispMapTypeControl = True Or DispMapScaleContr";
if (_dispmaptypecontrol==anywheresoftware.b4a.keywords.Common.True || _dispmapscalecontrol==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 248;BA.debugLine="pnlTop = 40dip";
_pnltop = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)));
 }else {
 //BA.debugLineNum = 250;BA.debugLine="pnlTop = 0";
_pnltop = (float) (0);
 };
 //BA.debugLineNum = 252;BA.debugLine="pnlDispGPSLatLng.Left = 0";
mostCurrent._pnldispgpslatlng.setLeft((int) (0));
 //BA.debugLineNum = 253;BA.debugLine="pnlDispGPSLatLng.Top = pnlTop";
mostCurrent._pnldispgpslatlng.setTop((int) (_pnltop));
 //BA.debugLineNum = 254;BA.debugLine="pnlDispGPSAltitude.Top = pnlDispGPSLatLng.Top";
mostCurrent._pnldispgpsaltitude.setTop(mostCurrent._pnldispgpslatlng.getTop());
 //BA.debugLineNum = 255;BA.debugLine="pnlDispGPSBearing.Top = pnlDispGPSLatLng.Top + pn";
mostCurrent._pnldispgpsbearing.setTop((int) (mostCurrent._pnldispgpslatlng.getTop()+mostCurrent._pnldispgpslatlng.getHeight()));
 //BA.debugLineNum = 256;BA.debugLine="pnlDispGPSSpeed.Top = pnlDispGPSLatLng.Top + pnlD";
mostCurrent._pnldispgpsspeed.setTop((int) (mostCurrent._pnldispgpslatlng.getTop()+mostCurrent._pnldispgpslatlng.getHeight()));
 //BA.debugLineNum = 257;BA.debugLine="pnlDispGPSWindrose.Height = pnlToolBox.Top - pnlD";
mostCurrent._pnldispgpswindrose.setHeight((int) (mostCurrent._pnltoolbox.getTop()-mostCurrent._pnldispgpsspeed.getTop()-mostCurrent._pnldispgpsspeed.getHeight()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 258;BA.debugLine="pnlDispGPSWindrose.Top = pnlDispGPSSpeed.Top + pn";
mostCurrent._pnldispgpswindrose.setTop((int) (mostCurrent._pnldispgpsspeed.getTop()+mostCurrent._pnldispgpsspeed.getHeight()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 259;BA.debugLine="pnlDispGPSWindrose.Width = pnlDispGPSWindrose.Hei";
mostCurrent._pnldispgpswindrose.setWidth(mostCurrent._pnldispgpswindrose.getHeight());
 //BA.debugLineNum = 260;BA.debugLine="pnlDispGPSNeedle.Width = pnlDispGPSWindrose.Heigh";
mostCurrent._pnldispgpsneedle.setWidth(mostCurrent._pnldispgpswindrose.getHeight());
 //BA.debugLineNum = 261;BA.debugLine="pnlDispGPSNeedle.Height = pnlDispGPSWindrose.Heig";
mostCurrent._pnldispgpsneedle.setHeight(mostCurrent._pnldispgpswindrose.getHeight());
 //BA.debugLineNum = 262;BA.debugLine="pnlDispGPSWindrose.Left = (Activity.Width - pnlDi";
mostCurrent._pnldispgpswindrose.setLeft((int) ((mostCurrent._activity.getWidth()-mostCurrent._pnldispgpswindrose.getWidth())/(double)2));
 //BA.debugLineNum = 263;BA.debugLine="pnlDispGPSWindrose.Visible = False";
mostCurrent._pnldispgpswindrose.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 264;BA.debugLine="cvsDispGPSWindrose.Initialize(pnlDispGPSWindrose)";
mostCurrent._cvsdispgpswindrose.Initialize((android.view.View)(mostCurrent._pnldispgpswindrose.getObject()));
 //BA.debugLineNum = 265;BA.debugLine="cvsDispGPSNeedle.Initialize(pnlDispGPSNeedle)";
mostCurrent._cvsdispgpsneedle.Initialize((android.view.View)(mostCurrent._pnldispgpsneedle.getObject()));
 //BA.debugLineNum = 266;BA.debugLine="imvNeedle.Visible = False";
mostCurrent._imvneedle.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 268;BA.debugLine="If GPS_On = True Then";
if (_gps_on==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 269;BA.debugLine="GPS1.Start(GPSMinTime, GPSMinDistance)";
_gps1.Start(processBA,_gpsmintime,_gpsmindistance);
 };
 //BA.debugLineNum = 272;BA.debugLine="If MapCenter.IsInitialized = False Then";
if (_mapcenter.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 273;BA.debugLine="MapCenter.Initialize";
_mapcenter.Initialize();
 //BA.debugLineNum = 274;BA.debugLine="MapCenter.Latitude = MapDefaultLat";
_mapcenter.setLatitude(_mapdefaultlat);
 //BA.debugLineNum = 275;BA.debugLine="MapCenter.Longitude = MapDefaultLng";
_mapcenter.setLongitude(_mapdefaultlng);
 };
 //BA.debugLineNum = 278;BA.debugLine="If GPSPath.Size > 0 And MapZoomCalculated = False";
if (_gpspath.getSize()>0 && _mapzoomcalculated==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 279;BA.debugLine="CalcMapZoom";
_calcmapzoom();
 };
 //BA.debugLineNum = 283;BA.debugLine="Dim bmd As BitmapDrawable";
_bmd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 284;BA.debugLine="If Map_On = False Then";
if (_map_on==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 285;BA.debugLine="If MapSetDraggable = True Then";
if (_mapsetdraggable==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 286;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btnm";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnmapmove0.png").getObject()));
 }else {
 //BA.debugLineNum = 288;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btnm";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnmapcoord0.png").getObject()));
 };
 }else {
 //BA.debugLineNum = 291;BA.debugLine="If MapSetDraggable = True Then";
if (_mapsetdraggable==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 292;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btnm";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnmapmove1.png").getObject()));
 }else {
 //BA.debugLineNum = 294;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btnm";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnmapcoord1.png").getObject()));
 };
 //BA.debugLineNum = 296;BA.debugLine="MapShow";
_mapshow();
 };
 //BA.debugLineNum = 298;BA.debugLine="btnMap.Background = bmd";
mostCurrent._btnmap.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 //BA.debugLineNum = 301;BA.debugLine="Dim bmd As BitmapDrawable";
_bmd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 302;BA.debugLine="If DispMapMarkers = True Then";
if (_dispmapmarkers==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 303;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btngp";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpspathmarker0.png").getObject()));
 }else if(_dispmappolylne==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 305;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btngp";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpspathline0.png").getObject()));
 }else {
 //BA.debugLineNum = 307;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btngp";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpspath0.png").getObject()));
 };
 //BA.debugLineNum = 309;BA.debugLine="btnGPSPath.Background = bmd";
mostCurrent._btngpspath.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 //BA.debugLineNum = 311;BA.debugLine="Activity.Title = ProgName & \" : \" & ProgVersion";
mostCurrent._activity.setTitle((Object)(_progname+" : "+_progversion));
 //BA.debugLineNum = 313;BA.debugLine="InitWindrose";
_initwindrose();
 //BA.debugLineNum = 314;BA.debugLine="InitNeedle";
_initneedle();
 //BA.debugLineNum = 315;BA.debugLine="End Sub";
return "";
}
public static String  _btngps_touch(int _action,float _x,float _y) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bmd = null;
 //BA.debugLineNum = 545;BA.debugLine="Sub btnGPS_Touch(Action As Int, x As Float, y As F";
 //BA.debugLineNum = 546;BA.debugLine="Dim bmd As BitmapDrawable";
_bmd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 548;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,mostCurrent._activity.ACTION_DOWN,mostCurrent._activity.ACTION_UP)) {
case 0: {
 //BA.debugLineNum = 550;BA.debugLine="If GPS_On = False Then";
if (_gps_on==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 551;BA.debugLine="ToolTip(\"ใช้งานจีพีเอส\")";
_tooltip("ใช้งานจีพีเอส");
 //BA.debugLineNum = 552;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btng";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngps1.png").getObject()));
 }else {
 //BA.debugLineNum = 554;BA.debugLine="ToolTip(\"หยุดจีพีเอส\")";
_tooltip("หยุดจีพีเอส");
 //BA.debugLineNum = 555;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btng";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngps0.png").getObject()));
 };
 //BA.debugLineNum = 557;BA.debugLine="btnGPS.Background = bmd";
mostCurrent._btngps.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 break; }
case 1: {
 //BA.debugLineNum = 559;BA.debugLine="If x > 0 And x < btnGPS.Width  And y > 0 And y <";
if (_x>0 && _x<mostCurrent._btngps.getWidth() && _y>0 && _y<mostCurrent._btngps.getHeight()) { 
 //BA.debugLineNum = 560;BA.debugLine="GPS_On = Not(GPS_On)";
_gps_on = anywheresoftware.b4a.keywords.Common.Not(_gps_on);
 //BA.debugLineNum = 561;BA.debugLine="If GPS_On = False Then";
if (_gps_on==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 562;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btn";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngps0.png").getObject()));
 //BA.debugLineNum = 563;BA.debugLine="GPS1.Stop";
_gps1.Stop();
 //BA.debugLineNum = 564;BA.debugLine="PhoneAwake.ReleaseKeepAlive";
_phoneawake.ReleaseKeepAlive();
 //BA.debugLineNum = 565;BA.debugLine="If GPSPath.Size > 0 Then";
if (_gpspath.getSize()>0) { 
 //BA.debugLineNum = 566;BA.debugLine="cvsMap.DrawRect(rectMapPos, Colors.Transparen";
mostCurrent._cvsmap.DrawRect((android.graphics.Rect)(mostCurrent._rectmappos.getObject()),anywheresoftware.b4a.keywords.Common.Colors.Transparent,anywheresoftware.b4a.keywords.Common.True,(float) (1));
 //BA.debugLineNum = 567;BA.debugLine="pnlMap.Invalidate2(rectMapPos)";
mostCurrent._pnlmap.Invalidate2((android.graphics.Rect)(mostCurrent._rectmappos.getObject()));
 //BA.debugLineNum = 569;BA.debugLine="If SaveGPSPath = True Then";
if (_savegpspath==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 570;BA.debugLine="StartActivity(GPSSave)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._gpssave.getObject()));
 };
 }else {
 //BA.debugLineNum = 573;BA.debugLine="Msgbox(\"ไม่มมีจุดเส้นทาง\", \"เส้นทางที่บันทึก\"";
anywheresoftware.b4a.keywords.Common.Msgbox("ไม่มมีจุดเส้นทาง","เส้นทางที่บันทึก",mostCurrent.activityBA);
 //BA.debugLineNum = 574;BA.debugLine="GPSModule.LoadPath";
mostCurrent._gpsmodule._loadpath(mostCurrent.activityBA);
 //BA.debugLineNum = 575;BA.debugLine="MapZoomLevel = MapZoomLevelOld";
_mapzoomlevel = _mapzoomlevelold;
 //BA.debugLineNum = 576;BA.debugLine="MapCenter = MapOldCenter";
_mapcenter = _mapoldcenter;
 //BA.debugLineNum = 577;BA.debugLine="MapShow";
_mapshow();
 };
 }else {
 //BA.debugLineNum = 580;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btn";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngps1.png").getObject()));
 //BA.debugLineNum = 581;BA.debugLine="If GPS1.GPSEnabled = False Then";
if (_gps1.getGPSEnabled()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 582;BA.debugLine="ToastMessageShow(\"โปรดเปิดจีพีเอส\" & CRLF & \"";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("โปรดเปิดจีพีเอส"+anywheresoftware.b4a.keywords.Common.CRLF+"กดปุ่ม กลับ เพื่อดำเนินการต่อ",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 583;BA.debugLine="StartActivity(GPS1.LocationSettingsIntent)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_gps1.getLocationSettingsIntent()));
 };
 //BA.debugLineNum = 585;BA.debugLine="GPSPath.Initialize";
_gpspath.Initialize();
 //BA.debugLineNum = 586;BA.debugLine="If Map_On Then";
if (_map_on) { 
 //BA.debugLineNum = 587;BA.debugLine="MapZoomLevelOld = MapZoomLevel";
_mapzoomlevelold = _mapzoomlevel;
 //BA.debugLineNum = 588;BA.debugLine="MapOldCenter = MapCenter";
_mapoldcenter = _mapcenter;
 //BA.debugLineNum = 589;BA.debugLine="MapZoomLevel = MapDefaultZoomLevel";
_mapzoomlevel = _mapdefaultzoomlevel;
 //BA.debugLineNum = 590;BA.debugLine="MapCenter.Latitude = MapDefaultLat";
_mapcenter.setLatitude(_mapdefaultlat);
 //BA.debugLineNum = 591;BA.debugLine="MapCenter.Longitude = MapDefaultLng";
_mapcenter.setLongitude(_mapdefaultlng);
 //BA.debugLineNum = 592;BA.debugLine="MapShow";
_mapshow();
 };
 //BA.debugLineNum = 594;BA.debugLine="PhoneAwake.KeepAlive(False)";
_phoneawake.KeepAlive(processBA,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 595;BA.debugLine="lblLatitude.Text = \"- - -\"";
mostCurrent._lbllatitude.setText((Object)("- - -"));
 //BA.debugLineNum = 596;BA.debugLine="lblLongitude.Text = \"- - -\"";
mostCurrent._lbllongitude.setText((Object)("- - -"));
 //BA.debugLineNum = 597;BA.debugLine="lblAltitude.Text = \"- - -\"";
mostCurrent._lblaltitude.setText((Object)("- - -"));
 //BA.debugLineNum = 598;BA.debugLine="lblBearing.Text = \"- - -\"";
mostCurrent._lblbearing.setText((Object)("- - -"));
 //BA.debugLineNum = 599;BA.debugLine="lblSpeed.Text = \"- - -\"";
mostCurrent._lblspeed.setText((Object)("- - -"));
 //BA.debugLineNum = 601;BA.debugLine="GPS1.Start(GPSMinTime, GPSMinDistance)";
_gps1.Start(processBA,_gpsmintime,_gpsmindistance);
 };
 //BA.debugLineNum = 603;BA.debugLine="pnlDispGPSLatLng.Visible = GPS_On";
mostCurrent._pnldispgpslatlng.setVisible(_gps_on);
 //BA.debugLineNum = 604;BA.debugLine="pnlDispGPSAltitude.Visible = GPS_On";
mostCurrent._pnldispgpsaltitude.setVisible(_gps_on);
 //BA.debugLineNum = 605;BA.debugLine="pnlDispGPSSpeed.Visible = GPS_On And GPSDispSpe";
mostCurrent._pnldispgpsspeed.setVisible(_gps_on && _gpsdispspeed);
 //BA.debugLineNum = 606;BA.debugLine="pnlDispGPSBearing.Visible = GPS_On And GPSDispB";
mostCurrent._pnldispgpsbearing.setVisible(_gps_on && _gpsdispbearing);
 //BA.debugLineNum = 607;BA.debugLine="pnlDispGPSWindrose.Visible = GPS_On And GPSDisp";
mostCurrent._pnldispgpswindrose.setVisible(_gps_on && _gpsdispwindrose);
 //BA.debugLineNum = 608;BA.debugLine="pnlMainBackgound.Visible = Not(pnlDispGPSWindro";
mostCurrent._pnlmainbackgound.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._pnldispgpswindrose.getVisible()));
 //BA.debugLineNum = 609;BA.debugLine="btnGPS.Background = bmd";
mostCurrent._btngps.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 };
 //BA.debugLineNum = 611;BA.debugLine="ToolTip(\"\")";
_tooltip("");
 break; }
}
;
 //BA.debugLineNum = 613;BA.debugLine="End Sub";
return "";
}
public static String  _btngpspath_touch(int _action,float _x,float _y) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bmd = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bmd1 = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bmd2 = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bmd3 = null;
 //BA.debugLineNum = 709;BA.debugLine="Sub btnGPSPath_Touch(Action As Int, x As Float, y";
 //BA.debugLineNum = 710;BA.debugLine="Dim bmd, bmd1, bmd2, bmd3 As BitmapDrawable";
_bmd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_bmd1 = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_bmd2 = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_bmd3 = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 712;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,mostCurrent._activity.ACTION_DOWN,mostCurrent._activity.ACTION_MOVE,mostCurrent._activity.ACTION_UP)) {
case 0: {
 //BA.debugLineNum = 714;BA.debugLine="ToolTip(\"แสดงจุดเส้นทาง\")";
_tooltip("แสดงจุดเส้นทาง");
 //BA.debugLineNum = 715;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btngp";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpspath1.png").getObject()));
 //BA.debugLineNum = 716;BA.debugLine="btnGPSPath.Background = bmd";
mostCurrent._btngpspath.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 //BA.debugLineNum = 717;BA.debugLine="If Map_On = True Then";
if (_map_on==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 718;BA.debugLine="pnlGPSPathToolbox.Visible = True";
mostCurrent._pnlgpspathtoolbox.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 break; }
case 1: {
 //BA.debugLineNum = 721;BA.debugLine="If x > 0 And x < btnGPSPath.Width And y > -3 * b";
if (_x>0 && _x<mostCurrent._btngpspath.getWidth() && _y>-3*mostCurrent._btngpspath.getHeight() && _y<-2*mostCurrent._btngpspath.getHeight()) { 
 //BA.debugLineNum = 722;BA.debugLine="bmd3.Initialize(LoadBitmap(File.DirAssets, \"bt";
_bmd3.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpspathmarker1.png").getObject()));
 //BA.debugLineNum = 723;BA.debugLine="bmd2.Initialize(LoadBitmap(File.DirAssets, \"bt";
_bmd2.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpspathline0.png").getObject()));
 //BA.debugLineNum = 724;BA.debugLine="bmd1.Initialize(LoadBitmap(File.DirAssets, \"bt";
_bmd1.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpspath0.png").getObject()));
 //BA.debugLineNum = 725;BA.debugLine="ToolTip(\"แสดงเส้นและจุด\")";
_tooltip("แสดงเส้นและจุด");
 }else if(_x>0 && _x<mostCurrent._btngpspath.getWidth() && _y>-2*mostCurrent._btngpspath.getHeight() && _y<-mostCurrent._btngpspath.getHeight()) { 
 //BA.debugLineNum = 727;BA.debugLine="bmd3.Initialize(LoadBitmap(File.DirAssets, \"bt";
_bmd3.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpspathmarker0.png").getObject()));
 //BA.debugLineNum = 728;BA.debugLine="bmd2.Initialize(LoadBitmap(File.DirAssets, \"bt";
_bmd2.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpspathline1.png").getObject()));
 //BA.debugLineNum = 729;BA.debugLine="bmd1.Initialize(LoadBitmap(File.DirAssets, \"bt";
_bmd1.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpspath0.png").getObject()));
 //BA.debugLineNum = 730;BA.debugLine="ToolTip(\"แสดงเส้นอย่างเดียว\")";
_tooltip("แสดงเส้นอย่างเดียว");
 }else if(_x>0 && _x<mostCurrent._btngpspath.getWidth() && _y>-mostCurrent._btngpspath.getHeight() && _y<0) { 
 //BA.debugLineNum = 732;BA.debugLine="bmd3.Initialize(LoadBitmap(File.DirAssets, \"bt";
_bmd3.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpspathmarker0.png").getObject()));
 //BA.debugLineNum = 733;BA.debugLine="bmd2.Initialize(LoadBitmap(File.DirAssets, \"bt";
_bmd2.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpspathline0.png").getObject()));
 //BA.debugLineNum = 734;BA.debugLine="bmd1.Initialize(LoadBitmap(File.DirAssets, \"bt";
_bmd1.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpspath1.png").getObject()));
 //BA.debugLineNum = 735;BA.debugLine="ToolTip(\"ไม่แสดงทั้งเส้นและจุด\")";
_tooltip("ไม่แสดงทั้งเส้นและจุด");
 }else {
 //BA.debugLineNum = 737;BA.debugLine="bmd3.Initialize(LoadBitmap(File.DirAssets, \"btn";
_bmd3.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpspathmarker0.png").getObject()));
 //BA.debugLineNum = 738;BA.debugLine="bmd2.Initialize(LoadBitmap(File.DirAssets, \"btn";
_bmd2.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpspathline0.png").getObject()));
 //BA.debugLineNum = 739;BA.debugLine="bmd1.Initialize(LoadBitmap(File.DirAssets, \"btn";
_bmd1.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpspath0.png").getObject()));
 //BA.debugLineNum = 740;BA.debugLine="ToolTip(\"แสดงจุดเส้นทาง\")";
_tooltip("แสดงจุดเส้นทาง");
 };
 //BA.debugLineNum = 742;BA.debugLine="btnGPSPath3.Background = bmd3";
mostCurrent._btngpspath3.setBackground((android.graphics.drawable.Drawable)(_bmd3.getObject()));
 //BA.debugLineNum = 743;BA.debugLine="btnGPSPath2.Background = bmd2";
mostCurrent._btngpspath2.setBackground((android.graphics.drawable.Drawable)(_bmd2.getObject()));
 //BA.debugLineNum = 744;BA.debugLine="btnGPSPath1.Background = bmd1";
mostCurrent._btngpspath1.setBackground((android.graphics.drawable.Drawable)(_bmd1.getObject()));
 break; }
case 2: {
 //BA.debugLineNum = 746;BA.debugLine="If x > 0 And x < btnGPSPath.Width Then";
if (_x>0 && _x<mostCurrent._btngpspath.getWidth()) { 
 //BA.debugLineNum = 747;BA.debugLine="If y > -3 * btnGPSPath.Height And y < -2 * btnG";
if (_y>-3*mostCurrent._btngpspath.getHeight() && _y<-2*mostCurrent._btngpspath.getHeight()) { 
 //BA.debugLineNum = 748;BA.debugLine="DispMapMarkers = True";
_dispmapmarkers = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 749;BA.debugLine="DispMapPolylne = True";
_dispmappolylne = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 750;BA.debugLine="MapShow";
_mapshow();
 }else if(_y>-2*mostCurrent._btngpspath.getHeight() && _y<-mostCurrent._btngpspath.getHeight()) { 
 //BA.debugLineNum = 752;BA.debugLine="DispMapMarkers = False";
_dispmapmarkers = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 753;BA.debugLine="DispMapPolylne = True";
_dispmappolylne = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 754;BA.debugLine="MapShow";
_mapshow();
 }else if(_y>-mostCurrent._btngpspath.getHeight() && _y<0) { 
 //BA.debugLineNum = 756;BA.debugLine="DispMapMarkers = False";
_dispmapmarkers = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 757;BA.debugLine="DispMapPolylne = False";
_dispmappolylne = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 758;BA.debugLine="MapShow";
_mapshow();
 }else if(_y>0 && _y<mostCurrent._btngpspath.getHeight()) { 
 //BA.debugLineNum = 760;BA.debugLine="StartActivity(\"GPSPaths\")";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)("GPSPaths"));
 };
 };
 //BA.debugLineNum = 763;BA.debugLine="If DispMapMarkers = True Then";
if (_dispmapmarkers==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 764;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btng";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpspathmarker0.png").getObject()));
 }else if(_dispmappolylne==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 766;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btng";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpspathline0.png").getObject()));
 }else {
 //BA.debugLineNum = 768;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btng";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpspath0.png").getObject()));
 };
 //BA.debugLineNum = 771;BA.debugLine="ToolTip(\"\")";
_tooltip("");
 //BA.debugLineNum = 772;BA.debugLine="pnlGPSPathToolbox.Visible = False";
mostCurrent._pnlgpspathtoolbox.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 773;BA.debugLine="btnGPSPath.Background = bmd";
mostCurrent._btngpspath.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 break; }
}
;
 //BA.debugLineNum = 775;BA.debugLine="End Sub";
return "";
}
public static String  _btnmap_touch(int _action,float _x,float _y) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bmd = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bmd1 = null;
 //BA.debugLineNum = 615;BA.debugLine="Sub btnMap_Touch(Action As Int, x As Float, y As F";
 //BA.debugLineNum = 616;BA.debugLine="Dim bmd, bmd1 As BitmapDrawable";
_bmd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_bmd1 = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 618;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,mostCurrent._activity.ACTION_DOWN,mostCurrent._activity.ACTION_MOVE,mostCurrent._activity.ACTION_UP)) {
case 0: {
 //BA.debugLineNum = 620;BA.debugLine="If Map_On = False Then";
if (_map_on==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 621;BA.debugLine="ToolTip(\"แสดงแผนที่\")";
_tooltip("แสดงแผนที่");
 //BA.debugLineNum = 622;BA.debugLine="If MapSetDraggable = True Then";
if (_mapsetdraggable==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 623;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btn";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnmapmove1.png").getObject()));
 }else {
 //BA.debugLineNum = 625;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btn";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnmapcoord1.png").getObject()));
 };
 }else {
 //BA.debugLineNum = 628;BA.debugLine="ToolTip(\"ซ่อนแผนที่\")";
_tooltip("ซ่อนแผนที่");
 //BA.debugLineNum = 629;BA.debugLine="If MapSetDraggable = True Then";
if (_mapsetdraggable==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 630;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btn";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnmapmove0.png").getObject()));
 //BA.debugLineNum = 631;BA.debugLine="bmd1.Initialize(LoadBitmap(File.DirAssets, \"bt";
_bmd1.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnmapcoord0.png").getObject()));
 }else {
 //BA.debugLineNum = 633;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btn";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnmapcoord0.png").getObject()));
 //BA.debugLineNum = 634;BA.debugLine="bmd1.Initialize(LoadBitmap(File.DirAssets, \"bt";
_bmd1.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnmapmove0.png").getObject()));
 };
 //BA.debugLineNum = 636;BA.debugLine="btnMap1.Background = bmd1";
mostCurrent._btnmap1.setBackground((android.graphics.drawable.Drawable)(_bmd1.getObject()));
 //BA.debugLineNum = 637;BA.debugLine="btnMap1.Visible = True";
mostCurrent._btnmap1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 639;BA.debugLine="btnMap.Background = bmd";
mostCurrent._btnmap.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 //BA.debugLineNum = 640;BA.debugLine="DoEvents";
anywheresoftware.b4a.keywords.Common.DoEvents();
 break; }
case 1: {
 //BA.debugLineNum = 642;BA.debugLine="If x > 0 And x < btnMap.Width  And y > -btnMap.H";
if (_x>0 && _x<mostCurrent._btnmap.getWidth() && _y>-mostCurrent._btnmap.getHeight() && _y<0) { 
 //BA.debugLineNum = 643;BA.debugLine="If MapSetDraggable = True Then";
if (_mapsetdraggable==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 644;BA.debugLine="bmd1.Initialize(LoadBitmap(File.DirAssets, \"bt";
_bmd1.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnmapcoord1.png").getObject()));
 //BA.debugLineNum = 645;BA.debugLine="ToolTip(\"แสดงตำแหน่ง\")";
_tooltip("แสดงตำแหน่ง");
 }else {
 //BA.debugLineNum = 647;BA.debugLine="bmd1.Initialize(LoadBitmap(File.DirAssets, \"bt";
_bmd1.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnmapmove1.png").getObject()));
 //BA.debugLineNum = 648;BA.debugLine="ToolTip(\"เลื่อนไปมาได้\")";
_tooltip("เลื่อนไปมาได้");
 };
 }else if(_x>0 && _x<mostCurrent._btnmap.getWidth() && _y>0 && _y<mostCurrent._btnmap.getHeight()) { 
 //BA.debugLineNum = 651;BA.debugLine="If MapSetDraggable = True Then";
if (_mapsetdraggable==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 652;BA.debugLine="bmd1.Initialize(LoadBitmap(File.DirAssets, \"bt";
_bmd1.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnmapcoord0.png").getObject()));
 }else {
 //BA.debugLineNum = 654;BA.debugLine="bmd1.Initialize(LoadBitmap(File.DirAssets, \"bt";
_bmd1.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnmapmove0.png").getObject()));
 };
 //BA.debugLineNum = 656;BA.debugLine="If Map_On = False Then";
if (_map_on==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 657;BA.debugLine="ToolTip(\"แสดงแผนที่\")";
_tooltip("แสดงแผนที่");
 }else {
 //BA.debugLineNum = 659;BA.debugLine="ToolTip(\"ซอนแผนที่\")";
_tooltip("ซอนแผนที่");
 };
 }else {
 //BA.debugLineNum = 662;BA.debugLine="If MapSetDraggable = True Then";
if (_mapsetdraggable==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 663;BA.debugLine="bmd1.Initialize(LoadBitmap(File.DirAssets, \"bt";
_bmd1.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnmapcoord0.png").getObject()));
 }else {
 //BA.debugLineNum = 665;BA.debugLine="bmd1.Initialize(LoadBitmap(File.DirAssets, \"bt";
_bmd1.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnmapmove0.png").getObject()));
 };
 //BA.debugLineNum = 667;BA.debugLine="If Map_On = False Then";
if (_map_on==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 668;BA.debugLine="ToolTip(\"แสดงแผนที่\")";
_tooltip("แสดงแผนที่");
 }else {
 //BA.debugLineNum = 670;BA.debugLine="ToolTip(\"ซ่อนแผนที่\")";
_tooltip("ซ่อนแผนที่");
 };
 };
 //BA.debugLineNum = 673;BA.debugLine="btnMap1.Background = bmd1";
mostCurrent._btnmap1.setBackground((android.graphics.drawable.Drawable)(_bmd1.getObject()));
 break; }
case 2: {
 //BA.debugLineNum = 675;BA.debugLine="If x > 0 And x < btnMap.Width  And y > -btnMap.H";
if (_x>0 && _x<mostCurrent._btnmap.getWidth() && _y>-mostCurrent._btnmap.getHeight() && _y<0) { 
 //BA.debugLineNum = 676;BA.debugLine="MapSetDraggable = Not(MapSetDraggable)";
_mapsetdraggable = anywheresoftware.b4a.keywords.Common.Not(_mapsetdraggable);
 }else if(_x>0 && _x<mostCurrent._btnmap.getWidth() && _y>0 && _y<mostCurrent._btnmap.getHeight()) { 
 //BA.debugLineNum = 678;BA.debugLine="Map_On = Not(Map_On)";
_map_on = anywheresoftware.b4a.keywords.Common.Not(_map_on);
 };
 //BA.debugLineNum = 681;BA.debugLine="btnMap1.Visible = False";
mostCurrent._btnmap1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 682;BA.debugLine="ToolTip(\"\")";
_tooltip("");
 //BA.debugLineNum = 683;BA.debugLine="DoEvents";
anywheresoftware.b4a.keywords.Common.DoEvents();
 //BA.debugLineNum = 685;BA.debugLine="If Map_On = False Then";
if (_map_on==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 686;BA.debugLine="If MapSetDraggable = True Then";
if (_mapsetdraggable==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 687;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btn";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnmapmove0.png").getObject()));
 }else {
 //BA.debugLineNum = 689;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btn";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnmapcoord0.png").getObject()));
 };
 //BA.debugLineNum = 691;BA.debugLine="MapViewer.Visible = False";
mostCurrent._mapviewer.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 692;BA.debugLine="pnlMap.Visible = False";
mostCurrent._pnlmap.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 693;BA.debugLine="btnMap.Background = bmd";
mostCurrent._btnmap.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 }else {
 //BA.debugLineNum = 695;BA.debugLine="If MapSetDraggable = True Then";
if (_mapsetdraggable==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 696;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btn";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnmapmove1.png").getObject()));
 }else {
 //BA.debugLineNum = 698;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btn";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnmapcoord1.png").getObject()));
 };
 //BA.debugLineNum = 700;BA.debugLine="btnMap.Background = bmd";
mostCurrent._btnmap.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 //BA.debugLineNum = 701;BA.debugLine="DoEvents";
anywheresoftware.b4a.keywords.Common.DoEvents();
 //BA.debugLineNum = 702;BA.debugLine="MapShow";
_mapshow();
 //BA.debugLineNum = 703;BA.debugLine="MapViewer.Visible = True";
mostCurrent._mapviewer.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 704;BA.debugLine="pnlMap.Visible = True";
mostCurrent._pnlmap.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 break; }
}
;
 //BA.debugLineNum = 707;BA.debugLine="End Sub";
return "";
}
public static String  _btnsatellites_touch(int _action,float _x,float _y) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bmd = null;
 //BA.debugLineNum = 505;BA.debugLine="Sub btnSatellites_Touch(Action As Int, x As Float,";
 //BA.debugLineNum = 506;BA.debugLine="Dim bmd As BitmapDrawable";
_bmd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 508;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,mostCurrent._activity.ACTION_DOWN,mostCurrent._activity.ACTION_UP)) {
case 0: {
 //BA.debugLineNum = 510;BA.debugLine="ToolTip(\"แสดงดาวเทียม\")";
_tooltip("แสดงดาวเทียม");
 //BA.debugLineNum = 511;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btnsa";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnsatellites1.png").getObject()));
 //BA.debugLineNum = 512;BA.debugLine="btnSatellites.Background = bmd";
mostCurrent._btnsatellites.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 break; }
case 1: {
 //BA.debugLineNum = 514;BA.debugLine="If x > 0 And x < btnSatellites.Width  And y > 0";
if (_x>0 && _x<mostCurrent._btnsatellites.getWidth() && _y>0 && _y<mostCurrent._btnsatellites.getHeight()) { 
 //BA.debugLineNum = 515;BA.debugLine="If GPS_On = False Then";
if (_gps_on==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 516;BA.debugLine="GPS1.Start(GPSMinTime, GPSMinDistance)";
_gps1.Start(processBA,_gpsmintime,_gpsmindistance);
 };
 //BA.debugLineNum = 518;BA.debugLine="Sats_On = True";
_sats_on = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 519;BA.debugLine="StartActivity(\"Satellites\")";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)("Satellites"));
 };
 //BA.debugLineNum = 521;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btnsa";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnsatellites0.png").getObject()));
 //BA.debugLineNum = 522;BA.debugLine="btnSatellites.Background = bmd";
mostCurrent._btnsatellites.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 //BA.debugLineNum = 523;BA.debugLine="ToolTip(\"\")";
_tooltip("");
 break; }
}
;
 //BA.debugLineNum = 525;BA.debugLine="End Sub";
return "";
}
public static String  _btnsetup_touch(int _action,float _x,float _y) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bmd = null;
 //BA.debugLineNum = 527;BA.debugLine="Sub btnSetup_Touch(Action As Int, x As Float, y As";
 //BA.debugLineNum = 528;BA.debugLine="Dim bmd As BitmapDrawable";
_bmd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 530;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,mostCurrent._activity.ACTION_DOWN,mostCurrent._activity.ACTION_UP)) {
case 0: {
 //BA.debugLineNum = 532;BA.debugLine="ToolTip(\"แสดงการตั้งค่า\")";
_tooltip("แสดงการตั้งค่า");
 //BA.debugLineNum = 533;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btnse";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnsetup1.png").getObject()));
 //BA.debugLineNum = 534;BA.debugLine="btnSetup.Background = bmd";
mostCurrent._btnsetup.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 break; }
case 1: {
 //BA.debugLineNum = 536;BA.debugLine="If x > 0 And x < btnSetup.Width  And y > 0 And y";
if (_x>0 && _x<mostCurrent._btnsetup.getWidth() && _y>0 && _y<mostCurrent._btnsetup.getHeight()) { 
 //BA.debugLineNum = 537;BA.debugLine="StartActivity(\"Setup\")";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)("Setup"));
 };
 //BA.debugLineNum = 539;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btnse";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnsetup0.png").getObject()));
 //BA.debugLineNum = 540;BA.debugLine="btnSetup.Background = bmd";
mostCurrent._btnsetup.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 //BA.debugLineNum = 541;BA.debugLine="ToolTip(\"\")";
_tooltip("");
 break; }
}
;
 //BA.debugLineNum = 543;BA.debugLine="End Sub";
return "";
}
public static String  _calcmapscales() throws Exception{
 //BA.debugLineNum = 826;BA.debugLine="Sub CalcMapScales";
 //BA.debugLineNum = 828;BA.debugLine="MapScaleLng = 360 / Power(2, MapZoomLevel) / Tile";
_mapscalelng = 360/(double)anywheresoftware.b4a.keywords.Common.Power(2,_mapzoomlevel)/(double)_tilesize;
 //BA.debugLineNum = 829;BA.debugLine="MapScaleLat = MapScaleLng * CosD(MapCenter.Latitu";
_mapscalelat = _mapscalelng*anywheresoftware.b4a.keywords.Common.CosD(_mapcenter.getLatitude());
 //BA.debugLineNum = 830;BA.debugLine="End Sub";
return "";
}
public static String  _calcmapzoom() throws Exception{
int _i = 0;
double _scale = 0;
double _scale0 = 0;
B4A.GPS.PaiNaiMa.main._gpslocation _loc = null;
 //BA.debugLineNum = 782;BA.debugLine="Sub CalcMapZoom";
 //BA.debugLineNum = 784;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 785;BA.debugLine="Dim Scale As Double";
_scale = 0;
 //BA.debugLineNum = 786;BA.debugLine="Dim Scale0 As Double";
_scale0 = 0;
 //BA.debugLineNum = 788;BA.debugLine="GPSPathLngMax = -180";
_gpspathlngmax = -180;
 //BA.debugLineNum = 789;BA.debugLine="GPSPathLngMin = 180";
_gpspathlngmin = 180;
 //BA.debugLineNum = 790;BA.debugLine="GPSPathLatMax = -90";
_gpspathlatmax = -90;
 //BA.debugLineNum = 791;BA.debugLine="GPSPathLatMin = 90";
_gpspathlatmin = 90;
 //BA.debugLineNum = 794;BA.debugLine="For i = 0 To GPSPath.Size -1";
{
final int step8 = 1;
final int limit8 = (int) (_gpspath.getSize()-1);
for (_i = (int) (0) ; (step8 > 0 && _i <= limit8) || (step8 < 0 && _i >= limit8); _i = ((int)(0 + _i + step8)) ) {
 //BA.debugLineNum = 795;BA.debugLine="Dim loc As GPSLocation";
_loc = new B4A.GPS.PaiNaiMa.main._gpslocation();
 //BA.debugLineNum = 796;BA.debugLine="loc = GPSPath.Get(i)";
_loc = (B4A.GPS.PaiNaiMa.main._gpslocation)(_gpspath.Get(_i));
 //BA.debugLineNum = 797;BA.debugLine="GPSPathLngMax = Max(GPSPathLngMax, loc.Longitude";
_gpspathlngmax = anywheresoftware.b4a.keywords.Common.Max(_gpspathlngmax,_loc.Longitude);
 //BA.debugLineNum = 798;BA.debugLine="GPSPathLngMin = Min(GPSPathLngMin, loc.Longitude";
_gpspathlngmin = anywheresoftware.b4a.keywords.Common.Min(_gpspathlngmin,_loc.Longitude);
 //BA.debugLineNum = 799;BA.debugLine="GPSPathLatMax = Max(GPSPathLatMax, loc.Latitude)";
_gpspathlatmax = anywheresoftware.b4a.keywords.Common.Max(_gpspathlatmax,_loc.Latitude);
 //BA.debugLineNum = 800;BA.debugLine="GPSPathLatMin = Min(GPSPathLatMin, loc.Latitude)";
_gpspathlatmin = anywheresoftware.b4a.keywords.Common.Min(_gpspathlatmin,_loc.Latitude);
 }
};
 //BA.debugLineNum = 803;BA.debugLine="GPSPathLngMean = (GPSPathLngMax + GPSPathLngMin)";
_gpspathlngmean = (_gpspathlngmax+_gpspathlngmin)/(double)2;
 //BA.debugLineNum = 804;BA.debugLine="GPSPathLatMean = (GPSPathLatMax + GPSPathLatMin)";
_gpspathlatmean = (_gpspathlatmax+_gpspathlatmin)/(double)2;
 //BA.debugLineNum = 806;BA.debugLine="MapZoomLevel = 20";
_mapzoomlevel = (int) (20);
 //BA.debugLineNum = 807;BA.debugLine="Scale0 = MapViewer.Width / TileSize * 360";
_scale0 = mostCurrent._mapviewer.getWidth()/(double)_tilesize*360;
 //BA.debugLineNum = 808;BA.debugLine="If (GPSPathLngMax - GPSPathLngMin) / MapViewer.Wi";
if ((_gpspathlngmax-_gpspathlngmin)/(double)mostCurrent._mapviewer.getWidth()>(_gpspathlatmax-_gpspathlatmin)/(double)mostCurrent._mapviewer.getHeight()/(double)anywheresoftware.b4a.keywords.Common.CosD(_gpspathlatmean)) { 
 //BA.debugLineNum = 809;BA.debugLine="Scale = Scale0 / Power(2, MapZoomLevel)";
_scale = _scale0/(double)anywheresoftware.b4a.keywords.Common.Power(2,_mapzoomlevel);
 //BA.debugLineNum = 810;BA.debugLine="Do While Scale <= GPSPathLngMax - GPSPathLngMin";
while (_scale<=_gpspathlngmax-_gpspathlngmin) {
 //BA.debugLineNum = 811;BA.debugLine="MapZoomLevel = MapZoomLevel - 1";
_mapzoomlevel = (int) (_mapzoomlevel-1);
 //BA.debugLineNum = 812;BA.debugLine="Scale = Scale0 / Power(2, MapZoomLevel)";
_scale = _scale0/(double)anywheresoftware.b4a.keywords.Common.Power(2,_mapzoomlevel);
 }
;
 }else {
 //BA.debugLineNum = 815;BA.debugLine="Scale0 = Scale0 / CosD(GPSPathLatMean)";
_scale0 = _scale0/(double)anywheresoftware.b4a.keywords.Common.CosD(_gpspathlatmean);
 //BA.debugLineNum = 816;BA.debugLine="Scale = Scale0 / Power(2, MapZoomLevel)";
_scale = _scale0/(double)anywheresoftware.b4a.keywords.Common.Power(2,_mapzoomlevel);
 //BA.debugLineNum = 817;BA.debugLine="Do While Scale <= GPSPathLatMax - GPSPathLatMin";
while (_scale<=_gpspathlatmax-_gpspathlatmin) {
 //BA.debugLineNum = 818;BA.debugLine="MapZoomLevel = MapZoomLevel - 1";
_mapzoomlevel = (int) (_mapzoomlevel-1);
 //BA.debugLineNum = 819;BA.debugLine="Scale = Scale0 / Power(2, MapZoomLevel)";
_scale = _scale0/(double)anywheresoftware.b4a.keywords.Common.Power(2,_mapzoomlevel);
 }
;
 };
 //BA.debugLineNum = 822;BA.debugLine="CalcMapScales";
_calcmapscales();
 //BA.debugLineNum = 823;BA.debugLine="MapZoomCalculated = True";
_mapzoomcalculated = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 824;BA.debugLine="End Sub";
return "";
}
public static String  _drawcursor(double _lat,double _lng) throws Exception{
 //BA.debugLineNum = 1177;BA.debugLine="Sub DrawCursor(Lat As Double, Lng As Double)";
 //BA.debugLineNum = 1179;BA.debugLine="cvsMap.DrawLine(MapXCursor, 0, MapXCursor, pnlMap";
mostCurrent._cvsmap.DrawLine(_mapxcursor,(float) (0),_mapxcursor,(float) (mostCurrent._pnlmap.getHeight()),anywheresoftware.b4a.keywords.Common.Colors.Transparent,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 1180;BA.debugLine="cvsMap.DrawLine(0, MapYCursor, pnlMap.Width, MapY";
mostCurrent._cvsmap.DrawLine((float) (0),_mapycursor,(float) (mostCurrent._pnlmap.getWidth()),_mapycursor,anywheresoftware.b4a.keywords.Common.Colors.Transparent,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 1182;BA.debugLine="MapXCursor = (Lng - MapCenter.Longitude) / MapSca";
_mapxcursor = (float) ((_lng-_mapcenter.getLongitude())/(double)_mapscalelng+mostCurrent._mapviewer.getWidth()/(double)2);
 //BA.debugLineNum = 1183;BA.debugLine="MapYCursor = (MapCenter.Latitude - Lat) / MapScal";
_mapycursor = (float) ((_mapcenter.getLatitude()-_lat)/(double)_mapscalelat+mostCurrent._mapviewer.getHeight()/(double)2);
 //BA.debugLineNum = 1185;BA.debugLine="cvsMap.DrawLine(MapXCursor, 0, MapXCursor, pnlMap";
mostCurrent._cvsmap.DrawLine(_mapxcursor,(float) (0),_mapxcursor,(float) (mostCurrent._pnlmap.getHeight()),anywheresoftware.b4a.keywords.Common.Colors.Red,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 1186;BA.debugLine="cvsMap.DrawLine(0, MapYCursor, pnlMap.Width, MapY";
mostCurrent._cvsmap.DrawLine((float) (0),_mapycursor,(float) (mostCurrent._pnlmap.getWidth()),_mapycursor,anywheresoftware.b4a.keywords.Common.Colors.Red,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 1187;BA.debugLine="pnlMap.Invalidate";
mostCurrent._pnlmap.Invalidate();
 //BA.debugLineNum = 1188;BA.debugLine="End Sub";
return "";
}
public static String  _drawgpsposition(float _xc,float _yc) throws Exception{
float _x1 = 0f;
float _y1 = 0f;
float _x2 = 0f;
float _y2 = 0f;
float _dd1 = 0f;
float _dd2 = 0f;
float _r = 0f;
 //BA.debugLineNum = 1190;BA.debugLine="Sub DrawGPSPosition(xc As Float, yc As Float)";
 //BA.debugLineNum = 1191;BA.debugLine="Dim x1, y1, x2, y2 As Float";
_x1 = 0f;
_y1 = 0f;
_x2 = 0f;
_y2 = 0f;
 //BA.debugLineNum = 1192;BA.debugLine="Dim dd1, dd2, r As Float";
_dd1 = 0f;
_dd2 = 0f;
_r = 0f;
 //BA.debugLineNum = 1194;BA.debugLine="cvsMap.DrawRect(rectMapPos, Colors.Transparent, T";
mostCurrent._cvsmap.DrawRect((android.graphics.Rect)(mostCurrent._rectmappos.getObject()),anywheresoftware.b4a.keywords.Common.Colors.Transparent,anywheresoftware.b4a.keywords.Common.True,(float) (1));
 //BA.debugLineNum = 1195;BA.debugLine="pnlMap.Invalidate2(rectMapPos)";
mostCurrent._pnlmap.Invalidate2((android.graphics.Rect)(mostCurrent._rectmappos.getObject()));
 //BA.debugLineNum = 1197;BA.debugLine="dd1 = 20dip";
_dd1 = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20)));
 //BA.debugLineNum = 1198;BA.debugLine="dd2 = 20dip";
_dd2 = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20)));
 //BA.debugLineNum = 1199;BA.debugLine="r = 10dip";
_r = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 1200;BA.debugLine="x1 = xc - dd1";
_x1 = (float) (_xc-_dd1);
 //BA.debugLineNum = 1201;BA.debugLine="y1 = yc - dd1";
_y1 = (float) (_yc-_dd1);
 //BA.debugLineNum = 1202;BA.debugLine="x2 = xc + dd2";
_x2 = (float) (_xc+_dd2);
 //BA.debugLineNum = 1203;BA.debugLine="y2 = yc + dd2";
_y2 = (float) (_yc+_dd2);
 //BA.debugLineNum = 1204;BA.debugLine="rectMapPos.Initialize(x1, y1, x2 + 1, y2 + 1)";
mostCurrent._rectmappos.Initialize((int) (_x1),(int) (_y1),(int) (_x2+1),(int) (_y2+1));
 //BA.debugLineNum = 1206;BA.debugLine="cvsMap.DrawLine(x1, yc, x2, yc, Colors.Red, 1dip)";
mostCurrent._cvsmap.DrawLine(_x1,_yc,_x2,_yc,anywheresoftware.b4a.keywords.Common.Colors.Red,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 1207;BA.debugLine="cvsMap.DrawLine(xc, y1, xc, y2, Colors.Red, 1dip)";
mostCurrent._cvsmap.DrawLine(_xc,_y1,_xc,_y2,anywheresoftware.b4a.keywords.Common.Colors.Red,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 1208;BA.debugLine="cvsMap.DrawCircle(xc, yc, r, Colors.Red, False, 3";
mostCurrent._cvsmap.DrawCircle(_xc,_yc,_r,anywheresoftware.b4a.keywords.Common.Colors.Red,anywheresoftware.b4a.keywords.Common.False,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (3))));
 //BA.debugLineNum = 1209;BA.debugLine="pnlMap.Invalidate2(rectMapPos)";
mostCurrent._pnlmap.Invalidate2((android.graphics.Rect)(mostCurrent._rectmappos.getObject()));
 //BA.debugLineNum = 1210;BA.debugLine="End Sub";
return "";
}
public static String  _drawneedle(float _angle) throws Exception{
 //BA.debugLineNum = 1290;BA.debugLine="Sub DrawNeedle(Angle As Float)";
 //BA.debugLineNum = 1292;BA.debugLine="cvsDispGPSNeedle.DrawRect(rectDispGPSNeedle, Colo";
mostCurrent._cvsdispgpsneedle.DrawRect((android.graphics.Rect)(mostCurrent._rectdispgpsneedle.getObject()),anywheresoftware.b4a.keywords.Common.Colors.Transparent,anywheresoftware.b4a.keywords.Common.True,(float) (1));
 //BA.debugLineNum = 1293;BA.debugLine="cvsDispGPSNeedle.DrawBitmapRotated(bmpNeedle, rec";
mostCurrent._cvsdispgpsneedle.DrawBitmapRotated((android.graphics.Bitmap)(mostCurrent._bmpneedle.getObject()),(android.graphics.Rect)(mostCurrent._rectneedlesrc.getObject()),(android.graphics.Rect)(mostCurrent._rectneedledest.getObject()),_angle);
 //BA.debugLineNum = 1294;BA.debugLine="pnlDispGPSNeedle.Invalidate";
mostCurrent._pnldispgpsneedle.Invalidate();
 //BA.debugLineNum = 1295;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 144;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 145;BA.debugLine="Dim pnlDispGPSLatLng, pnlToolBox, pnlSaveGPS As P";
mostCurrent._pnldispgpslatlng = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._pnltoolbox = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._pnlsavegps = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 147;BA.debugLine="Dim MapViewer As WebView";
mostCurrent._mapviewer = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 148;BA.debugLine="Dim MapViewerExtra As WebViewExtras";
mostCurrent._mapviewerextra = new uk.co.martinpearman.b4a.webviewextras.WebViewExtras();
 //BA.debugLineNum = 150;BA.debugLine="Dim btnSatellites, btnGPS, btnMap, btnMap1, btnSe";
mostCurrent._btnsatellites = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._btngps = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._btnmap = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._btnmap1 = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._btnsetup = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._pnlmap = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._pnlmainbackgound = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 151;BA.debugLine="Dim btnGPSPath, btnGPSPath1, btnGPSPath2, btnGPSP";
mostCurrent._btngpspath = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._btngpspath1 = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._btngpspath2 = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._btngpspath3 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 152;BA.debugLine="Dim pnlDispGPSAltitude, pnlDispGPSSpeed, pnlDispG";
mostCurrent._pnldispgpsaltitude = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._pnldispgpsspeed = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._pnldispgpsbearing = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 153;BA.debugLine="Dim pnlDispGPSWindrose, pnlDispGPSNeedle, pnlGPSP";
mostCurrent._pnldispgpswindrose = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._pnldispgpsneedle = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._pnlgpspathtoolbox = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 155;BA.debugLine="Dim cvsMap, cvsDispGPSWindrose, cvsDispGPSNeedle,";
mostCurrent._cvsmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
mostCurrent._cvsdispgpswindrose = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
mostCurrent._cvsdispgpsneedle = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
mostCurrent._cvsneedle = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
 //BA.debugLineNum = 156;BA.debugLine="Dim rectMapPos As Rect";
mostCurrent._rectmappos = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 157;BA.debugLine="Dim imvNeedle As ImageView";
mostCurrent._imvneedle = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 158;BA.debugLine="Dim csvNeedle As Canvas";
mostCurrent._csvneedle = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
 //BA.debugLineNum = 159;BA.debugLine="Dim bmpNeedle As Bitmap";
mostCurrent._bmpneedle = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 160;BA.debugLine="Dim rectNeedleSrc, rectNeedleDest, rectDispGPSNee";
mostCurrent._rectneedlesrc = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
mostCurrent._rectneedledest = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
mostCurrent._rectdispgpsneedle = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 162;BA.debugLine="Dim lblToolTip As Label";
mostCurrent._lbltooltip = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 163;BA.debugLine="Dim lblLatitude, lblLongitude, lblAltitude, lblSp";
mostCurrent._lbllatitude = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lbllongitude = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lblaltitude = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lblspeed = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lblbearing = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 164;BA.debugLine="Dim lblLatitude0, lblLongitude0, lblAltitude0, lb";
mostCurrent._lbllatitude0 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lbllongitude0 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lblaltitude0 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lblspeed0 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lblbearing0 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 166;BA.debugLine="Dim Phone1 As Phone";
mostCurrent._phone1 = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 167;BA.debugLine="End Sub";
return "";
}
public static String  _gps1_locationchanged(anywheresoftware.b4a.gps.LocationWrapper _location1) throws Exception{
B4A.GPS.PaiNaiMa.main._gpslocation _loc = null;
float _xc = 0f;
float _yc = 0f;
 //BA.debugLineNum = 1013;BA.debugLine="Sub GPS1_LocationChanged (Location1 As Location)";
 //BA.debugLineNum = 1014;BA.debugLine="Dim loc As GPSLocation";
_loc = new B4A.GPS.PaiNaiMa.main._gpslocation();
 //BA.debugLineNum = 1016;BA.debugLine="loc.Initialize";
_loc.Initialize();
 //BA.debugLineNum = 1017;BA.debugLine="lblLatitude.Text = NumberFormat(Location1.Latitu";
mostCurrent._lbllatitude.setText((Object)(anywheresoftware.b4a.keywords.Common.NumberFormat(_location1.getLatitude(),(int) (1),(int) (6))));
 //BA.debugLineNum = 1018;BA.debugLine="lblLongitude.Text = NumberFormat(Location1.Longi";
mostCurrent._lbllongitude.setText((Object)(anywheresoftware.b4a.keywords.Common.NumberFormat(_location1.getLongitude(),(int) (1),(int) (6))));
 //BA.debugLineNum = 1019;BA.debugLine="loc.Longitude = Location1.Longitude";
_loc.Longitude = _location1.getLongitude();
 //BA.debugLineNum = 1020;BA.debugLine="loc.Latitude = Location1.Latitude";
_loc.Latitude = _location1.getLatitude();
 //BA.debugLineNum = 1021;BA.debugLine="loc.Time = Location1.Time";
_loc.Time = _location1.getTime();
 //BA.debugLineNum = 1023;BA.debugLine="If Location1.AltitudeValid = True Then";
if (_location1.getAltitudeValid()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1024;BA.debugLine="lblAltitude.Text = NumberFormat2(Location1.Altit";
mostCurrent._lblaltitude.setText((Object)(anywheresoftware.b4a.keywords.Common.NumberFormat2(_location1.getAltitude(),(int) (1),(int) (1),(int) (1),anywheresoftware.b4a.keywords.Common.False)));
 //BA.debugLineNum = 1025;BA.debugLine="loc.Altitude = Location1.Altitude";
_loc.Altitude = _location1.getAltitude();
 }else {
 //BA.debugLineNum = 1027;BA.debugLine="lblAltitude.Text = \"- - -\"";
mostCurrent._lblaltitude.setText((Object)("- - -"));
 };
 //BA.debugLineNum = 1030;BA.debugLine="If Location1.BearingValid = True Then";
if (_location1.getBearingValid()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1031;BA.debugLine="lblBearing.Text = Location1.Bearing";
mostCurrent._lblbearing.setText((Object)(_location1.getBearing()));
 //BA.debugLineNum = 1032;BA.debugLine="loc.Bearing = NumberFormat(Location1.Bearing, 1,";
_loc.Bearing = (float)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.NumberFormat(_location1.getBearing(),(int) (1),(int) (1))));
 }else {
 //BA.debugLineNum = 1034;BA.debugLine="lblBearing.Text = \"- - -\"";
mostCurrent._lblbearing.setText((Object)("- - -"));
 };
 //BA.debugLineNum = 1037;BA.debugLine="If Location1.SpeedValid = True Then";
if (_location1.getSpeedValid()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1038;BA.debugLine="lblSpeed.Text = Location1.Speed";
mostCurrent._lblspeed.setText((Object)(_location1.getSpeed()));
 //BA.debugLineNum = 1039;BA.debugLine="loc.Speed = Location1.Speed";
_loc.Speed = _location1.getSpeed();
 }else {
 //BA.debugLineNum = 1041;BA.debugLine="lblSpeed.Text = \"- - -\"";
mostCurrent._lblspeed.setText((Object)("- - -"));
 };
 //BA.debugLineNum = 1044;BA.debugLine="If GPSPath.Size = 0 Then";
if (_gpspath.getSize()==0) { 
 //BA.debugLineNum = 1045;BA.debugLine="loc.Distance = 0";
_loc.Distance = 0;
 //BA.debugLineNum = 1046;BA.debugLine="loc.DistTot = 0";
_loc.DistTot = 0;
 //BA.debugLineNum = 1047;BA.debugLine="GPSPathDistTotPrev = 0";
_gpspathdisttotprev = 0;
 }else {
 //BA.debugLineNum = 1049;BA.debugLine="loc.Distance = GPSPathPreviuous.DistanceTo(Locat";
_loc.Distance = _gpspathpreviuous.DistanceTo((android.location.Location)(_location1.getObject()));
 //BA.debugLineNum = 1050;BA.debugLine="GPSPathDistTotPrev = GPSPathDistTotPrev + loc.Di";
_gpspathdisttotprev = _gpspathdisttotprev+_loc.Distance;
 //BA.debugLineNum = 1051;BA.debugLine="loc.DistTot = GPSPathDistTotPrev";
_loc.DistTot = _gpspathdisttotprev;
 };
 //BA.debugLineNum = 1053;BA.debugLine="GPSPathPreviuous = Location1";
_gpspathpreviuous = _location1;
 //BA.debugLineNum = 1055;BA.debugLine="If SaveGPSPath = True Then";
if (_savegpspath==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1056;BA.debugLine="GPSPath.Add(loc)";
_gpspath.Add((Object)(_loc));
 //BA.debugLineNum = 1057;BA.debugLine="Activity.Title = \"จุดเส้นทาง : \" & GPSPath.Size";
mostCurrent._activity.setTitle((Object)("จุดเส้นทาง : "+BA.NumberToString(_gpspath.getSize())));
 };
 //BA.debugLineNum = 1060;BA.debugLine="If GPSDispWindrose = True Then";
if (_gpsdispwindrose==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1061;BA.debugLine="DrawNeedle(loc.Bearing)";
_drawneedle(_loc.Bearing);
 };
 //BA.debugLineNum = 1064;BA.debugLine="If ShowGPSOnMap = True Then";
if (_showgpsonmap==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1065;BA.debugLine="Dim xc, yc As Float";
_xc = 0f;
_yc = 0f;
 //BA.debugLineNum = 1066;BA.debugLine="xc = (Location1.Longitude - MapCenter.Longitude)";
_xc = (float) ((_location1.getLongitude()-_mapcenter.getLongitude())/(double)_mapscalelng+mostCurrent._mapviewer.getWidth()/(double)2);
 //BA.debugLineNum = 1067;BA.debugLine="yc = (MapCenter.Latitude - Location1.Latitude) /";
_yc = (float) ((_mapcenter.getLatitude()-_location1.getLatitude())/(double)_mapscalelat+mostCurrent._mapviewer.getHeight()/(double)2);
 //BA.debugLineNum = 1068;BA.debugLine="If xc < 10%x Or xc > 90%x Or yc < 20%y Or yc > 8";
if (_xc<anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA) || _xc>anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA) || _yc<anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (20),mostCurrent.activityBA) || _yc>anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (80),mostCurrent.activityBA)) { 
 //BA.debugLineNum = 1069;BA.debugLine="MapCenter.Latitude = Location1.Latitude";
_mapcenter.setLatitude(_location1.getLatitude());
 //BA.debugLineNum = 1070;BA.debugLine="MapCenter.Longitude = Location1.Longitude";
_mapcenter.setLongitude(_location1.getLongitude());
 //BA.debugLineNum = 1071;BA.debugLine="MapShow";
_mapshow();
 };
 //BA.debugLineNum = 1073;BA.debugLine="DrawGPSPosition(xc, yc)";
_drawgpsposition(_xc,_yc);
 };
 //BA.debugLineNum = 1075;BA.debugLine="End Sub";
return "";
}
public static String  _gpspathmodifpoint(int _index,double _lat,double _lng) throws Exception{
B4A.GPS.PaiNaiMa.main._gpslocation _gpsloc1 = null;
B4A.GPS.PaiNaiMa.main._gpslocation _gpsloc2 = null;
anywheresoftware.b4a.gps.LocationWrapper _loc1 = null;
anywheresoftware.b4a.gps.LocationWrapper _loc2 = null;
anywheresoftware.b4a.objects.LabelWrapper _lbl = null;
int _n = 0;
int _i = 0;
 //BA.debugLineNum = 455;BA.debugLine="Sub GPSPathModifPoint(Index As Int, Lat As Double,";
 //BA.debugLineNum = 456;BA.debugLine="Dim GPSloc1, GPSloc2 As GPSLocation";
_gpsloc1 = new B4A.GPS.PaiNaiMa.main._gpslocation();
_gpsloc2 = new B4A.GPS.PaiNaiMa.main._gpslocation();
 //BA.debugLineNum = 457;BA.debugLine="Dim loc1, loc2 As Location";
_loc1 = new anywheresoftware.b4a.gps.LocationWrapper();
_loc2 = new anywheresoftware.b4a.gps.LocationWrapper();
 //BA.debugLineNum = 458;BA.debugLine="Dim lbl As Label";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 459;BA.debugLine="Dim n As Int";
_n = 0;
 //BA.debugLineNum = 462;BA.debugLine="GPSloc1 = GPSPath.Get(Index)";
_gpsloc1 = (B4A.GPS.PaiNaiMa.main._gpslocation)(_gpspath.Get(_index));
 //BA.debugLineNum = 463;BA.debugLine="GPSloc1.Latitude = Lat";
_gpsloc1.Latitude = _lat;
 //BA.debugLineNum = 464;BA.debugLine="GPSloc1.Longitude = Lng";
_gpsloc1.Longitude = _lng;
 //BA.debugLineNum = 465;BA.debugLine="GPSPath.Set(Index, GPSloc1)";
_gpspath.Set(_index,(Object)(_gpsloc1));
 //BA.debugLineNum = 469;BA.debugLine="If Index > 0 Then";
if (_index>0) { 
 //BA.debugLineNum = 470;BA.debugLine="GPSloc1 = GPSPath.Get(Index - 1)";
_gpsloc1 = (B4A.GPS.PaiNaiMa.main._gpslocation)(_gpspath.Get((int) (_index-1)));
 //BA.debugLineNum = 471;BA.debugLine="GPSloc2 = GPSPath.Get(Index)";
_gpsloc2 = (B4A.GPS.PaiNaiMa.main._gpslocation)(_gpspath.Get(_index));
 //BA.debugLineNum = 472;BA.debugLine="loc1.Initialize2(GPSloc1.Latitude, GPSloc1.Longi";
_loc1.Initialize2(BA.NumberToString(_gpsloc1.Latitude),BA.NumberToString(_gpsloc1.Longitude));
 //BA.debugLineNum = 473;BA.debugLine="loc2.Initialize2(GPSloc2.Latitude, GPSloc2.Longi";
_loc2.Initialize2(BA.NumberToString(_gpsloc2.Latitude),BA.NumberToString(_gpsloc2.Longitude));
 //BA.debugLineNum = 474;BA.debugLine="GPSloc2.Distance = loc1.DistanceTo(loc2)";
_gpsloc2.Distance = _loc1.DistanceTo((android.location.Location)(_loc2.getObject()));
 //BA.debugLineNum = 475;BA.debugLine="GPSloc2.DistTot = GPSloc1.DistTot + GPSloc2.Dist";
_gpsloc2.DistTot = _gpsloc1.DistTot+_gpsloc2.Distance;
 //BA.debugLineNum = 476;BA.debugLine="GPSloc2.Bearing = loc1.BearingTo(loc2)";
_gpsloc2.Bearing = _loc1.BearingTo((android.location.Location)(_loc2.getObject()));
 //BA.debugLineNum = 477;BA.debugLine="GPSloc2.Speed = NumberFormat(GPSloc2.Distance /";
_gpsloc2.Speed = (float)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.NumberFormat(_gpsloc2.Distance/(double)(_gpsloc2.Time-_gpsloc1.Time)*1000,(int) (1),(int) (1))));
 //BA.debugLineNum = 478;BA.debugLine="GPSPath.Set(Index, GPSloc2)";
_gpspath.Set(_index,(Object)(_gpsloc2));
 };
 //BA.debugLineNum = 483;BA.debugLine="If Index < GPSPath.Size - 1 Then";
if (_index<_gpspath.getSize()-1) { 
 //BA.debugLineNum = 484;BA.debugLine="GPSloc1 = GPSPath.Get(Index)";
_gpsloc1 = (B4A.GPS.PaiNaiMa.main._gpslocation)(_gpspath.Get(_index));
 //BA.debugLineNum = 485;BA.debugLine="GPSloc2 = GPSPath.Get(Index + 1)";
_gpsloc2 = (B4A.GPS.PaiNaiMa.main._gpslocation)(_gpspath.Get((int) (_index+1)));
 //BA.debugLineNum = 486;BA.debugLine="loc1.Initialize2(GPSloc1.Latitude, GPSloc1.Longi";
_loc1.Initialize2(BA.NumberToString(_gpsloc1.Latitude),BA.NumberToString(_gpsloc1.Longitude));
 //BA.debugLineNum = 487;BA.debugLine="loc2.Initialize2(GPSloc2.Latitude, GPSloc2.Longi";
_loc2.Initialize2(BA.NumberToString(_gpsloc2.Latitude),BA.NumberToString(_gpsloc2.Longitude));
 //BA.debugLineNum = 488;BA.debugLine="GPSloc2.Distance = loc1.DistanceTo(loc2)";
_gpsloc2.Distance = _loc1.DistanceTo((android.location.Location)(_loc2.getObject()));
 //BA.debugLineNum = 489;BA.debugLine="GPSloc2.DistTot = GPSloc1.DistTot + GPSloc2.Dist";
_gpsloc2.DistTot = _gpsloc1.DistTot+_gpsloc2.Distance;
 //BA.debugLineNum = 490;BA.debugLine="GPSloc2.Bearing = loc1.BearingTo(loc2)";
_gpsloc2.Bearing = _loc1.BearingTo((android.location.Location)(_loc2.getObject()));
 //BA.debugLineNum = 491;BA.debugLine="GPSloc2.Speed = NumberFormat(GPSloc2.Distance /";
_gpsloc2.Speed = (float)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.NumberFormat(_gpsloc2.Distance/(double)(_gpsloc2.Time-_gpsloc1.Time)*1000,(int) (1),(int) (1))));
 //BA.debugLineNum = 492;BA.debugLine="GPSPath.Set(Index + 1, GPSloc2)";
_gpspath.Set((int) (_index+1),(Object)(_gpsloc2));
 //BA.debugLineNum = 495;BA.debugLine="GPSloc1 = GPSPath.Get(Index)";
_gpsloc1 = (B4A.GPS.PaiNaiMa.main._gpslocation)(_gpspath.Get(_index));
 //BA.debugLineNum = 496;BA.debugLine="For i = Index + 1 To GPSPath.Size - 1";
{
final int step31 = 1;
final int limit31 = (int) (_gpspath.getSize()-1);
for (_i = (int) (_index+1) ; (step31 > 0 && _i <= limit31) || (step31 < 0 && _i >= limit31); _i = ((int)(0 + _i + step31)) ) {
 //BA.debugLineNum = 497;BA.debugLine="GPSloc2 = GPSPath.Get(i)";
_gpsloc2 = (B4A.GPS.PaiNaiMa.main._gpslocation)(_gpspath.Get(_i));
 //BA.debugLineNum = 498;BA.debugLine="GPSloc2.DistTot = GPSloc1.DistTot + GPSloc2.Dis";
_gpsloc2.DistTot = _gpsloc1.DistTot+_gpsloc2.Distance;
 //BA.debugLineNum = 499;BA.debugLine="GPSPath.Set(i, GPSloc2)";
_gpspath.Set(_i,(Object)(_gpsloc2));
 //BA.debugLineNum = 500;BA.debugLine="GPSloc1 = GPSloc2";
_gpsloc1 = _gpsloc2;
 }
};
 };
 //BA.debugLineNum = 503;BA.debugLine="End Sub";
return "";
}
public static String  _initneedle() throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper.PathWrapper _pth = null;
 //BA.debugLineNum = 1261;BA.debugLine="Sub InitNeedle";
 //BA.debugLineNum = 1263;BA.debugLine="Dim pth As Path";
_pth = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.PathWrapper();
 //BA.debugLineNum = 1265;BA.debugLine="imvNeedle.Height = pnlDispGPSWindrose.Height";
mostCurrent._imvneedle.setHeight(mostCurrent._pnldispgpswindrose.getHeight());
 //BA.debugLineNum = 1266;BA.debugLine="imvNeedle.Width = 22dip";
mostCurrent._imvneedle.setWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (22)));
 //BA.debugLineNum = 1267;BA.debugLine="rectNeedleSrc.Initialize(0, 0, imvNeedle.Width, i";
mostCurrent._rectneedlesrc.Initialize((int) (0),(int) (0),mostCurrent._imvneedle.getWidth(),mostCurrent._imvneedle.getHeight());
 //BA.debugLineNum = 1268;BA.debugLine="rectNeedleDest.Initialize((pnlDispGPSWindrose.Wid";
mostCurrent._rectneedledest.Initialize((int) ((mostCurrent._pnldispgpswindrose.getWidth()-mostCurrent._imvneedle.getWidth())/(double)2),(int) (0),(int) ((mostCurrent._pnldispgpswindrose.getWidth()+mostCurrent._imvneedle.getWidth())/(double)2),mostCurrent._imvneedle.getHeight());
 //BA.debugLineNum = 1269;BA.debugLine="rectDispGPSNeedle.Initialize(0, 0, pnlDispGPSNeed";
mostCurrent._rectdispgpsneedle.Initialize((int) (0),(int) (0),mostCurrent._pnldispgpsneedle.getWidth(),mostCurrent._pnldispgpsneedle.getHeight());
 //BA.debugLineNum = 1270;BA.debugLine="csvNeedle.Initialize(imvNeedle)";
mostCurrent._csvneedle.Initialize((android.view.View)(mostCurrent._imvneedle.getObject()));
 //BA.debugLineNum = 1272;BA.debugLine="pth.Initialize(imvNeedle.Width / 2, 0)";
_pth.Initialize((float) (mostCurrent._imvneedle.getWidth()/(double)2),(float) (0));
 //BA.debugLineNum = 1273;BA.debugLine="pth.LineTo(imvNeedle.Width -7dip, imvNeedle.Heigh";
_pth.LineTo((float) (mostCurrent._imvneedle.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (7))),(float) (mostCurrent._imvneedle.getHeight()/(double)2));
 //BA.debugLineNum = 1274;BA.debugLine="pth.LineTo(7dip, imvNeedle.Height / 2)";
_pth.LineTo((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (7))),(float) (mostCurrent._imvneedle.getHeight()/(double)2));
 //BA.debugLineNum = 1276;BA.debugLine="csvNeedle.DrawColor(Colors.Transparent)";
mostCurrent._csvneedle.DrawColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 1277;BA.debugLine="csvNeedle.DrawPath(pth, WindroseNeedleColor, True";
mostCurrent._csvneedle.DrawPath((android.graphics.Path)(_pth.getObject()),_windroseneedlecolor,anywheresoftware.b4a.keywords.Common.True,(float) (1));
 //BA.debugLineNum = 1278;BA.debugLine="csvNeedle.DrawCircle(imvNeedle.Width / 2, imvNeed";
mostCurrent._csvneedle.DrawCircle((float) (mostCurrent._imvneedle.getWidth()/(double)2),(float) (mostCurrent._imvneedle.getHeight()/(double)2),(float) (mostCurrent._imvneedle.getWidth()/(double)2),_windroseneedlecolor,anywheresoftware.b4a.keywords.Common.True,(float) (1));
 //BA.debugLineNum = 1279;BA.debugLine="csvNeedle.DrawCircle(imvNeedle.Width / 2, imvNeed";
mostCurrent._csvneedle.DrawCircle((float) (mostCurrent._imvneedle.getWidth()/(double)2),(float) (mostCurrent._imvneedle.getHeight()/(double)2),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (3))),anywheresoftware.b4a.keywords.Common.Colors.Transparent,anywheresoftware.b4a.keywords.Common.True,(float) (1));
 //BA.debugLineNum = 1280;BA.debugLine="bmpNeedle = imvNeedle.Bitmap";
mostCurrent._bmpneedle.setObject((android.graphics.Bitmap)(mostCurrent._imvneedle.getBitmap()));
 //BA.debugLineNum = 1281;BA.debugLine="DrawNeedle(142)";
_drawneedle((float) (142));
 //BA.debugLineNum = 1283;BA.debugLine="lblAltitude.Text = \"- - -\"";
mostCurrent._lblaltitude.setText((Object)("- - -"));
 //BA.debugLineNum = 1284;BA.debugLine="lblBearing.Text = \"- - -\"";
mostCurrent._lblbearing.setText((Object)("- - -"));
 //BA.debugLineNum = 1285;BA.debugLine="lblLatitude.Text = \"- - -\"";
mostCurrent._lbllatitude.setText((Object)("- - -"));
 //BA.debugLineNum = 1286;BA.debugLine="lblLongitude.Text = \"- - -\"";
mostCurrent._lbllongitude.setText((Object)("- - -"));
 //BA.debugLineNum = 1287;BA.debugLine="lblSpeed.Text = \"- - -\"";
mostCurrent._lblspeed.setText((Object)("- - -"));
 //BA.debugLineNum = 1288;BA.debugLine="End Sub";
return "";
}
public static String  _initwindrose() throws Exception{
float _a = 0f;
float _r1 = 0f;
float _r2 = 0f;
float _r3 = 0f;
float _r4 = 0f;
float _r5 = 0f;
float _xc = 0f;
float _yc = 0f;
float _x1 = 0f;
float _y1 = 0f;
int _i = 0;
int _col = 0;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _rectdispgpswindrose = null;
 //BA.debugLineNum = 1212;BA.debugLine="Sub InitWindrose";
 //BA.debugLineNum = 1214;BA.debugLine="Dim a, r1, r2, r3, r4, r5, xc, yc, x1, y1 As Floa";
_a = 0f;
_r1 = 0f;
_r2 = 0f;
_r3 = 0f;
_r4 = 0f;
_r5 = 0f;
_xc = 0f;
_yc = 0f;
_x1 = 0f;
_y1 = 0f;
 //BA.debugLineNum = 1215;BA.debugLine="Dim i, col As Int";
_i = 0;
_col = 0;
 //BA.debugLineNum = 1217;BA.debugLine="Dim rectDispGPSWindrose As Rect";
_rectdispgpswindrose = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 1218;BA.debugLine="rectDispGPSWindrose.Initialize(0, 0, pnlDispGPSWi";
_rectdispgpswindrose.Initialize((int) (0),(int) (0),mostCurrent._pnldispgpswindrose.getWidth(),mostCurrent._pnldispgpswindrose.getHeight());
 //BA.debugLineNum = 1219;BA.debugLine="cvsDispGPSWindrose.DrawRect(rectDispGPSWindrose,";
mostCurrent._cvsdispgpswindrose.DrawRect((android.graphics.Rect)(_rectdispgpswindrose.getObject()),anywheresoftware.b4a.keywords.Common.Colors.Transparent,anywheresoftware.b4a.keywords.Common.True,(float) (1));
 //BA.debugLineNum = 1220;BA.debugLine="xc = pnlDispGPSWindrose.Width / 2";
_xc = (float) (mostCurrent._pnldispgpswindrose.getWidth()/(double)2);
 //BA.debugLineNum = 1221;BA.debugLine="yc = xc";
_yc = _xc;
 //BA.debugLineNum = 1222;BA.debugLine="r4 = pnlDispGPSWindrose.Width / 2 - 2dip";
_r4 = (float) (mostCurrent._pnldispgpswindrose.getWidth()/(double)2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
 //BA.debugLineNum = 1223;BA.debugLine="cvsDispGPSWindrose.DrawCircle(xc, yc, r4, Windros";
mostCurrent._cvsdispgpswindrose.DrawCircle(_xc,_yc,_r4,_windrosecolor,anywheresoftware.b4a.keywords.Common.False,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))));
 //BA.debugLineNum = 1224;BA.debugLine="col = WindroseTicksColor";
_col = _windrosetickscolor;
 //BA.debugLineNum = 1226;BA.debugLine="If Activity.Width = 320 Then";
if (mostCurrent._activity.getWidth()==320) { 
 //BA.debugLineNum = 1227;BA.debugLine="r3 = r4 - 10dip";
_r3 = (float) (_r4-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 1228;BA.debugLine="r2 = r3 - 5dip";
_r2 = (float) (_r3-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 1229;BA.debugLine="r1 = r2 - 5dip";
_r1 = (float) (_r2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 1230;BA.debugLine="For i = 0 To 359 Step 5";
{
final int step15 = (int) (5);
final int limit15 = (int) (359);
for (_i = (int) (0) ; (step15 > 0 && _i <= limit15) || (step15 < 0 && _i >= limit15); _i = ((int)(0 + _i + step15)) ) {
 //BA.debugLineNum = 1231;BA.debugLine="a = i";
_a = (float) (_i);
 //BA.debugLineNum = 1232;BA.debugLine="If i Mod 90 = 0 Then";
if (_i%90==0) { 
 //BA.debugLineNum = 1233;BA.debugLine="cvsDispGPSWindrose.DrawLine(xc + r1 * SinD(a),";
mostCurrent._cvsdispgpswindrose.DrawLine((float) (_xc+_r1*anywheresoftware.b4a.keywords.Common.SinD(_a)),(float) (_yc+_r1*anywheresoftware.b4a.keywords.Common.CosD(_a)),(float) (_xc+_r4*anywheresoftware.b4a.keywords.Common.SinD(_a)),(float) (_yc+_r4*anywheresoftware.b4a.keywords.Common.CosD(_a)),_col,(float) (5));
 }else if(_i%45==0) { 
 //BA.debugLineNum = 1235;BA.debugLine="cvsDispGPSWindrose.DrawLine(xc + r2 * SinD(a),";
mostCurrent._cvsdispgpswindrose.DrawLine((float) (_xc+_r2*anywheresoftware.b4a.keywords.Common.SinD(_a)),(float) (_yc+_r2*anywheresoftware.b4a.keywords.Common.CosD(_a)),(float) (_xc+_r4*anywheresoftware.b4a.keywords.Common.SinD(_a)),(float) (_yc+_r4*anywheresoftware.b4a.keywords.Common.CosD(_a)),_col,(float) (5));
 }else if(_i%15==0) { 
 //BA.debugLineNum = 1237;BA.debugLine="cvsDispGPSWindrose.DrawLine(xc + r2 * SinD(a),";
mostCurrent._cvsdispgpswindrose.DrawLine((float) (_xc+_r2*anywheresoftware.b4a.keywords.Common.SinD(_a)),(float) (_yc+_r2*anywheresoftware.b4a.keywords.Common.CosD(_a)),(float) (_xc+_r4*anywheresoftware.b4a.keywords.Common.SinD(_a)),(float) (_yc+_r4*anywheresoftware.b4a.keywords.Common.CosD(_a)),_col,(float) (3));
 }else {
 //BA.debugLineNum = 1239;BA.debugLine="cvsDispGPSWindrose.DrawLine(xc + r3 * SinD(a),";
mostCurrent._cvsdispgpswindrose.DrawLine((float) (_xc+_r3*anywheresoftware.b4a.keywords.Common.SinD(_a)),(float) (_yc+_r3*anywheresoftware.b4a.keywords.Common.CosD(_a)),(float) (_xc+_r4*anywheresoftware.b4a.keywords.Common.SinD(_a)),(float) (_yc+_r4*anywheresoftware.b4a.keywords.Common.CosD(_a)),_col,(float) (1.1));
 };
 }
};
 }else {
 //BA.debugLineNum = 1243;BA.debugLine="r3 = r4 - 10dip";
_r3 = (float) (_r4-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 1244;BA.debugLine="r2 = r3 - 5dip";
_r2 = (float) (_r3-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 1245;BA.debugLine="r1 = r2 - 10dip";
_r1 = (float) (_r2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 1246;BA.debugLine="For i = 0 To 359 Step 2";
{
final int step31 = (int) (2);
final int limit31 = (int) (359);
for (_i = (int) (0) ; (step31 > 0 && _i <= limit31) || (step31 < 0 && _i >= limit31); _i = ((int)(0 + _i + step31)) ) {
 //BA.debugLineNum = 1247;BA.debugLine="a = i";
_a = (float) (_i);
 //BA.debugLineNum = 1248;BA.debugLine="If i Mod 90 = 0 Then";
if (_i%90==0) { 
 //BA.debugLineNum = 1249;BA.debugLine="cvsDispGPSWindrose.DrawLine(xc + r1 * SinD(a),";
mostCurrent._cvsdispgpswindrose.DrawLine((float) (_xc+_r1*anywheresoftware.b4a.keywords.Common.SinD(_a)),(float) (_yc+_r1*anywheresoftware.b4a.keywords.Common.CosD(_a)),(float) (_xc+_r4*anywheresoftware.b4a.keywords.Common.SinD(_a)),(float) (_yc+_r4*anywheresoftware.b4a.keywords.Common.CosD(_a)),_col,(float) (5));
 }else if(_i%10==0) { 
 //BA.debugLineNum = 1251;BA.debugLine="cvsDispGPSWindrose.DrawLine(xc + r2 * SinD(a),";
mostCurrent._cvsdispgpswindrose.DrawLine((float) (_xc+_r2*anywheresoftware.b4a.keywords.Common.SinD(_a)),(float) (_yc+_r2*anywheresoftware.b4a.keywords.Common.CosD(_a)),(float) (_xc+_r4*anywheresoftware.b4a.keywords.Common.SinD(_a)),(float) (_yc+_r4*anywheresoftware.b4a.keywords.Common.CosD(_a)),_col,(float) (5));
 }else {
 //BA.debugLineNum = 1253;BA.debugLine="cvsDispGPSWindrose.DrawLine(xc + r3 * SinD(a),";
mostCurrent._cvsdispgpswindrose.DrawLine((float) (_xc+_r3*anywheresoftware.b4a.keywords.Common.SinD(_a)),(float) (_yc+_r3*anywheresoftware.b4a.keywords.Common.CosD(_a)),(float) (_xc+_r4*anywheresoftware.b4a.keywords.Common.SinD(_a)),(float) (_yc+_r4*anywheresoftware.b4a.keywords.Common.CosD(_a)),_col,(float) (1.1));
 };
 }
};
 };
 //BA.debugLineNum = 1257;BA.debugLine="pnlDispGPSWindrose.Invalidate";
mostCurrent._pnldispgpswindrose.Invalidate();
 //BA.debugLineNum = 1259;BA.debugLine="End Sub";
return "";
}
public static String  _loadsetup() throws Exception{
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _tr = null;
String _version = "";
int _versionnb = 0;
 //BA.debugLineNum = 1120;BA.debugLine="Sub LoadSetup";
 //BA.debugLineNum = 1121;BA.debugLine="Dim tr As TextReader";
_tr = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1122;BA.debugLine="Dim version As String";
_version = "";
 //BA.debugLineNum = 1123;BA.debugLine="Dim versionNb As Int";
_versionnb = 0;
 //BA.debugLineNum = 1125;BA.debugLine="tr.Initialize(File.OpenInput(File.DirInternal, \"S";
_tr.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"Setup.txt").getObject()));
 //BA.debugLineNum = 1126;BA.debugLine="version = tr.ReadLine							' used for version co";
_version = _tr.ReadLine();
 //BA.debugLineNum = 1127;BA.debugLine="If version.CharAt(0) <> \"v\" Then";
if (_version.charAt((int) (0))!=BA.ObjectToChar("v")) { 
 //BA.debugLineNum = 1128;BA.debugLine="MapDefaultLat = version";
_mapdefaultlat = (double)(Double.parseDouble(_version));
 //BA.debugLineNum = 1129;BA.debugLine="versionNb = 0";
_versionnb = (int) (0);
 }else {
 //BA.debugLineNum = 1131;BA.debugLine="MapDefaultLat = tr.ReadLine";
_mapdefaultlat = (double)(Double.parseDouble(_tr.ReadLine()));
 //BA.debugLineNum = 1132;BA.debugLine="versionNb = version.SubString(1)";
_versionnb = (int)(Double.parseDouble(_version.substring((int) (1))));
 };
 //BA.debugLineNum = 1134;BA.debugLine="MapDefaultLng = tr.ReadLine";
_mapdefaultlng = (double)(Double.parseDouble(_tr.ReadLine()));
 //BA.debugLineNum = 1135;BA.debugLine="MapSetDraggable = tr.ReadLine";
_mapsetdraggable = BA.ObjectToBoolean(_tr.ReadLine());
 //BA.debugLineNum = 1136;BA.debugLine="GPSMinTime = tr.ReadLine";
_gpsmintime = (long)(Double.parseDouble(_tr.ReadLine()));
 //BA.debugLineNum = 1137;BA.debugLine="GPSMinDistance = tr.ReadLine";
_gpsmindistance = (float)(Double.parseDouble(_tr.ReadLine()));
 //BA.debugLineNum = 1138;BA.debugLine="GPSDispSpeed = tr.ReadLine";
_gpsdispspeed = BA.ObjectToBoolean(_tr.ReadLine());
 //BA.debugLineNum = 1139;BA.debugLine="GPSDispBearing = tr.ReadLine";
_gpsdispbearing = BA.ObjectToBoolean(_tr.ReadLine());
 //BA.debugLineNum = 1140;BA.debugLine="GPSDispWindrose = tr.ReadLine";
_gpsdispwindrose = BA.ObjectToBoolean(_tr.ReadLine());
 //BA.debugLineNum = 1141;BA.debugLine="GPSFilterDelta = tr.ReadLine";
_gpsfilterdelta = (double)(Double.parseDouble(_tr.ReadLine()));
 //BA.debugLineNum = 1142;BA.debugLine="MapDefaultZoomLevelIndex = tr.ReadLine";
_mapdefaultzoomlevelindex = (int)(Double.parseDouble(_tr.ReadLine()));
 //BA.debugLineNum = 1143;BA.debugLine="MapDefaultZoomLevel = tr.ReadLine";
_mapdefaultzoomlevel = (int)(Double.parseDouble(_tr.ReadLine()));
 //BA.debugLineNum = 1144;BA.debugLine="MapZoomLevel = MapDefaultZoomLevel";
_mapzoomlevel = _mapdefaultzoomlevel;
 //BA.debugLineNum = 1145;BA.debugLine="MapLineWidthIndex = tr.ReadLine";
_maplinewidthindex = (int)(Double.parseDouble(_tr.ReadLine()));
 //BA.debugLineNum = 1146;BA.debugLine="MapLineWidth = tr.ReadLine";
_maplinewidth = (int)(Double.parseDouble(_tr.ReadLine()));
 //BA.debugLineNum = 1147;BA.debugLine="MapLineColorIndex = tr.ReadLine";
_maplinecolorindex = (int)(Double.parseDouble(_tr.ReadLine()));
 //BA.debugLineNum = 1148;BA.debugLine="MapLineColor = tr.ReadLine";
_maplinecolor = _tr.ReadLine();
 //BA.debugLineNum = 1149;BA.debugLine="MapLineOpacityIndex = tr.ReadLine";
_maplineopacityindex = (int)(Double.parseDouble(_tr.ReadLine()));
 //BA.debugLineNum = 1150;BA.debugLine="MapLineOpacity = tr.ReadLine";
_maplineopacity = (float)(Double.parseDouble(_tr.ReadLine()));
 //BA.debugLineNum = 1151;BA.debugLine="MapMarkerClickable = tr.ReadLine";
_mapmarkerclickable = BA.ObjectToBoolean(_tr.ReadLine());
 //BA.debugLineNum = 1152;BA.debugLine="MapMarkerDragable = tr.ReadLine";
_mapmarkerdragable = BA.ObjectToBoolean(_tr.ReadLine());
 //BA.debugLineNum = 1153;BA.debugLine="DispMapTypeControl = tr.ReadLine";
_dispmaptypecontrol = BA.ObjectToBoolean(_tr.ReadLine());
 //BA.debugLineNum = 1154;BA.debugLine="If versionNb >= 1 Then";
if (_versionnb>=1) { 
 //BA.debugLineNum = 1155;BA.debugLine="DispMapTypeControlID = tr.ReadLine";
_dispmaptypecontrolid = _tr.ReadLine();
 //BA.debugLineNum = 1156;BA.debugLine="If versionNb >= 2 Then";
if (_versionnb>=2) { 
 //BA.debugLineNum = 1157;BA.debugLine="DispMapTypeControlIDIndex = tr.ReadLine";
_dispmaptypecontrolidindex = (int)(Double.parseDouble(_tr.ReadLine()));
 //BA.debugLineNum = 1158;BA.debugLine="DispMapZoomControlType = DispMapZoomControlType";
_dispmapzoomcontroltype = _dispmapzoomcontroltypetext[_dispmapzoomcontroltypeindex];
 //BA.debugLineNum = 1159;BA.debugLine="DispMapZoomControlType = tr.ReadLine";
_dispmapzoomcontroltype = _tr.ReadLine();
 //BA.debugLineNum = 1160;BA.debugLine="DispMapZoomControlTypeIndex = tr.ReadLine";
_dispmapzoomcontroltypeindex = (int)(Double.parseDouble(_tr.ReadLine()));
 };
 };
 //BA.debugLineNum = 1163;BA.debugLine="DispMapZoomControl = tr.ReadLine";
_dispmapzoomcontrol = BA.ObjectToBoolean(_tr.ReadLine());
 //BA.debugLineNum = 1164;BA.debugLine="DispMapScaleControl = tr.ReadLine";
_dispmapscalecontrol = BA.ObjectToBoolean(_tr.ReadLine());
 //BA.debugLineNum = 1165;BA.debugLine="DispMapCenter = tr.ReadLine";
_dispmapcenter = BA.ObjectToBoolean(_tr.ReadLine());
 //BA.debugLineNum = 1166;BA.debugLine="DispMapMarkers = tr.ReadLine";
_dispmapmarkers = BA.ObjectToBoolean(_tr.ReadLine());
 //BA.debugLineNum = 1167;BA.debugLine="DispMapPolylne = tr.ReadLine";
_dispmappolylne = BA.ObjectToBoolean(_tr.ReadLine());
 //BA.debugLineNum = 1168;BA.debugLine="ShowGPSOnMap = tr.ReadLine";
_showgpsonmap = BA.ObjectToBoolean(_tr.ReadLine());
 //BA.debugLineNum = 1169;BA.debugLine="SaveGPSPath = tr.ReadLine";
_savegpspath = BA.ObjectToBoolean(_tr.ReadLine());
 //BA.debugLineNum = 1170;BA.debugLine="DrawGPSPath = tr.ReadLine";
_drawgpspath = BA.ObjectToBoolean(_tr.ReadLine());
 //BA.debugLineNum = 1171;BA.debugLine="AltitudeUnitIndex = tr.ReadLine";
_altitudeunitindex = (int)(Double.parseDouble(_tr.ReadLine()));
 //BA.debugLineNum = 1172;BA.debugLine="SpeedUnitIndex = tr.ReadLine";
_speedunitindex = (int)(Double.parseDouble(_tr.ReadLine()));
 //BA.debugLineNum = 1173;BA.debugLine="DistanceUnitIndex = tr.ReadLine";
_distanceunitindex = (int)(Double.parseDouble(_tr.ReadLine()));
 //BA.debugLineNum = 1174;BA.debugLine="tr.Close";
_tr.Close();
 //BA.debugLineNum = 1175;BA.debugLine="End Sub";
return "";
}
public static String  _mapdisp(float _centerlat,float _centerlng,boolean _mapdraggable,int _zoom,boolean _maptypecontrol,String _maptypecontrolid,boolean _dispzoomcontrol,String _zoomcontrolposition,String _zoomcontrolstyle,boolean _dispscalecontrol,String _scalecontrolposition,boolean _dispmarkercenter,anywheresoftware.b4a.objects.collections.List _markerpos,boolean _dispmarkers,boolean _markersclickable,boolean _markersdragable,boolean _disppolyline,String _polylinecolor,float _polylineopacity,int _polylinewidth) throws Exception{
String _htmlcode = "";
int _i = 0;
int _j = 0;
int _n = 0;
B4A.GPS.PaiNaiMa.main._gpslocation _pos = null;
 //BA.debugLineNum = 852;BA.debugLine="Sub MapDisp(CenterLat As Float, CenterLng As Float";
 //BA.debugLineNum = 875;BA.debugLine="Dim HtmlCode As String";
_htmlcode = "";
 //BA.debugLineNum = 876;BA.debugLine="Dim i, j, n As Int";
_i = 0;
_j = 0;
_n = 0;
 //BA.debugLineNum = 877;BA.debugLine="Dim Pos As GPSLocation";
_pos = new B4A.GPS.PaiNaiMa.main._gpslocation();
 //BA.debugLineNum = 878;BA.debugLine="HtmlCode = \"<!DOCTYPE html><html><head><meta name";
_htmlcode = "<!DOCTYPE html><html><head><meta name='viewport' content='initial-scale=1.0, user-scalable=no' /><style type='text/css'>  html { height: 100% }  body { height: 100%; margin: 0px; padding: 0px }#map_canvas { height: 100% }</style><script type='text/javascript' src='http://maps.google.com/maps/api/js?sensor=true'></script><script type='text/javascript'> function initialize() {var latlng = new google.maps.LatLng("+BA.NumberToString(_centerlat)+","+BA.NumberToString(_centerlng)+"); var myOptions = { zoom: "+BA.NumberToString(_zoom)+", center: latlng, disableDefaultUI: true, draggable: "+BA.ObjectToString(_mapdraggable)+", zoomControl: "+BA.ObjectToString(_dispzoomcontrol)+", zoomControlOptions: {position: google.maps.ControlPosition."+_zoomcontrolposition+", style: google.maps.ZoomControlStyle."+_zoomcontrolstyle+"}, scaleControl: "+BA.ObjectToString(_dispscalecontrol)+", scaleControlOptions: {position: google.maps.ControlPosition."+_scalecontrolposition+"}, mapTypeControl: "+BA.ObjectToString(_maptypecontrol)+", mapTypeId: google.maps.MapTypeId."+_maptypecontrolid+" }; var map = new google.maps.Map(document.getElementById('map_canvas'),  myOptions)";
 //BA.debugLineNum = 880;BA.debugLine="If MapSetDraggable = False Then";
if (_mapsetdraggable==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 882;BA.debugLine="HtmlCode = HtmlCode & \";google.maps.event.addLis";
_htmlcode = _htmlcode+";google.maps.event.addListener(map, 'mousedown', function(mouseEvent){";
 //BA.debugLineNum = 883;BA.debugLine="HtmlCode = HtmlCode & \"var lat=mouseEvent.latLng";
_htmlcode = _htmlcode+"var lat=mouseEvent.latLng.lat();";
 //BA.debugLineNum = 884;BA.debugLine="HtmlCode = HtmlCode & \"var lng=mouseEvent.latLng";
_htmlcode = _htmlcode+"var lng=mouseEvent.latLng.lng();";
 //BA.debugLineNum = 885;BA.debugLine="HtmlCode = HtmlCode & \"B4A.CallSub('MapViewer_Mo";
_htmlcode = _htmlcode+"B4A.CallSub('MapViewer_MouseDown', true, lat, lng); })";
 //BA.debugLineNum = 888;BA.debugLine="HtmlCode = HtmlCode & \";google.maps.event.addLis";
_htmlcode = _htmlcode+";google.maps.event.addListener(map, 'mousemove', function(mouseEvent){";
 //BA.debugLineNum = 889;BA.debugLine="HtmlCode = HtmlCode & \"var lat=mouseEvent.latLng";
_htmlcode = _htmlcode+"var lat=mouseEvent.latLng.lat();";
 //BA.debugLineNum = 890;BA.debugLine="HtmlCode = HtmlCode & \"var lng=mouseEvent.latLng";
_htmlcode = _htmlcode+"var lng=mouseEvent.latLng.lng();";
 //BA.debugLineNum = 891;BA.debugLine="HtmlCode = HtmlCode & \"B4A.CallSub('MapViewer_Mo";
_htmlcode = _htmlcode+"B4A.CallSub('MapViewer_MouseMove', true, lat, lng); })";
 //BA.debugLineNum = 894;BA.debugLine="HtmlCode = HtmlCode & \";google.maps.event.addLis";
_htmlcode = _htmlcode+";google.maps.event.addListener(map, 'mouseup', function(mouseEvent){";
 //BA.debugLineNum = 895;BA.debugLine="HtmlCode = HtmlCode & \"var lat=mouseEvent.latLng";
_htmlcode = _htmlcode+"var lat=mouseEvent.latLng.lat();";
 //BA.debugLineNum = 896;BA.debugLine="HtmlCode = HtmlCode & \"var lng=mouseEvent.latLng";
_htmlcode = _htmlcode+"var lng=mouseEvent.latLng.lng();";
 //BA.debugLineNum = 897;BA.debugLine="HtmlCode = HtmlCode & \"B4A.CallSub('MapViewer_Mo";
_htmlcode = _htmlcode+"B4A.CallSub('MapViewer_MouseUp', true, lat, lng); })";
 }else {
 //BA.debugLineNum = 900;BA.debugLine="HtmlCode = HtmlCode & \";google.maps.event.addLis";
_htmlcode = _htmlcode+";google.maps.event.addListener(map, 'click', function(mouseEvent){";
 //BA.debugLineNum = 901;BA.debugLine="HtmlCode = HtmlCode & \"var lat=mouseEvent.latLng";
_htmlcode = _htmlcode+"var lat=mouseEvent.latLng.lat();";
 //BA.debugLineNum = 902;BA.debugLine="HtmlCode = HtmlCode & \"var lng=mouseEvent.latLng";
_htmlcode = _htmlcode+"var lng=mouseEvent.latLng.lng();";
 //BA.debugLineNum = 903;BA.debugLine="HtmlCode = HtmlCode & \"B4A.CallSub('MapViewer_Cl";
_htmlcode = _htmlcode+"B4A.CallSub('MapViewer_Click', true, lat, lng); })";
 //BA.debugLineNum = 906;BA.debugLine="HtmlCode = HtmlCode & \";google.maps.event.addLis";
_htmlcode = _htmlcode+";google.maps.event.addListener(map, 'center_changed', function() {";
 //BA.debugLineNum = 907;BA.debugLine="HtmlCode = HtmlCode & \"var centerlatlng = map.ge";
_htmlcode = _htmlcode+"var centerlatlng = map.getCenter();";
 //BA.debugLineNum = 908;BA.debugLine="HtmlCode = HtmlCode & \"B4A.CallSub('MapViewer_Ce";
_htmlcode = _htmlcode+"B4A.CallSub('MapViewer_CenterChanged', true, centerlatlng.lat(), centerlatlng.lng() );})";
 };
 //BA.debugLineNum = 912;BA.debugLine="HtmlCode = HtmlCode & \"; google.maps.event.addLis";
_htmlcode = _htmlcode+"; google.maps.event.addListener(map, 'zoom_changed', function() {";
 //BA.debugLineNum = 913;BA.debugLine="HtmlCode = HtmlCode & \"var zoomLevel = map.getZoo";
_htmlcode = _htmlcode+"var zoomLevel = map.getZoom();";
 //BA.debugLineNum = 914;BA.debugLine="HtmlCode = HtmlCode & \"B4A.CallSub('MapViewer_Zoo";
_htmlcode = _htmlcode+"B4A.CallSub('MapViewer_ZoomChanged', true, zoomLevel);";
 //BA.debugLineNum = 915;BA.debugLine="HtmlCode = HtmlCode & \" })\"";
_htmlcode = _htmlcode+" })";
 //BA.debugLineNum = 917;BA.debugLine="If DispMapTypeControl = True Then";
if (_dispmaptypecontrol==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 919;BA.debugLine="HtmlCode = HtmlCode & \"; google.maps.event.addLi";
_htmlcode = _htmlcode+"; google.maps.event.addListener(map, 'maptypeid_changed', function() {";
 //BA.debugLineNum = 920;BA.debugLine="HtmlCode = HtmlCode & \"var mapType = map.getMapT";
_htmlcode = _htmlcode+"var mapType = map.getMapTypeId();";
 //BA.debugLineNum = 921;BA.debugLine="HtmlCode = HtmlCode & \"B4A.CallSub('MapViewer_Ma";
_htmlcode = _htmlcode+"B4A.CallSub('MapViewer_MapTypeControlChanged', true, mapType);";
 //BA.debugLineNum = 922;BA.debugLine="HtmlCode = HtmlCode & \" })\"";
_htmlcode = _htmlcode+" })";
 };
 //BA.debugLineNum = 926;BA.debugLine="If DispMarkerCenter = True Then";
if (_dispmarkercenter==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 927;BA.debugLine="HtmlCode = HtmlCode & \"; var markerc = new googl";
_htmlcode = _htmlcode+"; var markerc = new google.maps.Marker({	position: new google.maps.LatLng("+BA.NumberToString(_centerlat)+","+BA.NumberToString(_centerlng)+"),map: map, title: '',clickable: false,icon: 'http://www.google.com/mapfiles/arrow.png' })";
 };
 //BA.debugLineNum = 930;BA.debugLine="If MarkerPos.Size > 0 Then";
if (_markerpos.getSize()>0) { 
 //BA.debugLineNum = 932;BA.debugLine="j = MarkerPos.Size - 1";
_j = (int) (_markerpos.getSize()-1);
 //BA.debugLineNum = 933;BA.debugLine="If DispPolyline = True And j > 0 Then";
if (_disppolyline==anywheresoftware.b4a.keywords.Common.True && _j>0) { 
 //BA.debugLineNum = 935;BA.debugLine="HtmlCode = HtmlCode & \"; var points = [];\"";
_htmlcode = _htmlcode+"; var points = [];";
 //BA.debugLineNum = 936;BA.debugLine="For i = 0 To j";
{
final int step44 = 1;
final int limit44 = _j;
for (_i = (int) (0) ; (step44 > 0 && _i <= limit44) || (step44 < 0 && _i >= limit44); _i = ((int)(0 + _i + step44)) ) {
 //BA.debugLineNum = 937;BA.debugLine="Pos = MarkerPos.Get(i)";
_pos = (B4A.GPS.PaiNaiMa.main._gpslocation)(_markerpos.Get(_i));
 //BA.debugLineNum = 939;BA.debugLine="HtmlCode = HtmlCode & \"var point = new google.";
_htmlcode = _htmlcode+"var point = new google.maps.LatLng("+BA.NumberToString(_pos.Latitude)+","+BA.NumberToString(_pos.Longitude)+");";
 //BA.debugLineNum = 941;BA.debugLine="HtmlCode = HtmlCode & \"points.push(point);\"";
_htmlcode = _htmlcode+"points.push(point);";
 }
};
 //BA.debugLineNum = 944;BA.debugLine="HtmlCode = HtmlCode & \"; var polyline = new goo";
_htmlcode = _htmlcode+"; var polyline = new google.maps.Polyline({path: points, strokeColor: '"+_polylinecolor+"', strokeOpacity: "+BA.NumberToString(_polylineopacity)+", strokeWeight: "+BA.NumberToString(_polylinewidth)+"});";
 //BA.debugLineNum = 948;BA.debugLine="HtmlCode = HtmlCode & \"polyline.setMap(map);\"";
_htmlcode = _htmlcode+"polyline.setMap(map);";
 };
 //BA.debugLineNum = 952;BA.debugLine="GPSMarkerIndexes.Initialize";
_gpsmarkerindexes.Initialize();
 //BA.debugLineNum = 953;BA.debugLine="If DispMarkers = True Then	' checks if display o";
if (_dispmarkers==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 954;BA.debugLine="For i = 0 To j";
{
final int step54 = 1;
final int limit54 = _j;
for (_i = (int) (0) ; (step54 > 0 && _i <= limit54) || (step54 < 0 && _i >= limit54); _i = ((int)(0 + _i + step54)) ) {
 //BA.debugLineNum = 955;BA.debugLine="Pos = MarkerPos.Get(i)";
_pos = (B4A.GPS.PaiNaiMa.main._gpslocation)(_markerpos.Get(_i));
 //BA.debugLineNum = 956;BA.debugLine="If Pos.Marker = True Then	' checks if the curr";
if (_pos.Marker==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 957;BA.debugLine="GPSMarkerIndexes.Add(i)";
_gpsmarkerindexes.Add((Object)(_i));
 //BA.debugLineNum = 958;BA.debugLine="n = GPSMarkerIndexes.Size - 1";
_n = (int) (_gpsmarkerindexes.getSize()-1);
 //BA.debugLineNum = 960;BA.debugLine="HtmlCode = HtmlCode & \"; var marker\" & n & \"";
_htmlcode = _htmlcode+"; var marker"+BA.NumberToString(_n)+" = new google.maps.Marker({	position: new google.maps.LatLng("+BA.NumberToString(_pos.Latitude)+","+BA.NumberToString(_pos.Longitude)+"),map: map, title: 'Test"+BA.NumberToString(_i)+"',clickable: "+BA.ObjectToString(_markersclickable)+", draggable: "+BA.ObjectToString(_markersdragable)+", icon: 'http://chart.apis.google.com/";
 //BA.debugLineNum = 961;BA.debugLine="If i = 0 Then";
if (_i==0) { 
 //BA.debugLineNum = 964;BA.debugLine="HtmlCode = HtmlCode & \"chart?chst=d_map_pin_";
_htmlcode = _htmlcode+"chart?chst=d_map_pin_letter&chld=A|00FF00|000000'})";
 }else if(_i==_j) { 
 //BA.debugLineNum = 967;BA.debugLine="HtmlCode = HtmlCode & \"chart?chst=d_map_pin_";
_htmlcode = _htmlcode+"chart?chst=d_map_pin_letter&chld="+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) ((_n%26)+65)))+"|FF0000|000000'})";
 }else {
 //BA.debugLineNum = 970;BA.debugLine="HtmlCode = HtmlCode & \"chart?chst=d_map_pin_";
_htmlcode = _htmlcode+"chart?chst=d_map_pin_letter&chld="+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) ((_n%26)+65)))+"|FFFF00|000000'})";
 };
 //BA.debugLineNum = 974;BA.debugLine="HtmlCode = HtmlCode & \"; google.maps.event.ad";
_htmlcode = _htmlcode+"; google.maps.event.addListener(marker"+BA.NumberToString(_n)+", 'dragend', function(mouseEvent) {";
 //BA.debugLineNum = 976;BA.debugLine="HtmlCode = HtmlCode & \"var lat = mouseEvent.l";
_htmlcode = _htmlcode+"var lat = mouseEvent.latLng.lat();";
 //BA.debugLineNum = 977;BA.debugLine="HtmlCode = HtmlCode & \"var lng = mouseEvent.l";
_htmlcode = _htmlcode+"var lng = mouseEvent.latLng.lng();";
 //BA.debugLineNum = 978;BA.debugLine="HtmlCode = HtmlCode & \"var latlng1 = new goog";
_htmlcode = _htmlcode+"var latlng1 = new google.maps.LatLng(lat, lng); ";
 //BA.debugLineNum = 980;BA.debugLine="HtmlCode = HtmlCode & \"map.setCenter(latlng1)";
_htmlcode = _htmlcode+"map.setCenter(latlng1);";
 //BA.debugLineNum = 982;BA.debugLine="HtmlCode = HtmlCode & \"B4A.CallSub('MapViewer";
_htmlcode = _htmlcode+"B4A.CallSub('MapViewer_MarkerDragEnd', true, '"+BA.NumberToString(_i)+"', lat, lng); ";
 //BA.debugLineNum = 986;BA.debugLine="HtmlCode = HtmlCode & \"polyline.setMap(null);";
_htmlcode = _htmlcode+"polyline.setMap(null);";
 //BA.debugLineNum = 987;BA.debugLine="HtmlCode = HtmlCode & \"points.push(latlng1);\"";
_htmlcode = _htmlcode+"points.push(latlng1);";
 //BA.debugLineNum = 988;BA.debugLine="HtmlCode = HtmlCode & \"var polyline1 = new go";
_htmlcode = _htmlcode+"var polyline1 = new google.maps.Polyline({path: points, strokeColor: '00ff00', strokeOpacity: "+BA.NumberToString(_polylineopacity)+", strokeWeight: "+BA.NumberToString(_polylinewidth)+"});";
 //BA.debugLineNum = 989;BA.debugLine="HtmlCode = HtmlCode & \"polyline1.setMap(map);";
_htmlcode = _htmlcode+"polyline1.setMap(map);";
 //BA.debugLineNum = 998;BA.debugLine="HtmlCode = HtmlCode & \"var polyline1 = new go";
_htmlcode = _htmlcode+"var polyline1 = new google.maps.Polyline({path: points, strokeColor: '00ff00', strokeOpacity: "+BA.NumberToString(_polylineopacity)+", strokeWeight: "+BA.NumberToString(_polylinewidth)+"});";
 //BA.debugLineNum = 999;BA.debugLine="HtmlCode = HtmlCode & \"polyline1.setMap(map);";
_htmlcode = _htmlcode+"polyline1.setMap(map);";
 //BA.debugLineNum = 1002;BA.debugLine="HtmlCode = HtmlCode & \"})\"";
_htmlcode = _htmlcode+"})";
 };
 }
};
 };
 };
 //BA.debugLineNum = 1007;BA.debugLine="HtmlCode = HtmlCode & \"; }</script></head><body o";
_htmlcode = _htmlcode+"; }</script></head><body onload='initialize()'>  <div id='map_canvas' style='width:100%; height:100%'></div></body></html>";
 //BA.debugLineNum = 1009;BA.debugLine="MapViewer.LoadHtml(HtmlCode)";
mostCurrent._mapviewer.LoadHtml(_htmlcode);
 //BA.debugLineNum = 1011;BA.debugLine="End Sub";
return "";
}
public static String  _mapdisptimer_tick() throws Exception{
 //BA.debugLineNum = 354;BA.debugLine="Sub MapDispTimer_Tick";
 //BA.debugLineNum = 356;BA.debugLine="MapDispTimer.Enabled = False";
_mapdisptimer.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 357;BA.debugLine="MapViewer_MouseUp(\"0\", \"0\")";
_mapviewer_mouseup("0","0");
 //BA.debugLineNum = 358;BA.debugLine="End Sub";
return "";
}
public static String  _mapshow() throws Exception{
 //BA.debugLineNum = 832;BA.debugLine="Sub MapShow";
 //BA.debugLineNum = 834;BA.debugLine="If Map_On = True Then";
if (_map_on==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 835;BA.debugLine="MapViewer.Visible = True";
mostCurrent._mapviewer.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 836;BA.debugLine="pnlMap.Visible = True";
mostCurrent._pnlmap.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 837;BA.debugLine="If DispMapPolylne = True Or DispMapMarkers = Tru";
if (_dispmappolylne==anywheresoftware.b4a.keywords.Common.True || _dispmapmarkers==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 838;BA.debugLine="MapCenter.Latitude = GPSPathLatMean";
_mapcenter.setLatitude(_gpspathlatmean);
 //BA.debugLineNum = 839;BA.debugLine="MapCenter.Longitude = GPSPathLngMean";
_mapcenter.setLongitude(_gpspathlngmean);
 }else {
 //BA.debugLineNum = 841;BA.debugLine="MapCenter.Latitude = MapDefaultLat";
_mapcenter.setLatitude(_mapdefaultlat);
 //BA.debugLineNum = 842;BA.debugLine="MapCenter.Longitude = MapDefaultLng";
_mapcenter.setLongitude(_mapdefaultlng);
 };
 //BA.debugLineNum = 844;BA.debugLine="If ShowGPSOnMap = True And GPS_On = True Then";
if (_showgpsonmap==anywheresoftware.b4a.keywords.Common.True && _gps_on==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 845;BA.debugLine="MapDisp(MapCenter.Latitude, MapCenter.Longitude";
_mapdisp((float) (_mapcenter.getLatitude()),(float) (_mapcenter.getLongitude()),_mapsetdraggable,_mapzoomlevel,_dispmaptypecontrol,_dispmaptypecontrolid,_dispmapzoomcontrol,"LEFT_CENTER",_dispmapzoomcontroltype,_dispmapscalecontrol,"TOP_RIGHT",_dispmapcenter,_gpspath,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,_maplinecolor,_maplineopacity,_maplinewidth);
 }else {
 //BA.debugLineNum = 847;BA.debugLine="MapDisp(MapCenter.Latitude, MapCenter.Longitude";
_mapdisp((float) (_mapcenter.getLatitude()),(float) (_mapcenter.getLongitude()),_mapsetdraggable,_mapzoomlevel,_dispmaptypecontrol,_dispmaptypecontrolid,_dispmapzoomcontrol,"LEFT_CENTER",_dispmapzoomcontroltype,_dispmapscalecontrol,"TOP_RIGHT",_dispmapcenter,_gpspath,_dispmapmarkers,_mapmarkerclickable,_mapmarkerdragable,_dispmappolylne,_maplinecolor,_maplineopacity,_maplinewidth);
 };
 };
 //BA.debugLineNum = 850;BA.debugLine="End Sub";
return "";
}
public static String  _mapviewer_centerchanged(String _latstr,String _lngstr) throws Exception{
double _lat = 0;
double _lng = 0;
 //BA.debugLineNum = 416;BA.debugLine="Sub MapViewer_CenterChanged(LatStr As String, LngS";
 //BA.debugLineNum = 418;BA.debugLine="Dim Lat, Lng As Double";
_lat = 0;
_lng = 0;
 //BA.debugLineNum = 420;BA.debugLine="MapCenter.Latitude = LatStr";
_mapcenter.setLatitude((double)(Double.parseDouble(_latstr)));
 //BA.debugLineNum = 421;BA.debugLine="MapCenter.Longitude = LngStr";
_mapcenter.setLongitude((double)(Double.parseDouble(_lngstr)));
 //BA.debugLineNum = 423;BA.debugLine="CalcMapScales";
_calcmapscales();
 //BA.debugLineNum = 424;BA.debugLine="End Sub";
return "";
}
public static String  _mapviewer_click(String _latstr,String _lngstr) throws Exception{
double _lat = 0;
double _lng = 0;
 //BA.debugLineNum = 360;BA.debugLine="Sub MapViewer_Click(LatStr As String, LngStr As St";
 //BA.debugLineNum = 362;BA.debugLine="Dim Lat, Lng As Double";
_lat = 0;
_lng = 0;
 //BA.debugLineNum = 364;BA.debugLine="Lat = LatStr";
_lat = (double)(Double.parseDouble(_latstr));
 //BA.debugLineNum = 365;BA.debugLine="Lng = LngStr";
_lng = (double)(Double.parseDouble(_lngstr));
 //BA.debugLineNum = 367;BA.debugLine="lblLatitude.Text = NumberFormat(Lat, 1, 6)";
mostCurrent._lbllatitude.setText((Object)(anywheresoftware.b4a.keywords.Common.NumberFormat(_lat,(int) (1),(int) (6))));
 //BA.debugLineNum = 368;BA.debugLine="lblLongitude.Text = NumberFormat(Lng, 1, 6)";
mostCurrent._lbllongitude.setText((Object)(anywheresoftware.b4a.keywords.Common.NumberFormat(_lng,(int) (1),(int) (6))));
 //BA.debugLineNum = 369;BA.debugLine="pnlDispGPSLatLng.Visible = True";
mostCurrent._pnldispgpslatlng.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 370;BA.debugLine="DrawCursor(Lat, Lng)";
_drawcursor(_lat,_lng);
 //BA.debugLineNum = 371;BA.debugLine="MapDispTimer.Enabled = True";
_mapdisptimer.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 372;BA.debugLine="End Sub";
return "";
}
public static String  _mapviewer_maptypecontrolchanged(String _mapcontrolid) throws Exception{
 //BA.debugLineNum = 432;BA.debugLine="Sub MapViewer_MapTypeControlChanged(MapControlID A";
 //BA.debugLineNum = 434;BA.debugLine="DispMapTypeControlID = MapControlID.ToUpperCase";
_dispmaptypecontrolid = _mapcontrolid.toUpperCase();
 //BA.debugLineNum = 435;BA.debugLine="End Sub";
return "";
}
public static String  _mapviewer_markerdragend(String _indexstr,String _latstr,String _lngstr) throws Exception{
double _lat = 0;
double _lng = 0;
int _index = 0;
 //BA.debugLineNum = 437;BA.debugLine="Sub MapViewer_MarkerDragEnd(IndexStr As String, La";
 //BA.debugLineNum = 439;BA.debugLine="Dim Lat, Lng As Double";
_lat = 0;
_lng = 0;
 //BA.debugLineNum = 440;BA.debugLine="Dim Index As Int";
_index = 0;
 //BA.debugLineNum = 441;BA.debugLine="Index = IndexStr";
_index = (int)(Double.parseDouble(_indexstr));
 //BA.debugLineNum = 442;BA.debugLine="Lat = LatStr";
_lat = (double)(Double.parseDouble(_latstr));
 //BA.debugLineNum = 443;BA.debugLine="Lng = LngStr";
_lng = (double)(Double.parseDouble(_lngstr));
 //BA.debugLineNum = 445;BA.debugLine="lblLatitude.Text = NumberFormat(Lat, 1, 6)";
mostCurrent._lbllatitude.setText((Object)(anywheresoftware.b4a.keywords.Common.NumberFormat(_lat,(int) (1),(int) (6))));
 //BA.debugLineNum = 446;BA.debugLine="lblLongitude.Text = NumberFormat(Lng, 1, 6)";
mostCurrent._lbllongitude.setText((Object)(anywheresoftware.b4a.keywords.Common.NumberFormat(_lng,(int) (1),(int) (6))));
 //BA.debugLineNum = 447;BA.debugLine="pnlDispGPSLatLng.Visible = True";
mostCurrent._pnldispgpslatlng.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 449;BA.debugLine="MapViewer_CenterChanged(Lat, Lng)";
_mapviewer_centerchanged(BA.NumberToString(_lat),BA.NumberToString(_lng));
 //BA.debugLineNum = 450;BA.debugLine="GPSPathModifPoint(Index, Lat, Lng)";
_gpspathmodifpoint(_index,_lat,_lng);
 //BA.debugLineNum = 453;BA.debugLine="End Sub";
return "";
}
public static String  _mapviewer_mousedown(String _latstr,String _lngstr) throws Exception{
double _lat = 0;
double _lng = 0;
 //BA.debugLineNum = 374;BA.debugLine="Sub MapViewer_MouseDown(LatStr As String, LngStr A";
 //BA.debugLineNum = 376;BA.debugLine="Dim Lat, Lng As Double";
_lat = 0;
_lng = 0;
 //BA.debugLineNum = 378;BA.debugLine="Lat = LatStr";
_lat = (double)(Double.parseDouble(_latstr));
 //BA.debugLineNum = 379;BA.debugLine="Lng = LngStr";
_lng = (double)(Double.parseDouble(_lngstr));
 //BA.debugLineNum = 381;BA.debugLine="lblLatitude.Text = NumberFormat(Lat, 1, 6)";
mostCurrent._lbllatitude.setText((Object)(anywheresoftware.b4a.keywords.Common.NumberFormat(_lat,(int) (1),(int) (6))));
 //BA.debugLineNum = 382;BA.debugLine="lblLongitude.Text = NumberFormat(Lng, 1, 6)";
mostCurrent._lbllongitude.setText((Object)(anywheresoftware.b4a.keywords.Common.NumberFormat(_lng,(int) (1),(int) (6))));
 //BA.debugLineNum = 383;BA.debugLine="pnlDispGPSLatLng.Visible = True";
mostCurrent._pnldispgpslatlng.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 384;BA.debugLine="DrawCursor(Lat, Lng)";
_drawcursor(_lat,_lng);
 //BA.debugLineNum = 385;BA.debugLine="If MapSetDraggable = True Then";
if (_mapsetdraggable==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 386;BA.debugLine="MapDispTimer.Enabled = True";
_mapdisptimer.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 388;BA.debugLine="End Sub";
return "";
}
public static String  _mapviewer_mousemove(String _latstr,String _lngstr) throws Exception{
double _lat = 0;
double _lng = 0;
 //BA.debugLineNum = 390;BA.debugLine="Sub MapViewer_MouseMove(LatStr As String, LngStr A";
 //BA.debugLineNum = 392;BA.debugLine="Dim Lat, Lng As Double";
_lat = 0;
_lng = 0;
 //BA.debugLineNum = 394;BA.debugLine="Lat = LatStr";
_lat = (double)(Double.parseDouble(_latstr));
 //BA.debugLineNum = 395;BA.debugLine="Lng = LngStr";
_lng = (double)(Double.parseDouble(_lngstr));
 //BA.debugLineNum = 397;BA.debugLine="lblLatitude.Text = NumberFormat(Lat, 1, 6)";
mostCurrent._lbllatitude.setText((Object)(anywheresoftware.b4a.keywords.Common.NumberFormat(_lat,(int) (1),(int) (6))));
 //BA.debugLineNum = 398;BA.debugLine="lblLongitude.Text = NumberFormat(Lng, 1, 6)";
mostCurrent._lbllongitude.setText((Object)(anywheresoftware.b4a.keywords.Common.NumberFormat(_lng,(int) (1),(int) (6))));
 //BA.debugLineNum = 399;BA.debugLine="pnlDispGPSLatLng.Visible = True";
mostCurrent._pnldispgpslatlng.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 400;BA.debugLine="DrawCursor(Lat, Lng)";
_drawcursor(_lat,_lng);
 //BA.debugLineNum = 401;BA.debugLine="End Sub";
return "";
}
public static String  _mapviewer_mouseup(String _latstr,String _lngstr) throws Exception{
double _lat = 0;
double _lng = 0;
 //BA.debugLineNum = 403;BA.debugLine="Sub MapViewer_MouseUp(LatStr As String, LngStr As";
 //BA.debugLineNum = 405;BA.debugLine="Dim Lat, Lng As Double";
_lat = 0;
_lng = 0;
 //BA.debugLineNum = 412;BA.debugLine="pnlDispGPSLatLng.Visible = False";
mostCurrent._pnldispgpslatlng.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 413;BA.debugLine="DrawCursor(-1,-1)";
_drawcursor(-1,-1);
 //BA.debugLineNum = 414;BA.debugLine="End Sub";
return "";
}
public static String  _mapviewer_zoomchanged(String _zoomlevel) throws Exception{
 //BA.debugLineNum = 426;BA.debugLine="Sub MapViewer_ZoomChanged(ZoomLevel As String)";
 //BA.debugLineNum = 428;BA.debugLine="MapZoomLevel = ZoomLevel";
_mapzoomlevel = (int)(Double.parseDouble(_zoomlevel));
 //BA.debugLineNum = 429;BA.debugLine="CalcMapScales";
_calcmapscales();
 //BA.debugLineNum = 430;BA.debugLine="End Sub";
return "";
}
public static boolean  _pnlgpspathtoolbox_touch(int _action,float _x,float _y) throws Exception{
 //BA.debugLineNum = 777;BA.debugLine="Sub	pnlGPSPathToolbox_Touch(Action As Int, x As Fl";
 //BA.debugLineNum = 779;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 780;BA.debugLine="End Sub";
return false;
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
satellites._process_globals();
setup._process_globals();
gpspaths._process_globals();
gpssave._process_globals();
gpsmodule._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 13;BA.debugLine="Dim ProgName As String								: ProgName = \"ไปไหน";
_progname = "";
 //BA.debugLineNum = 13;BA.debugLine="Dim ProgName As String								: ProgName = \"ไปไหน";
_progname = "ไปไหนมาล่ะอิห่า";
 //BA.debugLineNum = 14;BA.debugLine="Dim ProgVersion As String							: ProgVersion = \"";
_progversion = "";
 //BA.debugLineNum = 14;BA.debugLine="Dim ProgVersion As String							: ProgVersion = \"";
_progversion = "V 1.6.1";
 //BA.debugLineNum = 16;BA.debugLine="Dim GPS1 As GPS";
_gps1 = new anywheresoftware.b4a.gps.GPS();
 //BA.debugLineNum = 17;BA.debugLine="Dim PhoneAwake As PhoneWakeState";
_phoneawake = new anywheresoftware.b4a.phone.Phone.PhoneWakeState();
 //BA.debugLineNum = 19;BA.debugLine="Dim MapDispTimer As Timer";
_mapdisptimer = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 21;BA.debugLine="Type Position (Latitude As Double, Longitude As D";
;
 //BA.debugLineNum = 23;BA.debugLine="Dim MapCenter As Location";
_mapcenter = new anywheresoftware.b4a.gps.LocationWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Dim MapOldCenter As Location";
_mapoldcenter = new anywheresoftware.b4a.gps.LocationWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Dim MapSetDraggable As Boolean				: MapSetDraggab";
_mapsetdraggable = false;
 //BA.debugLineNum = 25;BA.debugLine="Dim MapSetDraggable As Boolean				: MapSetDraggab";
_mapsetdraggable = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 26;BA.debugLine="Dim MapDefaultZoomLevelIndex As Int		: MapDefault";
_mapdefaultzoomlevelindex = 0;
 //BA.debugLineNum = 26;BA.debugLine="Dim MapDefaultZoomLevelIndex As Int		: MapDefault";
_mapdefaultzoomlevelindex = (int) (1);
 //BA.debugLineNum = 27;BA.debugLine="Dim MapDefaultZoomLevel As Int				: MapDefaultZoo";
_mapdefaultzoomlevel = 0;
 //BA.debugLineNum = 27;BA.debugLine="Dim MapDefaultZoomLevel As Int				: MapDefaultZoo";
_mapdefaultzoomlevel = (int) (11);
 //BA.debugLineNum = 28;BA.debugLine="Dim MapZoomLevel As Int								: MapZoomLevel = M";
_mapzoomlevel = 0;
 //BA.debugLineNum = 28;BA.debugLine="Dim MapZoomLevel As Int								: MapZoomLevel = M";
_mapzoomlevel = _mapdefaultzoomlevel;
 //BA.debugLineNum = 29;BA.debugLine="Dim MapZoomLevelOld As Int						: MapZoomLevelOld";
_mapzoomlevelold = 0;
 //BA.debugLineNum = 29;BA.debugLine="Dim MapZoomLevelOld As Int						: MapZoomLevelOld";
_mapzoomlevelold = _mapdefaultzoomlevel;
 //BA.debugLineNum = 30;BA.debugLine="Dim MapZoomCalculated As Boolean			: MapZoomCalcu";
_mapzoomcalculated = false;
 //BA.debugLineNum = 30;BA.debugLine="Dim MapZoomCalculated As Boolean			: MapZoomCalcu";
_mapzoomcalculated = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 31;BA.debugLine="Dim MapDefaultLat	As Double						: MapDefaultLat";
_mapdefaultlat = 0;
 //BA.debugLineNum = 31;BA.debugLine="Dim MapDefaultLat	As Double						: MapDefaultLat";
_mapdefaultlat = 46.15;
 //BA.debugLineNum = 32;BA.debugLine="Dim MapDefaultLng	As Double						: MapDefaultLng";
_mapdefaultlng = 0;
 //BA.debugLineNum = 32;BA.debugLine="Dim MapDefaultLng	As Double						: MapDefaultLng";
_mapdefaultlng = 7.143;
 //BA.debugLineNum = 33;BA.debugLine="Dim MapLineWidthIndex As Int					: MapLineWidthIn";
_maplinewidthindex = 0;
 //BA.debugLineNum = 33;BA.debugLine="Dim MapLineWidthIndex As Int					: MapLineWidthIn";
_maplinewidthindex = (int) (2);
 //BA.debugLineNum = 34;BA.debugLine="Dim MapLineWidth As Int								: MapLineWidth = 3";
_maplinewidth = 0;
 //BA.debugLineNum = 34;BA.debugLine="Dim MapLineWidth As Int								: MapLineWidth = 3";
_maplinewidth = (int) (3);
 //BA.debugLineNum = 35;BA.debugLine="Dim MapLineColorIndex As Int					: MapLineColorIn";
_maplinecolorindex = 0;
 //BA.debugLineNum = 35;BA.debugLine="Dim MapLineColorIndex As Int					: MapLineColorIn";
_maplinecolorindex = (int) (0);
 //BA.debugLineNum = 36;BA.debugLine="Dim MapLineColor As String						: MapLineColor =";
_maplinecolor = "";
 //BA.debugLineNum = 36;BA.debugLine="Dim MapLineColor As String						: MapLineColor =";
_maplinecolor = "ff0000";
 //BA.debugLineNum = 37;BA.debugLine="Dim MapLineOpacityIndex As Int				: MapLineOpacit";
_maplineopacityindex = 0;
 //BA.debugLineNum = 37;BA.debugLine="Dim MapLineOpacityIndex As Int				: MapLineOpacit";
_maplineopacityindex = (int) (1);
 //BA.debugLineNum = 38;BA.debugLine="Dim MapLineOpacity As Float						: MapLineOpacity";
_maplineopacity = 0f;
 //BA.debugLineNum = 38;BA.debugLine="Dim MapLineOpacity As Float						: MapLineOpacity";
_maplineopacity = (float) (0.50);
 //BA.debugLineNum = 39;BA.debugLine="Dim MapMarkerClickable As Boolean			: MapMarkerCl";
_mapmarkerclickable = false;
 //BA.debugLineNum = 39;BA.debugLine="Dim MapMarkerClickable As Boolean			: MapMarkerCl";
_mapmarkerclickable = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 40;BA.debugLine="Dim MapMarkerDragable As Boolean			: MapMarkerDra";
_mapmarkerdragable = false;
 //BA.debugLineNum = 40;BA.debugLine="Dim MapMarkerDragable As Boolean			: MapMarkerDra";
_mapmarkerdragable = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 41;BA.debugLine="Dim MapXCursor As Float								: MapXCursor = -1";
_mapxcursor = 0f;
 //BA.debugLineNum = 41;BA.debugLine="Dim MapXCursor As Float								: MapXCursor = -1";
_mapxcursor = (float) (-1);
 //BA.debugLineNum = 42;BA.debugLine="Dim MapYCursor As Float								: MapYCursor = -1";
_mapycursor = 0f;
 //BA.debugLineNum = 42;BA.debugLine="Dim MapYCursor As Float								: MapYCursor = -1";
_mapycursor = (float) (-1);
 //BA.debugLineNum = 44;BA.debugLine="Type GPSLocation(Latitude As Double, Longitude As";
;
 //BA.debugLineNum = 45;BA.debugLine="Dim GPSPath As List";
_gpspath = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 46;BA.debugLine="Dim GPSDir As String									: GPSDir = File.DirR";
_gpsdir = "";
 //BA.debugLineNum = 46;BA.debugLine="Dim GPSDir As String									: GPSDir = File.DirR";
_gpsdir = anywheresoftware.b4a.keywords.Common.File.getDirRootExternal();
 //BA.debugLineNum = 48;BA.debugLine="Dim GPSPathFilename As String					: GPSPathFilena";
_gpspathfilename = "";
 //BA.debugLineNum = 48;BA.debugLine="Dim GPSPathFilename As String					: GPSPathFilena";
_gpspathfilename = "Test1.GPP";
 //BA.debugLineNum = 49;BA.debugLine="Dim GPSPathFilenameKML As String";
_gpspathfilenamekml = "";
 //BA.debugLineNum = 50;BA.debugLine="Dim GPSFilenameOld As String";
_gpsfilenameold = "";
 //BA.debugLineNum = 51;BA.debugLine="Dim GPSPathComment As String";
_gpspathcomment = "";
 //BA.debugLineNum = 53;BA.debugLine="Dim GPSPathLatMin, GPSPathLatMean, GPSPathLatMax";
_gpspathlatmin = 0;
_gpspathlatmean = 0;
_gpspathlatmax = 0;
 //BA.debugLineNum = 54;BA.debugLine="Dim GPSPathLngMin, GPSPathLngMean, GPSPathLngMax";
_gpspathlngmin = 0;
_gpspathlngmean = 0;
_gpspathlngmax = 0;
 //BA.debugLineNum = 55;BA.debugLine="Dim GPSMinTime As Long								: GPSMinTime = 5000";
_gpsmintime = 0L;
 //BA.debugLineNum = 55;BA.debugLine="Dim GPSMinTime As Long								: GPSMinTime = 5000";
_gpsmintime = (long) (5000);
 //BA.debugLineNum = 56;BA.debugLine="Dim GPSMinDistance As Float						: GPSMinDistance";
_gpsmindistance = 0f;
 //BA.debugLineNum = 56;BA.debugLine="Dim GPSMinDistance As Float						: GPSMinDistance";
_gpsmindistance = (float) (10);
 //BA.debugLineNum = 57;BA.debugLine="Dim GPSDispSpeed As Boolean						: GPSDispSpeed =";
_gpsdispspeed = false;
 //BA.debugLineNum = 57;BA.debugLine="Dim GPSDispSpeed As Boolean						: GPSDispSpeed =";
_gpsdispspeed = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 58;BA.debugLine="Dim GPSDispBearing As Boolean					: GPSDispBearin";
_gpsdispbearing = false;
 //BA.debugLineNum = 58;BA.debugLine="Dim GPSDispBearing As Boolean					: GPSDispBearin";
_gpsdispbearing = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 59;BA.debugLine="Dim GPSDispWindrose As Boolean				: GPSDispWindro";
_gpsdispwindrose = false;
 //BA.debugLineNum = 59;BA.debugLine="Dim GPSDispWindrose As Boolean				: GPSDispWindro";
_gpsdispwindrose = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 60;BA.debugLine="Dim GPSPathPreviuous As Location";
_gpspathpreviuous = new anywheresoftware.b4a.gps.LocationWrapper();
 //BA.debugLineNum = 61;BA.debugLine="Dim GPSPathDistTotPrev As Double";
_gpspathdisttotprev = 0;
 //BA.debugLineNum = 62;BA.debugLine="Dim GPSPathTime0 As Long";
_gpspathtime0 = 0L;
 //BA.debugLineNum = 63;BA.debugLine="Dim GPSFilterDelta As Double					: GPSFilterDelta";
_gpsfilterdelta = 0;
 //BA.debugLineNum = 63;BA.debugLine="Dim GPSFilterDelta As Double					: GPSFilterDelta";
_gpsfilterdelta = 5;
 //BA.debugLineNum = 64;BA.debugLine="Dim GPSDistance As Double";
_gpsdistance = 0;
 //BA.debugLineNum = 65;BA.debugLine="Dim GPSMarkerIndexes As List";
_gpsmarkerindexes = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 66;BA.debugLine="Dim flagscvGPSPath0 As Boolean				: flagscvGPSPat";
_flagscvgpspath0 = false;
 //BA.debugLineNum = 66;BA.debugLine="Dim flagscvGPSPath0 As Boolean				: flagscvGPSPat";
_flagscvgpspath0 = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 67;BA.debugLine="Dim flagscvGPSPath1 As Boolean				: flagscvGPSPat";
_flagscvgpspath1 = false;
 //BA.debugLineNum = 67;BA.debugLine="Dim flagscvGPSPath1 As Boolean				: flagscvGPSPat";
_flagscvgpspath1 = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 69;BA.debugLine="Dim GPS_On As Boolean									: GPS_On = False";
_gps_on = false;
 //BA.debugLineNum = 69;BA.debugLine="Dim GPS_On As Boolean									: GPS_On = False";
_gps_on = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 70;BA.debugLine="Dim GPSPathModified As Boolean				: GPSPathModifi";
_gpspathmodified = false;
 //BA.debugLineNum = 70;BA.debugLine="Dim GPSPathModified As Boolean				: GPSPathModifi";
_gpspathmodified = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 71;BA.debugLine="Dim Map_On As Boolean									: Map_On = False";
_map_on = false;
 //BA.debugLineNum = 71;BA.debugLine="Dim Map_On As Boolean									: Map_On = False";
_map_on = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 72;BA.debugLine="Dim Sats_On As Boolean								: Sats_On = False";
_sats_on = false;
 //BA.debugLineNum = 72;BA.debugLine="Dim Sats_On As Boolean								: Sats_On = False";
_sats_on = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 73;BA.debugLine="Dim Setup_On As Boolean								: Setup_On = False";
_setup_on = false;
 //BA.debugLineNum = 73;BA.debugLine="Dim Setup_On As Boolean								: Setup_On = False";
_setup_on = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 75;BA.debugLine="Dim MapScaleLat, MapScaleLng As Double";
_mapscalelat = 0;
_mapscalelng = 0;
 //BA.debugLineNum = 76;BA.debugLine="Dim MapX, MapY As Float";
_mapx = 0f;
_mapy = 0f;
 //BA.debugLineNum = 77;BA.debugLine="Dim TileSize As Int												: TileSize = 256";
_tilesize = 0;
 //BA.debugLineNum = 77;BA.debugLine="Dim TileSize As Int												: TileSize = 256";
_tilesize = (int) (256);
 //BA.debugLineNum = 79;BA.debugLine="Dim DispMapTypeControl As Boolean					: DispMapTy";
_dispmaptypecontrol = false;
 //BA.debugLineNum = 79;BA.debugLine="Dim DispMapTypeControl As Boolean					: DispMapTy";
_dispmaptypecontrol = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 80;BA.debugLine="Dim DispMapTypeControlIDNumber As Int			: DispMap";
_dispmaptypecontrolidnumber = 0;
 //BA.debugLineNum = 80;BA.debugLine="Dim DispMapTypeControlIDNumber As Int			: DispMap";
_dispmaptypecontrolidnumber = (int) (4);
 //BA.debugLineNum = 81;BA.debugLine="Dim DispMapTypeControlIDIndex As Int			: DispMapT";
_dispmaptypecontrolidindex = 0;
 //BA.debugLineNum = 81;BA.debugLine="Dim DispMapTypeControlIDIndex As Int			: DispMapT";
_dispmaptypecontrolidindex = (int) (2);
 //BA.debugLineNum = 82;BA.debugLine="Dim DispMapTypeControlIDText(DispMapTypeControlID";
_dispmaptypecontrolidtext = new String[_dispmaptypecontrolidnumber];
java.util.Arrays.fill(_dispmaptypecontrolidtext,"");
 //BA.debugLineNum = 83;BA.debugLine="DispMapTypeControlIDText(0) = \"ROADMAP\"";
_dispmaptypecontrolidtext[(int) (0)] = "ROADMAP";
 //BA.debugLineNum = 84;BA.debugLine="DispMapTypeControlIDText(1) = \"TERRAIN\"";
_dispmaptypecontrolidtext[(int) (1)] = "TERRAIN";
 //BA.debugLineNum = 85;BA.debugLine="DispMapTypeControlIDText(2) = \"SATELLITE\"";
_dispmaptypecontrolidtext[(int) (2)] = "SATELLITE";
 //BA.debugLineNum = 86;BA.debugLine="DispMapTypeControlIDText(3) = \"HYBRID\"";
_dispmaptypecontrolidtext[(int) (3)] = "HYBRID";
 //BA.debugLineNum = 87;BA.debugLine="Dim DispMapTypeControlID As String";
_dispmaptypecontrolid = "";
 //BA.debugLineNum = 88;BA.debugLine="DispMapTypeControlID = DispMapTypeControlIDText(";
_dispmaptypecontrolid = _dispmaptypecontrolidtext[_dispmaptypecontrolidindex];
 //BA.debugLineNum = 90;BA.debugLine="Dim DispMapZoomControl As Boolean					: DispMapZo";
_dispmapzoomcontrol = false;
 //BA.debugLineNum = 90;BA.debugLine="Dim DispMapZoomControl As Boolean					: DispMapZo";
_dispmapzoomcontrol = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 91;BA.debugLine="Dim DispMapZoomControlTypeNumber As Int		: DispMa";
_dispmapzoomcontroltypenumber = 0;
 //BA.debugLineNum = 91;BA.debugLine="Dim DispMapZoomControlTypeNumber As Int		: DispMa";
_dispmapzoomcontroltypenumber = (int) (3);
 //BA.debugLineNum = 92;BA.debugLine="Dim DispMapZoomControlTypeIndex As Int		: DispMap";
_dispmapzoomcontroltypeindex = 0;
 //BA.debugLineNum = 92;BA.debugLine="Dim DispMapZoomControlTypeIndex As Int		: DispMap";
_dispmapzoomcontroltypeindex = (int) (2);
 //BA.debugLineNum = 93;BA.debugLine="Dim DispMapZoomControlTypeText(DispMapZoomControl";
_dispmapzoomcontroltypetext = new String[_dispmapzoomcontroltypenumber];
java.util.Arrays.fill(_dispmapzoomcontroltypetext,"");
 //BA.debugLineNum = 94;BA.debugLine="DispMapZoomControlTypeText(0) = \"DEFAULT\"";
_dispmapzoomcontroltypetext[(int) (0)] = "DEFAULT";
 //BA.debugLineNum = 95;BA.debugLine="DispMapZoomControlTypeText(1) = \"SMALL\"";
_dispmapzoomcontroltypetext[(int) (1)] = "SMALL";
 //BA.debugLineNum = 96;BA.debugLine="DispMapZoomControlTypeText(2) = \"LARGE\"";
_dispmapzoomcontroltypetext[(int) (2)] = "LARGE";
 //BA.debugLineNum = 97;BA.debugLine="Dim DispMapZoomControlType As String";
_dispmapzoomcontroltype = "";
 //BA.debugLineNum = 98;BA.debugLine="DispMapZoomControlType = DispMapZoomControlTypeT";
_dispmapzoomcontroltype = _dispmapzoomcontroltypetext[_dispmapzoomcontroltypeindex];
 //BA.debugLineNum = 100;BA.debugLine="Dim DispMapScaleControl As Boolean	: DispMapScale";
_dispmapscalecontrol = false;
 //BA.debugLineNum = 100;BA.debugLine="Dim DispMapScaleControl As Boolean	: DispMapScale";
_dispmapscalecontrol = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 101;BA.debugLine="Dim DispMapCenter As Boolean				: DispMapCenter =";
_dispmapcenter = false;
 //BA.debugLineNum = 101;BA.debugLine="Dim DispMapCenter As Boolean				: DispMapCenter =";
_dispmapcenter = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 102;BA.debugLine="Dim DispMapMarkers As Boolean				: DispMapMarkers";
_dispmapmarkers = false;
 //BA.debugLineNum = 102;BA.debugLine="Dim DispMapMarkers As Boolean				: DispMapMarkers";
_dispmapmarkers = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 103;BA.debugLine="Dim DispMapPolylne As Boolean				: DispMapPolylne";
_dispmappolylne = false;
 //BA.debugLineNum = 103;BA.debugLine="Dim DispMapPolylne As Boolean				: DispMapPolylne";
_dispmappolylne = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 104;BA.debugLine="Dim ShowGPSOnMap As Boolean					: ShowGPSOnMap =";
_showgpsonmap = false;
 //BA.debugLineNum = 104;BA.debugLine="Dim ShowGPSOnMap As Boolean					: ShowGPSOnMap =";
_showgpsonmap = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 105;BA.debugLine="Dim SaveGPSPath As Boolean					: SaveGPSPath = Tr";
_savegpspath = false;
 //BA.debugLineNum = 105;BA.debugLine="Dim SaveGPSPath As Boolean					: SaveGPSPath = Tr";
_savegpspath = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 106;BA.debugLine="Dim DrawGPSPath As Boolean					: DrawGPSPath = Tr";
_drawgpspath = false;
 //BA.debugLineNum = 106;BA.debugLine="Dim DrawGPSPath As Boolean					: DrawGPSPath = Tr";
_drawgpspath = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 108;BA.debugLine="Dim AltitudeUnitNumber As Int				: AltitudeUnitNu";
_altitudeunitnumber = 0;
 //BA.debugLineNum = 108;BA.debugLine="Dim AltitudeUnitNumber As Int				: AltitudeUnitNu";
_altitudeunitnumber = (int) (2);
 //BA.debugLineNum = 109;BA.debugLine="Dim AltitudeUnitIndex As Int				: AltitudeUnitInd";
_altitudeunitindex = 0;
 //BA.debugLineNum = 109;BA.debugLine="Dim AltitudeUnitIndex As Int				: AltitudeUnitInd";
_altitudeunitindex = (int) (0);
 //BA.debugLineNum = 110;BA.debugLine="Dim AltitudeUnitRatio(AltitudeUnitNumber) As Doub";
_altitudeunitratio = new double[_altitudeunitnumber];
;
 //BA.debugLineNum = 111;BA.debugLine="AltitudeUnitRatio(0) = 1";
_altitudeunitratio[(int) (0)] = 1;
 //BA.debugLineNum = 112;BA.debugLine="AltitudeUnitRatio(1) = 1 / 0.3048";
_altitudeunitratio[(int) (1)] = 1/(double)0.3048;
 //BA.debugLineNum = 113;BA.debugLine="Dim AltitudeUnitText(AltitudeUnitNumber) As Strin";
_altitudeunittext = new String[_altitudeunitnumber];
java.util.Arrays.fill(_altitudeunittext,"");
 //BA.debugLineNum = 114;BA.debugLine="AltitudeUnitText(0) = \"m\"";
_altitudeunittext[(int) (0)] = "m";
 //BA.debugLineNum = 115;BA.debugLine="AltitudeUnitText(1) = \"ft\"";
_altitudeunittext[(int) (1)] = "ft";
 //BA.debugLineNum = 117;BA.debugLine="Dim SpeedUnitNumber As Int					: SpeedUnitNumber";
_speedunitnumber = 0;
 //BA.debugLineNum = 117;BA.debugLine="Dim SpeedUnitNumber As Int					: SpeedUnitNumber";
_speedunitnumber = (int) (3);
 //BA.debugLineNum = 118;BA.debugLine="Dim SpeedUnitIndex As Int						: SpeedUnitIndex =";
_speedunitindex = 0;
 //BA.debugLineNum = 118;BA.debugLine="Dim SpeedUnitIndex As Int						: SpeedUnitIndex =";
_speedunitindex = (int) (0);
 //BA.debugLineNum = 119;BA.debugLine="Dim SpeedUnitRatio(SpeedUnitNumber) As Double";
_speedunitratio = new double[_speedunitnumber];
;
 //BA.debugLineNum = 120;BA.debugLine="SpeedUnitRatio(0) = 1";
_speedunitratio[(int) (0)] = 1;
 //BA.debugLineNum = 121;BA.debugLine="SpeedUnitRatio(1) = 3.6";
_speedunitratio[(int) (1)] = 3.6;
 //BA.debugLineNum = 122;BA.debugLine="SpeedUnitRatio(2) = 3.6 / 1.609344";
_speedunitratio[(int) (2)] = 3.6/(double)1.609344;
 //BA.debugLineNum = 123;BA.debugLine="Dim SpeedUnitText(SpeedUnitNumber) As String";
_speedunittext = new String[_speedunitnumber];
java.util.Arrays.fill(_speedunittext,"");
 //BA.debugLineNum = 124;BA.debugLine="SpeedUnitText(0) = \"m/s\"";
_speedunittext[(int) (0)] = "m/s";
 //BA.debugLineNum = 125;BA.debugLine="SpeedUnitText(1) = \"km/h\"";
_speedunittext[(int) (1)] = "km/h";
 //BA.debugLineNum = 126;BA.debugLine="SpeedUnitText(2) = \"mile/h\"";
_speedunittext[(int) (2)] = "mile/h";
 //BA.debugLineNum = 128;BA.debugLine="Dim DistanceUnitNumber As Int			: DistanceUnitNum";
_distanceunitnumber = 0;
 //BA.debugLineNum = 128;BA.debugLine="Dim DistanceUnitNumber As Int			: DistanceUnitNum";
_distanceunitnumber = (int) (3);
 //BA.debugLineNum = 129;BA.debugLine="Dim DistanceUnitIndex As Int			: DistanceUnitInde";
_distanceunitindex = 0;
 //BA.debugLineNum = 129;BA.debugLine="Dim DistanceUnitIndex As Int			: DistanceUnitInde";
_distanceunitindex = (int) (0);
 //BA.debugLineNum = 130;BA.debugLine="Dim DistanceUnitRatio(DistanceUnitNumber) As Doub";
_distanceunitratio = new double[_distanceunitnumber];
;
 //BA.debugLineNum = 131;BA.debugLine="DistanceUnitRatio(0) = 1";
_distanceunitratio[(int) (0)] = 1;
 //BA.debugLineNum = 132;BA.debugLine="DistanceUnitRatio(1) = 0.001";
_distanceunitratio[(int) (1)] = 0.001;
 //BA.debugLineNum = 133;BA.debugLine="DistanceUnitRatio(2) = 0.001 / 1.609344";
_distanceunitratio[(int) (2)] = 0.001/(double)1.609344;
 //BA.debugLineNum = 134;BA.debugLine="Dim DistanceUnitText(DistanceUnitNumber) As Strin";
_distanceunittext = new String[_distanceunitnumber];
java.util.Arrays.fill(_distanceunittext,"");
 //BA.debugLineNum = 135;BA.debugLine="DistanceUnitText(0) = \"m\"";
_distanceunittext[(int) (0)] = "m";
 //BA.debugLineNum = 136;BA.debugLine="DistanceUnitText(1) = \"km\"";
_distanceunittext[(int) (1)] = "km";
 //BA.debugLineNum = 137;BA.debugLine="DistanceUnitText(2) = \"mile\"";
_distanceunittext[(int) (2)] = "mile";
 //BA.debugLineNum = 139;BA.debugLine="Dim WindroseColor As Int					: WindroseColor = Co";
_windrosecolor = 0;
 //BA.debugLineNum = 139;BA.debugLine="Dim WindroseColor As Int					: WindroseColor = Co";
_windrosecolor = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (165),(int) (42),(int) (42));
 //BA.debugLineNum = 140;BA.debugLine="Dim WindroseTicksColor As Int			: WindroseTicksCo";
_windrosetickscolor = 0;
 //BA.debugLineNum = 140;BA.debugLine="Dim WindroseTicksColor As Int			: WindroseTicksCo";
_windrosetickscolor = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (165),(int) (42),(int) (42));
 //BA.debugLineNum = 141;BA.debugLine="Dim WindroseNeedleColor As Int		: WindroseNeedleC";
_windroseneedlecolor = 0;
 //BA.debugLineNum = 141;BA.debugLine="Dim WindroseNeedleColor As Int		: WindroseNeedleC";
_windroseneedlecolor = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 142;BA.debugLine="End Sub";
return "";
}
public static String  _savesetup() throws Exception{
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _tw = null;
 //BA.debugLineNum = 1077;BA.debugLine="Sub SaveSetup";
 //BA.debugLineNum = 1078;BA.debugLine="Dim tw As TextWriter";
_tw = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 1080;BA.debugLine="tw.Initialize(File.OpenOutput(File.DirInternal, \"";
_tw.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"Setup.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 1081;BA.debugLine="tw.WriteLine(\"v2\")						' used for version contro";
_tw.WriteLine("v2");
 //BA.debugLineNum = 1082;BA.debugLine="tw.WriteLine(MapDefaultLat)";
_tw.WriteLine(BA.NumberToString(_mapdefaultlat));
 //BA.debugLineNum = 1083;BA.debugLine="tw.WriteLine(MapDefaultLng)";
_tw.WriteLine(BA.NumberToString(_mapdefaultlng));
 //BA.debugLineNum = 1084;BA.debugLine="tw.WriteLine(MapSetDraggable)";
_tw.WriteLine(BA.ObjectToString(_mapsetdraggable));
 //BA.debugLineNum = 1085;BA.debugLine="tw.WriteLine(GPSMinTime)";
_tw.WriteLine(BA.NumberToString(_gpsmintime));
 //BA.debugLineNum = 1086;BA.debugLine="tw.WriteLine(GPSMinDistance)";
_tw.WriteLine(BA.NumberToString(_gpsmindistance));
 //BA.debugLineNum = 1087;BA.debugLine="tw.WriteLine(GPSDispSpeed)";
_tw.WriteLine(BA.ObjectToString(_gpsdispspeed));
 //BA.debugLineNum = 1088;BA.debugLine="tw.WriteLine(GPSDispBearing)";
_tw.WriteLine(BA.ObjectToString(_gpsdispbearing));
 //BA.debugLineNum = 1089;BA.debugLine="tw.WriteLine(GPSDispWindrose)";
_tw.WriteLine(BA.ObjectToString(_gpsdispwindrose));
 //BA.debugLineNum = 1090;BA.debugLine="tw.WriteLine(GPSFilterDelta)";
_tw.WriteLine(BA.NumberToString(_gpsfilterdelta));
 //BA.debugLineNum = 1091;BA.debugLine="tw.WriteLine(MapDefaultZoomLevelIndex)";
_tw.WriteLine(BA.NumberToString(_mapdefaultzoomlevelindex));
 //BA.debugLineNum = 1092;BA.debugLine="tw.WriteLine(MapDefaultZoomLevel)";
_tw.WriteLine(BA.NumberToString(_mapdefaultzoomlevel));
 //BA.debugLineNum = 1093;BA.debugLine="tw.WriteLine(MapLineWidthIndex)";
_tw.WriteLine(BA.NumberToString(_maplinewidthindex));
 //BA.debugLineNum = 1094;BA.debugLine="tw.WriteLine(MapLineWidth)";
_tw.WriteLine(BA.NumberToString(_maplinewidth));
 //BA.debugLineNum = 1095;BA.debugLine="tw.WriteLine(MapLineColorIndex)";
_tw.WriteLine(BA.NumberToString(_maplinecolorindex));
 //BA.debugLineNum = 1096;BA.debugLine="tw.WriteLine(MapLineColor)";
_tw.WriteLine(_maplinecolor);
 //BA.debugLineNum = 1097;BA.debugLine="tw.WriteLine(MapLineOpacityIndex)";
_tw.WriteLine(BA.NumberToString(_maplineopacityindex));
 //BA.debugLineNum = 1098;BA.debugLine="tw.WriteLine(MapLineOpacity)";
_tw.WriteLine(BA.NumberToString(_maplineopacity));
 //BA.debugLineNum = 1099;BA.debugLine="tw.WriteLine(MapMarkerClickable)";
_tw.WriteLine(BA.ObjectToString(_mapmarkerclickable));
 //BA.debugLineNum = 1100;BA.debugLine="tw.WriteLine(MapMarkerDragable)";
_tw.WriteLine(BA.ObjectToString(_mapmarkerdragable));
 //BA.debugLineNum = 1101;BA.debugLine="tw.WriteLine(DispMapTypeControl)";
_tw.WriteLine(BA.ObjectToString(_dispmaptypecontrol));
 //BA.debugLineNum = 1102;BA.debugLine="tw.WriteLine(DispMapTypeControlID)				' added v1";
_tw.WriteLine(_dispmaptypecontrolid);
 //BA.debugLineNum = 1103;BA.debugLine="tw.WriteLine(DispMapTypeControlIDIndex)		' added";
_tw.WriteLine(BA.NumberToString(_dispmaptypecontrolidindex));
 //BA.debugLineNum = 1104;BA.debugLine="tw.WriteLine(DispMapZoomControlType)			' added v2";
_tw.WriteLine(_dispmapzoomcontroltype);
 //BA.debugLineNum = 1105;BA.debugLine="tw.WriteLine(DispMapZoomControlTypeIndex)	' added";
_tw.WriteLine(BA.NumberToString(_dispmapzoomcontroltypeindex));
 //BA.debugLineNum = 1106;BA.debugLine="tw.WriteLine(DispMapZoomControl)";
_tw.WriteLine(BA.ObjectToString(_dispmapzoomcontrol));
 //BA.debugLineNum = 1107;BA.debugLine="tw.WriteLine(DispMapScaleControl)";
_tw.WriteLine(BA.ObjectToString(_dispmapscalecontrol));
 //BA.debugLineNum = 1108;BA.debugLine="tw.WriteLine(DispMapCenter)";
_tw.WriteLine(BA.ObjectToString(_dispmapcenter));
 //BA.debugLineNum = 1109;BA.debugLine="tw.WriteLine(DispMapMarkers)";
_tw.WriteLine(BA.ObjectToString(_dispmapmarkers));
 //BA.debugLineNum = 1110;BA.debugLine="tw.WriteLine(DispMapPolylne)";
_tw.WriteLine(BA.ObjectToString(_dispmappolylne));
 //BA.debugLineNum = 1111;BA.debugLine="tw.WriteLine(ShowGPSOnMap)";
_tw.WriteLine(BA.ObjectToString(_showgpsonmap));
 //BA.debugLineNum = 1112;BA.debugLine="tw.WriteLine(SaveGPSPath)";
_tw.WriteLine(BA.ObjectToString(_savegpspath));
 //BA.debugLineNum = 1113;BA.debugLine="tw.WriteLine(DrawGPSPath)";
_tw.WriteLine(BA.ObjectToString(_drawgpspath));
 //BA.debugLineNum = 1114;BA.debugLine="tw.WriteLine(AltitudeUnitIndex)";
_tw.WriteLine(BA.NumberToString(_altitudeunitindex));
 //BA.debugLineNum = 1115;BA.debugLine="tw.WriteLine(SpeedUnitIndex)";
_tw.WriteLine(BA.NumberToString(_speedunitindex));
 //BA.debugLineNum = 1116;BA.debugLine="tw.WriteLine(DistanceUnitIndex)";
_tw.WriteLine(BA.NumberToString(_distanceunitindex));
 //BA.debugLineNum = 1117;BA.debugLine="tw.Close";
_tw.Close();
 //BA.debugLineNum = 1118;BA.debugLine="End Sub";
return "";
}
public static String  _tooltip(String _txt) throws Exception{
 //BA.debugLineNum = 344;BA.debugLine="Sub ToolTip(txt As String)";
 //BA.debugLineNum = 345;BA.debugLine="If txt = \"\" Then";
if ((_txt).equals("")) { 
 //BA.debugLineNum = 346;BA.debugLine="lblToolTip.Text = \"\"";
mostCurrent._lbltooltip.setText((Object)(""));
 //BA.debugLineNum = 347;BA.debugLine="lblToolTip.Visible = False";
mostCurrent._lbltooltip.setVisible(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 349;BA.debugLine="lblToolTip.Text = txt";
mostCurrent._lbltooltip.setText((Object)(_txt));
 //BA.debugLineNum = 350;BA.debugLine="lblToolTip.Visible = True";
mostCurrent._lbltooltip.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 352;BA.debugLine="End Sub";
return "";
}
}
