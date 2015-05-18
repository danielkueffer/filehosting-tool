/**
 * The file list jQuery plug-in
 * 
 * @param $
 */
(function($) {
	$.fn.filelist = function() {
		return this.each(function() {

			var $this = $(this);

			var preview = $("<div/>").addClass("dropzone-previews").addClass("upload-status");
			preview.appendTo($this);
			
			var fileTable = $this.find(".file-table");

			/**
			 * Create the drop zone to upload files
			 */
			var mainDropzone = new Dropzone(".wrapper", {
				url : "resource/file/upload",
				createImageThumbnails : false,
				addRemoveLinks : false,
				clickable : false,
				parallelUploads : 1,
				previewsContainer : ".upload-status"
			});

			/**
			 * Successful uploaded
			 */
			mainDropzone.on("success", function(file) {
				setTimeout(function() {
					$this.find(".dz-preview").fadeOut(function() {
						$(this).remove();
					});
					
					loadFileTable();
				}, 3000);
			});

			/**
			 * Populate the Table with the files
			 */
			var populateTable = function(json) {
				
				fileTable.find(".data-row").remove();
				
				$.each(json, function(key, val) {
						var row = "<tr class=\"data-row\">" +
						"<td>" + val.name + "</td>" +
						"<td>" + val.type + "</td>" +
						"<td>" + val.lastModified + "</td>" +
						"<td>" +
						"<a class=\"edit\" href=\"#\" data-id=\"" + val.id + "\">bearbeiten</a>" +
						"<a class=\"delete\" href=\"#\" data-id=\"" + val.id + "\">löschen</a>" +
						"</td>" +
						"</tr>";
					
					fileTable.append(row);
				});
			}

			/**
			 * Get all files of the current user
			 */
			var loadFileTable = function() {
				$.ajax({
					url : "resource/file",
					type : "GET"
				}).success(function(msg) {
					populateTable(msg);
				});
			}

			loadFileTable();
		});
	}
})(jQuery);