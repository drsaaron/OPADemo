const express = require("express");
const http = require('http');

const PORT = 25001;

const app = express();
var httpServer = http.createServer(app);

var DATA = [
    { 'manager': 'joe', 'legalEntityID': 1000, 'entitleableFunction': 'EarningsDemo', "startDate": "2023-01-01", "endDate": null },
    { 'manager': 'joe', 'legalEntityID': 1001, 'entitleableFunction': 'EarningsDemo', "startDate": "2023-01-01", "endDate": null },
    { 'manager': 'henrietta', 'legalEntityID': 1002, 'entitleableFunction': 'EarningsDemo', "startDate": "2023-01-01", "endDate": null },
    { 'manager': 'paula', 'legalEntityID': 1003, 'entitleableFunction': 'EarningsDemo', "startDate": "2023-01-01", "endDate": null },
    { 'manager': 'scott', 'legalEntityID': 1004, 'entitleableFunction': 'EarningsDemo', "startDate": "2023-01-01", "endDate": null },
    { 'manager': 'joe', 'legalEntityID': 1003, 'entitleableFunction': 'PreDistribution', "startDate": "2023-01-01", "endDate": null },

    // give a previous year relationship to demonstrate time dependence
    { 'manager': 'paula', 'legalEntityID': 1001, 'entitleableFunction': 'EarningsDemo', "startDate": "2022-01-01", "endDate": "2022-12-31" },
    { 'manager': 'joe', 'legalEntityID': 1003, 'entitleableFunction': 'EarningsDemo', "startDate": "2022-01-01", "endDate": "2022-12-31" }
];

function filterData(manager, entitleableFunction) {
    return DATA.filter(d => d.manager == manager && d.entitleableFunction === entitleableFunction);
}

app.get('/relationships/:fr', (req, res) => {
    var fr = req.params.fr;
    var entitleableFunction = req.query.entitleableFunction;
    var isOfficeUser = req.query.officeUser;
    
    console.log("got call for FR " + fr);
    var data = [];
    if (isOfficeUser === "true") {
	// ick ick ick.  temporary until the rules can be implemented in the policy
	data = DATA.filter(d => d.entitleableFunction === entitleableFunction);
    } else {
	data = filterData(fr, entitleableFunction);
    }

    res.json(data);
});

app.get('/relationships', (req, res) => {
    
    res.json(DATA);
});

var ENTITLEABLE_FUNCTION_DATA = [
    { "affiliate": "joe", "entitleableFunction": "EarningsDemo" }, 
    { "affiliate": "joe", "entitleableFunction": "PreDistribution" },
    { 'affiliate': 'henrietta', 'entitleableFunction': 'EarningsDemo' },
    { 'affiliate': 'paula', 'entitleableFunction': 'EarningsDemo' },
    { 'affiliate': 'scott', 'entitleableFunction': 'EarningsDemo' }
];

function filterEntitleableFunctionData(affiliate) {
    return ENTITLEABLE_FUNCTION_DATA.filter(d => d.affiliate === affiliate);
}

app.get('/entitleableFunction/:fr', (req, res) => {
    var fr = req.params.fr;
    var data = [];
    console.log("functions for " + fr);

    var data = filterEntitleableFunctionData(fr);

    res.json(data);

});

httpServer.listen(PORT, () => { console.log(`Application server listening on port ${PORT} (http)`)});

	
