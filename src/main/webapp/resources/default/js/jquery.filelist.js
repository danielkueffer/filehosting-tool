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
						var icon = "";
						
						if (val.type == "document") {
							var iconClass = "";
							
							switch(val.documentType) {
								case "doc" : iconClass = "icon-file-word file-icon";
								break;
								case "spreadsheet" : iconClass = "icon-file-excel file-icon";
								break;
								case "presentation" : iconClass = "icon-file-powerpoint file-icon";
								break;
								case "pdf" : iconClass = "icon-file-pdf file-icon";
								break;
							}
							
							icon = '<i class="' + iconClass + '"></i>';
						} else if (val.type == "audio") {
							icon = '<i class="icon-file-audio file-icon"></i>';
						} else if (val.type == "video") {
							icon = '<i class="icon-file-video file-icon"></i>';
						} else if (val.type == "image") {
							icon = '<i class="icon-file-image file-icon"></i>';
						} else {
							icon = '<i class="icon-doc file-icon"></i>';
						}
					
						var row = "<tr class=\"data-row\">" +
						"<td>" + icon + "<a href=\"resource/file/download/" + val.path + "\" class=\"file-name\">" + val.name + "</a></td>" +
						"<td>" + val.typeLabel + "</td>" +
						"<td>" + val.lastModified + "</td>" +
						"<td>" +
						"<a class=\"file-delete\" href=\"#\" data-id=\"" + val.id + "\"><i class=\"icon-trash\"></i> <span>delete</span></a>" +
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