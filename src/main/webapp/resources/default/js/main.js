$(document).ready(function() {
	if ($(".login").length) {
		$(".login").find("input[type='text']").focus();
	}
	
	// File list
	if ($(".file-wrapper").length) {
		$(".file-wrapper").filelist();
	}
	
	// Remove search text on focus
	$(".search").click(function() {
		if ($(this).val() == "Suchen") {
			$(this).val("");
		}
	}).blur(function() {
		if ($(this).val() == "") {
			$(this).val("Suchen");
		}
	});
	
	/**
	 * Navigation
	 */
	$(".mainnav .parent > a").click(function() {
		var child = $(this).parent().find("ul");
		
		$(".mainnav").find("li").each(function() {
			if ($(this).hasClass("parent") && ! $(this).hasClass("idle")) {
				$(this).removeClass("active");
			}
			
			$(this).removeClass("idle");
			$(this).removeClass("open");
		});
		
		$(".mainnav").find("ul ul").each(function() {
			$(this).slideUp(function() {
				$(this).removeClass("flyout");
			});
		});
		
		if (child.is(":hidden")) {
			$(".mainnav").find("li.active").addClass("idle");
			$(this).parent("li").addClass("active");
			
			if ($(".narrow").length) {
				child.addClass("flyout");
			}
			
			child.slideDown();
					
			$(this).parent("li").addClass("open");
		}
		
		$(this).blur();
		
		return false;
	});
	
	// Hide menu in narrow mode when mouse leaves
	$(".wrapper").on("mouseleave", ".sidebar.narrow .parent", function() {
		$(this).find("ul").css("display", "none");
		$(".mainnav").find("li.active").removeClass("idle");
		$(this).removeClass("active");
	});
	
	// Narrow Menu Bar
	$(".menu-trigger a").click(function() {
		if ($(".sidebar").hasClass("narrow")) {
			$(".sidebar").removeClass("narrow");
			$(".wrapper").removeClass("wide");;
			$(".mainnav").find("ul").removeClass("flyout");
			$(".mainnav").find(".open ul").css("display", "block");
		}
		else {
			$(".sidebar").addClass("narrow");
			$(".wrapper").addClass("wide");
			
			if ($(".mainnav").find(".open").length) {
				var openMenu = $(".mainnav").find(".open");
				openMenu.find("ul").addClass("flyout");
				openMenu.find("ul").hide();
			}
		}
		
		$(this).blur();
		
		return false;
	});
	
	if ($(".mainnav .open").length) {
		$(".mainnav .open").find("ul").css("display", "block");
	}
	
	// Fancybox overlay
	if ($(".overlay").length) {
		$(".overlay").fancybox({
			maxWidth	: 600,
			maxHeight	: 400,
			fitToView	: false,
			width		: '60%',
			height		: '50%',
			autoSize	: false,
			closeClick	: false,
			openEffect	: 'none',
			closeEffect	: 'none'
		});
	}
});