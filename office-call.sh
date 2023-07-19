#! /bin/sh

# call for all failures as an HO user
echo "getting all failures...."
curl -H "Authorization: Bearer $(getBlazarToken.sh )"  http://localhost:25000/failures

# get a specif failure
echo
echo "getting failure for 1001..."
curl -H "Authorization: Bearer $(getBlazarToken.sh )"  http://localhost:25000/failures/1001
