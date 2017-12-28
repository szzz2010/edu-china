function showData(value) {
	if(value === 1) {
		$(".management").addClass("active");
		$(".operation").removeClass("active");
		$(".control-data").css("display","block");
		$(".operation-data").addClass("hidden");
	} else if(value === 2) {
		$(".management").removeClass("active");
		$(".operation").addClass("active");
		$(".control-data").css("display","none");
		$(".operation-data").removeClass("hidden");
	}
	
}
