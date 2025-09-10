#! /usr/bin/env python3

from fastapi import FastAPI
from fastapi.encoders import jsonable_encoder
from fastapi.responses import JSONResponse
from pydantic import BaseModel
from fastapi.param_functions import Query
import uvicorn
import json

RELATIONSHIP_DATA = [
    { 'manager': 'joe', 'legalEntityID': 1000, 'entitleableFunction': 'EarningsDemo', "startDate": "2023-01-01", "endDate": None },
    { 'manager': 'joe', 'legalEntityID': 1001, 'entitleableFunction': 'EarningsDemo', "startDate": "2023-01-01", "endDate": None },
    { 'manager': 'henrietta', 'legalEntityID': 1002, 'entitleableFunction': 'EarningsDemo', "startDate": "2023-01-01", "endDate": None },
    { 'manager': 'paula', 'legalEntityID': 1003, 'entitleableFunction': 'EarningsDemo', "startDate": "2023-01-01", "endDate": None },
    { 'manager': 'scott', 'legalEntityID': 1004, 'entitleableFunction': 'EarningsDemo', "startDate": "2023-01-01", "endDate": None },
    { 'manager': 'joe', 'legalEntityID': 1003, 'entitleableFunction': 'PreDistribution', "startDate": "2023-01-01", "endDate": None },

    # give a previous year relationship to demonstrate time dependence
    { 'manager': 'paula', 'legalEntityID': 1001, 'entitleableFunction': 'EarningsDemo', "startDate": "2022-01-01", "endDate": "2022-12-31" },
    { 'manager': 'joe', 'legalEntityID': 1003, 'entitleableFunction': 'EarningsDemo', "startDate": "2022-01-01", "endDate": "2022-12-31" }
]

class Relationship(BaseModel):
    manager: str
    legalEntityId: int
    entitleableFunction: str
    startDate: str
    endDate: str = None

api = FastAPI(description = "python version of the relationship server", version = "0.0.1")

@api.get("/relationships", description = "get all values")
async def getAllRelationships() -> list[Relationship]:
    return JSONResponse(jsonable_encoder(RELATIONSHIP_DATA))

@api.get("/relationships/{fr}", description = "get values for one FR")
async def getFRRelationships(fr: str, entitleableFunction: str = Query(), isOfficeUser = Query(False)) -> list[Relationship]:
    print("getting FR relationships for " + fr)
    filteredData = []
    if (isOfficeUser):
        filteredData = list(filter(lambda r: r['entitleableFunction'] == entitleableFunction, RELATIONSHIP_DATA))
    else:
        filteredData = list(filter(lambda r: r['manager'] == fr and r['entitleableFunction'] == entitleableFunction, RELATIONSHIP_DATA))
    return JSONResponse(content = jsonable_encoder(filteredData))

ENTITLEABLE_FUNCTION_DATA = [
    { "affiliate": "joe", "entitleableFunction": "EarningsDemo" }, 
    { "affiliate": "joe", "entitleableFunction": "PreDistribution" },
    { 'affiliate': 'henrietta', 'entitleableFunction': 'EarningsDemo' },
    { 'affiliate': 'paula', 'entitleableFunction': 'EarningsDemo' },
    { 'affiliate': 'scott', 'entitleableFunction': 'EarningsDemo' }
]

@api.get('/entitleableFunction/{fr}')
async def getEntitleableFunctions(fr: str):
    data = list(filter(lambda e: e['affiliate'] == fr, ENTITLEABLE_FUNCTION_DATA))
    return  JSONResponse(content = data)

@api.get('/health')
async def healthCheck():
    return JSONResponse(content = { "status": "OK" })

# start 'er up the python way
if __name__ == "__main__":
    uvicorn.run("__main__:api", reload = True, port=25001, host="0.0.0.0")
    
