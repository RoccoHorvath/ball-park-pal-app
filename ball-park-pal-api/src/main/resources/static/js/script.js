    $(document).ready(function(){
        $("#example").DataTable({
            fixedColumns: {
        left: 1,
        right:1
    },
        ordering: false,
        paging: false,
        "dom": 'lfrt'
        });
    })
