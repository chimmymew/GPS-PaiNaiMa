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
	Dim Filter_On As Boolean							: Filter_On = False
	Dim Filtered_On As Boolean						: Filtered_On = False
	Dim EarthRadius As Double							: EarthRadius = 6371	' km
	Dim RowHeight As Float
	Dim RowHeight_1 As Float 
	Dim NumberOfColumns As Int						: NumberOfColumns = 9
	Dim NumberOfColumns_1 As Int					: NumberOfColumns_1 = NumberOfColumns - 1
	Dim NumberOfRows As Int								: NumberOfRows = 0
	Dim ColName0 As String
	Dim ColName(NumberOfColumns) As String
	Dim CellLineWidth As Float
	Dim CellWidth0, CellWidth0_1 As Float
	Dim CellWidth(NumberOfColumns) As Float
	Dim CellWidth_1(NumberOfColumns)  As Float
	Dim CellWidthTotal(NumberOfColumns + 1)  As Float
	Dim CellColor As Int
	Dim SelectedRowColor As Int
	Dim SelectedRow As Int								: SelectedRow = -1  ' unselected
	Dim CellAlignment As Int
	Dim CellTextSize As Float
	Dim CellTextColor As Int
	Dim CellLineColor As Int
	Dim HeaderHeight As Float
	Dim HeaderColor As Int
	Dim HeaderLineColor As Int
	Dim HeaderTextColor As Int
	Type RowCol(Col As Int, Row As Int)
	
	Dim GPSPathDelete As List
End Sub

Sub Globals
	Dim btnGPSPathLoad, btnGPSPathSave, btnGPSPathDeletePoint, btnGPSPathDeleteFile As Panel
	Dim btnGPSPathFilter, pnlGPSPathFilterDel, btnGPSPathFilterGo, btnGPSPathFilterDel As Panel
	Dim pnlGPSPath0, pnlGPSPath1,  pnlGPSPathToolbox, pnlGPSPathHeader0, pnlGPSPathHeader1 As Panel
	Dim pnlGPSPathFilter, pnlGPSPathFilterToolBox As Panel
	
	Dim edtGPSPathFilterMinDist As EditText
	
	Dim lblGPSPathComment, lblGPSPathDateTime, lblGPSPathDate, lblGPSPathTime As Label
	Dim lblToolTip, lblGPSPathFile, lblGPSPathNbPoints, lblGPSPathNbPoints1 As Label
	Dim lblGPSPathFilterNb0, lblGPSPathFilterNb1 As Label
	
	Dim scvGPSPath0, scvGPSPath1 As ScrollView

	Dim skbGPSPath As SeekBar
	Dim Dialog1 As FileDialog
	
	Dim GPSFilterIndexes As List
End Sub

Sub Activity_Create(FirstTime As Boolean)
	
	Activity.LoadLayout("gps_path")
	Activity.Title = "แสดงเส้นทาง"

	lblToolTip.Top = 0
	lblToolTip.Left = 0
	
	pnlGPSPathFilter.Top = 0
	pnlGPSPathFilter.Left = 0
	
	pnlGPSPath0.Top = lblGPSPathDateTime.Top + lblGPSPathDateTime.Height
	pnlGPSPathToolbox.Left = 0
	pnlGPSPathToolbox.Top = Activity.Height - pnlGPSPathToolbox.Height
	
	pnlGPSPathFilterToolBox.Top = pnlGPSPathToolbox.Top
	pnlGPSPathFilterToolBox.Left = pnlGPSPathToolbox.Left
	
	pnlGPSPath0.Height = pnlGPSPathToolbox.Top - lblGPSPathDate.Top - lblGPSPathDate.Height - skbGPSPath.Height
	pnlGPSPath0.Color = Colors.RGB(165,42,42)
	pnlGPSPath1.Top = pnlGPSPath0.Top
	pnlGPSPath1.Height = pnlGPSPath0.Height
	pnlGPSPath1.Color = Colors.RGB(165,42,42)
	skbGPSPath.Top = pnlGPSPathToolbox.Top - skbGPSPath.Height
	
	If Main.GPSPathFilename = "" Then
		btnGPSPathSave.Visible = False
		btnGPSPathDeletePoint.Visible = False
		btnGPSPathDeleteFile.Visible = False
		lblGPSPathFile.Text = "  ---"
		lblGPSPathNbPoints1.Text = "---"
		lblGPSPathComment.Text = "  ---"
		lblGPSPathDate.Text = "  ---"
		lblGPSPathTime.Text = "  ---"
	Else
		btnGPSPathSave.Visible = True
		btnGPSPathDeleteFile.Visible = True
		If SelectedRow >= 0 Then
			btnGPSPathDeletePoint.Visible = True
		Else
			btnGPSPathDeletePoint.Visible = False
		End If
		lblGPSPathFile.Text = "  " & Main.GPSPathFilename
		lblGPSPathComment.Text = "  " & Main.GPSPathComment
		lblGPSPathNbPoints1.Text = Main.GPSPath.Size & " จุด" 
		If Main.GPSPathTime0 > 0 Then
			lblGPSPathDate.Text = "  " & DateTime.Date(Main.GPSPathTime0)
			lblGPSPathTime.Text = "  " & DateTime.Time(Main.GPSPathTime0)
		Else
			lblGPSPathDate.Text = "  ---"
			lblGPSPathTime.Text = "  ---"
		End If
	End If
	
	lblToolTip.BringToFront
	InitGPSPathDisplay
	FillGPSPathTable
	
	If Not(GPSFilterIndexes.IsInitialized) Then
		GPSFilterIndexes.Initialize
	End If
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub Activity_KeyPress (KeyCode As Int) As Boolean 'Return True to consume the event
	If Filter_On = True Then
		pnlGPSPathFilter.Visible = False
		pnlGPSPathFilterToolBox.Visible = False
		Main.GPSFilterDelta = edtGPSPathFilterMinDist.Text
		If Filtered_On Then
			GPSPathUnFilter
		End If
		Filter_On = False
		Return True
	End If
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

Sub btnGPSPathSave_Touch(Action As Int, x As Float, y As Float)
	Dim bmd As BitmapDrawable
	Dim Send As Panel
	Dim i, Answ As Int
	Dim txt As String
	
	Send = Sender
	
	Select Action
	Case Activity.ACTION_DOWN
		ToolTip("บันทึก")
		bmd.Initialize(LoadBitmap(File.DirAssets, "btnsave1.png"))
		Send.Background = bmd
	Case Activity.ACTION_UP
		bmd.Initialize(LoadBitmap(File.DirAssets, "btnsave0.png"))
		Send.Background = bmd
		ToolTip("")
		If x > 0 And x < Send.Width  And y > 0 And y < Send.Height Then
			StartActivity("GPSSave")
		End If
	End Select
End Sub

Sub btnGPSPathLoad_Touch(Action As Int, x As Float, y As Float)
	Dim Answ, i1, i2 As Int
	Dim txt As String
	Dim Pos As GPSLocation
	Dim bmd As BitmapDrawable
	
	Select Action
	Case Activity.ACTION_DOWN
		ToolTip("เปิดเส้นทาง")
		bmd.Initialize(LoadBitmap(File.DirAssets, "btnload1.png"))
		btnGPSPathLoad.Background = bmd
	Case Activity.ACTION_UP
		If x > 0 And x < btnGPSPathLoad.Width  And y > 0 And y < btnGPSPathLoad.Height Then
			Dialog1.FilePath = Main.GPSDir
			Dialog1.FileFilter = ".GPP"
			Answ = Dialog1.Show("แฟ้ม", "เปิด", "ยกเลิก", "", Null)
			If Answ = DialogResponse.POSITIVE Then
				txt = Dialog1.ChosenName
				If txt.EndsWith(".GPP") = True Then
					Main.GPSPathFilename = txt
					GPSModule.LoadPath
					Main.MapZoomCalculated = False
					ClearTable
					FillGPSPathTable
					lblGPSPathFile.Text = "  " & Main.GPSPathFilename
					lblGPSPathComment.Text = "  " & Main.GPSPathComment
					lblGPSPathNbPoints1.Text = Main.GPSPath.Size & " จุด"
					If Main.GPSPathTime0 > 0 Then
						lblGPSPathDate.Text = "  " & DateTime.Date(Main.GPSPathTime0)
						lblGPSPathTime.Text = "  " & DateTime.Time(Main.GPSPathTime0)
					Else
						lblGPSPathDate.Text = "  ---"
						lblGPSPathTime.Text = "  ---"
					End If
				Else
					Return
				End If
			End If
		End If
		bmd.Initialize(LoadBitmap(File.DirAssets, "btnload0.png"))
		btnGPSPathLoad.Background = bmd
		ToolTip("")
	End Select
End Sub

Sub btnGPSPathDeletePoint_Touch(Action As Int, x As Float, y As Float)
	Dim bmd As BitmapDrawable
	Dim Answ As Int
	Dim txt As String
	
	Select Action
	Case Activity.ACTION_DOWN
		ToolTip("ลบจุดที่เลือก")
		bmd.Initialize(LoadBitmap(File.DirAssets, "btndeletepoint1.png"))
		btnGPSPathDeletePoint.Background = bmd
	Case Activity.ACTION_UP
		If x > 0 And x < btnGPSPathDeletePoint.Width  And y > 0 And y < btnGPSPathDeletePoint.Height Then
			txt = "ต้องการลบ" & CRLF
			txt = txt & "จุด " & SelectedRow & " ?"
			Answ = Msgbox2(txt, "โปรดเลือก", "ตกลง", "", "ยกเลิก", Null)
			If Answ = DialogResponse.POSITIVE Then
				GPSPathDeletePoint
			End If
		End If
		bmd.Initialize(LoadBitmap(File.DirAssets, "btndeletepoint0.png"))
		btnGPSPathDeletePoint.Background = bmd
		ToolTip("")
	End Select
End Sub

Sub btnGPSPathDeleteFile_Touch(Action As Int, x As Float, y As Float)
	Dim bmd As BitmapDrawable
	Dim Answ As Int
	Dim txt As String
	
	Select Action
	Case Activity.ACTION_DOWN
		ToolTip("ลบแฟ้มปัจจุบัน")
		bmd.Initialize(LoadBitmap(File.DirAssets, "btndeletefile1.png"))
		btnGPSPathDeleteFile.Background = bmd
	Case Activity.ACTION_UP
		If x > 0 And x < btnGPSPathDeleteFile.Width  And y > 0 And y < btnGPSPathDeleteFile.Height Then
			txt = "ต้องการลบ" & CRLF
			txt = txt & "แฟ้ม " & Main.GPSPathFilename & " ?"
			Answ = Msgbox2(txt, "โปรดเลือก", "ตกลง", "", "ยกเลิก", Null)
			If Answ = DialogResponse.POSITIVE Then
				File.Delete(Main.GPSDir, Main.GPSPathFilename)
				Main.GPSPathFilename = ""
				lblGPSPathFile.Text = "  " & Main.GPSPathFilename
				ClearTable
				Main.MapZoomCalculated = False
				btnGPSPathDeleteFile.Visible = False
			End If
		End If
		bmd.Initialize(LoadBitmap(File.DirAssets, "btndeletefile0.png"))
		btnGPSPathDeleteFile.Background = bmd
		ToolTip("")
	End Select
End Sub

Sub skbGPSPath_ValueChanged (Value As Int, UserChanged As Boolean)
	pnlGPSPath1.Left = CellWidth0 - Value
End Sub

Sub GPSPathDeletePoint
	Dim i, j, m, m1, n, n1 As Int
	Dim txt As String
	
	' removes the point from the list
	Main.GPSPath.RemoveAt(SelectedRow)
	
	If SelectedRow = NumberOfRows - 1 Then
		' if last row
		n = (NumberOfRows - 1) * NumberOfColumns
		For i = NumberOfColumns - 2 To 0 Step -1			'*****
			scvGPSPath1.Panel.RemoveViewAt(n + i)
		Next
		SelectedRow = -1
		NumberOfRows = NumberOfRows - 1
		scvGPSPath0.Panel.Height = NumberOfRows * RowHeight
		scvGPSPath1.Panel.Height = NumberOfRows * RowHeight
		Return
	End If
	
	' moves, in the table, the cell values one line up 
	For i = SelectedRow To NumberOfRows - 2
		m = i * NumberOfColumns
		m1 = (i + 1) * NumberOfColumns
		For j = 0 To NumberOfColumns_1
			n = m + j
			n1 = m1 + j
			Dim lbl, lbl1 As Label
			lbl =	scvGPSPath1.Panel.GetView(n)
			lbl1 =	scvGPSPath1.Panel.GetView(n1)
			Dim GPSloc1 As GPSLocation
			GPSloc1.Initialize
			GPSloc1 = Main.GPSPath.Get(i)
			lbl.Text = lbl1.Text
		Next
	Next
	' removes the views of tje last line in the table
	n = (NumberOfRows - 1) * NumberOfColumns
	For i = NumberOfColumns_1 To 0 Step -1
		scvGPSPath1.Panel.RemoveViewAt(n + i)
	Next
	
	' updates the values of Time, Distance, DistTot, Speed and Bearing
	If SelectedRow = 0 Then
		Dim GPSloc1 As GPSLocation
		GPSloc1.Initialize
		GPSloc1 = Main.GPSPath.Get(0)
		GPSloc1.Distance = 0
		GPSloc1.DistTot = 0
		GPSloc1.Speed = 0
		Main.GPSPath.Set(SelectedRow, GPSloc1)
		Dim lbl As Label
		lbl = scvGPSPath1.Panel.GetView(6)
		lbl.Text = GPSloc1.Distance
		Dim lbl As Label
		lbl = scvGPSPath1.Panel.GetView(7)
		lbl.Text = GPSloc1.DistTot
		Dim lbl As Label
		lbl = scvGPSPath1.Panel.GetView(5)
		lbl.Text = GPSloc1.Speed
		Main.GPSPathTime0 = GPSloc1.Time
		For i = 0 To NumberOfRows - 2
			Dim GPSloc1 As GPSLocation
			GPSloc1.Initialize
			GPSloc1 = Main.GPSPath.Get(i)
			Dim lbl As Label
			n = i * NumberOfColumns + 3
			lbl = scvGPSPath1.Panel.GetView(n)
			Log(i & " : " & GPSloc1.Time)
			lbl.Text = (GPSloc1.Time - Main.GPSPathTime0) 
		Next
	Else
		Dim GPSloc1, GPSloc2 As GPSLocation
		Dim loc1, loc2 As Location
		GPSloc1.Initialize
		GPSloc2.Initialize
		loc1.Initialize
		loc2.Initialize
		GPSloc1 = Main.GPSPath.Get(SelectedRow - 1)
		loc1.Latitude = GPSloc1.Latitude
		loc1.Longitude = GPSloc1.Longitude
		GPSloc2 = Main.GPSPath.Get(SelectedRow)
		loc2.Latitude = GPSloc2.Latitude
		loc2.Longitude = GPSloc2.Longitude
		GPSloc2.Distance = NumberFormat(loc1.DistanceTo(loc2), 1, 3)
		GPSloc2.Bearing = NumberFormat(loc1.BearingTo(loc2), 1 ,1)
		GPSloc2.Speed =  NumberFormat(GPSloc2.Distance / (GPSloc2.Time - GPSloc1.Time), 1, 1)
		Main.GPSPath.Set(SelectedRow, GPSloc2)
		n = SelectedRow * NumberOfColumns + 6
		Dim lbl As Label
		lbl = scvGPSPath1.Panel.GetView(n)
		lbl.Text = DispDistance(GPSloc2.Distance)
		n = SelectedRow * NumberOfColumns + 5
		Dim lbl As Label
		lbl = scvGPSPath1.Panel.GetView(n)
		lbl.Text = GPSloc2.Speed
	End If
	
	' updates DistTot in all rows after the selected one
	For i = SelectedRow To NumberOfRows - 2
		GPSloc2 = Main.GPSPath.Get(i)
		GPSloc2.DistTot = NumberFormat(GPSloc1.DistTot + GPSloc2.Distance, 1, 3)
		n = i * NumberOfColumns + 7
		lbl = scvGPSPath1.Panel.GetView(n)
		lbl.Text = DispDistance(GPSloc2.DistTot)
		GPSloc1 = GPSloc2
	Next
	
	' unselects the row and changes its color
	If SelectedRow < NumberOfRows - 1 Then
		ChangeCellSelection(SelectedRow, 0)	
	End If
	SelectedRow = -1
	
	NumberOfRows = NumberOfRows - 1
	scvGPSPath0.Panel.Height = NumberOfRows * RowHeight
	scvGPSPath1.Panel.Height = NumberOfRows * RowHeight
End Sub

Sub btnGPSPathFilter_Touch(Action As Int, x As Float, y As Float)
	Dim bmd As BitmapDrawable
	Dim Answ As Int
	
	Select Action
	Case Activity.ACTION_DOWN
		ToolTip("กรองจุดเส้นทาง")
		bmd.Initialize(LoadBitmap(File.DirAssets, "btngpsfilter1.png"))
		btnGPSPathFilter.Background = bmd
	Case Activity.ACTION_UP
		If x > 0 And x < btnGPSPathFilter.Width  And y > 0 And y < btnGPSPathFilter.Height Then
			Filter_On = Not(Filter_On)
		End If
		If Filter_On = True Then
			pnlGPSPathFilter.Visible = True
			pnlGPSPathFilterToolBox.Visible = True
			edtGPSPathFilterMinDist.Text = Main.GPSFilterDelta
			If Filtered_On Then
				GPSPathFilter
			Else
				lblGPSPathFilterNb0.Text = "จำนวนจุด"
				lblGPSPathFilterNb1.Text = Main.GPSPath.Size
			End If		
		End If
		bmd.Initialize(LoadBitmap(File.DirAssets, "btngpsfilter0.png"))
		btnGPSPathFilter.Background = bmd
		ToolTip("")
	End Select
End Sub

Sub btnGPSPathFilterGo_Touch(Action As Int, x As Float, y As Float)
	Dim bmd As BitmapDrawable
	
	Select Action
	Case Activity.ACTION_DOWN
		If Filtered_On = False Then
			ToolTip("กรองเส้นทางปัจจุบัน")
			bmd.Initialize(LoadBitmap(File.DirAssets, "btngpsfilter1.png"))
		Else
			ToolTip("ยกเลิกตัวกรอง")
			bmd.Initialize(LoadBitmap(File.DirAssets, "btngpsfilter0.png"))
		End If
	Case Activity.ACTION_UP
		If x > 0 And x < btnGPSPathFilterGo.Width  And y > 0 And y < btnGPSPathFilterGo.Height Then
			Filtered_On = Not(Filtered_On)
		End If
		If Filtered_On = False Then
			bmd.Initialize(LoadBitmap(File.DirAssets, "btngpsfilter0.png"))
			GPSPathUnFilter
		Else
			bmd.Initialize(LoadBitmap(File.DirAssets, "btngpsfilter1.png"))
			Main.GPSFilterDelta = edtGPSPathFilterMinDist.Text
			Main.GPSFilterDelta = Main.GPSFilterDelta / 1000
			GPSPathFilter
		End If
		btnGPSPathFilterGo.Background = bmd
		btnGPSPathFilterDel.Visible = Filtered_On
		ToolTip("")
	End Select
End Sub

Sub btnGPSPathFilterDel_Touch(Action As Int, x As Float, y As Float)
	Dim bmd As BitmapDrawable
	Dim Answ As Int
	
	Select Action
	Case Activity.ACTION_DOWN
		ToolTip("ลบจุดที่เลือกไว้")
		bmd.Initialize(LoadBitmap(File.DirAssets, "btngpsfilterdel1.png"))
		btnGPSPathFilterDel.Background = bmd
	Case Activity.ACTION_UP
		If x > 0 And x < btnGPSPathFilterDel.Width  And y > 0 And y < btnGPSPathFilterDel.Height Then
			Answ = Msgbox2("ต้องการลบจุดที่เลือกไว้ ?", "โปรดเลือก", "ตกลง", "", "ยกเลิก", Null)
			If Answ = DialogResponse.POSITIVE Then
				GPSPathFilterDelete
				btnGPSPathFilterDel.Visible = False
				btnGPSPathDeletePoint.Visible = False
				pnlGPSPathFilter.Visible = False
				pnlGPSPathFilterToolBox.Visible = False
				bmd.Initialize(LoadBitmap(File.DirAssets, "btngpsfilter0.png"))
				btnGPSPathFilter.Background = bmd
				Filtered_On = False
				SelectedRow = -1
			End If
		End If
		bmd.Initialize(LoadBitmap(File.DirAssets, "btngpsfilterdel0.png"))
		ToolTip("")
		btnGPSPathFilterDel.Background = bmd
	End Select
End Sub

Sub pnlGPSPathFilterToolBox_Touch(Action As Int, x As Float, y As Float)
	' Dummy routine to consume the touch event for views below
End Sub

Sub GPSPathUnFilter
	Dim i As Int
	
	For i = 0 To GPSPathDelete.Size - 1
		ChangeCellSelection(GPSPathDelete.Get(i), 2)
	Next
	lblGPSPathFilterNb0.Text = "จำนวนจุด"
	lblGPSPathFilterNb1.Text = Main.GPSPath.Size
	SelectedRow = -1
	btnGPSPathDeletePoint.Visible = True
End Sub

Sub GPSPathFilter
	Dim i0, i1, i2 As Int
	Dim x0, x1, x2 As Double
	Dim y0, y1, y2 As Double
	Dim g As GPSLocation
	Dim d As Double
	
	GPSPathDelete.Initialize
	i0 = Main.GPSPath.Size - 1
	g.Initialize
	g = Main.GPSPath.Get(i0)
	y0 = GPSCalcY(g.Latitude, g.Longitude)
	x0 = GPSCalcX(g.Latitude)

	i1 = i0 - 1
	g = Main.GPSPath.Get(i1)
	y1 = GPSCalcY(g.Latitude, g.Longitude)
	x1 = GPSCalcX(g.Latitude)
	For i = Main.GPSPath.Size - 3 To 0 Step - 1
		g = Main.GPSPath.Get(i)
		y2 = GPSCalcY(g.Latitude, g.Longitude)
		x2 = GPSCalcX(g.Latitude)
		d = GPSPathCalcDelta(x0, y0, x1, y1, x2, y2)
		If d <= Main.GPSFilterDelta Then
			GPSPathDelete.Add(i + 1)
			ChangeCellSelection(i + 1, 1)
		End If
		x0 = x1
		y0 = y1
		x1 = x2
		y1 = y2
	Next
	lblGPSPathFilterNb0.Text = "พบจุดเส้นทาง"
	lblGPSPathFilterNb1.Text = GPSPathDelete.Size & " / " & Main.GPSPath.Size
End Sub

Sub GPSCalcY(lat As Double, lng As Double) As Double
	' calculates the latitude distance in km from coordinates 0, 0
	Return lng * EarthRadius / 180 * cPI  * CosD(lat)
End Sub

Sub GPSCalcX(lat As Double) As Double
	' calculates the longitude distance in km from coordinates 0, 0
	Return lat * EarthRadius / 180 * cPI
End Sub

Sub GPSPathCalcDelta(x1 As Double, y1 As Double, xp As Double, yp As Double, x2 As Double, y2 As Double) As Double
	' calculates the distance between the point(xp,yp) 
	' and the line defined by the two ponts (x1,y1) and (x2,y2)
	Dim d, x, y As Double
	
	d = Abs((xp - x1) * (x2 - x1) + (yp - y1) * (y2 - y1))/((x2 - x1) * (x2 - x1) +  (y2 - y1) * (y2 - y1)) 
	x = x1 + d * (x2 - x1)
	y = y1 + d * (y2 - y1)
	d = Sqrt((xp - x) * (xp - x) + (yp - y) * (yp - y))
	Return d
End Sub

Sub GPSPathFilterDelete
	' removes the selected fixes (points) by the filter
	' clears the table and fills it again
	Dim i As Int
	
	For i = 0 To GPSPathDelete.Size - 1
		SelectedRow = GPSPathDelete.Get(i)
		GPSPathDeletePoint
	Next
	ClearTable
	FillGPSPathTable
	GPSPathDelete.Initialize
	lblGPSPathFilterNb0.Text = "จำนวนเส้นทาง"
	lblGPSPathFilterNb1.Text = Main.GPSPath.Size
	lblGPSPathNbPoints1.Text = Main.GPSPath.Size & " จุด"
End Sub

Sub InitGPSPathDisplay
	' Initializes the variables and the table to display a GPS path
	Dim i As Int
	
	CellLineWidth = 1dip
	HeaderHeight = 38dip
	RowHeight = 30dip
	RowHeight_1 = RowHeight - CellLineWidth
	
	scvGPSPath0.Top = HeaderHeight
	scvGPSPath0.Height = pnlGPSPath1.Height - HeaderHeight
	scvGPSPath0.Enabled = False
	
	scvGPSPath1.Top = scvGPSPath0.Top
	scvGPSPath1.Height = scvGPSPath0.Height
	skbGPSPath.Value = 0
	
	CellAlignment = Gravity.CENTER_HORIZONTAL + Gravity.CENTER_VERTICAL
	CellTextSize = 14
	CellColor = Colors.RGB(255,250,205)
	SelectedRowColor = Colors.RGB(255,196,196)
	CellTextColor = Colors.RGB(165,42,42)
	CellLineColor = Colors.RGB(165,42,42)
	HeaderColor = Colors.Blue
	HeaderLineColor = Colors.Yellow
	HeaderTextColor = Colors.Yellow
	
	ColName0 = "ลำดับ"
	ColName(0) = "ละติจูด" & CRLF & "[°]"
	ColName(1) = "ลองกิจูด" & CRLF & "[°]"
	ColName(2) = "ความสูง" & CRLF & "[" & Main.AltitudeUnitText(Main.AltitudeUnitIndex) & "]"
	ColName(3) = "เวลา" & CRLF & "[s]"
	ColName(4) = "มุมทิศ" & CRLF & "[°]"
	ColName(5) = "ความเร็ว" & CRLF & "[" & Main.SpeedUnitText(Main.SpeedUnitIndex) & "]"
	ColName(6) = "ระยะทาง" & CRLF & "[" & Main.DistanceUnitText(Main.DistanceUnitIndex) & "]"
	ColName(7) = "รวมระยะ" & CRLF & "[" & Main.DistanceUnitText(Main.DistanceUnitIndex) & "]"
	ColName(8) = "เครื่องหมาย"

	CellWidth0 = 40dip
	CellWidth0_1 = CellWidth0 - 2 * CellLineWidth
	CellWidth(0) = 80dip
	CellWidth(1) = 80dip
	CellWidth(2) = 60dip
	CellWidth(3) = 60dip
	CellWidth(4) = 60dip
	CellWidth(5) = 60dip
	CellWidth(6) = 70dip
	CellWidth(7) = 70dip
	CellWidth(8) = 80dip
	
	CellWidthTotal(0) = 0

	pnlGPSPathHeader0.Initialize("")
	pnlGPSPath0.AddView(pnlGPSPathHeader0, 0, 0, CellWidth0, HeaderHeight)
	pnlGPSPathHeader0.Color = HeaderLineColor
	
	Dim lbl As Label
	lbl.Initialize("Header")
	lbl.Tag = -1
	lbl.Color = HeaderColor
	lbl.TextColor = HeaderTextColor
	lbl.TextSize = 14
	lbl.Typeface = Typeface.DEFAULT_BOLD
	lbl.Gravity = CellAlignment
	lbl.Text = ColName0
	pnlGPSPathHeader0.AddView(lbl, 0, 0, CellWidth0_1,HeaderHeight)

	pnlGPSPathHeader1.Initialize("")
	pnlGPSPath1.AddView(pnlGPSPathHeader1, 0, 0, 100%x - CellWidth0, HeaderHeight)
	pnlGPSPathHeader1.Color = HeaderLineColor
	
	For i = 0 To NumberOfColumns_1
		CellWidth_1(i) = CellWidth(i) - CellLineWidth
		CellWidthTotal(i + 1) = CellWidthTotal(i) + CellWidth(i)
		Dim lbl As Label
		lbl.Initialize("Header")
		lbl.Tag = i
		lbl.Color = HeaderColor
		lbl.TextColor = HeaderTextColor
		lbl.TextSize = 14
		lbl.Typeface = Typeface.DEFAULT_BOLD
		lbl.Gravity = CellAlignment
		lbl.Text = ColName(i)
		pnlGPSPathHeader1.AddView(lbl, CellWidthTotal(i), 0, CellWidth_1(i),HeaderHeight)
	Next
	pnlGPSPath1.Left= CellWidth0
	pnlGPSPath1.Width = CellWidthTotal(NumberOfColumns)
	scvGPSPath1.Width = pnlGPSPath1.Width
	pnlGPSPathHeader1.Width = pnlGPSPath1.Width
	skbGPSPath.Max = pnlGPSPath1.Width - Activity.Width + CellWidth0
End Sub

Sub ClearTable
	' Clears the whole table, in fact removes all the views from the ScrollView
	Dim i As Int
	
	For i = scvGPSPath0.Panel.NumberOfViews - 1 To 0 Step -1
		scvGPSPath0.Panel.RemoveViewAt(i)
	Next
	For i = scvGPSPath1.Panel.NumberOfViews - 1 To 0 Step -1
		scvGPSPath1.Panel.RemoveViewAt(i)
	Next
End Sub

Sub FillGPSPathTable
	' Fills the table with the GPS path data
	Dim i As Int
	Dim GPSLoc As GPSLocation
	
	NumberOfRows = 0
	
	For i = 0 To Main.GPSPath.Size - 1
		Dim loc As GPSLocation
		loc = Main.GPSPath.Get(i)
		If i = 0 Then
			Main.GPSPathTime0 = loc.Time
			GPSLoc = loc
		End If
		GPSPathAddRow(loc)
		GPSLoc = loc
	Next
	scvGPSPath0.Panel.Height = NumberOfRows * RowHeight
	scvGPSPath1.Panel.Height = NumberOfRows * RowHeight
End Sub

Sub GPSPathAddRow(Values As GPSLocation)
' Adds a row to the table
	Dim i As Int
	
	Dim l As Label
	l.Initialize("cell")
	l.Text = NumberOfRows
	l.Gravity = CellAlignment
	l.TextSize = CellTextSize
	l.TextColor = CellTextColor
	l.Color = CellColor
	Dim rc As RowCol
	rc.Initialize
	rc.Col = -1
	rc.Row = NumberOfRows
	l.Tag = rc
	scvGPSPath0.Panel.AddView(l, 0, RowHeight * NumberOfRows, CellWidth0_1, RowHeight_1)

	For i = 0 To NumberOfColumns_1
		Dim l As Label
		l.Initialize("cell")
		Select i
		Case 0
			l.Text = NumberFormat(Values.Latitude, 1, 6)
		Case 1
			l.Text = NumberFormat(Values.Longitude, 1, 6)
		Case 2
			l.Text = DispAltitude(Values.Altitude)
		Case 3
			l.Text = (Values.Time - Main.GPSPathTime0) / 1000
		Case 4
			l.Text = NumberFormat(Values.Bearing, 1, 1)
		Case 5
			l.Text = NumberFormat(Values.Speed * Main.SpeedUnitRatio(Main.SpeedUnitIndex), 1 ,2)
		Case 6
			l.Text = DispDistance(Values.Distance)
		Case 7
			l.Text = DispDistance(Values.DistTot)
		Case 8
			If Values.Marker = True Then
				l.Text = "√"
			Else
				l.Text = "-"
			End If
		End Select
		
		l.Gravity = CellAlignment
		l.TextSize = CellTextSize
		l.TextColor = CellTextColor
		l.Color = CellColor
		Dim rc As RowCol
		rc.Initialize
		rc.Col = i
		rc.Row = NumberOfRows
		l.Tag = rc
		scvGPSPath1.Panel.AddView(l, CellWidthTotal(i), RowHeight * NumberOfRows, CellWidth_1(i), RowHeight_1)
	Next
	NumberOfRows = NumberOfRows + 1
'	scvGPSPath0.Panel.Height = NumberOfRows * RowHeight
'	scvGPSPath1.Panel.Height = NumberOfRows * RowHeight
End Sub

Sub scvGPSPath1_ScrollChanged(Position As Int)
	scvGPSPath0.ScrollPosition = Position
End Sub

Sub Cell_Click
	Dim Send As Label
	Dim rc As RowCol
	Dim GPSLoc As GPSLocation
	
	Send = Sender
	rc = Send.Tag
	If rc.Col < 8 Then
		ChangeCellSelection(rc.Row, 0)
	Else
		GPSLoc = Main.GPSPath.Get(rc.Row)
		GPSLoc.Marker = Not(GPSLoc.Marker)
		Main.GPSPath.Set(rc.Row, GPSLoc)
		If GPSLoc.Marker = True Then
			Send.Text = "√"
		Else
			Send.Text = "-"
		End If
	End If
End Sub

Sub ChangeCellSelection(row As Int, mode As Int)	
	'Changes the cell color depending on the selected row
	Dim i, j As Int
	
	' set standard color to the labels of the previous selected row
	If SelectedRow >= 0 And mode <> 1 Then
		scvGPSPath0.Panel.GetView(SelectedRow).Color = CellColor
		j = SelectedRow * NumberOfColumns
		For i = 0 To NumberOfColumns - 1
			scvGPSPath1.Panel.GetView(j + i).Color = CellColor
		Next
	End If
		
	If SelectedRow = row Then	
		' the user selects the same row > unselected 	
		SelectedRow = -1
		btnGPSPathDeletePoint.Visible = False
	Else
		' set selected color to the labels of the current selected row
		SelectedRow = row
		If mode < 2 Then
			scvGPSPath0.Panel.GetView(SelectedRow).Color = SelectedRowColor
			j = SelectedRow * NumberOfColumns
			For i = 0 To NumberOfColumns - 1
				scvGPSPath1.Panel.GetView(j + i).Color = SelectedRowColor
			Next
			btnGPSPathDeletePoint.Visible = True
		End If
	End If
End Sub

Sub Header_Click
	Dim Send As Label
	Dim i, n, Answ As Int
	Dim txt As String
	
	Send = Sender
	
	Select Send.Tag
	Case 2
		Main.AltitudeUnitIndex = Main.AltitudeUnitIndex + 1
		If Main.AltitudeUnitIndex > Main.AltitudeUnitNumber - 1 Then
			Main.AltitudeUnitIndex = 0
		End If
		UpdateDispAltitude
	Case 5
		Main.SpeedUnitIndex = Main.SpeedUnitIndex + 1
		If Main.SpeedUnitIndex > Main.SpeedUnitNumber - 1 Then
			Main.SpeedUnitIndex = 0
		End If
		UpdateDispSpeed
	Case 6, 7
		Main.DistanceUnitIndex = Main.DistanceUnitIndex + 1
		If Main.DistanceUnitIndex > Main.DistanceUnitNumber - 1 Then
			Main.DistanceUnitIndex = 0
		End If
		UpdateDispDistance
	Case 8
		txt = "ต้องการแสดง" & CRLF
		txt = txt & "หรือเอาเครื่องหมายออก ?"
		Answ = Msgbox2(txt, "โปรดเลือก", "แสดง", "ยกเลิก", "เอาออก", Null)
		Dim Set As Boolean
		Dim Ch As Char
		If Answ = DialogResponse.POSITIVE Then
			Set = True
			Ch = "√"
		Else If Answ = DialogResponse.NEGATIVE Then
			Set = False
			Ch = "-"
		Else
			Return
		End If
		For i = 0 To NumberOfRows - 1
			Dim GPSloc As GPSLocation
			Dim lbl1 As Label
			GPSloc = Main.GPSPath.Get(i)
			GPSloc.Marker = Set
			Main.GPSPath.Set(i, GPSloc)
			n = i * NumberOfColumns + 8
			lbl1 = scvGPSPath1.Panel.GetView(n)
			lbl1.Text = Ch
		Next
	End Select
End Sub

Sub DispDistance(Distance As Double) As String
	' returns a string of a distance value according to the selected scale
	Select Main.DistanceUnitIndex
	Case 0	' m
		Return NumberFormat2(Distance  * Main.DistanceUnitRatio(Main.DistanceUnitIndex), 1 ,1, 1, False)
	Case 1, 2	' km  mile
		Return NumberFormat2(Distance  * Main.DistanceUnitRatio(Main.DistanceUnitIndex), 1 ,4, 4, False)
	End Select
End Sub

Sub DispAltitude(Altitude As Double) As String
	' returns a string of an altitude value according to the selected scale
	Select Main.AltitudeUnitIndex
	Case 0	' m
		Return NumberFormat2(Altitude  * Main.AltitudeUnitRatio(Main.AltitudeUnitIndex), 1 ,2, 2, False)
	Case 1	' feet
		Return NumberFormat2(Altitude  * Main.AltitudeUnitRatio(Main.AltitudeUnitIndex), 1 ,1, 1, False)
	End Select
End Sub

Sub UpdateDispAltitude
	Dim i, n As Int
	
	Dim lbl1 As Label
	lbl1 = pnlGPSPathHeader1.GetView(2)
	lbl1.Text = "ความสูง" & CRLF & "[" & Main.AltitudeUnitText(Main.AltitudeUnitIndex) & "]"
	
	For i = 0 To NumberOfRows - 1
		Dim GPSloc As GPSLocation
		GPSloc = Main.GPSPath.Get(i)
		Dim lbl1 As Label
		n = i * NumberOfColumns + 2
		lbl1 = scvGPSPath1.Panel.GetView(n)
		lbl1.Text = DispAltitude(GPSloc.Altitude)
	Next
End Sub

Sub UpdateDispSpeed
	Dim i, n As Int
	
	Dim lbl1 As Label
	lbl1 = pnlGPSPathHeader1.GetView(5)
	lbl1.Text = "ความเร็ว" & CRLF & "[" & Main.SpeedUnitText(Main.SpeedUnitIndex) & "]"
	
	For i = 0 To NumberOfRows - 1
		Dim GPSloc As GPSLocation
		GPSloc = Main.GPSPath.Get(i)
		Dim lbl1 As Label
		n = i * NumberOfColumns + 5
		lbl1 = scvGPSPath1.Panel.GetView(n)
		lbl1.Text = NumberFormat(GPSloc.Speed * Main.SpeedUnitRatio(Main.SpeedUnitIndex), 1 ,2)
	Next
End Sub

Sub UpdateDispDistance
	Dim i, n As Int
	
	Dim lbl1 As Label
	lbl1 = pnlGPSPathHeader1.GetView(6)
	Dim lbl2 As Label
	lbl2 = pnlGPSPathHeader1.GetView(7)

	lbl1.Text = "ระยะทาง" & CRLF & "[" & Main.DistanceUnitText(Main.DistanceUnitIndex) & "]"
	lbl2.Text = "รวมระยะ" & CRLF & "[" & Main.DistanceUnitText(Main.DistanceUnitIndex) & "]"
	For i = 0 To NumberOfRows - 1
		Dim GPSloc As GPSLocation
		GPSloc = Main.GPSPath.Get(i)
		Dim lbl1 As Label
		n = i * NumberOfColumns + 6
		lbl1 = scvGPSPath1.Panel.GetView(n)
		lbl1.Text = DispDistance(GPSloc.Distance)
			Dim GPSloc As GPSLocation
		GPSloc = Main.GPSPath.Get(i)
		Dim lbl1 As Label
		n = i * NumberOfColumns + 7
		lbl1 = scvGPSPath1.Panel.GetView(n)
		lbl1.Text = DispDistance(GPSloc.DistTot)
	Next
End Sub


