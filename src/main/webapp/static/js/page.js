(function($){
	var ms = {
		init:function(totalsubpageTmep,args){
			return (function(){
				ms.fillHtml(totalsubpageTmep,args);
				ms.bindEvent(totalsubpageTmep,args);
			})();
		},
		//填充html
		fillHtml:function(totalsubpageTmep,args){
			return (function(){
				totalsubpageTmep="";
				var totalPage = args.totalPage;
				if(totalPage == 0){
					totalPage = 1;
				}
				/************************START*********************/
				if(args.currPage == 1){
					totalsubpageTmep += "<li class='disabled'><a href='javascript:void(0);'>上一页</a></li>";
				}else{
					var go_prev = args.currPage - 1;
					totalsubpageTmep += "<li><a href='javascript:gopage("+go_prev+")'>上一页</a></li>";
				}
				
				// 页码大于等于4的时候，添加第一个页码元素(总页数>=5，当前页>=4)
				if(args.currPage!=1 && args.currPage>=4 && totalPage!=4) {
					totalsubpageTmep += "<li><a href='javascript:gopage(1);'>"+1+"</a></li>";
				}
				/* 当前页码>4, 并且<=总页码，总页码>5，添加“···”*/
				if(args.currPage-2>2 && args.currPage<=totalPage && totalPage>5) {
					totalsubpageTmep += "<li><a href='javascript:void(0);'>...</a></li>";
				}
				/* 当前页码的前两页 */
				var start = args.currPage-2;
				/* 当前页码的后两页 */
				var end = args.currPage+2;
				
				if((start>1 && args.currPage<4) || args.currPage==1) {//当前页=1，start=-1，end=3
					end++;//end=4
				}
				if(args.currPage>totalPage-4 && args.currPage>=totalPage) {//当前页>=总页数
					start--;//比当前页小3
				}
				
				for(; start<=end; start++) {
					if(start<=totalPage && start>=1) {
						if(start != args.currPage) {
							totalsubpageTmep += "<li ><a href='javascript:gopage("+start+");'>"+start+"</a></li>";
						}else{
							totalsubpageTmep += "<li class='active'><a href='javascript:void(0);'>"+start+"</a></li>";
						}
					}
				}
				
				if(args.currPage+2<totalPage-1 && args.currPage>=1 && totalPage>5) {
					totalsubpageTmep += "<li><a href='javascript:void(0);'>...</a></li>";
				}
				
				if(args.currPage!=totalPage && args.currPage<totalPage-2 && totalPage!=4) {
					totalsubpageTmep += "<li><a href='javascript:gopage("+totalPage+");' >"+totalPage+"</a></li>";
				}
				
				if(args.currPage == totalPage){
					totalsubpageTmep += "<li class='disabled'><a href='javascript:void(0);'>下一页</a></li>";
				}else{
					var go_next = args.currPage + 1;
					totalsubpageTmep += "<li><a href='javascript:gopage("+go_next+");'>下一页</a></li>";
				}
				$("#my_page_ul").html(totalsubpageTmep);
			})();
		},
		//绑定事件
		bindEvent:function(totalsubpageTmep,args){
			return (function(){
				totalsubpageTmep.on("click","a.geraltTb_pager",function(event){
					var current = parseInt($(this).text());
					ms.fillHtml(totalsubpageTmep,{"currPage":current,"totalPage":args.totalPage,"turndown":args.turndown});
					if(typeof(args.backFn)=="function"){
						args.backFn(current);
					}
				});
				//上一页
				/*
				totalsubpageTmep.on("click",".geraltTb_pager",function(){
					var current = parseInt($(".geraltTb_pager a").text());
					ms.fillHtml(totalsubpageTmep,{"currPage":current-1,"pageCount":args.pageCount,"turndown":args.turndown});
					if(typeof(args.backFn)=="function"){
						console.log("b:"+current-1);
						args.backFn(current-1);
					}
				});
				//下一页
				totalsubpageTmep.on("click",".geraltTb_pager",function(){
					var current = parseInt($(".geraltTb_pager a").text());
					ms.fillHtml(totalsubpageTmep,{"currPage":current+1,"pageCount":args.pageCount,"turndown":args.turndown});
					if(typeof(args.backFn)=="function"){
						console.log("c:"+current+1);
						args.backFn(current+1);
					}
				});
				*/
			})();
		}
	}
	$.fn.createPage = function(options){		
		ms.init(this,options);
	}
})(jQuery);