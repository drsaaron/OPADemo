#! /bin/sh

token=$(getBlazarToken.sh)
if [ -z "$token" ]
then
    echo "unable to get blazar token!  Did you start the service?" 1>&2
    exit 1
fi

# call for all failures as an HO user
echo "getting all failures...."
curl -H "Authorization: Bearer $token"  http://localhost:25000/failures 2>/dev/null | jq

# get a specific failure
echo
echo "getting failure for 1001..."
curl -H "Authorization: Bearer $token"  http://localhost:25000/failures/1001 2>/dev/null | jq

echo
