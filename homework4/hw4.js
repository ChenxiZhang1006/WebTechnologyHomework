function loadXML(url){
	if (window.XMLHttpRequest)
	{// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	}
	else
	{// code for IE6, IE5
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.open("GET",url,false);
	xmlhttp.send(null);
	xmlDoc=xmlhttp.responseXML;
	return xmlDoc;
}

function generateHTML(xmlDoc){
	ELEMENT_NODE = 1;    // MS parser doesn't define Node.ELEMENT_NODE
	root = xmlDoc.DocumentElement;
	html_text = "<!DOCTYPE html><html><head>";
	html_text += "<meta content='text/html;charset=utf-8' http-equiv='Content-Type'>";
	html_text += "<meta content='utf-8' http-equiv='encoding'>";
	html_text += "<title>Parse Result</title><style type='text/css'>";
	html_text += "body{font-family: 'Times New Roman', Times, serif;width: auto;height: auto;text-align: left;}";
	html_text += "tr.title{font-weight: bold;text-align: center;}";				
	html_text += "</style></head>";
	html_text += "<body><h2>Real Estate Listings</h2>";
	html_text += "<table border='2'>";
	listings = xmlDoc.getElementsByTagName("Listing");
	listingNum = listings.length;
	for(i = 0; i < listingNum; i ++){
		listingNode = listings.item(i).childNodes;
		html_text += "<tr class='title'><td colspan=7>Listing</td></tr>";
		for(j = 0; j < listingNode.length; j++){
			if(listingNode.item(j).nodeName == "Location"){
				locations = listingNode.item(j).childNodes;
				html_text += "<tr class='title'>"+
						"<td>Location</td>"+
						"<td>Street</td>"+
						"<td>City</td>"+
						"<td>State</td>"+
						"<td>Zip</td>"+
						"<td>Lat</td>"+
						"<td>Long</td>"+
					"</tr>"+
					"<tr>"+
						"<td></td>";
				for(l = 0; l < locations.length; l ++){
					if(locations.item(l).nodeType == ELEMENT_NODE){
						html_text += "<td>" + locations.item(l).firstChild.nodeValue +"</td>";
					}					
				}
				html_text += "</tr>";
			}
			else if(listingNode.item(j).nodeName == "ListingDetails"){
				details = listingNode.item(j).childNodes;
				html_text += "<tr class='title'>"+
						"<td>Details</td>"+
						"<td>Status</td>"+
						"<td>Price</td>"+
						"<td>Listing URL</td>"+
						"<td colspan=3>Image</t>"+
					"</tr>"+
					"<tr>"+
						"<td></td>";
				for(d = 0; d < details.length; d ++){
					if(details.item(d).nodeType == ELEMENT_NODE){
						detail = details.item(d);
						if(detail.nodeName == "Status" || detail.nodeName == "Price"){
							html_text += "<td>" + detail.firstChild.nodeValue +"</td>";
						}
						else if(detail.nodeName == "ListingUrl"){
							html_text += "<td><a href='" + detail.firstChild.nodeValue +"'>Link to Listing</a></td>";
						}
						else if(detail.nodeName == "Image"){
							html_text += "<td colspan=3><img src='" + detail.firstChild.nodeValue +"' alt='Picture'></td>";
						}	
					}
					

				}

				html_text += "</tr>";
			}
		}		
	}
	html_text += "</table>" + "</body></html>";
}

function parseXML(form){
	var inputVaule = form.XMLURL.value;
	if(inputVaule == ''){
		alert("Please enter the url!");
		return false;
	}
	else{		
		xmlDoc = loadXML(inputVaule);
		if(window.ActiveXObject){
			if(xmlDoc.parseError.errorCode != 0){
				var myErr = xmlDoc.parseError;
				generateError(xmlDoc);
				hWin = window.open("", "Error", "height=300,width=350");
				hWin.document.write(html_text);
			}
			else{
				generateHTML(xmlDoc);
				hWin = window.open("","Result","height=auto,width=auto,scrollbars=yes");
				hWin.document.write(html_text);
			}
		}
		else{
			try{
				xmlDoc.onload = generateHTML(xmlDoc);
				hWin = window.open("","Result","height=auto,width=auto,scrollbars=yes");
				hWin.document.write(html_text);
			}catch(e){
				alert(e.name + ":" + e.message);
			}
			
		}
		hWin.document.close();
	}
}