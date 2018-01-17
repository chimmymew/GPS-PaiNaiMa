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
	Dim edtGPSPathFilename, edtGPSPathComment As EditText
	Dim lblToolTip, lblGPSPathNbPoints As Label
	Dim btnGPSPathSaveGPP, btnGPSPathSaveKML As Panel
End Sub

Sub Activity_Create(FirstTime As Boolean)
	Activity.LoadLayout("gps_save")
	
	lblToolTip.Top = 0
	lblToolTip.Left = 0
	btnGPSPathSaveGPP.Top = Activity.Height - btnGPSPathSaveGPP.Height
	btnGPSPathSaveKML.Top = Activity.Height - btnGPSPathSaveKML.Height
End Sub

Sub Activity_Resume
	edtGPSPathFilename.Text = Main.GPSPathFilename
	edtGPSPathComment.Text = Main.GPSPathComment
	lblGPSPathNbPoints.Text = "  " & Main.GPSPath.Size
End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub btnGPSPathSaveGPP_Touch(Action As Int, x As Float, y As Float)
	Dim bmd As BitmapDrawable
	Dim i, Answ As Int
	Dim txt As String
	
	Select Action
	Case Activity.ACTION_DOWN
		ToolTip("บันทึกเป็นไฟล์ GPP")
		bmd.Initialize(LoadBitmap(File.DirAssets, "btnsavegpp1.png"))
		btnGPSPathSaveGPP.Background = bmd
	Case Activity.ACTION_UP
		bmd.Initialize(LoadBitmap(File.DirAssets, "btnsavegpp0.png"))
		btnGPSPathSaveGPP.Background = bmd
		ToolTip("")
		If x > 0 And x < btnGPSPathSaveGPP.Width  And y > 0 And y < btnGPSPathSaveGPP.Height Then
			Main.GPSPathFilename = edtGPSPathFilename.Text
			If Main.GPSPathFilename = "" Then
				Msgbox("ไม่มีชื่อ !", "ข้อความ")
				Return
			Else
				i = Main.GPSPathFilename.IndexOf(".")
				If i = -1 Then
					Main.GPSPathFilename = Main.GPSPathFilename & ".GPP"
				Else
					Main.GPSPathFilename = Main.GPSPathFilename.SubString2(0,i) & ".GPP"
				End If
			End If
			If File.Exists(Main.GPSDir, Main.GPSPathFilename) = True Then
				txt = "แฟ้ม " & Main.GPSPathFilename & " นั้นมีอยู่แล้ว !" & CRLF
				txt = txt & "ต้องการเขียนทับ ?"
				Answ = Msgbox2(txt, "ข้อความ", "ตกลง", "", "ยกเลิก", Null)
				If Answ = DialogResponse.POSITIVE Then
					Main.GPSPathComment = edtGPSPathComment.Text
					GPSModule.SavePath
					Activity.Finish
				Else
				End If
			Else
				Main.GPSPathComment = edtGPSPathComment.Text
				GPSModule.SavePath
				Activity.Finish
			End If
		End If
	End Select
End Sub

Sub btnGPSPathSaveKML_Touch(Action As Int, x As Float, y As Float)
	Dim bmd As BitmapDrawable
	Dim i, Answ As Int
	Dim txt As String
	Dim KLMFilename As String
	
	Select Action
	Case Activity.ACTION_DOWN
		ToolTip("บันทึกเป็นไฟล์ KML")
		bmd.Initialize(LoadBitmap(File.DirAssets, "btnsavekml1.png"))
		btnGPSPathSaveKML.Background = bmd
	Case Activity.ACTION_UP
		bmd.Initialize(LoadBitmap(File.DirAssets, "btnsavekml0.png"))
		btnGPSPathSaveKML.Background = bmd
		ToolTip("")
		If x > 0 And x < btnGPSPathSaveKML.Width  And y > 0 And y < btnGPSPathSaveKML.Height Then
			Dim i As Int
			i = Main.GPSPathFilename.IndexOf(".")
			If i > -1 Then
				Main.GPSPathFilenameKML = Main.GPSPathFilename.SubString2(0, i) & ".kml"
			Else
				Main.GPSPathFilenameKML = Main.GPSPathFilename	
			End If

			If File.Exists(File.DirRootExternal, Main.GPSPathFilenameKML) = True Then
				txt = "แฟ้ม " & Main.GPSPathFilenameKML & " นั้นมีอยู่แล้ว !" & CRLF
				txt = txt & "ต้องการเขียนทับหรือไม่ ?"
				Answ = Msgbox2(txt, "ข้อความ", "ตกลง", "", "ยกเลิก", Null)
				If Answ = DialogResponse.POSITIVE Then
					If Main.GPSPath.Size = 1 Then
						GPSModule.SaveKMLPoint
					Else
'						GPSModule.SaveKMLLine(8, Colors.Red, True, "Start", True, "Finish")
						GPSModule.SaveKMLLine(8, Colors.ARGB(127, 255, 0, 0), True, "Start", True, "Finish")
					End If
					Activity.Finish
				Else
				End If
			Else
				If Main.GPSPath.Size = 1 Then
					GPSModule.SaveKMLPoint
				Else
'					GPSModule.SaveKMLLine(8, Colors.Red, True, "Start", True, "Finish")
					GPSModule.SaveKMLLine(8, Colors.ARGB(127, 255, 0, 0), True, "Start", True, "Finish")
				End If
				Activity.Finish
			End If
		End If
	End Select
End Sub

Sub ToolTip(txt As String)
	If txt = "" Then
		lblToolTip.Text = ""
		lblToolTip.Visible = False
	Else
		lblToolTip.Text = txt
		lblToolTip.Visible = True
	End If
End Sub

