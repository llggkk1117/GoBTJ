<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Insert title here</title>
	
	<!-- CDN -->
	<!--
	<link rel="stylesheet" type="text/css" href="//cdn.datatables.net/1.10.5/css/jquery.dataTables.css">
	<script type="text/javascript" charset="utf8" src="//code.jquery.com/jquery-1.10.2.min.js"></script>
	<script type="text/javascript" charset="utf8" src="//cdn.datatables.net/1.10.5/js/jquery.dataTables.js"></script>
	-->
	
	<!-- Local -->
	
	<link rel="stylesheet" type="text/css" href="resource/css/jquery.dataTables.min.css">
	<script type="text/javascript" charset="utf8" src="resource/js/jquery.js"></script>
	<script type="text/javascript" charset="utf8" src="resource/js/jquery.dataTables.js"></script>
	
	
	<script>
		$(document).ready(function() {
		    var t = $('#example').DataTable();
		    var counter = 1;
		 
		    $('#addRow').on( 'click', function () {
		        t.row.add( [
		            counter +'.1',
		            counter +'.2',
		            counter +'.3',
		            counter +'.4',
		            counter +'.5'
		        ] ).draw();
		 
		        counter++;
		    } );
		 
		    // Automatically add a first row of data
		    $('#addRow').click();
		} );
	</script>
	
</head>

<body>
	<button id="addRow">Add Row</button>
	<table id="example" class="display" cellspacing="0" width="100%">
        <thead>
            <tr>
                <th>Column 1</th>
                <th>Column 2</th>
                <th>Column 3</th>
                <th>Column 4</th>
                <th>Column 5</th>
            </tr>
        </thead>
 
        <tfoot>
            <tr>
                <th>Column 1</th>
                <th>Column 2</th>
                <th>Column 3</th>
                <th>Column 4</th>
                <th>Column 5</th>
            </tr>
        </tfoot>
    </table>
    
</body>
</html>