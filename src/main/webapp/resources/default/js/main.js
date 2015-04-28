$(document).ready(function() {
	if ($(".login").length) {
		$(".login").find("input[type='text']").focus();
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
	
	// Folding Navigation
	$(".mainnav .parent > a").click(function() {
		var child = $(this).parent().find("ul");
		
		if (child.is(":hidden")) {
			$(".mainnav").find("li.active").addClass("idle");
			$(this).parent("li").addClass("active");
			
			if ($(".narrow").length) {
				child.addClass("flyout");
			}
			
			child.slideDown();
		}
		else {
			$(".mainnav").find("li.active").removeClass("idle");
			$(this).parent("li").removeClass("active");
			
			child.slideUp(function() {
				$(this).removeClass("flyout");
			});
		}
		
		$(this).blur();
		
		return false;
	});
	
	// Hide menu in narrow mode when mouse leaves
	$(".wrapper").on("mouseleave", ".sidebar.narrow .parent", function() {
		$(this).find("ul").css("display", "none");
		$(this).find("ul").removeClass("flyout");
	});
	
	// Narrow Menu Bar
	$(".menu-trigger a").click(function() {
		if ($(".sidebar").hasClass("narrow")) {
			$(".sidebar").removeClass("narrow");
			$(".wrapper").removeClass("wide");
		}
		else {
			$(".sidebar").addClass("narrow");
			$(".wrapper").addClass("wide");
		}
		
		$(this).blur();
		
		return false;
	});
});