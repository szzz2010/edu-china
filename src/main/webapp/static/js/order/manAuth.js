//人工审核成功
$('#manAuthYes').click(function(){
    var userId = $('#userId').val();
    var orderId = $('#orderId').val();
    var refuseReason = $('#refuseReason').val();
    $.ajax({
    	async: true,
    	type: "POST",
    	url: webPath+"/order/manAuthOperation?userId="+userId+"&orderId="+orderId+"&operType=4",
    	success: function (data, textStatus) {
    		window.close();
        }
    });
})

$('#manAuthNo').click(function(){
    var userId = $('#userId').val();
    var orderId = $('#orderId').val();
    var refuseReason = $('#refuseReason').val();
    $.ajax({
    	async: true,
    	type: "POST",
    	url: webPath+"/order/manAuthOperation?userId="+userId+"&orderId="+orderId+"&operType=5",
    	success: function (data, textStatus) {
    		window.close();
        }
    });
    window.close();
})