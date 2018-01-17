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

public class gpssave extends Activity implements B4AActivity{
	public static gpssave mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "B4A.GPS.PaiNaiMa", "B4A.GPS.PaiNaiMa.gpssave");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (gpssave).");
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
		activityBA = new BA(this, layout, processBA, "B4A.GPS.PaiNaiMa", "B4A.GPS.PaiNaiMa.gpssave");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "B4A.GPS.PaiNaiMa.gpssave", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (gpssave) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (gpssave) Resume **");
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
		return gpssave.class;
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
        BA.LogInfo("** Activity (gpssave) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (gpssave) Resume **");
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
public anywheresoftware.b4a.objects.EditTextWrapper _edtgpspathfilename = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edtgpspathcomment = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbltooltip = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblgpspathnbpoints = null;
public anywheresoftware.b4a.objects.PanelWrapper _btngpspathsavegpp = null;
public anywheresoftware.b4a.objects.PanelWrapper _btngpspathsavekml = null;
public B4A.GPS.PaiNaiMa.main _main = null;
public B4A.GPS.PaiNaiMa.satellites _satellites = null;
public B4A.GPS.PaiNaiMa.setup _setup = null;
public B4A.GPS.PaiNaiMa.gpspaths _gpspaths = null;
public B4A.GPS.PaiNaiMa.gpsmodule _gpsmodule = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 17;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 18;BA.debugLine="Activity.LoadLayout(\"gps_save\")";
mostCurrent._activity.LoadLayout("gps_save",mostCurrent.activityBA);
 //BA.debugLineNum = 20;BA.debugLine="lblToolTip.Top = 0";
mostCurrent._lbltooltip.setTop((int) (0));
 //BA.debugLineNum = 21;BA.debugLine="lblToolTip.Left = 0";
mostCurrent._lbltooltip.setLeft((int) (0));
 //BA.debugLineNum = 22;BA.debugLine="btnGPSPathSaveGPP.Top = Activity.Height - btnGPSP";
mostCurrent._btngpspathsavegpp.setTop((int) (mostCurrent._activity.getHeight()-mostCurrent._btngpspathsavegpp.getHeight()));
 //BA.debugLineNum = 23;BA.debugLine="btnGPSPathSaveKML.Top = Activity.Height - btnGPSP";
mostCurrent._btngpspathsavekml.setTop((int) (mostCurrent._activity.getHeight()-mostCurrent._btngpspathsavekml.getHeight()));
 //BA.debugLineNum = 24;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 32;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 34;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 26;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 27;BA.debugLine="edtGPSPathFilename.Text = Main.GPSPathFilename";
mostCurrent._edtgpspathfilename.setText((Object)(mostCurrent._main._gpspathfilename));
 //BA.debugLineNum = 28;BA.debugLine="edtGPSPathComment.Text = Main.GPSPathComment";
mostCurrent._edtgpspathcomment.setText((Object)(mostCurrent._main._gpspathcomment));
 //BA.debugLineNum = 29;BA.debugLine="lblGPSPathNbPoints.Text = \"  \" & Main.GPSPath.Siz";
mostCurrent._lblgpspathnbpoints.setText((Object)("  "+BA.NumberToString(mostCurrent._main._gpspath.getSize())));
 //BA.debugLineNum = 30;BA.debugLine="End Sub";
return "";
}
public static String  _btngpspathsavegpp_touch(int _action,float _x,float _y) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bmd = null;
int _i = 0;
int _answ = 0;
String _txt = "";
 //BA.debugLineNum = 36;BA.debugLine="Sub btnGPSPathSaveGPP_Touch(Action As Int, x As Fl";
 //BA.debugLineNum = 37;BA.debugLine="Dim bmd As BitmapDrawable";
_bmd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 38;BA.debugLine="Dim i, Answ As Int";
_i = 0;
_answ = 0;
 //BA.debugLineNum = 39;BA.debugLine="Dim txt As String";
_txt = "";
 //BA.debugLineNum = 41;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,mostCurrent._activity.ACTION_DOWN,mostCurrent._activity.ACTION_UP)) {
case 0: {
 //BA.debugLineNum = 43;BA.debugLine="ToolTip(\"บันทึกเป็นไฟล์ GPP\")";
_tooltip("บันทึกเป็นไฟล์ GPP");
 //BA.debugLineNum = 44;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btnsa";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnsavegpp1.png").getObject()));
 //BA.debugLineNum = 45;BA.debugLine="btnGPSPathSaveGPP.Background = bmd";
mostCurrent._btngpspathsavegpp.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 break; }
case 1: {
 //BA.debugLineNum = 47;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btnsa";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnsavegpp0.png").getObject()));
 //BA.debugLineNum = 48;BA.debugLine="btnGPSPathSaveGPP.Background = bmd";
mostCurrent._btngpspathsavegpp.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 //BA.debugLineNum = 49;BA.debugLine="ToolTip(\"\")";
_tooltip("");
 //BA.debugLineNum = 50;BA.debugLine="If x > 0 And x < btnGPSPathSaveGPP.Width  And y";
if (_x>0 && _x<mostCurrent._btngpspathsavegpp.getWidth() && _y>0 && _y<mostCurrent._btngpspathsavegpp.getHeight()) { 
 //BA.debugLineNum = 51;BA.debugLine="Main.GPSPathFilename = edtGPSPathFilename.Text";
mostCurrent._main._gpspathfilename = mostCurrent._edtgpspathfilename.getText();
 //BA.debugLineNum = 52;BA.debugLine="If Main.GPSPathFilename = \"\" Then";
if ((mostCurrent._main._gpspathfilename).equals("")) { 
 //BA.debugLineNum = 53;BA.debugLine="Msgbox(\"ไม่มีชื่อ !\", \"ข้อความ\")";
anywheresoftware.b4a.keywords.Common.Msgbox("ไม่มีชื่อ !","ข้อความ",mostCurrent.activityBA);
 //BA.debugLineNum = 54;BA.debugLine="Return";
if (true) return "";
 }else {
 //BA.debugLineNum = 56;BA.debugLine="i = Main.GPSPathFilename.IndexOf(\".\")";
_i = mostCurrent._main._gpspathfilename.indexOf(".");
 //BA.debugLineNum = 57;BA.debugLine="If i = -1 Then";
if (_i==-1) { 
 //BA.debugLineNum = 58;BA.debugLine="Main.GPSPathFilename = Main.GPSPathFilename &";
mostCurrent._main._gpspathfilename = mostCurrent._main._gpspathfilename+".GPP";
 }else {
 //BA.debugLineNum = 60;BA.debugLine="Main.GPSPathFilename = Main.GPSPathFilename.S";
mostCurrent._main._gpspathfilename = mostCurrent._main._gpspathfilename.substring((int) (0),_i)+".GPP";
 };
 };
 //BA.debugLineNum = 63;BA.debugLine="If File.Exists(Main.GPSDir, Main.GPSPathFilenam";
if (anywheresoftware.b4a.keywords.Common.File.Exists(mostCurrent._main._gpsdir,mostCurrent._main._gpspathfilename)==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 64;BA.debugLine="txt = \"แฟ้ม \" & Main.GPSPathFilename & \" นั้นม";
_txt = "แฟ้ม "+mostCurrent._main._gpspathfilename+" นั้นมีอยู่แล้ว !"+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 65;BA.debugLine="txt = txt & \"ต้องการเขียนทับ ?\"";
_txt = _txt+"ต้องการเขียนทับ ?";
 //BA.debugLineNum = 66;BA.debugLine="Answ = Msgbox2(txt, \"ข้อความ\", \"ตกลง\", \"\", \"ยก";
_answ = anywheresoftware.b4a.keywords.Common.Msgbox2(_txt,"ข้อความ","ตกลง","","ยกเลิก",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 67;BA.debugLine="If Answ = DialogResponse.POSITIVE Then";
if (_answ==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 68;BA.debugLine="Main.GPSPathComment = edtGPSPathComment.Text";
mostCurrent._main._gpspathcomment = mostCurrent._edtgpspathcomment.getText();
 //BA.debugLineNum = 69;BA.debugLine="GPSModule.SavePath";
mostCurrent._gpsmodule._savepath(mostCurrent.activityBA);
 //BA.debugLineNum = 70;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 }else {
 };
 }else {
 //BA.debugLineNum = 74;BA.debugLine="Main.GPSPathComment = edtGPSPathComment.Text";
mostCurrent._main._gpspathcomment = mostCurrent._edtgpspathcomment.getText();
 //BA.debugLineNum = 75;BA.debugLine="GPSModule.SavePath";
mostCurrent._gpsmodule._savepath(mostCurrent.activityBA);
 //BA.debugLineNum = 76;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 };
 };
 break; }
}
;
 //BA.debugLineNum = 80;BA.debugLine="End Sub";
return "";
}
public static String  _btngpspathsavekml_touch(int _action,float _x,float _y) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bmd = null;
int _i = 0;
int _answ = 0;
String _txt = "";
String _klmfilename = "";
 //BA.debugLineNum = 82;BA.debugLine="Sub btnGPSPathSaveKML_Touch(Action As Int, x As Fl";
 //BA.debugLineNum = 83;BA.debugLine="Dim bmd As BitmapDrawable";
_bmd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 84;BA.debugLine="Dim i, Answ As Int";
_i = 0;
_answ = 0;
 //BA.debugLineNum = 85;BA.debugLine="Dim txt As String";
_txt = "";
 //BA.debugLineNum = 86;BA.debugLine="Dim KLMFilename As String";
_klmfilename = "";
 //BA.debugLineNum = 88;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,mostCurrent._activity.ACTION_DOWN,mostCurrent._activity.ACTION_UP)) {
case 0: {
 //BA.debugLineNum = 90;BA.debugLine="ToolTip(\"บันทึกเป็นไฟล์ KML\")";
_tooltip("บันทึกเป็นไฟล์ KML");
 //BA.debugLineNum = 91;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btnsa";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnsavekml1.png").getObject()));
 //BA.debugLineNum = 92;BA.debugLine="btnGPSPathSaveKML.Background = bmd";
mostCurrent._btngpspathsavekml.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 break; }
case 1: {
 //BA.debugLineNum = 94;BA.debugLine="bmd.Initialize(LoadBitmap(File.DirAssets, \"btnsa";
_bmd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"btnsavekml0.png").getObject()));
 //BA.debugLineNum = 95;BA.debugLine="btnGPSPathSaveKML.Background = bmd";
mostCurrent._btngpspathsavekml.setBackground((android.graphics.drawable.Drawable)(_bmd.getObject()));
 //BA.debugLineNum = 96;BA.debugLine="ToolTip(\"\")";
_tooltip("");
 //BA.debugLineNum = 97;BA.debugLine="If x > 0 And x < btnGPSPathSaveKML.Width  And y";
if (_x>0 && _x<mostCurrent._btngpspathsavekml.getWidth() && _y>0 && _y<mostCurrent._btngpspathsavekml.getHeight()) { 
 //BA.debugLineNum = 98;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 99;BA.debugLine="i = Main.GPSPathFilename.IndexOf(\".\")";
_i = mostCurrent._main._gpspathfilename.indexOf(".");
 //BA.debugLineNum = 100;BA.debugLine="If i > -1 Then";
if (_i>-1) { 
 //BA.debugLineNum = 101;BA.debugLine="Main.GPSPathFilenameKML = Main.GPSPathFilename";
mostCurrent._main._gpspathfilenamekml = mostCurrent._main._gpspathfilename.substring((int) (0),_i)+".kml";
 }else {
 //BA.debugLineNum = 103;BA.debugLine="Main.GPSPathFilenameKML = Main.GPSPathFilename";
mostCurrent._main._gpspathfilenamekml = mostCurrent._main._gpspathfilename;
 };
 //BA.debugLineNum = 106;BA.debugLine="If File.Exists(File.DirRootExternal, Main.GPSPa";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._gpspathfilenamekml)==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 107;BA.debugLine="txt = \"แฟ้ม \" & Main.GPSPathFilenameKML & \" นั";
_txt = "แฟ้ม "+mostCurrent._main._gpspathfilenamekml+" นั้นมีอยู่แล้ว !"+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 108;BA.debugLine="txt = txt & \"ต้องการเขียนทับหรือไม่ ?\"";
_txt = _txt+"ต้องการเขียนทับหรือไม่ ?";
 //BA.debugLineNum = 109;BA.debugLine="Answ = Msgbox2(txt, \"ข้อความ\", \"ตกลง\", \"\", \"ยก";
_answ = anywheresoftware.b4a.keywords.Common.Msgbox2(_txt,"ข้อความ","ตกลง","","ยกเลิก",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 110;BA.debugLine="If Answ = DialogResponse.POSITIVE Then";
if (_answ==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 111;BA.debugLine="If Main.GPSPath.Size = 1 Then";
if (mostCurrent._main._gpspath.getSize()==1) { 
 //BA.debugLineNum = 112;BA.debugLine="GPSModule.SaveKMLPoint";
mostCurrent._gpsmodule._savekmlpoint(mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 115;BA.debugLine="GPSModule.SaveKMLLine(8, Colors.ARGB(127, 25";
mostCurrent._gpsmodule._savekmlline(mostCurrent.activityBA,(int) (8),(long) (anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (127),(int) (255),(int) (0),(int) (0))),anywheresoftware.b4a.keywords.Common.True,"Start",anywheresoftware.b4a.keywords.Common.True,"Finish");
 };
 //BA.debugLineNum = 117;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 }else {
 };
 }else {
 //BA.debugLineNum = 121;BA.debugLine="If Main.GPSPath.Size = 1 Then";
if (mostCurrent._main._gpspath.getSize()==1) { 
 //BA.debugLineNum = 122;BA.debugLine="GPSModule.SaveKMLPoint";
mostCurrent._gpsmodule._savekmlpoint(mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 125;BA.debugLine="GPSModule.SaveKMLLine(8, Colors.ARGB(127, 255";
mostCurrent._gpsmodule._savekmlline(mostCurrent.activityBA,(int) (8),(long) (anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (127),(int) (255),(int) (0),(int) (0))),anywheresoftware.b4a.keywords.Common.True,"Start",anywheresoftware.b4a.keywords.Common.True,"Finish");
 };
 //BA.debugLineNum = 127;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 };
 };
 break; }
}
;
 //BA.debugLineNum = 131;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 11;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 12;BA.debugLine="Dim edtGPSPathFilename, edtGPSPathComment As Edit";
mostCurrent._edtgpspathfilename = new anywheresoftware.b4a.objects.EditTextWrapper();
mostCurrent._edtgpspathcomment = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 13;BA.debugLine="Dim lblToolTip, lblGPSPathNbPoints As Label";
mostCurrent._lbltooltip = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lblgpspathnbpoints = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Dim btnGPSPathSaveGPP, btnGPSPathSaveKML As Panel";
mostCurrent._btngpspathsavegpp = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._btngpspathsavekml = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 15;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 7;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="End Sub";
return "";
}
public static String  _tooltip(String _txt) throws Exception{
 //BA.debugLineNum = 133;BA.debugLine="Sub ToolTip(txt As String)";
 //BA.debugLineNum = 134;BA.debugLine="If txt = \"\" Then";
if ((_txt).equals("")) { 
 //BA.debugLineNum = 135;BA.debugLine="lblToolTip.Text = \"\"";
mostCurrent._lbltooltip.setText((Object)(""));
 //BA.debugLineNum = 136;BA.debugLine="lblToolTip.Visible = False";
mostCurrent._lbltooltip.setVisible(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 138;BA.debugLine="lblToolTip.Text = txt";
mostCurrent._lbltooltip.setText((Object)(_txt));
 //BA.debugLineNum = 139;BA.debugLine="lblToolTip.Visible = True";
mostCurrent._lbltooltip.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 141;BA.debugLine="End Sub";
return "";
}
}
