$().ready(function(){
	$("#submit").click(function(){
		$("#detailform").validate({
			rules:{
				address:{
					required:true
				},
				city:{
					required:true
				},
				state:{
					required:true
				}
			},
			errorPlacement:function(error, element){
				error.insertAfter(element.parent());
			},
			highlight:function(element){
				$(element).parent().addClass("has-error");
			},
			unhighlight:function(element){
				$(element).parent().removeClass("has-error");
			},
			submitHandler:function(form){
				$.ajax({
					url:"http://default-environment-akusfbpazd.elasticbeanstalk.com",
					data:{
						streetInput: $("#address").val(),
						cityInput: $("#city").val(),
						stateInput: $("#state").val()
					},
					type: "GET",
					crossDomain:true,
					success: function(output){
						//disable button, can't do next search
						$("#submit").attr("disabled","disabled");

						if(output == false){
							$("#rst").html("No exact match found -- Verify that the given address is correct");
							$("#rst").css({"color":"red","font-size":"x-large", "text-align":"center"});
						}
						else{
							window.fbAsyncInit = function() {
								FB.init({
									appId      : '572746302826251',
									xfbml      : true,
									version    : 'v2.1'
								});
							};
							(function(d, s, id) {
								var js, fjs = d.getElementsByTagName(s)[0];
								if (d.getElementById(id)) return;
								js = d.createElement(s); js.id = id;
								js.src = "//connect.facebook.net/en_US/sdk.js#xfbml=1&appId=572746302826251&version=v2.0";
								fjs.parentNode.insertBefore(js, fjs);
							}(document, 'script', 'facebook-jssdk'));
							//Nav tabs
							var tabul = $("<ul class=\"nav nav-tabs\" role = \"tablist\"></ul>");
							var tabliinfo = $("<li role=\"presentation\" class = \"active\"></li>");
							var tabliinfohref = $("<a href=\"#Info\" role=\"tab\" data-toggle=\"tab\"></a>").text("Basic Info");
							tabliinfo.append(tabliinfohref);

							var tablih = $("<li role=\"presentation\"></li>");
							var tabliHhref = $("<a href=\"#History\" role=\"tab\" data-toggle=\"tab\"></a>").text("Historical Zestimates");
							tablih.append(tabliHhref);

							tabul.append(tabliinfo, tablih);
							//tab panes
							var panesDiv = $("<div class=\"tab-content\"></div>");
							var infoDiv = $("<div role=\"tabpanel\" class=\"tab-pane active tabDiv\" id=\"Info\"></div>");
							var historyDiv = $("<div role=\"tabpanel\" class=\"tab-pane tabDiv\" id=\"History\"></div>");
							panesDiv.append(infoDiv, historyDiv);

							//footer
							var term = "<p style=\"text-align:center\">&copy Zillow, Inc., 2006-2014. Use is subject to <a href=\"http://www.zillow.com/corp/Terms.htm\" class=\"noUnderScorce\"  target=\"_blank\">Terms of Use</a></p>";
							var what = "<p style=\"text-align:center\"><a href=\"http://www.zillow.com/zestimate/\" class=\"noUnderScorce\"  target=\"_blank\">What's a Zestimate?</a></p>"

							$("#rst").append(tabul, panesDiv, term, what);

							//parse data here
							var obj = JSON.parse(output);

							var table = $("<table></table>").addClass("table");
							var downArrow = obj.result.imgn;
							var upArrow = obj.result.imgp;
							//first row
							var tr1 = $("<tr></tr>").addClass("bgGray");;
							var link = obj.result.homedetails;
							var address = obj.result.street + ", " + obj.result.city + ", " + obj.result.state + "-" + obj.result.zipcode;
							var tr1html = "<td class = \"left\">See more details for <a href = \"" + link + "\" class=\"noUnderScorce\" target=\"_blank\">" + address +"</a> on Zillow</td><td></td><td></td>";
							tr1html =tr1html + "<td class = \"right\"><div id =\"sharebtn\">share on <b>facebook</b></div></td>";
							tr1.html(tr1html);

							//second row
							var tr2 = $("<tr></tr>").addClass("bgWhite");
							var tr2html = "<td class = \"left\">Property Type:</td><td class = \"right\">" + obj.result.useCode + "</td>";
							tr2html = tr2html + "<td class = \"left\">Last Sold Price:</td><td class = \"right\">" + obj.result.lastSoldPrice + "</td>";
							tr2.html(tr2html);

							//third row
							var tr3 = $("<tr></tr>").addClass("bgGray");
							var tr3html = "<td class = \"left\">Year Built:</td><td class = \"right\">" + obj.result.yearBuilt + "</td>";
							tr3html = tr3html + "<td class = \"left\">Last Sold Data:</td><td class = \"right\">" + obj.result.lastSoldDate + "</td>";
							tr3.html(tr3html);

							//fourth row
							var tr4 = $("<tr></tr>").addClass("bgWhite");
							var tr4html = "<td class = \"left\">Lot Size:</td><td class = \"right\">" + obj.result.lotSizeSqFt + "</td>";
							tr4html = tr4html + "<td class = \"left\">Zestimate &reg Property Estimate as of " + obj.result.estimateLastUpdate +"</td><td class = \"right\">" + obj.result.estimateAmount + "</td>";
							tr4.html(tr4html);

							//fifth row
							var tr5 = $("<tr></tr>").addClass("bgGray");
							var tr5html = "<td class = \"left\">Finished Area:</td><td class = \"right\">" + obj.result.finishedSqFt + "</td>";
							tr5html = tr5html + "<td class = \"left\">30 Days Overall Change:</td><td class = \"right\">";
							if(obj.result.estimateValueChangeSign == "+"){
								tr5html = tr5html + "<img src=\""+ upArrow + "\"></img>";
							}
							else{
								tr5html = tr5html + "<img src=\""+ downArrow + "\"></img>";
							}
							tr5html = tr5html + obj.result.estimateValueChange + "</td>";
							tr5.html(tr5html);

							//sixth row
							var tr6 = $("<tr></tr>").addClass("bgWhite");
							var tr6html = "<td class = \"left\">Bathrooms:</td><td class = \"right\">" + obj.result.bathrooms + "</td>";
							tr6html = tr6html + "<td class = \"left\">All Time Property Range:</td><td class = \"right\">" + obj.result.estimateValuationRangeLow + " - " + obj.result.estimateValuationRangeHigh + "</td>";
							tr6.html(tr6html);

							//7th row
							var tr7 = $("<tr></tr>").addClass("bgGray");
							var tr7html = "<td class = \"left\">Bedrooms:</td><td class = \"right\">" + obj.result.bedrooms + "</td>";
							tr7html = tr7html + "<td class = \"left\">Rent Zestimate &reg Valuation as of " + obj.result.restimateLastUpdate +"</td><td class = \"right\">" + obj.result.restimateAmount + "</td>";
							tr7.html(tr7html);

							//8th row
							var tr8 = $("<tr></tr>").addClass("bgWhite");
							var tr8html = "<td class = \"left\">Tax Assessment Year:</td><td class = \"right\">" + obj.result.taxAssessmentYear + "</td>";
							tr8html = tr8html + "<td class = \"left\">30 Days Rent Change:</td><td class = \"right\">";
							if(obj.result.restimateValueChangeSign == "+"){
								tr8html = tr8html + "<img src=\""+ upArrow + "\"></img>";
							}
							else{
								tr8html = tr8html + "<img src=\""+ downArrow + "\"></img>";
							}
							tr8html = tr8html + obj.result.restimateValueChange + "</td>";
							tr8.html(tr8html);

							//9th row
							var tr9 = $("<tr></tr>").addClass("bgGray");
							var tr9html = "<td class = \"left\">Tax Assessment:</td><td class = \"right\">" + obj.result.taxAssessment + "</td>";
							tr9html = tr9html + "<td class = \"left\">All Time Rent Range:</td><td class = \"right\">" + obj.result.restimateValuationRangeLow + " - " + obj.result.restimateValuationRangeHigh + "</td>";
							tr9.html(tr9html);

							table.append(tr1, tr2, tr3, tr4, tr5, tr6, tr7, tr8, tr9);

							$("#Info").append(table);

							//for charts
							var carouselDiv = $("<div id=\"carousel\" class=\"carousel slide\" data-ride=\"carousel\"></div>");
							//indicator
							var indicator = "<ol class=\"carousel-indicators\">"+
							"<li data-target=\"#carousel\" data-slide-to=\"0\" class=\"active\"></li>"+
							"<li data-target=\"#carousel\" data-slide-to=\"1\"></li>"+
							"<li data-target=\"#carousel\" data-slide-to=\"2\"></li>"+
							"</ol>";
							//inner
							var innerDiv = $("<div role=\"listbox\"></div>").addClass("carousel-inner");

							var inner1 = $("<div align=\"center\"></div>").addClass("item active");
							var inner1html = "<img src=\"" + obj.chart.year1.url + "\"></img>";
							inner1html = inner1html + "<div class=\"carousel-caption captionDiv\">Historical Zestimates for the past 1 year<br><small>" + address +"</small></div>"
							inner1.html(inner1html);

							var inner2 = $("<div align=\"center\"></div>").addClass("item");
							var inner2html = "<img src=\"" + obj.chart.years5.url + "\"></img>";
							inner2html = inner2html + "<div class=\"carousel-caption captionDiv\">Historical Zestimates for the past 5 years<br><small>" + address +"</small></div>"
							inner2.html(inner2html);

							var inner3 = $("<div align=\"center\"></div>").addClass("item");
							var inner3html = "<img src=\"" + obj.chart.years10.url + "\"></img>";
							inner3html = inner3html + "<div class=\"carousel-caption captionDiv\">Historical Zestimates for the past 10 years<br><small>" + address +"</small></div>"
							inner3.html(inner3html);

							innerDiv.append(inner1, inner2, inner3);
							//button
							var control = "<a class=\"left carousel-control\" href=\"#carousel\" role=\"button\" data-slide=\"prev\">"+
							"<span class=\"glyphicon glyphicon-chevron-left\"></span>"+
							"<span class=\"sr-only\">Previous</span>"+
							"</a>"+
							"<a class=\"right carousel-control\" href=\"#carousel\" role=\"button\" data-slide=\"next\">"+
							"<span class=\"glyphicon glyphicon-chevron-right\"></span>"+
							"<span class=\"sr-only\">Next</span>"+
							"</a>";
							carouselDiv.append(indicator, innerDiv, control);
							$("#History").append(carouselDiv);
							$("#sharebtn").click(function(){
								var des = "Last Sold Price:" + obj.result.lastSoldPrice +", 30 Days Overall Change:" + obj.result.estimateValueChangeSign + obj.result.estimateValueChange;
								var param = {
									method:"feed",
									name: address,
									caption:"Property information from Zillow.com",
									description: des,
									link: link,
									picture: obj.chart.year1.url
								};
								FB.ui(param, function(response){
									if (response && !response.error_code) {
										alert('Posting completed.');
									} 
									else {
										alert('Error while posting.');
									}
								});
								
								
							});
						}
					},
					error:function(){

					}
				});
			}
		});
	});
});