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
			child.slideDown();
		}
		else {
			$(".mainnav").find("li.active").removeClass("idle");
			$(this).parent("li").removeClass("active");
			child.slideUp();
		}
		
		$(this).blur();
		
		return false;
	});
	
	// Narrow Menu Bar
	$(".menu-trigger a").click(function() {
		if ($(".sidebar").hasClass("narrow")) {
			$(".sidebar").removeClass("narrow");
		}
		else {
			$(".sidebar").addClass("narrow");
		}
		
		$(this).blur();
		
		return false;
	});
});