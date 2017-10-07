#!/bin/sh


APPLICATION_ID=X7HiVehVO7Zg9ufo0qCDXVPI3z0bFpUXtyq2ezYL
REST_API_KEY=LCTpX53aBmbtIXOtFmDb9dklESKUd0q58hFbnRYc

curl -X GET \
  -H "X-Parse-Application-Id: ${APPLICATION_ID}" \
  -H "X-Parse-REST-API-Key: ${REST_API_KEY}" \
  -H "X-Parse-Revocable-Session: 1" \
  -G \
  --data-urlencode 'username=test' \
  --data-urlencode 'password=test' \
  https://parseapi.back4app.com/login

