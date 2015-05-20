/**
 * The file list jQuery plug-in
 * 
 * @param $
 */
(function($) {
	$.fn.filelist = function() {
		return this.each(function() {

			var $this = $(this);
			
			var parent = 0;

			var preview = $("<div/>").addClass("dropzone-previews").addClass("upload-status");
			preview.appendTo($this);
			
			var fileTable = $this.find(".file-table");
			var folderForm = $this.find(".folder-form");
			var breadcrumb = $this.find(".file-breadcrumb");

			/**
			 * Create the drop zone to upload files
			 */
			var mainDropzone = new Dropzone(".wrapper", {
				url : "resource/file/upload",
				createImageThumbnails : false,
				addRemoveLinks : false,
				clickable : ".fileinput-button",
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
			 * Create breadcrumb
			 */
			var createBreadcrumb = function(json) {
				
				// Remove breadcrumb items
				breadcrumb.find("li").each(function() {
					if (! $(this).hasClass("breadcumb-home")) {
						$(this).remove();
					}
				});
				
				// Populate breadcrumb
				$.each(json, function(key, val) {
					var folderPath = val.folderPath;
					
					if (folderPath != "") {
						var bItem = "<li class=\"breadcumb-item\"><a href=\"#\">" + folderPath + "</a> <i class=\"icon-angle-right\"></i></li>";
						
						breadcrumb.append(bItem);
					}
				});
			}

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
						} else if (val.type == "folder") {
							icon = '<i class="icon-folder file-icon"></i>';
						} else {
							icon = '<i class="icon-doc file-icon"></i>';
						}
						
						var date = getDateFromTimestamp(val.lastModified);
						var dateStr = date.getDate() + "." + date.getMonth() + 1 + "." + date.getFullYear() + " " + date.getHours() + ":" + date.getMinutes();
					
						var row = "<tr class=\"data-row\">";
						
						if (val.type == "folder") {
							row += "<td>" + icon + "<a href=\"resource/file/download/" + val.path + "\" class=\"file-name folder-name\" data-folder-id=" + val.id + " data-parent=" + val.parent + ">" + val.name + "</a></td>";
						} else {
							row += "<td>" + icon + "<a href=\"resource/file/download/" + val.path + "\" class=\"file-name\">" + val.name + "</a></td>";
						}
						
						row += "<td>" + val.typeLabel + "</td>";
						row += "<td>" + dateStr + "</td>";
						row += "<td>";
						row += "<a class=\"file-delete\" href=\"#\" data-path=\"" + val.path + "\"><i class=\"icon-trash\"></i> <span>delete</span></a>";
						row += "</td>";
						row += "</tr>";
					
					fileTable.append(row);
				});
			}

			/**
			 * Get all files of the current user
			 */
			var loadFileTable = function() {
				$.ajax({
					url : "resource/file/" + parent,
					type : "GET"
				}).success(function(msg) {
					populateTable(msg["files"]);
					createBreadcrumb(msg["breadcrumb"]);
				});
			}
			
			/**
			 * Delete a file
			 */
			$this.on("click", ".file-delete", function() {
				var path = $(this).data("path");
				
				$.ajax({
					url: "resource/file/" + path,
					type: "DELETE"
				}).success(function() {
					loadFileTable();
				});
				
				return false;
			});
			
			/**
			 * Get a date object from a SQL timestamp
			 */
			var getDateFromTimestamp = function(timestamp) {
				var t = timestamp.split(/[- :]/);

				// Apply each element to the Date function
				var date = new Date(t[0], t[1]-1, t[2], t[3], t[4], t[5]);
				
				return date;
			}
			
			/**
			 * New folder submit
			 */
			folderForm.submit(function() {
				var folderName = $(this).find("#folder").val();
				
				// Value can't be empty
				if ($.trim(folderName) == '') {
					$(this).find("#folder").addClass("error");
				}
				else {
					
					// Submit the folder
					var data = {
						"folder": folderName,
						"parent": parent
					};
					
					$.ajax({
						url: "resource/file/folder/add",
						type: "POST",
						data: data
					}).success(function(msg) {
						loadFileTable();
					});
					
					jQuery.fancybox.close();
					$(this).find("#folder").removeClass("error");
					$(this).find("#folder").val("");
				}
				
				return false;
			});

			/**
			 * Open a folder
			 */
			$this.on("click", ".folder-name", function() {
				
				parent = $(this).data("folder-id");
				loadFileTable();
				
				return false;
			});
			
			loadFileTable();
		});
	}
})(jQuery);