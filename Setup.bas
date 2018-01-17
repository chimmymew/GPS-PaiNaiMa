Type=Activity
Version=6.3
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region Module Attributes
	#FullScreen: False
	#IncludeTitle: True
#End Region

'Activity module
Sub Process_Globals

End Sub

Sub Globals
	Dim pnlMapMarkers, pnlMapDefaultLocation As Panel
	Dim cbxMapDraggable, cbxDispMapTypeControl, cbxDispMapZoomControl, cbxDispMapScaleControl As CheckBox
	Dim cbxDispMapCenterMarker, cbxDispMapMarkers, cbxDispMapPolyline As CheckBox
	Dim cbxMapMarkersClickable, cbxMapMarkersDragable, cbxShowGPSOnMap As CheckBox
	Dim cbxDrawGPSPath, cbxSaveGPSPath, cbxGPSDispSpeed, cbxGPSDispBearing, cbxGPSDispWindrose As CheckBox
	
	Dim edtMapDefaultLocationLng, edtMapDefaultLocationLat, edtGPSMinTime, edtGPSMinDistance As EditText
	
	Dim spnMapZoomLevel, spnMapLineWidth, spnMapLineColor, spnMapLineOpacity As Spinner
	Dim spnMapTypeControlID, spnMapZoomControlType As Spinner
	Dim spnAltitudeUnit, spnSpeedUnit, spnDistanceUnit As Spinner

	Dim scvSetup As ScrollView
End Sub

Sub Activity_Create(FirstTime As Boolean)
	scvSetup.Initialize(0)
	Activity.AddView(scvSetup, 0, 0, 100%x, 100%y)
	scvSetup.Color = Colors.RGB(255, 205, 250)
	scvSetup.Panel.LoadLayout("setup")
	scvSetup.Panel.Height = pnlMapMarkers.Top + pnlMapMarkers.Height
End Sub

Sub Activity_Resume
	InitSetup
	UpdateSetup
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	Main.GPSMinTime = edtGPSMinTime.Text * 1000		' time in milliseconds
	Main.GPSMinDistance = edtGPSMinDistance.Text
	Main.MapDefaultLat = edtMapDefaultLocationLat.Text
	Main.MapDefaultLng = edtMapDefaultLocationLng.Text
End Sub

Sub InitSetup
	' Initializes the Spinners
	Dim i As Int
	
	spnMapZoomLevel.Clear
	For i = 0 To 8
		spnMapZoomLevel.Add(i + 10)
	Next
	
	spnMapLineWidth.Clear
	spnMapLineWidth.Add(1)
	spnMapLineWidth.Add(2)
	spnMapLineWidth.Add(3)
	spnMapLineWidth.Add(5)

	spnMapLineColor.Clear
	spnMapLineColor.Add("Red")
	spnMapLineColor.Add("Blue")
	spnMapLineColor.Add("Green")
	
	spnMapLineOpacity.Clear
	spnMapLineOpacity.Add(0.25)
	spnMapLineOpacity.Add(0.5)
	spnMapLineOpacity.Add(0.75)
	spnMapLineOpacity.Add(1)
	
	spnAltitudeUnit.Clear
	For i = 0 To Main.AltitudeUnitNumber - 1
		spnAltitudeUnit.Add(Main.AltitudeUnitText(i))
	Next

	spnSpeedUnit.Clear
	For i = 0 To Main.SpeedUnitNumber - 1
		spnSpeedUnit.Add(Main.SpeedUnitText(i))
	Next

	spnDistanceUnit.Clear
	For i = 0 To Main.DistanceUnitNumber - 1
		spnDistanceUnit.Add(Main.DistanceUnitText(i))
	Next
	
	spnMapTypeControlID.Clear
	For i = 0 To Main.DispMapTypeControlIDNumber - 1
		spnMapTypeControlID.Add(Main.DispMapTypeControlIDText(i))
	Next
	
	spnMapZoomControlType.Clear
	For i = 0 To Main.DispMapZoomControlTypeNumber - 1
		spnMapZoomControlType.Add(Main.DispMapZoomControlTypeText(i))
	Next
End Sub

Sub UpdateSetup
	edtMapDefaultLocationLng.Text = Main.MapDefaultLng
	edtMapDefaultLocationLat.Text = Main.MapDefaultLat
	edtGPSMinTime.Text = Main.GPSMinTime / 1000		' time in milliseconds but diplayed in seconds
	edtGPSMinDistance.Text = Main.GPSMinDistance
	cbxMapDraggable.Checked = Main.MapSetDraggable
	cbxDispMapTypeControl.Checked = Main.DispMapTypeControl
	cbxGPSDispSpeed.Checked = Main.GPSDispSpeed
	cbxGPSDispBearing.Checked = Main.GPSDispBearing
	cbxGPSDispWindrose.Checked = Main.GPSDispWindrose
	cbxDispMapZoomControl.Checked = Main.DispMapZoomControl
	cbxDispMapScaleControl.Checked = Main.DispMapScaleControl
	cbxDispMapCenterMarker.Checked = Main.DispMapCenter
	cbxDispMapMarkers.Checked = Main.DispMapMarkers
	cbxDispMapPolyline.Checked = Main.DispMapPolylne
	cbxMapMarkersClickable.Checked = Main.MapMarkerClickable
	cbxMapMarkersDragable.Checked = Main.MapMarkerDragable
	cbxShowGPSOnMap.Checked = Main.ShowGPSOnMap
	cbxDrawGPSPath.Checked = Main.DrawGPSPath
	cbxSaveGPSPath.Checked = Main.SaveGPSPath

	spnMapZoomLevel.SelectedIndex = Main.MapDefaultZoomLevelIndex
	spnMapLineColor.SelectedIndex = Main.MapLineColorIndex
	spnMapLineWidth.SelectedIndex = Main.MapLineWidthIndex
	spnMapLineOpacity.SelectedIndex = Main.MapLineOpacityIndex
	spnAltitudeUnit.SelectedIndex = Main.AltitudeUnitIndex
	spnSpeedUnit.SelectedIndex = Main.SpeedUnitIndex
	spnDistanceUnit.SelectedIndex = Main.DistanceUnitIndex
	spnMapTypeControlID.SelectedIndex = Main.DispMapTypeControlIDIndex
	spnMapZoomControlType.SelectedIndex = Main.DispMapZoomControlTypeIndex
	spnMapTypeControlID.Visible = Main.DispMapTypeControl
	spnMapZoomControlType.Visible = Main.DispMapZoomControl
End Sub

Sub edtMapDefaultLocationLat_FocusChanged (HasFocus As Boolean)
	If HasFocus = True Then
		scvSetup.ScrollPosition = pnlMapDefaultLocation.Top
	End If
End Sub

Sub edtMapDefaultLocationLng_FocusChanged (HasFocus As Boolean)
	If HasFocus = True Then
		scvSetup.ScrollPosition = pnlMapDefaultLocation.Top
	End If
End Sub

Sub cbxMapDraggable_CheckedChange(Checked As Boolean)
	Main.MapSetDraggable = Checked
End Sub

Sub cbxDispMapTypeControl_CheckedChange(Checked As Boolean)
	Main.DispMapTypeControl = Checked
	spnMapTypeControlID.Visible = Main.DispMapTypeControl
End Sub

Sub cbxDispMapZoomControl_CheckedChange(Checked As Boolean)
	Main.DispMapZoomControl = Checked
	spnMapZoomControlType.Visible = Main.DispMapZoomControl
End Sub

Sub cbxDispMapScaleControl_CheckedChange(Checked As Boolean)
	Main.DispMapScaleControl = Checked
End Sub

Sub cbxDispMapCenterMarker_CheckedChange(Checked As Boolean)
	Main.DispMapCenter = Checked
End Sub

Sub cbxDispMapMarkers_CheckedChange(Checked As Boolean)
	Main.DispMapMarkers = Checked
End Sub

Sub cbxMapMarkersClickable_CheckedChange(Checked As Boolean)
	Main.MapMarkerClickable = Checked
End Sub

Sub cbxMapMarkersDragable_CheckedChange(Checked As Boolean)
	Main.MapMarkerDragable = Checked
End Sub

Sub cbxDispMapPolyline_CheckedChange(Checked As Boolean)
	Main.DispMapPolylne = Checked
End Sub

Sub cbxShowGPSOnMap_CheckedChange(Checked As Boolean)
	Main.ShowGPSOnMap = Checked
End Sub

Sub cbxDrawGPSPath_CheckedChange(Checked As Boolean)
	Main.DrawGPSPath = Checked
End Sub

Sub cbxSaveGPSPath_CheckedChange(Checked As Boolean)
	Main.SaveGPSPath = Checked
End Sub

Sub cbxGPSDispSpeed_CheckedChange(Checked As Boolean)
	Main.GPSDispSpeed = Checked
End Sub

Sub cbxGPSDispBearing_CheckedChange(Checked As Boolean)
	Main.GPSDispBearing = Checked
End Sub

Sub cbxGPSDispWindrose_CheckedChange(Checked As Boolean)
	Main.GPSDispWindrose = Checked
End Sub

Sub spnMapZoomLevel_ItemClick (Position As Int, Value As Object)
	Main.MapDefaultZoomLevel = Value
	Main.MapDefaultZoomLevelIndex = Position
End Sub

Sub spnAltitudeUnit_ItemClick (Position As Int, Value As Object)
	Main.AltitudeUnitIndex = Position
End Sub

Sub spnSpeedUnit_ItemClick (Position As Int, Value As Object)
	Main.SpeedUnitIndex = Position
End Sub

Sub spnDistanceUnit_ItemClick (Position As Int, Value As Object)
	Main.DistanceUnitIndex = Position
End Sub

Sub spnMapLineColor_ItemClick (Position As Int, Value As Object)
	Main.MapLineColorIndex = Position
	Select Main.MapLineColorIndex
	Case 0
		Main.MapLineColor = "ff0000"
	Case 1
		Main.MapLineColor = "0000ff"
	Case 2
		Main.MapLineColor = "00ff00"
	End Select
End Sub

Sub spnMapLineWidth_ItemClick (Position As Int, Value As Object)
	Main.MapLineWidthIndex = Position
	Main.MapLineWidth = Value
End Sub

Sub spnMapLineOpacity_ItemClick (Position As Int, Value As Object)
	Main.MapLineOpacityIndex = Position
	Main.MapLineOpacity = Value
End Sub

Sub spnMapTypeControlID_ItemClick (Position As Int, Value As Object)
	Main.DispMapTypeControlIDIndex = Position
	Main.DispMapTypeControlID = Value
End Sub

Sub spnMapZoomControlType_ItemClick (Position As Int, Value As Object)
	Main.DispMapZoomControlTypeIndex = Position
	Main.DispMapZoomControlType = Value
End Sub
