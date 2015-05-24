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
			$this.dropzone({
				url: "resource/file/upload",
				createImageThumbnails: false,
				addRemoveLinks: false,
				clickable: ".fileinput-button",
				parallelUploads: 1,
				previewsContainer: ".upload-status"
			});
			
			/**
			 * Drop zone events
			 */
			$this.each(function() {
				Dropzone.forElement(this).on('sending', function(file, xhr, formData) {
					formData.append("parent", parent);
				});
				
				Dropzone.forElement(this).on("success", function(file) {
					setTimeout(function() {
						$this.find(".dz-preview").fadeOut(function() {
							$(this).remove();
						});
						
						loadFileTable();
					}, 3000);
				});
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
					var folderId = val.folderId;
					var folderName = val.folderName;
					
					if (folderName != "") {
						var bItem = "<li class=\"breadcumb-item\"><a href=\"#\" data-folder-id=" + folderId + " class=\"folder-name\">" + folderName + "</a> <i class=\"icon-angle-right\"></i></li>";
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
							row += "<td>" + icon + "<a href=\"resource/file/download/" + val.path + "\" class=\"file-name folder-name\" data-folder-id=" + val.id + " data-parent=" + val.parent + ">" + val.name + "</a> </a> <a class=\"update-file\" href=\"#\"><i class=\"icon-pencil\"></i></a></td>";
						} else {
							row += "<td>" + icon + "<a href=\"resource/file/download/" + val.path + "\" class=\"file-name\" data-folder-id=" + val.id + ">" + val.name + "</a> <a class=\"update-file\" href=\"#\"><i class=\"icon-pencil\"></i></a></td>";
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
				
				// Value can't be empty or have slashes or backslashes
				if ($.trim(folderName) == '' || folderName.indexOf("/") > -1 || folderName.indexOf("\\") > -1) {
					$(this).find("#folder").addClass("error");
				}
				else {
					
					// Submit the folder
					var data = {
						"folder": $.trim(folderName),
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
			
			/**
			 * Table row hover
			 */
			$this.on("mouseover", ".data-row", function() {
				var updateFile = $(this).find(".update-file");
				if (! $(this).find(".update-form").length) {
					updateFile.show();
				}
			}).on("mouseleave", ".data-row", function() {
				$(this).find(".update-file").hide();
			});
			
			/**
			 * Show update filename form
			 */
			$this.on("click", ".update-file", function() {
				
				$this.find(".update-form").remove();
				$(this).hide();
				
				var form = "<div class=\"update-form\">" +
					"<input type=\"text\" name=\"fileName\" id=\"filename-update\"/>" +
					"<button id=\"filename-button\">" +
					"<i class=\"icon-pencil\"></i>" +
					"</button>" + 
					"<button id=\"filename-button-cancel\">" +
					"<i class=\"icon-cancel\"></i>" +
					"</button>" + 
					"</div>";
				
				$(this).parent().append(form);
				var fileName = $(this).parent().find(".file-name");
				var inputWidth = 150;
				
				if(fileName.width() > 150) {
					inputWidth = fileName.width();
				}
				
				$(this).parent().find("#filename-update").css("width", inputWidth);
				
				return false;
			});
			
			/**
			 * Remove update filename form
			 */
			$this.on("click", "#filename-button-cancel", function() {
				$(this).parent("td").find(".update-file").show();
				$(this).parent().remove();
			});
			
			/**
			 * Submit filename update
			 */
			$this.on("click", "#filename-button", function() {
				var updateVal = $(this).parent().find("#filename-update").val();
				
				// Value can't be empty or have slashes or backslashes
				if ($.trim(updateVal) == '' || updateVal.indexOf("/") > -1 || updateVal.indexOf("\\") > -1) {
					$(this).parent().find("#filename-update").addClass("error");
				}
				else {
					var id = $(this).parents("td:first").find(".file-name").attr("data-folder-id");
					
					var data = {
						"fileName": updateVal,
						"id": id
					};
					
					$.ajax({
						url: "resource/file/update",
						type: "POST",
						data: data
					}).success(function(msg) {
						loadFileTable();
					});
				}
				
				return false;
			});
			
			loadFileTable();
		});
	}
})(jQuery);