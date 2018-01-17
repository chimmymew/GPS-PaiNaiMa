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

public class gpspaths extends Activity implements B4AActivity{
	public static gpspaths mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "B4A.GPS.PaiNaiMa", "B4A.GPS.PaiNaiMa.gpspaths");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (gpspaths).");
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
		activityBA = new BA(this, layout, processBA, "B4A.GPS.PaiNaiMa", "B4A.GPS.PaiNaiMa.gpspaths");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "B4A.GPS.PaiNaiMa.gpspaths", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (gpspaths) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (gpspaths) Resume **");
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
		return gpspaths.class;
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
        BA.LogInfo("** Activity (gpspaths) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (gpspaths) Resume **");
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
public static boolean _filter_on = false;
public static boolean _filtered_on = false;
public static double _earthradius = 0;
public static float _rowheight = 0f;
public static float _rowheight_1 = 0f;
public static int _numberofcolumns = 0;
public static int _numberofcolumns_1 = 0;
public static int _numberofrows = 0;
public static String _colname0 = "";
public static String[] _colname = null;
public static float _celllinewidth = 0f;
public static float _cellwidth0 = 0f;
public static float _cellwidth0_1 = 0f;
public static float[] _cellwidth = null;
public static float[] _cellwidth_1 = null;
public static float[] _cellwidthtotal = null;
public static int _cellcolor = 0;
public static int _selectedrowcolor = 0;
public static int _selectedrow = 0;
public static int _cellalignment = 0;
public static float _celltextsize = 0f;
public static int _celltextcolor = 0;
public static int _celllinecolor = 0;
public static float _headerheight = 0f;
public static int _headercolor = 0;
public static int _headerlinecolor = 0;
public static int _headertextcolor = 0;
public static anywheresoftware.b4a.objects.collections.List _gpspathdelete = null;
public anywheresoftware.b4a.objects.PanelWrapper _btngpspathload = null;
public anywheresoftware.b4a.objects.PanelWrapper _btngpspathsave = null;
public anywheresoftware.b4a.objects.PanelWrapper _btngpspathdeletepoint = null;
public anywheresoftware.b4a.objects.PanelWrapper _btngpspathdeletefile = null;
public anywheresoftware.b4a.objects.PanelWrapper _btngpspathfilter = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlgpspathfilterdel = null;
public anywheresoftware.b4a.objects.PanelWrapper _btngpspathfiltergo = null;
public anywheresoftware.b4a.objects.PanelWrapper _btngpspathfilterdel = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlgpspath0 = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlgpspath1 = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlgpspathtoolbox = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlgpspathheader0 = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlgpspathheader1 = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlgpspathfilter = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlgpspathfiltertoolbox = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edtgpspathfiltermindist = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblgpspathcomment = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblgpspathdatetime = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblgpspathdate = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblgpspathtime = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbltooltip = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblgpspathfile = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblgpspathnbpoints = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblgpspathnbpoints1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblgpspathfilternb0 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblgpspathfilternb1 = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _scvgpspath0 = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _scvgpspath1 = null;
public anywheresoftware.b4a.objects.SeekBarWrapper _skbgpspath = null;
public anywheresoftware.b4a.agraham.dialogs.InputDialog.FileDialog _dialog1 = null;
public anywheresoftware.b4a.objects.collections.List _gpsfilterindexes = null;
public B4A.GPS.PaiNaiMa.main _main = null;
public B4A.GPS.PaiNaiMa.satellites _satellites = null;
public B4A.GPS.PaiNaiMa.setup _setup = null;
public B4A.GPS.PaiNaiMa.gpssave _gpssave = null;
public B4A.GPS.PaiNaiMa.gpsmodule _gpsmodule = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static class _rowcol{
public boolean IsInitialized;
public int Col;
public int Row;
public void Initialize() {
IsInitialized = true;
Col = 0;
Row = 0;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 59;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 61;BA.debugLine="Activity.LoadLayout(\"gps_path\")";
mostCurrent._activity.LoadLayout("gps_path",mostCurrent.activityBA);
 //BA.debugLineNum = 62;BA.debugLine="Activity.Title = \"แสดงเส้นทาง\"";
mostCurrent._activity.setTitle((Object)("แสดงเส้นทาง"));
 //BA.debugLineNum = 64;BA.debugLine="lblToolTip.Top = 0";
mostCurrent._lbltooltip.setTop((int) (0));
 //BA.debugLineNum = 65;BA.debugLine="lblToolTip.Left = 0";
mostCurrent._lbltooltip.setLeft((int) (0));
 //BA.debugLineNum = 67;BA.debugLine="pnlGPSPathFilter.Top = 0";
mostCurrent._pnlgpspathfilter.setTop((int) (0));
 //BA.debugLineNum = 68;BA.debugLine="pnlGPSPathFilter.Left = 0";
mostCurrent._pnlgpspathfilter.setLeft((int) (0));
 //BA.debugLineNum = 70;BA.debugLine="pnlGPSPath0.Top = lblGPSPathDateTime.Top + lblGPS";
mostCurrent._pnlgpspath0.setTop((int) (mostCurrent._lblgpspathdatetime.getTop()+mostCurrent._lblgpspathdatetime.getHeight()));
 //BA.debugLineNum = 71;BA.debugLine="pnlGPSPathToolbox.Left = 0";
mostCurrent._pnlgpspathtoolbox.setLeft((int) (0));
 //BA.debugLineNum = 72;BA.debugLine="pnlGPSPathToolbox.Top = Activity.Height - pnlGPSP";
mostCurrent._pnlgpspathtoolbox.setTop((int) (mostCurrent._activity.getHeight()-mostCurrent._pnlgpspathtoolbox.getHeight()));
 //BA.debugLineNum = 74;BA.debugLine="pnlGPSPathFilterToolBox.Top = pnlGPSPathToolbox.T";
mostCurrent._pnlgpspathfiltertoolbox.setTop(mostCurrent._pnlgpspathtoolbox.getTop());
 //BA.debugLineNum = 75;BA.debugLine="pnlGPSPathFilterToolBox.Left = pnlGPSPathToolbox.";
mostCurrent._pnlgpspathfiltertoolbox.setLeft(mostCurrent._pnlgpspathtoolbox.getLeft());
 //BA.debugLineNum = 77;BA.debugLine="pnlGPSPath0.Height = pnlGPSPathToolbox.Top - lblG";
mostCurrent._pnlgpspath0.setHeight((int) (mostCurrent._pnlgpspathtoolbox.getTop()-mostCurrent._lblgpspathdate.getTop()-mostCurrent._lblgpspathdate.getHeight()-mostCurrent._skbgpspath.getHeight()));
 //BA.debugLineNum = 78;BA.debugLine="pnlGPSPath0.Color = Colors.RGB(165,42,42)";
mostCurrent._pnlgpspath0.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (165),(int) (42),(int) (42)));
 //BA.debugLineNum = 79;BA.debugLine="pnlGPSPath1.Top = pnlGPSPath0.Top";
mostCurrent._pnlgpspath1.setTop(mostCurrent._pnlgpspath0.getTop());
 //BA.debugLineNum = 80;BA.debugLine="pnlGPSPath1.Height = pnlGPSPath0.Height";
mostCurrent._pnlgpspath1.setHeight(mostCurrent._pnlgpspath0.getHeight());
 //BA.debugLineNum = 81;BA.debugLine="pnlGPSPath1.Color = Colors.RGB(165,42,42)";
mostCurrent._pnlgpspath1.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (165),(int) (42),(int) (42)));
 //BA.debugLineNum = 82;BA.debugLine="skbGPSPath.Top = pnlGPSPathToolbox.Top - skbGPSPa";
mostCurrent._skbgpspath.setTop((int) (mostCurrent._pnlgpspathtoolbox.getTop()-mostCurrent._skbgpspath.getHeight()));
 //BA.debugLineNum = 84;BA.debugLine="If Main.GPSPathFilename = \"\" Then";
if ((mostCurrent._main._gpspathfilename).equals("")) { 
 //BA.debugLineNum = 85;BA.debugLine="btnGPSPathSave.Visible = False";
mostCurrent._btngpspathsave.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 86;BA.debugLine="btnGPSPathDeletePoint.Visible = False";
mostCurrent._btngpspathdeletepoint.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 87;BA.debugLine="btnGPSPathDeleteFile.Visible = False";
mostCurrent._btngpspathdeletefile.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 88;BA.debugLine="lblGPSPathFile.Text = \"  ---\"";
mostCurrent._lblgpspathfile.setText((Object)("  ---"));
 //BA.debugLineNum = 89;BA.debugLine="lblGPSPathNbPoints1.Text = \"---\"";
mostCurrent._lblgpspathnbpoints1.setText((Object)("---"));
 //BA.debugLineNum = 90;BA.debugLine="lblGPSPathComment.Text = \"  ---\"";
mostCurrent._lblgpspathcomment.setText((Object)("  ---"));
 //BA.debugLineNum = 91;BA.debugLine="lblGPSPathDate.Text = \"  ---\"";
mostCurrent._lblgpspathdate.setText((Object)("  ---"));
 //BA.debugLineNum = 92;BA.debugLine="lblGPSPathTime.Text = \"  ---\"";
mostCurrent._lblgpspathtime.setText((Object)("  ---"));
 }else {
 //BA.debugLineNum = 94;BA.debugLine="btnGPSPathSave.Visible = True";
mostCurrent._btngpspathsave.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 95;BA.debugLine="btnGPSPathDeleteFile.Visible = True";
mostCurrent._btngpspathdeletefile.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 96;BA.debugLine="If SelectedRow >= 0 Then";
if (_selectedrow>=0) { 
 //BA.debugLineNum = 97;BA.debugLine="btnGPSPathDeletePoint.Visible = True";
mostCurrent._btngpspathdeletepoint.setVisible(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 99;BA.debugLine="btnGPSPathDeletePoint.Visible = False";
mostCurrent._btngpspathdeletepoint.setVisible(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 101;BA.debugLine="lblGPSPathFile.Text = \"  \" & Main.GPSPathFilenam";
mostCurrent._lblgpspathfile.setText((Object)("  "+mostCurrent._main._gpspathfilename));
 //BA.debugLineNum = 102;BA.debugLine="lblGPSPathComment.Text = \"  \" & Main.GPSPathComm";
mostCurrent._lblgpspathcomment.setText((Object)("  "+mostCurrent._main._gpspathcomment));
 //BA.debugLineNum = 103;BA.debugLine="lblGPSPathNbPoints1.Text = Main.GPSPath.Size & \"";
mostCurrent._lblgpspathnbpoints1.setText((Object)(BA.NumberToString(mostCurrent._main._gpspath.getSize())+" จุด"));
 //BA.debugLineNum = 104;BA.debugLine="If Main.GPSPathTime0 > 0 Then";
if (mostCurrent._main._gpspathtime0>0) { 
 //BA.debugLineNum = 105;BA.debugLine="lblGPSPathDate.Text = \"  \" & DateTime.Date(Main";
mostCurrent._lblgpspathdate.setText((Object)("  "+anywheresoftware.b4a.keywords.Common.DateTime.Date(mostCurrent._main._gpspathtime0)));
 //BA.debugLineNum = 106;BA.debugLine="lblGPSPathTime.Text = \"  \" & DateTime.Time(Main";
mostCurrent._lblgpspathtime.setText((Object)("  "+anywheresoftware.b4a.keywords.Common.DateTime.Time(mostCurrent._main._gpspathtime0)));
 }else {
 //BA.debugLineNum = 108;BA.debugLine="lblGPSPathDate.Text = \"  ---\"";
mostCurrent._lblgpspathdate.setText((Object)("  ---"));
 //BA.debugLineNum = 109;BA.debugLine="lblGPSPathTime.Text = \"  ---\"";
mostCurrent._lblgpspathtime.setText((Object)("  ---"));
 };
 };
 //BA.debugLineNum = 113;BA.debugLine="lblToolTip.BringToFront";
mostCurrent._lbltooltip.BringToFront();
 //BA.debugLineNum = 114;BA.debugLine="InitGPSPathDisplay";
_initgpspathdisplay();
 //BA.debugLineNum = 115;BA.debugLine="FillGPSPathTable";
_fillgpspathtable();
 //BA.debugLineNum = 117;BA.debugLine="If Not(GPSFilterIndexes.IsInitialized) Then";
if (anywheresoftware.b4a.keywords.Common.Not(mostCurrent._gpsfilterindexes.IsInitialized())) { 
 //BA.debugLineNum = 118;BA.debugLine="GPSFilterIndexes.Initialize";
mostCurrent._gpsfilterindexes.Initialize();
 };
 //BA.debugLineNum = 120;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 130;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 131;BA.debugLine="If Filter_On = True Then";
if (_filter_on==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 132;BA.debugLine="pnlGPSPathFilter.Visible = False";
mostCurrent._pnlgpspathfilter.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 133;BA.debugLine="pnlGPSPathFilterToolBox.Visible = False";
mostCurrent._pnlgpspathfiltertoolbox.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 134;BA.debugLine="Main.GPSFilterDelta = edtGPSPathFilterMinDist.Te";
mostCurrent._main._gpsfilterdelta = (double)(Double.parseDouble(mostCurrent._edtgpspathfiltermindist.getText()));
 //BA.debugLineNum = 135;BA.debugLine="If Filtered_On Then";
if (_filtered_on) { 
 //BA.debugLineNum = 136;BA.debugLine="GPSPathUnFilter";
_gpspathunfilter();
 };
 //BA.debugLineNum = 138;BA.debugLine="Filter_On = False";
_filter_on = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 139;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 141;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 126;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 128;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 122;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 124;BA.debugLine="End Sub";
return "";
}
public static String  _btngpspathdeletefile_touch(int _action,float _x,float _y) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bmd = null;
int _answ = 0;
String _txt = "";
 //BA.debugLineNum = 246;BA.debugLine="Sub btnGPSPathDeleteFile_Touch(Action As Int, x As";
 //BA.debugLineNum = 247;BA.debugLine="Dim bmd As BitmapDrawable";
_bmd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 248;BA.debugLine="Dim Answ As Int";
_answ = 0;
 //BA.debugLineNum = 249;BA.debugLine="Dim txt As String";
_txt = "";
 //BA.debugLineNum = 251;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,mostCurrent._activity.ACTION_DOWN,mostCurrent._activity.ACTION_UP)) {
case 0: {
 //BA.debugLineNum = 253;BA.debugLine="ToolTip(\"ลบแฟ้มปัจจุบัน\")";
_tooltip("ลบแฟ้มปัจจุบัน");
 //BA.debugLineNum = 254;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btnde";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btndeletefile1.png").getObject()));
 //BA.debugLineNum = 255;BA.debugLine="btnGPSPathDeleteFile.Background = bmd";
mostCurrent._btngpspathdeletefile.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 break; }
case 1: {
 //BA.debugLineNum = 257;BA.debugLine="If x > 0 And x < btnGPSPathDeleteFile.Width  And";
if (_x>0 && _x<mostCurrent._btngpspathdeletefile.getWidth() && _y>0 && _y<mostCurrent._btngpspathdeletefile.getHeight()) { 
 //BA.debugLineNum = 258;BA.debugLine="txt = \"ต้องการลบ\" & CRLF";
_txt = "ต้องการลบ"+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 259;BA.debugLine="txt = txt & \"แฟ้ม \" & Main.GPSPathFilename & \"";
_txt = _txt+"แฟ้ม "+mostCurrent._main._gpspathfilename+" ?";
 //BA.debugLineNum = 260;BA.debugLine="Answ = Msgbox2(txt, \"โปรดเลือก\", \"ตกลง\", \"\", \"ย";
_answ = anywheresoftware.b4a.keywords.Common.Msgbox2(_txt,"โปรดเลือก","ตกลง","","ยกเลิก",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 261;BA.debugLine="If Answ = DialogResponse.POSITIVE Then";
if (_answ==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 262;BA.debugLine="File.Delete(Main.GPSDir, Main.GPSPathFilename)";
anywheresoftware.b4a.keywords.Common.File.Delete(mostCurrent._main._gpsdir,mostCurrent._main._gpspathfilename);
 //BA.debugLineNum = 263;BA.debugLine="Main.GPSPathFilename = \"\"";
mostCurrent._main._gpspathfilename = "";
 //BA.debugLineNum = 264;BA.debugLine="lblGPSPathFile.Text = \"  \" & Main.GPSPathFilen";
mostCurrent._lblgpspathfile.setText((Object)("  "+mostCurrent._main._gpspathfilename));
 //BA.debugLineNum = 265;BA.debugLine="ClearTable";
_cleartable();
 //BA.debugLineNum = 266;BA.debugLine="Main.MapZoomCalculated = False";
mostCurrent._main._mapzoomcalculated = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 267;BA.debugLine="btnGPSPathDeleteFile.Visible = False";
mostCurrent._btngpspathdeletefile.setVisible(anywheresoftware.b4a.keywords.Common.False);
 };
 };
 //BA.debugLineNum = 270;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btnde";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btndeletefile0.png").getObject()));
 //BA.debugLineNum = 271;BA.debugLine="btnGPSPathDeleteFile.Background = bmd";
mostCurrent._btngpspathdeletefile.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 //BA.debugLineNum = 272;BA.debugLine="ToolTip(\"\")";
_tooltip("");
 break; }
}
;
 //BA.debugLineNum = 274;BA.debugLine="End Sub";
return "";
}
public static String  _btngpspathdeletepoint_touch(int _action,float _x,float _y) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bmd = null;
int _answ = 0;
String _txt = "";
 //BA.debugLineNum = 221;BA.debugLine="Sub btnGPSPathDeletePoint_Touch(Action As Int, x A";
 //BA.debugLineNum = 222;BA.debugLine="Dim bmd As BitmapDrawable";
_bmd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 223;BA.debugLine="Dim Answ As Int";
_answ = 0;
 //BA.debugLineNum = 224;BA.debugLine="Dim txt As String";
_txt = "";
 //BA.debugLineNum = 226;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,mostCurrent._activity.ACTION_DOWN,mostCurrent._activity.ACTION_UP)) {
case 0: {
 //BA.debugLineNum = 228;BA.debugLine="ToolTip(\"ลบจุดที่เลือก\")";
_tooltip("ลบจุดที่เลือก");
 //BA.debugLineNum = 229;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btnde";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btndeletepoint1.png").getObject()));
 //BA.debugLineNum = 230;BA.debugLine="btnGPSPathDeletePoint.Background = bmd";
mostCurrent._btngpspathdeletepoint.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 break; }
case 1: {
 //BA.debugLineNum = 232;BA.debugLine="If x > 0 And x < btnGPSPathDeletePoint.Width  An";
if (_x>0 && _x<mostCurrent._btngpspathdeletepoint.getWidth() && _y>0 && _y<mostCurrent._btngpspathdeletepoint.getHeight()) { 
 //BA.debugLineNum = 233;BA.debugLine="txt = \"ต้องการลบ\" & CRLF";
_txt = "ต้องการลบ"+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 234;BA.debugLine="txt = txt & \"จุด \" & SelectedRow & \" ?\"";
_txt = _txt+"จุด "+BA.NumberToString(_selectedrow)+" ?";
 //BA.debugLineNum = 235;BA.debugLine="Answ = Msgbox2(txt, \"โปรดเลือก\", \"ตกลง\", \"\", \"ย";
_answ = anywheresoftware.b4a.keywords.Common.Msgbox2(_txt,"โปรดเลือก","ตกลง","","ยกเลิก",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 236;BA.debugLine="If Answ = DialogResponse.POSITIVE Then";
if (_answ==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 237;BA.debugLine="GPSPathDeletePoint";
_gpspathdeletepoint();
 };
 };
 //BA.debugLineNum = 240;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btnde";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btndeletepoint0.png").getObject()));
 //BA.debugLineNum = 241;BA.debugLine="btnGPSPathDeletePoint.Background = bmd";
mostCurrent._btngpspathdeletepoint.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 //BA.debugLineNum = 242;BA.debugLine="ToolTip(\"\")";
_tooltip("");
 break; }
}
;
 //BA.debugLineNum = 244;BA.debugLine="End Sub";
return "";
}
public static String  _btngpspathfilter_touch(int _action,float _x,float _y) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bmd = null;
int _answ = 0;
 //BA.debugLineNum = 399;BA.debugLine="Sub btnGPSPathFilter_Touch(Action As Int, x As Flo";
 //BA.debugLineNum = 400;BA.debugLine="Dim bmd As BitmapDrawable";
_bmd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 401;BA.debugLine="Dim Answ As Int";
_answ = 0;
 //BA.debugLineNum = 403;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,mostCurrent._activity.ACTION_DOWN,mostCurrent._activity.ACTION_UP)) {
case 0: {
 //BA.debugLineNum = 405;BA.debugLine="ToolTip(\"กรองจุดเส้นทาง\")";
_tooltip("กรองจุดเส้นทาง");
 //BA.debugLineNum = 406;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btngp";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpsfilter1.png").getObject()));
 //BA.debugLineNum = 407;BA.debugLine="btnGPSPathFilter.Background = bmd";
mostCurrent._btngpspathfilter.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 break; }
case 1: {
 //BA.debugLineNum = 409;BA.debugLine="If x > 0 And x < btnGPSPathFilter.Width  And y >";
if (_x>0 && _x<mostCurrent._btngpspathfilter.getWidth() && _y>0 && _y<mostCurrent._btngpspathfilter.getHeight()) { 
 //BA.debugLineNum = 410;BA.debugLine="Filter_On = Not(Filter_On)";
_filter_on = anywheresoftware.b4a.keywords.Common.Not(_filter_on);
 };
 //BA.debugLineNum = 412;BA.debugLine="If Filter_On = True Then";
if (_filter_on==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 413;BA.debugLine="pnlGPSPathFilter.Visible = True";
mostCurrent._pnlgpspathfilter.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 414;BA.debugLine="pnlGPSPathFilterToolBox.Visible = True";
mostCurrent._pnlgpspathfiltertoolbox.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 415;BA.debugLine="edtGPSPathFilterMinDist.Text = Main.GPSFilterDe";
mostCurrent._edtgpspathfiltermindist.setText((Object)(mostCurrent._main._gpsfilterdelta));
 //BA.debugLineNum = 416;BA.debugLine="If Filtered_On Then";
if (_filtered_on) { 
 //BA.debugLineNum = 417;BA.debugLine="GPSPathFilter";
_gpspathfilter();
 }else {
 //BA.debugLineNum = 419;BA.debugLine="lblGPSPathFilterNb0.Text = \"จำนวนจุด\"";
mostCurrent._lblgpspathfilternb0.setText((Object)("จำนวนจุด"));
 //BA.debugLineNum = 420;BA.debugLine="lblGPSPathFilterNb1.Text = Main.GPSPath.Size";
mostCurrent._lblgpspathfilternb1.setText((Object)(mostCurrent._main._gpspath.getSize()));
 };
 };
 //BA.debugLineNum = 423;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btngp";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpsfilter0.png").getObject()));
 //BA.debugLineNum = 424;BA.debugLine="btnGPSPathFilter.Background = bmd";
mostCurrent._btngpspathfilter.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 //BA.debugLineNum = 425;BA.debugLine="ToolTip(\"\")";
_tooltip("");
 break; }
}
;
 //BA.debugLineNum = 427;BA.debugLine="End Sub";
return "";
}
public static String  _btngpspathfilterdel_touch(int _action,float _x,float _y) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bmd = null;
int _answ = 0;
 //BA.debugLineNum = 460;BA.debugLine="Sub btnGPSPathFilterDel_Touch(Action As Int, x As";
 //BA.debugLineNum = 461;BA.debugLine="Dim bmd As BitmapDrawable";
_bmd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 462;BA.debugLine="Dim Answ As Int";
_answ = 0;
 //BA.debugLineNum = 464;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,mostCurrent._activity.ACTION_DOWN,mostCurrent._activity.ACTION_UP)) {
case 0: {
 //BA.debugLineNum = 466;BA.debugLine="ToolTip(\"ลบจุดที่เลือกไว้\")";
_tooltip("ลบจุดที่เลือกไว้");
 //BA.debugLineNum = 467;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btngp";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpsfilterdel1.png").getObject()));
 //BA.debugLineNum = 468;BA.debugLine="btnGPSPathFilterDel.Background = bmd";
mostCurrent._btngpspathfilterdel.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 break; }
case 1: {
 //BA.debugLineNum = 470;BA.debugLine="If x > 0 And x < btnGPSPathFilterDel.Width  And";
if (_x>0 && _x<mostCurrent._btngpspathfilterdel.getWidth() && _y>0 && _y<mostCurrent._btngpspathfilterdel.getHeight()) { 
 //BA.debugLineNum = 471;BA.debugLine="Answ = Msgbox2(\"ต้องการลบจุดที่เลือกไว้ ?\", \"โป";
_answ = anywheresoftware.b4a.keywords.Common.Msgbox2("ต้องการลบจุดที่เลือกไว้ ?","โปรดเลือก","ตกลง","","ยกเลิก",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 472;BA.debugLine="If Answ = DialogResponse.POSITIVE Then";
if (_answ==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 473;BA.debugLine="GPSPathFilterDelete";
_gpspathfilterdelete();
 //BA.debugLineNum = 474;BA.debugLine="btnGPSPathFilterDel.Visible = False";
mostCurrent._btngpspathfilterdel.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 475;BA.debugLine="btnGPSPathDeletePoint.Visible = False";
mostCurrent._btngpspathdeletepoint.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 476;BA.debugLine="pnlGPSPathFilter.Visible = False";
mostCurrent._pnlgpspathfilter.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 477;BA.debugLine="pnlGPSPathFilterToolBox.Visible = False";
mostCurrent._pnlgpspathfiltertoolbox.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 478;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btn";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpsfilter0.png").getObject()));
 //BA.debugLineNum = 479;BA.debugLine="btnGPSPathFilter.Background = bmd";
mostCurrent._btngpspathfilter.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 //BA.debugLineNum = 480;BA.debugLine="Filtered_On = False";
_filtered_on = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 481;BA.debugLine="SelectedRow = -1";
_selectedrow = (int) (-1);
 };
 };
 //BA.debugLineNum = 484;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btngp";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpsfilterdel0.png").getObject()));
 //BA.debugLineNum = 485;BA.debugLine="ToolTip(\"\")";
_tooltip("");
 //BA.debugLineNum = 486;BA.debugLine="btnGPSPathFilterDel.Background = bmd";
mostCurrent._btngpspathfilterdel.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 break; }
}
;
 //BA.debugLineNum = 488;BA.debugLine="End Sub";
return "";
}
public static String  _btngpspathfiltergo_touch(int _action,float _x,float _y) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bmd = null;
 //BA.debugLineNum = 429;BA.debugLine="Sub btnGPSPathFilterGo_Touch(Action As Int, x As F";
 //BA.debugLineNum = 430;BA.debugLine="Dim bmd As BitmapDrawable";
_bmd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 432;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,mostCurrent._activity.ACTION_DOWN,mostCurrent._activity.ACTION_UP)) {
case 0: {
 //BA.debugLineNum = 434;BA.debugLine="If Filtered_On = False Then";
if (_filtered_on==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 435;BA.debugLine="ToolTip(\"กรองเส้นทางปัจจุบัน\")";
_tooltip("กรองเส้นทางปัจจุบัน");
 //BA.debugLineNum = 436;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btng";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpsfilter1.png").getObject()));
 }else {
 //BA.debugLineNum = 438;BA.debugLine="ToolTip(\"ยกเลิกตัวกรอง\")";
_tooltip("ยกเลิกตัวกรอง");
 //BA.debugLineNum = 439;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btng";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpsfilter0.png").getObject()));
 };
 break; }
case 1: {
 //BA.debugLineNum = 442;BA.debugLine="If x > 0 And x < btnGPSPathFilterGo.Width  And y";
if (_x>0 && _x<mostCurrent._btngpspathfiltergo.getWidth() && _y>0 && _y<mostCurrent._btngpspathfiltergo.getHeight()) { 
 //BA.debugLineNum = 443;BA.debugLine="Filtered_On = Not(Filtered_On)";
_filtered_on = anywheresoftware.b4a.keywords.Common.Not(_filtered_on);
 };
 //BA.debugLineNum = 445;BA.debugLine="If Filtered_On = False Then";
if (_filtered_on==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 446;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btng";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpsfilter0.png").getObject()));
 //BA.debugLineNum = 447;BA.debugLine="GPSPathUnFilter";
_gpspathunfilter();
 }else {
 //BA.debugLineNum = 449;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btng";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btngpsfilter1.png").getObject()));
 //BA.debugLineNum = 450;BA.debugLine="Main.GPSFilterDelta = edtGPSPathFilterMinDist.T";
mostCurrent._main._gpsfilterdelta = (double)(Double.parseDouble(mostCurrent._edtgpspathfiltermindist.getText()));
 //BA.debugLineNum = 451;BA.debugLine="Main.GPSFilterDelta = Main.GPSFilterDelta / 100";
mostCurrent._main._gpsfilterdelta = mostCurrent._main._gpsfilterdelta/(double)1000;
 //BA.debugLineNum = 452;BA.debugLine="GPSPathFilter";
_gpspathfilter();
 };
 //BA.debugLineNum = 454;BA.debugLine="btnGPSPathFilterGo.Background = bmd";
mostCurrent._btngpspathfiltergo.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 //BA.debugLineNum = 455;BA.debugLine="btnGPSPathFilterDel.Visible = Filtered_On";
mostCurrent._btngpspathfilterdel.setVisible(_filtered_on);
 //BA.debugLineNum = 456;BA.debugLine="ToolTip(\"\")";
_tooltip("");
 break; }
}
;
 //BA.debugLineNum = 458;BA.debugLine="End Sub";
return "";
}
public static String  _btngpspathload_touch(int _action,float _x,float _y) throws Exception{
int _answ = 0;
int _i1 = 0;
int _i2 = 0;
String _txt = "";
B4A.GPS.PaiNaiMa.main._gpslocation _pos = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bmd = null;
 //BA.debugLineNum = 176;BA.debugLine="Sub btnGPSPathLoad_Touch(Action As Int, x As Float";
 //BA.debugLineNum = 177;BA.debugLine="Dim Answ, i1, i2 As Int";
_answ = 0;
_i1 = 0;
_i2 = 0;
 //BA.debugLineNum = 178;BA.debugLine="Dim txt As String";
_txt = "";
 //BA.debugLineNum = 179;BA.debugLine="Dim Pos As GPSLocation";
_pos = new B4A.GPS.PaiNaiMa.main._gpslocation();
 //BA.debugLineNum = 180;BA.debugLine="Dim bmd As BitmapDrawable";
_bmd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 182;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,mostCurrent._activity.ACTION_DOWN,mostCurrent._activity.ACTION_UP)) {
case 0: {
 //BA.debugLineNum = 184;BA.debugLine="ToolTip(\"เปิดเส้นทาง\")";
_tooltip("เปิดเส้นทาง");
 //BA.debugLineNum = 185;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btnlo";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnload1.png").getObject()));
 //BA.debugLineNum = 186;BA.debugLine="btnGPSPathLoad.Background = bmd";
mostCurrent._btngpspathload.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 break; }
case 1: {
 //BA.debugLineNum = 188;BA.debugLine="If x > 0 And x < btnGPSPathLoad.Width  And y > 0";
if (_x>0 && _x<mostCurrent._btngpspathload.getWidth() && _y>0 && _y<mostCurrent._btngpspathload.getHeight()) { 
 //BA.debugLineNum = 189;BA.debugLine="Dialog1.FilePath = Main.GPSDir";
mostCurrent._dialog1.setFilePath(mostCurrent._main._gpsdir);
 //BA.debugLineNum = 190;BA.debugLine="Dialog1.FileFilter = \".GPP\"";
mostCurrent._dialog1.setFileFilter(".GPP");
 //BA.debugLineNum = 191;BA.debugLine="Answ = Dialog1.Show(\"แฟ้ม\", \"เปิด\", \"ยกเลิก\", \"";
_answ = mostCurrent._dialog1.Show("แฟ้ม","เปิด","ยกเลิก","",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 192;BA.debugLine="If Answ = DialogResponse.POSITIVE Then";
if (_answ==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 193;BA.debugLine="txt = Dialog1.ChosenName";
_txt = mostCurrent._dialog1.getChosenName();
 //BA.debugLineNum = 194;BA.debugLine="If txt.EndsWith(\".GPP\") = True Then";
if (_txt.endsWith(".GPP")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 195;BA.debugLine="Main.GPSPathFilename = txt";
mostCurrent._main._gpspathfilename = _txt;
 //BA.debugLineNum = 196;BA.debugLine="GPSModule.LoadPath";
mostCurrent._gpsmodule._loadpath(mostCurrent.activityBA);
 //BA.debugLineNum = 197;BA.debugLine="Main.MapZoomCalculated = False";
mostCurrent._main._mapzoomcalculated = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 198;BA.debugLine="ClearTable";
_cleartable();
 //BA.debugLineNum = 199;BA.debugLine="FillGPSPathTable";
_fillgpspathtable();
 //BA.debugLineNum = 200;BA.debugLine="lblGPSPathFile.Text = \"  \" & Main.GPSPathFile";
mostCurrent._lblgpspathfile.setText((Object)("  "+mostCurrent._main._gpspathfilename));
 //BA.debugLineNum = 201;BA.debugLine="lblGPSPathComment.Text = \"  \" & Main.GPSPathC";
mostCurrent._lblgpspathcomment.setText((Object)("  "+mostCurrent._main._gpspathcomment));
 //BA.debugLineNum = 202;BA.debugLine="lblGPSPathNbPoints1.Text = Main.GPSPath.Size";
mostCurrent._lblgpspathnbpoints1.setText((Object)(BA.NumberToString(mostCurrent._main._gpspath.getSize())+" จุด"));
 //BA.debugLineNum = 203;BA.debugLine="If Main.GPSPathTime0 > 0 Then";
if (mostCurrent._main._gpspathtime0>0) { 
 //BA.debugLineNum = 204;BA.debugLine="lblGPSPathDate.Text = \"  \" & DateTime.Date(M";
mostCurrent._lblgpspathdate.setText((Object)("  "+anywheresoftware.b4a.keywords.Common.DateTime.Date(mostCurrent._main._gpspathtime0)));
 //BA.debugLineNum = 205;BA.debugLine="lblGPSPathTime.Text = \"  \" & DateTime.Time(M";
mostCurrent._lblgpspathtime.setText((Object)("  "+anywheresoftware.b4a.keywords.Common.DateTime.Time(mostCurrent._main._gpspathtime0)));
 }else {
 //BA.debugLineNum = 207;BA.debugLine="lblGPSPathDate.Text = \"  ---\"";
mostCurrent._lblgpspathdate.setText((Object)("  ---"));
 //BA.debugLineNum = 208;BA.debugLine="lblGPSPathTime.Text = \"  ---\"";
mostCurrent._lblgpspathtime.setText((Object)("  ---"));
 };
 }else {
 //BA.debugLineNum = 211;BA.debugLine="Return";
if (true) return "";
 };
 };
 };
 //BA.debugLineNum = 215;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btnlo";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnload0.png").getObject()));
 //BA.debugLineNum = 216;BA.debugLine="btnGPSPathLoad.Background = bmd";
mostCurrent._btngpspathload.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 //BA.debugLineNum = 217;BA.debugLine="ToolTip(\"\")";
_tooltip("");
 break; }
}
;
 //BA.debugLineNum = 219;BA.debugLine="End Sub";
return "";
}
public static String  _btngpspathsave_touch(int _action,float _x,float _y) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bmd = null;
anywheresoftware.b4a.objects.PanelWrapper _send = null;
int _i = 0;
int _answ = 0;
String _txt = "";
 //BA.debugLineNum = 153;BA.debugLine="Sub btnGPSPathSave_Touch(Action As Int, x As Float";
 //BA.debugLineNum = 154;BA.debugLine="Dim bmd As BitmapDrawable";
_bmd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 155;BA.debugLine="Dim Send As Panel";
_send = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 156;BA.debugLine="Dim i, Answ As Int";
_i = 0;
_answ = 0;
 //BA.debugLineNum = 157;BA.debugLine="Dim txt As String";
_txt = "";
 //BA.debugLineNum = 159;BA.debugLine="Send = Sender";
_send.setObject((android.view.ViewGroup)(anywheresoftware.b4a.keywords.Common.Sender(mostCurrent.activityBA)));
 //BA.debugLineNum = 161;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,mostCurrent._activity.ACTION_DOWN,mostCurrent._activity.ACTION_UP)) {
case 0: {
 //BA.debugLineNum = 163;BA.debugLine="ToolTip(\"บันทึก\")";
_tooltip("บันทึก");
 //BA.debugLineNum = 164;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btnsa";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnsave1.png").getObject()));
 //BA.debugLineNum = 165;BA.debugLine="Send.Background = bmd";
_send.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 break; }
case 1: {
 //BA.debugLineNum = 167;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btnsa";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnsave0.png").getObject()));
 //BA.debugLineNum = 168;BA.debugLine="Send.Background = bmd";
_send.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 //BA.debugLineNum = 169;BA.debugLine="ToolTip(\"\")";
_tooltip("");
 //BA.debugLineNum = 170;BA.debugLine="If x > 0 And x < Send.Width  And y > 0 And y < S";
if (_x>0 && _x<_send.getWidth() && _y>0 && _y<_send.getHeight()) { 
 //BA.debugLineNum = 171;BA.debugLine="StartActivity(\"GPSSave\")";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)("GPSSave"));
 };
 break; }
}
;
 //BA.debugLineNum = 174;BA.debugLine="End Sub";
return "";
}
public static String  _cell_click() throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _send = null;
B4A.GPS.PaiNaiMa.gpspaths._rowcol _rc = null;
B4A.GPS.PaiNaiMa.main._gpslocation _gpsloc = null;
 //BA.debugLineNum = 772;BA.debugLine="Sub Cell_Click";
 //BA.debugLineNum = 773;BA.debugLine="Dim Send As Label";
_send = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 774;BA.debugLine="Dim rc As RowCol";
_rc = new B4A.GPS.PaiNaiMa.gpspaths._rowcol();
 //BA.debugLineNum = 775;BA.debugLine="Dim GPSLoc As GPSLocation";
_gpsloc = new B4A.GPS.PaiNaiMa.main._gpslocation();
 //BA.debugLineNum = 777;BA.debugLine="Send = Sender";
_send.setObject((android.widget.TextView)(anywheresoftware.b4a.keywords.Common.Sender(mostCurrent.activityBA)));
 //BA.debugLineNum = 778;BA.debugLine="rc = Send.Tag";
_rc = (B4A.GPS.PaiNaiMa.gpspaths._rowcol)(_send.getTag());
 //BA.debugLineNum = 779;BA.debugLine="If rc.Col < 8 Then";
if (_rc.Col<8) { 
 //BA.debugLineNum = 780;BA.debugLine="ChangeCellSelection(rc.Row, 0)";
_changecellselection(_rc.Row,(int) (0));
 }else {
 //BA.debugLineNum = 782;BA.debugLine="GPSLoc = Main.GPSPath.Get(rc.Row)";
_gpsloc = (B4A.GPS.PaiNaiMa.main._gpslocation)(mostCurrent._main._gpspath.Get(_rc.Row));
 //BA.debugLineNum = 783;BA.debugLine="GPSLoc.Marker = Not(GPSLoc.Marker)";
_gpsloc.Marker = anywheresoftware.b4a.keywords.Common.Not(_gpsloc.Marker);
 //BA.debugLineNum = 784;BA.debugLine="Main.GPSPath.Set(rc.Row, GPSLoc)";
mostCurrent._main._gpspath.Set(_rc.Row,(Object)(_gpsloc));
 //BA.debugLineNum = 785;BA.debugLine="If GPSLoc.Marker = True Then";
if (_gpsloc.Marker==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 786;BA.debugLine="Send.Text = \"√\"";
_send.setText((Object)("√"));
 }else {
 //BA.debugLineNum = 788;BA.debugLine="Send.Text = \"-\"";
_send.setText((Object)("-"));
 };
 };
 //BA.debugLineNum = 791;BA.debugLine="End Sub";
return "";
}
public static String  _changecellselection(int _row,int _mode) throws Exception{
int _i = 0;
int _j = 0;
 //BA.debugLineNum = 793;BA.debugLine="Sub ChangeCellSelection(row As Int, mode As Int)";
 //BA.debugLineNum = 795;BA.debugLine="Dim i, j As Int";
_i = 0;
_j = 0;
 //BA.debugLineNum = 798;BA.debugLine="If SelectedRow >= 0 And mode <> 1 Then";
if (_selectedrow>=0 && _mode!=1) { 
 //BA.debugLineNum = 799;BA.debugLine="scvGPSPath0.Panel.GetView(SelectedRow).Color = C";
mostCurrent._scvgpspath0.getPanel().GetView(_selectedrow).setColor(_cellcolor);
 //BA.debugLineNum = 800;BA.debugLine="j = SelectedRow * NumberOfColumns";
_j = (int) (_selectedrow*_numberofcolumns);
 //BA.debugLineNum = 801;BA.debugLine="For i = 0 To NumberOfColumns - 1";
{
final int step5 = 1;
final int limit5 = (int) (_numberofcolumns-1);
for (_i = (int) (0) ; (step5 > 0 && _i <= limit5) || (step5 < 0 && _i >= limit5); _i = ((int)(0 + _i + step5)) ) {
 //BA.debugLineNum = 802;BA.debugLine="scvGPSPath1.Panel.GetView(j + i).Color = CellCo";
mostCurrent._scvgpspath1.getPanel().GetView((int) (_j+_i)).setColor(_cellcolor);
 }
};
 };
 //BA.debugLineNum = 806;BA.debugLine="If SelectedRow = row Then";
if (_selectedrow==_row) { 
 //BA.debugLineNum = 808;BA.debugLine="SelectedRow = -1";
_selectedrow = (int) (-1);
 //BA.debugLineNum = 809;BA.debugLine="btnGPSPathDeletePoint.Visible = False";
mostCurrent._btngpspathdeletepoint.setVisible(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 812;BA.debugLine="SelectedRow = row";
_selectedrow = _row;
 //BA.debugLineNum = 813;BA.debugLine="If mode < 2 Then";
if (_mode<2) { 
 //BA.debugLineNum = 814;BA.debugLine="scvGPSPath0.Panel.GetView(SelectedRow).Color =";
mostCurrent._scvgpspath0.getPanel().GetView(_selectedrow).setColor(_selectedrowcolor);
 //BA.debugLineNum = 815;BA.debugLine="j = SelectedRow * NumberOfColumns";
_j = (int) (_selectedrow*_numberofcolumns);
 //BA.debugLineNum = 816;BA.debugLine="For i = 0 To NumberOfColumns - 1";
{
final int step17 = 1;
final int limit17 = (int) (_numberofcolumns-1);
for (_i = (int) (0) ; (step17 > 0 && _i <= limit17) || (step17 < 0 && _i >= limit17); _i = ((int)(0 + _i + step17)) ) {
 //BA.debugLineNum = 817;BA.debugLine="scvGPSPath1.Panel.GetView(j + i).Color = Selec";
mostCurrent._scvgpspath1.getPanel().GetView((int) (_j+_i)).setColor(_selectedrowcolor);
 }
};
 //BA.debugLineNum = 819;BA.debugLine="btnGPSPathDeletePoint.Visible = True";
mostCurrent._btngpspathdeletepoint.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 };
 //BA.debugLineNum = 822;BA.debugLine="End Sub";
return "";
}
public static String  _cleartable() throws Exception{
int _i = 0;
 //BA.debugLineNum = 673;BA.debugLine="Sub ClearTable";
 //BA.debugLineNum = 675;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 677;BA.debugLine="For i = scvGPSPath0.Panel.NumberOfViews - 1 To 0";
{
final int step2 = (int) (-1);
final int limit2 = (int) (0);
for (_i = (int) (mostCurrent._scvgpspath0.getPanel().getNumberOfViews()-1) ; (step2 > 0 && _i <= limit2) || (step2 < 0 && _i >= limit2); _i = ((int)(0 + _i + step2)) ) {
 //BA.debugLineNum = 678;BA.debugLine="scvGPSPath0.Panel.RemoveViewAt(i)";
mostCurrent._scvgpspath0.getPanel().RemoveViewAt(_i);
 }
};
 //BA.debugLineNum = 680;BA.debugLine="For i = scvGPSPath1.Panel.NumberOfViews - 1 To 0";
{
final int step5 = (int) (-1);
final int limit5 = (int) (0);
for (_i = (int) (mostCurrent._scvgpspath1.getPanel().getNumberOfViews()-1) ; (step5 > 0 && _i <= limit5) || (step5 < 0 && _i >= limit5); _i = ((int)(0 + _i + step5)) ) {
 //BA.debugLineNum = 681;BA.debugLine="scvGPSPath1.Panel.RemoveViewAt(i)";
mostCurrent._scvgpspath1.getPanel().RemoveViewAt(_i);
 }
};
 //BA.debugLineNum = 683;BA.debugLine="End Sub";
return "";
}
public static String  _dispaltitude(double _altitude) throws Exception{
 //BA.debugLineNum = 888;BA.debugLine="Sub DispAltitude(Altitude As Double) As String";
 //BA.debugLineNum = 890;BA.debugLine="Select Main.AltitudeUnitIndex";
switch (BA.switchObjectToInt(mostCurrent._main._altitudeunitindex,(int) (0),(int) (1))) {
case 0: {
 //BA.debugLineNum = 892;BA.debugLine="Return NumberFormat2(Altitude  * Main.AltitudeUn";
if (true) return anywheresoftware.b4a.keywords.Common.NumberFormat2(_altitude*mostCurrent._main._altitudeunitratio[mostCurrent._main._altitudeunitindex],(int) (1),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False);
 break; }
case 1: {
 //BA.debugLineNum = 894;BA.debugLine="Return NumberFormat2(Altitude  * Main.AltitudeUn";
if (true) return anywheresoftware.b4a.keywords.Common.NumberFormat2(_altitude*mostCurrent._main._altitudeunitratio[mostCurrent._main._altitudeunitindex],(int) (1),(int) (1),(int) (1),anywheresoftware.b4a.keywords.Common.False);
 break; }
}
;
 //BA.debugLineNum = 896;BA.debugLine="End Sub";
return "";
}
public static String  _dispdistance(double _distance) throws Exception{
 //BA.debugLineNum = 878;BA.debugLine="Sub DispDistance(Distance As Double) As String";
 //BA.debugLineNum = 880;BA.debugLine="Select Main.DistanceUnitIndex";
switch (BA.switchObjectToInt(mostCurrent._main._distanceunitindex,(int) (0),(int) (1),(int) (2))) {
case 0: {
 //BA.debugLineNum = 882;BA.debugLine="Return NumberFormat2(Distance  * Main.DistanceUn";
if (true) return anywheresoftware.b4a.keywords.Common.NumberFormat2(_distance*mostCurrent._main._distanceunitratio[mostCurrent._main._distanceunitindex],(int) (1),(int) (1),(int) (1),anywheresoftware.b4a.keywords.Common.False);
 break; }
case 1: 
case 2: {
 //BA.debugLineNum = 884;BA.debugLine="Return NumberFormat2(Distance  * Main.DistanceUn";
if (true) return anywheresoftware.b4a.keywords.Common.NumberFormat2(_distance*mostCurrent._main._distanceunitratio[mostCurrent._main._distanceunitindex],(int) (1),(int) (4),(int) (4),anywheresoftware.b4a.keywords.Common.False);
 break; }
}
;
 //BA.debugLineNum = 886;BA.debugLine="End Sub";
return "";
}
public static String  _fillgpspathtable() throws Exception{
int _i = 0;
B4A.GPS.PaiNaiMa.main._gpslocation _gpsloc = null;
B4A.GPS.PaiNaiMa.main._gpslocation _loc = null;
 //BA.debugLineNum = 685;BA.debugLine="Sub FillGPSPathTable";
 //BA.debugLineNum = 687;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 688;BA.debugLine="Dim GPSLoc As GPSLocation";
_gpsloc = new B4A.GPS.PaiNaiMa.main._gpslocation();
 //BA.debugLineNum = 690;BA.debugLine="NumberOfRows = 0";
_numberofrows = (int) (0);
 //BA.debugLineNum = 692;BA.debugLine="For i = 0 To Main.GPSPath.Size - 1";
{
final int step4 = 1;
final int limit4 = (int) (mostCurrent._main._gpspath.getSize()-1);
for (_i = (int) (0) ; (step4 > 0 && _i <= limit4) || (step4 < 0 && _i >= limit4); _i = ((int)(0 + _i + step4)) ) {
 //BA.debugLineNum = 693;BA.debugLine="Dim loc As GPSLocation";
_loc = new B4A.GPS.PaiNaiMa.main._gpslocation();
 //BA.debugLineNum = 694;BA.debugLine="loc = Main.GPSPath.Get(i)";
_loc = (B4A.GPS.PaiNaiMa.main._gpslocation)(mostCurrent._main._gpspath.Get(_i));
 //BA.debugLineNum = 695;BA.debugLine="If i = 0 Then";
if (_i==0) { 
 //BA.debugLineNum = 696;BA.debugLine="Main.GPSPathTime0 = loc.Time";
mostCurrent._main._gpspathtime0 = _loc.Time;
 //BA.debugLineNum = 697;BA.debugLine="GPSLoc = loc";
_gpsloc = _loc;
 };
 //BA.debugLineNum = 699;BA.debugLine="GPSPathAddRow(loc)";
_gpspathaddrow(_loc);
 //BA.debugLineNum = 700;BA.debugLine="GPSLoc = loc";
_gpsloc = _loc;
 }
};
 //BA.debugLineNum = 702;BA.debugLine="scvGPSPath0.Panel.Height = NumberOfRows * RowHeig";
mostCurrent._scvgpspath0.getPanel().setHeight((int) (_numberofrows*_rowheight));
 //BA.debugLineNum = 703;BA.debugLine="scvGPSPath1.Panel.Height = NumberOfRows * RowHeig";
mostCurrent._scvgpspath1.getPanel().setHeight((int) (_numberofrows*_rowheight));
 //BA.debugLineNum = 704;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 39;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 40;BA.debugLine="Dim btnGPSPathLoad, btnGPSPathSave, btnGPSPathDel";
mostCurrent._btngpspathload = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._btngpspathsave = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._btngpspathdeletepoint = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._btngpspathdeletefile = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Dim btnGPSPathFilter, pnlGPSPathFilterDel, btnGPS";
mostCurrent._btngpspathfilter = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._pnlgpspathfilterdel = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._btngpspathfiltergo = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._btngpspathfilterdel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Dim pnlGPSPath0, pnlGPSPath1,  pnlGPSPathToolbox,";
mostCurrent._pnlgpspath0 = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._pnlgpspath1 = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._pnlgpspathtoolbox = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._pnlgpspathheader0 = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._pnlgpspathheader1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Dim pnlGPSPathFilter, pnlGPSPathFilterToolBox As";
mostCurrent._pnlgpspathfilter = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._pnlgpspathfiltertoolbox = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Dim edtGPSPathFilterMinDist As EditText";
mostCurrent._edtgpspathfiltermindist = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Dim lblGPSPathComment, lblGPSPathDateTime, lblGPS";
mostCurrent._lblgpspathcomment = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lblgpspathdatetime = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lblgpspathdate = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lblgpspathtime = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Dim lblToolTip, lblGPSPathFile, lblGPSPathNbPoint";
mostCurrent._lbltooltip = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lblgpspathfile = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lblgpspathnbpoints = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lblgpspathnbpoints1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Dim lblGPSPathFilterNb0, lblGPSPathFilterNb1 As L";
mostCurrent._lblgpspathfilternb0 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lblgpspathfilternb1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Dim scvGPSPath0, scvGPSPath1 As ScrollView";
mostCurrent._scvgpspath0 = new anywheresoftware.b4a.objects.ScrollViewWrapper();
mostCurrent._scvgpspath1 = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Dim skbGPSPath As SeekBar";
mostCurrent._skbgpspath = new anywheresoftware.b4a.objects.SeekBarWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Dim Dialog1 As FileDialog";
mostCurrent._dialog1 = new anywheresoftware.b4a.agraham.dialogs.InputDialog.FileDialog();
 //BA.debugLineNum = 56;BA.debugLine="Dim GPSFilterIndexes As List";
mostCurrent._gpsfilterindexes = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 57;BA.debugLine="End Sub";
return "";
}
public static double  _gpscalcx(double _lat) throws Exception{
 //BA.debugLineNum = 547;BA.debugLine="Sub GPSCalcX(lat As Double) As Double";
 //BA.debugLineNum = 549;BA.debugLine="Return lat * EarthRadius / 180 * cPI";
if (true) return _lat*_earthradius/(double)180*anywheresoftware.b4a.keywords.Common.cPI;
 //BA.debugLineNum = 550;BA.debugLine="End Sub";
return 0;
}
public static double  _gpscalcy(double _lat,double _lng) throws Exception{
 //BA.debugLineNum = 542;BA.debugLine="Sub GPSCalcY(lat As Double, lng As Double) As Doub";
 //BA.debugLineNum = 544;BA.debugLine="Return lng * EarthRadius / 180 * cPI  * CosD(lat)";
if (true) return _lng*_earthradius/(double)180*anywheresoftware.b4a.keywords.Common.cPI*anywheresoftware.b4a.keywords.Common.CosD(_lat);
 //BA.debugLineNum = 545;BA.debugLine="End Sub";
return 0;
}
public static String  _gpspathaddrow(B4A.GPS.PaiNaiMa.main._gpslocation _values) throws Exception{
int _i = 0;
anywheresoftware.b4a.objects.LabelWrapper _l = null;
B4A.GPS.PaiNaiMa.gpspaths._rowcol _rc = null;
 //BA.debugLineNum = 706;BA.debugLine="Sub GPSPathAddRow(Values As GPSLocation)";
 //BA.debugLineNum = 708;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 710;BA.debugLine="Dim l As Label";
_l = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 711;BA.debugLine="l.Initialize(\"cell\")";
_l.Initialize(mostCurrent.activityBA,"cell");
 //BA.debugLineNum = 712;BA.debugLine="l.Text = NumberOfRows";
_l.setText((Object)(_numberofrows));
 //BA.debugLineNum = 713;BA.debugLine="l.Gravity = CellAlignment";
_l.setGravity(_cellalignment);
 //BA.debugLineNum = 714;BA.debugLine="l.TextSize = CellTextSize";
_l.setTextSize(_celltextsize);
 //BA.debugLineNum = 715;BA.debugLine="l.TextColor = CellTextColor";
_l.setTextColor(_celltextcolor);
 //BA.debugLineNum = 716;BA.debugLine="l.Color = CellColor";
_l.setColor(_cellcolor);
 //BA.debugLineNum = 717;BA.debugLine="Dim rc As RowCol";
_rc = new B4A.GPS.PaiNaiMa.gpspaths._rowcol();
 //BA.debugLineNum = 718;BA.debugLine="rc.Initialize";
_rc.Initialize();
 //BA.debugLineNum = 719;BA.debugLine="rc.Col = -1";
_rc.Col = (int) (-1);
 //BA.debugLineNum = 720;BA.debugLine="rc.Row = NumberOfRows";
_rc.Row = _numberofrows;
 //BA.debugLineNum = 721;BA.debugLine="l.Tag = rc";
_l.setTag((Object)(_rc));
 //BA.debugLineNum = 722;BA.debugLine="scvGPSPath0.Panel.AddView(l, 0, RowHeight * Numbe";
mostCurrent._scvgpspath0.getPanel().AddView((android.view.View)(_l.getObject()),(int) (0),(int) (_rowheight*_numberofrows),(int) (_cellwidth0_1),(int) (_rowheight_1));
 //BA.debugLineNum = 724;BA.debugLine="For i = 0 To NumberOfColumns_1";
{
final int step15 = 1;
final int limit15 = _numberofcolumns_1;
for (_i = (int) (0) ; (step15 > 0 && _i <= limit15) || (step15 < 0 && _i >= limit15); _i = ((int)(0 + _i + step15)) ) {
 //BA.debugLineNum = 725;BA.debugLine="Dim l As Label";
_l = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 726;BA.debugLine="l.Initialize(\"cell\")";
_l.Initialize(mostCurrent.activityBA,"cell");
 //BA.debugLineNum = 727;BA.debugLine="Select i";
switch (_i) {
case 0: {
 //BA.debugLineNum = 729;BA.debugLine="l.Text = NumberFormat(Values.Latitude, 1, 6)";
_l.setText((Object)(anywheresoftware.b4a.keywords.Common.NumberFormat(_values.Latitude,(int) (1),(int) (6))));
 break; }
case 1: {
 //BA.debugLineNum = 731;BA.debugLine="l.Text = NumberFormat(Values.Longitude, 1, 6)";
_l.setText((Object)(anywheresoftware.b4a.keywords.Common.NumberFormat(_values.Longitude,(int) (1),(int) (6))));
 break; }
case 2: {
 //BA.debugLineNum = 733;BA.debugLine="l.Text = DispAltitude(Values.Altitude)";
_l.setText((Object)(_dispaltitude(_values.Altitude)));
 break; }
case 3: {
 //BA.debugLineNum = 735;BA.debugLine="l.Text = (Values.Time - Main.GPSPathTime0) / 10";
_l.setText((Object)((_values.Time-mostCurrent._main._gpspathtime0)/(double)1000));
 break; }
case 4: {
 //BA.debugLineNum = 737;BA.debugLine="l.Text = NumberFormat(Values.Bearing, 1, 1)";
_l.setText((Object)(anywheresoftware.b4a.keywords.Common.NumberFormat(_values.Bearing,(int) (1),(int) (1))));
 break; }
case 5: {
 //BA.debugLineNum = 739;BA.debugLine="l.Text = NumberFormat(Values.Speed * Main.Speed";
_l.setText((Object)(anywheresoftware.b4a.keywords.Common.NumberFormat(_values.Speed*mostCurrent._main._speedunitratio[mostCurrent._main._speedunitindex],(int) (1),(int) (2))));
 break; }
case 6: {
 //BA.debugLineNum = 741;BA.debugLine="l.Text = DispDistance(Values.Distance)";
_l.setText((Object)(_dispdistance(_values.Distance)));
 break; }
case 7: {
 //BA.debugLineNum = 743;BA.debugLine="l.Text = DispDistance(Values.DistTot)";
_l.setText((Object)(_dispdistance(_values.DistTot)));
 break; }
case 8: {
 //BA.debugLineNum = 745;BA.debugLine="If Values.Marker = True Then";
if (_values.Marker==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 746;BA.debugLine="l.Text = \"√\"";
_l.setText((Object)("√"));
 }else {
 //BA.debugLineNum = 748;BA.debugLine="l.Text = \"-\"";
_l.setText((Object)("-"));
 };
 break; }
}
;
 //BA.debugLineNum = 752;BA.debugLine="l.Gravity = CellAlignment";
_l.setGravity(_cellalignment);
 //BA.debugLineNum = 753;BA.debugLine="l.TextSize = CellTextSize";
_l.setTextSize(_celltextsize);
 //BA.debugLineNum = 754;BA.debugLine="l.TextColor = CellTextColor";
_l.setTextColor(_celltextcolor);
 //BA.debugLineNum = 755;BA.debugLine="l.Color = CellColor";
_l.setColor(_cellcolor);
 //BA.debugLineNum = 756;BA.debugLine="Dim rc As RowCol";
_rc = new B4A.GPS.PaiNaiMa.gpspaths._rowcol();
 //BA.debugLineNum = 757;BA.debugLine="rc.Initialize";
_rc.Initialize();
 //BA.debugLineNum = 758;BA.debugLine="rc.Col = i";
_rc.Col = _i;
 //BA.debugLineNum = 759;BA.debugLine="rc.Row = NumberOfRows";
_rc.Row = _numberofrows;
 //BA.debugLineNum = 760;BA.debugLine="l.Tag = rc";
_l.setTag((Object)(_rc));
 //BA.debugLineNum = 761;BA.debugLine="scvGPSPath1.Panel.AddView(l, CellWidthTotal(i),";
mostCurrent._scvgpspath1.getPanel().AddView((android.view.View)(_l.getObject()),(int) (_cellwidthtotal[_i]),(int) (_rowheight*_numberofrows),(int) (_cellwidth_1[_i]),(int) (_rowheight_1));
 }
};
 //BA.debugLineNum = 763;BA.debugLine="NumberOfRows = NumberOfRows + 1";
_numberofrows = (int) (_numberofrows+1);
 //BA.debugLineNum = 766;BA.debugLine="End Sub";
return "";
}
public static double  _gpspathcalcdelta(double _x1,double _y1,double _xp,double _yp,double _x2,double _y2) throws Exception{
double _d = 0;
double _x = 0;
double _y = 0;
 //BA.debugLineNum = 552;BA.debugLine="Sub GPSPathCalcDelta(x1 As Double, y1 As Double, x";
 //BA.debugLineNum = 555;BA.debugLine="Dim d, x, y As Double";
_d = 0;
_x = 0;
_y = 0;
 //BA.debugLineNum = 557;BA.debugLine="d = Abs((xp - x1) * (x2 - x1) + (yp - y1) * (y2 -";
_d = anywheresoftware.b4a.keywords.Common.Abs((_xp-_x1)*(_x2-_x1)+(_yp-_y1)*(_y2-_y1))/(double)((_x2-_x1)*(_x2-_x1)+(_y2-_y1)*(_y2-_y1));
 //BA.debugLineNum = 558;BA.debugLine="x = x1 + d * (x2 - x1)";
_x = _x1+_d*(_x2-_x1);
 //BA.debugLineNum = 559;BA.debugLine="y = y1 + d * (y2 - y1)";
_y = _y1+_d*(_y2-_y1);
 //BA.debugLineNum = 560;BA.debugLine="d = Sqrt((xp - x) * (xp - x) + (yp - y) * (yp - y";
_d = anywheresoftware.b4a.keywords.Common.Sqrt((_xp-_x)*(_xp-_x)+(_yp-_y)*(_yp-_y));
 //BA.debugLineNum = 561;BA.debugLine="Return d";
if (true) return _d;
 //BA.debugLineNum = 562;BA.debugLine="End Sub";
return 0;
}
public static String  _gpspathdeletepoint() throws Exception{
int _i = 0;
int _j = 0;
int _m = 0;
int _m1 = 0;
int _n = 0;
int _n1 = 0;
String _txt = "";
anywheresoftware.b4a.objects.LabelWrapper _lbl = null;
anywheresoftware.b4a.objects.LabelWrapper _lbl1 = null;
B4A.GPS.PaiNaiMa.main._gpslocation _gpsloc1 = null;
B4A.GPS.PaiNaiMa.main._gpslocation _gpsloc2 = null;
anywheresoftware.b4a.gps.LocationWrapper _loc1 = null;
anywheresoftware.b4a.gps.LocationWrapper _loc2 = null;
 //BA.debugLineNum = 280;BA.debugLine="Sub GPSPathDeletePoint";
 //BA.debugLineNum = 281;BA.debugLine="Dim i, j, m, m1, n, n1 As Int";
_i = 0;
_j = 0;
_m = 0;
_m1 = 0;
_n = 0;
_n1 = 0;
 //BA.debugLineNum = 282;BA.debugLine="Dim txt As String";
_txt = "";
 //BA.debugLineNum = 285;BA.debugLine="Main.GPSPath.RemoveAt(SelectedRow)";
mostCurrent._main._gpspath.RemoveAt(_selectedrow);
 //BA.debugLineNum = 287;BA.debugLine="If SelectedRow = NumberOfRows - 1 Then";
if (_selectedrow==_numberofrows-1) { 
 //BA.debugLineNum = 289;BA.debugLine="n = (NumberOfRows - 1) * NumberOfColumns";
_n = (int) ((_numberofrows-1)*_numberofcolumns);
 //BA.debugLineNum = 290;BA.debugLine="For i = NumberOfColumns - 2 To 0 Step -1			'****";
{
final int step6 = (int) (-1);
final int limit6 = (int) (0);
for (_i = (int) (_numberofcolumns-2) ; (step6 > 0 && _i <= limit6) || (step6 < 0 && _i >= limit6); _i = ((int)(0 + _i + step6)) ) {
 //BA.debugLineNum = 291;BA.debugLine="scvGPSPath1.Panel.RemoveViewAt(n + i)";
mostCurrent._scvgpspath1.getPanel().RemoveViewAt((int) (_n+_i));
 }
};
 //BA.debugLineNum = 293;BA.debugLine="SelectedRow = -1";
_selectedrow = (int) (-1);
 //BA.debugLineNum = 294;BA.debugLine="NumberOfRows = NumberOfRows - 1";
_numberofrows = (int) (_numberofrows-1);
 //BA.debugLineNum = 295;BA.debugLine="scvGPSPath0.Panel.Height = NumberOfRows * RowHei";
mostCurrent._scvgpspath0.getPanel().setHeight((int) (_numberofrows*_rowheight));
 //BA.debugLineNum = 296;BA.debugLine="scvGPSPath1.Panel.Height = NumberOfRows * RowHei";
mostCurrent._scvgpspath1.getPanel().setHeight((int) (_numberofrows*_rowheight));
 //BA.debugLineNum = 297;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 301;BA.debugLine="For i = SelectedRow To NumberOfRows - 2";
{
final int step15 = 1;
final int limit15 = (int) (_numberofrows-2);
for (_i = _selectedrow ; (step15 > 0 && _i <= limit15) || (step15 < 0 && _i >= limit15); _i = ((int)(0 + _i + step15)) ) {
 //BA.debugLineNum = 302;BA.debugLine="m = i * NumberOfColumns";
_m = (int) (_i*_numberofcolumns);
 //BA.debugLineNum = 303;BA.debugLine="m1 = (i + 1) * NumberOfColumns";
_m1 = (int) ((_i+1)*_numberofcolumns);
 //BA.debugLineNum = 304;BA.debugLine="For j = 0 To NumberOfColumns_1";
{
final int step18 = 1;
final int limit18 = _numberofcolumns_1;
for (_j = (int) (0) ; (step18 > 0 && _j <= limit18) || (step18 < 0 && _j >= limit18); _j = ((int)(0 + _j + step18)) ) {
 //BA.debugLineNum = 305;BA.debugLine="n = m + j";
_n = (int) (_m+_j);
 //BA.debugLineNum = 306;BA.debugLine="n1 = m1 + j";
_n1 = (int) (_m1+_j);
 //BA.debugLineNum = 307;BA.debugLine="Dim lbl, lbl1 As Label";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
_lbl1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 308;BA.debugLine="lbl =	scvGPSPath1.Panel.GetView(n)";
_lbl.setObject((android.widget.TextView)(mostCurrent._scvgpspath1.getPanel().GetView(_n).getObject()));
 //BA.debugLineNum = 309;BA.debugLine="lbl1 =	scvGPSPath1.Panel.GetView(n1)";
_lbl1.setObject((android.widget.TextView)(mostCurrent._scvgpspath1.getPanel().GetView(_n1).getObject()));
 //BA.debugLineNum = 310;BA.debugLine="Dim GPSloc1 As GPSLocation";
_gpsloc1 = new B4A.GPS.PaiNaiMa.main._gpslocation();
 //BA.debugLineNum = 311;BA.debugLine="GPSloc1.Initialize";
_gpsloc1.Initialize();
 //BA.debugLineNum = 312;BA.debugLine="GPSloc1 = Main.GPSPath.Get(i)";
_gpsloc1 = (B4A.GPS.PaiNaiMa.main._gpslocation)(mostCurrent._main._gpspath.Get(_i));
 //BA.debugLineNum = 313;BA.debugLine="lbl.Text = lbl1.Text";
_lbl.setText((Object)(_lbl1.getText()));
 }
};
 }
};
 //BA.debugLineNum = 317;BA.debugLine="n = (NumberOfRows - 1) * NumberOfColumns";
_n = (int) ((_numberofrows-1)*_numberofcolumns);
 //BA.debugLineNum = 318;BA.debugLine="For i = NumberOfColumns_1 To 0 Step -1";
{
final int step31 = (int) (-1);
final int limit31 = (int) (0);
for (_i = _numberofcolumns_1 ; (step31 > 0 && _i <= limit31) || (step31 < 0 && _i >= limit31); _i = ((int)(0 + _i + step31)) ) {
 //BA.debugLineNum = 319;BA.debugLine="scvGPSPath1.Panel.RemoveViewAt(n + i)";
mostCurrent._scvgpspath1.getPanel().RemoveViewAt((int) (_n+_i));
 }
};
 //BA.debugLineNum = 323;BA.debugLine="If SelectedRow = 0 Then";
if (_selectedrow==0) { 
 //BA.debugLineNum = 324;BA.debugLine="Dim GPSloc1 As GPSLocation";
_gpsloc1 = new B4A.GPS.PaiNaiMa.main._gpslocation();
 //BA.debugLineNum = 325;BA.debugLine="GPSloc1.Initialize";
_gpsloc1.Initialize();
 //BA.debugLineNum = 326;BA.debugLine="GPSloc1 = Main.GPSPath.Get(0)";
_gpsloc1 = (B4A.GPS.PaiNaiMa.main._gpslocation)(mostCurrent._main._gpspath.Get((int) (0)));
 //BA.debugLineNum = 327;BA.debugLine="GPSloc1.Distance = 0";
_gpsloc1.Distance = 0;
 //BA.debugLineNum = 328;BA.debugLine="GPSloc1.DistTot = 0";
_gpsloc1.DistTot = 0;
 //BA.debugLineNum = 329;BA.debugLine="GPSloc1.Speed = 0";
_gpsloc1.Speed = (float) (0);
 //BA.debugLineNum = 330;BA.debugLine="Main.GPSPath.Set(SelectedRow, GPSloc1)";
mostCurrent._main._gpspath.Set(_selectedrow,(Object)(_gpsloc1));
 //BA.debugLineNum = 331;BA.debugLine="Dim lbl As Label";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 332;BA.debugLine="lbl = scvGPSPath1.Panel.GetView(6)";
_lbl.setObject((android.widget.TextView)(mostCurrent._scvgpspath1.getPanel().GetView((int) (6)).getObject()));
 //BA.debugLineNum = 333;BA.debugLine="lbl.Text = GPSloc1.Distance";
_lbl.setText((Object)(_gpsloc1.Distance));
 //BA.debugLineNum = 334;BA.debugLine="Dim lbl As Label";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 335;BA.debugLine="lbl = scvGPSPath1.Panel.GetView(7)";
_lbl.setObject((android.widget.TextView)(mostCurrent._scvgpspath1.getPanel().GetView((int) (7)).getObject()));
 //BA.debugLineNum = 336;BA.debugLine="lbl.Text = GPSloc1.DistTot";
_lbl.setText((Object)(_gpsloc1.DistTot));
 //BA.debugLineNum = 337;BA.debugLine="Dim lbl As Label";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 338;BA.debugLine="lbl = scvGPSPath1.Panel.GetView(5)";
_lbl.setObject((android.widget.TextView)(mostCurrent._scvgpspath1.getPanel().GetView((int) (5)).getObject()));
 //BA.debugLineNum = 339;BA.debugLine="lbl.Text = GPSloc1.Speed";
_lbl.setText((Object)(_gpsloc1.Speed));
 //BA.debugLineNum = 340;BA.debugLine="Main.GPSPathTime0 = GPSloc1.Time";
mostCurrent._main._gpspathtime0 = _gpsloc1.Time;
 //BA.debugLineNum = 341;BA.debugLine="For i = 0 To NumberOfRows - 2";
{
final int step52 = 1;
final int limit52 = (int) (_numberofrows-2);
for (_i = (int) (0) ; (step52 > 0 && _i <= limit52) || (step52 < 0 && _i >= limit52); _i = ((int)(0 + _i + step52)) ) {
 //BA.debugLineNum = 342;BA.debugLine="Dim GPSloc1 As GPSLocation";
_gpsloc1 = new B4A.GPS.PaiNaiMa.main._gpslocation();
 //BA.debugLineNum = 343;BA.debugLine="GPSloc1.Initialize";
_gpsloc1.Initialize();
 //BA.debugLineNum = 344;BA.debugLine="GPSloc1 = Main.GPSPath.Get(i)";
_gpsloc1 = (B4A.GPS.PaiNaiMa.main._gpslocation)(mostCurrent._main._gpspath.Get(_i));
 //BA.debugLineNum = 345;BA.debugLine="Dim lbl As Label";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 346;BA.debugLine="n = i * NumberOfColumns + 3";
_n = (int) (_i*_numberofcolumns+3);
 //BA.debugLineNum = 347;BA.debugLine="lbl = scvGPSPath1.Panel.GetView(n)";
_lbl.setObject((android.widget.TextView)(mostCurrent._scvgpspath1.getPanel().GetView(_n).getObject()));
 //BA.debugLineNum = 348;BA.debugLine="Log(i & \" : \" & GPSloc1.Time)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_i)+" : "+BA.NumberToString(_gpsloc1.Time));
 //BA.debugLineNum = 349;BA.debugLine="lbl.Text = (GPSloc1.Time - Main.GPSPathTime0)";
_lbl.setText((Object)((_gpsloc1.Time-mostCurrent._main._gpspathtime0)));
 }
};
 }else {
 //BA.debugLineNum = 352;BA.debugLine="Dim GPSloc1, GPSloc2 As GPSLocation";
_gpsloc1 = new B4A.GPS.PaiNaiMa.main._gpslocation();
_gpsloc2 = new B4A.GPS.PaiNaiMa.main._gpslocation();
 //BA.debugLineNum = 353;BA.debugLine="Dim loc1, loc2 As Location";
_loc1 = new anywheresoftware.b4a.gps.LocationWrapper();
_loc2 = new anywheresoftware.b4a.gps.LocationWrapper();
 //BA.debugLineNum = 354;BA.debugLine="GPSloc1.Initialize";
_gpsloc1.Initialize();
 //BA.debugLineNum = 355;BA.debugLine="GPSloc2.Initialize";
_gpsloc2.Initialize();
 //BA.debugLineNum = 356;BA.debugLine="loc1.Initialize";
_loc1.Initialize();
 //BA.debugLineNum = 357;BA.debugLine="loc2.Initialize";
_loc2.Initialize();
 //BA.debugLineNum = 358;BA.debugLine="GPSloc1 = Main.GPSPath.Get(SelectedRow - 1)";
_gpsloc1 = (B4A.GPS.PaiNaiMa.main._gpslocation)(mostCurrent._main._gpspath.Get((int) (_selectedrow-1)));
 //BA.debugLineNum = 359;BA.debugLine="loc1.Latitude = GPSloc1.Latitude";
_loc1.setLatitude(_gpsloc1.Latitude);
 //BA.debugLineNum = 360;BA.debugLine="loc1.Longitude = GPSloc1.Longitude";
_loc1.setLongitude(_gpsloc1.Longitude);
 //BA.debugLineNum = 361;BA.debugLine="GPSloc2 = Main.GPSPath.Get(SelectedRow)";
_gpsloc2 = (B4A.GPS.PaiNaiMa.main._gpslocation)(mostCurrent._main._gpspath.Get(_selectedrow));
 //BA.debugLineNum = 362;BA.debugLine="loc2.Latitude = GPSloc2.Latitude";
_loc2.setLatitude(_gpsloc2.Latitude);
 //BA.debugLineNum = 363;BA.debugLine="loc2.Longitude = GPSloc2.Longitude";
_loc2.setLongitude(_gpsloc2.Longitude);
 //BA.debugLineNum = 364;BA.debugLine="GPSloc2.Distance = NumberFormat(loc1.DistanceTo(";
_gpsloc2.Distance = (double)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.NumberFormat(_loc1.DistanceTo((android.location.Location)(_loc2.getObject())),(int) (1),(int) (3))));
 //BA.debugLineNum = 365;BA.debugLine="GPSloc2.Bearing = NumberFormat(loc1.BearingTo(lo";
_gpsloc2.Bearing = (float)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.NumberFormat(_loc1.BearingTo((android.location.Location)(_loc2.getObject())),(int) (1),(int) (1))));
 //BA.debugLineNum = 366;BA.debugLine="GPSloc2.Speed =  NumberFormat(GPSloc2.Distance /";
_gpsloc2.Speed = (float)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.NumberFormat(_gpsloc2.Distance/(double)(_gpsloc2.Time-_gpsloc1.Time),(int) (1),(int) (1))));
 //BA.debugLineNum = 367;BA.debugLine="Main.GPSPath.Set(SelectedRow, GPSloc2)";
mostCurrent._main._gpspath.Set(_selectedrow,(Object)(_gpsloc2));
 //BA.debugLineNum = 368;BA.debugLine="n = SelectedRow * NumberOfColumns + 6";
_n = (int) (_selectedrow*_numberofcolumns+6);
 //BA.debugLineNum = 369;BA.debugLine="Dim lbl As Label";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 370;BA.debugLine="lbl = scvGPSPath1.Panel.GetView(n)";
_lbl.setObject((android.widget.TextView)(mostCurrent._scvgpspath1.getPanel().GetView(_n).getObject()));
 //BA.debugLineNum = 371;BA.debugLine="lbl.Text = DispDistance(GPSloc2.Distance)";
_lbl.setText((Object)(_dispdistance(_gpsloc2.Distance)));
 //BA.debugLineNum = 372;BA.debugLine="n = SelectedRow * NumberOfColumns + 5";
_n = (int) (_selectedrow*_numberofcolumns+5);
 //BA.debugLineNum = 373;BA.debugLine="Dim lbl As Label";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 374;BA.debugLine="lbl = scvGPSPath1.Panel.GetView(n)";
_lbl.setObject((android.widget.TextView)(mostCurrent._scvgpspath1.getPanel().GetView(_n).getObject()));
 //BA.debugLineNum = 375;BA.debugLine="lbl.Text = GPSloc2.Speed";
_lbl.setText((Object)(_gpsloc2.Speed));
 };
 //BA.debugLineNum = 379;BA.debugLine="For i = SelectedRow To NumberOfRows - 2";
{
final int step88 = 1;
final int limit88 = (int) (_numberofrows-2);
for (_i = _selectedrow ; (step88 > 0 && _i <= limit88) || (step88 < 0 && _i >= limit88); _i = ((int)(0 + _i + step88)) ) {
 //BA.debugLineNum = 380;BA.debugLine="GPSloc2 = Main.GPSPath.Get(i)";
_gpsloc2 = (B4A.GPS.PaiNaiMa.main._gpslocation)(mostCurrent._main._gpspath.Get(_i));
 //BA.debugLineNum = 381;BA.debugLine="GPSloc2.DistTot = NumberFormat(GPSloc1.DistTot +";
_gpsloc2.DistTot = (double)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.NumberFormat(_gpsloc1.DistTot+_gpsloc2.Distance,(int) (1),(int) (3))));
 //BA.debugLineNum = 382;BA.debugLine="n = i * NumberOfColumns + 7";
_n = (int) (_i*_numberofcolumns+7);
 //BA.debugLineNum = 383;BA.debugLine="lbl = scvGPSPath1.Panel.GetView(n)";
_lbl.setObject((android.widget.TextView)(mostCurrent._scvgpspath1.getPanel().GetView(_n).getObject()));
 //BA.debugLineNum = 384;BA.debugLine="lbl.Text = DispDistance(GPSloc2.DistTot)";
_lbl.setText((Object)(_dispdistance(_gpsloc2.DistTot)));
 //BA.debugLineNum = 385;BA.debugLine="GPSloc1 = GPSloc2";
_gpsloc1 = _gpsloc2;
 }
};
 //BA.debugLineNum = 389;BA.debugLine="If SelectedRow < NumberOfRows - 1 Then";
if (_selectedrow<_numberofrows-1) { 
 //BA.debugLineNum = 390;BA.debugLine="ChangeCellSelection(SelectedRow, 0)";
_changecellselection(_selectedrow,(int) (0));
 };
 //BA.debugLineNum = 392;BA.debugLine="SelectedRow = -1";
_selectedrow = (int) (-1);
 //BA.debugLineNum = 394;BA.debugLine="NumberOfRows = NumberOfRows - 1";
_numberofrows = (int) (_numberofrows-1);
 //BA.debugLineNum = 395;BA.debugLine="scvGPSPath0.Panel.Height = NumberOfRows * RowHeig";
mostCurrent._scvgpspath0.getPanel().setHeight((int) (_numberofrows*_rowheight));
 //BA.debugLineNum = 396;BA.debugLine="scvGPSPath1.Panel.Height = NumberOfRows * RowHeig";
mostCurrent._scvgpspath1.getPanel().setHeight((int) (_numberofrows*_rowheight));
 //BA.debugLineNum = 397;BA.debugLine="End Sub";
return "";
}
public static String  _gpspathfilter() throws Exception{
int _i0 = 0;
int _i1 = 0;
int _i2 = 0;
double _x0 = 0;
double _x1 = 0;
double _x2 = 0;
double _y0 = 0;
double _y1 = 0;
double _y2 = 0;
B4A.GPS.PaiNaiMa.main._gpslocation _g = null;
double _d = 0;
int _i = 0;
 //BA.debugLineNum = 506;BA.debugLine="Sub GPSPathFilter";
 //BA.debugLineNum = 507;BA.debugLine="Dim i0, i1, i2 As Int";
_i0 = 0;
_i1 = 0;
_i2 = 0;
 //BA.debugLineNum = 508;BA.debugLine="Dim x0, x1, x2 As Double";
_x0 = 0;
_x1 = 0;
_x2 = 0;
 //BA.debugLineNum = 509;BA.debugLine="Dim y0, y1, y2 As Double";
_y0 = 0;
_y1 = 0;
_y2 = 0;
 //BA.debugLineNum = 510;BA.debugLine="Dim g As GPSLocation";
_g = new B4A.GPS.PaiNaiMa.main._gpslocation();
 //BA.debugLineNum = 511;BA.debugLine="Dim d As Double";
_d = 0;
 //BA.debugLineNum = 513;BA.debugLine="GPSPathDelete.Initialize";
_gpspathdelete.Initialize();
 //BA.debugLineNum = 514;BA.debugLine="i0 = Main.GPSPath.Size - 1";
_i0 = (int) (mostCurrent._main._gpspath.getSize()-1);
 //BA.debugLineNum = 515;BA.debugLine="g.Initialize";
_g.Initialize();
 //BA.debugLineNum = 516;BA.debugLine="g = Main.GPSPath.Get(i0)";
_g = (B4A.GPS.PaiNaiMa.main._gpslocation)(mostCurrent._main._gpspath.Get(_i0));
 //BA.debugLineNum = 517;BA.debugLine="y0 = GPSCalcY(g.Latitude, g.Longitude)";
_y0 = _gpscalcy(_g.Latitude,_g.Longitude);
 //BA.debugLineNum = 518;BA.debugLine="x0 = GPSCalcX(g.Latitude)";
_x0 = _gpscalcx(_g.Latitude);
 //BA.debugLineNum = 520;BA.debugLine="i1 = i0 - 1";
_i1 = (int) (_i0-1);
 //BA.debugLineNum = 521;BA.debugLine="g = Main.GPSPath.Get(i1)";
_g = (B4A.GPS.PaiNaiMa.main._gpslocation)(mostCurrent._main._gpspath.Get(_i1));
 //BA.debugLineNum = 522;BA.debugLine="y1 = GPSCalcY(g.Latitude, g.Longitude)";
_y1 = _gpscalcy(_g.Latitude,_g.Longitude);
 //BA.debugLineNum = 523;BA.debugLine="x1 = GPSCalcX(g.Latitude)";
_x1 = _gpscalcx(_g.Latitude);
 //BA.debugLineNum = 524;BA.debugLine="For i = Main.GPSPath.Size - 3 To 0 Step - 1";
{
final int step16 = (int) (-1);
final int limit16 = (int) (0);
for (_i = (int) (mostCurrent._main._gpspath.getSize()-3) ; (step16 > 0 && _i <= limit16) || (step16 < 0 && _i >= limit16); _i = ((int)(0 + _i + step16)) ) {
 //BA.debugLineNum = 525;BA.debugLine="g = Main.GPSPath.Get(i)";
_g = (B4A.GPS.PaiNaiMa.main._gpslocation)(mostCurrent._main._gpspath.Get(_i));
 //BA.debugLineNum = 526;BA.debugLine="y2 = GPSCalcY(g.Latitude, g.Longitude)";
_y2 = _gpscalcy(_g.Latitude,_g.Longitude);
 //BA.debugLineNum = 527;BA.debugLine="x2 = GPSCalcX(g.Latitude)";
_x2 = _gpscalcx(_g.Latitude);
 //BA.debugLineNum = 528;BA.debugLine="d = GPSPathCalcDelta(x0, y0, x1, y1, x2, y2)";
_d = _gpspathcalcdelta(_x0,_y0,_x1,_y1,_x2,_y2);
 //BA.debugLineNum = 529;BA.debugLine="If d <= Main.GPSFilterDelta Then";
if (_d<=mostCurrent._main._gpsfilterdelta) { 
 //BA.debugLineNum = 530;BA.debugLine="GPSPathDelete.Add(i + 1)";
_gpspathdelete.Add((Object)(_i+1));
 //BA.debugLineNum = 531;BA.debugLine="ChangeCellSelection(i + 1, 1)";
_changecellselection((int) (_i+1),(int) (1));
 };
 //BA.debugLineNum = 533;BA.debugLine="x0 = x1";
_x0 = _x1;
 //BA.debugLineNum = 534;BA.debugLine="y0 = y1";
_y0 = _y1;
 //BA.debugLineNum = 535;BA.debugLine="x1 = x2";
_x1 = _x2;
 //BA.debugLineNum = 536;BA.debugLine="y1 = y2";
_y1 = _y2;
 }
};
 //BA.debugLineNum = 538;BA.debugLine="lblGPSPathFilterNb0.Text = \"พบจุดเส้นทาง\"";
mostCurrent._lblgpspathfilternb0.setText((Object)("พบจุดเส้นทาง"));
 //BA.debugLineNum = 539;BA.debugLine="lblGPSPathFilterNb1.Text = GPSPathDelete.Size & \"";
mostCurrent._lblgpspathfilternb1.setText((Object)(BA.NumberToString(_gpspathdelete.getSize())+" / "+BA.NumberToString(mostCurrent._main._gpspath.getSize())));
 //BA.debugLineNum = 540;BA.debugLine="End Sub";
return "";
}
public static String  _gpspathfilterdelete() throws Exception{
int _i = 0;
 //BA.debugLineNum = 564;BA.debugLine="Sub GPSPathFilterDelete";
 //BA.debugLineNum = 567;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 569;BA.debugLine="For i = 0 To GPSPathDelete.Size - 1";
{
final int step2 = 1;
final int limit2 = (int) (_gpspathdelete.getSize()-1);
for (_i = (int) (0) ; (step2 > 0 && _i <= limit2) || (step2 < 0 && _i >= limit2); _i = ((int)(0 + _i + step2)) ) {
 //BA.debugLineNum = 570;BA.debugLine="SelectedRow = GPSPathDelete.Get(i)";
_selectedrow = (int)(BA.ObjectToNumber(_gpspathdelete.Get(_i)));
 //BA.debugLineNum = 571;BA.debugLine="GPSPathDeletePoint";
_gpspathdeletepoint();
 }
};
 //BA.debugLineNum = 573;BA.debugLine="ClearTable";
_cleartable();
 //BA.debugLineNum = 574;BA.debugLine="FillGPSPathTable";
_fillgpspathtable();
 //BA.debugLineNum = 575;BA.debugLine="GPSPathDelete.Initialize";
_gpspathdelete.Initialize();
 //BA.debugLineNum = 576;BA.debugLine="lblGPSPathFilterNb0.Text = \"จำนวนเส้นทาง\"";
mostCurrent._lblgpspathfilternb0.setText((Object)("จำนวนเส้นทาง"));
 //BA.debugLineNum = 577;BA.debugLine="lblGPSPathFilterNb1.Text = Main.GPSPath.Size";
mostCurrent._lblgpspathfilternb1.setText((Object)(mostCurrent._main._gpspath.getSize()));
 //BA.debugLineNum = 578;BA.debugLine="lblGPSPathNbPoints1.Text = Main.GPSPath.Size & \"";
mostCurrent._lblgpspathnbpoints1.setText((Object)(BA.NumberToString(mostCurrent._main._gpspath.getSize())+" จุด"));
 //BA.debugLineNum = 579;BA.debugLine="End Sub";
return "";
}
public static String  _gpspathunfilter() throws Exception{
int _i = 0;
 //BA.debugLineNum = 494;BA.debugLine="Sub GPSPathUnFilter";
 //BA.debugLineNum = 495;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 497;BA.debugLine="For i = 0 To GPSPathDelete.Size - 1";
{
final int step2 = 1;
final int limit2 = (int) (_gpspathdelete.getSize()-1);
for (_i = (int) (0) ; (step2 > 0 && _i <= limit2) || (step2 < 0 && _i >= limit2); _i = ((int)(0 + _i + step2)) ) {
 //BA.debugLineNum = 498;BA.debugLine="ChangeCellSelection(GPSPathDelete.Get(i), 2)";
_changecellselection((int)(BA.ObjectToNumber(_gpspathdelete.Get(_i))),(int) (2));
 }
};
 //BA.debugLineNum = 500;BA.debugLine="lblGPSPathFilterNb0.Text = \"จำนวนจุด\"";
mostCurrent._lblgpspathfilternb0.setText((Object)("จำนวนจุด"));
 //BA.debugLineNum = 501;BA.debugLine="lblGPSPathFilterNb1.Text = Main.GPSPath.Size";
mostCurrent._lblgpspathfilternb1.setText((Object)(mostCurrent._main._gpspath.getSize()));
 //BA.debugLineNum = 502;BA.debugLine="SelectedRow = -1";
_selectedrow = (int) (-1);
 //BA.debugLineNum = 503;BA.debugLine="btnGPSPathDeletePoint.Visible = True";
mostCurrent._btngpspathdeletepoint.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 504;BA.debugLine="End Sub";
return "";
}
public static String  _header_click() throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _send = null;
int _i = 0;
int _n = 0;
int _answ = 0;
String _txt = "";
boolean _set = false;
char _ch = '\0';
B4A.GPS.PaiNaiMa.main._gpslocation _gpsloc = null;
anywheresoftware.b4a.objects.LabelWrapper _lbl1 = null;
 //BA.debugLineNum = 824;BA.debugLine="Sub Header_Click";
 //BA.debugLineNum = 825;BA.debugLine="Dim Send As Label";
_send = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 826;BA.debugLine="Dim i, n, Answ As Int";
_i = 0;
_n = 0;
_answ = 0;
 //BA.debugLineNum = 827;BA.debugLine="Dim txt As String";
_txt = "";
 //BA.debugLineNum = 829;BA.debugLine="Send = Sender";
_send.setObject((android.widget.TextView)(anywheresoftware.b4a.keywords.Common.Sender(mostCurrent.activityBA)));
 //BA.debugLineNum = 831;BA.debugLine="Select Send.Tag";
switch (BA.switchObjectToInt(_send.getTag(),(Object)(2),(Object)(5),(Object)(6),(Object)(7),(Object)(8))) {
case 0: {
 //BA.debugLineNum = 833;BA.debugLine="Main.AltitudeUnitIndex = Main.AltitudeUnitIndex";
mostCurrent._main._altitudeunitindex = (int) (mostCurrent._main._altitudeunitindex+1);
 //BA.debugLineNum = 834;BA.debugLine="If Main.AltitudeUnitIndex > Main.AltitudeUnitNum";
if (mostCurrent._main._altitudeunitindex>mostCurrent._main._altitudeunitnumber-1) { 
 //BA.debugLineNum = 835;BA.debugLine="Main.AltitudeUnitIndex = 0";
mostCurrent._main._altitudeunitindex = (int) (0);
 };
 //BA.debugLineNum = 837;BA.debugLine="UpdateDispAltitude";
_updatedispaltitude();
 break; }
case 1: {
 //BA.debugLineNum = 839;BA.debugLine="Main.SpeedUnitIndex = Main.SpeedUnitIndex + 1";
mostCurrent._main._speedunitindex = (int) (mostCurrent._main._speedunitindex+1);
 //BA.debugLineNum = 840;BA.debugLine="If Main.SpeedUnitIndex > Main.SpeedUnitNumber -";
if (mostCurrent._main._speedunitindex>mostCurrent._main._speedunitnumber-1) { 
 //BA.debugLineNum = 841;BA.debugLine="Main.SpeedUnitIndex = 0";
mostCurrent._main._speedunitindex = (int) (0);
 };
 //BA.debugLineNum = 843;BA.debugLine="UpdateDispSpeed";
_updatedispspeed();
 break; }
case 2: 
case 3: {
 //BA.debugLineNum = 845;BA.debugLine="Main.DistanceUnitIndex = Main.DistanceUnitIndex";
mostCurrent._main._distanceunitindex = (int) (mostCurrent._main._distanceunitindex+1);
 //BA.debugLineNum = 846;BA.debugLine="If Main.DistanceUnitIndex > Main.DistanceUnitNum";
if (mostCurrent._main._distanceunitindex>mostCurrent._main._distanceunitnumber-1) { 
 //BA.debugLineNum = 847;BA.debugLine="Main.DistanceUnitIndex = 0";
mostCurrent._main._distanceunitindex = (int) (0);
 };
 //BA.debugLineNum = 849;BA.debugLine="UpdateDispDistance";
_updatedispdistance();
 break; }
case 4: {
 //BA.debugLineNum = 851;BA.debugLine="txt = \"ต้องการแสดง\" & CRLF";
_txt = "ต้องการแสดง"+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 852;BA.debugLine="txt = txt & \"หรือเอาเครื่องหมายออก ?\"";
_txt = _txt+"หรือเอาเครื่องหมายออก ?";
 //BA.debugLineNum = 853;BA.debugLine="Answ = Msgbox2(txt, \"โปรดเลือก\", \"แสดง\", \"ยกเลิก";
_answ = anywheresoftware.b4a.keywords.Common.Msgbox2(_txt,"โปรดเลือก","แสดง","ยกเลิก","เอาออก",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 854;BA.debugLine="Dim Set As Boolean";
_set = false;
 //BA.debugLineNum = 855;BA.debugLine="Dim Ch As Char";
_ch = '\0';
 //BA.debugLineNum = 856;BA.debugLine="If Answ = DialogResponse.POSITIVE Then";
if (_answ==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 857;BA.debugLine="Set = True";
_set = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 858;BA.debugLine="Ch = \"√\"";
_ch = BA.ObjectToChar("√");
 }else if(_answ==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
 //BA.debugLineNum = 860;BA.debugLine="Set = False";
_set = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 861;BA.debugLine="Ch = \"-\"";
_ch = BA.ObjectToChar("-");
 }else {
 //BA.debugLineNum = 863;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 865;BA.debugLine="For i = 0 To NumberOfRows - 1";
{
final int step39 = 1;
final int limit39 = (int) (_numberofrows-1);
for (_i = (int) (0) ; (step39 > 0 && _i <= limit39) || (step39 < 0 && _i >= limit39); _i = ((int)(0 + _i + step39)) ) {
 //BA.debugLineNum = 866;BA.debugLine="Dim GPSloc As GPSLocation";
_gpsloc = new B4A.GPS.PaiNaiMa.main._gpslocation();
 //BA.debugLineNum = 867;BA.debugLine="Dim lbl1 As Label";
_lbl1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 868;BA.debugLine="GPSloc = Main.GPSPath.Get(i)";
_gpsloc = (B4A.GPS.PaiNaiMa.main._gpslocation)(mostCurrent._main._gpspath.Get(_i));
 //BA.debugLineNum = 869;BA.debugLine="GPSloc.Marker = Set";
_gpsloc.Marker = _set;
 //BA.debugLineNum = 870;BA.debugLine="Main.GPSPath.Set(i, GPSloc)";
mostCurrent._main._gpspath.Set(_i,(Object)(_gpsloc));
 //BA.debugLineNum = 871;BA.debugLine="n = i * NumberOfColumns + 8";
_n = (int) (_i*_numberofcolumns+8);
 //BA.debugLineNum = 872;BA.debugLine="lbl1 = scvGPSPath1.Panel.GetView(n)";
_lbl1.setObject((android.widget.TextView)(mostCurrent._scvgpspath1.getPanel().GetView(_n).getObject()));
 //BA.debugLineNum = 873;BA.debugLine="lbl1.Text = Ch";
_lbl1.setText((Object)(_ch));
 }
};
 break; }
}
;
 //BA.debugLineNum = 876;BA.debugLine="End Sub";
return "";
}
public static String  _initgpspathdisplay() throws Exception{
int _i = 0;
anywheresoftware.b4a.objects.LabelWrapper _lbl = null;
 //BA.debugLineNum = 581;BA.debugLine="Sub InitGPSPathDisplay";
 //BA.debugLineNum = 583;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 585;BA.debugLine="CellLineWidth = 1dip";
_celllinewidth = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)));
 //BA.debugLineNum = 586;BA.debugLine="HeaderHeight = 38dip";
_headerheight = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (38)));
 //BA.debugLineNum = 587;BA.debugLine="RowHeight = 30dip";
_rowheight = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)));
 //BA.debugLineNum = 588;BA.debugLine="RowHeight_1 = RowHeight - CellLineWidth";
_rowheight_1 = (float) (_rowheight-_celllinewidth);
 //BA.debugLineNum = 590;BA.debugLine="scvGPSPath0.Top = HeaderHeight";
mostCurrent._scvgpspath0.setTop((int) (_headerheight));
 //BA.debugLineNum = 591;BA.debugLine="scvGPSPath0.Height = pnlGPSPath1.Height - HeaderH";
mostCurrent._scvgpspath0.setHeight((int) (mostCurrent._pnlgpspath1.getHeight()-_headerheight));
 //BA.debugLineNum = 592;BA.debugLine="scvGPSPath0.Enabled = False";
mostCurrent._scvgpspath0.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 594;BA.debugLine="scvGPSPath1.Top = scvGPSPath0.Top";
mostCurrent._scvgpspath1.setTop(mostCurrent._scvgpspath0.getTop());
 //BA.debugLineNum = 595;BA.debugLine="scvGPSPath1.Height = scvGPSPath0.Height";
mostCurrent._scvgpspath1.setHeight(mostCurrent._scvgpspath0.getHeight());
 //BA.debugLineNum = 596;BA.debugLine="skbGPSPath.Value = 0";
mostCurrent._skbgpspath.setValue((int) (0));
 //BA.debugLineNum = 598;BA.debugLine="CellAlignment = Gravity.CENTER_HORIZONTAL + Gravi";
_cellalignment = (int) (anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL+anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 599;BA.debugLine="CellTextSize = 14";
_celltextsize = (float) (14);
 //BA.debugLineNum = 600;BA.debugLine="CellColor = Colors.RGB(255,250,205)";
_cellcolor = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (255),(int) (250),(int) (205));
 //BA.debugLineNum = 601;BA.debugLine="SelectedRowColor = Colors.RGB(255,196,196)";
_selectedrowcolor = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (255),(int) (196),(int) (196));
 //BA.debugLineNum = 602;BA.debugLine="CellTextColor = Colors.RGB(165,42,42)";
_celltextcolor = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (165),(int) (42),(int) (42));
 //BA.debugLineNum = 603;BA.debugLine="CellLineColor = Colors.RGB(165,42,42)";
_celllinecolor = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (165),(int) (42),(int) (42));
 //BA.debugLineNum = 604;BA.debugLine="HeaderColor = Colors.Blue";
_headercolor = anywheresoftware.b4a.keywords.Common.Colors.Blue;
 //BA.debugLineNum = 605;BA.debugLine="HeaderLineColor = Colors.Yellow";
_headerlinecolor = anywheresoftware.b4a.keywords.Common.Colors.Yellow;
 //BA.debugLineNum = 606;BA.debugLine="HeaderTextColor = Colors.Yellow";
_headertextcolor = anywheresoftware.b4a.keywords.Common.Colors.Yellow;
 //BA.debugLineNum = 608;BA.debugLine="ColName0 = \"ลำดับ\"";
_colname0 = "ลำดับ";
 //BA.debugLineNum = 609;BA.debugLine="ColName(0) = \"ละติจูด\" & CRLF & \"[°]\"";
_colname[(int) (0)] = "ละติจูด"+anywheresoftware.b4a.keywords.Common.CRLF+"[°]";
 //BA.debugLineNum = 610;BA.debugLine="ColName(1) = \"ลองกิจูด\" & CRLF & \"[°]\"";
_colname[(int) (1)] = "ลองกิจูด"+anywheresoftware.b4a.keywords.Common.CRLF+"[°]";
 //BA.debugLineNum = 611;BA.debugLine="ColName(2) = \"ความสูง\" & CRLF & \"[\" & Main.Altitu";
_colname[(int) (2)] = "ความสูง"+anywheresoftware.b4a.keywords.Common.CRLF+"["+mostCurrent._main._altitudeunittext[mostCurrent._main._altitudeunitindex]+"]";
 //BA.debugLineNum = 612;BA.debugLine="ColName(3) = \"เวลา\" & CRLF & \"[s]\"";
_colname[(int) (3)] = "เวลา"+anywheresoftware.b4a.keywords.Common.CRLF+"[s]";
 //BA.debugLineNum = 613;BA.debugLine="ColName(4) = \"มุมทิศ\" & CRLF & \"[°]\"";
_colname[(int) (4)] = "มุมทิศ"+anywheresoftware.b4a.keywords.Common.CRLF+"[°]";
 //BA.debugLineNum = 614;BA.debugLine="ColName(5) = \"ความเร็ว\" & CRLF & \"[\" & Main.Speed";
_colname[(int) (5)] = "ความเร็ว"+anywheresoftware.b4a.keywords.Common.CRLF+"["+mostCurrent._main._speedunittext[mostCurrent._main._speedunitindex]+"]";
 //BA.debugLineNum = 615;BA.debugLine="ColName(6) = \"ระยะทาง\" & CRLF & \"[\" & Main.Distan";
_colname[(int) (6)] = "ระยะทาง"+anywheresoftware.b4a.keywords.Common.CRLF+"["+mostCurrent._main._distanceunittext[mostCurrent._main._distanceunitindex]+"]";
 //BA.debugLineNum = 616;BA.debugLine="ColName(7) = \"รวมระยะ\" & CRLF & \"[\" & Main.Distan";
_colname[(int) (7)] = "รวมระยะ"+anywheresoftware.b4a.keywords.Common.CRLF+"["+mostCurrent._main._distanceunittext[mostCurrent._main._distanceunitindex]+"]";
 //BA.debugLineNum = 617;BA.debugLine="ColName(8) = \"เครื่องหมาย\"";
_colname[(int) (8)] = "เครื่องหมาย";
 //BA.debugLineNum = 619;BA.debugLine="CellWidth0 = 40dip";
_cellwidth0 = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)));
 //BA.debugLineNum = 620;BA.debugLine="CellWidth0_1 = CellWidth0 - 2 * CellLineWidth";
_cellwidth0_1 = (float) (_cellwidth0-2*_celllinewidth);
 //BA.debugLineNum = 621;BA.debugLine="CellWidth(0) = 80dip";
_cellwidth[(int) (0)] = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)));
 //BA.debugLineNum = 622;BA.debugLine="CellWidth(1) = 80dip";
_cellwidth[(int) (1)] = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)));
 //BA.debugLineNum = 623;BA.debugLine="CellWidth(2) = 60dip";
_cellwidth[(int) (2)] = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 624;BA.debugLine="CellWidth(3) = 60dip";
_cellwidth[(int) (3)] = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 625;BA.debugLine="CellWidth(4) = 60dip";
_cellwidth[(int) (4)] = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 626;BA.debugLine="CellWidth(5) = 60dip";
_cellwidth[(int) (5)] = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 627;BA.debugLine="CellWidth(6) = 70dip";
_cellwidth[(int) (6)] = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (70)));
 //BA.debugLineNum = 628;BA.debugLine="CellWidth(7) = 70dip";
_cellwidth[(int) (7)] = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (70)));
 //BA.debugLineNum = 629;BA.debugLine="CellWidth(8) = 80dip";
_cellwidth[(int) (8)] = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)));
 //BA.debugLineNum = 631;BA.debugLine="CellWidthTotal(0) = 0";
_cellwidthtotal[(int) (0)] = (float) (0);
 //BA.debugLineNum = 633;BA.debugLine="pnlGPSPathHeader0.Initialize(\"\")";
mostCurrent._pnlgpspathheader0.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 634;BA.debugLine="pnlGPSPath0.AddView(pnlGPSPathHeader0, 0, 0, Cell";
mostCurrent._pnlgpspath0.AddView((android.view.View)(mostCurrent._pnlgpspathheader0.getObject()),(int) (0),(int) (0),(int) (_cellwidth0),(int) (_headerheight));
 //BA.debugLineNum = 635;BA.debugLine="pnlGPSPathHeader0.Color = HeaderLineColor";
mostCurrent._pnlgpspathheader0.setColor(_headerlinecolor);
 //BA.debugLineNum = 637;BA.debugLine="Dim lbl As Label";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 638;BA.debugLine="lbl.Initialize(\"Header\")";
_lbl.Initialize(mostCurrent.activityBA,"Header");
 //BA.debugLineNum = 639;BA.debugLine="lbl.Tag = -1";
_lbl.setTag((Object)(-1));
 //BA.debugLineNum = 640;BA.debugLine="lbl.Color = HeaderColor";
_lbl.setColor(_headercolor);
 //BA.debugLineNum = 641;BA.debugLine="lbl.TextColor = HeaderTextColor";
_lbl.setTextColor(_headertextcolor);
 //BA.debugLineNum = 642;BA.debugLine="lbl.TextSize = 14";
_lbl.setTextSize((float) (14));
 //BA.debugLineNum = 643;BA.debugLine="lbl.Typeface = Typeface.DEFAULT_BOLD";
_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 644;BA.debugLine="lbl.Gravity = CellAlignment";
_lbl.setGravity(_cellalignment);
 //BA.debugLineNum = 645;BA.debugLine="lbl.Text = ColName0";
_lbl.setText((Object)(_colname0));
 //BA.debugLineNum = 646;BA.debugLine="pnlGPSPathHeader0.AddView(lbl, 0, 0, CellWidth0_1";
mostCurrent._pnlgpspathheader0.AddView((android.view.View)(_lbl.getObject()),(int) (0),(int) (0),(int) (_cellwidth0_1),(int) (_headerheight));
 //BA.debugLineNum = 648;BA.debugLine="pnlGPSPathHeader1.Initialize(\"\")";
mostCurrent._pnlgpspathheader1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 649;BA.debugLine="pnlGPSPath1.AddView(pnlGPSPathHeader1, 0, 0, 100%";
mostCurrent._pnlgpspath1.AddView((android.view.View)(mostCurrent._pnlgpspathheader1.getObject()),(int) (0),(int) (0),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-_cellwidth0),(int) (_headerheight));
 //BA.debugLineNum = 650;BA.debugLine="pnlGPSPathHeader1.Color = HeaderLineColor";
mostCurrent._pnlgpspathheader1.setColor(_headerlinecolor);
 //BA.debugLineNum = 652;BA.debugLine="For i = 0 To NumberOfColumns_1";
{
final int step59 = 1;
final int limit59 = _numberofcolumns_1;
for (_i = (int) (0) ; (step59 > 0 && _i <= limit59) || (step59 < 0 && _i >= limit59); _i = ((int)(0 + _i + step59)) ) {
 //BA.debugLineNum = 653;BA.debugLine="CellWidth_1(i) = CellWidth(i) - CellLineWidth";
_cellwidth_1[_i] = (float) (_cellwidth[_i]-_celllinewidth);
 //BA.debugLineNum = 654;BA.debugLine="CellWidthTotal(i + 1) = CellWidthTotal(i) + Cell";
_cellwidthtotal[(int) (_i+1)] = (float) (_cellwidthtotal[_i]+_cellwidth[_i]);
 //BA.debugLineNum = 655;BA.debugLine="Dim lbl As Label";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 656;BA.debugLine="lbl.Initialize(\"Header\")";
_lbl.Initialize(mostCurrent.activityBA,"Header");
 //BA.debugLineNum = 657;BA.debugLine="lbl.Tag = i";
_lbl.setTag((Object)(_i));
 //BA.debugLineNum = 658;BA.debugLine="lbl.Color = HeaderColor";
_lbl.setColor(_headercolor);
 //BA.debugLineNum = 659;BA.debugLine="lbl.TextColor = HeaderTextColor";
_lbl.setTextColor(_headertextcolor);
 //BA.debugLineNum = 660;BA.debugLine="lbl.TextSize = 14";
_lbl.setTextSize((float) (14));
 //BA.debugLineNum = 661;BA.debugLine="lbl.Typeface = Typeface.DEFAULT_BOLD";
_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 662;BA.debugLine="lbl.Gravity = CellAlignment";
_lbl.setGravity(_cellalignment);
 //BA.debugLineNum = 663;BA.debugLine="lbl.Text = ColName(i)";
_lbl.setText((Object)(_colname[_i]));
 //BA.debugLineNum = 664;BA.debugLine="pnlGPSPathHeader1.AddView(lbl, CellWidthTotal(i)";
mostCurrent._pnlgpspathheader1.AddView((android.view.View)(_lbl.getObject()),(int) (_cellwidthtotal[_i]),(int) (0),(int) (_cellwidth_1[_i]),(int) (_headerheight));
 }
};
 //BA.debugLineNum = 666;BA.debugLine="pnlGPSPath1.Left= CellWidth0";
mostCurrent._pnlgpspath1.setLeft((int) (_cellwidth0));
 //BA.debugLineNum = 667;BA.debugLine="pnlGPSPath1.Width = CellWidthTotal(NumberOfColumn";
mostCurrent._pnlgpspath1.setWidth((int) (_cellwidthtotal[_numberofcolumns]));
 //BA.debugLineNum = 668;BA.debugLine="scvGPSPath1.Width = pnlGPSPath1.Width";
mostCurrent._scvgpspath1.setWidth(mostCurrent._pnlgpspath1.getWidth());
 //BA.debugLineNum = 669;BA.debugLine="pnlGPSPathHeader1.Width = pnlGPSPath1.Width";
mostCurrent._pnlgpspathheader1.setWidth(mostCurrent._pnlgpspath1.getWidth());
 //BA.debugLineNum = 670;BA.debugLine="skbGPSPath.Max = pnlGPSPath1.Width - Activity.Wid";
mostCurrent._skbgpspath.setMax((int) (mostCurrent._pnlgpspath1.getWidth()-mostCurrent._activity.getWidth()+_cellwidth0));
 //BA.debugLineNum = 671;BA.debugLine="End Sub";
return "";
}
public static String  _pnlgpspathfiltertoolbox_touch(int _action,float _x,float _y) throws Exception{
 //BA.debugLineNum = 490;BA.debugLine="Sub pnlGPSPathFilterToolBox_Touch(Action As Int, x";
 //BA.debugLineNum = 492;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 7;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 8;BA.debugLine="Dim Filter_On As Boolean							: Filter_On = Fals";
_filter_on = false;
 //BA.debugLineNum = 8;BA.debugLine="Dim Filter_On As Boolean							: Filter_On = Fals";
_filter_on = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 9;BA.debugLine="Dim Filtered_On As Boolean						: Filtered_On = F";
_filtered_on = false;
 //BA.debugLineNum = 9;BA.debugLine="Dim Filtered_On As Boolean						: Filtered_On = F";
_filtered_on = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 10;BA.debugLine="Dim EarthRadius As Double							: EarthRadius = 6";
_earthradius = 0;
 //BA.debugLineNum = 10;BA.debugLine="Dim EarthRadius As Double							: EarthRadius = 6";
_earthradius = 6371;
 //BA.debugLineNum = 11;BA.debugLine="Dim RowHeight As Float";
_rowheight = 0f;
 //BA.debugLineNum = 12;BA.debugLine="Dim RowHeight_1 As Float";
_rowheight_1 = 0f;
 //BA.debugLineNum = 13;BA.debugLine="Dim NumberOfColumns As Int						: NumberOfColumns";
_numberofcolumns = 0;
 //BA.debugLineNum = 13;BA.debugLine="Dim NumberOfColumns As Int						: NumberOfColumns";
_numberofcolumns = (int) (9);
 //BA.debugLineNum = 14;BA.debugLine="Dim NumberOfColumns_1 As Int					: NumberOfColumn";
_numberofcolumns_1 = 0;
 //BA.debugLineNum = 14;BA.debugLine="Dim NumberOfColumns_1 As Int					: NumberOfColumn";
_numberofcolumns_1 = (int) (_numberofcolumns-1);
 //BA.debugLineNum = 15;BA.debugLine="Dim NumberOfRows As Int								: NumberOfRows = 0";
_numberofrows = 0;
 //BA.debugLineNum = 15;BA.debugLine="Dim NumberOfRows As Int								: NumberOfRows = 0";
_numberofrows = (int) (0);
 //BA.debugLineNum = 16;BA.debugLine="Dim ColName0 As String";
_colname0 = "";
 //BA.debugLineNum = 17;BA.debugLine="Dim ColName(NumberOfColumns) As String";
_colname = new String[_numberofcolumns];
java.util.Arrays.fill(_colname,"");
 //BA.debugLineNum = 18;BA.debugLine="Dim CellLineWidth As Float";
_celllinewidth = 0f;
 //BA.debugLineNum = 19;BA.debugLine="Dim CellWidth0, CellWidth0_1 As Float";
_cellwidth0 = 0f;
_cellwidth0_1 = 0f;
 //BA.debugLineNum = 20;BA.debugLine="Dim CellWidth(NumberOfColumns) As Float";
_cellwidth = new float[_numberofcolumns];
;
 //BA.debugLineNum = 21;BA.debugLine="Dim CellWidth_1(NumberOfColumns)  As Float";
_cellwidth_1 = new float[_numberofcolumns];
;
 //BA.debugLineNum = 22;BA.debugLine="Dim CellWidthTotal(NumberOfColumns + 1)  As Float";
_cellwidthtotal = new float[(int) (_numberofcolumns+1)];
;
 //BA.debugLineNum = 23;BA.debugLine="Dim CellColor As Int";
_cellcolor = 0;
 //BA.debugLineNum = 24;BA.debugLine="Dim SelectedRowColor As Int";
_selectedrowcolor = 0;
 //BA.debugLineNum = 25;BA.debugLine="Dim SelectedRow As Int								: SelectedRow = -1";
_selectedrow = 0;
 //BA.debugLineNum = 25;BA.debugLine="Dim SelectedRow As Int								: SelectedRow = -1";
_selectedrow = (int) (-1);
 //BA.debugLineNum = 26;BA.debugLine="Dim CellAlignment As Int";
_cellalignment = 0;
 //BA.debugLineNum = 27;BA.debugLine="Dim CellTextSize As Float";
_celltextsize = 0f;
 //BA.debugLineNum = 28;BA.debugLine="Dim CellTextColor As Int";
_celltextcolor = 0;
 //BA.debugLineNum = 29;BA.debugLine="Dim CellLineColor As Int";
_celllinecolor = 0;
 //BA.debugLineNum = 30;BA.debugLine="Dim HeaderHeight As Float";
_headerheight = 0f;
 //BA.debugLineNum = 31;BA.debugLine="Dim HeaderColor As Int";
_headercolor = 0;
 //BA.debugLineNum = 32;BA.debugLine="Dim HeaderLineColor As Int";
_headerlinecolor = 0;
 //BA.debugLineNum = 33;BA.debugLine="Dim HeaderTextColor As Int";
_headertextcolor = 0;
 //BA.debugLineNum = 34;BA.debugLine="Type RowCol(Col As Int, Row As Int)";
;
 //BA.debugLineNum = 36;BA.debugLine="Dim GPSPathDelete As List";
_gpspathdelete = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 37;BA.debugLine="End Sub";
return "";
}
public static String  _scvgpspath1_scrollchanged(int _position) throws Exception{
 //BA.debugLineNum = 768;BA.debugLine="Sub scvGPSPath1_ScrollChanged(Position As Int)";
 //BA.debugLineNum = 769;BA.debugLine="scvGPSPath0.ScrollPosition = Position";
mostCurrent._scvgpspath0.setScrollPosition(_position);
 //BA.debugLineNum = 770;BA.debugLine="End Sub";
return "";
}
public static String  _skbgpspath_valuechanged(int _value,boolean _userchanged) throws Exception{
 //BA.debugLineNum = 276;BA.debugLine="Sub skbGPSPath_ValueChanged (Value As Int, UserCha";
 //BA.debugLineNum = 277;BA.debugLine="pnlGPSPath1.Left = CellWidth0 - Value";
mostCurrent._pnlgpspath1.setLeft((int) (_cellwidth0-_value));
 //BA.debugLineNum = 278;BA.debugLine="End Sub";
return "";
}
public static String  _tooltip(String _txt) throws Exception{
 //BA.debugLineNum = 143;BA.debugLine="Sub ToolTip(txt As String)";
 //BA.debugLineNum = 144;BA.debugLine="If txt = \"\" Then";
if ((_txt).equals("")) { 
 //BA.debugLineNum = 145;BA.debugLine="lblToolTip.Text = \"\"";
mostCurrent._lbltooltip.setText((Object)(""));
 //BA.debugLineNum = 146;BA.debugLine="lblToolTip.Visible = False";
mostCurrent._lbltooltip.setVisible(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 148;BA.debugLine="lblToolTip.Text = txt";
mostCurrent._lbltooltip.setText((Object)(_txt));
 //BA.debugLineNum = 149;BA.debugLine="lblToolTip.Visible = True";
mostCurrent._lbltooltip.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 151;BA.debugLine="End Sub";
return "";
}
public static String  _updatedispaltitude() throws Exception{
int _i = 0;
int _n = 0;
anywheresoftware.b4a.objects.LabelWrapper _lbl1 = null;
B4A.GPS.PaiNaiMa.main._gpslocation _gpsloc = null;
 //BA.debugLineNum = 898;BA.debugLine="Sub UpdateDispAltitude";
 //BA.debugLineNum = 899;BA.debugLine="Dim i, n As Int";
_i = 0;
_n = 0;
 //BA.debugLineNum = 901;BA.debugLine="Dim lbl1 As Label";
_lbl1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 902;BA.debugLine="lbl1 = pnlGPSPathHeader1.GetView(2)";
_lbl1.setObject((android.widget.TextView)(mostCurrent._pnlgpspathheader1.GetView((int) (2)).getObject()));
 //BA.debugLineNum = 903;BA.debugLine="lbl1.Text = \"ความสูง\" & CRLF & \"[\" & Main.Altitud";
_lbl1.setText((Object)("ความสูง"+anywheresoftware.b4a.keywords.Common.CRLF+"["+mostCurrent._main._altitudeunittext[mostCurrent._main._altitudeunitindex]+"]"));
 //BA.debugLineNum = 905;BA.debugLine="For i = 0 To NumberOfRows - 1";
{
final int step5 = 1;
final int limit5 = (int) (_numberofrows-1);
for (_i = (int) (0) ; (step5 > 0 && _i <= limit5) || (step5 < 0 && _i >= limit5); _i = ((int)(0 + _i + step5)) ) {
 //BA.debugLineNum = 906;BA.debugLine="Dim GPSloc As GPSLocation";
_gpsloc = new B4A.GPS.PaiNaiMa.main._gpslocation();
 //BA.debugLineNum = 907;BA.debugLine="GPSloc = Main.GPSPath.Get(i)";
_gpsloc = (B4A.GPS.PaiNaiMa.main._gpslocation)(mostCurrent._main._gpspath.Get(_i));
 //BA.debugLineNum = 908;BA.debugLine="Dim lbl1 As Label";
_lbl1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 909;BA.debugLine="n = i * NumberOfColumns + 2";
_n = (int) (_i*_numberofcolumns+2);
 //BA.debugLineNum = 910;BA.debugLine="lbl1 = scvGPSPath1.Panel.GetView(n)";
_lbl1.setObject((android.widget.TextView)(mostCurrent._scvgpspath1.getPanel().GetView(_n).getObject()));
 //BA.debugLineNum = 911;BA.debugLine="lbl1.Text = DispAltitude(GPSloc.Altitude)";
_lbl1.setText((Object)(_dispaltitude(_gpsloc.Altitude)));
 }
};
 //BA.debugLineNum = 913;BA.debugLine="End Sub";
return "";
}
public static String  _updatedispdistance() throws Exception{
int _i = 0;
int _n = 0;
anywheresoftware.b4a.objects.LabelWrapper _lbl1 = null;
anywheresoftware.b4a.objects.LabelWrapper _lbl2 = null;
B4A.GPS.PaiNaiMa.main._gpslocation _gpsloc = null;
 //BA.debugLineNum = 932;BA.debugLine="Sub UpdateDispDistance";
 //BA.debugLineNum = 933;BA.debugLine="Dim i, n As Int";
_i = 0;
_n = 0;
 //BA.debugLineNum = 935;BA.debugLine="Dim lbl1 As Label";
_lbl1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 936;BA.debugLine="lbl1 = pnlGPSPathHeader1.GetView(6)";
_lbl1.setObject((android.widget.TextView)(mostCurrent._pnlgpspathheader1.GetView((int) (6)).getObject()));
 //BA.debugLineNum = 937;BA.debugLine="Dim lbl2 As Label";
_lbl2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 938;BA.debugLine="lbl2 = pnlGPSPathHeader1.GetView(7)";
_lbl2.setObject((android.widget.TextView)(mostCurrent._pnlgpspathheader1.GetView((int) (7)).getObject()));
 //BA.debugLineNum = 940;BA.debugLine="lbl1.Text = \"ระยะทาง\" & CRLF & \"[\" & Main.Distanc";
_lbl1.setText((Object)("ระยะทาง"+anywheresoftware.b4a.keywords.Common.CRLF+"["+mostCurrent._main._distanceunittext[mostCurrent._main._distanceunitindex]+"]"));
 //BA.debugLineNum = 941;BA.debugLine="lbl2.Text = \"รวมระยะ\" & CRLF & \"[\" & Main.Distanc";
_lbl2.setText((Object)("รวมระยะ"+anywheresoftware.b4a.keywords.Common.CRLF+"["+mostCurrent._main._distanceunittext[mostCurrent._main._distanceunitindex]+"]"));
 //BA.debugLineNum = 942;BA.debugLine="For i = 0 To NumberOfRows - 1";
{
final int step8 = 1;
final int limit8 = (int) (_numberofrows-1);
for (_i = (int) (0) ; (step8 > 0 && _i <= limit8) || (step8 < 0 && _i >= limit8); _i = ((int)(0 + _i + step8)) ) {
 //BA.debugLineNum = 943;BA.debugLine="Dim GPSloc As GPSLocation";
_gpsloc = new B4A.GPS.PaiNaiMa.main._gpslocation();
 //BA.debugLineNum = 944;BA.debugLine="GPSloc = Main.GPSPath.Get(i)";
_gpsloc = (B4A.GPS.PaiNaiMa.main._gpslocation)(mostCurrent._main._gpspath.Get(_i));
 //BA.debugLineNum = 945;BA.debugLine="Dim lbl1 As Label";
_lbl1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 946;BA.debugLine="n = i * NumberOfColumns + 6";
_n = (int) (_i*_numberofcolumns+6);
 //BA.debugLineNum = 947;BA.debugLine="lbl1 = scvGPSPath1.Panel.GetView(n)";
_lbl1.setObject((android.widget.TextView)(mostCurrent._scvgpspath1.getPanel().GetView(_n).getObject()));
 //BA.debugLineNum = 948;BA.debugLine="lbl1.Text = DispDistance(GPSloc.Distance)";
_lbl1.setText((Object)(_dispdistance(_gpsloc.Distance)));
 //BA.debugLineNum = 949;BA.debugLine="Dim GPSloc As GPSLocation";
_gpsloc = new B4A.GPS.PaiNaiMa.main._gpslocation();
 //BA.debugLineNum = 950;BA.debugLine="GPSloc = Main.GPSPath.Get(i)";
_gpsloc = (B4A.GPS.PaiNaiMa.main._gpslocation)(mostCurrent._main._gpspath.Get(_i));
 //BA.debugLineNum = 951;BA.debugLine="Dim lbl1 As Label";
_lbl1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 952;BA.debugLine="n = i * NumberOfColumns + 7";
_n = (int) (_i*_numberofcolumns+7);
 //BA.debugLineNum = 953;BA.debugLine="lbl1 = scvGPSPath1.Panel.GetView(n)";
_lbl1.setObject((android.widget.TextView)(mostCurrent._scvgpspath1.getPanel().GetView(_n).getObject()));
 //BA.debugLineNum = 954;BA.debugLine="lbl1.Text = DispDistance(GPSloc.DistTot)";
_lbl1.setText((Object)(_dispdistance(_gpsloc.DistTot)));
 }
};
 //BA.debugLineNum = 956;BA.debugLine="End Sub";
return "";
}
public static String  _updatedispspeed() throws Exception{
int _i = 0;
int _n = 0;
anywheresoftware.b4a.objects.LabelWrapper _lbl1 = null;
B4A.GPS.PaiNaiMa.main._gpslocation _gpsloc = null;
 //BA.debugLineNum = 915;BA.debugLine="Sub UpdateDispSpeed";
 //BA.debugLineNum = 916;BA.debugLine="Dim i, n As Int";
_i = 0;
_n = 0;
 //BA.debugLineNum = 918;BA.debugLine="Dim lbl1 As Label";
_lbl1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 919;BA.debugLine="lbl1 = pnlGPSPathHeader1.GetView(5)";
_lbl1.setObject((android.widget.TextView)(mostCurrent._pnlgpspathheader1.GetView((int) (5)).getObject()));
 //BA.debugLineNum = 920;BA.debugLine="lbl1.Text = \"ความเร็ว\" & CRLF & \"[\" & Main.SpeedU";
_lbl1.setText((Object)("ความเร็ว"+anywheresoftware.b4a.keywords.Common.CRLF+"["+mostCurrent._main._speedunittext[mostCurrent._main._speedunitindex]+"]"));
 //BA.debugLineNum = 922;BA.debugLine="For i = 0 To NumberOfRows - 1";
{
final int step5 = 1;
final int limit5 = (int) (_numberofrows-1);
for (_i = (int) (0) ; (step5 > 0 && _i <= limit5) || (step5 < 0 && _i >= limit5); _i = ((int)(0 + _i + step5)) ) {
 //BA.debugLineNum = 923;BA.debugLine="Dim GPSloc As GPSLocation";
_gpsloc = new B4A.GPS.PaiNaiMa.main._gpslocation();
 //BA.debugLineNum = 924;BA.debugLine="GPSloc = Main.GPSPath.Get(i)";
_gpsloc = (B4A.GPS.PaiNaiMa.main._gpslocation)(mostCurrent._main._gpspath.Get(_i));
 //BA.debugLineNum = 925;BA.debugLine="Dim lbl1 As Label";
_lbl1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 926;BA.debugLine="n = i * NumberOfColumns + 5";
_n = (int) (_i*_numberofcolumns+5);
 //BA.debugLineNum = 927;BA.debugLine="lbl1 = scvGPSPath1.Panel.GetView(n)";
_lbl1.setObject((android.widget.TextView)(mostCurrent._scvgpspath1.getPanel().GetView(_n).getObject()));
 //BA.debugLineNum = 928;BA.debugLine="lbl1.Text = NumberFormat(GPSloc.Speed * Main.Spe";
_lbl1.setText((Object)(anywheresoftware.b4a.keywords.Common.NumberFormat(_gpsloc.Speed*mostCurrent._main._speedunitratio[mostCurrent._main._speedunitindex],(int) (1),(int) (2))));
 }
};
 //BA.debugLineNum = 930;BA.debugLine="End Sub";
return "";
}
}
