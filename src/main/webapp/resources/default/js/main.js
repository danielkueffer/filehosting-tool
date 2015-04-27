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
});