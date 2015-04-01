function contentCheck(){
 	var bStreet = false;
	var bCity = false;
	var bState = false;
	if(document.myform.street.value != ""){
		bStreet = true;
	}
	if(document.myform.city.value != ""){
		bCity = true;
	}
	if(document.myform.state.value != ""){
		bState = true;
	}
	var alertMsg = "";
	if(!bStreet || !bCity || !bState){
		alertMsg = "Please enter values for the following fields:\n "
		if(bStreet == false){
			alertMsg += "Street Address\n";
		}
		if(bCity == false){
			alertMsg += "City\n";
		}
		if(bState == false){
			alertMsg += "State\n";
		}
		alert(alertMsg);
		return false;
	}
	else{
		return true;
	}
}	