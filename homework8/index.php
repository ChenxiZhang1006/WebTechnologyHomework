<?php
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST');
function getCurrency($item){
	$currency = $item->attributes()->currency;
 	if($currency){
 		if($currency == "USD"){
 			return "$";
 		}
 		elseif ($currency == "EUR") {
 			return "€";
 		}
 		elseif ($currency == "GBP") {
 			return "£";		
 		}
 	}
 	return "$";
}

 function formatNum($item){
 	$st = (string)$item;
 	return number_format(intval($st), 2); 	
 }

function nullValue(&$ch){
 	if($ch == false){
 		$ch = "N/A";
 	}
}

function obj2Str(&$ch){
	$ch = (string)$ch;
}

function getRstArray($url, $opt){
 	$xmlRspData = file_get_contents($url);
 	if($xmlRspData === false){
 		return false;
 	}

 	$xmlData = simplexml_load_string($xmlRspData);
 	if($xmlData === false){
 		return false;
 	}

 	$rstArray = array();
 	if($opt == "rst"){
 		if($xmlData->response->results){
	 		foreach ($xmlData->response->results as $entry) {
		 		$rstArray[] = $entry->result;
		 	}
	 	}	
 	}
 	elseif ($opt == "chart") {
 		if($xmlData->response->url){
 			$rstUrl = $xmlData->response->url;
 			nullValue($rstUrl);
 			obj2Str($rstUrl);
		 	$rstArray["url"] = $rstUrl;
	 	}
 	}
 	
 	if($rstArray == false){
 		return false;
 	}

 	return $rstArray;
}

function parseDetail($rstArray){
	$detailArray = array();
	foreach ($rstArray as $rstItem) {
		$detailArray["zpid"] = (string)$rstItem->zpid;

		$link = $rstItem->links->homedetails;
		nullValue($link);
		obj2Str($link);
		$detailArray["homedetails"] = $link;

		$street = $rstItem->address->street;
		nullValue($street);
		obj2Str($street);
		$detailArray["street"] = $street;

		$city = $rstItem->address->city;
		nullValue($city);
		obj2Str($city);
		$detailArray["city"] = $city;

		$state = $rstItem->address->state;
		nullValue($state);
		obj2Str($state);
		$detailArray["state"] = $state;

		$zipcode = $rstItem->address->zipcode;
		nullValue($zipcode);
		obj2Str($zipcode);
		$detailArray["zipcode"] = $zipcode;

		$latitude = $rstItem->address->latitude;
		nullValue($latitude);
		obj2Str($latitude);
		$detailArray["latitude"] = $latitude;

		$longitude = $rstItem->address->longitude;
		nullValue($longitude);
		obj2Str($longitude);
		$detailArray["longitude"] = $longitude;

		$useCode = $rstItem->useCode;
		nullValue($useCode);
		obj2Str($useCode);
		$detailArray["useCode"] = $useCode;

		$lastSoldPrice = $rstItem->lastSoldPrice;
		nullValue($lastSoldPrice);
		$lastSoldPrice = $lastSoldPrice == "N/A" ? $lastSoldPrice : getCurrency($lastSoldPrice) . formatNum($lastSoldPrice);
		obj2Str($lastSoldPrice);
		$detailArray["lastSoldPrice"] = $lastSoldPrice;

		$yearBuilt = $rstItem->yearBuilt;
		nullValue($yearBuilt);
		obj2Str($yearBuilt);
		$detailArray["yearBuilt"] = $yearBuilt;

		$lastSoldDate = $rstItem->lastSoldDate;
		nullValue($lastSoldDate);
		$lastSoldDate = $lastSoldDate == "N/A" ? $lastSoldDate : strftime("%Y-%m-%d", strtotime($lastSoldDate));
		$detailArray["lastSoldDate"] = $lastSoldDate;

		$lotSizeSqFt = $rstItem->lotSizeSqFt; 
		nullValue($lotSizeSqFt);
		$lotSizeSqFt = $lotSizeSqFt == "N/A" ? $lotSizeSqFt : $lotSizeSqFt ." sq. ft.";
		$detailArray["lotSizeSqFt"] = $lotSizeSqFt;

		$estimateLastUpdate = $rstItem->zestimate->{"last-updated"};
		nullValue($estimateLastUpdate);
		$estimateLastUpdate = $estimateLastUpdate == "N/A" ? $estimateLastUpdate : strftime("%Y-%m-%d", strtotime($estimateLastUpdate));
		$detailArray["estimateLastUpdate"] = $estimateLastUpdate;

		$estimateAmount = $rstItem->zestimate->amount;
		nullValue($estimateAmount);
		$estimateAmount = $estimateAmount == "N/A" ? $estimateAmount : getCurrency($estimateAmount) . formatNum($estimateAmount);
		$detailArray["estimateAmount"] = $estimateAmount;

		$finishedSqFt = $rstItem->finishedSqFt;
		nullValue($finishedSqFt);
		$finishedSqFt = $finishedSqFt == "N/A" ? $finishedSqFt : $finishedSqFt ." sq. ft.";
		$detailArray["finishedSqFt"] = $finishedSqFt;

		$estimateValueChange = $rstItem->zestimate->valueChange;
		nullValue($estimateValueChange);
		$symbol = getCurrency($estimateValueChange);
		$estimateValueChangeSign = "+";
		if($estimateValueChange < 0){
			$estimateValueChange = -$estimateValueChange;
			$estimateValueChangeSign = "-";
		}
		$detailArray["estimateValueChangeSign"] = $estimateValueChangeSign;
		$detailArray["imgn"] = "http://www-scf.usc.edu/~csci571/2014Spring/hw6/down_r.gif";
		$detailArray["imgp"] = "http://www-scf.usc.edu/~csci571/2014Spring/hw6/up_g.gif";
		$estimateValueChange = $estimateValueChange == "N/A" ? $estimateValueChange : $symbol . formatNum($estimateValueChange);	
		$detailArray["estimateValueChange"] = $estimateValueChange;

		$bathrooms = $rstItem->bathrooms;
		nullValue($bathrooms);
		obj2Str($bathrooms);
		$detailArray["bathrooms"] = $bathrooms;

		$estimateValuationRangeLow = $rstItem->zestimate->valuationRange->low;
		nullValue($estimateValuationRangeLow);
		$estimateValuationRangeLow = $estimateValuationRangeLow == "N/A" ? $estimateValuationRangeLow : getCurrency($estimateValuationRangeLow) . formatNum($estimateValuationRangeLow);
		$detailArray["estimateValuationRangeLow"] = $estimateValuationRangeLow;

		$estimateValuationRangeHigh = $rstItem->zestimate->valuationRange->low;
		nullValue($estimateValuationRangeHigh);
		$estimateValuationRangeHigh = $estimateValuationRangeHigh == "N/A" ? $estimateValuationRangeHigh : getCurrency($estimateValuationRangeHigh) . formatNum($estimateValuationRangeHigh);
		$detailArray["estimateValuationRangeHigh"] = $estimateValuationRangeHigh;

		$bedrooms = $rstItem->bedrooms;
		nullValue($bedrooms);
		obj2Str($bedrooms);
		$detailArray["bedrooms"] = $bedrooms;

		$restimateLastUpdate = $rstItem->rentzestimate->{"last-updated"};
		nullValue($restimateLastUpdate);
		$restimateLastUpdate = $restimateLastUpdate == "N/A" ? $restimateLastUpdate : strftime("%Y-%m-%d", strtotime($restimateLastUpdate));
		$detailArray["restimateLastUpdate"] = $restimateLastUpdate;

		$restimateAmount = $rstItem->rentzestimate->amount;
		nullValue($restimateAmount);
		$restimateAmount = $restimateAmount == "N/A" ? $restimateAmount : getCurrency($restimateAmount) . formatNum($restimateAmount);
		$detailArray["restimateAmount"] = $restimateAmount;

		$taxAssessmentYear = $rstItem->taxAssessmentYear;
		nullValue($taxAssessmentYear);
		obj2Str($taxAssessmentYear);
		$detailArray["taxAssessmentYear"] = $taxAssessmentYear;

		$restimateValueChange = $rstItem->rentzestimate->valueChange;
		nullValue($restimateValueChange);
		$symbol = getCurrency($restimateValueChange);
		$restimateValueChangeSign = "+";
		if($restimateValueChange < 0){
			$restimateValueChange = -$restimateValueChange;
			$restimateValueChangeSign = "-";
		}
		$detailArray["restimateValueChangeSign"] = $restimateValueChangeSign;
		$restimateValueChange = $restimateValueChange == "N/A" ? $restimateValueChange : $symbol . $restimateValueChange;
		$detailArray["restimateValueChange"] = $restimateValueChange;

		$taxAssessment = $rstItem->taxAssessment;
 		nullValue($taxAssessment);
 		$taxAssessment = $taxAssessment == "N/A" ? $taxAssessment : getCurrency($taxAssessment) . formatNum($taxAssessment);
 		$detailArray["taxAssessment"] = $taxAssessment;

 		$restimateValuationRangeLow = $rstItem->rentzestimate->valuationRange->low;
 		nullValue($restimateValuationRangeLow);
 		$restimateValuationRangeLow = $restimateValuationRangeLow == "N/A" ? $restimateValuationRangeLow : getCurrency($restimateValuationRangeLow) . formatNum($restimateValuationRangeLow);
 		$detailArray["restimateValuationRangeLow"] = $restimateValuationRangeLow;

 		$restimateValuationRangeHigh = $rstItem->rentzestimate->valuationRange->high;
 		nullValue($restimateValuationRangeHigh);		
 		$restimateValuationRangeHigh = $restimateValuationRangeHigh == "N/A" ? $restimateValuationRangeHigh : getCurrency($restimateValuationRangeHigh) . formatNum($restimateValuationRangeHigh);
 		$detailArray["restimateValuationRangeHigh"] = $restimateValuationRangeHigh;
	}
	return $detailArray;
}

$street = $_GET["streetInput"];
$city = $_GET["cityInput"];
$state = $_GET["stateInput"];

//get detail

$address = str_replace(" ", "+", $street);
$citystate = str_replace(" ", "+", $city) ."%2C+" .$state;

$urlPrefix = "http://www.zillow.com/webservice/GetDeepSearchResults.htm";
$urlUser = "zws-id=X1-ZWz1b30hjg6mtn_5taq0";
$url = $urlPrefix ."?" .$urlUser ."&address=" .$address ."&citystatezip=" .$citystate ."&rentzestimate=true";

$rstArray = getRstArray($url, "rst");

if($rstArray == false){
	return false;
}

//format own JSON Array
$jsonArray = array();

date_default_timezone_set("America/Los_Angeles");
$detailArray = parseDetail($rstArray);
$jsonArray["result"] = $detailArray;

//get chart
$zpid = $detailArray["zpid"];
$chartArray = array();

$urlPrefix = "http://www.zillow.com/webservice/GetChart.htm";
$urlPrefix = $urlPrefix ."?" .$urlUser . "&unit-type=percent&zpid=" .$zpid . "&width=600&height=300&chartDuration=";
//1year
$url = $urlPrefix . "1year";
$rstArray = getRstArray($url, "chart");
if($rstArray == false){
	return false;
}
$chartArray["year1"] = $rstArray;
//5years
$url = $urlPrefix . "5years";
$rstArray = getRstArray($url, "chart");
if($rstArray == false){
	return false;
}
$chartArray["years5"] = $rstArray;
//10years
$url = $urlPrefix . "10years";
$rstArray = getRstArray($url, "chart");
if($rstArray == false){
	return false;
}
$chartArray["years10"] = $rstArray;

$jsonArray["chart"] = $chartArray;

$json = json_encode($jsonArray); 

echo $json;
?>