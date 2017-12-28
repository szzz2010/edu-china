/**删除数据**/
function del(id){
	swq.create_bootstarp_confirm({
        title : '删除数据',
        content : '确定删除当前行吗?',
        button : {
            '确定' : function() {
                $.ajax({
                	async: true,
                	type: "POST",
                	url: webPath+"/config/idNoToCity/del?id="+id,
                	success: function (data, textStatus) {
                		alert("删除成功！")
                    }
                });
            },
            '取消' : function() {
            }
        }
    })
}