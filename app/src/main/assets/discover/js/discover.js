!function(a){
	/*banner轮播*/
	var silder= function() {
		this.index=0,
		this.title=$(".yzd-wrap img").eq(this.index).attr("data-item");
		$(".page-item").text(this.title);
		var itemArray=[]
		for(var i in $(".yzd-wrap img")){
			itemArray.push($(".yzd-wrap img").eq(i).attr("data-item"))
		}
		window.mySwipe = new a(document.getElementById("slider"), {
			startSlide: 0,
			speed: 400,
			auto: 3000,
			continuous: true,
			callback: function(index, element) {
				var title=itemArray[index];
				$(".page-item").text(title);
				$(".page-index i").eq(index).addClass("indexActive").siblings().removeClass("indexActive")
			}
		});
	}()
}(Swipe || (Swipe={}))

!function(z){
	function c(){
		this.page=document.querySelector(".mainPage"),
		document.querySelector(".disPage").style.height=window.innerHeight+"px",
		this.top=0,
		this.touchstartY=0,
		this.buttom=parseInt(window.innerHeight - this.page.offsetHeight),
		this.timer=0,
		this.ajaxRefresh=!1,
		this.ajaxLoader=!1,
		this.count=0,
		this.init()
	}
	
	c.prototype.init=function(){
		var a=this;
		this.page.addEventListener("touchstart",function(e){
			e.preventDefault(),
			a.startY = e.touches[0].clientY,
			a.originY=a.startY,
			a.touchstartY === 0 ? a.ajaxRefresh=!0 : a.ajaxRefresh=!1,
			a.touchstartY === a.buttom ? a.ajaxLoader=!0 : a.ajaxLoader=!1
		}),
		this.page.addEventListener("touchmove",function(e){
			e.preventDefault();
			a.MY = e.changedTouches[0].clientY-a.originY,
			a.originY=e.changedTouches[0].clientY,
			a.moveY=a.MY,
			a.end=!1,
			a.translateMove(),
			a.count++
		}),
		this.page.addEventListener("touchend",function(e){
			e.preventDefault();
			a.count < 8 && (a.moveY =2*(e.changedTouches[0].clientY-a.startY)),
			a.end=!0,
			a.translateMove(),
			a.count=0
		})
	},
	c.prototype.translateMove=function(){
				this.touchstartY+=this.moveY,
				this.touchstartY > this.top && (this.touchstartY = this.end?((this.touchstartY>=(this.top+60))?this.refresh():this.top)
				:(this.touchstartY >=(this.top+60)?(this.top+60):this.touchstartY)),
				this.touchstartY < this.buttom && (this.touchstartY = this.end?((this.touchstartY <= (this.buttom-60))?this.loader():this.buttom)
				:(this.touchstartY <= (this.buttom-60)?(this.buttom-60):this.touchstartY)),
				this.moveY !==0 && z(".mainPage").css({
					"-webkit-transform": "translate3d(0px,"+ this.touchstartY + "px,0px)",
					"-webkit-transition": "-webkit-transform 0.2s cubic-bezier(0.33, 0.66, 0.66, 1)"
				});
	},
	c.prototype.refresh=function(){
		if(this.ajaxRefresh){
		 z(".mainPage").css({
			"-webkit-transform": "translate3d(0px,"+ this.top+40 + "px,0px)",
			"-webkit-transition": "-webkit-transform 0.2s cubic-bezier(0.33, 0.66, 0.66, 1)"
		}),
		z(".refresh").show();
		var a=this;
		z.ajax({
			type:"POST",
			url:"area.json",
			data:{"id":"123"},
			dataType:"json",
			success:function(e){
				z(".freshtext").text('刷新完成');
				setTimeout(function(){
					a.touchstartY=a.top,
					 z(".mainPage").css({
						"-webkit-transform": "translate3d(0px,"+ a.top + "px,0px)",
						"-webkit-transition": "-webkit-transform 0.2s cubic-bezier(0.33, 0.66, 0.66, 1)"
					}),
					z(".freshtext").text('正在刷新'),
					z(".refresh").hide();
				},400)	
			}
		});
		}
		else{
			this.ajaxRefresh=!0,
			this.touchstartY=this.top;
			return this.touchstartY
		}
	},
	c.prototype.loader=function(){
		if(this.ajaxLoader){
		 z(".mainPage").css({
			"-webkit-transform": "translate3d(0px,"+ this.buttom-40 + "px,0px)",
			"-webkit-transition": "-webkit-transform 0.2s cubic-bezier(0.33, 0.66, 0.66, 1)"
		}),
		z(".loader").show();
		var a=this;
		z.ajax({
			type:"POST",
			url:"area.json",
			data:{"id":"123"},
			dataType:"json",
			success:function(e){
				z(".freshtext1").text('加载完成');
				setTimeout(function(){
					a.touchstartY=a.buttom,
					 z(".mainPage").css({
						"-webkit-transform": "translate3d(0px,"+ a.buttom + "px,0px)",
						"-webkit-transition": "-webkit-transform 0.2s cubic-bezier(0.33, 0.66, 0.66, 1)"
					}),
					z(".page-wrap").append('<a class="page-list" href="#">'+
						'<div class="page-box-3">'+
							'<h3>吾悦国际无印良品，新会员享5折优惠</h3>'+
							'<p>即日起至2017年1月10日，来无印良品吾悦国际店办理会员，即可享受热销商品五折优惠。</p>'+
						'</div>'+
						'<div class="page-box-2"><img src="img/list-tmp.jpg"></div>'+
					'</a>'),
					a.buttom=parseInt(window.innerHeight - a.page.offsetHeight),
					z(".freshtext1").text('正在加载'),
					z(".loader").hide();
				},400)
			}
		});			
		}
		else{
			this.ajaxLoader=!0,
			this.touchstartY=this.buttom;
			return this.touchstartY
		}
	},

	window.addEventListener("load",function(){new c()})

}(Zepto);
