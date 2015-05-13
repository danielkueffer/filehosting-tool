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

			var mainDropzone = new Dropzone(".wrapper", { 
				url: "resource/file/upload",
				createImageThumbnails: false,
				addRemoveLinks: false,
				clickable: false,
				parallelUploads: 1,
				previewsContainer: ".upload-status"
			});
			
			mainDropzone.on("success", function(file) {
				setTimeout(function() {
					$this.find(".dz-preview").fadeOut(function() {
						$(this).remove();
					});
				}, 3000);
			});
			
			var populateTable = function(json) {}
			
			var loadFileTable = function() {
				$.ajax({
					url: "resource/file",
					type: "GET"
				}).success(function(msg) {
					populateTable(msg);
				});
			}
			
			loadFileTable();
		});
	}
})(jQuery);