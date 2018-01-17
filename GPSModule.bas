Type=StaticCode
Version=6.3
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
'Code module
'Subs in this code module will be accessible from all modules.
Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.

End Sub

Sub LoadPath
	Dim RandomFile As RandomAccessFile

	Main.GPSPath.Initialize
	RandomFile.Initialize(Main.GPSDir, Main.GPSPathFilename, False)
	Main.GPSPathComment = RandomFile.ReadObject(RandomFile.CurrentPosition)
	Main.GPSPath = RandomFile.ReadObject(RandomFile.CurrentPosition)
	RandomFile.Close
	Dim loc As GPSLocation
	loc = Main.GPSPath.Get(0)
	Main.GPSPathTime0 = loc.Time
End Sub

Sub SavePath
	Dim RandomFile As RandomAccessFile
	
	RandomFile.Initialize(Main.GPSDir, Main.GPSPathFilename, False)
	RandomFile.WriteObject(Main.GPSPathComment, False, RandomFile.CurrentPosition)
	RandomFile.WriteObject(Main.GPSPath, False, RandomFile.CurrentPosition)
	RandomFile.Close
End Sub

Sub SaveKMLLine(LineWidth As Int, LineColor As Long, DispStartPoint As Boolean, StartPointText As String , DispEndPoint As Boolean, EndPointText As String)
	Dim i As Int
	Dim txt As String
	Dim loc1, loc2 As GPSLocation
	Dim TAB2, TAB3, TAB4, TAB5, TAB6 As String
	
	TAB2 = TAB & TAB
	TAB3 = TAB2 & TAB
	TAB4 = TAB3 & TAB
	TAB5 = TAB4 & TAB
	TAB6 = TAB5 & TAB
	
	loc1 = Main.GPSPath.Get(0)
	loc2 = Main.GPSPath.Get(Main.GPSPath.Size - 1)
	
	txt = "<?xml version='1.0' encoding='UTF-8'?>" & CRLF
	txt = txt & "<kml xmlns='http://earth.google.com/kml/2.2'>" & CRLF

	txt = txt & TAB & "<Folder>" & CRLF

	If DispStartPoint = True Then
		txt = txt & TAB2 & "<Placemark>" & CRLF
		txt = txt & TAB3 & "<name>" & StartPointText & "</name>" & CRLF
		txt = txt & TAB3 & "<Style>" & CRLF
		txt = txt & TAB4 & "<IconStyle>" & CRLF
		txt = txt & TAB5 & "<Icon>" & CRLF
		txt = txt & TAB6 & "<href>http://maps.google.com/mapfiles/kml/pushpin/grn-pushpin.png</href>" & CRLF
		txt = txt & TAB5 & "</Icon>" & CRLF
		txt = txt & TAB4 & "</IconStyle>" & CRLF
		txt = txt & TAB3 & "</Style>" & CRLF
		txt = txt & TAB3 & "<Point>" & CRLF
		txt = txt & TAB4 & "<coordinates>" & CRLF
		loc1 = Main.GPSPath.Get(0)
		txt = txt & TAB5 & loc1.Longitude & "," & loc1.Latitude & "," & loc1.Altitude & " " & CRLF
		txt = txt & TAB4 & "</coordinates>" & CRLF
		txt = txt & TAB3 & "</Point>" & CRLF
		txt = txt & TAB2 & "</Placemark>" & CRLF
	End If
	
	If DispEndPoint = True Then
		txt = txt & TAB2 & "<Placemark>" & CRLF
		txt = txt & TAB3 & "<name>" & EndPointText & "</name>" & CRLF
		txt = txt & TAB3 & "<Style>" & CRLF
		txt = txt & TAB4 & "<IconStyle>" & CRLF
		txt = txt & TAB5 & "<Icon>" & CRLF
		txt = txt & TAB6 & "<href>http://maps.google.com/mapfiles/kml/pushpin/red-pushpin.png</href>" & CRLF
		txt = txt & TAB5 & "</Icon>" & CRLF
		txt = txt & TAB4 & "</IconStyle>" & CRLF
		txt = txt & TAB3 & "</Style>" & CRLF
		txt = txt & TAB3 & "<Point>" & CRLF
		txt = txt & TAB4 & "<coordinates>" & CRLF
		loc1 = Main.GPSPath.Get(Main.GPSPath.Size - 1)
		txt = txt & TAB5 & loc1.Longitude & "," & loc1.Latitude & "," & loc1.Altitude & " " & CRLF
		txt = txt & TAB4 & "</coordinates>" & CRLF
		txt = txt & TAB3 & "</Point>" & CRLF
		txt = txt & TAB2 & "</Placemark>" & CRLF
	End If
	
	txt = txt & TAB2 & "<Placemark>" & CRLF
	txt = txt & TAB3 & "<name>" & Main.GPSPathFilenameKML &"</name>" & CRLF
	txt = txt & TAB4 & "<description>" & CRLF
	If Main.GPSPathComment<>"" Then
		txt = txt & TAB4 & Main.GPSPathComment & CRLF
	End If
	If loc1.Time > 0 Then
		txt = txt & TAB4 & "Date: " & DateTime.Date(loc1.Time) & CRLF
		txt = txt & TAB4 & "Start Time: " & DateTime.Time(loc1.Time) & CRLF
		txt = txt & TAB4 & "End Time: " & DateTime.Time(loc2.Time) & CRLF
	End If
	txt = txt & TAB3 & "</description>" & CRLF
	txt = txt & TAB3 & "<Style>" & CRLF
	txt = txt & TAB4 & "<LineStyle>" & CRLF
	' color  aa bb gg rr
	' aa alpha transparency  00 = transparent  ff = opaque
	' bb blue  gg green  rr red
	' converts Long to Hex
	Dim bytes() As Byte
	Dim bc As ByteConverter
	Dim cols(1) As Long
	Dim col, col1 As String
	cols(0) = LineColor
	bytes = bc.LongsToBytes(cols)		' convert Long to Bytes
	col = bc.HexFromBytes(bytes)		' color in hex, convert Bytes To Hex
	col = col.SubString2(col.Length - 8, col.Length)	' removes leading hex bytes
	col = col.ToLowerCase
	' invert ARGB to aa bb gg rr
	col1 = col.SubString2(0, 2) & col.SubString2(6, 8) & col.SubString2(4, 6) & col.SubString2(2, 4)
	txt = txt & TAB5 & "<color>" & col1 & "</color>" & CRLF
	txt = txt & TAB5 & "<width>" & LineWidth & "</width>" & CRLF
	txt = txt & TAB4 & "</LineStyle>" & CRLF
	txt = txt & TAB3 & "</Style>" & CRLF
	txt = txt & TAB3 & "<LineString>" & CRLF
	txt = txt & TAB4 & "<coordinates>" & CRLF
	
	txt = txt & TAB4
	For i = 0 To Main.GPSPath.Size - 1
		loc1 = Main.GPSPath.Get(i)
		txt = txt & loc1.Longitude & "," & loc1.Latitude & "," & loc1.Altitude & " "
	Next
	txt = txt & CRLF & TAB4 & "</coordinates>" & CRLF
	txt = txt & TAB3 & "</LineString>" & CRLF
	txt = txt & TAB2 & "</Placemark>" & CRLF

	txt = txt & TAB & "</Folder>" & CRLF
	
	txt = txt & "</kml>"
	
	Dim tw As TextWriter
	
	tw.Initialize(File.OpenOutput(Main.GPSDir, Main.GPSPathFilenameKML, False))
	tw.Write(txt)
	tw.Close
End Sub

Sub SaveKMLPoint
	Dim txt As String
	Dim loc1 As GPSLocation
	Dim TAB2, TAB3, TAB4, TAB5, TAB6 As String
	
	TAB2 = TAB & TAB
	TAB3 = TAB2 & TAB
	TAB4 = TAB3 & TAB
	TAB5 = TAB4 & TAB
	TAB6 = TAB5 & TAB
	
	loc1 = Main.GPSPath.Get(0)
	
	txt = "<?xml version='1.0' encoding='UTF-8'?>" & CRLF
	txt = txt & "<kml xmlns='http://earth.google.com/kml/2.2'>" & CRLF

	txt = txt & TAB & "<Placemark>" & CRLF
	txt = txt & TAB2 & "<name>" & Main.GPSPathFilenameKML & "</name>" & CRLF
	txt = txt & TAB2 & "<description>" & CRLF
	If Main.GPSPathComment<>"" Then
		txt = txt & TAB3 & Main.GPSPathComment & CRLF
	End If
	If loc1.Time > 0 Then
		txt = txt & TAB3 & "Date: " & DateTime.Date(loc1.Time) & CRLF
		txt = txt & TAB3 & "Time: " & DateTime.Time(loc1.Time) & CRLF
	End If
	txt = txt & TAB2 & "</description>" & CRLF

	txt = txt & TAB2 & "<Point>" & CRLF
	txt = txt & TAB3 & "<coordinates>" & CRLF
	loc1 = Main.GPSPath.Get(0)
	txt = txt & TAB4 & loc1.Longitude & "," & loc1.Latitude & "," & loc1.Altitude & " " & CRLF
	txt = txt & TAB3 & "</coordinates>" & CRLF
	txt = txt & TAB2 & "</Point>" & CRLF
	txt = txt & TAB & "</Placemark>" & CRLF
	txt = txt & "</kml>"

	Dim tw As TextWriter

	tw.Initialize(File.OpenOutput(Main.GPSDir, Main.GPSPathFilenameKML, False))
	tw.Write(txt)
	tw.Close
End Sub
