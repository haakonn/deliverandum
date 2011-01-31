<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3/jquery.min.js"></script> 
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7/jquery-ui.min.js"></script>

<script type="text/javascript">
    $(function() {
        $("#beginDate").datepicker({ dateFormat: 'dd/mm yy', altFormat: 'yy-mm-dd', altField: '#beginDateFull', firstDay: 1 });
        $("#endDate").datepicker({ dateFormat: 'dd/mm yy', altFormat: 'yy-mm-dd', altField: '#endDateFull', firstDay: 1 });
    });
</script>
