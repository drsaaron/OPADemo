package demo.earnings

default allow := false
default filter_list := [ { "legalEntityID": -1, "manager": "application" } ]

# request is allowed if the requestor is
#   a. an application rather than a user OR
#   b. has a relationship to the FR OR
#   c. home office
allow {
      caller_is_application
}{
      caller_is_homeoffice
}{
      caller_has_entitleableFunction
      fr == -1
}{
  # I am not sure why I need this 4th check instead of combining 3 and 4 via any([fr==-1, caller_has_relationship])
  # I swear it worked at one time, but now doesn't
      caller_has_entitleableFunction
      caller_has_relationship
}

# which records does this caller have access to?
# NB this only works for office and field callers, but only because the relationships service accepts
# the officeUser flag to control whether it returns all data or just some data.  This policy should
# have the logic to control that directly, using the allRelationships call or the user relationships
# call.  But getting a bit better.
filter_list = relationshipList

# data from request
caller := decodedJwtToken[1].sub
fr := input.fr
jwtToken := input.encodedJwt
decodedJwtToken := io.jwt.decode(jwtToken)
roles := decodedJwtToken[1].roles
method := input.method

# is the caller an application.  For now just check that the JWT token is null since the
# real world would not yet have secured user identification.  eventually I would assume there
# would be a JWT token for the caller that would include claims or something that would allow
# differentiation of a generic ID from a user ID, though we probably wouldn't need to if the
# generic ID is in the expected group like a home office user would be.
caller_is_application {
    jwtToken == null
}

# is the caller home office? if yes, the user will have the group ROLE_EARNINGS_DEMO_USER in the roles
caller_is_homeoffice {
    roles[_] == "ROLE_EARNINGS_DEMO_USER"
}

default officeUser := false
officeUser {
   caller_is_homeoffice
} {
   caller_is_application
}

# does the caller have a relationship to the fr?
relationshipList = relationshipsCall.body
caller_has_relationship {
    
    some i

    relationshipList[i].legalEntityID == fr
    relationshipList[i].manager == caller
}

# does the caller have an entitleable relationship to EarningsDemo function
entitleableFunctionList = entitleableFunctionCall.body
caller_has_entitleableFunction {

    some i
    entitleableFunctionList[i].entitleableFunction == "EarningsDemo"
}

relationshipUrl = sprintf("http://relationshipServer:25001/relationships/%v?officeUser=%v&entitleableFunction=EarningsDemo", [caller, officeUser])
relationshipsCall = http.send(
    {
      "method": "get",
      "url": relationshipUrl
    }
)

entitleableFunctionUrl = sprintf("http://relationshipServer:25001/entitleableFunction/%v", [caller])
entitleableFunctionCall = http.send(
    {
      "method": "get",
      "url": entitleableFunctionUrl
    }
)
