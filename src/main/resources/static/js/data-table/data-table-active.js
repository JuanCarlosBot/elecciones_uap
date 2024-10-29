(function ($) {
 "use strict";

		var $table = $('#table');
				$('#toolbar').find('select').change(function () {
					$table.bootstrapTable('destroy').bootstrapTable({
						exportDataType: $(this).val()
					});
				});
		var $table1 = $('#table1');
			$('#toolbar1').find('select1').change(function () {
				$table1.bootstrapTable('destroy1').bootstrapTable({
					exportDataType: $(this).val()
				});
			});
 
})(jQuery); 