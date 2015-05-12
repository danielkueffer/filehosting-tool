/**
 * The file list jQuery plug-in
 * 
 * @param $
 */
(function($) {
	$.fn.filelist = function() {
		return this.each(function() {

			var $this = $(this);

			var mainDropzone = $this.dropzone({ 
				url: "resource/file/upload",
				createImageThumbnails: false,
				addRemoveLinks: false,
				clickable: false,
				parallelUploads: 1
			});
			
			mainDropzone.on("success", function(file) {
				console.log(file + " success");
			});
			
		});
	}
})(jQuery);