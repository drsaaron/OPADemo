#! /bin/sh

token=$(getBlazarToken.sh -u joe)

if [ -z "$token" ]
then
    echo "unable to get token! Did you start the service?" 1>&2
    exit 1
fi

# call for all failures as an office user.  This user has access to 2 failures in 2023 and 1 failure
# in 2022.  So this call should return 3 failures.
echo "calling for all failures...."
curl -H "Authorization: Bearer $token"  http://localhost:25000/failures

# get a specific failure as an office user
echo
echo "calling for failure on 1001..."
curl -H "Authorization: Bearer $token"  http://localhost:25000/failures/1001

# make an unauthorized call
echo
echo "call for unauthorized..."
curl -v -H "Authorization: Bearer $token" http://localhost:25000/failures/1002 2>&1 | grep HTTP

echo



