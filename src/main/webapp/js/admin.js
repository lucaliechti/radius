$(document).ready(function() {
    var recipientUsers = $('#recipients_users');
    var recipientNewsletter = $('#recipients_newsletter');

    $('#matchingtable').DataTable({
        "lengthMenu": [ 10, 20, 50, 100 ]
    });

    var usertable = $('#usertable').DataTable({
        "columnDefs": [ {
            orderable: false,
            className: 'select-checkbox',
            targets:   0
        } ],
        "lengthMenu": [ 10, 20, 50, 100 ],
        "select": {
            style:    'multi',
            selector: 'td:first-child'
        }
    });
    usertable
        .on( 'select', function () { updateSelectedRows(usertable, USER_EMAIL_COLUMN, recipientUsers); } )
        .on( 'deselect', function () { updateSelectedRows(usertable, USER_EMAIL_COLUMN, recipientUsers); } );

    var newslettertable = $('#newslettertable').DataTable({
        "columnDefs": [ {
            orderable: false,
            className: 'select-checkbox',
            targets:   0
        } ],
        "lengthMenu": [ 10, 20, 50, 100 ],
        "select": {
            style:    'multi',
            selector: 'td:first-child'
        }
    });
    newslettertable
        .on( 'select', function () { updateSelectedRows(newslettertable, NEWSLETTER_EMAIL_COLUMN, recipientNewsletter); } )
        .on( 'deselect', function () { updateSelectedRows(newslettertable, NEWSLETTER_EMAIL_COLUMN, recipientNewsletter); } );
});

var USER_EMAIL_COLUMN = 3;
var USER_STATUS_COLUMN = 5;
var NEWSLETTER_EMAIL_COLUMN = 1;
var NEWSLETTER_SOURCE_COLUMN = 2;

function updateSelectedRows(table, emailColumn, recipientField) {
    recipientSet = new Set();
    var selectedRows = table.rows( { selected: true } );
    var indexArray = selectedRows.indexes().toArray();
    for (var i = 0; i < indexArray.length; i++) {
        recipientSet.add(table.row(indexArray[i]).data()[emailColumn]);
    }
    recipientField.val([...recipientSet].join(';'));
}

function selectDeselect(isSelected, table, attr, attributeColumn) {
    var rows = table.data().toArray();
    for(var i = 0; i < rows.length; i++) {
        if(rows[i][attributeColumn] === attr) {
            isSelected ? table.rows(i).select() : table.rows(i).deselect();
        }
    }
}
