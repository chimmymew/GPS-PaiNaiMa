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
	Dim lblSatellitesNb, lblSatellitesIndex, lblSatellitesAzimuth As Label
	Dim lblSatellitesElevation, lblSatellitesUsed, lblSatellitesS_N As Label
	Dim Button1 As Button
	Dim Sats As List
End Sub

Sub Activity_Create(FirstTime As Boolean)
	Activity.LoadLayout("satellites")
End Sub

Sub Activity_Resume
	UpdateSatellites
	Main.GPS1.Start(0, 0)
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	If Main.GPS_On = False Then 
		Main.GPS1.Stop
	End If
End Sub

Sub GPS1_GpsStatus (Satellites As List)
	Dim i As Int
	Dim txt1, txt2, txt3, txt4, txt5 As String
	
	txt1 = "ลำดับ"
	txt2 = "มุมเงย"
	txt3 = "ขั้นความสูง"
	txt4 = "ใข้งาน"
	txt5 = "การรบกวน"
	lblSatellitesNb.Text = "จำนวนดาวเทียมที่พบ: " & Satellites.Size
	Activity.Title = "ดาวเทียม " & Satellites.Size
	For i = 0 To Satellites.Size -1
		Dim GPSSat As GPSSatellite
		GPSSat = Satellites.Get(i)
		txt1 = txt1 & CRLF & (i + 1) 
		txt2 = txt2 & CRLF & GPSSat.Azimuth
		txt3 = txt3 & CRLF & GPSSat.Elevation
		txt4 = txt4 & CRLF & GPSSat.UsedInFix
		txt5 = txt5 & CRLF & NumberFormat(GPSSat.Snr, 1, 2)
		Activity.Title = "ดาวเทียมทั้งหมด " & i
	Next
	lblSatellitesIndex.Text = txt1
	lblSatellitesAzimuth.Text = txt2
	lblSatellitesElevation.Text = txt3
	lblSatellitesUsed.Text = txt4
	lblSatellitesS_N.Text = txt5
End Sub

Sub UpdateSatellites
	If Main.GPS1.GPSEnabled = False Then
		ToastMessageShow("โปรดเปิดจีพีเอส", True)
		StartActivity(Main.GPS1.LocationSettingsIntent)
	End If
End Sub
