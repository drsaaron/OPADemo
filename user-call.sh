#! /bin/sh

# call for all failures as an office user.  This user has access to 2 failures in 2023 and 1 failure
# in 2022.  So this call should return 3 failures.
echo "calling for all failures...."
curl -H "Authorization: Bearer $(getBlazarToken.sh -u joe)"  http://localhost:25000/failures

# get a specific failure as an office user
echo
echo "calling for failure on 1001..."
curl -H "Authorization: Bearer $(getBlazarToken.sh -u joe)"  http://localhost:25000/failures/1001


