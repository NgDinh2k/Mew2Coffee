USE [QLKNL]
GO
/****** Object:  StoredProcedure [dbo].[SP_TONGTIENBAN]    Script Date: 2/9/2023 4:58:16 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_TONGTIENBAN]
       @TUNGAY DATETIME, @DENNGAY DATETIME
AS
BEGIN
	   

	SELECT PX.MAPX, SUM(SOLUONG*DONGIA) AS THANHTIEN
	FROM (SELECT MAPX FROM PHIEUXUAT WHERE NGAYXUAT >= @TUNGAY AND NGAYXUAT <= @DENNGAY) PX,CTHD
	WHERE PX.MAPX = CTHD.MAPX
	GROUP BY PX.MAPX
	ORDER BY PX.MAPX
END
GO
